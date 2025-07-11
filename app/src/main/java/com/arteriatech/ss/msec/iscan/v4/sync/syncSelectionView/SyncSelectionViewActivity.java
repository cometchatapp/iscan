package com.arteriatech.ss.msec.iscan.v4.sync.syncSelectionView;

import static com.arteriatech.ss.msec.iscan.v4.common.Constants.isAllSync;

import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.server_Text;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.server_Text_DR;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.server_Text_default;
import static com.arteriatech.ss.msec.iscan.v4.registration.RegistrationActivity.registrationModel;
import static com.arteriatech.ss.msec.iscan.v4.sync.SyncSelectionActivity.isLocalSync;
import static com.arteriatech.ss.msec.iscan.v4.sync.SyncUtils.isRetailerOrBeatSync;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.AvailableServer;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.asyncTask.RefreshAsyncTask;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.log.ErrorCode;
import com.arteriatech.ss.msec.iscan.v4.mbo.ErrorBean;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.Operation;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack;
import com.arteriatech.ss.msec.iscan.v4.registration.Configuration;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.sync.OutletSQLHelper;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncInfo.RefreshListInterface;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncUtils;
import com.sap.client.odata.v4.core.GUID;
import com.sap.smp.client.odata.exception.ODataException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class SyncSelectionViewActivity extends AppCompatActivity implements SyncSelectionViewInterface, UIListener, View.OnClickListener {

    private boolean dialogCancelled = false;
    private ArrayList<String> alAssignColl = new ArrayList<>();
    private ProgressDialog syncProgDialog = null;
    TextView toolbar_text;
    private GUID refguid;
    boolean isFresh,isRoute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_selection_view);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
//        MSFAApplication.setAnalytics(this,MSFAApplication.SP_UID,this.getClass().getSimpleName(),"Download sync");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        ConstantsUtils.initActionBarView(this, toolbar, true, getString(R.string.sync_menu), 0);
        toolbar_text = (TextView) findViewById(R.id.toolbar_text);
        toolbar_text.setText("Download Items");
        toolbar_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Fragment fragment = new SyncSelectionViewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.EXTRA_BEAN_LIST, SyncUtils.getSyncSelectionView(SyncSelectionViewActivity.this));
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //MSFAApplication.setAnalytics(this,MSFAApplication.SP_UID,this.getClass().getSimpleName(),"Download sync");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private AsyncSyncData asyncSyncData = null;
    @Override
    public void onSelectedCollection(ArrayList<String> selectedList) {
        alAssignColl.clear();
//        if (Constants.isSync) {
//            ConstantsUtils.displayLongToast(SyncSelectionViewActivity.this, getString(R.string.alert_auto_sync_is_progress));
//        } else {
            if (UtilConstants.isNetworkAvailable(SyncSelectionViewActivity.this)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AvailableServerAllSync(selectedList);
                    }
                }).start();

            } else {
                SyncSelectionViewActivity.this.runOnUiThread(() -> Constants.showAlert(ErrorCode.NETWORK_ERROR, SyncSelectionViewActivity.this));
            }
//        }
    }
    
    
    private void syncDownload(ArrayList<String> selectedList){
        try {
            Constants.isSync = true;
            dialogCancelled = false;
            alAssignColl.addAll(selectedList);
            asyncSyncData = new AsyncSyncData();
            asyncSyncData.execute();
                    /*ConstantsUtils.checkNetwork(this, isFailed -> {
                        if (isFailed) {
                            runOnUiThread(() -> {
                                asyncSyncData.cancel(true);
                                if (!asyncSyncData.isCancelled()){
                                    while (asyncSyncData.isCancelled()){
                                        asyncSyncData.cancel(true);
                                    }
                                }
                                closingProgressDialog();
                                SyncSelectionViewActivity.this.runOnUiThread(() -> Constants.showAlert(ErrorCode.NETWORK_ERROR, SyncSelectionViewActivity.this));

                            });
                        }
                    },false);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
//        allSyncPresenter.onDestroy();
        super.onDestroy();

    }

    private void AvailableServerAllSync(ArrayList<String> selectedList){
        if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    syncProgDialog = new ProgressDialog(SyncSelectionViewActivity.this, R.style.ProgressDialogTheme);
                    syncProgDialog.setTitle("Verifying server. Please wait...");
                    setTimer();
                    messageProgess = "Verifying server. Please wait...";
                    syncProgDialog.setMessage(messageProgess);
                    syncProgDialog.setCancelable(false);
                    syncProgDialog.setCanceledOnTouchOutside(false);
                    syncProgDialog.show();
                }
            });

            String hostName = getSharedPreferences(Constants.PREFS_NAME, 0).getString(Constants.ActiveHost, "");
            try {
                AvailableServer.clearCookies();
                if (AvailableServer.pingServer(server_Text)) {
                    JSONObject resourceFileReadresponse = AvailableServer.getResourceFile(server_Text,this);
                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.ActiveHost, server_Text);
                    editor.apply();
                    server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                    if (resourceFileReadresponse != null) {
                        JSONObject serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                        boolean status = serverResponse.getBoolean(Constants.Status);
                        int responseCode = serverResponse.getInt(Constants.ResponseCode);
                        String messageError = serverResponse.getString(Constants.Message);
                        if (responseCode == 200) {
                            if (status) {
                                syncDownload(selectedList);
                            } else {
                                closingProgressDialog();
                                LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                showAlert(messageError);
                            }
                        } else if (responseCode == 503 || responseCode == 404 || responseCode == 998 || responseCode == 999 || responseCode == 996) {
                            // 503 - service unavailable , 404 - Resource file not maintained , 998 - Backend unavailable , 999 - Resource file is empty
                            //996 - Resource file available but backend property missing
                            if (!server_Text.equalsIgnoreCase(server_Text_default)) {
//                                AvailableServer.clearCookies();
                                if (AvailableServer.pingServer(server_Text_default)) {
//                                    editor.putString(Constants.ActiveHost, server_Text_default);
//                                    editor.apply();
//                                    server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                                    resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_default,this);
                                    if (resourceFileReadresponse != null) {
                                        serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                                        status = serverResponse.getBoolean(Constants.Status);
                                        responseCode = serverResponse.getInt(Constants.ResponseCode);
                                        messageError = serverResponse.getString(Constants.Message);
                                        if (responseCode == 200) {
                                            if (status) {
                                                syncDownload(selectedList);
                                            } else {
                                                closingProgressDialog();
                                                LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                                LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                                showAlert(messageError);
                                            }
                                        } else if (responseCode == 401) {
                                            LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                            LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                            try {
                                                sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                                String loginUser = sharedPreferences.getString("username", "");
                                                String login_pwd = sharedPreferences.getString("password", "");
                                                Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                                                    @Override
                                                    public void passwordStatus(final JSONObject jsonObject) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                closingProgressDialog();
                                                                Constants.passwordStatusErrorMessage(SyncSelectionViewActivity.this, jsonObject, registrationModel, loginUser);
                                                            }
                                                        });

                                                    }
                                                });
                                            } catch (Throwable throwable) {
                                                throwable.printStackTrace();
                                            }
                                        } else {
                                            closingProgressDialog();
                                            LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                            LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                            showAlert(messageError);
                                        }
                                    }
                                } else {
                                    closingProgressDialog();
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    showAlert(messageError);
                                }
                            } else if (!server_Text.equalsIgnoreCase(server_Text_DR)) {
//                                AvailableServer.clearCookies();
                                if (AvailableServer.pingServer(server_Text_DR)) {
//                                    editor.putString(Constants.ActiveHost, server_Text_DR);
//                                    editor.apply();
//                                    server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                                    resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_DR,this);
                                    if (resourceFileReadresponse != null) {
                                        serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                                        status = serverResponse.getBoolean(Constants.Status);
                                        responseCode = serverResponse.getInt(Constants.ResponseCode);
                                        messageError = serverResponse.getString(Constants.Message);
                                        if (responseCode == 200) {
                                            if (status) {
                                                syncDownload(selectedList);
                                            } else {
                                                closingProgressDialog();
                                                LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                                LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                                showAlert(messageError);
                                            }
                                        } else if (responseCode == 401) {
                                            LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                            LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                            try {
                                                sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                                String loginUser = sharedPreferences.getString("username", "");
                                                String login_pwd = sharedPreferences.getString("password", "");
                                                Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                                                    @Override
                                                    public void passwordStatus(final JSONObject jsonObject) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                closingProgressDialog();
                                                                Constants.passwordStatusErrorMessage(SyncSelectionViewActivity.this, jsonObject, registrationModel, loginUser);
                                                            }
                                                        });

                                                    }
                                                });
                                            } catch (Throwable throwable) {
                                                throwable.printStackTrace();
                                            }
                                        } else {
                                            closingProgressDialog();
                                            LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                            LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                            showAlert(messageError);
                                        }
                                    }
                                } else {
                                    closingProgressDialog();
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    showAlert(messageError);
                                }
                            }
                        } else if (responseCode == 401) {
                            LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                            LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                            try {
                                sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                String loginUser = sharedPreferences.getString("username", "");
                                String login_pwd = sharedPreferences.getString("password", "");
                                Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                                    @Override
                                    public void passwordStatus(final JSONObject jsonObject) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                closingProgressDialog();
                                                Constants.passwordStatusErrorMessage(SyncSelectionViewActivity.this, jsonObject, registrationModel, loginUser);
                                            }
                                        });

                                    }
                                });
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        } else {
                            closingProgressDialog();
                            LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                            LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                            showAlert(messageError);
                        }
                    }
                } else {
                    //xumu != xumu
                    if (!server_Text.equalsIgnoreCase(server_Text_default)) {
                        AvailableServer.clearCookies();
                        if (AvailableServer.pingServer(server_Text_default)) {
                            SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.ActiveHost, server_Text_default);
                            editor.apply();
                            server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                            JSONObject resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_default,this);
                            if (resourceFileReadresponse != null) {
                                JSONObject serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                                boolean status = serverResponse.getBoolean(Constants.Status);
                                int responseCode = serverResponse.getInt(Constants.ResponseCode);
                                String messageError = serverResponse.getString(Constants.Message);
                                if (responseCode == 200) {
                                    if (status) {
                                        try {
                                            if (syncProgDialog!=null) {
                                                messageProgess = "Registering user. Please wait...";
                                                syncProgDialog.setMessage("Registering user. Please wait...");
                                            }
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                        AvailableServer.registerWithAvailableServer(this, new AvailableServer.RegisterServer() {
                                            @Override
                                            public void requestSuccess() {runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        messageProgess = getString(R.string.msg_sync_progress_msg_plz_wait);
                                                        syncProgDialog.setMessage(messageProgess);
                                                    } catch (Throwable e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                                AvailableServer.openStoreWithAvailableServer(SyncSelectionViewActivity.this, new UIListener() {
                                                    @Override
                                                    public void onRequestError(int operation, Exception e) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                onError(operation, e);
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                onSuccess(operation,key,selectedList);
                                                            }
                                                        });
                                                    }
                                                });
                                            }

                                            @Override
                                            public void requestError(String errorMessage) {
                                                closingProgressDialog();
                                                showAlert(errorMessage);
                                            }
                                        });
                                    } else {
                                        closingProgressDialog();
                                        LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                        LogManager.writeLogInfo("getResouce file check request failed : " + messageError);LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                        LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                        showAlert(messageError);
                                    }
                                } else if (responseCode == 503 || responseCode == 404 || responseCode == 998 || responseCode == 999 || responseCode == 996) {
                                    // 503 - service unavailable , 404 - Resource file not maintained , 998 - Backend unavailable , 999 - Resource file is empty
                                    //996 - Resource file available but backend property missing
                                    closingProgressDialog();
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    showAlert(messageError);
                                } else if (responseCode == 401) {
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    try {
                                        sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                        String loginUser = sharedPreferences.getString("username", "");
                                        String login_pwd = sharedPreferences.getString("password", "");
                                        Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                                            @Override
                                            public void passwordStatus(final JSONObject jsonObject) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        closingProgressDialog();
                                                        Constants.passwordStatusErrorMessage(SyncSelectionViewActivity.this, jsonObject, registrationModel, loginUser);
                                                    }
                                                });

                                            }
                                        });
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                } else {
                                    closingProgressDialog();
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    showAlert(messageError);
                                }
                            }
                        } else {
                            closingProgressDialog();
                            LogManager.writeLogError("Sync can't be performed due to server unavailability. Please try later");
                            showAlert("Sync can't be performed due to server unavailability. Please try later");
                        }
                    }
                    //Xumu != Ap1
                    else if (!server_Text.equalsIgnoreCase(server_Text_DR)) {
                        AvailableServer.clearCookies();
                        server_Text = server_Text_DR;
                        if (AvailableServer.pingServer(server_Text_DR)) {
                            SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.ActiveHost, server_Text_DR);
                            editor.apply();
                            server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                            JSONObject resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_DR,this);
                            if (resourceFileReadresponse != null) {
                                JSONObject serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                                boolean status = serverResponse.getBoolean(Constants.Status);
                                int responseCode = serverResponse.getInt(Constants.ResponseCode);
                                String messageError = serverResponse.getString(Constants.Message);
                                if (responseCode == 200) {
                                    if (status) {
                                        try {
                                            if (syncProgDialog!=null) {
                                                messageProgess = "Registering user. Please wait...";
                                                syncProgDialog.setMessage("Registering user. Please wait...");
                                            }
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                        AvailableServer.registerWithAvailableServer(this, new AvailableServer.RegisterServer() {
                                            @Override
                                            public void requestSuccess() {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            messageProgess = getString(R.string.msg_sync_progress_msg_plz_wait);
                                                            syncProgDialog.setMessage(messageProgess);
                                                        } catch (Throwable e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                                AvailableServer.openStoreWithAvailableServer(SyncSelectionViewActivity.this, new UIListener() {
                                                    @Override
                                                    public void onRequestError(int operation, Exception e) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                onError(operation, e);
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                onSuccess(operation,key,selectedList);
                                                            }
                                                        });
                                                    }
                                                });
                                            }

                                            @Override
                                            public void requestError(String errorMessage) {
                                                closingProgressDialog();
                                                showAlert(errorMessage);
                                            }
                                        });
                                    } else {
                                        closingProgressDialog();
                                        LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                        LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                        showAlert(messageError);
                                    }
                                } else if (responseCode == 503 || responseCode == 404 || responseCode == 998 || responseCode == 999 || responseCode == 996) {
                                    // 503 - service unavailable , 404 - Resource file not maintained , 998 - Backend unavailable , 999 - Resource file is empty
                                    //996 - Resource file available but backend property missing
                                    closingProgressDialog();
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    showAlert(messageError);
                                } else if (responseCode == 401) {
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    try {
                                        sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                        String loginUser = sharedPreferences.getString("username", "");
                                        String login_pwd = sharedPreferences.getString("password", "");
                                        Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                                            @Override
                                            public void passwordStatus(final JSONObject jsonObject) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        closingProgressDialog();
                                                        Constants.passwordStatusErrorMessage(SyncSelectionViewActivity.this, jsonObject, registrationModel, loginUser);
                                                    }
                                                });

                                            }
                                        });
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                } else {
                                    closingProgressDialog();
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    showAlert(messageError);
                                }
                            }
                        } else {
                            closingProgressDialog();
                            LogManager.writeLogError("Sync can't be performed due to server unavailability. Please try later");
                            showAlert("Sync can't be performed due to server unavailability. Please try later");
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Constants.showAlert(ErrorCode.NETWORK_ERROR, SyncSelectionViewActivity.this);
                }
            });

            isAllSync = false;
        }
    }

    @Override
    public void onRequestError(int operation, Exception e) {
        ConstantsUtils.checkNetwork(this,null,true);
        String errorCode  = ConstantsUtils.getErrorCode(String.valueOf(e.getCause()));
        ErrorBean errorBean = Constants.getErrorCode(operation, e, SyncSelectionViewActivity.this);
        if (e.toString().contains("HTTP Status 401 ? Unauthorized") || e.toString().contains("invalid authentication")) {
            try {
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                String loginUser = sharedPreferences.getString("username", "");
                String login_pwd = sharedPreferences.getString("password", "");
                Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                    @Override
                    public void passwordStatus(final JSONObject jsonObject) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closingProgressDialog();
                                Constants.passwordStatusErrorMessage(SyncSelectionViewActivity.this, jsonObject, registrationModel, loginUser);
                            }
                        });

                    }
                });
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            if (!TextUtils.equals(errorCode, "10345") && !TextUtils.equals(errorCode, "10346") && !TextUtils.equals(errorCode, "10349")) {
                if (errorBean.hasNoError()) {
                    if (!dialogCancelled && !Constants.isStoreClosed) {
                        if (operation == Operation.OfflineRefresh.getValue()) {
                            Constants.isSync = false;
                            SyncUtils.updatingSyncHistroyTime(SyncSelectionViewActivity.this, alAssignColl, Constants.DownLoad, new RefreshListInterface() {
                                @Override
                                public void refreshList() {
                                    closingProgressDialog();
                                    syncCompletedWithErrorDialog();
                                }
                            }, refguid.toString().toUpperCase());

//                        updatingSyncTime();
//                        closingProgressDialog();
//                        Constants.isSync = false;
//                        syncCompletedWithErrorDialog();
                        } else if (operation == Operation.GetStoreOpen.getValue()) {
                            Constants.isSync = false;
                            SyncUtils.updatingSyncHistroyTime(SyncSelectionViewActivity.this, alAssignColl, Constants.DownLoad, new RefreshListInterface() {
                                @Override
                                public void refreshList() {
                                    closingProgressDialog();
                                    syncCompletedWithErrorDialog();
                                }
                            }, refguid.toString().toUpperCase());
//                        closingProgressDialog();
//                        Constants.isSync = false;
//                        syncCompletedWithErrorDialog();
                        }
                    }
                } else if (errorBean.isStoreFailed()) {
                    if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                        closingProgressDialog();
                        Constants.isSync = true;
                        dialogCancelled = false;
                        asyncSyncData = new AsyncSyncData();
                        asyncSyncData.execute();
                    } else {
                        Constants.isSync = false;
                        closingProgressDialog();
                        Constants.displayMsgReqError(errorBean.getErrorCode(), SyncSelectionViewActivity.this);
                    }
                } else {
                    Constants.isSync = false;
                    closingProgressDialog();
                    Constants.displayMsgReqError(errorBean.getErrorCode(), SyncSelectionViewActivity.this);
                }
            } else {
                closingProgressDialog();
                Constants.isSync = false;
            }
        }
    }

    @Override
    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
        if (dialogCancelled == false && !Constants.isStoreClosed) {
            if (operation == Operation.OfflineRefresh.getValue()) {
//                try {
//                    LogManager.writeLogDebug("Sync Selection View DB Export");
//                    SyncSelectionViewActivity.exportDB(SyncSelectionViewActivity.this);
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                }
                try {
                    Constants.getTodayDashboardAch(SyncSelectionViewActivity.this);
                } catch (Throwable e) {
                    e.printStackTrace();
                }


                try {
                    OfflineManager.getAuthorizations(getApplicationContext());
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
                OutletSQLHelper helper = new OutletSQLHelper(this);
                if (isFresh){

                }
                if (isRoute) {

                }
//                Constants.setBirthdayListToDataValut(SyncSelectViewActivity.this);
//                setAppointmentNotification();
                isLocalSync =false;
//                updatingSyncTime();
                Constants.isSync = false;
                ConstantsUtils.checkNetwork(this,null,true);
                SyncUtils.updatingSyncHistroyTime(SyncSelectionViewActivity.this, alAssignColl, Constants.DownLoad, new RefreshListInterface() {
                    @Override
                    public void refreshList() {
                        new LoadData(SyncSelectionViewActivity.this).execute();
//                        closingProgressDialog();
//                        syncCompletedWithErrorDialog();
                    }
                },refguid.toString().toUpperCase());
//                new LoadData(this).execute();
                if (isLocalSync) {
                    isRetailerOrBeatSync=false;
//                ConstantsUtils.startAutoSync(SyncSelectionViewActivity.this,false);

                }
               /* if(alAssignColl.contains(Constants.Visits) || alAssignColl.contains(Constants.ChannelPartners) ) {
                    Constants.setBirthdayListToDataValut(SyncSelectionViewActivity.this);
                    Constants.setBirthDayRecordsToDataValut(SyncSelectionViewActivity.this);
                    Constants.setAppointmentNotification(SyncSelectionViewActivity.this);
                }

                if(alAssignColl.contains(Constants.Alerts)) {
                    Constants.setAlertsRecordsToDataValut(SyncSelectionViewActivity.this);;
                }*/
            } else if (operation == Operation.GetStoreOpen.getValue() && OfflineManager.isOfflineStoreOpen()) {
                Constants.isSync = false;
                try {
                    OfflineManager.getAuthorizations(getApplicationContext());
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
                Constants.setSyncTime(SyncSelectionViewActivity.this);

                if(alAssignColl.contains(Constants.Visits) || alAssignColl.contains(Constants.ChannelPartners) ) {
                    Constants.setBirthdayListToDataValut(SyncSelectionViewActivity.this);
                    Constants.setBirthDayRecordsToDataValut(SyncSelectionViewActivity.this);
                    Constants.setAppointmentNotification(SyncSelectionViewActivity.this);
                }

                if(alAssignColl.contains(Constants.Alerts)) {
                    Constants.setAlertsRecordsToDataValut(SyncSelectionViewActivity.this);
                }
                ConstantsUtils.startAutoSync(SyncSelectionViewActivity.this,false);
                closingProgressDialog();
                syncCompletedDialog();
            }
        }
    }

    public void getSchemList(){

    }

    private void updatingSyncTime() {
        SyncUtils.updatingSyncTime(SyncSelectionViewActivity.this, alAssignColl);
    }

    private void closingProgressDialog() {
        try {
            if (syncProgDialog!=null&&syncProgDialog.isShowing()) {
                syncProgDialog.dismiss();
                cancelTimer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void syncCompletedWithErrorDialog() {
        Constants.dialogBoxWithCallBack(SyncSelectionViewActivity.this, "", getString(R.string.msg_error_occured_during_sync), getString(R.string.ok), "", false, new DialogCallBack() {
            @Override
            public void clickedStatus(boolean b) {
                onBackPressed();
            }
        });
    }
    private void syncCompletedDialog() {
        Constants.dialogBoxWithCallBack(SyncSelectionViewActivity.this, "", getString(R.string.msg_sync_successfully_completed), getString(R.string.ok), "", false, new DialogCallBack() {
            @Override
            public void clickedStatus(boolean b) {
               /* if (!AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, SyncSelectionViewActivity.this, BuildConfig.APPLICATION_ID, true, Constants.APP_UPGRADE_TYPESET_VALUE))
                    onBackPressed();*/
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
           /* case R.id.imageViewHome:
                startActivity(new Intent(this, BILMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;*/
        }
    }

    public class AsyncSyncData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            syncProgDialog = new ProgressDialog(SyncSelectionViewActivity.this, R.style.ProgressDialogTheme);
//            syncProgDialog.setTitle("Loading..");
//            setTimer();
            messageProgess =getString(R.string.msg_sync_progress_msg_plz_wait);
//            syncProgDialog.setMessage(messageProgess);
            syncProgDialog.setCancelable(true);
//            syncProgDialog.setCanceledOnTouchOutside(false);
//            syncProgDialog.show();

            syncProgDialog
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface Dialog) {
                            UtilConstants.dialogBoxWithCallBack(SyncSelectionViewActivity.this, "", getString(R.string.do_want_cancel_sync), getString(R.string.yes)
                                    , getString(R.string.no), false, new DialogCallBack() {
                                        @Override
                                        public void clickedStatus(boolean clickedStatus) {
                                            if (clickedStatus) {
                                                dialogCancelled = true;
                                                onBackPressed();
                                            } else {
                                                try {
                                                    syncProgDialog
                                                            .show();
                                                    syncProgDialog
                                                            .setCancelable(true);
                                                    syncProgDialog
                                                            .setCanceledOnTouchOutside(false);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                dialogCancelled = false;
                                            }
                                        }
                                    });
//                            AlertDialog.Builder builder = new AlertDialog.Builder(
//                                    SyncSelectionViewActivity.this, R.style.MyTheme);
//                            builder.setMessage(R.string.do_want_cancel_sync)
//                                    .setCancelable(false)
//                                    .setPositiveButton(
//                                            R.string.yes,
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(
//                                                        DialogInterface Dialog,
//                                                        int id) {
//                                                    dialogCancelled = true;
//
//                                                    onBackPressed();
//                                                }
//                                            })
//                                    .setNegativeButton(
//                                            R.string.no,
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(
//                                                        DialogInterface Dialog,
//                                                        int id) {
//
//                                                    try {
//                                                        syncProgDialog
//                                                                .show();
//                                                        syncProgDialog
//                                                                .setCancelable(true);
//                                                        syncProgDialog
//                                                                .setCanceledOnTouchOutside(false);
//                                                    } catch (Exception e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                    dialogCancelled = false;
//
//                                                }
//                                            });
//                            builder.show();
                        }
                    });
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (!alAssignColl.contains(Constants.ConfigTypsetTypeValues))
                alAssignColl.add(Constants.ConfigTypsetTypeValues);
            if (!OfflineManager.isOfflineStoreOpen()) {
                try {
//                    OfflineManager.openOfflineStore(SyncSelectionViewActivity.this, SyncSelectionViewActivity.this);
                    OfflineManager.openOfflineStoreInternal(SyncSelectionViewActivity.this, SyncSelectionViewActivity.this);
                } catch (Throwable e) {
                    e.printStackTrace();
                    LogManager.writeLogError(Constants.error_txt + e.getMessage());
                }
            } else {
                try {
                    String concatCollectionStr = Constants.getConcatinatinFlushCollectios(alAssignColl);
                    if (concatCollectionStr.contains("ChannelPartners")){
                        isFresh =true;
                    }
                    if (concatCollectionStr.contains("RouteSchedulePlans")){
                        isRoute =true;
                    }
                    refguid = GUID.newRandom();
                    SyncUtils.updatingSyncStartTime(SyncSelectionViewActivity.this,Constants.DownLoad,Constants.StartSync,refguid.toString().toUpperCase());
                    OfflineManager.refreshStoreSync(getApplicationContext(), SyncSelectionViewActivity.this, Constants.Fresh, concatCollectionStr);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    class LoadData extends AsyncTask<Void,Void,Void>{
        Context context;

        public LoadData(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String rollID = ConstantsUtils.getAuthInformation(SyncSelectionViewActivity.this);
                if(rollID.equalsIgnoreCase(Constants.PSM)) {
                  //  DashBoardPresenter.getBeatsRemoteData(SyncSelectionViewActivity.this, DashBoard.distributorBean.getCPGUID());
                }else {
                    getSchemList();
                                   }
                while (!isLocalSync) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    closingProgressDialog();
                    syncCompletedDialog();
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            closingProgressDialog();
            syncCompletedDialog();
        }
    }

    String countdown = "";
    long second = 0;
    String messageProgess = "";
    long milliSec = 2400000;
    CountDownTimer countDownTimer = null;

    private void setTimer() {
        countdown = "";
        try {
            if (countDownTimer != null) {
                LogManager.writeLogInfo("Login Timer count Down is cancel");
                countDownTimer.cancel();
                countDownTimer = null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        countDownTimer = new CountDownTimer(milliSec, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = milliSec / 1000 - millisUntilFinished / 1000;
                long minutes = ((milliSec / 1000) / 60) - ((millisUntilFinished / 1000) / 60) - 1;
//                            if(minutes==(seconds/60)){
//                                second=0;
//                            }else{
//                                second++;
//                            }

                int sec = (int) seconds - (int) minutes * 60;
                countdown = String.format("%02d", minutes) + ":" + String.format("%02d", sec);
                String timeStamp = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm aa", new Date());
                if (syncProgDialog != null && syncProgDialog.isShowing()) {
//                                progressDialog.setMessage(message+"  \n\n "+timeStamp+"\t\t\t"+countdown);
                    syncProgDialog.setTitle(timeStamp);
                    syncProgDialog.setMessage(messageProgess + "\t\t" + countdown);

                    if (String.format("%02d", minutes).equalsIgnoreCase("10")) {
                        syncProgDialog.dismiss();
                        if (asyncSyncData != null)
                            asyncSyncData.cancel(true);
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                SyncSelectionViewActivity.this, R.style.MyTheme);
                        builder.setMessage(R.string.error_sync_msg)
                                .setCancelable(false)
                                .setPositiveButton(
                                        R.string.ok,
                                        (Dialog1, id) -> {
                                            dialogCancelled = true;
                                            onBackPressed();
                                            if (countDownTimer != null) {
                                                LogManager.writeLogInfo("Login Timer count Down is cancel");
                                                countDownTimer.cancel();
                                                countDownTimer = null;
                                            }
                                        });
                        builder.show();
                    }
                }
            }

            @Override
            public void onFinish() {

            }

        };
        countDownTimer.start();
    }

    private void cancelTimer() {
        try {
            if (countDownTimer != null) {
                LogManager.writeLogInfo("update sync Timer count Down is cancel");
                countDownTimer.cancel();
                countDownTimer = null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private void showAlert(String message) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SyncSelectionViewActivity.this, R.style.DialogTheme);
                    String timeStamp = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm aa", new Date());
                    builder.setTitle(timeStamp);
//            builder.setMessage(message+"\n\n"+timeStamp+"").setCancelable(false)
                    builder.setMessage(message).setCancelable(false)
                            .setPositiveButton(getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onError(int operation, Exception e){
        ConstantsUtils.checkNetwork(this,null,true);
        String errorCode  = ConstantsUtils.getErrorCode(String.valueOf(e.getCause()));
        ErrorBean errorBean = Constants.getErrorCode(operation, e, SyncSelectionViewActivity.this);
        if (e.toString().contains("HTTP Status 401 ? Unauthorized") || e.toString().contains("invalid authentication")) {
            try {
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                String loginUser = sharedPreferences.getString("username", "");
                String login_pwd = sharedPreferences.getString("password", "");
                Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                    @Override
                    public void passwordStatus(final JSONObject jsonObject) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closingProgressDialog();
                                Constants.passwordStatusErrorMessage(SyncSelectionViewActivity.this, jsonObject, registrationModel, loginUser);
                            }
                        });

                    }
                });
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            if (!TextUtils.equals(errorCode, "10345") && !TextUtils.equals(errorCode, "10346") && !TextUtils.equals(errorCode, "10349")) {
                if (errorBean.hasNoError()) {
                    if (!dialogCancelled && !Constants.isStoreClosed) {
                        if (operation == Operation.OfflineRefresh.getValue()) {
                            Constants.isSync = false;
                            SyncUtils.updatingSyncHistroyTime(SyncSelectionViewActivity.this, alAssignColl, Constants.DownLoad, new RefreshListInterface() {
                                @Override
                                public void refreshList() {
                                    closingProgressDialog();
                                    syncCompletedWithErrorDialog();
                                }
                            }, refguid.toString().toUpperCase());

//                        updatingSyncTime();
//                        closingProgressDialog();
//                        Constants.isSync = false;
//                        syncCompletedWithErrorDialog();
                        } else if (operation == Operation.GetStoreOpen.getValue()) {
                            Constants.isSync = false;
                            SyncUtils.updatingSyncHistroyTime(SyncSelectionViewActivity.this, alAssignColl, Constants.DownLoad, new RefreshListInterface() {
                                @Override
                                public void refreshList() {
                                    closingProgressDialog();
                                    syncCompletedWithErrorDialog();
                                }
                            }, refguid.toString().toUpperCase());
//                        closingProgressDialog();
//                        Constants.isSync = false;
//                        syncCompletedWithErrorDialog();
                        }
                    }
                } else if (errorBean.isStoreFailed()) {
                    if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                        closingProgressDialog();
                        Constants.isSync = true;
                        dialogCancelled = false;
                        asyncSyncData = new AsyncSyncData();
                        asyncSyncData.execute();
                    } else {
                        Constants.isSync = false;
                        closingProgressDialog();
                        Constants.displayMsgReqError(errorBean.getErrorCode(), SyncSelectionViewActivity.this);
                    }
                } else {
                    Constants.isSync = false;
                    closingProgressDialog();
                    Constants.displayMsgReqError(errorBean.getErrorCode(), SyncSelectionViewActivity.this);
                }
            } else {
                closingProgressDialog();
                Constants.isSync = false;
            }
        }
    }

    private void onSuccess(int operation, String key,ArrayList<String> selectedList) {
//            syncDownload(selectedList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageProgess =getString(R.string.msg_sync_progress_msg_plz_wait);
//            syncProgDialog.setMessage(messageProgess);
                syncProgDialog.setCancelable(true);
            }
        });

        refguid = GUID.newRandom();
        new RefreshAsyncTask(SyncSelectionViewActivity.this, "", this).execute();
    }
    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
}

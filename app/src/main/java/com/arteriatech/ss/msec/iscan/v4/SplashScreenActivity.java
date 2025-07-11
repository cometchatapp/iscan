package com.arteriatech.ss.msec.iscan.v4;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.MSFAApplication;
import com.arteriatech.ss.msec.iscan.v4.databinding.ActivitySplashScreenNewBinding;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack;
import com.arteriatech.ss.msec.iscan.v4.registration.Configuration;
import com.arteriatech.ss.msec.iscan.v4.registration.RegistrationActivity;
import com.sap.client.odata.v4.DataServiceException;
import com.sap.client.odata.v4.OnlineODataProvider;
import com.sap.client.odata.v4.core.AndroidSystem;
import com.sap.client.odata.v4.http.HttpException;
import com.sap.client.odata.v4.http.SDKHttpHandler;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.authflows.CommonAuthFlowsConfigurator;
import com.sap.smp.client.httpc.authflows.UsernamePasswordProvider;
import com.sap.smp.client.httpc.authflows.UsernamePasswordToken;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.httpc.events.ISendEvent;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.smpclient.Connection;
import com.sap.smp.client.smpclient.SmpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by e10526 on 03-02-2017.
 *
 */
public class SplashScreenActivity extends AppCompatActivity implements UIListener {
    private ActivitySplashScreenNewBinding binding;
    // android components
    public volatile static boolean isThreadActive;
    String errorLog;
    AlertDialog progressDialog = null;
    SharedPreferences sharedPreferences;
    public static String loginUser_Text = "";
    public static String login_pwd = "";
    private int mCurrentAttempt = 0;
    private ArrayList<String> userList = new ArrayList();
    private int totalAttempt = 3;
    private DataServiceException dataServiceException = null;
    private String errorMsg = "";
    private HttpException httpException = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTheme(R.style.Theme_BHUBRURAL);
        sharedPreferences=getSharedPreferences(Constants.PREFS_NAME,0);
       // navigateHomeScreen();
        testUseAndroidString();
    }


    private void navigateHomeScreen(){
        errorLog =  getIntent().getStringExtra(Constants.EXTRA_CRASHED_FLAG);
        if (errorLog!=null&&!errorLog.isEmpty()){
            String finalErrorLog = errorLog;
            Constants.dialogBoxWithCallBack(this, "", "Application closed unexpectedly", "Open App", "Share Details", false, new DialogCallBack() {
                @Override
                public void clickedStatus(boolean clickedStatus) {
                    if (clickedStatus){
                        sharePref();
                    }else{
                        File file = new File(SplashScreenActivity.this.getFilesDir(), "text");
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        try {
                            File gpxfile = new File(file, "DBSMCrashLog.txt");
                            FileWriter writer = new FileWriter(gpxfile);
                            writer.append(finalErrorLog);
                            writer.flush();
                            writer.close();
                            shareFile(gpxfile);
                        } catch (Throwable e) {
                            LogManager.writeLogDebug(e.toString());
                        }
                    }
                }
            });
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sharePref();
                }
            },2000);
        }

    }

    private void sharePref() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME,0);
        boolean introScreen = sharedPreferences.getBoolean(Constants.introScreen,false);
        if(!introScreen){
            //startActivity(new Intent(SplashScreenActivity.this, IntroActivity.class));
           // finish();
        }else {
            startActivity(new Intent(SplashScreenActivity.this, RegistrationActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestError(int operation, Exception e) {
        isThreadActive =false;
    }

    @Override
    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
        isThreadActive =false;
    }

    private void shareFile(File file) {

        try {
            Uri fileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            Intent intent = ShareCompat.IntentBuilder.from(this)
                    .setStream(fileUri) // uri from FileProvider
                    .setType("text/html")
                    .getIntent()
                    .setAction(Intent.ACTION_SEND) //Change if needed
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            MSFAApplication.setGAFields(this);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //MSFAApplication.setAnalytics(this,spNo,this.getClass().getSimpleName(),"Splash Screen");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        navigateHomeScreen();
    }

    public void testUseAndroidString() {
        Intent intent = getIntent();
        String receivedText = intent.getStringExtra("key1");
        System.out.println("received no " + receivedText);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString("mobileNo",receivedText);
        editor.commit();

        checkUserIsActive();




    }
    HttpsURLConnection connection = null;
    JSONObject jsonHeaderObject = null;
    String errorMessage="";
    private boolean doUserValidation(URL url, String userName, String psw) {
        boolean isVaildUser = false;
        String resultJson = "";
        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(Configuration.connectionTimeOut);
            connection.setConnectTimeout(Configuration.connectionTimeOut);
            String userCredentials = userName + ":" + psw;
            String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes("UTF-8"), 2);
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestProperty("x-smp-appid", Configuration.APP_ID);
            connection.setRequestProperty("x-smp-enduser", userName);
            connection.setRequestProperty("X-CSRF-Token", "Fetch");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();

            connection.getResponseMessage();
            InputStream stream = null;
            System.out.println("response code " + responseCode);
            if (responseCode != 200) {
                if (responseCode == 401) {
                    errorMessage = "Error Code 401-You don't have authorization to access the app for this brand. Kindly contact your sales executive";
                } else
                    errorMessage = "Your account is currently Inactive. Kindly contact DMS Helpdesk.";
                throw new IOException("HTTP error code: " + responseCode);
            } else if (responseCode == 200) {
                if (responseCode == 200) {
                    stream = connection.getInputStream();
                    if (stream != null) {
                        resultJson = readResponse(stream);
                    }
                } else {
                    stream = connection.getErrorStream();
                    if (stream != null) {
                        resultJson = readResponse(stream);
                    }
                }
                if (!TextUtils.isEmpty(resultJson)) {
                    JSONObject jsonObject = new JSONObject(resultJson);
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("d"));
                    JSONArray jsonArray = jsonObject1.optJSONArray("results");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject authObject = null;
                            try {
                                authObject = jsonArray.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (!TextUtils.isEmpty(authObject.optString("Application")) && authObject.optString("Application").equalsIgnoreCase("PD")) {
                                isVaildUser = true;
                                SharedPreferences sharedPerf = getSharedPreferences(Constants.PREFS_NAME, 0);
                                SharedPreferences.Editor editor = sharedPerf.edit();
                                editor.putString(Constants.PartnerID,  authObject.optString("PartnerID"));
                                editor.putString(Constants.PartnerName,  authObject.optString("PartnerName"));
                                editor.putString(Constants.LoginID,  authObject.optString("LoginID"));
                                editor.putString(Constants.PartnerTypeID,  authObject.optString("PartnerTypeID"));
                                editor.apply();
                                break;
                            } else if (!TextUtils.isEmpty(authObject.optString("StatusID")) && authObject.optString("StatusID").equalsIgnoreCase("02")) {
                                isVaildUser = false;
                                errorMessage = "Your account is currently blocked. Kindly contact your sales executive.";
                            } else {
                                errorMessage = "Your account is currently Inactive. Kindly contact DMS Helpdesk.";
                                isVaildUser = false;
                            }
                        }


                    }
                }


            }

        } catch (Exception var12) {
            var12.printStackTrace();
        } finally {
			/*if (connection != null) {
				connection.disconnect();
			}*/

        }


        return isVaildUser;
    }

    private static String readResponse(InputStream stream) throws IOException {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder buffer = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line).append('\n');
        }

        return buffer.toString();
    }

    private void checkUserIsActive() {
        progressDialog = UtilConstants.showProgressDialogs(this, "Getting OTP please wait", false);
        String host = "https://" + Configuration.server_Text + "/" + Configuration.APP_ID;
        String username=sharedPreferences.getString("mobileNo","8888888888");
     //   String url = host + "/UserPartners?$filter=%20LoginID%20eq'" + username + "'%20and%20Application%20eq'PD'";
        String url = host + "/UserPartners";


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean isActiveUser = false;
                    String pass = sharedPreferences.getString(Constants.AutoPasswordForReset, "Welcome@2025");
                    if (TextUtils.isEmpty(pass))
                        pass = "Welcome@2025";
                    isActiveUser = doUserValidation(new URL(url), username.trim(), pass);
                    if (isActiveUser) {
                        validateUser();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!TextUtils.isEmpty(errorMessage) && errorMessage.contains("Error Code 401")) {
                                   // checkPasswordStatus();
                                } else {
                                 //   closeProgDialog();
                                    if (!TextUtils.isEmpty(errorMessage))
                                        UtilConstants.showAlert(errorMessage, SplashScreenActivity.this);
                                    else
                                        UtilConstants.showAlert("Your account is currently Inactive. Kindly contact DMS Helpdesk.", SplashScreenActivity.this);
                                }
                            }
                        });

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void validateUser() {
        progressDialog = UtilConstants.showProgressDialogs(this, "Getting OTP please wait", false);
        String host = "https://" + Configuration.server_Text + "/" + Configuration.APP_ID;

        String url = host + "/UserProfiles('PD')";

        onRegister(SplashScreenActivity.this);


       /* new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean isValidatedUser = false;
                    String pass = sharedPreferences.getString(Constants.AutoPasswordForReset, "Welcome@2020");
                    if (TextUtils.isEmpty(pass))
                        pass = "Welcome@2020";

                    isValidatedUser = getUserDetials(new URL(url), txtusername.getText().toString().trim(), pass);
                    if (isValidatedUser) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showEntereOTPDialog();
                                closeProgDialog();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeProgDialog();
                                UtilConstants.showAlert("User is not Authorized for Exide Humsafar Application.Please Contact Channel Team", UtilRegistrationActivity.this);
                            }
                        });

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }


    private void onRegister(Context mContext) {
            String userName = sharedPreferences.getString(Constants.LoginID,"");
            this.mCurrentAttempt = Collections.frequency(this.userList, userName);
            if (this.mCurrentAttempt <= this.totalAttempt) {
                this.requestOnline();
            } else {
                UtilConstants.showAlert("[A1000] " + this.getString(R.string.wrong_psw_error_msg_3, new Object[]{String.valueOf(this.totalAttempt)}), this);
            }

    }

    private void requestOnline() {
        if (UtilConstants.isNetworkAvailable(this)) {
            this.progressDialog = UtilConstants.showProgressDialogs(this, this.getString(R.string.register_with_server_plz_wait), false);
            loginUser_Text =sharedPreferences.getString(Constants.LoginID,"");
            login_pwd = "Welcome@2025";
            (new AsynTaskRegistration()).execute(new Void[0]);
        } else {
            UtilConstants.dialogBoxWithCallBack(this, "", this.getString(R.string.reg_no_network_conn, new Object[]{"A3000"}), this.getString(R.string.network_retry), this.getString(R.string.cancel), false, new DialogCallBack() {
                public void clickedStatus(boolean clickedStatus) {
                    if (clickedStatus) {
                        requestOnline();
                    }

                }
            });
            LogManager.writeLogError(this.getString(R.string.reg_no_network_conn, new Object[]{"A3000"}));
        }

    }


    @SuppressLint({"LongLogTag"})
    private class AsynTaskRegistration extends AsyncTask<Void, Boolean, Boolean> {
        String passwordStatus;
        String PUserID;

        private AsynTaskRegistration() {
            this.passwordStatus = "";
            this.PUserID = "";
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... params) {
            return onDeviceReg();
        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (dataServiceException == null && result) {
                try {
                    com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, loginUser_Text,login_pwd, Configuration.IDPURL, new com.arteriatech.mutils.common.UtilConstants.PasswordStatusCallback() {
                        public void passwordStatus(final JSONObject jsonObject) {
                            Thread thread = new Thread(new Runnable() {
                                public void run() {
                                    JSONObject jsonUsrDetails = null;

                                    try {
                                        String jsonValue = com.arteriatech.mutils.common.UtilConstants.getUserDetails(jsonObject, Configuration.IDPURL, Configuration.IDPTUSRNAME, Configuration.IDPTUSRPWD);

                                        try {
                                            if (!TextUtils.isEmpty(jsonValue)) {
                                                jsonUsrDetails = new JSONObject(jsonValue);
                                                AsynTaskRegistration.this.passwordStatus = jsonUsrDetails.optString("passwordStatus");
                                                AsynTaskRegistration.this.PUserID = jsonUsrDetails.optString("id");
                                            }
                                        } catch (Throwable var4) {
                                            var4.printStackTrace();
                                        }
                                    } catch (IOException var5) {
                                        var5.printStackTrace();
                                        closeProgDialog();
                                    }

                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            closeProgDialog();
                                            Intent intentNavChangePwdScreen;


                                        }
                                    });
                                }
                            });
                            thread.start();
                        }
                    });
                } catch (Throwable var4) {
                    var4.printStackTrace();
                   closeProgDialog();
                }
            } else if (httpException != null) {
                closeProgDialog();
                UtilConstants.dialogBoxWithCallBack(SplashScreenActivity.this, "", "Registration cannot be performed due to network unavailability", getString(R.string.network_retry), getString(R.string.cancel), false, new DialogCallBack() {
                    public void clickedStatus(boolean clickedStatus) {
                        if (clickedStatus) {
                            requestOnline();
                        }

                    }
                });
            } else {
                try {
                    String err_msg = null;
                    if (dataServiceException != null) {
                        err_msg = UtilConstants.getErrorMsg(dataServiceException.getStatus(), SplashScreenActivity.this);
                        diaplyErrorMsg(err_msg, dataServiceException.getStatus());
                    } else {
                        userIDandPasswordcheck();
                    }
                } catch (Throwable var3) {
                    var3.printStackTrace();
                }
            }

        }
    }

    private void closeProgDialog() {
        try {
            UtilConstants.hideProgressDialog(this.progressDialog);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    private void diaplyErrorMsg(String err_msg, int err_code) {
        String mStrAttemptText = "";
        if (err_code == 401) {
            if (UtilConstants.isNetworkAvailable(this)) {
                try {
                    com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, loginUser_Text, login_pwd, Configuration.APP_ID, new com.arteriatech.mutils.common.UtilConstants.PasswordStatusCallback() {
                        public void passwordStatus(final JSONObject jsonObject) {
                            runOnUiThread(new Runnable() {
                                @SuppressLint({"StringFormatInvalid"})
                                public void run() {
                                    closeProgDialog();
                                    String finalErrorMsg = "";
                                    if (jsonObject != null) {
                                        int code = jsonObject.optInt("code");
                                        String message = jsonObject.optString("message");
                                        if (code == 401) {
                                            if (message.equalsIgnoreCase("")) {
                                                finalErrorMsg = getString(R.string.userid_error_message, new Object[]{"A3000"});
                                            } else if (message.equalsIgnoreCase("Unauthorized")) {
                                                finalErrorMsg = getString(R.string.unauthorized_error_message, new Object[]{"A3000"});
                                            } else if (message.equalsIgnoreCase("USER_INACTIVE")) {
                                                finalErrorMsg = getString(R.string.forget_pwd_user_inactive_error_message, new Object[]{"A3000"});
                                            } else if (message.equalsIgnoreCase("PASSWORD_LOCKED")) {
                                                finalErrorMsg = getString(R.string.login_password_lock_error_message, new Object[]{"A3000"});
                                            }

                                            LogManager.writeLogError(finalErrorMsg);
                                            com.arteriatech.mutils.common.UtilConstants.passwordStatusErrorMessage(SplashScreenActivity.this, jsonObject, loginUser_Text, Configuration.IDPURL, Configuration.IDPTUSRNAME, Configuration.IDPTUSRPWD, Configuration.APP_ID);
                                        }
                                    }

                                }
                            });
                        }
                    });
                } catch (IOException var6) {
                    this.closeProgDialog();
                    var6.printStackTrace();
                }
            } else {
                this.closeProgDialog();
            }
        } else {
            LogManager.writeLogError(err_msg);
            this.closeProgDialog();
            UtilConstants.showAlertWithHeading(err_msg, this, mStrAttemptText);
        }

    }


    public boolean onDeviceReg() {
        try {
            this.dataServiceException = null;
            String requestUri = getAppURL(Configuration.server_Text, Configuration.port_Text, Configuration.APP_ID, Configuration.IS_HTTPS);
            final UsernamePasswordToken token = new UsernamePasswordToken(loginUser_Text, login_pwd);
            HttpConversationManager conversationManager = (new CommonAuthFlowsConfigurator(SplashScreenActivity.this)).supportBasicAuthUsing(new UsernamePasswordProvider(){
                public Object onCredentialsNeededUpfront(ISendEvent event) {
                    return token;
                }

                public Object onCredentialsNeededForChallenge(IReceiveEvent event) {
                    return token;
                }
            }).configure(new HttpConversationManager(SplashScreenActivity.this));
            SDKHttpHandler sdkHttpHandler = new SDKHttpHandler(SplashScreenActivity.this, conversationManager);
            AndroidSystem.setContext(SplashScreenActivity.this);
            OnlineODataProvider registrationProvider = new OnlineODataProvider("SmpClient", requestUri);
            registrationProvider.getNetworkOptions().setHttpHandler(sdkHttpHandler);
            registrationProvider.getServiceOptions().setCheckVersion(false);
            SmpClient smpClient = new SmpClient(registrationProvider);
            Connection newConnection = new Connection();
            newConnection.setDeviceType("Android");
            newConnection.setDefaultValues();
            if (UtilConstants.isNetworkAvailable(this)) {
                try {
                    try {
                        smpClient.createEntity(newConnection);
                        //onSaveConfig(this.registrationModel, newConnection);
                        return true;
                    } catch (DataServiceException var9) {
                        LogManager.writeLogInfo("Registration failed due " + var9.getMessage());
                        var9.printStackTrace();
                        this.dataServiceException = var9;
                    }
                } catch (Throwable var10) {
                    this.errorMsg = var10.getMessage();
                    LogManager.writeLogInfo("Registration failed due " + var10.getMessage());
                    var10.printStackTrace();
                }

                return false;
            } else {
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        UtilConstants.dialogBoxWithCallBack(SplashScreenActivity.this, "", SplashScreenActivity.this.getString(R.string.reg_no_network_conn, new Object[]{"A3000"}),getString(R.string.network_retry), getString(R.string.cancel), false, new DialogCallBack() {
                            public void clickedStatus(boolean clickedStatus) {
                                if (clickedStatus) {
                                    requestOnline();
                                }

                            }
                        });
                    }
                });
                LogManager.writeLogError(this.getString(R.string.reg_no_network_conn, new Object[]{"A3000"}));
                return false;
            }
        } catch (Throwable var11) {
            var11.printStackTrace();
            return false;
        }
    }
    public static String getAppURL(String serverText, String port, String appID, boolean isHttps) {
        //"https://mobile-a4597c6af.hana.ondemand.com:443/odata/applications/v4/com.arteriatech.mDealerConnect"
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(isHttps ? "https" : "http")
                .encodedAuthority(serverText + ":" + port)
                .appendPath("odata")
                .appendPath("applications")
                .appendPath("v4")
                .appendPath(appID);
//                .fragment(applicationName);
        return builder.build().toString();
    }

    private void userIDandPasswordcheck() {
        LogManager.writeLogInfo("Registration failed  due to " + this.errorMsg);
        if (this.errorMsg.contains("No address associated with hostname")) {
            LogManager.writeLogInfo("Registration failed  due to network unavailability");
            this.diaplyErrorMsg("Registration cannot be performed due to network unavailability", 0);
        } else if (this.errorMsg.contains("Maximum restarts reached")) {
            if (UtilConstants.isNetworkAvailable(this)) {
                try {
                    com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, loginUser_Text, login_pwd, Configuration.APP_ID, new com.arteriatech.mutils.common.UtilConstants.PasswordStatusCallback() {
                        public void passwordStatus(final JSONObject jsonObject) {
                            runOnUiThread(new Runnable() {
                                @SuppressLint({"StringFormatInvalid"})
                                public void run() {
                                    closeProgDialog();
                                    if (jsonObject != null) {
                                        String finalErrorMsg = "";
                                        int code = jsonObject.optInt("code");
                                        String message = jsonObject.optString("message");
                                        if (code == 401) {
                                            if (message.equalsIgnoreCase("")) {
                                                finalErrorMsg = getString(R.string.userid_error_message, new Object[]{"A3000"});
                                            } else if (message.equalsIgnoreCase("Unauthorized")) {
                                                finalErrorMsg =getString(R.string.unauthorized_error_message, new Object[]{"A3000"});
                                            } else if (message.equalsIgnoreCase("USER_INACTIVE")) {
                                                finalErrorMsg = getString(R.string.forget_pwd_user_inactive_error_message, new Object[]{"A3000"});
                                            } else if (message.equalsIgnoreCase("PASSWORD_LOCKED")) {
                                                finalErrorMsg = getString(R.string.login_password_lock_error_message, new Object[]{"A3000"});
                                            }

                                            LogManager.writeLogError(finalErrorMsg);
                                            com.arteriatech.mutils.common.UtilConstants.passwordStatusErrorMessage(SplashScreenActivity.this, jsonObject,loginUser_Text, Configuration.IDPURL, Configuration.IDPTUSRNAME, Configuration.IDPTUSRPWD, Configuration.APP_ID);
                                        }
                                    }

                                }
                            });
                        }
                    });
                } catch (IOException var2) {
                    var2.printStackTrace();
                    this.closeProgDialog();
                }
            } else {
                this.closeProgDialog();
            }
        }

    }






}

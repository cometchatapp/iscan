package com.arteriatech.ss.msec.iscan.v4.mutils.registration;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.mutils.actionbar.ActionBarView;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack;
import com.arteriatech.mutils.log.LogManager;
import com.sap.client.odata.v4.DataServiceException;
import com.sap.client.odata.v4.OnlineODataProvider;
import com.sap.client.odata.v4.core.AndroidSystem;
import com.sap.client.odata.v4.http.SDKHttpHandler;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.authflows.CommonAuthFlowsConfigurator;
import com.sap.smp.client.httpc.authflows.UsernamePasswordProvider;
import com.sap.smp.client.httpc.authflows.UsernamePasswordToken;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.httpc.events.ISendEvent;
import com.sap.smp.client.smpclient.Connection;
import com.sap.smp.client.smpclient.SmpClient;
import com.sybase.persistence.PrivateDataVault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by e10526 on 9/13/2017.
 */

public class UtilRegistrationActivity extends AppCompatActivity {
    /*sharedPreference key don't change*/
    public static final String KEY_AppName = "AppName";
    public static final String KEY_username = "username";
    public static final String KEY_password = "password";
    public static final String KEY_serverHost = "serverHost";
    public static final String KEY_serverPort = "serverPort";
    public static final String KEY_serverClient = "serverClient";
    public static final String KEY_companyid = "companyid";
    public static final String KEY_securityConfig = "securityConfig";
    public static final String KEY_appConnID = "appConnID";
    public static final String KEY_xCSRF_TOKEN = "XCSRToken";
    public static final String KEY_xCSRF_TOKEN_CLOUD = "XCSRTokenCloud";
    public static final String KEY_appID = "appID";
    public static final String KEY_isPasswordSaved = "isPasswordSaved";
    public static final String KEY_isDeviceRegistered = "isDeviceRegistered";
    public static final String KEY_isFirstTimeReg = "isFirstTimeReg";
    public static final String KEY_isFirstTimeDB = "KEY_isFirstTimeDB";
    public static final String KEY_isForgotPwdActivated = "isForgotPwdActivated";
    public static final String KEY_isManadtoryUpdate = "isManadtoryUpdate";
    public static final String KEY_isUserIsLocked = "isUserIsLocked";
    public static final String KEY_ForgotPwdOTP = "ForgotPwdOTP";
    public static final String KEY_ForgotPwdGUID = "ForgotPwdGUID";
    public static final String KEY_isFOSUserRole = "isFOSUserRole";
    public static final String KEY_MaximumAttemptKey = "MaximumAttemptKey";
    public static final String KEY_VisitSeqId = "VisitSeqId";
    public static final String KEY_FARMID = "farmId";
    public static final String KEY_SUFFIX = "suffix";
    public static final String KEY_ISHTTPS = "isHttps";
    public static final String KEY_appEndPoint = "appEndPoint";
    public static final String KEY_pushEndPoint = "pushEndPoint";
    public static final String KEY_SalesPersonName = "SalesPersonName";
    public static final String KEY_SalesPersonMobileNo = "SalesPersonMobileNo";
    public static final String KEY_BirthDayAlertsDate = "BirthDayAlertsDate";


    public static final String EXTRA_IS_FROM_REGISTRATION = "isRegistrationExtra";
    public static final String EXTRA_BUNDLE_REGISTRATION = "isRegBundleExtra";


    public static String loginUser_Text = "";
    public static String login_pwd = "";


    AlertDialog progressDialog = null;
    private com.arteriatech.mutils.registration.RegistrationModel registrationModel = null;
    private EditText txtusername, txtLoginPassword;
    private CheckBox savePass;
    private Button register, support, clear;
    private TextView tvVersion;
    private ImageView ivLogo;
    private Handler mHandler = null;
    private int mCurrentAttempt = 0;
    private int totalAttempt = 3;
    private SharedPreferences sharedPreferences;
    private Context mContext;
    private Bundle bundleExtra = null;
    private Toolbar toolbar;
    private TextInputLayout ilPassword;
    private TextInputLayout ilUserName;
    private RelativeLayout relMain;
    private RelativeLayout rlRegistration;
    private ArrayList<String> userList = new ArrayList<>();
    private DataServiceException dataServiceException = null;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundleExtras = getIntent().getExtras();
        if (bundleExtras != null) {
            registrationModel = (com.arteriatech.mutils.registration.RegistrationModel) bundleExtras.getSerializable(UtilConstants.RegIntentKey);
            bundleExtra = bundleExtras.getBundle(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION);
        }
        LogManager.initialize(UtilRegistrationActivity.this,registrationModel);
        PrivateDataVault.init(getApplicationContext());
        mContext = UtilRegistrationActivity.this;
        mHandler = new Handler();
        if (registrationModel != null) {
            int icon = 0;
            if (registrationModel.getAppActionBarIcon() != 0) {
                icon = registrationModel.getAppActionBarIcon();
            }
            sharedPreferences = getSharedPreferences(registrationModel.getShredPrefKey(), 0);
            String strPref = sharedPreferences.getString(KEY_username, null);
            // show login form
            if (registrationModel.getLayout() != 0) {
                setContentView(registrationModel.getLayout());
            } else {
                setContentView(R.layout.registration);
            }

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (!TextUtils.isEmpty(strPref)) {
//                initLogonCore(UtilRegistrationActivity.this, registrationModel);
               /* try {
                    String mStrUserName = sharedPreferences.getString(KEY_username, "");
                    logonCore.unlockStore(mStrUserName);
                } catch (LogonCoreException e) {
                    e.printStackTrace();
                }*/
                if (registrationModel.getLogInActivity() != null || registrationModel.getPasscodeLoginActivity() != null) {
                    Intent intentNavChangePwdScreen = null;
                    if (registrationModel.getPasscodeLoginActivity() != null) {
                        intentNavChangePwdScreen = new Intent(UtilRegistrationActivity.this, registrationModel.getPasscodeLoginActivity());
                    } else {
                        if (registrationModel.getLoginSuccessActivity() != null) {
                            intentNavChangePwdScreen = new Intent(UtilRegistrationActivity.this, registrationModel.getLoginSuccessActivity());
                        } else {
                            intentNavChangePwdScreen = new Intent(UtilRegistrationActivity.this, registrationModel.getLogInActivity());
                        }
                    }
                    intentNavChangePwdScreen.putExtra(EXTRA_IS_FROM_REGISTRATION, true);
                    if (bundleExtra != null) {
                        intentNavChangePwdScreen.putExtra(EXTRA_BUNDLE_REGISTRATION, bundleExtra);
                    }
                    if (registrationModel != null) {
                        intentNavChangePwdScreen.putExtra(UtilConstants.RegIntentKey, registrationModel);
                    }
                    startActivity(intentNavChangePwdScreen);
                    finish();
                }
            } else {
                String appName = getString(R.string.app_name);
                if (registrationModel != null)
                    appName = registrationModel.getAppName();
                if (toolbar != null)
                    ActionBarView.initActionBarView(this, toolbar, false, appName, icon, 0);
                if ((registrationModel.getLayout() != 0)) {
                    initializeCustomLayout();
                } else {
                    initializeVariables();
                }
            }
        }
    }

    public void onClickClear() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        onClear();
    }

    private void initializeCustomLayout() {
        register = (Button) findViewById(R.id.btRegister);
        clear = (Button) findViewById(R.id.btClear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickClear();
            }
        });

        support = (Button) findViewById(R.id.btSupport);
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                support();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegister(UtilRegistrationActivity.this);
            }
        });

        txtusername = (EditText) findViewById(R.id.et_username);
        txtLoginPassword = (EditText) findViewById(R.id.et_password);
    }

    /*@Override
    public void registrationFinished(boolean success, String s, int errorCode, DataVault.DVPasswordPolicy dvPasswordPolicy) {
        if (success) {
            try {
                // if successful, persist registration
                // DO NOT follow the manual storage of APPCID as shown in SMP docs
                logonCore.persistRegistration();
                onSaveConfig(registrationModel);
                if (registrationModel.getRegisterSuccessActivity() != null) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                closeProgDialog();
                                Intent intentNavChangePwdScreen = new Intent(UtilRegistrationActivity.this, registrationModel.getRegisterSuccessActivity());
                                intentNavChangePwdScreen.putExtra(EXTRA_IS_FROM_REGISTRATION, true);
                                if (bundleExtra != null) {
                                    intentNavChangePwdScreen.putExtra(EXTRA_BUNDLE_REGISTRATION, bundleExtra);
                                }
                                intentNavChangePwdScreen.putExtra(UtilConstants.RegIntentKey, registrationModel);
                                startActivity(intentNavChangePwdScreen);
                                try {
                                    Toast.makeText(mContext, mContext.getString(R.string.registration_success), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    try {
                        logonCore.removeStore();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    closeProgDialog();
                }

            } catch (LogonCoreException e) {
                e.printStackTrace();
                try {
                    logonCore.removeStore();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                closeProgDialog();
            }
        } else {
            try {
                logonCore.removeStore();
            } catch (Exception e) {
                e.printStackTrace();
            }
            closeProgDialog();
            String err_msg = UtilConstants.getErrorMsg(errorCode, UtilRegistrationActivity.this);
            diaplyErrorMsg(err_msg, errorCode);
        }
    }*/

    private void initializeVariables() {
        register = (Button) findViewById(R.id.btRegister);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        relMain = (RelativeLayout) findViewById(R.id.rel_main);
        rlRegistration = (RelativeLayout) findViewById(R.id.rlRegistration);
        tvVersion.setText(registrationModel.getAppVersionName());
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        if (registrationModel.getAppLogo() != 0) {
            ivLogo.setVisibility(View.VISIBLE);
            ivLogo.setImageDrawable(AppCompatResources.getDrawable(UtilRegistrationActivity.this, registrationModel.getAppLogo()));
        } else {
            ivLogo.setVisibility(View.GONE);
        }
        if (registrationModel.getViewBackgroundImage() != 0) {
            rlRegistration.setBackground(ContextCompat.getDrawable(UtilRegistrationActivity.this, registrationModel.getViewBackgroundImage()));
            relMain.setBackground(getResources().getDrawable(R.drawable.border_transparent));
        }
        if (registrationModel.getViewDrawableBorder() != 0) {
            relMain.setBackground(getResources().getDrawable(registrationModel.getViewDrawableBorder()));
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegister(UtilRegistrationActivity.this);
            }
        });
        clear = (Button) findViewById(R.id.btClear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickClear();
            }
        });
        support = (Button) findViewById(R.id.btSupport);
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                support();
            }
        });
        txtusername = (EditText) findViewById(R.id.et_username);
        ilUserName = (TextInputLayout) findViewById(R.id.ilUserName);
        ilPassword = (TextInputLayout) findViewById(R.id.ilPassword);
        txtLoginPassword = (EditText) findViewById(R.id.et_password);
        savePass = (CheckBox) findViewById(R.id.ch_save_pass);
        savePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savePass.isChecked()) {

                } else {

                }
            }
        });
    }
/*
    @Override
    public void deregistrationFinished(boolean b) {

    }

    @Override
    public void backendPasswordChanged(boolean b) {

    }

    @Override
    public void applicationSettingsUpdated() {

    }

    @Override
    public void traceUploaded() {

    }

    @Override
    public void onLogonFinished(String s, boolean b, LogonContext logonContext) {

    }

    @Override
    public void onSecureStorePasswordChanged(boolean b, String s) {

    }

    @Override
    public void onBackendPasswordChanged(boolean b) {

    }

    @Override
    public void onUserDeleted() {

    }

    @Override
    public void onApplicationSettingsUpdated() {

    }

    @Override
    public void registrationInfo() {

    }

    @Override
    public void objectFromSecureStoreForKey() {

    }

    @Override
    public void onRefreshCertificate(boolean b, String s) {

    }*/

    /*private void initLogonCore(Context mContext, RegistrationModel registrationModel) {
        try {
            // get instance
            logonCore = LogonCore.getInstance();

            // get an instance of the LogonUIFacade
            mLogonUIFacade = LogonUIFacade.getInstance();

            mLogonUIFacade.init(this, mContext, registrationModel.getAppID());

            // set listener for LogonCore
            logonCore.setLogonCoreListener(this);

            // boot up
            logonCore.init(this, registrationModel.getAppID());

            // check if store is open and available
            // open and unlock if not
            try {
                if (!logonCore.isStoreAvailable()) {
                    logonCore.createStore(null, false);
                }
            } catch (LogonCoreException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            LogManager.writeLogError(this.getClass().getSimpleName() + ".initLogonCore: " + e.getMessage());
        }
    }*/

    /**
     * This method user credential saved into shared preferences.
     */
    private void onSaveConfig(com.arteriatech.mutils.registration.RegistrationModel registrationModel, Connection newConnection) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
       /* LogonCoreContext lgCtx = LogonCore.getInstance().getLogonContext();
        String mStrBackEndUser = "";
        String mStrBackEndPWD = "";
        String appConnID = "";
        try {
            appConnID = LogonCore.getInstance().getLogonContext()
                    .getConnId();
            mStrBackEndUser = lgCtx.getBackendUser();
            mStrBackEndPWD = lgCtx.getBackendPassword();
        } catch (LogonCoreException e) {
            e.printStackTrace();
        }*/
        editor.putString(KEY_username, loginUser_Text);
        editor.putString(KEY_password, login_pwd);
        editor.putString(KEY_serverHost, registrationModel.getServerText());
        editor.putString(KEY_serverPort, registrationModel.getPort());
        editor.putString(KEY_securityConfig, registrationModel.getSecConfig());
        editor.putString(KEY_appID, registrationModel.getAppID());
        editor.putString(KEY_FARMID, registrationModel.getFormID());
        editor.putString(KEY_SUFFIX, registrationModel.getSuffix());
        editor.putBoolean(KEY_ISHTTPS, registrationModel.getHttps());
        editor.putString(KEY_appConnID, newConnection.getApplicationConnectionId());
        editor.putString(KEY_appEndPoint, "");
        editor.putString(KEY_pushEndPoint, "");
        editor.putString(KEY_SalesPersonName, "");
        editor.putString(KEY_SalesPersonMobileNo, "");
        editor.putBoolean(KEY_isPasswordSaved, true);
        editor.putBoolean(KEY_isDeviceRegistered, true);
        editor.putBoolean(KEY_isFirstTimeReg, true);
        editor.putBoolean(KEY_isForgotPwdActivated, false);
        editor.putBoolean(KEY_isManadtoryUpdate, true);
        editor.putBoolean(KEY_isUserIsLocked, false);
        editor.putString(KEY_ForgotPwdOTP, "");
        editor.putString(KEY_ForgotPwdGUID, "");
        editor.putString(KEY_isFOSUserRole, "");
        editor.putInt(KEY_MaximumAttemptKey, 0);
        editor.putInt(KEY_VisitSeqId, 0);
        editor.putString(KEY_BirthDayAlertsDate, UtilConstants.getDate1());
        editor.apply();
    }

    /**
     * This method clear user credential.
     */
    private void onClear() {
        if (ilUserName != null && ilPassword != null) {
            ilUserName.setErrorEnabled(false);
            ilPassword.setErrorEnabled(false);
        }
        txtusername.setText("");
        txtLoginPassword.setText("");
        txtusername.setFocusable(true);
        txtusername.requestFocus();
    }

    /**
     * This method navigates to AboutUs activity.
     */
    private void support() {
        Intent intent = new Intent(UtilRegistrationActivity.this, SupportActivity.class);
        intent.putExtra(UtilConstants.RegIntentKey, registrationModel);
        intent.putExtra(EXTRA_IS_FROM_REGISTRATION, true);
        if (bundleExtra != null)
            intent.putExtra(EXTRA_BUNDLE_REGISTRATION, bundleExtra);
        startActivity(intent);
    }

    public boolean onDeviceReg() {
        // create LogonCoreContext
        // this is used for registration

       /* LogonCoreContext context = null;
        try {
            context = logonCore.getLogonContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // set configuration
        try {
            context.setHost(registrationModel.getServerText());
            context.setPort(Integer.parseInt(registrationModel.getPort()));
            context.setHttps(registrationModel.getHttps());
            if (!registrationModel.getFormID().equalsIgnoreCase("")) {
                context.setFarmId(registrationModel.getFormID());
                context.setResourcePath(registrationModel.getSuffix());
            }
            context.setSecurtityConfig(registrationModel.getSecConfig());
            context.setBackendUser(loginUser_Text);
            context.setBackendPassword(login_pwd);
        } catch (LogonCoreException e) {
            e.printStackTrace();
        }

        try {
            // call registration
            logonCore.register(context);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        dataServiceException = null;
        String requestUri = getAppURL(registrationModel.getServerText(), registrationModel.getPort(), registrationModel.getAppID(), registrationModel.getHttps());
        final UsernamePasswordToken token = new UsernamePasswordToken(loginUser_Text, login_pwd);
        HttpConversationManager conversationManager = new CommonAuthFlowsConfigurator(mContext).supportBasicAuthUsing(new UsernamePasswordProvider() {
            @Override
            public Object onCredentialsNeededUpfront(ISendEvent event) {
                return token;
            }

            @Override
            public Object onCredentialsNeededForChallenge(IReceiveEvent event) {
                return token;
            }
        }).configure(new HttpConversationManager(mContext));

        SDKHttpHandler sdkHttpHandler = new SDKHttpHandler(mContext, conversationManager);
        AndroidSystem.setContext(mContext);
        OnlineODataProvider registrationProvider = new OnlineODataProvider("SmpClient", requestUri);
        registrationProvider.getNetworkOptions().setHttpHandler(sdkHttpHandler);
        registrationProvider.getServiceOptions().setCheckVersion(false);
        SmpClient smpClient = new SmpClient(registrationProvider);
        Connection newConnection = new Connection();
        newConnection.setDeviceType("Android");
        newConnection.setDefaultValues();
        try {
            smpClient.createEntity(newConnection);
            onSaveConfig(registrationModel, newConnection);
            return true;
        } catch (DataServiceException e) {
            e.printStackTrace();
            dataServiceException = e;

        }
        return false;

    }

    /**
     * This method register values into server.
     */
    private void onRegister(final Context mContext) {
        if (!getValues()) {
            String userName = txtusername.getText().toString();
            mCurrentAttempt = Collections.frequency(userList, userName);
            userList.add(userName);
            if ((mCurrentAttempt + 1) <= totalAttempt) {
                requestOnline();
            } else {
                UtilConstants.showAlert("[" + UtilConstants.ERROR_CODE_REGISTRATION_USER_LOCKED + "] " + getString(R.string.wrong_psw_error_msg_3, String.valueOf(totalAttempt)), UtilRegistrationActivity.this);
            }
        }
    }

    private void requestOnline() {
        if (UtilConstants.isNetworkAvailable(UtilRegistrationActivity.this)) {
            progressDialog = UtilConstants.showProgressDialogs(UtilRegistrationActivity.this, getString(R.string.register_with_server_plz_wait), false);
            loginUser_Text = txtusername.getText().toString().trim();
            login_pwd = txtLoginPassword.getText().toString().trim();
            new AsynTaskRegistration().execute();
        } else {
            UtilConstants.dialogBoxWithCallBack(UtilRegistrationActivity.this, "", getString(R.string.reg_no_network_conn, UtilConstants.ERROR_CODE_NETWORK), getString(R.string.network_retry), getString(R.string.cancel), false, new DialogCallBack() {
                @Override
                public void clickedStatus(boolean clickedStatus) {
                    if (clickedStatus) {
                        requestOnline();
                    }
                }
            });
            LogManager.writeLogError(getString(R.string.reg_no_network_conn, UtilConstants.ERROR_CODE_NETWORK));
        }
    }

    /**
     * This method checks weather fill all required fields or not.
     */
    private boolean getValues() {
        int isValidMandotry = 0;
        String userName = txtusername.getText().toString();
        String password = txtLoginPassword.getText().toString();
        if (ilUserName != null) {
            ilUserName.setErrorEnabled(false);
            if (TextUtils.isEmpty(userName)) {
                isValidMandotry = 1;
                ilUserName.setErrorEnabled(true);
                ilUserName.setError(getString(R.string.validation_plz_enter_user_name));
            } else {
                boolean areSpaces = checkIfSpaces(userName);
                if (areSpaces) {
                    isValidMandotry = 3;
                    ilUserName.setErrorEnabled(true);
                    ilUserName.setError(getString(R.string.validation_user_name_space));
                }
            }
        } else {
            txtusername.setBackground(getResources().getDrawable(R.drawable.edit_bg));
            if (TextUtils.isEmpty(userName)) {
                isValidMandotry = 1;
                txtusername.setBackground(getResources().getDrawable(R.drawable.edit_error_bg));
            } else {
                boolean areSpaces = checkIfSpaces(userName);
                if (areSpaces) {
                    isValidMandotry = 3;
                    txtusername.setBackground(getResources().getDrawable(R.drawable.edit_error_bg));
                }
            }
        }
        if (ilPassword != null) {
            ilPassword.setErrorEnabled(false);
            if (TextUtils.isEmpty(password)) {
                isValidMandotry = 1;
                ilPassword.setErrorEnabled(true);
                ilPassword.setError(getString(R.string.validation_plz_enter_psw));
            } else {
                boolean areSpaces = checkIfSpaces(password);
                if (areSpaces) {
                    ilPassword.setErrorEnabled(true);
                    ilPassword.setError(getString(R.string.validation_psw_space));
                    isValidMandotry = 3;
                }
            }
        } else {
            txtLoginPassword.setBackground(getResources().getDrawable(R.drawable.edit_bg));
            if (TextUtils.isEmpty(password)) {
                isValidMandotry = 1;
                txtLoginPassword.setBackground(getResources().getDrawable(R.drawable.edit_error_bg));
            } else {
                boolean areSpaces = checkIfSpaces(password);
                if (areSpaces) {
                    isValidMandotry = 3;
                    txtLoginPassword.setBackground(getResources().getDrawable(R.drawable.edit_error_bg));
                }
            }
        }
        return isValidMandotry != 0;
    }

    /**
     * This method checks spaces available or not.
     */
    public boolean checkIfSpaces(String str) {
        boolean result = false;
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(str);
        result = matcher.find();
        return result;

    }

    private void closeProgDialog() {
        try {
            UtilConstants.hideProgressDialog(progressDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void diaplyErrorMsg(final String err_msg, final int err_code) {

        String mStrAttemptText = "";
        String errorMsg = err_msg;
        if (err_code == UtilConstants.ERROR_UN_AUTH) {
            mCurrentAttempt++;
            mStrAttemptText = getString(R.string.attempt) + " " + mCurrentAttempt + "/" + totalAttempt + "";
            if (mCurrentAttempt >= totalAttempt) {
                errorMsg = "[" + UtilConstants.ERROR_CODE_REGISTRATION_USER_LOCKED + "] " + getString(R.string.wrong_psw_error_msg_3, String.valueOf(totalAttempt));//String.format(getString(R.string.wrong_psw_error_msg_3),String.valueOf(totalAttempt));
            } else {
                int numberOfAttempt = totalAttempt - mCurrentAttempt;
                String stAtmt = " attempts";
                if (numberOfAttempt == 1) {
                    stAtmt = " attempt";
                }
                errorMsg = "[" + err_code + "] " + getString(R.string.error_un_autorized, String.valueOf(numberOfAttempt) + stAtmt);
            }
        }
        LogManager.writeLogError(errorMsg);
        UtilConstants.showAlertWithHeading(errorMsg, UtilRegistrationActivity.this, mStrAttemptText);

    }

    @SuppressLint("LongLogTag")
    private class AsynTaskRegistration extends AsyncTask<Void, Boolean, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return onDeviceReg();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            closeProgDialog();
            if (dataServiceException == null) {
                if (registrationModel.getRegisterSuccessActivity() != null) {
                    try {
                        Intent intentNavChangePwdScreen = new Intent(UtilRegistrationActivity.this, registrationModel.getRegisterSuccessActivity());
                        intentNavChangePwdScreen.putExtra(EXTRA_IS_FROM_REGISTRATION, true);
                        if (bundleExtra != null) {
                            intentNavChangePwdScreen.putExtra(EXTRA_BUNDLE_REGISTRATION, bundleExtra);
                        }
                        intentNavChangePwdScreen.putExtra(UtilConstants.RegIntentKey, registrationModel);
                        startActivity(intentNavChangePwdScreen);
                        try {
                            Toast.makeText(mContext, mContext.getString(R.string.registration_success), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else {
                String err_msg = UtilConstants.getErrorMsg(dataServiceException.getStatus(), UtilRegistrationActivity.this);
                diaplyErrorMsg(err_msg, dataServiceException.getStatus());
            }
        }
    }
}

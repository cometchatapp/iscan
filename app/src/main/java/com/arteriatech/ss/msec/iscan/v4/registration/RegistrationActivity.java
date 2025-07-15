package com.arteriatech.ss.msec.iscan.v4.registration;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.arteriatech.mutils.camera.CameraXActivity;
import com.arteriatech.mutils.initialPassword.InitialPasswordActivity;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.mutils.log.TraceLog;
import com.arteriatech.mutils.registration.MainMenuBean;
import com.arteriatech.mutils.registration.RegistrationModel;
import com.arteriatech.ss.msec.iscan.v4.BuildConfig;
import com.arteriatech.ss.msec.iscan.v4.DeviceIDUtils;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.asyncTask.RefreshAsyncTask;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.common.MSFAApplication;
import com.arteriatech.ss.msec.iscan.v4.common.MyUtils;
import com.arteriatech.ss.msec.iscan.v4.databinding.ActivityRegistrationNewBinding;
import com.arteriatech.ss.msec.iscan.v4.facerecognation.MultipartUtility;
import com.arteriatech.ss.msec.iscan.v4.fusedlocation.FusedLocationUtil;
import com.arteriatech.ss.msec.iscan.v4.interfaces.AsyncTaskCallBack;
import com.arteriatech.ss.msec.iscan.v4.log.ErrorCode;
import com.arteriatech.ss.msec.iscan.v4.log.LogActivity;
import com.arteriatech.ss.msec.iscan.v4.mbo.AttendanceBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.ErrorBean;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.Operation;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack;
import com.arteriatech.ss.msec.iscan.v4.mutils.location.LocationGeoInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.location.LocationInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.location.LocationModel;
import com.arteriatech.ss.msec.iscan.v4.mutils.location.LocationUtils;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.SalesOrderActivity;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity;
import com.arteriatech.ss.msec.iscan.v4.mutils.sync.SyncHistoryDB;
import com.arteriatech.ss.msec.iscan.v4.mutils.sync.SyncHistoryModel;

import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.store.OnlineManager;
import com.arteriatech.ss.msec.iscan.v4.sync.OutletSQLHelper;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncInfo.RefreshListInterface;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncSelectionActivity;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncUtils;
import com.arteriatech.ss.msec.iscan.v4.ui.BannerParser;
import com.arteriatech.ss.msec.iscan.v4.ui.DialogFactory;
import com.arteriatech.ss.msec.iscan.v4.ui.OnDialogClick;
import com.arteriatech.ss.msec.iscan.v4.utils.OfflineUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
/*import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;*/
import com.sap.client.odata.v4.DataServiceException;
import com.sap.client.odata.v4.OnlineODataProvider;
import com.sap.client.odata.v4.core.AndroidSystem;
import com.sap.client.odata.v4.core.GUID;
import com.sap.client.odata.v4.http.HttpException;
import com.sap.client.odata.v4.http.SDKHttpHandler;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.authflows.CommonAuthFlowsConfigurator;
import com.sap.smp.client.httpc.authflows.UsernamePasswordProvider;
import com.sap.smp.client.httpc.authflows.UsernamePasswordToken;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.httpc.events.ISendEvent;
import com.sap.smp.client.odata.ODataDuration;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.impl.ODataDurationDefaultImpl;
import com.sap.smp.client.smpclient.Connection;
import com.sap.smp.client.smpclient.SmpClient;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.net.ssl.HttpsURLConnection;

import static com.arteriatech.ss.msec.iscan.v4.common.Constants.REPEATABLE_REQUEST_ID;
import static com.arteriatech.ss.msec.iscan.v4.common.Constants.createLogDirectory;
import static com.arteriatech.ss.msec.iscan.v4.common.Constants.getIDPSharedPrefValue;
import static com.arteriatech.ss.msec.iscan.v4.common.Constants.httpErrorCodes;
import static com.arteriatech.ss.msec.iscan.v4.common.MSFAApplication.AW_GEO_FENCING;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.APP_ID;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.APP_ID_RES;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.IS_HTTPS;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.USER_NAME_DEVICE_ID;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.USER_PASSWORD_DEVICE_ID;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.farm_ID;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.port_Text;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.secConfig_Text;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.server_Text;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.server_Text_DR;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.server_Text_default;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.suffix;
import static com.arteriatech.ss.msec.iscan.v4.store.OfflineManager.isOfflineStoreOpen;


public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, UIListener, TextWatcher {
    //final constants
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
    public static final String KEY_appID = "appID";
    public static final String KEY_isPasswordSaved = "isPasswordSaved";
    public static final String KEY_isDeviceRegistered = "isDeviceRegistered";
    public static final String KEY_isFirstTimeReg = "isFirstTimeReg";
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
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
//            Manifest.permission.READ_PHONE_STATE
    };
    private static String[] PERMISSIONS_STORAGE_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
//            Manifest.permission.READ_PHONE_STATE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private int requestPermissionCode = 1;
    HttpsURLConnection connection = null;
    private ProgressDialog pdLoadDialog = null;
    private SharedPreferences sharedPerf;
    //android components
    ProgressDialog allSyncDialog;
    //variables
    public static GUID refguid =null;
    public static RegistrationModel registrationModel;
    private DataServiceException dataServiceException = null;
    private HttpException httpException = null;
    private String errorMsg="";
    private String passwordStatus="",PUserID="";
    private String filename="";
    private String syncHistoryType="";
    private ActivityRegistrationNewBinding binding=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setTheme(R.style.AppThemeNoActionBar);
//        openStore();
        binding = ActivityRegistrationNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registrationModel = new com.arteriatech.mutils.registration.RegistrationModel();
        registrationModel.setAppID(Configuration.APP_ID);
        registrationModel.setHttps(Configuration.IS_HTTPS);
//        registrationModel.setPassword(Configuration.pwd_text);
        registrationModel.setPort(Configuration.port_Text);
        registrationModel.setSecConfig(Configuration.secConfig_Text);
        registrationModel.setServerText(Configuration.server_Text);
        registrationModel.setShredPrefKey(Constants.PREFS_NAME);
        registrationModel.setFormID(Configuration.farm_ID);
        registrationModel.setSuffix(Configuration.suffix);
        registrationModel.setIDPURL(Configuration.IDPURL);
        registrationModel.setExternalTUserName(Configuration.IDPTUSRNAME);
        registrationModel.setExternalTPWD(Configuration.IDPTUSRPWD);
        registrationModel.setAppVersionName("1.0");
        registrationModel.setEmainId("phoenixhelp@britindia.com");
        registrationModel.setPhoneNo("+91 8088482105");
        registrationModel.setEmailSubject("");
        registrationModel.setAppActionBarIcon(R.drawable.ic_britannia_logo);
        registrationModel.setDataVaultFileName(Constants.DataVaultFileName);
        registrationModel.setOfflineDBPath(Constants.offlineDBPath);
        registrationModel.setOfflineReqDBPath(Constants.offlineReqDBPath);
        registrationModel.setIcurrentUDBPath(Constants.icurrentUDBPath);
        registrationModel.setIbackupUDBPath(Constants.ibackupUDBPath);
        registrationModel.setIcurrentRqDBPath(Constants.icurrentRqDBPath);
        registrationModel.setIbackupRqDBPath(Constants.ibackupRqDBPath);
        registrationModel.setDisplayDBReInitMenu(true);
        registrationModel.setDisplayExportDataMenu(true);
        registrationModel.setDisplayExportMenu(true);
        registrationModel.setDisplayImportMenu(true);
        registrationModel.setDisplayExportDataMenu(true);
        registrationModel.setDisplayImportDataMenu(true);
        registrationModel.setDataVaultPassword(Constants.EncryptKey);
        registrationModel.setAlEntityNames(Constants.getEntityNames());
        registrationModel.setRegisterSuccessActivity(SalesOrderActivity.class);
        ArrayList<MainMenuBean> mainMenuBeanArrayList = new ArrayList<>();
        MainMenuBean mainMenuBean = new MainMenuBean();
        mainMenuBean.setActivityRedirect(LogActivity.class);
        mainMenuBean.setMenuImage(R.drawable.ic_log_list);
        mainMenuBean.setMenuName("View");
        mainMenuBeanArrayList.add(mainMenuBean);
        registrationModel.setMenuBeen(mainMenuBeanArrayList);
//        verifyStoragePermissions();
        LogManager.initialize(this, registrationModel);
        String errorLog =  getIntent().getStringExtra(Constants.EXTRA_CRASHED_FLAG);
        if (errorLog!=null&&!errorLog.isEmpty()){
            LogManager.writeLogError(errorLog);
        }
        sharedPerf = getSharedPreferences(Constants.PREFS_NAME, 0);
        if (TextUtils.isEmpty(getSharedPreferences(Constants.PREFS_NAME,0).getString(UtilRegistrationActivity.KEY_username, ""))) {
            SharedPreferences.Editor editor = getSharedPreferences(Constants.PREFS_NAME,0).edit();
            try {
                editor.putInt(Constants.CURRENT_VERSION_CODE, Constants.NewDefingRequestVersion);
                editor.putString(Constants.ActiveHost,server_Text_default);
                editor.putBoolean(Constants.introScreen,true);
                editor.apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        server_Text = getSharedPreferences(Constants.PREFS_NAME,0).getString(Constants.ActiveHost,server_Text_default);
        if (!server_Text.equalsIgnoreCase(server_Text_default) && !server_Text.equalsIgnoreCase(server_Text_DR)) {
            server_Text = server_Text_default;
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME,
                    0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.ActiveHost, server_Text);
            editor.commit();
        }
        registrationModel.setServerText(Configuration.server_Text);
        clearCookies();
        handleOverlaySettings();
        checkStoragePermission();
        initializeUI();

        CheckChromeVersion();
        //AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, this, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE);
//        CheckWebViewVersion();
      /*  SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        String date = settings.getString(Constants.BirthDayAlertsDate, "");
        if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
            clearCookies();
        }*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        String SPName= getSharedPreferences(Constants.PREFS_NAME,0).getString(Constants.SP_NAME, "");
        if (SPName!=null&&!TextUtils.isEmpty(SPName)) {
            try {
                MSFAApplication.setGAFields(this);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            //MSFAApplication.setAnalytics(this,spNo,this.getClass().getSimpleName(),"Login");
        }else {
           // MSFAApplication.setAnalytics(this,MSFAApplication.SP_UID,this.getClass().getSimpleName(),"Registration");
        }
    }

    private void clearCookies() {
        try {
            com.sap.smp.client.httpc.SAPCookieManager.getInstance().getCookieStore().removeAll();
            LogManager.writeLogError("Cookies cleared");
        } catch (Throwable e) {
            LogManager.writeLogError("Clear Cookies Error : "+e.getMessage());
            e.printStackTrace();
        }
    }

    private void checkStoragePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            int location = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int camera = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
//            int telephone = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
//            if (location != PackageManager.PERMISSION_GRANTED || camera != PackageManager.PERMISSION_GRANTED || telephone != PackageManager.PERMISSION_GRANTED) {
            if (location != PackageManager.PERMISSION_GRANTED || camera != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        this,
                        PERMISSIONS_STORAGE_33,
                        REQUEST_EXTERNAL_STORAGE
                );
            }else{
                try {
                    createLogDirectory();
//                    captureLog();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if we have write permission
            int storage = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int location = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int camera = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
//            int telephone = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
//            if (storage != PackageManager.PERMISSION_GRANTED || location != PackageManager.PERMISSION_GRANTED || camera != PackageManager.PERMISSION_GRANTED || telephone != PackageManager.PERMISSION_GRANTED) {
            if (storage != PackageManager.PERMISSION_GRANTED || location != PackageManager.PERMISSION_GRANTED || camera != PackageManager.PERMISSION_GRANTED ) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        this,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }else{
                try {
                    createLogDirectory();
//                    captureLog();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void CheckChromeVersion(){
        try {
            String chromeVersion = Constants.getChromeVersion(this);
            LogManager.writeLogError("Chrome version is : "+chromeVersion);
            String[] versionSplit = null;

            //webview
            String webViewVersion = Constants.getWebViewVersion(this);
            LogManager.writeLogError("WebView version is : "+webViewVersion);
            String[] versionWebSplit = null;
            if (webViewVersion != null) {
                versionWebSplit = webViewVersion.split("\\.");
            }
            //end of web

            if (chromeVersion != null) {
                versionSplit = chromeVersion.split("\\.");
            }
            if (versionSplit != null) {
                if (Integer.parseInt(versionSplit[0])<80) {
                    Constants.navigateToPlayStore("Please update chrome versions to 80 and above ", this,"com.android.chrome");
                }
            }else if (versionWebSplit != null) {
                if (Integer.parseInt(versionWebSplit[0])<80) {
                    Constants.navigateToPlayStore("Please update Web view version to 80 and above ", this,"com.google.android.webview");
                }
            }else if(versionSplit!=null&&versionWebSplit!=null) {
                if (Integer.parseInt(versionSplit[0])<80&&Integer.parseInt(versionWebSplit[0])<80) {
                    Constants.navigateToPlayStore("Please update Chrome and Web view version to 80 and above ", this,"com.android.chrome");
                }
            }
        } catch (Throwable e) {
            LogManager.writeLogError("Chrome version exception : "+e.toString());
            e.printStackTrace();
        }
    }

    private void CheckWebViewVersion(){
        try {
            String webViewVersion = Constants.getWebViewVersion(this);
            LogManager.writeLogError("WebView version is : "+webViewVersion);
            String[] versionSplit = null;
            if (webViewVersion != null) {
                versionSplit = webViewVersion.split("\\.");
            }
            if (versionSplit != null) {
                if (Integer.parseInt(versionSplit[0])<80) {
                    Constants.navigateToPlayStore("Please update web view version to 80 and above ", this,"com.google.android.webview");
                }
            }
        } catch (Throwable e) {
            LogManager.writeLogError("WebView version exception : "+e.toString());
            e.printStackTrace();
        }
    }


    public void initializeUI() {
        try {
            try {
//             prompt();
                if (getSharedPreferences(Constants.PREFS_NAME,0).getBoolean(Constants.PASSWORD_SAVE,false)) {
                    String userName = getSharedPreferences(Constants.PREFS_NAME,0).getString(UtilRegistrationActivity.KEY_username, "");
                    String password = getSharedPreferences(Constants.PREFS_NAME,0).getString(UtilRegistrationActivity.KEY_password, "");
                    if(userName!=null||!TextUtils.isEmpty(userName)){
                        binding.editTextUserName.setText(userName);
                    }
                    if (password!=null||!TextUtils.isEmpty(password)){
                        binding.editTextPassword.setText(password);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            initializeListeners();
            initializeObjects();
            String SPName= getSharedPreferences(Constants.PREFS_NAME,0).getString(Constants.SP_NAME, "");
            if (SPName!=null&&!TextUtils.isEmpty(SPName)) {
//                textViewSPName.setText(SPName);
//                textViewTitle.setText("Login");
//                iv_setting.setVisibility(View.VISIBLE);
//                iv_setting.setColorFilter(ContextCompat.getColor(this, R.color.secondaryColor), android.graphics.PorterDuff.Mode.SRC_IN);
            }else{
//                textViewTitle.setText("Registration");
//                iv_setting.setVisibility(View.GONE);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public void initializeObjects() {
        try {
            Log.e("LOG_INIT",":init");

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void initializeListeners() {
      //  binding.buttonLogin.setOnClickListener(this);
        binding.tvForgot.setOnClickListener(this);
        binding.tvSupport.setOnClickListener(this);
//        binding.tvForgot.setVisibility(View.GONE);
//        iv_setting.setOnClickListener(this);
//        textViewLandscape.setOnClickListener(this);
        binding.textViewDeviceID.setOnClickListener(this);
        try {
            binding.textViewDeviceID.setText("Device ID: "+MSFAApplication.APP_DEVICEID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.editTextUserName.addTextChangedListener(this);
        binding.editTextPassword.addTextChangedListener(this);
    }

    boolean isClickable = false;
    //    String countdown = "";
//    long second = 0;
    String message = "";
    //    long milliSec=2400000;
//    CountDownTimer countDownTimer=null;
    @Override
    public void onClick(View v) {
       /* try {
            switch (v.getId()){
                case R.id.buttonLogin:
                    if (!com.arteriatech.mutils.common.UtilConstants.isGPSEnabled(RegistrationActivity.this)) {
                        new DialogFactory.Alert(RegistrationActivity.this).isAlert(true).setOnDialogClick(new OnDialogClick() {
                            @Override
                            public void onDialogClick(boolean isPositive) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        }).setMessage("Please enable GPS for marking attendance").show();
                        return;
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME,0);
                    if(!sharedPreferences.getBoolean(UtilRegistrationActivity.KEY_isFirstTimeDB,false)){
                        Constants.syncStartTime = UtilConstants.getSyncHistoryddmmyyyyTime();
                    }else {
                        Constants.syncStartTime  = "";
                    }
                    if (TextUtils.isEmpty(getSharedPreferences(Constants.PREFS_NAME,0).getString(UtilRegistrationActivity.KEY_username, ""))) {
                        SharedPreferences.Editor editor = getSharedPreferences(Constants.PREFS_NAME,0).edit();
                        try {
                            editor.putString(Constants.ActiveHost,server_Text_default);
                            editor.apply();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        server_Text = getSharedPreferences(Constants.PREFS_NAME,0).getString(Constants.ActiveHost,server_Text);
                    }
                    Constants.writeLogsToInternalStorage(this,"Initial Registration :\n " +
                            "log -Registration called  \n");

//                    countdown="";
//                    try {
//                        if (countDownTimer != null) {
//                            LogManager.writeLogInfo("Login Timer count Down is cancel");
//                            countDownTimer.cancel();
//                            countDownTimer=null;
//                        }
//                    } catch (Throwable e) {
//                        e.printStackTrace();
//                    }
//                    countDownTimer = new CountDownTimer(milliSec, 1000) {
//
//                        public void onTick(long millisUntilFinished) {
//                            long seconds = milliSec/ 1000 -millisUntilFinished / 1000;
//                            long minutes = ((milliSec / 1000) / 60) -((millisUntilFinished / 1000) / 60)-1;
////                            if(minutes==(seconds/60)){
////                                second=0;
////                            }else{
////                                second++;
////                            }
//
//                            int sec = (int )seconds- (int)minutes*60;
//                            countdown = String.format("%02d",minutes) + ":" + String.format("%02d",sec);
//                            String timeStamp = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm aa", new Date());
//                            if(progressDialog!=null && progressDialog.isShowing()){
////                                progressDialog.setMessage(message+"  \n\n "+timeStamp+"\t\t\t"+countdown);
//                                progressDialog.setTitle(timeStamp);
//                                progressDialog.setMessage(message+"\t\t"+countdown);
//                                progressDialog.show();
//
//                                if (syncHistoryType.equalsIgnoreCase("01")) {
//                                    if (String.format("%02d", minutes).equalsIgnoreCase("10")) {
//                                        progressDialog.dismiss();
//                                        if (asyncTask != null)
//                                            asyncTask.cancel(true);
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(
//                                                RegistrationActivity.this, R.style.MyTheme);
//                                        builder.setTitle(timeStamp);
//                                        builder.setMessage(R.string.error_sync_msg)
//                                                .setCancelable(false)
//                                                .setPositiveButton(
//                                                        R.string.ok,
//                                                        (Dialog1, id) -> {
////                                                            onBackPressed();
//                                                            if (countDownTimer != null) {
//                                                                LogManager.writeLogInfo("Login Timer count Down is cancel");
//                                                                countDownTimer.cancel();
//                                                                countDownTimer = null;
//                                                            }
//                                                        });
//                                        builder.show();
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFinish() {
//
//                        }
//
//                    };
//                    countDownTimer.start();
                    LogManager.writeLogInfo("Login Timer count Down is started");
                    if(!isClickable) {
                        if (ConstantsUtils.isAutomaticTimeZone(RegistrationActivity.this)) {
                            isClickable = true;
                            onRegister();
                        }else{
                            ConstantsUtils.showAutoDateSetDialog(RegistrationActivity.this);
                        }

                    }
                    break;
                case R.id.textViewDeviceID:
                    share();
                    break;
                case R.id.tv_support:
                    Intent support = new Intent(RegistrationActivity.this, com.arteriatech.mutils.registration.SupportActivity.class);
                    registrationModel.setTheme(R.style.AppThemeActivity);
                    ArrayList<MainMenuBean> mainMenuBeanArrayList = new ArrayList<>();
                    MainMenuBean mainMenuBean = new MainMenuBean();
                    mainMenuBean.setActivityRedirect(LogActivity.class);
                    mainMenuBean.setMenuImage(0);
                    mainMenuBean.setMenuName("View");
                    mainMenuBeanArrayList.add(mainMenuBean);
                    registrationModel.setMenuBeen(mainMenuBeanArrayList);
                    support.putExtra(com.arteriatech.mutils.common.UtilConstants.RegIntentKey, registrationModel);
                    startActivity(support);
                    break;
                case R.id.tv_forgot:
                    Intent intent = new Intent(this, ForgetPasswordActivity.class);
                    intent.putExtra("IDPURL", this.registrationModel.getIDPURL());
                    intent.putExtra("IDPUName", this.registrationModel.getExternalTUserName());
                    intent.putExtra("IDPUPassword", this.registrationModel.getExternalTPWD());
                    intent.putExtra("theme", R.style.AppThemeActivity);
                    this.startActivity(intent);
                    break;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new NullPointerException();
        }*/
    }

    private void opensettingsFragment() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME,0);
        String userName = sharedPreferences.getString(com.arteriatech.mutils.registration.UtilRegistrationActivity.KEY_username,"");
        Intent intent = new Intent(this, com.arteriatech.mutils.support.SecuritySettingActivity.class);
        registrationModel.setExtenndPwdReq(true);
        registrationModel.setUpdateAsPortalPwdReq(true);
        registrationModel.setIDPURL(Configuration.IDPURL);
        registrationModel.setExternalTUserName(Configuration.IDPTUSRNAME);
        registrationModel.setExternalTPWD(Configuration.IDPTUSRPWD);
        registrationModel.setUserName(userName);
        intent.putExtra(com.arteriatech.mutils.common.UtilConstants.RegIntentKey, registrationModel);
        startActivity(intent);
    }

    private void clearAppData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "","Unable to register due to connectivity error[403]. \n\n " +
                        "Please follow these steps to try again. \n 1.Click OK to close the app \n 2.Open the app once again and retry" , "", getString(R.string.ok), false, new DialogCallBack() {
                    @Override
                    public void clickedStatus(boolean clickedStatus) {
                        try {
                            Runtime runtime = Runtime.getRuntime();
                            runtime.exec("pm clear " + RegistrationActivity.this.getPackageName() + " HERE");
                            LogManager.writeLogInfo("Clear app data successful");
                        } catch (Throwable e) {
                            LogManager.writeLogInfo("Clear app data failed : "+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    private void clearAppData404() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "","You have not used application for more than 30 days.  App will close now. Open and Register again" , "", getString(R.string.ok), false, new DialogCallBack() {
                    @Override
                    public void clickedStatus(boolean clickedStatus) {
                        try {
                            Runtime runtime = Runtime.getRuntime();
                            runtime.exec("pm clear " + RegistrationActivity.this.getPackageName() + " HERE");
                            LogManager.writeLogInfo("Clear app data successful");
                        } catch (Throwable e) {
                            LogManager.writeLogInfo("Clear app data failed : "+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        /*if (s==binding.editTextUserName.getEditableText()){
            if (s.length()==0){
                binding.editTextUserName.setTypeface(Typeface.MONOSPACE);
            }else{
                binding.editTextUserName.setTypeface(Typeface.SANS_SERIF);
            }

        }else */if(s==binding.editTextPassword.getEditableText()){
            if (s.length()==0){
                binding.editTextPassword.setTypeface(Typeface.MONOSPACE);
            }else{
                binding.editTextPassword.setTypeface(Typeface.SANS_SERIF);
            }
        }
    }

    private String selectedImagePath="";
    private String mimeType="";
    private String mStrPopUpText="";
    boolean isImageCaptured;
    private int mLongBitmapSize=0;
    public static Bitmap bitMapImage = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantsUtils.CAMERA_REQUEST_CODE) {
            try {
                if (data != null) {
//                    if(refguid==null){
                    refguid =  GUID.newRandom();
//                    }
                    SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_CAMERA_CAPUTURED,Constants.StartSync,refguid.toString36().toUpperCase());
                    String fileName = data.getStringExtra(CameraXActivity.CAMERA_X_FILE_NAME);
                    this.filename = fileName;
                    LogManager.writeLogDebug("Face Recognition onActivityResult called with fileName : "+fileName);
                    try {
                        File f = new File(Objects.requireNonNull(getExternalFilesDir(null)).toString());
                        LogManager.writeLogDebug("Face Recognition f size : "+f.length());
                        for (File temp : Objects.requireNonNull(f.listFiles())) {
                            if (temp.getName().equals(fileName)) {
                                LogManager.writeLogDebug("Face Recognition temp fileName : "+temp.getName());
                                f = temp;
                                break;
                            }
                        }

//                        final Bitmap bitMap = Compressor.getDefault(this).compressToBitmap(f);
                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        final Bitmap bitMap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                        bitMapImage= bitMap;
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        final byte[] imageInByte = stream.toByteArray();
                        mLongBitmapSize = imageInByte.length;
                        LogManager.writeLogDebug("Face Recognition bitmap file length : "+imageInByte.length);
//                        File fileName1= Constants.SaveImageInDevice(fileName, bitMap);
                        File fileName1= Constants.SaveImageInDevice1(this,fileName, bitMap);
                        selectedImagePath = fileName1.getPath();
                        LogManager.writeLogDebug("Face Recognition file path after save in device : "+selectedImagePath);
                        //mime
                        String strMimeType = MimeTypeMap.getFileExtensionFromUrl(selectedImagePath);
                        mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(strMimeType);
                        if (UtilConstants.isNetworkAvailable(this)) {
                            LogManager.writeLogDebug("Face Recognition before method call : "+fileName1);
                            postFaceRecognitionImage1(fileName1);
                        }else {
                            UtilConstants.dialogBoxWithCallBack(this, "", "Face recognition cannot be performed. Please check your internet connectivity", getString(R.string.OK),"", false, new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                    isClickable = false;
                                    if (clickedStatus) {
//                            requestOnline();
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                    }
                }else{
                    SharedPreferences.Editor editor = sharedPerf.edit();
                    editor.putBoolean(Constants.FACE_REINIT, true);
                    editor.apply();
                }
            }catch(Throwable e){
                e.printStackTrace();
            }


        }
        else if (requestCode == 01230) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    handleOverlaySettings();
                }
            }
        }
    }

    @SuppressLint("LongLogTag")
    private class AsynTaskRegistration extends AsyncTask<Void, Void, Boolean> {
        private boolean isRegister = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                new DialogFactory.Progress(RegistrationActivity.this).setProgressMessage("Registering user. Please wait...");
                message = "Registering user. Please wait...";;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        public AsynTaskRegistration(boolean isRegister) {
            this.isRegister=isRegister;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return onDeviceReg(isRegister);
            } catch (Throwable e) {
                e.printStackTrace();
                Constants.writeLogsToInternalStorage(getApplicationContext(),"Initial Registration :\n " +
                        "log -Registration failed with error \n"+e.toString());
                runOnUiThread(() -> {
                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                });
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            isClickable=false;
            if (dataServiceException == null&&result) {
                runOnUiThread(() -> new DialogFactory.Progress(RegistrationActivity.this)
                        .setProgressMessage("Fetching Role Info. Please wait..."));
                message = "Fetching Role Info. Please wait...";;
                checkInitialPasswordStatus();
//                try {
//                    BannerParser.downloadBannerFile(RegistrationActivity.this);
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                }
                //storeBannerImages(RegistrationActivity.this,true,"");
                /*ConstantsUtils.getRollId(RegistrationActivity.this, new AsyncTaskCallBack() {
                    @Override
                    public void onStatus(boolean status, String values) {
                        try {
                            if (status) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new DialogFactory.Progress(RegistrationActivity.this)
                                                .setProgressMessage("Fetching Authorization Info. Please wait...");
                                        message = "Fetching Authorization Info. Please wait...";
                                        ;
                                    }
                                });
                                ConstantsUtils.getUserAuthInfo(RegistrationActivity.this, new AsyncTaskCallBack() {
                                    @Override
                                    public void onStatus(boolean status, String values) {
                                        if (status) {
                                            SyncUtils.checkAndCreateDB(RegistrationActivity.this);
                                            new OpenOfflineStoreAsync(false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        } else {
                                            try {
                                                runOnUiThread(() -> new DialogFactory.Progress(RegistrationActivity.this).hide());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if(values.contains("403")){
                                                clearAppData();
                                            }else {
                                                runOnUiThread(() -> Constants.showAlert(values, RegistrationActivity.this));
                                            }
                                        }
                                    }
                                });
                                *//*SyncUtils.checkAndCreateDB(RegistrationActivity.this);
                                Log.e("ROLE",values);
                                new OpenOfflineStoreAsync(false).execute();*//*

                            } else {
                                try {
                                    runOnUiThread(() -> new DialogFactory.Progress(RegistrationActivity.this).hide());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if(values.contains("403")){
                                    clearAppData();
                                }else {
                                    runOnUiThread(() -> Constants.showAlert(values, RegistrationActivity.this));
                                }
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });*/
            }
            else if(httpException!=null){
                new DialogFactory.Progress(RegistrationActivity.this).hide();
                UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "", getString(R.string.network_error_message), getString(R.string.network_retry), getString(R.string.cancel), false, new DialogCallBack() {
                    @Override
                    public void clickedStatus(boolean clickedStatus) {
                        if (clickedStatus) {
                            requestOnline(isRegister);
                        }
                    }
                });
            } else {
                try {
                    String err_msg = null;
                    if (dataServiceException != null) {
                        err_msg = com.arteriatech.mutils.common.UtilConstants.getErrorMsg(RegistrationActivity.this.dataServiceException.getStatus(), RegistrationActivity.this);
                        LogManager.writeLogError("Registration failed  due to "+err_msg);
                        diaplyErrorMsg(err_msg, RegistrationActivity.this.dataServiceException.getStatus());
                    } else {
                        new DialogFactory.Progress(RegistrationActivity.this).hide();
//                        UtilRegistrationActivity.this.diaplyErrorMsg(UtilRegistrationActivity.this.getString(R.string.user_auth_error), 401);
                        userIDandPasswordcheck();
                    }
                } catch (Throwable var3) {
                    var3.printStackTrace();
                }
            }
        }
    }
    private int mCurrentAttempt = 0;
    private int totalAttempt = 3;
    String finalErrorMsg = "";
    private void diaplyErrorMsg(String err_msg, int err_code) {

        String mStrAttemptText = "";
        String errorMsg = err_msg;
        if (err_code == UtilConstants.ERROR_UN_AUTH) {
            try {
                try {
                    com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, binding.editTextUserName.getText().toString().trim(), binding.editTextPassword.getText().toString().trim(), Configuration.APP_ID, jsonObject -> {
                        runOnUiThread(() -> {
                            new DialogFactory.Progress(RegistrationActivity.this).hide();
//                            com.arteriatech.mutils.common.UtilConstants.passwordStatusErrorMessage(
//                                    RegistrationActivity.this,
//                                    jsonObject,
//                                    USER_NAME_DEVICE_ID, Configuration.IDPURL, Configuration.IDPTUSRNAME, Configuration.IDPTUSRPWD, Configuration.APP_ID);
                            Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject,registrationModel,binding.editTextUserName.getText().toString().trim());
                        });
                    });
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (Throwable e) {
                e.printStackTrace();
                new DialogFactory.Progress(RegistrationActivity.this).hide();
            }
        }
        else if(err_code==403){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                    clearAppData();
                }
            });
        } else{
            new DialogFactory.Progress(RegistrationActivity.this).hide();
            Constants.showAlert("Registration failed with "+err_msg+" Please retry",this);
        }
        LogManager.writeLogError(errorMsg);
    }
    public boolean onDeviceReg(boolean isRegister) {
        try {
            dataServiceException = null;
            String requestUri = getAppURL(server_Text, port_Text, APP_ID, IS_HTTPS);
            final UsernamePasswordToken token = new UsernamePasswordToken(binding.editTextUserName.getText().toString().trim(), binding.editTextPassword.getText().toString().trim());
            CommonAuthFlowsConfigurator commonAuthFlowsConfigurator = new CommonAuthFlowsConfigurator(this);
            commonAuthFlowsConfigurator.supportBasicAuthUsing(new UsernamePasswordProvider() {
                @Override
                public Object onCredentialsNeededUpfront(ISendEvent event) {
                    return token;
                }

                @Override
                public Object onCredentialsNeededForChallenge(IReceiveEvent event) {
                    return token;
                }
            });
            HttpConversationManager conversationManager = commonAuthFlowsConfigurator.configure(new HttpConversationManager(this));
            SDKHttpHandler sdkHttpHandler = new SDKHttpHandler(this, conversationManager);
            AndroidSystem.setContext(this);
            OnlineODataProvider registrationProvider = new OnlineODataProvider("SmpClient", requestUri);
            registrationProvider.getNetworkOptions().setHttpHandler(sdkHttpHandler);
            registrationProvider.getServiceOptions().setCheckVersion(false);
            SmpClient smpClient = new SmpClient(registrationProvider);
            Connection newConnection = new Connection();
            newConnection.setDeviceType("Android");
            newConnection.setDeviceSubType(Build.VERSION.RELEASE);
            newConnection.setApplicationVersion ("1.0");
            newConnection.setCustomCustom1 ("1.0");
            String brandModel = Build.BRAND+" "+Build.MODEL;
            newConnection.setDeviceModel (brandModel);
            newConnection.setDefaultValues();
            if (com.arteriatech.mutils.common.UtilConstants.isNetworkAvailable(this)) {
                try {
                    try {
                        LogManager.writeLogError("Initial Registration :\n " +
                                "log -Registration called with create connection");
                        smpClient.createEntity(newConnection);
                        this.onSaveConfig(newConnection,smpClient,isRegister);
                        LogManager.writeLogError("Initial Registration :\n " +
                                "log -Registration create connection successful");
                        return true;
                    } catch (DataServiceException var10) {
                        validateMetadata(Configuration.APP_ID,binding.editTextUserName.getText().toString(),binding.editTextPassword.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LogManager.writeLogError("Registration failed due "+var10.getMessage());
                            }
                        });
                        var10.printStackTrace();
                        this.dataServiceException = var10;
                    }
                } catch (Throwable e) {
                    errorMsg = e.getMessage();
                    LogManager.writeLogError("Registration failed due "+e.getMessage());
                    e.printStackTrace();
                    Constants.writeLogsToInternalStorage(getApplicationContext(),"Initial Registration :\n " +
                            "log -Registration failed with error \n"+e.toString());
                }

                return false;
            } else {
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        com.arteriatech.mutils.common.UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "", RegistrationActivity.this.getString(R.string.network_error_message), RegistrationActivity.this.getString(R.string.network_retry), RegistrationActivity.this.getString(R.string.cancel), false, new com.arteriatech.mutils.interfaces.DialogCallBack() {
                            public void clickedStatus(boolean clickedStatus) {
                                if (clickedStatus) {
                                    requestOnline(isRegister);
                                }

                            }
                        });
                    }
                });
                LogManager.writeLogError(this.getString(R.string.network_error_message));
                return false;
            }

        } catch (Throwable e) {
            e.printStackTrace();
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
//              .fragment(applicationName);
        return builder.build().toString();
    }
    /**
     * This method register values into server.
     */
    private void onRegister() {
        if(!getSharedPreferences(Constants.PREFS_NAME,0).getBoolean(UtilRegistrationActivity.KEY_isFirstTimeReg, false)){
            LogManager.writeLogInfo("Login Status : Registration");
            if (UtilConstants.isNetworkAvailable(this)) {
                if (binding.editTextUserName!=null&&!TextUtils.isEmpty(binding.editTextUserName.getText().toString().trim())&&binding.editTextPassword!=null&&!TextUtils.isEmpty(binding.editTextPassword.getText().toString().trim())) {
                    new DialogFactory.Progress(RegistrationActivity.this).setTitle("Registration").setMessage("Verifying server. Please wait...").setTheme(R.style.MaterialAlertDialog).show();
                    message = "Verifying server. Please wait...";
                    USER_NAME_DEVICE_ID = binding.editTextUserName.getText().toString().trim();
                    USER_PASSWORD_DEVICE_ID = binding.editTextPassword.getText().toString().trim();
                    try {
                        if (UtilConstants.isNetworkAvailable(this)) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    serverRegister();
                                }
                            }).start();
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                                    new DialogFactory.Alert(RegistrationActivity.this).setMessage(getString(R.string.network_error_message)).setOnDialogClick(new OnDialogClick() {
                                        @Override
                                        public void onDialogClick(boolean isPositive) {
                                            if (isPositive){
                                                try {
                                                    try {
                                                        serverRegister();
                                                    } catch (Throwable e) {
                                                        e.printStackTrace();
                                                    }
                                                } catch (Throwable e) {
                                                    e.printStackTrace();
                                                }
                                            }else{
                                                isClickable =false;
                                            }
                                        }
                                    }).setNegativeButton(getString(R.string.cancel)).setPositiveButton(getString(R.string.network_retry)).show();                                }

                            });
                            isClickable = false;
                            LogManager.writeLogError(getString(R.string.network_error_message));
                        }
                    } catch (Throwable e) {
                        isClickable = false;
                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                        runOnUiThread(() -> Constants.showAlert(e.toString(), RegistrationActivity.this));
                    }
//                        requestOnline();
                }else{
//                        textViewError.setText("Login failed.");
                    if (binding.editTextUserName!=null&&TextUtils.isEmpty(binding.editTextUserName.getText().toString().trim())){
                        Constants.showAlert(getString(R.string.enter_valid_username),this);
                    }else if (binding.editTextPassword!=null&&TextUtils.isEmpty(binding.editTextPassword.getText().toString().trim())){
                        Constants.showAlert(getString(R.string.enter_valid_password),this);
                    }
                    isClickable = false;
                }
            }else{
                isClickable = false;
                Constants.writeLogsToInternalStorage(this,"Initial Registration :\n " +
                        "log -Registration called with No network \n");
                UtilConstants.dialogBoxWithCallBack(this, "", getString(R.string.network_error_message), getString(R.string.network_retry), getString(R.string.cancel), false, new DialogCallBack() {
                    @Override
                    public void clickedStatus(boolean clickedStatus) {
                        isClickable = false;
                        if (clickedStatus) {
//                            requestOnline();
                        }
                    }
                });
            }
        }else{
            boolean isValidUserName =true;
            boolean isValidPassword =true;
            LogManager.writeLogInfo("Login Status : Login");
            String userName = getSharedPreferences(Constants.PREFS_NAME,0).getString(UtilRegistrationActivity.KEY_username, "");
            String password = getSharedPreferences(Constants.PREFS_NAME,0).getString(UtilRegistrationActivity.KEY_password, "");
            if(userName==null||!userName.equalsIgnoreCase(binding.editTextUserName.getText().toString().trim())){
                isValidUserName =false;
            }if (password==null||!TextUtils.equals(password,binding.editTextPassword.getText().toString().trim())){
                isValidPassword =false;
            }
            if (isValidUserName&&isValidPassword){
                if (!getSharedPreferences(Constants.PREFS_NAME,0).getBoolean(Constants.DEVICE_ID_CHECK,true)){
                    if (UtilConstants.isNetworkAvailable(this)) {
                        new DialogFactory.Progress(RegistrationActivity.this)
                                .setMessage("Verifying server. Please wait...").setTitle("Registration").setTheme( R.style.ProgressDialogTheme).show();
                        message = "Verifying server. Please wait...";
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                serverRegister();
                            }
                        }).start();

//                           initDeviceIDValidation(userName,password,false);
                    } else {
                        isClickable = false;
                        UtilConstants.dialogBoxWithCallBack(this, "", getString(R.string.network_error_message), getString(R.string.network_retry), getString(R.string.cancel), false, new DialogCallBack() {
                            @Override
                            public void clickedStatus(boolean clickedStatus) {
                                if (clickedStatus) {
                                    try {
                                        serverRegister();
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
//                                       initDeviceIDValidation(userName,password,false);
                                }else {
                                    isClickable = false;
                                }
                            }
                        });
                        LogManager.writeLogError(getString(R.string.network_error_message));
                    }
                }else{
                    loginAvailableServer();
                }
//                    initStore();
            }else{
                isClickable = false;
//                    textViewError.setText("Login failed.");
                if (!isValidUserName){
                    Constants.showAlert(getString(R.string.enter_valid_username),this);
                }else    {
                    Constants.showAlert(getString(R.string.enter_valid_password),this);
                }
            }
        }

    }
    private void initStore(boolean isPing){
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(isPing){
                        new DialogFactory.Progress(RegistrationActivity.this)
                                .setProgressMessage("Fetching Role Info. Please wait...");
                        message = "Fetching Role Info. Please wait...";
                    }else {
                        new DialogFactory.Progress(RegistrationActivity.this).setTitle("Registration").setMessage("Fetching Role Info. Please wait...").setTheme(R.style.MaterialAlertDialog).show();
                        message = "Fetching Role Info. Please wait...";
                        ;
                    }
                }
            });

            ConstantsUtils.getRollId(RegistrationActivity.this, new AsyncTaskCallBack() {
                @Override
                public void onStatus(boolean status, String values) {
                    try {
                        if (status) {
                            isClickable = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new DialogFactory.Progress(RegistrationActivity.this)
                                            .setProgressMessage("Fetching Authorization Info. Please wait...");
                                    message = "Fetching Authorization Info. Please wait...";;
                                }
                            });
                            ConstantsUtils.getUserAuthInfo(RegistrationActivity.this, new AsyncTaskCallBack() {
                                @Override
                                public void onStatus(boolean status, String values) {
                                    if (status) {
                                        isClickable = false;
                                        SyncUtils.checkAndCreateDB(RegistrationActivity.this);
                                        new OpenOfflineStoreAsync(false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    } else {
                                        isClickable = false;
                                        try {
                                            runOnUiThread(() -> new DialogFactory.Progress(RegistrationActivity.this).hide());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        if(values.contains("403")){
                                            clearAppData();
                                        }else {
                                            runOnUiThread(() -> Constants.showAlert(getJSONError(values), RegistrationActivity.this));
                                        }
                                    }
                                }
                            });
                        } else {
                            try {
                                isClickable = false;
                                runOnUiThread(() -> new DialogFactory.Progress(RegistrationActivity.this).hide());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(values.contains("403")){
                                clearAppData();
                            }else {
                                runOnUiThread(() -> Constants.showAlert(values, RegistrationActivity.this));
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String err_msg = "";
    public void validateUserRole( String APP_ID, @NonNull final String userName, @NonNull final String password, String fetchURL, Context context){
        new Thread(() -> {
            HttpsURLConnection connection = null;
            int responseCode = 0;
            LogManager.writeLogInfo("User Management check for "+binding.editTextUserName.getText().toString());
            try {
                URL url =  new URL(fetchURL);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setReadTimeout(Configuration.connectionTimeOut);
                connection.setConnectTimeout(Configuration.connectionTimeOut);
                String userCredentials = userName + ":" + password;
                String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8), 2);
                connection.setRequestProperty("Authorization", basicAuth);
                connection.setRequestProperty("x-smp-appid", APP_ID);
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setDefaultUseCaches(false);
                connection.setAllowUserInteraction(false);
                connection.connect();
                LogManager.writeLogInfo("User Management Qry "+fetchURL);
                responseCode = connection.getResponseCode();
                connection.getResponseMessage();
                String resultJson = "";
                InputStream stream = null;
                if(responseCode==200){
                    LogManager.writeLogInfo("User Management check request success "+responseCode);
                    stream = connection.getInputStream();
                    if (stream != null) {
                        resultJson = DeviceIDUtils.readResponse(stream);
                    }
                    try {
                        JSONObject jsonObj = new JSONObject(resultJson);
                        JSONObject objectD = jsonObj.optJSONObject("d");

                        if(!TextUtils.isEmpty(objectD.toString())){
                            String roleID = objectD.optString("RoleID");
                            LogManager.writeLogError(binding.editTextUserName.getText().toString()+" role id is "+roleID);
                            if(roleID.equalsIgnoreCase("000006")){
                                LogManager.writeLogError(binding.editTextUserName.getText().toString()+" is  authorized to use the application. ");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new DialogFactory.Progress(RegistrationActivity.this)
                                                .setProgressMessage("Fetching user auth. Please wait...");
                                        message = "Fetching user auth. Please wait...";
                                    }
                                });
                                String url1 = MyUtils.getDefaultOnlineQryURL() +"UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27";
                                getUserAuth(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,url1,RegistrationActivity.this);
//                                initDeviceIDValidation(USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,true);
                            }else {
                                isClickable = false;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new DialogFactory.Progress(RegistrationActivity.this).hide();                                        LogManager.writeLogError(binding.editTextUserName.getText().toString()+" is not authorized to use the application. Please contact support team");
                                        UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "",binding.editTextUserName.getText().toString()+" is not authorized to use the application. Please contact support team" , getString(R.string.OK), "", false, new DialogCallBack() {
                                            @Override
                                            public void clickedStatus(boolean clickedStatus) {
                                            }
                                        });
                                    }

                                });

                            }
                        }else {
                            isClickable = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new DialogFactory.Progress(RegistrationActivity.this).hide();                                    LogManager.writeLogError("User management not done. Please contact support team");
                                    UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "","User management not done. Please contact support team" , getString(R.string.OK), "", false, new DialogCallBack() {
                                        @Override
                                        public void clickedStatus(boolean clickedStatus) {
                                        }
                                    });
                                }

                            });

                        }
                    } catch (JSONException e) {
                        isClickable = false;
                        LogManager.writeLogError("User management not done error :"+e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new DialogFactory.Progress(RegistrationActivity.this).hide();                            }

                        });
                        e.printStackTrace();
                    }
                }else if (responseCode == 401){
                    LogManager.writeLogInfo("User Management check request failed : "+responseCode);
                    isClickable = false;
                    try {
                        com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, binding.editTextUserName.getText().toString().trim(), binding.editTextPassword.getText().toString().trim(), Configuration.APP_ID, jsonObject -> {
                            runOnUiThread(() -> {
                                new DialogFactory.Progress(RegistrationActivity.this).hide();
                                int value =jsonObject.optInt("code");
//                                com.arteriatech.mutils.common.UtilConstants.passwordStatusErrorMessage(
//                                        RegistrationActivity.this,
//                                        jsonObject,
//                                        USER_NAME_DEVICE_ID, Configuration.IDPURL, Configuration.IDPTUSRNAME, Configuration.IDPTUSRPWD, Configuration.APP_ID);
                                Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject,registrationModel,binding.editTextUserName.getText().toString().trim());

                            });
                        });
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else if(responseCode == 503){
                    LogManager.writeLogInfo("User Management check request failed : "+responseCode);
                    isClickable = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                            LogManager.writeLogError("Service unavailable [503]. Please contact support team");
                            UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "","Service unavailable. Please contact support team" , getString(R.string.OK), "", false, new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                }
                            });
                        }

                    });
                } else {
                    LogManager.writeLogInfo("User Management check request failed : "+responseCode);
                    isClickable = false;
                    int finalResponseCode = responseCode;
                    err_msg =connection.getResponseMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                            try {
                                Constants.httphashmaperrorcodes();
                                err_msg = httpErrorCodes.get(""+finalResponseCode);
                            } catch (Throwable e) {
                                e.printStackTrace();
                                LogManager.writeLogError("httpErrorCodes error : "+e.getMessage());
                            }
                            LogManager.writeLogError("Unable to fetch user management details due to "+ err_msg);
                            UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "","Unable to fetch user management details due to "+ err_msg +" ["+finalResponseCode+"]", getString(R.string.OK), "", false, new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                }
                            });
                        }

                    });

                }
            } catch (Throwable e) {
                isClickable = false;
                LogManager.writeLogError("Unable to fetch user management details due to error :"+e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                        if (e.getMessage().contains("timeout")) {
                            UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Unable to fetch user management details due to connection timeout[UserProfiles]", getString(R.string.OK), "", false, new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                }
                            });
                        }else {
                            UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Unable to fetch user management details due to poor internet connectivity [UserProfiles]. \n\n" +
                                    "Please check internet and retry", getString(R.string.OK), "", false, new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                }
                            });
                        }
                    }

                });

            }
        }).start();
    }

    public void getUserAuth( String APP_ID, @NonNull final String userName, @NonNull final String password, String fetchURL, Context context){
        new Thread(() -> {
            HttpsURLConnection connection = null;
            int responseCode = 0;
            LogManager.writeLogInfo("User Auth check for "+binding.editTextUserName.getText().toString());
            try {
                URL url =  new URL(fetchURL);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setReadTimeout(Configuration.connectionTimeOut);
                connection.setConnectTimeout(Configuration.connectionTimeOut);
                String userCredentials = userName + ":" + password;
                String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8), 2);
                connection.setRequestProperty("Authorization", basicAuth);
                connection.setRequestProperty("x-smp-appid", APP_ID);
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setDefaultUseCaches(false);
                connection.setAllowUserInteraction(false);
                connection.connect();
                LogManager.writeLogInfo("User Auth Qry "+fetchURL);
                responseCode = connection.getResponseCode();
                connection.getResponseMessage();
                String resultJson = "";
                InputStream stream = null;
                if(responseCode==200){
                    LogManager.writeLogInfo("User Auth check request success "+responseCode);
                    stream = connection.getInputStream();
                    if (stream != null) {
                        resultJson = DeviceIDUtils.readResponse(stream);
                    }
                    try {
                        JSONObject jsonObj = new JSONObject(resultJson);
                        JSONObject objectD = jsonObj.optJSONObject("d");

                        if(!TextUtils.isEmpty(objectD.toString())){
                            String value = "";
                            String valueTemp = "";
                            JSONArray jsonArray = objectD.getJSONArray("results");
                            boolean result = false;
                            for (int i=0; i<jsonArray.length();i++){
                                try {
                                    value = "";
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    value = jsonObject.optString(Constants.AuthOrgValue);
                                    if(value.equalsIgnoreCase("AWSM")){
                                        valueTemp = jsonObject.optString(Constants.AuthOrgValue);
                                        break;
                                    }else if(value.equalsIgnoreCase("BVAN")){
                                        valueTemp = jsonObject.optString(Constants.AuthOrgValue);
                                        break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            if(valueTemp.equalsIgnoreCase("BVAN")){
                                requestOnline(true);
                            }else {
                                initDeviceIDValidation(USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,true);
                            }

                        }else {
                            isClickable = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new DialogFactory.Progress(RegistrationActivity.this).hide();                                    LogManager.writeLogError("User Auth not done. Please contact support team");
                                    UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "","User Auth not done. Please contact support team" , getString(R.string.OK), "", false, new DialogCallBack() {
                                        @Override
                                        public void clickedStatus(boolean clickedStatus) {
                                        }
                                    });
                                }

                            });

                        }
                    } catch (JSONException e) {
                        isClickable = false;
                        LogManager.writeLogError("User Auth not done error :"+e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new DialogFactory.Progress(RegistrationActivity.this).hide();                            }

                        });
                        e.printStackTrace();
                    }
                }else if (responseCode == 401){
                    LogManager.writeLogInfo("User Auth check request failed : "+responseCode);
                    isClickable = false;
                    try {
                        com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, binding.editTextUserName.getText().toString().trim(), binding.editTextPassword.getText().toString().trim(), Configuration.APP_ID, jsonObject -> {
                            runOnUiThread(() -> {
                                new DialogFactory.Progress(RegistrationActivity.this).hide();
                                int value =jsonObject.optInt("code");
//                                com.arteriatech.mutils.common.UtilConstants.passwordStatusErrorMessage(
//                                        RegistrationActivity.this,
//                                        jsonObject,
//                                        USER_NAME_DEVICE_ID, Configuration.IDPURL, Configuration.IDPTUSRNAME, Configuration.IDPTUSRPWD, Configuration.APP_ID);
                                Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject,registrationModel,binding.editTextUserName.getText().toString().trim());

                            });
                        });
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else if(responseCode == 503){
                    LogManager.writeLogInfo("User Auth check request failed : "+responseCode);
                    isClickable = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                            LogManager.writeLogError("Service unavailable [503]. Please contact support team");
                            UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "","Service unavailable. Please contact support team" , getString(R.string.OK), "", false, new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                }
                            });
                        }

                    });
                } else {
                    LogManager.writeLogInfo("User Auth check request failed : "+responseCode);
                    isClickable = false;
                    int finalResponseCode = responseCode;
                    err_msg =connection.getResponseMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                            try {
                                Constants.httphashmaperrorcodes();
                                err_msg = httpErrorCodes.get(""+finalResponseCode);
                            } catch (Throwable e) {
                                e.printStackTrace();
                                LogManager.writeLogError("httpErrorCodes error : "+e.getMessage());
                            }
                            LogManager.writeLogError("Unable to fetch user Auth details due to "+ err_msg);
                            UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "","Unable to fetch user Auth details due to "+ err_msg +" ["+finalResponseCode+"]", getString(R.string.OK), "", false, new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                }
                            });
                        }

                    });

                }
            } catch (Throwable e) {
                isClickable = false;
                LogManager.writeLogError("Unable to fetch user Auth details due to error :"+e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                        if (e.getMessage().contains("timeout")) {
                            UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Unable to fetch user Auth details due to connection timeout[UserProfiles]", getString(R.string.OK), "", false, new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                }
                            });
                        }else {
                            UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Unable to fetch user Auth details due to poor internet connectivity [UserProfiles]. \n\n" +
                                    "Please check internet and retry", getString(R.string.OK), "", false, new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                }
                            });
                        }
                    }

                });

            }
        }).start();
    }


    /**
     * Check deviceID validation. If deviceID is not available insert current DeviceID.
     * if deviceiID is exisit check if its matching or not, if matching return else throw error to
     *  update deviceID in backend.
     * @param userName puserName
     * @param password pUser password
     * @param isReg if this comes initially this will check and do registration else open store.
     */
    private void initDeviceIDValidation(String userName,String password,boolean isReg){
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new DialogFactory.Progress(RegistrationActivity.this)
                            .setProgressMessage("Verifying Device ID. Please wait...");
                    message = "Verifying Device ID. Please wait...";
                }
            });

            String userAuthURL = MyUtils.getDefaultOnlineQryURL() + "UserProfileAuthSet?$filter=" + URLEncoder.encode("Application eq 'MSEC'", "UTF-8");
            DeviceIDUtils.validateDeviceID(
                    MSFAApplication.APP_DEVICEID,
                    APP_ID,
                    userName,
                    password, userAuthURL,
                    this, (isError, message, code) -> {
                        if (isError){
                            try {
                                isClickable = false;
                                runOnUiThread(() -> {
                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                                    if (code!=DeviceIDUtils.UNAUTH_CODE) {
                                        Constants.showAlert(message,RegistrationActivity.this);
                                    }
                                });
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        } else{
                            isClickable = false;
                            runOnUiThread(() -> {
                                if (code==DeviceIDUtils.POST_REQUEST){
                                    try {
                                        SharedPreferences sharedPerf = getSharedPreferences(Constants.PREFS_NAME, 0);
                                        SharedPreferences.Editor editor = sharedPerf.edit();
                                        editor.putBoolean(Constants.DEVICE_ID_CHECK, true);
                                        editor.apply();
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        if (isReg) {
                                            requestOnline(true);
                                        }else{
                                            loginAvailableServer();
//                                            initStore();
                                        }
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
        } catch (Throwable e) {
            LogManager.writeLogError("Error while validating deviceID : \n"+e.toString());
        }
    }

    private void requestOnline(boolean isReg) {
        if (UtilConstants.isNetworkAvailable(this)) {
            new AsynTaskRegistration(isReg).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            isClickable = false;
            UtilConstants.dialogBoxWithCallBack(this, "", getString(R.string.network_error_message), getString(R.string.network_retry), getString(R.string.cancel), false, new DialogCallBack() {
                @Override
                public void clickedStatus(boolean clickedStatus) {
                    if (clickedStatus) {
                        requestOnline(isReg);
                    }else {
                        isClickable=false;
                    }
                }
            });
            LogManager.writeLogError(getString(R.string.network_error_message));
        }
    }


    /**
     * This method user credential saved into shared preferences.
     */
    private void onSaveConfig( Connection newConnection, SmpClient smpClient,boolean isRegister) {
        SharedPreferences.Editor editor =getSharedPreferences(Constants.PREFS_NAME,0).edit();
        editor.putString(KEY_username, binding.editTextUserName.getText().toString().trim());
        editor.putString(KEY_password, binding.editTextPassword.getText().toString().trim());
        editor.putString(KEY_serverHost, server_Text);
        editor.putString(KEY_serverPort, port_Text);
        editor.putString(KEY_securityConfig, secConfig_Text);
        editor.putString(KEY_appID, APP_ID);
        editor.putString(KEY_FARMID, farm_ID);
        editor.putString(KEY_SUFFIX, suffix);
        editor.putBoolean(KEY_ISHTTPS, IS_HTTPS);
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
        if(newConnection.getAndroidGcmPushEnabled()){
            LogManager.writeLogInfo("Gcm Push is Enabled");
            editor.putBoolean("GcmPushEnabled", newConnection.getAndroidGcmPushEnabled());
            if(!TextUtils.isEmpty(newConnection.getAndroidGcmSenderId())){
                LogManager.writeLogInfo("Gcm Sender Id is "+newConnection.getAndroidGcmSenderId());
                editor.putString("GcmSenderId", newConnection.getAndroidGcmSenderId());
//                String gcmRegsitrationID = getGCMRegsitrationID(newConnection.getAndroidGcmSenderId(), this);
                String gcmRegsitrationID = fcmMessagingInitToken();
                if(!TextUtils.isEmpty(gcmRegsitrationID)) {
                    LogManager.writeLogInfo("Gcm Sender Regsitration id is "+gcmRegsitrationID);
                    editor.putString("GcmRegistrationId", gcmRegsitrationID);
                    updateGCMRegsitrationIDtoServer(newConnection,smpClient,this,gcmRegsitrationID);
                }else {
                    LogManager.writeLogInfo("Gcm Sender Regsitration id is empty");
                }
            }else {
                LogManager.writeLogInfo("Gcm Sender Id is empty");
            }
        }else {
            LogManager.writeLogInfo("Gcm Push is not Enabled");
        }
        if(isRegister) {
            editor.putString(KEY_BirthDayAlertsDate, UtilConstants.getDate1());
        }
        editor.apply();
    }
    private boolean isValidated(){
        boolean isValid= true;
        if (TextUtils.isEmpty(binding.editTextUserName.getText().toString().trim())){
            isValid =false;
        }else if(TextUtils.isEmpty(binding.editTextPassword.getText().toString().trim())){
            isValid =false;
        }
        return isValid;
    }
    private class OpenOfflineStoreAsync extends AsyncTask<Void, String, String> {
        boolean isStoreOpened = false;
        boolean readRollId = false;
        ErrorBean errorBean = null;

        OpenOfflineStoreAsync(boolean readRollId) {
            this.readRollId = readRollId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
//                String message = "";
                if (!sharedPerf.getBoolean(UtilRegistrationActivity.KEY_isFirstTimeDB, false)) {
                    message= "Initial download sync in progress. Please wait...";
                }else{
//                    SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
//                    String date = settings.getString(Constants.BirthDayAlertsDate, "");
//                    if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
//                        message = "Day login sync in progress. Please wait...";
//                    }else {
//                        message = "Opening app. Please wait...";
                    message = "Preparing App. Please wait...";
//                    }
                }
                new DialogFactory.Progress(RegistrationActivity.this)
                        .setProgressMessage(message);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            String errorMsg = "";
//            SyncUtils.checkAndCreateDB(RegistrationActivity.this);
            try {
                if (OfflineManager.offlineStore != null) {
                    if (!isOfflineStoreOpen()) {
                        try {
                            LogManager.writeLogError("initializing Store");

//                            OfflineManager.openOfflineStore(RegistrationActivity.this, RegistrationActivity.this);
                            OfflineManager.openOfflineStoreInternal(RegistrationActivity.this, RegistrationActivity.this);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            LogManager.writeLogError(Constants.error_txt + e.getMessage());
                        }
                    } else if(OfflineManager.isOfflineStoreOpen()){
                        Log.e("STORE_STATE","doInBackground called isStoreOpened = true" );
//                        LogManager.writeLogError("doInBackground called");
                        isStoreOpened = true;
                    }
                } else {
                    try {
                        LogManager.writeLogError("initializing Store");
//                        OfflineManager.openOfflineStore(RegistrationActivity.this, RegistrationActivity.this);
                        OfflineManager.openOfflineStoreInternal(RegistrationActivity.this, RegistrationActivity.this);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        LogManager.writeLogError(Constants.error_txt + e.getMessage());
                    }
                }
            }catch (Throwable e) {
                e.printStackTrace();
                errorMsg = e.getMessage();
                LogManager.writeLogError(Constants.error_txt + e.getMessage());
            }
            return errorMsg;

        }

        @Override
        protected void onPostExecute(String result) {
            isClickable = false;
            super.onPostExecute(result);
            try {
                if (!TextUtils.isEmpty(result)) {
                    try {
                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    LogManager.writeLogError(Constants.error_txt + result);
                    Constants.showAlert(result,RegistrationActivity.this);
                    //                Constants.displayMsgReqError(result, MainActivity.this);
                } else if (errorBean != null) {
                    try {
                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    LogManager.writeLogError(Constants.error_txt + "Not able to get roll information");
                    Constants.displayMsgReqError(errorBean.getErrorCode(), RegistrationActivity.this);
                } else if (isStoreOpened) {
                    try {
//                        if (progressDialog!=null&&progressDialog.isShowing()) {
                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                        if (MSFAApplication.isSDA){
                           // startActivity(new Intent(RegistrationActivity.this, SDACommunicationDashboardActivity.class));
                        }else if(MSFAApplication.isAWSM){
                            SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                            String date = settings.getString(Constants.BirthDayAlertsDate, "");
                            if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
                                // perform all sync
                                String url =getBundleURL()+"/Resource";
//                                    validateBundleData(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,
//                                            url,
//                                            RegistrationActivity.this,false);
                                validateBundleData(payload);
//                                    createOnline();
                            }else{
                                LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                                boolean isFaceEnabled = RegistrationActivity.this.getSharedPreferences(Constants.PREFS_NAME,0).getBoolean(Constants.FACE_REINIT,false);
                                if (!isFaceEnabled) {
                                    boolean isAttendanceExist = getAttendance();
                                    if (!isAttendanceExist) {
//                if(refguid==null){
                                        refguid =  GUID.newRandom();
//                }
                                        SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_START,Constants.StartSync,refguid.toString36().toUpperCase());
                                        startActivityForResult(new Intent(RegistrationActivity.this, CameraXActivity.class), ConstantsUtils.CAMERA_REQUEST_CODE);
                                    } else {
                                     //   startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                        finish();
                                    }
                                }else{
                                    openCamera();
                                }
                            }
                        }else if(MSFAApplication.isBCRVAN){
                            SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                            String date = settings.getString(Constants.BirthDayAlertsDate, "");
                            if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
                                // perform all sync
                                String url =getBundleURL()+"/Resource";
//                                    validateBundleData(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,
//                                            url,
//                                            RegistrationActivity.this,false);
                                validateBundleData(payload);
//                                    createOnline();
                            }else{
                                LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                                boolean isFaceEnabled = RegistrationActivity.this.getSharedPreferences(Constants.PREFS_NAME,0).getBoolean(Constants.FACE_REINIT,false);
                                if (!isFaceEnabled) {
                                    boolean isAttendanceExist = getAttendance();
                                    if (!isAttendanceExist) {
//                if(refguid==null){
                                        refguid =  GUID.newRandom();
//                }
                                        SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_START,Constants.StartSync,refguid.toString36().toUpperCase());
                                        startActivityForResult(new Intent(RegistrationActivity.this, CameraXActivity.class), ConstantsUtils.CAMERA_REQUEST_CODE);
                                    } else {
                                      //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                        finish();
                                    }
                                }else{
                                    openCamera();
                                }
                            }
                        }else if(MSFAApplication.isCallCenter){
                            SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                            String date = settings.getString(Constants.BirthDayAlertsDate, "");
                            if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
                                // perform all sync
                                String url =getBundleURL()+"/Resource";
//                                    validateBundleData(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,
//                                            url,
//                                            RegistrationActivity.this,false);
                                validateBundleData(payload);
//                                    createOnline();
                            }else{
                                LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                                boolean isFaceEnabled = RegistrationActivity.this.getSharedPreferences(Constants.PREFS_NAME,0).getBoolean(Constants.FACE_REINIT,false);
                                if (!isFaceEnabled) {
                                    boolean isAttendanceExist = getAttendance();
                                    if (!isAttendanceExist) {
//                if(refguid==null){
                                        refguid =  GUID.newRandom();
//                }
                                        SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_START,Constants.StartSync,refguid.toString36().toUpperCase());
                                        startActivityForResult(new Intent(RegistrationActivity.this, CameraXActivity.class), ConstantsUtils.CAMERA_REQUEST_CODE);
                                    } else {
                                       // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                        finish();
                                    }
                                }else{
                                    openCamera();
                                }
                            }
                        }else if(MSFAApplication.isPSM){
                            SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                            String date = settings.getString(Constants.BirthDayAlertsDate, "");
                            if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
                                // perform all sync
                                createOnline();
                            }else{
                                LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                              //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                finish();
                            }
                        }else if(MSFAApplication.isVAN){
                            SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                            String date = settings.getString(Constants.BirthDayAlertsDate, "");
                            if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
                                // perform all sync
                                createOnline();
                            }else{
                                LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                               // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                finish();
                            }
                        } else if (MSFAApplication.isBVAN) {
                            SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                            String date = settings.getString(Constants.BirthDayAlertsDate, "");
                            if (date != null && !TextUtils.isEmpty(date) && !TextUtils.equals(date, UtilConstants.getDate1())) {
                                // perform all sync
                                createOnline();
                            } else {
                                LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                              //  startActivity(new Intent(RegistrationActivity.this, BreadDashBoard.class));
                                finish();
                            }
                        }

//                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        LogManager.writeLogError(Constants.error_txt + e1.getMessage());
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
                LogManager.writeLogError(Constants.error_txt + e.getMessage());
            }
        }
    }
    private boolean isReinitStore =false;
    @Override
    public void onRequestError(int operation, Exception e) {
        isClickable=false;
        try {
            String errorMessage = e.getMessage();
            LogManager.writeLogError("store failed to open with an error : "+e.toString());
            if(e.toString().contains("HTTP Status 401 ? Unauthorized") || e.toString().contains("invalid authentication")) {
                try {
                    Constants.writeLogsToInternalStorage(getApplicationContext(),"Initial Sync - onRequestError :\n " +
                            "log -onRequestError method called  with auth error \n"+e.toString());
                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                    String loginUser = sharedPreferences.getString("username", "");
                    String login_pwd = sharedPreferences.getString("password", "");
                    com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, jsonObject -> runOnUiThread(() -> {
                        new DialogFactory.Progress(RegistrationActivity.this).hide();
//                        com.arteriatech.mutils.common.UtilConstants.passwordStatusErrorMessage(
//                                RegistrationActivity.this,
//                                jsonObject,
//                                loginUser,Configuration.IDPURL,Configuration.IDPTUSRNAME,Configuration.IDPTUSRPWD,Configuration.APP_ID);
                        Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject,registrationModel,binding.editTextUserName.getText().toString().trim());
                    }));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }else if(errorMessage != null && (errorMessage.contains("10345"))){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                        UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Unable to Synchronize due to poor internet connectivity [10345]. \n\n Please check internet and retry", "", getString(R.string.ok), false, new DialogCallBack() {
                            @Override
                            public void clickedStatus(boolean clickedStatus) {
                            }
                        });
                    }
                });
            }else if(errorMessage != null && (errorMessage.contains("10346"))){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                        UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Unable to Synchronize due to poor internet connectivity [10346]. \n\n Please check internet and retry", "", getString(R.string.ok), false, new DialogCallBack() {
                            @Override
                            public void clickedStatus(boolean clickedStatus) {
                            }
                        });
                    }
                });
            } else if (errorMessage != null && (errorMessage.contains("-10251")
                    ||errorMessage.contains("-10227") ||
                    errorMessage.contains("InvalidStoreOptionValue")||
                    errorMessage.contains("InvalidInputParameters")||
                    errorMessage.contains("UnexpectedError"))&& !isReinitStore) {
                LogManager.writeLogError("store failed to open with an error but re-initializing store : \n"+e.toString());
                // Workaround for error "The file transfer failed due to an error on the server: -857 (SERVER_SYNCHRONIZATION_ERROR) (-10251)"
                // For this file transfer error with -10251 error, the current offline store need to be cleared for next initialization.
                OfflineManager.closeStore(this);
                try {
                    isReinitStore =true;
//                    OfflineManager.openOfflineStore(RegistrationActivity.this, RegistrationActivity.this);
                    OfflineManager.openOfflineStoreInternal(RegistrationActivity.this, RegistrationActivity.this);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    LogManager.writeLogError(Constants.error_txt + ex.getMessage());
                }
            }/*else if (errorMessage != null && (errorMessage.contains("503"))){
                final boolean isImportDB = importDB(RegistrationActivity.this, RegistrationActivity.this);
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (progressDialog!=null) {
                            progressDialog.dismiss();
                        }
                        if (!isImportDB) {//success
                            Constants.showAlert(e.toString(),RegistrationActivity.this);
                        }
                    }
                });
            }*/else{
                isReinitStore =false;
                ErrorBean errorBean = Constants.getErrorCode(operation, e, RegistrationActivity.this);
                penReqCount++;
                mBoolIsReqResAval = true;
                if ((operation == Operation.Create.getValue()) && (penReqCount == mIntPendingCollVal)) {
                    postOfflineData();
                }
                if (operation == Operation.OfflineFlush.getValue()) {
//                    if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                    afterPostNavigateDistributorScreen();
//                    }
                }else if (errorBean.hasNoError()) {
                    new DialogFactory.Progress(RegistrationActivity.this).hide();
//                Toast.makeText(RegistrationActivity.this, getString(R.string.err_odata_unexpected, e.getMessage()), Toast.LENGTH_LONG).show();
                    String mStrPopUpText = "";
                    try {
                        mStrPopUpText = errorBean.getErrorMsg();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (TextUtils.isEmpty(mStrPopUpText)) {
                        mStrPopUpText = getString(R.string.alert_sync_cannot_be_performed);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Constants.showAlert(errorBean.getErrorMsg(),RegistrationActivity.this);
                        }
                    });
                } else {
                    try {
                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (errorBean.getErrorMsg()!=null&&!TextUtils.isEmpty(errorBean.getErrorMsg())) {
                                    Constants.showAlert(errorBean.getErrorMsg(),RegistrationActivity.this);
                                }else{
                                    Constants.showAlert(e.toString(),RegistrationActivity.this);
                                }
                            }
                        });
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    Constants.displayMsgReqError(errorBean.getErrorCode(), RegistrationActivity.this);
                }
            }

        } catch (Throwable ex) {
            ex.printStackTrace();
            LogManager.writeLogError(Constants.error_txt + ex.getMessage());
        }
        if (allSyncDialog!=null) {
            allSyncDialog.dismiss();
        }
    }
    public class AsyncPostOffline extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
                concatFlushCollStr = UtilConstants.getConcatinatinFlushCollectios(alFlushColl);
                try {
                    if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                        try {
                            OfflineManager.flushQueuedRequests(RegistrationActivity.this, concatFlushCollStr);
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (ODataException e) {
                    e.printStackTrace();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
    private void postOfflineData() {
        try {
            if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                    try {
                        new AsyncPostOffline().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    closingProgressDialog();
                    Constants.showAlert(getString(R.string.data_conn_lost_during_sync), this);
                }
            } else {
//                if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                afterPostNavigateDistributorScreen();
//                } else {
//                    closingProgressDialog();
//                    Constants.showAlert(getString(R.string.data_conn_lost_during_sync), this);
//                }
            }

        } catch (ODataException e) {
            e.printStackTrace();
        }
    }

    private void closingProgressDialog() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String concatCollectionStr = "";
    @Override
    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
        try {
            isClickable=false;
            isReinitStore =false;
            if (operation == Operation.Create.getValue() && mIntPendingCollVal > 0) {
                mBoolIsReqResAval = true;
                Constants.removeDataValtFromSharedPref(this, invKeyValues[penReqCount][1], invKeyValues[penReqCount][0], false);

                try {
                    if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.SecondarySOCreate)) {

                        JSONObject jsonObject1 = null;
                        String orderNo = "";
                        try {
                            jsonObject1 = Constants.getJSONBody(key);
                            orderNo = jsonObject1.getString("OrderNo");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        LogManager.writeLogInfo("Sale Order " + orderNo + " created successfully");
                        Constants.saveDeviceDocNoToSharedPref(this, Constants.SecondarySOCreate, invKeyValues[penReqCount][0]);
                        fetchJsonHeaderObject.put(Constants.Status, "000000");
                        fetchJsonHeaderObject.put(Constants.OrderNo, orderNo);
                        LogManager.writeLogInfo("Device order no. " + invKeyValues[penReqCount][0] + " updated with sale Order no. " + orderNo + " and status updated  after post ");
                        ConstantsUtils.storeInDataVault(invKeyValues[penReqCount][0], fetchJsonHeaderObject.toString(), this);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                try {
                    SyncHistoryDB.deleteInvoiceDocID(this, invKeyValues[penReqCount][0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                penReqCount++;
            }
            if ((operation == Operation.Create.getValue()) && (penReqCount == mIntPendingCollVal)) {
                postOfflineData();
            }else if (operation == Operation.Update.getValue()) {
                postOfflineData();
            }else if (operation == Operation.OfflineFlush.getValue()) {
                LogManager.writeLogInfo("offline flush completed");
//            if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                Constants.updateLastSyncTimeToTable(RegistrationActivity.this, alAssignColl);
                afterPostNavigateDistributorScreen();
//            }
            }
            if (operation == Operation.OfflineRefresh.getValue()) {
                LogManager.writeLogInfo("Offline refresh is in progress");
                Constants.updateLastSyncTimeToTable(RegistrationActivity.this, pendingCollectionList);
                runOnUiThread(()-> {
//                    if (progressDialog!=null) {
//                        try {
//                            if (OfflineManager.isOfflineStoreOpen()) {
//                                LogManager.writeLogDebug("Day All Sync DB Export");
//                                SyncSelectionActivity.exportDB(RegistrationActivity.this);
//                            }
//                        } catch (Throwable e) {
//                            e.printStackTrace();
//                        }

                    if (OfflineManager.isOfflineStoreOpen()) {

                        try {
                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (MSFAApplication.isSDA){
                           // startActivity(new Intent(RegistrationActivity.this, SDACommunicationDashboardActivity.class));
                        }else if(MSFAApplication.isAWSM){
                            try {
                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                           //     startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                finish();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }else if(MSFAApplication.isBCRVAN){
                            try {
                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                                //startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                finish();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }else if(MSFAApplication.isCallCenter){
                            try {
                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                               // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                finish();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }else if(MSFAApplication.isPSM){
                            try {
                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                               // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                finish();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }else if(MSFAApplication.isVAN){
                            try {
                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                               // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                finish();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }else if(MSFAApplication.isBVAN){
                            try {
                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                             //   startActivity(new Intent(RegistrationActivity.this, BreadDashBoard.class));
                                finish();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }

                    }

//                    }
                });
            }else if (operation == Operation.GetStoreOpen.getValue()) {
                try {
                    try {
                        OfflineManager.getAuthorizations(RegistrationActivity.this);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    getCPConfigData();
                    new OutletSQLHelper(this);
                    final boolean[] isAskPasswordSave = {false};
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            if (progressDialog!=null&&progressDialog.isShowing()) {
                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                            SharedPreferences sharedPerf = getSharedPreferences(Constants.PREFS_NAME, 0);
                            try {
                                if (!sharedPerf.getBoolean(UtilRegistrationActivity.KEY_isFirstTimeDB, false)) {
//                                    try {
//                                        BannerParser.extractBannerImages(RegistrationActivity.this);
//                                    } catch (Throwable e) {
//                                        e.printStackTrace();
//                                    }
//                                try {
//                                    if (OfflineManager.isOfflineStoreOpen()) {
//                                        LogManager.writeLogDebug("Intial Sync DB Export");
//                                        SyncSelectionActivity.exportDB(RegistrationActivity.this);
//                                    }
//                                } catch (Throwable e) {
//                                    e.printStackTrace();
//                                }
                                  /*  try {
                                        if (OfflineManager.isOfflineStoreOpen()) {
                                            MSFAApplication.getGAFields(RegistrationActivity.this);
                                        }
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }*/
                                    isAskPasswordSave[0] =true;
                                    SharedPreferences.Editor editor = sharedPerf.edit();
                                    editor.putBoolean(UtilRegistrationActivity.KEY_isFirstTimeDB, true);
                                    editor.apply();
                                    // initial sync to update SyncHistory
                                    refguid =  GUID.newRandom();
                                    SyncUtils.updatingSyncStartTime(RegistrationActivity.this,Constants.Sync_initial,Constants.StartSync,refguid.toString36().toUpperCase());
                                    ArrayList<String> syncCollectionList = new ArrayList<>();
                                    String[] definingReqArray = Constants.getDefiningReq(RegistrationActivity.this);
                                    for (String aDefiningReqArray : definingReqArray) {
                                        String collection = aDefiningReqArray;
                                        if (collection.contains("?$")) {
                                            String[] splitCollName = collection.split("\\?");
                                            collection = splitCollName[0];
                                        }
                                        syncCollectionList.add(collection);
                                    }
                                    SyncUtils.updatingSyncTime(RegistrationActivity.this,syncCollectionList,Constants.Sync_initial, new RefreshListInterface() {
                                        @Override
                                        public void refreshList() {

                                        }
                                    },refguid.toString36().toUpperCase());
                                    SyncUtils.updateAllSyncHistory(RegistrationActivity.this);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (OfflineManager.isOfflineStoreOpen()) {
                                if (MSFAApplication.isSDA){
                                    //startActivity(new Intent(RegistrationActivity.this, SDACommunicationDashboardActivity.class));
                                }else if(MSFAApplication.isAWSM){
                                    try {
                                        if (isAskPasswordSave[0]) {
                                            Constants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Do you want to save password?", "Yes", "No", false, new DialogCallBack() {
                                                @Override
                                                public void clickedStatus(boolean b) {
                                                    if (b){
                                                        SharedPreferences.Editor editor = sharedPerf.edit();
                                                        editor.putBoolean(Constants.PASSWORD_SAVE, true);
                                                        editor.apply();
                                                        boolean ischannel = channelCheck();
                                                        if (ischannel) {
                                                            openCamera();
                                                        }else {
                                                            if (AW_GEO_FENCING != null && !TextUtils.isEmpty(AW_GEO_FENCING) && AW_GEO_FENCING.length() >= 3 && AW_GEO_FENCING.substring(1, 2).equalsIgnoreCase("Y")) {
                                                                openCamera();
                                                            } else if (passwordStatus != null && passwordStatus.equalsIgnoreCase("initial")) {
                                                                initInitialPasswordScreen();
                                                            } else {
                                                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                                                              //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                                finish();
                                                            }
                                                        }
                                                    }else{
                                                        boolean ischannel = channelCheck();
                                                        if (ischannel) {
                                                            openCamera();
                                                        }else {
                                                            if (AW_GEO_FENCING != null && !TextUtils.isEmpty(AW_GEO_FENCING) && AW_GEO_FENCING.length() >= 3 && AW_GEO_FENCING.substring(1, 2).equalsIgnoreCase("Y")) {
                                                                openCamera();
                                                            } else if (passwordStatus != null && passwordStatus.equalsIgnoreCase("initial")) {
                                                                initInitialPasswordScreen();
                                                            } else {
                                                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                                                               // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                                finish();
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                        }else{
                                            SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                                            String date = settings.getString(Constants.BirthDayAlertsDate, "");
                                            if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
                                                // clear cookies before day sync.
//                                            clearCookies();
                                                // perform all sync
                                                String url =getBundleURL()+"/Resource";
//                                            validateBundleData(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,
//                                                    url,
//                                                    RegistrationActivity.this,false);
                                                validateBundleData(payload);
//                                            createOnline();
                                            }else{
                                                reInitializeStore();
//                                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
//                                                finish();
                                            }

                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.e("SERVICE-SCHEME",e.toString());
                                    }
                                }else if(MSFAApplication.isBCRVAN){
                                    try {
                                        if (isAskPasswordSave[0]) {
                                            Constants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Do you want to save password?", "Yes", "No", false, new DialogCallBack() {
                                                @Override
                                                public void clickedStatus(boolean b) {
                                                    if (b){
                                                        SharedPreferences.Editor editor = sharedPerf.edit();
                                                        editor.putBoolean(Constants.PASSWORD_SAVE, true);
                                                        editor.apply();
                                                        boolean ischannel = channelCheck();
                                                        if (ischannel) {
                                                            openCamera();
                                                        }else {
                                                            if (AW_GEO_FENCING != null && !TextUtils.isEmpty(AW_GEO_FENCING) && AW_GEO_FENCING.length() >= 3 && AW_GEO_FENCING.substring(1, 2).equalsIgnoreCase("Y")) {
                                                                openCamera();
                                                            } else if (passwordStatus != null && passwordStatus.equalsIgnoreCase("initial")) {
                                                                initInitialPasswordScreen();
                                                            } else {
                                                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                                                            //    startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                                finish();
                                                            }
                                                        }
                                                    }else{
                                                        boolean ischannel = channelCheck();
                                                        if (ischannel) {
                                                            openCamera();
                                                        }else {
                                                            if (AW_GEO_FENCING != null && !TextUtils.isEmpty(AW_GEO_FENCING) && AW_GEO_FENCING.length() >= 3 && AW_GEO_FENCING.substring(1, 2).equalsIgnoreCase("Y")) {
                                                                openCamera();
                                                            } else if (passwordStatus != null && passwordStatus.equalsIgnoreCase("initial")) {
                                                                initInitialPasswordScreen();
                                                            } else {
                                                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                                                              //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                                finish();
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                        }else{
                                            SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                                            String date = settings.getString(Constants.BirthDayAlertsDate, "");
                                            if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
                                                // clear cookies before day sync.
//                                            clearCookies();
                                                // perform all sync
                                                String url =getBundleURL()+"/Resource";
//                                            validateBundleData(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,
//                                                    url,
//                                                    RegistrationActivity.this,false);
                                                validateBundleData(payload);
//                                            createOnline();
                                            }else{
                                                reInitializeStore();
//                                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
//                                                finish();
                                            }

                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.e("SERVICE-SCHEME",e.toString());
                                    }
                                }else if(MSFAApplication.isCallCenter){
                                    try {
                                        if (isAskPasswordSave[0]) {
                                            Constants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Do you want to save password?", "Yes", "No", false, new DialogCallBack() {
                                                @Override
                                                public void clickedStatus(boolean b) {
                                                    if (b){
                                                        SharedPreferences.Editor editor = sharedPerf.edit();
                                                        editor.putBoolean(Constants.PASSWORD_SAVE, true);
                                                        editor.apply();
                                                        boolean ischannel = channelCheck();
                                                        if (ischannel) {
                                                            openCamera();
                                                        }else {
                                                            if (AW_GEO_FENCING != null && !TextUtils.isEmpty(AW_GEO_FENCING) && AW_GEO_FENCING.length() >= 3 && AW_GEO_FENCING.substring(1, 2).equalsIgnoreCase("Y")) {
                                                                openCamera();
                                                            } else if (passwordStatus != null && passwordStatus.equalsIgnoreCase("initial")) {
                                                                initInitialPasswordScreen();
                                                            } else {
                                                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                                                               // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                                finish();
                                                            }
                                                        }
                                                    }else{
                                                        boolean ischannel = channelCheck();
                                                        if (ischannel) {
                                                            openCamera();
                                                        }else {
                                                            if (AW_GEO_FENCING != null && !TextUtils.isEmpty(AW_GEO_FENCING) && AW_GEO_FENCING.length() >= 3 && AW_GEO_FENCING.substring(1, 2).equalsIgnoreCase("Y")) {
                                                                openCamera();
                                                            } else if (passwordStatus != null && passwordStatus.equalsIgnoreCase("initial")) {
                                                                initInitialPasswordScreen();
                                                            } else {
                                                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                                                              //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                                finish();
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                        }else{
                                            SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                                            String date = settings.getString(Constants.BirthDayAlertsDate, "");
                                            if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
                                                // clear cookies before day sync.
//                                            clearCookies();
                                                // perform all sync
                                                String url =getBundleURL()+"/Resource";
//                                            validateBundleData(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,
//                                                    url,
//                                                    RegistrationActivity.this,false);
                                                validateBundleData(payload);
//                                            createOnline();
                                            }else{
                                                reInitializeStore();
//                                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
//                                                finish();
                                            }

                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.e("SERVICE-SCHEME",e.toString());
                                    }
                                }else if(MSFAApplication.isPSM){
                                    try {
                                        if (isAskPasswordSave[0]) {
                                            Constants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Do you want to save password?", "Yes", "No", false, new DialogCallBack() {
                                                @Override
                                                public void clickedStatus(boolean b) {
                                                    if (b){
                                                        SharedPreferences.Editor editor = sharedPerf.edit();
                                                        editor.putBoolean(Constants.PASSWORD_SAVE, true);
                                                        editor.apply();
                                                        if (passwordStatus!=null&&passwordStatus.equalsIgnoreCase("initial")) {
                                                            initInitialPasswordScreen();
                                                        }else{
                                                            LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                                                          //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                            finish();
                                                        }
                                                    }else{
                                                        if (passwordStatus!=null&&passwordStatus.equalsIgnoreCase("initial")) {
                                                            initInitialPasswordScreen();
                                                        }else{
                                                            LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                                                        //    startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                            finish();
                                                        }
                                                    }
                                                }
                                            });
                                        }else{
                                            SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                                            String date = settings.getString(Constants.BirthDayAlertsDate, "");
                                            if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
                                                // clear cookies before day sync.
//                                            clearCookies();
                                                // perform all sync
                                                String url =getBundleURL();
//                                            validateBundleData(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,
//                                                    url,
//                                                    RegistrationActivity.this,false);
                                                validateBundleData(payload);
//                                            createOnline();
                                            }else{
                                                reInitializeStore();
//                                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
//                                                finish();
                                            }

                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.e("SERVICE-SCHEME",e.toString());
                                    }
                                }else if(MSFAApplication.isVAN){
                                    try {
                                        if (isAskPasswordSave[0]) {
                                            Constants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Do you want to save password?", "Yes", "No", false, new DialogCallBack() {
                                                @Override
                                                public void clickedStatus(boolean b) {
                                                    if (b){
                                                        SharedPreferences.Editor editor = sharedPerf.edit();
                                                        editor.putBoolean(Constants.PASSWORD_SAVE, true);
                                                        editor.apply();
                                                        if (passwordStatus!=null&&passwordStatus.equalsIgnoreCase("initial")) {
                                                            initInitialPasswordScreen();
                                                        }else{
                                                            getAttendenceDetails();
                                                            LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                        startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
//                                                        finish();
                                                        }
                                                    }else{
                                                        if (passwordStatus!=null&&passwordStatus.equalsIgnoreCase("initial")) {
                                                            initInitialPasswordScreen();
                                                        }else{
                                                            getAttendenceDetails();
                                                            LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                        startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
//                                                        finish();
                                                        }
                                                    }
                                                }
                                            });
                                        }else{
                                            SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                                            String date = settings.getString(Constants.BirthDayAlertsDate, "");
                                            if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
                                                // clear cookies before day sync.
//                                            clearCookies();
                                                // perform all sync
                                                String url =getBundleURL();
//                                            validateBundleData(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,
//                                                    url,
//                                                    RegistrationActivity.this,false);
                                                validateBundleData(payload);
//                                            createOnline();
                                            }else{
                                                reInitializeStore();
//                                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
//                                                finish();
                                            }

                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.e("SERVICE-SCHEME",e.toString());
                                    }
                                } else if (MSFAApplication.isBVAN) {
                                    try {
                                        if (isAskPasswordSave[0]) {
                                            Constants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Do you want to save password?", "Yes", "No", false, new DialogCallBack() {
                                                @Override
                                                public void clickedStatus(boolean b) {
                                                    if (b) {
                                                        SharedPreferences.Editor editor = sharedPerf.edit();
                                                        editor.putBoolean(Constants.PASSWORD_SAVE, true);
                                                        editor.apply();
                                                        if (passwordStatus != null && passwordStatus.equalsIgnoreCase("initial")) {
                                                            initInitialPasswordScreen();
                                                        } else {
                                                            getAttendenceDetails();
                                                            LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                                                        }
                                                    } else {
                                                        if (passwordStatus != null && passwordStatus.equalsIgnoreCase("initial")) {
                                                            initInitialPasswordScreen();
                                                        } else {
                                                            getAttendenceDetails();
                                                            LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                                                        }
                                                    }
                                                }
                                            });
                                        } else {
                                            SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                                            String date = settings.getString(Constants.BirthDayAlertsDate, "");
                                            if (date != null && !TextUtils.isEmpty(date) && !TextUtils.equals(date, UtilConstants.getDate1())) {
                                                // clear cookies before day sync.
//                                            clearCookies();
                                                // perform all sync
                                                String url = getBundleURL();
//                                            validateBundleData(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,
//                                                    url,
//                                                    RegistrationActivity.this,false);
                                                validateBundleData(payload);
//                                            createOnline();
                                            } else {
                                                reInitializeStore();
                                                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                                              //  startActivity(new Intent(RegistrationActivity.this, BreadDashBoard.class));
                                                finish();
                                            }
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.e("SERVICE-SCHEME", e.toString());
                                    }
                                }

                            }
//                            }
                        }
                    });
                } catch (Throwable e) {
                    e.printStackTrace();
                    LogManager.writeLogError(Constants.error_txt + e.getMessage());
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int reqcode = requestCode;
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    validateImeiFromServer();
                    try {
                        createLogDirectory();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                } else {
                    checkStoragePermission();
                }
                return;
            }
        }
    }

    private String getJSONError(String value){
        String response="";
        try {
            JSONObject jsonObject = new JSONObject(value);
            jsonObject = jsonObject.getJSONObject("error");
            jsonObject = jsonObject.getJSONObject("message");
            response  = jsonObject.getString("value");
            if (response.contains("not found in type UserProfileAuth")){
                return "401 Unauthorized..!";
            }
        } catch (Throwable e) {
            return value;
        }
        return value;
    }
    private void share(){
        Constants.dialogBoxWithCallBack(this, "Device ID", MSFAApplication.APP_DEVICEID, "Share Id", "Cancel", false, new DialogCallBack() {
            @Override
            public void clickedStatus(boolean b) {
                if (b) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Device ID");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Device ID - " + MSFAApplication.APP_DEVICEID);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (allSyncDialog!=null){
            allSyncDialog.dismiss();
        }
        new DialogFactory.Progress(RegistrationActivity.this).hide();
    }
    ArrayList<String>list  = new ArrayList<>();
    ArrayList<String> pendingCollectionList =  new ArrayList<>();
    String[] tempRODevList = null;
    final int[] penROReqCount = {0};
    int pendingROVal = 0;
    private void createOnline(){
        try {
            LogManager.writeLogError("createOnline called to perform all Sync");
            if (UtilConstants.isNetworkAvailable(RegistrationActivity.this)) {
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                String loginUser = sharedPreferences.getString("username", "");
                String login_pwd = sharedPreferences.getString("password", "");
                try {
                    new DialogFactory.Progress(RegistrationActivity.this)
                            .setProgressMessage("Synchronization is in progress. Please wait...");
                    message = "Synchronization is in progress. Please wait...";
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                Constants.updateIDPCredential(this);
                getIDPSharedPrefValue(this);
                Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, jsonObject -> runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int value = jsonObject.optInt("code");
                        if (value==200){
                            SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME,0);
                            String p_user_id = sharedPreferences.getString("p_user_id","");
                            if(TextUtils.isEmpty(p_user_id)){
                                checkInitialPasswordStatus();
                            }
                            initDaySync();
                        }else if(value==0&&jsonObject.optString("message").contains("java.net.UnknownHostException")){
                            Constants.showAlert(ErrorCode.NETWORK_ERROR,RegistrationActivity.this);
                        }else{
                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                            Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject,registrationModel,binding.editTextUserName.getText().toString().trim());
                        }
                    }
                }));

            }else{
                isClickable = false;
                new DialogFactory.Progress(RegistrationActivity.this).hide();
                Constants.showAlert(ErrorCode.NETWORK_ERROR,this);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void onUpdateSync() {
        try {
            LogManager.writeLogInfo("Login Update sync started");
            if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                LogManager.writeLogInfo("Internet available");

                try {
                    removeFromDatavault(this);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    LogManager.writeLogInfo("Check Pending Request");
                    checkPendingReqIsAval();

                    if (OfflineManager.offlineStore.getRequestQueueIsEmpty() && mIntPendingCollVal == 0) {
                        afterPostNavigateDistributorScreen();
                    } else {
                        if (mIntPendingCollVal > 0) {
                            LogManager.writeLogInfo("Pending Request(s) : " + mIntPendingCollVal);

                            if (!alAssignColl.contains(Constants.ConfigTypsetTypeValues)) {
                                alAssignColl.add(Constants.ConfigTypsetTypeValues);
                            }

                            if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                                onPostOnlineData();
                            } else {
                                Constants.showAlert(ErrorCode.NETWORK_ERROR, this);
                            }
                        } else if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                            if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                                postOfflineData();
                            } else {
                                Constants.showAlert(ErrorCode.NETWORK_ERROR, this);
                            }
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                LogManager.writeLogInfo("Internet not available. Upload sync ended");
                Constants.showAlert(ErrorCode.NETWORK_ERROR, this);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void onPostOnlineData() {
        try {
            PostingDataValutData asyncTask = new PostingDataValutData();
            asyncTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean tokenFlag = false;
    private boolean mBoolIsReqResAval = false;
    boolean onlineStoreOpen = false;
    public class PostingDataValutData extends AsyncTask<Void, Boolean, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
                tokenFlag = false;

                Constants.x_csrf_token = "";
                Constants.ErrorCode = 0;
                Constants.ErrorNo = 0;
                Constants.ErrorName = "";
                Constants.IsOnlineStoreFailed = false;
                Constants.IsOnlineStoreStarted = false;
                mBoolIsReqResAval = true;
                LogManager.writeLogInfo("Posting data to backend started");
                try {
                    readValuesFromDataVault();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closingProgressDialog();
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return onlineStoreOpen = true;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {
                closingProgressDialog();
                if (!onlineStoreOpen) {
                    if (Constants.ErrorNo == Constants.Network_Error_Code && Constants.ErrorName.equalsIgnoreCase(Constants.NetworkError_Name)) {
                        Constants.showAlert(getString(R.string.data_conn_lost_during_sync_error_code, Constants.ErrorNo + ""), RegistrationActivity.this);

                    } else if (Constants.ErrorNo == Constants.UnAuthorized_Error_Code && Constants.ErrorName.equalsIgnoreCase(Constants.NetworkError_Name)) {
                        Constants.showAlert(getString(R.string.auth_fail_plz_contact_admin, Constants.ErrorNo + ""), RegistrationActivity.this);
                    } else if (Constants.ErrorNo == Constants.Comm_Error_Code) {
                        Constants.showAlert(getString(R.string.data_conn_lost_during_sync_error_code, Constants.ErrorNo + ""), RegistrationActivity.this);
                    } else {
                        Constants.showAlert(getString(R.string.data_conn_lost_during_sync_error_code, Constants.ErrorNo + ""), RegistrationActivity.this);
                    }
                } else if (!tokenFlag) {
                    Constants.displayMsgINet(Constants.ErrorNo_Get_Token, RegistrationActivity.this);
                } else if (Constants.x_csrf_token == null || Constants.x_csrf_token.equalsIgnoreCase("")) {
                    Constants.showAlert(getString(R.string.data_conn_lost_during_sync_error_code, -2 + ""), RegistrationActivity.this);
                }
            }
        }
    }

    private JSONObject fetchJsonHeaderObject = null;
    private void readValuesFromDataVault() {
        if (mIntPendingCollVal > 0) {
            for (int k = 0; k < invKeyValues.length; k++) {

                while (!mBoolIsReqResAval) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mBoolIsReqResAval = false;
                String store = ConstantsUtils.getFromDataVault(invKeyValues[k][0].toString(), this);
                //Fetch object from data vault
                if (store != null && !TextUtils.isEmpty(store)) {
                    try {
                        JSONObject fetchJsonHeaderObject = new JSONObject(store);
                        this.fetchJsonHeaderObject = fetchJsonHeaderObject;
                        dbHeadTable = new Hashtable();
                        arrtable = new ArrayList<>();
                        if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                            if (!alAssignColl.contains(Constants.SSINVOICES)) {
                                //                            alAssignColl.add(Constants.SSInvoiceItemDetails);
                                alAssignColl.add(Constants.SSINVOICES);
                            }
                            if (!alAssignColl.contains(Constants.SSSOs)) {
                                alAssignColl.add(Constants.SSSoItemDetails);
                                alAssignColl.add(Constants.SSSOs);
                            }
                            JSONObject headerObject = Constants.getSOHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                            LogManager.writeLogInfo("Starting SO Post " + k);
                            OnlineManager.createEntity(REPEATABLE_REQUEST_ID, headerObject.toString(), Constants.SSSOs, this, this);
                            LogManager.writeLogInfo("After SO Post " + k);
                        } else if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.ChannelPartners)) {
                            // preparing entity pending
                            if (!alAssignColl.contains(Constants.ChannelPartners)) {
                                alAssignColl.add(Constants.ChannelPartners);
                                alAssignColl.add(Constants.CPDMSDivisions);
                                alAssignColl.add(Constants.RoutePlans);
                                alAssignColl.add(Constants.RouteSchedulePlans);
                                alAssignColl.add(Constants.RouteScheduleSPs);
                                alAssignColl.add(Constants.RouteScheduleDetails);
                                //                            alAssignColl.add(Constants.CPDocuments);
                            }
                            JSONObject headerObject = Constants.getCPHeaderValuesFromJsonObject(fetchJsonHeaderObject,this);
                            LogManager.writeLogInfo("Starting Retailer Post " + k);
                            OnlineManager.createEntity(REPEATABLE_REQUEST_ID, headerObject.toString(), Constants.ChannelPartners, this, this);
                            LogManager.writeLogInfo("After Retailer Post " + k);
                        } else if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.ReturnOrderCreate)) {

                            // preparing entity pending
                            if (!alAssignColl.contains(Constants.SSROs)) {
                                alAssignColl.add(Constants.SSROs);
//                                alAssignColl.add(Constants.SSSoItemDetails);
                            }
                            JSONObject headerObject = Constants.getROHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                            OnlineManager.createEntity(REPEATABLE_REQUEST_ID, headerObject.toString(), Constants.SSROs, this, this);
                        }else if(fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SampleDisbursement)){
                            if (!alAssignColl.contains(Constants.SSINVOICES)) {
                                alAssignColl.add(Constants.SSInvoiceItemDetails);
                                alAssignColl.add(Constants.SSINVOICES);
                                alAssignColl.add(Constants.SPStockItemSNos);
                                alAssignColl.add(Constants.SPStockItems);
                            }
                        /*dbHeadTable = Constants.getSSInvoiceHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                        String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);
                        arrtable = UtilConstants.convertToArrayListMap(itemsString);
                        try {
                            OnlineManager.createSSInvoiceEntity(dbHeadTable, arrtable, SyncSelectionActivity.this);
                        } catch (OnlineODataStoreException e) {
                            e.printStackTrace();
                        }*/
                            JSONObject headerObject = Constants.getSSInvoiceHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                            OnlineManager.createEntity(REPEATABLE_REQUEST_ID,headerObject.toString(), Constants.SSINVOICES, this, this);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        mBoolIsReqResAval = true;
                        LogManager.writeLogError(e.toString());
                        runOnUiThread(this::closingProgressDialog);
                        runOnUiThread(() -> {
                            Constants.showAlert(e.getMessage(),RegistrationActivity.this);
                        });
                    }
                }

            }
        }
    }

    int mIntPendingCollVal = 0;
    String[][] invKeyValues;
    private int penReqCount = 0;
    private void checkPendingReqIsAval() {
        try {
            mIntPendingCollVal = 0;
            invKeyValues = null;
            ArrayList<Object> objectArrayList = SyncSelectionActivity.getPendingCollList(this, false);
            if (!objectArrayList.isEmpty()) {
                mIntPendingCollVal = (int) objectArrayList.get(0);
                invKeyValues = (String[][]) objectArrayList.get(1);
            }
            penReqCount = 0;
            alAssignColl.clear();
            alFlushColl.clear();
            concatCollectionStr = "";
            concatFlushCollStr = "";
            ArrayList<String> allAssignColl = SyncSelectionActivity.getRefreshList();
            if (!allAssignColl.isEmpty()) {
                alAssignColl.addAll(allAssignColl);
                alFlushColl.addAll(allAssignColl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void removeFromDatavault(Context context){
        Set<String> set = new HashSet<>();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
        set = sharedPreferences.getStringSet(Constants.SecondarySOCreate, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, context);
//                    LogManager.writeLogInfo("Remove SO orders from Datavault");
                    if (store == null) {
                        LogManager.writeLogInfo("Remove SO orders from Datavault key value null");
                        Constants.removeDataValtFromSharedPref(context, Constants.SecondarySOCreate, deviceNo, false);
//                        Constants.removeDataValtFromSharedPref(context, Constants.SecondarySOCreateTemp, deviceNo, false);
                    }else {
                        if (TextUtils.isEmpty(store)) {
                            LogManager.writeLogInfo("Remove SO orders from Datavault key value empty");
                            Constants.removeDataValtFromSharedPref(context, Constants.SecondarySOCreate, deviceNo, false);
//                            Constants.removeDataValtFromSharedPref(context, Constants.SecondarySOCreateTemp, deviceNo, false);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        set = sharedPreferences.getStringSet(Constants.Feedbacks, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, context);
//                    LogManager.writeLogInfo("Remove Feedbacks from Datavault");
                    if (store == null) {
                        LogManager.writeLogInfo("Remove Feedbacks from Datavault key value null");
                        Constants.removeDataValtFromSharedPref(context, Constants.Feedbacks, deviceNo, false);
                    }else {
                        if (TextUtils.isEmpty(store)) {
                            LogManager.writeLogInfo("Remove Feedbacks from Datavault key value empty");
                            Constants.removeDataValtFromSharedPref(context, Constants.Feedbacks, deviceNo, false);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        set = sharedPreferences.getStringSet(Constants.FinancialPostings, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, context);
//                    LogManager.writeLogInfo("Remove Feedbacks from Datavault");
                    if (store == null) {
                        LogManager.writeLogInfo("Remove FinancialPostings from Datavault key value null");
                        Constants.removeDataValtFromSharedPref(context, Constants.FinancialPostings, deviceNo, false);
                    }else {
                        if (TextUtils.isEmpty(store)) {
                            LogManager.writeLogInfo("Remove FinancialPostings from Datavault key value empty");
                            Constants.removeDataValtFromSharedPref(context, Constants.FinancialPostings, deviceNo, false);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        set = sharedPreferences.getStringSet(Constants.SampleDisbursement, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, context);
//                    LogManager.writeLogInfo("Remove Feedbacks from Datavault");
                    if (store == null) {
                        LogManager.writeLogInfo("Remove SampleDisbursement from Datavault key value null");
                        Constants.removeDataValtFromSharedPref(context, Constants.SampleDisbursement, deviceNo, false);
                    }else {
                        if (TextUtils.isEmpty(store)) {
                            LogManager.writeLogInfo("Remove SampleDisbursement from Datavault key value empty");
                            Constants.removeDataValtFromSharedPref(context, Constants.SampleDisbursement, deviceNo, false);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        set = sharedPreferences.getStringSet(Constants.CPList, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, context);
//                    LogManager.writeLogInfo("Remove Feedbacks from Datavault");
                    if (store == null) {
                        LogManager.writeLogInfo("Remove CPList from Datavault key value null");
                        Constants.removeDataValtFromSharedPref(context, Constants.CPList, deviceNo, false);
                    }else {
                        if (TextUtils.isEmpty(store)) {
                            LogManager.writeLogInfo("Remove CPList from Datavault key value empty");
                            Constants.removeDataValtFromSharedPref(context, Constants.CPList, deviceNo, false);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        set = sharedPreferences.getStringSet(Constants.ROList, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, context);
//                    LogManager.writeLogInfo("Remove Feedbacks from Datavault");
                    if (store == null) {
                        LogManager.writeLogInfo("Remove ROList from Datavault key value null");
                        Constants.removeDataValtFromSharedPref(context, Constants.ROList, deviceNo, false);
                    }else {
                        if (TextUtils.isEmpty(store)) {
                            LogManager.writeLogInfo("Remove ROList from Datavault key value empty");
                            Constants.removeDataValtFromSharedPref(context, Constants.ROList, deviceNo, false);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        set = sharedPreferences.getStringSet(Constants.Expenses, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, context);
//                    LogManager.writeLogInfo("Remove Feedbacks from Datavault");
                    if (store == null) {
                        LogManager.writeLogInfo("Remove Expenses from Datavault key value null");
                        Constants.removeDataValtFromSharedPref(context, Constants.Expenses, deviceNo, false);
                    }else {
                        if (TextUtils.isEmpty(store)) {
                            LogManager.writeLogInfo("Remove Expenses from Datavault key value empty");
                            Constants.removeDataValtFromSharedPref(context, Constants.Expenses, deviceNo, false);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private DaySyncAsyncTask asyncTask=null;
    private void initDaySync(){
        try {
            asyncTask = new DaySyncAsyncTask();
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new Thread(() -> ConstantsUtils.checkNetwork(this, isFailed -> {
                if (isFailed) {
                    runOnUiThread(() -> {
                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                        asyncTask.cancel(true);
                        Constants.showAlert(ErrorCode.NETWORK_ERROR, RegistrationActivity.this);
                    });
                }
            },false)).start();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public class AsyncPostOfflineData extends AsyncTask<Void, Void, Void> {
        String colloections ="";

        public AsyncPostOfflineData(String colloections) {
            this.colloections = colloections;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
                try {
                    if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                        try {
                            OfflineManager.flushQueuedRequests(RegistrationActivity.this, colloections);
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (ODataException e) {
                    e.printStackTrace();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private void userIDandPasswordcheck(){
        LogManager.writeLogError("Registration failed  due to "+errorMsg);
        if(errorMsg.contains("No address associated with hostname")){
            LogManager.writeLogError("Registration failed  due to network unavailability");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Constants.showAlert("Registration cannot be performed due to network unavailability", RegistrationActivity.this);
                }
            });
        }else if(errorMsg.contains("Maximum restarts reached")){
            if (com.arteriatech.mutils.common.UtilConstants.isNetworkAvailable(this)) {
                try {
                    com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, binding.editTextUserName.getText().toString().trim(), binding.editTextPassword.getText().toString().trim(), Configuration.APP_ID, jsonObject -> {
                        runOnUiThread(() -> {
                            int value =jsonObject.optInt("code");
                            if(value==0&&jsonObject.optString("message").contains("java.net.UnknownHostException")){
                                Constants.showAlert(ErrorCode.NETWORK_ERROR,RegistrationActivity.this);
                            }else{
//                                com.arteriatech.mutils.common.UtilConstants.passwordStatusErrorMessage(
//                                        RegistrationActivity.this,
//                                        jsonObject,
//                                        USER_NAME_DEVICE_ID, Configuration.IDPURL, Configuration.IDPTUSRNAME, Configuration.IDPTUSRPWD, Configuration.APP_ID);
                                Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject,registrationModel,binding.editTextUserName.getText().toString().trim());
                            }

                        });
                    });
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private class DaySyncAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                syncHistoryType="01";
//                try {
//                    BannerParser.downloadBannerFile(RegistrationActivity.this);
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                }
                /*if (!OfflineManager.isOfflineStoreOpen()) {
                    //                Constants.printLogInfo("check store is failed");
                    try {
                        try {
                            refguid = GUID.newRandom();
                            SyncUtils.updatingSyncStartTime(RegistrationActivity.this,Constants.Sync_All,Constants.StartSync,refguid.toString().toUpperCase());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        OfflineManager.refreshStoreSync(getApplicationContext(), RegistrationActivity.this, Constants.ALL, "");
                    } catch (OfflineODataStoreException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        try {
                            refguid =  GUID.newRandom();
                            SyncUtils.updatingDaySyncStartTime(RegistrationActivity.this,Constants.Sync_Day_login,Constants.StartSync,refguid.toString36().toUpperCase());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String syncType ="";
                        String def ="";
                        if (isFullSync){
                            syncType = Constants.ALL;
                            def="";
                        }else{
                            syncType=Constants.Fresh;
                            def=getRefreshList();
                        }
                        OfflineManager.refreshStoreSync(getApplicationContext(), new UIListener() {
                            @Override
                            public void onRequestError(int operation, Exception e) {
                                try {
                                    String errorMessage = e.getMessage();
                                    isClickable=false;
                                    ConstantsUtils.checkNetwork(RegistrationActivity.this,null,true);
                                    LogManager.writeLogError("store failed to open with an error : "+e.toString());
                                    if(e.toString().contains("HTTP Status 401 ? Unauthorized") || e.toString().contains("invalid authentication")) {
                                        try {
                                            Constants.writeLogsToInternalStorage(getApplicationContext(),"Initial Sync - onRequestError :\n " +
                                                    "log -onRequestError method called  with auth error \n"+e.toString());
                                            SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                            String loginUser = sharedPreferences.getString("username", "");
                                            String login_pwd = sharedPreferences.getString("password", "");

                                            Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, jsonObject -> runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                    Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject,registrationModel,binding.editTextUserName.getText().toString().trim());
                                                }
                                            }));
                                        } catch (Throwable throwable) {
                                            throwable.printStackTrace();
                                        }
                                    }*//*else if (errorMessage != null && (errorMessage.contains("503"))){
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                final boolean isImportDB = importDB(RegistrationActivity.this, RegistrationActivity.this);
                                                if (progressDialog!=null) {
                                                    progressDialog.dismiss();
                                                }
                                                if (!isImportDB) {//success
                                                    Constants.showAlert(e.toString(),RegistrationActivity.this);
                                                }else {
                                                    if (MSFAApplication.isSDA) {
                                                        startActivity(new Intent(RegistrationActivity.this, SDACommunicationDashboardActivity.class));
                                                    } else if (MSFAApplication.isAWSM) {

                                                        LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                                                        boolean isFaceEnabled = RegistrationActivity.this.getSharedPreferences(Constants.PREFS_NAME, 0).getBoolean(Constants.FACE_REINIT, false);
                                                        if (!isFaceEnabled) {
                                                            startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                            finish();
                                                        } else {
                                                            openCamera();
                                                        }

                                                    } else if (MSFAApplication.isPSM) {

                                                        LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                                                        startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                        finish();

                                                    } else if (MSFAApplication.isVAN) {

                                                        LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                                                        startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                        finish();

                                                    }
                                                }
                                            }
                                        });
                                    }*//*else if(e.getMessage() != null && (e.getMessage().contains("10345"))){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Unable to Synchronize due to poor internet connectivity [10345]. \n\n Please check internet and retry", "", getString(R.string.ok), false, new DialogCallBack() {
                                                    @Override
                                                    public void clickedStatus(boolean clickedStatus) {
                                                    }
                                                });
                                            }
                                        });
                                    }else if(e.getMessage() != null && (e.getMessage().contains("10346"))){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Unable to Synchronize due to poor internet connectivity [10346]. \n\n Please check internet and retry", "", getString(R.string.ok), false, new DialogCallBack() {
                                                    @Override
                                                    public void clickedStatus(boolean clickedStatus) {
                                                    }
                                                });
                                            }
                                        });
                                    }
                                    else if (e.getMessage() != null && (e.getMessage().contains("-10251")
                                            ||e.getMessage().contains("-10227")||
                                            e.getMessage().contains("InvalidStoreOptionValue")||
                                            e.getMessage().contains("InvalidInputParameters")||
                                            e.getMessage().contains("UnexpectedError"))&& !isReinitStore) {
                                        LogManager.writeLogError("store failed to open with an error but re-initializing store : \n"+e.toString());
                                        // Workaround for error "The file transfer failed due to an error on the server: -857 (SERVER_SYNCHRONIZATION_ERROR) (-10251)"
                                        // For this file transfer error with -10251 error, the current offline store need to be cleared for next initialization.
                                        OfflineManager.closeStore(RegistrationActivity.this);
                                        try {
                                            isReinitStore =true;
//                                            OfflineManager.openOfflineStore(RegistrationActivity.this, RegistrationActivity.this);
                                            OfflineManager.openOfflineStoreInternal(RegistrationActivity.this, RegistrationActivity.this);
                                        } catch (Throwable ex) {
                                            ex.printStackTrace();
                                            LogManager.writeLogError(Constants.error_txt + ex.getMessage());
                                        }
                                    }else if(e.toString().contains("404")){
                                        clearAppData404();
                                    }else{
                                        isReinitStore =false;
                                        ErrorBean errorBean = Constants.getErrorCode(operation, e, RegistrationActivity.this);
                                        if (errorBean.hasNoError()) {
                                            new DialogFactory.Progress(RegistrationActivity.this).hide();
//                Toast.makeText(RegistrationActivity.this, getString(R.string.err_odata_unexpected, e.getMessage()), Toast.LENGTH_LONG).show();
                                            String mStrPopUpText = "";
                                            try {
                                                mStrPopUpText = errorBean.getErrorMsg();
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                            if (TextUtils.isEmpty(mStrPopUpText)) {
                                                mStrPopUpText = getString(R.string.alert_sync_cannot_be_performed);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Constants.showAlert(errorBean.getErrorMsg(),RegistrationActivity.this);
                                                }
                                            });
                                        } else {
                                            try {
                                                new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (errorBean.getErrorMsg()!=null&&!TextUtils.isEmpty(errorBean.getErrorMsg())) {
                                                            Constants.showAlert(errorBean.getErrorMsg(),RegistrationActivity.this);
                                                        }else{
                                                            Constants.showAlert(e.toString(),RegistrationActivity.this);
                                                        }
                                                    }
                                                });
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }
                                            Constants.displayMsgReqError(errorBean.getErrorCode(), RegistrationActivity.this);
                                        }
                                    }

                                } catch (Throwable ex) {
                                    ex.printStackTrace();
                                    LogManager.writeLogError(Constants.error_txt + ex.getMessage());
                                }
                            }

                            @Override
                            public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                                isReinitStore =false;
                                isClickable=false;
                                ConstantsUtils.checkNetwork(RegistrationActivity.this,null,true);
                                if (operation == Operation.OfflineRefresh.getValue()) {
                                    Constants.updateIDPCredential(RegistrationActivity.this);
                                    getIDPSharedPrefValue(RegistrationActivity.this);
                                    try {
                                        BannerParser.extractBannerImages(RegistrationActivity.this);
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        OfflineManager.getAuthorizations(getApplicationContext());
                                    } catch (OfflineODataStoreException e) {
                                        e.printStackTrace();
                                    }
//                                    try {
//                                        if (OfflineManager.isOfflineStoreOpen()) {
//                                            LogManager.writeLogDebug("Day Sync DB Export");
//                                            SyncSelectionActivity.exportDB(RegistrationActivity.this);
//                                        }
//                                    } catch (Throwable e) {
//                                        e.printStackTrace();
//                                    }
                                    try {
                                        if (OfflineManager.isOfflineStoreOpen()) {
                                            MSFAApplication.getGAFields(RegistrationActivity.this);
                                        }
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        LogManager.writeLogDebug("SQL : Day sync completed removing SQL data");
                                        OutletSQLHelper helper = new OutletSQLHelper(getApplicationContext());
                                        helper.deleteChannelPartners();
                                        helper.deleteCPDMSDivisions();
                                        helper.deleteRouteSchPlans();
                                        CommonDB commonDB = new CommonDB(RegistrationActivity.this);
                                        commonDB.deleteAll();
                                    } catch (Throwable e) {

                                    }
                                    SharedPreferences sharedPerf = getSharedPreferences(Constants.PREFS_NAME, 0);
                                    SharedPreferences.Editor editor = sharedPerf.edit();
                                    editor.putString(Constants.ULPOTarget, "");
                                    editor.apply();
                                    Constants.updateLastSyncTimeToTable(RegistrationActivity.this, dayLoginCollectionList, refguid.toString().toUpperCase(), Constants.Sync_Day_login,null);
                                    try {
                                        refguid =  GUID.newRandom();
                                        SyncUtils.updatingDaySyncStartTime(RegistrationActivity.this,Constants.Sync_Day_login,Constants.EndSync,refguid.toString36().toUpperCase());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (MSFAApplication.isAWSM) {
                                        getCPConfigData();
                                    }else if (MSFAApplication.isBCRVAN) {
                                        getCPConfigData();
                                    }else if (MSFAApplication.isCallCenter) {
                                        getCPConfigData();
                                    }
                                    runOnUiThread(()-> {
                                        *//*if (progressDialog != null) {
                                            progressDialog.dismiss();
                                            afterPostNavigateDistributorScreen();
                                        }*//*
                                        onUpdateSync();
                                    });
                                }
                            }
                        }, syncType, def);
                    } catch (OfflineODataStoreException e) {
                        e.printStackTrace();
                    }
                }*/
                //storeBannerImages(RegistrationActivity.this,false,"");
            } catch (Throwable e) {
                LogManager.writeLogError("Error while doing day sync : \n"+e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }


    private void dayLogin(){
        if (!OfflineManager.isOfflineStoreOpen()) {
            //                Constants.printLogInfo("check store is failed");
            try {
                try {
                    refguid = GUID.newRandom();
                    SyncUtils.updatingSyncStartTime(RegistrationActivity.this,Constants.Sync_All,Constants.StartSync,refguid.toString().toUpperCase());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                OfflineManager.refreshStoreSync(getApplicationContext(), RegistrationActivity.this, Constants.ALL, "");
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
        } else {
            try {
                try {
                    refguid =  GUID.newRandom();
                    SyncUtils.updatingDaySyncStartTime(RegistrationActivity.this,Constants.Sync_Day_login,Constants.StartSync,refguid.toString36().toUpperCase());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String syncType ="";
                String def ="";
                if (isFullSync){
                    syncType = Constants.ALL;
                    def="";
                }else{
                    syncType=Constants.Fresh;
                    def=getRefreshList();
                }
                OfflineManager.refreshStoreSync(getApplicationContext(), new UIListener() {
                    @Override
                    public void onRequestError(int operation, Exception e) {
                        try {
                            String errorMessage = e.getMessage();
                            isClickable=false;
                            ConstantsUtils.checkNetwork(RegistrationActivity.this,null,true);
                            LogManager.writeLogError("store failed to open with an error : "+e.toString());
                            if(e.toString().contains("HTTP Status 401 ? Unauthorized") || e.toString().contains("invalid authentication")) {
                                try {
                                    Constants.writeLogsToInternalStorage(getApplicationContext(),"Initial Sync - onRequestError :\n " +
                                            "log -onRequestError method called  with auth error \n"+e.toString());
                                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                    String loginUser = sharedPreferences.getString("username", "");
                                    String login_pwd = sharedPreferences.getString("password", "");

                                    Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, jsonObject -> runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                                            Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject,registrationModel,binding.editTextUserName.getText().toString().trim());
                                        }
                                    }));
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }/*else if (errorMessage != null && (errorMessage.contains("503"))){
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                final boolean isImportDB = importDB(RegistrationActivity.this, RegistrationActivity.this);
                                                if (progressDialog!=null) {
                                                    progressDialog.dismiss();
                                                }
                                                if (!isImportDB) {//success
                                                    Constants.showAlert(e.toString(),RegistrationActivity.this);
                                                }else {
                                                    if (MSFAApplication.isSDA) {
                                                        startActivity(new Intent(RegistrationActivity.this, SDACommunicationDashboardActivity.class));
                                                    } else if (MSFAApplication.isAWSM) {

                                                        LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                                                        boolean isFaceEnabled = RegistrationActivity.this.getSharedPreferences(Constants.PREFS_NAME, 0).getBoolean(Constants.FACE_REINIT, false);
                                                        if (!isFaceEnabled) {
                                                            startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                            finish();
                                                        } else {
                                                            openCamera();
                                                        }

                                                    } else if (MSFAApplication.isPSM) {

                                                        LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                                                        startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                        finish();

                                                    } else if (MSFAApplication.isVAN) {

                                                        LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                                                        startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                        finish();

                                                    }
                                                }
                                            }
                                        });
                                    }*/else if(e.getMessage() != null && (e.getMessage().contains("10345"))){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                                        UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Unable to Synchronize due to poor internet connectivity [10345]. \n\n Please check internet and retry", "", getString(R.string.ok), false, new DialogCallBack() {
                                            @Override
                                            public void clickedStatus(boolean clickedStatus) {
                                            }
                                        });
                                    }
                                });
                            }else if(e.getMessage() != null && (e.getMessage().contains("10346"))){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                                        UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "", "Unable to Synchronize due to poor internet connectivity [10346]. \n\n Please check internet and retry", "", getString(R.string.ok), false, new DialogCallBack() {
                                            @Override
                                            public void clickedStatus(boolean clickedStatus) {
                                            }
                                        });
                                    }
                                });
                            }
                            else if (e.getMessage() != null && (e.getMessage().contains("-10251")
                                    ||e.getMessage().contains("-10227")||
                                    e.getMessage().contains("InvalidStoreOptionValue")||
                                    e.getMessage().contains("InvalidInputParameters")||
                                    e.getMessage().contains("UnexpectedError"))&& !isReinitStore) {
                                LogManager.writeLogError("store failed to open with an error but re-initializing store : \n"+e.toString());
                                // Workaround for error "The file transfer failed due to an error on the server: -857 (SERVER_SYNCHRONIZATION_ERROR) (-10251)"
                                // For this file transfer error with -10251 error, the current offline store need to be cleared for next initialization.
                                OfflineManager.closeStore(RegistrationActivity.this);
                                try {
                                    isReinitStore =true;
//                                            OfflineManager.openOfflineStore(RegistrationActivity.this, RegistrationActivity.this);
                                    OfflineManager.openOfflineStoreInternal(RegistrationActivity.this, RegistrationActivity.this);
                                } catch (Throwable ex) {
                                    ex.printStackTrace();
                                    LogManager.writeLogError(Constants.error_txt + ex.getMessage());
                                }
                            }else if(e.toString().contains("404")){
                                clearAppData404();
                            }else{
                                isReinitStore =false;
                                ErrorBean errorBean = Constants.getErrorCode(operation, e, RegistrationActivity.this);
                                if (errorBean.hasNoError()) {
                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
//                Toast.makeText(RegistrationActivity.this, getString(R.string.err_odata_unexpected, e.getMessage()), Toast.LENGTH_LONG).show();
                                    String mStrPopUpText = "";
                                    try {
                                        mStrPopUpText = errorBean.getErrorMsg();
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    if (TextUtils.isEmpty(mStrPopUpText)) {
                                        mStrPopUpText = getString(R.string.alert_sync_cannot_be_performed);
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Constants.showAlert(errorBean.getErrorMsg(),RegistrationActivity.this);
                                        }
                                    });
                                } else {
                                    try {
                                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (errorBean.getErrorMsg()!=null&&!TextUtils.isEmpty(errorBean.getErrorMsg())) {
                                                    Constants.showAlert(errorBean.getErrorMsg(),RegistrationActivity.this);
                                                }else{
                                                    Constants.showAlert(e.toString(),RegistrationActivity.this);
                                                }
                                            }
                                        });
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                    Constants.displayMsgReqError(errorBean.getErrorCode(), RegistrationActivity.this);
                                }
                            }

                        } catch (Throwable ex) {
                            ex.printStackTrace();
                            LogManager.writeLogError(Constants.error_txt + ex.getMessage());
                        }
                    }

                    @Override
                    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                        isReinitStore =false;
                        isClickable=false;
                        ConstantsUtils.checkNetwork(RegistrationActivity.this,null,true);
                        if (operation == Operation.OfflineRefresh.getValue()) {
                            Constants.updateIDPCredential(RegistrationActivity.this);
                            getIDPSharedPrefValue(RegistrationActivity.this);
//                            try {
//                                BannerParser.extractBannerImages(RegistrationActivity.this);
//                            } catch (Throwable e) {
//                                e.printStackTrace();
//                            }
                            try {
                                OfflineManager.getAuthorizations(getApplicationContext());
                            } catch (OfflineODataStoreException e) {
                                e.printStackTrace();
                            }
//                                    try {
//                                        if (OfflineManager.isOfflineStoreOpen()) {
//                                            LogManager.writeLogDebug("Day Sync DB Export");
//                                            SyncSelectionActivity.exportDB(RegistrationActivity.this);
//                                        }
//                                    } catch (Throwable e) {
//                                        e.printStackTrace();
//                                    }


                            SharedPreferences sharedPerf = getSharedPreferences(Constants.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPerf.edit();
                            editor.putString(Constants.ULPOTarget, "");
                            editor.apply();
                            Constants.updateLastSyncTimeToTable(RegistrationActivity.this, dayLoginCollectionList, refguid.toString().toUpperCase(), Constants.Sync_Day_login,null);
                            try {
                                refguid =  GUID.newRandom();
                                SyncUtils.updatingDaySyncStartTime(RegistrationActivity.this,Constants.Sync_Day_login,Constants.EndSync,refguid.toString36().toUpperCase());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (MSFAApplication.isAWSM) {
                                getCPConfigData();
                            }else if (MSFAApplication.isBCRVAN) {
                                getCPConfigData();
                            }else if (MSFAApplication.isCallCenter) {
                                getCPConfigData();
                            }
                            runOnUiThread(()-> {
                                        /*if (progressDialog != null) {
                                            progressDialog.dismiss();
                                            afterPostNavigateDistributorScreen();
                                        }*/
                                onUpdateSync();
                            });
                        }
                    }
                }, syncType, def);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
        }
    }

    private void afterPostNavigateDistributorScreen(){
        new DialogFactory.Progress(RegistrationActivity.this).hide();
        if (OfflineManager.isOfflineStoreOpen()) {
            try {
                if(TextUtils.isEmpty(AW_GEO_FENCING)){
                    getCPConfigData();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (MSFAApplication.isSDA) {
                //startActivity(new Intent(RegistrationActivity.this, SDACommunicationDashboardActivity.class));
            } else if (MSFAApplication.isAWSM) {
                boolean check = Arrays.asList(Constants.getDefiningReq(RegistrationActivity.this)).contains(Constants.Attendances);
                if(check){
                    SyncHistoryDB syncHistoryDB = new SyncHistoryDB(RegistrationActivity.this);
                    if (syncHistoryDB.getSyncTimeBySpecificColl(SyncHistoryDB.COL_COLLECTION, Constants.Attendances).isEmpty()) {
                        syncHistoryDB.createRecord(new SyncHistoryModel("", Constants.Attendances, UtilConstants.getSyncHistoryddmmyyyyTime()));
                    }
                }
               /* try {
                    boolean isVersionUpdate = AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, RegistrationActivity.this, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE, new AppUpgradeConfig.IAppUpdateListener() {
                        @Override
                        public void OnLaterListener() {
                            LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                                String AW_GEO_FENCING =  getSharedPreferences(Constants.PREFS_NAME, 0).getString("AW_GEO_FENCING","");
                            boolean ischannel = channelCheck();
                            if (ischannel) {
                                openCamera();
                            }else {
                                if (AW_GEO_FENCING != null && !TextUtils.isEmpty(AW_GEO_FENCING) && AW_GEO_FENCING.length() >= 3 && AW_GEO_FENCING.substring(1, 2).equalsIgnoreCase("Y")) {
                                    openCamera();
                                } else {
                                  //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                    finish();
                                }
                            }
                        }
                    });
                    if (!isVersionUpdate) {
                        LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                            String AW_GEO_FENCING =  getSharedPreferences(Constants.PREFS_NAME, 0).getString("AW_GEO_FENCING","");
                        boolean ischannel = channelCheck();
                        if (ischannel) {
                            openCamera();
                        }else {
                            if (AW_GEO_FENCING != null && !TextUtils.isEmpty(AW_GEO_FENCING) && AW_GEO_FENCING.length() >= 3 && AW_GEO_FENCING.substring(1, 2).equalsIgnoreCase("Y")) {
                                openCamera();
                            } else {
                              //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                finish();
                            }
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }*/
            }else if (MSFAApplication.isBCRVAN) {
                boolean check = Arrays.asList(Constants.getDefiningReq(RegistrationActivity.this)).contains(Constants.Attendances);
                if(check){
                    SyncHistoryDB syncHistoryDB = new SyncHistoryDB(RegistrationActivity.this);
                    if (syncHistoryDB.getSyncTimeBySpecificColl(SyncHistoryDB.COL_COLLECTION, Constants.Attendances).isEmpty()) {
                        syncHistoryDB.createRecord(new SyncHistoryModel("", Constants.Attendances, UtilConstants.getSyncHistoryddmmyyyyTime()));
                    }
                }
               /* try {
                    boolean isVersionUpdate = AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, RegistrationActivity.this, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE, new AppUpgradeConfig.IAppUpdateListener() {
                        @Override
                        public void OnLaterListener() {
                            LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                                String AW_GEO_FENCING =  getSharedPreferences(Constants.PREFS_NAME, 0).getString("AW_GEO_FENCING","");
                            boolean ischannel = channelCheck();
                            if (ischannel) {
                                openCamera();
                            }else {
                                if (AW_GEO_FENCING != null && !TextUtils.isEmpty(AW_GEO_FENCING) && AW_GEO_FENCING.length() >= 3 && AW_GEO_FENCING.substring(1, 2).equalsIgnoreCase("Y")) {
                                    openCamera();
                                } else {
                                    //startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                    finish();
                                }
                            }
                        }
                    });
                    if (!isVersionUpdate) {
                        LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                            String AW_GEO_FENCING =  getSharedPreferences(Constants.PREFS_NAME, 0).getString("AW_GEO_FENCING","");
                        boolean ischannel = channelCheck();
                        if (ischannel) {
                            openCamera();
                        }else {
                            if (AW_GEO_FENCING != null && !TextUtils.isEmpty(AW_GEO_FENCING) && AW_GEO_FENCING.length() >= 3 && AW_GEO_FENCING.substring(1, 2).equalsIgnoreCase("Y")) {
                                openCamera();
                            } else {
                              //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                finish();
                            }
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }*/
            }else if (MSFAApplication.isCallCenter) {
                boolean check = Arrays.asList(Constants.getDefiningReq(RegistrationActivity.this)).contains(Constants.Attendances);
                if(check){
                    SyncHistoryDB syncHistoryDB = new SyncHistoryDB(RegistrationActivity.this);
                    if (syncHistoryDB.getSyncTimeBySpecificColl(SyncHistoryDB.COL_COLLECTION, Constants.Attendances).isEmpty()) {
                        syncHistoryDB.createRecord(new SyncHistoryModel("", Constants.Attendances, UtilConstants.getSyncHistoryddmmyyyyTime()));
                    }
                }
                /*try {
                    boolean isVersionUpdate = AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, RegistrationActivity.this, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE, new AppUpgradeConfig.IAppUpdateListener() {
                        @Override
                        public void OnLaterListener() {
                            LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                                String AW_GEO_FENCING =  getSharedPreferences(Constants.PREFS_NAME, 0).getString("AW_GEO_FENCING","");
                            boolean ischannel = channelCheck();
                            if (ischannel) {
                                openCamera();
                            }else {
                                if (AW_GEO_FENCING != null && !TextUtils.isEmpty(AW_GEO_FENCING) && AW_GEO_FENCING.length() >= 3 && AW_GEO_FENCING.substring(1, 2).equalsIgnoreCase("Y")) {
                                    openCamera();
                                } else {
                                  //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                    finish();
                                }
                            }
                        }
                    });
                    if (!isVersionUpdate) {
                        LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                            String AW_GEO_FENCING =  getSharedPreferences(Constants.PREFS_NAME, 0).getString("AW_GEO_FENCING","");
                        boolean ischannel = channelCheck();
                        if (ischannel) {
                            openCamera();
                        }else {
                            if (AW_GEO_FENCING != null && !TextUtils.isEmpty(AW_GEO_FENCING) && AW_GEO_FENCING.length() >= 3 && AW_GEO_FENCING.substring(1, 2).equalsIgnoreCase("Y")) {
                                openCamera();
                            } else {
                               // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                finish();
                            }
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }*/
            }else if (MSFAApplication.isPSM) {
               /* try {
                    boolean isVersionUpdate = AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, RegistrationActivity.this, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE, new AppUpgradeConfig.IAppUpdateListener() {
                        @Override
                        public void OnLaterListener() {
                            LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                          //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                            finish();
                        }
                    });
                    if (!isVersionUpdate) {
                        LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                      //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                        finish();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }*/
            }else if (MSFAApplication.isVAN) {
               /* try {
                    boolean isVersionUpdate = AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, RegistrationActivity.this, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE, new AppUpgradeConfig.IAppUpdateListener() {
                        @Override
                        public void OnLaterListener() {
                            LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                                startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
//                                                                finish();
                            getAttendenceDetails();
                        }
                    });
                    if (!isVersionUpdate) {
                        LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                            startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
//                                                            finish();
                        getAttendenceDetails();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }*/
            }else if (MSFAApplication.isBVAN) {
               /* try {
                    boolean isVersionUpdate = AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, RegistrationActivity.this, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE, new AppUpgradeConfig.IAppUpdateListener() {
                        @Override
                        public void OnLaterListener() {
                            LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                                startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
//                                                                finish();
                            getAttendenceDetails();
                        }
                    });
                    if (!isVersionUpdate) {
                        LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
//                                                            startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
//                                                            finish();
                        getAttendenceDetails();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }*/
            }
        }
    }

    private void initInitialPasswordScreen(){
        LogManager.writeLogInfo("Registration successful navigate to initial password screen");
        Intent intentNavChangePwdScreen = new Intent(this, InitialPasswordActivity.class);
        intentNavChangePwdScreen.putExtra(com.arteriatech.mutils.common.UtilConstants.RegIntentKey, registrationModel);
        intentNavChangePwdScreen.putExtra("isRegisteration", true);
        intentNavChangePwdScreen.putExtra("P_USER_ID", PUserID);
        startActivity(intentNavChangePwdScreen);
        finish();
    }

    private void checkInitialPasswordStatus(){
        try {
            new Thread(() -> {
                try {
                    com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, binding.editTextUserName.getText().toString().trim(), binding.editTextPassword.getText().toString().trim(), Configuration.APP_ID, jsonObject -> {
                        JSONObject jsonUsrDetails = null;

                        try {
                            String jsonValue = com.arteriatech.mutils.common.UtilConstants.getUserDetails(jsonObject, registrationModel.getIDPURL(), registrationModel.getExternalTUserName(),registrationModel.getExternalTPWD());

                            try {
                                if (!TextUtils.isEmpty(jsonValue)) {
                                    jsonUsrDetails = new JSONObject(jsonValue);
                                    passwordStatus = jsonUsrDetails.optString("passwordStatus");
                                    PUserID = jsonUsrDetails.optString("id");
                                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME,0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("p_user_id",PUserID);
                                    editor.apply();
                                }
                            } catch (Throwable var4) {
                                var4.printStackTrace();
                            }
                        } catch (IOException var5) {
                            var5.printStackTrace();
                        }
                    });
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Throwable e1) {
            e1.printStackTrace();
        }
    }
    private void increaseSharedPerfVal(String versionCode, int version) {
        SharedPreferences.Editor editor = sharedPerf.edit();
        try {
            editor.putInt(versionCode, version);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reInitializeStore(){
        if (getSharedPreferences(Constants.PREFS_NAME,0).getInt(Constants.CURRENT_VERSION_CODE, 0) == Constants.NewDefingRequestVersion) {
            refreshStore();
        } else {
            if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                new DialogFactory.Progress(RegistrationActivity.this).hide();
                new DialogFactory.Progress(RegistrationActivity.this).setTitle("Registration").setMessage("Updating application. Please wait...").setTheme(R.style.MaterialAlertDialog).show();
                message = "Updating application. Please wait...";
                try {
                    if(OfflineManager.offlineStore!=null) {
                        if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                            try {
                                OfflineManager.flushQueuedRequests(new UIListener() {
                                    @Override
                                    public void onRequestError(int i, Exception e) {
                                        isClickable=false;
                                        LogManager.writeLogError("before DB reinit error while flush "+e.toString());
                                        refreshStore();
                                    }

                                    @Override
                                    public void onRequestSuccess(int i, String s) throws ODataException, OfflineODataStoreException {
                                        isClickable=false;
                                        increaseSharedPerfVal(Constants.CURRENT_VERSION_CODE, Constants.NewDefingRequestVersion);
                                        Constants.closeStore(RegistrationActivity.this);
                                        LogManager.writeLogError("DB reinitialized refreshing data");
                                        new RefreshAsyncTask(RegistrationActivity.this, "", RegistrationActivity.this).execute();
                                    }
                                }, "");
                            } catch (OfflineODataStoreException e) {
                                refreshStore();
                                e.printStackTrace();
                            }
                        } else {
                            if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                                Constants.isSync = true;
                                increaseSharedPerfVal(Constants.CURRENT_VERSION_CODE, Constants.NewDefingRequestVersion);
                                Constants.closeStore(RegistrationActivity.this);
                                new RefreshAsyncTask(RegistrationActivity.this, "", this).execute();
                            } else {
                                refreshStore();
                            }
                        }
                    }else {
                        refreshStore();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                refreshStore();
            }
        }
    }

    private void refreshStore() {
       /* try {
            boolean isVersionUpdate = AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, RegistrationActivity.this, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE, new AppUpgradeConfig.IAppUpdateListener() {
                @Override
                public void OnLaterListener() {
                    LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                    boolean isFaceEnabled = RegistrationActivity.this.getSharedPreferences(Constants.PREFS_NAME,0).getBoolean(Constants.FACE_REINIT,false);
                    if (!isFaceEnabled) {
                        if(MSFAApplication.isBVAN){
                          //  startActivity(new Intent(RegistrationActivity.this, BreadDashBoard.class));
                            finish();
                        }else {
                          //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                            finish();
                        }
                    }else{
                        openCamera();
                    }
                }
            });
            if (!isVersionUpdate) {
                LogManager.writeLogError("store opened successfully  & navigating to DashBoard");
                boolean isFaceEnabled = RegistrationActivity.this.getSharedPreferences(Constants.PREFS_NAME,0).getBoolean(Constants.FACE_REINIT,false);
                if (!isFaceEnabled) {
                    if(MSFAApplication.isBVAN){
                      //  startActivity(new Intent(RegistrationActivity.this, BreadDashBoard.class));
                        finish();
                    }else {
                      //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                        finish();
                    }
                }else{
                    openCamera();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }*/
    }
    public static ArrayList<String>dayLoginCollectionList = new ArrayList<>();
    private String getRefreshList(){
        ArrayList<String>list = new ArrayList<>();
        String rollID = ConstantsUtils.getAuthInformation(this);

        if(rollID.equalsIgnoreCase(Constants.PSM)) {
            list.add(Constants.CPSPRelations);
            list.add(Constants.ChannelPartners);
            list.add(Constants.CPDMSDivisions);
            list.add(Constants.CPPartnerFunctions);
            list.add(Constants.CPConfigs);
            list.addAll(SyncUtils.getBeat(this));
        }else if(MSFAApplication.isVAN){
            list.add("CPConfigs");
            list.add("Attendances");
            list.add("ChannelPartners");
            list.add("SalesPersons");
            list.add("UserSalesPersons");
            list.add("RouteSchedulePlans");
            list.add("RouteScheduleSPs");
            list.add("RouteScheduleDetails");
            list.add("RouteProductDetTypes");
            list.add(Constants.SyncHistroy);
            list.add(Constants.UserPartners);
            list.add("SPStockItems");
            list.add("SPStockItemSNos");
            list.add("RouteSchedules");
            list.add("CPSPRelations");
            list.add(Constants.Schemes);
            list.add(Constants.SchemeItemDetails);
            list.add(Constants.SchemeSlabs);
            list.add(Constants.SchemeGeographies);
            list.add(Constants.SchemeCPs);
            list.add(Constants.SchemeSalesAreas);
            list.add(Constants.SSInvoiceTypes);
            list.add(Constants.SSINVOICES);
            list.add(Constants.SSInvoiceItemDetails);
            list.add("Visits");
            list.add("VisitActivities");
            list.add("UserProfileAuthSet");
            list.add("ConfigTypsetTypeValues");
            list.add("ConfigTypesetTypes");
            list.add("ValueHelps");
        } else if (MSFAApplication.isBVAN) {
            list.add("CPConfigs");
            list.add("Attendances");
            list.add("ChannelPartners");
            list.add("SalesPersons");
            list.add("UserSalesPersons");
            list.add("RouteSchedulePlans");
            list.add("RouteScheduleSPs");
            list.add("RouteScheduleDetails");
            list.add("RouteProductDetTypes");
            list.add(Constants.SyncHistroy);
            list.add(Constants.UserPartners);
            list.add("SPStockItems");
            list.add("SPStockItemSNos");
            list.add("RouteSchedules");
            list.add("CPSPRelations");
            list.add(Constants.Schemes);
            list.add(Constants.SchemeItemDetails);
            list.add(Constants.SchemeSlabs);
            list.add(Constants.SchemeGeographies);
            list.add(Constants.SchemeCPs);
            list.add(Constants.SchemeSalesAreas);
            list.add(Constants.SSInvoiceTypes);
            list.add(Constants.SSInvoiceItemDetails);
            list.add("Visits");
            list.add("VisitActivities");
            list.add("UserProfileAuthSet");
            list.add("ConfigTypsetTypeValues");
            list.add("ConfigTypesetTypes");
            list.add("ValueHelps");
        }else {
        /*list.add(Constants.SalesPersons);
        list.add(Constants.UserSalesPersons);
        list.add(Constants.CPSPRelations);*/
            list.add(Constants.CPSPRelations);
            list.add(Constants.ChannelPartners);
            list.add(Constants.CPDMSDivisions);
            list.add(Constants.CPPartnerFunctions);
            list.add(Constants.CPConfigs);
            list.add(Constants.Attendances);
            list.add(Constants.SalesPersons);
//        list.add(Constants.SSINVOICES);

        /*list.add(Constants.SubDistricts);
        list.add(Constants.Towns);*/

            list.add(Constants.Schemes);
            list.add(Constants.SchemeItemDetails);
            list.add(Constants.SchemeSlabs);
            list.add(Constants.SchemeGeographies);
            list.add(Constants.SchemeCPs);
            list.add(Constants.SchemeSalesAreas);

        /*list.add(Constants.KPISet);
        list.add(Constants.Targets);
        list.add(Constants.KPIItems);*/
            list.add(Constants.TargetItems);
            list.add(Constants.Questions);
            list.add(Constants.SPPayouts);
            list.add(Constants.VisitQuestionnaires);
            list.add(Constants.KPISet);
            list.add(Constants.Targets);
            list.add(Constants.KPIItems);
            list.add(Constants.Brands);
            list.add(Constants.OrderMaterialGroups);
            list.add(Constants.BrandsCategories);
            list.add(Constants.SSSOs);
            list.add(Constants.SSSoItemDetails);
            list.add(Constants.CPStockItems);
            list.add(Constants.CPStockItemSnos);
            list.add(Constants.SegmentedMaterials);
            list.add(Constants.CPPerformances);
            list.add(Constants.ConfigTypsetTypeValues);
            list.add(Constants.ConfigTypesetTypes);
            list.add("UserProfileAuthSet");
            list.add(Constants.CPSummary1Set);
            list.addAll(SyncUtils.getBeat(this));
        }
        dayLoginCollectionList = new ArrayList<>();
        dayLoginCollectionList.addAll(list);
        concatCollectionStr="";
        return concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(list);
    }

    private void lauchApplication(){
        try {
            Intent i = getPackageManager().getLaunchIntentForPackage("com.arteriatech.ss.msec.bil");
            startActivity(i);
            LogManager.writeLogInfo("Lauching the application successful");
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            LogManager.writeLogInfo("Lauching the application failed : "+e.getMessage());
        }
    }

    private void deRegister() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String requestUri = UtilRegistrationActivity.getAppURL(Configuration.server_Text, Configuration.port_Text, Configuration.APP_ID, Configuration.IS_HTTPS);
                    AndroidSystem.setContext(RegistrationActivity.this);
                    OnlineODataProvider registrationProvider = new OnlineODataProvider("SmpClient", requestUri);
                    registrationProvider.getServiceOptions().setCheckVersion(false);
                    SmpClient smpClient = new SmpClient(registrationProvider);

                    SharedPreferences preferences = getSharedPreferences(Constants.PREFS_NAME, 0);

                    Connection newConnection = new Connection();
                    newConnection.setDeviceType("Android");
                    newConnection.setApplicationConnectionId(preferences.getString(UtilRegistrationActivity.KEY_appConnID, ""));
                    newConnection.setUserName(preferences.getString(UtilRegistrationActivity.KEY_username, ""));
                    if (newConnection != null) {
                        smpClient.deleteEntity(newConnection);
                        if (OfflineManager.isOfflineStoreOpen()) {
                            try {
                                OfflineManager.closeOfflineStore(RegistrationActivity.this, OfflineManager.options);
                                OfflineManager.offlineStore = null;
                            } catch (OfflineODataStoreException e) {
                                e.printStackTrace();
                            }
                        }
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        //for passCode
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);
                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor1.clear();
                        editor1.apply();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    pdLoadDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(RegistrationActivity.this, RegistrationActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                } catch (DataServiceException e) {
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
       /* LogonCore.getInstance().deregister();
        if (OfflineManager.isOfflineStoreOpen()) {
            try {
                OfflineManager.closeOfflineStore(this,OfflineManager.options);
                OfflineManager.offlineStore=null;
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
        }
        OnlineStoreListener openListener = OnlineStoreListener.getInstance();
        OnlineODataStore store = openListener.getStore();
        if(store!=null)
        {
            openListener.closeStore();
        }
        SharedPreferences preferences = getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        //for passCode
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor1 = sharedPreferences.edit();
        editor1.clear();
        editor1.commit();
        Intent intent=new Intent(MainActivity.this, RegistrationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();*/
    }
    public static boolean importDB(RegistrationModel regModel) {
        /*if (OfflineManager.isOfflineStoreOpen()) {
            try {
                OfflineManager.closeOfflineStore();
                LogManager.writeLogError(mContext.getString(R.string.msg_sync_terminated));
            } catch (OfflineODataStoreException e) {
                LogManager.writeLogError(Constants.error_during_offline_close + e.getMessage());
            }
        }*/

        File isd = Environment.getExternalStorageDirectory();
        File idata = Environment.getDataDirectory();
        FileChannel isource = null;
        FileChannel idestination = null;
        String currentDBPath = regModel.getOfflineDBPath();
        String currentrqDBPath = regModel.getOfflineReqDBPath();

        String backupDBPath = regModel.getIbackupUDBPath();
        String backuprqDBPath = regModel.getIbackupRqDBPath();
        File ibackupDB = new File(idata, backupDBPath);
        File icurrentDB = new File(isd, currentDBPath);

        File ibackupRqDB = new File(idata, backuprqDBPath);
        File icurrentRqDB = new File(isd, currentrqDBPath);
        try {
            isource = new FileInputStream(icurrentDB).getChannel();
            idestination = new FileOutputStream(ibackupDB).getChannel();
            idestination.transferFrom(isource, 0, isource.size());

            isource = new FileInputStream(icurrentRqDB).getChannel();
            idestination = new FileOutputStream(ibackupRqDB).getChannel();
            idestination.transferFrom(isource, 0, isource.size());

            isource.close();
            /*if (!OfflineManager.isOfflineStoreOpen()) {
                try {
                    OfflineManager.openOfflineStore(mContext, uiListener);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
            }*/
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void exportLog() {
        String sourcePath = "/data/data/com.arteriatech.ss.msec.bil/files/";

        File source = new File(sourcePath);

        String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File destination = new File(destinationPath);
        try
        {
            FileUtils.copyDirectory(source, destination);
            Toast.makeText(this, "Logs exported successfully", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            Toast.makeText(this, "Logs failed to export", Toast.LENGTH_SHORT).show();
        }
    }
    private List distributorBeanList;
    public void openCamera() {
        try {
//            startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
//            finish();
            boolean isAttendanceExist = getAttendance();
            if (!isAttendanceExist) {
//                if(refguid==null){
                refguid =  GUID.newRandom();
//                }
                SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_START,Constants.StartSync,refguid.toString36().toUpperCase());
                startActivityForResult(new Intent(this, CameraXActivity.class), ConstantsUtils.CAMERA_REQUEST_CODE);
            } else {
               // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postFaceRecognitionImage(File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int status = 0;
                String error_message= null;
                boolean person_match= false;
                try {
                    MultipartUtility multipart = null;
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new DialogFactory.Progress(RegistrationActivity.this).setTitle("Registration").setMessage("Photo recognition in progress. Please wait...").setTheme(R.style.MaterialAlertDialog).show();
                                message = "Photo recognition in progress. Please wait...";
                            }
                        });


                        String[] faceCredential=Constants.getFaceDetails();
                        String requestURL="";
                        if (!faceCredential[0].isEmpty()){
                            requestURL = faceCredential[0];
                        }
                        LogManager.writeLogDebug("Face Recogn url"+requestURL);
                        multipart = null;
                        try {
                            if (!faceCredential[1].isEmpty()) {
                                multipart = new MultipartUtility(requestURL, "UTF-8",faceCredential[1],"");
                            }else{
                                multipart = new MultipartUtility(requestURL, "UTF-8","","");
                            }
                        } catch (IOException e) {
                            LogManager.writeLogInfo("Face Recgn error int multi obj: "+e.getMessage());
                            e.printStackTrace();
                        }

                        // In your case you are not adding form data so ignore this
                        /*This is to add parameter values */
                        if (multipart!=null) {
                            String soNo = "";
                            try {
                                soNo = OfflineManager.getValueDetailsByColumnName(Constants.UserSalesPersons + "?$select=" + Constants.SPNo, Constants.SPNo);
                            } catch (Exception e) {
                                LogManager.writeLogInfo("Face Recgn error awsn_id : "+e.getMessage());
                                e.printStackTrace();
                            }
                            multipart.addFormField("awsm_id", soNo);
//                            multipart.addFormField("synchronous", String.valueOf(true));
                            LogManager.writeLogDebug("Face Recogn awsn_id = "+soNo);
                            LogManager.writeLogDebug("Face Recogn synchronous = "+String.valueOf(true));
                            try {
                                multipart.addFilePart("img", file);
                                LogManager.writeLogDebug("Face Recogn img = "+file);
                                LogManager.writeLogDebug("Face Recogn img file length = "+file.length());

                            } catch (IOException e) {
                                LogManager.writeLogInfo("Face Recgn error set headers in multi obj: "+e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    } catch (Throwable e) {
                        LogManager.writeLogInfo("Face Recgn error form multi obj: "+e.getMessage());
                        e.printStackTrace();
                    }


                    List<String> response = null;
                    if (multipart!=null) {
                        try {
                            response = multipart.finish();
                        } catch (Throwable e) {
                            LogManager.writeLogInfo("Face Recgn posting : "+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    status = 0;
                    error_message = null;
                    person_match = false;
                    if (response!=null) {
                        String responseString = null;
                        for (String line : response) {
                            responseString = line;
                        }
                        status = 0;
                        error_message = "";
                        person_match = false;
                        LogManager.writeLogInfo("Face Recognition response data : "+responseString);
                        try {
                            if (responseString!=null) {
                                JSONObject jsonObject = new JSONObject(responseString);
                                try {
                                    status = jsonObject.getInt("status");
                                    LogManager.writeLogInfo("Face Recognition response status : "+status);
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                                try {
                                    error_message = jsonObject.getString("message");
                                    LogManager.writeLogInfo("Face Recognition response error message : "+error_message);
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                                try {
                                    if (jsonObject.has("person_match")) {
                                        person_match = jsonObject.getBoolean("person_match");
                                    }
                                    LogManager.writeLogInfo("Face Recognition response person match : "+person_match);
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME,0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                LogManager.writeLogInfo("Face Recognition response code : "+status);
                if (status==601){
                    editor.putBoolean(Constants.START_MY_ATT,true);
                    editor.apply();
                    LogManager.writeLogInfo("Face recognized Successfully");
                    boolean isAttendanceExist = getAttendance();
                    if (!isAttendanceExist) {
                        onSave();
                    }else{
                        if (passwordStatus!=null&&passwordStatus.equalsIgnoreCase("initial")) {
                            initInitialPasswordScreen();
                        }else{
                            navigativeToDistributorScree(message,RegistrationActivity.this,"Face Recognition ");
                        }
                    }
                }else if(status==602){
                    editor.putBoolean(Constants.START_MY_ATT,false);
                    editor.apply();
                    String  message = "Your face not matching with the KYCed image \n 1) Click Try again to take photo again.\n 2)Click Continue to take order.Your attendance will not marked for today. Please connect with your ASE to get clarification" ;
                    LogManager.writeLogInfo("Face Recognition error message : "+message);
                    if (passwordStatus!=null&&passwordStatus.equalsIgnoreCase("initial")) {
                        initInitialPasswordScreen();
                    }else{
                        navigativeToDistributorScree(message,RegistrationActivity.this,"Face Recognition ");
                    }
                }else if(status==603){
                    editor.putBoolean(Constants.START_MY_ATT,false);
                    editor.apply();
                    String  message = "The quality of the captured image is not proper. Please capture the new image and re-try";
                    LogManager.writeLogInfo("Face Recognition error message : "+message);
                    if (passwordStatus!=null&&passwordStatus.equalsIgnoreCase("initial")) {
                        initInitialPasswordScreen();
                    }else{
                        navigativeToDistributorScree(message,RegistrationActivity.this,"Face Recognition ");
                    }
                }else if(status==604){
                    editor.putBoolean(Constants.START_MY_ATT,false);
                    editor.apply();
                    LogManager.writeLogInfo("Face Recognition error message : "+error_message);
                    if (passwordStatus!=null&&passwordStatus.equalsIgnoreCase("initial")) {
                        initInitialPasswordScreen();
                    }else{
                        navigativeToDistributorScree(error_message,RegistrationActivity.this,"Face Recognition ");
                    }
                }
                else if(status==606){
                    editor.putBoolean(Constants.START_MY_ATT,false);
                    editor.apply();
                    String  message = "Face Recognition API issue. Please contact support team";
                    LogManager.writeLogInfo("Face Recognition error message : "+message);
                    if (passwordStatus!=null&&passwordStatus.equalsIgnoreCase("initial")) {
                        initInitialPasswordScreen();
                    }else{
                        navigativeToDistributorScree(message,RegistrationActivity.this,"Face Recognition ");
                    }
                }else{
                    editor.putBoolean(Constants.START_MY_ATT,false);
                    editor.apply();
                    String  message = "Error in recognition. Please contact support team";
                    LogManager.writeLogInfo("Face Recognition error message : "+message);
                    navigativeToDistributorScree(message,RegistrationActivity.this,"Face Recognition ");
                }

            }
        }).start();
    }

    private int statusTemp = 0;
    private String remark1 = "";
    public void postFaceRecognitionImage1(File file) {
        remark1 = "";
        new Thread(new Runnable() {
            @Override
            public void run() {
                int status = 0;
                int httpcode = 0;
                statusTemp = 0;
                String error_message= null;
                boolean person_match= false;
                try {
//                    if(refguid==null){
                    refguid =  GUID.newRandom();
//                    }
                    SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_POST,Constants.StartSync,refguid.toString36().toUpperCase());

                    MultipartUtility multipart = null;
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new DialogFactory.Progress(RegistrationActivity.this).setTitle("Registration").setMessage("Photo recognition in progress. Please wait...").setTheme(R.style.MaterialAlertDialog).show();
                                message = "Photo recognition in progress. Please wait...";
                            }
                        });


//                        String[] faceCredential=Constants.getFaceDetails();
                        String requestURL="";
                        String basicAuth="";
//                        if (!faceCredential[0].isEmpty()){
                        requestURL = MyUtils.getDefaultEndPointFaceRegocination();
//                        }
                        basicAuth = MyUtils.getBasicAuthCredential(RegistrationActivity.this);
                        LogManager.writeLogDebug("Face Recogn url"+requestURL);
                        multipart = null;
                        try {
//                            if (!faceCredential[1].isEmpty()) {
//                                multipart = new MultipartUtility(requestURL, "UTF-8",faceCredential[1]);
//                            }else{
                            multipart = new MultipartUtility(requestURL, "UTF-8",basicAuth, APP_ID);
//                            }
                        } catch (IOException e) {
                            LogManager.writeLogInfo("Face Recgn error int multi obj: "+e.getMessage());
                            e.printStackTrace();
                        }

                        // In your case you are not adding form data so ignore this
                        /*This is to add parameter values */
                        if (multipart!=null) {
                            String soNo = "";
                            try {
                                soNo = OfflineManager.getValueDetailsByColumnName(Constants.UserSalesPersons + "?$select=" + Constants.SPNo, Constants.SPNo);
                            } catch (Exception e) {
                                LogManager.writeLogInfo("Face Recgn error awsn_id : "+e.getMessage());
                                e.printStackTrace();
                            }
                            multipart.addFormField("awsm_id", soNo);
//                            multipart.addFormField("synchronous", String.valueOf(true));
                            LogManager.writeLogDebug("Face Recogn awsn_id = "+soNo);
                            LogManager.writeLogDebug("Face Recogn synchronous = "+String.valueOf(true));
                            try {
                                multipart.addFilePart("img", file);
                                LogManager.writeLogDebug("Face Recogn img = "+file);
                                LogManager.writeLogDebug("Face Recogn img file length = "+file.length());

                            } catch (IOException e) {
                                LogManager.writeLogInfo("Face Recgn error set headers in multi obj: "+e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    } catch (Throwable e) {
                        LogManager.writeLogInfo("Face Recgn error form multi obj: "+e.getMessage());
                        e.printStackTrace();
                    }


                    JSONObject response = null;
                    if (multipart!=null) {
                        try {
                            response = multipart.finish1();
                        } catch (Throwable e) {
                            LogManager.writeLogInfo("Face Recgn posting : "+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    status = 0;
                    error_message = null;
                    person_match = false;
//                    if(refguid==null){
                    refguid =  GUID.newRandom();
//                    }
                    SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_POST_END,Constants.StartSync,refguid.toString36().toUpperCase());

                    if (response!=null) {
                        String responseString = "";
                        status = 0;
                        httpcode = 0;
                        if(response.has(Constants.HTTPCODE)){
                            httpcode = response.optInt(Constants.HTTPCODE);
                        }
                        if(response.has(Constants.ResponseBody)){
                            responseString = response.optString(Constants.ResponseBody);
                        }
                        error_message = "";
                        person_match = false;
                        LogManager.writeLogInfo("Face Recognition response data : "+responseString);
                        if(httpcode==200) {
                            try {
                                if (responseString != null) {
                                    JSONObject jsonObject = new JSONObject(responseString);
                                    try {
                                        status = jsonObject.getInt("status");
                                        LogManager.writeLogInfo("Face Recognition response status : " + status);
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        error_message = jsonObject.getString("message");
                                        LogManager.writeLogInfo("Face Recognition response error message : " + error_message);
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        if (jsonObject.has("person_match")) {
                                            person_match = jsonObject.getBoolean("person_match");
                                        }
                                        LogManager.writeLogInfo("Face Recognition response person match : " + person_match);
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt(Constants.FR_STATUS, status);
                                    editor.apply();
                                    LogManager.writeLogInfo("Face Recognition response code : " + status);
                                    statusTemp = status;
                                    if (status == 601) {
//                                        if(refguid==null){
                                        refguid =  GUID.newRandom();
//                                        }
                                        remark1="F";
                                        SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_STATUS_601,Constants.StartSync,refguid.toString36().toUpperCase());

                                        editor.putBoolean(Constants.START_MY_ATT, true);
                                        editor.apply();
                                        LogManager.writeLogInfo("Face recognized Successfully");
                                        boolean isAttendanceExist = getAttendance();
                                        if (!isAttendanceExist) {
                                            onSave();
                                        } else {
                                            if (passwordStatus != null && passwordStatus.equalsIgnoreCase("initial")) {
                                                initInitialPasswordScreen();
                                            } else {
                                                navigativeToDistributorScree(message, RegistrationActivity.this, "Face Recognition ");
                                            }
                                        }
                                    } else if (status == 602) {
//                                        if(refguid==null){
                                        refguid =  GUID.newRandom();
//                                        }
                                        SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_STATUS_602,Constants.StartSync,refguid.toString36().toUpperCase());

                                        editor.putBoolean(Constants.START_MY_ATT, false);
                                        editor.apply();
                                        String message = "Your face not matching with the Base image \n 1) Click Try again to take photo again.\n 2)Click Continue to take order.Your attendance will not marked for today. Please connect with your ASE/Help Desk to update your base image";
                                        LogManager.writeLogInfo("Face Recognition error message : " + message);
                                        if (passwordStatus != null && passwordStatus.equalsIgnoreCase("initial")) {
                                            initInitialPasswordScreen();
                                        } else {
                                            navigativeToDistributorScree(message, RegistrationActivity.this, "Face Recognition ");
                                        }
                                    } else if (status == 603) {
//                                        if(refguid==null){
                                        refguid =  GUID.newRandom();
//                                        }
                                        SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_STATUS_603,Constants.StartSync,refguid.toString36().toUpperCase());

                                        editor.putBoolean(Constants.START_MY_ATT, false);
                                        editor.apply();
                                        String message = "The quality of the captured image is not proper. Please capture the new image and re-try";
                                        LogManager.writeLogInfo("Face Recognition error message : " + message);
                                        if (passwordStatus != null && passwordStatus.equalsIgnoreCase("initial")) {
                                            initInitialPasswordScreen();
                                        } else {
                                            navigativeToDistributorScree(message, RegistrationActivity.this, "Face Recognition ");
                                        }
                                    } else if (status == 604) {
//                                        if(refguid==null){
                                        refguid =  GUID.newRandom();
//                                        }
                                        SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_STATUS_604,Constants.StartSync,refguid.toString36().toUpperCase());

                                        editor.putBoolean(Constants.START_MY_ATT, false);
                                        editor.apply();
                                        LogManager.writeLogInfo("Face Recognition error message : " + error_message);
                                        if (passwordStatus != null && passwordStatus.equalsIgnoreCase("initial")) {
                                            initInitialPasswordScreen();
                                        } else {
                                            navigativeToDistributorScree(error_message, RegistrationActivity.this, "Face Recognition ");
                                        }
                                    } else if (status == 606) {
//                                        if(refguid==null){
                                        refguid =  GUID.newRandom();
//                                        }
                                        SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_STATUS_606,Constants.StartSync,refguid.toString36().toUpperCase());

                                        editor.putBoolean(Constants.START_MY_ATT, false);
                                        editor.apply();
                                        String message = "Face Recognition API issue. Please contact support team";
                                        LogManager.writeLogInfo("Face Recognition error message : " + message);
                                        if (passwordStatus != null && passwordStatus.equalsIgnoreCase("initial")) {
                                            initInitialPasswordScreen();
                                        } else {
                                            navigativeToDistributorScree(message, RegistrationActivity.this, "Face Recognition ");
                                        }
                                    } else {
//                                        if(refguid==null){
                                        refguid =  GUID.newRandom();
//                                        }
                                        SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_STATUS_607,Constants.StartSync,refguid.toString36().toUpperCase());

                                        editor.putBoolean(Constants.START_MY_ATT, false);
                                        editor.apply();
                                        String message = "Error in recognition. Please contact support team";
                                        LogManager.writeLogInfo("Face Recognition error message : " + message);
                                        navigativeToDistributorScree(message, RegistrationActivity.this, "Face Recognition ");
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
//                            if(refguid==null){
                            refguid =  GUID.newRandom();
//                            }
                            SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_STATUS_607,Constants.StartSync,refguid.toString36().toUpperCase());

                            SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(Constants.START_MY_ATT, false);
                            editor.putInt(Constants.FR_STATUS, httpcode);
                            editor.apply();
                            String message = responseString+ "("+httpcode+"). Please contact support team";
                            LogManager.writeLogInfo("Face Recognition response http code : " + httpcode);
                            LogManager.writeLogInfo("Face Recognition error message : " + message);
                            navigativeToDistributorScree(message, RegistrationActivity.this, "Face Recognition ");
                        }
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new DialogFactory.Progress(RegistrationActivity.this).hide();
                                SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(Constants.START_MY_ATT, false);
                                editor.putInt(Constants.FR_STATUS, 0);
                                editor.apply();
//                                if(refguid==null){
                                refguid =  GUID.newRandom();
//                                }
                                SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_STATUS_607,Constants.StartSync,refguid.toString36().toUpperCase());

                                String message = "Error in recognition. Please contact support team";
                                LogManager.writeLogInfo("Face Recognition error message : " + message);
                                navigativeToDistributorScree(message, RegistrationActivity.this, "Face Recognition ");
                            }
                        });
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showAlert(String message, Context context,boolean isfalg) {
        runOnUiThread(() -> {
            new DialogFactory.Progress(RegistrationActivity.this).hide();
            //            Toast.makeText(CameraXActivity.this,"Success",Toast.LENGTH_LONG);

            Constants.showAlert(message,context);
//            try {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context,  R.style.DialogTheme);
//                String timeStamp = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm", new Date());
//                builder.setTitle(timeStamp);
//                builder.setMessage(message).setCancelable(false)
//                        .setPositiveButton(context.getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                /*if(isfalg){
//                                    try {
//                                        runOnUiThread(() -> relativeLayoutPreview.setVisibility(View.GONE));
//                                    } catch (Throwable e) {
//                                        e.printStackTrace();
//                                    }
//                                }else {
////                                    startActivity(new Intent(CameraXActivity.this, DashBoard.class));
//                                }*/
//                            }
//                        });
//                builder.show();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        });

    }

    private void showAlert(String message, Context context,String ApplicationName) {
        runOnUiThread(() -> {
            new DialogFactory.Progress(RegistrationActivity.this).hide();
//            Toast.makeText(CameraXActivity.this,"Success",Toast.LENGTH_LONG);

            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(context,  R.style.DialogTheme);
                String timeStamp = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm", new Date());
                builder.setTitle(ApplicationName+ " - " +timeStamp);
                builder.setMessage(message).setCancelable(false)
                        .setPositiveButton(context.getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                /*if(isfalg){
                                    try {
                                        runOnUiThread(() -> relativeLayoutPreview.setVisibility(View.GONE));
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                }else {
//                                    startActivity(new Intent(CameraXActivity.this, DashBoard.class));
                                }*/
                            }
                        });
                builder.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void navigativeToDistributorScree(String message, Context context,String applicationName) {
        runOnUiThread(() -> {
            new DialogFactory.Progress(RegistrationActivity.this).hide();
//            Toast.makeText(CameraXActivity.this,"Success",Toast.LENGTH_LONG);
            try {
                androidx.appcompat.app.AlertDialog.Builder builder = null;
                builder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.MaterialAlertDialog);
                String timeStamp = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm a", new Date());
                builder.setTitle("Face Recognition\n"+timeStamp);
                builder.setMessage(message).setCancelable(false);
                builder.setPositiveButton(context.getString(R.string.try_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        if(refguid==null){
                        refguid =  GUID.newRandom();
//                        }
                        SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_RETRY,Constants.StartSync,refguid.toString36().toUpperCase());

                        dialog.dismiss();
                        openCamera();
                    }
                });
                builder.setNegativeButton(context.getString(R.string.continue_), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(MSFAApplication.isBCRVAN){
                            boolean isAttendanceExist = getAttendance();
                            if (!isAttendanceExist) {
                                onSave();
                            }else{
                              //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                finish();
                            }
                        }else {
                            if(statusTemp==602 ||statusTemp==604){
                                boolean isAttendanceExist = getAttendance();
                                if (!isAttendanceExist) {
                                    onSave();
                                } else {
                                 //   startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                    finish();
                                }
                            }else {
                                //startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                finish();
                            }
                        }
                    }
                });
                builder.setNeutralButton("View Logs", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent support = new Intent(RegistrationActivity.this, com.arteriatech.mutils.registration.SupportActivity.class);
                        registrationModel.setTheme(R.style.AppThemeActivity);
                        ArrayList<MainMenuBean> mainMenuBeanArrayList = new ArrayList<>();
                        MainMenuBean mainMenuBean = new MainMenuBean();
                        mainMenuBean.setActivityRedirect(LogActivity.class);
                        mainMenuBean.setMenuImage(0);
                        mainMenuBean.setMenuName("View");
                        mainMenuBeanArrayList.add(mainMenuBean);
                        registrationModel.setMenuBeen(mainMenuBeanArrayList);
                        support.putExtra(com.arteriatech.mutils.common.UtilConstants.RegIntentKey, registrationModel);
                        startActivity(support);
                    }
                });
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            } catch (Throwable e) {
                e.printStackTrace();
            }
//            new DialogFactory.Alert(RegistrationActivity.this).setNegativeButton(context.getString(R.string.continue_)).setPositiveButton(context.getString(R.string.try_again)).setTheme(R.style.MaterialAlertDialog).setMessage(message).
//                    setOnDialogClick(new OnDialogClick() {
//                        @Override
//                        public void onDialogClick(boolean isPositive) {
//                            try {
//                                if(isPositive){
//                                /*try {
//                                    runOnUiThread(() -> relativeLayoutPreview.setVisibility(View.GONE));
//                                } catch (Throwable e) {
//                                    e.printStackTrace();
//                                }*/
//                                    openCamera();
//                                }else {
//                                    startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
//                                    finish();
//                                }
//                            } catch (Throwable e) {
//                                com.arteriatech.mutils.log.LogManager.writeLogInfo("Clear app data failed : "+e.getMessage());
//                                e.printStackTrace();
//                            }
//                        }
//                    }).show();
        });

    }

    /*Saves start attendance data into store*/
    private void onSave() {
        LocationUtils.checkLocationPermission(RegistrationActivity.this, new LocationInterface() {
            @Override
            public void location(boolean status, LocationModel location, String errorMsg, int errorCode) {
                Log.d("location fun", "1");
                if (status) {

                    refguid =  GUID.newRandom();
                    try {
                        FusedLocationUtil.getInstance().fetchLastLocation(RegistrationActivity.this, (latitude, longitude) -> {
                            LogManager.writeLogInfo("Warehouselist inside bottomattendance FusedLocationUtil listner");

                            double lat = latitude;
                            double longi = longitude;
                            UtilConstants.latitude = lat;
                            UtilConstants.longitude = longi;
                            System.out.println("latitude AWSM " + lat + "longi " + longi);
                            SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_LOCATION_PERM,Constants.StartSync,refguid.toString36().toUpperCase());
                            if (ConstantsUtils.isAutomaticTimeZone(RegistrationActivity.this)) {
                                refguid =  GUID.newRandom();
                                SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_AUTODATE_PERM,Constants.StartSync,refguid.toString36().toUpperCase());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                                    }
                                });
                                onLoadDialog();
                            } else {
                                refguid =  GUID.newRandom();
                                SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_AUTODATE_NOT_PERM,Constants.StartSync,refguid.toString36().toUpperCase());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                                    }
                                });
                                LogManager.writeLogError("Create Attendance Auto date not enable");
                                ConstantsUtils.showAutoDateSetDialog(RegistrationActivity.this);
                            }
                        });
                    } catch (Throwable e) {
                        LogManager.writeLogError("Warehouselist inside bottomattendance FusedLocationUtil listner : " + e.getMessage());

                    }

                    //locationPerGranted();
                }else {
                    refguid =  GUID.newRandom();
                    SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_LOCATION_NOT_PERM,Constants.StartSync,refguid.toString36().toUpperCase());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                        }
                    });
                }
            }
        });
    }

    private void locationPerGranted() {
        UtilConstants.latitude = 0.0;
        UtilConstants.longitude = 0.0;
        new DialogFactory.Progress(RegistrationActivity.this).setTitle("Registration").setMessage(getString(R.string.gps_progress)).setTheme(R.style.MaterialAlertDialog).show();
        LocationUtils.getLocation(RegistrationActivity.this, new LocationGeoInterface() {
            @Override
            public void location(boolean status, LocationModel locationModel, String errorMsg, LocationCallback locationCallback, FusedLocationProviderClient mFusedLocationClient) {
                if (status) {
                    refguid =  GUID.newRandom();
                    SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_GPS_PERM,Constants.StartSync,refguid.toString36().toUpperCase());
                    if (mFusedLocationClient != null) {
                        mFusedLocationClient.removeLocationUpdates(locationCallback);
                    }
                    if(locationModel!=null) {
                        UtilConstants.latitude = locationModel.getLocation().getLatitude();
                        UtilConstants.longitude = locationModel.getLocation().getLongitude();
                    }
                    /*if (ConstantsUtils.isAutomaticTimeZone(RegistrationActivity.this)) {
                        refguid =  GUID.newRandom();
                        SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_AUTODATE_PERM,Constants.StartSync,refguid.toString36().toUpperCase());
                        onLoadDialog();
                    } else {
                        refguid =  GUID.newRandom();
                        SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_AUTODATE_NOT_PERM,Constants.StartSync,refguid.toString36().toUpperCase());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new DialogFactory.Progress(RegistrationActivity.this).hide();
                            }
                        });
                        LogManager.writeLogError("Create Attendance Auto date not enable");
                        ConstantsUtils.showAutoDateSetDialog(RegistrationActivity.this);
                    }*/
                } else {
                    if (mFusedLocationClient != null) {
                        mFusedLocationClient.removeLocationUpdates(locationCallback);
                    }
                    refguid =  GUID.newRandom();
                    SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_GPS_NOT_PERM,Constants.StartSync,refguid.toString36().toUpperCase());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                        }
                    });
                    com.arteriatech.mutils.log.LogManager.writeLogError("Create Attendance get location : "+ status);
                }
            }
        });
        /*Constants.getLocation(RegistrationActivity.this, new LocationInterface() {
            @Override
            public void location(boolean status, LocationModel locationModel, String errorMsg, int errorCode) {
                if (status) {
                    refguid =  GUID.newRandom();
                    SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_GPS_PERM,Constants.StartSync,refguid.toString36().toUpperCase());

                    if (ConstantsUtils.isAutomaticTimeZone(RegistrationActivity.this)) {
                        refguid =  GUID.newRandom();
                        SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_AUTODATE_PERM,Constants.StartSync,refguid.toString36().toUpperCase());

                        onLoadDialog();
                    } else {
                        refguid =  GUID.newRandom();
                        SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_AUTODATE_NOT_PERM,Constants.StartSync,refguid.toString36().toUpperCase());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new DialogFactory.Progress(RegistrationActivity.this).hide();
                            }
                        });
                        LogManager.writeLogError("Create Attendance Auto date not enable");
                        ConstantsUtils.showAutoDateSetDialog(RegistrationActivity.this);
                    }
                }else {
                    refguid =  GUID.newRandom();
                    SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_GPS_NOT_PERM,Constants.StartSync,refguid.toString36().toUpperCase());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                        }
                    });
                    com.arteriatech.mutils.log.LogManager.writeLogError("Create Attendance get location : "+ status);
                }
            }
        });*/
    }


    /*Starts Attendance*/
    private void onLoadDialog() {
        /*  if (Constants.onGpsCheck(getApplicationContext())) {*/
        mStrPopUpText = "Saving attendance. Please wait...";
        message = "Saving attendance. Please wait...";
        try {
            new LoadingData().execute();
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                }
            });
            e.printStackTrace();
            ConstantsUtils.printErrorLog("Create Atnd" + e.getMessage());
        }
        //  }
    }

    /*AsyncTask to store attendance data into offline store*/
    private class LoadingData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new DialogFactory.Progress(RegistrationActivity.this)
                            .setMessage(message).setTitle("Registration").setTheme( R.style.ProgressDialogTheme).show();
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
                onSaveDayStartData();
            } catch (InterruptedException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                    }
                });
                ConstantsUtils.printErrorLog("Create Atnd" + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private boolean mBooleanPictureTaken=false;
    private Hashtable tableItm=null;
    ArrayList<String> alAssignColl = new ArrayList<>();
    Hashtable dbHeadTable;
    ArrayList<HashMap<String, String>> arrtable;
    ArrayList<String> alFlushColl = new ArrayList<>();
    String concatFlushCollStr = "";
    /*Save Day start data on offline store*/
    private void onSaveDayStartData() {
        try {
            refguid =  GUID.newRandom();
            SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_ATT_SAVING,Constants.StartSync,refguid.toString36().toUpperCase());

            Constants.MapEntityVal.clear();
            GUID guid = GUID.newRandom();
            Hashtable hashTableAttendanceValues = new Hashtable();
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
            String loginIdVal = sharedPreferences.getString(UtilRegistrationActivity.KEY_username, "");

            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.LOGINID, loginIdVal);
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.AttendanceGUID, guid.toString());
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.StartDate, com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants.getNewDate());

            final Calendar calCurrentTime = Calendar.getInstance();
            int hourOfDay = calCurrentTime.get(Calendar.HOUR_OF_DAY); // 24 hour clock
            int minute = calCurrentTime.get(Calendar.MINUTE);
            int second = calCurrentTime.get(Calendar.SECOND);
            ODataDuration oDataDuration = null;
            try {
                oDataDuration = new ODataDurationDefaultImpl();
                oDataDuration.setHours(hourOfDay);
                oDataDuration.setMinutes(minute);
                oDataDuration.setSeconds(BigDecimal.valueOf(second));
            } catch (Exception e) {
                e.printStackTrace();
            }

            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.StartTime, oDataDuration);
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.StartLat, BigDecimal.valueOf(UtilConstants.round(UtilConstants.latitude, 12)));
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.StartLong, BigDecimal.valueOf(UtilConstants.round(UtilConstants.longitude, 12)));
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.EndLat, "");
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.EndLong, "");
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.EndDate, "");
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.EndTime, "");

            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.Remarks, "");
            hashTableAttendanceValues.put(Constants.Remarks1, remark1);

            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.AttendanceTypeH1, "01");
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.AttendanceTypeH2, "01");



            hashTableAttendanceValues.put(Constants.SPGUID, Constants.getSPGUID());

            hashTableAttendanceValues.put(Constants.SetResourcePath, "guid'" + guid.toString() + "'");

            SharedPreferences sharedPreferencesVal = getSharedPreferences(Constants.PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPreferencesVal.edit();
            editor.putInt(Constants.VisitSeqId, 0);
            editor.commit();
            tableItm = new Hashtable();
            mBooleanPictureTaken=false;
            try {
                if (!TextUtils.isEmpty(selectedImagePath)) {
                    tableItm.put(Constants.DocumentLink, selectedImagePath);
                    tableItm.put(Constants.AttendanceGUID, guid.toString36().toUpperCase());
                    tableItm.put(Constants.ImageMimeType, mimeType);
                    tableItm.put(Constants.ImageSize, String.valueOf(mLongBitmapSize));
                    tableItm.put(Constants.FileName, filename);
                    mBooleanPictureTaken=true;
                }


            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                //noinspection unchecked
                OfflineManager.createAttendance(hashTableAttendanceValues, new UIListener() {
                    @Override
                    public void onRequestError(int operation, Exception e) {
                        runOnUiThread(() -> {
                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                        });
                        com.arteriatech.mutils.log.LogManager.writeLogError("Create Attendance request error : "+e.getMessage());
                        e.printStackTrace();
                        ErrorBean errorBean = Constants.getErrorCode(operation, e, RegistrationActivity.this);
                        if (errorBean.hasNoError()) {
                            try {
                                mStrPopUpText = getString(R.string.err_msg_concat, getString(R.string.lbl_attence_start), e.getMessage());
                            } catch (Exception er) {
                                er.printStackTrace();
                                mStrPopUpText = getString(R.string.msg_start_upd_sync_error);
                            }
                            if (operation == Operation.Create.getValue()) {
                                Constants.isSync = false;
                            } else if (operation == Operation.OfflineFlush.getValue()) {
                                Constants.isSync = false;
                            } else {
                                Constants.isSync = false;
                            }
                        } else if (errorBean.isStoreFailed()) {
                            if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                                Constants.isSync = true;
//                                new RefreshAsyncTask(RegistrationActivity.this, concatCollectionStr, this).execute();
                            } else {
                                Constants.isSync = false;
                                mStrPopUpText = Constants.makeMsgReqError(errorBean.getErrorCode(), RegistrationActivity.this, false);
                            }
                        } else {
                            Constants.isSync = false;
                            mStrPopUpText = Constants.makeMsgReqError(errorBean.getErrorCode(), RegistrationActivity.this, false);
                        }
                    }

                    @Override
                    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                        com.arteriatech.mutils.log.LogManager.writeLogError("Create Attendance request success ");
                        if (operation == Operation.Create.getValue()) {
                            String message="";
//                            if(refguid==null){
                            refguid =  GUID.newRandom();
//                            }
                            SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_ATT_MARK,Constants.StartSync,refguid.toString36().toUpperCase());

                            if (MSFAApplication.isVAN) {
                                message = "Your attendance has been captured successfully.";
                                if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                                    alFlushColl = Constants.getPendingList();
                                    alAssignColl = Constants.getRefreshList();
                                    concatFlushCollStr = Constants.getConcatinatinFlushCollectios(alFlushColl);
                                    try {
                                        OfflineManager.flushQueuedRequests(this, concatFlushCollStr);
                                    } catch (OfflineODataStoreException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    mStrPopUpText = getString(R.string.no_network_conn);
                                    runOnUiThread(() -> {
                                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                                        try {
                                            new DialogFactory.Alert(RegistrationActivity.this).setMessage(mStrPopUpText).isAlert(true).setOnDialogClick(new OnDialogClick() {
                                                @Override
                                                public void onDialogClick(boolean isPositive) {
                                                    if (isPositive) {
                                                        try {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                   // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                                    finish();
                                                                }
                                                            });
                                                        } catch (Throwable e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        isClickable = false;
                                                    }
                                                }
                                            }).setPositiveButton(getString(R.string.ok)).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                                }
                            }else if (MSFAApplication.isBVAN) {
                                if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                                    alFlushColl = Constants.getPendingList();
                                    alAssignColl = Constants.getRefreshList();
                                    concatFlushCollStr = Constants.getConcatinatinFlushCollectios(alFlushColl);
                                    try {
                                        OfflineManager.flushQueuedRequests(this, concatFlushCollStr);
                                    } catch (OfflineODataStoreException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    mStrPopUpText = getString(R.string.no_network_conn);
                                    runOnUiThread(() -> {
                                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                                        try {
                                            new DialogFactory.Alert(RegistrationActivity.this).setMessage(mStrPopUpText).isAlert(true).setOnDialogClick(new OnDialogClick() {
                                                @Override
                                                public void onDialogClick(boolean isPositive) {
                                                    if (isPositive) {
                                                        try {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                   // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                                    finish();
                                                                }
                                                            });
                                                        } catch (Throwable e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        isClickable = false;
                                                    }
                                                }
                                            }).setNegativeButton(getString(R.string.ok)).setPositiveButton(getString(R.string.ok)).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                                }
                            } else {
                                message = "Face recognized. Your attendance has been captured successfully. Please ensure 22 bill cuts today";
                                mStrPopUpText=message;
                                String finalMessage = message;
                                String finalMessage1 = message;
                                /*runOnUiThread(() -> {
                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
//                                    try {
//                                        new DialogFactory.Alert(RegistrationActivity.this).setMessage(finalMessage1).isAlert(true).setOnDialogClick(new OnDialogClick() {
//                                            @Override
//                                            public void onDialogClick(boolean isPositive) {
//                                                if (isPositive){
//                                                    try {
//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
//                                                                finish();
//                                                            }
//                                                        });
//                                                    } catch (Throwable e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                }else{
//                                                    isClickable =false;
//                                                }
//                                            }
//                                        }).setNegativeButton(getString(R.string.ok)).setPositiveButton(getString(R.string.ok)).show();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
                                    startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                    finish();
                                });*/
                                message = "Your attendance has been captured successfully.";
                                if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                                    alFlushColl = Constants.getPendingList();
                                    alAssignColl = Constants.getRefreshList();
                                    concatFlushCollStr = Constants.getConcatinatinFlushCollectios(alFlushColl);
                                    refguid =  GUID.newRandom();
                                    SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_ATT_POST,Constants.StartSync,refguid.toString36().toUpperCase());

                                    try {
                                        OfflineManager.flushQueuedRequests(this, concatFlushCollStr);
                                    } catch (OfflineODataStoreException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    refguid =  GUID.newRandom();
                                    SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_ATT_SAVING_LOC,Constants.StartSync,refguid.toString36().toUpperCase());

                                    mStrPopUpText = getString(R.string.no_network_conn);
                                    runOnUiThread(() -> {
                                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                                        try {
                                            new DialogFactory.Alert(RegistrationActivity.this).setMessage(mStrPopUpText).isAlert(true).setOnDialogClick(new OnDialogClick() {
                                                @Override
                                                public void onDialogClick(boolean isPositive) {
                                                    if (isPositive) {
                                                        try {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    //startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                                    finish();
                                                                }
                                                            });
                                                        } catch (Throwable e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        isClickable = false;
                                                    }
                                                }
                                            }).setPositiveButton(getString(R.string.ok)).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                                }
                            }

                        } else if (operation == Operation.OfflineFlush.getValue()) {
                            try {
                                concatCollectionStr = Constants.getConcatinatinFlushCollectios(alAssignColl);
//                                if(refguid==null){
                                refguid =  GUID.newRandom();
//                                                }
                                SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_ATT_POST_END,Constants.StartSync,refguid.toString36().toUpperCase());

                                new RefreshAsyncTask(RegistrationActivity.this, concatCollectionStr, this).execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                                TraceLog.e(Constants.SyncOnRequestSuccess, e);
                            }
                        } else if (operation == Operation.OfflineRefresh.getValue()) {
//            Constants.updateLastSyncTimeToTable(alAssignColl);//TODO need to enable
                            refguid =  GUID.newRandom();
//                                                                }
                            SyncUtils.updatingSyncStartTime1(RegistrationActivity.this,Constants.Sync_FR_END,Constants.StartSync,refguid.toString36().toUpperCase());

                            mStrPopUpText = "Your attendance has been captured successfully.";
                            runOnUiThread(() -> {
//                                if (progressDialog!=null&&progressDialog.isShowing())progressDialog.dismiss();
//                                try {
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this,  R.style.DialogTheme);
//                                    builder.setMessage(mStrPopUpText).setCancelable(false)
//                                            .setPositiveButton(RegistrationActivity.this.getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    dialog.cancel();
//                                                    runOnUiThread(new Runnable() {
//                                                        @Override
//                                                        public void run() {
                                if(MSFAApplication.isBVAN){
                                  //  startActivity(new Intent(RegistrationActivity.this, BreadDashBoard.class));
                                    finish();
                                }else {
                                  //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                    finish();
                                }
//                                                        }
//                                                    });
//
//                                                }
//                                            });
//                                    builder.show();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
                            });
                        } else if (operation == Operation.GetStoreOpen.getValue() && OfflineManager.isOfflineStoreOpen()) {
                            Constants.isSync = false;
                            try {
                                OfflineManager.getAuthorizations(RegistrationActivity.this);
                            } catch (OfflineODataStoreException e) {
                                e.printStackTrace();
                            }
                            Constants.setSyncTime(RegistrationActivity.this);
                            mStrPopUpText = getString(R.string.dialog_day_started);
                        }
                    }
                }, RegistrationActivity.this);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
                ConstantsUtils.printErrorLog("Create Atnd" + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                }
            });
            ConstantsUtils.printErrorLog("Create Atnd" + e.getMessage());
        }
    }

    /*public static String getGCMRegsitrationID(String senderID, Context context){
        String gcmRegsitrationid="";
        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
            gcmRegsitrationid = gcm.register(senderID);
            LogManager.writeLogInfo("Gcm Sender Regsitration id success");
        } catch (Throwable e) {
            gcmRegsitrationid="";
            LogManager.writeLogError("Gcm Sender Regsitration id error : "+e.getMessage());
            e.printStackTrace();
        }
        return gcmRegsitrationid;
    }*/

    private String fcmMessagingInitToken(){
        String fcmRegsitrationid="";
       /* try {
            FirebaseApp.initializeApp(this);
            fcmRegsitrationid = FirebaseMessaging.getInstance().getToken().toString();
            LogManager.writeLogError( "Fetching FCM registration token success");
        } catch (Throwable e) {
            fcmRegsitrationid="";
            LogManager.writeLogError( "Fetching FCM registration token failed : "+ e.getMessage());
            e.printStackTrace();
        }*/
        /*try {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                LogManager.writeLogError( "Fetching FCM registration token failed : "+ task.getException());
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();
                            LogManager.writeLogError( "Fetching FCM registration token success : "+token);
                            // Log and toast
                            Toast.makeText(RegistrationActivity.this, token, Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            LogManager.writeLogError("Fcm get Sender Regsitration id error : "+e.getMessage());
            e.printStackTrace();
        }*/
        return fcmRegsitrationid;
    }

    public static void updateGCMRegsitrationIDtoServer(Connection newConnection, SmpClient smpClient, Context context,String gcmRegsitrationid){
        try {
            newConnection.setAndroidGcmRegistrationId(gcmRegsitrationid);
//            newConnection.setAndroidGcmSenderId(newConnection.getAndroidGcmSenderId());
            smpClient.updateEntity(newConnection);
            LogManager.writeLogInfo("Gcm Sender Regsitration id is updated successfully to smp client");
        } catch (Throwable e) {
            gcmRegsitrationid="";
            LogManager.writeLogInfo("Gcm Sender Regsitration id is updated failed error :"+e.getMessage());
            e.printStackTrace();
        }
    }

    public void getCPConfigData() {

    }

    private boolean getAttendance() {
        String spguid = Constants.getSPGUID();
        String query = Constants.Attendances + "?$select=AttendanceGUID &$filter=StartDate eq datetime'" + UtilConstants.getNewDate() + "' " +
                "and SPGUID eq guid'"+spguid+"'";
        List oDataList = new OfflineUtils.ODataOfflineBuilder<>()
                .setHeaderPayloadObject(new AttendanceBean())
                .setODataEntitySet(Constants.Attendances)
                .setODataEntityType(Constants.ATTENDANCEENTITY)
                .setUIListener(this)
                .setQuery(query)
                .returnODataList(OfflineManager.offlineStore);
        return oDataList != null && !oDataList.isEmpty();
    }
    public static boolean isFullSync=false;
    public void  validateBundleData(String payload){
        try {
            try {
                isFullSync =false;
                new DialogFactory.Progress(RegistrationActivity.this)
                        .setMessage("Verifying resource data. Please wait...").setTitle("Registration").setTheme( R.style.ProgressDialogTheme).show();
                message = "Verifying resource data. Please wait...";
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if(!TextUtils.isEmpty(payload)) {
                String result[] = payload.split("\n");
                if(result!=null) {
                    String fullsync = result[0].replace("S4=", "");
                    String date = ConstantsUtils.returnCurrentDate("dd-MM-yyyy");
                    if (date != null && date.equalsIgnoreCase(fullsync.trim())) {
                        String daySyncSkip = result[1].replace("DaySyncSkip=", "");
                        if(daySyncSkip!=null && !daySyncSkip.equalsIgnoreCase("X")) {
                            isFullSync = true;
                            createOnline();
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                                    if(MSFAApplication.isBVAN){
                                       // startActivity(new Intent(RegistrationActivity.this, BreadDashBoard.class));
                                        finish();
                                    }else {
                                       // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                        finish();
                                    }
                                }
                            });
                        }
                    } else {
                        String daySyncSkip = result[1].replace("DaySyncSkip=", "");
                        if(daySyncSkip!=null && !daySyncSkip.equalsIgnoreCase("X")) {
                            isFullSync = false;
                            createOnline();
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                                    if(MSFAApplication.isBVAN){
                                      //  startActivity(new Intent(RegistrationActivity.this, BreadDashBoard.class));
                                        finish();
                                    }else {
                                      //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                }else {
                    isFullSync = false;
                    createOnline();
                }
            }else {
                isFullSync = false;
                createOnline();
            }
        } catch (Throwable e) {
            isClickable = false;
            isFullSync=false;
            createOnline();
        }
    }
    public void validateBundleData( String APP_ID, @NonNull final String userName, @NonNull final String password, String fetchURL, Context context, boolean isAP1){
        if (UtilConstants.isNetworkAvailable(RegistrationActivity.this)) {
            try {
                isFullSync =false;
//                progressDialog = ConstantsUtils.showProgressDialog(RegistrationActivity.this);
//                progressDialog.setMessage("Verifying resource data. Please wait...");
                message = "Verifying resource data. Please wait...";
            } catch (Throwable e) {
                e.printStackTrace();
            }
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
            Future<?> isConnected = executor.submit(() -> {
                HttpsURLConnection connection = null;
                int responseCode = 0;
                try {
                    URL url =  new URL(fetchURL);
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setReadTimeout(Configuration.connectionTimeOut);
                    connection.setConnectTimeout(Configuration.connectionTimeOut);
                    String userCredentials = binding.editTextUserName.getText().toString() + ":" + binding.editTextPassword.getText().toString();
                    String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8), 2);
                    connection.setRequestProperty("Authorization", basicAuth);
                    connection.setRequestProperty("x-smp-appid", APP_ID);
                    //                connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    connection.setDefaultUseCaches(false);
                    connection.setAllowUserInteraction(false);
                    connection.connect();
                    LogManager.writeLogInfo("Bundle Qry "+fetchURL);
                    responseCode = connection.getResponseCode();
                    connection.getResponseMessage();
                    String resultJson = "";
                    InputStream stream = null;
                    if(responseCode==200){
                        try {
                            BannerParser.downloadBannerFile(RegistrationActivity.this);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        stream = connection.getInputStream();
                        if (stream != null) {
                            resultJson = DeviceIDUtils.readResponse(stream);
                        }
                        try {
                            if(!TextUtils.isEmpty(resultJson)) {
                                String result[] = resultJson.split("\n");
                                if(result!=null) {
                                    String fullsync = result[0].replace("S4=", "");
                                    String date = ConstantsUtils.returnCurrentDate("dd-MM-yyyy");
                                    if (date != null && date.equalsIgnoreCase(fullsync.trim())) {
                                        String daySyncSkip = result[1].replace("DaySyncSkip=", "");
                                        if(daySyncSkip!=null && !daySyncSkip.equalsIgnoreCase("X")) {
                                            isFullSync = true;
                                            createOnline();
                                        }else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                   // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                    finish();
                                                }
                                            });
                                        }
                                    } else {
                                        String daySyncSkip = result[1].replace("DaySyncSkip=", "");
                                        if(daySyncSkip!=null && !daySyncSkip.equalsIgnoreCase("X")) {
                                            isFullSync = false;
                                            createOnline();
                                        }else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                   // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                    finish();
                                                }
                                            });
                                        }
                                    }
                                }else {
                                    isFullSync = false;
                                    createOnline();
                                }
                            }else {
                                isFullSync = false;
                                createOnline();
                            }
                        } catch (Throwable e) {
                            isClickable = false;
                            isFullSync=false;
                            createOnline();
                        }
                    }else {
//                        if(progressDialog!=null && progressDialog.isShowing()){
//                            progressDialog.dismiss();
//                        }
//                        if(!isAP1) {
//                            String urlAP1 = getBundleURLAP1() + "/Resource";
//                            validateBundleData(Configuration.APP_ID, USER_NAME_DEVICE_ID, USER_PASSWORD_DEVICE_ID,
//                                    urlAP1,
//                                    RegistrationActivity.this, true);
//                        }else {
                        isFullSync=false;
                        createOnline();
//                        }
                    }
                } catch (Throwable e) {
                    isClickable = false;
                }finally {
                    isClickable = false;
                }
            });
        }else{
            isClickable = false;
            Constants.showAlert(ErrorCode.NETWORK_ERROR,this);
        }
    }
    public static String getBundleURL(){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Configuration.IS_HTTPS?"https":"http")
                .encodedAuthority(Configuration.server_Text+":"+Configuration.port_Text)
                .appendPath("mobileservices")
                .appendPath("application")
                .appendPath(Configuration.APP_ID)
                .appendPath("bundles")
                .appendPath("v1")
                .appendPath("runtime")
                .appendPath("bundle")
                .appendPath("application")
                .appendPath(Configuration.APP_ID)
                .appendPath("bundle");
//                .fragment(applicationName);
        return builder.build().toString();
    }

   /*public static String getBundleURLAP1(){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Configuration.IS_HTTPS?"https":"http")
                .encodedAuthority(Configuration.server_Text_AP1+":"+Configuration.port_Text)
                .appendPath("mobileservices")
                .appendPath("application")
                .appendPath(Configuration.APP_ID)
                .appendPath("bundles")
                .appendPath("v1")
                .appendPath("runtime")
                .appendPath("bundle")
                .appendPath("application")
                .appendPath(Configuration.APP_ID)
                .appendPath("bundle");
//                .fragment(applicationName);
        return builder.build().toString();
    }*/

    private void getAttendenceDetails() {
//        String mStrSPGUID = Constants.getSPGUID();
        boolean isAttendanceExist =getAttendance();
        try {
            if (!isAttendanceExist){
                onSave();
            }else{
                if(MSFAApplication.isBVAN){
                   // startActivity(new Intent(RegistrationActivity.this, BreadDashBoard.class));
                    finish();
                }else {
                  //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                    finish();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
       /* if (!TextUtils.isEmpty(mStrSPGUID)) {
            Constants.MapEntityVal.clear();
            String prvDayQry = Constants.Attendances + "?$filter=StartDate eq datetime'" + UtilConstants.getNewDate() + "' and " + Constants.SPGUID + " eq guid'" + mStrSPGUID + "'";
            try {
               String mStrAttendanceId = OfflineManager.getAttendance(prvDayQry);

            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
                ConstantsUtils.printErrorLog(e.getMessage());
            }

            String startDateStr;
            String endDateStr;
            if (Constants.MapEntityVal.isEmpty()) {
                Constants.MapEntityVal.clear();
                onSave();
            } else {
                Constants.MapEntityVal.clear();
                startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                finish();
            }
        }else {
            startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
            finish();
        }*/

    }
    public static void validateMetadata(String APP_ID, @NonNull final String userName, @NonNull final String password){
        new Thread(() -> {
            HttpsURLConnection connection = null;
            int responseCode = 0;
            LogManager.writeLogInfo("Requesting metadata.. ");
            try {
                String fetchURL = MyUtils.getDefaultOnlineQryURL() +"$metadata";
                URL url =  new URL(fetchURL);
                connection = (HttpsURLConnection) url.openConnection();
                String userCredentials = userName + ":" + password;
                String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8), 2);
                connection.setRequestProperty("Authorization", basicAuth);
                connection.setRequestProperty("x-smp-appid", APP_ID);
                //connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setDefaultUseCaches(false);
                connection.setAllowUserInteraction(false);
                connection.connect();
                LogManager.writeLogInfo("Requesting metadata..with URL "+fetchURL);
                responseCode = connection.getResponseCode();
                connection.getResponseMessage();
                String resultJson = "";
                InputStream stream = null;
                if (responseCode == 401){
                    LogManager.writeLogInfo("metadata fetch request failed : "+responseCode);
                    LogManager.writeLogError("X-SMP-LOG-CORRELATION-ID  :  "+connection.getHeaderField("X-SMP-LOG-CORRELATION-ID"));
                    LogManager.writeLogError("X-message-code  :  "+connection.getHeaderField("X-message-code"));
                }
            } catch (Throwable e) {
                LogManager.writeLogError("Unable to fetch $metadata details due to error :"+e.toString());
            }
        }).start();
    }

    /*Import Offline DB into application*/
    public static boolean importDB(Context mContext, UIListener uiListener) {
        if (OfflineManager.isOfflineStoreOpen()) {
            try {
                OfflineManager.closeOfflineStore();
                LogManager.writeLogError(mContext.getString(R.string.msg_sync_terminated));
            } catch (OfflineODataStoreException e) {
                LogManager.writeLogError(mContext.getString(R.string.error_during_offline_close) + e.getMessage());
            }
        }

//        File isd = Environment.getExternalStorageDirectory();
        File isd = new File(Environment.getExternalStoragePublicDirectory("") + "/AWSMDBFiles");
        File idata = Environment.getDataDirectory();
        FileChannel isource = null;
        FileChannel idestination = null;
        File ibackupDB = new File(idata, Constants.icurrentUDBPath);
        File icurrentDB = new File(isd, Constants.ibackupUDBPath);

        File ibackupRqDB = new File(idata, Constants.icurrentRqDBPath);
        File icurrentRqDB = new File(isd, Constants.ibackupRqDBPath);
        try {
            isource = new FileInputStream(icurrentDB).getChannel();
            idestination = new FileOutputStream(ibackupDB).getChannel();
            idestination.transferFrom(isource, 0, isource.size());

            isource = new FileInputStream(icurrentRqDB).getChannel();
            idestination = new FileOutputStream(ibackupRqDB).getChannel();
            idestination.transferFrom(isource, 0, isource.size());

            isource.close();
            if (!OfflineManager.isOfflineStoreOpen()) {
                try {
                    OfflineManager.openOfflineStoreInternal(mContext, uiListener);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }

    private boolean pingServer(String hostName){
        LogManager.writeLogInfo("Ping Server Host Name : "+hostName);
        HttpsURLConnection connection = null;
        int responseCode = 0;
        String fetchURL = "";
        Uri.Builder builder = new Uri.Builder();
//        builder.scheme(Configuration.IS_HTTPS?"https":"http")
//                .encodedAuthority(hostName+":"+Configuration.port_Text)
//                .appendPath(Configuration.PING_APP_ID);

        builder.scheme(Configuration.IS_HTTPS?"https":"http")
                .encodedAuthority(hostName)
                .appendPath(Configuration.path1)
                .appendPath(Configuration.path2)
                .appendPath(Configuration.path3)
                .appendPath(Configuration.PING_APP_ID);

        fetchURL = builder.build().toString();
        try {
            URL url =  new URL(fetchURL);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(Configuration.connectionTimeOut);
            connection.setConnectTimeout(Configuration.connectionTimeOut);
            connection.setRequestProperty("x-smp-appid", Configuration.PING_APP_ID);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.connect();
            LogManager.writeLogInfo("Ping Server Qry "+fetchURL);
            responseCode = connection.getResponseCode();
            connection.getResponseMessage();
            String resultJson = "";
            InputStream stream = null;
            if(responseCode==200){
                LogManager.writeLogInfo("Ping Server check request success "+responseCode);
                return true;
            } else {
                LogManager.writeLogInfo("Ping Server check request failed : " + responseCode);
                return false;
            }
        } catch (Throwable e) {
            LogManager.writeLogError("Ping Server check due to error :"+e.toString());
        }
        return false;
    }

    public JSONObject getResourceFile(String hostName){
        JSONObject resourceFileRes = new JSONObject();
        try {
            if (UtilConstants.isNetworkAvailable(RegistrationActivity.this)) {
                resourceFileRes.put(Constants.HostName,hostName);
                String fetchURL = "";
                Uri.Builder builder = new Uri.Builder();
                builder.scheme(Configuration.IS_HTTPS ? "https" : "http")
                        .encodedAuthority(hostName + ":" + Configuration.port_Text)
                        .appendPath("mobileservices")
                        .appendPath("application")
                        .appendPath(Configuration.APP_ID)
                        .appendPath("bundles")
                        .appendPath("v1")
                        .appendPath("runtime")
                        .appendPath("bundle")
                        .appendPath("application")
                        .appendPath(Configuration.APP_ID)
                        .appendPath("bundle");
                fetchURL = builder.toString()+"/Resource";
                String finalFetchURL = fetchURL;
                HttpsURLConnection connection = null;
                int responseCode = 0;
                try {
                    URL url = new URL(finalFetchURL);
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setReadTimeout(Configuration.connectionTimeOut);
                    connection.setConnectTimeout(Configuration.connectionTimeOut);
                    String userCredentials = binding.editTextUserName.getText().toString() + ":" + binding.editTextPassword.getText().toString();
                    String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8), 2);
                    connection.setRequestProperty("Authorization", basicAuth);
                    connection.setRequestProperty("x-smp-appid", APP_ID);
                    //                connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    connection.setDefaultUseCaches(false);
                    connection.setAllowUserInteraction(false);
                    connection.connect();
                    LogManager.writeLogInfo("getResourceFile Qry " + finalFetchURL);
                    responseCode = connection.getResponseCode();
                    connection.getResponseMessage();
                    String resultJson = "";
                    InputStream stream = null;
                    if (responseCode == 200) {
                        stream = connection.getInputStream();
                        if (stream != null) {
                            resultJson = DeviceIDUtils.readResponse(stream);
                        }
                        try {
                            if (!TextUtils.isEmpty(resultJson)) {
                                resourceFileRes.put(Constants.Status, true);
                                resourceFileRes.put(Constants.ResponseCode, responseCode);
                                resourceFileRes.put(Constants.PayLoad, resultJson);
                                resourceFileRes.put(Constants.Message, "");
                            } else {
                                resourceFileRes.put(Constants.Status, false);
                                resourceFileRes.put(Constants.ResponseCode, 999);
                                resourceFileRes.put(Constants.PayLoad, "");
                                resourceFileRes.put(Constants.Message, "Resource file has no data.Please contact  support team");
                            }
                        } catch (Throwable e) {
                            isClickable = false;
                        }
                    } else if (responseCode == 503) {
                        resourceFileRes.put(Constants.Status, false);
                        resourceFileRes.put(Constants.ResponseCode, responseCode);
                        resourceFileRes.put(Constants.PayLoad, "");
                        resourceFileRes.put(Constants.Message, "Server unavailable. Registration cannot be performed now");
                    } else if (responseCode == 404) {
                        String message = "";
                        try {
                            if(hostName.equalsIgnoreCase(server_Text_default)){
                                String[] hostSPilt = server_Text_default.split("-");
                                String hostSPiltTemp = hostSPilt[1].replace(".hana.ondemand.com","");
                                message = "Resource file not available in "+hostSPiltTemp+". Please contact support team";
                            }else  if(hostName.equalsIgnoreCase(server_Text_DR)){
                                String[] hostSPilt = server_Text_DR.split("-");
                                String hostSPiltTemp = hostSPilt[1].replace(".hana.ondemand.com","");
                                message = "Resource file not available in "+hostSPiltTemp+". Please contact support team";
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                            message = "Resource file not available"+". Please contact support team";
                        }
                        resourceFileRes.put(Constants.Status, false);
                        resourceFileRes.put(Constants.ResponseCode, responseCode);
                        resourceFileRes.put(Constants.PayLoad, "");
                        resourceFileRes.put(Constants.Message, message);
                    } else if (responseCode == 401) {
                        resourceFileRes.put(Constants.Status, false);
                        resourceFileRes.put(Constants.ResponseCode, responseCode);
                        resourceFileRes.put(Constants.PayLoad, "");
                        resourceFileRes.put(Constants.Message, connection.getResponseMessage());
                    } else {
                        resourceFileRes.put(Constants.Status, false);
                        resourceFileRes.put(Constants.ResponseCode, responseCode);
                        resourceFileRes.put(Constants.PayLoad, "");
                        resourceFileRes.put(Constants.Message, connection.getResponseMessage());
                    }
                } catch (Throwable e) {
                    isClickable = false;
                }
                return resourceFileRes;

            } else {
                isClickable = false;
                resourceFileRes.put(Constants.Status, false);
                resourceFileRes.put(Constants.ResponseCode, 997);
                resourceFileRes.put(Constants.PayLoad, "");
                resourceFileRes.put(Constants.Message, ErrorCode.NETWORK_ERROR);
                return resourceFileRes;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private void serverRegister(){

        try {
            if(pingServer(server_Text_default)) {
                getResourceIDP(server_Text_default);
                JSONObject resourceFileReadresponse = getResourceFile(server_Text_default);
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME,0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.ActiveHost,server_Text_default);
                editor.apply();
                server_Text = sharedPreferences.getString(Constants.ActiveHost,server_Text);
                if(resourceFileReadresponse!=null) {
                    JSONObject serverResponse = getJSONServerResponse(resourceFileReadresponse);
                    boolean status = serverResponse.getBoolean(Constants.Status);
                    int responseCode = serverResponse.getInt(Constants.ResponseCode);
                    String messageError = serverResponse.getString(Constants.Message);
                    if(responseCode == 200){
                        if(status){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new DialogFactory.Progress(RegistrationActivity.this)
                                            .setProgressMessage("Verifying user management. Please wait...");
                                    message = "Verifying user management. Please wait...";
                                }
                            });
                            validateUserRole(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,MyUtils.getDefaultOnlineQryURL() +Constants.UserProfiles + URLEncoder.encode("('PD')", "UTF-8"),RegistrationActivity.this);
                        }else {
                            isClickable = false;
                            showAlert(messageError,RegistrationActivity.this,false);
                        }
                    }else if(responseCode == 503 || responseCode == 404 || responseCode == 998 || responseCode == 999 || responseCode == 996){
                        // 503 - service unavailable , 404 - Resource file not maintained , 998 - Backend unavailable , 999 - Resource file is empty
                        //996 - Resource file available but backend property missing
                        if(pingServer(server_Text_DR)) {
                            resourceFileReadresponse = getResourceFile(server_Text_DR);
                            if(resourceFileReadresponse!=null){
                                serverResponse = getJSONServerResponse(resourceFileReadresponse);
                                status = serverResponse.getBoolean(Constants.Status);
                                responseCode = serverResponse.getInt(Constants.ResponseCode);
                                messageError = serverResponse.getString(Constants.Message);
                                if(responseCode == 200){
                                    if(status){
//                                        editor.putString(Constants.ActiveHost,server_Text_DR);
//                                        editor.apply();
//                                        server_Text = sharedPreferences.getString(Constants.ActiveHost,server_Text);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                new DialogFactory.Progress(RegistrationActivity.this)
                                                        .setProgressMessage("Verifying user management. Please wait...");
                                                message = "Verifying user management. Please wait...";
                                            }
                                        });
                                        validateUserRole(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,MyUtils.getDefaultOnlineQryURL() +Constants.UserProfiles + URLEncoder.encode("('PD')", "UTF-8"),RegistrationActivity.this);
                                    }else {
                                        isClickable = false;
                                        showAlert(messageError,RegistrationActivity.this,false);
                                    }
                                }else if(responseCode == 401){
                                    LogManager.writeLogInfo("getResouce file check request failed : "+responseCode);
                                    isClickable = false;
                                    try {
                                        com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, binding.editTextUserName.getText().toString().trim(), binding.editTextPassword.getText().toString().trim(), Configuration.APP_ID, jsonObject -> {
                                            runOnUiThread(() -> {
                                                new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject,registrationModel,binding.editTextUserName.getText().toString().trim());
                                            });
                                        });
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }else {
                                    isClickable = false;
                                    showAlert(messageError,RegistrationActivity.this,false);
                                }
                            }
                        }else {
                            isClickable = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                                    LogManager.writeLogError("Unable to read resource file from servers. Registration can't be performed now");
                                    UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "","Unable to read resource file from servers. Registration can't be performed now" , getString(R.string.OK), "", false, new DialogCallBack() {
                                        @Override
                                        public void clickedStatus(boolean clickedStatus) {
                                        }
                                    });
                                }

                            });
                        }
                    }else if(responseCode == 401){
                        LogManager.writeLogInfo("getResouce file check request failed : "+responseCode);
                        isClickable = false;
                        try {
                            com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, binding.editTextUserName.getText().toString().trim(), binding.editTextPassword.getText().toString().trim(), Configuration.APP_ID, jsonObject -> {
                                runOnUiThread(() -> {
                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                                    Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject,registrationModel,binding.editTextUserName.getText().toString().trim());
                                });
                            });
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }else {
                        isClickable = false;
                        showAlert(messageError,RegistrationActivity.this,false);
                    }
                }
            }else {
                if(pingServer(server_Text_DR)) {
                    getResourceIDP(server_Text_DR);
                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME,0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.ActiveHost,server_Text_DR);
                    editor.apply();
                    server_Text = sharedPreferences.getString(Constants.ActiveHost,server_Text);
                    JSONObject resourceFileReadresponse = getResourceFile(server_Text_DR);
                    if(resourceFileReadresponse!=null) {
                        JSONObject serverResponse = getJSONServerResponse(resourceFileReadresponse);
                        boolean status = serverResponse.getBoolean(Constants.Status);
                        int responseCode = serverResponse.getInt(Constants.ResponseCode);
                        String messageError = serverResponse.getString(Constants.Message);
                        if (responseCode == 200) {
                            if (status) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new DialogFactory.Progress(RegistrationActivity.this)
                                                .setProgressMessage("Verifying user management. Please wait...");
                                        message = "Verifying user management. Please wait...";
                                    }
                                });
                                validateUserRole(Configuration.APP_ID, USER_NAME_DEVICE_ID, USER_PASSWORD_DEVICE_ID, MyUtils.getDefaultOnlineQryURL() + Constants.UserProfiles + URLEncoder.encode("('PD')", "UTF-8"), RegistrationActivity.this);
                            } else {
                                isClickable = false;
                                showAlert(messageError, RegistrationActivity.this, false);
                            }
                        } else if(responseCode == 503 || responseCode == 404|| responseCode == 998 || responseCode == 999 || responseCode == 996){
                            // 503 - service unavailable , 404 - Resource file not maintained , 998 - Backend unavailable , 999 - Resource file is empty
                            //996 - Resource file available but backend property missing
                            if(pingServer(server_Text_default)) {
                                resourceFileReadresponse = getResourceFile(server_Text_default);
                                if(resourceFileReadresponse!=null){
                                    serverResponse = getJSONServerResponse(resourceFileReadresponse);
                                    status = serverResponse.getBoolean(Constants.Status);
                                    responseCode = serverResponse.getInt(Constants.ResponseCode);
                                    messageError = serverResponse.getString(Constants.Message);
                                    if(responseCode == 200){
                                        if(status){
//                                            editor.putString(Constants.ActiveHost,server_Text_default);
//                                            editor.apply();
//                                            server_Text = sharedPreferences.getString(Constants.ActiveHost,server_Text);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    new DialogFactory.Progress(RegistrationActivity.this)
                                                            .setProgressMessage("Verifying user management. Please wait...");
                                                    message = "Verifying user management. Please wait...";
                                                }
                                            });
                                            validateUserRole(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,MyUtils.getDefaultOnlineQryURL() +Constants.UserProfiles + URLEncoder.encode("('PD')", "UTF-8"),RegistrationActivity.this);
                                        }else {
                                            isClickable = false;
                                            showAlert(messageError,RegistrationActivity.this,false);
                                        }
                                    }else if(responseCode == 401){
                                        LogManager.writeLogInfo("getResouce file check request failed : "+responseCode);
                                        isClickable = false;
                                        try {
                                            com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, binding.editTextUserName.getText().toString().trim(), binding.editTextPassword.getText().toString().trim(), Configuration.APP_ID, jsonObject -> {
                                                runOnUiThread(() -> {
                                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                    Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject,registrationModel,binding.editTextUserName.getText().toString().trim());
                                                });
                                            });
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    }else {
                                        isClickable = false;
                                        showAlert(messageError,RegistrationActivity.this,false);
                                    }
                                }
                            }else {
                                isClickable = false;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                                        LogManager.writeLogError("Unable to read resource file from servers. Registration can't be performed now");
                                        UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "","Unable to read resource file from servers. Registration can't be performed now" , getString(R.string.OK), "", false, new DialogCallBack() {
                                            @Override
                                            public void clickedStatus(boolean clickedStatus) {
                                            }
                                        });
                                    }

                                });
                            }
                        } else if (responseCode == 401) {
                            LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                            isClickable = false;
                            try {
                                com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, binding.editTextUserName.getText().toString().trim(), binding.editTextPassword.getText().toString().trim(), Configuration.APP_ID, jsonObject -> {
                                    runOnUiThread(() -> {
                                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                                        Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject, registrationModel, binding.editTextUserName.getText().toString().trim());
                                    });
                                });
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }  else {
                            isClickable = false;
                            showAlert(messageError, RegistrationActivity.this, false);
                        }
                    }
                }else {
                    isClickable = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                            LogManager.writeLogError("Registration can't be performed due to server unavailability. Please try later");
                            UtilConstants.dialogBoxWithCallBack(RegistrationActivity.this, "","Registration can't be performed due to server unavailability. Please try later" , getString(R.string.OK), "", false, new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                }
                            });
                        }

                    });
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void navigativeDistribute(String hostName){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.ActiveHost,hostName);
        editor.apply();
        server_Text = sharedPreferences.getString(Constants.ActiveHost,"");
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!sharedPerf.getBoolean(UtilRegistrationActivity.KEY_isFirstTimeDB, false)) {
                            message= "Initial download sync in progress. Please wait...";
                        }else{
                            message = "Preparing App. Please wait...";
                        }
                        new DialogFactory.Progress(RegistrationActivity.this).setProgressMessage(message);
//                        if (progressDialog!=null&&progressDialog.isShowing()){
//                            String finalMessage = message;
//                            progressDialog.setMessage(finalMessage);
//                        }else if(progressDialog!=null&&!progressDialog.isShowing()){
//                            progressDialog.show();
//                            progressDialog.setMessage(message);
//                        }else{
//                            if(progressDialog==null) {
//                                progressDialog = ConstantsUtils.showProgressDialog(RegistrationActivity.this);
//                                progressDialog.setMessage(message);
//                            }else {
//                                progressDialog.setMessage(message);
//                            }
//                        }
                    }
                });
                if (OfflineManager.offlineStore != null) {
                    if (!isOfflineStoreOpen()) {
                        try {
                            LogManager.writeLogError("initializing Store");

//                            OfflineManager.openOfflineStore(RegistrationActivity.this, RegistrationActivity.this);
                            OfflineManager.openOfflineStoreInternal(RegistrationActivity.this, new UIListener() {
                                @Override
                                public void onRequestError(int operation, Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                new DialogFactory.Progress(RegistrationActivity.this).hide();
                                            } catch (Throwable e) {
                                                e.printStackTrace();
                                            }
                                            showAlert(e.getMessage(),RegistrationActivity.this,false);
                                        }
                                    });
                                }

                                @Override
                                public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                new DialogFactory.Progress(RegistrationActivity.this).hide();
                                            } catch (Throwable e) {
                                                e.printStackTrace();
                                            }
                                            if(MSFAApplication.isBVAN){
                                             //   startActivity(new Intent(RegistrationActivity.this, BreadDashBoard.class));
                                                finish();
                                            }else {
                                               // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                finish();
                                            }
                                        }
                                    });
                                }
                            });
                        } catch (Throwable e) {
                            e.printStackTrace();
                            LogManager.writeLogError(Constants.error_txt + e.getMessage());
                        }
                    } else if(OfflineManager.isOfflineStoreOpen()){
                        Log.e("STORE_STATE","doInBackground called isStoreOpened = true" );
//                        LogManager.writeLogError("doInBackground called");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
//                                    if (progressDialog!=null&&progressDialog.isShowing()) {
                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                                    if(MSFAApplication.isBVAN){
                                      //  startActivity(new Intent(RegistrationActivity.this, BreadDashBoard.class));
                                        finish();
                                    }else {
                                       // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                        finish();
                                    }
//                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    LogManager.writeLogError(Constants.error_txt + e1.getMessage());
                                }
                            }
                        });
                    }
                } else {
                    try {
                        LogManager.writeLogError("initializing Store");
//                        OfflineManager.openOfflineStore(RegistrationActivity.this, RegistrationActivity.this);
                        OfflineManager.openOfflineStoreInternal(RegistrationActivity.this, new UIListener() {
                            @Override
                            public void onRequestError(int operation, Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                        showAlert(e.getMessage(),RegistrationActivity.this,false);
                                    }
                                });
                            }

                            @Override
                            public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                        if(MSFAApplication.isBVAN){
                                           // startActivity(new Intent(RegistrationActivity.this, BreadDashBoard.class));
                                            finish();
                                        }else {
                                          ////  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                            finish();
                                        }
                                    }
                                });
                            }
                        });
                    } catch (Throwable e) {
                        e.printStackTrace();
                        LogManager.writeLogError(Constants.error_txt + e.getMessage());
                    }
                }
            }
        }).start();
    }

    private void serverStoreOpen(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (OfflineManager.offlineStore != null) {
                    if (!isOfflineStoreOpen()) {
                        try {
                            LogManager.writeLogError("initializing Store");

//                            OfflineManager.openOfflineStore(RegistrationActivity.this, RegistrationActivity.this);
                            OfflineManager.openOfflineStoreInternal(RegistrationActivity.this, RegistrationActivity.this);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            LogManager.writeLogError(Constants.error_txt + e.getMessage());
                        }
                    } else if(OfflineManager.isOfflineStoreOpen()){
                        Log.e("STORE_STATE","doInBackground called isStoreOpened = true" );
//                        LogManager.writeLogError("doInBackground called");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
//                                    if (progressDialog!=null&&progressDialog.isShowing()) {
                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                                    if (MSFAApplication.isSDA){
                                      //  startActivity(new Intent(RegistrationActivity.this, SDACommunicationDashboardActivity.class));
                                    }else if(MSFAApplication.isAWSM){
                                        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                                        String date = settings.getString(Constants.BirthDayAlertsDate, "");
                                        if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
                                            // perform all sync
                                            String url =getBundleURL()+"/Resource";
//                                                validateBundleData(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,
//                                                        url,
//                                                        RegistrationActivity.this,false);
                                            validateBundleData(payload);
//                                    createOnline();
                                        }else{
                                            LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                                            boolean isFaceEnabled = RegistrationActivity.this.getSharedPreferences(Constants.PREFS_NAME,0).getBoolean(Constants.FACE_REINIT,false);
                                            if (!isFaceEnabled) {
                                                //startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                finish();
                                            }else{
                                                openCamera();
                                            }
                                        }
                                    }else if(MSFAApplication.isBCRVAN){
                                        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                                        String date = settings.getString(Constants.BirthDayAlertsDate, "");
                                        if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
                                            // perform all sync
                                            String url =getBundleURL()+"/Resource";
//                                                validateBundleData(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,
//                                                        url,
//                                                        RegistrationActivity.this,false);
                                            validateBundleData(payload);
//                                    createOnline();
                                        }else{
                                            LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                                            boolean isFaceEnabled = RegistrationActivity.this.getSharedPreferences(Constants.PREFS_NAME,0).getBoolean(Constants.FACE_REINIT,false);
                                            if (!isFaceEnabled) {
                                               // startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                finish();
                                            }else{
                                                openCamera();
                                            }
                                        }
                                    }else if(MSFAApplication.isCallCenter){
                                        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                                        String date = settings.getString(Constants.BirthDayAlertsDate, "");
                                        if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
                                            // perform all sync
                                            String url =getBundleURL()+"/Resource";
//                                                validateBundleData(Configuration.APP_ID,USER_NAME_DEVICE_ID,USER_PASSWORD_DEVICE_ID,
//                                                        url,
//                                                        RegistrationActivity.this,false);
                                            validateBundleData(payload);
//                                    createOnline();
                                        }else{
                                            LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                                            boolean isFaceEnabled = RegistrationActivity.this.getSharedPreferences(Constants.PREFS_NAME,0).getBoolean(Constants.FACE_REINIT,false);
                                            if (!isFaceEnabled) {
                                              //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                                finish();
                                            }else{
                                                openCamera();
                                            }
                                        }
                                    }else if(MSFAApplication.isPSM){
                                        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                                        String date = settings.getString(Constants.BirthDayAlertsDate, "");
                                        if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
                                            // perform all sync
                                            createOnline();
                                        }else{
                                            LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                                            //startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                            finish();
                                        }
                                    }else if(MSFAApplication.isVAN){
                                        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                                        String date = settings.getString(Constants.BirthDayAlertsDate, "");
                                        if (date!=null&&!TextUtils.isEmpty(date)&&!TextUtils.equals(date,UtilConstants.getDate1())) {
                                            // perform all sync
                                            createOnline();
                                        }else{
                                            LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                                          //  startActivity(new Intent(RegistrationActivity.this, DashBoard.class));
                                            finish();
                                        }
                                    }else if (MSFAApplication.isBVAN) {
                                        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                                        String date = settings.getString(Constants.BirthDayAlertsDate, "");
                                        if (date != null && !TextUtils.isEmpty(date) && !TextUtils.equals(date, UtilConstants.getDate1())) {
                                            // perform all sync
                                            createOnline();
                                        } else {
                                            LogManager.writeLogError("store opened successfully & Navigating to DashBoard");
                                          //  startActivity(new Intent(RegistrationActivity.this, BreadDashBoard.class));
                                            finish();
                                        }
                                    }

//                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    LogManager.writeLogError(Constants.error_txt + e1.getMessage());
                                }
                            }
                        });
                    }
                } else {
                    try {
                        LogManager.writeLogError("initializing Store");
//                        OfflineManager.openOfflineStore(RegistrationActivity.this, RegistrationActivity.this);
                        OfflineManager.openOfflineStoreInternal(RegistrationActivity.this, RegistrationActivity.this);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        LogManager.writeLogError(Constants.error_txt + e.getMessage());
                    }
                }
            }
        }).start();
    }

    private  String payload = "";

    private void loginAvailableServer() {
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        String date = settings.getString(Constants.BirthDayAlertsDate, "");
        if (date != null && !TextUtils.isEmpty(date) && !TextUtils.equals(date, UtilConstants.getDate1())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (UtilConstants.isNetworkAvailable(RegistrationActivity.this)) {
                        String hostName = getSharedPreferences(Constants.PREFS_NAME, 0).getString(Constants.ActiveHost, "");
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new DialogFactory.Progress(RegistrationActivity.this).setTitle("Registration").setMessage("Verifying server. Please wait...").setTheme(R.style.MaterialAlertDialog).show();
                                    message = "Verifying server. Please wait...";
                                }
                            });
                            if (pingServer(server_Text)) {
                                JSONObject resourceFileReadresponse = getResourceFile(server_Text);
                                SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Constants.ActiveHost, server_Text);
                                editor.apply();
                                server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                                if (resourceFileReadresponse != null) {
                                    JSONObject serverResponse = resourceFileReadresponse;
                                    boolean status = serverResponse.getBoolean(Constants.Status);
                                    int responseCode = serverResponse.getInt(Constants.ResponseCode);
                                    String messageError = serverResponse.getString(Constants.Message);
                                    payload = serverResponse.getString(Constants.PayLoad);
                                    if (responseCode == 200) {
                                        if (status) {
                                            initStore(true);
                                        } else {
                                            isClickable = false;
                                            navigativeDistribute(hostName);
                                        }
                                    } else if (responseCode == 503 || responseCode == 404 || responseCode == 998 || responseCode == 999 || responseCode == 996) {
                                        // 503 - service unavailable , 404 - Resource file not maintained , 998 - Backend unavailable , 999 - Resource file is empty
                                        //996 - Resource file available but backend property missing
                                        if (!server_Text.equalsIgnoreCase(server_Text_default)) {
                                            if (pingServer(server_Text_default)) {
                                                resourceFileReadresponse = getResourceFile(server_Text_default);
                                                if (resourceFileReadresponse != null) {
                                                    serverResponse = getJSONServerResponse(resourceFileReadresponse);
                                                    status = serverResponse.getBoolean(Constants.Status);
                                                    responseCode = serverResponse.getInt(Constants.ResponseCode);
                                                    messageError = serverResponse.getString(Constants.Message);
                                                    if (responseCode == 200) {
                                                        if (status) {
//                                                           requestOnline(false);
                                                            initStore(true);
                                                        } else {
                                                            isClickable = false;
                                                            navigativeDistribute(hostName);
                                                        }
                                                    } else if (responseCode == 401) {
                                                        LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                                        isClickable = false;
                                                        try {
                                                            com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, binding.editTextUserName.getText().toString().trim(), binding.editTextPassword.getText().toString().trim(), Configuration.APP_ID, jsonObject -> {
                                                                runOnUiThread(() -> {
                                                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                                    Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject, registrationModel, binding.editTextUserName.getText().toString().trim());
                                                                });
                                                            });
                                                        } catch (IOException e1) {
                                                            e1.printStackTrace();
                                                        }
                                                    } else {
                                                        isClickable = false;
                                                        navigativeDistribute(hostName);
                                                    }
                                                }
                                            } else {
                                                isClickable = false;
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                        LogManager.writeLogError("Unable to read resource file from servers. Registration can't be performed now");
                                                        navigativeDistribute(hostName);
                                                    }

                                                });
                                            }
                                        } else if (!server_Text.equalsIgnoreCase(server_Text_DR)) {
                                            if (pingServer(server_Text_DR)) {
                                                resourceFileReadresponse = getResourceFile(server_Text_DR);
                                                if (resourceFileReadresponse != null) {
                                                    serverResponse = getJSONServerResponse(resourceFileReadresponse);
                                                    status = serverResponse.getBoolean(Constants.Status);
                                                    responseCode = serverResponse.getInt(Constants.ResponseCode);
                                                    messageError = serverResponse.getString(Constants.Message);
                                                    if (responseCode == 200) {
                                                        if (status) {
                                                            requestOnline(false);
                                                        } else {
                                                            isClickable = false;
                                                            navigativeDistribute(hostName);
                                                        }
                                                    } else if (responseCode == 401) {
                                                        LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                                        isClickable = false;
                                                        try {
                                                            com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, binding.editTextUserName.getText().toString().trim(), binding.editTextPassword.getText().toString().trim(), Configuration.APP_ID, jsonObject -> {
                                                                runOnUiThread(() -> {
                                                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                                    Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject, registrationModel, binding.editTextUserName.getText().toString().trim());
                                                                });
                                                            });
                                                        } catch (IOException e1) {
                                                            e1.printStackTrace();
                                                        }
                                                    } else {
                                                        isClickable = false;
                                                        navigativeDistribute(hostName);
                                                    }
                                                }
                                            } else {
                                                isClickable = false;
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                        LogManager.writeLogError("Unable to read resource file from servers. Registration can't be performed now");
                                                        navigativeDistribute(hostName);
                                                    }

                                                });
                                            }
                                        }
                                    } else if (responseCode == 401) {
                                        LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                        isClickable = false;
                                        try {
                                            com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, binding.editTextUserName.getText().toString().trim(), binding.editTextPassword.getText().toString().trim(), Configuration.APP_ID, jsonObject -> {
                                                runOnUiThread(() -> {
                                                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                    Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject, registrationModel, binding.editTextUserName.getText().toString().trim());
                                                });
                                            });
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    } else {
                                        isClickable = false;
                                        navigativeDistribute(hostName);
                                    }
                                }
                            } else {
                                //xumu != xumu
                                if (!server_Text.equalsIgnoreCase(server_Text_default)) {
                                    if (pingServer(server_Text_default)) {
                                        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(Constants.ActiveHost, server_Text_default);
                                        editor.apply();
                                        server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                                        JSONObject resourceFileReadresponse = getResourceFile(server_Text_default);
                                        if (resourceFileReadresponse != null) {
                                            JSONObject serverResponse = getJSONServerResponse(resourceFileReadresponse);
                                            boolean status = serverResponse.getBoolean(Constants.Status);
                                            int responseCode = serverResponse.getInt(Constants.ResponseCode);
                                            String messageError = serverResponse.getString(Constants.Message);
                                            if (responseCode == 200) {
                                                if (status) {
                                                    requestOnline(false);
                                                } else {
                                                    isClickable = false;
                                                    navigativeDistribute(hostName);
                                                }
                                            } else if (responseCode == 503 || responseCode == 404 || responseCode == 998 || responseCode == 999 || responseCode == 996) {
                                                // 503 - service unavailable , 404 - Resource file not maintained , 998 - Backend unavailable , 999 - Resource file is empty
                                                //996 - Resource file available but backend property missing
                                                isClickable = false;
                                                navigativeDistribute(hostName);
                                            } else if (responseCode == 401) {
                                                LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                                isClickable = false;
                                                try {
                                                    com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, binding.editTextUserName.getText().toString().trim(), binding.editTextPassword.getText().toString().trim(), Configuration.APP_ID, jsonObject -> {
                                                        runOnUiThread(() -> {
                                                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                            Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject, registrationModel, binding.editTextUserName.getText().toString().trim());
                                                        });
                                                    });
                                                } catch (IOException e1) {
                                                    e1.printStackTrace();
                                                }
                                            } else {
                                                isClickable = false;
                                                navigativeDistribute(hostName);
                                            }
                                        }
                                    } else {
                                        isClickable = false;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                LogManager.writeLogError("Registration can't be performed due to server unavailability. Please try later");
                                                navigativeDistribute(hostName);
                                            }

                                        });
                                    }
                                }
                                //Xumu != Ap1
                                else if (!server_Text.equalsIgnoreCase(server_Text_DR)) {
                                    if (pingServer(server_Text_DR)) {
                                        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(Constants.ActiveHost, server_Text_DR);
                                        editor.apply();
                                        server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                                        JSONObject resourceFileReadresponse = getResourceFile(server_Text_DR);
                                        if (resourceFileReadresponse != null) {
                                            JSONObject serverResponse = getJSONServerResponse(resourceFileReadresponse);
                                            boolean status = serverResponse.getBoolean(Constants.Status);
                                            int responseCode = serverResponse.getInt(Constants.ResponseCode);
                                            String messageError = serverResponse.getString(Constants.Message);
                                            if (responseCode == 200) {
                                                if (status) {
                                                    requestOnline(false);
                                                } else {
                                                    isClickable = false;
                                                    navigativeDistribute(hostName);
                                                }
                                            } else if (responseCode == 503 || responseCode == 404 || responseCode == 998 || responseCode == 999 || responseCode == 996) {
                                                // 503 - service unavailable , 404 - Resource file not maintained , 998 - Backend unavailable , 999 - Resource file is empty
                                                //996 - Resource file available but backend property missing
                                                isClickable = false;
                                                navigativeDistribute(hostName);
                                            } else if (responseCode == 401) {
                                                LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                                isClickable = false;
                                                try {
                                                    com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, binding.editTextUserName.getText().toString().trim(), binding.editTextPassword.getText().toString().trim(), Configuration.APP_ID, jsonObject -> {
                                                        runOnUiThread(() -> {
                                                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                            Constants.passwordStatusErrorMessage(RegistrationActivity.this, jsonObject, registrationModel, binding.editTextUserName.getText().toString().trim());
                                                        });
                                                    });
                                                } catch (IOException e1) {
                                                    e1.printStackTrace();
                                                }
                                            } else {
                                                isClickable = false;
                                                navigativeDistribute(hostName);
                                            }
                                        }
                                    } else {
                                        isClickable = false;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                new DialogFactory.Progress(RegistrationActivity.this).hide();
                                                LogManager.writeLogError("Registration can't be performed due to server unavailability. Please try later");
                                                navigativeDistribute(hostName);
                                            }

                                        });
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
                                isClickable = false;
                                Constants.showAlert(ErrorCode.NETWORK_ERROR, RegistrationActivity.this);
                            }
                        });
                    }
                }
            }).start();
        } else {
            initStore(false);
        }

    }

    private JSONObject getJSONServerResponse(JSONObject serverResponse){
        JSONObject resourceFileRes = new JSONObject();
        try {
            boolean status = serverResponse.getBoolean(Constants.Status);
            int responseCode = serverResponse.getInt(Constants.ResponseCode);
            String messageError = serverResponse.getString(Constants.Message);
            payload = serverResponse.getString(Constants.PayLoad);
            String hostName = serverResponse.getString(Constants.HostName);
            if (responseCode == 200) {
                if (status) {
                    String result[] = payload.split("\n");
                    if (result != null) {
                        if(payload.contains("BackendAvailable")) {
                            if (result.length >= 3) {
                                String registrationFlag = result[2].replace("BackendAvailable=", "");
                                if (registrationFlag != null && registrationFlag.equalsIgnoreCase("X")) {
                                    resourceFileRes.put(Constants.Status, true);
                                    resourceFileRes.put(Constants.ResponseCode, responseCode);
                                    resourceFileRes.put(Constants.Message, "");
                                } else {
                                    resourceFileRes.put(Constants.Status, false);
                                    resourceFileRes.put(Constants.ResponseCode, 998);
                                    resourceFileRes.put(Constants.Message, "Backend System unavailable. Registration cannot be performed now");
                                }
                            } else {
                                resourceFileRes.put(Constants.Status, false);
                                resourceFileRes.put(Constants.ResponseCode, 998);
                                resourceFileRes.put(Constants.Message, "Backend System unavailable. Registration cannot be performed now");
                            }
                        }else {
                            String message = "";
                            try {
                                if(hostName.equalsIgnoreCase(server_Text_default)){
                                    String[] hostSPilt = server_Text_default.split("-");
                                    String hostSPiltTemp = hostSPilt[1].replace(".hana.ondemand.com","");
                                    message = "Backend Property missing in "+hostSPiltTemp+" Resource file"+". Please contact support team";
                                }else  if(hostName.equalsIgnoreCase(server_Text_DR)){
                                    String[] hostSPilt = server_Text_DR.split("-");
                                    String hostSPiltTemp = hostSPilt[1].replace(".hana.ondemand.com","");
                                    message = "Backend Property missing in "+hostSPiltTemp+" Resource file"+". Please contact support team";
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                                message = "Backend Property missing in Resource file"+". Please contact support team";
                            }

                            resourceFileRes.put(Constants.Status, false);
                            resourceFileRes.put(Constants.ResponseCode, 996);
                            resourceFileRes.put(Constants.Message, message);
                        }
                    } else {
                        resourceFileRes.put(Constants.Status, false);
                        resourceFileRes.put(Constants.ResponseCode, 999);
                        resourceFileRes.put(Constants.Message, "Resource file is empty.Please contact to support team");
                    }
                } else {
                    resourceFileRes.put(Constants.Status, status);
                    resourceFileRes.put(Constants.ResponseCode, responseCode);
                    resourceFileRes.put(Constants.Message, messageError);
                }
            }else {
                resourceFileRes.put(Constants.Status, status);
                resourceFileRes.put(Constants.ResponseCode, responseCode);
                resourceFileRes.put(Constants.Message, messageError);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return resourceFileRes;
    }

    public void getResourceIDP(String hostName){
        try {
            if (UtilConstants.isNetworkAvailable(RegistrationActivity.this)) {
                String fetchURL = "";
                Uri.Builder builder = new Uri.Builder();
                builder.scheme(Configuration.IS_HTTPS ? "https" : "http")
                        .encodedAuthority(hostName + ":" + Configuration.port_Text)
                        .appendPath("mobileservices")
                        .appendPath("application")
                        .appendPath(APP_ID_RES)
                        .appendPath("bundles")
                        .appendPath("v1")
                        .appendPath("runtime")
                        .appendPath("bundle")
                        .appendPath("application")
                        .appendPath(APP_ID_RES)
                        .appendPath("bundle");
                fetchURL = builder.toString()+"/ResourceUtils";
                String finalFetchURL = fetchURL;
                HttpsURLConnection connection = null;
                int responseCode = 0;
                try {
                    URL url = new URL(finalFetchURL);
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setReadTimeout(Configuration.connectionTimeOut);
                    connection.setConnectTimeout(Configuration.connectionTimeOut);
                    connection.setRequestProperty("x-smp-appid", APP_ID_RES);
                    //                connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    connection.setDefaultUseCaches(false);
                    connection.setAllowUserInteraction(false);
                    connection.connect();
                    LogManager.writeLogInfo("getResourceFileIDP Qry " + finalFetchURL);
                    responseCode = connection.getResponseCode();
                    connection.getResponseMessage();
                    String resultJson = "";
                    InputStream stream = null;
                    if (responseCode == 200) {
                        stream = connection.getInputStream();
                        if (stream != null) {
                            resultJson = DeviceIDUtils.readResponse(stream);
                        }
                        String result[] = resultJson.split("\n");
                        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("IDPURL", result[0].replace("IDPurl=", ""));
                        editor.putString("IDPUserId", result[1].replace("IDPID=", ""));
                        editor.putString("IDPPass", result[2].replace("IDPPwd=", ""));
                        editor.commit();
                        getIDPSharedPrefValue(RegistrationActivity.this);
                        registrationModel.setIDPURL(Configuration.IDPURL);
                        registrationModel.setExternalTUserName(Configuration.IDPTUSRNAME);
                        registrationModel.setExternalTPWD(Configuration.IDPTUSRPWD);
                    } else {
                        runOnUiThread(() -> {
                            new DialogFactory.Progress(RegistrationActivity.this).hide();
                        });
                        Constants.showAlert("IDP Resource file not maintained.",this);
                    }
                } catch (Throwable e) {
                    isClickable = false;
                }

            } else {
                isClickable = false;
                runOnUiThread(() -> {
                    new DialogFactory.Progress(RegistrationActivity.this).hide();
                });
                Constants.showAlert(ErrorCode.NETWORK_ERROR,RegistrationActivity.this);

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private boolean channelCheck(){
       return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void handleOverlaySettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                new DialogFactory.Alert(RegistrationActivity.this).setMessage( getString(R.string.this_app_needs_OVERLAY_permission)).isAlert(true)
                        .setTheme(R.style.MaterialAlertDialog).setPositiveButton(getString(R.string.autodate_change_btn))
                        .setOnDialogClick(isPositive -> {
                            if (isPositive) {
                                Intent intent = new  Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + getPackageName()));
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(intent, 01230);
                                }
                            }
                        })
                        .show();
            }
        }
    }




    private int imgCount =0;
    private boolean imageResponse= false;
    private void  getDetails(){
        ConstantsUtils.getRollId(RegistrationActivity.this, new AsyncTaskCallBack() {
            @Override
            public void onStatus(boolean status, String values) {
                try {
                    if (status) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new DialogFactory.Progress(RegistrationActivity.this)
                                        .setProgressMessage("Fetching Authorization Info. Please wait...");
                                message = "Fetching Authorization Info. Please wait...";
                                ;
                            }
                        });
                        ConstantsUtils.getUserAuthInfo(RegistrationActivity.this, new AsyncTaskCallBack() {
                            @Override
                            public void onStatus(boolean status, String values) {
                                if (status) {
                                    SyncUtils.checkAndCreateDB(RegistrationActivity.this);
                                    new OpenOfflineStoreAsync(false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    try {
                                        runOnUiThread(() -> new DialogFactory.Progress(RegistrationActivity.this).hide());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if(values.contains("403")){
                                        clearAppData();
                                    }else {
                                        runOnUiThread(() -> Constants.showAlert(values, RegistrationActivity.this));
                                    }
                                }
                            }
                        });
                                /*SyncUtils.checkAndCreateDB(RegistrationActivity.this);
                                Log.e("ROLE",values);
                                new OpenOfflineStoreAsync(false).execute();*/

                    } else {
                        try {
                            runOnUiThread(() -> new DialogFactory.Progress(RegistrationActivity.this).hide());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(values.contains("403")){
                            clearAppData();
                        }else {
                            runOnUiThread(() -> Constants.showAlert(values, RegistrationActivity.this));
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

}


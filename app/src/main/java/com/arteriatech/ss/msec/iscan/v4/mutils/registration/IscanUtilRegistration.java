package com.arteriatech.ss.msec.iscan.v4.mutils.registration;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.http.HttpException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.mutils.registration.MainMenuBean;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.log.LogActivity;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack;
import com.arteriatech.ss.msec.iscan.v4.registration.Configuration;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class IscanUtilRegistration extends AppCompatActivity implements View.OnClickListener {
    public static final String KEY_AppName = "AppName";
    public static final String KEY_username = "username";
    public static final String KEY_password = "password";
    public static final String KEY_serverHost = "serverHost";
    public static final String KEY_serverPort = "serverPort";
    public static final String KEY_serverClient = "servreerClient";
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
    public static final String EXTRA_IS_FROM_REGISTRATION = "isRegistrationExtra";
    public static final String EXTRA_BUNDLE_REGISTRATION = "isRegBundleExtra";
    public static String loginUser_Text = "";
    public static String login_pwd = "";
    AlertDialog progressDialog = null;
    private com.arteriatech.ss.msec.iscan.v4.mutils.registration.RegistrationModel registrationModel = null;

    private Handler mHandler = null;
    private int mCurrentAttempt = 0;
    private int totalAttempt = 3;
    private SharedPreferences sharedPreferences;
    private Context mContext;
    private Bundle bundleExtra = null;

    private ArrayList<String> userList = new ArrayList();
    private DataServiceException dataServiceException = null;
    private HttpException httpException = null;
    private String errorMsg = "";
    private String otp = "";
    private String errorMessage = "";
    private String cdata = "";
    private String alertData = "";
    private String dataFromDl = "";

    public IscanUtilRegistration() {
    }

    public static String getAppURL(String serverText, String port, String appID, boolean isHttps) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(isHttps ? "https" : "http").encodedAuthority(serverText + ":" + port).appendPath("odata").appendPath("applications").appendPath("v4").appendPath(appID);
        return builder.build().toString();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrivateDataVault.init(this.getApplicationContext());
        registrationModel = new com.arteriatech.ss.msec.iscan.v4.mutils.registration.RegistrationModel();
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
        ArrayList<com.arteriatech.mutils.registration.MainMenuBean> mainMenuBeanArrayList = new ArrayList<>();
        com.arteriatech.mutils.registration.MainMenuBean mainMenuBean = new MainMenuBean();
        mainMenuBean.setActivityRedirect(LogActivity.class);
        mainMenuBean.setMenuImage(R.drawable.ic_log_list);
        mainMenuBean.setMenuName("View");
        mainMenuBeanArrayList.add(mainMenuBean);
        registrationModel.setMenuBeen(mainMenuBeanArrayList);


      //  LogManager.initialize(this, this.registrationModel);
        PrivateDataVault.init(this.getApplicationContext());
        this.mContext = this;
        this.mHandler = new Handler();
        if (this.registrationModel != null) {
            int icon = 0;
            if (this.registrationModel.getAppActionBarIcon() != 0) {
                icon = this.registrationModel.getAppActionBarIcon();
            }

            this.sharedPreferences = this.getSharedPreferences(this.registrationModel.getShredPrefKey(), 0);
            String strPref = this.sharedPreferences.getString("username", (String) null);
            //if (this.registrationModel.getLayout() != 0) {
                this.setContentView(R.layout.activity_splash_screen_new);
            //}


            //UtilConstants.CheckChromeVersion(this);
        }
        testUseAndroidString();

    }

    private void checkUserIsActive() {
        progressDialog = UtilConstants.showProgressDialogs(this, "Getting user info please wait", false);
        String host = "https://" + Configuration.server_Text + "/" + Configuration.APP_ID;
        String username=sharedPreferences.getString("mobileNo","");
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
                                    checkPasswordStatus();
                                } else {
                                    closeProgDialog();
                                    if (!TextUtils.isEmpty(errorMessage))
                                        UtilConstants.showAlert(errorMessage, IscanUtilRegistration.this);
                                    else
                                        UtilConstants.showAlert("Your account is currently Inactive. Kindly contact DMS Helpdesk.", IscanUtilRegistration.this);
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

    String pwdStatus, pusrID;

    private void checkPasswordStatus() {
        try {
            String userName = sharedPreferences.getString(Constants.LoginID,"");
            UtilRegistrationActivity.loginUser_Text = userName.trim();
            UtilRegistrationActivity.login_pwd = "Welcome@2025";
            com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(IscanUtilRegistration.this.registrationModel.getIDPURL(), UtilRegistrationActivity.loginUser_Text, UtilRegistrationActivity.login_pwd, IscanUtilRegistration.this.registrationModel.getIDPURL(), new com.arteriatech.mutils.common.UtilConstants.PasswordStatusCallback() {
                public void passwordStatus(final JSONObject jsonObject) {
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            String message = "";
                            String code = "";
                            JSONObject jsonUsrDetails = null;
                            if (jsonObject.has("message")) {
                                message = jsonObject.optString("message");
                                LogManager.writeLogInfo("Password Status Response message : " + message);
                            }
                            if (jsonObject.has("code")) {
                                code = jsonObject.optString("code");
                            }
                            if ((message.equalsIgnoreCase("PASSWORD_CHANGE_REQUIRED") || message.equalsIgnoreCase("PASSWORD_RESET_REQUIRED")) && registrationModel != null) {
                                SharedPreferences.Editor spEditer = sharedPreferences.edit();
                                spEditer.putString(Constants.AutoPasswordForReset, "Welcome@2026");
                                spEditer.apply();
                                String strPref = sharedPreferences.getString(Constants.AutoPasswordForReset, "Welcome@2025");
                                extendPassword(mContext, registrationModel.getIDPURL(), registrationModel.getExternalTUserName(), registrationModel.getExternalTPWD(), UtilRegistrationActivity.loginUser_Text, strPref);
                            } else if (!TextUtils.isEmpty(code) && code.equalsIgnoreCase("401")) {
                                SharedPreferences.Editor spEditer = sharedPreferences.edit();
                                spEditer.putString(Constants.AutoPasswordForReset, "Welcome@2026");
                                spEditer.apply();
                                String strPref = sharedPreferences.getString(Constants.AutoPasswordForReset, "Welcome@2025");
                                extendPassword(mContext, registrationModel.getIDPURL(), registrationModel.getExternalTUserName(), registrationModel.getExternalTPWD(), UtilRegistrationActivity.loginUser_Text, strPref);
                            } else {
                                closeProgDialog();
                                if (!TextUtils.isEmpty(errorMessage)) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            UtilConstants.showAlert(errorMessage, IscanUtilRegistration.this);

                                        }

                                    });
                                }
                                else
                                    UtilConstants.showAlert("Your account is currently Inactive. Kindly contact DMS Helpdesk.", IscanUtilRegistration.this);
                            }

                            /*UtilRegistrationActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    UtilRegistrationActivity.this.closeProgDialog();
                                    Intent intentNavChangePwdScreen;
                                    if (pwdStatus.equalsIgnoreCase("initial")) {
                                        intentNavChangePwdScreen = new Intent(UtilRegistrationActivity.this, InitialPasswordActivity.class);
                                        intentNavChangePwdScreen.putExtra("isRegBundleExtra", UtilRegistrationActivity.this.bundleExtra);
                                        intentNavChangePwdScreen.putExtra(UtilConstants.RegIntentKey, UtilRegistrationActivity.this.registrationModel);
                                        intentNavChangePwdScreen.putExtra("isRegisteration", true);
                                        intentNavChangePwdScreen.putExtra("P_USER_ID", pusrID);
                                        UtilRegistrationActivity.this.startActivity(intentNavChangePwdScreen);
                                        UtilRegistrationActivity.this.finish();
                                    } else if (UtilRegistrationActivity.this.registrationModel.getRegisterSuccessActivity() != null) {
                                        try {
                                            intentNavChangePwdScreen = new Intent(UtilRegistrationActivity.this, UtilRegistrationActivity.this.registrationModel.getRegisterSuccessActivity());
                                            intentNavChangePwdScreen.putExtra("isRegistrationExtra", true);
                                            if (UtilRegistrationActivity.this.bundleExtra != null) {
                                                intentNavChangePwdScreen.putExtra("isRegBundleExtra", UtilRegistrationActivity.this.bundleExtra);
                                            }

                                            intentNavChangePwdScreen.putExtra(UtilConstants.RegIntentKey, UtilRegistrationActivity.this.registrationModel);
                                            UtilRegistrationActivity.this.startActivity(intentNavChangePwdScreen);

                                            try {
                                                Toast.makeText(UtilRegistrationActivity.this.mContext, UtilRegistrationActivity.this.mContext.getString(string.registration_success), Toast.LENGTH_SHORT).show();
                                            } catch (Exception var3) {
                                                var3.printStackTrace();
                                            }

                                            UtilRegistrationActivity.this.finish();
                                        } catch (Exception var4) {
                                            var4.printStackTrace();
                                        }
                                    }

                                }
                            });*/
                        }
                    });
                    thread.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extendPassword(final Context mContext, final String domineUrl, final String tUserName, final String tPsw, final String pUserID, final String password) {

        /*this.pdLoadDialog = new ProgressDialog(mContext, style.ProgressDialogTheme);
        this.pdLoadDialog.setMessage(this.getString(string.update_pwd_please_wait));
        this.pdLoadDialog.setCancelable(false);
        this.pdLoadDialog.show();*/
        (new Thread(new Runnable() {
            public void run() {
                String url = "";
                if (pUserID != null && pUserID.contains("@")) {
                    url = domineUrl + "/service/scim/Users?filter=emails%20eq%20'" + pUserID + "'";
                } else {
                    url = domineUrl + "/service/scim/Users?filter=userName%20eq%20'" + pUserID + "'";
                }

                String puserID = pUserID;

                try {
                    String jsonValue = UtilConstants.getPuserIdUtilsReponse(new URL(url), tUserName, tPsw);
                    if (!TextUtils.isEmpty(jsonValue)) {
                        JSONObject jsonObject = new JSONObject(jsonValue);
                        JSONArray jsonArray = jsonObject.optJSONArray("Resources");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            puserID = jsonArray.getJSONObject(0).getString("id");
                        }

                        if (!TextUtils.isEmpty(puserID)) {
                            String url1 = domineUrl + "/service/scim/Users/" + puserID;
                            String validatePuser = UtilConstants.getPuserIdUtilsReponse(new URL(url1), tUserName, tPsw);
                            if (!TextUtils.isEmpty(validatePuser)) {
                                JSONObject userObject = new JSONObject(validatePuser);
                                String userStatus = userObject.optString("passwordStatus");
                                JSONObject metaObject = userObject.getJSONObject("meta");
                                JSONArray schemasArray = userObject.optJSONArray("schemas");
                                JSONObject bodyObject = new JSONObject();
                                bodyObject.put("id", puserID);
                                bodyObject.put("password", password);
                                bodyObject.put("passwordStatus", "enabled");
                                bodyObject.put("meta", metaObject);
                                bodyObject.put("schemas", schemasArray);
                                String changePassword = UtilConstants.getPswResetUtilsReponse(new URL(url1), tUserName, tPsw, bodyObject.toString());
                                if (!TextUtils.isEmpty(changePassword)) {
                                    try {
                                        JSONObject userPObject = new JSONObject(changePassword);
                                        String userPStatus = userPObject.optString("passwordStatus");
                                        if (!TextUtils.isEmpty(userPStatus) && userPStatus.equalsIgnoreCase("enabled")) {
                                            // setPwdInDataVault(mContext, password);

                                            try {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        try {
                                                            //pdLoadDialog.cancel();
                                                            closeProgDialog();
                                                        } catch (Exception var2) {
                                                            var2.printStackTrace();
                                                        }

                                                        UtilConstants.dialogBoxWithCallBack(IscanUtilRegistration.this, "", getResources().getString(R.string.extend_pwd_updated_succefully_temp), IscanUtilRegistration.this.getString(R.string.OK), "", false, new DialogCallBack() {
                                                            public void clickedStatus(boolean clickedStatus) {
                                                                if (clickedStatus) {
                                                                    IscanUtilRegistration.this.finishAffinity();
                                                                    System.exit(0);
                                                                }

                                                            }
                                                        });
                                                    }
                                                });
                                            } catch (Exception var17) {
                                                var17.printStackTrace();
                                            }
                                        } else {
                                            displayErrorMessage(mContext.getString(R.string.extend_pwd_error_occured) + " " + userPStatus, mContext);
                                        }
                                    } catch (JSONException var18) {
                                        var18.printStackTrace();
                                        displayErrorMessage(changePassword + " Please use different password", mContext);
                                    }
                                } else {
                                    displayErrorMessage(mContext.getString(R.string.extend_pwd_error_occured), mContext);
                                }
                            } else {
                                SharedPreferences.Editor spEditer = sharedPreferences.edit();
                                spEditer.putString(Constants.AutoPasswordForReset, "Welcome@2025");
                                spEditer.apply();
                                displayErrorMessage(mContext.getString(R.string.extend_pwd_error_occured), mContext);
                            }
                        } else {
                            displayErrorMessage(mContext.getString(R.string.extend_pwd_error_occured), mContext);
                        }
                    } else {
                        displayErrorMessage(mContext.getString(R.string.no_network_conn), mContext);
                    }
                } catch (IOException var19) {
                    var19.printStackTrace();
                    displayErrorMessage(var19.getMessage(), mContext);
                } catch (JSONException var20) {
                    var20.printStackTrace();
                    displayErrorMessage(var20.getMessage(), mContext);
                } catch (Exception var21) {
                    var21.printStackTrace();
                    displayErrorMessage(var21.getMessage(), mContext);
                }

            }
        })).start();
    }

    private void displayErrorMessage(final String strMsg, final Context mContext) {
        try {
            this.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        //   pdLoadDialog.cancel();
                        closeProgDialog();

                        if (!TextUtils.isEmpty(errorMessage))
                            UtilConstants.showAlert(errorMessage, IscanUtilRegistration.this);
                        else
                            UtilConstants.showAlert("Your account is currently Inactive. Kindly contact DMS Helpdesk.", IscanUtilRegistration.this);

                    } catch (Exception var2) {
                        var2.printStackTrace();
                    }

                    Toast.makeText(mContext, strMsg, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    /*  JSONObject jsonHeaderObject=null;
      private void submitData() {
          Hashtable dbHeadTable = new Hashtable();

          {
              "AggregatorID": "AGGR0135",
                  "LoginName": "9844130130",
                  "Password": "Welcome@123",
                  "OTP": "142131",
                  "IsLoginNameMobileNo": "YES"
          }


              dbHeadTable.put("AggregatorID", "AGGR0135");
              dbHeadTable.put("LoginName", "9844130130");
              dbHeadTable.put("Password", "Welcome@123");
              dbHeadTable.put("OTP", "142131");

          dbHeadTable.put("IsLoginNameMobileNo", "YES");
          jsonHeaderObject = new JSONObject(dbHeadTable);
          new postRegistrationData().execute();
      }
      class postRegistrationData extends AsyncTask<Void,Void,Void> {

          @Override
          protected void onPreExecute() {
              super.onPreExecute();
            //  swipeRefresh.setRefreshing(true);
          }


          @Override
          protected Void doInBackground(Void... voids) {

              // JSONObject headerObject = Constants.getOrderApprovalHeaderValueFromHEaderObject(jsonHeaderObject);
              String mStrDateTime="";
              try {
                  String createdOn = UtilConstants.getNewDateTimeFormat();
                  String createdAt= UtilConstants.getOdataDuration().toString();
                  mStrDateTime  = UtilConstants.getReArrangeDateFormat(createdOn) + Constants.T + UtilConstants.convertTimeOnly(createdAt);
              } catch (Exception e) {
                  e.printStackTrace();
              }
              OnlineManager.createMobileRegistrationEntity(jsonHeaderObject.toString(),"4",mStrDateTime, Constants.APPREGISTRATION, new UIListener() {
                  @Override
                  public void onRequestError(int operation, Exception e) {

                      Log.d("Message","s"+e.getMessage());
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              closeProgDialog();
                              UtilConstants.showAlert(e.getMessage(),UtilRegistrationActivity.this);
                          }
                      });

                      *//*((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(view!=null) {
                                view.hideProgress();
                            }
                        }
                    });*//*
                }

                @Override
                public void onRequestSuccess(int operation, String responseBody) throws ODataException, OfflineODataStoreException {
                    String docNo = "";
                    JSONObject jsonObject = null;
                    JSONObject dObject = null;
                    RegistrationViewBean resultData = new RegistrationViewBean();
                    try {
                        jsonObject = new JSONObject(responseBody);

                        String  dd = jsonObject.optString("message");
                        JSONArray json_data1 = new JSONArray(dd);
                        JSONObject res = null;



                       *//* JSONArray jsonArray = jsonObject.getJSONArray("Description");
                        JSONObject j1 = new JSONObject(dd);
                        JSONArray entities = jsonObject.getJSONArray("Description");

                            String dsd = j1.optString("batterySerialNumber");
                            dObject = jsonObject.getJSONObject("d");*//*
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               closeProgDialog();

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, UtilRegistrationActivity.this);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }*/
    private String csrfToken;
    List<String> setCookies = new ArrayList<>();


    private void validateUser() {
        progressDialog = UtilConstants.showProgressDialogs(this, "Getting user info please wait", false);
        String host = "https://" + Configuration.server_Text + "/" + Configuration.APP_ID;

        String url = host + "/UserProfiles('PD')";
        onRegister(IscanUtilRegistration.this);

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean isValidatedUser = false;
                    String pass = sharedPreferences.getString(Constants.AutoPasswordForReset, "Welcome@2025");
                    if (TextUtils.isEmpty(pass))
                        pass = "Welcome@2025";

                    isValidatedUser = getUserDetials(new URL(url), txtusername.getText().toString().trim(), pass);
                    if (isValidatedUser) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onRegister(IscanUtilRegistration.this);
                               // showEntereOTPDialog();
                                closeProgDialog();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeProgDialog();
                                UtilConstants.showAlert("User is not Authorized for Exide Humsafar Application.Please Contact Channel Team", IscanUtilRegistration.this);
                            }
                        });

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

    private String fcmToken="";

    HttpsURLConnection connection = null;
    JSONObject jsonHeaderObject = null;

    private boolean getUserDetials(URL url, String userName, String psw) {
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


            if (responseCode != 200) {
                throw new IOException("HTTP error code: " + responseCode);
            } else if (responseCode == 200) {
                csrfToken = connection.getHeaderField("X-CSRF-Token");
                setCookies.addAll(connection.getHeaderFields().get("Set-Cookie"));
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

    private boolean postDataToServer(final URL url, final String userName, final String psw, final String csrfToken, final String body) {
        boolean result = false;
        // InputStream stream = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(30000);
            connection.setConnectTimeout(30000);
            String userCredentials = userName + ":" + psw;
            String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes("UTF-8"), 2);
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestProperty("x-smp-appid",Configuration.APP_ID);
            connection.setRequestProperty("X-CSRF-Token", csrfToken);
            for (int i = 0; i < setCookies.size(); i++) {
                connection.addRequestProperty("Cookie", setCookies.get(i));
            }
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            //connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(body.getBytes("UTF-8"));
            os.close();
            int responseCode = connection.getResponseCode();
            if (responseCode != 200 && responseCode != 400 && responseCode != 201) {
                //    extralogToStorage("HTTP error code : " + responseCode + " - " + doc_id);
                throw new IOException("HTTP error code: " + responseCode);
            }
            if (responseCode == 200) {
                InputStream stream = connection.getInputStream();
                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append('\n');
                }
                Log.d("Stram", "" + buffer.toString());
                Log.i("Geo", "postedSuccefully");
                result = true;
                SharedPreferences.Editor spEditer = sharedPreferences.edit();
                spEditer.putString(Constants.AutoPasswordForReset, "Welcome@2025");
                spEditer.apply();

            }

                  /*  if (responseCode == 200) {
                        stream = connection.getInputStream();
                        if (stream != null) {
                            //   result = readResponse(stream);
                        }
                    } else {
                        stream = connection.getErrorStream();
                        if (stream != null) {
                            // result = readResponse(stream);
                        }
                    }*/
        } catch (Exception var14) {
            var14.printStackTrace();
        } finally {
                   /* if (stream != null) {

                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }*/

		/*	if (connection != null) {
				connection.disconnect();
			}*/

        }


        return result;

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


    private void onSaveConfig(RegistrationModel registrationModel, com.sap.smp.client.smpclient.Connection newConnection) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("username", loginUser_Text);
        editor.putString("password", login_pwd);
        editor.putString("serverHost", registrationModel.getServerText());
        editor.putString("serverPort", registrationModel.getPort());
        editor.putString("securityConfig", registrationModel.getSecConfig());
        editor.putString("appID", registrationModel.getAppID());
        editor.putString("farmId", registrationModel.getFormID());
        editor.putString("suffix", registrationModel.getSuffix());
        editor.putBoolean("isHttps", registrationModel.getHttps());
        editor.putString("appConnID", newConnection.getApplicationConnectionId());
        editor.putString("appEndPoint", "");
        editor.putString("pushEndPoint", "");
        editor.putString("SalesPersonName", "");
        editor.putString("SalesPersonMobileNo", "");
        editor.putBoolean("isPasswordSaved", true);
        editor.putBoolean("isDeviceRegistered", true);
        editor.putBoolean("isFirstTimeReg", true);
        editor.putBoolean("isForgotPwdActivated", false);
        editor.putBoolean("isManadtoryUpdate", true);
        editor.putBoolean("isUserIsLocked", false);
        editor.putString("ForgotPwdOTP", "");
        editor.putString("ForgotPwdGUID", "");
        editor.putString("isFOSUserRole", "");
        editor.putInt("MaximumAttemptKey", 0);
        editor.putInt("VisitSeqId", 0);
        editor.putString("BirthDayAlertsDate", UtilConstants.getDate1());
        editor.apply();
    }



    public boolean onDeviceReg() {
        try {
            this.dataServiceException = null;
            String requestUri = getAppURL(this.registrationModel.getServerText(), this.registrationModel.getPort(), this.registrationModel.getAppID(), this.registrationModel.getHttps());
            final UsernamePasswordToken token = new UsernamePasswordToken(loginUser_Text, login_pwd);
            HttpConversationManager conversationManager = (new CommonAuthFlowsConfigurator(this.mContext)).supportBasicAuthUsing(new UsernamePasswordProvider() {
                public Object onCredentialsNeededUpfront(ISendEvent event) {
                    return token;
                }

                public Object onCredentialsNeededForChallenge(IReceiveEvent event) {
                    return token;
                }
            }).configure(new HttpConversationManager(this.mContext));
            SDKHttpHandler sdkHttpHandler = new SDKHttpHandler(this.mContext, conversationManager);
            AndroidSystem.setContext(this.mContext);
            OnlineODataProvider registrationProvider = new OnlineODataProvider("SmpClient", requestUri);
            registrationProvider.getNetworkOptions().setHttpHandler(sdkHttpHandler);
            registrationProvider.getServiceOptions().setCheckVersion(false);
            SmpClient smpClient = new SmpClient(registrationProvider);
            com.sap.smp.client.smpclient.Connection newConnection = new Connection();
            newConnection.setDeviceType("Android");
            if(!TextUtils.isEmpty(fcmToken)) {
                newConnection.setAndroidGcmRegistrationId(fcmToken);
                newConnection.setAndroidGcmPushEnabled(true);
            }
            newConnection.setDefaultValues();
            if (UtilConstants.isNetworkAvailable(this)) {
                try {
                    try {
                        smpClient.createEntity(newConnection);
                        this.onSaveConfig(this.registrationModel, newConnection);
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
                        UtilConstants.dialogBoxWithCallBack(IscanUtilRegistration.this, "", IscanUtilRegistration.this.getString(R.string.reg_no_network_conn, new Object[]{"A3000"}), IscanUtilRegistration.this.getString(R.string.network_retry), IscanUtilRegistration.this.getString(R.string.cancel), false, new DialogCallBack() {
                            public void clickedStatus(boolean clickedStatus) {
                                if (clickedStatus) {
                                    IscanUtilRegistration.this.requestOnline();
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

    private void onRegister(Context mContext) {
       // if (!this.getValues()) {
            String userName = sharedPreferences.getString(Constants.LoginID,"");
            this.mCurrentAttempt = Collections.frequency(this.userList, userName);
            if (this.mCurrentAttempt <= this.totalAttempt) {
                this.requestOnline();
            } else {
                UtilConstants.showAlert("[A1000] " + this.getString(R.string.wrong_psw_error_msg_3, new Object[]{String.valueOf(this.totalAttempt)}), this);
            }
       // }

    }

    private void requestOnline() {
        if (UtilConstants.isNetworkAvailable(this)) {
            this.progressDialog = UtilConstants.showProgressDialogs(this, this.getString(R.string.register_with_server_plz_wait), false);
            loginUser_Text =sharedPreferences.getString(Constants.LoginID,"");
            login_pwd = "Welcome@2025";
            String username=sharedPreferences.getString("username","");
            if(TextUtils.isEmpty(username)){
                (new AsynTaskRegistration()).execute(new Void[0]);
            }else{
                Intent intent=new Intent(IscanUtilRegistration.this,SalesOrderActivity.class);
                startActivity(intent);
            }

        } else {
            UtilConstants.dialogBoxWithCallBack(this, "", this.getString(R.string.reg_no_network_conn, new Object[]{"A3000"}), this.getString(R.string.network_retry), this.getString(R.string.cancel), false, new DialogCallBack() {
                public void clickedStatus(boolean clickedStatus) {
                    if (clickedStatus) {
                        IscanUtilRegistration.this.requestOnline();
                    }

                }
            });
            LogManager.writeLogError(this.getString(R.string.reg_no_network_conn, new Object[]{"A3000"}));
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
                    com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(this.registrationModel.getIDPURL(), loginUser_Text, login_pwd, this.registrationModel.getAppID(), new com.arteriatech.mutils.common.UtilConstants.PasswordStatusCallback() {
                        public void passwordStatus(final JSONObject jsonObject) {
                            IscanUtilRegistration.this.runOnUiThread(new Runnable() {
                                @SuppressLint({"StringFormatInvalid"})
                                public void run() {
                                    IscanUtilRegistration.this.closeProgDialog();
                                    String finalErrorMsg = "";
                                    if (jsonObject != null) {
                                        int code = jsonObject.optInt("code");
                                        String message = jsonObject.optString("message");
                                        if (code == 401) {
                                            if (message.equalsIgnoreCase("")) {
                                                finalErrorMsg = IscanUtilRegistration.this.getString(R.string.userid_error_message, new Object[]{"A3000"});
                                            } else if (message.equalsIgnoreCase("Unauthorized")) {
                                                finalErrorMsg = IscanUtilRegistration.this.getString(R.string.unauthorized_error_message, new Object[]{"A3000"});
                                            } else if (message.equalsIgnoreCase("USER_INACTIVE")) {
                                                finalErrorMsg = IscanUtilRegistration.this.getString(R.string.forget_pwd_user_inactive_error_message, new Object[]{"A3000"});
                                            } else if (message.equalsIgnoreCase("PASSWORD_LOCKED")) {
                                                finalErrorMsg = IscanUtilRegistration.this.getString(R.string.login_password_lock_error_message, new Object[]{"A3000"});
                                            }

                                            LogManager.writeLogError(finalErrorMsg);
                                            com.arteriatech.mutils.common.UtilConstants.passwordStatusErrorMessage(IscanUtilRegistration.this, jsonObject, UtilRegistrationActivity.loginUser_Text, IscanUtilRegistration.this.registrationModel.getIDPURL(), IscanUtilRegistration.this.registrationModel.getExternalTUserName(), IscanUtilRegistration.this.registrationModel.getExternalTPWD(), IscanUtilRegistration.this.registrationModel.getAppID());
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

    private void userIDandPasswordcheck() {
        LogManager.writeLogInfo("Registration failed  due to " + this.errorMsg);
        if (this.errorMsg.contains("No address associated with hostname")) {
            LogManager.writeLogInfo("Registration failed  due to network unavailability");
            this.diaplyErrorMsg("Registration cannot be performed due to network unavailability", 0);
        } else if (this.errorMsg.contains("Maximum restarts reached")) {
            if (UtilConstants.isNetworkAvailable(this)) {
                try {
                    com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(this.registrationModel.getIDPURL(), loginUser_Text, login_pwd, this.registrationModel.getAppID(), new com.arteriatech.mutils.common.UtilConstants.PasswordStatusCallback() {
                        public void passwordStatus(final JSONObject jsonObject) {
                            IscanUtilRegistration.this.runOnUiThread(new Runnable() {
                                @SuppressLint({"StringFormatInvalid"})
                                public void run() {
                                    IscanUtilRegistration.this.closeProgDialog();
                                    if (jsonObject != null) {
                                        String finalErrorMsg = "";
                                        int code = jsonObject.optInt("code");
                                        String message = jsonObject.optString("message");
                                        if (code == 401) {
                                            if (message.equalsIgnoreCase("")) {
                                                finalErrorMsg = IscanUtilRegistration.this.getString(R.string.userid_error_message, new Object[]{"A3000"});
                                            } else if (message.equalsIgnoreCase("Unauthorized")) {
                                                finalErrorMsg = IscanUtilRegistration.this.getString(R.string.unauthorized_error_message, new Object[]{"A3000"});
                                            } else if (message.equalsIgnoreCase("USER_INACTIVE")) {
                                                finalErrorMsg = IscanUtilRegistration.this.getString(R.string.forget_pwd_user_inactive_error_message, new Object[]{"A3000"});
                                            } else if (message.equalsIgnoreCase("PASSWORD_LOCKED")) {
                                                finalErrorMsg = IscanUtilRegistration.this.getString(R.string.login_password_lock_error_message, new Object[]{"A3000"});
                                            }

                                            LogManager.writeLogError(finalErrorMsg);
                                            com.arteriatech.mutils.common.UtilConstants.passwordStatusErrorMessage(IscanUtilRegistration.this, jsonObject, UtilRegistrationActivity.loginUser_Text, IscanUtilRegistration.this.registrationModel.getIDPURL(), IscanUtilRegistration.this.registrationModel.getExternalTUserName(), IscanUtilRegistration.this.registrationModel.getExternalTPWD(), IscanUtilRegistration.this.registrationModel.getAppID());
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

    @Override
    public void onClick(View view) {

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
            return IscanUtilRegistration.this.onDeviceReg();
        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (IscanUtilRegistration.this.dataServiceException == null && result) {
                Intent intent=new Intent(IscanUtilRegistration.this,SalesOrderActivity.class);
                startActivity(intent);
            } else if (IscanUtilRegistration.this.httpException != null) {

            } else {

            }

        }
    }

    public void testUseAndroidString() {
        Intent intent = getIntent();
        String receivedText = intent.getStringExtra("username");
        System.out.println("received no " + receivedText);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString("mobileNo",receivedText);
        editor.commit();

        checkUserIsActive();




    }
}


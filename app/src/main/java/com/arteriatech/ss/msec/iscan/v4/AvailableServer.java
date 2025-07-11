package com.arteriatech.ss.msec.iscan.v4;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.mutils.store.CredentialsProvider;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.MyUtils;
import com.arteriatech.ss.msec.iscan.v4.log.ErrorCode;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity;
import com.arteriatech.ss.msec.iscan.v4.registration.Configuration;
import com.arteriatech.ss.msec.iscan.v4.registration.RegistrationActivity;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.sap.client.odata.v4.DataServiceException;
import com.sap.client.odata.v4.OnlineODataProvider;
import com.sap.client.odata.v4.core.AndroidSystem;
import com.sap.client.odata.v4.http.HttpException;
import com.sap.client.odata.v4.http.SDKHttpHandler;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.IHttpConversation;
import com.sap.smp.client.httpc.authflows.CommonAuthFlowsConfigurator;
import com.sap.smp.client.httpc.authflows.UsernamePasswordProvider;
import com.sap.smp.client.httpc.authflows.UsernamePasswordToken;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.httpc.events.ISendEvent;
import com.sap.smp.client.httpc.listeners.IResponseListener;
import com.sap.smp.client.smpclient.Connection;
import com.sap.smp.client.smpclient.SmpClient;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.APP_ID;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.IS_HTTPS;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.farm_ID;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.port_Text;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.secConfig_Text;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.server_Text;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.server_Text_DR;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.server_Text_default;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.suffix;

public class AvailableServer {

    public static boolean pingServer(String hostName){
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

    public static JSONObject getResourceFile(String hostName,Context context){
        JSONObject resourceFileRes = new JSONObject();
        try {
            if (UtilConstants.isNetworkAvailable(context)) {
                String userName = context.getSharedPreferences(Constants.PREFS_NAME,0).getString(UtilRegistrationActivity.KEY_username, "");
                String password = context.getSharedPreferences(Constants.PREFS_NAME,0).getString(UtilRegistrationActivity.KEY_password, "");
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
                    String userCredentials = userName + ":" + password;
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
                            LogManager.writeLogError("Available Server error : "+e.getMessage());
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
                    LogManager.writeLogError("Available Server error : "+e.getMessage());
                }
                return resourceFileRes;

            } else {
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

    public static JSONObject getJSONServerResponse(JSONObject serverResponse){
        JSONObject resourceFileRes = new JSONObject();
        try {
            boolean status = serverResponse.getBoolean(Constants.Status);
            int responseCode = serverResponse.getInt(Constants.ResponseCode);
            String messageError = serverResponse.getString(Constants.Message);
            String payLoad = serverResponse.getString(Constants.PayLoad);
            String hostName = serverResponse.getString(Constants.HostName);
            if (responseCode == 200) {
                if (status) {
                    String result[] = payLoad.split("\n");
                    if (result != null) {
                        if(payLoad.contains("BackendAvailable")) {
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

    public static void registerWithAvailableServer(Context context, RegisterServer registerServer){
        clearCookies();
        requestOnline(context,registerServer);
    }

    public static void registerServer(Context context, RegisterServer registerServer){
        requestOnline(context,registerServer);
    }

    public interface RegisterServer{
        void requestSuccess();
        void requestError(String errorMessage);
    }

    public static void requestOnline(Context context, RegisterServer registerServer) {
        if (UtilConstants.isNetworkAvailable(context)) {
            new AsynTaskRegistration(registerServer,context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            if(registerServer!=null){
                registerServer.requestError(context.getString(R.string.network_error_message));
            }
        }
    }

    public static class AsynTaskRegistration extends AsyncTask<Void, Void, Boolean> {
        private DataServiceException dataServiceException = null;
        RegisterServer registerServer = null;
        Context context = null;
        private HttpException httpException = null;
        public AsynTaskRegistration(RegisterServer registerServer,Context context) {
            this.registerServer= registerServer;
            this.context= context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return onDeviceReg();
            } catch (Throwable e) {
                e.printStackTrace();
                if(registerServer!=null){
                    registerServer.requestError("Initial Registration :\n " +
                            "log -Registration failed with error \n"+e.toString());
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (dataServiceException == null&&result) {
                if(registerServer!=null){
                    registerServer.requestSuccess();
                }
            }
            else if(httpException!=null){
                if(registerServer!=null){
                    registerServer.requestError(context.getString(R.string.network_error_message));
                }
            } else {
                try {
                    String err_msg = null;
                    if (dataServiceException != null) {
                        err_msg = com.arteriatech.mutils.common.UtilConstants.getErrorMsg(dataServiceException.getStatus(), context);
                        LogManager.writeLogError("Registration failed  due to "+err_msg);
                        if(registerServer!=null){
                            registerServer.requestError(err_msg);
                        }
                    } else {
                        if(registerServer!=null){
                            registerServer.requestError(context.getString(R.string.user_auth_error));
                        }
                    }
                } catch (Throwable var3) {
                    var3.printStackTrace();
                }
            }
        }
        public boolean onDeviceReg() {
            try {
                String userName = context.getSharedPreferences(Constants.PREFS_NAME,0).getString(UtilRegistrationActivity.KEY_username, "");
                String password = context.getSharedPreferences(Constants.PREFS_NAME,0).getString(UtilRegistrationActivity.KEY_password, "");
                dataServiceException = null;
                String requestUri = getAppURL(server_Text, port_Text, APP_ID, IS_HTTPS);
                final UsernamePasswordToken token = new UsernamePasswordToken(userName.toString().trim(), password.toString().trim());
                CommonAuthFlowsConfigurator commonAuthFlowsConfigurator = new CommonAuthFlowsConfigurator(context);
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
                HttpConversationManager conversationManager = commonAuthFlowsConfigurator.configure(new HttpConversationManager(context));

                SDKHttpHandler sdkHttpHandler = new SDKHttpHandler(context, conversationManager);
                AndroidSystem.setContext(context);
                OnlineODataProvider registrationProvider = new OnlineODataProvider("SmpClient", requestUri);
                registrationProvider.getNetworkOptions().setHttpHandler(sdkHttpHandler);
                registrationProvider.getServiceOptions().setCheckVersion(false);
                SmpClient smpClient = new SmpClient(registrationProvider);
                Connection newConnection = new Connection();
                newConnection.setDeviceType("Android");
                newConnection.setDeviceSubType(Build.VERSION.RELEASE);
                newConnection.setApplicationVersion ("1");
                newConnection.setCustomCustom1 ("1");
                String brandModel = Build.BRAND+" "+Build.MODEL;
                newConnection.setDeviceModel (brandModel);
                newConnection.setDefaultValues();
                if (com.arteriatech.mutils.common.UtilConstants.isNetworkAvailable(context)) {
                    try {
                        try {
                            LogManager.writeLogError("Initial Registration :\n " +
                                    "log -Registration called with create connection");
                            smpClient.createEntity(newConnection);
                            onSaveConfig(newConnection,smpClient);
                            LogManager.writeLogError("Initial Registration :\n " +
                                    "log -Registration create connection successful");
                            return true;
                        } catch (DataServiceException var10) {
                            LogManager.writeLogError("Registration failed due " + var10.getMessage());
                            var10.printStackTrace();
                            this.dataServiceException = var10;
                        }
                    } catch (Throwable e) {
                        if(registerServer!=null){
                            registerServer.requestError("Registration failed due "+e.getMessage());
                        }
                        LogManager.writeLogError("Registration failed due "+e.getMessage());
                        e.printStackTrace();
                    }

                    return false;
                } else {
                    if(registerServer!=null){
                        registerServer.requestError(context.getString(R.string.network_error_message));
                    }
                    LogManager.writeLogError(context.getString(R.string.network_error_message));
                    return false;
                }

            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }
        private String getAppURL(String serverText, String port, String appID, boolean isHttps) {
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
        private void onSaveConfig( Connection newConnection, SmpClient smpClient) {
            SharedPreferences.Editor editor =context.getSharedPreferences(Constants.PREFS_NAME,0).edit();
            String userName = context.getSharedPreferences(Constants.PREFS_NAME,0).getString(UtilRegistrationActivity.KEY_username, "");
            String password = context.getSharedPreferences(Constants.PREFS_NAME,0).getString(UtilRegistrationActivity.KEY_password, "");
            editor.putString(RegistrationActivity.KEY_username, userName.toString().trim());
            editor.putString(RegistrationActivity.KEY_password, password.toString().trim());
            editor.putString(RegistrationActivity.KEY_serverHost, server_Text);
            editor.putString(RegistrationActivity.KEY_serverPort, port_Text);
            editor.putString(RegistrationActivity.KEY_securityConfig, secConfig_Text);
            editor.putString(RegistrationActivity.KEY_appID, APP_ID);
            editor.putString(RegistrationActivity.KEY_FARMID, farm_ID);
            editor.putString(RegistrationActivity.KEY_SUFFIX, suffix);
            editor.putBoolean(RegistrationActivity.KEY_ISHTTPS, IS_HTTPS);
            editor.putString(RegistrationActivity.KEY_appConnID, newConnection.getApplicationConnectionId());
            editor.putString(RegistrationActivity.KEY_appEndPoint, "");
            editor.putString(RegistrationActivity.KEY_pushEndPoint, "");
            editor.putString(RegistrationActivity.KEY_SalesPersonName, "");
            editor.putString(RegistrationActivity.KEY_SalesPersonMobileNo, "");
            editor.putBoolean(RegistrationActivity.KEY_isPasswordSaved, true);
            editor.putBoolean(RegistrationActivity.KEY_isDeviceRegistered, true);
            editor.putBoolean(RegistrationActivity.KEY_isFirstTimeReg, true);
            editor.putBoolean(RegistrationActivity.KEY_isForgotPwdActivated, false);
            editor.putBoolean(RegistrationActivity.KEY_isManadtoryUpdate, true);
            editor.putBoolean(RegistrationActivity.KEY_isUserIsLocked, false);
            editor.putString(RegistrationActivity.KEY_ForgotPwdOTP, "");
            editor.putString(RegistrationActivity.KEY_ForgotPwdGUID, "");
            editor.putString(RegistrationActivity.KEY_isFOSUserRole, "");
            editor.putInt(RegistrationActivity.KEY_MaximumAttemptKey, 0);
            editor.putInt(RegistrationActivity.KEY_VisitSeqId, 0);
            editor.putString(RegistrationActivity.KEY_BirthDayAlertsDate, UtilConstants.getDate1());
            editor.apply();
        }
    }

    public static void clearCookies() {
        try {
            com.sap.smp.client.httpc.SAPCookieManager.getInstance().getCookieStore().removeAll();
            LogManager.writeLogError("Cookies cleared");
        } catch (Throwable e) {
            LogManager.writeLogError("Clear Cookies Error : "+e.getMessage());
            e.printStackTrace();
        }
    }

    public static void openStoreWithAvailableServer(Context context , UIListener uiListener){
//        clearCookies();
        new OpenOfflineStore(context,uiListener).execute();
    }

    public static class OpenOfflineStore extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        private UIListener uiListener =  null;
        private Context context =  null;
        public OpenOfflineStore(Context context, UIListener uiListener) {
            this.context=context;
            this.uiListener=uiListener;
        }

        @Override
        protected Void doInBackground(Void... params) {
            closeStore(context);
//            if (OfflineManager.offlineStore != null) {
//                try {
//                    OfflineManager.offlineStore.closeStore();
//                    try {
//                        Toast.makeText(context,"Store closed",Toast.LENGTH_SHORT).show();
//                    } catch (Throwable e) {
//                        e.printStackTrace();
//                    }
//                } catch (ODataException e) {
//                    e.printStackTrace();
//                }
//            }
            OfflineManager.offlineStore = null;
            OfflineManager.options = null;
            try {
//                if (OfflineManager.offlineStore != null) {
//                    if (!isOfflineStoreOpen()) {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
//                                    Toast.makeText(context,"Opening store",Toast.LENGTH_SHORT).show();
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            LogManager.writeLogError("Available server initializing Store");

//                            OfflineManager.openOfflineStore(RegistrationActivity.this, RegistrationActivity.this);
                            OfflineManager.openOfflineStoreInternal(context, uiListener);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            LogManager.writeLogError(Constants.error_txt + e.getMessage());
                        }
//                    }
//                } else {
//                    ((Activity)context).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Toast.makeText(context,"Opening store",Toast.LENGTH_SHORT).show();
//                            } catch (Throwable e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                    try {
//                        LogManager.writeLogError("Available server initializing Store");
//                        OfflineManager.openOfflineStore(RegistrationActivity.this, RegistrationActivity.this);
//                        OfflineManager.openOfflineStoreInternal(context, uiListener);
//                    } catch (Throwable e) {
//                        e.printStackTrace();
//                        LogManager.writeLogError(Constants.error_txt + e.getMessage());
//                    }
//                }
            }catch (Throwable e) {
                e.printStackTrace();
                LogManager.writeLogError(Constants.error_txt + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
        public static void closeStore(Context context) {
            try {
                try {
//                    UtilOfflineManager.setStoreState(ODataOfflineStoreState.ODataOfflineStoreClosed.name());
                    if(OfflineManager.offlineStore!=null) {
                        OfflineManager.offlineStore.closeStore();
                    }
                } catch (Exception e) {
                    LogManager.writeLogError(Constants.offline_store_not_closed + e.getMessage());
                    throw new OfflineODataStoreException(e);
                }
                LogManager.writeLogInfo(context.getString(R.string.store_close));
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
                LogManager.writeLogError(context.getString(R.string.error_during_offline_close) + e.getMessage());
            }
        }
    }

    public static void requestServiceDocument(Context context, String appMetaURL, IResponseListener iResponseListener) {
        HttpConversationManager fetch = new CommonAuthFlowsConfigurator(context).supportBasicAuthUsing(CredentialsProvider.getInstance(context)).configure(new HttpConversationManager(context));
        IHttpConversation conv = null;
        try {
            conv = fetch.create(new URL(appMetaURL));
            SharedPreferences sharedPref = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            conv.addHeader("X-SMP-APPCID", sharedPref.getString(com.arteriatech.mutils.registration.UtilRegistrationActivity.KEY_appConnID, ""));
            conv.addHeader("Authorization", MyUtils.getBasicAuthCredential(context));
            conv.setResponseListener(iResponseListener);
            conv.start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}

package com.arteriatech.ss.msec.iscan.v4;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.MyUtils;
import com.arteriatech.ss.msec.iscan.v4.registration.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import static com.arteriatech.ss.msec.iscan.v4.common.Constants.httpErrorCodes;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.USER_NAME_DEVICE_ID;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.USER_PASSWORD_DEVICE_ID;

public class DeviceIDUtils<T> {
    //final constants
    public static final int FETCH_BASIC_INFO = 100;
    public static final int FETCH_CSRF = 101;
    public static final int POST_REQUEST = 102;
    public static final int UNAUTH_CODE = 103;

    private Context context;
    private static List<String> setCookies;
    private static String userName="";
    private static String password="";
    private static String APP_ID="";
    private static int retryCount=3;
    private static String APP_DEVICEID;
    private static OnDeviceIDListener listener;


    public DeviceIDUtils(Context context) {


    }

    public static class DeviceIDBuilder<T>{
        private Context context;
        private static String userName="";
        private static String password="";
        private static String APP_ID="";
        private static int retryCount=3;
        private static OnDeviceIDListener listener;
        private static String APP_DEVICEID;
    }


    public static void validateDeviceID(String APP_DEVICEID, String APP_ID, @NonNull final String userName, @NonNull final String password, String fetchURL, Context context, OnDeviceIDListener listener){
        new Thread(() -> {
            setCookies= new ArrayList<>();
            DeviceIDUtils.userName = userName;
            DeviceIDUtils.password = password;
            DeviceIDUtils.APP_ID = APP_ID;
            DeviceIDUtils.APP_DEVICEID = APP_DEVICEID;
            DeviceIDUtils.listener=listener;
            HttpsURLConnection connection = null;
            int responseCode = 0;
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
                responseCode = connection.getResponseCode();
                connection.getResponseMessage();
                fetchGetResponse(responseCode,connection,context);
            } catch (Throwable e) {
                LogManager.writeLogError("Device ID validation failed with error :"+e.toString());
                if (e.getMessage().contains("timeout")) {
                    listener.onResult(true, "DeviceID validation cannot be performed due to connection timeout[UserProfileAuthSet]",FETCH_BASIC_INFO);
                } else {
                    listener.onResult(true, "DeviceID validation cannot be performed due to poor internet connectivity [UserProfileAuthSet].\n\n Please check internet and retry",FETCH_BASIC_INFO);
                }
//                listener.onResult(true, "DeviceID validation cannot be performed. Please check your internet connectivity",FETCH_BASIC_INFO);
            }
        }).start();
    }

    private static void fetchGetResponse(int responseCode,@NonNull HttpsURLConnection connection,Context context){
        try {
            String resultJson = "";
            InputStream stream = null;
            if (responseCode == 200) {
                listener.onResult(false, "DeviceID details fetched successfully",FETCH_BASIC_INFO);
                LogManager.writeLogInfo("Device ID validation success with 200 code");
                stream = connection.getInputStream();
                if (stream != null) {
                    resultJson = readResponse(stream);
                }
                try {
                    JSONObject jsonObj = new JSONObject(resultJson);
                    JSONObject objectD = jsonObj.optJSONObject("d");
                    String value = "";
                    if (objectD!=null) {
                        JSONArray jsonArray = objectD.getJSONArray("results");
                        if(jsonArray.length()>0) {
                            JSONObject object = (JSONObject) jsonArray.get(0);
                            String applicationID = object.optString("Application");
                            String LoginID = object.optString("LoginID");
                            String ERPLoginID = object.optString("ERPSystemID");
                            String AuthOrgValue = "";
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                if (object1.optString("AuthOrgTypeID") != null && object1.optString("AuthOrgTypeID").equalsIgnoreCase("000038")) {
                                    AuthOrgValue = object1.optString("AuthOrgValue");
                                }
                            }
                            if (TextUtils.isEmpty(AuthOrgValue)){
                                LogManager.writeLogInfo("Device-ID not mapped, attempting to create new device ID");
                                postUserAuthSet(APP_DEVICEID,applicationID,LoginID,ERPLoginID);
                            }else if(!TextUtils.isEmpty(AuthOrgValue)&&!TextUtils.equals(AuthOrgValue,APP_DEVICEID)){
                                LogManager.writeLogInfo("Device-ID mapped with other device need to overwrite with this device");
                                listener.onResult(true, "Another device id mapped with '"+userName+"'. Please map this device id and try again. Contact support team for help",FETCH_BASIC_INFO);
                            }else if(TextUtils.equals(AuthOrgValue,APP_DEVICEID)){
                                LogManager.writeLogInfo("Device-ID mapped with same device");
                                listener.onResult(false, "Device-ID mapped with same device",POST_REQUEST);
                            }
                        }else {
                            LogManager.writeLogInfo("User management not done (MSEC). Please contact support team");
                            listener.onResult(true, "User management not done (MSEC). Please contact support team",POST_REQUEST);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogManager.writeLogError("Device ID validation failed with error :"+e.toString());
                    listener.onResult(true, "DeviceID validation cannot be performed. Please check your internet connectivity",FETCH_BASIC_INFO);
                }
            }else if(responseCode==401){
                LogManager.writeLogInfo("Device ID validation failed error code : "+responseCode);
                try {
                    com.arteriatech.mutils.common.UtilConstants.getPasswordStatus(Configuration.IDPURL, USER_NAME_DEVICE_ID, USER_PASSWORD_DEVICE_ID, Configuration.APP_ID, jsonObject -> {
                        ((Activity) context).runOnUiThread(() -> {
                            listener.onResult(true,"" ,UNAUTH_CODE);
                            com.arteriatech.mutils.common.UtilConstants.passwordStatusErrorMessage(
                                    context,
                                    jsonObject,
                                    USER_NAME_DEVICE_ID, Configuration.IDPURL, Configuration.IDPTUSRNAME, Configuration.IDPTUSRPWD, Configuration.APP_ID);
                        });
                    });
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }else {
                try {

                    String err_msg = connection.getResponseMessage();
                    try {
                        Constants.httphashmaperrorcodes();
                        err_msg = httpErrorCodes.get(""+responseCode);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        LogManager.writeLogError(" Device ID httpErrorCodes error : "+e.getMessage());
                    }

                    listener.onResult(true, "DeviceID validation cannot be performed due to "+err_msg+"["+responseCode+"]",FETCH_BASIC_INFO);
                    LogManager.writeLogError("DeviceID validation cannot be performed due to "+err_msg+"["+responseCode+"]");
                } catch (Throwable e) {
                    if(e instanceof FileNotFoundException){
                        listener.onResult(true, "DeviceID validation cannot be performed. Please check your internet connectivity",FETCH_BASIC_INFO);
                    }
                    LogManager.writeLogError("Device ID validation failed with response code :"+responseCode +" with error \n "+connection.getInputStream().toString());
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void postUserAuthSet(String deviceID,String applicationID, String LoginID, String ERPID) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Application", applicationID);
            jsonObject.put("ErpSystemID", ERPID);
            jsonObject.put("LoginID", "");
            jsonObject.put("AuthOrgTypeID", "000038");
            jsonObject.put("AuthOrgValue", deviceID);
            jsonObject.put("AuthOrgValDsc", userName);
            String URL= MyUtils.getDefaultOnlineQryURL()+"DeviceIDSet";
            getCSRFToken(new URL(URL),userName,password,APP_ID,jsonObject.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }



    private static void getCSRFToken(final URL url, final String userName, final String psw,String APP_ID,String body) throws IOException {
        String result = "";
        HttpsURLConnection connection = null;
        try {
            LogManager.writeLogInfo("fetching csft-token");
            connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(30000);
            connection.setConnectTimeout(30000);
            String userCredentials = userName + ":" + psw;
            String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8), 2);
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestProperty("x-smp-appid", APP_ID);
            connection.setRequestProperty("x-csrf-token", "fetch");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            connection.getResponseMessage();
            InputStream stream = null;

            if (responseCode != 200&&responseCode!=501) {
                if (retryCount==0){
                    LogManager.writeLogError("fetching CSRF Token failed re-attempt completed with error \n"+readResponse(connection.getErrorStream()));
                    listener.onResult(true, "Device-ID not mapped. Please retry after mapping..!",FETCH_CSRF);
                }else{
                    LogManager.writeLogError("fetching CSRF Token failed re-attempt scheduled with error \n"+readResponse(connection.getErrorStream()));
                    retryCount--;
                    getCSRFToken(url,userName,psw,APP_ID,body);
                }
            } else if(responseCode==501){
                LogManager.writeLogError("fetching CSRF Token fetched successfully");
                String CSRF_TOKEN = connection.getHeaderField("x-csrf-token");
                if (connection.getHeaderFields().get("Set-Cookie")!=null) {
                    setCookies.addAll(Objects.requireNonNull(connection.getHeaderFields().get("Set-Cookie")));
                }
                listener.onResult(false, "csrf token fetched successfully",FETCH_CSRF);
                LogManager.writeLogError("posting DeviceIDto server");
                postAuth(url,userName,psw,CSRF_TOKEN,body);
            }else if(responseCode==200){
                LogManager.writeLogError("fetching CSRF Token fetched successfully");
                String CSRF_TOKEN = connection.getHeaderField("x-csrf-token");
                if (connection.getHeaderFields().get("Set-Cookie")!=null) {
                    setCookies.addAll(Objects.requireNonNull(connection.getHeaderFields().get("Set-Cookie")));
                }
                listener.onResult(false, "csrf token fetched successfully",FETCH_CSRF);
                LogManager.writeLogError("posting DeviceIDto server");
                postAuth(url,userName,psw,CSRF_TOKEN,body);
            }

        } catch (Exception var12) {
            LogManager.writeLogError("error while fetching csrf token :"+var12.toString());
            listener.onResult(true, "DeviceID validation cannot be performed. Please check your internet connectivity",FETCH_BASIC_INFO);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static void postAuth(final URL url, final String userName, final String psw, final String csrfToken, final String body) {
        String result = null;
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(30000);
            connection.setConnectTimeout(30000);
            String userCredentials = userName + ":" + psw;
            String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8), 2);
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestProperty("x-smp-appid", APP_ID);
            connection.setRequestProperty("x-csrf-token", csrfToken);
            for (int i = 0; i < setCookies.size(); i++) {
                connection.addRequestProperty("Cookie", setCookies.get(i));
            }
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(body.getBytes(StandardCharsets.UTF_8));
            os.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == 201||responseCode == 200) {
                LogManager.writeLogError("Device ID posted successfully");
                listener.onResult(false, "Device-ID posted successfully",POST_REQUEST);
            }else {
                try {
                    InputStream stream = connection.getErrorStream();
                    String response = readResponse(stream);
                    JSONObject jsonObject = new JSONObject(response);
                    String errorCode = jsonObject.getJSONObject("error").getString("code");
                    String errorMessage = jsonObject.getJSONObject("error").getJSONObject("message").getString("value");
                    LogManager.writeLogInfo(errorMessage);
                    listener.onResult(true, "Unable to map Device ID. Please try again",POST_REQUEST);
                } catch (Throwable e) {
                    LogManager.writeLogInfo(e.toString());
                    listener.onResult(true, "Unable to map Device ID. Please try again",POST_REQUEST);
                }
            }
        } catch (Exception var14) {
            LogManager.writeLogInfo(var14.toString());
            listener.onResult(true, "Unable to map Device ID. Please try again",POST_REQUEST);
        } finally {
           if (connection!=null)
               connection.disconnect();
        }
    }
    public static String readResponse(InputStream stream) throws IOException {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder buffer = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line).append('\n');
        }

        return buffer.toString();
    }

    public interface OnDeviceIDListener{
        void onResult(boolean isError, String message,int code);
    }

}

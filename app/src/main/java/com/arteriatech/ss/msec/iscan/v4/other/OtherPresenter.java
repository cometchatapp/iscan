package com.arteriatech.ss.msec.iscan.v4.other;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Toast;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.MSFAApplication;
import com.arteriatech.ss.msec.iscan.v4.registration.Configuration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class OtherPresenter implements OtherPresenterView {
    private Activity context=null;
    private ProgressDialog progressDialog;
    private CourseListener courseListener;

    public OtherPresenter(Activity context, CourseListener courseListener) {
        this.context=context;
        this.courseListener =courseListener;
    }

    @Override
    public void onStart() {

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
                String timeStamp = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm", new Date());
                showProgress(messageProgess + "\t\t" + countdown);
            }

            @Override
            public void onFinish() {

            }

        };
        countDownTimer.start();
    }

    public void cancelTimer() {
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
    //--------------------------------------------------------------------------quodeck--------------------------------------------------------------------------
    private HashMap<String,String> headerMapListAuth;
    public void quodeckSignIn(String UID,String firstName,String lastName,String regionName,String AWcode){
        setTimer();
        messageProgess ="Authenticating Quodeck, Please wait..";
        String SignInUrl = "https://britannia.quodeck.com/api/v2/auth/sign_in";
        headerMapListAuth= new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", Configuration.quodecAdminUserName);
            jsonObject.put("password", Configuration.quodecAdminPassword);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                HttpsURLConnection connection = null;
                try {
                    URL url =new URL(SignInUrl);
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setReadTimeout(30000);
                    connection.setConnectTimeout(30000);
                    connection.addRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    OutputStream os = connection.getOutputStream();
                    os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                    os.close();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 201||responseCode == 200) {
                        headerMapListAuth.put("client",connection.getHeaderField("client"));
                        headerMapListAuth.put("access-token",connection.getHeaderField("access-token"));
                        headerMapListAuth.put("expiry",connection.getHeaderField("expiry"));
                        headerMapListAuth.put("expires",connection.getHeaderField("expires"));
                        headerMapListAuth.put("uid",connection.getHeaderField("uid"));
                        headerMapListAuth.put("token-type",connection.getHeaderField("token-type"));
                        InputStream stream = connection.getInputStream();
                        String response = readResponse(stream);
                        JSONObject jsonObject1 = new JSONObject(response);
                        String id = jsonObject1.getString("_id");
                        createEnroll(UID,firstName,lastName,regionName,AWcode);
                        LogManager.writeLogInfo("SignIn API Successfull");
                    }else if(responseCode==422){
                        cancelTimer();
                        hideProgress();
                        SharedPreferences.Editor editor =context.getSharedPreferences(Constants.PREFS_NAME,0).edit();
                        editor.putBoolean(Constants.QUODECK_ENROLL, true);
                        editor.apply();
                        Toast.makeText(context, MSFAApplication.SP_UID+" Already enrolled", Toast.LENGTH_SHORT).show();
                        LogManager.writeLogInfo(MSFAApplication.SP_UID+" Already enrolled");
                    }else {
                        cancelTimer();
                        hideProgress();
                        try {
                            InputStream stream = connection.getErrorStream();
                            String response = readResponse(stream);
                            LogManager.writeLogInfo(response);
                        } catch (Throwable e) {
                            LogManager.writeLogInfo(e.toString());
                        }
                    }
                } catch (Exception var14) {
                    hideProgress();
                    LogManager.writeLogInfo(var14.toString());
                } finally {
                    if (connection!=null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    public void createEnroll(String UID,String firstName,String lastName,String regionName,String AWcode){
        boolean is422 = context.getSharedPreferences(Constants.PREFS_NAME,0).getBoolean(Constants.QUODECK_ENROLL,false);
        if (!is422) {
            messageProgess= "Enrolling courses, Please wait..";
            String enrollURL = "https://britannia.quodeck.com/api/v2/users/createnroll";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", UID);
                jsonObject.put("password", "password");
                jsonObject.put("first_name", firstName);
                if (lastName!=null&!TextUtils.isEmpty(lastName)) {
                    jsonObject.put("last_name", lastName);
                }else{
                    jsonObject.put("last_name", UID);
                }
                jsonObject.put("email", UID+"@gmail.com");
                JSONObject detalsObject = new JSONObject();
                detalsObject.put("company","Britannia");
                detalsObject.put("Regionname",regionName);
                detalsObject.put("AWcode",AWcode);
                jsonObject.put("details", detalsObject);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = null;
                    HttpsURLConnection connection = null;
                    try {
                        URL url =new URL(enrollURL);
                        connection = (HttpsURLConnection) url.openConnection();
                        connection.setReadTimeout(30000);
                        connection.setConnectTimeout(30000);
                        connection.addRequestProperty("Content-Type", "application/json");
                        connection.setRequestProperty("Accept", "application/json");

                        connection.addRequestProperty("client", headerMapListAuth.get("client"));
                        connection.addRequestProperty("access-token", headerMapListAuth.get("access-token"));
                        connection.addRequestProperty("expiry", headerMapListAuth.get("expiry"));
                        connection.addRequestProperty("expires", headerMapListAuth.get("expires"));
                        connection.addRequestProperty("uid", headerMapListAuth.get("uid"));
                        connection.addRequestProperty("token-type", headerMapListAuth.get("token-type"));
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        OutputStream os = connection.getOutputStream();
                        os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                        os.close();
                        int responseCode = connection.getResponseCode();
                        if (responseCode == 201||responseCode == 200) {
                            try {
                                InputStream stream = connection.getInputStream();
                                String response = readResponse(stream);
                                JSONObject jsonObject1 = new JSONObject(response);
                            /*JSONArray jsonArray = jsonObject.getJSONArray("courses");
                            String courses = jsonArray.toString();*/
                                SharedPreferences.Editor editor =context.getSharedPreferences(Constants.PREFS_NAME,0).edit();
                                editor.putBoolean(Constants.QUODECK_ENROLL, true);
                                if (jsonObject1.has("_id")) {
                                    editor.putString(Constants.QUODECK_ENROLL_ID, jsonObject1.getString("_id"));
                                }
                            /*if (courses!=null&&!TextUtils.isEmpty(courses)) {
                                editor.putString(Constants.QUODECK_ENROLL_COURSES, courses);
                            }*/
                                editor.apply();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            messageProgess= "Fetching courses, Please wait..";
                            getUserDetails(UID,"password");
                            LogManager.writeLogInfo(MSFAApplication.SP_UID+" enrolled");
                        }else if(responseCode==422){
                            messageProgess= "Fetching courses, Please wait..";
                            getUserDetails(UID,"password");
                            SharedPreferences.Editor editor =context.getSharedPreferences(Constants.PREFS_NAME,0).edit();
                            editor.putBoolean(Constants.QUODECK_ENROLL, true);
                            editor.apply();
                            LogManager.writeLogInfo(MSFAApplication.SP_UID+" Already enrolled");
                        }else {
                            try {
                                InputStream stream = connection.getErrorStream();
                                String response = readResponse(stream);
                                LogManager.writeLogInfo(response);
                            } catch (Throwable e) {
                                LogManager.writeLogInfo(e.toString());
                            }
                        }
                    } catch (Exception var14) {
                        LogManager.writeLogInfo(var14.toString());
                    } finally {
                        if (connection!=null)
                            connection.disconnect();
                    }
                }
            }).start();
        }else{
            messageProgess= "Fetching courses, Please wait..";
            getUserDetails(UID,"password");
        }
    }

    /**
     * User Details can be fetched from Sign In API. ID, Course details can be fetched from below
     *
     * Coursed which are enrolled not completed will be listed. Completed Courses need to call another API (Ref: )
     * method post
     */

    private HashMap<String,String>headerMapList;
    public void getUserDetails(String Username,String password){
        String SignInUrl = "https://britannia.quodeck.com/api/v2/auth/sign_in";
        headerMapList= new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", Username);
            jsonObject.put("password", password);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                HttpsURLConnection connection = null;
                try {
                    URL url =new URL(SignInUrl);
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setReadTimeout(30000);
                    connection.setConnectTimeout(30000);
                    connection.addRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    OutputStream os = connection.getOutputStream();
                    os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                    os.close();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 201||responseCode == 200) {
                        headerMapList.put("client",connection.getHeaderField("client"));
                        headerMapList.put("access-token",connection.getHeaderField("access-token"));
                        headerMapList.put("expiry",connection.getHeaderField("expiry"));
                        headerMapList.put("expires",connection.getHeaderField("expires"));
                        headerMapList.put("uid",connection.getHeaderField("uid"));
                        headerMapList.put("token-type",connection.getHeaderField("token-type"));
                        InputStream stream = connection.getInputStream();
                        String response = readResponse(stream);
                        JSONObject jsonObject1 = new JSONObject(response);
                        String id = jsonObject1.getString("_id");
                        getCoursess(id,headerMapList);
                        LogManager.writeLogInfo("SignIn API Successfull");
                    }else if(responseCode==422){
                        cancelTimer();
                        hideProgress();
                        SharedPreferences.Editor editor =context.getSharedPreferences(Constants.PREFS_NAME,0).edit();
                        editor.putBoolean(Constants.QUODECK_ENROLL, true);
                        editor.apply();
                        Toast.makeText(context, MSFAApplication.SP_UID+" Already enrolled", Toast.LENGTH_SHORT).show();
                        LogManager.writeLogInfo(MSFAApplication.SP_UID+" Already enrolled");
                    }else {
                        cancelTimer();
                        hideProgress();
                        try {
                            InputStream stream = connection.getErrorStream();
                            String response = readResponse(stream);
                            LogManager.writeLogInfo(response);
                        } catch (Throwable e) {
                            LogManager.writeLogInfo(e.toString());
                        }
                    }
                } catch (Exception var14) {
                    hideProgress();
                    LogManager.writeLogInfo(var14.toString());
                } finally {
                    if (connection!=null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    public void getCoursess(String id,HashMap<String,String>headerList){
        String courseURL = "https://britannia.quodeck.com/api/v2/analytics/"+id+"/courses/all";
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                HttpsURLConnection connection = null;
                try {
                    URL url =new URL(courseURL);
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setReadTimeout(30000);
                    connection.setConnectTimeout(30000);
                    connection.addRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");

                    connection.addRequestProperty("client", headerList.get("client"));
                    connection.addRequestProperty("access-token", headerList.get("access-token"));
                    connection.addRequestProperty("expiry", headerList.get("expiry"));
                    connection.addRequestProperty("expires", headerList.get("expires"));
                    connection.addRequestProperty("uid", headerList.get("uid"));
                    connection.addRequestProperty("token-type", headerList.get("token-type"));

                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    connection.setDefaultUseCaches(false);
                    connection.setAllowUserInteraction(false);
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 201||responseCode == 200) {
                        InputStream stream = connection.getInputStream();
                        String response = readResponse(stream);
                        JSONArray jsonArray = new JSONArray(response);
                        try {
                            SharedPreferences.Editor editor =context.getSharedPreferences(Constants.PREFS_NAME,0).edit();
                            editor.putString(Constants.QUODECK_COURSES, jsonArray.toString());
                            editor.apply();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        if (courseListener!=null){
                            courseListener.onResponse(false,jsonArray);
                        }
                        cancelTimer();
                        LogManager.writeLogInfo("Courses API fetched Successfully");
                    }else if(responseCode==422){
                        cancelTimer();
                        if (courseListener!=null){
                            courseListener.onResponse(true,null);
                        }
                        SharedPreferences.Editor editor =context.getSharedPreferences(Constants.PREFS_NAME,0).edit();
                        editor.putBoolean(Constants.QUODECK_ENROLL, true);
                        editor.apply();
                        Toast.makeText(context, MSFAApplication.SP_UID+" Already enrolled", Toast.LENGTH_SHORT).show();
                        LogManager.writeLogInfo(MSFAApplication.SP_UID+" Already enrolled");
                    }else {
                        if (courseListener!=null){
                            courseListener.onResponse(true,null);
                        }
                        try {
                            InputStream stream = connection.getErrorStream();
                            String response = readResponse(stream);
                            LogManager.writeLogInfo(response);
                        } catch (Throwable e) {
                            LogManager.writeLogInfo(e.toString());
                        }
                    }
                } catch (Exception var14) {
                    LogManager.writeLogInfo(var14.toString());
                } finally {
                    if (connection!=null)
                        connection.disconnect();
                }
            }
        }).start();
    }
    public void getCompletedCourses(){

    }
    public void getPendingCourses(){

    }

    public interface CourseListener{
        void onResponse(boolean isError,JSONArray jsonArray);
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
    public void hideProgress() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }
    public void showProgress(String message) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog!=null&&progressDialog.isShowing()){
                    String timeStamp = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm aa", new Date());
                    progressDialog.setTitle(timeStamp);
                    progressDialog.setMessage(message);
                }if(progressDialog==null){
                    progressDialog = new ProgressDialog(context);
                    String timeStamp = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm aa", new Date());
                    progressDialog.setTitle(timeStamp);
                    progressDialog.setMessage(message);
                    progressDialog.show();
                }
            }
        });

    }
}

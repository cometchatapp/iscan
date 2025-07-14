package com.arteriatech.ss.msec.iscan.v4.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.mutils.securityassement.SecurityAssementClass;
import com.arteriatech.mutils.securityassement.SecurityAssementInterface;

import com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity;
//import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.sybase.persistence.PrivateDataVault;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by e10769 on 12-Apr-18.
 */

public class MSFAApplication extends MultiDexApplication implements LifecycleObserver {
    public static volatile boolean isAWSM,isCallCenter,isRPD,isPSM,isSDA,isVAN,isBCRVAN, isBVAN;
    public static String DISTRIBUTOR_NAME = "";
    public static boolean jarAnimation = false;
    public static boolean awsmBanner = false;
    public static String DISTRIBUTOR_NO = "";
    public static String SALES_PERSON_NAME = "";
    public static String SLIPT_BEAT_FLAG = "";
    public static String BEAT_NAME = "";
    public static String DISTRIBUTOR_GUID = "";
    public static String STOCK_OWNER = "";
    public static String SPGUID = "";
    public static String SDA_FROM_CPGUID = "";
    public static String SDA_FROM_CPTYPE = "";
    public static String SDA_FROM_CPNO= "";
    public static String SDA_FROM_CPNAME= "";
    public static String PARNET_TYPE_ID = "";
    public static String CPNO = "";
    public static String APP_DEVICEID = "";
    public static String AW_REVERIFICATION = "";
    public static String AW_TIME = "";
    public static String AW_GEO_FENCING = "";
    public static String AW_CPID = "";
    public static String AW_Outlet_Lock = "";
    public static String Validate_outlet_reVerf = "";
    public static String STKEXPUPTO = "";
    public static String SP_UID = "";
    public static String VehicleNo = "";
    public static MSFAApplication mContext;
    public MediaPlayer mPlayer;
    private static final String expectedPackageName = "com.arteriatech.ss.msec.bil.v4";
    private static final String[] expectedSigningCertificateHashBase64 = new String[]{
            "mVr/qQLO8DKTwqlL+B1qigl9NoBnbiUs8b4c2Ewcz0k=",
            "cVr/qQLO8DKTwqlL+B1qigl9NoBnbiUs8b4c2Ewcz0m="
    };
    // Replace with your release (!) signing certificate hashes
    private static final String watcherMail = "teamarteria@gmail.com"; // for Alerts and Reports
    private static final String[] supportedAlternativeStores = new String[]{
// Google Play Store and Huawei AppGallery are supported out of the box, you can pass empty array or null or add other stores like the Samsung's one:
            "com.sec.android.app.samsungapps" // Samsung Store
    };
    private static final boolean isProd = true;

    @Override
    public void onCreate() {
        super.onCreate();
      //  Fresco.initialize(this);
      //  FirebaseApp.initializeApp(this);

        // below code for SSL Pinning , root, Code Obfuscation

       // startSecurityAssments();

// below code for App level ScreenShot Control
        mContext = this;
        mPlayer = new MediaPlayer();
        MultiDex.install(this);
        setupActivityListener();

        //ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        PrivateDataVault.init(getApplicationContext());

        Constants.iSAutoSync=false;
        Constants.isBGSync=false;
        APP_DEVICEID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String rollID = ConstantsUtils.getAuthInformation(this);
        switch (rollID){
            case Constants.MD:
                isRPD=true;
               break;
            case Constants.MSP:
                isSDA =true;
                break;
            case Constants.DSR:
                isAWSM = true;
                break;
            case Constants.PSM:
                isPSM = true;
                break;
            case Constants.VAN:
                isVAN = true;
                break;
            case Constants.BCRVAN:
                isBCRVAN = true;
                break;
            case Constants.BVAN:
                isBVAN = true;
                break;
        }


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause() {
        SharedPreferences sharedPerf = getSharedPreferences(Constants.PREFS_NAME, 0);
        if (!TextUtils.isEmpty(getSharedPreferences(Constants.PREFS_NAME,0).getString(UtilRegistrationActivity.KEY_username, ""))) {
            //startAutoSync(getApplicationContext());
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {
        if(alarmPendingIntent!=null){
            alarmPendingIntent.cancel();
            Intent intent1 = new Intent(getApplicationContext(), AutoLogOffaService.class);
            stopService(intent1);
        }
    }

    public static MSFAApplication getContext() {
        return mContext;
    }
    public static void setAnalytics(Context context,String ID, String className, String screenName){
        try {
            Bundle bundleCA = new Bundle();
            bundleCA.putString(FirebaseAnalytics.Param.ITEM_ID, ID);
            bundleCA.putString(FirebaseAnalytics.Param.ITEM_NAME, "Add New Task");
            bundleCA.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
            bundleCA.putString(FirebaseAnalytics.Param.SCREEN_CLASS, className);
            bundleCA.putString("user_id", ID);
            FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
            mFirebaseAnalytics.setUserProperty("user_id",ID);
            String userName = context.getSharedPreferences(Constants.PREFS_NAME,0).getString(com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity.KEY_username, "");
            String p_user_id = context.getSharedPreferences(Constants.PREFS_NAME,0).getString("p_user_id", "");
            if (p_user_id != null && !TextUtils.isEmpty(p_user_id)) {
                mFirebaseAnalytics.setUserProperty("p_user_id", p_user_id);
            } else {
                if (userName != null && !TextUtils.isEmpty(userName)) {
                    mFirebaseAnalytics.setUserProperty("p_user_id", userName);
                }
            }


        } catch (Throwable e) {
            LogManager.writeLogError("Analytics Error : "+e.toString());
        }
    }



    public static void setGAFields(Context context) {

    }
    public static final List<Activity> activityList = new ArrayList<>();
    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//                    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
//                }
                activityList.add(activity);
            }
            @Override
            public void onActivityStarted(Activity activity) {
            }
            @Override
            public void onActivityResumed(Activity activity) {
//                if(alarmPendingIntent!=null){
//                    alarmPendingIntent.cancel();
//                    Intent intent1 = new Intent(activity, AutoLogOffaService.class);
//                    stopService(intent1);
//                }
            }
            @Override
            public void onActivityPaused(Activity activity) {
//                if(!activity.isFinishing()) {
//                    startAutoSync(activity.getApplicationContext());
//                }
            }
            @Override
            public void onActivityStopped(Activity activity) {
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
                try {
                    if(activityList.size()>0){
                        if(activityList.contains(activity)) {
                            activityList.remove(this);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
//                if(alarmPendingIntent!=null){
//                try {
//                    Intent intent = new Intent(activity, AutoLogOffAlarmReceiver.class);
////alarmManager.cancel(PendingIntent.getService(context, 100, intent, 0));
//                    PendingIntent pIntent = PendingIntent.getBroadcast(activity, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
//                    alarmManager.cancel(pIntent);
//                    pIntent.cancel();
//                    Intent intent1 = new Intent(activity, AutoLogOffaService.class);
//                    stopService(intent1);
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                }
////                }
            }
        });
    }
    public static void finishAllActivities() {
        Set set = new HashSet(activityList);
        activityList.clear();
        activityList.addAll(set);
        try {
            if(activityList.size()>0) {
//                for (int i = activityList.size() - 1;i<=0;i--) {
                for (int i = 0;i<activityList.size();i++) {
                    Activity activity = activityList.get(i);
                    if (!activity.isFinishing()) {
                        activity.finish();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        activityList.clear();
    }

    public static PendingIntent alarmPendingIntent;
    @SuppressLint("ScheduleExactAlarm")
    public static void startAutoSync(Context mContext) {
        try {
            Constants.isSync = false;
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.PREFS_NAME,0);
            String autoSyncTime = sharedPreferences.getString(Constants.AUTOLOGOFF,"");
            if(!TextUtils.isEmpty(autoSyncTime)) {
              //  Intent intent = new Intent(mContext.getApplicationContext(), AutoLogOffAlarmReceiver.class);
                // Create a PendingIntent to be triggered when the alarm goes off
//                alarmPendingIntent = PendingIntent.getBroadcast(mContext, AutoSyncDataAlarmReceiver.REQUEST_CODE,
//                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                 /*   alarmPendingIntent = PendingIntent.getBroadcast
                            (mContext, AutoLogOffAlarmReceiver.REQUEST_CODE,
                                    intent, PendingIntent.FLAG_IMMUTABLE);*/
                } else {
                   /* alarmPendingIntent = PendingIntent.getBroadcast(mContext, AutoLogOffAlarmReceiver.REQUEST_CODE,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);*/
                }
                // Setup periodic alarm every 5 seconds
                long firstMillis = System.currentTimeMillis(); // first run of alarm is immediate
                int intervalMillis = 1000 * 60 * Integer.parseInt(autoSyncTime);
                // as of API 19, alarm manager will be forced up to 60000 to save battery
                AlarmManager alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
// Set alarm 10 seconds from now
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR, 0);
                calendar.add(Calendar.MINUTE, Integer.parseInt(autoSyncTime));
                calendar.add(Calendar.SECOND, 0);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(System.currentTimeMillis());

//                alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
//                        calendar.getTimeInMillis(), alarmPendingIntent);
                // See https://developer.android.com/training/scheduling/alarms.html
//                alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, alarmPendingIntent);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    alarm.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), alarmPendingIntent);
//                }
//                else {
                    alarm.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), alarmPendingIntent);
//                }
//                alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmPendingIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogManager.writeLogError(Constants.error_txt + e.getMessage());
        }
    }

}

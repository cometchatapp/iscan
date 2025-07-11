/*
package com.arteriatech.ss.msec.bil.v4.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.arteriatech.ss.msec.bil.v4.R;

*/
/**
 * Created by E10953 on 11-10-2019.
 *//*



public class AutoLogOffAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    ViewGroup valetModeWindow = null;
    WindowManager  windowManager = null;
    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DEBUG", "AutoSyncDataAlarmReceiver triggered");
//        try {
//            Intent intent1 = new Intent(context, AutoLogOffaService.class);
//            context.startService(intent1);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        try {
            windowManager = (WindowManager)context.getSystemService(WINDOW_SERVICE);
            // LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // View view = layoutInflater.inflate(R.layout.dummy_layout, null);
            valetModeWindow = (ViewGroup) View.inflate(context, R.layout.alert_autologoof, null);
            Button btn_cnt = valetModeWindow.findViewById(R.id.btn_cnt);
            Button btn_off = valetModeWindow.findViewById(R.id.btn_off);

            int LAYOUT_FLAG;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
            }
            WindowManager.LayoutParams params=new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            params.horizontalMargin=80;
            params.gravity= Gravity.CENTER|Gravity.CENTER;
            params.x=0;
            params.y=0;
            try {
                if(windowManager!=null) {
                    windowManager.addView(valetModeWindow, params);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            btn_cnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        try {
                            windowManager.removeView(valetModeWindow);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(context, AutoLogOffAlarmReceiver.class);
//alarmManager.cancel(PendingIntent.getService(context, 100, intent, 0));
                        PendingIntent pIntent = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                            pIntent = PendingIntent.getBroadcast
                                    (context, AutoLogOffAlarmReceiver.REQUEST_CODE,
                                            intent, PendingIntent.FLAG_IMMUTABLE);
                        } else {
                            pIntent = PendingIntent.getBroadcast(context, AutoLogOffAlarmReceiver.REQUEST_CODE,
                                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        }
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.cancel(pIntent);
                        pIntent.cancel();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
            btn_off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        try {
                            windowManager.removeView(valetModeWindow);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        OfflineManager.offlineStore = null;
                        OfflineManager.options = null;
//                        android.os.Process.killProcess(android.os.Process.myPid());
//                        ((Activity)getApplicationContext()).finishAffinity();
                        Intent intent = new Intent(context, AutoLogOffAlarmReceiver.class);
//alarmManager.cancel(PendingIntent.getService(context, 100, intent, 0));
                        PendingIntent pIntent = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                            pIntent = PendingIntent.getBroadcast
                                    (context, AutoLogOffAlarmReceiver.REQUEST_CODE,
                                            intent, PendingIntent.FLAG_IMMUTABLE);
                        } else {
                            pIntent = PendingIntent.getBroadcast(context, AutoLogOffAlarmReceiver.REQUEST_CODE,
                                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        }
                        MSFAApplication.finishAllActivities();
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.cancel(pIntent);
                        pIntent.cancel();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                }
            });
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME,0);
            String autoSyncTime = sharedPreferences.getString(Constants.AUTOPOPOFF,"");
            int time=0;
            if(!TextUtils.isEmpty(autoSyncTime)){
                time =  Integer.parseInt(autoSyncTime);
            }else{
                time =  10000;
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            windowManager.removeView(valetModeWindow);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        OfflineManager.offlineStore = null;
                        OfflineManager.options = null;
//                        android.os.Process.killProcess(android.os.Process.myPid());
//                        System.exit(1);
//                        ((Activity)getApplicationContext()).finishAffinity();
                        Intent intent = new Intent(context, AutoLogOffAlarmReceiver.class);
//alarmManager.cancel(PendingIntent.getService(context, 100, intent, 0));
                        PendingIntent pIntent = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                            pIntent = PendingIntent.getBroadcast
                                    (context, AutoLogOffAlarmReceiver.REQUEST_CODE,
                                            intent, PendingIntent.FLAG_IMMUTABLE);
                        } else {
                            pIntent = PendingIntent.getBroadcast(context, AutoLogOffAlarmReceiver.REQUEST_CODE,
                                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        }
                        MSFAApplication.finishAllActivities();
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.cancel(pIntent);
                        pIntent.cancel();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            },time);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}*/

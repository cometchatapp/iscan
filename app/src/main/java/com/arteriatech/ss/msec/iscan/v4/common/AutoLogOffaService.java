package com.arteriatech.ss.msec.iscan.v4.common;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;

/**
 * Created by E10953 on 11-10-2019.
 */

public class AutoLogOffaService extends Service {
    ViewGroup valetModeWindow = null;
    WindowManager  windowManager = null;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(AutoLogOffaService.this);
        builder.setTitle("Test dialog");
        builder.setMessage("Content");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        AlertDialog alert = builder.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 8.0 new features
            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY - 1);
        } else {
            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        }
        alert.show();*/
        try {
            windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
            // LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // View view = layoutInflater.inflate(R.layout.dummy_layout, null);
            valetModeWindow = (ViewGroup) View.inflate(this, R.layout.alert_autologoof, null);
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
               /*     try {
                        try {
                            windowManager.removeView(valetModeWindow);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(AutoLogOffaService.this, AutoLogOffAlarmReceiver.class);
//alarmManager.cancel(PendingIntent.getService(context, 100, intent, 0));
                        PendingIntent pIntent = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                            pIntent = PendingIntent.getBroadcast
                                    (AutoLogOffaService.this, AutoLogOffAlarmReceiver.REQUEST_CODE,
                                            intent, PendingIntent.FLAG_IMMUTABLE);
                        } else {
                            pIntent = PendingIntent.getBroadcast(AutoLogOffaService.this, AutoLogOffAlarmReceiver.REQUEST_CODE,
                                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        }
                        AlarmManager alarmManager = (AlarmManager) AutoLogOffaService.this.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.cancel(pIntent);
                        pIntent.cancel();
                        stopSelf();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }*/
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
                        System.exit(1);
//                        ((Activity)getApplicationContext()).finishAffinity();
                        /*Intent intent = new Intent(AutoLogOffaService.this, AutoLogOffAlarmReceiver.class);
//alarmManager.cancel(PendingIntent.getService(context, 100, intent, 0));
                        PendingIntent pIntent = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                            pIntent = PendingIntent.getBroadcast
                                    (AutoLogOffaService.this, AutoLogOffAlarmReceiver.REQUEST_CODE,
                                            intent, PendingIntent.FLAG_IMMUTABLE);
                        } else {
                            pIntent = PendingIntent.getBroadcast(AutoLogOffaService.this, AutoLogOffAlarmReceiver.REQUEST_CODE,
                                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        }
                        AlarmManager alarmManager = (AlarmManager) AutoLogOffaService.this.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.cancel(pIntent);
                        pIntent.cancel();
                        MSFAApplication.finishAllActivities();
                        stopSelf();*/
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                }
            });
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.PREFS_NAME,0);
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
                       /* Intent intent = new Intent(AutoLogOffaService.this, AutoLogOffAlarmReceiver.class);
//alarmManager.cancel(PendingIntent.getService(context, 100, intent, 0));
                        PendingIntent pIntent = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                            pIntent = PendingIntent.getBroadcast
                                    (AutoLogOffaService.this, AutoLogOffAlarmReceiver.REQUEST_CODE,
                                            intent, PendingIntent.FLAG_IMMUTABLE);
                        } else {
                            pIntent = PendingIntent.getBroadcast(AutoLogOffaService.this, AutoLogOffAlarmReceiver.REQUEST_CODE,
                                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        }
                        AlarmManager alarmManager = (AlarmManager) AutoLogOffaService.this.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.cancel(pIntent);
                        pIntent.cancel();
                        MSFAApplication.finishAllActivities();
                        stopSelf();*/
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            },time);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

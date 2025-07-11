package com.arteriatech.ss.msec.iscan.v4.autosync.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;

/**
 * Created by E10953 on 02-08-2019.
 */


public class ScheduleSyncDataAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.codepath.example.servicesdemo.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.d("DEBUG", "AutoSyncDataAlarmReceiver triggered");

            Intent i = new Intent(context, ScheduleSyncDataService.class);
            i.putExtra("foo", "alarm!!");
            // i.putExtra("receiver", MySimpleReceiver.setupServiceReceiver(context));
            LogManager.writeLogError("AutoSync started");
            if (!Constants.isSync&&!Constants.isBGSync) {
                ScheduleSyncDataService.enqueueWork(context, i);
            }else{
                ConstantsUtils.startScheduleSyncBG(context, true);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
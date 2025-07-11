package com.arteriatech.ss.msec.iscan.v4.autosync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.arteriatech.mutils.log.LogManager;

/**
 * Created by E10953 on 02-08-2019.
 */


public class AutoSyncDataAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.codepath.example.servicesdemo.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DEBUG", "AutoSyncDataAlarmReceiver triggered");

        Intent i = new Intent(context, AutoSynDataService.class);
        i.putExtra("foo", "alarm!!");
       // i.putExtra("receiver", MySimpleReceiver.setupServiceReceiver(context));
        LogManager.writeLogError("AutoSync started");
        AutoSynDataService.enqueueWork(context, i);
    }
}
package com.arteriatech.ss.msec.iscan.v4.exceptions;

import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.arteriatech.ss.msec.iscan.v4.SplashScreenActivity;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;

import org.jetbrains.annotations.NotNull;

public class UnhandledExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "UnUnHandler";
    private final AppCompatActivity activity;

    public UnhandledExceptionHandler(final AppCompatActivity activity) {
        this.activity = activity;
    }

    public void uncaughtException(@NotNull Thread unusedThread, final Throwable e) {
        /*android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);*/
        navigateToOtherActivity(unusedThread,e);
    }
    String DOUBLE_LINE_SEP ="\n\n";
    String SINGLE_LINE_SEP ="\n";
    private void navigateToOtherActivity(@NotNull Thread unusedThread, final Throwable e){
        StackTraceElement[] arr = e.getStackTrace();
        final StringBuffer report = new StringBuffer(e.toString());
//        final String lineSeperator = "-------------------------------\n\n";
        report.append(DOUBLE_LINE_SEP);
//        report.append("--------- Stack trace ---------\n\n");
        for (int i = 0; i < arr.length; i++) {
            report.append( "    ");
            report.append(arr[i].toString());
            report.append(SINGLE_LINE_SEP);
        }
//        report.append(lineSeperator);
        // If the exception was thrown in a background thread inside
        // AsyncTask, then the actual exception can be found with getCause
        report.append("--------- Cause ---------\n\n");

        report.append(e.getMessage());
        if (e.getCause()!= null) {
            report.append(e.getCause().toString());
            report.append(DOUBLE_LINE_SEP);
            arr = e.getCause().getStackTrace();
            for (int i = 0; i < arr.length; i++) {
                report.append("    ");
                report.append(arr[i].toString());
                report.append(SINGLE_LINE_SEP);
            }
        }
        // Getting the Device brand,model and sdk verion details.
//        report.append(lineSeperator);
        report.append("--------- Device ---------\n\n");
        report.append("Brand: ");
        report.append(Build.BRAND);
        report.append(SINGLE_LINE_SEP);
        report.append("Device: ");
        report.append(Build.DEVICE);
        report.append(SINGLE_LINE_SEP);
        report.append("Model: ");
        report.append(Build.MODEL);
        report.append(SINGLE_LINE_SEP);
        report.append("Id: ");
        report.append(Build.ID);
        report.append(SINGLE_LINE_SEP);
        report.append("Product: ");
        report.append(Build.PRODUCT);
        report.append(SINGLE_LINE_SEP);
//        report.append(lineSeperator);
        report.append("--------- Firmware ---------\n\n");
        report.append("SDK: ");
        report.append(Build.VERSION.SDK);
        report.append(SINGLE_LINE_SEP);
        report.append("Release: ");
        report.append(Build.VERSION.RELEASE);
        report.append(SINGLE_LINE_SEP);
        report.append("Incremental: ");
        report.append(Build.VERSION.INCREMENTAL);
        report.append(SINGLE_LINE_SEP);
//        report.append(lineSeperator);

        Log.e("Report ::", report.toString());
        Intent crashedIntent = new Intent(activity, SplashScreenActivity.class);
        crashedIntent.putExtra(Constants.EXTRA_CRASHED_FLAG,  report.toString());
        crashedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(crashedIntent);

        System.exit(0);
        // If you don't kill the VM here the app goes into limbo

    }
}

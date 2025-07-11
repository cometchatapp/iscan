package com.arteriatech.ss.msec.iscan.v4.mutils.upgrade;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by e10769 on 01-09-2017.
 */

public class ApplicationLifecycleHandler implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    private static final String TAG = ApplicationLifecycleHandler.class.getSimpleName();
    public static boolean isInLoginPage = false;
    public static boolean isInBackground = false;
    public static boolean isUpdateQueued = false;
    public static boolean isPopupToShow = false;


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

        if (isInBackground) {
            Log.d(TAG, "app went to foreground");
            isInBackground = false;
            if (isPopupToShow) {
                isPopupToShow = false;
                    Intent dialogIntent = new Intent(activity.getApplicationContext(), UpdateDialogActivity.class);
                    dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(dialogIntent);
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override
    public void onLowMemory() {
    }

    @Override
    public void onTrimMemory(int i) {
        if (i == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Log.d(TAG, "app went to background");
            isInBackground = true;
            if (isUpdateQueued)
                isPopupToShow = true;
            else
                isPopupToShow = false;
        }
    }
}

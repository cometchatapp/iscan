package com.arteriatech.ss.msec.iscan.v4.mutils.security;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;


import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.login.PasscodeLoginActivity;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.RegistrationModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by e10769 on 11-10-2017.
 */

public class DefaultAppLock extends AppLockApplication {

    private static final String UNLOCK_CLASS_NAME = PasscodeLoginActivity.class.getName();
    private static String TAG = DefaultAppLock.class.getSimpleName();
    private final Application mCurrentApp;
    private final SharedPreferences mSharedPreferences;
    private Date mLostFocusDate = null;
    public static final String EXTRA_FROM_APPLOCK = "extraFromAppLock";
    public static final String EXTRA_FROM_VERIFY_PASSCODE = "extraFromVerifyPasscode";
    private ArrayList<String> classNameList = new ArrayList<>();
    private RegistrationModel registrationModel=null;

    public DefaultAppLock(Application app, RegistrationModel registrationModel) {
        super();
        mCurrentApp = app;
        this.registrationModel=registrationModel;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mCurrentApp);
    }

    public static boolean isSupportedApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d(TAG, "onActivityStarted: ");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d(TAG, "onActivityResumed: ");
        if (!isExemptActivity(activity.getClass().getName()) && shouldShowUnlockScreen()) {
            Intent i = new Intent(activity.getApplicationContext(), PasscodeLoginActivity.class);
            i.putExtra(UtilConstants.RegIntentKey, registrationModel);
            i.putExtra(EXTRA_FROM_APPLOCK, true);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.getApplication().startActivity(i);
        }
    }

    private boolean shouldShowUnlockScreen() {
        if (!isPasswordLocked()) {
            return false;
        }
        if (mLostFocusDate == null) {
            return true;
        }

        int currentTimeOut = getTimeout();

        setTimeLimit();
        if (timeSinceLocked() < currentTimeOut) {
            return false;
        }
//        mLostFocusDate = null;
        return true;
    }

    private void setTimeLimit() {
        int timeLimitSec = mSharedPreferences.getInt(UtilConstants.SET_TIME_OUT_LIMIT, DEFAULT_TIMEOUT_S);
        setOneTimeTimeout(timeLimitSec);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d(TAG, "onActivityPaused: ");
        if (!isExemptActivity(activity.getClass().getName())) mLostFocusDate = new Date();
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d(TAG, "onActivityStopped: ");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.d(TAG, "onActivitySaveInstanceState: ");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d(TAG, "onActivityDestroyed: ");
    }

    public boolean isExemptActivity(String activityName) {
        return (classNameList != null && classNameList.contains(activityName)) || super.isExemptActivity(activityName) || UNLOCK_CLASS_NAME.equals(activityName);
    }

    public void enable() {
        if (!isPasswordLocked()) return;
        if (isSupportedApi()) {
            mCurrentApp.unregisterActivityLifecycleCallbacks(this);
            mCurrentApp.registerActivityLifecycleCallbacks(this);
        }
    }

    private int timeSinceLocked() {
        return Math.abs((int) ((new Date().getTime() - mLostFocusDate.getTime()) / 1000));
    }

    public void disable() {
        if (isSupportedApi()) {
            mCurrentApp.unregisterActivityLifecycleCallbacks(this);
        }
    }

    public void forcePasswordLock() {
        mLostFocusDate = null;
    }

    private boolean isPasswordLocked() {
//        mSharedPreferences.getString(Constants.QUICK_PIN, "");
        String permission = mSharedPreferences.getString(UtilConstants.QUICK_PIN_ACCESS, "");
        String enablePermission = mSharedPreferences.getString(UtilConstants.ENABLE_ACCESS, "");
        return UtilConstants.SECURITY_ON.equalsIgnoreCase(permission) && UtilConstants.SECURITY_ON.equalsIgnoreCase(enablePermission);
    }

    public boolean verifyPassword(String password) {
        if (TextUtils.isEmpty(password)) return false;

        // successful fingerprint scan bypasses PIN security
        if (isFingerprintPassword(password)) {
            mLostFocusDate = new Date();
            return true;
        }
        String securePassword = mSharedPreferences.getString(UtilConstants.QUICK_PIN, "");
        if (!password.equalsIgnoreCase(securePassword))
            return false;
        mLostFocusDate = new Date();
        return true;
    }

    public boolean setPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            disable();
            forcePasswordLock();
        } else {
            enable();
            verifyPassword(password);
        }
        return true;
    }

    @Override
    public void setUnlockClassName(ArrayList<String> classNameList) {
        this.classNameList.addAll(classNameList);

    }
}

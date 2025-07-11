package com.arteriatech.ss.msec.iscan.v4.mutils.security;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by e10769 on 11-10-2017.
 */

public abstract class AppLockApplication implements Application.ActivityLifecycleCallbacks {
    public static final int DEFAULT_TIMEOUT_S = 1;
    public static final String FINGERPRINT_VERIFICATION_BYPASS = "fingerprint-bypass__";
    private int mLockTimeout = DEFAULT_TIMEOUT_S;
    private String[] mExemptActivities;

    public boolean isExemptActivity(String name) {
        if (name == null) return false;
        for (String activityName : getExemptActivities()) {
            if (name.equals(activityName)) return true;
        }
        return false;
    }

    public String[] getExemptActivities() {
        if (mExemptActivities == null) setExemptActivities(new String[0]);
        return mExemptActivities;
    }

    public void setExemptActivities(String[] exemptActivities) {
        mExemptActivities = exemptActivities;
    }

    public void setOneTimeTimeout(int timeout) {
        mLockTimeout = timeout;
    }

    public int getTimeout() {
        return mLockTimeout;
    }
    protected boolean isFingerprintPassword(String password) {
        return FINGERPRINT_VERIFICATION_BYPASS.equals(password);
    }

    /**
     * Whether the fingerprint unlocking should be available as option in the unlock screen.
     * Default is true, but implementation can override this and make their choice.
     * <p>
     * Note that this doesn't affect system setting, the device must already have fingerprint unlock
     * available and correctly working.
     *
     * @return true if fingerprint unlock should be enabled on the lock screen
     */
    public boolean isFingerprintEnabled() {
        return true;
    }
    public abstract void enable();
    public abstract void disable();
    public abstract void forcePasswordLock();
    public abstract boolean verifyPassword(String password);
    public abstract boolean setPassword(String password);
    public abstract void setUnlockClassName(ArrayList<String> classNameList);
}

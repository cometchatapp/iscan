package com.arteriatech.ss.msec.iscan.v4.mutils.security;

import android.app.Application;

import com.arteriatech.ss.msec.iscan.v4.mutils.registration.RegistrationModel;

/**
 * Created by e10769 on 11-10-2017.
 */

public class AppLockManager {
    private static AppLockManager instance;
    private AppLockApplication currentAppLocker;

    public static AppLockManager getInstance() {
        if (instance == null) {
            instance = new AppLockManager();
        }
        return instance;
    }

    public void enableDefaultAppLockIfAvailable(Application currentApp, RegistrationModel registrationModel) {
        if (!DefaultAppLock.isSupportedApi()) return;

        if (currentAppLocker != null) {
            if (currentAppLocker instanceof DefaultAppLock) {
                // A previous default applocker is already in place
                // No need to re-enable it
                return;
            }
            // A previous NON-default applockr is in place. Disable it.
            currentAppLocker.disable();
        }

        currentAppLocker = new DefaultAppLock(currentApp, registrationModel);
        currentAppLocker.enable();
    }

    public AppLockApplication getAppLock() {
        return currentAppLocker;
    }
}

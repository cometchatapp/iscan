package com.arteriatech.ss.msec.iscan.v4.mutils.upgrade;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;

/**
 * Created by e10769 on 01-09-2017.
 */

public class UpdateDialogActivity extends Activity {
    public static String EXTRA_SCHEDULE_TIME = "scheduleTime";
    public static String EXTRA_UPDATE_TYPE = "updateType";
    public static String EXTRA_PACKAGE_NAME = "packageName";

    public static String KEY_SCHEDULE_TIME = "scheduleTimeKey";
    public static String KEY_UPDATE_TYPE = "updateTypeKey";
    public static String KEY_PACKAGE_NAME = "packageNameKey";
    public static String KEY_SERVER_VERSION = "appServerVersionKey";
    public static String KEY_SERVER_VERSION_CODE = "appServerVersionCodeKey";

    private String scheduleTime = "";
    private String updateType = "";
    private String packageName = "";
    private String currentVersionName = "0";
    private String serverVersionName = "0";
    private int serverVersionCode = 0;
    private int currentVersionCode = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            scheduleTime = intent.getString(EXTRA_SCHEDULE_TIME, "");
            updateType = intent.getString(EXTRA_UPDATE_TYPE, "");
            packageName = intent.getString(EXTRA_PACKAGE_NAME, "");
        }
        if (ApplicationLifecycleHandler.isInBackground) {
            finish();
            UpdateDialogActivity.this.moveTaskToBack(true);
            ApplicationLifecycleHandler.isUpdateQueued = true;
        } else if (ApplicationLifecycleHandler.isInLoginPage) {
            finish();
        } else {
            try {
                SharedPreferences settings = getSharedPreferences("settingsPref", 0);
                if (TextUtils.isEmpty(scheduleTime)) {
                    scheduleTime = settings.getString(KEY_SCHEDULE_TIME, "");
                }
                if (TextUtils.isEmpty(updateType)) {
                    updateType = settings.getString(KEY_UPDATE_TYPE, "");
                }
                if (TextUtils.isEmpty(packageName)) {
                    packageName = settings.getString(KEY_PACKAGE_NAME, "");
                }

                serverVersionName = settings.getString(KEY_SERVER_VERSION, "");
                serverVersionCode = settings.getInt(KEY_SERVER_VERSION_CODE, 0);
                ApplicationLifecycleHandler.isUpdateQueued = false;
                int versionDiffCount = 0;
                if (!TextUtils.isEmpty(serverVersionName)) {
                    try {
                        currentVersionName = getPackageManager()
                                .getPackageInfo(getPackageName(), 0).versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    String[] appVersionNumberArray = currentVersionName.split("\\.");
                    String[] backEndVersionNumberArray = serverVersionName.split("\\.");
                    versionDiffCount = Integer.parseInt(backEndVersionNumberArray[backEndVersionNumberArray.length - 1]) -
                            Integer.parseInt(appVersionNumberArray[appVersionNumberArray.length - 1]);
                } else {
                    try {
                        currentVersionCode = getPackageManager()
                                .getPackageInfo(getPackageName(), 0).versionCode;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    versionDiffCount = serverVersionCode - currentVersionCode;
                }
                if (versionDiffCount > 0) {
                    AppUpgradeConfig.displayUpdatePopup(UpdateDialogActivity.this, updateType, scheduleTime, packageName, true);
                } else {
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }

        }
    }
}

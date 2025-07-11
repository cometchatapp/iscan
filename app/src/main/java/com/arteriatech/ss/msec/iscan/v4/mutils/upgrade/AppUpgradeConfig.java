package com.arteriatech.ss.msec.iscan.v4.mutils.upgrade;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilOfflineManager;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilOnlineManager;
import com.arteriatech.ss.msec.iscan.v4.ui.DialogFactory;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.offline.ODataOfflineStore;

import java.util.HashMap;
import java.util.List;


public class AppUpgradeConfig {
    public static void updateApplicationNow(Activity context, String packageName) {
        try {
            if (TextUtils.isEmpty(packageName)) {
                if (UtilConstants.isPackageInstalled("com.Android.Afaria", context.getPackageManager())) {
                    Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage("com.Android.Afaria");
                    LaunchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(LaunchIntent);
                    context.finishAffinity();
                    System.exit(0);
                } else {
                    openPlayStore(context, "com.Android.Afaria");
                }
            } else {
                openPlayStore(context, packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openPlayStore(Activity context, String appPackageName) {
        try {
            try {
                Intent intentNavPrevScreen = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
                intentNavPrevScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intentNavPrevScreen);
                context.finishAffinity();
                System.exit(0);
            } catch (android.content.ActivityNotFoundException anfe) {
                Intent intentNavPrevScreen = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
                intentNavPrevScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intentNavPrevScreen);
                context.finishAffinity();
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayUpdatePopup(final Activity context, final String updateType, final String scheduleTime, final String packageName, final boolean isCallFinish) {
        try {
            if (updateType.equalsIgnoreCase("01")) {
                new DialogFactory.Alert(context).setMessage(context.getString(R.string.alert_update_app_version)).setPositiveButton(context.getString(R.string.ok))
                        .setTheme(R.style.MaterialAlertDialog).isAlert(true)
                        .setOnDialogClick(isPositive -> {
                            if (isPositive) {
                                updateApplicationNow(context, packageName);
                            }
                        })
                        .show();
            } else if (updateType.equalsIgnoreCase("02")) {
                new DialogFactory.Alert(context).setMessage(context.getString(R.string.alert_update_app_version)).setPositiveButton(context.getString(R.string.ok))
                        .setNegativeButton(context.getString(R.string.lbl_later))
                        .setTheme(R.style.MaterialAlertDialog)
                        .setOnDialogClick(isPositive -> {
                            if (isPositive) {
                                updateApplicationNow(context, packageName);
                            }else {
                                scheduleLater(context, scheduleTime, updateType, packageName);
                                if (isCallFinish)
                                    context.finish();
                            }
                        })
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayUpdatePopup(final Activity context, final String updateType, final String scheduleTime, final String packageName, final boolean isCallFinish,IAppUpdateListener updateListener) {
        try {
            if (updateType.equalsIgnoreCase("01")) {
                new DialogFactory.Alert(context).setMessage(context.getString(R.string.alert_update_app_version)).setPositiveButton(context.getString(R.string.ok))
                        .setTheme(R.style.MaterialAlertDialog).isAlert(true)
                        .setOnDialogClick(isPositive -> {
                            if (isPositive) {
                                updateApplicationNow(context, packageName);
                            }
                        })
                        .show();
            } else if (updateType.equalsIgnoreCase("02")) {
                new DialogFactory.Alert(context).setMessage(context.getString(R.string.alert_update_app_version)).setPositiveButton(context.getString(R.string.ok))
                        .setNegativeButton(context.getString(R.string.lbl_later))
                        .setTheme(R.style.MaterialAlertDialog)
                        .setOnDialogClick(isPositive -> {
                            if (isPositive) {
                                updateApplicationNow(context, packageName);
                            }else {
                                scheduleLater(context, scheduleTime, updateType, packageName);
                                if (isCallFinish)
                                    context.finish();
                            }
                        })
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void scheduleLater(final Activity mContext, final String updateNotificationTime, final String updateType, final String packageName) {
        try {
            long intervalValInMiliSeconds = Integer.parseInt(updateNotificationTime) * 60 * 1000;//time in minutes
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after x minutes
                    Intent dialogIntent = new Intent(mContext, UpdateDialogActivity.class);
                    dialogIntent.putExtra(UpdateDialogActivity.EXTRA_SCHEDULE_TIME, updateNotificationTime);
                    dialogIntent.putExtra(UpdateDialogActivity.EXTRA_UPDATE_TYPE, updateType);
                    dialogIntent.putExtra(UpdateDialogActivity.EXTRA_PACKAGE_NAME, packageName);
                    dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    dialogIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mContext.startActivity(dialogIntent);
                }
            }, intervalValInMiliSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getUpdateAvailability(ODataOfflineStore offlineStore, Activity activity, String packageName,boolean isCallFinish) {
        String currentVersionName = "0";
        String serverVersionName = "0";
        String updateNotificationTime = "0";
        String updateType = "0";
        HashMap<String, String> typesetValues = null;
        try {
            currentVersionName = activity.getPackageManager()
                    .getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            typesetValues = UtilOfflineManager.getBackendVersionName(offlineStore, "ConfigTypsetTypeValues?$filter=(Types eq 'V3APPVER' or Types eq 'V3NOTFINTV' or Types eq 'V3UPDTYP')");

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (typesetValues != null && !typesetValues.isEmpty()) {
                serverVersionName = typesetValues.get("V3APPVER");
                if (serverVersionName == null) {
                    serverVersionName = "0";
                }
                updateNotificationTime = typesetValues.get("V3NOTFINTV");
                if (updateNotificationTime == null) {
                    updateNotificationTime = "0";
                }
                updateType = typesetValues.get("V3UPDTYP");
                if (updateType == null) {
                    updateType = "0";
                }
            }
            String[] appVersionNumberArray = currentVersionName.split("\\.");
            String[] backEndVersionNumberArray = serverVersionName.split("\\.");
            int versionDiffCount = Integer.parseInt(backEndVersionNumberArray[backEndVersionNumberArray.length - 1]) -
                    Integer.parseInt(appVersionNumberArray[appVersionNumberArray.length - 1]);
            SharedPreferences settings = activity.getSharedPreferences("settingsPref", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(UpdateDialogActivity.KEY_SCHEDULE_TIME, updateNotificationTime);
            editor.putString(UpdateDialogActivity.KEY_UPDATE_TYPE, updateType);
            editor.putString(UpdateDialogActivity.KEY_PACKAGE_NAME, packageName);
            editor.putString(UpdateDialogActivity.KEY_SERVER_VERSION, serverVersionName);
            editor.apply();
            if (versionDiffCount > 0) {
                displayUpdatePopup(activity, updateType, updateNotificationTime, packageName, isCallFinish);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean getUpdateOnlineAvailability( List<ODataEntity> list, Activity activity, String packageName, boolean isCallFinish) {
        int currentVersionCode = 0;
        int serverVersionCode = 0;
        String updateNotificationTime = "0";
        String updateType = "0";
        HashMap<String, String> typesetValues = null;
        try {
            currentVersionCode = activity.getPackageManager()
                    .getPackageInfo(activity.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            typesetValues = UtilOnlineManager.getAppVersionDetails(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (typesetValues != null && !typesetValues.isEmpty()) {
               String serverVersionName = typesetValues.get("V3APPVER");
                if (serverVersionName == null) {
                    serverVersionCode = 0;
                }else {
                    serverVersionCode = Integer.parseInt(serverVersionName);
                }
                updateNotificationTime = typesetValues.get("V3NOTFINTV");
                if (updateNotificationTime == null) {
                    updateNotificationTime = "0";
                }
                updateType = typesetValues.get("V3UPDTYP");
                if (updateType == null) {
                    updateType = "0";
                }
            }
            int versionDiffCount = serverVersionCode - currentVersionCode;

            SharedPreferences settings = activity.getSharedPreferences("settingsPref", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(UpdateDialogActivity.KEY_SCHEDULE_TIME, updateNotificationTime);
            editor.putString(UpdateDialogActivity.KEY_UPDATE_TYPE, updateType);
            editor.putString(UpdateDialogActivity.KEY_PACKAGE_NAME, packageName);
           editor.putString(UpdateDialogActivity.KEY_SERVER_VERSION, "");
            editor.putInt(UpdateDialogActivity.KEY_SERVER_VERSION_CODE, serverVersionCode);
            editor.apply();
            if (versionDiffCount > 0) {
                displayUpdatePopup(activity, updateType, updateNotificationTime, packageName, isCallFinish);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean getUpdateAvlUsingVerCode(ODataOfflineStore offlineStore, Activity activity, String packageName,boolean isCallFinish) {
        return getUpdateAvlUsingVerCode(offlineStore,activity,packageName,isCallFinish,"");
    }
    public static boolean getUpdateAvlUsingVerCode(ODataOfflineStore offlineStore, Activity activity, String packageName,boolean isCallFinish, String typeSet) {
        int currentVersionCode = 0;
        int serverVersionCode = 0;
        String updateNotificationTime = "0";
        String updateType = "0";
        HashMap<String, String> typesetValues = null;

        try {
            currentVersionCode = activity.getPackageManager()
                    .getPackageInfo(activity.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            if (!TextUtils.isEmpty(typeSet)){
                typesetValues = UtilOfflineManager.getBackendVersionName(offlineStore, "ConfigTypsetTypeValues?$filter=(Types eq 'V3APPVER' or Types eq 'V3NOTFINTV' or Types eq 'V3UPDTYP') and Typeset eq '"+typeSet+"'");
            }else {
                typesetValues = UtilOfflineManager.getBackendVersionName(offlineStore, "ConfigTypsetTypeValues?$filter=(Types eq 'V3APPVER' or Types eq 'V3NOTFINTV' or Types eq 'V3UPDTYP')");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (typesetValues != null && !typesetValues.isEmpty()) {

                String serverVersionName = typesetValues.get("V3APPVER");
                if (serverVersionName != null && !TextUtils.isEmpty(serverVersionName)) {
                    try {
                        serverVersionCode = Integer.parseInt(serverVersionName);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                updateNotificationTime = typesetValues.get("V3NOTFINTV");
                if (updateNotificationTime == null) {
                    updateNotificationTime = "0";
                }
                updateType = typesetValues.get("V3UPDTYP");
                if (updateType == null) {
                    updateType = "0";
                }
            }
            int versionDiffCount = serverVersionCode - currentVersionCode;
            SharedPreferences settings = activity.getSharedPreferences("settingsPref", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(UpdateDialogActivity.KEY_SCHEDULE_TIME, updateNotificationTime);
            editor.putString(UpdateDialogActivity.KEY_UPDATE_TYPE, updateType);
            editor.putString(UpdateDialogActivity.KEY_PACKAGE_NAME, packageName);
            editor.putInt(UpdateDialogActivity.KEY_SERVER_VERSION_CODE, serverVersionCode);
            editor.apply();
            if (versionDiffCount > 0) {
                displayUpdatePopup(activity, updateType, updateNotificationTime, packageName, isCallFinish);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean getUpdateAvlUsingVerCode(ODataOfflineStore offlineStore, Activity activity, String packageName,boolean isCallFinish, String typeSet,IAppUpdateListener updateListener) {
        int currentVersionCode = 0;
        int serverVersionCode = 0;
        String updateNotificationTime = "0";
        String updateType = "0";
        HashMap<String, String> typesetValues = null;

        try {
            currentVersionCode = activity.getPackageManager()
                    .getPackageInfo(activity.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            if (!TextUtils.isEmpty(typeSet)){
                typesetValues = UtilOfflineManager.getBackendVersionName(offlineStore, "ConfigTypsetTypeValues?$filter=(Types eq 'V3APPVER' or Types eq 'V3NOTFINTV' or Types eq 'V3UPDTYP') and Typeset eq '"+typeSet+"'");
            }else {
                typesetValues = UtilOfflineManager.getBackendVersionName(offlineStore, "ConfigTypsetTypeValues?$filter=(Types eq 'V3APPVER' or Types eq 'V3NOTFINTV' or Types eq 'V3UPDTYP')");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (typesetValues != null && !typesetValues.isEmpty()) {

                String serverVersionName = typesetValues.get("V3APPVER");
                if (serverVersionName != null && !TextUtils.isEmpty(serverVersionName)) {
                    try {
                        serverVersionCode = Integer.parseInt(serverVersionName);
                    }catch (Exception e){
//                        e.printStackTrace();
                    }
                }
                updateNotificationTime = typesetValues.get("V3NOTFINTV");
                if (updateNotificationTime == null) {
                    updateNotificationTime = "0";
                }
                updateType = typesetValues.get("V3UPDTYP");
                if (updateType == null) {
                    updateType = "0";
                }
            }
            int versionDiffCount = serverVersionCode - currentVersionCode;
            SharedPreferences settings = activity.getSharedPreferences("settingsPref", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(UpdateDialogActivity.KEY_SCHEDULE_TIME, updateNotificationTime);
            editor.putString(UpdateDialogActivity.KEY_UPDATE_TYPE, updateType);
            editor.putString(UpdateDialogActivity.KEY_PACKAGE_NAME, packageName);
            editor.putInt(UpdateDialogActivity.KEY_SERVER_VERSION_CODE, serverVersionCode);
            editor.apply();
            if (versionDiffCount > 0) {
                displayUpdatePopup(activity, updateType, updateNotificationTime, packageName, isCallFinish,updateListener);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public interface IAppUpdateListener{
        void OnLaterListener();
    }

}

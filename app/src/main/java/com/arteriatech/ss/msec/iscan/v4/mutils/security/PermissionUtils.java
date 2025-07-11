package com.arteriatech.ss.msec.iscan.v4.mutils.security;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by e10769 on 09-10-2017.
 */

public class PermissionUtils {


    public static final int CAMERA_PERMISSION_CONSTANT = 890;
    public static final int STORAGE_PERMISSION = 701;
    public static final String KEY_SHAREDPREF="permissionStatus";

    public static void setPermissionStatus(Context mContext, String key, boolean value) {
        SharedPreferences permissionStatus = mContext.getSharedPreferences(KEY_SHAREDPREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = permissionStatus.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getPermissionStatus(Context mContext, String key) {
        SharedPreferences permissionStatus = mContext.getSharedPreferences(KEY_SHAREDPREF, MODE_PRIVATE);
        return permissionStatus.getBoolean(key, false);
    }

    public static boolean checkStoragePermission(final Activity activity) {
        boolean isPermissionEnable = false;
        if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((activity), Manifest.permission.READ_EXTERNAL_STORAGE)
                        && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
                } else if (getPermissionStatus(activity, Manifest.permission.READ_EXTERNAL_STORAGE) && getPermissionStatus(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    UtilConstants.dialogBoxWithCallBack(activity, "", activity.getString(R.string.this_app_needs_storage_permission), activity.getString(R.string.msg_go_to_settings),
                            activity.getString(R.string.cancel), false, new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                    if (clickedStatus) {
                                        UtilConstants.navigateToAppSettingsScreen(activity);
                                    }
                                }
                            });
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION);
                }
                setPermissionStatus(activity, Manifest.permission.READ_EXTERNAL_STORAGE, true);
                setPermissionStatus(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            } else {
                //You already have the permission
                isPermissionEnable = true;
            }
        } else {
            isPermissionEnable = true;
        }
        return isPermissionEnable;
    }
}

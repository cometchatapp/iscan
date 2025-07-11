package com.arteriatech.ss.msec.iscan.v4.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity;
import com.sap.smp.client.httpc.authflows.UsernamePasswordProvider;
import com.sap.smp.client.httpc.authflows.UsernamePasswordToken;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.httpc.events.ISendEvent;

public class CredentialsProvider implements UsernamePasswordProvider {
    private static CredentialsProvider instance;
    private SharedPreferences sharedPreferences;
    private Context mContext;

    private CredentialsProvider(Context mContext) {
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
    }

    public static CredentialsProvider getInstance(Context mContext) {
        if (instance == null) {
            instance = new CredentialsProvider(mContext);
        }
        return instance;
    }

    @Override
    public Object onCredentialsNeededForChallenge(IReceiveEvent arg0) {
//        return new UsernamePasswordToken(Configuration.UserName, Configuration.Password);
        return new UsernamePasswordToken(sharedPreferences.getString(UtilRegistrationActivity.KEY_username,""), sharedPreferences.getString(UtilRegistrationActivity.KEY_password,""));
    }

    @Override
    public Object onCredentialsNeededUpfront(ISendEvent iSendEvent) {
//        return new UsernamePasswordToken(Configuration.UserName, Configuration.Password);
        return new UsernamePasswordToken(sharedPreferences.getString(UtilRegistrationActivity.KEY_username,""), sharedPreferences.getString(UtilRegistrationActivity.KEY_password,""));
    }
}
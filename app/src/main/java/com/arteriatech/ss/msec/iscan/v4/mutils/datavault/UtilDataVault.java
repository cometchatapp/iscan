package com.arteriatech.ss.msec.iscan.v4.mutils.datavault;


import android.content.Context;

import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.sybase.persistence.DataVaultException;
import com.sybase.persistence.PrivateDataVault;

/**
 * Created by e10742 on 23-11-2016.
 */
public class UtilDataVault {

    public static void storeInDataVault(String docNo, String jsonHeaderObjectAsString, Context mContext, String password) {
       /* try {
            LogonCore.getInstance().addObjectToStore(docNo, jsonHeaderObjectAsString);
        } catch (LogonCoreException e) {
            e.printStackTrace();
        }*/
        PrivateDataVault privateDataVault = null;
        boolean isPrivateDVAvailable = false;
        try {
            try {
                isPrivateDVAvailable = PrivateDataVault.vaultExists(mContext.getPackageName());
            } catch (DataVaultException e) {
                e.printStackTrace();
            }
            if (!isPrivateDVAvailable) {
                privateDataVault = PrivateDataVault.createVault(mContext.getPackageName(), password.toCharArray());
            } else {
                privateDataVault = PrivateDataVault.getVault(mContext.getPackageName());
            }
            if (privateDataVault != null) {
                if (privateDataVault.isLocked())
                    privateDataVault.unlock(password.toCharArray());
                privateDataVault.setString(docNo, jsonHeaderObjectAsString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getValueFromDataVault(String key, Context mContext, String password) {
        String store = null;
       /* try {
            store = LogonCore.getInstance().getObjectFromStore(key);
        } catch (LogonCoreException e) {
            e.printStackTrace();
        }*/
        try {
            PrivateDataVault privateDataVault = PrivateDataVault.getVault(mContext.getPackageName());
            if (privateDataVault != null) {
                if (privateDataVault.isLocked())
                    privateDataVault.unlock(password.toCharArray());
                store = privateDataVault.getString(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return store;
    }

    public static void deleteValue(String key, Context context){
        try {
            PrivateDataVault privateDataVault = PrivateDataVault.getVault(context.getPackageName());
            if (privateDataVault != null) {
                if (privateDataVault.isLocked())
                    privateDataVault.unlock(Constants.EncryptKey.toCharArray());
                privateDataVault.deleteValue(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

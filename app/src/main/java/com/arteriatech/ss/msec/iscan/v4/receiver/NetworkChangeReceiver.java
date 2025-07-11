package com.arteriatech.ss.msec.iscan.v4.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.sync.BackgroundSOSync;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if(wifi.isConnected() ){
            if (OfflineManager.offlineStore!=null&&OfflineManager.isOfflineStoreOpen()) {
//                backGroundSaleOrderPost(context);
            }
//            Toast.makeText(context, "isConnected", Toast.LENGTH_SHORT).show();
        }else if(mobile.isConnected() ){
            if (OfflineManager.offlineStore!=null&&OfflineManager.isOfflineStoreOpen()) {
//                backGroundSaleOrderPost(context);
            }
//            Toast.makeText(context, "isConnected", Toast.LENGTH_SHORT).show();
        }else {
            Constants.isBGSync=false;
            Constants.iSAutoSync=false;
            LogManager.writeLogError("Back ground so sync cannot be performed. Please check your internet connectivity");
//            Toast.makeText(context, "isDisconnected", Toast.LENGTH_SHORT).show();
        }
    }

    private void backGroundSaleOrderPost(Context context){
        if (UtilConstants.isNetworkAvailable(context)) {
            if(ConstantsUtils.isPinging()){
//                Toast.makeText(context, "isConnected", Toast.LENGTH_SHORT).show();
                if (!Constants.iSAutoSync && !Constants.isBGSync) {
                    new BackgroundSOSync(true).init(context, new BackgroundSOSync.IBGSyncListener() {
                        @Override
                        public void onCreateListener(boolean isError, int createType) {
                            if (!isError&&createType==BackgroundSOSync.BG_SO){
                            }
                        }

                        @Override
                        public void onFlushListener(boolean isError) {
                            // write Log.
                            if (isError) {
                                LogManager.writeLogInfo("OfflineFlush failed in background sync");
                            }else{
                                LogManager.writeLogInfo("OfflineFlush completed successfully in background sync");
                            }
                        }

                        @Override
                        public void onRefreshListener(boolean isError) {
                            // write Log.
                            try {
                                LogManager.writeLogInfo("Offline Refresh completed successfully in background sync");
                                SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                if (sharedPreferences.contains( Constants.OUTLET_LOCKED)) {
                                    editor.remove( Constants.OUTLET_LOCKED);
                                    editor.commit();
                                }
                            } catch (Throwable e) {
                            }
                        }
                    }).syncData();
                }else {
                    if(Constants.isBGSync){
                        LogManager.writeLogInfo("Background Sync is in progress, Background Sync cannot be performed.");
                    }else {
                        LogManager.writeLogInfo("AutoSync is in progress, Background Sync cannot be performed.");
                    }
                }
            }else {
//                Toast.makeText(context, "isDisconnected", Toast.LENGTH_SHORT).show();
                LogManager.writeLogError("Back ground so sync cannot be performed. Please check your internet connectivity");
            }
        }else {
//            Toast.makeText(context, "isDisconnected", Toast.LENGTH_SHORT).show();
            LogManager.writeLogError("Back ground so sync cannot be performed. Please check your internet connectivity");
        }
    }
}

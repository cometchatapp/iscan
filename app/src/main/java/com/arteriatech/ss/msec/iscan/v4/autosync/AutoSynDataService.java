package com.arteriatech.ss.msec.iscan.v4.autosync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.arteriatech.mutils.common.OfflineODataStoreException;
import com.arteriatech.mutils.common.UIListener;
import com.arteriatech.mutils.common.UtilConstants;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.MSFAApplication;
import com.arteriatech.ss.msec.iscan.v4.sync.AutoBackgroundSync;
import com.arteriatech.ss.msec.iscan.v4.sync.BackgroundSync;
import com.sap.client.odata.v4.core.GUID;
import com.sap.smp.client.odata.exception.ODataException;

import java.util.ArrayList;

/**
 * Created by E10953 on 02-08-2019.
 */

public class AutoSynDataService extends JobIntentService implements UIListener {
    public static final int JOB_ID = 416;
    long timestamp;
    private Handler mHandlerDifferentTrd = new Handler();
    public static String TAG = "AutoSynDataService";
    public static Context sContext ;
    private int penReqCount = 0;
    private int mIntPendingCollVal = 0;
    private String[][] invKeyValues = null;
    private ArrayList<String> alAssignColl = new ArrayList<>();
    private ArrayList<String> alFlushColl = new ArrayList<>();
    private int mError = 0;
    private boolean tokenFlag = false, onlineStoreOpen = false;
    private GUID refguid =null;
    private String TAG_SO=getClass().getName();


    public static void enqueueWork(Context context, Intent work) {
        sContext = context;
        enqueueWork(context, AutoSynDataService.class, JOB_ID, work);
    }

    // This describes what will happen when service is triggered
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d("DEBUG", "AutoSynDataService triggered");
        sContext = AutoSynDataService.this;

        timestamp =  System.currentTimeMillis();
        // Extract additional values from the bundle
        String val = intent.getStringExtra("foo");

        mHandlerDifferentTrd.post(() -> {
            try {
                Log.d(TAG, "auto sync run: started");

                Constants.mErrorCount = 0;

                if (!Constants.isSync&&!Constants.isBGSync) {
                    Constants.iSAutoSync = true;
                    if (UtilConstants.isNetworkAvailable(sContext)) {
                        if (!Constants.isDayStartSyncEnbled)
                            LogManager.writeLogInfo(sContext.getString(R.string.auto_sync_started));
                        Constants.mApplication = (MSFAApplication) sContext.getApplicationContext();
                        LogManager.writeLogError("AutoSync is in progress");
                        new AutoBackgroundSync(true,true).init(sContext, new AutoBackgroundSync.IBGSyncListener() {
                            @Override
                            public void onCreateListener(boolean isError, int createType) {
                                if (!isError&&createType==BackgroundSync.BG_SO){
                                    // write Log.
                                }
                            }

                            @Override
                            public void onFlushListener(boolean isError) {
                                // write Log.
                                if (isError) {
                                    Constants.iSAutoSync = false;
                                    LogManager.writeLogInfo("AutoSync stopped with an error while flush request");
                                    LogManager.writeLogInfo("OfflineFlush failed in background sync");
                                }else{
                                    LogManager.writeLogInfo("OfflineFlush completed successfully in background sync");

                                }
                            }

                            @Override
                            public void onRefreshListener(boolean isError) {
//                                try {
//                                    if (OfflineManager.isOfflineStoreOpen()) {
//                                        LogManager.writeLogDebug("Auto Sync DB Export");
//                                        SyncSelectionActivity.exportDB(sContext);
//                                    }
//                                } catch (Throwable e) {
//                                    e.printStackTrace();
//                                }
                                // write Log.
                                try {
                                    Constants.iSAutoSync = false;
                                    LogManager.writeLogInfo("AutoSync completed");
                                    SharedPreferences sharedPreferences = sContext.getSharedPreferences(Constants.PREFS_NAME, 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    if (sharedPreferences.contains( Constants.OUTLET_LOCKED)) {
                                        editor.remove( Constants.OUTLET_LOCKED);
                                        editor.apply();
                                    }
                                } catch (Throwable e) {
                                    Constants.iSAutoSync = false;
                                }
                            }
                        }).syncData();
                    } else {
                        LogManager.writeLogInfo(sContext.getString(R.string.auto_sync_not_perfrom_due_to_no_network));
                        Constants.iSAutoSync = false;
//                            setCallBackToUI(true, sContext.getString(R.string.no_network_conn),null);
                    }
                } else {
                    Log.d(TAG, "run: stoped started");
                    if (Constants.isSync) {
                        LogManager.writeLogInfo("Sync is in progress, AutoSync is paused");
                    }else{
                        LogManager.writeLogInfo("Background Sync is in progress, AutoSync is paused");
                    }
//                        setCallBackToUI(true, sContext.getString(R.string.alert_auto_sync_is_progress),null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogManager.writeLogInfo("AutoSync error" + e.getMessage());
//                    setCallBackToUI(true, e.getMessage(),null);
            }
        });
    }


    @Override
    public void onRequestError(int i, Exception e) {

    }

    @Override
    public void onRequestSuccess(int i, String s) throws ODataException, OfflineODataStoreException {

    }
}

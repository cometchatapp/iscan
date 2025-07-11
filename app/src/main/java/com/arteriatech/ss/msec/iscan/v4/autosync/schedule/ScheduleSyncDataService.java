package com.arteriatech.ss.msec.iscan.v4.autosync.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.arteriatech.mutils.common.Operation;
import com.arteriatech.mutils.common.UtilConstants;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.asyncTask.FlushDataAsyncTask;
import com.arteriatech.ss.msec.iscan.v4.asyncTask.RefreshAsyncTask;
import com.arteriatech.ss.msec.iscan.v4.asyncTask.SyncFromDataValtAsyncTask;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.common.MSFAApplication;
import com.arteriatech.ss.msec.iscan.v4.interfaces.MessageWithBooleanCallBack;
import com.arteriatech.ss.msec.iscan.v4.mbo.ErrorBean;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.store.OnlineManager;
import com.sap.client.odata.v4.core.GUID;
import com.sap.smp.client.odata.exception.ODataException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.arteriatech.ss.msec.iscan.v4.common.Constants.REPEATABLE_REQUEST_ID;

/**
 * Created by E10953 on 02-08-2019.
 */

public class ScheduleSyncDataService extends JobIntentService implements UIListener {
    public static final int JOB_ID = 423;
    long timestamp;
    private Handler mHandlerDifferentTrd = new Handler();
    public static String TAG = "ScheduleSynDataService";
    public static Context sContext ;
    private int penReqCount = 0;
    private int mIntPendingCollVal = 0;
    private String[][] invKeyValues = null;
    private ArrayList<String> alAssignColl = new ArrayList<>();
    private int mError = 0;
    private boolean tokenFlag = false, onlineStoreOpen = false;
    private GUID refguid =null;
    private String TAG_SO=getClass().getName();


    public static void enqueueWork(Context context, Intent work) {
        sContext = context;
        enqueueWork(context, ScheduleSyncDataService.class, JOB_ID, work);
    }

    // This describes what will happen when service is triggered
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        try {
            Log.d("DEBUG", "ScheduleSynDataService triggered");
            sContext = ScheduleSyncDataService.this;

            timestamp =  System.currentTimeMillis();
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
                            LogManager.writeLogError("Scheduler is in progress");
                        } else {
                            LogManager.writeLogInfo(sContext.getString(R.string.auto_sync_not_perfrom_due_to_no_network));
                            Constants.iSAutoSync = false;
                        }
                    } else {
                        Log.d(TAG, "run: stoped started");
                        if (Constants.isSync) {
                            LogManager.writeLogInfo("Sync is in progress, Scheduler is paused");
                        }else{
                            LogManager.writeLogInfo("Background Sync is in progress, Scheduler is paused");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogManager.writeLogInfo("Scheduler error" + e.getMessage());
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestError(int i, Exception e) {
        Constants.isBGSync=false;
        Constants.iSAutoSync = false;
        e.printStackTrace();
        ConstantsUtils.checkNetwork(sContext,null,true);
        LogManager.writeLogDebug(" So Create Failed "+ e.getLocalizedMessage());
        onError(i,e);
    }

    @Override
    public void onRequestSuccess(int i, String s) throws ODataException, OfflineODataStoreException {
        onSuccess(i,s);
    }

    private ArrayList<String> pendingCollectionList = new ArrayList<>();
    private ArrayList<String> alFlushColl = new ArrayList<>();
    private int penROReqCount = 0;
    private int pendingROVal = 0;
    private String[] tempRODevList = null;
    private String concatCollectionStr = "";

    private boolean dialogCancelled = false;

    public void onError(int operation, Exception e) {
        e.printStackTrace();
        try {
            ErrorBean errorBean = Constants.getErrorCode(operation, e, sContext);
            Constants.mBoolIsReqResAval=true;
            LogManager.writeLogError("SO AUTO SYNC : error posting SO "+e.getMessage());
            if (errorBean.hasNoError()) {
                if (!dialogCancelled && !Constants.isStoreClosed) {
                    if (operation == Operation.OfflineRefresh.getValue()) {
                        try {
                            ConstantsUtils.deletePostedSOData(sContext);
                        } catch (Throwable ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public void onSuccess(int operation, String responseBody) throws ODataException, com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException {
        Constants.iSAutoSync = false;
        try {
            if (!dialogCancelled && !Constants.isStoreClosed) {
                if (operation == Operation.Create.getValue() && pendingROVal > 0) {
                    Constants.mBoolIsReqResAval=true;
                    try {
                        if (SyncFromDataValtAsyncTask.fetchJsonHeaderObject.has(Constants.OrderNo)) {
                            LogManager.writeLogInfo("ScheduleSync Sales order created successfully for ORDER NO :"+SyncFromDataValtAsyncTask.fetchJsonHeaderObject.getString(Constants.OrderNo));
                        }
                        Constants.removeDataValtFromSharedPref(sContext, Constants.SecondarySOCreate, tempRODevList[penROReqCount], false);

                        try {
                            if (SyncFromDataValtAsyncTask.fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                                JSONObject jsonObject1 = null;
                                String orderNo="";
                                try {
                                    jsonObject1 = Constants.getJSONBody(responseBody);
                                    orderNo=jsonObject1.getString("OrderNo");

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                LogManager.writeLogInfo("ScheduleSync Sale Order No - "+orderNo+" posted successfull");
                                Constants.saveDeviceDocNoToSharedPref(sContext, Constants.SecondarySOCreate, tempRODevList[penROReqCount]);
                                SyncFromDataValtAsyncTask.fetchJsonHeaderObject.put(Constants.Status,"000000");
                                SyncFromDataValtAsyncTask.fetchJsonHeaderObject.put(Constants.OrderNo,orderNo);
                                ConstantsUtils.storeInDataVault(tempRODevList[penROReqCount], SyncFromDataValtAsyncTask.fetchJsonHeaderObject.toString(), sContext);
                                LogManager.writeLogInfo("ScheduleSync Device order no. "+tempRODevList[penROReqCount]+" updated with sale Order no. "+orderNo+" and status updated  after post ");
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    } catch (Throwable e) {
                    }
                    penROReqCount++;
                }
                if ((operation == Operation.Create.getValue()) && (penROReqCount == pendingROVal)) {
//                    Constants.iSAutoSync = false;
                    try {
                        if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                            try {
                                LogManager.writeLogInfo("Auto flush start : "+alFlushColl.toString());
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            if (UtilConstants.isNetworkAvailable(sContext)) {
                                try {
                                    new FlushDataAsyncTask(this, alFlushColl).execute();
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            } else {
                                Constants.iSAutoSync = false;
                            }
                        } else {
                                Constants.iSAutoSync = false;
                        }
                    } catch (ODataException e) {
                        e.printStackTrace();
                    }
                }else if (operation == Operation.OfflineFlush.getValue()) {
                    try {
                        LogManager.writeLogInfo("Auto refresh start : "+alAssignColl.toString());
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    if (UtilConstants.isNetworkAvailable(sContext)) {
                        onAllSync(sContext);
                    } else {
                        Constants.iSAutoSync = false;
                    }
                } else if (operation == Operation.OfflineRefresh.getValue()) {
//                    try {
//                        if (OfflineManager.isOfflineStoreOpen()) {
//                            LogManager.writeLogDebug("Schedule Sync DB Export");
//                            SyncSelectionActivity.exportDB(sContext);
//                        }
//                    } catch (Throwable e) {
//                        e.printStackTrace();
//                    }
                    try {
                        LogManager.writeLogInfo("Auto refresh ended");
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        ConstantsUtils.deletePostedSOData(sContext);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                    Constants.iSAutoSync = false;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    private void onAllSync(Context mContext) {
        String syncCollection = UtilConstants.getConcatinatinFlushCollectios(alAssignColl);
        if (!TextUtils.isEmpty(syncCollection)) {
            new RefreshAsyncTask(mContext, syncCollection, this).execute();
        } else {
            Constants.iSAutoSync=false;
        }
    }
    public static class ScheduleSyncFromDataValtAsyncTask extends AsyncTask<String, Boolean, Boolean> {
        private Context mContext;
        private UIListener uiListener;
        private JSONObject dbHeadTable;
        private ArrayList<HashMap<String, String>> arrtable;
        private String[] invKeyValues = null;
        public static JSONObject fetchJsonHeaderObject = null;
        private MessageWithBooleanCallBack dialogCallBack = null;
        private String errorMsg = "";

        public ScheduleSyncFromDataValtAsyncTask(Context context, String[] invKeyValues, UIListener uiListener,
                                                 MessageWithBooleanCallBack dialogCallBack) {
            this.mContext = context;
            this.uiListener = uiListener;
            this.invKeyValues = invKeyValues;
            this.dialogCallBack = dialogCallBack;
        }

        @Override
        protected Boolean doInBackground(String... params) {
//        boolean onlineStoreOpen = false;
            try {
                Constants.IsOnlineStoreFailed = false;
//            Constants.onlineStore = null;
//            OnlineStoreListener.instance = null;
                Constants.AL_ERROR_MSG.clear();

                Constants.ErrorCode = 0;
                Constants.ErrorNo = 0;
                Constants.ErrorName = "";
                Constants.mBoolIsReqResAval = true;
//            onlineStoreOpen = OnlineManager.openOnlineStore(mContext);

//            if (onlineStoreOpen) {
                if (invKeyValues != null) {
                    for (int k = 0; k < invKeyValues.length; k++) {
                        String store = ConstantsUtils.getFromDataVault(invKeyValues[k].toString(), mContext);
                        //Fetch object from data vault
                        while (!Constants.mBoolIsReqResAval) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        Constants.mBoolIsReqResAval = false;
                        try {
                            JSONObject fetchJsonHeaderObject = new JSONObject(store);
                            com.arteriatech.ss.msec.iscan.v4.asyncTask.SyncFromDataValtAsyncTask.fetchJsonHeaderObject = fetchJsonHeaderObject;
                            try {
                                if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                                    if (fetchJsonHeaderObject.getString(Constants.Status).equalsIgnoreCase("000000")) {
                                        Constants.mBoolIsReqResAval = true;
                                        continue;
                                    }
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            arrtable = new ArrayList<>();
                            if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                                JSONObject headerObject = Constants.getSOHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                                LogManager.writeLogError("SO AUTO SYNC : posting record Sno " + k);
                                OnlineManager.createEntity(REPEATABLE_REQUEST_ID, headerObject.toString(), Constants.SSSOs, uiListener, mContext);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            errorMsg = e.getMessage();
                        }
                    }
                }
//            } else {
//                return onlineStoreOpen;
//            }


            } catch (Exception e) {
                e.printStackTrace();
                errorMsg = e.getMessage();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!TextUtils.isEmpty(errorMsg)) {
                setCallBackToUI(false, errorMsg);
            } else if (!aBoolean) {
                setCallBackToUI(aBoolean, Constants.makeMsgReqError(Constants.ErrorNo, mContext, false));
            }

        }


        private void setCallBackToUI(boolean status, String error_Msg) {
            if (dialogCallBack != null) {
                dialogCallBack.clickedStatus(status, error_Msg, null);
            }
        }
    }
}

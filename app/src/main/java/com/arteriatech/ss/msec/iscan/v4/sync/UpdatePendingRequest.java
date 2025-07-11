package com.arteriatech.ss.msec.iscan.v4.sync;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

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
import com.arteriatech.ss.msec.iscan.v4.mutils.common.Operation;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.SalesOrderActivity;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.sap.smp.client.odata.exception.ODataException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


public class UpdatePendingRequest extends TimerTask implements UIListener, MessageWithBooleanCallBack {
    public static String TAG = "UpdatePendingRequest";
    public static UpdatePendingRequest instance = null;
    public static boolean isDataAvailable = false;
    static MessageWithBooleanCallBack dialogCallBack = null;
    private Context mContext = SalesOrderActivity.mContext;
    private String endPointURL = "";
    private String appConnID = "";
    private int penReqCount = 0;
    private int mIntPendingCollVal = 0;
    private String[][] invKeyValues = null;
    private ArrayList<String> alAssignColl = new ArrayList<>();
    private ArrayList<String> alFlushColl = new ArrayList<>();
    private Handler mHandler = new Handler();
    private Handler mHandlerDifferentTrd = new Handler();
    private int mError = 0;
    private boolean tokenFlag = false, onlineStoreOpen = false;
    private Timer timer = null;
    /*final Runnable mUpdateResults = new Runnable() {
        public void run() {
            updateResultsInUi();
        }
    };*/

    public static UpdatePendingRequest getInstance(MessageWithBooleanCallBack dialogCalls) {
        if (null == instance) {
            instance = new UpdatePendingRequest();
        }
        dialogCallBack = dialogCalls;
        return instance;
    }

    @Override
    public void run() {
        mHandlerDifferentTrd.post(new Runnable() {
            public void run() {
                try {
                    Log.d(TAG, "auto sync run: started");
                    Constants.mErrorCount = 0;
//                    if (!Constants.isDayStartSyncEnbled)
//                        LogManager.writeLogInfo(mContext.getString(R.string.auto_sync_trigger));
                    if (!Constants.isSync) {
                        Constants.isSync = false;
                        Constants.iSAutoSync = true;
                        if (UtilConstants.isNetworkAvailable(mContext)) {
                            if (!Constants.isDayStartSyncEnbled)
                                LogManager.writeLogInfo(mContext.getString(R.string.auto_sync_started));
                            ConstantsUtils.checkNetwork(mContext, isFailed -> {
                                if (isFailed) {
                                    LogManager.writeLogError("Unable to perform auto sync due to internet connectivity. please check internet connectivity");
                                    Constants.iSAutoSync=false;
                                }
                            },false);
                            Constants.mApplication = (MSFAApplication) mContext.getApplicationContext();
                            onUpdateSync(mContext, UpdatePendingRequest.this);
                        } else {
                            LogManager.writeLogInfo(mContext.getString(R.string.auto_sync_not_perfrom_due_to_no_network));
                            Constants.iSAutoSync = false;
                            Constants.mErrorCount++;
                            setCallBackToUI(true, mContext.getString(R.string.no_network_conn), null);
                        }
                    } else {
                        Log.d(TAG, "run: stoped started");
                        if (!Constants.isDayStartSyncEnbled)
                            LogManager.writeLogInfo(mContext.getString(R.string.sync_prog_auto_sync_not_perfrom));
                        Constants.mErrorCount++;
                        setCallBackToUI(true, mContext.getString(R.string.alert_auto_sync_is_progress), null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogManager.writeLogInfo("Autto Sync error" + e.getMessage());
                    Constants.mErrorCount++;
                    setCallBackToUI(true, e.getMessage(), null);
                }
            }
        });



        /*    }
        }).start();
*/
    }

    private void onUpdateSync(final Context mContext, final UIListener uiListener) {
        try {

            Set<String> set = new HashSet<>();
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
            set = sharedPreferences.getStringSet(Constants.SecondarySOCreate, null);
            if (set != null && !set.isEmpty()) {
                Iterator itr = set.iterator();
                while (itr.hasNext()) {
                    String store = null, deviceNo = "";
                    deviceNo = itr.next().toString();
                    try {
                        store = ConstantsUtils.getFromDataVault(deviceNo, mContext);
                        if (store != null) {
                            if(!TextUtils.isEmpty(store)) {
                                JSONObject fetchJsonHeaderObject = new JSONObject(store);
                                if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                                    if (!fetchJsonHeaderObject.getString(Constants.Status).equalsIgnoreCase("X") && !fetchJsonHeaderObject.getString(Constants.Status).equalsIgnoreCase("000000")) {
                                        Constants.removeDataValtFromSharedPref(mContext, Constants.SecondarySOCreate, deviceNo, false);
                                    }

                                }
                            }else {
                                Constants.removeDataValtFromSharedPref(mContext, Constants.SecondarySOCreate, deviceNo, false);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            penReqCount = 0;
            mIntPendingCollVal = 0;
          /*  ArrayList<Object> objectArrayLists = SyncSelectionActivity.getPendingInvList(mContext);
            if (!objectArrayLists.isEmpty()) {
                mIntPendingCollVal = (int) objectArrayLists.get(0);
                invKeyValues = (String[][]) objectArrayLists.get(1);
            }

            if (mIntPendingCollVal > 0) {

            } else {*/
//                mIntPendingCollVal = 0;
            invKeyValues = null;
            ArrayList<Object> objectArrayList = SyncSelectionActivity.getPendingCollList(mContext, true);
            if (!objectArrayList.isEmpty()) {
                mIntPendingCollVal = (int) objectArrayList.get(0);
                invKeyValues = (String[][]) objectArrayList.get(1);
//                    cancelSOCount=(int[]) objectArrayList.get(2);
            }

//            }
            penReqCount = 0;

            if (!OfflineManager.isOfflineStoreOpen()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            OfflineManager.openOfflineStore(mContext, new UIListener() {
                            OfflineManager.openOfflineStoreInternal(mContext, new UIListener() {
                                @Override
                                public void onRequestError(int operation, Exception exception) {
                                    ConstantsUtils.checkNetwork(mContext,null,true);
                                    ErrorBean errorBean = Constants.getErrorCode(operation, exception, mContext);

                                    Constants.iSAutoSync = false;
                                    Constants.mErrorCount++;
                                    setCallBackToUI(true, Constants.makeMsgReqError(errorBean.getErrorCode(), mContext, false), null);
                                }


                                @Override
                                public void onRequestSuccess(int i, String s) throws ODataException, OfflineODataStoreException {
                                    if (OfflineManager.isOfflineStoreOpen()) {
                                        try {
                                            OfflineManager.getAuthorizations(mContext);
                                        } catch (OfflineODataStoreException e) {
                                            e.printStackTrace();
                                        }
                                        Constants.mErrorCount = 0;
                                        Constants.iSAutoSync = false;
                                        setCallBackToUI(true, "", null);
                                    }
                                }
                            });
                        } catch (Throwable e) {
                            e.printStackTrace();
                            Constants.iSAutoSync = false;
                            LogManager.writeLogError(Constants.error_txt + e.getMessage());
                            setCallBackToUI(true, e.getMessage(), null);
                        }
                    }
                }).start();

            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postData(uiListener);
                    }
                }).start();
            }


        } catch (Exception e) {
            e.printStackTrace();
            Constants.iSAutoSync = false;
            LogManager.writeLogInfo("Autto Sync " + e.getMessage());
            setCallBackToUI(true, e.getMessage(), null);
        }
    }

    private void postData(UIListener uiListener) {
        try {
            if (OfflineManager.offlineStore.getRequestQueueIsEmpty() && mIntPendingCollVal == 0) {
//                if (mIntPendingCollVal == 0) {
                LogManager.writeLogInfo(mContext.getString(R.string.no_req_to_update_sap));
                Constants.iSAutoSync = false;
                setCallBackToUI(true, mContext.getString(R.string.no_req_to_update_sap), null);
                /*if (UtilConstants.isNetworkAvailable(mContext)) {
                    alAssignColl.addAll(Constants.getDefinigReqList(mContext));
                    onAllSync(mContext);
                } else {
                    Constants.iSAutoSync = false;
                    Constants.mErrorCount++;
                    setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync), null);
                }*/
//                }
            } else {
                alAssignColl.clear();
                alFlushColl.clear();
                ArrayList<String> allAssignColl = SyncSelectionActivity.getRefreshList();
                if (!allAssignColl.isEmpty()) {
                    alAssignColl.addAll(allAssignColl);
                    alFlushColl.addAll(allAssignColl);
                }
                if (mIntPendingCollVal > 0) {
                    if (UtilConstants.isNetworkAvailable(mContext)) {
                        onlineStoreOpen = false;
                        Constants.mBoolIsReqResAval = true;
                        Constants.mBoolIsNetWorkNotAval = false;
//                        Constants.onlineStore = null;

                        tokenFlag = false;
                        Constants.x_csrf_token = "";
                        Constants.ErrorCode = 0;
                        Constants.ErrorNo = 0;
                        Constants.ErrorName = "";
                        Constants.ErrorNo_Get_Token = 0;
                        Constants.IsOnlineStoreFailed = false;
//                        OnlineStoreListener.instance = null;
                       /* try {
                            onlineStoreOpen = OnlineManager.openOnlineStore(mContext);
                        } catch (OnlineODataStoreException e) {
                            e.printStackTrace();
                        }*/
//                        if (onlineStoreOpen) {
                            /*SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
                            if (sharedPreferences.getString(Constants.isInvoiceCreateKey, "").equalsIgnoreCase(Constants.isInvoiceTcode)) {
                                onLoadToken(mContext);
                                if (tokenFlag) {
                                    if (Constants.x_csrf_token != null && !Constants.x_csrf_token.equalsIgnoreCase("")) {
                                        try {
                                            new PostDataFromDataValt(mContext, uiListener, invKeyValues, iNetListener).execute();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Constants.iSAutoSync = false;
                                        Constants.mErrorCount++;
                                        setCallBackToUI(true, Constants.makeMsgReqError(-2, mContext, true), null);
                                    }
                                } else {
                                    Constants.iSAutoSync = false;
                                    Constants.mErrorCount++;
                                    setCallBackToUI(true, Constants.makeMsgReqError(Constants.ErrorNo_Get_Token, mContext, true), null);
                                }
                            } else {*/
                        String[] tempArr = new String[invKeyValues.length];
                        for (int i = 0; i < invKeyValues.length; i++) {
                            tempArr[i] = invKeyValues[i][0];
                        }
                        new SyncFromDataValtAsyncTask(mContext, tempArr, uiListener, UpdatePendingRequest.this).execute();
//                            }
                       /* } else {
                            Constants.iSAutoSync = false;
                            Constants.mErrorCount++;
                            setCallBackToUI(true, Constants.makeMsgReqError(Constants.ErrorNo, mContext, false), null);
                        }*/


                    } else {
                        Constants.iSAutoSync = false;
                        Constants.mErrorCount++;
                        setCallBackToUI(true, mContext.getString(R.string.no_network_conn), null);
                        LogManager.writeLogInfo(mContext.getString(R.string.no_network_conn));
                    }
                }
                //old code started
                else if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                    if (UtilConstants.isNetworkAvailable(mContext)) {
                        try {
                            new FlushDataAsyncTask(this, alFlushColl).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Constants.iSAutoSync = false;
                        Constants.mErrorCount++;
                        setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync), null);
                        LogManager.writeLogInfo(mContext.getString(R.string.data_conn_lost_during_sync));
                    }
                } else {
                    if (!UtilConstants.isNetworkAvailable(mContext)) {
                        Constants.iSAutoSync = false;
                        Constants.mErrorCount++;
                        setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync), null);
                        LogManager.writeLogInfo(mContext.getString(R.string.data_conn_lost_during_sync));
                    } else {
                        onAllSync(mContext);
                    }
                }
                //old code ended
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.mErrorCount++;
            setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync), null);
        }
    }


    public void callSchedule(String duration) {
        try {
            if (timer != null) {
                timer.cancel();
            }
            if (!TextUtils.isEmpty(duration)) {
                UpdatePendingRequest as = new UpdatePendingRequest();
                TimerTask sync = as;
                Calendar date = Calendar.getInstance();
                date.set(Calendar.MINUTE, date.get(Calendar.MINUTE) + Integer.parseInt(duration));
                timer = new Timer();
                timer.scheduleAtFixedRate(sync, date.getTime(), 1000 * 60 * Integer.parseInt(duration));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callScheduleFirstLoginSync() throws Exception {
        UpdatePendingRequest as = new UpdatePendingRequest();
        System.out.println("callScheduleFirstLoginSync------------>" + UtilConstants.getSyncHistoryddmmyyyyTime());
        TimerTask syncTime = as;
        Timer timer = new Timer();
        timer.schedule(syncTime, 100);
    }

    @Override
    public void onRequestError(int operation, Exception exception) {
        ConstantsUtils.checkNetwork(mContext,null,true);
        ErrorBean errorBean = Constants.getErrorCode(operation, exception, mContext);
        try {
            if (errorBean.hasNoError()) {
                mError++;
                penReqCount++;
                Constants.mBoolIsReqResAval = true;
                Constants.mErrorCount++;

                if ((operation == Operation.Create.getValue()) && (penReqCount == mIntPendingCollVal)) {
                    //new code
                    final String mErrorMsg = "";
                    Constants.iSAutoSync = false;
                    setErrorUI(mErrorMsg, errorBean);
                    //old code
                   /* try {
                        if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                            if (UtilConstants.isNetworkAvailable(mContext)) {
                                try {
                                    new FlushDataAsyncTask(this, alFlushColl).execute();
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            } else {
                                Constants.iSAutoSync = false;
                                Constants.mErrorCount++;
                                setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync), null);
                            }
                        } else {
                            if (UtilConstants.isNetworkAvailable(mContext)) {
                                alAssignColl.clear();
                                alAssignColl.addAll(Constants.getDefinigReqList(mContext));
                                onAllSync(mContext);
                            } else {
                                Constants.iSAutoSync = false;
                                Constants.mErrorCount++;
                                setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync), null);
                            }
                        }
                    } catch (ODataException e3) {
                        e3.printStackTrace();
                    }*/

                }

                if (operation == Operation.OfflineFlush.getValue()) {
                    Constants.iSAutoSync = false;
                    Constants.mErrorCount++;
                    final String mErrorMsg = "";
                    setErrorUI(mErrorMsg, errorBean);
                    //Old code
                   /* if (UtilConstants.isNetworkAvailable(mContext)) {
                        alAssignColl.clear();
                        alAssignColl.addAll(Constants.getDefinigReqList(mContext));
                        onAllSync(mContext);
                    } else {
                        Constants.iSAutoSync = false;
                        Constants.mErrorCount++;
                        setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync), null);
                    }*/
                } else if (operation == Operation.OfflineRefresh.getValue()) {
                    Constants.iSAutoSync = false;
                    Constants.mErrorCount++;
                    final String mErrorMsg = "";
                    setErrorUI(mErrorMsg, errorBean);
                    if (alAssignColl.contains(Constants.MerchReviews)) {
                        Constants.deleteDeviceMerchansisingFromDataVault(mContext);
                    }
                   /* try {
                        new SyncMustSellAsyncTask(mContext, new MessageWithBooleanCallBack() {
                            @Override
                            public void clickedStatus(boolean clickedStatus, String errorMsg, ErrorBean errorBean) {
                                Log.d("clickedStatus Req", clickedStatus+"");

                            }
                        }, Constants.All).execute();
                    } catch (Exception e) {
                        setErrorUI(mErrorMsg,errorBean);
                        e.printStackTrace();
                    }*/


                }
            } else {
                Constants.mBoolIsNetWorkNotAval = true;
                Constants.mBoolIsReqResAval = true;
                if (Constants.iSAutoSync) {
                    Constants.iSAutoSync = false;
                }
                Constants.mErrorCount++;

                if (errorBean.isStoreFailed()) {
                    OfflineManager.offlineStore = null;
                    OfflineManager.options = null;
                    openStore(errorBean);
                } else {
                    setCallBackToUI(true, Constants.makeMsgReqError(errorBean.getErrorCode(), mContext, false), errorBean);
                }

            }
        } catch (Exception e) {
            Constants.mBoolIsNetWorkNotAval = true;
            Constants.mBoolIsReqResAval = true;
            if (Constants.iSAutoSync) {
                Constants.iSAutoSync = false;
            }
            Constants.mErrorCount++;
            setCallBackToUI(true, Constants.makeMsgReqError(errorBean.getErrorCode(), mContext, false), null);
        }
    }

    private void setErrorUI(String mErrorMsg, ErrorBean errorBean) {
        if (Constants.AL_ERROR_MSG.size() > 0) {
            mErrorMsg = Constants.convertALBussinessMsgToString(Constants.AL_ERROR_MSG);
        }
        if (mErrorMsg.equalsIgnoreCase("")) {
            setCallBackToUI(true, errorBean.getErrorMsg(), null);
        } else {
            setCallBackToUI(true, mErrorMsg, null);
        }
    }

    private void openStore(final ErrorBean errorBean) {
        if (!OfflineManager.isOfflineStoreOpen()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
//                        OfflineManager.openOfflineStore(mContext, new UIListener() {
                        OfflineManager.openOfflineStoreInternal(mContext, new UIListener() {
                            @Override
                            public void onRequestError(int operation, Exception exception) {
                                ConstantsUtils.checkNetwork(mContext,null,true);
                                ErrorBean errorBean = Constants.getErrorCode(operation, exception, mContext);

                                Constants.iSAutoSync = false;
                                Constants.mErrorCount++;
                                setCallBackToUI(true, Constants.makeMsgReqError(errorBean.getErrorCode(), mContext, false), null);
                            }


                            @Override
                            public void onRequestSuccess(int i, String s) throws ODataException, OfflineODataStoreException {
                                if (OfflineManager.isOfflineStoreOpen()) {
                                    try {
                                        OfflineManager.getAuthorizations(mContext);
                                    } catch (OfflineODataStoreException e) {
                                        e.printStackTrace();
                                    }
                                    Constants.mErrorCount = 0;
                                    setCallBackToUI(true, "", null);
                                }
                            }
                        });
                    } catch (Throwable e) {
                        setCallBackToUI(true, Constants.makeMsgReqError(errorBean.getErrorCode(), mContext, false), errorBean);
                        LogManager.writeLogError(Constants.error_txt + e.getMessage());
                    }
                }
            }).start();

        } else {
            setCallBackToUI(true, Constants.makeMsgReqError(errorBean.getErrorCode(), mContext, false), errorBean);
        }
    }

    @Override
    public void onRequestSuccess(int operation, String s) throws ODataException, OfflineODataStoreException {
        Log.d(TAG, "onRequestSuccess: ");
        if (operation == Operation.Create.getValue() && mIntPendingCollVal > 0) {
            Constants.mBoolIsReqResAval = true;
            alAssignColl.addAll(Constants.removeDataValtFromSharedPref(mContext, invKeyValues[penReqCount][1], invKeyValues[penReqCount][0], false));
            try {
                if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.SecondarySOCreate)) {
                    JSONObject jsonObject1 = null;
                    String orderNo="";
                    try {
                        jsonObject1 = Constants.getJSONBody(s);
                        orderNo=jsonObject1.getString("OrderNo");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LogManager.writeLogInfo("Sale Order No - "+orderNo+" posted successfull");
                    Constants.saveDeviceDocNoToSharedPref(mContext, Constants.SecondarySOCreate, invKeyValues[penReqCount][0]);
                    SyncFromDataValtAsyncTask.fetchJsonHeaderObject.put(Constants.Status,"000000");
                    SyncFromDataValtAsyncTask.fetchJsonHeaderObject.put(Constants.OrderNo,orderNo);
                    ConstantsUtils.storeInDataVault(invKeyValues[penReqCount][0], SyncFromDataValtAsyncTask.fetchJsonHeaderObject.toString(), mContext);
                    LogManager.writeLogInfo("Device order no. "+invKeyValues[penReqCount][0]+" updated with sale Order no. "+orderNo+" and status updated  after post ");
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            /*if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.FinancialPostings)) {
                Constants.removeDeviceDocNoFromSharedPref(mContext, Constants.FinancialPostings, invKeyValues[penReqCount][0]);
            } else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.SecondarySOCreate)) {
                Constants.removeDeviceDocNoFromSharedPref(mContext, Constants.SecondarySOCreate, invKeyValues[penReqCount][0]);
            } else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.Feedbacks)) {
                Constants.removeDeviceDocNoFromSharedPref(mContext, Constants.Feedbacks, invKeyValues[penReqCount][0]);
            }*/
//            UtilDataVault.storeInDataVault(invKeyValues[penReqCount][0], "");
//            ConstantsUtils.storeInDataVault(invKeyValues[penReqCount][0], "", mContext);
            penReqCount++;
        }
        if ((operation == Operation.Create.getValue()) && (penReqCount == mIntPendingCollVal)) {

            //old code
            try {
                if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                    if (UtilConstants.isNetworkAvailable(mContext)) {
                        try {
                            new FlushDataAsyncTask(this, alFlushColl).execute();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    } else {
//                        setUI();
                        //Old code
//                        Constants.iSAutoSync = false;
                        setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync), null);
                    }
                } else {
//                    setUI();
                    //Old code
                    if (UtilConstants.isNetworkAvailable(mContext)) {
//                        alAssignColl.clear();
//                        alAssignColl.addAll(Constants.getDefinigReqList(mContext));
                        onAllSync(mContext);
                    } else {
                        setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync), null);
                    }
                }

            } catch (ODataException e) {
                e.printStackTrace();
            }

        } else if (operation == Operation.OfflineFlush.getValue()) {
//            setUI();
            //old code
            if (UtilConstants.isNetworkAvailable(mContext)) {
//                alAssignColl.clear();
//                alAssignColl.addAll(Constants.getDefinigReqList(mContext));
                onAllSync(mContext);
            } else {
                setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync), null);
            }
        } else if (operation == Operation.OfflineRefresh.getValue()) {
            ConstantsUtils.checkNetwork(mContext,null,true);
            try {
                OfflineManager.getAuthorizations(mContext);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            try {
                ConstantsUtils.deletePostedSOData(mContext);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            Constants.setBirthdayListToDataValut(mContext);
            Constants.updateLastSyncTimeToTable(mContext, alAssignColl);
//                Constants.deleteDeviceMerchansisingFromDataVault(mContext);
//            setUI();
            setCallBackToUI(true, "", null);
//                Constants.setAppointmentNotification(mContext);
//                if(alAssignColl.contains(Constants.RoutePlans) || alAssignColl.contains(Constants.ChannelPartners) || alAssignColl.contains(Constants.Visits)) {
//                    Constants.alTodayBeatRet.clear();
//                    Constants.TodayTargetRetailersCount = Constants.getVisitTargetForToday();
//                    Constants.TodayActualVisitRetailersCount = Constants.getVisitedRetailerCount(Constants.alTodayBeatRet);
//                }
//                if(alAssignColl.contains(Constants.SSSOs) || alAssignColl.contains(Constants.Targets)) {
//                    Constants.loadingTodayAchived(mContext,Constants.alTodayBeatRet);
//                }

               /* try {
                    new SyncMustSellAsyncTask(mContext, new MessageWithBooleanCallBack() {
                        @Override
                        public void clickedStatus(boolean clickedStatus, String errorMsg, ErrorBean errorBean) {
                            Log.d("clickedStatus Req", clickedStatus+"");
                            setUI();
                        }
                    }, Constants.All).execute();
                } catch (Exception e) {
                    setUI();
                    e.printStackTrace();
                }*/
                /*Constants.iSAutoSync = false;

                String mErrorMsg = "";
                if (Constants.AL_ERROR_MSG.size() > 0) {
                    mErrorMsg = Constants.convertALBussinessMsgToString(Constants.AL_ERROR_MSG);
                }

                if (mErrorMsg.equalsIgnoreCase("")) {
                    setCallBackToUI(true, mContext.getString(R.string.error_occured_during_post),null);
                } else {
                    setCallBackToUI(true, mErrorMsg,null);
                }*/

        }
    }

    private void setUI() {
        Constants.iSAutoSync = false;

        String mErrorMsg = "";
        if (Constants.AL_ERROR_MSG.size() > 0) {
            mErrorMsg = Constants.convertALBussinessMsgToString(Constants.AL_ERROR_MSG);
        }

        if (mErrorMsg.equalsIgnoreCase("")) {
            setCallBackToUI(true, mContext.getString(R.string.error_occured_during_post), null);
        } else {
            setCallBackToUI(true, mErrorMsg, null);
        }
    }

   /* @Override
    public void onSuccess(IRequest iRequest, IResponse iResponse) {
        Log.d(TAG, "onSuccess: ");
        try {
            Constants.mBoolIsReqResAval = true;
            if (mIntPendingCollVal > 0) {

                if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.CollList)) {
                    Constants.removeDeviceDocNoFromSharedPref(mContext, Constants.CollList, invKeyValues[penReqCount][0]);
                } else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.SOList)) {
                    Constants.removeDeviceDocNoFromSharedPref(mContext, Constants.SOList, invKeyValues[penReqCount][0]);
                } else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.FeedbackList)) {
                    Constants.removeDeviceDocNoFromSharedPref(mContext, Constants.FeedbackList, invKeyValues[penReqCount][0]);
                } else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.InvList)) {
                    Constants.removeDeviceDocNoFromSharedPref(mContext, Constants.InvList, invKeyValues[penReqCount][0]);
                } else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.ROList)) {
                    Constants.removeDeviceDocNoFromSharedPref(mContext, Constants.ROList, invKeyValues[penReqCount][0]);
                } else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.SampleDisbursement)) {
                    Constants.removeDeviceDocNoFromSharedPref(mContext, Constants.SampleDisbursement, invKeyValues[penReqCount][0]);
                } else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.Expenses)) {
                    Constants.removeDeviceDocNoFromSharedPref(mContext, Constants.Expenses, invKeyValues[penReqCount][0]);
                }

                UtilDataVault.storeInDataVault(invKeyValues[penReqCount][0], "");

                penReqCount++;
            }

            //ignore this sections for hard codes (parsing xml request for invoice creation)
            String repData = EntityUtils.toString(iResponse.getEntity());

            int repStInd = repData.toString().indexOf("<d:InvoiceNo>") + 13;
            int repEndInd = repData.toString().indexOf("</d:InvoiceNo>");
            String invNo = repData.substring(repStInd, repEndInd);

            String popUpText = "Retailer invoice # " + invNo + " created successfully.";

            LogManager.writeLogInfo(popUpText);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (penReqCount == mIntPendingCollVal) {
            mHandler.post(mUpdateResults);
        }
    }

    @Override
    public void onError(IRequest iRequest, IResponse iResponse, IRequestStateElement iReqStateElement) {
        final int errorcode = iReqStateElement.getErrorCode();
        if (IRequestStateElement.AUTHENTICATION_ERROR != errorcode && IRequestStateElement.NETWORK_ERROR != errorcode) {
            mError++;
            penReqCount++;
            Constants.mBoolIsReqResAval = true;

            Constants.mErrorCount++;
            try {
                Constants.parser = Constants.mApplication.getParser();

                IODataError errResponse = null;
                HttpResponse response = iResponse;
                if (iResponse != null) {
                    try {
                        HttpEntity responseEntity = iResponse.getEntity();
                        String responseString = EntityUtils.toString(responseEntity);
                        responseString = responseString.replace(mContext.getString(R.string.Bad_Request), "");

                        errResponse = Constants.parser.parseODataError(responseString);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();

                    } catch (IllegalStateException e) {
                        e.printStackTrace();

                    } catch (ParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
                String errorMsg = errResponse.getMessage() != null ? errResponse.getMessage() : "";

                LogManager.writeLogError(mContext.getString(R.string.Error_in_Retailer_invoice) + errorMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (penReqCount == mIntPendingCollVal) {
                mHandler.post(mUpdateResults);
            }
        } else {
            if (iReqStateElement.getErrorCode() == 4) {
                LogManager.writeLogError(Constants.Error + " :" + mContext.getString(R.string.auth_fail_plz_contact_admin, iReqStateElement.getErrorCode() + ""));
            } else if (iReqStateElement.getErrorCode() == 3) {
                LogManager.writeLogError(Constants.Error + " :" + mContext.getString(R.string.data_conn_lost_during_sync_error_code, iReqStateElement.getErrorCode() + ""));
            } else {
                LogManager.writeLogError(Constants.Error + " :" + mContext.getString(R.string.data_conn_lost_during_sync_error_code, iReqStateElement.getErrorCode() + ""));
            }
            Constants.mBoolIsNetWorkNotAval = true;
            Constants.mBoolIsReqResAval = true;
            if (Constants.iSAutoSync) {
                Constants.iSAutoSync = false;
            }
            setCallBackToUI(true, Constants.makeMsgReqError(errorcode, mContext, true),null);

        }


    }*/

  /*  protected void updateResultsInUi() {

        if (mError == 0) {

            if (!UtilConstants.isNetworkAvailable(mContext)) {

                LogManager.writeLogInfo(mContext.getString(R.string.data_conn_lost_during_sync));
                Constants.iSAutoSync = false;
                setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync),null);

            } else {

                mIntPendingCollVal = 0;
                invKeyValues = null;
                ArrayList<Object> objectArrayList = SyncSelectionActivity.getPendingCollList(mContext);
                if (!objectArrayList.isEmpty()) {
                    mIntPendingCollVal = (int) objectArrayList.get(0);
                    invKeyValues = (String[][]) objectArrayList.get(1);
                }
                penReqCount = 0;
                Constants.mBoolIsReqResAval = true;
                Constants.mBoolIsNetWorkNotAval = false;
                if (mIntPendingCollVal > 0) {
                    if (UtilConstants.isNetworkAvailable(mContext)) {
                        try {
                            new PostDataFromDataValt(mContext, this, invKeyValues, this).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (Constants.iSAutoSync) {
                            Constants.iSAutoSync = false;
                        }
                        setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync),null);
                    }
                } else {

                    try {
                        if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                            if (UtilConstants.isNetworkAvailable(mContext)) {
                                try {
                                    new FlushDataAsyncTask(this, alFlushColl).execute();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (Constants.iSAutoSync) {
                                    Constants.iSAutoSync = false;
                                }
                                setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync),null);
                            }
                        } else {
                            if (UtilConstants.isNetworkAvailable(mContext)) {
                                alAssignColl.clear();
                                alAssignColl.addAll(Constants.getDefinigReqList(mContext));
                                onAllSync(mContext);
                            } else {
                                if (Constants.iSAutoSync) {
                                    Constants.iSAutoSync = false;
                                }
                                setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync),null);
                            }
                        }

                    } catch (ODataException e) {
                        e.printStackTrace();
                    }
                }
            }

        } else {

            try {
                if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                    if (UtilConstants.isNetworkAvailable(mContext)) {
                        try {
                            new FlushDataAsyncTask(this, alFlushColl).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (Constants.iSAutoSync) {
                            Constants.iSAutoSync = false;
                        }
                        setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync),null);
                    }
                } else {
                    if (UtilConstants.isNetworkAvailable(mContext)) {
                        alAssignColl.clear();
                        alAssignColl.addAll(Constants.getDefinigReqList(mContext));
                        onAllSync(mContext);
                    } else {
                        if (Constants.iSAutoSync) {
                            Constants.iSAutoSync = false;
                        }
                        setCallBackToUI(true, mContext.getString(R.string.data_conn_lost_during_sync),null);
                    }
                }

            } catch (ODataException e) {
                e.printStackTrace();
            }

        }
    }*/

    private void onAllSync(Context mContext) {
        String syncCollection = UtilConstants.getConcatinatinFlushCollectios(alAssignColl);
        if (!TextUtils.isEmpty(syncCollection)) {
            new RefreshAsyncTask(mContext, syncCollection, this).execute();
        } else {
            setCallBackToUI(true, "No offline data to post", null);
        }
    }

    private void setCallBackToUI(final boolean status, final String error_Msg, final ErrorBean errorBean) {
        Constants.iSAutoSync = false;
        if (mContext != null && mContext instanceof Activity) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialogCallBack != null) {
                        dialogCallBack.clickedStatus(status, error_Msg, errorBean);
                    } else {
                        if (!Constants.isDayStartSyncEnbled)
                            LogManager.writeLogInfo(mContext.getString(R.string.auto_sync_end));
                    }
                }
            });
        }

    }

    @Override
    public void clickedStatus(boolean clickedStatus, String errorMsg, ErrorBean errorBean) {
        setCallBackToUI(clickedStatus, errorMsg, errorBean);
    }

    /*public void onLoadToken(final Context context) {
        String endPointURL = "";
        String appConnID = "";
        try {
            // get Application Connection ID
            LogonCoreContext lgCtx = LogonCore.getInstance().getLogonContext();
            endPointURL = lgCtx.getAppEndPointUrl();
            appConnID = LogonCore.getInstance().getLogonContext()
                    .getConnId();
        } catch (LogonCoreException e) {
            e.printStackTrace();
        }

        String PushUrl = endPointURL + "/?sap-language=en";
        IRequest req = new BaseRequest();
        req.setRequestMethod(IRequest.REQUEST_METHOD_GET);
        req.setPriority(1);
        req.setRequestUrl(PushUrl);
        Hashtable headers = new Hashtable();
        headers.put("X-SMP-APPCID", appConnID);
        ((BaseRequest) req).setHeaders(headers);
        req.setListener(new INetListener() {
            @Override
            public void onSuccess(IRequest iRequest, IResponse iResponse) {
                try {
                    tokenFlag = true;
                    isDataAvailable = true;
                    Constants.x_csrf_token = iResponse.getHeaders("X-CSRF-Token")[0].getValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(IRequest iRequst, IResponse iResponse, IRequestStateElement iReqStateElement) {
                Constants.ErrorNo_Get_Token = iReqStateElement.getErrorCode();
                try {
                    isDataAvailable = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (iReqStateElement.getErrorCode() == 4) {
                    LogManager.writeLogError(Constants.Error + " :" + context.getString(R.string.auth_fail_plz_contact_admin, iReqStateElement.getErrorCode() + ""));
                } else if (iReqStateElement.getErrorCode() == 3) {
                    LogManager.writeLogError(Constants.Error + " :" + context.getString(R.string.data_conn_lost_during_sync_error_code, iReqStateElement.getErrorCode() + ""));
                } else {
                    LogManager.writeLogError(Constants.Error + " :" + context.getString(R.string.data_conn_lost_during_sync_error_code, iReqStateElement.getErrorCode() + ""));
                }
            }
        });
        IRequestManager mRequestmanager = null;
        mRequestmanager = Constants.mApplication.getRequestManager();
        mRequestmanager.makeRequest(req);
        while (!isDataAvailable) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isDataAvailable = false;

    }*/
}

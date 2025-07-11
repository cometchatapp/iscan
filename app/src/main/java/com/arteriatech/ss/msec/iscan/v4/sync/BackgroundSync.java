package com.arteriatech.ss.msec.iscan.v4.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.common.MSFAApplication;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.store.OnlineManager;
import com.sap.smp.client.odata.exception.ODataException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.arteriatech.ss.msec.iscan.v4.common.Constants.REPEATABLE_REQUEST_ID;

@SuppressWarnings("all")
public class BackgroundSync{
    public static final int BG_SO=1001;
    public static final int BG_CP=1002;

    private Context context;
    private String TAG_SO="Order Entry:";
    private List<String> refreshSyncList = new ArrayList<>();
    public IBGSyncListener listener;
    private boolean isRefresh,isPostInProgress;

    public BackgroundSync() {
    }

    public BackgroundSync(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public BackgroundSync init(Context context, IBGSyncListener listener) {
        this.context = context;
        this.listener = listener;
        this.isPostInProgress=false;
        return this;
    }

    public void syncData(){
        Constants.isBGSync =true;

        Set<String> set = new HashSet<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        set = sharedPreferences.getStringSet(Constants.SecondarySOCreate, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, context);
                    if (store != null) {
                        if(!TextUtils.isEmpty(store)){
                            JSONObject fetchJsonHeaderObject = new JSONObject(store);
                            if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                                if(!fetchJsonHeaderObject.getString(Constants.Status).equalsIgnoreCase("X")) {
                                    Constants.removeDataValtFromSharedPref(context, Constants.SecondarySOCreate, deviceNo, false);
                                }

                            }
                        }else {
                            Constants.removeDataValtFromSharedPref(context, Constants.SecondarySOCreate, deviceNo, false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        fetchPendingList();
        ConstantsUtils.checkNetwork(context, isFailed -> {
            if (isFailed) {
                LogManager.writeLogError("Unable to perform background sync due to internet connectivity. please check internet connectivity");
                Constants.isBGSync=false;
            }
        },false);
    }

    /**
     * Fetching all pending data from DataVault.
     */
    private void fetchPendingList(){
        try {
            Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                    "log - Bacground sync called fetching pending list in datavault ");

            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            Set<String> set = new HashSet<>();

            if (sharedPreferences.getStringSet(Constants.SecondarySOCreate, null)!=null){
                set.addAll(sharedPreferences.getStringSet(Constants.SecondarySOCreate, null));
            }if (sharedPreferences.getStringSet(Constants.CPList, null)!=null){
                set.addAll(sharedPreferences.getStringSet(Constants.CPList, null));
            }
            Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                    "log - Bacground sync called fetching data from sharedpreference ");
            if (set!=null&&!set.isEmpty()) {
                Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                        "log - Bacground sync called posting datavault data ");
                postVaultData(set);
            }else{
                Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                        "log - Bacground sync called no datavault data available posting offlinestore data ");
                postOfflineData();
            }
        } catch (Throwable e) {
            Constants.isBGSync =false;
            LogManager.writeLogError("Error in Background Sync \n"+e.toString());
        }
    }

    /**
     * Posting the DataVault data to server with reference of stored set.
     * @param set
     */
    private void  postVaultData(Set<String>set){
      Thread thread = new Thread(() -> {
          try {
              isPostInProgress = true;
              for (String value :set) {
                  try {
                      while (!isPostInProgress){
                          try {
                              Thread.sleep(1000);
                          } catch (Throwable e) {
                              e.printStackTrace();
                          }
                      }
                      isPostInProgress =false;
                      String store = ConstantsUtils.getFromDataVault(value, context);
                      if (store!=null&&!TextUtils.isEmpty(store)) {
                          JSONObject jsonObject = new JSONObject(store);
                          /**
                           * This condition executes for SalesOrder Create with own UIListener to ensure the data safety..
                           */
                          if (jsonObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                              refreshSyncList.addAll(SyncUtils.getSOsCollection());
                              refreshSyncList.addAll(SyncUtils.getValueHelps());
                              postOrderEntryData(value,jsonObject);
                              Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                                      "log - Bacground sync called \n posting order entry data");
                          }else if (jsonObject.getString(Constants.entityType).equalsIgnoreCase(Constants.ChannelPartners)) {
                              refreshSyncList.addAll(SyncUtils.getValueHelps());
                              refreshSyncList.addAll(SyncUtils.getRetailer());
                              refreshSyncList.addAll(SyncUtils.getBeat(context));
                              postChannelPartnerData(value,jsonObject);
                          }
                      }
                  } catch (Throwable e) {
                      isPostInProgress =true;
                      Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                              "log - Bacground sync called \n error while posting datavault data \n"+e.toString());
                  }
              }
          } catch (Throwable e) {
              Constants.isBGSync =false;
          }
          postOfflineData();
        });
        thread.setName("BACKGROUND_SYNC");
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

    }

    /**
     * Post Sales Order from DataVault.
     * @param docNo reference Number
     * @param jsonObject Object has Data
     */
    private void postOrderEntryData(String docNo,JSONObject jsonObject){
        try {
            JSONObject headerObject = Constants.getSOHeaderValuesFromJsonObject(jsonObject);
            OnlineManager.createEntity(REPEATABLE_REQUEST_ID, headerObject.toString(), Constants.SSSOs, new UIListener() {
                @Override
                public void onRequestError(int operation, Exception exception) {
                    Constants.isBGSync =false;
                    ConstantsUtils.checkNetwork(context,null,true);
                    isPostInProgress =true;
                    writeErrorLogSO(exception);
                    if (listener!=null)listener.onCreateListener(true,BG_SO);
                }

                @Override
                public void onRequestSuccess(int operation, String key) {
                    isPostInProgress =true;
                    writeInfoLogSO(docNo,jsonObject,key);
                    if (listener!=null)listener.onCreateListener(false,BG_SO);
                }
            }, context);
        } catch (Throwable e) {
            isPostInProgress =true;
            Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                    "log - Bacground sync called  postOrderEntryData method triggered \n error while posting datavault data \n"+e.toString());
        }
    }
    /**
     * Post ChannelPartner from DataVault.
     * @param docNo reference Number
     * @param jsonObject Object has Data
     */
    private void postChannelPartnerData(String docNo,JSONObject jsonObject){
        try {
            JSONObject headerObject = Constants.getCPHeaderValuesFromJsonObject(jsonObject,context);
            OnlineManager.createEntity(REPEATABLE_REQUEST_ID, headerObject.toString(), Constants.ChannelPartners, new UIListener() {
                @Override
                public void onRequestError(int operation, Exception e) {
                    ConstantsUtils.checkNetwork(context,null,true);
                    Constants.isBGSync =false;
                    isPostInProgress =true;
                    writeErrorLogCP(e);
                    if (listener!=null)listener.onCreateListener(true,BG_CP);
                }

                @Override
                public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                    isPostInProgress =true;
                    writeInfoLogCP(docNo,jsonObject);
                    if (listener!=null)listener.onCreateListener(false,BG_CP);
                }
            }, context);
        } catch (Throwable e) {
            isPostInProgress =true;
        }
    }


//-------------------------------------------------------------------------------------------------Post Offline and Refresh Section---------------------------------------------
    /**
     * Posting all offline data/ flushing the RQ DB to server.
     */
    private void postOfflineData(){
        try {
            if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                try {
                    OfflineManager.flushQueuedRequests(new UIListener() {
                        @Override
                        public void onRequestError(int operation, Exception e) {
                            ConstantsUtils.checkNetwork(context,null,true);
                            writeErrorFlushLog(e);
                            Constants.isBGSync =false;
                            if (listener!=null)listener.onFlushListener(true);
                        }
                        @Override
                        public void onRequestSuccess(int operation, String key) {
                            writeInfoFlushLog();
                            if (listener!=null)listener.onFlushListener(false);
                            /**
                             * on flushing Data from request-Q Data refresh to be done with required collections.
                             */
                            if (isRefresh) {
                                Set<String>set= new HashSet<>(refreshSyncList);
                                refreshSyncList.clear();
                                refreshSyncList.addAll(set);
                                refreshCollections(refreshSyncList);
                            }else{
                                ArrayList<String>hashSet = new ArrayList<>(SyncUtils.getAllSyncValue(context));
                                refreshCollections(hashSet);
                            }
                        }
                    },getCollections(refreshSyncList));
                } catch (Throwable e) {
                    Constants.isBGSync =false;
                    Log.e(TAG_SO,e.toString());
                    Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                            "log - Bacground sync called  postOfflineData method triggered \n error while postOfflineData data \n"+e.toString());
                }
            }else{
                if (!isRefresh) {
                    ArrayList<String>hashSet = new ArrayList<>(SyncUtils.getAllSyncValue(context));
                    refreshCollections(hashSet);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Constants.isBGSync =false;
            Log.e(TAG_SO,e.toString());
            Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                    "log - Bacground sync called  postOfflineData method triggered \n error while  postOfflineData data \n"+e.toString());
        }

    }

    /**
     * Refreshing Offline Store DB with the updated records
     * @param list refresh list to be passed to get Records.
     */
    private void refreshCollections(List<String>list){
        if (!list.isEmpty()) {
            try {
                OfflineManager.refreshStoreSync(context, new UIListener() {
                    @Override
                    public void onRequestError(int operation, Exception e) {
                        ConstantsUtils.checkNetwork(context,null,true);
                        writeErrorRefreshLog(e);
                        Constants.isBGSync =false;
                        if (listener!=null)listener.onRefreshListener(true);
                    }

                    @Override
                    public void onRequestSuccess(int operation, String key) {
                        ConstantsUtils.checkNetwork(context,null,true);
                        writeInfoRefreshLog(list);
                        Constants.isBGSync =false;
                        try {
                            ConstantsUtils.deletePostedSOData(context);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        Constants.updateLastSyncTimeToTable(context, (ArrayList<String>) list);
                        listener.onRefreshListener(false);
                    }
                }, Constants.Fresh, getCollections(list));
            } catch (Throwable e) {
                Constants.isBGSync =false;
                LogManager.writeLogError(Constants.error_txt + e.getMessage());
                Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                        "log - Bacground sync called  refreshCollections method triggered \n error while refreshing data \n"+e.toString());
            }
        }
    }

    /**
     * which will give the query collections to be refreshed.
     */
    private String getCollections(List<String>list){
        String query="";
        try {
            for (int i = 0; i <list.size(); i++) {
                if (i!=list.size()-1) {
                    query =query.concat(list.get(i)+",");
                }else{
                    query =query.concat(list.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                    "log - Bacground sync called \n getCollections called with error \n"+e.toString());
        }
        return query;
    }

//----------------------------------------------------------------- Write Logs as well as Remove Records from DataValut.------------------
    /**
     * error log will be printed in system as well as log Activity.
     * @param exception exception is used find the message and code
     */
    private void writeErrorLogSO(Exception exception){
        try {
            String strErrorMsg=exception.getMessage();
            if (strErrorMsg.contains("invalid authentication")) {
                Log.e(TAG_SO,"401 invalid authentication "+strErrorMsg);
                LogManager.writeLogError("401 Order entry :"+exception.getMessage());
            } else {
                Log.e(TAG_SO, "Order entry :" + exception.getMessage());
                LogManager.writeLogError("Order entry :" + exception.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                    "log - Bacground sync called \n writeErrorLogSO method called with error \n"+exception.toString());
        }
        Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                "log - Bacground sync called \n posting order entry data with error \n"+exception.toString());
    }

    /**
     * used to print info log for sales order.
     * @param value reference object used for delecting the record in DataVault.
     * @param jsonObject used to print the order No.
     */
    private void writeInfoLogSO(String value,JSONObject jsonObject,String responseBody){
        try {
            if (jsonObject.has(Constants.OrderNo)) {
                LogManager.writeLogInfo(" Sales order created successfully for ORDER NO :"+jsonObject.getString(Constants.OrderNo));
            }
            Constants.removeDataValtFromSharedPref(context, Constants.SecondarySOCreate, value, false);

            try {
                if (jsonObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                    JSONObject jsonObject1 = null;
                    String orderNo="";
                    try {
                        jsonObject1 = Constants.getJSONBody(responseBody);
                        orderNo=jsonObject1.getString("OrderNo");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LogManager.writeLogInfo("Sale Order No - "+orderNo+" posted successfull");
                    Constants.saveDeviceDocNoToSharedPref(context, Constants.SecondarySOCreate, value);
                    jsonObject.put(Constants.Status,"000000");
                    jsonObject.put(Constants.OrderNo,orderNo);
                    ConstantsUtils.storeInDataVault(value, jsonObject.toString(), context);
                    LogManager.writeLogInfo("Device order no. "+value+" updated with sale Order no. "+orderNo+" and status updated  after post ");
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                    "log - Bacground sync called \n removing data vault data after posting order entry");
        } catch (Throwable e) {
            Constants.isBGSync =false;
            Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                    "log - Bacground sync called \n posting order entry data with error \n"+e.toString());
        }
    }

    /**
     * error log will be printed in system as well as log Activity.
     * @param exception exception is used find the message and code
     */
    private void writeErrorLogCP(Exception exception){
        String strErrorMsg=exception.toString();
        if (strErrorMsg.contains("invalid authentication")) {
            Log.e(TAG_SO,"401 invalid authentication "+strErrorMsg);
            LogManager.writeLogError("401 Order entry :",exception);
        } else {
            Log.e(TAG_SO, "Channel Partner :" + exception.toString());
            LogManager.writeLogError("Channel Partner :" + exception.toString());
        }
    }

    /**
     * used to print info log for CP Create.
     * @param value reference object used for delecting the record in DataVault.
     * @param jsonObject used to print the CP No.
     */
    private void writeInfoLogCP(String value,JSONObject jsonObject){
        try {
            if (jsonObject.has(Constants.CPNo)) {
                LogManager.writeLogInfo(" ChannelParnter created successfully :"+jsonObject.getString(Constants.CPNo));
            }
            Constants.removeDataValtFromSharedPref(context, Constants.CPList, value, false);
        } catch (Throwable e) {
            Constants.isBGSync =false;
        }
    }

    //---------------------------------------------------------------------------------------------Writee Only Log Section----------------------------------
    /**
     * write the Flush Error Log
     * @param exception
     */
    private void writeErrorFlushLog(Exception exception){
        Log.e(TAG_SO, "OfflineFlush :"+exception.toString());
        LogManager.writeLogError("OfflineFlush :"+exception.toString());
        Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                "log - Bacground sync called \n Offlline flush called with error\n"+exception.toString());
    }

    /**
     * Writes Flush successLog.
     */
    private void writeInfoFlushLog(){
        LogManager.writeLogInfo("OfflineFlush : Completed successfully");
    }

    /**
     * Writes the Refresh Sync Error Log.
     * @param exception
     */
    private void writeErrorRefreshLog(Exception exception){
        Log.e(TAG_SO, "OfflineRefresh :"+exception.toString());
        LogManager.writeLogError("OfflineRefresh :"+exception.toString());
        Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
                "log - Bacground sync called \n Offlline refresh called with error\n"+exception.toString());
    }

    /**
     * writes the Refresh SuccessLog.
     * @param list
     */
    private void writeInfoRefreshLog(List<String>list){
        LogManager.writeLogInfo("OfflineRefresh : Completed successfully with "+list.toString());
    }

    public interface IBGSyncListener{
        void onCreateListener(boolean isError, int createType);
        void onFlushListener(boolean isError);
        void onRefreshListener(boolean isError);
    }
}

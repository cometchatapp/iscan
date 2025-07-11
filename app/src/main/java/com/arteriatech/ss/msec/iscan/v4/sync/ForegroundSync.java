package com.arteriatech.ss.msec.iscan.v4.sync;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.common.MSFAApplication;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.store.OnlineManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.arteriatech.ss.msec.iscan.v4.common.Constants.REPEATABLE_REQUEST_ID;

@SuppressWarnings("all")
public class ForegroundSync {
    public static final int BG_SO=1001;
    public static final int BG_CP=1002;

    private Context context;
    private String TAG_SO="Order Entry:";
    private List<String> refreshSyncList = new ArrayList<>();
    public IBGSyncListener listener;
    private boolean isRefresh,isPostInProgress;

    public ForegroundSync() {
    }

    public ForegroundSync(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public ForegroundSync init(Context context, IBGSyncListener listener) {
        this.context = context;
        this.listener = listener;
        this.isPostInProgress=false;
        return this;
    }

    String docno = "";
    String refGuid = "";
    public void syncData(String docno,String refGuid,String jsonStr){
//        Constants.isBGSync =true;
        this.docno=docno;
        this.refGuid=refGuid;
        LogManager.writeLogInfo("Add Outlet : fetching pending list ");
        fetchPendingList(jsonStr);
    }

    /**
     * Fetching all pending data from DataVault.
     */
    private void fetchPendingList(String jsonStr){
        try {

            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            Set<String> set = new HashSet<>();
            postVaultData(jsonStr);
        } catch (Throwable e) {
            Constants.isBGSync =false;
            LogManager.writeLogError("Add Outlet :  Error \n"+e.toString());
        }
    }

    /**
     * Posting the DataVault data to server with reference of stored set.
     * @param set
     */
    private void  postVaultData(String jsonStr){
      Thread thread = new Thread(() -> {
          try {
              isPostInProgress = true;
//              for (String value :set) {
                  try {
//                      while (!isPostInProgress){
//                          try {
//                              Thread.sleep(1000);
//                          } catch (Throwable e) {
//                              e.printStackTrace();
//                          }
//                      }
//                      if(docno.equalsIgnoreCase(value)){
                          isPostInProgress =false;
//                          String store = ConstantsUtils.getFromDataVault(value, context);
                          JSONObject jsonObject = new JSONObject(jsonStr);
                          /**
                           * This condition executes for SalesOrder Create with own UIListener to ensure the data safety..
                           */
//                          if (jsonObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
//                              refreshSyncList.addAll(SyncUtils.getSOsCollection());
//                              refreshSyncList.addAll(SyncUtils.getValueHelps());
//                              postOrderEntryData(value,jsonObject);
//                              Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
//                                      "log - Bacground sync called \n posting order entry data");
//                          }else if (jsonObject.getString(Constants.entityType).equalsIgnoreCase(Constants.ChannelPartners)) {
                              refreshSyncList.addAll(SyncUtils.getRetailer());
                              refreshSyncList.addAll(SyncUtils.getBeat(context));
                              LogManager.writeLogInfo("Add Outlet : Added refresh list");
                              postChannelPartnerData(docno,jsonObject);
//                          }
//                      }

                  } catch (Throwable e) {
                      isPostInProgress =true;
                      LogManager.writeLogInfo("Add Outlet : Erro occured "+e.getMessage());
                  }
//              }
          } catch (Throwable e) {
              Constants.isBGSync =false;
          }
//          postOfflineData();
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

    /**
     * Post ChannelPartner from DataVault.
     * @param docNo reference Number
     * @param jsonObject Object has Data
     */
    private void postChannelPartnerData(String docNo,JSONObject jsonObject){
        try {
            LogManager.writeLogInfo("Add Outlet : fetching data before post");
            JSONObject headerObject = Constants.getCPHeaderValuesFromJsonObject(jsonObject,context);
            OnlineManager.createCPEntity(REPEATABLE_REQUEST_ID, headerObject.toString(), Constants.ChannelPartners, new OnlineManager.OnlineResponseListener() {
                /*@Override
                public void onRequestError(int operation, Exception e) {
                    Constants.isBGSync =false;
                    isPostInProgress =true;
                    writeErrorLogCP(e);
                    if (listener!=null)listener.onCreateListener(true,BG_CP,e.getMessage());
                }*/

                @Override
                public void onRequestError(int operation, String error) {
                    Constants.isBGSync =false;
                    isPostInProgress =true;
                    writeErrorLogCP(error);
                    if (listener!=null)listener.onCreateListener(true,BG_CP,error.toString(),"");
                }

                @Override
                public void onRequestSuccess(int operation, String key) {
                    isPostInProgress =true;

                    String value="";
                    try {
                        JSONObject jsonObject1 = new JSONObject(key);
                        value=jsonObject1.getJSONObject("d").getString("CPUID");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    writeInfoLogCP(value,jsonObject);
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                SyncUtils.updatingDaySyncStartTime(context,Constants.add_outlet_post_start,Constants.EndSync,refGuid.toUpperCase());
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    });
//                    postOfflineData();
                    if (listener!=null)listener.onCreateListener(false,BG_CP,"",value);
                }
            }, context);
        } catch (Throwable e) {
            isPostInProgress =true;
            LogManager.writeLogInfo("Add Outlet : Erro occured "+e.getMessage());
        }
    }


//-------------------------------------------------------------------------------------------------Post Offline and Refresh Section---------------------------------------------
    /**
     * Posting all offline data/ flushing the RQ DB to server.
     */
    private void postOfflineData(){
        try {
//            if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
//                try {
//                    OfflineManager.flushQueuedRequests(new UIListener() {
//                        @Override
//                        public void onRequestError(int operation, Exception e) {
//                            writeErrorFlushLog(e);
//                            Constants.isBGSync =false;
//                            if (listener!=null)listener.onFlushListener(true,e.toString());
//                        }
//                        @Override
//                        public void onRequestSuccess(int operation, String key) {
//                            writeInfoFlushLog();
//                            if (listener!=null)listener.onFlushListener(false,"");
//                            /**
//                             * on flushing Data from request-Q Data refresh to be done with required collections.
//                             */
//                            if (isRefresh) {
//                                ((Activity)context).runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        try {
//                                            SyncUtils.updatingDaySyncStartTime(context,Constants.add_outlet_refresh_start,Constants.StartSync,refGuid.toUpperCase());
//                                        } catch (Throwable e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                });
//
//                                Set<String>set= new HashSet<>(refreshSyncList);
//                                refreshSyncList.clear();
//                                refreshSyncList.addAll(set);
//                                refreshCollections(refreshSyncList);
//                            }else{
//                                ((Activity)context).runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        try {
//                                            SyncUtils.updatingDaySyncStartTime(context,Constants.add_outlet_refresh_start,Constants.StartSync,refGuid.toUpperCase());
//                                        } catch (Throwable e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                });
//                                ArrayList<String>hashSet = new ArrayList<>(SyncUtils.getAllSyncValue(context));
//                                refreshCollections(hashSet);
//                            }
//                        }
//                    },getCollections(refreshSyncList));
//                } catch (Throwable e) {
//                    Constants.isBGSync =false;
//                    Log.e(TAG_SO,e.toString());
//                    Constants.writeLogsToInternalStorage(context,"Distributor: "+ MSFAApplication.DISTRIBUTOR_NAME+"\n" +
//                            "log - Bacground sync called  postOfflineData method triggered \n error while postOfflineData data \n"+e.toString());
//                }
//            }else{
            ((Activity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            SyncUtils.updatingDaySyncStartTime(context,Constants.add_outlet_refresh_start,Constants.StartSync,refGuid.toUpperCase());
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
//                if (!isRefresh) {
//                    ArrayList<String>hashSet = new ArrayList<>(SyncUtils.getAllSyncValue(context));
//                    refreshCollections(hashSet);
//                }else{
                    if (refreshSyncList!=null&&!refreshSyncList.isEmpty()) {
                        Set<String>set= new HashSet<>(refreshSyncList);
                        refreshSyncList.clear();
                        refreshSyncList.addAll(set);
                        refreshCollections(refreshSyncList);
                    }
//                }
//            }
        } catch (Throwable e) {
            LogManager.writeLogInfo("Add Outlet : Erro occured "+e.getMessage());
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
                        writeErrorRefreshLog(e);
                        if (listener!=null)listener.onRefreshListener(true,e.toString());
                    }

                    @Override
                    public void onRequestSuccess(int operation, String key) {
                        writeInfoRefreshLog(list);
                        Constants.updateLastSyncTimeToTable(context, (ArrayList<String>) list);

                        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
                        Set<String> set = new HashSet<>();
                        if (sharedPreferences.getStringSet(Constants.CPList, null)!=null){
                            set.addAll(sharedPreferences.getStringSet(Constants.CPList, null));
                        }

                        for (String value :set) {
                            try {
                                String store = ConstantsUtils.getFromDataVault(value, context);
                                JSONObject jsonObject = new JSONObject(store);

                            } catch (Throwable e) {
                                e.printStackTrace();
                                LogManager.writeLogError("Add outlet mobile no already exist deleting from datavault error : "+e.getMessage());
                            }
                        }

                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    SyncUtils.updatingDaySyncStartTime(context,Constants.add_outlet_refresh_start,Constants.EndSync,refGuid.toUpperCase());
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        listener.onRefreshListener(false,"");
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
            String strErrorMsg=exception.toString();
            if (strErrorMsg.contains("invalid authentication")) {
                Log.e(TAG_SO,"401 invalid authentication "+strErrorMsg);
                LogManager.writeLogError("401 Order entry :",exception);
            } else {
                Log.e(TAG_SO, "Order entry :" + exception.toString());
                LogManager.writeLogError("Order entry :" + exception.toString());
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
    private void writeInfoLogSO(String value,JSONObject jsonObject){
        try {
            if (jsonObject.has(Constants.OrderNo)) {
                LogManager.writeLogInfo(" Sales order created successfully for ORDER NO :"+jsonObject.getString(Constants.OrderNo));
            }
            Constants.removeDataValtFromSharedPref(context, Constants.SecondarySOCreate, value, false);
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
    private void writeErrorLogCP(String exception){
        String strErrorMsg=exception.toString();
        if (strErrorMsg.contains("invalid authentication")) {
            Log.e(TAG_SO,"401 invalid authentication "+strErrorMsg);
            LogManager.writeLogError("Add outlet 401 with error :"+exception.toString());
        } else {
            Log.e(TAG_SO, "Channel Partner :" + exception.toString());
            LogManager.writeLogError("Add outlet :" + exception.toString());
        }
    }

    /**
     * used to print info log for CP Create.
     * @param value reference object used for delecting the record in DataVault.
     * @param jsonObject used to print the CP No.
     */
    private void writeInfoLogCP(String value,JSONObject jsonObject){
        try {
            LogManager.writeLogInfo("Add outlet created successfully :"+value);

//            Constants.removeDataValtFromSharedPref(context, Constants.CPList, value, false);
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
        void onCreateListener(boolean isError, int createType,String errorMessage,String CPUID);
        void onFlushListener(boolean isError,String errorMessage);
        void onRefreshListener(boolean isError,String errorMessage);
    }
}

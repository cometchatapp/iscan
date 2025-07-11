package com.arteriatech.ss.msec.iscan.v4.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.store.OnlineManager;
import com.sap.smp.client.odata.exception.ODataException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.arteriatech.ss.msec.iscan.v4.common.Constants.REPEATABLE_REQUEST_ID;

@SuppressWarnings("all")
public class AutoBackgroundSync {
    public static final int BG_SO=1001;
    public static final int BG_CP=1002;

    private Context context;
    private String TAG_SO="Order Entry:";
    private List<String> refreshSyncList ;
    public IBGSyncListener listener;
    private boolean isRefresh,isSORefresh,isPostInProgress;

    public AutoBackgroundSync() {
    }

    public AutoBackgroundSync(boolean isRefresh,boolean isSORefresh) {
        this.isRefresh = isRefresh;
        this.isSORefresh = isSORefresh;
    }

    public AutoBackgroundSync init(Context context, IBGSyncListener listener) {
        this.context = context;
        this.listener = listener;
        this.isPostInProgress = false;
        this.refreshSyncList = new ArrayList<>();
        return this;
    }

    public void syncData(){
        Constants.iSAutoSync = true;
        fetchPendingList();
    }

    /**
     * Fetching all pending data from DataVault.
     */
    private void fetchPendingList(){
        try {
            LogManager.writeLogInfo("AutoSync is in progress, fetching pending list in datavault");
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            Set<String> set = new HashSet<>();

            if (sharedPreferences.getStringSet(Constants.SecondarySOCreate, null)!=null){
                set.addAll(sharedPreferences.getStringSet(Constants.SecondarySOCreate, null));
            }if (sharedPreferences.getStringSet(Constants.CPList, null)!=null){
                set.addAll(sharedPreferences.getStringSet(Constants.CPList, null));
            }
            if (set!=null&&!set.isEmpty()) {
                LogManager.writeLogInfo(" posting valut data");
                postVaultData(set);
            }else{
                LogManager.writeLogInfo("AutoSync: valut data is not available, posting offline data");
                postOfflineData();
            }
        } catch (Throwable e) {
            Constants.iSAutoSync = false;
            LogManager.writeLogInfo("Error while posting data : \n"+e.toString());
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
                      JSONObject jsonObject = new JSONObject(store);
                      /**
                       * This condition executes for SalesOrder Create with own UIListener to ensure the data safety..
                       */
                      if (jsonObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                          refreshSyncList.addAll(SyncUtils.getSOsCollection());
                          refreshSyncList.addAll(SyncUtils.getValueHelps());
                          postOrderEntryData(value,jsonObject);
                          LogManager.writeLogInfo("AutoSync: posting SO data");
                      }else if (jsonObject.getString(Constants.entityType).equalsIgnoreCase(Constants.ChannelPartners)) {
                          refreshSyncList.addAll(SyncUtils.getValueHelps());
                          refreshSyncList.addAll(SyncUtils.getRetailer());
                          refreshSyncList.addAll(SyncUtils.getBeat(context));
                          LogManager.writeLogInfo("AutoSync: posting Retailer data");
                          postChannelPartnerData(value,jsonObject);
                      }
                  } catch (Throwable e) {
                      isPostInProgress=true;
                      Constants.iSAutoSync = false;
                      LogManager.writeLogInfo("Error while posting data : \n"+e.toString());
                  }
              }
          } catch (Throwable e) {
              Constants.iSAutoSync = false;
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
                    isPostInProgress=true;
                    writeErrorLogSO(exception);
                    if (listener!=null)listener.onCreateListener(true,BG_SO);
                }

                @Override
                public void onRequestSuccess(int operation, String key) {
                    isPostInProgress=true;
                    writeInfoLogSO(docNo,jsonObject);
                    if (listener!=null)listener.onCreateListener(false,BG_SO);
                }
            }, context);
        } catch (Throwable e) {
            Constants.iSAutoSync = false;
            isPostInProgress=true;
            if (listener!=null)listener.onCreateListener(true,BG_SO);
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
                    isPostInProgress=true;
                    writeErrorLogCP(e);
                    if (listener!=null)listener.onCreateListener(true,BG_CP);
                }

                @Override
                public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                    isPostInProgress=true;
                    writeInfoLogCP(docNo,jsonObject);
                    if (listener!=null)listener.onCreateListener(false,BG_CP);
                }
            }, context);
        } catch (Throwable e) {
            Constants.iSAutoSync = false;
            isPostInProgress=true;
            if (listener!=null)listener.onCreateListener(false,BG_CP);
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
                            writeErrorFlushLog(e);
                            Constants.iSAutoSync = false;
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
                    Constants.iSAutoSync = false;
                    Log.e(TAG_SO,e.toString());
                }
            }else if(!isRefresh&&!isSORefresh){
                if (!isRefresh) {
                    ArrayList<String>hashSet = new ArrayList<>(SyncUtils.getAllSyncValue(context));
                    refreshCollections(hashSet);
                }
            }else if(isSORefresh){
                refreshSyncList.add(Constants.ChannelPartners);
                refreshSyncList.add(Constants.CPDMSDivisions);
                refreshSyncList.add(Constants.Towns);
                refreshSyncList.add(Constants.SubDistricts);
                refreshSyncList.add(Constants.KPISet);
                refreshSyncList.add(Constants.Targets);
                refreshSyncList.add(Constants.KPIItems);
                refreshSyncList.add(Constants.TargetItems);
                refreshSyncList.add(Constants.Schemes);
                refreshSyncList.add(Constants.SchemeItemDetails);
                refreshSyncList.add(Constants.SchemeSlabs);
                refreshSyncList.add(Constants.SchemeGeographies);
                refreshSyncList.add(Constants.SchemeCPs);
                refreshSyncList.add(Constants.SchemeSalesAreas);
                refreshSyncList.addAll(SyncUtils.getBeat(context));
                refreshSyncList.addAll(SyncUtils.getSOsCollection());
                refreshSyncList.addAll(SyncUtils.getStock());
                refreshCollections(refreshSyncList);
            }
        } catch (Throwable e) {
            Constants.iSAutoSync = false;
            Log.e(TAG_SO,e.toString());
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
                        Constants.iSAutoSync = false;
                        if (listener!=null)listener.onRefreshListener(true);
                    }

                    @Override
                    public void onRequestSuccess(int operation, String key) {
                        writeInfoRefreshLog(list);
                        Constants.iSAutoSync = false;
                        Constants.updateLastSyncTimeToTable(context, (ArrayList<String>) list);
                        listener.onRefreshListener(false);
                    }
                }, Constants.Fresh, getCollections(list));
            } catch (Throwable e) {
                e.printStackTrace();
                LogManager.writeLogError(Constants.error_txt + e.getMessage());
            }
        }else{
            Constants.iSAutoSync = false;
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
        }
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
        } catch (Throwable e) {
            Constants.iSAutoSync = false;
            LogManager.writeLogInfo(" Sales order failed with an error : \n "+e.toString());

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
            Constants.iSAutoSync = false;
            LogManager.writeLogError("Channel Partner :" + e.toString());
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

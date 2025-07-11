package com.arteriatech.ss.msec.iscan.v4.reports.salesorder.pendingsync;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;

import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.Operation;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.asyncTask.RefreshAsyncTask;
import com.arteriatech.ss.msec.iscan.v4.asyncTask.SyncFromDataValtAsyncTask;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.interfaces.MessageWithBooleanCallBack;
import com.arteriatech.ss.msec.iscan.v4.mbo.ErrorBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.SalesOrderBean;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncUtils;

import java.util.ArrayList;

/**
 * Created by e10847 on 07-12-2017.
 */

public class SalesOrderHeaderPendingSyncPresenter implements ISalesOrderHeaderPendingSyncPresenter, UIListener, MessageWithBooleanCallBack {
    private Activity context = null;
    private String CPGUID = "";
    private ArrayList<SalesOrderBean> salesOrderHeaderArrayList = new ArrayList<>();
    private ISalesOrderPendingSyncView salesOrderView = null;
    private ArrayList<String> pendingCollectionList = null;
    private View view;
    private boolean isErrorFromBackend = false;
    private int penROReqCount = 0;
    private int pendingROVal = 0;
    private int isFromWhere = 0;
    private String[] tempRODevList = null;
    private boolean dialogCancelled = false;
    private String concatCollectionStr = "";

    public SalesOrderHeaderPendingSyncPresenter(Activity context, String CPGUID, ISalesOrderPendingSyncView salesOrderPendingSyncView, View view) {
        this.context = context;
        this.CPGUID = CPGUID;
        this.salesOrderView = salesOrderPendingSyncView;
        this.salesOrderHeaderArrayList = new ArrayList<>();
        this.pendingCollectionList = new ArrayList<>();
        this.view = view;
    }

    @Override
    public void connectToOfflineDB(ISalesOrderPendingSyncView.SalesOrderResponse<SalesOrderBean> salesOrderResponse) {
        GetSalesOrderAsyncTask salesOrderAsyncTask = new GetSalesOrderAsyncTask(salesOrderResponse);
        salesOrderAsyncTask.execute();
    }

    @Override
    public void onSync() {
        onSyncSOrder();
    }

    @Override
    public void onDestroy() {
        salesOrderView = null;
    }

    @Override
    public void onRequestError(int i, Exception e) {
        onError(i, e);
    }

    @Override
    public void onRequestSuccess(int i, String s) throws OfflineODataStoreException {
        onSuccess(i, s);
    }

    /**
     * get SOs Pending Sync List from DataVault
     */

    private void getSalesOrderList() {
        try {
            salesOrderHeaderArrayList.addAll(OfflineManager.getSSSoListFromDataValt(context, CPGUID));
        } catch (OfflineODataStoreException e) {
            e.printStackTrace();
        }
    }

    private void onSyncSOrder() {
        isErrorFromBackend = false;
        isFromWhere = 4;
        Constants.mBoolIsReqResAval = true;
        Constants.mBoolIsNetWorkNotAval = false;
        try {
            salesOrderHeaderArrayList.clear();
            salesOrderHeaderArrayList = (ArrayList<SalesOrderBean>) OfflineManager.getSSSoListFromDataValt(context, CPGUID);
            if (!salesOrderHeaderArrayList.isEmpty()) {
                if (Constants.iSAutoSync) {
                    if (salesOrderView != null) {
                        salesOrderView.hideProgressDialog();
                        salesOrderView.showMessage(context.getString(R.string.alert_auto_sync_is_progress));
                    }
                } else {
                    if (UtilConstants.isNetworkAvailable(context)) {
                        pendingCollectionList.clear();
                        pendingCollectionList.addAll(SyncUtils.getSOsCollection());
                        pendingCollectionList.add(Constants.ConfigTypsetTypeValues);
                        pendingROVal = 0;
                        if (tempRODevList != null) {
                            tempRODevList = null;
                            penROReqCount = 0;
                        }

                        if (salesOrderHeaderArrayList != null && salesOrderHeaderArrayList.size() > 0) {
                            tempRODevList = new String[salesOrderHeaderArrayList.size()];

                            for (SalesOrderBean SalesOrderBean : salesOrderHeaderArrayList) {
                                tempRODevList[pendingROVal] = SalesOrderBean.getDeviceNo();
                                pendingROVal++;
                            }
                            if (salesOrderView != null) {
                                salesOrderView.showProgressDialog();
                            }
                            new SyncFromDataValtAsyncTask(context, tempRODevList, this, this).execute();
                        }
                    } else {
                        if (salesOrderView != null) {
                            salesOrderView.hideProgressDialog();
                            salesOrderView.showMessage(context.getString(R.string.no_network_conn));
                        }
                    }
                }
            } else if (salesOrderView != null) {
                salesOrderView.hideProgressDialog();
                salesOrderView.showMessage(context.getString(R.string.no_req_to_update_sap));
            }
        } catch (OfflineODataStoreException e) {
            e.printStackTrace();
            if (salesOrderView != null) {
                salesOrderView.hideProgressDialog();
            }
        }
    }

    public void onError(int operation, Exception e) {
        ErrorBean errorBean = Constants.getErrorCode(operation, e, context);
        if (errorBean.hasNoError()) {
            isErrorFromBackend = true;
            if (isFromWhere == 1 || isFromWhere == 2) {
                if (operation == Operation.OfflineRefresh.getValue()) {

                    Constants.isSync = false;
                    if (salesOrderView != null) {
                        salesOrderView.hideProgressDialog();
                        salesOrderView.onReloadData();
                        if (!Constants.isStoreClosed) {
                            salesOrderView.showMessage(context.getString(R.string.msg_error_occured_during_sync));
                        } else {
                            salesOrderView.showMessage(context.getString(R.string.msg_sync_terminated));
                        }
                    }
                }


            } else {
                Constants.mBoolIsReqResAval = true;
                penROReqCount++;
                if ((operation == Operation.Create.getValue()) && (penROReqCount == pendingROVal)) {
                    LogManager.writeLogError(Constants.Error + " : " + e.getMessage());
                    concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(pendingCollectionList);
                    new RefreshAsyncTask(context, concatCollectionStr, this).execute();
                }

                if (operation == Operation.OfflineFlush.getValue()) {
                    new RefreshAsyncTask(context, Constants.Visits, this).execute();
                } else if (operation == Operation.OfflineRefresh.getValue()) {
                    LogManager.writeLogError(Constants.Error + " : " + e.getMessage());
                    if (salesOrderView != null) {
                        salesOrderView.hideProgressDialog();
                        salesOrderView.onReloadData();
                        UtilConstants.showAlert(context.getString(R.string.msg_error_occured_during_sync), context);
                    }
                }
            }
        } else if (errorBean.isStoreFailed()) {
            Constants.mBoolIsReqResAval = true;
            Constants.mBoolIsNetWorkNotAval = true;
            if (UtilConstants.isNetworkAvailable(context)) {
                Constants.isSync = true;
                if (salesOrderView != null) {
                    salesOrderView.showProgressDialog();
                }
                new RefreshAsyncTask(context, "", this).execute();
            } else {
                Constants.isSync = false;
                if (salesOrderView != null) {
                    salesOrderView.hideProgressDialog();
                    salesOrderView.onReloadData();
                    Constants.displayMsgReqError(errorBean.getErrorCode(), context);
                }
            }
        } else {
            Constants.mBoolIsReqResAval = true;
            Constants.isSync = false;
            if (salesOrderView != null) {
                salesOrderView.hideProgressDialog();
                salesOrderView.onReloadData();
                Constants.displayMsgReqError(errorBean.getErrorCode(), context);
            }
        }
    }

    public void onSuccess(int operation, String s) throws OfflineODataStoreException {
        if (operation == Operation.OfflineRefresh.getValue() && isFromWhere == 2) {
            Constants.updateLastSyncTimeToTable(context, pendingCollectionList);
            Constants.isSync = false;
            ConstantsUtils.startAutoSync(context, false);
            if (salesOrderView != null) {
                salesOrderView.hideProgressDialog();
                salesOrderView.showMessage(context.getString(R.string.msg_sync_successfully_completed));
                salesOrderView.onReloadData();
               // AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, context, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE);
            }
        }
        if (isFromWhere == 4) {
            if (operation == Operation.Create.getValue() && pendingROVal > 0) {
                Constants.mBoolIsReqResAval = true;
                /*Set<String> set = new HashSet<>();
                SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
                set = sharedPreferences.getStringSet(Constants.SecondarySOCreate, null);

                HashSet<String> setTemp = new HashSet<>();
                if (set != null && !set.isEmpty()) {
                    Iterator itr = set.iterator();
                    while (itr.hasNext()) {
                        setTemp.add(itr.next().toString());
                    }
                }

                setTemp.remove(tempRODevList[penROReqCount]);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet(Constants.SecondarySOCreate, setTemp);
                editor.commit();*/
                Constants.removeDataValtFromSharedPref(context, Constants.SecondarySOCreate, tempRODevList[penROReqCount], false);
                ConstantsUtils.storeInDataVault(tempRODevList[penROReqCount],"",context);
                penROReqCount++;


            }
            if ((operation == Operation.Create.getValue()) && (penROReqCount == pendingROVal)) {
                concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(pendingCollectionList);
                new RefreshAsyncTask(context, concatCollectionStr, this).execute();
            } else if (operation == Operation.OfflineFlush.getValue()) {
                concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(pendingCollectionList);
                new RefreshAsyncTask(context, concatCollectionStr, this).execute();
            } else if (operation == Operation.OfflineRefresh.getValue()) {
                Constants.updateLastSyncTimeToTable(context, pendingCollectionList);
                if (salesOrderView != null) {
                    salesOrderView.hideProgressDialog();
                    String msg = "";
                    ConstantsUtils.startAutoSync(context, false);
                    if (salesOrderView != null) {
                        if (!isErrorFromBackend) {
                            salesOrderView.showMessage(context.getString(R.string.msg_sync_successfully_completed));
                            salesOrderView.onReloadData();
                        } else {
                            salesOrderView.showMessage(context.getString(R.string.msg_error_occured_during_sync));
                            salesOrderView.onReloadData();
                        }
                       // AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, context, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE);
                    }
                }


            } else if (operation == Operation.GetStoreOpen.getValue() && OfflineManager.isOfflineStoreOpen()) {
                Constants.isSync = false;
                Constants.mBoolIsReqResAval = true;
                try {
                    OfflineManager.getAuthorizations(context);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
                Constants.setSyncTime(context);
                ConstantsUtils.startAutoSync(context, false);
                if (salesOrderView != null) {
                    salesOrderView.onReloadData();
                    salesOrderView.hideProgressDialog();
                   // AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, context, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE);
                }
            }
        }
    }

    @Override
    public void clickedStatus(boolean clickedStatus, String err_msg, ErrorBean errorBean) {
        if (!clickedStatus) {
            if (salesOrderView != null) {
                salesOrderView.hideProgressDialog();
                UtilConstants.showAlert(err_msg, context);
            }
        }
    }

    /**
     * Get SOs on Background Thread
     */
    public class GetSalesOrderAsyncTask extends AsyncTask<Void, Void, ArrayList<SalesOrderBean>> {
        ISalesOrderPendingSyncView.SalesOrderResponse<SalesOrderBean> salesOrderResponse;

        public GetSalesOrderAsyncTask(ISalesOrderPendingSyncView.SalesOrderResponse<SalesOrderBean> salesOrderResponse) {
            this.salesOrderResponse = salesOrderResponse;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (salesOrderView != null) {
                salesOrderView.showProgressDialog();
            }
        }

        @Override
        protected ArrayList<SalesOrderBean> doInBackground(Void... params) {
            salesOrderHeaderArrayList.clear();
            getSalesOrderList();
            return salesOrderHeaderArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<SalesOrderBean> salesOrderBeenArrayList) {
            super.onPostExecute(salesOrderBeenArrayList);
            if (salesOrderView != null) {
                salesOrderResponse.success(salesOrderBeenArrayList);
                salesOrderView.hideProgressDialog();
            }
        }
    }
}

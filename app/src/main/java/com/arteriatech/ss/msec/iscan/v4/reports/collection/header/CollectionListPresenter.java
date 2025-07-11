package com.arteriatech.ss.msec.iscan.v4.reports.collection.header;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;

import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.Operation;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.asyncTask.RefreshAsyncTask;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.mbo.CollectionHistoryBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.ErrorBean;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncUtils;
import com.sap.smp.client.odata.exception.ODataException;

import java.util.ArrayList;

/**
 * Created by e10847 on 07-12-2017.
 */

public class CollectionListPresenter implements ICollectionHeaderListPresenter, UIListener {
    ArrayList<String> alAssignColl = null;
    private String startDate = "";
    private String endDate = "";
    private String filterType = "";
    private Activity context = null;
    private String customerNumber = "";
    private String customerName = "";
    private ArrayList<CollectionHistoryBean> collArrayList = null;
    private ArrayList<CollectionHistoryBean> searchBeanArrayList = null;
    private ICollectionListView ICollListView = null;
    private String searchText = "";
    private String delvStatusId = "";
    private String statusId = "";
    private String statusName = "";
    private String delvStatusName = "";
    private long refreshTime = 0;
    private boolean isErrorFromBackend = false;
    private View view = null;
    private String cpGuid;

    public CollectionListPresenter(String cpGuid, Activity context, String customerNumber, String customerName, ICollectionListView ICollListView, View view) {
        this.context = context;
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.ICollListView = ICollListView;
        this.collArrayList = new ArrayList<>();
        this.view = view;
        this.searchBeanArrayList = new ArrayList<>();
        this.cpGuid = cpGuid;
    }

    @Override
    public void connectToOfflineDB() {
        new GetSalesOrderAsyncTask().execute();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {
        ICollListView = null;
    }

    @Override
    public void onResume() {
        if (ICollListView != null) {
            if (refreshTime != 0)
                ICollListView.displayRefreshTime(SyncUtils.getCollectionSyncTime(context, Constants.SSSOs));
        }
    }

    @Override
    public void onFilter() {
        if (ICollListView != null) {
            ICollListView.openFilter(startDate, endDate, filterType, statusId, delvStatusId);
        }
    }

    @Override
    public void onSearch(String searchText) {
        if (!this.searchText.equalsIgnoreCase(searchText)) {
            this.searchText = searchText;
            onSearch(searchText, statusId, delvStatusId);
        }
    }

    private void onSearch(String searchText, String soStatus, String delivetyType) {
        this.searchText = searchText;
        searchBeanArrayList.clear();
//        boolean soTypeStatus = false;
        boolean soDelStatus = false;
        boolean soSearchStatus = false;
        if (collArrayList != null) {
            if (TextUtils.isEmpty(searchText) && (TextUtils.isEmpty(delivetyType) || delivetyType.equalsIgnoreCase(Constants.All))) {
                searchBeanArrayList.addAll(collArrayList);
            } else {
                for (CollectionHistoryBean item : collArrayList) {

                    if (!TextUtils.isEmpty(searchText)) {
                        soSearchStatus = item.getFIPDocNo().toLowerCase().contains(searchText.toLowerCase());
                    } else {
                        soSearchStatus = true;
                    }
                    if (soSearchStatus)
                        searchBeanArrayList.add(item);
                }
            }
        }
        if (ICollListView != null) {
            ICollListView.searchResult(searchBeanArrayList);
        }
    }

    @Override
    public void onRefresh() {
        onRefreshSOrder();
    }

    @Override
    public void startFilter(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void getRefreshTime() {
        if (ICollListView != null) {
//            if (refreshTime != 0)
            ICollListView.displayRefreshTime(SyncUtils.getCollectionSyncTime(context, Constants.SSSOs));
        }
    }

    @Override
    public void getDetails(final CollectionHistoryBean collectionHistoryBean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<CollectionHistoryBean> alCollHisDetails = new ArrayList<>();
                String mStrCollectionItemQry = Constants.FinancialPostingItemDetails + "?$filter=" + Constants.FIPGUID + " eq " + collectionHistoryBean.getFIPGUID() + " &$orderby=" + Constants.FIPItemNo + " asc";
                try {
                    alCollHisDetails = OfflineManager.getCollectionItemDetails(context, mStrCollectionItemQry);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
                final ArrayList<CollectionHistoryBean> finalAlCollHisDetails = alCollHisDetails;
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (ICollListView != null) {
                            collectionHistoryBean.setAlCollItemList(finalAlCollHisDetails);
                            ICollListView.openSODetail(collectionHistoryBean);
                        }
                    }
                });
            }
        }).start();
    }

    private void displayFilterType() {
        try {
            String statusDesc = "";
            if (!TextUtils.isEmpty(statusId)) {
                statusDesc = ", " + statusName;
            }
            if (!TextUtils.isEmpty(delvStatusId)) {
                statusDesc = statusDesc + ", " + delvStatusName;
            }
            if (ICollListView != null) {
                ICollListView.setFilterDate(statusDesc);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void onRefreshSOrder() {
        alAssignColl = new ArrayList<>();
        String concatCollectionStr = "";
        if (UtilConstants.isNetworkAvailable(context)) {
            alAssignColl.clear();
            concatCollectionStr = "";
            alAssignColl.addAll(SyncUtils.getFIPCollection());
            alAssignColl.add(Constants.ConfigTypsetTypeValues);
            concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(alAssignColl);

            if (Constants.iSAutoSync) {
                if (ICollListView != null) {
                    ICollListView.hideProgressDialog();
                    ICollListView.showMessage(context.getString(R.string.alert_auto_sync_is_progress));
                }
            } else {
                try {
                    Constants.isSync = true;
                    new RefreshAsyncTask(context, concatCollectionStr, this).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (ICollListView != null) {
                ICollListView.hideProgressDialog();
                ICollListView.showMessage(context.getString(R.string.no_network_conn));
            }
        }
    }

    @Override
    public void onRequestError(int operation, Exception e) {
        ErrorBean errorBean = Constants.getErrorCode(operation, e, context);
        if (errorBean.hasNoError()) {
            isErrorFromBackend = true;
            if (operation == Operation.OfflineRefresh.getValue()) {
                Constants.updateLastSyncTimeToTable(context, alAssignColl);
                Constants.isSync = false;
                if (ICollListView != null) {
                    ICollListView.hideProgressDialog();
                    ICollListView.showMessage(context.getString(R.string.msg_error_occured_during_sync));
                }


            } else if (operation == Operation.GetStoreOpen.getValue()) {
                Constants.isSync = false;
                if (ICollListView != null) {
                    ICollListView.hideProgressDialog();
                    ICollListView.showMessage(context.getString(R.string.msg_error_occured_during_sync));
                }
            }

        } else if (errorBean.isStoreFailed()) {
            if (UtilConstants.isNetworkAvailable(context)) {
                Constants.isSync = true;
                if (ICollListView != null) {
                    ICollListView.showProgressDialog();
                }
                new RefreshAsyncTask(context, "", this).execute();
            } else {
                Constants.isSync = false;
                if (ICollListView != null) {
                    ICollListView.hideProgressDialog();
                    Constants.displayMsgReqError(errorBean.getErrorCode(), context);
                }
            }
        } else {
            Constants.isSync = false;
            if (ICollListView != null) {
                ICollListView.hideProgressDialog();
                Constants.displayMsgReqError(errorBean.getErrorCode(), context);
            }
        }
    }


    @Override
    public void onRequestSuccess(int operation, String s) throws ODataException, OfflineODataStoreException {
        if (operation == Operation.OfflineRefresh.getValue()) {
            Constants.updateLastSyncTimeToTable(context, alAssignColl);
            Constants.isSync = false;
            ConstantsUtils.startAutoSync(context, false);
            if (ICollListView != null) {
                ICollListView.hideProgressDialog();
                connectToOfflineDB();
               // AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, context, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE);
            }
        } else if (operation == Operation.GetStoreOpen.getValue() && OfflineManager.isOfflineStoreOpen()) {
            Constants.isSync = false;
            try {
                OfflineManager.getAuthorizations(context);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            Constants.setSyncTime(context);
            ConstantsUtils.startAutoSync(context, false);
            if (ICollListView != null) {
                ICollListView.hideProgressDialog();
                connectToOfflineDB();
               // AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, context, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE);
            }
        }
    }

    /**
     * get SOs HeaderList from OfflineDB
     */
    private void getCollList() {
//        String query = Constants.FinancialPostings + "?$filter=" + Constants.CPGUID + " eq guid'" + Constants.convertStrGUID32to36(cpGuid) + "'";
        String query = Constants.FinancialPostings;
        try {
            collArrayList.clear();
            collArrayList = OfflineManager.getCollectionHistoryList(query, context);
        } catch (OfflineODataStoreException e) {
            e.printStackTrace();
        }
    }


    /**
     * Get SOs on Background Thread
     */
    public class GetSalesOrderAsyncTask extends AsyncTask<Void, Void, ArrayList<CollectionHistoryBean>> {

        public GetSalesOrderAsyncTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (ICollListView != null)
                ICollListView.showProgressDialog();
        }

        @Override
        protected ArrayList<CollectionHistoryBean> doInBackground(Void... params) {
            getCollList();
            return collArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<CollectionHistoryBean> collBeanArrayList) {
            super.onPostExecute(collBeanArrayList);
            if (ICollListView != null) {
                if (collBeanArrayList != null) {
                    onSearch(searchText, statusId, delvStatusId);
                }
                ICollListView.success();
                ICollListView.hideProgressDialog();
            }
        }

    }

}

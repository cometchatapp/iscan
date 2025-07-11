package com.arteriatech.ss.msec.iscan.v4.reports.outstndinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.Operation;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.asyncTask.RefreshAsyncTask;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.datefilter.DateFilterFragment;
import com.arteriatech.ss.msec.iscan.v4.mbo.ErrorBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.OutstandingBean;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncUtils;
import com.sap.smp.client.odata.exception.ODataException;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by e10847 on 19-12-2017.
 */

public class OutstndingListPresenter implements OutStndingInvoicePresenter, UIListener {

    private Context context;
    private OutStndingInvoiceListView iReqListViewPresenter;
    private Activity activity;
    private ArrayList<OutstandingBean> invoiceBeanArrayList;
    private ArrayList<OutstandingBean> searchBeanArrayList;
    private Hashtable<String, String> headerTable;
    private String SPGUID = "";
    private String searchText = "";
    private String CPGUID = "", CPUID = "", cpNo = "", cpName = "";
    private String mStrBundleRetID = "", mStrBundleCPGUID = "",mStrBundleCPGUID36="";
    private String startDate = "";
    private String endDate = "";
    private String filterType = "";
    private String delvStatusId = "";
    private String statusId = "";
    private String statusName = "";
    private String delvStatusName = "";
    ArrayList<String> alAssignColl = null;
    private boolean isErrorFromBackend = false;

    public OutstndingListPresenter(Context context, OutStndingInvoiceListView iReqListViewPresenter, Activity activity, String mStrBundleRetID, String mStrBundleCPGUID,String mStrBundleCPGUID36) {
        this.context = context;
        this.iReqListViewPresenter = iReqListViewPresenter;
        this.invoiceBeanArrayList = new ArrayList<>();
        this.searchBeanArrayList = new ArrayList<>();
        this.headerTable = new Hashtable<>();
        this.activity = activity;
        this.mStrBundleRetID = mStrBundleRetID;
        this.mStrBundleCPGUID = mStrBundleCPGUID;
        this.mStrBundleCPGUID36 = mStrBundleCPGUID36;
    }


    @Override
    public void onFilter() {
        if (iReqListViewPresenter != null) {
            iReqListViewPresenter.openFilter(startDate, endDate, filterType, statusId, delvStatusId);
        }
    }

    @Override
    public void onSearch(String searchText) {
        if (!this.searchText.equalsIgnoreCase(searchText)) {
            this.searchText = searchText;
            onSearchQuery(searchText);
        }
    }

    private void onSearchQuery(String searchText) {
        this.searchText = searchText;
        searchBeanArrayList.clear();
        boolean soTypeStatus = false;
        boolean soDelStatus = false;
        boolean soSearchStatus = false;
        if (invoiceBeanArrayList != null) {
            if (TextUtils.isEmpty(searchText)) {
                searchBeanArrayList.addAll(invoiceBeanArrayList);
            } else {
                for (OutstandingBean item : invoiceBeanArrayList) {
                    soTypeStatus = false;
                    soDelStatus = false;
                    soSearchStatus = false;

                    if (!TextUtils.isEmpty(searchText)) {
                        soSearchStatus = item.getInvoiceNo().toLowerCase().contains(searchText.toLowerCase());
                    } else {
                        soSearchStatus = true;
                    }
                    if (soSearchStatus)
                        searchBeanArrayList.add(item);
                }
            }
        }
        if (iReqListViewPresenter != null) {
            iReqListViewPresenter.searchResult(searchBeanArrayList);
        }
    }

    @Override
    public void onRefresh() {
        onRefreshInvoices();
    }

    @Override
    public void startFilter(int requestCode, int resultCode, Intent data) {
        filterType = data.getStringExtra(DateFilterFragment.EXTRA_DEFAULT);

//        requestSOList(startDate, endDate);
        displayFilterType();
    }

    @Override
    public ArrayList<OutstandingBean> getInvoiceList() {
        try {
            invoiceBeanArrayList = OfflineManager.getOutstandingList(Constants.SSOutstandingInvoices+"?$filter="+ Constants.SoldToCPGUID+" eq guid'"+mStrBundleCPGUID36+"' and " + Constants.PaymentStatusID + " ne '" + "03" + "' and StatusID eq '03'",context,"",mStrBundleCPGUID);
        } catch (OfflineODataStoreException e) {
            LogManager.writeLogError(Constants.error_txt + e.getMessage());
        }
        return invoiceBeanArrayList;
    }

    @Override
    public void calTotalAmount(ArrayList<OutstandingBean> alOutList) {
        Double totalOutVal = 0.0;
        Double totalNetVal = 0.0;
        String mStrCurency = "";
        if(alOutList!=null && alOutList.size()>0){
            for (OutstandingBean invoice : alOutList) {
                try {
                    totalOutVal = totalOutVal + (Double.parseDouble(invoice.getInvoiceBalanceAmount()));
                    totalNetVal = totalNetVal + (Double.parseDouble(invoice.getInvoiceAmount()));
                } catch (NumberFormatException e) {
                    totalOutVal = 0.0;
                    totalNetVal = 0.0;
                    e.printStackTrace();
                }
                mStrCurency = invoice.getCurrency();
            }
        }


        if (iReqListViewPresenter != null) {
            iReqListViewPresenter.displayTotalValue(totalOutVal+"",totalNetVal+"",mStrCurency);
        }
    }

    /**
     * Getting the Invoice Items List from DB
     *
     * @return
     */
    public ArrayList<OutstandingBean> getInvoiceItemsList() {
        /*try {
            invoiceBeanArrayList = OfflineManager.getNewInvoiceHistoryList(Constants.InvoiceItems + "?$filter=" + Constants.CustomerNo + " eq '" + mStrBundleRetID + "' " +
                    "and " + Constants.InvoiceDate + " ge datetime'" + Constants.getLastMonthDate() + "' ", this.activity, "", mStrBundleCPGUID);

        } catch (OfflineODataStoreException e) {
            LogManager.writeLogError(Constants.error_txt + e.getMessage());
        }*/

        return invoiceBeanArrayList;

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
            if (iReqListViewPresenter != null) {
                iReqListViewPresenter.setFilterDate(statusDesc);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void getInvoiceDetails(String invoiceNumber) {
        try {
            new InvoiceDetailsAsyncTask(invoiceNumber).execute();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(int i, Exception e) {
        ErrorBean errorBean = Constants.getErrorCode(i, e, context);
        if (errorBean.hasNoError()) {
            isErrorFromBackend = true;
            if (!Constants.isStoreClosed) {
                if (i == Operation.OfflineRefresh.getValue()) {
                    LogManager.writeLogError(Constants.Error + " : " + e.getMessage());
                    if (iReqListViewPresenter != null) {
                        iReqListViewPresenter.hideProgressDialog();
                        iReqListViewPresenter.showMessage(context.getString(R.string.msg_error_occured_during_sync));
                    }


                }else if (i == Operation.GetStoreOpen.getValue()) {
                    Constants.isSync = false;
                    if (iReqListViewPresenter != null) {
                        iReqListViewPresenter.hideProgressDialog();
                        iReqListViewPresenter.showMessage(context.getString(R.string.msg_error_occured_during_sync));
                    }
                }
            }

        } else if (errorBean.isStoreFailed()) {
            if (UtilConstants.isNetworkAvailable(context)) {
                Constants.isSync = true;
                if (iReqListViewPresenter != null) {
                    iReqListViewPresenter.showProgressDialog();
                }
                new RefreshAsyncTask(context, "", this).execute();
            } else {
                Constants.isSync = false;
                if (iReqListViewPresenter != null) {
                    iReqListViewPresenter.hideProgressDialog();
                    Constants.displayMsgReqError(errorBean.getErrorCode(), context);
                }
            }
        } else {
            Constants.isSync = false;
            Constants.displayMsgReqError(errorBean.getErrorCode(), context);
        }
    }

    @Override
    public void onRequestSuccess(int i, String s) throws ODataException, OfflineODataStoreException {
            if (i == Operation.OfflineRefresh.getValue()) {
                Constants.updateLastSyncTimeToTable(context,alAssignColl);
                Constants.isSync = false;
                if (iReqListViewPresenter != null) {
                    iReqListViewPresenter.hideProgressDialog();
                    iReqListViewPresenter.invoiceListFresh();
                    //AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, activity, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE);
                }
            } else if (i == Operation.GetStoreOpen.getValue() && OfflineManager.isOfflineStoreOpen()) {
                Constants.isSync = false;
                try {
                    OfflineManager.getAuthorizations(context);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
                Constants.setSyncTime(context);
                if (iReqListViewPresenter != null) {
                    iReqListViewPresenter.hideProgressDialog();
                    iReqListViewPresenter.invoiceListFresh();
                   // AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, activity, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE);
                }
            }

    }

    public class InvoiceDetailsAsyncTask extends AsyncTask<Void, Void, Void> {
        private String soNo = "";
        ArrayList<OutstandingBean> invoiceListBean = new ArrayList<>();

        public InvoiceDetailsAsyncTask(String soNo) {
            this.soNo = soNo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            iReqListViewPresenter.showProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String query = Constants.SSOutstandingInvoiceItemDetails+"?$filter="+Constants.InvoiceGUID+" eq guid'"+soNo+"' &$orderby="+Constants.ItemNo+" asc" ;
            try {
                invoiceListBean = OfflineManager.getOutstandingDetails(query/*, context*/);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            iReqListViewPresenter.invoiceDetails(invoiceListBean);
            iReqListViewPresenter.hideProgressDialog();
        }
    }

    private void onRefreshInvoices() {
        alAssignColl = new ArrayList<>();
        String concatCollectionStr = "";
        if (UtilConstants.isNetworkAvailable(context)) {
            alAssignColl.clear();
            concatCollectionStr = "";
            alAssignColl.addAll(SyncUtils.getFIPCollection());
            alAssignColl.add(Constants.ConfigTypsetTypeValues);
            concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(alAssignColl);
            if (Constants.iSAutoSync) {
                UtilConstants.showAlert(context.getString(R.string.alert_auto_sync_is_progress), context);
            } else {
                try {
                    Constants.isSync = true;
                    new RefreshAsyncTask(context, concatCollectionStr, this).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (iReqListViewPresenter != null) {
                iReqListViewPresenter.hideProgressDialog();
            }
            UtilConstants.showAlert(context.getString(R.string.no_network_conn), context);
        }
    }

}

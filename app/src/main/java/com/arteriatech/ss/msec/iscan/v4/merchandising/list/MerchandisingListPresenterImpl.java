package com.arteriatech.ss.msec.iscan.v4.merchandising.list;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.Operation;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.asyncTask.RefreshAsyncTask;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.mbo.ErrorBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.MerchandisingBean;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncUtils;
import com.sap.smp.client.odata.exception.ODataException;

import java.util.ArrayList;

public class MerchandisingListPresenterImpl implements MerchandisingListPresenter, UIListener {
    private String comingFrom = "";
    private Activity mContext;
    private MerchandisingListView merchView = null;
    private String mStrBundleCPGUID = "";
    private String mStrBundleRetID = "";
    private String mStrBundleRetName = "";
    private String mStrBundleRetUID = "";
    private ArrayList<MerchandisingBean> alMercBean = new ArrayList<>();
    private ArrayList<String> alFlushColl = new ArrayList<>();
    private ArrayList<String> alAssignColl = new ArrayList<>();
    private int mError = 0;

    public MerchandisingListPresenterImpl(Activity mContext, MerchandisingListView merchView, Bundle bundleExtras) {
        this.mContext = mContext;
        this.merchView = merchView;
        if (bundleExtras != null) {
            mStrBundleRetID = bundleExtras.getString(Constants.CPNo);
            mStrBundleRetName = bundleExtras.getString(Constants.RetailerName);
            mStrBundleRetUID = bundleExtras.getString(Constants.CPUID);
            mStrBundleCPGUID = bundleExtras.getString(Constants.CPGUID);
            comingFrom = bundleExtras.getString(Constants.comingFrom);
        }
    }

    @Override
    public void onStart() {
        if (merchView != null) {
            merchView.showProgress();
        }



        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    alMercBean.clear();
                    if (comingFrom.equalsIgnoreCase(Constants.Device)) {
                        alMercBean.addAll(OfflineManager.getMerchandisingList(mContext, Constants.MerchReviews + " " + Constants.isLocalFilterQry + " and " + Constants.CPGUID + " eq '" + mStrBundleCPGUID.toUpperCase() + "' &$orderby=" + Constants.MerchReviewDate + "%20desc", Constants.Device));
                    } else {
                        alMercBean.addAll(OfflineManager.getMerchandisingList(mContext, Constants.MerchReviews + " " + Constants.isNonLocalFilterQry + " and " + Constants.CPGUID + " eq '" + mStrBundleCPGUID.toUpperCase() + "' &$orderby=" + Constants.MerchReviewDate + "%20desc", Constants.NonDevice));

//                        alMercBean.addAll(OfflineManager.getMerchandisingList(mContext, Constants.MerchReviews + "?$filter= " + Constants.CPGUID + " eq '" + mStrBundleCPGUID.toUpperCase() + "' and not sap.islocal() &$orderby=" + Constants.MerchReviewDate + "%20desc", Constants.NonDevice));
                    }

                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (merchView != null) {
                            merchView.hideProgress();
                            merchView.displayList(alMercBean);
                            merchView.displayLSTSyncTime(SyncUtils.getCollectionSyncTime(mContext, Constants.MerchReviews));
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        merchView = null;
    }

    @Override
    public void onItemClick(MerchandisingBean merchandisingBean) {
        onNavigateToDetails(merchandisingBean);
    }

    @Override
    public void onRefresh() {
        if (merchView != null) {
            merchView.showProgress();
        }
        mError = 0;
        alAssignColl = new ArrayList<>();
        String concatCollectionStr = "";
        if (UtilConstants.isNetworkAvailable(mContext)) {
            alAssignColl.clear();
            alAssignColl.addAll(SyncUtils.getMerchandising());
            alAssignColl.add(Constants.ConfigTypsetTypeValues);
            concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(alAssignColl);

            if (Constants.iSAutoSync) {
                if (merchView != null) {
                    merchView.hideProgress();
                    merchView.showMessage(mContext.getString(R.string.alert_auto_sync_is_progress));
                }
            } else {
                try {
                    Constants.isSync = true;
                    new RefreshAsyncTask(mContext, concatCollectionStr, this).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (merchView != null) {
                merchView.hideProgress();
                merchView.showMessage(mContext.getString(R.string.no_network_conn));
            }
        }

    }

    @Override
    public void onUploadData() {
        try {
            mError = 0;
            if (OfflineManager.offlineStore.getRequestQueueIsEmpty() && alMercBean.size() == 0) {
                if (merchView != null) {
                    merchView.hideProgress();
                    merchView.showMessage(mContext.getString(R.string.no_req_to_update_merchant));
                }
            } else {
                if (Constants.iSAutoSync) {
                    if (merchView != null) {
                        merchView.hideProgress();
                        merchView.showMessage(mContext.getString(R.string.alert_auto_sync_is_progress));
                    }
                } else {
                    getRefreshList();
                    if (UtilConstants.isNetworkAvailable(mContext)) {
                        if (merchView != null) {
                            merchView.showProgress();
                        }
                        Constants.isSync = true;
                        try {
                            new AsyncPostOfflineData().execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (merchView != null) {
                            merchView.hideProgress();
                            merchView.showMessage(mContext.getString(R.string.no_network_conn));
                        }
                    }
                }
            }

        } catch (ODataException e) {
            e.printStackTrace();
            if (merchView != null) {
                merchView.hideProgress();
            }
        }
    }

    private void getRefreshList() {
        alAssignColl.clear();
        alFlushColl.clear();
        try {
            if (OfflineManager.getVisitStatusForCustomer(Constants.MerchReviews + Constants.isLocalFilterQry)) {
                alAssignColl.add(Constants.MerchReviews);
                alAssignColl.add(Constants.MerchReviewImages);
                alFlushColl.add(Constants.MerchReviews);
            }

            if (OfflineManager.getVisitStatusForCustomer(Constants.VisitActivities + Constants.isLocalFilterQry)) {
                alAssignColl.add(Constants.VisitActivities);
                alFlushColl.add(Constants.VisitActivities);
            }


        } catch (OfflineODataStoreException e) {
            LogManager.writeLogError(Constants.error_txt + e.getMessage());
        }
    }

    private void onNavigateToDetails(MerchandisingBean merchandisingBean) {

    }

    @Override
    public void onRequestError(int operation, Exception exception) {
        ErrorBean errorBean = Constants.getErrorCode(operation, exception, mContext);
        if (errorBean.hasNoError()) {
            mError++;
            if (operation == Operation.OfflineFlush.getValue()) {
                Constants.isSync = false;
               // MerchandisingListActivity.mBoolRefreshDone = true;
                if (merchView != null) {
                    merchView.hideProgress();
                    Constants.displayErrorDialog(mContext, errorBean.getErrorMsg());
                }
            } else if (operation == Operation.OfflineRefresh.getValue()) {
                Constants.isSync = false;
              //  MerchandisingListActivity.mBoolRefreshDone = true;
                if (merchView != null) {
                    merchView.hideProgress();
                    Constants.displayErrorDialog(mContext, errorBean.getErrorMsg());
                }
            } else if (operation == Operation.GetStoreOpen.getValue()) {
                Constants.isSync = false;
                if (merchView != null) {
                    merchView.hideProgress();
                    merchView.showMessage(mContext.getString(R.string.msg_offline_store_failure));
                }
            }
        } else if (errorBean.isStoreFailed()) {
            Constants.mBoolIsReqResAval = true;
            Constants.mBoolIsNetWorkNotAval = true;
            if (UtilConstants.isNetworkAvailable(mContext)) {
                Constants.isSync = true;
                if (merchView != null) {
                    merchView.hideProgress();
                }
                new RefreshAsyncTask(mContext, "", this).execute();
            } else {
                Constants.isSync = false;
                if (merchView != null) {
                    merchView.hideProgress();
                    Constants.displayMsgReqError(errorBean.getErrorCode(), mContext);
                }
            }
        } else {
            Constants.isSync = false;
            if (merchView != null) {
                merchView.hideProgress();
                Constants.displayMsgReqError(errorBean.getErrorCode(), mContext);
            }
        }
    }

    @Override
    public void onRequestSuccess(int operation, String s) throws ODataException, OfflineODataStoreException {
        if (operation == Operation.OfflineFlush.getValue()) {
            if (UtilConstants.isNetworkAvailable(mContext)) {
                String concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(alAssignColl);
                new RefreshAsyncTask(mContext, concatCollectionStr, this).execute();
            } else {
                Constants.isSync = false;
                ConstantsUtils.startAutoSync(mContext, false);
                //MerchandisingListActivity.mBoolRefreshDone = true;
                if (merchView != null) {
                    merchView.hideProgress();
                    merchView.showMessage(mContext.getString(R.string.no_network_conn));
                }
            }
        } else if (operation == Operation.OfflineRefresh.getValue()) {
            Constants.updateLastSyncTimeToTable(mContext, alAssignColl);
            Constants.isSync = false;
           // MerchandisingListActivity.mBoolRefreshDone = true;
            ConstantsUtils.startAutoSync(mContext, false);
            if (merchView != null) {
                merchView.hideProgress();
                if (comingFrom.equalsIgnoreCase(Constants.Device)) {
                    if (mError == 0) {
                        merchView.showMessage(mContext.getString(R.string.msg_sync_successfully_completed));
                    } else {
                        merchView.showMessage(mContext.getString(R.string.msg_error_occured_during_sync));
                    }
                }
               // AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, mContext, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE);
                onStart();
            }
        } else if (operation == Operation.GetStoreOpen.getValue() && OfflineManager.isOfflineStoreOpen()) {
            try {
                OfflineManager.getAuthorizations(mContext);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            ConstantsUtils.startAutoSync(mContext, false);
            Constants.setSyncTime(mContext);
            if (merchView != null) {
                merchView.hideProgress();
                merchView.showMessage(mContext.getString(R.string.msg_offline_store_success));
              //  AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, mContext, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE);
                onStart();
            }
        }
    }

    public class AsyncPostOfflineData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);

                String concatFlushCollStr = UtilConstants.getConcatinatinFlushCollectios(alFlushColl);
                Constants.Entity_Set.clear();
                Constants.AL_ERROR_MSG.clear();
                try {
                    if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                        try {
                            OfflineManager.flushQueuedRequests(MerchandisingListPresenterImpl.this, concatFlushCollStr);
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (ODataException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}

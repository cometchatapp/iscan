package com.arteriatech.ss.msec.iscan.v4.retailerapproval;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.asyncTask.RefreshAsyncTask;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.datefilter.DateFilterFragment;
import com.arteriatech.ss.msec.iscan.v4.mbo.ErrorBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.RetailerBean;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.Operation;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.store.GetOnlineODataInterface;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.store.OnlineManager;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncUtils;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.store.ODataRequestExecution;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.arteriatech.ss.msec.iscan.v4.store.OfflineManager.TAG;

/**
 * Created by e10847 on 19-12-2017.
 */

public class RetailerApprovalPresenter implements ICustomerPresenter, UIListener, GetOnlineODataInterface {
    public static boolean isRefresh = false;
    public ArrayList<RetailerBean> retailerArrayList, searchBeanArrayList;
    ArrayList<String> alAssignColl = null;
    boolean isValidData = true;
    RetailerBean retailerBean = null;
    private int comingFrom = 0;
    private Context context;
    private ICustomerViewPresenter iCustomerViewPresenter;
    private Activity activity;
    private String visitType = "", customerNumber = "", filterType = "", statusId = "", statusName = "", delvStatusId = "", delvStatusName = "";
    private boolean isErrorFromBackend = false;
    private String searchText = "";
    private String salesDistrictId = "";
    private String mStrCustName = "";
    private String beatGuid = "";
    private String cpGuid = "";
    private ArrayList<RetailerBean> alRSCHList = null;
    private int countPerRequest = 50;


    public RetailerApprovalPresenter(Context context, Activity activity, ICustomerViewPresenter iCustomerViewPresenter, String visitType, @NonNull String customerNumber, String salesDistrictId, String cpGuid) {
        this.context = context;
        this.iCustomerViewPresenter = iCustomerViewPresenter;
        this.retailerArrayList = new ArrayList<>();
//        this.cpGrp4Desc = new HashMap<>();
        this.searchBeanArrayList = new ArrayList<>();
        this.visitType = visitType;
        this.activity = activity;
        this.customerNumber = customerNumber;
        this.salesDistrictId = salesDistrictId;
        this.cpGuid = cpGuid;
    }


    @Override
    public void onRequestError(int i, Exception e) {
        ErrorBean errorBean = Constants.getErrorCode(i, e, context);
        if (iCustomerViewPresenter != null) {
            iCustomerViewPresenter.hideProgressDialog();
        }
        if (errorBean.hasNoError()) {
            isErrorFromBackend = true;
            if (i == Operation.OfflineRefresh.getValue()) {
                Constants.isSync = false;
                if (iCustomerViewPresenter != null) {
                    iCustomerViewPresenter.hideProgressDialog();
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(
                            context, R.style.MyTheme);
                    builder.setMessage(context.getString(R.string.msg_error_occured_during_sync))
                            .setCancelable(false)
                            .setPositiveButton(context.getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                        }
                                    });

                    builder.show();
                }
            }

        } else if (errorBean.isStoreFailed()) {
            if (UtilConstants.isNetworkAvailable(context)) {
                Constants.isSync = true;
                if (iCustomerViewPresenter != null) {
                    iCustomerViewPresenter.showProgressDialog();
                }
                new RefreshAsyncTask(context, "", this).execute();
            } else {
                Constants.isSync = false;
                if (iCustomerViewPresenter != null) {
                    iCustomerViewPresenter.hideProgressDialog();
                    Constants.displayMsgReqError(errorBean.getErrorCode(), context);
                }
            }
        } else {
            Constants.isSync = false;
            if (iCustomerViewPresenter != null) {
                iCustomerViewPresenter.hideProgressDialog();
                Constants.displayMsgReqError(errorBean.getErrorCode(), context);
            }
        }
    }

    @Override
    public void onRequestSuccess(int i, String s) throws ODataException, OfflineODataStoreException {
        if (i == Operation.OfflineRefresh.getValue()) {
            Constants.updateLastSyncTimeToTable(context, alAssignColl);
            Constants.isSync = false;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (iCustomerViewPresenter != null) {
                        iCustomerViewPresenter.hideProgressDialog();
                      //  AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, activity, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE);
                    }
                }
            });
        } else if (i == Operation.GetStoreOpen.getValue() && OfflineManager.isOfflineStoreOpen()) {
            Constants.isSync = false;
            try {
                OfflineManager.getAuthorizations(context);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            Constants.setSyncTime(context);
            if (iCustomerViewPresenter != null) {
                iCustomerViewPresenter.hideProgressDialog();
                //AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, activity, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE);
            }
        }
    }

    @Override
    public void onFilter() {
        if (iCustomerViewPresenter != null) {
            iCustomerViewPresenter.openFilter(filterType, statusId, delvStatusId);
        }
    }

    @Override
    public void onSearch(String searchText) {
        if (!this.searchText.equalsIgnoreCase(searchText)) {
            this.searchText = searchText;
            onSearchQuery(searchText);
        }
    }

    @Override
    public void onRefresh() {
        onRefreshRetailerList();
    }

    @Override
    public void startFilter(int requestCode, int resultCode, Intent data) {
        filterType = data.getStringExtra(DateFilterFragment.EXTRA_DEFAULT);

//        requestSOList(startDate, endDate);
        displayFilterType();
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
            if (iCustomerViewPresenter != null) {
                iCustomerViewPresenter.setFilterDate(statusDesc);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadAsyncTask(String beatGuid) {
        this.beatGuid = beatGuid;
        retailerArrayList.clear();
        requestRetApprovalList();
    }

    @Override
    public void getRetailerDetails(RetailerBean retailerBean) {

        String qry = Constants.ChannelPartners + "(guid'" + retailerBean.getCPGUID() + "')?$expand=CPDMSDivisions";
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_RESOURCE_PATH, qry);
        bundle.putInt(Constants.BUNDLE_REQUEST_CODE, 2);
        bundle.putInt(Constants.BUNDLE_OPERATION, Operation.GetRequest.getValue());
        bundle.putBoolean(Constants.BUNDLE_SESSION_REQUIRED, false);
        bundle.putBoolean(Constants.BUNDLE_SESSION_URL_REQUIRED, false);
        try {
            if (iCustomerViewPresenter != null)
                iCustomerViewPresenter.showProgressDialog();
            OnlineManager.requestOnline(this, bundle, context);
        } catch (Exception e) {
            LogManager.writeLogError(Constants.error_txt1 + " : " + e.getMessage());
            e.printStackTrace();
        }

    }


    /**
     * Searc query to update the retailerList
     *
     * @param searchText
     */
    private void onSearchQuery(String searchText) {
        this.searchText = searchText;
        searchBeanArrayList.clear();
        boolean isCustomerID = false;
        boolean isCustomerName = false;
        boolean isCity = false;
        if (retailerArrayList != null) {
            if (TextUtils.isEmpty(searchText)) {
                for (RetailerBean retailerBean : retailerArrayList) {
                    ArrayList<RetailerBean> tempArr = retailerBean.getItemList();
                    retailerBean.setRetailerCount(String.valueOf(tempArr.size()));
                    searchBeanArrayList.add(retailerBean);
                    searchBeanArrayList.addAll(tempArr);
                }
            } else {

                try {
                    for (RetailerBean item : retailerArrayList) {
                        ArrayList<RetailerBean> tempRetailerList = new ArrayList<>();
                        for (RetailerBean retailerBean : item.getItemList()) {
                            if (!TextUtils.isEmpty(searchText)) {
                                isCustomerID = retailerBean.getCPNo().toLowerCase().contains(searchText.toLowerCase());
                                isCustomerName = retailerBean.getRetailerName().toLowerCase().contains(searchText.toLowerCase());
                                isCity = retailerBean.getCity().toLowerCase().contains(searchText.toLowerCase());
                            } else {
                                isCustomerID = true;
                                isCustomerName = true;
                                isCity = true;
                            }
                            if (isCustomerID || isCustomerName || isCity) {
                                tempRetailerList.add(retailerBean);
                            }
                        }
                        if (tempRetailerList.size() > 0) {
                            item.setRetailerCount(String.valueOf(tempRetailerList.size()));
                            searchBeanArrayList.add(item);
                            searchBeanArrayList.addAll(tempRetailerList);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (iCustomerViewPresenter != null) {
            iCustomerViewPresenter.searchResult(searchBeanArrayList);
        }
    }

    /**
     * refreshing the RetailerList Online
     */
    private void onRefreshRetailerList() {
        alAssignColl = new ArrayList<>();
        String concatCollectionStr = "";
        if (UtilConstants.isNetworkAvailable(context)) {
            alAssignColl.clear();
            alAssignColl.addAll(SyncUtils.getFOS());
            alAssignColl.addAll(SyncUtils.getBeatCollection());
            alAssignColl.add(Constants.ConfigTypsetTypeValues);
            concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(alAssignColl);

            if (Constants.iSAutoSync) {
                if (iCustomerViewPresenter != null) {
                    iCustomerViewPresenter.hideProgressDialog();
                    iCustomerViewPresenter.displayMsg(context.getString(R.string.alert_auto_sync_is_progress));
                }
            } else {
                try {
                    Constants.isSync = true;
                    // progressDialog = Constants.showProgressDialog(context, "", context.getString(R.string.msg_sync_progress_msg_plz_wait));
                    new RefreshAsyncTask(context, concatCollectionStr, this).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (iCustomerViewPresenter != null) {
                        iCustomerViewPresenter.hideProgressDialog();
                        iCustomerViewPresenter.displayMsg(e.getMessage());
                    }
                }
            }
        } else {
            if (iCustomerViewPresenter != null) {
                iCustomerViewPresenter.hideProgressDialog();
                iCustomerViewPresenter.displayMsg(context.getString(R.string.no_network_conn));
            }
        }
    }


    @Override
    public void responseSuccess(ODataRequestExecution oDataRequestExecution, List<ODataEntity> entities, int operation, int requestCode, String resourcePath, Bundle bundle) {
        int type = bundle != null ? bundle.getInt(Constants.BUNDLE_REQUEST_CODE) : 0;
        switch (type) {

            case 1:
                try {
                    retailerArrayList = OfflineManager.getRetailerApprovalList(entities);
                    Log.d(TAG, "Ret Approval List Loaded");

                } catch (Exception e) {
                    e.printStackTrace();
                    LogManager.writeLogError(Constants.error_txt + " : " + e.getMessage());
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (iCustomerViewPresenter != null) {
                            iCustomerViewPresenter.hideProgressDialog();
                        }
                        onSearchQuery(searchText);
                    }
                });
                break;
            case 2:

                /*try {// TODO: 19-12-2018 Hided for version upgrade
                    retailerBean = OnlineManager.getRetailerApprovalDetails(oDataRequestExecution,retailerBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (iCustomerViewPresenter != null) {
                            iCustomerViewPresenter.hideProgressDialog();
                        }
                        iCustomerViewPresenter.retailerDetails(retailerBean);
                    }
                });
                break;
        }


    }

    @Override
    public void responseFailed(ODataRequestExecution oDataRequestExecution, int operation, int requestCode, String resourcePath, String errorMsg, Bundle bundle) {
        try {
            displayErrorMsg(errorMsg);

        } catch (Exception e) {
            LogManager.writeLogError(Constants.error_txt + " : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayErrorMsg(String errorMsg) {
        if (iCustomerViewPresenter != null) {
            iCustomerViewPresenter.hideProgressDialog();
            ConstantsUtils.displayErrorDialog(context, errorMsg);
        }
    }

    /*private void openStore() {
        if (iCustomerViewPresenter != null) {
            iCustomerViewPresenter.showProgressDialog();
        }
//        if (store == null) {
        Log.e("Main", "opening store for user profile ");
        try {
           *//* OnlineStoreListener openListener = OnlineStoreListener.getInstance();
            OnlineODataStore store = openListener.getStore();
            if (store != null) {// TODO: 19-12-2018 Hided for version upgrade
                requestRetApprovalList();
            } else {*//*
                new OpenOnlineManagerStore(context, new AsyncTaskCallBack() {
                    @Override
                    public void onStatus(boolean status, String values) {
                        if (iCustomerViewPresenter != null) {
                            iCustomerViewPresenter.hideProgressDialog();
                        }
                        if (status) {

                            requestRetApprovalList();
                        } else {
                            ConstantsUtils.displayShortToast(context, values);
                        }
                    }
                }).execute();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }*/
    private void requestRetApprovalList() {
        if (UtilConstants.isNetworkAvailable(context)) {
//                new LoadData().execute();
            Log.d(TAG, " Before Getting SO List");
            getDataFromDB(context);
            isRefresh = false;
        } else {
            ConstantsUtils.dialogBoxWithButton(context, "", context.getString(R.string.no_network_conn), context.getString(R.string.ok), "", null);
        }


    }


    private void getDataFromDB(Context mContext) {
        String qry = "";
        iCustomerViewPresenter.showProgressDialog();
       /* Bundle bundle = new Bundle();
        if (comingFrom == 1)//Contract approval
            bundle.putString(Constants.BUNDLE_RESOURCE_PATH, Constants.Tasks + "/?$select=InstanceID,EntityKey,EntityDate1,EntityKeyID,EntityKeyDesc,EntityCurrency,EntityValue1,EntityAttribute1,EntityType,PriorityNumber&$filter=" + Constants.EntityType + "+eq+'CONTRACT'");
        else //SO approval
            bundle.putString(Constants.BUNDLE_RESOURCE_PATH, Constants.Tasks + "/?$select=InstanceID,EntityKey,EntityDate1,EntityKeyID,EntityKeyDesc,EntityCurrency,EntityValue1,EntityAttribute1,EntityType,PriorityNumber&$filter=" + Constants.EntityType + "+eq+'CP'");
        bundle.putInt(Constants.BUNDLE_REQUEST_CODE, 1);
        bundle.putInt(Constants.BUNDLE_OPERATION, Operation.GetRequest.getValue());
        bundle.putBoolean(Constants.BUNDLE_SESSION_REQUIRED, true);
        bundle.putBoolean(Constants.BUNDLE_SESSION_URL_REQUIRED, true);
        try {
            OnlineManager.requestOnline(this, bundle, mContext);
        } catch (Exception e) {
            LogManager.writeLogError(Constants.error_txt1 + " : " + e.getMessage());
            e.printStackTrace();
            iCustomerViewPresenter.hideProgressDialog();

        }*/
        if (comingFrom == 1)//Contract approval
            qry = Constants.Tasks + "/?$select=InstanceID,EntityKey,EntityDate1,EntityKeyID,EntityKeyDesc,EntityCurrency,EntityValue1,EntityAttribute1,EntityType,PriorityNumber&$filter=" + Constants.EntityType + "+eq+'CONTRACT'";
        else //SO approval
            qry = Constants.Tasks + "/?$select=InstanceID,EntityKey,EntityDate1,EntityKeyID,EntityKeyDesc,EntityCurrency,EntityValue1,EntityAttribute1,EntityType,PriorityNumber&$filter=" + Constants.EntityType + "+eq+'CP'";

        OnlineManager.doOnlineGetRequest(qry, context, event -> {
            if (event.getResponseStatusCode() == 200) {
                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(responseBody);
                    JSONObject dObject = jsonObj.getJSONObject("d");
                    JSONArray resultArray = dObject.getJSONArray("results");
                    retailerArrayList = OfflineManager.getRetailerApprovalList(resultArray);
                    if (iCustomerViewPresenter != null) {
                        iCustomerViewPresenter.hideProgressDialog();
                    }
                    onSearchQuery(searchText);
                } catch (JSONException e) {
                    e.printStackTrace();
                    displayErrorMsg(responseBody);
                }
            } else {
                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                displayErrorMsg(responseBody);
            }
        }, iError -> {
            iError.printStackTrace();
            Log.d("OnlineManager", "getUserRollInfo: ");
            displayErrorMsg(iError.getMessage());
        });
    }


}

package com.arteriatech.ss.msec.iscan.v4.reports.returnorder.invoiceselection;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.store.OnlineODataInterface;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.mbo.CompetitorBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.ROCreateBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.ReturnOrderBean;
import com.arteriatech.ss.msec.iscan.v4.reports.invoicelist.InvoiceListBean;
import com.arteriatech.ss.msec.iscan.v4.reports.returnorder.create.ROFilterDialogFragment;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.sap.smp.client.odata.ODataDuration;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.store.ODataRequestExecution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddInvoiceItemCreatePresenterImpl implements AddInvoiceItemCreatePresenter, OnlineODataInterface, UIListener {
    // data members
    private Context mContext = null;
    private AddInvoiceItemCreateView addROCreateView = null;
    private boolean isSessionRequired = false;
    private Activity activity;
    private ArrayList<ReturnOrderBean> list;
    private ArrayList<ReturnOrderBean> searchBeanArrayList = new ArrayList<>();
    private String searchText = "";
    private String mStrSelBrandId = "";
    private String mStrSelCategoryId = "";
    private String mStrSelOrderMaterialID = "";
    HashMap<String, String> mapUOM =null;
    private ROCreateBean roCreateBean;
    private ODataDuration mStartTimeDuration;
    private Activity mActivity;
    /**
     * @desc parameterized constructor to initialize required fields
     */
    public AddInvoiceItemCreatePresenterImpl(Context mContext, AddInvoiceItemCreateView roCreateView, boolean isSessionRequired, Activity mActivity, ROCreateBean roCreateBean) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.addROCreateView = roCreateView;
        this.isSessionRequired = isSessionRequired;
        this.mStartTimeDuration = UtilConstants.getOdataDuration();
        this.roCreateBean = roCreateBean;
    }
    @Override
    public void onRequestError(int i, Exception e) {

    }

    @Override
    public void onRequestSuccess(int i, String s) throws ODataException, OfflineODataStoreException {

    }

    @Override
    public void responseSuccess(ODataRequestExecution oDataRequestExecution, List<ODataEntity> list, Bundle bundle) {

    }

    @Override
    public void responseFailed(ODataRequestExecution oDataRequestExecution, String s, Bundle bundle) {

    }

    @Override
    public void onStart() {
        addROCreateView.displayList(list);
    }

    @Override
    public void getInvoiceList() {
        if (addROCreateView != null) {
            addROCreateView.showProgressDialog();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                getDataFromDB();

                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (addROCreateView != null) {
                            addROCreateView.hideProgressDialog();

                            addROCreateView.displayList(list);
                        }
                    }
                });
            }
        }).start();


       /* new Thread(new Runnable() {
            @Override
            public void run() {
                getDataFromDB();
                ((Activity) mActivity).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (addROCreateView != null) {
                            addROCreateView.hideProgressDialog();
                            addROCreateView.displayList(list);
                        }
                    }
                });
            }
        }).start();*/
    }
    private void getDataFromDB() {
        String qry = Constants.ConfigTypesetTypes + "?$filter=" + Constants.Typeset + " eq '" + Constants.UOMNO0 + "' ";
        try {
            mapUOM =new HashMap<>();
            mapUOM = OfflineManager.getUOMMapVal(qry);
        } catch (OfflineODataStoreException e) {
            e.printStackTrace();
        }
        String[][] mArrayInvoiceTypeId = OfflineManager.getInVoidTypeId();
        String invQry= Constants.SSINVOICES + "?$filter=" + Constants.SoldToCPGUID + " eq guid'"
                + roCreateBean.getCPGUID()+ "' and "+Constants.StatusID+" eq '03' and "+Constants.InvoiceTypeID+" ne '"+mArrayInvoiceTypeId[0][0]+"'";//
        ArrayList<InvoiceListBean> alInvList=null;

        String mStrInvListQry = Constants.makeInvoiceListQry(alInvList,Constants.InvoiceGUID);
        List<ODataEntity> entities=null;
        if(mStrInvListQry!=null && !mStrInvListQry.equalsIgnoreCase("")){
            try {
                entities = Constants.getListEntities(Constants.SSInvoiceItemDetails + "?$filter= " + mStrInvListQry  ,OfflineManager.offlineStore);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            list =new ArrayList<>();
            list = OfflineManager.getROInvoiceList(entities, list,mapUOM);
        } catch (OfflineODataStoreException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {

    }

    @Override
    public boolean validateFields(ROCreateBean competitorBean) {
        return false;
    }

    @Override
    public boolean isValidMargin(String margin) {
        return false;
    }

    @Override
    public void onAsignData(CompetitorBean competitorBean) {

    }

    @Override
    public void onSaveData() {

    }

    @Override
    public void onSearch(String query) {
        this.searchText = query;
        onFilter();
    }

    private void onFilter() {
        searchBeanArrayList.clear();
        boolean brandStatus = false;
        boolean categoryStatus = false;
        boolean omgStatus = false;
        boolean soDelStatus = false;
        boolean soSearchStatus = false;
        if (list != null) {
            if (TextUtils.isEmpty(searchText) && (TextUtils.isEmpty(mStrSelBrandId) || mStrSelBrandId.equalsIgnoreCase(Constants.None)) && (TextUtils.isEmpty(mStrSelCategoryId) || mStrSelCategoryId.equalsIgnoreCase(Constants.None)) && (TextUtils.isEmpty(mStrSelOrderMaterialID) || mStrSelOrderMaterialID.equalsIgnoreCase(Constants.None))) {
                searchBeanArrayList.addAll(list);
            } else {
                for (ReturnOrderBean item : list) {
                    if (!TextUtils.isEmpty(mStrSelBrandId) && !mStrSelBrandId.equalsIgnoreCase(Constants.None)) {
                        brandStatus = item.getBrand().toLowerCase().contains(mStrSelBrandId.toLowerCase());
                    } else {
                        brandStatus = true;
                    }
                    if (!TextUtils.isEmpty(mStrSelCategoryId) && !mStrSelCategoryId.equalsIgnoreCase(Constants.None)) {
                        categoryStatus = item.getProductCategoryID().toLowerCase().contains(mStrSelCategoryId.toLowerCase());
                    } else {
                        categoryStatus = true;
                    }
                    if (!TextUtils.isEmpty(mStrSelOrderMaterialID) && !mStrSelOrderMaterialID.equalsIgnoreCase(Constants.None)) {
                        omgStatus = item.getOrderMaterialGroupID().toLowerCase().contains(mStrSelOrderMaterialID.toLowerCase());
                    } else {
                        omgStatus = true;
                    }

                    if (!TextUtils.isEmpty(searchText)) {
                        soSearchStatus = (item.getMaterialDesc().toLowerCase().contains(searchText.toLowerCase()) || item.getMaterialNo().toLowerCase().contains(searchText.toLowerCase()));
                    } else {
                        soSearchStatus = true;
                    }
                     if (brandStatus && categoryStatus && omgStatus && soSearchStatus)
                        searchBeanArrayList.add(item);
                }
            }
        }
        if (addROCreateView != null) {
            addROCreateView.searchResult(searchBeanArrayList);
        }
    }

    @Override
    public void onFragmentInteraction(String brand, String brandName, String category, String categoryName, String creskuGrp, String creskuGrpName) {
        this.mStrSelBrandId = brand;
        this.mStrSelCategoryId = category;
        this.mStrSelOrderMaterialID = creskuGrp;
        filterType(brandName, categoryName, creskuGrpName);
        onFilter();
    }

    @Override
    public Bundle openFilter() {
        Bundle bundle = new Bundle();
        bundle.putString(ROFilterDialogFragment.KEY_BRAND, mStrSelBrandId);
        bundle.putString(ROFilterDialogFragment.KEY_CATEGORY, mStrSelCategoryId);
        bundle.putString(ROFilterDialogFragment.KEY_SKUGRP, mStrSelOrderMaterialID);
        return bundle;
    }

    @Override
    public void sendResultToOtherActivity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<ReturnOrderBean> finalSendResult = new ArrayList<>();
                if (list != null && list.size() > 0) {
                    for (ReturnOrderBean returnOrderBean : list) {
                        if (returnOrderBean.getSelected()) {
                            finalSendResult.add(returnOrderBean);
                        }
                    }
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalSendResult.size() > 0) {
                                roCreateBean.setRoList(finalSendResult);

                            } else {
                                if (addROCreateView!=null){
                                    addROCreateView.showMessage(mContext.getString(R.string.validation_sel_atlest_one_material),false);
                                }
                            }
                        }
                    });
                }

            }
        }).start();
    }

    private void filterType(String brandName, String categoryName, String creskuGrpName) {
        try {
            String filteredResult = "";
            if (!TextUtils.isEmpty(brandName) && !brandName.equalsIgnoreCase(Constants.None)) {
                filteredResult = brandName;
            }
            if (!TextUtils.isEmpty(categoryName) && !categoryName.equalsIgnoreCase(Constants.None)) {
                filteredResult = filteredResult + ", " + categoryName;
            }
            if (!TextUtils.isEmpty(creskuGrpName) && !creskuGrpName.equalsIgnoreCase(Constants.None)) {
                filteredResult = filteredResult + ", " + creskuGrpName;
            }
            if (addROCreateView != null) {
                addROCreateView.setFilterDate(filteredResult);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * @desc adding return order requests
     */
    private void getRequests() {
    }
}

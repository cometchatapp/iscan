package com.arteriatech.ss.msec.iscan.v4.retailercreate;

import android.content.Intent;

import com.arteriatech.ss.msec.iscan.v4.mbo.RetailerClassificationBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.RetailerCreateBean;

/**
 * Created by e10526 on 21-04-2018.
 *
 */

public interface RetailerCreatePresenter {
    void onStart();


    void onDestroy();

    boolean validateFields(RetailerCreateBean retailerBean);
    boolean validateDMSDiv(RetailerCreateBean retailerBean);
    boolean validateDMSDivFields(RetailerClassificationBean retailerClassificationBean);

    void requestCPType();

    void onReqSalesData();
    void onReqDMSDivsion(String mStrDistId);
    void onReqGrp2byGrpOne(String mStrTypeValue);
    void onReqGrp3byGrpTwo(String mStrTypeValue);

    void onAsignData(String save, String strRejReason, String strRejReasonDesc, RetailerCreateBean retailerBean);
    void approveData(String ids, String description, String approvalStatus);
    void onSaveData(RetailerCreateBean retailerDefCreateBean);

    void openCameraIntent();

    void onActivityResult(int requestCode, int resultCode, Intent data);
    public void getRetailerGroupList(String CPGUI);
}

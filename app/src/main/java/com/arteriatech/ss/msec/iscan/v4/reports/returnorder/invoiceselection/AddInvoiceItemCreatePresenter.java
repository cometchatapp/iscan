package com.arteriatech.ss.msec.iscan.v4.reports.returnorder.invoiceselection;

import android.os.Bundle;

import com.arteriatech.ss.msec.iscan.v4.mbo.CompetitorBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.ROCreateBean;

public interface AddInvoiceItemCreatePresenter {
    void onStart();
    void getInvoiceList();

    void onDestroy();

    boolean validateFields(ROCreateBean competitorBean);

    boolean isValidMargin(String margin);

    void onAsignData(CompetitorBean competitorBean);

    void onSaveData();

    void onSearch(String query);

    void onFragmentInteraction(String brand, String brandName, String category, String categoryName, String creskuGrp, String creskuGrpName);

    Bundle openFilter();

    void sendResultToOtherActivity();
}

package com.arteriatech.ss.msec.iscan.v4.outlet.outletdetails;

import java.util.ArrayList;
import java.util.List;

public interface IOutletDetailsView {
    void showMessage(String message);
    void showProgress();
    void showProgress(String message);
    void refreshLast6Invoice(List ssInvoiceList);
    void refreshSalesSummary(ArrayList<SummaryBean> summaryList,String strLastOrderValue);
    void refreshULPOSummary();
    void hideProgress();
}

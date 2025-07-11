package com.arteriatech.ss.msec.iscan.v4.reports.returnorder.invoiceselection;

import com.arteriatech.ss.msec.iscan.v4.mbo.ReturnOrderBean;

import java.util.ArrayList;

public interface AddInvoiceItemCreateView {

    void showProgressDialog(String message);

    void hideProgressDialog();

    void displayMessage(String message);

    void conformationDialog(String message, int from);

    void showMessage(String message, boolean isSimpleDialog);

    void searchResult(ArrayList<ReturnOrderBean> skuSearchList);

    void displayList(ArrayList<ReturnOrderBean> displayList);

    void showProgressDialog();

    void setFilterDate(String filterType);
    void displayRefreshTime(String refreshTime);
}

package com.arteriatech.ss.msec.iscan.v4.reports.invoicelist.invoiceDetails;


import com.arteriatech.ss.msec.iscan.v4.reports.invoicelist.InvoiceListBean;

/**
 * Created by e10769 on 04-07-2017.
 */

public interface InvoiceDetailsView {
    void displayHeaderList(InvoiceListBean invoiceListBean);
    void showProgressDialog(String s);
    void hideProgressDialog();
    void showMessage(String message, boolean isSimpleDialog);

}

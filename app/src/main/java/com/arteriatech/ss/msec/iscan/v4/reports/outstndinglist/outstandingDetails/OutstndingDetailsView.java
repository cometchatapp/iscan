package com.arteriatech.ss.msec.iscan.v4.reports.outstndinglist.outstandingDetails;


import com.arteriatech.ss.msec.iscan.v4.mbo.OutstandingBean;

/**
 * Created by e10769 on 04-07-2017.
 */

public interface OutstndingDetailsView {
    void displayHeaderList(OutstandingBean invoiceListBean);
    void showProgressDialog(String s);
    void hideProgressDialog();
    void showMessage(String message, boolean isSimpleDialog);

}

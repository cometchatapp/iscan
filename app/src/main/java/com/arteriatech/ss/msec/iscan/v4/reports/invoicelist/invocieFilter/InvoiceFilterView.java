package com.arteriatech.ss.msec.iscan.v4.reports.invoicelist.invocieFilter;



import com.arteriatech.ss.msec.iscan.v4.mbo.ConfigTypesetTypesBean;

import java.util.ArrayList;

/**
 * Created by e10860 on 12/2/2017.
 */

public interface InvoiceFilterView {
    void displayList(ArrayList<ConfigTypesetTypesBean> configTypesetTypesBeen, ArrayList<ConfigTypesetTypesBean> configTypesetDeliveryList);

    void showMessage(String message);

    void showProgressDialog();

    void hideProgressDialog();
}

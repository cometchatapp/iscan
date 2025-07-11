package com.arteriatech.ss.msec.iscan.v4.reports.returnorder.create;

import com.arteriatech.ss.msec.iscan.v4.mbo.ConfigTypesetTypesBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.ReturnOrderBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.SKUGroupBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e10526 on 21-04-2018.
 */

public interface ROCreateView {
    void showProgressDialog(String message);

    void hideProgressDialog();

    void displayMessage(String message);

    void conformationDialog(String message, int from);

    void showMessage(String message, boolean isSimpleDialog);

    void displaySO(ArrayList<SKUGroupBean> alCRSSKUGrpList);

    void searchResult(ArrayList<SKUGroupBean> skuSearchList);

    void displayCat(String[][] strCats);

    void displayBrands(String[][] strBrands);

    void displayMustSells(String[][] strMustSells);

    void showProgressDialog();

    void RODetails(ArrayList<ReturnOrderBean> list, String stackOwner, List<ConfigTypesetTypesBean> configTypesetTypesBeanList);

    void setFilterDate(String filterType);

    void roDisplayList(ArrayList<ReturnOrderBean> returnOrderList);
}

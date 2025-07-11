package com.arteriatech.ss.msec.iscan.v4.reports.returnorder.create;

import com.arteriatech.ss.msec.iscan.v4.mbo.BrandBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.SKUGroupBean;

import java.util.ArrayList;

interface ROCreateFilterView {
    void showProgressDialog();

    void hideProgressDialog();

    void showMessage(String message);

    void categoryList(String[][] arrCategory);

    void crsSKUList(ArrayList<SKUGroupBean> arrCrsSku);

    void brandList(ArrayList<BrandBean> arrBrand);
}

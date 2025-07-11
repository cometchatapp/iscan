package com.arteriatech.ss.msec.iscan.v4.reports.outstndinglist;

import android.content.Intent;

import com.arteriatech.ss.msec.iscan.v4.mbo.OutstandingBean;

import java.util.ArrayList;

/**
 * Created by e10847 on 19-12-2017.
 */

public interface OutStndingInvoicePresenter {
    void onFilter();
    void onSearch(String searchText);
    void onRefresh();
    void startFilter(int requestCode, int resultCode, Intent data);
    ArrayList<OutstandingBean>  getInvoiceList();
    void calTotalAmount(ArrayList<OutstandingBean> alOutList);
}

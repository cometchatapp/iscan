package com.arteriatech.ss.msec.iscan.v4.merchandising.list;

import com.arteriatech.ss.msec.iscan.v4.mbo.MerchandisingBean;

import java.util.ArrayList;

public interface MerchandisingListView {
    void showProgress();

    void hideProgress();

    void showMessage(String msg);

    void displayList(ArrayList<MerchandisingBean> alMercBean);

    void onRefreshView();

    void displayLSTSyncTime(String time);
}

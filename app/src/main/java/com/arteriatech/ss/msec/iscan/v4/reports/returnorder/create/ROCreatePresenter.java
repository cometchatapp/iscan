package com.arteriatech.ss.msec.iscan.v4.reports.returnorder.create;

import android.content.Intent;

import com.arteriatech.ss.msec.iscan.v4.mbo.CompetitorBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.ReturnOrderBean;

/**
 * Created by e10526 on 21-04-2018.
 */

public interface ROCreatePresenter {
    void onStart();

    void onDestroy();

    boolean validateFields();

    boolean isValidMargin(String margin);

    void onAsignData(CompetitorBean competitorBean);

    void onSaveData();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void removeItem(ReturnOrderBean retailerStkBean);

    void onSearch(String query);
}

package com.arteriatech.ss.msec.iscan.v4.reports.returnorder.create;

import android.os.Bundle;

import com.arteriatech.ss.msec.iscan.v4.mbo.CompetitorBean;

public interface AddROCreatePresenter {
    void onStart();

    void onDestroy();

    boolean validateFields(CompetitorBean competitorBean);

    boolean isValidMargin(String margin);

    void onAsignData(CompetitorBean competitorBean);

    void onSaveData();

    void onSearch(String query);

    void onFragmentInteraction(String brand, String brandName, String category, String categoryName, String creskuGrp, String creskuGrpName);

    Bundle openFilter();
}

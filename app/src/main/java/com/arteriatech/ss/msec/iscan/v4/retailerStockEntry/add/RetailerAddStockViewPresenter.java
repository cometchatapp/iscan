package com.arteriatech.ss.msec.iscan.v4.retailerStockEntry.add;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arteriatech.ss.msec.iscan.v4.mbo.RetailerStockBean;
import java.util.ArrayList;



public interface RetailerAddStockViewPresenter {
    void initializeUI();
    void initializeClickListeners();
    void initializeObjects();
    void initializeRecyclerViewAdapter(LinearLayoutManager layoutManager);
    void showProgressDialog();
    void hideProgressDialog();
    void showMessage(String message, int status);
    void refreshAdapter(ArrayList<?> arrayList);
    void loadIntentData(Intent intent);
    void searchResult(ArrayList<RetailerStockBean> searchBeanArrayList);

    interface RetailerAddStkPresenter{
        void loadMaterialData();
        void onSearch(String s);
    }

}

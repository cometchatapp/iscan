package com.arteriatech.ss.msec.iscan.v4.retailerapproval;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arteriatech.ss.msec.iscan.v4.mbo.RetailerBean;

import java.util.ArrayList;

/**
 * Created by e10847 on 19-12-2017.
 */

public interface ICustomerViewPresenter<T> {
    void initializeUI(Context context);
    void initializeClickListeners();
    void initializeObjects(Context context);
    void initializeRecyclerViewItems(LinearLayoutManager linearLayoutManager);
    void showProgressDialog();
    void hideProgressDialog();
    void onRefreshData();
    void customersListSync();
    void openFilter(String filterType, String status, String grStatus);
    void searchResult(ArrayList<RetailerBean> retailerSearchList);
    void displayBeat(ArrayList<RetailerBean> alBeat);
    void setFilterDate(String filterType);
    void displayRefreshTime(String refreshTime);
    void displayMsg(String msg);
    void sendSelectedItem(Intent intent);
    void retailerDetails(RetailerBean retailerBean);
}

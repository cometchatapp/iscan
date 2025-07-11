package com.arteriatech.ss.msec.iscan.v4.retailerapproval;

import android.content.Intent;

import com.arteriatech.ss.msec.iscan.v4.mbo.RetailerBean;

/**
 * Created by e10847 on 19-12-2017.
 */

public interface ICustomerPresenter {
    void onFilter();
    void onSearch(String searchText);
    void onRefresh();
    void startFilter(int requestCode, int resultCode, Intent data);
    void loadAsyncTask(String beatID);
    void getRetailerDetails(RetailerBean retailerbean);


}

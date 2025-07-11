package com.arteriatech.ss.msec.iscan.v4.behaviourlist;


import com.arteriatech.ss.msec.iscan.v4.mbo.RetailerBean;

import java.util.ArrayList;

/**
 * Created by e10893 on 25-01-2018.
 */

public interface IBehaviourListView {

    void showMessage(String message);


    void showProgressDialog();

    void hideProgressDialog();

    void searchResult(ArrayList<RetailerBean> CustomerBeanArrayList);

    void displayRefreshTime(String refreshTime);

}
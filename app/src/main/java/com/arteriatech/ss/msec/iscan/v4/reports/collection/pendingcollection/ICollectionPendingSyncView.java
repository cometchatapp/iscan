package com.arteriatech.ss.msec.iscan.v4.reports.collection.pendingcollection;

import com.arteriatech.ss.msec.iscan.v4.mbo.CollectionHistoryBean;

import java.util.ArrayList;

/**
 * Created by e10769 on 29-06-2017.
 */

public interface ICollectionPendingSyncView {
    interface SalesOrderResponse<T>{
        void success(ArrayList<T> success);
        void error(String message);
    }
    void showProgressDialog();
    void onReloadData();
    void hideProgressDialog();
    void showMessaage(String message);
    void openCollDetail(CollectionHistoryBean collectionHistoryBean);

}

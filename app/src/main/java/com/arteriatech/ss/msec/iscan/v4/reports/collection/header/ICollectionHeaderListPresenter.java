package com.arteriatech.ss.msec.iscan.v4.reports.collection.header;

import android.content.Intent;

import com.arteriatech.ss.msec.iscan.v4.mbo.CollectionHistoryBean;

/**
 * Created by e10847 on 07-12-2017.
 */

public interface ICollectionHeaderListPresenter {
    /**
     * This will connect to offline Manager using AsyncTask and return the Result From OfflineManger to View.
     * @param
     */
    void connectToOfflineDB();
    void onStart();
    void onDestroy();
    void onResume();
    void onFilter();
    void onSearch(String searchText);
    void onRefresh();
    void startFilter(int requestCode, int resultCode, Intent data);
    void getRefreshTime();

    void getDetails(CollectionHistoryBean collectionHistoryBean);


}

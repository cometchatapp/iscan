package com.arteriatech.ss.msec.iscan.v4.merchandising.list;

import com.arteriatech.ss.msec.iscan.v4.mbo.MerchandisingBean;

public interface MerchandisingListPresenter {
    void onStart();

    void onDestroy();

    void onItemClick(MerchandisingBean merchandisingBean);

    void onRefresh();

    void onUploadData();
}

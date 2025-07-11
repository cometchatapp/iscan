package com.arteriatech.ss.msec.iscan.v4.mutils.registration;

import androidx.recyclerview.widget.RecyclerView;



/**
 * Created by e10769 on 20-12-2017.
 */

public interface SOCreateQtyTextWatcherInterface {
    void onTextChange(String charSequence, MaterialBean soItemBean, RecyclerView.ViewHolder holder,int position);
}

package com.arteriatech.ss.msec.iscan.v4.retailerStockEntry;

import androidx.recyclerview.widget.RecyclerView;

import com.arteriatech.ss.msec.iscan.v4.mbo.RetailerStockBean;



public interface RetailerStkCrtQtyTxtWtchrInterface {
    void onTextChange(String charSequence, RetailerStockBean stockBean, RecyclerView.ViewHolder holder);
}

package com.arteriatech.ss.msec.iscan.v4.retailerStockEntry;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.R;


public class StockMaterialReviewVH extends RecyclerView.ViewHolder {
    TextView tvMatDesc, tvLandingPrice;
    public StockMaterialReviewVH(View viewItem,RetailerStockCrtQtyTxtWtchr soCreateQtyTextWatcher) {
        super(viewItem);
        tvMatDesc = (TextView) viewItem.findViewById(R.id.tvMatDesc);
        tvLandingPrice = (TextView) viewItem.findViewById(R.id.tvLandingPrice);
    }
}

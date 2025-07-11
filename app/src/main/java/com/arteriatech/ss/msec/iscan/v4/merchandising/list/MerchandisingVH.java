package com.arteriatech.ss.msec.iscan.v4.merchandising.list;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.R;

class MerchandisingVH extends RecyclerView.ViewHolder {
    public TextView tvMerchType, tvtvMerchDate;

    public MerchandisingVH(View view) {
        super(view);
        tvMerchType = (TextView)view.findViewById(R.id.tvMerchType);
        tvtvMerchDate = (TextView)view.findViewById(R.id.tvtvMerchDate);
    }
}

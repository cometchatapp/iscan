package com.arteriatech.ss.msec.iscan.v4.reports.invoicelist;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.R;


/**
 * Created by e10769 on 02-12-2017.
 */

public class SOItemDetailsVH1 extends RecyclerView.ViewHolder {
    public final ImageView ivDelvStatus;
    public final TextView tvMaterialDesc, tvQty, tvAmount;

    public SOItemDetailsVH1(View viewItem, Context mContext) {
        super(viewItem);
        ivDelvStatus = (ImageView)viewItem.findViewById(R.id.beatDistance);
        tvMaterialDesc = (TextView)viewItem.findViewById(R.id.tvMaterialDesc);
        tvQty = (TextView)viewItem.findViewById(R.id.expenseAmount);
        tvAmount = (TextView)viewItem.findViewById(R.id.textViewExpenseTypeDesc);
    }
}

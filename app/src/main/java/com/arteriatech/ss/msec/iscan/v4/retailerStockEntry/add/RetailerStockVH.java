package com.arteriatech.ss.msec.iscan.v4.retailerStockEntry.add;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.arteriatech.ss.msec.iscan.v4.R;

public class RetailerStockVH extends RecyclerView.ViewHolder {
    public CheckBox checkBoxMaterial;
    public RetailerStockVH(View itemView) {
        super(itemView);
        checkBoxMaterial = (CheckBox)itemView.findViewById(R.id.checkBoxMaterial);
    }
}

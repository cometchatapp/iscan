package com.arteriatech.ss.msec.iscan.v4.reports.returnorder.create;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.arteriatech.ss.msec.iscan.v4.R;

public class AddROCreateVH extends RecyclerView.ViewHolder {
    CheckBox cbMaterial;

    public AddROCreateVH(View view) {
        super(view);
        cbMaterial = (CheckBox) view.findViewById(R.id.cbMaterial);
    }
}

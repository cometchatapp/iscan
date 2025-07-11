package com.arteriatech.ss.msec.iscan.v4.mutils.registration;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.R;

/**
 * Created by e10769 on 13-11-2017.
 */

public class SupportVH extends RecyclerView.ViewHolder {
    public final TextView tvMenuText;

    public SupportVH(View itemView) {
        super(itemView);
        tvMenuText = (TextView)itemView.findViewById(R.id.icon_text);
    }
}
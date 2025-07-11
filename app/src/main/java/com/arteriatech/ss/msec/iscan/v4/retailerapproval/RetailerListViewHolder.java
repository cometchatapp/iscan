package com.arteriatech.ss.msec.iscan.v4.retailerapproval;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.R;


/**
 * Created by e10769 on 29-06-2017.
 */

public class RetailerListViewHolder extends RecyclerView.ViewHolder {
    public TextView tvRetailerName, tv_retailer_mob_no, tvRetailerCatTypeDesc, tv_status_color, tv_down_color, tv_address2;
    public ImageView ivMobileNo, iv_expand_icon;
    public ConstraintLayout detailsLayout, mainLayout;
    public TextView tvName, tvGrp3;


    public RetailerListViewHolder(View itemView) {
        super(itemView);
        tvRetailerName = (TextView) itemView.findViewById(R.id.tv_RetailerName);
        ivMobileNo = (ImageView) itemView.findViewById(R.id.iv_mobile);
        tv_retailer_mob_no = (TextView) itemView.findViewById(R.id.tv_retailer_mob_no);
        detailsLayout = (ConstraintLayout) itemView.findViewById(R.id.detailsLayout);
        mainLayout = (ConstraintLayout) itemView.findViewById(R.id.mainLayout);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvGrp3 = (TextView) itemView.findViewById(R.id.tvGrp3);
        //  tvRetailerCatTypeDesc = (TextView) itemView.findViewById(R.id.tv_retailer_cat_type_desc);
        /* tv_status_color = (TextView) itemView.findViewById(R.id.tv_status_color);
         tv_down_color = (TextView) itemView.findViewById(R.id.tv_down_color);*/
        tv_address2 = (TextView) itemView.findViewById(R.id.tv_address2);
        //iv_expand_icon = (ImageView) itemView.findViewById(R.id.iv_expand_icon);
    }
}

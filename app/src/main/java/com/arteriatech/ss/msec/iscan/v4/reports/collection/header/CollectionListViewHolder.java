package com.arteriatech.ss.msec.iscan.v4.reports.collection.header;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.R;


/**
 * Created by e10769 on 29-06-2017.
 */

public class CollectionListViewHolder extends RecyclerView.ViewHolder {
    public TextView /*textViewOrderID,*/textViewCollDate,textViewCollAmt,textViewMaterialName,textViewCollType,textViewPaymentMode,tvDueDays,tv_inv_no,tv_retailer;
    public CollectionListViewHolder(View itemView) {
        super(itemView);
//        textViewOrderID = (TextView) itemView.findViewById(R.id.tv_order_id);
        textViewCollDate = (TextView) itemView.findViewById(R.id.tv_inv_date);
        textViewCollAmt = (TextView) itemView.findViewById(R.id.tv_coll_value);
        textViewCollType= (TextView) itemView.findViewById(R.id.tv_coll_type);
        textViewPaymentMode= (TextView) itemView.findViewById(R.id.tv_payment_mode);
        tv_retailer= (TextView) itemView.findViewById(R.id.tv_retailer);
        tv_inv_no= (TextView) itemView.findViewById(R.id.tv_inv_no);
        tvDueDays= (TextView) itemView.findViewById(R.id.imgStatus);
    }
}

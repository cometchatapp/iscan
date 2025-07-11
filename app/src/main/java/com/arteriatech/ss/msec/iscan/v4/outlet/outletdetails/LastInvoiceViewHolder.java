package com.arteriatech.ss.msec.iscan.v4.outlet.outletdetails;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.arteriatech.ss.msec.iscan.v4.R;


/**
 * Created by e10769 on 29-06-2017.
 */

public class LastInvoiceViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_invoice_no,tv_invoice_date,tv_invoice_day,tv_invoice_ulpo,tv_invoice_amount;
    public LinearLayout ll_invoice_header;
//    public SwipeRevealLayout swipeRemove;
    public View view;

    public LastInvoiceViewHolder(View itemView) {
        super(itemView);
        ll_invoice_header = (LinearLayout) itemView.findViewById(R.id.ll_invoice_header);
        tv_invoice_no = (TextView) itemView.findViewById(R.id.tv_invoice_no);
        tv_invoice_day = (TextView) itemView.findViewById(R.id.tv_invoice_day);
        tv_invoice_ulpo = (TextView) itemView.findViewById(R.id.tv_invoice_ulpo);
        tv_invoice_amount = (TextView) itemView.findViewById(R.id.tv_invoice_amount);
        tv_invoice_date = (TextView) itemView.findViewById(R.id.tv_invoice_date);
    }
}

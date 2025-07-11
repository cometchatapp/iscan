package com.arteriatech.ss.msec.iscan.v4.mutils.sync;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.R;


/**
 * Created by e10769 on 13-01-2017.
 */
public class SyncHistoryViewHolder extends RecyclerView.ViewHolder {
    public TextView tvCollection, tvTime;

    public SyncHistoryViewHolder(View itemView) {
        super(itemView);
        tvCollection = (TextView) itemView.findViewById(R.id.collection_view);
        tvTime = (TextView) itemView.findViewById(R.id.time_view);
    }
}

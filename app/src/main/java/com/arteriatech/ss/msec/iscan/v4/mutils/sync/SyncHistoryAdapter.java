package com.arteriatech.ss.msec.iscan.v4.mutils.sync;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.arteriatech.ss.msec.iscan.v4.R;

import java.util.List;


public class SyncHistoryAdapter extends RecyclerView.Adapter<SyncHistoryViewHolder> {
    private List<SyncHistoryModel> syncHistoryModelList;

    public SyncHistoryAdapter(List<SyncHistoryModel> syncHistoryModelList) {
        this.syncHistoryModelList = syncHistoryModelList;
    }

    @Override
    public SyncHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sync_history_item, parent, false);
        return new SyncHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SyncHistoryViewHolder holder, int position) {
        SyncHistoryModel syncHistoryModel = syncHistoryModelList.get(position);
        holder.tvCollection.setText(syncHistoryModel.getCollections());
        holder.tvTime.setText(syncHistoryModel.getTimeStamp());
    }

    @Override
    public int getItemCount() {
        return syncHistoryModelList.size();
    }
}

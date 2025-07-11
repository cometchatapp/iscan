package com.arteriatech.ss.msec.iscan.v4.mutils.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by e10769 on 29-06-2017.
 */

public interface AdapterInterface<T> {
    void onItemClick(T item, View view, int position);
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, View viewItem);
    void onBindViewHolder(RecyclerView.ViewHolder holder, final int position, T item);
}

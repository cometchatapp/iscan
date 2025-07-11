package com.arteriatech.ss.msec.iscan.v4.mutils.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arteriatech.ss.msec.iscan.v4.mutils.filterlist.SearchFilter;
import com.arteriatech.ss.msec.iscan.v4.mutils.filterlist.SearchFilterInterface;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by e10769 on 29-06-2017.
 */

public class SimpleRecyclerViewTypeAdapter<T> extends RecyclerView.Adapter {
    private ArrayList<T> commonArrayList = new ArrayList<>();
    private int resource;
    private AdapterViewInterface adapterInterface = null;
    private View listView;
    private View noDataFound;
    private Context mContext;

    public SimpleRecyclerViewTypeAdapter(Context mContext, int resource, AdapterViewInterface adapterInterface, View listView, View noDataFound) {
        this.mContext = mContext;
        this.resource = resource;
        this.adapterInterface = adapterInterface;
        this.listView = listView;
        this.noDataFound = noDataFound;
    }

    @Override
    public int getItemViewType(int position) {
        if (adapterInterface != null) {
            return adapterInterface.getItemViewType(position, commonArrayList);
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (adapterInterface != null) {
            View viewItem = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            return adapterInterface.onCreateViewHolder(parent, viewType, viewItem);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final T t = commonArrayList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapterInterface != null) {
                    adapterInterface.onItemClick(t, v, position);
                }
            }
        });
        if (adapterInterface != null) {
            adapterInterface.onBindViewHolder(holder, position, t, commonArrayList);
        }
    }

    @Override
    public int getItemCount() {
        return commonArrayList.size();
    }
    /*refresh adapter*/
    public void refreshAdapter(ArrayList<T> commonArrayList) {
        this.commonArrayList.clear();
        this.commonArrayList.addAll(commonArrayList);
        notifyDataSetChanged();
        if (this.commonArrayList.isEmpty()) {
            if (this.noDataFound != null)
                this.noDataFound.setVisibility(View.VISIBLE);
            if (this.listView != null)
                this.listView.setVisibility(View.GONE);
        } else {
            if (this.noDataFound != null)
                this.noDataFound.setVisibility(View.GONE);
            if (this.listView != null)
                this.listView.setVisibility(View.VISIBLE);
        }
    }
    /*search filter*/
    public void searchFilter(final ArrayList<T> commonArrayList, final SearchFilterInterface searchFilterInterface) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Collection filterList = SearchFilter.filter(commonArrayList, searchFilterInterface);
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshAdapter((ArrayList<T>) filterList);
                    }
                });
            }
        }).start();
    }
}

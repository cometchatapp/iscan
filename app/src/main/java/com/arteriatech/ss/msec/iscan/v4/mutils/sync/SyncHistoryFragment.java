package com.arteriatech.ss.msec.iscan.v4.mutils.sync;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arteriatech.ss.msec.iscan.v4.R;

import java.util.List;

/**
 * Created by e10742 on 6/19/2017.
 */

public class SyncHistoryFragment extends Fragment {

        private RecyclerView recyclerView = null;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View myInflatedView = inflater.inflate(R.layout.fragment_sync_hist, container, false);
            recyclerView = (RecyclerView) myInflatedView.findViewById(R.id.recycler_view);
            List<SyncHistoryModel> syncHistoryModelList = new SyncHistoryDB(getActivity()).getAllRecord();
            if (!syncHistoryModelList.isEmpty()) {
                recyclerView.setHasFixedSize(false);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(linearLayoutManager);
                SyncHistoryAdapter syncHistoryAdapter = new SyncHistoryAdapter(syncHistoryModelList);
                recyclerView.setAdapter(syncHistoryAdapter);
            }
            return myInflatedView;
        }

}

package com.arteriatech.ss.msec.iscan.v4.sync.syncSelectionView;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.mutils.adapter.AdapterInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.adapter.SimpleRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SyncSelectionViewFragment extends Fragment implements AdapterInterface<SyncSelectionViewBean>, View.OnClickListener {
    private ArrayList<SyncSelectionViewBean> syncSelectionViewBeanArrayList = null;
    private RecyclerView recyclerView;
    private Button buttonCancel,buttonOk;
    //    private TextView tvNoRecordFound;
    private SimpleRecyclerViewAdapter<SyncSelectionViewBean> simpleRecyclerViewAdapter;
    private SyncSelectionViewInterface syncSelectionInterface = null;
    private boolean onBind = false;

    public SyncSelectionViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            syncSelectionInterface = (SyncSelectionViewInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            syncSelectionViewBeanArrayList = (ArrayList<SyncSelectionViewBean>) bundle.getSerializable(Constants.EXTRA_BEAN_LIST);
        }
        if (syncSelectionViewBeanArrayList != null) {
            SyncSelectionViewBean syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setAll(true);
            syncSelectionViewBean.setDisplayName("Select All");
            syncSelectionViewBeanArrayList.add(0, syncSelectionViewBean);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_menu_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonOk = view.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
//        tvNoRecordFound = (TextView) view.findViewById(R.id.no_record_found);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        simpleRecyclerViewAdapter = new SimpleRecyclerViewAdapter<SyncSelectionViewBean>(getContext(), R.layout.sync_selection_item, this, recyclerView, null);
        recyclerView.setAdapter(simpleRecyclerViewAdapter);
        simpleRecyclerViewAdapter.refreshAdapter(syncSelectionViewBeanArrayList);
        setHasOptionsMenu(true);
    }

    @Override
    public void onItemClick(SyncSelectionViewBean item, View view, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, View viewItem) {
        return new SyncSelectionVH(viewItem);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, final SyncSelectionViewBean syncSelectionViewBean) {
        ((SyncSelectionVH) holder).cbCollection.setText(syncSelectionViewBean.getDisplayName());
        ((SyncSelectionVH) holder).cbCollection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                syncSelectionViewBean.setChecked(isChecked);
                if (syncSelectionViewBean.isAll()) {
                    selectAllItems(syncSelectionViewBeanArrayList, isChecked);
                }
            }
        });
        onBind = true;
        ((SyncSelectionVH) holder).cbCollection.setChecked(syncSelectionViewBean.isChecked());
        onBind = false;
    }

    private void selectAllItems(ArrayList<SyncSelectionViewBean> syncSelectionViewBeanArrayList, boolean selectStatus) {
        for (SyncSelectionViewBean syncSelectionViewBean : syncSelectionViewBeanArrayList) {
            syncSelectionViewBean.setChecked(selectStatus);
        }
        if (!onBind)
            simpleRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sync, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
          /*  case R.id.menu_sync:
                onSync();
                break;*/
        }
        return true;
    }

    private void onSync() {
        ArrayList<String> selectedCollection = new ArrayList<>();
        for (SyncSelectionViewBean syncSelectionViewBean : syncSelectionViewBeanArrayList) {
            if (syncSelectionViewBean.isChecked()) {
                selectedCollection.addAll(syncSelectionViewBean.getCollectionName());
            }
        }
        if (!selectedCollection.isEmpty()) {
            syncSelectionInterface.onSelectedCollection(selectedCollection);
        } else {
            Constants.showAlert(getString(R.string.plz_select_one_coll), getContext());
        }
    }

    @Override
    public void onClick(View v) {
       /* switch (v.getId()){
            case R.id.buttonOk:
                try {
                    onSync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.buttonCancel:
                if (getActivity()!=null) {
                    getActivity().finish();
                }
                break;
        }*/
    }

}

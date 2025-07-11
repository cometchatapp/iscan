package com.arteriatech.ss.msec.iscan.v4.mutils.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arteriatech.ss.msec.iscan.v4.R;

import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.databinding.ActivitySalesOrderBinding;
import com.arteriatech.ss.msec.iscan.v4.mutils.adapter.AdapterInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.adapter.SimpleRecyclerViewAdapter;
import com.arteriatech.ss.msec.iscan.v4.ui.MaterialDesignSpinner;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SalesOrderActivity extends AppCompatActivity implements SalesView, SOCreateQtyTextWatcherInterface{
    public static Context mContext;
    ActivitySalesOrderBinding binding;
    SalesOrderPresenter presenter;
    SimpleRecyclerViewAdapter<MaterialBean> simpleRecyclerViewAdapter;
    String commonUrl="https://docs.google.com/uc?export=open&id=";
    ArrayList<MaterialBean> totalMatArrayList=new ArrayList<>();
    public static ArrayList<MaterialBean> matList=new ArrayList<>();
    MaterialBean matBean;
    boolean isScrolling=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySalesOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter=new SalesOrderPresenter(SalesOrderActivity.this,SalesOrderActivity.this);

        initializeRv();
        boolean isDataVault=presenter.retrieveMaterials();
        if(!isDataVault) {
            presenter.getMaterials();
        }


        binding.toolbar.txtSkipOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                matList.clear();
                ArrayList<MaterialBean> materialBeanArrayList=new ArrayList<>();

                for(int i=0;i<totalMatArrayList.size();i++){
                    if(!TextUtils.isEmpty(totalMatArrayList.get(i).getQty())&&!totalMatArrayList.get(i).getQty().equalsIgnoreCase("0")){
                        materialBeanArrayList.add(totalMatArrayList.get(i));
                    }
                }

                matList.addAll(materialBeanArrayList);

                Intent intent=new Intent(SalesOrderActivity.this,SalesOrderSummaryActivity.class);
                startActivity(intent);

            }
        });



        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                setScrolling(true); // Set scrolling state to true
            }
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    setScrolling(false); // Set scrolling state to false
                }
            }
        });

    }

    @Override
    public void getMaterialList(ArrayList<MaterialBean> materialBeanArrayList) {
        this.totalMatArrayList=materialBeanArrayList;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                simpleRecyclerViewAdapter.refreshAdapter(materialBeanArrayList);
            }
        });

    }

    @Override
    public void callDataVault() {

        presenter.retrieveMaterials();
    }


    private void initializeRv() {
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(SalesOrderActivity.this);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setNestedScrollingEnabled(true);
        binding.recyclerView.setHasFixedSize(true);

        simpleRecyclerViewAdapter = new SimpleRecyclerViewAdapter<MaterialBean>(SalesOrderActivity.this, R.layout.item_material, new AdapterInterface<MaterialBean>() {
            @Override
            public void onItemClick(MaterialBean item, View view, int position) {

            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, View viewItem) {
                return new MaterialVH(viewItem,new SOCreateQtyTextWatcher());
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, MaterialBean item) {
                matBean=item;
                ((MaterialVH)holder).txtMaterial.setText(item.getMaterialDesc());
                setSpinner(((MaterialVH)holder).spinner,item.getUomList(),item.getUom());
                ((MaterialVH)holder).spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(!isScrolling){
                            item.setUom(item.getUomList().get(i));
                            if(!TextUtils.isEmpty(item.getQty())) {
                                if (item.getUom().equalsIgnoreCase("NOS")) {
                                    double mrpAmount = Double.parseDouble(item.getQty()) * Double.parseDouble(item.getStkConvQty1()) * Double.parseDouble(matBean.getMRP());
                                    item.setMRP(mrpAmount + "");
                                    ((MaterialVH) holder).txtMrp.setText(item.getMRP());
                                } else {
                                    double mrpAmount = Double.parseDouble(item.getQty()) * Double.parseDouble(matBean.getMRP());
                                    item.setMRP(mrpAmount + "");
                                    ((MaterialVH) holder).txtMrp.setText(item.getMRP());
                                }
                                item.setClickable("true");
                            }
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                String urlID = "";
                try {
                    if (!TextUtils.isEmpty(item.getCatalogInfo())) {
                        urlID = item.getCatalogInfo();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                if(!TextUtils.isEmpty(urlID)) {
                    Glide.with(SalesOrderActivity.this)
                            .load(commonUrl + urlID)
                            .into(((MaterialVH) holder).iv_img);
                }else{
                    ((MaterialVH) holder).iv_img.setVisibility(View.VISIBLE);
                }
                ((MaterialVH) holder).soCreateQtyTextWatcher.updateTextWatcher(item, holder,position, SalesOrderActivity.this);
                ((MaterialVH) holder).txtMaterialQty.setText(item.getQty());
                ((MaterialVH)holder).txtMrp.setText(item.getMRP());


            }
        }, binding.recyclerView, null);
        binding.recyclerView.setAdapter(simpleRecyclerViewAdapter);

    }

    private void setSpinner(final MaterialDesignSpinner spinnerView, @NonNull final ArrayList<?> arrayList,String value) {
        if (!arrayList.isEmpty()) {
            ArrayAdapter<String> adapterShipToList = new ArrayAdapter(SalesOrderActivity.this, R.layout.custom_textview,
                    R.id.tvItemValue, arrayList) {
                @Override
                public View getDropDownView(final int position, final View convertView, final ViewGroup parent) {
                    final View v = super.getDropDownView(position, convertView, parent);
                    ConstantsUtils.selectedView(v, spinnerView, position, getContext());
                    return v;
                }
            };

            adapterShipToList.setDropDownViewResource(R.layout.spinnerinside);
            spinnerView.setAdapter(adapterShipToList);
            spinnerView.showFloatingLabel();

            if (value != null) {
                int spinnerPosition = adapterShipToList.getPosition(value);
                spinnerView.setSelection(spinnerPosition);
            }

        }
    }

    @Override
    public void onTextChange(String charSequence, MaterialBean soItemBean, RecyclerView.ViewHolder holder,int position) {
        final double[] orderAmount = {0.0};
        ((MaterialVH)holder).txtMrp.setText(soItemBean.getMRP());
        if(TextUtils.isEmpty(soItemBean.getUom()) || soItemBean.getUom()==null){
            soItemBean.setUom(soItemBean.getUomList().get(0));
            totalMatArrayList.set(position,soItemBean);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i=0;i<totalMatArrayList.size();i++){
                    if(!TextUtils.isEmpty(totalMatArrayList.get(i).getQty())) {
                        orderAmount[0] = orderAmount[0] + Double.parseDouble(totalMatArrayList.get(i).getMRP());
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.tvTotalAmt.setText("Order Amount : "+orderAmount[0]);
                    }
                });

            }
        }).start();



    }

    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
        simpleRecyclerViewAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the views
    }



}

package com.arteriatech.ss.msec.iscan.v4.mutils.registration;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.mutils.adapter.AdapterInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.adapter.SimpleRecyclerViewAdapter;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack;
import com.arteriatech.ss.msec.iscan.v4.mutils.location.LocationInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.location.LocationModel;
import com.arteriatech.ss.msec.iscan.v4.mutils.location.LocationUtils;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;

public class SalesOrderSummaryActivity extends AppCompatActivity implements SalesView{

    RecyclerView recyclerView;
    SimpleRecyclerViewAdapter<MaterialBean> simpleRecyclerViewAdapter;
    String commonUrl="https://docs.google.com/uc?export=open&id=";
    double orderAmount=0.0;
    TextView textViewitemValue,textViewValue;
    Button btnSave;
    SalesOrderPresenter presenter;
    AppCompatTextView txtSkipOrder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.arteriatech.ss.msec.iscan.v4.R.layout.sales_order_summary);

        recyclerView=(RecyclerView)findViewById(com.arteriatech.ss.msec.iscan.v4.R.id.recyclerView);
        textViewitemValue=(TextView)findViewById(R.id.textViewitemValue);
        textViewValue=(TextView)findViewById(R.id.textViewValue);
        txtSkipOrder=(AppCompatTextView) findViewById(R.id.txtSkipOrder);
        btnSave=(Button) findViewById(R.id.buttonSave);

        txtSkipOrder.setVisibility(View.GONE);

        presenter=new SalesOrderPresenter(SalesOrderSummaryActivity.this,SalesOrderSummaryActivity.this);

        initializeRv();
        intiailizeObjects();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.dialogBoxWithCallBack2(SalesOrderSummaryActivity.this, null, "Do you want to save?", "Yes", "No", false, new DialogCallBack() {
                    @Override
                    public void clickedStatus(boolean clickedStatus) {
                        if (clickedStatus) {
                            LogManager.writeLogInfo("Saving Sales Order Data");
//                                    presenter.onSaveSOCreateToDataVault(soCreateBILBean,arrayList);
                            checkGPS(SalesOrderActivity.matList);

                        }
                    }
                });

            }
        });

    }

    private void intiailizeObjects() {
        for(int i=0;i<SalesOrderActivity.matList.size();i++){
            orderAmount=orderAmount+Double.parseDouble(SalesOrderActivity.matList.get(i).getMRP());
        }
        textViewValue.setText(orderAmount+"");

    }

    private void initializeRv() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(SalesOrderSummaryActivity.this);
        recyclerView.setLayoutManager(manager);

        simpleRecyclerViewAdapter = new SimpleRecyclerViewAdapter<MaterialBean>(SalesOrderSummaryActivity.this, R.layout.item_review_material, new AdapterInterface<MaterialBean>() {
            @Override
            public void onItemClick(MaterialBean item, View view, int position) {

            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, View viewItem) {
                return new MaterialVH(viewItem,new SOCreateQtyTextWatcher());
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, MaterialBean item) {
                ((MaterialVH)holder).txtMaterial.setText(item.getMaterialDesc());
                ((MaterialVH)holder).txtUom.setText(item.getUom());

                String urlID = "";
                try {
                    JSONObject jsonObject = new JSONObject(item.getCatalogInfo());
                    if (jsonObject.has("tn")) {
                        urlID = jsonObject.getString("tn");
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                if(!TextUtils.isEmpty(urlID)) {
                    Glide.with(SalesOrderSummaryActivity.this)
                            .load(commonUrl + urlID)
                            .into(((MaterialVH) holder).iv_img);
                }else{
                    ((MaterialVH) holder).iv_img.setVisibility(View.VISIBLE);
                }
                if(!TextUtils.isEmpty(item.getQty())){
                    if(item.getQty()!=null) {
                        ((MaterialVH) holder).txtMaterialQty.setText(item.getQty());
                    }
                }

                ((MaterialVH)holder).txtMrp.setText(item.getMRP());

            }
        }, recyclerView, null);
        recyclerView.setAdapter(simpleRecyclerViewAdapter);

        simpleRecyclerViewAdapter.refreshAdapter(SalesOrderActivity.matList);

    }

    public void checkGPS(ArrayList<MaterialBean> items) {
        if (LocationUtils.isGPSEnabled(SalesOrderSummaryActivity.this)) {
            checkVisitStartLocPermission( items);
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SalesOrderSummaryActivity.this, R.style.DialogTheme);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?").setCancelable(false)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            LocationUtils.checkLocationPermission(SalesOrderSummaryActivity.this, new LocationInterface() {
                                @Override
                                public void location(boolean status, LocationModel location, String errorMsg, int errorCode) {
                                    if (status) {
                                        if (ConstantsUtils.isAutomaticTimeZone(SalesOrderSummaryActivity.this)) {
                                            dialog.dismiss();
                                        } else {
                                            ConstantsUtils.showAutoDateSetDialog(SalesOrderSummaryActivity.this);
                                        }
                                    }
                                }
                            });
                        }
                    }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
            builder.show();
        }
    }

    private void checkVisitStartLocPermission(ArrayList<MaterialBean> items) {
        LogManager.writeLogInfo("Save Order checking Location Permission");
        if (UtilConstants.round(UtilConstants.latitude, 12) == 0 && UtilConstants.round(UtilConstants.longitude, 12) == 0) {
            ProgressDialog pdLoadDialog = Constants.showProgressDialog(this, "", getString(R.string.checking_pemission));
            LocationUtils.checkLocationPermission(SalesOrderSummaryActivity.this, new LocationInterface() {
                @Override
                public void location(boolean status, LocationModel location, String errorMsg, int errorCode) {
                    LogManager.writeLogInfo("Save Order checking Location Permission status : " + status);
                    pdLoadDialog.dismiss();
                    if (status) {

                        if (ConstantsUtils.isAutomaticTimeZone(SalesOrderSummaryActivity.this)) {
                            ProgressDialog pdLoadDialog1 = Constants.showProgressDialog(SalesOrderSummaryActivity.this, "", "Fetching lat and long...");
                            LogManager.writeLogInfo("Save Order AutomaticTimeZone status : true");
                            Constants.getLocation(SalesOrderSummaryActivity.this, new LocationInterface() {
                                @Override
                                public void location(boolean status, LocationModel locationModel, String errorMsg, int errorCode) {
                                    pdLoadDialog1.dismiss();
                                    LogManager.writeLogInfo("Save Order Location status : " + status);
                                  //  presenter.getRetailerDetails();
                                    presenter.onSave(items,orderAmount);
                                }
                            });
                        } else {
                            LogManager.writeLogInfo("Save Order AutomaticTimeZone status : false");
                            ConstantsUtils.showAutoDateSetDialog(SalesOrderSummaryActivity.this);
                        }

                    } else {
                    }
                }
            });
        } else {
           /////// presenter.getRetailerDetails();
            presenter.onSave(items,orderAmount);
//            presenter.onSaveSOCreateToDataVault(header,items);
        }
    }

    @Override
    public void getMaterialList(ArrayList<MaterialBean> materialBeanArrayList) {

    }

    @Override
    public void callDataVault() {

    }
}

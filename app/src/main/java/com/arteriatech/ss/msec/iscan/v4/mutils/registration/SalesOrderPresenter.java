package com.arteriatech.ss.msec.iscan.v4.mutils.registration;

import static com.arteriatech.ss.msec.iscan.v4.common.Constants.REPEATABLE_REQUEST_ID;
import static com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils.getID;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.airbnb.lottie.LottieAnimationView;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.common.MSFAApplication;
import com.arteriatech.ss.msec.iscan.v4.databinding.OrderSuccessDialog1Binding;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.store.OnlineManager;
import com.arteriatech.ss.msec.iscan.v4.ui.DialogFactory;
//import com.example.mylibrary.GolfView;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sap.client.odata.v4.core.GUID;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.odata.exception.ODataException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class SalesOrderPresenter {

    Activity mActivity;
    SalesView salesView;
    String doc_no = "";
    GUID ssoHeaderGuid =null;
    String SSSOGUID = "";
   // JSONObject headerTable = new JSONObject();
    Hashtable<String, String> headerTable = new Hashtable<>();
    JSONObject jsonHeaderObject;
    String orderTime="";
    public SalesOrderPresenter(Activity activity,SalesView mSalesView){
        this.mActivity=activity;
        this.salesView=mSalesView;
    }

    public void getMaterials() {
        showProgressDialog();
        String qry="ZmerdiseSet";
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences(Constants.PREFS_NAME, 0);

        OnlineManager.doZCommonGetRequest(qry,mActivity,iReceiveEvent -> {
           if(iReceiveEvent.getResponseStatusCode()==200){

               String responseBody = IReceiveEvent.Util.getResponseBody(iReceiveEvent.getReader());
               System.out.println("response "+ responseBody);
               try {
                   JSONObject jsonObject=new JSONObject(responseBody);
                   JSONObject objectD = jsonObject.optJSONObject("d");
                   JSONArray jsonArray = objectD.getJSONArray("results");
                   ArrayList<JSONObject> dataForDV =  new ArrayList<>();
                   if(jsonArray.length()>0) {
                       for (int i = 0, len = jsonArray.length(); i < len; i++) {
                           JSONObject obj = jsonArray.getJSONObject(i);
                           // Do your removals
                           obj.remove("__metadata");
                           String obj1=obj.getString("CatalogInfo");
                           if(!TextUtils.isEmpty(obj1)) {
                               JSONObject jsonObject1 = new JSONObject(obj1);
                               String imageLink = jsonObject1.getString("tn");
                               obj.put("CatalogInfo", imageLink);
                           }
                           dataForDV.add(jsonArray.getJSONObject(i));
                           // etc.
                       }
                   }

                   String doc_no = "BILMaterials";
                   Hashtable<String, String> masterHeaderTable = new Hashtable<>();

                   Gson gson1 = new Gson();
                   String jsonFromMap = "";
                   try {
                       jsonFromMap = gson1.toJson(dataForDV.toString());
                   } catch (Exception e) {
                       e.printStackTrace();
                   }

                   masterHeaderTable.put(Constants.BILMaterials, jsonFromMap);
                   masterHeaderTable.put(Constants.EntityType, Constants.BILMaterials);
                 //  setTemp.add(doc_no);

                   // System.out.println("docNo Secondary 123 " + doc_no);
                   JSONObject jsonHeaderObject = new JSONObject(masterHeaderTable);
                   ConstantsUtils.storeInDataVault(Constants.SPCDV03072025, jsonHeaderObject.toString(), mActivity);

                   salesView.callDataVault();


               } catch (JSONException e) {
                   throw new RuntimeException(e);
               }



           }else{
               hideProgressDialog();
           }
        },iError->{
            hideProgressDialog();
        });
    }

    public void showProgressDialog(){
        new DialogFactory.Progress(mActivity).setMessage("Loading materials.Please wait").show();
    }

    public void hideProgressDialog(){
        new DialogFactory.Progress(mActivity).hide();
    }

    public void onSave(ArrayList<MaterialBean> items,double orderAmount) {
        new DialogFactory.Progress(mActivity).setMessage("Saving sales order please wait").show();
        HashSet<String> materialList=new HashSet<>();
        ssoHeaderGuid = GUID.newRandom();
        SSSOGUID = ssoHeaderGuid.toString36().toUpperCase();
        try {
            headerTable.put(Constants.SSSOGuid, ssoHeaderGuid.toString36().toUpperCase());

        headerTable.put(Constants.OrderNo, String.valueOf(getID()));
        headerTable.put(Constants.Source, "MOBILE");
        headerTable.put(Constants.OrderEntry,"BEATVISIT");
        headerTable.put(Constants.OrderOrigin,"G");
        headerTable.put(Constants.OrderType, "000010");
        headerTable.put(Constants.OrderTypeDesc, "");
        headerTable.put(Constants.BeatName,"");
        headerTable.put(Constants.BeatGuid,"00000000-0000-0000-0000-000000000000");

        headerTable.put(Constants.OrderDate, UtilConstants.getNewDateTimeFormat());
        headerTable.put(Constants.TestRun,"M");
        headerTable.put(Constants.PONo, "");
        headerTable.put(Constants.PODate, UtilConstants.getNewDateTimeFormat());

        headerTable.put(Constants.FromCPGUID, "");
        headerTable.put(Constants.FromCPNo, "");
        headerTable.put(Constants.FromCPName, "");
        headerTable.put(Constants.FromCPTypId, "01");
        headerTable.put(Constants.CPType,"20");
        headerTable.put(Constants.FromCPTypDs, "Customer");
       // headerTable.put(Constants.CPGUID, "6D8C7B84-A1B7-47CA-8CCA-D5A6EE473663");
       // headerTable.put(Constants.CPNo, "WB-4977351");
        headerTable.put(Constants.CPGUID, "00000000-0000-0000-0000-000000000000");
        headerTable.put(Constants.CPNo, "GJ-5003432");

       // if (retailerSelectionBean.getOwnerName()!=null) {
            headerTable.put(Constants.CPName, "");
       // }
       // if (retailerSelectionBean.getCPTypeDesc()!=null) {
            headerTable.put(Constants.CPTypeDesc, "Retailer");
        //}
       // if (retailerSelectionBean.getCPGUID()!=null) {
           // headerTable.put(Constants.SoldToCPGUID,  "6D8C7B84-A1B7-47CA-8CCA-D5A6EE473663");
            headerTable.put(Constants.SoldToCPGUID,  "00000000-0000-0000-0000-000000000000");
        //}
       // if (retailerSelectionBean.getCPNo()!=null) {
            headerTable.put(Constants.SoldToId, "GJ-5003432");
       // }

       // if (retailerSelectionBean.getName()!=null) {
           //// String sold=retailerSelectionBean.getName();
           // String soldToDesc=sold.substring(0,Math.min(40,sold.length()));
            headerTable.put(Constants.SoldToDesc, "");
        //}
        //if (retailerSelectionBean.getCPTypeID()!=null) {
            headerTable.put(Constants.SoldToType, "20");
        //}
        headerTable.put(Constants.SPGUID, "00000000-0000-0000-0000-000000000000");
        headerTable.put(Constants.Status, "000001");
        headerTable.put(Constants.Currency,"INR");
        headerTable.put(Constants.TestRun,"M");
        headerTable.put(Constants.GrossAmt,String.valueOf(orderAmount));
        headerTable.put(Constants.NetPrice,String.valueOf(orderAmount));
        headerTable.put(Constants.Latitude,"12.9753649");
        headerTable.put(Constants.Longitude,"77.7266254");
        headerTable.put(Constants.OrderTime,"PT15H26M5S");
        headerTable.put(Constants.CreatedOn, UtilConstants.getNewDateTimeFormat());
        headerTable.put(Constants.CreatedAt, UtilConstants.getOdataDuration().toString());

        int i=0;
        ArrayList<HashMap<String, String>> soItems = new ArrayList<>();
        for (MaterialBean materialBean : items) {
            i=i+1;
            HashMap<String, String> singleItem=new HashMap<>();
            GUID ssoItemGuid = GUID.newRandom();
            singleItem.put(Constants.SSSOItemGUID, ssoItemGuid.toString36().toUpperCase());
            try {
                singleItem.put(Constants.SSSOGuid, ssoHeaderGuid.toString36().toUpperCase());
                singleItem.put(Constants.Quantity, materialBean.getQty());
                singleItem.put(Constants.ItemNo, i+"0");
                singleItem.put(Constants.MaterialNo,materialBean.getMaterialNo());
                singleItem.put(Constants.SkuGroup,materialBean.getMaterialNo());
                singleItem.put(Constants.BomMatIndicator,"");
                singleItem.put(Constants.Currency,"INR");
                singleItem.put(Constants.Uom,materialBean.getUom());
                singleItem.put(Constants.NetPrice,materialBean.getMRP());
                singleItem.put(Constants.MRP,materialBean.getMRP());
                singleItem.put(Constants.GrossAmt,materialBean.getMRP());
                singleItem.put(Constants.UnitPrice,materialBean.getMRP());
                singleItem.put(Constants.CashDiscountPerc,"0");
                singleItem.put(Constants.IsfreeGoodsItem,"0");
                singleItem.put(Constants.Status,"000001");
                singleItem.put(Constants.ProposedDlvQty,"0");
                soItems.add(singleItem);
            } catch (Throwable e) {
                LogManager.writeLogError("SSSOGuid thrown error "+e.toString());
            }
        }
        if (soItems!=null&&!soItems.isEmpty()) {
                headerTable.put(Constants.ITEM_TXT, UtilConstants.convertArrListToGsonString(soItems));
        }

            SharedPreferences sharedPreferences = mActivity.getSharedPreferences(Constants.PREFS_NAME, 0);
            LogManager.writeLogInfo("Sales order save device doc no to shared pref : "+doc_no);
            headerTable.put(Constants.LOGINID, sharedPreferences.getString(Constants.username, "").toUpperCase());
            jsonHeaderObject = new JSONObject(headerTable);

            if (UtilConstants.isNetworkAvailable(mActivity)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Constants.saveDeviceDocNoToSharedPref(mActivity, Constants.SecondarySOCreate, doc_no);
                        ConstantsUtils.storeInDataVault(doc_no, jsonHeaderObject.toString(), mActivity);

                        JSONObject headerObject = Constants.getSOHeaderValuesFromJsonObject(jsonHeaderObject);
                        OnlineManager.createEntity(REPEATABLE_REQUEST_ID, headerObject.toString(), Constants.SSSOs, new UIListener() {
                            @Override
                            public void onRequestError(int operation, Exception e) {
                                 System.out.println("request error " + e.getMessage());
                                 new DialogFactory.Progress(mActivity).hide();
                            }

                            @Override
                            public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                                System.out.println("request success " + key);
                                new DialogFactory.Progress(mActivity).hide();

                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showDialogue(true,orderAmount);
                                    }
                                });
                            }
                        }, mActivity);
                       // uploadDataWithAvailableServer(false);
                    }
                }).start();
            }else{

            }

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    private void showDialogue(boolean isVisible,double orderAmount){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            orderTime = formatter.format(new Date());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        OrderSuccessDialog1Binding orderReviewDialogBinding = OrderSuccessDialog1Binding.inflate(mActivity.getLayoutInflater());
        Dialog dialog = new Dialog(mActivity, R.style.BottomSheetDialog);
        Window window1 = dialog.getWindow();
        dialog.setCancelable(false);
        dialog.setContentView(orderReviewDialogBinding.getRoot());
        TextView tvOrderno = dialog.findViewById(R.id.tvOrderno);
        tvOrderno.setText(doc_no);
        TextView tvordertime = dialog.findViewById(R.id.tvordertime);
        tvordertime.setText(orderTime);
        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        Button btn_order = dialog.findViewById(R.id.backToOutlet);
       /* GolfView gifGolfView = dialog.findViewById(R.id.gifGolfView);
        gifGolfView.setVisibility(View.GONE);*/
        if(MSFAApplication.isBCRVAN){
            iv_close.setVisibility(View.GONE);
        }else{
            iv_close.setVisibility(View.VISIBLE);
        }
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       Intent intent=new Intent(mActivity,SalesOrderActivity.class);
                       mActivity.startActivity(intent);
                       mActivity.finish();
                    }
                });

//                context.startActivity(new Intent(context, DashBoard.class).putExtra(Constants.comingFrom, Constants.INTENT_EXTRA_ORDER_ENTRY).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                dialog.dismiss();
            }
        });
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mActivity,SalesOrderActivity.class);
                mActivity.startActivity(intent);
                mActivity.finish();
//                context.startActivity(new Intent(context, DashBoard.class).putExtra(Constants.comingFrom, Constants.INTENT_EXTRA_ORDER_ENTRY).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                dialog.dismiss();
            }
        });
        TextView tvordervale = dialog.findViewById(R.id.tvordervale);
        tvordervale.setText(Constants.RUPEE_SYMBOL+ orderAmount);
        TextView tvRetailername = dialog.findViewById(R.id.tvRetailername);
        tvRetailername.setText("anwar");
        dialog.show();
        Objects.requireNonNull(window1).setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window1.setGravity(Gravity.CENTER);

    }


    public void getRetailerDetails() {

       /* GUID ssoHeaderGuid = GUID.newRandom();
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("LoginID","GJ-5003432");
            JSONArray jsonArray=new JSONArray();
            jsonObject.put("SSSOItemDetails",jsonArray);
            jsonObject.put("TestRun","R");
            REPEATABLE_REQUEST_ID = ssoHeaderGuid.toString32().toUpperCase();
            OnlineManager.createEntityRetailer(REPEATABLE_REQUEST_ID, jsonObject.toString(), Constants.SSSOs, new UIListener() {
                @Override
                public void onRequestError(int operation, Exception e) {
                      System.out.println("request error " + e.getMessage());
                }

                @Override
                public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                    System.out.println("request success " + key);
                }
            },mActivity);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }*/


    }

    public boolean retrieveMaterials() {
        boolean isAvailable=false;
        ArrayList<MaterialBean> materialBeanArrayList=new ArrayList<>();
        TreeMap<String, JSONObject> result = new TreeMap<>();
        String store = null;
        try {
            store = ConstantsUtils.getFromDataVault(Constants.SPCDV03072025, mActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (store != null) {
            isAvailable=true;
            try {
                JSONObject fetchJsonHeaderObject = new JSONObject(store);
                result = new Gson().fromJson(fetchJsonHeaderObject.toString(), TreeMap.class);
                /*String secondarySales=fetchJsonHeaderObject.getString(Constants.BILMaterials);
                secondarySales = secondarySales.replaceAll("^\"|\"$", "");
                secondarySales = secondarySales.replaceAll("\\\\", "");
                String modifiedString = secondarySales.replaceAll("\\\\\\\\\\\\", "");*/
                System.out.println("fetchJson " + result);
                for(Map.Entry m : result.entrySet()){
                    try {
                        Gson gson = new Gson();
                        if(m.getKey().toString().equalsIgnoreCase(Constants.BILMaterials)) {
                            String jsonObject = gson.toJsonTree(m.getValue()).getAsString();
                            jsonObject = jsonObject.replaceAll("\\\\", "");
                            jsonObject=jsonObject.replaceAll("^\"|\"$", "");
                            Type productListType = new TypeToken<List<MaterialBean>>() {}.getType();
                            List<MaterialBean> productList = gson.fromJson(jsonObject, productListType);
                            ArrayList<MaterialBean> arrayList = new ArrayList<>(productList);
//                        JSONArray jsonArray=new JSONArray(jsonObject);
                            Log.i("TAG", "retrieveMaterials: " + arrayList.size());

                            for(int i=0;i<arrayList.size();i++){
                                ArrayList<String> uomList=new ArrayList<>();
                                uomList.add(arrayList.get(i).getStkConvUom1());
                                uomList.add(arrayList.get(i).getStkConvUom2());
                                arrayList.get(i).setUomList(uomList);
                            }
                     /*   for(int i=0;i<productList.size();i++){
                            MaterialBean materialBean=new MaterialBean() ;
                            JSONObject object = jsonArray.getJSONObject(i);
                            String MaterialDesc=object.getString("MaterialDesc");
                            String MaterialNo=object.getString("MaterialNo");
                            String StkConvQty1=object.getString("StkConvQty1");
                            String StkConvQty2=object.getString("StkConvQty2");
                            String StkConvUom1=object.getString("StkConvUom1");
                            String StkConvUom2=object.getString("StkConvUom2");
                            String MRP=object.getString("MRP");
                            String MerdiseGuid=object.getString("MerdiseGuid");
                            String LandingPrice=object.getString("LandingPrice");
                            String CatalogInfo=object.getString("CatalogInfo");
                            materialBean.setMaterialNo(MaterialNo);
                            materialBean.setMaterialDesc(MaterialDesc);
                            materialBean.setStkConvQty1(StkConvQty1);
                            materialBean.setStkConvQty2(StkConvQty2);
                            materialBean.setStkConvUom1(StkConvUom1);
                            materialBean.setStkConvUom2(StkConvUom2);
                            materialBean.setMRP(MRP);
                            materialBean.setMerdiseGuid(MerdiseGuid);
                            materialBean.setLandingPrice(LandingPrice);
                            materialBean.setCatalogInfo(CatalogInfo);
                            ArrayList<String> uomList=new ArrayList<>();
                            uomList.add(StkConvUom1);
                            uomList.add(StkConvUom2);
                            materialBean.setUomList(uomList);
                            materialBeanArrayList.add(materialBean);
                        }*/

                            salesView.getMaterialList(arrayList);

                            hideProgressDialog();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return isAvailable;


    }
}

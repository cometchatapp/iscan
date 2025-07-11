/*
package com.arteriatech.ss.msec.bil.v4.outlet.outletdetails;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.bil.v4.R;
import com.arteriatech.ss.msec.bil.v4.common.Constants;
import com.arteriatech.ss.msec.bil.v4.expenselist.expenselistdetails.DocumentBean;
import com.arteriatech.ss.msec.bil.v4.mbo.TargetItemsBean;
import com.arteriatech.ss.msec.bil.v4.rpd.billbrowser.SSInvoiceItemDetailsBean;
import com.arteriatech.ss.msec.bil.v4.rpd.billbrowser.SSInvoicesBean;
import com.arteriatech.ss.msec.bil.v4.rpd.updatecustomer.UpdateCustomerPresenter;
import com.arteriatech.ss.msec.bil.v4.store.OfflineManager;
import com.arteriatech.ss.msec.bil.v4.store.OnlineManager;
import com.arteriatech.ss.msec.bil.v4.ui.BannerParser;
import com.arteriatech.ss.msec.bil.v4.ui.DialogFactory;
import com.arteriatech.ss.msec.bil.v4.utils.OfflineUtils;
import com.denzcoskun.imageslider.models.SlideModel;
import com.sap.smp.client.httpc.events.IReceiveEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OutletDetailsPresenter {
    private Context context;
    private IOutletDetailsView view;
    public static List ssInvoiceList = new ArrayList();
    public static List salesSummaryList = new ArrayList();
    private List lastOrderList = new ArrayList();
    public static ArrayList<SummaryBean> alRetailerSummary = new ArrayList();
    private com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean retailerCreateBean = null;
    public static String strLastOrderValue = "0";

    public OutletDetailsPresenter(Context context, IOutletDetailsView view, com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean retailerCreateBean) {
        this.context = context;
        this.view = view;
        this.retailerCreateBean = retailerCreateBean;
    }

    public void onStart() {
        GetLast6InvoicesAsyncTask asyncTask = new GetLast6InvoicesAsyncTask();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class GetLast6InvoicesAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.showProgress("Fetching invoices. Please wait");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(3000);
                List<SlideModel> list = BannerParser.getOutletBannerImageList(context,retailerCreateBean.getName() + "_" + retailerCreateBean.getCPNo());
                if(list.size()==0) {
                    storeCPBannerImages(context, retailerCreateBean.getName() + "_" + retailerCreateBean.getCPNo());
                }else {
                    imageResponse = true;
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (view != null) {
                                view.hideProgress();
                                view.refreshLast6Invoice(ssInvoiceList);
                                view.refreshSalesSummary(alRetailerSummary, strLastOrderValue);
                            }
                        }
                    });
                }

//                getInvoiceList();
//
//
//                getSalesSummary();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            while (!imageResponse) {
////                try {
////                    Thread.sleep(100);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
//            }
//            ((Activity) context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (view != null) {
//                        view.hideProgress();
//                        view.refreshLast6Invoice(ssInvoiceList);
//                        view.refreshSalesSummary(alRetailerSummary, strLastOrderValue);
//                    }
//                }
//            });
        }
    }


    private boolean imageResponse =  false;
    private DocumentBean getImageBase64(String qry1,Context context,int totalSize,String name) {
        DocumentBean documentBean = new DocumentBean();
        try {

//            new Thread(() -> {
            String qry = "ProcessDocument?" + qry1;
            OnlineManager.fetchDocAP1GetRequest(qry, context, iReceiveEvent -> {
                if (iReceiveEvent.getResponseStatusCode() == 200) {
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                    int nRead;
                    byte[] result = null;
                    byte[] data = new byte[16384];

                    while ((nRead = iReceiveEvent.getStream().read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }

                    try {
                        String resultN = buffer.toString();
                        JSONObject jsonObject = new JSONObject(resultN);
                        documentBean.setImageFile(jsonObject.getString("FileContent"));
                        byte[] decodedString = Base64.decode(documentBean.getImageFile(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        BannerParser.saveCPImage(context, decodedByte, name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    imageResponse = true;
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (view != null) {
                                view.hideProgress();
                                view.refreshLast6Invoice(ssInvoiceList);
                                view.refreshSalesSummary(alRetailerSummary, strLastOrderValue);
                            }
                        }
                    });
                } else {
                    imageResponse = true;
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (view != null) {
                                view.hideProgress();
                                view.refreshLast6Invoice(ssInvoiceList);
                                view.refreshSalesSummary(alRetailerSummary, strLastOrderValue);
                            }
                        }
                    });
                }
            }, e -> {
                Log.e("Online request error : ", e.getMessage());
                LogManager.writeLogError(e.toString());
                imageResponse = true;
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (view != null) {
                            view.hideProgress();
                            view.refreshLast6Invoice(ssInvoiceList);
                            view.refreshSalesSummary(alRetailerSummary, strLastOrderValue);
                        }
                    }
                });
            });
//            }).start();

        } catch (Exception e) {
            LogManager.writeLogError(Constants.error_txt1 + " : " + e.getMessage());
            e.printStackTrace();
            imageResponse = true;
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (view != null) {
                        view.hideProgress();
                        view.refreshLast6Invoice(ssInvoiceList);
                        view.refreshSalesSummary(alRetailerSummary, strLastOrderValue);
                    }
                }
            });
        }
        return documentBean;
    }
    private void storeCPBannerImages(Context context,String cpName){
        String query = Constants.ZCustomerDocLinks+"?$filter=Param2 eq '11' and Param3 eq '"+retailerCreateBean.getCPGUID().toUpperCase().replaceAll("-","")+"'";
        OnlineManager.doZCommonGetRequest(query, context, event -> {
            if (event.getResponseStatusCode() == 200) {
                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(responseBody);
                    JSONObject objectD = jsonObj.optJSONObject("d");
                    JSONArray jsonArray = objectD.getJSONArray("results");
                    ArrayList<DocumentBean> documentBeansList = new ArrayList<>();
                    if (jsonArray.length() > 0) {
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            DocumentBean bean = new DocumentBean();
                            bean.setFileName(object.optString("FileName"));
                            bean.setCreatedAt(object.optString("CreatedAt"));
                            bean.setCreatedOn(Constants.ConvertJsonDate(object.optString("CreatedOn"),"yyyy/MM/dd"));
                            bean.setDocumentID(object.optString("DocumentID"));
                            String createOn = "";
                            String createAt = bean.getCreatedAt().replaceAll("PT","")
                                    .replaceAll("H","").replaceAll("M","").replaceAll("S","");
                            try {
                                String date = bean.getCreatedOn();
                                createOn = date.replaceAll("/","");
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            bean.setCreatedOnTemp(createOn+createAt);
                            documentBeansList.add(bean);
                        }

                        Collections.sort(documentBeansList, new Comparator<DocumentBean>() {
                            @Override
                            public int compare(DocumentBean documentBean, DocumentBean t1) {
                                return t1.getCreatedOnTemp().compareTo(documentBean.getCreatedOnTemp());
                            }
                        });
                        if (documentBeansList.size() > 0) {
                            DocumentBean object = documentBeansList.get(0);
                            String res = object.getFileName();
                            String FileName = cpName;
                            DocumentBean documentBean = getImageBase64(res, context, jsonArray.length(),FileName);
                        }else {
                            imageResponse = true;
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (view != null) {
                                        view.hideProgress();
                                        view.refreshLast6Invoice(ssInvoiceList);
                                        view.refreshSalesSummary(alRetailerSummary, strLastOrderValue);
                                    }
                                }
                            });
                        }
                    }else {
                        if(UpdateCustomerPresenter.chesseList.size()>0){
                            String stringMsg = "";
                            for(Object ob:UpdateCustomerPresenter.chesseList){
                                TargetItemsBean targetItemsBean = (TargetItemsBean) ob;
                                if(!TextUtils.isEmpty(stringMsg)){
                                    stringMsg = stringMsg+"\n"+targetItemsBean.getOtherRefDesc()+" ("+targetItemsBean.getOrderMaterialGroupID()+")";
                                }else{
                                    stringMsg = stringMsg+"Cheese Recommendation\n"+targetItemsBean.getOtherRefDesc()+" ("+targetItemsBean.getOrderMaterialGroupID()+")";
                                }
                            }
                            if(!TextUtils.isEmpty(stringMsg)){
                                String finalStringMsg = stringMsg;
                                ((Activity)context).runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       new DialogFactory.Alert(context).setTitle("").setMessage(finalStringMsg).isAlert(true)
                                               .setTheme(R.style.MyDialogTheme_new).setPositiveButton(context.getString(R.string.msg_ok))
                                               .setOnDialogClick(isPositive -> {
                                                   if (isPositive) {
                                                   }
                                               })
                                               .show();
                                   }
                               });
                            }
                        }
                        imageResponse = true;
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (view != null) {
                                    view.hideProgress();
                                    view.refreshLast6Invoice(ssInvoiceList);
                                    view.refreshSalesSummary(alRetailerSummary, strLastOrderValue);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (view != null) {
                                view.hideProgress();
                                view.refreshLast6Invoice(ssInvoiceList);
                                view.refreshSalesSummary(alRetailerSummary, strLastOrderValue);
                            }
                        }
                    });
                }
            } else {
                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                imageResponse = true;
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (view != null) {
                            view.hideProgress();
                            view.refreshLast6Invoice(ssInvoiceList);
                            view.refreshSalesSummary(alRetailerSummary, strLastOrderValue);
                        }
                    }
                });
            }
        }, iError -> {
            iError.printStackTrace();
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (view != null) {
                        view.hideProgress();
                        view.refreshLast6Invoice(ssInvoiceList);
                        view.refreshSalesSummary(alRetailerSummary, strLastOrderValue);
                    }
                }
            });
        });
    }

    // to get retailer SalesSummary
    private void getSalesSummary() {
        alRetailerSummary.clear();
        salesSummaryList.clear();
        strLastOrderValue = "0";
//        String strSalesOrderQry = Constants.SSSOs + "?$filter=SPGUID eq guid'" + Constants.getSPGUID() + "' & SoldToCPGUID eq guid'" + retailerCreateBean.getCPGUID() + "' &$orderby=OrderDate,OrderNo desc &$top=1";
//        lastOrderList = new OfflineUtils.ODataOfflineBuilder<>()
//                .setHeaderPayloadObject(new SSSOsHeaderBean())
//                .setODataEntitySet(Constants.SSSOs)
//                .setODataEntityType(Constants.SSSOEntity)
//                .setQuery(strSalesOrderQry)
//                .returnODataList(OfflineManager.offlineStore);


        String summaryQry = Constants.CPSummary1Set;
        salesSummaryList = new OfflineUtils.ODataOfflineBuilder<>()
                .setHeaderPayloadObject(new CPSummary1SetBean())
                .setODataEntitySet(Constants.CPSummary1Set)
                .setODataEntityType(Constants.CPSummary1)
                .setQuery(summaryQry)
                .returnODataList(OfflineManager.offlineStore);

        String strSalesSummary = "";


//        if (!lastOrderList.isEmpty()) {
//            for (Object SSSOsHeaderBeans : lastOrderList) {
//                SSSOsHeaderBean sssOsHeaderBean = (SSSOsHeaderBean) SSSOsHeaderBeans;
//                strLastOrderValue = sssOsHeaderBean.getNetPrice();
//            }
//        }

        if (!salesSummaryList.isEmpty()) {

            for (Object cpSummary1setBean : salesSummaryList) {
                CPSummary1SetBean cpSummary1SetBean = (CPSummary1SetBean) cpSummary1setBean;
                strSalesSummary = cpSummary1SetBean.getSummary();
            }

            try {

                JSONArray jsonArray = new JSONArray(strSalesSummary);
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String strCpUid = jsonObject.optString("cpUid");
                        if (strCpUid.equalsIgnoreCase(retailerCreateBean.getCPUID())) {

                            SummaryBean summaryBean = new SummaryBean();
                            summaryBean.setCpGuid(jsonObject.getString("cpGuid"));
                            summaryBean.setCpUid(jsonObject.getString("cpUid"));
                            summaryBean.setCpType(jsonObject.getString("cpType"));
                            summaryBean.setLastVisit(jsonObject.getString("lastVisit"));
                            summaryBean.setUlpomtd(jsonObject.getString("ulpomtd"));
                            summaryBean.setMonthavgsale(jsonObject.getString("monthavgsale"));
                            summaryBean.setCurrency(jsonObject.getString("currency"));
                            try {
                                strLastOrderValue = jsonObject.getString("lastordval");
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                             */
/*   String strDummyArray = "{\n" +
                                        "   \"saleSummary\":[\n" +
                                        "      {\n" +
                                        "         \"trg\":\"10000\",\n" +
                                        "         \"act\":\"500\"\n" +
                                        "      },\n" +
                                        "      {\n" +
                                        "         \"trg\":\"2000\",\n" +
                                        "         \"act\":\"300\"\n" +
                                        "      },\n" +
                                        "      {\n" +
                                        "         \"trg\":\"1000\",\n" +
                                        "         \"act\":\"300\"\n" +
                                        "      },\n" +
                                        "      {\n" +
                                        "         \"trg\":\"500\",\n" +
                                        "         \"act\":\"300\"\n" +
                                        "      },\n" +
                                        "      {\n" +
                                        "         \"trg\":\"200\",\n" +
                                        "         \"act\":\"800\"\n" +
                                        "      },\n" +
                                        "      {\n" +
                                        "         \"trg\":\"0\",\n" +
                                        "         \"act\":\"800\"\n" +
                                        "      },\n" +
                                        "      {\n" +
                                        "         \"trg\":\"0\",\n" +
                                        "         \"act\":\"2000\"\n" +
                                        "      },\n" +
                                        "      {\n" +
                                        "         \"trg\":\"20000\",\n" +
                                        "         \"act\":\"1800\"\n" +
                                        "      },\n" +
                                        "      {\n" +
                                        "         \"trg\":\"0\",\n" +
                                        "         \"act\":\"500\"\n" +
                                        "      },\n" +
                                        "      {\n" +
                                        "         \"trg\":\"0\",\n" +
                                        "         \"act\":\"1000\"\n" +
                                        "      },\n" +
                                        "      {\n" +
                                        "         \"trg\":\"20000\",\n" +
                                        "         \"act\":\"8000\"\n" +
                                        "      },\n" +
                                        "      {\n" +
                                        "         \"trg\":\"100\",\n" +
                                        "         \"act\":\"10\"\n" +
                                        "      }\n" +
                                        "   ]\n" +
                                        "}";*//*

//                                String strDummyArray=
//                                JSONObject object = new JSONObject(strDummyArray);
                                JSONArray summaryArray = new JSONArray(jsonObject.getString("saleSummary"));
                                ArrayList<SummaryGraphBean> alGraphList = new ArrayList<>();
                                if (summaryArray.length() > 0) {
                                    for (int j = 0; j < summaryArray.length(); j++) {
                                        JSONObject graphJsonObject = (JSONObject) summaryArray.get(j);
                                        SummaryGraphBean summaryGraphBean = new SummaryGraphBean();
                                        summaryGraphBean.setMonthID(graphJsonObject.getString("period"));
                                        summaryGraphBean.setTargetValue(graphJsonObject.getString("targetValue"));
                                        summaryGraphBean.setAchValue(graphJsonObject.getString("actualValue"));
                                        alGraphList.add(summaryGraphBean);

                                    }


                                    summaryBean.setAlSummaryGraph(alGraphList);
                                }


                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }

//                            alRetailerSummary.add(summaryBean);
                            try {

                            */
/*    String strInvoiceulpo = "{\n" +
                                        "\"invoiceulpo\":[\n" +
                                        "{\n" +
                                        "\"billval\":\"1000\",\n" +
                                        "\"ulpo\":\"5\"\n" +
                                        "},\n" +
                                        "{\n" +
                                        "\"billval\":\"10000\",\n" +
                                        "\"ulpo\":\"50\"\n" +
                                        "},\n" +
                                        "{\n" +
                                        "\"billval\":\"5000\",\n" +
                                        "\"ulpo\":\"25\"\n" +
                                        "},\n" +
                                        "{\n" +
                                        "\"billval\":\"6000\",\n" +
                                        "\"ulpo\":\"30\"\n" +
                                        "},\n" +
                                        "{\n" +
                                        "\"billval\":\"7000\",\n" +
                                        "\"ulpo\":\"30\"\n" +
                                        "},\n" +
                                        "{\n" +
                                        "\"billval\":\"1000\",\n" +
                                        "\"ulpo\":\"30\"\n" +
                                        "}\n" +
                                        "\n" +
                                        "]\n" +
                                        "}";*//*


//                                JSONObject object = new JSONObject(strInvoiceulpo);
                                JSONArray invoiceArray = new JSONArray(jsonObject.getString("invoiceulpo"));
                                ArrayList<InvoiceUlpoBean> alInvoice = new ArrayList<>();
                                if (invoiceArray.length() > 0) {
                                    for (int j = 0; j < invoiceArray.length(); j++) {
                                        JSONObject graphJsonObject = (JSONObject) invoiceArray.get(j);
                                        InvoiceUlpoBean invoiceUlpoBean = new InvoiceUlpoBean();
                                        Calendar calendar = Calendar.getInstance();

                                        int currentMonth = calendar.get(Calendar.MONTH);
                                        String currentMonthStr = String.valueOf(currentMonth + 1);
                                        if (currentMonthStr.length() == 1) {
                                            currentMonthStr = "0" + currentMonthStr;
                                        } else {
                                            currentMonthStr = currentMonthStr;
                                        }
                                        if (!currentMonthStr.equalsIgnoreCase(graphJsonObject.getString("zmonth"))) {
                                            invoiceUlpoBean.setMonthId(graphJsonObject.getString("zmonth"));
                                            String strYear = graphJsonObject.getString("zyear");
                                            String strFinalYear = "";
                                            try {
                                                if (strYear != null && strYear.length() >= 2) {
                                                    strFinalYear = strYear.substring(strYear.length() - 2);
                                                }
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                            invoiceUlpoBean.setYear(strFinalYear);
                                            invoiceUlpoBean.setBillval(graphJsonObject.getString("billValue"));
                                            invoiceUlpoBean.setUlpo(graphJsonObject.getString("ulpo"));
                                            alInvoice.add(invoiceUlpoBean);
                                        }


                                    }

                                    Collections.sort(alInvoice, new Comparator<InvoiceUlpoBean>() {
                                        @Override
                                        public int compare(InvoiceUlpoBean invoiceUlpoBean, InvoiceUlpoBean t1) {
                                            return invoiceUlpoBean.getMonthId().compareTo(t1.getMonthId());
                                        }
                                    });

                                    try {
                                        Collections.reverse(alInvoice);

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    try {
                                        for (int k = 0; k < alInvoice.size(); k++) {
                                            InvoiceUlpoBean invoiceUlpoBean = alInvoice.get(k);

                                            if (k + 1 < alInvoice.size()) {

                                                InvoiceUlpoBean invoiceUlpoBeanOne = alInvoice.get(k + 1);
                                                BigDecimal bigDecimal = new BigDecimal(invoiceUlpoBean.getBillval());
                                                System.out.println("bigdecimal " + invoiceUlpoBean.getBillval());
                                                BigDecimal bigDecimalCompare = new BigDecimal(invoiceUlpoBeanOne.getBillval());

                                                System.out.println("bigdecimal 123 " + invoiceUlpoBeanOne.getBillval());

                                                if (bigDecimal.compareTo(bigDecimalCompare) == 1) {

                                                    System.out.println("bigdecimal greater");
                                                    invoiceUlpoBean.setBillIndicator("02");
                                                } else {
                                                    System.out.println("bigdecimal lesser");
                                                    invoiceUlpoBean.setBillIndicator("01");
                                                }

                                                BigDecimal bigDecimalUlpo = new BigDecimal(invoiceUlpoBean.getUlpo());
                                                BigDecimal bigDecimalUlpoCompare = new BigDecimal(invoiceUlpoBeanOne.getUlpo());
                                                if (bigDecimalUlpo.compareTo(bigDecimalUlpoCompare) == 1) {

                                                    System.out.println("bigdecimal greater");
                                                    invoiceUlpoBean.setUlpoIndicator("02");
                                                } else {
                                                    System.out.println("bigdecimal lesser");
                                                    invoiceUlpoBean.setUlpoIndicator("01");
                                                }
                                            }


                                           */
/* BigDecimal bigDecimal=new BigDecimal(invoiceUlpoBean.getBillval());
                                            BigDecimal bigDecimalCompare=new BigDecimal(invoiceUlpoBeanOne.getBillval());

                                            if(bigDecimalCompare.compareTo(bigDecimal)<0){
                                                invoiceUlpoBean.setBillIndicator("01");
                                            }*//*



                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    InvoiceUlpoBean ssInvoicesBean = new InvoiceUlpoBean();
                                    ssInvoicesBean.setMonthId("Month");
                                    ssInvoicesBean.setBillval("Value");
                                    ssInvoicesBean.setUlpo("ULPO");
                                    alInvoice.add(0, ssInvoicesBean);


                                    summaryBean.setAlInvoiceUlpo(alInvoice);
                                }

                            } catch (JSONException je) {
                                je.printStackTrace();
                            }


                            alRetailerSummary.add(summaryBean);


                        }


                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (view != null) {
                    view.hideProgress();
                    view.refreshLast6Invoice(ssInvoiceList);
                    view.refreshSalesSummary(alRetailerSummary, strLastOrderValue);
                }
            }
        });
    }

    private void getInvoiceList() {
        ssInvoiceList.clear();
        String qry = "SSInvoices?$select=SoldToCPGUID,InvoiceNo,InvoiceDate,NetAmount,InvoiceGUID &$filter=SoldToCPGUID eq guid'" + retailerCreateBean.getCPGUID() + "' and StatusID ne '02' &$orderby=InvoiceDate desc &$top=6";
        ssInvoiceList = new OfflineUtils.ODataOfflineBuilder<>()
                .setHeaderPayloadObject(new SSInvoicesBean())
                .setODataEntitySet("SSInvoices")
                .setODataEntityType(".SSInvoice")
                .setQuery(qry)
                .returnODataList(OfflineManager.offlineStore);

        double billedAmount = 0;
        for (int i = 0; i < ssInvoiceList.size(); i++) {
            SSInvoicesBean ssInvoicesBean1 = (SSInvoicesBean) ssInvoiceList.get(i);
            Calendar calendar = Calendar.getInstance();
            String[] strDate = ssInvoicesBean1.getInvoiceDate().split("/");
            calendar.set(Integer.parseInt(strDate[2]), Integer.parseInt(strDate[1]), Integer.parseInt(strDate[0]));
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            ssInvoicesBean1.setDay(Constants.getWeekOfTheFullDay1("" + week));
            String qrySSInvoicesDetails = Constants.SSInvoiceItemDetails + "?$filter=" + Constants.InvoiceGUID + " eq guid'" + ssInvoicesBean1.getInvoiceGUID() + "'";
            SSInvoiceItemDetailsBean itemDetailsBeans = new SSInvoiceItemDetailsBean();
            ArrayList<SSInvoiceItemDetailsBean> ssInvoicesDetails = (ArrayList) new OfflineUtils.ODataOfflineBuilder<>().
                    setHeaderPayloadObject(itemDetailsBeans).
                    setODataEntitySet(Constants.SSInvoiceItemDetails).
                    setODataEntityType(Constants.SSInvoiceItemDetailEntity).
                    setQuery(qrySSInvoicesDetails).
                    returnODataList(OfflineManager.offlineStore);
            ssInvoicesBean1.setULPO("" + ssInvoicesDetails.size());
            ssInvoicesBean1.setULPO("" + ssInvoicesDetails.size());
            try {
                if (i < 3) {
                    if (!ssInvoicesBean1.getNetAmount().equalsIgnoreCase(""))
                        billedAmount = billedAmount + Double.parseDouble(ssInvoicesBean1.getNetAmount());
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        retailerCreateBean.setBilledAmount("" + billedAmount);
        SSInvoicesBean ssInvoicesBean = new SSInvoicesBean();
        ssInvoicesBean.setInvoiceNo("Invoice #");
        ssInvoicesBean.setInvoiceDate("Date");
        ssInvoicesBean.setDay("Day");
        ssInvoicesBean.setULPO("ULPO");
        ssInvoicesBean.setNetAmount("Value");

        ssInvoiceList.add(0, ssInvoicesBean);

    }
}

*/

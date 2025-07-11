/*
package com.arteriatech.ss.msec.bil.v4.outlet;

import static com.arteriatech.ss.msec.bil.v4.common.Constants.REPEATABLE_REQUEST_ID;
import static com.arteriatech.ss.msec.bil.v4.common.Constants.TotalProCalls;
import static com.arteriatech.ss.msec.bil.v4.common.Constants.getDatebynumberofDays;
import static com.arteriatech.ss.msec.bil.v4.common.Constants.getQuoDeckUrl;
import static com.arteriatech.ss.msec.bil.v4.dashboard.DashBoard.distributorBean;
import static com.arteriatech.ss.msec.bil.v4.dashboard.DashBoard.isCourse;
import static com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionViewModel.getCurrentMonthPeriod;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.annimon.stream.Stream;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.mutils.log.TraceLog;
import com.arteriatech.ss.msec.bil.v4.DeviceIDUtils;
import com.arteriatech.ss.msec.bil.v4.R;
import com.arteriatech.ss.msec.bil.v4.asyncTask.RefreshAsyncTask;
import com.arteriatech.ss.msec.bil.v4.common.Constants;
import com.arteriatech.ss.msec.bil.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.bil.v4.common.MSFAApplication;
import com.arteriatech.ss.msec.bil.v4.log.ErrorCode;
import com.arteriatech.ss.msec.bil.v4.mbo.AttendanceBean;
import com.arteriatech.ss.msec.bil.v4.mbo.CPConfigBean;
import com.arteriatech.ss.msec.bil.v4.mbo.CPPerformanceBean;
import com.arteriatech.ss.msec.bil.v4.mbo.CPProductDetTypeBean;
import com.arteriatech.ss.msec.bil.v4.mbo.ConfigTypsetTypeValuesBean;
import com.arteriatech.ss.msec.bil.v4.mbo.ErrorBean;
import com.arteriatech.ss.msec.bil.v4.mbo.KPISetBean;
import com.arteriatech.ss.msec.bil.v4.mbo.RoutePlansBean;
import com.arteriatech.ss.msec.bil.v4.mbo.RouteProductDetTypeBean;
import com.arteriatech.ss.msec.bil.v4.mbo.RouteScheduleDetailBean;
import com.arteriatech.ss.msec.bil.v4.mbo.RouteScheduleSPBean;
import com.arteriatech.ss.msec.bil.v4.mbo.SPStockItemBean;
import com.arteriatech.ss.msec.bil.v4.mbo.SPStockItemSNoBean;
import com.arteriatech.ss.msec.bil.v4.mbo.TargetItemsBean;
import com.arteriatech.ss.msec.bil.v4.mbo.TargetsBean;
import com.arteriatech.ss.msec.bil.v4.mbo.UserChannelPartnerBean;
import com.arteriatech.ss.msec.bil.v4.mbo.UserProfileAuthSetBean;
import com.arteriatech.ss.msec.bil.v4.mbo.VisitActivityBean;
import com.arteriatech.ss.msec.bil.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.bil.v4.mutils.common.Operation;
import com.arteriatech.ss.msec.bil.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.bil.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.bil.v4.mutils.common.UtilOfflineManager;
import com.arteriatech.ss.msec.bil.v4.mutils.registration.UtilRegistrationActivity;
import com.arteriatech.ss.msec.bil.v4.pda.distributorselection.ColorBean;
import com.arteriatech.ss.msec.bil.v4.pda.distributorselection.DistributorSelectionBean;
import com.arteriatech.ss.msec.bil.v4.pda.distributorselection.IDistributorSelectionPresenterImplement;
import com.arteriatech.ss.msec.bil.v4.pda.distributorselection.IDistributorSelectionView;
import com.arteriatech.ss.msec.bil.v4.pda.distributorselection.IRetailerDataListener;
import com.arteriatech.ss.msec.bil.v4.pda.distributorselection.SOItemGOBean;
import com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean;
import com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean;
import com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RouteSchPlansBean;
import com.arteriatech.ss.msec.bil.v4.pda.sales.eodoperation.VisitBean;
import com.arteriatech.ss.msec.bil.v4.pda.sales.orderentry.CommonDB;
import com.arteriatech.ss.msec.bil.v4.rpd.addretailer.UserSalesPersonBean;
import com.arteriatech.ss.msec.bil.v4.rpd.billbrowser.SSInvoiceTypesBean;
import com.arteriatech.ss.msec.bil.v4.rpd.billbrowser.SSInvoicesBean;
import com.arteriatech.ss.msec.bil.v4.rpd.stock.CPStockItemBean;
import com.arteriatech.ss.msec.bil.v4.rpd.stock.CPStockItemSnoBean;
import com.arteriatech.ss.msec.bil.v4.so.SSSOsHeaderBean;
import com.arteriatech.ss.msec.bil.v4.store.OfflineManager;
import com.arteriatech.ss.msec.bil.v4.store.OnlineManager;
import com.arteriatech.ss.msec.bil.v4.sync.OutletSQLHelper;
import com.arteriatech.ss.msec.bil.v4.sync.SyncSelectionActivity;
import com.arteriatech.ss.msec.bil.v4.ui.DialogFactory;
import com.arteriatech.ss.msec.bil.v4.utils.ErrorListener;
import com.arteriatech.ss.msec.bil.v4.utils.OfflineUtils;
import com.arteriatech.ss.msec.bil.v4.van.allsku.AllSKUActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sap.client.odata.v4.core.GUID;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.odata.ODataDuration;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.impl.ODataDurationDefaultImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;


@SuppressWarnings("all")
public class DashBoardPresenter implements IDistributorSelectionPresenterImplement, UIListener {
    private Context context;
    private Activity activity;
    private IDistributorSelectionView view;
    public static List distributorBeanList;
    private List salesManBeanList;
    private String CPGUID = "";
    private VisitBean visitBean =  null;
    private RetailerSelectionBean retailerSelectionBean = null;
    public static List beatList;
    public static List retailersList;
    public static List routeList;
    public static List SSInovoiceDetailsList;
    public static List SSInvoiceColorList;
    public static List visitedColorList;
    public static List SSSOColorList;
    public static List SSSOItemDetailsList;
    public static List cpPerformanceList;
    public static List targetsItemsList;
    public static boolean isStockLoaded=false;
    public static List<Object>stockList = new ArrayList<>();
    public static List<Object>stockListAWSM = new ArrayList<>();
    public static HashMap<String,Integer> hashMapWinkin = new HashMap<>();
    private static IRetailerDataListener retailerDataListener;
    private CourseListener courseListener;
    private boolean isMaterSyncing=false;
    public static boolean isOutletsLoaded=true;
    public static boolean isBeatLoaded=true;
    public static List ssInvoicesList;
    public static List sssOsHeaderBeansList;
    public static ArrayList<SSInvoiceTypesBean> ssInvoiceTypeList=null;
    public static ExecutorService executorService = null;
    public DashBoardPresenter(Context context, Activity activity, IDistributorSelectionView view) {
        this.context = context;
        this.activity = activity;
        this.view = view;
        this.isMaterSyncing = false;
        this.distributorBeanList = new ArrayList<>();
        this.salesManBeanList = new ArrayList<>();
        this.retailersList = new CopyOnWriteArrayList();
        this.beatList = new ArrayList();
        this.routeList = new ArrayList();
        this.SSInovoiceDetailsList = new ArrayList();
        this.SSInvoiceColorList = new ArrayList();
        this.visitedColorList = new ArrayList();
        this.SSSOColorList = new ArrayList();
        this.cpPerformanceList = new ArrayList();
        this.SSSOItemDetailsList = new ArrayList();
        this.ssInvoicesList = new ArrayList();
        this.sssOsHeaderBeansList = new ArrayList();
        this.ssInvoiceTypeList = new ArrayList();
        isOutletsLoaded =true;
        if (MSFAApplication.isVAN){
            getBillBrowserList(context);
            getOrderBrowserList(context);
        }
    }
    public DashBoardPresenter(Context context, Activity activity, IDistributorSelectionView view, CourseListener courseListener) {
        this.context = context;
        this.activity = activity;
        this.view = view;
        this.isMaterSyncing = false;
        this.distributorBeanList = new ArrayList<>();
        this.salesManBeanList = new ArrayList<>();
        this.retailersList = new CopyOnWriteArrayList();
        this.beatList = new ArrayList();
        this.routeList = new ArrayList();
        this.SSInovoiceDetailsList = new ArrayList();
        this.SSInvoiceColorList = new ArrayList();
        this.visitedColorList = new ArrayList();
        this.SSSOColorList = new ArrayList();
        this.cpPerformanceList = new ArrayList();
        this.SSSOItemDetailsList = new ArrayList();
        this.courseListener =courseListener;
        this.ssInvoicesList = new ArrayList();
        this.sssOsHeaderBeansList = new ArrayList();
        this.ssInvoiceTypeList = new ArrayList();
        isOutletsLoaded =true;
        if (MSFAApplication.isVAN){
            getBillBrowserList(context);
            getOrderBrowserList(context);
        }
    }

    public DashBoardPresenter() {
    }

    public void registerListener(IRetailerDataListener listener){
        this.retailerDataListener=listener;
    }
    public void removeListener(IRetailerDataListener listener){
        this.retailerDataListener=listener;
    }

    public static void getWinkinOutlets(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        try {
//                    hashMapWinkin.clear();
            String value="";
            try {
                if (OfflineManager.isOfflineStoreOpen()) {
                    value = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '"+Constants.ZWKKPI+"' &$top=1", Constants.TypeValue);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            String kpiQry = Constants.KPISet + "?$select=" + Constants.KPIGUID+" &$filter= " + Constants.ValidTo + " ge datetime'" + UtilConstants.getNewDate() + "' and " + Constants.KPICode + " eq '"+value+"'";
            List list = new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new KPISetBean())
                    .setODataEntitySet(Constants.KPISet)
                    .setODataEntityType(".KPI")
                    .setQuery(kpiQry)
                    .returnODataList(OfflineManager.offlineStore);
            if (list != null && !list.isEmpty()) {
                if (list.get(0) instanceof KPISetBean) {
                    String targetQuery = Constants.Targets + "?$select=TargetGUID &$filter=Period eq '" + getCurrentMonthPeriod() + "' and KPIGUID eq ";
                    KPISetBean bean = (KPISetBean) list.get(0);
                    targetQuery = targetQuery.concat("guid'" + bean.getKPIGUID() + "'");
                    List targetList = new OfflineUtils.ODataOfflineBuilder<>()
                            .setHeaderPayloadObject(new TargetsBean())
                            .setODataEntitySet(Constants.Targets)
                            .setODataEntityType(".Target")
                            .setQuery(targetQuery)
                            .returnODataList(OfflineManager.offlineStore);
                    if (targetList != null && !targetList.isEmpty()) {
                        String targetItemQuery = Constants.TargetItems + "?$select=MaterialNo,MaterialDesc,TargetGUID,TargetItemGUID,KPICode,PartnerGUID," +
                                "TargetQty,ActualQty &$filter=Period eq '" + getCurrentMonthPeriod() + "' and TargetGUID eq ";
                        if (targetList.get(0) instanceof TargetsBean) {
                            TargetsBean bean1 = (TargetsBean) targetList.get(0);
                            targetItemQuery = targetItemQuery.concat("guid'" + bean1.getTargetGUID() + "'");
                            List targetItemList = new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new TargetItemsBean())
                                    .setODataEntitySet(Constants.TargetItems)
                                    .setODataEntityType(".TargetItem")
                                    .setQuery(targetItemQuery)
                                    .returnODataList(OfflineManager.offlineStore);
                            if(targetItemList.size()>0){
                                for(Object object : targetItemList){
                                    TargetItemsBean targetItemsBean = (TargetItemsBean) object;
                                    int targetqty =0;
                                    try {
                                        if(!TextUtils.isEmpty(targetItemsBean.getTargetQty())){
                                            targetqty = (int) Double.parseDouble(targetItemsBean.getTargetQty());
                                        }
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        hashMapWinkin.put(Constants.convertStrGUID32to36(targetItemsBean.getPartnerGUID()).toLowerCase(),targetqty);
                                    } catch (Throwable e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//            }
//        }).start();

    }

    @Override
    public void onStart() {
        LogManager.writeLogInfo("Distributor selection presenter start");
        Constants.setCurrentDateTOSharedPerf(context);
        checkIfCheckOutAllowed();
        GetDistributorsAsyncTask asyncTask =new GetDistributorsAsyncTask();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new Thread(() -> ConstantsUtils.checkNetwork(context, isFailed -> {
            if (isFailed) {
                ConstantsUtils.checkNetwork(activity,null,true);
                activity.runOnUiThread(() -> {
                    view.hideProgress();
                    asyncTask.cancel(true);
                    if (isMaterSyncing) {
                        isMaterSyncing=false;
                        Constants.showAlert(ErrorCode.NETWORK_ERROR, context);
                    }
                });
            }
        },false)).start();
        quodeckCredential= getQuoDeckUrl();
    }

    @Override
    public void onSaveVisit(DistributorSelectionBean bean) {
        try {
            visitBean =  new VisitBean();
            visitBean.setVisitGUID(GUID.newRandom().toString36().toUpperCase());
            if (retailerSelectionBean!=null) {
                visitBean.setCPGUID(retailerSelectionBean.getCPGUID().replaceAll("-","").toUpperCase());
            }else{
                visitBean.setCPGUID(bean.getCPGUID().toUpperCase());
            }
            visitBean.setStartDate(ConstantsUtils.returnCurrentDate());
            visitBean.setVisitDate(ConstantsUtils.returnCurrentDate());
            visitBean.setStartLat(String.valueOf(UtilConstants.round(UtilConstants.latitude,12)));
            visitBean.setStartLong(String.valueOf(UtilConstants.round(UtilConstants.longitude,12)));
            visitBean.setStartTime(""+ConstantsUtils.oDataTimeFormat());
            visitBean.setVisitCatID(Constants.str_01);
            visitBean.setStatusID(Constants.str_01);
            visitBean.setCPTypeID(Constants.str_02);
            visitBean.setSPGUID(MSFAApplication.SPGUID);
            new OfflineUtils.ODataOfflineBuilder()
                    .setODataEntitySet(Constants.Visits)
                    .setODataEntityType(Constants.VISITENTITY)
                    .setUIListener(this)
                    .setCreate(true)
                    .setHeaderPayloadObject(visitBean)
                    .build(new ErrorListener() {
                        @Override
                        public void error(String errorAtField, String errorMessage) {
                            Toast.makeText(activity, "Error at field "+errorAtField.concat(" "+errorMessage), Toast.LENGTH_SHORT).show();
                        }
                    });
            if(view!=null){
                view.navigateToNextScreen();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(int operation, Exception e) {
        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {

    }

    public static boolean getMDVisitList(Context context,String CPGUID) {
        try {
            SharedPreferences mPrefs =context.getSharedPreferences(Constants.PREFS_NAME,0);
            Gson gson = new Gson();
            String json = mPrefs.getString(Constants.DISTRIBUTOR_DETAILS, "");
            if(!TextUtils.isEmpty(json)) {
                DistributorSelectionBean bean = gson.fromJson(json, DistributorSelectionBean.class);
                String visitQry  = Constants.Visits + "?$filter="+ Constants.StartDate + " eq datetime'" +
                        UtilConstants.getNewDate() + "' and "+ Constants.EndDate + " eq null and "+Constants.CPGUID  + " eq '"+bean.getCPGUID().replaceAll("-","").toUpperCase()+"' and SPGUID eq guid'"+ MSFAApplication.SPGUID+"'";
                boolean isCheck = OfflineManager.getVisitedList(visitQry);

                if (isCheck && !bean.getCPGUID().equalsIgnoreCase(CPGUID)) {
                    return true;
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    private class GetDistributorsAsyncTask  extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LogManager.writeLogInfo("Distributor selection presenter async on pre execute");
//            view.showProgress();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            LogManager.writeLogInfo("Distributor selection presenter async on background");
            getWinkinOutlets();
            try {
                getSalesPersonList();
                try {
                    getDistributorList();
                } catch (Exception e) {
                    ConstantsUtils.checkNetwork(activity,null,true);
                    LogManager.writeLogInfo("Distributor selection presenter async on background distributor list error : "+e.getMessage());
                    e.printStackTrace();
                }
                try {
                    if(MSFAApplication.isBVAN) {
                        getFactoriesList();
                    }
                } catch (Exception e) {
                    ConstantsUtils.checkNetwork(activity,null,true);
                    LogManager.writeLogInfo("Distributor selection presenter async on background Factories list error : "+e.getMessage());
                    e.printStackTrace();
                }
//                try{
////                    getStockMaterials();
//                }catch (Exception ex){
//                    LogManager.writeLogInfo("Distributor selection presenter async on background distributor list error : "+ex.getMessage());
//                    ex.printStackTrace();
//                }

            } catch (Exception e) {
                ConstantsUtils.checkNetwork(activity,null,true);
                LogManager.writeLogInfo("Distributor selection presenter async on background sale preson list error : "+e.getMessage());
                e.printStackTrace();
                activity.runOnUiThread(() -> {
//                    view.hideProgress();
                    view.showMessage(e.toString(),1);
                });
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LogManager.writeLogInfo("Distributor selection presenter async on post execute");
//            view.hideProgress();
        }
    }
    List distributorBeanListNew=new ArrayList();
    private void getDistributorList() {
        String rollID = ConstantsUtils.getAuthInformation(context);
        LogManager.writeLogInfo("Distributor selection role id : "+rollID);
        switch (rollID){
            case Constants.DSR: // AWSM
                if (OfflineManager.offlineStore!=null) {
                    try {
                        String routeQry = Constants.CPSPRelations+"?$select=CPSPGUID,CPTypeID,CPTypeDesc,CPGUID,CPNo,CPName,CPUID,SPType,SPTypeDesc,SPGUID,SPNo,FirstName,LastName,Address1,Address2,Address3,Address4,CityDesc," +
                                "StateDesc,CountryDesc,PostalCode,MobileNo";
                        LogManager.writeLogInfo("Distributor selection distributor qry  : "+routeQry);
                        if (OfflineManager.isOfflineStoreOpen()) {
                            distributorBeanListNew = new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new DistributorSelectionBean())
                                    .setODataEntitySet(Constants.CPSPRelations)
                                    .setODataEntityType(Constants.CPSPRelationEntity)
                                    .setUIListener(this)
                                    .setQuery(routeQry)
                                    .returnODataList(OfflineManager.offlineStore);
                            LogManager.writeLogInfo("Distributor selection distributor list count  : "+distributorBeanListNew.size());
                            if (distributorBeanListNew!=null&&!distributorBeanListNew.isEmpty()) {
                                for(int i=0;i<distributorBeanListNew.size();i++){
                                    DistributorSelectionBean ditributorBean=(DistributorSelectionBean) distributorBeanListNew.get(i);
                                    if (ditributorBean.getCPTypeID().equalsIgnoreCase("01")){
                                        distributorBeanList.add(ditributorBean);
                                    }
                                }

                                Set set = new HashSet(distributorBeanList);
                                distributorBeanList.clear();
                                distributorBeanList.addAll(set);
                                Collections.sort(distributorBeanList, (Comparator<DistributorSelectionBean>) (one, other) -> one.getCPName().compareTo(other.getCPName()));
                                ConstantsUtils.checkNetwork(activity,null,true);
                                view.refreshSpinners(distributorBeanList, 1);

                            }else{
                                refreshCPSPRelations();
                                LogManager.writeLogError("CPSPRelations giving 0 records");
                            }
                        }else {
                            LogManager.writeLogError("Offline store not opened, unable to fetch distributors");
                        }
                    } catch (Throwable e) {
                        LogManager.writeLogError("error while fetching distributors \n"+e.toString());
                    }
                }else{
                    LogManager.writeLogError("offlineStore is null");
                }
                break;
            case Constants.BCRVAN: // BCRVAN
                if (OfflineManager.offlineStore != null) {
                    try {
                        String routeQry = Constants.CPSPRelations + "?$select=CPSPGUID,CPTypeID,CPTypeDesc,CPGUID,CPNo,CPName,CPUID,SPType,SPTypeDesc,SPGUID,SPNo,FirstName,LastName,Address1,Address2,Address3,Address4,CityDesc," +
                                "StateDesc,CountryDesc,PostalCode,MobileNo";
                        LogManager.writeLogInfo("Distributor selection distributor qry  : " + routeQry);
                        if (OfflineManager.isOfflineStoreOpen()) {
                            distributorBeanListNew = new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new DistributorSelectionBean())
                                    .setODataEntitySet(Constants.CPSPRelations)
                                    .setODataEntityType(Constants.CPSPRelationEntity)
                                    .setUIListener(this)
                                    .setQuery(routeQry)
                                    .returnODataList(OfflineManager.offlineStore);
                            LogManager.writeLogInfo("Distributor selection distributor list count  : " + distributorBeanListNew.size());
                            if (distributorBeanListNew != null && !distributorBeanListNew.isEmpty()) {
                                for (int i = 0; i < distributorBeanListNew.size(); i++) {
                                    DistributorSelectionBean ditributorBean = (DistributorSelectionBean) distributorBeanListNew.get(i);
                                    if (ditributorBean.getCPTypeID().equalsIgnoreCase("01")) {
                                        distributorBeanList.add(ditributorBean);
                                    }
                                }

                                Set set = new HashSet(distributorBeanList);
                                distributorBeanList.clear();
                                distributorBeanList.addAll(set);
                                Collections.sort(distributorBeanList, (Comparator<DistributorSelectionBean>) (one, other) -> one.getCPName().compareTo(other.getCPName()));
                                ConstantsUtils.checkNetwork(activity, null, true);
                                view.refreshSpinners(distributorBeanList, 1);

                            } else {
                                refreshCPSPRelations();
                                LogManager.writeLogError("CPSPRelations giving 0 records");
                            }
                        } else {
                            LogManager.writeLogError("Offline store not opened, unable to fetch distributors");
                        }
                    } catch (Throwable e) {
                        LogManager.writeLogError("error while fetching distributors \n" + e.toString());
                    }
                } else {
                    LogManager.writeLogError("offlineStore is null");
                }
                break;
            case Constants.CALL_CNTR: // Call Center
                if (OfflineManager.offlineStore!=null) {
                    try {
                        String routeQry = Constants.CPSPRelations+"?$select=CPSPGUID,CPTypeID,CPTypeDesc,CPGUID,CPNo,CPName,CPUID,SPType,SPTypeDesc,SPGUID,SPNo,FirstName,LastName,Address1,Address2,Address3,Address4,CityDesc," +
                                "StateDesc,CountryDesc,PostalCode,MobileNo";
                        LogManager.writeLogInfo("Distributor selection distributor qry  : "+routeQry);
                        if (OfflineManager.isOfflineStoreOpen()) {
                            distributorBeanListNew = new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new DistributorSelectionBean())
                                    .setODataEntitySet(Constants.CPSPRelations)
                                    .setODataEntityType(Constants.CPSPRelationEntity)
                                    .setUIListener(this)
                                    .setQuery(routeQry)
                                    .returnODataList(OfflineManager.offlineStore);
                            LogManager.writeLogInfo("Distributor selection distributor list count  : "+distributorBeanListNew.size());
                            if (distributorBeanListNew!=null&&!distributorBeanListNew.isEmpty()) {
                                for(int i=0;i<distributorBeanListNew.size();i++){
                                    DistributorSelectionBean ditributorBean=(DistributorSelectionBean) distributorBeanListNew.get(i);
                                    if (ditributorBean.getCPTypeID().equalsIgnoreCase("01")){
                                        distributorBeanList.add(ditributorBean);
                                    }
                                }

                                Set set = new HashSet(distributorBeanList);
                                distributorBeanList.clear();
                                distributorBeanList.addAll(set);
                                Collections.sort(distributorBeanList, (Comparator<DistributorSelectionBean>) (one, other) -> one.getCPName().compareTo(other.getCPName()));
                                ConstantsUtils.checkNetwork(activity,null,true);
                                view.refreshSpinners(distributorBeanList, 1);

                            }else{
                                refreshCPSPRelations();
                                LogManager.writeLogError("CPSPRelations giving 0 records");
                            }
                        }else {
                            LogManager.writeLogError("Offline store not opened, unable to fetch distributors");
                        }
                    } catch (Throwable e) {
                        LogManager.writeLogError("error while fetching distributors \n"+e.toString());
                    }
                }else{
                    LogManager.writeLogError("offlineStore is null");
                }
                break;
            case Constants.PSM: // PSM
                if (OfflineManager.offlineStore != null) {
                    try {
                        String routeQry = Constants.CPSPRelations + "?$select=CPSPGUID,CPTypeID,CPTypeDesc,CPGUID,CPNo,CPName,CPUID,SPType,SPTypeDesc,SPGUID,SPNo,FirstName,LastName,Address1,Address2,Address3,Address4,CityDesc," +
                                "StateDesc,CountryDesc,PostalCode,MobileNo";
                        LogManager.writeLogInfo("Distributor selection distributor qry  : " + routeQry);
                        if (OfflineManager.isOfflineStoreOpen()) {
                            distributorBeanListNew = new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new DistributorSelectionBean())
                                    .setODataEntitySet(Constants.CPSPRelations)
                                    .setODataEntityType(Constants.CPSPRelationEntity)
                                    .setUIListener(this)
                                    .setQuery(routeQry)
                                    .returnODataList(OfflineManager.offlineStore);
                            LogManager.writeLogInfo("Distributor selection distributor list count  : " + distributorBeanListNew.size());
                            if (distributorBeanListNew != null && !distributorBeanListNew.isEmpty()) {
                                for(int i=0;i<distributorBeanListNew.size();i++){
                                    DistributorSelectionBean ditributorBean=(DistributorSelectionBean) distributorBeanListNew.get(i);
                                    if (ditributorBean.getCPTypeID().equalsIgnoreCase("01")){
                                        distributorBeanList.add(ditributorBean);
                                    }
                                }

                                Set set = new HashSet(distributorBeanList);
                                distributorBeanList.clear();
                                distributorBeanList.addAll(set);
                                Collections.sort(distributorBeanList, (Comparator<DistributorSelectionBean>) (one, other) -> one.getCPName().compareTo(other.getCPName()));
                                ConstantsUtils.checkNetwork(activity, null, true);
                                view.refreshSpinners(distributorBeanList, 1);
                            } else {
                                refreshCPSPRelations();
                                LogManager.writeLogError("CPSPRelations giving 0 records");
                            }
                        } else {
                            LogManager.writeLogError("Offline store not opened, unable to fetch distributors");
                        }
                    } catch (Throwable e) {
                        LogManager.writeLogError("error while fetching distributors \n" + e.toString());
                    }
                } else {
                    LogManager.writeLogError("offlineStore is null");
                }
                break;
            case Constants.VAN: // PSM
                if (OfflineManager.offlineStore != null) {
                    try {
                        String routeQry = Constants.CPSPRelations + "?$select=CPSPGUID,CPTypeID,CPTypeDesc,CPGUID,CPNo,CPName,CPUID,SPType,SPTypeDesc,SPGUID,SPNo,FirstName,LastName,Address1,Address2,Address3,Address4,CityDesc," +
                                "StateDesc,CountryDesc,PostalCode,MobileNo";
                        LogManager.writeLogInfo("Distributor selection distributor qry  : " + routeQry);
                        if (OfflineManager.isOfflineStoreOpen()) {
                            distributorBeanListNew = new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new DistributorSelectionBean())
                                    .setODataEntitySet(Constants.CPSPRelations)
                                    .setODataEntityType(Constants.CPSPRelationEntity)
                                    .setUIListener(this)
                                    .setQuery(routeQry)
                                    .returnODataList(OfflineManager.offlineStore);
                            LogManager.writeLogInfo("Distributor selection distributor list count  : " + distributorBeanListNew.size());
                            if (distributorBeanListNew != null && !distributorBeanListNew.isEmpty()) {
                                for(int i=0;i<distributorBeanListNew.size();i++){
                                    DistributorSelectionBean ditributorBean=(DistributorSelectionBean) distributorBeanListNew.get(i);
                                    if (ditributorBean.getCPTypeID().equalsIgnoreCase("01")){
                                        distributorBeanList.add(ditributorBean);
                                    }
                                }

                                Set set = new HashSet(distributorBeanList);
                                distributorBeanList.clear();
                                distributorBeanList.addAll(set);
                                Collections.sort(distributorBeanList, (Comparator<DistributorSelectionBean>) (one, other) -> one.getCPName().compareTo(other.getCPName()));
                                ConstantsUtils.checkNetwork(activity, null, true);
                                view.refreshSpinners(distributorBeanList, 1);
                            } else {
                                refreshCPSPRelations();
                                LogManager.writeLogError("CPSPRelations giving 0 records");
                            }
                        } else {
                            LogManager.writeLogError("Offline store not opened, unable to fetch distributors");
                        }
                    } catch (Throwable e) {
                        LogManager.writeLogError("error while fetching distributors \n" + e.toString());
                    }
                } else {
                    LogManager.writeLogError("offlineStore is null");
                }
                break;
            case Constants.MSP: // SDA
                String routeQrySDA = Constants.CPSPRelations+"?$select=CPSPGUID,CPTypeID,CPTypeDesc,CPGUID,CPNo,CPName,CPUID,SPType,SPTypeDesc,SPGUID,SPNo,FirstName,LastName,Address1,Address2,Address3,Address4,CityDesc," +
                        "StateDesc,CountryDesc,PostalCode,MobileNo &$filter=CPTypeID eq '02'";
                LogManager.writeLogInfo("Distributor selection distributor list qry  : "+routeQrySDA);
                distributorBeanList = new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(new DistributorSelectionBean())
                        .setODataEntitySet(Constants.CPSPRelations)
                        .setODataEntityType(Constants.CPSPRelationEntity)
                        .setUIListener(this)
                        .setQuery(routeQrySDA)
                        .returnODataList(OfflineManager.offlineStore);
                LogManager.writeLogInfo("Distributor selection distributor list count  : "+distributorBeanList.size());
                Set sets = new HashSet(distributorBeanList);
                distributorBeanList.clear();
                distributorBeanList.addAll(sets);
                Collections.sort(distributorBeanList, (Comparator<DistributorSelectionBean>) (one, other) -> one.getCPName().compareTo(other.getCPName()));
                view.refreshSpinners(distributorBeanList, 1);
                break;

            case Constants.BVAN: // Bread Van
                if (OfflineManager.offlineStore != null) {
                    try {
                        String routeQry = Constants.CPSPRelations + "?$select=CPSPGUID,CPTypeID,CPTypeDesc,CPGUID,CPNo,CPName,CPUID,SPType,SPTypeDesc,SPGUID,SPNo,FirstName,LastName,Address1,Address2,Address3,Address4,CityDesc," + "StateDesc,CountryDesc,PostalCode,MobileNo";
                        LogManager.writeLogInfo("Distributor selection distributor qry  : " + routeQry);
                        if (OfflineManager.isOfflineStoreOpen()) {
                            distributorBeanListNew = new OfflineUtils.ODataOfflineBuilder<>().setHeaderPayloadObject(new DistributorSelectionBean()).setODataEntitySet(Constants.CPSPRelations).setODataEntityType(Constants.CPSPRelationEntity).setUIListener(this).setQuery(routeQry).returnODataList(OfflineManager.offlineStore);
                            LogManager.writeLogInfo("Distributor selection distributor list count  : " + distributorBeanListNew.size());
                            if (distributorBeanListNew != null && !distributorBeanListNew.isEmpty()) {
                                for (int i = 0; i < distributorBeanListNew.size(); i++) {
                                    DistributorSelectionBean ditributorBean = (DistributorSelectionBean) distributorBeanListNew.get(i);
                                    if (ditributorBean.getCPTypeID().equalsIgnoreCase("01")) {
                                        distributorBeanList.add(ditributorBean);
                                    }
                                }

                                Set set = new HashSet(distributorBeanList);
                                distributorBeanList.clear();
                                distributorBeanList.addAll(set);
                                Collections.sort(distributorBeanList, (Comparator<DistributorSelectionBean>) (one, other) -> one.getCPName().compareTo(other.getCPName()));
                                ConstantsUtils.checkNetwork(activity, null, true);
                                view.refreshSpinners(distributorBeanList, 1);
                            } else {
                                refreshCPSPRelations();
                                LogManager.writeLogError("CPSPRelations giving 0 records");
                            }
                        } else {
                            LogManager.writeLogError("Offline store not opened, unable to fetch distributors");
                        }
                    } catch (Throwable e) {
                        LogManager.writeLogError("error while fetching distributors \n" + e.toString());
                    }
                } else {
                    LogManager.writeLogError("offlineStore is null");
                }
                break;
        }

        if(salesManBeanList.size()>0){
            UserSalesPersonBean salesPersonBean = (UserSalesPersonBean) salesManBeanList.get(0);
            MSFAApplication.SPGUID = salesPersonBean.getSPGUID().toUpperCase();
        }
        if(distributorBeanList.size()>0) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME,0);
            SharedPreferences mPrefs = activity.getSharedPreferences(Constants.PREFS_NAME, 0);
            //Update ULPO Target in SharePreference
            Gson gson = new Gson();
            String json = mPrefs.getString(Constants.DISTRIBUTOR_NAME, "");

            if (TextUtils.isEmpty(json)) {
                DistributorSelectionBean distributorSelectionBean = (DistributorSelectionBean) distributorBeanList.get(0);
                MSFAApplication.DISTRIBUTOR_NAME = distributorSelectionBean.getCPName();
                MSFAApplication.DISTRIBUTOR_NO = distributorSelectionBean.getCPNo();
                getBeatsRemoteData(context, distributorSelectionBean.getCPGUID(), distributorSelectionBean.getCPNo());
            }else {
                DistributorSelectionBean distributorSelectionBean = gson.fromJson(json, DistributorSelectionBean.class);
                MSFAApplication.DISTRIBUTOR_NAME = distributorSelectionBean.getCPName();
                MSFAApplication.DISTRIBUTOR_NO = distributorSelectionBean.getCPNo();
                getBeatsRemoteData(context, distributorSelectionBean.getCPGUID(), distributorSelectionBean.getCPNo());
            }
        }

    }
    private void getSalesPersonList() {
        try {
            if (OfflineManager.isOfflineStoreOpen()) {

                String routeQry = Constants.UserSalesPersons;
                LogManager.writeLogInfo("Distributor selection sales person qry : "+routeQry);
                salesManBeanList = new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(new UserSalesPersonBean())
                        .setODataEntitySet(Constants.UserSalesPersons)
                        .setODataEntityType(Constants.UserSalesPersonEntity)
                        .setUIListener(this)
                        .setQuery(routeQry)
                        .returnODataList(OfflineManager.offlineStore);
                LogManager.writeLogInfo("Distributor selection sales person list count : "+salesManBeanList.size());
                view.refreshData(salesManBeanList,1);
            }else{
                LogManager.writeLogError("Offline store not opened, unable to fetch UserSalesPersons data");
            }
        } catch (Throwable e) {
            LogManager.writeLogError("error while fetching UserSalesPersons data \n"+e.toString());
        }
    }
    public static String getCPConfigData(String cpNo,Context context) {
        String routeQry = Constants.CPConfigs+"?$select=STKEXPUPTO,CFGAtt1,CFGAtt2,CFGAtt3,CFGAtt5,CFGAtt6 &$filter=CPGuid eq '"+cpNo+"'";
        List cpConfigList = new OfflineUtils.ODataOfflineBuilder<>()
                .setHeaderPayloadObject(new CPConfigBean())
                .setODataEntitySet(Constants.CPConfigs)
                .setODataEntityType(Constants.CPConfigEntity)
                .setQuery(routeQry)
                .returnODataList(OfflineManager.offlineStore);
        if (cpConfigList!=null&&!cpConfigList.isEmpty()){
            MSFAApplication.AW_Outlet_Lock=((CPConfigBean)cpConfigList.get(0)).getCFGAtt2();
            MSFAApplication.STKEXPUPTO=((CPConfigBean)cpConfigList.get(0)).getSTKEXPUPTO();
            try {
                MSFAApplication.AW_TIME=((CPConfigBean)cpConfigList.get(0)).getCFGAtt3();
            } catch (Throwable e) {
                e.printStackTrace();
            }try {
                MSFAApplication.Validate_outlet_reVerf=((CPConfigBean)cpConfigList.get(0)).getCFGAtt6();
            } catch (Throwable e) {
                e.printStackTrace();
            }try {
                MSFAApplication.AW_GEO_FENCING=((CPConfigBean)cpConfigList.get(0)).getCFGAtt5();
                SharedPreferences sharedPerf = context.getSharedPreferences(Constants.PREFS_NAME, 0);
                SharedPreferences.Editor editor = sharedPerf.edit();
                editor.putString("AW_GEO_FENCING", MSFAApplication.AW_GEO_FENCING);
                editor.apply();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                String attr6 = ((CPConfigBean) cpConfigList.get(0)).getCFGAtt6();
                if (!TextUtils.isEmpty(attr6)) {
                    boolean isMultipleAW = checkT(attr6);
                    SharedPreferences sharedPerf = context.getSharedPreferences(Constants.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPerf.edit();
                    editor.putBoolean(Constants.TransporterModel, isMultipleAW);
                    editor.apply();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return ((CPConfigBean)cpConfigList.get(0)).getCFGAtt1();
        }else{
            return "";
        }
    }



    //TODO: Check if T=X ---> Transporter Model i.e Multiple AW
    public static boolean checkT(String input) {
        String regex = ".*T=(X|\"\"|).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String tValue = matcher.group(1);
            return "X".equals(tValue);
        }
        return false;
    }

    */
/**
     * This will return the Beat OfflineData
     *//*

    public static int beatCount=0;
    public static int retailerCount=0;
    public static void getBeatsRemoteData(Context context,String parentNo,String withOutZeroParebtNo) {
        executorService = Executors.newFixedThreadPool(11);
        executorService.submit(new Runnable() {
            @Override
            public void run() {

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
                try {
                    LogManager.writeLogDebug("Beats loaing");
                    if (retailerDataListener!=null)retailerDataListener.requestStarted();
                    Constants.writeTimeLog("beats loop start");
                    SyncSelectionActivity.isLocalSync=false;
                    if (beatList!=null&&!beatList.isEmpty()){
                        beatList.clear();
                    }
                    isOutletsLoaded=false;
                    if(SSInvoiceColorList!=null)SSInvoiceColorList.clear();
                    if(visitedColorList!=null)visitedColorList.clear();
                    if(SSSOColorList!=null)SSSOColorList.clear();
                    if(targetsItemsList!=null)targetsItemsList.clear();
                    LogManager.writeLogDebug("Stock list  start");
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            fetchStockMaterials();
                        }
                    });
                    LogManager.writeLogDebug("Stock list  end");
                    LogManager.writeLogDebug("SSSOColorList  start");
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            SSSOColorList();
                        }
                    });
                    LogManager.writeLogDebug("SSSOColorList  end");
                    if(!MSFAApplication.isCallCenter) {
                        executorService.submit(new Runnable() {
                            @Override
                            public void run() {
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
                                ComputeTargets();
//                            }
//                        }).start();
                            }
                        });
                    }
                    LogManager.writeLogDebug("ComputeTargets  start");
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            SSInvoiceColorList();
                        }
                    });
                    LogManager.writeLogDebug("SSInvoiceColorList  start");
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            VisitedColorList();
                        }
                    });
                    LogManager.writeLogDebug("VisitedColorList  start");
                    String routeSPQry="";
                    String routePlans="RoutePlans?$select=RschGuid,VisitDate &$filter=SPGuid eq guid'"+MSFAApplication.SPGUID+"' and VisitDate eq datetime'"+UtilConstants.getNewDate()+"'";
                    List plansList = new ArrayList();
                    plansList = new OfflineUtils.ODataOfflineBuilder<>()
                            .setHeaderPayloadObject(new RoutePlansBean())
                            .setODataEntitySet(Constants.RoutePlans)
                            .setODataEntityType(".RoutePlan")
                            .setQuery(routePlans)
                            .returnODataList(OfflineManager.offlineStore);
                    LogManager.writeLogDebug("Routeplans loaded");
                    Map<String,String> mapRouteScheduleDate = new HashMap<>();
                    List SPlist = new ArrayList();
                    if (plansList!=null&&!plansList.isEmpty()){
                        String qry = "";
                        for(int i=0;i<plansList.size();i++){
                            RoutePlansBean bean = (RoutePlansBean) plansList.get(0);
                            mapRouteScheduleDate.put(bean.getRschGuid(),bean.getVisitDate());
                            if(!TextUtils.isEmpty(qry)){
                                qry = qry + " or "+"RouteSchGUID eq guid'"+bean.getRschGuid()+"'";
                            }else{
                                qry = qry + "RouteSchGUID eq guid'"+bean.getRschGuid()+"'";
                            }
                        }

                        routeSPQry = Constants.RouteScheduleSPs+"?$select=RouteSchGUID,OrderSeqID &$filter=SalesPersonID eq guid'"+MSFAApplication.SPGUID+"' and ParentNo eq '"+parentNo+"' and ("+qry+")";
                        SPlist = new OfflineUtils.ODataOfflineBuilder<>()
                                .setHeaderPayloadObject(new RouteScheduleSPBean())
                                .setODataEntitySet(Constants.RouteScheduleSPs)
                                .setODataEntityType(Constants.RouteScheduleSPsEntity)
                                .setQuery(routeSPQry)
                                .returnODataList(OfflineManager.offlineStore);
                        LogManager.writeLogDebug("RouteScheduleSPs loaded");
                        if (SPlist!=null&&!SPlist.isEmpty()) {
                            String routeQry = Constants.RouteSchedules + "?$select=BeatCatID,Description,RouteSchGUID,RoutId &$filter=(StatusID eq '01' and ApprovalStatus eq '03') and (";
                            for (int i = 0; i < SPlist.size(); i++) {
                                Object object1 = SPlist.get(i);
                                if (object1 instanceof RouteScheduleSPBean) {
                                    RouteScheduleSPBean bean1 = (RouteScheduleSPBean) object1;
                                    if (!TextUtils.isEmpty(bean1.getRouteSchGUID())) {
                                        if (i != SPlist.size() - 1) {
                                            routeQry = routeQry.concat("RouteSchGUID eq guid'" + bean1.getRouteSchGUID() + "' or ");
                                        } else {
                                            routeQry = routeQry.concat("RouteSchGUID eq guid'" + bean1.getRouteSchGUID() + "')");
                                        }
                                    }
                                }
                            }
                            beatList .addAll( new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean())
                                    .setODataEntitySet(Constants.RouteSchedules)
                                    .setODataEntityType(Constants.RouteScheduleEntity)
                                    .setQuery(routeQry)
                                    .returnODataList(OfflineManager.offlineStore));
                            if(beatList.size()>0){
                                for(Object ob : beatList){
                                    if (ob instanceof com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean) {
                                        com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean beatSelectionBean = (BeatSelectionBean) ob;
                                        if(mapRouteScheduleDate.containsKey(beatSelectionBean.getRouteSchGUID())){
                                            try {
                                                Calendar calendar = Calendar.getInstance();
                                                String s = mapRouteScheduleDate.get(beatSelectionBean.getRouteSchGUID());
                                                String[] strDate =s.split("/");
                                                calendar.set(Integer.parseInt(strDate[2]), Integer.parseInt(strDate[1])-1, Integer.parseInt(strDate[0]));
                                                int week = calendar.get(Calendar.DAY_OF_WEEK)-1;
                                                int month = calendar.get(Calendar.MONTH);
                                                beatSelectionBean.setDefaultWeekOfDay(Constants.getWeekOfTheFullDay(""+week));
                                                beatSelectionBean.setIsTodayBeat("X");
                                            } catch (Throwable e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }*/
/*else{*//*

                    if (MSFAApplication.isAWSM) {
                        routeSPQry = Constants.RouteScheduleSPs+"?$select=RouteSchGUID,OrderSeqID &$filter=SalesPersonID eq guid'"+MSFAApplication.SPGUID+"' and ParentNo eq '"+parentNo+"'";
                    }else{
                        if(MSFAApplication.isBVAN){
                            routeSPQry = Constants.RouteScheduleSPs + "?$select=RouteSchGUID,OrderSeqID,ParentNo,ParentName";
                        }else {
                            routeSPQry = Constants.RouteScheduleSPs + "?$select=RouteSchGUID,OrderSeqID,ParentNo,ParentName &$filter=SalesPersonID eq guid'" + MSFAApplication.SPGUID + "' and ParentNo eq '" + parentNo + "'";
                        }
                    }
                    */
/*}*//*

                    SPlist = new OfflineUtils.ODataOfflineBuilder<>()
                            .setHeaderPayloadObject(new RouteScheduleSPBean())
                            .setODataEntitySet(Constants.RouteScheduleSPs)
                            .setODataEntityType(Constants.RouteScheduleSPsEntity)
                            .setQuery(routeSPQry)
                            .returnODataList(OfflineManager.offlineStore);
                    LogManager.writeLogDebug("RouteScheduleSPs loaded");
                    if (SPlist!=null&&!SPlist.isEmpty()) {
                        String routeQry=Constants.RouteSchedules + "?$select=BeatCatID,Description,RouteSchGUID,RoutId &$filter=(StatusID eq '01' and ApprovalStatus eq '03') and (";
                        for (int i = 0; i < SPlist.size(); i++) {
                            Object object1 =SPlist.get(i);
                            if (object1 instanceof RouteScheduleSPBean){
                                RouteScheduleSPBean bean1 = (RouteScheduleSPBean) object1;
                                if (!TextUtils.isEmpty(bean1.getRouteSchGUID())){
                                    if (i!=SPlist.size()-1) {
                                        routeQry =routeQry.concat("RouteSchGUID eq guid'"+bean1.getRouteSchGUID()+"' or ");
                                    }else{
                                        routeQry =routeQry.concat("RouteSchGUID eq guid'"+bean1.getRouteSchGUID()+"')");
                                    }
                                }
                            }
                        }
                        beatList.addAll(new OfflineUtils.ODataOfflineBuilder<>()
                                .setHeaderPayloadObject(new BeatSelectionBean())
                                .setODataEntitySet(Constants.RouteSchedules)
                                .setODataEntityType(Constants.RouteScheduleEntity)
                                .setQuery(routeQry)
                                .returnODataList(OfflineManager.offlineStore));
                        for (Object object:SPlist) {
                            RouteScheduleSPBean bean1 = (RouteScheduleSPBean) object;
                            try {
                                if (Build.VERSION.SDK_INT >= 24) {
                                    beatList.stream().filter(predicate -> ((BeatSelectionBean) predicate)
                                                    .getRouteSchGUID().equalsIgnoreCase(bean1.getRouteSchGUID()))
                                            .forEach(predicate -> {
                                                ((BeatSelectionBean) predicate).setOrderBy(bean1.getOrderSeqID());
                                                ((BeatSelectionBean) predicate).setParentName(bean1.getParentName());
                                                ((BeatSelectionBean) predicate).setParentNo(bean1.getParentNo());
                                            });
                                }else{
                                    Stream.of(beatList)
                                            .filter(predicate -> ((BeatSelectionBean)predicate).getRouteSchGUID().equalsIgnoreCase(bean1.getRouteSchGUID()))
                                            .forEach(predicate -> {
                                                ((BeatSelectionBean) predicate).setOrderBy(bean1.getOrderSeqID());
                                                ((BeatSelectionBean) predicate).setParentName(bean1.getParentName());
                                                ((BeatSelectionBean) predicate).setParentNo(bean1.getParentNo());
                                            });
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                        LogManager.writeLogDebug("RouteSchedules loaded");
                        if (plansList!=null&&!plansList.isEmpty()) {
                            BeatSelectionBean beatSelectionBean = (BeatSelectionBean) beatList.get(0);
//                            RetailerSelectionViewModel.getSplitBeatData(beatSelectionBean.getRouteSchGUID(),beatSelectionBean.getBeatCatID());
                        }
                        ArrayList<String> alRouteguid = new ArrayList<>();
                        List beatListTemp = new ArrayList();
                        for(int i=0;i<beatList.size();i++){
                            com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean bean1 = (BeatSelectionBean) beatList.get(i);
                            if(mapRouteScheduleDate.containsKey(bean1.getRouteSchGUID())){
                                bean1.setIsTodayBeat("X");
                            }
                            if(!alRouteguid.contains(bean1.getRouteSchGUID())){
                                alRouteguid.add(bean1.getRouteSchGUID());
                                beatListTemp.add(bean1);
                            }
                        }
                        beatList.clear();
                        beatList .addAll(beatListTemp);
                        LogManager.writeLogDebug("Beats Size - "+beatList.size());
                        LogManager.writeLogDebug("Beats completed");
                        LogManager.writeLogDebug("Retailer started loading");
                        beatCount = beatList.size();
                        isBeatLoaded = false;
                        getRetailersRemoteData(context,withOutZeroParebtNo);
                    } else {
                        isBeatLoaded = false;
                        isOutletsLoaded = true;
                        beatCount=0;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    isBeatLoaded = false;
                    isOutletsLoaded=true;
                }
//            }
//        });
//        thread.setPriority(Thread.MAX_PRIORITY);
//        thread.start();
            }
        });
    }



    public static void getBeatsRemoteData(Context context,String parentNo) {
        executorService = Executors.newFixedThreadPool(11);
        executorService.submit(new Runnable() {
            @Override
            public void run() {

//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
                try {
                    LogManager.writeLogDebug("Beats loaing");
                    if (retailerDataListener!=null)retailerDataListener.requestStarted();
                    Constants.writeTimeLog("beats loop start");
                    SyncSelectionActivity.isLocalSync=false;
                    if (beatList!=null&&!beatList.isEmpty()){
                        beatList.clear();
                    }
                    isOutletsLoaded=false;
                    if(SSInvoiceColorList!=null)SSInvoiceColorList.clear();
                    if(visitedColorList!=null)visitedColorList.clear();
                    if(SSSOColorList!=null)SSSOColorList.clear();
                    if(targetsItemsList!=null)targetsItemsList.clear();
                    LogManager.writeLogDebug("Stock list  start");
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            fetchStockMaterials();
                        }
                    });
                    LogManager.writeLogDebug("Stock list  end");
                    LogManager.writeLogDebug("SSSOColorList  start");
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            SSSOColorList();
                        }
                    });
                    LogManager.writeLogDebug("SSSOColorList  end");
                    if(!MSFAApplication.isCallCenter) {
                        executorService.submit(new Runnable() {
                            @Override
                            public void run() {
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
                                ComputeTargets();
//                                    }
//                                }).start();
                            }
                        });
                    }
                    beatList.clear();
                    LogManager.writeLogDebug("ComputeTargets  start");
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            SSInvoiceColorList();
                        }
                    });
                    LogManager.writeLogDebug("SSInvoiceColorList  start");
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            VisitedColorList();
                        }
                    });
                    LogManager.writeLogDebug("VisitedColorList  start");
                    String routeSPQry="";
                    String routePlans="RoutePlans?$select=RschGuid,VisitDate &$filter=SPGuid eq guid'"+MSFAApplication.SPGUID+"' and VisitDate eq datetime'"+UtilConstants.getNewDate()+"'";
                    List plansList = new ArrayList();
                    plansList = new OfflineUtils.ODataOfflineBuilder<>()
                            .setHeaderPayloadObject(new RoutePlansBean())
                            .setODataEntitySet(Constants.RoutePlans)
                            .setODataEntityType(".RoutePlan")
                            .setQuery(routePlans)
                            .returnODataList(OfflineManager.offlineStore);
                    LogManager.writeLogDebug("Routeplans loaded");
                    Map<String,String> mapRouteScheduleDate = new HashMap<>();
                    List SPlist = new ArrayList();
                    if (plansList!=null&&!plansList.isEmpty()){
                        String qry = "";
                        for(int i=0;i<plansList.size();i++){
                            RoutePlansBean bean = (RoutePlansBean) plansList.get(0);
                            mapRouteScheduleDate.put(bean.getRschGuid(),bean.getVisitDate());
                            if(!TextUtils.isEmpty(qry)){
                                qry = qry + " or "+"RouteSchGUID eq guid'"+bean.getRschGuid()+"'";
                            }else{
                                qry = qry + "RouteSchGUID eq guid'"+bean.getRschGuid()+"'";
                            }
                        }

                        routeSPQry = Constants.RouteScheduleSPs+"?$select=RouteSchGUID,OrderSeqID &$filter=SalesPersonID eq guid'"+MSFAApplication.SPGUID+"' and ParentNo eq '"+parentNo+"' and ("+qry+")";
                        SPlist = new OfflineUtils.ODataOfflineBuilder<>()
                                .setHeaderPayloadObject(new RouteScheduleSPBean())
                                .setODataEntitySet(Constants.RouteScheduleSPs)
                                .setODataEntityType(Constants.RouteScheduleSPsEntity)
                                .setQuery(routeSPQry)
                                .returnODataList(OfflineManager.offlineStore);
                        LogManager.writeLogDebug("RouteScheduleSPs loaded");
                        if (SPlist!=null&&!SPlist.isEmpty()) {
                            String routeQry = Constants.RouteSchedules + "?$select=BeatCatID,Description,RouteSchGUID,RoutId &$filter=(StatusID eq '01' and ApprovalStatus eq '03') and (";
                            for (int i = 0; i < SPlist.size(); i++) {
                                Object object1 = SPlist.get(i);
                                if (object1 instanceof RouteScheduleSPBean) {
                                    RouteScheduleSPBean bean1 = (RouteScheduleSPBean) object1;
                                    if (!TextUtils.isEmpty(bean1.getRouteSchGUID())) {
                                        if (i != SPlist.size() - 1) {
                                            routeQry = routeQry.concat("RouteSchGUID eq guid'" + bean1.getRouteSchGUID() + "' or ");
                                        } else {
                                            routeQry = routeQry.concat("RouteSchGUID eq guid'" + bean1.getRouteSchGUID() + "')");
                                        }
                                    }
                                }
                            }
                            beatList .addAll( new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean())
                                    .setODataEntitySet(Constants.RouteSchedules)
                                    .setODataEntityType(Constants.RouteScheduleEntity)
                                    .setQuery(routeQry)
                                    .returnODataList(OfflineManager.offlineStore));
                            if(beatList.size()>0){
                                for(Object ob : beatList){
                                    if (ob instanceof com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean) {
                                        com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean beatSelectionBean = (BeatSelectionBean) ob;
                                        if(mapRouteScheduleDate.containsKey(beatSelectionBean.getRouteSchGUID())){
                                            try {
                                                Calendar calendar = Calendar.getInstance();
                                                String s = mapRouteScheduleDate.get(beatSelectionBean.getRouteSchGUID());
                                                String[] strDate =s.split("/");
                                                calendar.set(Integer.parseInt(strDate[2]), Integer.parseInt(strDate[1])-1, Integer.parseInt(strDate[0]));
                                                int week = calendar.get(Calendar.DAY_OF_WEEK)-1;
                                                int month = calendar.get(Calendar.MONTH);
                                                beatSelectionBean.setDefaultWeekOfDay(Constants.getWeekOfTheFullDay(""+week));
                                                beatSelectionBean.setIsTodayBeat("X");
                                            } catch (Throwable e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }*/
/*else{*//*

                    if (MSFAApplication.isAWSM) {
                        routeSPQry = Constants.RouteScheduleSPs+"?$select=RouteSchGUID,OrderSeqID &$filter=SalesPersonID eq guid'"+MSFAApplication.SPGUID+"' and ParentNo eq '"+parentNo+"'";
                    }else{
                        routeSPQry = Constants.RouteScheduleSPs+"?$select=RouteSchGUID,OrderSeqID &$filter=SalesPersonID eq guid'"+MSFAApplication.SPGUID+"' and ParentNo eq '"+parentNo+"'";
                    }
                    */
/*}*//*

                    SPlist = new OfflineUtils.ODataOfflineBuilder<>()
                            .setHeaderPayloadObject(new RouteScheduleSPBean())
                            .setODataEntitySet(Constants.RouteScheduleSPs)
                            .setODataEntityType(Constants.RouteScheduleSPsEntity)
                            .setQuery(routeSPQry)
                            .returnODataList(OfflineManager.offlineStore);
                    LogManager.writeLogDebug("RouteScheduleSPs loaded");
                    if (SPlist!=null&&!SPlist.isEmpty()) {
                        String routeQry=Constants.RouteSchedules + "?$select=BeatCatID,Description,RouteSchGUID,RoutId &$filter=(StatusID eq '01' and ApprovalStatus eq '03') and (";
                        for (int i = 0; i < SPlist.size(); i++) {
                            Object object1 =SPlist.get(i);
                            if (object1 instanceof RouteScheduleSPBean){
                                RouteScheduleSPBean bean1 = (RouteScheduleSPBean) object1;
                                if (!TextUtils.isEmpty(bean1.getRouteSchGUID())){
                                    if (i!=SPlist.size()-1) {
                                        routeQry =routeQry.concat("RouteSchGUID eq guid'"+bean1.getRouteSchGUID()+"' or ");
                                    }else{
                                        routeQry =routeQry.concat("RouteSchGUID eq guid'"+bean1.getRouteSchGUID()+"')");
                                    }
                                }
                            }
                        }
                        beatList .addAll( new OfflineUtils.ODataOfflineBuilder<>()
                                .setHeaderPayloadObject(new com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean())
                                .setODataEntitySet(Constants.RouteSchedules)
                                .setODataEntityType(Constants.RouteScheduleEntity)
                                .setQuery(routeQry)
                                .returnODataList(OfflineManager.offlineStore));
                        for (Object object:SPlist) {
                            RouteScheduleSPBean bean1 = (RouteScheduleSPBean) object;
                            try {
                                if (Build.VERSION.SDK_INT >= 24) {
                                    beatList.stream().filter(predicate -> ((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean) predicate)
                                                    .getRouteSchGUID().equalsIgnoreCase(bean1.getRouteSchGUID()))
                                            .forEach(predicate->((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean) predicate).setOrderBy(bean1.getOrderSeqID()));
                                }else{
                                    Stream.of(beatList)
                                            .filter(predicate -> ((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean)predicate).getRouteSchGUID().equalsIgnoreCase(bean1.getRouteSchGUID()))
                                            .forEach(predicate->((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean) predicate).setOrderBy(bean1.getOrderSeqID()));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                        LogManager.writeLogDebug("RouteSchedules loaded");
                        if (plansList!=null&&!plansList.isEmpty()) {
                            com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean beatSelectionBean = (com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean) beatList.get(0);
//                            RetailerSelectionViewModel.getSplitBeatData(beatSelectionBean.getRouteSchGUID(),beatSelectionBean.getBeatCatID());
                        }
                        ArrayList<String> alRouteguid = new ArrayList<>();
                        List beatListTemp = new ArrayList();
                        for(int i=0;i<beatList.size();i++){
                            com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean bean1 = (BeatSelectionBean) beatList.get(i);
                            if(mapRouteScheduleDate.containsKey(bean1.getRouteSchGUID())){
                                bean1.setIsTodayBeat("X");
                            }
                            if(!alRouteguid.contains(bean1.getRouteSchGUID())){
                                alRouteguid.add(bean1.getRouteSchGUID());
                                beatListTemp.add(bean1);
                            }
                        }
                        beatList.clear();
                        beatList .addAll(beatListTemp);
                        LogManager.writeLogDebug("Beats Size - "+beatList.size());
                        LogManager.writeLogDebug("Beats completed");
                        LogManager.writeLogDebug("Retailer started loading");
                        beatCount = beatList.size();
                        getRetailersRemoteData(context);
                    } else {
                        isOutletsLoaded = true;
                        beatCount=0;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    isOutletsLoaded=true;
                }
//            }
//        });
//        thread.setPriority(Thread.MAX_PRIORITY);
//        thread.start();
            }
        });
    }

    private static void SSInvoiceColorList(){
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
        String qry = "SSInvoices?$select=SoldToCPGUID &$filter=InvoiceDate ge datetime'"+Constants.getFirstDateOfCurrentMonth()+"' and StatusID ne '02'";
        SSInvoiceColorList = new OfflineUtils.ODataOfflineBuilder<>()
                .setHeaderPayloadObject(new ColorBean())
                .setODataEntitySet("SSInvoices")
                .setODataEntityType(".SSInvoice")
                .setQuery(qry)
                .returnODataList(OfflineManager.offlineStore);
        LogManager.writeLogDebug("SSInvoice data loaded with "+SSInvoiceColorList.size());
//            }
//        });
//        thread.start();
    }
    private static void VisitedColorList(){
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
        String qry = "Visits?$select=CPGUID,Reason,ReasonDesc &$filter=VisitDate ge datetime'"+ com.arteriatech.mutils.common.UtilConstants.getNewDate()+"' and SPGUID eq guid'"+Constants.getSPGUID()+"'";
        visitedColorList = new OfflineUtils.ODataOfflineBuilder<>()
                .setHeaderPayloadObject(new VisitBean())
                .setODataEntitySet("Visits")
                .setODataEntityType(".Visit")
                .setQuery(qry)
                .returnODataList(OfflineManager.offlineStore);
        List visitedColorListTemp = new ArrayList();
        for(Object object : visitedColorList){
            VisitBean visitBean1 = (VisitBean) object;
            if(!TextUtils.isEmpty(visitBean1.getReason())){
                visitedColorListTemp.add(visitBean1);
            }
        }
        visitedColorList.clear();
        visitedColorList.addAll(visitedColorListTemp);
        LogManager.writeLogDebug("visited data loaded with "+visitedColorList.size());
//            }
//        });
//        thread.start();
    }
    private static void SSSOColorList(){
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
        String saleOrderQry = Constants.SSSOs + "?$select=SoldToCPGUID,SSSOGuid &$filter=OrderDate eq datetime'" + UtilConstants.getNewDate() + "' and " + Constants.SPGUID + " eq guid'" + MSFAApplication.SPGUID + "'";
        SSSOColorList = new OfflineUtils.ODataOfflineBuilder<>()
                .setHeaderPayloadObject(new ColorBean())
                .setODataEntitySet("SSSOs")
                .setODataEntityType(".SSSO")
                .setQuery(saleOrderQry)
                .returnODataList(OfflineManager.offlineStore);
        String itemQry = Constants.SSSoItemDetails + "?$select=MaterialNo,SSSOGuid,SkuGroup &$filter=(";
        for (int i = 0; i < SSSOColorList.size(); i++) {
            ColorBean colorBean = (ColorBean) SSSOColorList.get(i);
            if (!TextUtils.isEmpty(colorBean.getSSSOGuid())){
                if (i!=SSSOColorList.size()-1) {
                    itemQry =itemQry.concat("SSSOGuid eq guid'"+colorBean.getSSSOGuid()+"' or ");
                }else{
                    itemQry =itemQry.concat("SSSOGuid eq guid'"+colorBean.getSSSOGuid()+"')");
                }
            }
        }
        SSSOItemDetailsList = new OfflineUtils.ODataOfflineBuilder<>()
                .setHeaderPayloadObject(new SOItemGOBean())
                .setODataEntitySet(Constants.SSSoItemDetails)
                .setODataEntityType(Constants.SSSoItemDetailEntity)
                .setQuery(itemQry)
                .returnODataList(OfflineManager.offlineStore);

//            }
//        });
//        thread.start();
    }


    */
/**
     * This method will return Retailer List based on Beat GUID.
     * @param routeSchGUID
     *//*

    private static HashMap<String, String> mapInvCPList = null;
    public static void getRetailersRemoteData(Context context,String parentNO) {
        try {
            OutletSQLHelper sqlHelper = new OutletSQLHelper(context);
            */
/**
             * Looping each and every beat to get all retailers present in all beats. later will filter in retailer selection.
             *//*

            if (retailersList!=null&&!retailersList.isEmpty()){
                retailersList.clear();
            }
            List retailerTemp = new ArrayList();
            String routeCPQry ="";
            if(MSFAApplication.isBVAN){
                routeCPQry = Constants.ChannelPartners + "?$select=IsKeyCP,URL1,VATNo,ConstructionType,Tax1,Latitude,Longitude,Mobile3,Group3Desc,Group4Desc,Landline,Name,PostalCode,MobileVerifed,OwnerName,CPUID,CPGUID,Address1,Address2,Address3,Mobile1,CPTypeID,CPNo,CPTypeDesc,ActivationDate,DOB,Group8,Group2,Group8Desc,Group2Desc,StateID,StateDesc,DistrictID,DistrictDesc,CityID,CityDesc,TownID,TownDesc,WardID,WardName,WardGUID,SubDistrictGUID,Group1,Landmark,ParentID,ParentName &$filter=ApprvlStatusID eq '03' and StatusID eq '01' and (" + Constants.CPTypeID + " eq '50' or " + Constants.CPTypeID + " eq '20' or " + Constants.CPTypeID + " eq '10' or " + Constants.CPTypeID + " eq '01')";
            }else {
                routeCPQry = Constants.ChannelPartners + "?$select=IsKeyCP,Source,CreatedOn,URL1,VATNo,ConstructionType,Tax1,Latitude,Longitude,Mobile3,Group3,Group3Desc,Group4Desc,Landline,Name,PostalCode,MobileVerifed,OwnerName,CPUID,CPGUID,Address1,Address2,Address3,Mobile1,CPTypeID,CPNo,CPTypeDesc,ActivationDate,DOB,Group8,Group2,Group8Desc,Group2Desc,Group10Desc,Group10,StateID,StateDesc,DistrictID,DistrictDesc,CityID,CityDesc,TownID,TownDesc,WardID,WardName,WardGUID,SubDistrictGUID,Group1,Landmark,ParentID,ParentName &$filter=ApprvlStatusID eq '03' and StatusID eq '01' and (CPTypeID eq '20' or CPTypeID eq '10')";
            }
            final List retailerObject = new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new RetailerSelectionBean())
                    .setODataEntitySet(Constants.ChannelPartners)
                    .setODataEntityType(Constants.ChannelPartnerEntity)
                    .setQuery(routeCPQry)
                    .returnODataList(OfflineManager.offlineStore);

            LogManager.writeLogDebug("ChannelPartners loaded");
            try {
                if (retailerObject!=null&&!retailerObject.isEmpty()) {
                    List<RetailerSelectionBean>list = sqlHelper.getAllOutlets();
                    LogManager.writeLogDebug("SQL : fetching data with ChannelPartners size : "+list.size());
                    if (list!=null&&!list.isEmpty()) {
                        retailerObject.addAll(list);
                    }
                    retailerCount = retailerObject.size();
                }else{
                    retailerCount=0;
                    isOutletsLoaded=true;
                }
            } catch (Throwable e) {
                LogManager.writeLogDebug("SQL : error fetching data with ChannelPartners"+e.getMessage());
            }
            String cpGuidQry = "";
//            for(Object object : retailerObject){
//                RetailerSelectionBean selectionBean = (RetailerSelectionBean) object;
//                if(TextUtils.isEmpty(cpGuidQry)){
//                    cpGuidQry = cpGuidQry+"VisitCPGUID eq '"+selectionBean.getCPGUID().replaceAll("-","").toUpperCase()+"'";
//                }else {
//                    cpGuidQry = cpGuidQry+" or VisitCPGUID eq '"+selectionBean.getCPGUID().replaceAll("-","").toUpperCase()+"'";
//                }
//            }
            String routeQry = "";
            if(MSFAApplication.isBVAN){
                routeQry = Constants.RouteSchedulePlans + "?$select=SequenceNo,VisitCPGUID,RouteSchGUID &$filter=(VisitCPTypeID eq '50' or VisitCPTypeID eq '20' or VisitCPTypeID eq '10' or VisitCPTypeID eq '01')";
            }else {
                routeQry = Constants.RouteSchedulePlans + "?$select=SequenceNo,VisitCPGUID,RouteSchGUID &$filter=(VisitCPTypeID eq '20' or VisitCPTypeID eq '10')";
            }

//            routeQry = routeQry.concat("");
            List routeSchedulePlansList = new ArrayList();
            */
/**
             * Get routes w.r.t to RouteSchedules where will get guid Value to be passed to CP table to fetch available retaileres,
             *//*

            routeSchedulePlansList = new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new RouteSchPlansBean())
                    .setODataEntitySet(Constants.RouteSchedulePlans)
                    .setODataEntityType(Constants.RouteSchedulePlanEntity)
                    .setQuery(routeQry)
                    .returnODataList(OfflineManager.offlineStore);
            try {
                if (routeSchedulePlansList!=null&&!routeSchedulePlansList.isEmpty()) {
//                    List<RouteSchPlansBean>list = sqlHelper.getRouteSchedulePlansSqlList();
//                    LogManager.writeLogDebug("SQL : fetching data with RouteSchedulePlans size : "+list.size());
//                    if (list!=null&&!list.isEmpty()) {
//                        routeSchedulePlansList.addAll(list);
//                    }
                }
            } catch (Throwable e) {
                LogManager.writeLogDebug("SQL : error fetching data with RouteSchedulePlans"+e.getMessage());
            }
            LogManager.writeLogDebug("RouteSchedulePlans loaded");
            int orderBy=1;
            CommonDB commonDB = new CommonDB(context);
            for (int j = 0; j < beatList.size(); j++) {
//                LogManager.writeLogDebug("beat loop started");
                final boolean[] isSequenceMaintained = {false};
                if (retailerDataListener!=null)retailerDataListener.requestProgress();
                BeatSelectionBean bean = (BeatSelectionBean) beatList.get(j);
                if (bean.getOrderBy()!=null&&!TextUtils.isEmpty(bean.getOrderBy())) {
                    orderBy = Integer.parseInt(bean.getOrderBy());
                }
                if (!TextUtils.isEmpty(bean.getRouteSchGUID())) {
                    List plansList= new ArrayList(routeSchedulePlansList.size());
                    if (Build.VERSION.SDK_INT >= 24) {
                        plansList = (List) routeSchedulePlansList.stream().filter((Predicate<Object>) predicate -> ((RouteSchPlansBean) predicate).getRouteSchGUID().equalsIgnoreCase(bean.getRouteSchGUID())).collect(java.util.stream.Collectors.toList());
                    } else {
                        plansList = (List) Stream.of(routeSchedulePlansList).filter((com.annimon.stream.function.Predicate<Object>) predicate -> ((RouteSchPlansBean) predicate).getRouteSchGUID().equalsIgnoreCase(bean.getRouteSchGUID())).collect(com.annimon.stream.Collectors.toList());
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        isSequenceMaintained[0] = plansList.stream().anyMatch(predicate -> {
                            RouteSchPlansBean bean1 = (RouteSchPlansBean) predicate;
                            return bean1.getSequenceNo() == null ||bean1.getSequenceNo().equalsIgnoreCase("");
                        });
                    } else {
                        isSequenceMaintained[0] = Stream.of(plansList).anyMatch(predicate -> {
                            RouteSchPlansBean bean1 = (RouteSchPlansBean) predicate;
                            return bean1.getSequenceNo() == null ||bean1.getSequenceNo().equalsIgnoreCase("");
                        });
                    }
                    if (isSequenceMaintained[0]) {
                        bean.setSequenceMaintained(false);
                    }else{
                        bean.setSequenceMaintained(true);
                    }

                    for (Object object : plansList) {
                        if (object instanceof RouteSchPlansBean) {
                            RouteSchPlansBean bean1 = (RouteSchPlansBean) object;
                            try {
                                if (Build.VERSION.SDK_INT >= 24) {
                                    retailerObject.stream().filter(predicate -> ((RetailerSelectionBean) predicate)
                                                    .getCPGUID().replace("-", "").equalsIgnoreCase(bean1.getVisitCPGUID()))
                                            .forEach(predicate -> {
                                                ((RetailerSelectionBean) predicate).setBeatGUID(bean1.getRouteSchGUID());
                                                ((RetailerSelectionBean) predicate).setBeatCatID(bean.getBeatCatID());
                                                if (bean1.getSequenceNo() != null && !bean1.getSequenceNo().isEmpty()) {
                                                    int value = Integer.parseInt(bean1.getSequenceNo().trim());
                                                    ((RetailerSelectionBean) predicate).setSequenceNo(value);
                                                    ((RetailerSelectionBean) predicate).setIndexSequenceNo(value);
                                                }
//                                                ((RetailerSelectionBean) predicate).setProductType(getSplitBeatCPData(((RetailerSelectionBean) predicate).getBeatGUID(),((RetailerSelectionBean) predicate).getBeatCatID(),((RetailerSelectionBean) predicate).getCPGUID()));
                                                retailerTemp.add(setOuletBeansValue((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) predicate));
                                            });
                                } else {
                                    Stream.of(retailerObject)
                                            .filter(predicate -> ((RetailerSelectionBean) predicate).getCPGUID().replace("-", "").equalsIgnoreCase(bean1.getVisitCPGUID()))
                                            .forEach(predicate -> {
                                                ((RetailerSelectionBean) predicate).setBeatGUID(bean1.getRouteSchGUID());
                                                ((RetailerSelectionBean) predicate).setBeatCatID(bean.getBeatCatID());
                                                if (bean1.getSequenceNo() != null && !bean1.getSequenceNo().isEmpty()) {
                                                    int value = Integer.parseInt(bean1.getSequenceNo().trim());
                                                    ((RetailerSelectionBean) predicate).setSequenceNo(value);
                                                    ((RetailerSelectionBean) predicate).setIndexSequenceNo(value);
                                                }
//                                                ((RetailerSelectionBean) predicate).setProductType(getSplitBeatCPData(((RetailerSelectionBean) predicate).getBeatGUID(),((RetailerSelectionBean) predicate).getBeatCatID(),((RetailerSelectionBean) predicate).getCPGUID()));
                                                retailerTemp.add(setOuletBeansValue((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) predicate));
                                            });
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
            LogManager.writeLogDebug("beat loop ended");
            */
/**
             * Here appending some circumstances to display colors w.r.t Sales Order entry for retailer
             *//*

            try {
                retailersList.clear();
            } catch (Throwable e) {
                e.printStackTrace();
            }
//            retailersList.addAll(retailerObject);
            retailersList.addAll(retailerTemp);
            retailerCount = retailerTemp.size();
            try {
                CPPerformanceBean cpPerformanceBean = null;
                if(commonDB==null){
                    commonDB = new CommonDB(context);
                }
                if (commonDB.getRecordExistNew(MSFAApplication.SPGUID,parentNO)) {
                    cpPerformanceList = commonDB.getCPDataNew(MSFAApplication.SPGUID,parentNO);
                }else {
                    try {
                        String qry = Constants.CPPerformances + "?$filter=SPGUID eq guid'" + MSFAApplication.SPGUID + "' and ParentNo eq '" + parentNO + "'";
                        cpPerformanceList = new OfflineUtils.ODataOfflineBuilder<>()
                                .setHeaderPayloadObject(new CPPerformanceBean())
                                .setODataEntitySet(Constants.CPPerformances)
                                .setODataEntityType(Constants.CPPerformanceEntity)
                                .setQuery(qry)
                                .returnODataList(OfflineManager.offlineStore);
                        if (cpPerformanceList.size()>0){
                            LogManager.writeLogDebug("Before CPPerformance INsertion");
                            for (int i=0;i<cpPerformanceList.size();i++){
                                CPPerformanceBean cpPerformanceBean1=new CPPerformanceBean();
                                cpPerformanceBean1=(CPPerformanceBean) cpPerformanceList.get(i);
                                commonDB.createRecord(cpPerformanceBean1);
                            }
                            LogManager.writeLogDebug("After CPPerformance INsertion");
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            List<String> datavalutList = OfflineManager.getSOListFromDataVault(context);
            for (int i = 0; i < retailersList.size(); i++) {
                Log.e("Retailer Time","invoice"+i);

//                LogManager.writeLogDebug("Retailer Time :"+i);
                if (retailerDataListener!=null)retailerDataListener.requestProgress();
                RetailerSelectionBean item = (RetailerSelectionBean) retailersList.get(i);
//                item.setProductType(getSplitBeatCPData(item.getBeatGUID(),item.getBeatCatID(),item.getCPGUID()));
//                String saleOrderQry = Constants.SSSOs + "?$select=SSSOGuid &$filter=SoldToCPGUID eq guid'" + item.getCPGUID() + "' and " + Constants.OrderDate + " eq datetime'" + UtilConstants.getNewDate() + "' and " + Constants.SPGUID + " eq guid'" + MSFAApplication.SPGUID + "'";
                try {
                    boolean isInvoiceAvailable =false;
                    boolean isVisitAvailable =false;
                    boolean isSaleOrderAval =false;
                    boolean isLocalOrder =false;
                    if (Build.VERSION.SDK_INT >= 24) {
                        isInvoiceAvailable = SSInvoiceColorList.stream().anyMatch(predicate -> {
                            ColorBean bean1 = (ColorBean) predicate;
                            return bean1.getSoldToCPGUID() != null && item.getCPGUID().equalsIgnoreCase(bean1.getSoldToCPGUID());
                        });
                    } else {
                        isInvoiceAvailable = Stream.of(SSInvoiceColorList).anyMatch(predicate -> {
                            ColorBean bean1 = (ColorBean) predicate;
                            return bean1.getSoldToCPGUID() != null && item.getCPGUID().equalsIgnoreCase(bean1.getSoldToCPGUID());
                        });
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        isVisitAvailable = visitedColorList.stream().anyMatch(predicate -> {
                            VisitBean bean1 = (VisitBean) predicate;
                            return bean1.getCPGUID() != null && item.getCPGUID().replaceAll("-","").toLowerCase().equalsIgnoreCase(bean1.getCPGUID().toLowerCase());
                        });
                    } else {
                        isVisitAvailable = Stream.of(visitedColorList).anyMatch(predicate -> {
                            VisitBean bean1 = (VisitBean) predicate;
                            return bean1.getCPGUID() != null && item.getCPGUID().replaceAll("-","").toLowerCase().equalsIgnoreCase(bean1.getCPGUID().toLowerCase());
                        });
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        isSaleOrderAval = SSSOColorList.stream().anyMatch(predicate -> {
                            ColorBean bean1 = (ColorBean) predicate;
                            return bean1.getSoldToCPGUID() != null && item.getCPGUID().equalsIgnoreCase(bean1.getSoldToCPGUID());
                        });
                    } else {
                        isSaleOrderAval = Stream.of(SSSOColorList).anyMatch(predicate -> {
                            ColorBean bean1 = (ColorBean) predicate;
                            return bean1.getSoldToCPGUID() != null && item.getCPGUID().equalsIgnoreCase(bean1.getSoldToCPGUID());
                        });
                    }
                    boolean isInvoiceExist = OfflineManager.isInvoiceExist(context,item.getCPGUID());
                    if (isInvoiceExist){
                        isInvoiceAvailable =true;
                    }

                    if (isInvoiceAvailable){
                        item.setInvoiceAval("X");
                        if (MSFAApplication.isAWSM) {
                            item.setColor(ContextCompat.getColor(context,R.color.bil_theme_dark_clr));
                        }else{
                            item.setColor(ContextCompat.getColor(context,R.color.GREEN));
                        }
                    }else{
                        if (MSFAApplication.isVAN) {
                            item.setInvoiceAval("");
                            item.setColor(ContextCompat.getColor(context, R.color.eod_color));
                        }else if (MSFAApplication.isBVAN) {
                            item.setInvoiceAval("");
                            item.setColor(ContextCompat.getColor(context, R.color.eod_color));
                        }else {
                            item.setInvoiceAval("");
                            item.setColor(ContextCompat.getColor(context,R.color.RED));
                        }
                    }
                    if (MSFAApplication.isAWSM) {
                        if (isSaleOrderAval) {
                            item.setSaleOrderAval("X");
                            item.setSaleOrderAvalTemp("X");
                            item.setColor(ContextCompat.getColor(context,R.color.order_yelloe));
                        } else {
                            item.setSaleOrderAval("");
                            item.setSaleOrderAvalTemp("");
                        }
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        isLocalOrder = datavalutList.stream().anyMatch(predicate -> {
                            String bean1 = (String) predicate;
                            return bean1 != null && item.getCPGUID().equalsIgnoreCase(bean1);
                        });
                    } else {
                        isLocalOrder = Stream.of(datavalutList).anyMatch(predicate -> {
                            String bean1 = (String) predicate;
                            return bean1 != null && item.getCPGUID().equalsIgnoreCase(bean1);
                        });
                    }
                    if (MSFAApplication.isAWSM) {
                        if (isLocalOrder) {
                            item.setSaleOrderAval("X");
                            item.setSaleOrderAvalTemp("X");
                            item.setColor(ContextCompat.getColor(context,R.color.order_yelloe));
                        } else {
                            item.setSaleOrderAval("");
                            item.setSaleOrderAvalTemp("");
                        }
                    }
                    if (MSFAApplication.isAWSM) {
//                        fetchInvoiceColor(item,context);
                        isCurrentMonthGO(item,context);
                    }
                    if(isVisitAvailable){
                        item.setColor(ContextCompat.getColor(context,R.color.theme_orange));
                    }
                } catch (Exception e) {
                    LogManager.writeLogDebug("Outlets failed to load "+e.toString());
                }
            }

            if (retailerDataListener!=null)retailerDataListener.requestFinished();
            isOutletsLoaded=true;
            System.out.println("SYNCED WITH RETAILER: true");
            SyncSelectionActivity.isLocalSync=true;
            LogManager.writeLogDebug("Retailer loaded successfully");
            MSFAApplication.AW_REVERIFICATION = getCPConfigData(MSFAApplication.AW_CPID,context);
            if(executorService!=null){
                executorService.shutdown();
            }
        } catch (Throwable e) {
            isOutletsLoaded=true;
            if (retailerDataListener!=null)retailerDataListener.requestFinished();
            SyncSelectionActivity.isLocalSync=true;
            LogManager.writeLogDebug("Outlets failed to load "+e.toString());
        }
    }

    List<ConfigTypsetTypeValuesBean> configList = new ArrayList();

    //To check if validate option and Check out to be enabled or disabled
    // CHKOUTSKIP = 'X' ---> To disable CheckOut and validate
    public void checkIfCheckOutAllowed() {
        String qry = "ConfigTypsetTypeValues?$filter=" + Constants.Typeset + " eq 'ZBDCRS'";
        Thread thread = null;
        try {
            thread = new Thread(() -> {
                configList = new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(new ConfigTypsetTypeValuesBean())
                        .setODataEntitySet(Constants.ConfigTypsetTypeValues)
                        .setODataEntityType(Constants.ConfigTypsetEntity)
                        .setQuery(qry)
                        .returnODataList(OfflineManager.offlineStore);
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
        }

        Boolean isEnabled = false;
        if (configList.size() > 0) {
            for (int i = 0; i < configList.size(); i++) {
                String type = configList.get(i).getTypes();
                String typeValue = configList.get(i).getTypeValue();
                if (type.equalsIgnoreCase(Constants.CHKOUTSKIP)) {
                    if (typeValue.equalsIgnoreCase("X")) {
                        isEnabled = true;
                        break;
                    }
                }
            }
        }
        SharedPreferences sharedPerf = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPerf.edit();
        editor.putBoolean(Constants.CHKOUTSKIP, isEnabled);
        editor.apply();

    }

    public static void getRetailersRemoteData(Context context) {
        try {
            OutletSQLHelper sqlHelper = new OutletSQLHelper(context);
            */
/**
             * Looping each and every beat to get all retailers present in all beats. later will filter in retailer selection.
             *//*

            if (retailersList!=null&&!retailersList.isEmpty()){
                retailersList.clear();
            }
            String routeCPQry = Constants.ChannelPartners + "?$select=IsKeyCP,Source,CreatedOn,URL1,VATNo,Group1,ConstructionType,Tax1,Latitude,Longitude,Mobile3,Group3,Group3Desc,Group4Desc,Landline,Name,PostalCode,MobileVerifed,OwnerName,CPUID,CPGUID,Address1,Address2,Address3,Mobile1,CPTypeID,CPNo,CPTypeDesc,ActivationDate,DOB,Group8,Group2,Group10,Group10Desc,Group8Desc,Group2Desc,StateID,StateDesc,DistrictID,DistrictDesc,CityID,CityDesc,TownID,TownDesc,WardID,WardName,WardGUID,SubDistrictGUID,Landmark &$filter=ApprvlStatusID eq '03' and StatusID eq '01' and (CPTypeID eq '20' or CPTypeID eq '10')";
            final List retailerObject = new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean())
                    .setODataEntitySet(Constants.ChannelPartners)
                    .setODataEntityType(Constants.ChannelPartnerEntity)
                    .setQuery(routeCPQry)
                    .returnODataList(OfflineManager.offlineStore);

            final List retailerTemp = new ArrayList();
            LogManager.writeLogDebug("ChannelPartners loaded");
            try {
                if (retailerObject!=null&&!retailerObject.isEmpty()) {
                    List<com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean>list = sqlHelper.getAllOutlets();
                    LogManager.writeLogDebug("SQL : fetching data with ChannelPartners size : "+list.size());
                    if (list!=null&&!list.isEmpty()) {
                        retailerObject.addAll(list);
                    }
                    retailerCount = retailerObject.size();
                }else{
                    retailerCount=0;
                    isOutletsLoaded=true;
                }
            } catch (Throwable e) {
                LogManager.writeLogDebug("SQL : error fetching data with ChannelPartners"+e.getMessage());
            }
            String routeQry = Constants.RouteSchedulePlans + "?$select=VisitCPName,RouteDesc,SequenceNo,VisitCPGUID,RouteSchGUID &$filter=(VisitCPTypeID eq '20' or VisitCPTypeID eq '10')";

//            routeQry = routeQry.concat("");
            List routeSchedulePlansList = new ArrayList();
            */
/**
             * Get routes w.r.t to RouteSchedules where will get guid Value to be passed to CP table to fetch available retaileres,
             *//*

            routeSchedulePlansList = new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RouteSchPlansBean())
                    .setODataEntitySet(Constants.RouteSchedulePlans)
                    .setODataEntityType(Constants.RouteSchedulePlanEntity)
                    .setQuery(routeQry)
                    .returnODataList(OfflineManager.offlineStore);
            try {
                if (routeSchedulePlansList!=null&&!routeSchedulePlansList.isEmpty()) {
                    List<com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RouteSchPlansBean>list = sqlHelper.getRouteSchedulePlansSqlList();
                    LogManager.writeLogDebug("SQL : fetching data with RouteSchedulePlans size : "+list.size());
                    if (list!=null&&!list.isEmpty()) {
                        routeSchedulePlansList.addAll(list);
                    }
                }
            } catch (Throwable e) {
                LogManager.writeLogDebug("SQL : error fetching data with RouteSchedulePlans"+e.getMessage());
            }
            LogManager.writeLogDebug("RouteSchedulePlans loaded");
            int orderBy=1;
            for (int j = 0; j < beatList.size(); j++) {
//                LogManager.writeLogDebug("beat loop started");
                final boolean[] isSequenceMaintained = {false};
                if (retailerDataListener!=null)retailerDataListener.requestProgress();
                com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean bean = (com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean) beatList.get(j);
                if (bean.getOrderBy()!=null&&!TextUtils.isEmpty(bean.getOrderBy())) {
                    orderBy = Integer.parseInt(bean.getOrderBy());
                }
                if (!TextUtils.isEmpty(bean.getRouteSchGUID())) {
                    List plansList= new ArrayList(routeSchedulePlansList.size());
                    if (Build.VERSION.SDK_INT >= 24) {
                        plansList = (List) routeSchedulePlansList.stream().filter((Predicate<Object>) predicate -> ((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RouteSchPlansBean) predicate).getRouteSchGUID().equalsIgnoreCase(bean.getRouteSchGUID())).collect(java.util.stream.Collectors.toList());
                    } else {
                        plansList = (List) Stream.of(routeSchedulePlansList).filter((com.annimon.stream.function.Predicate<Object>) predicate -> ((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RouteSchPlansBean) predicate).getRouteSchGUID().equalsIgnoreCase(bean.getRouteSchGUID())).collect(com.annimon.stream.Collectors.toList());
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        isSequenceMaintained[0] = plansList.stream().anyMatch(predicate -> {
                            com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RouteSchPlansBean bean1 = (com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RouteSchPlansBean) predicate;
                            return bean1.getSequenceNo() == null ||bean1.getSequenceNo().equalsIgnoreCase("");
                        });
                    } else {
                        isSequenceMaintained[0] = Stream.of(plansList).anyMatch(predicate -> {
                            com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RouteSchPlansBean bean1 = (com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RouteSchPlansBean) predicate;
                            return bean1.getSequenceNo() == null ||bean1.getSequenceNo().equalsIgnoreCase("");
                        });
                    }
                    if (isSequenceMaintained[0]) {
                        bean.setSequenceMaintained(false);
                    }else{
                        bean.setSequenceMaintained(true);
                    }

                    for (Object object : plansList) {
                        if (object instanceof com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RouteSchPlansBean) {
                            com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RouteSchPlansBean bean1 = (com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RouteSchPlansBean) object;
                            try {
                                if (Build.VERSION.SDK_INT >= 24) {
                                    retailerObject.stream().filter(predicate -> ((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) predicate)
                                                    .getCPGUID().replace("-", "").equalsIgnoreCase(bean1.getVisitCPGUID()))
                                            .forEach(predicate -> {
                                                ((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) predicate).setBeatGUID(bean1.getRouteSchGUID());
                                                ((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) predicate).setBeatCatID(bean.getBeatCatID());
                                                if (bean1.getSequenceNo() != null && !bean1.getSequenceNo().isEmpty()) {
                                                    int value = Integer.parseInt(bean1.getSequenceNo().trim());
                                                    ((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) predicate).setSequenceNo(value);
                                                    ((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) predicate).setIndexSequenceNo(value);
                                                }
//                                                ((com.arteriatech.ss.msec.bil.v3.pda.retailerselection.RetailerSelectionBean) predicate).setProductType(getSplitBeatCPData(((com.arteriatech.ss.msec.bil.v3.pda.retailerselection.RetailerSelectionBean) predicate).getBeatGUID(),((com.arteriatech.ss.msec.bil.v3.pda.retailerselection.RetailerSelectionBean) predicate).getBeatCatID(),((com.arteriatech.ss.msec.bil.v3.pda.retailerselection.RetailerSelectionBean) predicate).getCPGUID()));
                                                retailerTemp.add(setOuletBeansValue((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) predicate));
                                            });
                                } else {
                                    Stream.of(retailerObject)
                                            .filter(predicate -> ((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) predicate).getCPGUID().replace("-", "").equalsIgnoreCase(bean1.getVisitCPGUID()))
                                            .forEach(predicate -> {
                                                ((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) predicate).setBeatGUID(bean1.getRouteSchGUID());
                                                ((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) predicate).setBeatCatID(bean.getBeatCatID());
                                                if (bean1.getSequenceNo() != null && !bean1.getSequenceNo().isEmpty()) {
                                                    int value = Integer.parseInt(bean1.getSequenceNo().trim());
                                                    ((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) predicate).setSequenceNo(value);
                                                    ((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) predicate).setIndexSequenceNo(value);
                                                }
//                                                ((com.arteriatech.ss.msec.bil.v3.pda.retailerselection.RetailerSelectionBean) predicate).setProductType(getSplitBeatCPData(((com.arteriatech.ss.msec.bil.v3.pda.retailerselection.RetailerSelectionBean) predicate).getBeatGUID(),((com.arteriatech.ss.msec.bil.v3.pda.retailerselection.RetailerSelectionBean) predicate).getBeatCatID(),((com.arteriatech.ss.msec.bil.v3.pda.retailerselection.RetailerSelectionBean) predicate).getCPGUID()));
                                                retailerTemp.add(setOuletBeansValue((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) predicate));
                                            });
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
            LogManager.writeLogDebug("beat loop ended");
            */
/**
             * Here appending some circumstances to display colors w.r.t Sales Order entry for retailer
             *//*

            try {
                retailersList.clear();
            } catch (Throwable e) {
                e.printStackTrace();
            }
//            retailersList.addAll(retailerObject);
            retailersList.addAll(retailerTemp);
            retailerCount = retailerTemp.size();
            CommonDB commonDB = new CommonDB(context);
            CPPerformanceBean cpPerformanceBean = null;
            if (commonDB.getRecordExistNew(MSFAApplication.SPGUID, DashBoard.distributorBean.getCPNo())) {
                cpPerformanceList = commonDB.getCPDataNew(MSFAApplication.SPGUID,DashBoard.distributorBean.getCPNo());
            }else {
                try {
                    String qry = Constants.CPPerformances + "?$filter=SPGUID eq guid'" + MSFAApplication.SPGUID + "' and ParentNo eq '" + DashBoard.distributorBean.getCPNo() + "'";
                    cpPerformanceList = new OfflineUtils.ODataOfflineBuilder<>()
                            .setHeaderPayloadObject(new CPPerformanceBean())
                            .setODataEntitySet(Constants.CPPerformances)
                            .setODataEntityType(Constants.CPPerformanceEntity)
                            .setQuery(qry)
                            .returnODataList(OfflineManager.offlineStore);
                    if (cpPerformanceList.size()>0){
                        LogManager.writeLogDebug("Before CPPerformance INsertion");
                        for (int i=0;i<cpPerformanceList.size();i++){
                            CPPerformanceBean cpPerformanceBean1=new CPPerformanceBean();
                            cpPerformanceBean1=(CPPerformanceBean) cpPerformanceList.get(i);
                            commonDB.createRecord(cpPerformanceBean1);
                        }
                        LogManager.writeLogDebug("After CPPerformance INsertion");
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            List<String> datavalutList = OfflineManager.getSOListFromDataVault(context);
            for (int i = 0; i < retailersList.size(); i++) {
                Log.e("Retailer Time","invoice"+i);

//                LogManager.writeLogDebug("Retailer Time :"+i);
                if (retailerDataListener!=null)retailerDataListener.requestProgress();
                RetailerSelectionBean item = (RetailerSelectionBean) retailersList.get(i);
//                String saleOrderQry = Constants.SSSOs + "?$select=SSSOGuid &$filter=SoldToCPGUID eq guid'" + item.getCPGUID() + "' and " + Constants.OrderDate + " eq datetime'" + UtilConstants.getNewDate() + "' and " + Constants.SPGUID + " eq guid'" + MSFAApplication.SPGUID + "'";
                try {
                    boolean isInvoiceAvailable =false;
                    boolean isVisitAvailable =false;
                    boolean isSaleOrderAval =false;
                    boolean isLocalOrder =false;
                    if (Build.VERSION.SDK_INT >= 24) {
                        isInvoiceAvailable = SSInvoiceColorList.stream().anyMatch(predicate -> {
                            ColorBean bean1 = (ColorBean) predicate;
                            return bean1.getSoldToCPGUID() != null && item.getCPGUID().equalsIgnoreCase(bean1.getSoldToCPGUID());
                        });
                    } else {
                        isInvoiceAvailable = Stream.of(SSInvoiceColorList).anyMatch(predicate -> {
                            ColorBean bean1 = (ColorBean) predicate;
                            return bean1.getSoldToCPGUID() != null && item.getCPGUID().equalsIgnoreCase(bean1.getSoldToCPGUID());
                        });
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        isVisitAvailable = visitedColorList.stream().anyMatch(predicate -> {
                            VisitBean bean1 = (VisitBean) predicate;
                            return bean1.getCPGUID() != null && item.getCPGUID().replaceAll("-","").toLowerCase().equalsIgnoreCase(bean1.getCPGUID().toLowerCase());
                        });
                    } else {
                        isVisitAvailable = Stream.of(visitedColorList).anyMatch(predicate -> {
                            VisitBean bean1 = (VisitBean) predicate;
                            return bean1.getCPGUID() != null && item.getCPGUID().replaceAll("-","").toLowerCase().equalsIgnoreCase(bean1.getCPGUID().toLowerCase());
                        });
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        isSaleOrderAval = SSSOColorList.stream().anyMatch(predicate -> {
                            ColorBean bean1 = (ColorBean) predicate;
                            return bean1.getSoldToCPGUID() != null && item.getCPGUID().equalsIgnoreCase(bean1.getSoldToCPGUID());
                        });
                    } else {
                        isSaleOrderAval = Stream.of(SSSOColorList).anyMatch(predicate -> {
                            ColorBean bean1 = (ColorBean) predicate;
                            return bean1.getSoldToCPGUID() != null && item.getCPGUID().equalsIgnoreCase(bean1.getSoldToCPGUID());
                        });
                    }
                    boolean isInvoiceExist = OfflineManager.isInvoiceExist(context,item.getCPGUID());
                    if (isInvoiceExist)isInvoiceAvailable =true;

                    if (isInvoiceAvailable){
                        item.setInvoiceAval("X");
                        if (MSFAApplication.isAWSM) {
                            item.setColor(ContextCompat.getColor(context,R.color.bil_theme_dark_clr));
                        }else{
                            item.setColor(ContextCompat.getColor(context,R.color.bil_theme_dark_clr));
                        }
                    }else{
                        if (MSFAApplication.isVAN) {
                            item.setInvoiceAval("");
                            item.setColor(ContextCompat.getColor(context, R.color.eod_color));
                        }else if (MSFAApplication.isBVAN) {
                            item.setInvoiceAval("");
                            item.setColor(ContextCompat.getColor(context, R.color.eod_color));
                        }else {
                            item.setInvoiceAval("");
                            item.setColor(ContextCompat.getColor(context,R.color.RED));
                        }
                    }
                    if (MSFAApplication.isAWSM) {
                        if (isSaleOrderAval) {
                            item.setSaleOrderAval("X");
                            item.setSaleOrderAvalTemp("X");
                            item.setColor(ContextCompat.getColor(context,R.color.order_yelloe));
                        } else {
                            item.setSaleOrderAval("");
                            item.setSaleOrderAvalTemp("");
                        }
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        isLocalOrder = datavalutList.stream().anyMatch(predicate -> {
                            String bean1 = (String) predicate;
                            return bean1 != null && item.getCPGUID().equalsIgnoreCase(bean1);
                        });
                    } else {
                        isLocalOrder = Stream.of(datavalutList).anyMatch(predicate -> {
                            String bean1 = (String) predicate;
                            return bean1 != null && item.getCPGUID().equalsIgnoreCase(bean1);
                        });
                    }
                    if (MSFAApplication.isAWSM) {
                        if (isLocalOrder) {
                            item.setSaleOrderAval("X");
                            item.setSaleOrderAvalTemp("X");
                            item.setColor(ContextCompat.getColor(context,R.color.order_yelloe));
                        } else {
                            if(!isSaleOrderAval) {
                                item.setSaleOrderAval("");
                                item.setSaleOrderAvalTemp("");
                            }
                        }
                    }
                    if (MSFAApplication.isAWSM) {
//                        fetchInvoiceColor(item,context);
                        isCurrentMonthGO(item,context);
                    }
                    if(isVisitAvailable){
                        item.setColor(ContextCompat.getColor(context,R.color.theme_orange));
                    }
                } catch (Exception e) {
                    LogManager.writeLogDebug("Outlets failed to load "+e.toString());
                }
            }

            if (retailerDataListener!=null)retailerDataListener.requestFinished();
            isOutletsLoaded=true;
            System.out.println("SYNCED WITH RETAILER: true");
            SyncSelectionActivity.isLocalSync=true;
            LogManager.writeLogDebug("Retailer loaded successfully");
            MSFAApplication.AW_REVERIFICATION = getCPConfigData(MSFAApplication.AW_CPID,context);
        } catch (Throwable e) {
            isOutletsLoaded=true;
            if (retailerDataListener!=null)retailerDataListener.requestFinished();
            SyncSelectionActivity.isLocalSync=true;
            LogManager.writeLogDebug("Outlets failed to load "+e.toString());
        }
    }

    private static void fetchInvoiceColor(RetailerSelectionBean bean,Context context){
        try {
            if (targetsItemsList!=null&&!targetsItemsList.isEmpty()){
                double targetLines=0;
                double actualLines=0;
                double actualValue=0;
                double targetValue=0;
                try {
                    List<TargetItemsBean>targetItemsBeanList=null;
                    if (Build.VERSION.SDK_INT >= 24) {
                        if (targetsItemsList != null && !targetsItemsList.isEmpty()) {
                            targetItemsBeanList = (List) targetsItemsList.stream().filter((Predicate<Object>) predicate -> ((TargetItemsBean) predicate).getPartnerGUID()!=null&&((TargetItemsBean) predicate).getPartnerGUID().equalsIgnoreCase(bean.getCPGUID().replace("-",""))).collect(java.util.stream.Collectors.toList());
                        }
                    } else {
                        if (targetsItemsList != null && !targetsItemsList.isEmpty()) {
                            targetItemsBeanList = (List) Stream.of(targetsItemsList).filter((com.annimon.stream.function.Predicate<Object>) predicate -> ((TargetItemsBean) predicate).getPartnerGUID()!=null&&((TargetItemsBean) predicate).getPartnerGUID().equalsIgnoreCase(bean.getCPGUID().replace("-",""))).collect(com.annimon.stream.Collectors.toList());
                        }
                    }
                    for (Object object:targetItemsBeanList) {
                        if (object instanceof TargetItemsBean){
                            TargetItemsBean targetItemsBean = (TargetItemsBean) object;
                            if (targetItemsBean.getActualQty()!=null&&!TextUtils.isEmpty(targetItemsBean.getActualQty())){
                                try {
                                    actualLines = actualLines+Double.parseDouble(targetItemsBean.getActualQty());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    if (actualLines>0){
                        bean.setInvoiceAval("X");
                        //bean.setColor(ContextCompat.getColor(context,R.color.WHITE));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void fetchInvoiceItems(String CPGUID){
        try {
            */
/*if (CPGUID!=null&&!TextUtils.isEmpty(CPGUID)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (SSInovoiceDetailsList!=null) {
                            SSInovoiceDetailsList.clear();
                        }
                        String headerQry ="SSInvoices?$select=InvoiceGUID &$filter=InvoiceDate ge datetime'"+UtilConstants.getFirstDateOfCurrentMonth()+"' and BillToGuid eq guid'"+CPGUID+"'";
                        String query = Constants.SSInvoiceItemDetails+"?$select=MaterialNo &$filter=";
                        List list= new OfflineUtils.ODataOfflineBuilder<>()
                                .setHeaderPayloadObject(new SSInvoiceItemBean())
                                .setODataEntitySet("SSInvoices")
                                .setODataEntityType(".SSInvoice")
                                .setQuery(headerQry)
                                .returnODataList(OfflineManager.offlineStore);
                        for (int i = 0; i < list.size(); i++) {
                            SSInvoiceItemBean bean = (SSInvoiceItemBean) list.get(i);
                            if (!TextUtils.isEmpty(bean.getInvoiceGUID())) {
                                if (i != list.size() - 1) {
                                    query = query.concat("InvoiceGUID eq guid'" + bean.getInvoiceGUID() + "' or ");
                                } else {
                                    query = query.concat("InvoiceGUID eq guid'" + bean.getInvoiceGUID() + "'");
                                }
                            }
                        }
                        if (!list.isEmpty()) {
                            SSInovoiceDetailsList= new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new SSInvoiceItemDetailsBean())
                                    .setODataEntitySet(Constants.SSInvoiceItemDetails)
                                    .setODataEntityType(Constants.SSInvoiceItemDetailEntity)
                                    .setQuery(query)
                                    .returnODataList(OfflineManager.offlineStore);
                            Set set = new HashSet(SSInovoiceDetailsList);
                            SSInovoiceDetailsList.clear();
                            SSInovoiceDetailsList.addAll(set);
                        }
                    }
                }).start();
            }*//*

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void getStockMaterialsMRPWise(){
        stockListAWSM = new ArrayList<>();
        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                String mStrMyStockQry = "";
                String additionalQuery = "";
                if(MSFAApplication.isSDA){
                    if (distributorBean != null) {
                        if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                        }
                        mStrMyStockQry = Constants.CPStockItems + "?$select=SKUGroup,Brand,AlternativeUOM2Den,CPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM " +
                                "&$orderby=" + Constants.MaterialDesc + " &$filter=" + Constants.StockOwner + " eq '" + distributorBean.getCPTypeID() + "' " + additionalQuery;
                    }
                }else {
                    if (distributorBean != null) {
                        if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                        }
                        mStrMyStockQry = Constants.CPStockItems + "?$select=BrandDesc,SKUGroupDesc,SKUGroup,Brand,AlternativeUOM2Den,CPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM " +
                                "&$orderby=" + Constants.MaterialDesc + " &$filter=" + Constants.StockOwner + " eq '" + distributorBean.getCPTypeID() + "' " + additionalQuery;
                    }
                }
                String qty = "";
                String calculatedValue = "";
                String removedZerosValue = "";
                int number = 0;
                List materialList = new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(new CPStockItemBean())
                        .setODataEntitySet(Constants.CPStockItems)
                        .setODataEntityType(Constants.CPStockItemEntity)
                        .setQuery(mStrMyStockQry)
                        .returnODataList(OfflineManager.offlineStore);

                if (!materialList.isEmpty()) {
                    for (CPStockItemBean bean : (List<CPStockItemBean>) materialList) {
                        ArrayList<CPStockItemSnoBean> itemSNOList = new ArrayList<>();
//                        SPStockItemSNoBean snoBean = null;
                        try {
                            String date ="";
                            if (MSFAApplication.STKEXPUPTO!=null&&!TextUtils.isEmpty(MSFAApplication.STKEXPUPTO)&&!MSFAApplication.STKEXPUPTO.equalsIgnoreCase("0")) {
                                try {
                                    date = ConstantsUtils.getCPConfigDate(Integer.parseInt(MSFAApplication.STKEXPUPTO));
                                } catch (Throwable e) {
                                    date = UtilConstants.getNewDate();
                                }
                            }else{
                                date = UtilConstants.getNewDate();
                            }
                            String CPStockSnosQry = "CPStockItemSnos?$select=CPSnoGUID,CPStockItemGUID,AltUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,IntermUnitPrice,Batch,ManufacturingDate,ExpiryDate,MaterialNo,MaterialDesc &$filter=CPStockItemGUID eq guid'" + bean.getCPStockItemGUID().toUpperCase() + "'";

//                            snoBean = OfflineManager.getVANMRP(CPStockSnosQry);
                            itemSNOList.addAll(OfflineManager.getAWSMMRPStock(CPStockSnosQry));
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        for(CPStockItemSnoBean snoBean : itemSNOList) {
                            CPStockItemBean beanTemp = new CPStockItemBean();
                            beanTemp.setMatGrpDesc(bean.getMatGrpDesc());
                            beanTemp.setSKUGroup(bean.getSKUGroup());
                            beanTemp.setSKUGroupDesc(bean.getSKUGroupDesc());
                            beanTemp.setAlternativeUOM2(bean.getAlternativeUOM2());
                            beanTemp.setAlternativeUOM2Num(bean.getAlternativeUOM2Num());
                            beanTemp.setAlternativeUOM2Den(bean.getAlternativeUOM2Den());
                            beanTemp.setAlternativeUOM1(bean.getAlternativeUOM1());
                            beanTemp.setAlternativeUOM1Num(bean.getAlternativeUOM1Num());
                            beanTemp.setAlternativeUOM1Den(bean.getAlternativeUOM1Den());
                            beanTemp.setStockValue(bean.getStockValue());
                            beanTemp.setStockTypeID(bean.getStockTypeID());
                            beanTemp.setStockTypeDesc(bean.getStockTypeDesc());
                            beanTemp.setCPStockItemGUID(bean.getCPStockItemGUID());
                            beanTemp.setMaterialNo(bean.getMaterialNo());
                            beanTemp.setMaterialDesc(bean.getMaterialDesc());
//                                        beanTemp.setMRP(bean.getMRP());
                            beanTemp.setUnrestrictedQty(bean.getUnrestrictedQty());
                            beanTemp.setUOM(bean.getUOM());
                            beanTemp.setCurrency(bean.getCurrency());
                            beanTemp.setLandingPrice(bean.getLandingPrice());
                            beanTemp.setBrand(bean.getBrand());
                            beanTemp.setBrandDesc(bean.getBrandDesc());
                            beanTemp.setOrderMaterialGroupDesc(bean.getOrderMaterialGroupDesc());
                            beanTemp.setOrderMaterialGroupID(bean.getOrderMaterialGroupID());
                            beanTemp.setStockOwner(bean.getStockOwner());
                            beanTemp.setStockOwnerDesc(bean.getStockOwnerDesc());
                            if (snoBean != null) {

                                beanTemp.setRate(snoBean.getUnitPrice());
                                beanTemp.setMRP(snoBean.getMRP());
                                beanTemp.setGross(snoBean.getIntermUnitPrice());
                                beanTemp.setTax1(snoBean.getTax1());
                                beanTemp.setTax2(snoBean.getTax2());
                                beanTemp.setRateTax(snoBean.getRateTax());
                                beanTemp.setMaterialDesc(snoBean.getMaterialDesc());
                                beanTemp.setMaterialNo(snoBean.getMaterialNo());
                                beanTemp.setAlternativeUOM1(snoBean.getAltUom());
                                beanTemp.setAlternativeUOM1Num(snoBean.getAltUomDen());
                                beanTemp.setAlternativeUOM1Den(snoBean.getAltUomNum());
                                beanTemp.setUnrestrictedQty(snoBean.getQuantity());
                                if (snoBean.getBatch()!=null&&!snoBean.getBatch().isEmpty()) {
                                    beanTemp.setBatch(snoBean.getBatch());
                                }if (snoBean.getCPSnoGUID()!=null&&!snoBean.getCPSnoGUID().isEmpty()) {
                                    beanTemp.setCPSnoGUID(snoBean.getCPSnoGUID());
                                }
                                if (!TextUtils.isEmpty(beanTemp.getUnrestrictedQty())) {
                                    double value = 0.0;
                                    double pieces = 0.0;
                                    double finalValue = 0.0;
                                    double count = 0.0;
                                    double finalPieces = 0.0;
                                    double unrestrictedQty = 0.0;
                                    double altUom2Num = 0.0;
                                    int TotalValue = 0;
                                    beanTemp.setUnrestrictedQtyTemp(beanTemp.getUnrestrictedQty());
                                    unrestrictedQty = Double.parseDouble(beanTemp.getUnrestrictedQty());
                                    if (beanTemp.getAlternativeUOM2Den() != null) {
                                        altUom2Num = Double.parseDouble(beanTemp.getAlternativeUOM1Num());
                                        if (altUom2Num > 0) {
                                            qty = String.valueOf(unrestrictedQty / altUom2Num);
                                            try {
                                                if (qty.contains(".")) {
                                                    double d = Double.parseDouble(qty);
                                                    number = (int) d;
                                                    beanTemp.setUnrestrictedQty(String.valueOf(number));
                                                    if (!TextUtils.isEmpty(qty)) {
                                                        value = Double.parseDouble(qty);
                                                        pieces = value - number;
                                                        if (pieces != 0) {
                                                            finalValue = pieces * Double.parseDouble(beanTemp.getAlternativeUOM1Num());
                                                            calculatedValue = String.valueOf(finalValue);
                                                            removedZerosValue = UtilConstants.removeLeadingZerowithTwoDecimal(calculatedValue);
                                                            count = Double.parseDouble(removedZerosValue);
                                                            finalPieces = Double.parseDouble(String.valueOf(count));
                                                            TotalValue = (int) finalPieces;
                                                            beanTemp.setPieces(UtilConstants.removeLeadingZero(finalValue));
//                                                    bean.setUnrestrictedQty(qty);
                                                        }
                                                    }
                                                } else {
                                                    beanTemp.setUnrestrictedQty(qty);
                                                }
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                }
                                stockListAWSM.add(beanTemp);
                            }
                        }

                    }

                }
            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    public static void getStockMaterials(DistributorSelectionBean distributorSelectionBean) {
        isStockLoaded = false;
        distributorBean=distributorSelectionBean;
        stockList = new ArrayList<>();
        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                String mStrMyStockQry = "";
                String additionalQuery = "";
                if(MSFAApplication.isSDA){
                    if (distributorBean != null) {
                        if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                        }
                        mStrMyStockQry = Constants.CPStockItems + "?$select=SKUGroup,Brand,AlternativeUOM2Den,CPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM " +
                                "&$orderby=" + Constants.MaterialDesc + " &$filter=" + Constants.StockOwner + " eq '" + distributorBean.getCPTypeID() + "' " + additionalQuery;
                    }
                }else {
                    if (distributorBean != null) {
                        if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                        }
                        mStrMyStockQry = Constants.CPStockItems + "?$select=BrandDesc,SKUGroupDesc,SKUGroup,Brand,AlternativeUOM2Den,CPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM " +
                                "&$orderby=" + Constants.MaterialDesc + " &$filter=" + Constants.StockOwner + " eq '" + distributorBean.getCPTypeID() + "' " + additionalQuery;
                    }
                }
                String qty = "";
                String calculatedValue = "";
                String removedZerosValue = "";
                int number = 0;
                List materialList = new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(new CPStockItemBean())
                        .setODataEntitySet(Constants.CPStockItems)
                        .setODataEntityType(Constants.CPStockItemEntity)
                        .setQuery(mStrMyStockQry)
                        .returnODataList(OfflineManager.offlineStore);

                if (!materialList.isEmpty()) {
                    for (CPStockItemBean bean : (List<CPStockItemBean>) materialList) {
                        CPStockItemSnoBean snoBean = null;
                        try {
                            snoBean = OfflineManager.getMRP("CPStockItemSnos?$select=CPSnoGUID,CPStockItemGUID,AltUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,IntermUnitPrice,Batch,ManufacturingDate,ExpiryDate,MaterialNo,MaterialDesc &$filter=CPStockItemGUID eq guid'" + bean.getCPStockItemGUID().toUpperCase() + "'");
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                        }
                        if (snoBean != null) {
                            bean.setRate(snoBean.getUnitPrice());
                            bean.setMRP(snoBean.getMRP());
                            bean.setGross(snoBean.getIntermUnitPrice());
                            bean.setTax1(snoBean.getTax1());
                            bean.setTax2(snoBean.getTax2());
                            bean.setRateTax(snoBean.getRateTax());
                            bean.setMaterialDesc(snoBean.getMaterialDesc());
                            bean.setMaterialNo(snoBean.getMaterialNo());
                            bean.setAlternativeUOM1(snoBean.getAltUom());
                            bean.setAlternativeUOM1Num(snoBean.getAltUomDen());
                            bean.setAlternativeUOM1Den(snoBean.getAltUomNum());
                            if (snoBean.getBatch()!=null&&!snoBean.getBatch().isEmpty()) {
                                bean.setBatch(snoBean.getBatch());
                            }if (snoBean.getCPSnoGUID()!=null&&!snoBean.getCPSnoGUID().isEmpty()) {
                                bean.setCPSnoGUID(snoBean.getCPSnoGUID());
                            }
                        }
                        if (!TextUtils.isEmpty(bean.getUnrestrictedQty())) {
                            double value = 0.0;
                            double pieces = 0.0;
                            double finalValue = 0.0;
                            double count = 0.0;
                            double finalPieces = 0.0;
                            double unrestrictedQty = 0.0;
                            double altUom2Num = 0.0;
                            int TotalValue = 0;
                            bean.setUnrestrictedQtyTemp(bean.getUnrestrictedQty());
                            unrestrictedQty = Double.parseDouble(bean.getUnrestrictedQty());
                            if (bean.getAlternativeUOM2Den()!=null) {
                                altUom2Num = Double.parseDouble(bean.getAlternativeUOM1Num());
                                if (altUom2Num>0) {
                                    qty = String.valueOf(unrestrictedQty/altUom2Num);
                                    try {
                                        if (qty.contains(".")) {
                                            double d = Double.parseDouble(qty);
                                            number = (int) d;
                                            bean.setUnrestrictedQty(String.valueOf(number));
                                            if (!TextUtils.isEmpty(qty)) {
                                                value = Double.parseDouble(qty);
                                                pieces = value - number;
                                                if (pieces != 0) {
                                                    finalValue = pieces * Double.parseDouble(bean.getAlternativeUOM1Num());
                                       */
/* calculatedValue = String.valueOf(finalValue);
                                        removedZerosValue = UtilConstants.removeLeadingZerowithTwoDecimal(calculatedValue);
                                        count = Double.parseDouble(removedZerosValue);
                                        finalPieces = Double.parseDouble(String.valueOf(count));
                                        TotalValue = (int) finalPieces;*//*

                                                    bean.setPieces(UtilConstants.removeLeadingZero(finalValue));
                                                }
                                            }
                                        } else {
                                            bean.setUnrestrictedQty(qty);
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                        stockList.add(bean);
                    }
                }
                isStockLoaded = true;
            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

    }
    public static void getVANStockMaterials() {
        stockList = new ArrayList<>();
        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                String mStrMyStockQry = "";
                String additionalQuery = "";
                if(MSFAApplication.isSDA){
                    if (distributorBean != null) {
                        if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                        }
                        mStrMyStockQry = Constants.SPStockItems + "?$select=SkuGroup,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " +
                                "&$orderby=" + Constants.MaterialDesc + " &$filter=" + Constants.StockOwner + " eq '" + distributorBean.getCPTypeID() + "' " + additionalQuery;
                    }
                }else if(MSFAApplication.isVAN) {
                    if (distributorBean != null) {
                        if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                        }
                        mStrMyStockQry = Constants.SPStockItems + "?$select=SkuGroup,HSNCode,AlternativeUOM1,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " +
                                "&$orderby=" + Constants.MaterialDesc;
                    }
                }else if (MSFAApplication.isBVAN) {
                    if (BreadDashBoard.distributorBean != null) {
                        if (!TextUtils.isEmpty(BreadDashBoard.distributorBean.getCPGUID()) && !BreadDashBoard.distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            // additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                            additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + BreadDashBoard.distributorBean.getCPGUID() + "'";
                            //   additionalQuery = "StorageLoc eq '"+cpguid+"'";
                        }
                        mStrMyStockQry = Constants.SPStockItems + "?$select=CatalogInfo,SkuGroup,HSNCode,AlternativeUOM1,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " + "&$orderby=" + Constants.MaterialDesc;
                    }
                } else {
                    if (distributorBean != null) {
                        if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                        }
                        mStrMyStockQry = Constants.SPStockItems + "?$select=SkuGroup,HSNCode,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " +
                                "&$orderby=" + Constants.MaterialDesc + " &$filter=" + Constants.StockOwner + " eq '" + distributorBean.getCPTypeID() + "' " + additionalQuery;
                    }
                }
                String qty = "";
                String calculatedValue = "";
                String removedZerosValue = "";
                int number = 0;
                List materialList = new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(new SPStockItemBean())
                        .setODataEntitySet(Constants.SPStockItems)
                        .setODataEntityType(Constants.SPStockItemsEntity)
                        .setQuery(mStrMyStockQry)
                        .returnODataList(OfflineManager.offlineStore);

                if (!materialList.isEmpty()) {
                    for (SPStockItemBean bean : (List<SPStockItemBean>) materialList) {
                        ArrayList<SPStockItemSNoBean> itemSNOList = new ArrayList<>();
//                        SPStockItemSNoBean snoBean = null;
                        try {
                            String date ="";
                            if (MSFAApplication.STKEXPUPTO!=null&&!TextUtils.isEmpty(MSFAApplication.STKEXPUPTO)&&!MSFAApplication.STKEXPUPTO.equalsIgnoreCase("0")) {
                                try {
                                    date = ConstantsUtils.getCPConfigDate(Integer.parseInt(MSFAApplication.STKEXPUPTO));
                                } catch (Throwable e) {
                                    date = UtilConstants.getNewDate();
                                }
                            }else{
                                date = UtilConstants.getNewDate();
                            }
                            String CPStockSnosQry = Constants.SPStockItemSNos+ "?$select=Quantity,IntrmUnitPrice,AlternateUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,Tax3,Tax4,Tax5,Tax6,Tax7,Tax8,Tax9,Tax10,Batch,MFD,ExpiryDate,MaterialNo,MaterialDesc " +
                                    "&$filter=(StockTypeID eq '1' and SPStockItemGUID eq guid'"+bean.getSPStockItemGUID().toUpperCase()+"') and (ExpiryDate gt datetime'"+date+"' or ExpiryDate eq null) &$orderby=MFD desc ";

//                            snoBean = OfflineManager.getVANMRP(CPStockSnosQry);
                            itemSNOList.addAll(OfflineManager.getVANMRPStockList(CPStockSnosQry));
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        for(SPStockItemSNoBean snoBean : itemSNOList) {
                            SPStockItemBean beanTemp = new SPStockItemBean();
                            beanTemp.setHSNCode(bean.getHSNCode());
                            beanTemp.setProdHier7(bean.getProdHier7());
                            beanTemp.setMatGrpId(bean.getMatGrpId());
                            beanTemp.setMatGrpDesc(bean.getMatGrpDesc());
                            beanTemp.setSkuGroup(bean.getSkuGroup());
                            beanTemp.setAlternativeUOM2(bean.getAlternativeUOM2());
                            beanTemp.setAlternativeUOM2Num(bean.getAlternativeUOM2Num());
                            beanTemp.setAlternativeUOM2Den(bean.getAlternativeUOM2Den());
                            beanTemp.setAlternativeUOM1(bean.getAlternativeUOM1());
                            beanTemp.setAlternativeUOM1Num(bean.getAlternativeUOM1Num());
                            beanTemp.setAlternativeUOM1Den(bean.getAlternativeUOM1Den());
                            beanTemp.setStockValue(bean.getStockValue());
                            beanTemp.setStockTypeID(bean.getStockTypeID());
                            beanTemp.setStockTypeDesc(bean.getStockTypeDesc());
                            beanTemp.setSPStockItemGUID(bean.getSPStockItemGUID());
                            beanTemp.setMaterialNo(bean.getMaterialNo());
                            beanTemp.setMaterialDesc(bean.getMaterialDesc());
//                                        beanTemp.setMRP(bean.getMRP());
                            beanTemp.setUnrestrictedQty(bean.getUnrestrictedQty());
                            beanTemp.setUOM(bean.getUOM());
                            beanTemp.setCurrency(bean.getCurrency());
                            beanTemp.setLandingPrice(bean.getLandingPrice());
                            beanTemp.setBrand(bean.getBrand());
                            beanTemp.setBrandDesc(bean.getBrandDesc());
                            beanTemp.setOrderMaterialGroupDesc(bean.getOrderMaterialGroupDesc());
                            beanTemp.setOrderMaterialGroupID(bean.getOrderMaterialGroupID());
                            beanTemp.setStockOwner(bean.getStockOwner());
                            beanTemp.setStockOwnerDesc(bean.getStockOwnerDesc());
                            if (snoBean != null) {
                                beanTemp.setRate(snoBean.getUnitPrice());
                                beanTemp.setMRP(snoBean.getMRP());
                                beanTemp.setGross(snoBean.getIntermUnitPrice());
                                beanTemp.setTax1(snoBean.getTax1());
                                beanTemp.setTax2(snoBean.getTax2());
                                beanTemp.setRateTax(snoBean.getRateTax());
                                beanTemp.setMaterialDesc(snoBean.getMaterialDesc());
                                beanTemp.setMaterialNo(snoBean.getMaterialNo());
                                beanTemp.setAlternativeUOM1(snoBean.getAlternateUom());
                                beanTemp.setAlternativeUOM1Num(snoBean.getAltUomDen());
                                beanTemp.setAlternativeUOM1Den(snoBean.getAltUomNum());
                                beanTemp.setUnrestrictedQty(snoBean.getQuantity());
                                if (!TextUtils.isEmpty(beanTemp.getUnrestrictedQty())) {
                                    double value = 0.0;
                                    double pieces = 0.0;
                                    double finalValue = 0.0;
                                    double count = 0.0;
                                    double finalPieces = 0.0;
                                    double unrestrictedQty = 0.0;
                                    double altUom2Num = 0.0;
                                    int TotalValue = 0;
                                    beanTemp.setUnrestrictedQtyTemp(beanTemp.getUnrestrictedQty());
                                    unrestrictedQty = Double.parseDouble(beanTemp.getUnrestrictedQty());
                                    if (beanTemp.getAlternativeUOM2Den() != null) {
                                        altUom2Num = Double.parseDouble(beanTemp.getAlternativeUOM1Num());
                                        if (altUom2Num > 0) {
                                            qty = String.valueOf(unrestrictedQty / altUom2Num);
                                            try {
                                                if (qty.contains(".")) {
                                                    double d = Double.parseDouble(qty);
                                                    number = (int) d;
                                                    beanTemp.setUnrestrictedQty(String.valueOf(number));
                                                    if (!TextUtils.isEmpty(qty)) {
                                                        value = Double.parseDouble(qty);
                                                        pieces = value - number;
                                                        if (pieces != 0) {
                                                            finalValue = pieces * Double.parseDouble(beanTemp.getAlternativeUOM1Num());
                                                            calculatedValue = String.valueOf(finalValue);
                                                            removedZerosValue = UtilConstants.removeLeadingZerowithTwoDecimal(calculatedValue);
                                                            count = Double.parseDouble(removedZerosValue);
                                                            finalPieces = Double.parseDouble(String.valueOf(count));
                                                            TotalValue = (int) finalPieces;
                                                            beanTemp.setPieces(UtilConstants.removeLeadingZero(finalValue));
//                                                    bean.setUnrestrictedQty(qty);
                                                        }
                                                    }
                                                } else {
                                                    beanTemp.setUnrestrictedQty(qty);
                                                }
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                }
                                stockList.add(beanTemp);
                            }
                        }

                    }

                }
                AllSKUActivity.isRefresh = false;
            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

    }


    public static void getBreadVANStockMaterials() {
        stockList = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String mStrMyStockQry = "";
                String additionalQuery = "";
                if (MSFAApplication.isSDA) {
                    if (distributorBean != null) {
                        if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                        }
                        mStrMyStockQry = Constants.SPStockItems + "?$select=CatalogInfo,SkuGroup,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " + "&$orderby=" + Constants.MaterialDesc + " &$filter=" + Constants.StockOwner + " eq '" + distributorBean.getCPTypeID() + "' " + additionalQuery;
                    }
                } else if (MSFAApplication.isVAN) {
                    if (distributorBean != null) {
                        if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                        }
                        mStrMyStockQry = Constants.SPStockItems + "?$select=CatalogInfo,SkuGroup,HSNCode,AlternativeUOM1,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " + "&$orderby=" + Constants.MaterialDesc;
                    }
                } else if (MSFAApplication.isBVAN) {
                    if (BreadDashBoard.distributorBean != null) {
                        if (!TextUtils.isEmpty(BreadDashBoard.distributorBean.getCPGUID()) && !BreadDashBoard.distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + BreadDashBoard.distributorBean.getCPGUID() + "'";
                            //additionalQuery =   Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                        }
                        mStrMyStockQry = Constants.SPStockItems + "?$select=CatalogInfo,SkuGroup,HSNCode,AlternativeUOM1,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " + "&$orderby=" + Constants.MaterialDesc;
                    }
                } else {
                    if (distributorBean != null) {
                        if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                        }
                        mStrMyStockQry = Constants.SPStockItems + "?$select=CatalogInfo,SkuGroup,HSNCode,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " + "&$orderby=" + Constants.MaterialDesc + " &$filter=" + Constants.StockOwner + " eq '" + distributorBean.getCPTypeID() + "' " + additionalQuery;
                    }
                }
                String qty = "";
                String calculatedValue = "";
                String removedZerosValue = "";
                int number = 0;
                List materialList = new OfflineUtils.ODataOfflineBuilder<>().setHeaderPayloadObject(new SPStockItemBean()).setODataEntitySet(Constants.SPStockItems).setODataEntityType(Constants.SPStockItemsEntity).setQuery(mStrMyStockQry).returnODataList(OfflineManager.offlineStore);

                if (!materialList.isEmpty()) {
                    for (SPStockItemBean bean : (List<SPStockItemBean>) materialList) {
                        ArrayList<SPStockItemSNoBean> itemSNOList = new ArrayList<>();
//                        SPStockItemSNoBean snoBean = null;
                        try {
                            String date = "";
                            if (MSFAApplication.STKEXPUPTO != null && !TextUtils.isEmpty(MSFAApplication.STKEXPUPTO) && !MSFAApplication.STKEXPUPTO.equalsIgnoreCase("0")) {
                                try {
                                    date = ConstantsUtils.getCPConfigDate(Integer.parseInt(MSFAApplication.STKEXPUPTO));
                                } catch (Throwable e) {
                                    date = UtilConstants.getNewDate();
                                }
                            } else {
                                date = UtilConstants.getNewDate();
                            }
                            String CPStockSnosQry = Constants.SPStockItemSNos + "?$select=Quantity,IntrmUnitPrice,AlternateUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,Tax3,Tax4,Tax5,Tax6,Tax7,Tax8,Tax9,Tax10,Batch,MFD,ExpiryDate,MaterialNo,MaterialDesc " + "&$filter=(StockTypeID eq '1' and SPStockItemGUID eq guid'" + bean.getSPStockItemGUID().toUpperCase() + "') and (ExpiryDate ge datetime'" + date + "' or ExpiryDate eq null) &$orderby=MFD desc ";

//                            snoBean = OfflineManager.getVANMRP(CPStockSnosQry);
                            itemSNOList.addAll(OfflineManager.getVANMRPStockList(CPStockSnosQry));
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        for (SPStockItemSNoBean snoBean : itemSNOList) {
                            SPStockItemBean beanTemp = new SPStockItemBean();
                            beanTemp.setCatalogInfo(bean.getCatalogInfo());
                            beanTemp.setHSNCode(bean.getHSNCode());
                            beanTemp.setProdHier7(bean.getProdHier7());
                            beanTemp.setMatGrpId(bean.getMatGrpId());
                            beanTemp.setMatGrpDesc(bean.getMatGrpDesc());
                            beanTemp.setSkuGroup(bean.getSkuGroup());
                            beanTemp.setAlternativeUOM2(bean.getAlternativeUOM2());
                            beanTemp.setAlternativeUOM2Num(bean.getAlternativeUOM2Num());
                            beanTemp.setAlternativeUOM2Den(bean.getAlternativeUOM2Den());
                            beanTemp.setAlternativeUOM1(bean.getAlternativeUOM1());
                            beanTemp.setAlternativeUOM1Num(bean.getAlternativeUOM1Num());
                            beanTemp.setAlternativeUOM1Den(bean.getAlternativeUOM1Den());
                            beanTemp.setStockValue(bean.getStockValue());
                            beanTemp.setStockTypeID(bean.getStockTypeID());
                            beanTemp.setStockTypeDesc(bean.getStockTypeDesc());
                            beanTemp.setSPStockItemGUID(bean.getSPStockItemGUID());
                            beanTemp.setMaterialNo(bean.getMaterialNo());
                            beanTemp.setMaterialDesc(bean.getMaterialDesc());
                            beanTemp.setMRP(bean.getMRP());
                            beanTemp.setUnrestrictedQty(bean.getUnrestrictedQty());
                            beanTemp.setUOM(bean.getUOM());
                            beanTemp.setCurrency(bean.getCurrency());
                            beanTemp.setLandingPrice(bean.getLandingPrice());
                            beanTemp.setBrand(bean.getBrand());
                            beanTemp.setBrandDesc(bean.getBrandDesc());
                            beanTemp.setOrderMaterialGroupDesc(bean.getOrderMaterialGroupDesc());
                            beanTemp.setOrderMaterialGroupID(bean.getOrderMaterialGroupID());
                            beanTemp.setStockOwner(bean.getStockOwner());
                            beanTemp.setStockOwnerDesc(bean.getStockOwnerDesc());
                            if (snoBean != null) {
                                beanTemp.setRate(snoBean.getUnitPrice());
                                beanTemp.setMRP(snoBean.getMRP());
                                beanTemp.setGross(snoBean.getIntermUnitPrice());
                                beanTemp.setTax1(snoBean.getTax1());
                                beanTemp.setTax2(snoBean.getTax2());
                                beanTemp.setRateTax(snoBean.getRateTax());
                                beanTemp.setMaterialDesc(snoBean.getMaterialDesc());
                                beanTemp.setMaterialNo(snoBean.getMaterialNo());
                                beanTemp.setAlternativeUOM1(snoBean.getAlternateUom());
                                beanTemp.setAlternativeUOM1Num(snoBean.getAltUomDen());
                                beanTemp.setAlternativeUOM1Den(snoBean.getAltUomNum());
                                beanTemp.setUnrestrictedQty(snoBean.getQuantity());
                                if (!TextUtils.isEmpty(beanTemp.getUnrestrictedQty()) && !beanTemp.getUnrestrictedQty().equalsIgnoreCase("0")) {
                                    double value = 0.0;
                                    double pieces = 0.0;
                                    double finalValue = 0.0;
                                    double count = 0.0;
                                    double finalPieces = 0.0;
                                    double unrestrictedQty = 0.0;
                                    double altUom2Num = 0.0;
                                    int TotalValue = 0;
                                    beanTemp.setUnrestrictedQtyTemp(beanTemp.getUnrestrictedQty());
                                    unrestrictedQty = Double.parseDouble(beanTemp.getUnrestrictedQty());
                                    if (beanTemp.getAlternativeUOM2Den() != null) {
                                        altUom2Num = Double.parseDouble(beanTemp.getAlternativeUOM1Num());
                                        if (altUom2Num > 0) {
                                            qty = String.valueOf(unrestrictedQty / altUom2Num);
                                            try {
                                                if (qty.contains(".")) {
                                                    double d = Double.parseDouble(qty);
                                                    number = (int) d;
                                                    beanTemp.setUnrestrictedQty(String.valueOf(number));
                                                    if (!TextUtils.isEmpty(qty)) {
                                                        value = Double.parseDouble(qty);
                                                        pieces = value - number;
                                                        if (pieces != 0) {
                                                            finalValue = pieces * Double.parseDouble(beanTemp.getAlternativeUOM1Num());
                                                            calculatedValue = String.valueOf(finalValue);
                                                            removedZerosValue = UtilConstants.removeLeadingZerowithTwoDecimal(calculatedValue);
                                                            count = Double.parseDouble(removedZerosValue);
                                                            finalPieces = Double.parseDouble(String.valueOf(count));
                                                            TotalValue = (int) finalPieces;
                                                            beanTemp.setPieces(UtilConstants.removeLeadingZero(finalValue));
//                                                    bean.setUnrestrictedQty(qty);
                                                        }else{
                                                            beanTemp.setPieces(UtilConstants.removeLeadingZero(finalValue));
                                                        }
                                                    }
                                                } else {
                                                    beanTemp.setUnrestrictedQty(qty);
                                                }
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                }
                                stockList.add(beanTemp);
                            }
                        }

                    }
                }
                try {
                    if (stockList != null && !stockList.isEmpty()) {
                        Set<Object> set = new HashSet<>(stockList);
                        stockList.clear();
                        stockList.addAll(set);
                        Collections.sort(stockList, new Comparator<Object>() {
                            @Override
                            public int compare(Object o1, Object o2) {
                                SPStockItemBean bean1 = (SPStockItemBean) o1;
                                SPStockItemBean bean2 = (SPStockItemBean) o2;
                                Double value1 = Double.valueOf(bean1.getMRP());
                                Double value2 = Double.valueOf(bean2.getMRP());
                                return Double.compare(value1, value2);
                            }
                        });
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                BreadStockListActivity.isRefresh = false;
                BreadAllSKUActivity.isRefresh = false;
            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

    }

    public static void getBreadVANStockMaterials(String cpguid) {
        try {
            stockList = new ArrayList<>();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String mStrMyStockQry = "";
                    String additionalQuery = "";
                    if (MSFAApplication.isSDA) {
                        if (distributorBean != null) {
                            if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                                additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                            }
                            mStrMyStockQry = Constants.SPStockItems + "?$select=CatalogInfo,SkuGroup,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " + "&$orderby=" + Constants.MaterialDesc + " &$filter=" + Constants.StockOwner + " eq '" + distributorBean.getCPTypeID() + "' " + additionalQuery;
                        }
                    } else if (MSFAApplication.isVAN) {
                        if (distributorBean != null) {
                            if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                                additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                            }
                            mStrMyStockQry = Constants.SPStockItems + "?$select=CatalogInfo,SkuGroup,HSNCode,AlternativeUOM1,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " + "&$orderby=" + Constants.MaterialDesc;
                        }
                    } else if (MSFAApplication.isBVAN) {
                        //                    if (BreadDashBoard.distributorBean != null) {
//                        if (!TextUtils.isEmpty(BreadDashBoard.distributorBean.getCPGUID()) && !BreadDashBoard.distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                        // additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + BreadDashBoard.distributorBean.getCPGUID() + "' and StorageLoc eq '"+cpguid+"'";
                        int desiredLength = 10;
                        // Pad the string with leading zeros
                        String distributorCode = String.format("%0" + desiredLength + "d", Integer.parseInt(cpguid));
                        additionalQuery = "StorageLoc eq '" + distributorCode + "'";
//                        additionalQuery = "StorageLoc eq '" + cpguid + "'";
//                        }
                        mStrMyStockQry = Constants.SPStockItems + "?$select=CatalogInfo,SkuGroup,HSNCode,AlternativeUOM1,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " + "&$orderby=" + Constants.MaterialDesc + " &$filter=" + additionalQuery;
//                    }
                    } else {
                        if (distributorBean != null) {
                            if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                                additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                            }
                            mStrMyStockQry = Constants.SPStockItems + "?$select=CatalogInfo,SkuGroup,HSNCode,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " + "&$orderby=" + Constants.MaterialDesc + " &$filter=" + Constants.StockOwner + " eq '" + distributorBean.getCPTypeID() + "' " + additionalQuery;
                        }
                    }
                    String qty = "";
                    String calculatedValue = "";
                    String removedZerosValue = "";
                    int number = 0;
                    List materialList = new OfflineUtils.ODataOfflineBuilder<>().setHeaderPayloadObject(new SPStockItemBean()).setODataEntitySet(Constants.SPStockItems).setODataEntityType(Constants.SPStockItemsEntity).setQuery(mStrMyStockQry).returnODataList(OfflineManager.offlineStore);

                    if (!materialList.isEmpty()) {
                        for (SPStockItemBean bean : (List<SPStockItemBean>) materialList) {
                            ArrayList<SPStockItemSNoBean> itemSNOList = new ArrayList<>();
//                        SPStockItemSNoBean snoBean = null;
                            try {
                                String date = "";
                                if (MSFAApplication.STKEXPUPTO != null && !TextUtils.isEmpty(MSFAApplication.STKEXPUPTO) && !MSFAApplication.STKEXPUPTO.equalsIgnoreCase("0")) {
                                    try {
                                        date = ConstantsUtils.getCPConfigDate(Integer.parseInt(MSFAApplication.STKEXPUPTO));
                                    } catch (Throwable e) {
                                        date = UtilConstants.getNewDate();
                                    }
                                } else {
                                    date = UtilConstants.getNewDate();
                                }
                                String CPStockSnosQry = Constants.SPStockItemSNos + "?$select=Quantity,IntrmUnitPrice,AlternateUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,Tax3,Tax4,Tax5,Tax6,Tax7,Tax8,Tax9,Tax10,Batch,MFD,ExpiryDate,MaterialNo,MaterialDesc " + "&$filter=(StockTypeID eq '1' and SPStockItemGUID eq guid'" + bean.getSPStockItemGUID().toUpperCase() + "') and (ExpiryDate ge datetime'" + date + "' or ExpiryDate eq null) &$orderby=MFD desc ";

//                            snoBean = OfflineManager.getVANMRP(CPStockSnosQry);
                                itemSNOList.addAll(OfflineManager.getVANMRPStockList(CPStockSnosQry));
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            for (SPStockItemSNoBean snoBean : itemSNOList) {
                                SPStockItemBean beanTemp = new SPStockItemBean();
                                beanTemp.setCatalogInfo(bean.getCatalogInfo());
                                beanTemp.setHSNCode(bean.getHSNCode());
                                beanTemp.setProdHier7(bean.getProdHier7());
                                beanTemp.setMatGrpId(bean.getMatGrpId());
                                beanTemp.setMatGrpDesc(bean.getMatGrpDesc());
                                beanTemp.setSkuGroup(bean.getSkuGroup());
                                beanTemp.setAlternativeUOM2(bean.getAlternativeUOM2());
                                beanTemp.setAlternativeUOM2Num(bean.getAlternativeUOM2Num());
                                beanTemp.setAlternativeUOM2Den(bean.getAlternativeUOM2Den());
                                beanTemp.setAlternativeUOM1(bean.getAlternativeUOM1());
                                beanTemp.setAlternativeUOM1Num(bean.getAlternativeUOM1Num());
                                beanTemp.setAlternativeUOM1Den(bean.getAlternativeUOM1Den());
                                beanTemp.setStockValue(bean.getStockValue());
                                beanTemp.setStockTypeID(bean.getStockTypeID());
                                beanTemp.setStockTypeDesc(bean.getStockTypeDesc());
                                beanTemp.setSPStockItemGUID(bean.getSPStockItemGUID());
                                beanTemp.setMaterialNo(bean.getMaterialNo());
                                beanTemp.setMaterialDesc(bean.getMaterialDesc());
                                beanTemp.setMRP(bean.getMRP());
                                beanTemp.setUnrestrictedQty(bean.getUnrestrictedQty());
                                beanTemp.setUOM(bean.getUOM());
                                beanTemp.setCurrency(bean.getCurrency());
                                beanTemp.setLandingPrice(bean.getLandingPrice());
                                beanTemp.setBrand(bean.getBrand());
                                beanTemp.setBrandDesc(bean.getBrandDesc());
                                beanTemp.setOrderMaterialGroupDesc(bean.getOrderMaterialGroupDesc());
                                beanTemp.setOrderMaterialGroupID(bean.getOrderMaterialGroupID());
                                beanTemp.setStockOwner(bean.getStockOwner());
                                beanTemp.setStockOwnerDesc(bean.getStockOwnerDesc());
                                if (snoBean != null) {
                                    beanTemp.setRate(snoBean.getUnitPrice());
                                    beanTemp.setMRP(snoBean.getMRP());
                                    beanTemp.setGross(snoBean.getIntermUnitPrice());
                                    beanTemp.setTax1(snoBean.getTax1());
                                    beanTemp.setTax2(snoBean.getTax2());
                                    beanTemp.setRateTax(snoBean.getRateTax());
                                    beanTemp.setMaterialDesc(snoBean.getMaterialDesc());
                                    beanTemp.setMaterialNo(snoBean.getMaterialNo());
                                    beanTemp.setAlternativeUOM1(snoBean.getAlternateUom());
                                    beanTemp.setAlternativeUOM1Num(snoBean.getAltUomDen());
                                    beanTemp.setAlternativeUOM1Den(snoBean.getAltUomNum());
                                    beanTemp.setUnrestrictedQty(snoBean.getQuantity());
                                    if (!TextUtils.isEmpty(beanTemp.getUnrestrictedQty())) {
                                        if (!beanTemp.getUnrestrictedQty().equalsIgnoreCase("0") && !beanTemp.getUnrestrictedQty().equalsIgnoreCase("0.0")) {
                                            double value = 0.0;
                                            double pieces = 0.0;
                                            double finalValue = 0.0;
                                            double count = 0.0;
                                            double finalPieces = 0.0;
                                            double unrestrictedQty = 0.0;
                                            double altUom2Num = 0.0;
                                            int TotalValue = 0;
                                            beanTemp.setUnrestrictedQtyTemp(beanTemp.getUnrestrictedQty());
                                            unrestrictedQty = Double.parseDouble(beanTemp.getUnrestrictedQty());
                                            if (beanTemp.getAlternativeUOM2Den() != null) {
                                                altUom2Num = Double.parseDouble(beanTemp.getAlternativeUOM1Num());
                                                if (altUom2Num > 0) {
                                                    qty = String.valueOf(unrestrictedQty / altUom2Num);
                                                    try {
                                                        if (qty.contains(".")) {
                                                            double d = Double.parseDouble(qty);
                                                            number = (int) d;
                                                            beanTemp.setUnrestrictedQty(String.valueOf(number));
                                                            if (!TextUtils.isEmpty(qty)) {
                                                                value = Double.parseDouble(qty);
                                                                pieces = value - number;
                                                                if (pieces != 0) {
                                                                    finalValue = pieces * Double.parseDouble(beanTemp.getAlternativeUOM1Num());
                                                                    calculatedValue = String.valueOf(finalValue);
                                                                    removedZerosValue = UtilConstants.removeLeadingZerowithTwoDecimal(calculatedValue);
                                                                    count = Double.parseDouble(removedZerosValue);
                                                                    finalPieces = Double.parseDouble(String.valueOf(count));
                                                                    TotalValue = (int) finalPieces;
                                                                    beanTemp.setPieces(UtilConstants.removeLeadingZero(finalValue));
//                                                    bean.setUnrestrictedQty(qty);
                                                                } else {
                                                                    beanTemp.setPieces(UtilConstants.removeLeadingZero(finalValue));
                                                                }
                                                            }
                                                        } else {
                                                            beanTemp.setUnrestrictedQty(qty);
                                                        }
                                                    } catch (NumberFormatException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                            }
                                        }
                                    }
                                    stockList.add(beanTemp);
                                }
                            }

                        }

                    }
                    try {
                        if (stockList != null && !stockList.isEmpty()) {
                            Set<Object> set = new HashSet<>(stockList);
                            stockList.clear();
                            stockList.addAll(set);
                            Collections.sort(stockList, new Comparator<Object>() {
                                @Override
                                public int compare(Object o1, Object o2) {
                                    SPStockItemBean bean1 = (SPStockItemBean) o1;
                                    SPStockItemBean bean2 = (SPStockItemBean) o2;
                                    Double value1 = Double.valueOf(bean1.getMRP());
                                    Double value2 = Double.valueOf(bean2.getMRP());
                                    return Double.compare(value1, value2);
                                }
                            });
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    BreadStockListActivity.isRefresh = false;
                    BreadAllSKUActivity.isRefresh = false;
                }
            });
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public static void getBCRVANStockMaterials() {
        stockList = new ArrayList<>();
        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                String mStrMyStockQry = "";
                String additionalQuery = "";

                if (distributorBean != null) {
                    if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                        additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                    }
                    mStrMyStockQry = Constants.SPStockItems + "?$select=SkuGroup,HSNCode,AlternativeUOM1,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " +
                            "&$orderby=" + Constants.MaterialDesc;
                }
                String qty = "";
                String calculatedValue = "";
                String removedZerosValue = "";
                int number = 0;
                List materialList = new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(new SPStockItemBean())
                        .setODataEntitySet(Constants.SPStockItems)
                        .setODataEntityType(Constants.SPStockItemsEntity)
                        .setQuery(mStrMyStockQry)
                        .returnODataList(OfflineManager.offlineStore);

                if (!materialList.isEmpty()) {
                    for (SPStockItemBean bean : (List<SPStockItemBean>) materialList) {
                        ArrayList<SPStockItemSNoBean> itemSNOList = new ArrayList<>();
//                        SPStockItemSNoBean snoBean = null;
                        try {
                            String date ="";
                            if (MSFAApplication.STKEXPUPTO!=null&&!TextUtils.isEmpty(MSFAApplication.STKEXPUPTO)&&!MSFAApplication.STKEXPUPTO.equalsIgnoreCase("0")) {
                                try {
                                    date = ConstantsUtils.getCPConfigDate(Integer.parseInt(MSFAApplication.STKEXPUPTO));
                                } catch (Throwable e) {
                                    date = UtilConstants.getNewDate();
                                }
                            }else{
                                date = UtilConstants.getNewDate();
                            }
                            String CPStockSnosQry = Constants.SPStockItemSNos+ "?$select=Quantity,IntrmUnitPrice,AlternateUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,Tax3,Tax4,Tax5,Tax6,Tax7,Tax8,Tax9,Tax10,Batch,MFD,ExpiryDate,MaterialNo,MaterialDesc " +
                                    "&$filter=(StockTypeID eq '1' and SPStockItemGUID eq guid'"+bean.getSPStockItemGUID().toUpperCase()+"') and (ExpiryDate gt datetime'"+date+"' or ExpiryDate eq null) &$orderby=MFD desc ";

//                            snoBean = OfflineManager.getVANMRP(CPStockSnosQry);
                            itemSNOList.addAll(OfflineManager.getVANMRPStockList(CPStockSnosQry));
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        for(SPStockItemSNoBean snoBean : itemSNOList) {
                            SPStockItemBean beanTemp = new SPStockItemBean();
                            beanTemp.setHSNCode(bean.getHSNCode());
                            beanTemp.setProdHier7(bean.getProdHier7());
                            beanTemp.setMatGrpId(bean.getMatGrpId());
                            beanTemp.setMatGrpDesc(bean.getMatGrpDesc());
                            beanTemp.setSkuGroup(bean.getSkuGroup());
                            beanTemp.setAlternativeUOM2(bean.getAlternativeUOM2());
                            beanTemp.setAlternativeUOM2Num(bean.getAlternativeUOM2Num());
                            beanTemp.setAlternativeUOM2Den(bean.getAlternativeUOM2Den());
                            beanTemp.setAlternativeUOM1(bean.getAlternativeUOM1());
                            beanTemp.setAlternativeUOM1Num(bean.getAlternativeUOM1Num());
                            beanTemp.setAlternativeUOM1Den(bean.getAlternativeUOM1Den());
                            beanTemp.setStockValue(bean.getStockValue());
                            beanTemp.setStockTypeID(bean.getStockTypeID());
                            beanTemp.setStockTypeDesc(bean.getStockTypeDesc());
                            beanTemp.setSPStockItemGUID(bean.getSPStockItemGUID());
                            beanTemp.setMaterialNo(bean.getMaterialNo());
                            beanTemp.setMaterialDesc(bean.getMaterialDesc());
//                                        beanTemp.setMRP(bean.getMRP());
                            beanTemp.setUnrestrictedQty(bean.getUnrestrictedQty());
                            beanTemp.setUOM(bean.getUOM());
                            beanTemp.setCurrency(bean.getCurrency());
                            beanTemp.setLandingPrice(bean.getLandingPrice());
                            beanTemp.setBrand(bean.getBrand());
                            beanTemp.setBrandDesc(bean.getBrandDesc());
                            beanTemp.setOrderMaterialGroupDesc(bean.getOrderMaterialGroupDesc());
                            beanTemp.setOrderMaterialGroupID(bean.getOrderMaterialGroupID());
                            beanTemp.setStockOwner(bean.getStockOwner());
                            beanTemp.setStockOwnerDesc(bean.getStockOwnerDesc());
                            if (snoBean != null) {
                                beanTemp.setRate(snoBean.getUnitPrice());
                                beanTemp.setMRP(snoBean.getMRP());
                                beanTemp.setGross(snoBean.getIntermUnitPrice());
                                beanTemp.setTax1(snoBean.getTax1());
                                beanTemp.setTax2(snoBean.getTax2());
                                beanTemp.setRateTax(snoBean.getRateTax());
                                beanTemp.setMaterialDesc(snoBean.getMaterialDesc());
                                beanTemp.setMaterialNo(snoBean.getMaterialNo());
                                beanTemp.setAlternativeUOM1(snoBean.getAlternateUom());
                                beanTemp.setAlternativeUOM1Num(snoBean.getAltUomDen());
                                beanTemp.setAlternativeUOM1Den(snoBean.getAltUomNum());
                                beanTemp.setUnrestrictedQty(snoBean.getQuantity());
                                if (!TextUtils.isEmpty(beanTemp.getUnrestrictedQty())) {
                                    double value = 0.0;
                                    double pieces = 0.0;
                                    double finalValue = 0.0;
                                    double count = 0.0;
                                    double finalPieces = 0.0;
                                    double unrestrictedQty = 0.0;
                                    double altUom2Num = 0.0;
                                    int TotalValue = 0;
                                    beanTemp.setUnrestrictedQtyTemp(beanTemp.getUnrestrictedQty());
                                    unrestrictedQty = Double.parseDouble(beanTemp.getUnrestrictedQty());
                                    if (beanTemp.getAlternativeUOM2Den() != null) {
                                        altUom2Num = Double.parseDouble(beanTemp.getAlternativeUOM1Num());
                                        if (altUom2Num > 0) {
                                            qty = String.valueOf(unrestrictedQty / altUom2Num);
                                            try {
                                                if (qty.contains(".")) {
                                                    double d = Double.parseDouble(qty);
                                                    number = (int) d;
                                                    beanTemp.setUnrestrictedQty(String.valueOf(number));
                                                    if (!TextUtils.isEmpty(qty)) {
                                                        value = Double.parseDouble(qty);
                                                        pieces = value - number;
                                                        if (pieces != 0) {
                                                            finalValue = pieces * Double.parseDouble(beanTemp.getAlternativeUOM1Num());
                                                            calculatedValue = String.valueOf(finalValue);
                                                            removedZerosValue = UtilConstants.removeLeadingZerowithTwoDecimal(calculatedValue);
                                                            count = Double.parseDouble(removedZerosValue);
                                                            finalPieces = Double.parseDouble(String.valueOf(count));
                                                            TotalValue = (int) finalPieces;
                                                            beanTemp.setPieces(UtilConstants.removeLeadingZero(finalValue));
//                                                    bean.setUnrestrictedQty(qty);
                                                        }
                                                    }
                                                } else {
                                                    beanTemp.setUnrestrictedQty(qty);
                                                }
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                }
                                stockList.add(beanTemp);
                            }
                        }

                    }

                }
            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

    }

    public void fetchUserChannelPartners(String CPNo){
        String query = Constants.UserChannelPartners;
        List userChannelPartnersList = new OfflineUtils.ODataOfflineBuilder<>()
                .setHeaderPayloadObject(new UserChannelPartnerBean())
                .setODataEntitySet(Constants.UserChannelPartners)
                .setODataEntityType(Constants.UserChannelPartnerEntity)
                .setUIListener(this)
                .setQuery(query)
                .returnODataList(OfflineManager.offlineStore);
        if (userChannelPartnersList!=null&&!userChannelPartnersList.isEmpty()) {
            UserChannelPartnerBean bean = (UserChannelPartnerBean) userChannelPartnersList.get(0);
            distributorBean.setGSTIN(bean.getGSTIN());
            distributorBean.setFSSAI(bean.getFSSAI());
        }
    }

    */
/**
     * This method will give us the daily productive calls which has the unique order count towards retailer.
     * only soldto will be selected for current date order, along with fetching the total customer details.
     * @param context
     *//*

    public static void getDailyPC(Context context){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String mStrPC="";
                    try {
                        mStrPC = OfflineManager.getUniqueSOCount(Constants.SSSOs +
                                "?$select=SoldToCPGUID &$filter=" + Constants.OrderDate + " eq datetime'" + UtilConstants.getNewDate() + "' and " + Constants.SPGUID + " eq guid'" +MSFAApplication.SPGUID + "'",context);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    int intPC;
                    try {
                        intPC = OfflineManager.getDeviceTotalOrderUnique(Constants.SecondarySOCreate, context, UtilConstants.getNewDate());
                    } catch (Exception e) {
                        intPC = 0;
                        e.printStackTrace();
                    }
                    */
/**
                     * getting the total customers count for visited/ordered.
                     *//*

                    String totalCustomersQry = "ChannelPartners/$count?$filter=StatusID eq '01' and ApprvlStatusID eq '03'";
                    try {
                        Constants.TotalCustomers  = UtilOfflineManager.getEntityCount(OfflineManager.offlineStore, totalCustomersQry);
                    } catch (OfflineODataStoreException e) {
                        e.printStackTrace();
                    }
                    if(!TextUtils.isEmpty(mStrPC)) {
                        double pecentage = 0.00;
                        try {
                            if(!TextUtils.isEmpty(Constants.TotalCustomers)){
                                pecentage = (double)(Integer.parseInt(mStrPC) + intPC)* (Integer.parseInt(Constants.TotalCustomers))/100;
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        TotalProCalls = String.valueOf(Integer.parseInt(mStrPC) + intPC) + " (" +pecentage +"%)";
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    */
/**
     * This method will give the DailyLine items ordered. initially get SSSO Header data and form a query to call Item Details for total line items count.
     * also while forming query calculate the total order value and appenidng the datavalut order value..
     *//*

    public static void getDailyLineCuts(Context context){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String query="SSSOs?$select=SSSOGuid,NetPrice &$filter="+Constants.OrderDate + " eq datetime'" + UtilConstants.getNewDate() + "' and " + Constants.SPGUID + " eq guid'" +MSFAApplication.SPGUID+"'";
                List list = (ArrayList) new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(new SSSOsHeaderBean())
                        .setODataEntitySet(Constants.SSSOs)
                        .setODataEntityType(Constants.SSSOEntity)
                        .setQuery(query)
                        .returnODataList(OfflineManager.offlineStore);
                String itemQry="";
                double totalOrderValue = 0;
                double mdouDevOrderVal=0;
                int deviceItemSSSOCount = 0;

                try {
                    deviceItemSSSOCount = OfflineManager.getDeviceSSSoDetails(Constants.SecondarySOCreate,context, UtilConstants.getNewDate());
                } catch (Exception e) {
                    deviceItemSSSOCount =0;
                }
                try {
                    mdouDevOrderVal = OfflineManager.getDeviceTotalOrderAmt(Constants.SecondarySOCreate, context, UtilConstants.getNewDate(), null);
                } catch (Exception e) {
                    mdouDevOrderVal = 0.0;
                }
                if (list!=null&&!list.isEmpty()){
                    itemQry="SSSOItemDetails/$count?$filter=";
                    for (int i = 0; i < list.size(); i++) {
                        Object object1 =list.get(i);
                        if (object1 instanceof SSSOsHeaderBean){
                            SSSOsHeaderBean bean1 = (SSSOsHeaderBean) object1;
                            if (!TextUtils.isEmpty(bean1.getSSSOGuid())){
                                if (i!=list.size()-1) {
                                    if (bean1.getNetPrice()!=null&&!TextUtils.isEmpty(bean1.getNetPrice())) {
                                        totalOrderValue = totalOrderValue + Double.parseDouble(bean1.getNetPrice());
                                    }
                                    itemQry =itemQry.concat("SSSOGuid eq guid'"+bean1.getSSSOGuid()+"' or ");
                                }else{
                                    if (bean1.getNetPrice()!=null&&!TextUtils.isEmpty(bean1.getNetPrice())) {
                                        totalOrderValue = totalOrderValue + Double.parseDouble(bean1.getNetPrice());
                                    }
                                    itemQry =itemQry.concat("SSSOGuid eq guid'"+bean1.getSSSOGuid()+"'");
                                }
                            }
                        }
                    }
                    try {
                        Constants.TotalLineCuts  = UtilOfflineManager.getEntityCount(OfflineManager.offlineStore, itemQry);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                */
/**
                 * getting the datavault total line count and appending it to offline count.
                 *//*

                try {
                    try {
                        try {

                            Constants.TotalLineCuts = String.valueOf(Integer.parseInt(Constants.TotalLineCuts)+deviceItemSSSOCount);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    */
/**
                     * getting the datavault total order value appending it to offline data value.
                     *//*


                    try {
                        Constants.TodayOrderVal = String.valueOf(UtilConstants.round(totalOrderValue + mdouDevOrderVal,2));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private static void isCurrentMonthGO(RetailerSelectionBean bean,Context context){
        List<CPPerformanceBean>cpList=null;
        if (Build.VERSION.SDK_INT >= 24) {
            if (cpPerformanceList != null && !cpPerformanceList.isEmpty()) {
                cpList = (List) cpPerformanceList.stream().filter((Predicate<Object>) predicate -> ((CPPerformanceBean) predicate).getCPGUID()!=null&&((CPPerformanceBean) predicate).getCPGUID().equalsIgnoreCase(bean.getCPGUID().replace("-",""))).collect(java.util.stream.Collectors.toList());
            }
        } else {
            if (cpPerformanceList != null && !cpPerformanceList.isEmpty()) {
                cpList = (List) Stream.of(cpPerformanceList).filter((com.annimon.stream.function.Predicate<Object>) predicate -> ((CPPerformanceBean) predicate).getCPGUID()!=null&&((CPPerformanceBean) predicate).getCPGUID().equalsIgnoreCase(bean.getCPGUID().replace("-",""))).collect(com.annimon.stream.Collectors.toList());
            }
        }
        if (cpList!=null&&!cpList.isEmpty()){
            CPPerformanceBean cpBean = cpList.get(0);
            try {
                if (cpBean.getString1() != null && !TextUtils.isEmpty(cpBean.getString1())) {
                    JSONObject object = new JSONObject(cpBean.getString1());
                    CPPerformanceBean cpBean1 = OfflineManager.getLMGOJSONData(object, "cm");
                    if (cpBean1 != null && cpBean1.isGO()) {
                        bean.setCMGO(true);
                        if(bean.getGroup10().equalsIgnoreCase("X")) {
                            bean.setColor(ContextCompat.getColor(context, R.color.bil_theme_clr));
                        }else{
                            bean.setColor(ContextCompat.getColor(context, R.color.bil_theme_dark_clr));
                        }
                    }else{
                        SOMaterialCalculation(bean,context);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void SOMaterialCalculation(RetailerSelectionBean bean,Context context){
        List<ColorBean>soList=null;
        List<SOItemGOBean>soItemList=null;
        if (Build.VERSION.SDK_INT >= 24) {
            if (SSSOColorList != null && !SSSOColorList.isEmpty()) {
                soList = (List) SSSOColorList.stream().filter((Predicate<Object>) predicate -> ((ColorBean) predicate).getSoldToCPGUID()!=null&&((ColorBean) predicate).getSoldToCPGUID().equalsIgnoreCase(bean.getCPGUID())).collect(java.util.stream.Collectors.toList());
            }
        } else {
            if (SSSOColorList != null && !SSSOColorList.isEmpty()) {
                soList = (List) Stream.of(SSSOColorList).filter((com.annimon.stream.function.Predicate<Object>) predicate -> ((ColorBean) predicate).getSoldToCPGUID()!=null&&((ColorBean) predicate).getSoldToCPGUID().equalsIgnoreCase(bean.getCPGUID())).collect(com.annimon.stream.Collectors.toList());
            }
        }
        HashSet<String>hashSet=new HashSet<>();
        if (soList!=null&&!soList.isEmpty()){
            for (ColorBean soBean:soList) {
                if (Build.VERSION.SDK_INT >= 24) {
                    if (SSSOItemDetailsList != null && !SSSOItemDetailsList.isEmpty()) {
                        soItemList = (List) SSSOItemDetailsList.stream().filter((Predicate<Object>) predicate -> ((SOItemGOBean) predicate).getSSSOGuid()!=null&&((SOItemGOBean) predicate).getSSSOGuid().equalsIgnoreCase(soBean.getSSSOGuid())).collect(java.util.stream.Collectors.toList());
                    }
                } else {
                    if (SSSOItemDetailsList != null && !SSSOItemDetailsList.isEmpty()) {
                        soItemList = (List) Stream.of(SSSOItemDetailsList).filter((com.annimon.stream.function.Predicate<Object>) predicate -> ((SOItemGOBean) predicate).getSSSOGuid()!=null&&((SOItemGOBean) predicate).getSSSOGuid().equalsIgnoreCase(soBean.getSSSOGuid())).collect(com.annimon.stream.Collectors.toList());
                    }
                }
                for (SOItemGOBean value:soItemList) {
                    if (value.getSkuGroup()!=null&&!TextUtils.isEmpty(value.getSkuGroup())) {
                        hashSet.add(value.getSkuGroup());
                    }
                }
            }
        }
        HashSet<String>materialSet = OfflineManager.getSOLocalItemMaterialList(context,bean.getCPGUID());
        if (materialSet!=null&&!materialSet.isEmpty()) {
            hashSet.addAll(materialSet);
        }
        boolean isGO=false;
        try {
            if (targetsItemsList!=null&&!targetsItemsList.isEmpty()){
                double targetLines=0;
                double actualLines=0;
                double actualValue=0;
                double targetValue=0;
                try {
                    List<TargetItemsBean>targetItemsBeanList=null;
                    if (Build.VERSION.SDK_INT >= 24) {
                        if (targetsItemsList != null && !targetsItemsList.isEmpty()) {
                            targetItemsBeanList = (List) targetsItemsList.stream().filter((Predicate<Object>) predicate -> ((TargetItemsBean) predicate).getPartnerGUID()!=null&&((TargetItemsBean) predicate).getPartnerGUID().equalsIgnoreCase(bean.getCPGUID().replace("-",""))).collect(java.util.stream.Collectors.toList());
                        }
                    } else {
                        if (targetsItemsList != null && !targetsItemsList.isEmpty()) {
                            targetItemsBeanList = (List) Stream.of(targetsItemsList).filter((com.annimon.stream.function.Predicate<Object>) predicate -> ((TargetItemsBean) predicate).getPartnerGUID()!=null&&((TargetItemsBean) predicate).getPartnerGUID().equalsIgnoreCase(bean.getCPGUID().replace("-",""))).collect(com.annimon.stream.Collectors.toList());
                        }
                    }
                    for (Object object:targetItemsBeanList) {
                        if (object instanceof TargetItemsBean){
                            TargetItemsBean targetItemsBean = (TargetItemsBean) object;
                            if (targetItemsBean.getTargetQty()!=null&&!TextUtils.isEmpty(targetItemsBean.getTargetQty())){
                                try {
                                    targetLines = targetLines+Double.parseDouble(targetItemsBean.getTargetQty());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (targetItemsBean.getTargetValue()!=null&&!TextUtils.isEmpty(targetItemsBean.getTargetValue())){
                                try {
                                    targetValue = targetValue+Double.parseDouble(targetItemsBean.getTargetValue());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }

                            */
/*if (targetItemsBean.getActualQty()!=null&&!TextUtils.isEmpty(targetItemsBean.getActualQty())){
                                try {
                                    actualLines = actualLines+Double.parseDouble(targetItemsBean.getActualQty());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (targetItemsBean.getActualValue()!=null&&!TextUtils.isEmpty(targetItemsBean.getActualValue())){
                                try {
                                    actualValue = actualValue+Double.parseDouble(targetItemsBean.getActualValue());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }*//*

                        }
                    }
                    JSONObject jsonObject = null;
                    CPPerformanceBean cpPerformanceBean = null;
                    List list;
                    CommonDB commonDB = new CommonDB(context);
                    if (commonDB.getRecordExist(bean.getCPGUID().replaceAll("-","").toUpperCase(),DashBoard.distributorBean.getCPNo())) {
                        list = commonDB.getCPData(bean.getCPGUID().replaceAll("-","").toUpperCase(),DashBoard.distributorBean.getCPNo());
                    }else
                    {
                        String qry = Constants.CPPerformances+"?$filter=SPGUID eq guid'"+MSFAApplication.SPGUID+"' and CPGUID eq '"+bean.getCPGUID().toUpperCase().replace("-","")+"' and ParentNo eq '"+ DashBoard.distributorBean.getCPNo() +"'";
                        list = new OfflineUtils.ODataOfflineBuilder<>()
                                .setHeaderPayloadObject(new CPPerformanceBean())
                                .setODataEntitySet(Constants.CPPerformances)
                                .setODataEntityType(Constants.CPPerformanceEntity)
                                .setQuery(qry)
                                .setETag(true)
                                .returnODataList(OfflineManager.offlineStore);
                    }

                    if (list!=null&&!list.isEmpty()){
                        try {
                            cpPerformanceBean = (CPPerformanceBean) list.get(0);
                            if(cpPerformanceBean.getString1()!=null&&!TextUtils.isEmpty(cpPerformanceBean.getString1())){
                                jsonObject = new JSONObject(cpPerformanceBean.getString1());
                                if (jsonObject.getJSONObject("cmbilledmat")!=null) {
                                    String materialValue =jsonObject.getJSONObject("cmbilledmat").getString("material");
                                    if (materialValue!=null&&!TextUtils.isEmpty(materialValue)) {
                                        hashSet.addAll(Arrays.asList(materialValue.split(",")));
                                    }
                                }
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }

                    }
                    actualLines = hashSet.size();
                    if (targetValue>=0&&targetLines>0) {
                        isGO = actualLines>=targetLines;
                    }
                    if (isGO){
                        bean.setCMGO(isGO);
                        bean.setColor(ContextCompat.getColor(context,R.color.GREEN));
                    }
                    updateCPPerformamce(bean.getCPGUID(),hashSet,isGO,context);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
    private static void updateCPPerformamce(String CPGUID, HashSet<String> materialList, boolean isGO,Context context) {
        try {

            new Thread(() -> {

                CommonDB commonDB = new CommonDB(context);
                CPPerformanceBean cpPerformanceBean = null;
                if (!commonDB.getRecordExist(CPGUID.replaceAll("-","").toUpperCase(),DashBoard.distributorBean.getCPNo())) {
                    String qry = Constants.CPPerformances+"?$filter=SPGUID eq guid'"+MSFAApplication.SPGUID+"' and CPGUID eq '"+CPGUID.toUpperCase().replace("-","")+"' and ParentNo eq '"+ DashBoard.distributorBean.getCPNo() +"'";
                    List list = new OfflineUtils.ODataOfflineBuilder<>()
                            .setHeaderPayloadObject(new CPPerformanceBean())
                            .setODataEntitySet(Constants.CPPerformances)
                            .setODataEntityType(Constants.CPPerformanceEntity)
                            .setQuery(qry)
                            .setETag(true)
                            .returnODataList(OfflineManager.offlineStore);
                    if (list!=null&&!list.isEmpty()){
                        cpPerformanceBean = (CPPerformanceBean) list.get(0);
                    }
                    if (cpPerformanceBean!=null) {
                        try {
                            if (cpPerformanceBean.getString1()!=null&&!TextUtils.isEmpty(cpPerformanceBean.getString1())) {
                                JSONObject jsonObject = new JSONObject(cpPerformanceBean.getString1());
                                if (isGO) {
                                    jsonObject.getJSONObject("cm").put("go", "X");
                                    if (materialList != null && !materialList.isEmpty()) {
                                        markGreenMaterials(jsonObject, materialList);
                                    }
                                    cpPerformanceBean.setString1(jsonObject.toString());
                                    commonDB.createRecord(cpPerformanceBean);

                                }else if(materialList!=null&&!materialList.isEmpty()) {
                                    markGreenMaterials(jsonObject,materialList);
                                    cpPerformanceBean.setString1(jsonObject.toString());
                                    commonDB.createRecord(cpPerformanceBean);
                                }
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    //update
                    ArrayList<CPPerformanceBean> list = commonDB.getCPData(CPGUID.replaceAll("-","").toUpperCase(),DashBoard.distributorBean.getCPNo());

                    if (list!=null&&!list.isEmpty()){
                        cpPerformanceBean = (CPPerformanceBean) list.get(0);
                    }
                    if (cpPerformanceBean!=null) {
                        try {
                            if (cpPerformanceBean.getString1() != null && !TextUtils.isEmpty(cpPerformanceBean.getString1())) {
                                JSONObject jsonObject = new JSONObject(cpPerformanceBean.getString1());
                                if (isGO) {
                                    jsonObject.getJSONObject("cm").put("go", "X");
                                    if (materialList != null && !materialList.isEmpty()) {
                                        markGreenMaterials(jsonObject, materialList);
                                    }
                                    cpPerformanceBean.setString1(jsonObject.toString());
//                                    commonDB.createRecord(cpPerformanceBean);

                                }else if(materialList!=null&&!materialList.isEmpty()) {
                                    markGreenMaterials(jsonObject,materialList);
                                    cpPerformanceBean.setString1(jsonObject.toString());
//                                    commonDB.createRecord(cpPerformanceBean);
                                }
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        commonDB.updatedCPRecord(cpPerformanceBean,cpPerformanceBean.getCPPerfmGUID());
                    }
                }



                */
/*CPPerformanceBean cpPerformanceBean = null;
                String qry = Constants.CPPerformances+"?$filter=SPGUID eq guid'"+MSFAApplication.SPGUID+"' and CPGUID eq '"+CPGUID.toUpperCase().replace("-","")+"' and ParentNo eq '"+ DashBoard.distributorBean.getCPNo() +"'";
                List list = new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(new CPPerformanceBean())
                        .setODataEntitySet(Constants.CPPerformances)
                        .setODataEntityType(Constants.CPPerformanceEntity)
                        .setQuery(qry)
                        .setETag(true)
                        .returnODataList(OfflineManager.offlineStore);
                if (list!=null&&!list.isEmpty()){
                    cpPerformanceBean = (CPPerformanceBean) list.get(0);
                }
                if (cpPerformanceBean!=null) {
                    try {
                        if (cpPerformanceBean.getString1()!=null&&!TextUtils.isEmpty(cpPerformanceBean.getString1())) {
                            JSONObject jsonObject = new JSONObject(cpPerformanceBean.getString1());
                            if (isGO) {
                                jsonObject.getJSONObject("cm").put("go","X");
                                if (materialList!=null&&!materialList.isEmpty()) {
                                    markGreenMaterials(jsonObject,materialList);
                                }
                                cpPerformanceBean.setString1(jsonObject.toString());
                                String resourcePath = "CPPerformances(guid'" + cpPerformanceBean.getCPPerfmGUID().toUpperCase() + "')";
                                new OfflineUtils.ODataOfflineBuilder()
                                        .setODataEntitySet(Constants.CPPerformances)
                                        .setODataEntityType(Constants.CPPerformanceEntity)
                                        .setCreate(false)
                                        .setUpdate(true)
                                        .setDelete(false)
                                        .setHeaderPayloadObject(cpPerformanceBean)
                                        .setResourcePath(resourcePath, resourcePath)
                                        .setETag(cpPerformanceBean.getETAG())
                                        .build((errorAtField, errorMessage) -> {
                                            LogManager.writeLogError(errorAtField + " " + errorAtField);
                                        });
                            }else if(materialList!=null&&!materialList.isEmpty()) {
                                markGreenMaterials(jsonObject,materialList);
                                cpPerformanceBean.setString1(jsonObject.toString());
                                String resourcePath = "CPPerformances(guid'" + cpPerformanceBean.getCPPerfmGUID().toUpperCase() + "')";
                                new OfflineUtils.ODataOfflineBuilder()
                                        .setODataEntitySet(Constants.CPPerformances)
                                        .setODataEntityType(Constants.CPPerformanceEntity)
                                        .setCreate(false)
                                        .setUpdate(true)
                                        .setDelete(false)
                                        .setHeaderPayloadObject(cpPerformanceBean)
                                        .setResourcePath(resourcePath, resourcePath)
                                        .setETag(cpPerformanceBean.getETAG())
                                        .build((errorAtField, errorMessage) -> {
                                            LogManager.writeLogError(errorAtField + " " + errorAtField);
                                        });
                            }

                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }*//*

            }).start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    private static void markGreenMaterials(JSONObject jsonObject,HashSet<String>materialList){
        try {
            String value = jsonObject.getJSONObject("cmbilledmat").getString("material");
            for (String material :materialList) {
                if (!value.contains(material)){
                    if (value.length()!=0){
                        value = value+","+material;
                    }else{
                        value = material;
                    }
                }
            }
            if(jsonObject.getJSONObject("cmbilledmat")!=null){
                jsonObject.getJSONObject("cmbilledmat").put("material",value);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void ComputeTargets(){
        */
/*String kpiQuery = Constants.ConfigTypsetTypeValues+"?$filter=Typeset eq 'ZKPICD' and Types eq '02'";
        List kpiList = new OfflineUtils.ODataOfflineBuilder<>()
                .setHeaderPayloadObject(new ConfigTypsetTypeValuesBean())
                .setODataEntitySet(Constants.ConfigTypsetTypeValues)
                .setODataEntityType(".ConfigTypsetTypeValue")
                .setQuery(kpiQuery)
                .returnODataList(OfflineManager.offlineStore);
        String code="";
        if (kpiList!=null&&!kpiList.isEmpty()) {
            code = ((ConfigTypsetTypeValuesBean)kpiList.get(0)).getTypeValue();
            code =UtilConstants.removeLeadingZeros(code);
        }

        if (!TextUtils.isEmpty(code)) {
            String query = Constants.KPISet + "?$select=KPIGUID &$filter=ValidTo ge datetime'" + UtilConstants.getNewDate() + "' and KPICode eq '" + code + "'";
            String targetQuery = Constants.Targets + "?$select=TargetGUID &$filter=Period eq '" + getCurrentMonthPeriod() + "' and KPIGUID eq ";
            String targetItemQuery = Constants.TargetItems + "?$select=TargetGUID,TargetItemGUID,KPICode,Period,Periodicity,PartnerGUID,PartnerType,MaterialNo,TargetQty," +
                    "TargetValue,ActualQty,ActualValue,TargetAchivement,IncentiveAmount,ActualIncentiveAmt,LotSize &$filter=Period eq '" + getCurrentMonthPeriod() + "' and TargetGUID eq ";
            List list = new ArrayList();
            *//*
*/
/**
         * fetching the KPI Header based on the Typeset value ULPO provided in query.
         *//*
*/
/*
            list = new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new KPISetBean())
                    .setODataEntitySet(Constants.KPISet)
                    .setODataEntityType(".KPI")
                    .setQuery(query)
                    .returnODataList(OfflineManager.offlineStore);
            if (list != null && !list.isEmpty()) {
                if (list.get(0) instanceof KPISetBean) {
                    KPISetBean bean = (KPISetBean) list.get(0);
                    targetQuery = targetQuery.concat("guid'" + bean.getKPIGUID() + "'");
                }
            }
            *//*
*/
/**
         * fetching the Targets based on the KPIGUID provided in query.
         *//*
*/
/*
            List targetList = new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new TargetsBean())
                    .setODataEntitySet(Constants.Targets)
                    .setODataEntityType(".Target")
                    .setQuery(targetQuery)
                    .returnODataList(OfflineManager.offlineStore);
            if (targetList != null && !targetList.isEmpty()) {
                if (targetList.get(0) instanceof TargetsBean) {
                    TargetsBean bean = (TargetsBean) targetList.get(0);
                    targetItemQuery = targetItemQuery.concat("guid'" + bean.getTargetGUID() + "'");
                }
            }
            *//*
*/
/**
         * fetching the Target Items based on the TargetGUID provided in query.
         *//*
*/
/*
            if (!targetList.isEmpty()) {
                targetsItemsList = new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(new TargetItemsBean())
                        .setODataEntitySet(Constants.TargetItems)
                        .setODataEntityType(".TargetItem")
                        .setQuery(targetItemQuery)
                        .returnODataList(OfflineManager.offlineStore);

            }*//*

        String kpiQuery = Constants.ConfigTypsetTypeValues+"?$filter=Typeset eq 'SF' and Types eq 'ZKPIBM'";
        List kpiList = new OfflineUtils.ODataOfflineBuilder<>()
                .setHeaderPayloadObject(new ConfigTypsetTypeValuesBean())
                .setODataEntitySet(Constants.ConfigTypsetTypeValues)
                .setODataEntityType(".ConfigTypsetTypeValue")
                .setQuery(kpiQuery)
                .returnODataList(OfflineManager.offlineStore);
        String code="";
        if (kpiList!=null&&!kpiList.isEmpty()) {
            code = ((ConfigTypsetTypeValuesBean)kpiList.get(0)).getTypeValue();
//            code =UtilConstants.removeLeadingZeros(code);
        }
        String targetItemQuery = Constants.TargetItems + "?$select=TargetGUID,TargetItemGUID,KPICode,Period,Periodicity,PartnerGUID,PartnerType,MaterialNo,TargetQty," +
                "TargetValue,ActualQty,ActualValue,TargetAchivement,IncentiveAmount,ActualIncentiveAmt,LotSize &$filter=Period eq '" + getCurrentMonthPeriod() + "' and KPICode eq '"+code+"'";

        targetsItemsList = new OfflineUtils.ODataOfflineBuilder<>()
                .setHeaderPayloadObject(new TargetItemsBean())
                .setODataEntitySet(Constants.TargetItems)
                .setODataEntityType(".TargetItem")
                .setQuery(targetItemQuery)
                .returnODataList(OfflineManager.offlineStore);
    }

    private void refreshCPSPRelations(){
        try {
            if (UtilConstants.isNetworkAvailable(context)) {
                isMaterSyncing =true;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setTimer();
                        messageProgess= "Refreshing Distributor details, Please wait..";
                        view.showProgress(messageProgess);
                    }
                });
                OfflineManager.refreshStoreSync(context, new UIListener() {
                    @Override
                    public void onRequestError(int operation, Exception e) {
                        try {
                            ConstantsUtils.checkNetwork(activity,null,true);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    view.hideProgress();
                                    cancelTimer();
                                }
                            });
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Constants.showAlert("Synchronization not completed due to error in CPSPRelations[400]. Please contact support team [10340]",context);
                                }
                            });
                            LogManager.writeLogError("store failed to open with an error : "+e.toString());
                        } catch (Throwable ex) {
                            ex.printStackTrace();
                            LogManager.writeLogError(Constants.error_txt + ex.getMessage());
                        }
                    }

                    @Override
                    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                        ConstantsUtils.checkNetwork(activity,null,true);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.hideProgress();
                                cancelTimer();
                            }
                        });
                        String routeQry = Constants.CPSPRelations+"?$select=CPSPGUID,CPTypeID,CPTypeDesc,CPGUID,CPNo,CPName,CPUID,SPType,SPTypeDesc,SPGUID,SPNo,FirstName,LastName,Address1,Address2,Address3,Address4,CityDesc," +
                                "StateDesc,CountryDesc,PostalCode,MobileNo";
                        LogManager.writeLogInfo("Distributor selection distributor qry  : "+routeQry);
                        if (OfflineManager.isOfflineStoreOpen()) {

                            distributorBeanList = new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new DistributorSelectionBean())
                                    .setODataEntitySet(Constants.CPSPRelations)
                                    .setODataEntityType(Constants.CPSPRelationEntity)
                                    .setUIListener(this)
                                    .setQuery(routeQry)
                                    .returnODataList(OfflineManager.offlineStore);
                            LogManager.writeLogInfo("Distributor selection distributor list count  : "+distributorBeanList.size());
                            if (distributorBeanList!=null&&!distributorBeanList.isEmpty()) {
                                Set set = new HashSet(distributorBeanList);
                                distributorBeanList.clear();
                                distributorBeanList.addAll(set);
                                Collections.sort(distributorBeanList, (Comparator<DistributorSelectionBean>) (one, other) -> one.getCPName().compareTo(other.getCPName()));
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.refreshSpinners(distributorBeanList, 1);
                                    }
                                });
                            }else{
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Constants.showAlert("Incorrect or missing authorization for CPSPRelations. Please contact support team",context);
                                    }
                                });
                                LogManager.writeLogError("CPSPRelations giving 0 records");
                            }
                        }

                    }
                }, Constants.Fresh,Constants.CPSPRelations);
            }else{
                Constants.showAlert(context.getString(R.string.network_error_message), context);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    String countdown = "";
    long second = 0;
    String messageProgess = "";
    long milliSec = 2400000;
    CountDownTimer countDownTimer = null;

    private void setTimer() {
        countdown = "";
        try {
            if (countDownTimer != null) {
                LogManager.writeLogInfo("Login Timer count Down is cancel");
                countDownTimer.cancel();
                countDownTimer = null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        countDownTimer = new CountDownTimer(milliSec, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = milliSec / 1000 - millisUntilFinished / 1000;
                long minutes = ((milliSec / 1000) / 60) - ((millisUntilFinished / 1000) / 60) - 1;
//                            if(minutes==(seconds/60)){
//                                second=0;
//                            }else{
//                                second++;
//                            }

                int sec = (int) seconds - (int) minutes * 60;
                countdown = String.format("%02d", minutes) + ":" + String.format("%02d", sec);
                String timeStamp = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm", new Date());
                view.showProgress(messageProgess + "\t\t" + countdown);
            }

            @Override
            public void onFinish() {

            }

        };
        countDownTimer.start();
    }

    public void cancelTimer() {
        try {
            if (countDownTimer != null) {
                LogManager.writeLogInfo("update sync Timer count Down is cancel");
                countDownTimer.cancel();
                countDownTimer = null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
    //--------------------------------------------------------------------------quodeck--------------------------------------------------------------------------
    private HashMap<String,String>headerMapListAuth;
    String[] quodeckCredential;
    public void quodeckSignIn(String UID,String firstName,String lastName,String regionName,String AWcode){
        if (!quodeckCredential[0].isEmpty() || !quodeckCredential[1].isEmpty() || !quodeckCredential[2].isEmpty()) {
//            setTimer();
            messageProgess = "Authenticating Quodeck, Please wait..";
            view.showProgress(messageProgess);
            String SignInUrl = quodeckCredential[0] + "/auth/sign_in";
            headerMapListAuth = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", quodeckCredential[1]);
                jsonObject.put("password", quodeckCredential[2]);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = null;
                    HttpsURLConnection connection = null;
                    try {
                        URL url = new URL(SignInUrl);
                        connection = (HttpsURLConnection) url.openConnection();
                        connection.setReadTimeout(30000);
                        connection.setConnectTimeout(30000);
                        connection.addRequestProperty("Content-Type", "application/json");
                        connection.setRequestProperty("Accept", "application/json");
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        OutputStream os = connection.getOutputStream();
                        os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                        os.close();
                        int responseCode = connection.getResponseCode();
                        if (responseCode == 201 || responseCode == 200) {
                            headerMapListAuth.put("client", connection.getHeaderField("client"));
                            headerMapListAuth.put("access-token", connection.getHeaderField("access-token"));
                            headerMapListAuth.put("expiry", connection.getHeaderField("expiry"));
                            headerMapListAuth.put("expires", connection.getHeaderField("expires"));
                            headerMapListAuth.put("uid", connection.getHeaderField("uid"));
                            headerMapListAuth.put("token-type", connection.getHeaderField("token-type"));
                            InputStream stream = connection.getInputStream();
                            String response = DeviceIDUtils.readResponse(stream);
                            JSONObject jsonObject1 = new JSONObject(response);
                            String id = jsonObject1.getString("_id");
                            createEnroll(UID, firstName, lastName, regionName, AWcode);
                            LogManager.writeLogInfo("SignIn API Successfull");
                        } else if (responseCode == 422) {
                            cancelTimer();
                            view.hideProgress();
                            SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.PREFS_NAME, 0).edit();
                            editor.putBoolean(Constants.QUODECK_ENROLL, true);
                            editor.apply();
                            Toast.makeText(context, MSFAApplication.SP_UID + " Already enrolled", Toast.LENGTH_SHORT).show();
                            LogManager.writeLogInfo(MSFAApplication.SP_UID + " Already enrolled");
                        } else {
                            cancelTimer();
                            view.hideProgress();
                            try {
                                InputStream stream = connection.getErrorStream();
                                String response = DeviceIDUtils.readResponse(stream);
                                LogManager.writeLogInfo(response);
                            } catch (Throwable e) {
                                LogManager.writeLogInfo(e.toString());
                            }
                        }
                    } catch (Exception var14) {
                        view.hideProgress();
                        LogManager.writeLogInfo(var14.toString());
                    } finally {
                        isCourse=false;
                        if (connection != null)
                            connection.disconnect();
                    }
                }
            }).start();
        }else{
            view.hideProgress();
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.ProgressDialogTheme);
            builder.setMessage("Quodeck credentials not maintained in backend. Please contact support team");
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    public void createEnroll(String UID,String firstName,String lastName,String regionName,String AWcode){
        boolean is422 = activity.getSharedPreferences(Constants.PREFS_NAME,0).getBoolean(Constants.QUODECK_ENROLL,false);
        if (!is422) {
            messageProgess= "Enrolling courses, Please wait..";
            String enrollURL =quodeckCredential[0]+"/users/createnroll";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", UID);
                jsonObject.put("password", "password");
                jsonObject.put("first_name", firstName);
                if (lastName!=null&!TextUtils.isEmpty(lastName)) {
                    jsonObject.put("last_name", lastName);
                }else{
                    jsonObject.put("last_name", UID);
                }
                jsonObject.put("email", UID+"@gmail.com");
                JSONObject detalsObject = new JSONObject();
                detalsObject.put("company","Britannia");
                detalsObject.put("Regionname",regionName);
                detalsObject.put("AWcode",AWcode);
                jsonObject.put("details", detalsObject);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = null;
                    HttpsURLConnection connection = null;
                    try {
                        URL url =new URL(enrollURL);
                        connection = (HttpsURLConnection) url.openConnection();
                        connection.setReadTimeout(30000);
                        connection.setConnectTimeout(30000);
                        connection.addRequestProperty("Content-Type", "application/json");
                        connection.setRequestProperty("Accept", "application/json");

                        connection.addRequestProperty("client", headerMapListAuth.get("client"));
                        connection.addRequestProperty("access-token", headerMapListAuth.get("access-token"));
                        connection.addRequestProperty("expiry", headerMapListAuth.get("expiry"));
                        connection.addRequestProperty("expires", headerMapListAuth.get("expires"));
                        connection.addRequestProperty("uid", headerMapListAuth.get("uid"));
                        connection.addRequestProperty("token-type", headerMapListAuth.get("token-type"));
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        OutputStream os = connection.getOutputStream();
                        os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                        os.close();
                        int responseCode = connection.getResponseCode();
                        if (responseCode == 201||responseCode == 200) {
                            try {
                                InputStream stream = connection.getInputStream();
                                String response = DeviceIDUtils.readResponse(stream);
                                JSONObject jsonObject1 = new JSONObject(response);
                            */
/*JSONArray jsonArray = jsonObject.getJSONArray("courses");
                            String courses = jsonArray.toString();*//*

                                SharedPreferences.Editor editor =activity.getSharedPreferences(Constants.PREFS_NAME,0).edit();
                                editor.putBoolean(Constants.QUODECK_ENROLL, true);
                                if (jsonObject1.has("_id")) {
                                    editor.putString(Constants.QUODECK_ENROLL_ID, jsonObject1.getString("_id"));
                                }
                            */
/*if (courses!=null&&!TextUtils.isEmpty(courses)) {
                                editor.putString(Constants.QUODECK_ENROLL_COURSES, courses);
                            }*//*

                                editor.apply();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            messageProgess= "Fetching courses, Please wait..";
                            getUserDetails(UID,"password");
                            LogManager.writeLogInfo(MSFAApplication.SP_UID+" enrolled");
                        }else if(responseCode==422){
                            messageProgess= "Fetching courses, Please wait..";
                            getUserDetails(UID,"password");
                            SharedPreferences.Editor editor =activity.getSharedPreferences(Constants.PREFS_NAME,0).edit();
                            editor.putBoolean(Constants.QUODECK_ENROLL, true);
                            editor.apply();
                            LogManager.writeLogInfo(MSFAApplication.SP_UID+" Already enrolled");
                        }else {
                            try {
                                InputStream stream = connection.getErrorStream();
                                String response = DeviceIDUtils.readResponse(stream);
                                LogManager.writeLogInfo(response);
                            } catch (Throwable e) {
                                LogManager.writeLogInfo(e.toString());
                            }
                        }
                    } catch (Exception var14) {
                        LogManager.writeLogInfo(var14.toString());
                    } finally {
                        isCourse=false;
                        if (connection!=null)
                            connection.disconnect();
                    }
                }
            }).start();
        }else{
            messageProgess= "Fetching courses, Please wait..";
            getUserDetails(UID,"password");
        }
    }

    */
/**
     * User Details can be fetched from Sign In API. ID, Course details can be fetched from below
     *
     * Coursed which are enrolled not completed will be listed. Completed Courses need to call another API (Ref: )
     * method post
     *//*


    private HashMap<String,String>headerMapList;
    public void getUserDetails(String Username,String password){
        String SignInUrl =quodeckCredential[0]+"/auth/sign_in";
        headerMapList= new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", Username);
            jsonObject.put("password", password);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                HttpsURLConnection connection = null;
                try {
                    URL url =new URL(SignInUrl);
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setReadTimeout(30000);
                    connection.setConnectTimeout(30000);
                    connection.addRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    OutputStream os = connection.getOutputStream();
                    os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                    os.close();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 201||responseCode == 200) {
                        headerMapList.put("client",connection.getHeaderField("client"));
                        headerMapList.put("access-token",connection.getHeaderField("access-token"));
                        headerMapList.put("expiry",connection.getHeaderField("expiry"));
                        headerMapList.put("expires",connection.getHeaderField("expires"));
                        headerMapList.put("uid",connection.getHeaderField("uid"));
                        headerMapList.put("token-type",connection.getHeaderField("token-type"));
                        InputStream stream = connection.getInputStream();
                        String response = DeviceIDUtils.readResponse(stream);
                        JSONObject jsonObject1 = new JSONObject(response);
                        String id = jsonObject1.getString("_id");
                        getCoursess(id,headerMapList);
                        LogManager.writeLogInfo("SignIn API Successfull");
                    }else if(responseCode==422){
                        cancelTimer();
                        view.hideProgress();
                        SharedPreferences.Editor editor =activity.getSharedPreferences(Constants.PREFS_NAME,0).edit();
                        editor.putBoolean(Constants.QUODECK_ENROLL, true);
                        editor.apply();
                        Toast.makeText(context, MSFAApplication.SP_UID+" Already enrolled", Toast.LENGTH_SHORT).show();
                        LogManager.writeLogInfo(MSFAApplication.SP_UID+" Already enrolled");
                    }else {
                        cancelTimer();
                        view.hideProgress();
                        try {
                            InputStream stream = connection.getErrorStream();
                            String response = DeviceIDUtils.readResponse(stream);
                            LogManager.writeLogInfo(response);
                        } catch (Throwable e) {
                            LogManager.writeLogInfo(e.toString());
                        }
                    }
                } catch (Exception var14) {
                    view.hideProgress();
                    LogManager.writeLogInfo(var14.toString());
                } finally {
                    isCourse=false;
                    if (connection!=null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    public void getCoursess(String id,HashMap<String,String>headerList){
        String courseURL = quodeckCredential[0]+"/analytics/"+id+"/courses/all";
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                HttpsURLConnection connection = null;
                try {
                    URL url =new URL(courseURL);
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setReadTimeout(30000);
                    connection.setConnectTimeout(30000);
                    connection.addRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");

                    connection.addRequestProperty("client", headerList.get("client"));
                    connection.addRequestProperty("access-token", headerList.get("access-token"));
                    connection.addRequestProperty("expiry", headerList.get("expiry"));
                    connection.addRequestProperty("expires", headerList.get("expires"));
                    connection.addRequestProperty("uid", headerList.get("uid"));
                    connection.addRequestProperty("token-type", headerList.get("token-type"));

                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    connection.setDefaultUseCaches(false);
                    connection.setAllowUserInteraction(false);
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 201||responseCode == 200) {
                        InputStream stream = connection.getInputStream();
                        String response = DeviceIDUtils.readResponse(stream);
                        JSONArray jsonArray = new JSONArray(response);
                        try {
                            SharedPreferences.Editor editor =activity.getSharedPreferences(Constants.PREFS_NAME,0).edit();
                            editor.putString(Constants.QUODECK_COURSES, jsonArray.toString());
                            editor.apply();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        if (courseListener!=null){
                            courseListener.onResponse(false,jsonArray);
                        }
                        cancelTimer();
                        LogManager.writeLogInfo("Courses API fetched Successfully");
                    }else if(responseCode==422){
                        cancelTimer();
                        if (courseListener!=null){
                            courseListener.onResponse(true,null);
                        }
                        SharedPreferences.Editor editor =activity.getSharedPreferences(Constants.PREFS_NAME,0).edit();
                        editor.putBoolean(Constants.QUODECK_ENROLL, true);
                        editor.apply();
                        Toast.makeText(context, MSFAApplication.SP_UID+" Already enrolled", Toast.LENGTH_SHORT).show();
                        LogManager.writeLogInfo(MSFAApplication.SP_UID+" Already enrolled");
                    }else {
                        if (courseListener!=null){
                            courseListener.onResponse(true,null);
                        }
                        try {
                            InputStream stream = connection.getErrorStream();
                            String response = DeviceIDUtils.readResponse(stream);
                            LogManager.writeLogInfo(response);
                        } catch (Throwable e) {
                            LogManager.writeLogInfo(e.toString());
                        }
                    }
                } catch (Exception var14) {
                    LogManager.writeLogInfo(var14.toString());
                } finally {
                    if (connection!=null)
                        connection.disconnect();
                }
            }
        }).start();
    }
    public void getCompletedCourses(){

    }
    public void getPendingCourses(){

    }

    public interface CourseListener{
        void onResponse(boolean isError, JSONArray jsonArray);
    }

    //-----------------------------------------------------Billing for VAN -----------------------------
    public static void getOrderBrowserList(Context context) {
        if (sssOsHeaderBeansList==null){
            sssOsHeaderBeansList = new ArrayList();
        }else{
            sssOsHeaderBeansList.clear();
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String sssoListQry = Constants.SSSOs + "?$select=SSSOGuid,BeatGuid,SoldToCPGUID,SoldToId,OrderDate,SoldToDesc,NetPrice,OrderNo,Status,BeatName,CashDiscPercAmt,CashDiscountPerc,CashDiscount,Tax &$filter=" + Constants.OrderDate + " ge datetime'" + Constants.getLast30Days() + "' &$orderby=OrderDate desc";
                SSSOsHeaderBean sssOsHeaderBean = new SSSOsHeaderBean();
                sssOsHeaderBeansList = (ArrayList) new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(sssOsHeaderBean)
                        .setODataEntitySet(Constants.SSSOs)
                        .setODataEntityType(Constants.SSSOEntity)
                        .setQuery(sssoListQry)
                        .returnODataList(OfflineManager.offlineStore);
                try {
                    Collections.sort(sssOsHeaderBeansList, (Comparator<SSSOsHeaderBean>) (one, other) -> -Integer.valueOf(one.getOrderNo()).compareTo(Integer.valueOf(other.getOrderNo())));
                    boolean isCheck = false;
                    for (Object bean : sssOsHeaderBeansList) {
                        SSSOsHeaderBean osHeaderBean = (SSSOsHeaderBean) bean;
                        isCheck = OfflineManager.getSSSOGuidFromDataVault(context, osHeaderBean.getSSSOGuid());
                        if (isCheck) {
                            osHeaderBean.setStatus("000003");
                        }
                    }
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void getBillBrowserList(Context context) {
        try {
            if (ssInvoicesList == null){
                ssInvoicesList = new ArrayList();
            }else{
                ssInvoicesList.clear();
            }
            if (ssInvoiceTypeList==null){
                ssInvoiceTypeList = new ArrayList<>();
            }else{
                ssInvoiceTypeList.clear();
            }
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String qrySSInvoiceTypes = Constants.SSInvoiceTypes + "?$select=InvoiceTypeID,GoodsIssueFromID &$filter=" + Constants.GoodsIssueFromID + " eq '000002' and GoodsIssueCatID eq '000002'";
                        SSInvoiceTypesBean invoiceTypesBean = new SSInvoiceTypesBean();
                        ssInvoiceTypeList = (ArrayList) new OfflineUtils.ODataOfflineBuilder<>().
                                setHeaderPayloadObject(invoiceTypesBean).
                                setODataEntitySet(Constants.SSInvoiceTypes).
                                setODataEntityType(Constants.SSInvoiceTypeEntity).
                                setQuery(qrySSInvoiceTypes).
                                returnODataList(OfflineManager.offlineStore);
                        String qrySSInvoices = Constants.SSINVOICES +"?$select=SoldToName,StatusID,StatusDesc,InvoiceNo,NetAmount,InvoiceDate,SoldToCPGUID,InvoiceGUID,BeatDesc,CPName,CashDiscAmount,Tax,Tax1,Tax2,CashDiscPerc,CashDiscPercAmt,BeatGUID &$filter=" + Constants.InvoiceTypeID + " ne '" + ssInvoiceTypeList.get(0).getInvoiceTypeID() + "' and " + Constants.InvoiceDate + " ge datetime'" + Constants.getLast30Days()+ "' &$orderby=InvoiceNo asc";
                        SSInvoicesBean invoicesBean = new SSInvoicesBean();
                        ssInvoicesList = (ArrayList) new OfflineUtils.ODataOfflineBuilder<>().
                                setHeaderPayloadObject(invoicesBean).
                                setODataEntitySet(Constants.SSINVOICES).
                                setODataEntityType(Constants.SSINVOICESEntity).
                                setQuery(qrySSInvoices).
                                returnODataList(OfflineManager.offlineStore);
                        try {
                            Collections.sort(ssInvoicesList, (Comparator<SSInvoicesBean>) (one, other) -> -Long.valueOf(one.getInvoiceNo().toUpperCase()).compareTo(Long.valueOf(other.getInvoiceNo().toUpperCase())));

                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void getSlipBeatFlag() {
        new Thread(() -> {
            LogManager.writeLogDebug("Distributor Selection Presenter get slip beat flag");
            String qry = Constants.CPPerformances+"?$filter=SPGUID eq guid'"+MSFAApplication.SPGUID+"' and CPGUID eq ''";
            LogManager.writeLogDebug("Distributor Selection Presenter get slip beat flag qry : "+qry);
            List list = new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new CPPerformanceBean())
                    .setODataEntitySet(Constants.CPPerformances)
                    .setODataEntityType(Constants.CPPerformanceEntity)
                    .setQuery(qry)
                    .returnODataList(OfflineManager.offlineStore);
            LogManager.writeLogDebug("Distributor Selection Presenter get slip beat flag size : "+list.size());
            if (list!=null&&!list.isEmpty()){
                CPPerformanceBean bean = (CPPerformanceBean) list.get(0);
                try {
                    if (bean.getString1()!=null&&!TextUtils.isEmpty(bean.getString1())) {
                        LogManager.writeLogDebug("Distributor Selection Presenter get slip beat flag string not empty");
                        JSONObject object = new JSONObject(bean.getString1());
                        try {
                            JSONObject jsonOther = object.getJSONObject("others");
                            if(jsonOther.has("allMatFlag")){
                                LogManager.writeLogDebug("Distributor Selection Presenter get slip beat flag value : "+jsonOther.optString("allMatFlag"));
                                MSFAApplication.SLIPT_BEAT_FLAG = jsonOther.optString("allMatFlag");
                            }else {
                                MSFAApplication.SLIPT_BEAT_FLAG = "";
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }else{
                        LogManager.writeLogDebug("Distributor Selection Presenter get slip beat flag string empty");
                    }
                } catch (Throwable e) {
                    LogManager.writeLogDebug("Distributor Selection Presenter get slip beat flag error : "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean setOuletBeansValue(com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean retailerSelectionBean){
        com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean retailerSelectionBean1 = new RetailerSelectionBean();
        retailerSelectionBean1.setAccountGrp(retailerSelectionBean.getAccountGrp());
        retailerSelectionBean1.setActivationDate(retailerSelectionBean.getActivationDate());
        retailerSelectionBean1.setURL6(retailerSelectionBean.getURL6());
        retailerSelectionBean1.setAddress1(retailerSelectionBean.getAddress1());
        retailerSelectionBean1.setAddress2(retailerSelectionBean.getAddress2());
        retailerSelectionBean1.setAddress3(retailerSelectionBean.getAddress3());
        retailerSelectionBean1.setAddress4(retailerSelectionBean.getAddress4());
        retailerSelectionBean1.setAnniversary(retailerSelectionBean.getAnniversary());
        retailerSelectionBean1.setApprovedAt(retailerSelectionBean.getApprovedAt());
        retailerSelectionBean1.setApprovedBy(retailerSelectionBean.getApprovedBy());
        retailerSelectionBean1.setApprovedOn(retailerSelectionBean.getApprovedOn());
        retailerSelectionBean1.setApprvlStatusDesc(retailerSelectionBean.getApprvlStatusDesc());
        retailerSelectionBean1.setApprvlStatusID(retailerSelectionBean.getApprvlStatusID());
        retailerSelectionBean1.setBlockID(retailerSelectionBean.getBlockID());
        retailerSelectionBean1.setBlockName(retailerSelectionBean.getBlockName());
        retailerSelectionBean1.setBuiltupArea(retailerSelectionBean.getBuiltupArea());
        retailerSelectionBean1.setBuiltupAreaUom(retailerSelectionBean.getBuiltupAreaUom());
        retailerSelectionBean1.setBusinessID1(retailerSelectionBean.getBusinessID1());
        retailerSelectionBean1.setBusinessID2(retailerSelectionBean.getBusinessID2());
        retailerSelectionBean1.setCPGUID(retailerSelectionBean.getCPGUID());
        retailerSelectionBean1.setCPNo(retailerSelectionBean.getCPNo());
        retailerSelectionBean1.setCPStock(retailerSelectionBean.getCPStock());
        retailerSelectionBean1.setCPTypeDesc(retailerSelectionBean.getCPTypeDesc());
        retailerSelectionBean1.setCPTypeID(retailerSelectionBean.getCPTypeID());
        retailerSelectionBean1.setCPUID(retailerSelectionBean.getCPUID());
        retailerSelectionBean1.setChangedAt(retailerSelectionBean.getChangedAt());
        retailerSelectionBean1.setChangedBy(retailerSelectionBean.getChangedBy());
        retailerSelectionBean1.setChangedOn(retailerSelectionBean.getChangedOn());
        retailerSelectionBean1.setCityDesc(retailerSelectionBean.getCityDesc());
        retailerSelectionBean1.setCityID(retailerSelectionBean.getCityID());
        retailerSelectionBean1.setClosingTime(retailerSelectionBean.getClosingTime());
        retailerSelectionBean1.setConstructionStageDs(retailerSelectionBean.getConstructionStageDs());
        retailerSelectionBean1.setConstructionStageID(retailerSelectionBean.getConstructionStageID());
        retailerSelectionBean1.setConstructionType(retailerSelectionBean.getConstructionType());
        retailerSelectionBean1.setConstructionTypeDs(retailerSelectionBean.getConstructionTypeDs());
        retailerSelectionBean1.setContPerson(retailerSelectionBean.getContPerson());
        retailerSelectionBean1.setContPersonEmail(retailerSelectionBean.getContPersonEmail());
        retailerSelectionBean1.setContPersonMobile(retailerSelectionBean.getContPersonMobile());
        retailerSelectionBean1.setCountry(retailerSelectionBean.getCountry());
        retailerSelectionBean1.setCountryName(retailerSelectionBean.getCountryName());
        retailerSelectionBean1.setCreatedAt(retailerSelectionBean.getCreatedAt());
        retailerSelectionBean1.setCreatedBy(retailerSelectionBean.getCreatedBy());
        retailerSelectionBean1.setCreatedOn(retailerSelectionBean.getCreatedOn());
        retailerSelectionBean1.setCreditDays(retailerSelectionBean.getCreditDays());
        retailerSelectionBean1.setCreditLimit(retailerSelectionBean.getCreditLimit());
        retailerSelectionBean1.setCurrency(retailerSelectionBean.getCurrency());
        retailerSelectionBean1.setCurrentPotentialAmt(retailerSelectionBean.getCurrentPotentialAmt());
        retailerSelectionBean1.setCurrentPotentialQty(retailerSelectionBean.getCurrentPotentialQty());
        retailerSelectionBean1.setDOB(retailerSelectionBean.getDOB());
        retailerSelectionBean1.setDeactivatedOn(retailerSelectionBean.getDeactivatedOn());
        retailerSelectionBean1.setDeactivationDate(retailerSelectionBean.getDeactivationDate());
        retailerSelectionBean1.setDestinationDesc(retailerSelectionBean.getDestinationDesc());
        retailerSelectionBean1.setDestinationID(retailerSelectionBean.getDestinationID());
        retailerSelectionBean1.setDistCpGuid(retailerSelectionBean.getDistCpGuid());
        retailerSelectionBean1.setDistCpUid(retailerSelectionBean.getDistCpUid());
        retailerSelectionBean1.setDistrictDesc(retailerSelectionBean.getDistrictDesc());
        retailerSelectionBean1.setDistrictID(retailerSelectionBean.getDistrictID());
        retailerSelectionBean1.setEmailID(retailerSelectionBean.getEmailID());
        retailerSelectionBean1.setEmailID2(retailerSelectionBean.getEmailID2());
        retailerSelectionBean1.setFaxNo(retailerSelectionBean.getFaxNo());
        retailerSelectionBean1.setGeo1Desc(retailerSelectionBean.getGeo1Desc());
        retailerSelectionBean1.setGeo1ID(retailerSelectionBean.getGeo1ID());
        retailerSelectionBean1.setGeo2ID(retailerSelectionBean.getGeo2ID());
        retailerSelectionBean1.setGeo2Desc(retailerSelectionBean.getGeo2Desc());
        retailerSelectionBean1.setGeo3ID(retailerSelectionBean.getGeo3ID());
        retailerSelectionBean1.setGeo3Desc(retailerSelectionBean.getGeo3Desc());
        retailerSelectionBean1.setGeo3ID(retailerSelectionBean.getGeo3ID());
        retailerSelectionBean1.setGeo3Desc(retailerSelectionBean.getGeo3Desc());
        retailerSelectionBean1.setGeo4Desc(retailerSelectionBean.getGeo4Desc());
        retailerSelectionBean1.setGeo4ID(retailerSelectionBean.getGeo4ID());
        retailerSelectionBean1.setProductType(retailerSelectionBean.getProductType());
        retailerSelectionBean1.setGroup10(retailerSelectionBean.getGroup10());
        retailerSelectionBean1.setGroup10Desc(retailerSelectionBean.getGroup10Desc());
        retailerSelectionBean1.setGroup1(retailerSelectionBean.getGroup1());
        retailerSelectionBean1.setGroup1Desc(retailerSelectionBean.getGroup1Desc());
        retailerSelectionBean1.setGroup2(retailerSelectionBean.getGroup2());
        retailerSelectionBean1.setGroup2Desc(retailerSelectionBean.getGroup2Desc());
        retailerSelectionBean1.setGroup3(retailerSelectionBean.getGroup3());
        retailerSelectionBean1.setGroup3Desc(retailerSelectionBean.getGroup3Desc());
        retailerSelectionBean1.setGroup4(retailerSelectionBean.getGroup4());
        retailerSelectionBean1.setGroup9(retailerSelectionBean.getGroup9());
        retailerSelectionBean1.setGroup4Desc(retailerSelectionBean.getGroup4Desc());
        retailerSelectionBean1.setID1(retailerSelectionBean.getID1());
        retailerSelectionBean1.setID2(retailerSelectionBean.getID2());
        retailerSelectionBean1.setIsCompBillAvl(retailerSelectionBean.getIsCompBillAvl());
        retailerSelectionBean1.setIsEduInstNrby(retailerSelectionBean.getIsEduInstNrby());
        retailerSelectionBean1.setIsHomedlvryAvl(retailerSelectionBean.getIsHomedlvryAvl());
        retailerSelectionBean1.setIsHsptlNearBy(retailerSelectionBean.getIsHsptlNearBy());
        retailerSelectionBean1.setIsKeyCP(retailerSelectionBean.getIsKeyCP());
        retailerSelectionBean1.setIsPhOrderAvl(retailerSelectionBean.getIsPhOrderAvl());
        retailerSelectionBean1.setIsSmartPhAvl(retailerSelectionBean.getIsSmartPhAvl());
        retailerSelectionBean1.setLandline(retailerSelectionBean.getLandline());
        retailerSelectionBean1.setLandline2(retailerSelectionBean.getLandline2());
        retailerSelectionBean1.setLandmark(retailerSelectionBean.getLandmark());
        retailerSelectionBean1.setLatitude(retailerSelectionBean.getLatitude());
        retailerSelectionBean1.setLoginID(retailerSelectionBean.getLoginID());
        retailerSelectionBean1.setLongitude(retailerSelectionBean.getLongitude());
        retailerSelectionBean1.setLunchTime(retailerSelectionBean.getLunchTime());
        retailerSelectionBean1.setMobile1(retailerSelectionBean.getMobile1());
        retailerSelectionBean1.setMobile2(retailerSelectionBean.getMobile2());
        retailerSelectionBean1.setMobile3(retailerSelectionBean.getMobile3());
        retailerSelectionBean1.setMobileVerifed(retailerSelectionBean.getMobileVerifed());
        retailerSelectionBean1.setMonthlySalesAmt(retailerSelectionBean.getMonthlySalesAmt());
        retailerSelectionBean1.setMonthlySalesQty(retailerSelectionBean.getMonthlySalesQty());
        retailerSelectionBean1.setName(retailerSelectionBean.getName());
        retailerSelectionBean1.setNoOfCounters(retailerSelectionBean.getNoOfCounters());
        retailerSelectionBean1.setNoOfEmployee(retailerSelectionBean.getNoOfEmployee());
        retailerSelectionBean1.setNoOfWindowDisp(retailerSelectionBean.getNoOfWindowDisp());
        retailerSelectionBean1.setOpenAdvanceAmt(retailerSelectionBean.getOpenAdvanceAmt());
        retailerSelectionBean1.setOpeningTime(retailerSelectionBean.getOpeningTime());
        retailerSelectionBean1.setOutletLocDesc(retailerSelectionBean.getOutletLocDesc());
        retailerSelectionBean1.setOutletLocId(retailerSelectionBean.getOutletLocId());
        retailerSelectionBean1.setOutletShapeDesc(retailerSelectionBean.getOutletShapeDesc());
        retailerSelectionBean1.setOutletShapeId(retailerSelectionBean.getOutletShapeId());
        retailerSelectionBean1.setOutletSizeDesc(retailerSelectionBean.getOutletSizeDesc());
        retailerSelectionBean1.setOutletSizeId(retailerSelectionBean.getOutletSizeId());
        retailerSelectionBean1.setOwnerName(retailerSelectionBean.getOwnerName());
        retailerSelectionBean1.setPAN(retailerSelectionBean.getPAN());
        retailerSelectionBean1.setParentID(retailerSelectionBean.getParentID());
        retailerSelectionBean1.setParentName(retailerSelectionBean.getParentName());
        retailerSelectionBean1.setParentTypDesc(retailerSelectionBean.getParentTypDesc());
        retailerSelectionBean1.setParentTypeID(retailerSelectionBean.getParentTypeID());
        retailerSelectionBean1.setParentUID(retailerSelectionBean.getParentUID());
        retailerSelectionBean1.setPartnerMgrGUID(retailerSelectionBean.getPartnerMgrGUID());
        retailerSelectionBean1.setPartnerMgrName(retailerSelectionBean.getPartnerMgrName());
        retailerSelectionBean1.setPartnerMgrNo(retailerSelectionBean.getPartnerMgrNo());
        retailerSelectionBean1.setPostalCode(retailerSelectionBean.getPostalCode());
        retailerSelectionBean1.setPotentialCurrency(retailerSelectionBean.getPotentialCurrency());
        retailerSelectionBean1.setPotentialType(retailerSelectionBean.getPotentialType());
        retailerSelectionBean1.setPotentialTypeDesc(retailerSelectionBean.getPotentialTypeDesc());
        retailerSelectionBean1.setPotentialUOM(retailerSelectionBean.getPotentialUOM());
        retailerSelectionBean1.setProspectStatusDs(retailerSelectionBean.getProspectStatusDs());
        retailerSelectionBean1.setProspectStatusID(retailerSelectionBean.getProspectStatusID());
        retailerSelectionBean1.setRouteDesc(retailerSelectionBean.getRouteDesc());
        retailerSelectionBean1.setRouteID(retailerSelectionBean.getRouteID());
        retailerSelectionBean1.setSalesGroupID(retailerSelectionBean.getSalesGroupID());
        retailerSelectionBean1.setSalesGrpDesc(retailerSelectionBean.getSalesGrpDesc());
        retailerSelectionBean1.setSalesOffDesc(retailerSelectionBean.getSalesOffDesc());
        retailerSelectionBean1.setSalesOfficeID(retailerSelectionBean.getSalesOfficeID());
        retailerSelectionBean1.setSearchTerm(retailerSelectionBean.getSearchTerm());
        retailerSelectionBean1.setSource(retailerSelectionBean.getSource());
        retailerSelectionBean1.setStateDesc(retailerSelectionBean.getStateDesc());
        retailerSelectionBean1.setStateID(retailerSelectionBean.getStateID());
        retailerSelectionBean1.setStatusDesc(retailerSelectionBean.getStatusDesc());
        retailerSelectionBean1.setStatusID(retailerSelectionBean.getStatusID());
        retailerSelectionBean1.setSubDistrictDesc(retailerSelectionBean.getSubDistrictDesc());
        retailerSelectionBean1.setSubDistrictGUID(retailerSelectionBean.getSubDistrictGUID());
        retailerSelectionBean1.setSubDistrictID(retailerSelectionBean.getSubDistrictID());
        retailerSelectionBean1.setTIN(retailerSelectionBean.getTIN());
        retailerSelectionBean1.setTax1(retailerSelectionBean.getTax1());
        retailerSelectionBean1.setTax2(retailerSelectionBean.getTax2());
        retailerSelectionBean1.setTax3(retailerSelectionBean.getTax3());
        retailerSelectionBean1.setTax4(retailerSelectionBean.getTax4());
        retailerSelectionBean1.setTaxCategory(retailerSelectionBean.getTaxCategory());
        retailerSelectionBean1.setTaxCategoryDesc(retailerSelectionBean.getTaxCategoryDesc());
        retailerSelectionBean1.setTaxClassification(retailerSelectionBean.getTaxClassification());
        retailerSelectionBean1.setTaxClassificationDesc(retailerSelectionBean.getTaxClassificationDesc());
        retailerSelectionBean1.setTaxRegStatus(retailerSelectionBean.getTaxRegStatus());
        retailerSelectionBean1.setTaxRegStatusDesc(retailerSelectionBean.getTaxRegStatusDesc());
        retailerSelectionBean1.setTestRun(retailerSelectionBean.getTestRun());
        retailerSelectionBean1.setTotalPotentialAmt(retailerSelectionBean.getTotalPotentialAmt());
        retailerSelectionBean1.setTotalPotentialQty(retailerSelectionBean.getTotalPotentialQty());
        retailerSelectionBean1.setTownDesc(retailerSelectionBean.getTownDesc());
        retailerSelectionBean1.setTownID(retailerSelectionBean.getTownID());
        retailerSelectionBean1.setUOM(retailerSelectionBean.getUOM());
        retailerSelectionBean1.setVATNo(retailerSelectionBean.getVATNo());
        retailerSelectionBean1.setWeeklyOff(retailerSelectionBean.getWeeklyOff());
        retailerSelectionBean1.setWeeklyOffDesc(retailerSelectionBean.getWeeklyOffDesc());
        retailerSelectionBean1.setZoneDesc(retailerSelectionBean.getZoneDesc());
        retailerSelectionBean1.setZoneID(retailerSelectionBean.getZoneID());
        retailerSelectionBean1.setVerified(retailerSelectionBean.getVerified());
        retailerSelectionBean1.setGroup8(retailerSelectionBean.getGroup8());
        retailerSelectionBean1.setGroup8Desc(retailerSelectionBean.getGroup8Desc());
        retailerSelectionBean1.setAddDetails(retailerSelectionBean.getAddDetails());
        retailerSelectionBean1.setTimeTaken(retailerSelectionBean.getTimeTaken());
        retailerSelectionBean1.setGeoAccuracy(retailerSelectionBean.getGeoAccuracy());
        retailerSelectionBean1.setURL1(retailerSelectionBean.getURL1());
        retailerSelectionBean1.setURL2(retailerSelectionBean.getURL2());
        retailerSelectionBean1.setURL3(retailerSelectionBean.getURL3());
        retailerSelectionBean1.setURL4(retailerSelectionBean.getURL4());
        retailerSelectionBean1.setURL5(retailerSelectionBean.getURL5());
        retailerSelectionBean1.setURL6(retailerSelectionBean.getURL6());
        retailerSelectionBean1.setTownGuid(retailerSelectionBean.getTownGuid());
        retailerSelectionBean1.setVerificationDat(retailerSelectionBean.getVerificationDat());
        retailerSelectionBean1.setBeatGUID(retailerSelectionBean.getBeatGUID());
        retailerSelectionBean1.setBeatCatID(retailerSelectionBean.getBeatCatID());
        retailerSelectionBean1.setSequenceNo(retailerSelectionBean.getSequenceNo());
        retailerSelectionBean1.setIndexSequenceNo(retailerSelectionBean.getIndexSequenceNo());
        retailerSelectionBean1.setColor(retailerSelectionBean.getColor());
        retailerSelectionBean1.setStatePosition(retailerSelectionBean.getStatePosition());
        retailerSelectionBean1.setOLocked(retailerSelectionBean.isOLocked());
        retailerSelectionBean1.setOrdered(retailerSelectionBean.isOrdered());
        retailerSelectionBean1.setWarned(retailerSelectionBean.isWarned());
        retailerSelectionBean1.setCMGO(retailerSelectionBean.isCMGO());
        retailerSelectionBean1.setSpStockItemBeans(retailerSelectionBean.getSpStockItemBeans());
        return retailerSelectionBean1;
    }

    public static String getSplitBeatCPData(String routeSchGUID,String beatCatID,String cpguid){
        try {
            try {
                ArrayList<RouteProductDetTypeBean> productDetList = new ArrayList<>();
                String routeQry = "";
                String routeSPQry = "";
//                if (!TextUtils.isEmpty(routeSchGUID) && beatCatID != null && TextUtils.equals(beatCatID, "02")) {
                if (MSFAApplication.SLIPT_BEAT_FLAG.equalsIgnoreCase("X")) {
                    return "";
                } else {
                    LogManager.writeLogDebug("BEAT :  splitbeat  flag empty");
                    LogManager.writeLogDebug("BEAT :  fetching splitbeat  data");
                    routeSPQry = Constants.CPProductDetTypes + "?$select=PrdDetGuid &$filter=EntityKey eq 'SPGUID:" + MSFAApplication.SPGUID.replaceAll("-", "").toUpperCase() + ";CPGUID:" + cpguid.replaceAll("-", "").toUpperCase() + "'";
                    List cpProductDetTypesList = new ArrayList();
                    cpProductDetTypesList = new OfflineUtils.ODataOfflineBuilder<>()
                            .setHeaderPayloadObject(new CPProductDetTypeBean())
                            .setODataEntitySet(Constants.CPProductDetTypes)
                            .setODataEntityType(Constants.CPProductDetTypeEntity)
                            .setQuery(routeSPQry)
                            .returnODataList(OfflineManager.offlineStore);

                    if (cpProductDetTypesList.size() > 0) {
                        if (cpProductDetTypesList.get(0) instanceof CPProductDetTypeBean) {
                            CPProductDetTypeBean bean = (CPProductDetTypeBean) cpProductDetTypesList.get(0);
                            String routeProdDetQry = "RouteProductDetTypes?$select=PrdDetType &$filter=PrdDetGUID eq guid'" + bean.getPrdDetGuid() + "'";
                            productDetList = (ArrayList<RouteProductDetTypeBean>) new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new RouteProductDetTypeBean())
                                    .setODataEntitySet(Constants.RouteProductDetTypes)
                                    .setODataEntityType(Constants.RouteProductDetTypeEntity)
                                    .setQuery(routeProdDetQry)
                                    .returnODataList(OfflineManager.offlineStore);
                            if(productDetList.size()>0){
                                return productDetList.get(0).getPrdDetType();
                            }else {
                                return "";
                            }
                        }else {
                            return "";
                        }
                    } else {
                        routeSPQry = Constants.RouteScheduleSPs + "?$select=RouteSchSPGUID &$filter=RouteSchGUID eq guid'" + routeSchGUID + "' and SalesPersonID eq guid'" + MSFAApplication.SPGUID + "'";
                        List SPlist = new ArrayList();
                        SPlist = new OfflineUtils.ODataOfflineBuilder<>()
                                .setHeaderPayloadObject(new RouteScheduleSPBean())
                                .setODataEntitySet(Constants.RouteScheduleSPs)
                                .setODataEntityType(Constants.RouteScheduleSPsEntity)
                                .setQuery(routeSPQry)
                                .returnODataList(OfflineManager.offlineStore);
                        LogManager.writeLogDebug("BEAT :  fetched RouteScheduleSPs  data");
                        if (SPlist != null && !SPlist.isEmpty()) {
                            RouteScheduleSPBean SPbean = (RouteScheduleSPBean) SPlist.get(0);
                            routeQry = Constants.RouteScheduleDetails + "?$select=RouteSchDetailGUID,ProdDetGUID &$filter=RouteSchSPGUID eq guid'" + SPbean.getRouteSchSPGUID() + "' and RouteSchGUID eq guid'" + routeSchGUID + "'";
                            List list = new ArrayList();
                            list = new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new RouteScheduleDetailBean())
                                    .setODataEntitySet(Constants.RouteScheduleDetails)
                                    .setODataEntityType(Constants.RouteScheduleDetailEntity)
                                    .setQuery(routeQry)
                                    .returnODataList(OfflineManager.offlineStore);
                            LogManager.writeLogDebug("BEAT :  fetched RouteScheduleDetails  data");
                            String routeProdDetQry = "";
                            if (list != null && !list.isEmpty()) {
                                if (list.get(0) instanceof RouteScheduleDetailBean) {
                                    RouteScheduleDetailBean bean = (RouteScheduleDetailBean) list.get(0);
                                    routeProdDetQry = "RouteProductDetTypes?$select=PrdDetType &$filter=PrdDetGUID eq guid'" + bean.getProdDetGUID() + "'";
                                    productDetList = (ArrayList<RouteProductDetTypeBean>) new OfflineUtils.ODataOfflineBuilder<>()
                                            .setHeaderPayloadObject(new RouteProductDetTypeBean())
                                            .setODataEntitySet(Constants.RouteProductDetTypes)
                                            .setODataEntityType(Constants.RouteProductDetTypeEntity)
                                            .setQuery(routeProdDetQry)
                                            .returnODataList(OfflineManager.offlineStore);
                                    if(productDetList.size()>0){
                                        return productDetList.get(0).getPrdDetType();
                                    }else {
                                        return "";
                                    }
                                }else {
                                    return "";
                                }
                            }else {
                                return "";
                            }
                        }else {
                            return "";
                        }
                    }
                }
//                } else {
//                    return "";
//                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean checkAttndMarkedForDay() {
        String spguid = Constants.getSPGUID();
        String query = Constants.Attendances + "?$select=AttendanceGUID &$filter=StartDate eq datetime'" + UtilConstants.getNewDate() + "' " + "and SPGUID eq guid'" + spguid + "'";
        List oDataList = new OfflineUtils.ODataOfflineBuilder<>().setHeaderPayloadObject(new AttendanceBean()).setODataEntitySet(Constants.Attendances).setODataEntityType(Constants.ATTENDANCEENTITY).setUIListener(this).setQuery(query).returnODataList(OfflineManager.offlineStore);
        return oDataList != null && !oDataList.isEmpty();
    }

    static UserProfileAuthSetBean cpBean;
    String vehicleNo;

    public void onLoadDialog(Context context, UserProfileAuthSetBean channelPartnerBean, String vanNum) {
        try {
//            cpBean = channelPartnerBean;
            cpBean = channelPartnerBean;
            vehicleNo = vanNum;
            new LoadingData().execute();
        } catch (Exception e) {
            view.hideProgress();
            view.showMessage(context.getString(R.string.no_network_error_message), 0);
            e.printStackTrace();
            ConstantsUtils.printErrorLog("Create Atnd" + e.getMessage());
        }
        //  }
    }

    class LoadingData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);

                onSaveDayStartData(context);
            } catch (InterruptedException e) {
                e.printStackTrace();
                view.hideProgress();
                view.showMessage(e.getMessage(), 0);
//                ((Activity) context).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        new DialogFactory.Progress(context).hide();
//                    }
//                });
//                ConstantsUtils.printErrorLog("Create Atnd" + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }


    */
/*Save Day start data on offline store*//*

    public void onSaveDayStartData(Context context) {
        try {
            Constants.MapEntityVal.clear();
            GUID guid = GUID.newRandom();
            Hashtable hashTableAttendanceValues = new Hashtable();
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            String loginIdVal = sharedPreferences.getString(UtilRegistrationActivity.KEY_username, "");

            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.LOGINID, loginIdVal);
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.AttendanceGUID, guid.toString());
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.StartDate, UtilConstants.getNewDate());
            final Calendar calCurrentTime = Calendar.getInstance();
            int hourOfDay = calCurrentTime.get(Calendar.HOUR_OF_DAY); // 24 hour clock
            int minute = calCurrentTime.get(Calendar.MINUTE);
            int second = calCurrentTime.get(Calendar.SECOND);
            ODataDuration oDataDuration = null;
            try {
                oDataDuration = new ODataDurationDefaultImpl();
                oDataDuration.setHours(hourOfDay);
                oDataDuration.setMinutes(minute);
                oDataDuration.setSeconds(BigDecimal.valueOf(second));
            } catch (Exception e) {
                e.printStackTrace();
            }

            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.StartTime, oDataDuration);
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.StartLat, BigDecimal.valueOf(UtilConstants.round(UtilConstants.latitude, 12)));
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.StartLong, BigDecimal.valueOf(UtilConstants.round(UtilConstants.longitude, 12)));
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.EndLat, "");
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.EndLong, "");
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.EndDate, "");
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.EndTime, "");

            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.Remarks, "");

            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.AttendanceTypeH1, "");
            //noinspection unchecked
            hashTableAttendanceValues.put(Constants.AttendanceTypeH2, "");


            hashTableAttendanceValues.put(Constants.SPGUID, Constants.getSPGUID());
            hashTableAttendanceValues.put(Constants.Source, "MOBILE");

            hashTableAttendanceValues.put(Constants.SetResourcePath, "guid'" + guid.toString() + "'");

            SharedPreferences sharedPreferencesVal = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPreferencesVal.edit();
            editor.putInt(Constants.VisitSeqId, 0);
            editor.commit();
            try {
                OfflineManager.createAttendance(hashTableAttendanceValues, new UIListener() {
                    @Override
                    public void onRequestError(int operation, Exception e) {
//                        ((Activity) context).runOnUiThread(() -> {
//                            new DialogFactory.Progress(context).hide();
//                        });
                        view.hideProgress();
                        String mStrPopUpText = "";
                        com.arteriatech.mutils.log.LogManager.writeLogError("Create Attendance request error : " + e.getMessage());
                        e.printStackTrace();
                        ErrorBean errorBean = Constants.getErrorCode(operation, e, context);
                        if (errorBean.hasNoError()) {
                            try {
                                mStrPopUpText = context.getString(R.string.err_msg_concat, context.getString(R.string.lbl_attence_start), e.getMessage());
                            } catch (Exception er) {
                                er.printStackTrace();
                                mStrPopUpText = context.getString(R.string.msg_start_upd_sync_error);
                            }
                            view.showMessage(mStrPopUpText, 0);
//                            showAlter(mStrPopUpText);
                        } else {
                            Constants.isSync = false;
                            mStrPopUpText = Constants.makeMsgReqError(errorBean.getErrorCode(), context, false);
//                            showAlter(mStrPopUpText);
                            view.showMessage(mStrPopUpText, 0);

                        }
                    }

                    @Override
                    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                        String mStrPopUpText = "";
                        com.arteriatech.mutils.log.LogManager.writeLogInfo("Create Attendance request success, Create Visit.. ");
//                        if (MSFAApplication.isBVAN) {
                        startVisit(cpBean, vehicleNo);
//                        } else {
//                            ((Activity) context).runOnUiThread(() -> {
//                                new DialogFactory.Progress(context).hide();
//                            });
//                        }
                    }
                }, context);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
                view.hideProgress();
                view.showMessage(e.getMessage(), 0);
                ConstantsUtils.printErrorLog("Create Atnd" + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
//            ((Activity) context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    new DialogFactory.Progress(context).hide();
//                }
//            });
            view.hideProgress();
            view.showMessage(e.getMessage(), 0);
            ConstantsUtils.printErrorLog("Create Atnd" + e.getMessage());
        }
    }
    private static String mStrPopUpText = "";
    static ArrayList<String> alFlushColl = new ArrayList<>();
    static ArrayList<String> alAssignColl = new ArrayList<>();
    static boolean isClickable = false;
    private static String concatCollectionStr = "";
    static String message = "";
    private static String concatFlushCollStr = "";
    public void startVisit(UserProfileAuthSetBean channelPartnerBean, String vehicleNo) {
        try {
            LogManager.writeLogInfo("CP Visit start and end method start");
            VisitBean visitBean = new VisitBean();
            String visitGUID = GUID.newRandom().toString36().toUpperCase();
            try {
                visitBean.setVisitGUID(visitGUID);
//                visitBean.setCPGUID(channelPartnerBean.getCPGUID().replaceAll("-", "").toUpperCase());
                visitBean.setCPGUID(channelPartnerBean.getAuthOrgValue());
                visitBean.setSPGUID(Constants.getSPGUID());
                visitBean.setVisitCatID(Constants.str_02);
                visitBean.setStatusID(Constants.B1);
                visitBean.setStatusDesc(Constants.Start_Factory);
                visitBean.setCPTypeID(Constants.str_02);
                visitBean.setStartLat(String.valueOf(UtilConstants.round(UtilConstants.latitude, 12)));
                visitBean.setStartLong(String.valueOf(UtilConstants.round(UtilConstants.longitude, 12)));
                visitBean.setVisitDate(UtilConstants.getNewDate1());
                visitBean.setStartDate(UtilConstants.getNewDate1());
                try {
                    visitBean.setStartTime(""+ConstantsUtils.oDataTimeFormat());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                LogManager.writeLogInfo("CP visit saving data"+visitBean);
                new OfflineUtils.ODataOfflineBuilder().setODataEntitySet(Constants.Visits).setODataEntityType(Constants.VISITENTITY).setUIListener(new UIListener() {
                    @Override
                    public void onRequestError(int operation, Exception e) {
                        com.arteriatech.mutils.log.LogManager.writeLogError("Create Visit request error : " + e.getMessage());
                        e.printStackTrace();
                        ErrorBean errorBean = Constants.getErrorCode(operation, e, context);
                        if (errorBean.hasNoError()) {
                            try {
                                mStrPopUpText = context.getString(R.string.err_msg_concat, context.getString(R.string.lbl_visit_start), e.getMessage());
                            } catch (Exception er) {
                                er.printStackTrace();
                                mStrPopUpText = context.getString(R.string.msg_start_upd_sync_error);
                            }
                            view.hideProgress();
                            view.showMessage(mStrPopUpText, 0);
                        } else if (errorBean.isStoreFailed()) {
                            if (UtilConstants.isNetworkAvailable(context)) {
                                Constants.isSync = true;
//                                new RefreshAsyncTask(RegistrationActivity.this, concatCollectionStr, this).execute();
                            } else {
                                Constants.isSync = false;
                                mStrPopUpText = Constants.makeMsgReqError(errorBean.getErrorCode(), context, false);
                            }
                            view.hideProgress();
                            view.showMessage(mStrPopUpText, 0);
                        } else {
                            Constants.isSync = false;
                            mStrPopUpText = Constants.makeMsgReqError(errorBean.getErrorCode(), context, false);
                            view.hideProgress();
                            view.showMessage(mStrPopUpText, 0);
                        }
                    }

                    @Override
                    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                        com.arteriatech.mutils.log.LogManager.writeLogInfo("Create Visit request success ");
                        if (operation == Operation.Create.getValue()) {
                            if (MSFAApplication.isVAN) {
                                if (UtilConstants.isNetworkAvailable(context)) {
                                    alFlushColl = Constants.getPendingList();
                                    alAssignColl = Constants.getRefreshList();
                                    concatFlushCollStr = Constants.getConcatinatinFlushCollectios(alFlushColl);
                                    try {
                                        OfflineManager.flushQueuedRequests(this, concatFlushCollStr);
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    mStrPopUpText = context.getString(R.string.no_network_conn);
                                    view.hideProgress();
                                    view.showMessage(mStrPopUpText, 0);
                                }
                            } else if (MSFAApplication.isBVAN) {
                                if (UtilConstants.isNetworkAvailable(context)) {
                                */
/*    alFlushColl = Constants.getPendingList();
                                    alAssignColl = Constants.getRefreshList();
                                    concatFlushCollStr = Constants.getConcatinatinFlushCollectios(alFlushColl);*//*

                                    try {
//                                        OfflineManager.flushQueuedRequests(this, concatFlushCollStr);
                                        postVisitActivity(visitGUID, vehicleNo);
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                        view.hideProgress();
                                        view.showMessage(e.getMessage(), 0);
                                    }
                                    LogManager.writeLogInfo(" Flush Visit started");
                                } else {
                                    mStrPopUpText = context.getString(R.string.no_network_conn);
                                    view.hideProgress();
                                    view.showMessage(mStrPopUpText, 0);
                                    LogManager.writeLogDebug(mStrPopUpText);
                                }
                            } else {
                                message = "Face recognized. Your visit has been captured successfully. Please ensure 22 bill cuts today";
                                mStrPopUpText = message;
                                String finalMessage = message;
                                String finalMessage1 = message;
                                view.hideProgress();
                                view.showMessage(mStrPopUpText, 0);
                            }
                        } else if (operation == Operation.OfflineFlush.getValue()) {
                            try {
                                concatCollectionStr = Constants.getConcatinatinFlushCollectios(alAssignColl);
                                new RefreshAsyncTask(context, concatCollectionStr, this).execute();
                                LogManager.writeLogInfo(" Flush Visit completed, Refresh started");
                            } catch (Exception e) {
                                e.printStackTrace();
                                view.hideProgress();
                                view.showMessage(e.getMessage(), 0);
                                TraceLog.e(Constants.SyncOnRequestSuccess, e);
                            }
                        } else if (operation == Operation.OfflineRefresh.getValue()) {
//                            mStrPopUpText = "Factory visit started successfully";
//                            view.hideProgress();
//                            view.showMessage(mStrPopUpText, 0);
                            LogManager.writeLogInfo("Refresh completed, Factory Visit started");
                        } else if (operation == Operation.GetStoreOpen.getValue() && OfflineManager.isOfflineStoreOpen()) {
                            Constants.isSync = false;
                            try {
                                OfflineManager.getAuthorizations(context);
                            } catch (OfflineODataStoreException e) {
                                e.printStackTrace();
                                view.hideProgress();
                                view.showMessage(e.getMessage(), 0);
                            }
                            Constants.setSyncTime(context);
                            mStrPopUpText = context.getString(R.string.dialog_day_started);
                        }
                    }
                }).setCreate(true).setHeaderPayloadObject(visitBean).build(new ErrorListener() {
                    @Override
                    public void error(String errorAtField, String errorMessage) {
                        mStrPopUpText = errorMessage;
                        LogManager.writeLogInfo("CP visit saving data saving Error at field " + errorAtField.concat(" " + errorMessage));
                        view.hideProgress();
                        view.showMessage(mStrPopUpText, 0);

                    }
                });
            } catch (Throwable e) {
                e.printStackTrace();
                view.hideProgress();
                view.showMessage(e.getLocalizedMessage(), 0);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            view.hideProgress();
            view.showMessage(e.getLocalizedMessage(), 0);
        }
    }

    private void postVisitActivity(String visitGuid, String vehicleNo) {
        try {
            String visitActivityGUID = GUID.newRandom().toString36().toUpperCase();
            LogManager.writeLogInfo("CP Visit Activity start and end method start");
            VisitActivityBean activityBean = new VisitActivityBean();
            try {
                activityBean.setVisitGUID(visitGuid);
                activityBean.setStartTime("" + ConstantsUtils.oDataTimeFormat());
                activityBean.setVisitActivityGUID(visitActivityGUID);
                activityBean.setActivityRefID(visitGuid);
                activityBean.setActivityType(Constants.B1);
                activityBean.setActivityTypeDesc(Constants.Start_Factory);
                activityBean.setLatitude(String.valueOf(UtilConstants.round(UtilConstants.latitude, 12)));
                activityBean.setLongitude(String.valueOf(UtilConstants.round(UtilConstants.longitude, 12)));
                activityBean.setCreatedOn((ConstantsUtils.returnCurrentDate()));
                activityBean.setEndTime(ConstantsUtils.oDataTimeFormat());
                LogManager.writeLogInfo("CP visit Activity saving data");
                new OfflineUtils.ODataOfflineBuilder().setODataEntitySet(Constants.VisitActivities).setODataEntityType(Constants.VISITACTIVITYENTITY).setUIListener(new UIListener() {
                    @Override
                    public void onRequestError(int operation, Exception e) {
                        com.arteriatech.mutils.log.LogManager.writeLogError("Create Visit Activity request error : " + e.getMessage());
                        e.printStackTrace();
                        ErrorBean errorBean = Constants.getErrorCode(operation, e, context);
                        if (errorBean.hasNoError()) {
                            try {
                                mStrPopUpText = context.getString(R.string.err_msg_concat, context.getString(R.string.lbl_visit_start), e.getMessage());
                            } catch (Exception er) {
                                er.printStackTrace();
                                mStrPopUpText = context.getString(R.string.msg_start_upd_sync_error);
                            }
                            view.hideProgress();
                            view.showMessage(mStrPopUpText, 0);
                        } else if (errorBean.isStoreFailed()) {
                            if (UtilConstants.isNetworkAvailable(context)) {
                                Constants.isSync = true;
//                                new RefreshAsyncTask(RegistrationActivity.this, concatCollectionStr, this).execute();
                            } else {
                                Constants.isSync = false;
                                mStrPopUpText = Constants.makeMsgReqError(errorBean.getErrorCode(), context, false);
                            }
                            view.hideProgress();
                            view.showMessage(mStrPopUpText, 0);
                        } else {
                            Constants.isSync = false;
                            mStrPopUpText = Constants.makeMsgReqError(errorBean.getErrorCode(), context, false);
                            view.hideProgress();
                            view.showMessage(mStrPopUpText, 0);
                        }
                    }

                    @Override
                    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                        com.arteriatech.mutils.log.LogManager.writeLogInfo("Create Visit request success ");
                        if (operation == Operation.Create.getValue()) {
                            if (MSFAApplication.isVAN) {
                                if (UtilConstants.isNetworkAvailable(context)) {
                                    alFlushColl = Constants.getPendingList();
                                    alAssignColl = Constants.getRefreshList();
                                    concatFlushCollStr = Constants.getConcatinatinFlushCollectios(alFlushColl);
                                    try {
                                        OfflineManager.flushQueuedRequests(this, concatFlushCollStr);
                                    } catch (OfflineODataStoreException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    mStrPopUpText = context.getString(R.string.no_network_conn);
                                    view.hideProgress();
                                    view.showMessage(mStrPopUpText, 0);
                                }
                            } else if (MSFAApplication.isBVAN) {
                                if (UtilConstants.isNetworkAvailable(context)) {
                                    alFlushColl = Constants.getPendingList();
                                    alAssignColl = Constants.getRefreshList();
                                    concatFlushCollStr = Constants.getConcatinatinFlushCollectios(alFlushColl);
                                    try {
                                        OfflineManager.flushQueuedRequests(this, concatFlushCollStr);
                                    } catch (OfflineODataStoreException e) {
                                        e.printStackTrace();
                                    }
                                    LogManager.writeLogInfo(" Flush Visit started");
                                } else {
                                    mStrPopUpText = context.getString(R.string.no_network_conn);
                                    view.hideProgress();
                                    view.showMessage(mStrPopUpText, 0);
                                    LogManager.writeLogDebug(mStrPopUpText);
                                }
                            } else {
                                message = "Face recognized. Your visit has been captured successfully. Please ensure 22 bill cuts today";
                                mStrPopUpText = message;
                                String finalMessage = message;
                                String finalMessage1 = message;
                                view.hideProgress();
                                view.showMessage(mStrPopUpText, 0);
                            }
                        } else if (operation == Operation.OfflineFlush.getValue()) {
                            try {
                                concatCollectionStr = Constants.getConcatinatinFlushCollectios(alAssignColl);
                                new RefreshAsyncTask(context, concatCollectionStr, this).execute();
                                LogManager.writeLogInfo(" Flush Visit activity completed, Refresh started");
                            } catch (Exception e) {
                                e.printStackTrace();
                                view.hideProgress();
                                view.showMessage(e.getMessage(), 0);
                                TraceLog.e(Constants.SyncOnRequestSuccess, e);
                            }
                        } else if (operation == Operation.OfflineRefresh.getValue()) {
                             */
/*  mStrPopUpText = "Factory visit started successfully";
                            view.hideProgress();
                            view.showMessage(mStrPopUpText, 0);*//*

                            LogManager.writeLogInfo("Refresh completed, Factory Visit started");

                            postVanData(vehicleNo, visitGuid);
                        } else if (operation == Operation.GetStoreOpen.getValue() && OfflineManager.isOfflineStoreOpen()) {
                            Constants.isSync = false;
                            try {
                                OfflineManager.getAuthorizations(context);
                            } catch (OfflineODataStoreException e) {
                                e.printStackTrace();
                                view.hideProgress();
                                view.showMessage(e.getMessage(), 0);
                            }
                            Constants.setSyncTime(context);
                            mStrPopUpText = context.getString(R.string.dialog_day_started);
                        }
                    }
                }).setCreate(true).setHeaderPayloadObject(activityBean).build(new ErrorListener() {
                    @Override
                    public void error(String errorAtField, String errorMessage) {
                        mStrPopUpText = errorMessage;
                        LogManager.writeLogInfo("CP visit saving data saving Error at field " + errorAtField.concat(" " + errorMessage));
                        view.hideProgress();
                        view.showMessage(mStrPopUpText, 0);

                    }
                });
            } catch (Throwable e) {
                e.printStackTrace();
                view.hideProgress();
                view.showMessage(e.getLocalizedMessage(), 0);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            view.hideProgress();
            view.showMessage(e.getLocalizedMessage(), 0);
        }
    }

    private void postVanData(String vehicleNo, String visitGUID) {
        String routePlanKey = GUID.newRandom().toString32().toUpperCase();
        String spguid = Constants.getSPGUID();

        Hashtable hashtable = new Hashtable();
        hashtable.put("RoutePlanKey", routePlanKey);
        hashtable.put("VisitDate", UtilConstants.getNewDateTimeFormat());
        hashtable.put("VisitType", "06");
        hashtable.put("VehicleNo", vehicleNo);
        hashtable.put("VehicleGuid", spguid);
        hashtable.put("VisitGuid", spguid);
        hashtable.put("Source", "MOBILE");
        hashtable.put("VistCategory", "01");

        JSONObject obj = new JSONObject(hashtable);
        OnlineManager.createZCommonEntity(REPEATABLE_REQUEST_ID, obj.toString(), Constants.ZVisitPlans, new UIListener() {
            @Override
            public void onRequestError(int operation, Exception e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.hideProgress();
                        view.showMessage(e.getMessage().toString(), 0);
                    }
                });
            }

            @Override
            public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SharedPreferences sharedPerf = context.getSharedPreferences(Constants.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPerf.edit();
                            editor.putString("VehicleNo", vehicleNo);
                            editor.apply();
                            view.hideProgress();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new DialogFactory.Alert(context).setMessage("Factory visit started successfully!").isAlert(true)
                                            .setTheme(R.style.MyDialogTheme_new).setPositiveButton("OK")
                                            .setOnDialogClick(isPositive -> {
                                                if (isPositive) {

                                                }
                                            })
                                            .show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, context);


    }

    String strRfqCount = "";
    public void getVisitCount() {
        String spguid = Constants.getSPGUID().toLowerCase();
//        String query = Constants.Visits + "?$filter=VisitDate eq datetime'" + UtilConstants.getNewDate() + "' " + "and SPGUID eq guid'" + spguid + "' &$select=StatusID";
        String query = Constants.Visits + "/$count?$filter=StatusID eq 'B8' and VisitDate ge datetime'" + UtilConstants.getNoOfDays(2) + "' " + "and SPGUID eq guid'" + spguid + "'";
        OnlineManager.doOnlineGetRequest(query, context, event -> {
            if (event.getResponseStatusCode() == 200) {
                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                JSONObject jsonObj = null;
                try {
                    String finalInstructionCount = String.valueOf(responseBody);
                    strRfqCount = finalInstructionCount;
                    view.refreshCount(strRfqCount);
                } catch (Exception e) {
                    e.printStackTrace();
                    view.hideProgress();
                    view.showMessage(e.getMessage(), 0);
                }
            } else {
                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                view.hideProgress();
                view.showMessage(responseBody, 0);
            }
        }, iError -> {
            iError.printStackTrace();
            Log.d("OnlineManager", "getUserRollInfo: ");
            String errorMessage = "";
            if (iError.getMessage().contains("No address associated with hostname") || iError.getMessage().contains("Software caused connection") || iError.getMessage().contains("Failed to connect") || iError.getMessage().contains("during system call")) {
                errorMessage = "Unable to fetch user authorization data due to poor internet connectivity[UserProfileAuthSet]. \n\n Please check internet and retry";
            }
            String finalErrorMessage = errorMessage;
            view.hideProgress();
            view.showMessage(finalErrorMessage, 0);
        });
    }

    public void checkVisitStatus() {
        String spguid = Constants.getSPGUID().toLowerCase();
        String query = Constants.Visits + "/$count?$filter=VisitDate ge datetime'" + UtilConstants.getNoOfDays(2) + "' " +
                "and SPGUID eq guid'" + spguid + "' and StatusID ne 'B8'";
        OnlineManager.doOnlineGetRequest(query, context, event -> {
            if (event.getResponseStatusCode() == 200) {
                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                try {
                    String count = String.valueOf(responseBody);
                    if (Integer.parseInt(count) > 0) {
                        //Check out not marked yet for existing visit
                        ArrayList<VisitBean> visitBeans = new ArrayList<>();
                        view.refreshData(visitBeans, 3);
                    } else {
                        view.setVisitStart();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.hideProgress();
                    view.showMessage(e.getLocalizedMessage(), 0);
                }
            } else {
                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                view.hideProgress();
                view.showMessage(responseBody.toString(), 0);
            }
        }, iError -> {
            iError.printStackTrace();
            Log.d("OnlineManager", "getUserRollInfo: ");
            String errorMessage = "";
            if (iError.getMessage().contains("No address associated with hostname") || iError.getMessage().contains("Software caused connection") || iError.getMessage().contains("Failed to connect") || iError.getMessage().contains("during system call")) {
                errorMessage = "Unable to fetch user authorization data due to poor internet connectivity[UserProfileAuthSet]. \n\n Please check internet and retry";
            }
            String finalErrorMessage = errorMessage;
            view.hideProgress();
            view.showMessage(finalErrorMessage, 0);
        });
    }

    public void getVisitData() {
        String spguid = Constants.getSPGUID().toLowerCase();
        String query = Constants.Visits + "?$filter=VisitDate eq datetime'" + UtilConstants.getNewDate() + "' " +
                "and SPGUID eq guid'" + spguid + "' ";
        OnlineManager.doOnlineGetRequest(query, context, event -> {
            if (event.getResponseStatusCode() == 200) {
                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                JSONObject jsonObj = null;
                ArrayList<VisitBean> visitBeanList = new ArrayList<>();
                try {
                    jsonObj = new JSONObject(responseBody);
                    JSONObject objectD = jsonObj.optJSONObject("d");
                    JSONArray jsonArray = objectD.getJSONArray("results");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            try {
                                VisitBean visitBean = new VisitBean();
                                visitBean.setCPName(object.optString(Constants.CPName));
                                visitBean.setCPNo(object.optString(Constants.CPNo));
                                visitBean.setCPGUID(object.optString(Constants.CPGUID));
                                visitBean.setSPName(object.optString(Constants.SPName));
                                visitBeanList.add(visitBean);
                                view.refreshData(visitBeanList, 2);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                view.hideProgress();
                                view.showMessage(responseBody.toString(), 0);
                            }
                        }
                    } else {
                        // VISIT NOT CREATED, START FACTORY VISIT
                        view.refreshCount("1");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.hideProgress();
                    view.showMessage(e.getLocalizedMessage(), 0);
                }
            } else {
                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                view.hideProgress();
                view.showMessage(responseBody.toString(), 0);
            }
        }, iError -> {
            iError.printStackTrace();
            Log.d("OnlineManager", "getUserRollInfo: ");
            String errorMessage = "";
            if (iError.getMessage().contains("No address associated with hostname") || iError.getMessage().contains("Software caused connection") || iError.getMessage().contains("Failed to connect") || iError.getMessage().contains("during system call")) {
                errorMessage = "Unable to fetch user authorization data due to poor internet connectivity[UserProfileAuthSet]. \n\n Please check internet and retry";
            }
            String finalErrorMessage = errorMessage;
            view.hideProgress();
            view.showMessage(finalErrorMessage, 0);
        });
    }

    public static void getVANStockMaterials1(String cpguid) {
        stockList = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String mStrMyStockQry = "";
                String additionalQuery = "";
                if (MSFAApplication.isSDA) {
                    if (distributorBean != null) {
                        if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                        }
                        mStrMyStockQry = Constants.SPStockItems + "?$select=CatalogInfo,SkuGroup,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " + "&$orderby=" + Constants.MaterialDesc + " &$filter=" + Constants.StockOwner + " eq '" + distributorBean.getCPTypeID() + "' " + additionalQuery;
                    }
                } else if (MSFAApplication.isVAN) {
                    if (distributorBean != null) {
                        if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                        }
                        mStrMyStockQry = Constants.SPStockItems + "?$select=CatalogInfo,SkuGroup,HSNCode,AlternativeUOM1,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " + "&$orderby=" + Constants.MaterialDesc;
                    }
                } else if (MSFAApplication.isBVAN) {
                    if (distributorBean != null) {
                        if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            // additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "' and StorageLoc eq '"+cpguid+"'";
                            int desiredLength = 10;
                            // Pad the string with leading zeros
                            String distributorCode = String.format("%0" + desiredLength + "d", Integer.parseInt(cpguid));
                            additionalQuery = "StorageLoc eq '" + distributorCode + "'";
                        }
                        mStrMyStockQry = Constants.SPStockItems + "?$select=CatalogInfo,SkuGroup,HSNCode,AlternativeUOM1,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " + "&$orderby=" + Constants.MaterialDesc +" &$filter=" + additionalQuery;
                    }
                } else {
                    if (distributorBean != null) {
                        if (!TextUtils.isEmpty(distributorBean.getCPGUID()) && !distributorBean.getCPGUID().equalsIgnoreCase(Constants.None)) {
                            additionalQuery = additionalQuery + " and " + Constants.CPGUID + " eq '" + distributorBean.getCPGUID() + "'";
                        }
                        mStrMyStockQry = Constants.SPStockItems + "?$select=CatalogInfo,SkuGroup,HSNCode,AlternativeUOM2Den,SPStockItemGUID,ManufacturingDate,UnrestrictedQty,MaterialNo,MaterialDesc,AlternativeUOM1Num,StockValue,Currency,LandingPrice,OrderMaterialGroupID,OrderMaterialGroupDesc,Batch,UOM,Brand " + "&$orderby=" + Constants.MaterialDesc + " &$filter=" + Constants.StockOwner + " eq '" + distributorBean.getCPTypeID() + "' " + additionalQuery;
                    }
                }
                String qty = "";
                String calculatedValue = "";
                String removedZerosValue = "";
                int number = 0;
                List materialList = new OfflineUtils.ODataOfflineBuilder<>().setHeaderPayloadObject(new SPStockItemBean()).setODataEntitySet(Constants.SPStockItems).setODataEntityType(Constants.SPStockItemsEntity).setQuery(mStrMyStockQry).returnODataList(OfflineManager.offlineStore);

                if (!materialList.isEmpty()) {
                    for (SPStockItemBean bean : (List<SPStockItemBean>) materialList) {
                        ArrayList<SPStockItemSNoBean> itemSNOList = new ArrayList<>();
//                        SPStockItemSNoBean snoBean = null;
                        try {
                            String date = "";
                            if (MSFAApplication.STKEXPUPTO != null && !TextUtils.isEmpty(MSFAApplication.STKEXPUPTO) && !MSFAApplication.STKEXPUPTO.equalsIgnoreCase("0")) {
                                try {
                                    date = ConstantsUtils.getCPConfigDate(Integer.parseInt(MSFAApplication.STKEXPUPTO));
                                } catch (Throwable e) {
                                    date = UtilConstants.getNewDate();
                                }
                            } else {
                                date = UtilConstants.getNewDate();
                            }
                            String CPStockSnosQry = Constants.SPStockItemSNos + "?$select=Quantity,IntrmUnitPrice,AlternateUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,Tax3,Tax4,Tax5,Tax6,Tax7,Tax8,Tax9,Tax10,Batch,MFD,ExpiryDate,MaterialNo,MaterialDesc " + "&$filter=(StockTypeID eq '1' and SPStockItemGUID eq guid'" + bean.getSPStockItemGUID().toUpperCase() + "') and (ExpiryDate ge datetime'" + date + "' or ExpiryDate eq null) &$orderby=MFD desc ";

//                            snoBean = OfflineManager.getVANMRP(CPStockSnosQry);
                            itemSNOList.addAll(OfflineManager.getVANMRPStockList(CPStockSnosQry));
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        for (SPStockItemSNoBean snoBean : itemSNOList) {
                            SPStockItemBean beanTemp = new SPStockItemBean();
                            beanTemp.setCatalogInfo(bean.getCatalogInfo());
                            beanTemp.setHSNCode(bean.getHSNCode());
                            beanTemp.setProdHier7(bean.getProdHier7());
                            beanTemp.setMatGrpId(bean.getMatGrpId());
                            beanTemp.setMatGrpDesc(bean.getMatGrpDesc());
                            beanTemp.setSkuGroup(bean.getSkuGroup());
                            beanTemp.setAlternativeUOM2(bean.getAlternativeUOM2());
                            beanTemp.setAlternativeUOM2Num(bean.getAlternativeUOM2Num());
                            beanTemp.setAlternativeUOM2Den(bean.getAlternativeUOM2Den());
                            beanTemp.setAlternativeUOM1(bean.getAlternativeUOM1());
                            beanTemp.setAlternativeUOM1Num(bean.getAlternativeUOM1Num());
                            beanTemp.setAlternativeUOM1Den(bean.getAlternativeUOM1Den());
                            beanTemp.setStockValue(bean.getStockValue());
                            beanTemp.setStockTypeID(bean.getStockTypeID());
                            beanTemp.setStockTypeDesc(bean.getStockTypeDesc());
                            beanTemp.setSPStockItemGUID(bean.getSPStockItemGUID());
                            beanTemp.setMaterialNo(bean.getMaterialNo());
                            beanTemp.setMaterialDesc(bean.getMaterialDesc());
//                                        beanTemp.setMRP(bean.getMRP());
                            beanTemp.setUnrestrictedQty(bean.getUnrestrictedQty());
                            beanTemp.setUOM(bean.getUOM());
                            beanTemp.setCurrency(bean.getCurrency());
                            beanTemp.setLandingPrice(bean.getLandingPrice());
                            beanTemp.setBrand(bean.getBrand());
                            beanTemp.setBrandDesc(bean.getBrandDesc());
                            beanTemp.setOrderMaterialGroupDesc(bean.getOrderMaterialGroupDesc());
                            beanTemp.setOrderMaterialGroupID(bean.getOrderMaterialGroupID());
                            beanTemp.setStockOwner(bean.getStockOwner());
                            beanTemp.setStockOwnerDesc(bean.getStockOwnerDesc());
                            if (snoBean != null) {
                                beanTemp.setRate(snoBean.getUnitPrice());
                                beanTemp.setMRP(snoBean.getMRP());
                                beanTemp.setGross(snoBean.getIntermUnitPrice());
                                beanTemp.setTax1(snoBean.getTax1());
                                beanTemp.setTax2(snoBean.getTax2());
                                beanTemp.setRateTax(snoBean.getRateTax());
                                beanTemp.setMaterialDesc(snoBean.getMaterialDesc());
                                beanTemp.setMaterialNo(snoBean.getMaterialNo());
                                beanTemp.setAlternativeUOM1(snoBean.getAlternateUom());
                                beanTemp.setAlternativeUOM1Num(snoBean.getAltUomDen());
                                beanTemp.setAlternativeUOM1Den(snoBean.getAltUomNum());
                                beanTemp.setUnrestrictedQty(snoBean.getQuantity());
                                if (!TextUtils.isEmpty(beanTemp.getUnrestrictedQty())) {
                                    double value = 0.0;
                                    double pieces = 0.0;
                                    double finalValue = 0.0;
                                    double count = 0.0;
                                    double finalPieces = 0.0;
                                    double unrestrictedQty = 0.0;
                                    double altUom2Num = 0.0;
                                    int TotalValue = 0;
                                    beanTemp.setUnrestrictedQtyTemp(beanTemp.getUnrestrictedQty());
                                    unrestrictedQty = Double.parseDouble(beanTemp.getUnrestrictedQty());
                                    if (beanTemp.getAlternativeUOM2Den() != null) {
                                        altUom2Num = Double.parseDouble(beanTemp.getAlternativeUOM1Num());
                                        if (altUom2Num > 0) {
                                            qty = String.valueOf(unrestrictedQty / altUom2Num);
                                            try {
                                                if (qty.contains(".")) {
                                                    double d = Double.parseDouble(qty);
                                                    number = (int) d;
                                                    beanTemp.setUnrestrictedQty(String.valueOf(number));
                                                    if (!TextUtils.isEmpty(qty)) {
                                                        value = Double.parseDouble(qty);
                                                        pieces = value - number;
                                                        if (pieces != 0) {
                                                            finalValue = pieces * Double.parseDouble(beanTemp.getAlternativeUOM1Num());
                                                            calculatedValue = String.valueOf(finalValue);
                                                            removedZerosValue = UtilConstants.removeLeadingZerowithTwoDecimal(calculatedValue);
                                                            count = Double.parseDouble(removedZerosValue);
                                                            finalPieces = Double.parseDouble(String.valueOf(count));
                                                            TotalValue = (int) finalPieces;
                                                            beanTemp.setPieces(UtilConstants.removeLeadingZero(finalValue));
//                                                    bean.setUnrestrictedQty(qty);
                                                        }else{
                                                            beanTemp.setPieces(UtilConstants.removeLeadingZero(finalValue));
                                                        }
                                                    }
                                                } else {
                                                    beanTemp.setUnrestrictedQty(qty);
                                                }
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                }
                                stockList.add(beanTemp);
                            }
                        }

                    }

                }
                BreadStockListActivity.isRefresh = false;
                AllSKUActivity.isRefresh = false;
                BreadAllSKUActivity.isRefresh = false;
            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

    }


    List userProfList = new ArrayList();
    List userAuthList = new ArrayList();
    private void getFactoriesList() {
        if (OfflineManager.offlineStore != null) {
            try {
                String userAuthQry = Constants.UserProfileAuthSet + "?$filter=AuthOrgTypeID eq '000001'";
                LogManager.writeLogInfo("UserProfileAuthSet qry  : " + userAuthQry);
                if (OfflineManager.isOfflineStoreOpen()) {
                    userAuthList = new OfflineUtils.ODataOfflineBuilder<>().setHeaderPayloadObject(new UserProfileAuthSetBean()).setODataEntitySet(Constants.UserProfileAuthSet).setODataEntityType(Constants.UserProfileAuth).setUIListener(this).setQuery(userAuthQry).returnODataList(OfflineManager.offlineStore);
                    LogManager.writeLogInfo("UserProfileAuthSet list count  : " + userAuthList.size());
//                    ArrayList<String> factoryList = new ArrayList<>();
                    if (userAuthList != null && !userAuthList.isEmpty()) {
                        String qryCPUID = "";
                        for (int i = 0; i < userAuthList.size(); i++) {

                            Set set = new HashSet(userAuthList);
                            userProfList.clear();
                            userProfList.addAll(set);
//                            UserProfileAuthSetBean userProfileAuthSetBean = (UserProfileAuthSetBean) userAuthList.get(i);
//                            userProfList.add(userProfileAuthSetBean.getAuthOrgValueDesc());
//                            userProfileAuthSetBean.getAuthOrgValueDesc();
                          */
/*  if (qryCPUID.isEmpty()) {
                                qryCPUID = qryCPUID + Constants.CPUID + " eq '" + userProfileAuthSetBean.getAuthOrgValue() + "'";
                            } else {
                                qryCPUID = qryCPUID + " or " + Constants.CPUID + " eq '" + userProfileAuthSetBean.getAuthOrgValue() + "'";
                            }*//*

                        }
//                        getChannelPartnersList(qryCPUID);

                        view.refreshSpinners(userProfList, 4);
                    } else {
//                        refreshCPSPRelations();
                        LogManager.writeLogError("CPSPRelations giving 0 records");
                    }
                } else {
                    LogManager.writeLogError("Offline store not opened, unable to fetch distributors");
                }
            } catch (Throwable e) {
                LogManager.writeLogError("error while fetching distributors \n" + e.toString());
            }
        } else {
            LogManager.writeLogError("offlineStore is null");
        }
    }


    public static List<Object> allStockMaterialList = null;
    public static void fetchStockMaterials() {
        try {
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
            allStockMaterialList = new CopyOnWriteArrayList<>();
            String stockNosQry = Constants.CPStockItemSnos + "?$filter=GRDate ge datetime'"+getDatebynumberofDays("120")+"'";

            List materialListNoDays = new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new CPStockItemSnoBean())
                    .setODataEntitySet(Constants.CPStockItemSnos)
                    .setODataEntityType(Constants.CPStockItemSnosEntity)
                    .setQuery(stockNosQry)
                    .returnODataList(OfflineManager.offlineStore);
            if (materialListNoDays!=null&&!materialListNoDays.isEmpty()) {
                Set treeSet1 = new TreeSet(new Comparator() {
                    @Override
                    public int compare(Object o, Object t1) {
                        CPStockItemSnoBean bean1 = (CPStockItemSnoBean) o;
                        CPStockItemSnoBean bean2 = (CPStockItemSnoBean) t1;
                        return bean1.getCPStockItemGUID().compareTo(bean2.getCPStockItemGUID());
                    }
                });
                treeSet1.addAll(materialListNoDays);
                materialListNoDays.clear();
                materialListNoDays.addAll(treeSet1);
                */
/*for (CPStockItemSnoBean beanSno:(List<CPStockItemSnoBean>)materialListNoDays) {*//*


                String cpStockItemGuidQry =getCPStockNosQry(materialListNoDays);

                if (cpStockItemGuidQry!=null&&!TextUtils.isEmpty(cpStockItemGuidQry)){
                    cpStockItemGuidQry = "("+cpStockItemGuidQry+")";
                }
                String stockQry = Constants.CPStockItems + "?$select=CatalogInfo,SubBrand,BomMatIndicator,MatGrpID,MatGrpDesc,SKUGroup,AlternativeUOM2,AlternativeUOM2Num,AlternativeUOM2Den,AlternativeUOM1,AlternativeUOM1Num,AlternativeUOM1Den,StockValue,StockTypeID,StockTypeDesc,CPStockItemGUID,MaterialNo,MaterialDesc,MRP,UnrestrictedQty,UOM,Currency,LandingPrice,Brand,BrandDesc,OrderMaterialGroupDesc,OrderMaterialGroupID,StockOwner,StockOwnerDesc";
                //String stockQry = Constants.CPStockItems + "?$select=BomMatIndicator,MatGrpID,MatGrpDesc,SKUGroup,AlternativeUOM2,AlternativeUOM2Num,AlternativeUOM2Den,AlternativeUOM1,AlternativeUOM1Num,AlternativeUOM1Den,StockValue,StockTypeID,StockTypeDesc,CPStockItemGUID,MaterialNo,MaterialDesc,MRP,UnrestrictedQty,UOM,Currency,LandingPrice,Brand,BrandDesc,OrderMaterialGroupDesc,OrderMaterialGroupID,StockOwner,StockOwnerDesc &$filter=CPStockItemGuid eq '005056AC-0CA3-1EDB-A7CF-849A38DF74F3'";

                */
/**
                 * Appending split beat query if it is splitbeat
                 *//*


                stockQry = stockQry+"&$filter="+cpStockItemGuidQry;
                LogManager.writeLogDebug("BEAT :  formed query CPStockItems with prod det");
                LogManager.writeLogError("Split beat with stock Qry : "+stockQry);
                List materialList = new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(new CPStockItemBean())
                        .setODataEntitySet(Constants.CPStockItems)
                        .setODataEntityType(Constants.CPStockItemEntity)
                        .setQuery(stockQry)
                        .returnODataList(OfflineManager.offlineStore);

                LogManager.writeLogDebug("BEAT :  fetched CPStockItems ");
                try {
                    if (materialList!=null&&!materialList.isEmpty()) {
                        Set treeSet = new TreeSet(new Comparator() {
                            @Override
                            public int compare(Object o, Object t1) {
                                CPStockItemBean bean1 = (CPStockItemBean) o;
                                CPStockItemBean bean2 = (CPStockItemBean) t1;
                                return bean1.getSKUGroup().compareTo(bean2.getSKUGroup());
                            }
                        });
                        treeSet.addAll(materialList);
                        materialList.clear();
                        materialList.addAll(treeSet);
                        LogManager.writeLogDebug("BEAT :  fetching CPStockItems with SKUGrp");
                                */
/*for (CPStockItemBean bean:(List<CPStockItemBean>)materialList) {
                                    CPStockItemSnoBean snoBean = null;
                                    try {
                                        String query = Constants.CPStockItemSnos + "?$select=AltUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,Tax3,Tax4,Tax5,Tax6,Tax7,Tax8,Tax9,Tax10,IntermUnitPrice,Batch,ManufacturingDate,ExpiryDate,MaterialNo,MaterialDesc " +
                                                "&$filter=StockTypeID eq '1' and CPStockItemGUID eq guid'" + bean.getCPStockItemGUID().toUpperCase() + "'";
                                        snoBean = OfflineManager.getMRP(query);
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                    if (snoBean != null) {
                                        bean.setRate(snoBean.getUnitPrice());
                                        bean.setMRP(snoBean.getMRP());
                                        bean.setGross(snoBean.getIntermUnitPrice());
                                        bean.setTax1(snoBean.getTax1());
                                        bean.setTax2(snoBean.getTax2());
                                        bean.setRateTax(snoBean.getRateTax());
                                        bean.setAlternativeUOM1(snoBean.getAltUom());
                                        bean.setAlternativeUOM1Num(snoBean.getAltUomDen());
                                        bean.setAlternativeUOM1Den(snoBean.getAltUomNum());
                                    }
                                    ArrayList<String> strUOM = new ArrayList<>();
                                    strUOM.add(bean.getUOM());
                                    strUOM.add(bean.getAlternativeUOM1());
                                    bean.setStrUOM(strUOM);
                                    allStockMaterialList.add(bean);
                                }*//*

                        allStockMaterialList.addAll(materialList);
                        LogManager.writeLogDebug("BEAT :  fetched CPStockItems and CPStockItemDetails with SKUGrp");
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }
            try {
                if (allStockMaterialList != null && !allStockMaterialList.isEmpty()) {
                    Set<Object> set = new HashSet<>(allStockMaterialList);
                    allStockMaterialList.clear();
                    allStockMaterialList.addAll(set);
                    Collections.sort(allStockMaterialList, new Comparator<Object>() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            CPStockItemBean bean1 = (CPStockItemBean) o1;
                            CPStockItemBean bean2 = (CPStockItemBean) o2;
                            int group = 0;
                            if ((bean1.getMatGrpID()!=null&&!bean1.getMatGrpID().isEmpty())&&(bean2.getMatGrpID()!=null&&!bean2.getMatGrpID().isEmpty())) {
                                group = bean1.getMatGrpID().compareTo(bean2.getMatGrpID());
                            }
                            if (group != 0) {
                                return group;
                            }
                            int brand = bean1.getBrandDesc().compareTo(bean2.getBrandDesc());
                            if (brand != 0) return brand;
                            Double value1 = Double.valueOf(bean1.getMRP());
                            Double value2 = Double.valueOf(bean2.getMRP());
                            return Double.compare(value1,value2);
                        }
                    });
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
//                }
//            });
//            thread.setPriority(Thread.MAX_PRIORITY);
//            thread.start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static String getCPStockNosQry(List stockQryList){
        String stockQry="";
        try {

            if (stockQryList!=null&&!stockQryList.isEmpty()) {
                for (int i = 0; i < stockQryList.size(); i++) {
                    Object object1 =stockQryList.get(i);
                    if (object1 instanceof CPStockItemSnoBean){
                        CPStockItemSnoBean bean1 = (CPStockItemSnoBean) object1;
                        if (bean1.getCPStockItemGUID()!=null){
                            if (i!=stockQryList.size()-1) {
                                stockQry =stockQry.concat("CPStockItemGUID eq guid'"+bean1.getCPStockItemGUID().toUpperCase()+"' or ");
                            }else{
                                stockQry =stockQry.concat("CPStockItemGUID eq guid'"+bean1.getCPStockItemGUID().toUpperCase()+"'");
                            }

                        }
                    }
                }
            }else{
                LogManager.writeLogError("ProdetList has 0 size ");
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
//        LogManager.writeLogError("Split beat final qry : "+stockQry);
        return stockQry;
    }
}
*/

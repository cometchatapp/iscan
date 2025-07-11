/*
package com.arteriatech.ss.msec.bil.v4.outlet;

import static com.arteriatech.ss.msec.bil.v4.common.Constants.REPEATABLE_REQUEST_ID;
import static com.arteriatech.ss.msec.bil.v4.common.Constants.getDatebynumberofDays;
import static com.arteriatech.ss.msec.bil.v4.outlet.DashBoardPresenter.allStockMaterialList;
import static com.arteriatech.ss.msec.bil.v4.pda.sales.orderentry.allsku.SOAllSKUActivity.orderedArrayList;
import static com.arteriatech.ss.msec.bil.v4.registration.Configuration.server_Text;
import static com.arteriatech.ss.msec.bil.v4.registration.Configuration.server_Text_DR;
import static com.arteriatech.ss.msec.bil.v4.registration.Configuration.server_Text_default;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.bil.v4.AvailableServer;
import com.arteriatech.ss.msec.bil.v4.R;
import com.arteriatech.ss.msec.bil.v4.common.Constants;
import com.arteriatech.ss.msec.bil.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.bil.v4.common.MSFAApplication;
import com.arteriatech.ss.msec.bil.v4.mbo.CPPerformanceBean;
import com.arteriatech.ss.msec.bil.v4.mbo.CPProductDetTypeBean;
import com.arteriatech.ss.msec.bil.v4.mbo.ConfigTypsetTypeValuesBean;
import com.arteriatech.ss.msec.bil.v4.mbo.KPISetBean;
import com.arteriatech.ss.msec.bil.v4.mbo.RouteProductDetTypeBean;
import com.arteriatech.ss.msec.bil.v4.mbo.RouteScheduleDetailBean;
import com.arteriatech.ss.msec.bil.v4.mbo.RouteScheduleSPBean;
import com.arteriatech.ss.msec.bil.v4.mbo.SPStockItemBean;
import com.arteriatech.ss.msec.bil.v4.mbo.SPStockItemSNoBean;
import com.arteriatech.ss.msec.bil.v4.mbo.TargetItemsBean;
import com.arteriatech.ss.msec.bil.v4.mbo.TargetsBean;
import com.arteriatech.ss.msec.bil.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.bil.v4.mutils.common.Operation;
import com.arteriatech.ss.msec.bil.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.bil.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.bil.v4.mutils.location.LocationModel;
import com.arteriatech.ss.msec.bil.v4.outlet.outletdetails.CPSummary1SetBean;
import com.arteriatech.ss.msec.bil.v4.pda.distributorselection.ColorBean;
import com.arteriatech.ss.msec.bil.v4.pda.distributorselection.DistributorSelectionBean;
import com.arteriatech.ss.msec.bil.v4.pda.retailerselection.BeatSelectionBean;
import com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RouteSchPlansBean;
import com.arteriatech.ss.msec.bil.v4.pda.sales.eodoperation.VisitBean;
import com.arteriatech.ss.msec.bil.v4.pda.sales.orderbrowser.PDAOrderBrowserDetailsPresentImpl;
import com.arteriatech.ss.msec.bil.v4.pda.sales.orderentry.CommonDB;
import com.arteriatech.ss.msec.bil.v4.rpd.ValueHelpBean;
import com.arteriatech.ss.msec.bil.v4.rpd.billbrowser.SSInvoiceItemDetailsBean;
import com.arteriatech.ss.msec.bil.v4.rpd.billbrowser.SSInvoiceTypesBean;
import com.arteriatech.ss.msec.bil.v4.rpd.stock.CPStockItemBean;
import com.arteriatech.ss.msec.bil.v4.rpd.stock.CPStockItemSnoBean;
import com.arteriatech.ss.msec.bil.v4.so.SSSOItemDetailsBean;
import com.arteriatech.ss.msec.bil.v4.so.SSSOsHeaderBean;
import com.arteriatech.ss.msec.bil.v4.store.OfflineManager;
import com.arteriatech.ss.msec.bil.v4.store.OnlineManager;
import com.arteriatech.ss.msec.bil.v4.sync.SyncUtils;
import com.arteriatech.ss.msec.bil.v4.utils.ErrorListener;
import com.arteriatech.ss.msec.bil.v4.utils.OfflineUtils;
import com.sap.client.odata.v4.core.GUID;
import com.sap.smp.client.odata.ODataDuration;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataGuid;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.ODataProperty;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.exception.ODataParserException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

@SuppressWarnings("all")
public class OutletListViewModel implements UIListener, IOutletListView.IRetailerSelectionPresenter{
    private List beatSelectionBeanList;
    private List<com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean> beatRetailersList = new ArrayList();
    private Context context;
    private Activity activity;
    private IOutletListView view;
    private String isComingFrom = "";
    private String qrySSInvoices = "";
    private ArrayList<SSInvoiceTypesBean> ssInvoiceTypeList=null;
    private ArrayList<SSInvoiceTypesBean> ssInvoiceList=null;
    private List tempList = null;
    private String KPICode="";
    public static TargetItemsBean targetItemsBean = new TargetItemsBean();
    public static boolean isSurveyEnabled;
    public static List productDetList;
    public static CPPerformanceBean cpPerformanceBean;
    public static List<Object> stockMaterialList,schemeStockList;
    public static List<Object> spstockMaterialList;
    public static boolean isCDGO,isCMGO,isAllSKU;
    public static HashSet<String> MTDMaterialList;
    public static HashMap<String,String> billedMaterialList=new HashMap<>();
    public OutletListViewModel(Context context, Activity activity, IOutletListView view, String isComingFrom) {
        this.context = context;
        this.activity = activity;
        this.view = view;
        this.isComingFrom = isComingFrom;
        this.tempList = new ArrayList();
        this.ssInvoiceTypeList = new ArrayList();
        this.ssInvoiceList = new ArrayList();
        this.cpPerformanceBean = new CPPerformanceBean();
        this.MTDMaterialList = new HashSet<>();
        isCDGO=false;
        isCMGO=false;
    }

    @Override
    public void initializeRequest() {
//        new GetBeatsAsyncTask().execute();
//        getDistributorList();
    }

    private List distributorBeanList = new ArrayList();
    private List distributorBeanListNew = new ArrayList();
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
                            distributorBeanList = new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new DistributorSelectionBean())
                                    .setODataEntitySet(Constants.CPSPRelations)
                                    .setODataEntityType(Constants.CPSPRelationEntity)
                                    .setUIListener(this)
                                    .setQuery(routeQry)
                                    .returnODataList(OfflineManager.offlineStore);

                            LogManager.writeLogInfo("Distributor selection distributor list count  : "+distributorBeanList.size());
                            if (distributorBeanList!=null&&!distributorBeanList.isEmpty()) {
                                for(int i=0;i<distributorBeanList.size();i++){
                                    DistributorSelectionBean ditributorBean=(DistributorSelectionBean) distributorBeanList.get(i);
                                    if (ditributorBean.getCPTypeID().equalsIgnoreCase("01")){
                                        distributorBeanListNew.add(ditributorBean);
                                    }
                                }
                                Set set = new HashSet(distributorBeanListNew);
                                distributorBeanListNew.clear();
                                distributorBeanListNew.addAll(set);
                                Collections.sort(distributorBeanListNew, (Comparator<DistributorSelectionBean>) (one, other) -> one.getCPName().compareTo(other.getCPName()));
                                ConstantsUtils.checkNetwork(activity,null,true);
                                view.refreshSpinners(distributorBeanListNew, 4);
                            }else{
//                                refreshCPSPRelations();
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
                            distributorBeanList = new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new DistributorSelectionBean())
                                    .setODataEntitySet(Constants.CPSPRelations)
                                    .setODataEntityType(Constants.CPSPRelationEntity)
                                    .setUIListener(this)
                                    .setQuery(routeQry)
                                    .returnODataList(OfflineManager.offlineStore);
                            LogManager.writeLogInfo("Distributor selection distributor list count  : " + distributorBeanList.size());
                            if (distributorBeanList != null && !distributorBeanList.isEmpty()) {
                                for(int i=0;i<distributorBeanList.size();i++){
                                    DistributorSelectionBean ditributorBean=(DistributorSelectionBean) distributorBeanList.get(i);
                                    if (ditributorBean.getCPTypeID().equalsIgnoreCase("01")){
                                        distributorBeanListNew.add(ditributorBean);
                                    }
                                }
                                Set set = new HashSet(distributorBeanListNew);
                                distributorBeanListNew.clear();
                                distributorBeanListNew.addAll(set);
                                Collections.sort(distributorBeanListNew, (Comparator<DistributorSelectionBean>) (one, other) -> one.getCPName().compareTo(other.getCPName()));
                                ConstantsUtils.checkNetwork(activity, null, true);
                                view.refreshSpinners(distributorBeanListNew, 4);
                            } else {
//                                refreshCPSPRelations();
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
                            distributorBeanList = new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new DistributorSelectionBean())
                                    .setODataEntitySet(Constants.CPSPRelations)
                                    .setODataEntityType(Constants.CPSPRelationEntity)
                                    .setUIListener(this)
                                    .setQuery(routeQry)
                                    .returnODataList(OfflineManager.offlineStore);
                            LogManager.writeLogInfo("Distributor selection distributor list count  : " + distributorBeanList.size());
                            if (distributorBeanList != null && !distributorBeanList.isEmpty()) {
                                for(int i=0;i<distributorBeanList.size();i++){
                                    DistributorSelectionBean ditributorBean=(DistributorSelectionBean) distributorBeanList.get(i);
                                    if (ditributorBean.getCPTypeID().equalsIgnoreCase("01")){
                                        distributorBeanListNew.add(ditributorBean);
                                    }
                                }

                                Set set = new HashSet(distributorBeanListNew);
                                distributorBeanListNew.clear();
                                distributorBeanListNew.addAll(set);
                                Collections.sort(distributorBeanListNew, (Comparator<DistributorSelectionBean>) (one, other) -> one.getCPName().compareTo(other.getCPName()));
                                ConstantsUtils.checkNetwork(activity, null, true);
                                view.refreshSpinners(distributorBeanListNew, 4);
                            } else {
//                                refreshCPSPRelations();
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
                view.refreshSpinners(distributorBeanList, 4);
                break;
        }

    }

    @Override
    public void getRetailers(String guid) {
        new GetRetailerAsyncTask(guid).execute();
    }

    @Override
    public void onSave(VisitBean visitBean) {
        try {
//            visitBean.setSPGUID(MSFAApplication.SPGUID.toUpperCase());
            visitBean.setVisitGUID(GUID.newRandom().toString36().toUpperCase());
            visitBean.setCPGUID(visitBean.getCPGUID().replaceAll("-","").toUpperCase());
            visitBean.setStartDate((ConstantsUtils.returnCurrentDate()));
            visitBean.setVisitDate(ConstantsUtils.returnCurrentDate());
            visitBean.setEndDate((ConstantsUtils.returnCurrentDate()));
            visitBean.setStartLat(String.valueOf(UtilConstants.round(UtilConstants.latitude,12)));
            visitBean.setStartLong(String.valueOf(UtilConstants.round(UtilConstants.longitude,12)));
            visitBean.setEndLat(String.valueOf(UtilConstants.round(UtilConstants.latitude,12)));
            visitBean.setEndLong(String.valueOf(UtilConstants.round(UtilConstants.longitude,12)));
            visitBean.setStartTime(ConstantsUtils.oDataTimeFormat());
            visitBean.setEndTime(ConstantsUtils.oDataTimeFormat());
            visitBean.setVisitCatID(Constants.str_01);
            visitBean.setStatusID(Constants.str_01);
            visitBean.setCPTypeID(Constants.str_02);
            visitBean.setSPGUID(Constants.getSPGUID());
            visitBean.setChangedAt("");
            visitBean.setCreatedAt("");
            visitBean.setPlannedEndTime("");
            visitBean.setPlannedStartTime("");
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

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate(VisitBean visitBean) {
        try {
            visitBean.setETAG(null);
            */
/**
             * Reading Visit list for getting ETag seperately
             *//*

            if (visitBean.getETAG()==null||TextUtils.isEmpty(visitBean.getETAG())) {
                String query = Constants.Visits + "?$filter= VisitGUID eq guid'"+visitBean.getVisitGUID()+"'";
                List visitList =null;
                visitList = new ArrayList();
                visitList = new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(new VisitBean())
                        .setODataEntitySet(Constants.Visits)
                        .setODataEntityType(Constants.VISITENTITY)
                        .setUIListener(this)
                        .setQuery(query)
                        .setETag(true)
                        .returnODataList(OfflineManager.offlineStore);
                if (!visitList.isEmpty()) {
                    VisitBean bean = (VisitBean) visitList.get(0);
                    visitBean.setETAG(bean.getETAG());
                }
            }
            */
/**
             * updating the Visit
             *//*

            try {
                visitBean.setStartTime(ConstantsUtils.oDataTimeFormat());
                visitBean.setEndTime(ConstantsUtils.oDataTimeFormat());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            String resourcePath = "Visits(guid'" + visitBean.getVisitGUID().toUpperCase() + "')";
            new OfflineUtils.ODataOfflineBuilder()
                    .setODataEntitySet(Constants.Visits)
                    .setODataEntityType(Constants.VISITENTITY)
                    .setUIListener(this)
                    .setCreate(false)
                    .setUpdate(true)
                    .setHeaderPayloadObject(visitBean)
                    .setResourcePath(resourcePath, resourcePath)
                    .setETag(visitBean.getETAG())
                    .build(new ErrorListener() {
                        @Override
                        public void error(String errorAtField, String errorMessage) {
                            Toast.makeText(context, errorAtField+" : "+errorMessage, Toast.LENGTH_SHORT).show();
                            LogManager.writeLogError(errorAtField + " " + errorAtField);
                        }
                    });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    */
/**
     * This will return the Beat OfflineData
     *//*

    private List getBeatsRemoteData(){
        try {
            String routeQry = Constants.RouteSchedules+"?$select=Description,RouteSchGUID";
            beatSelectionBeanList =new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new BeatSelectionBean())
                    .setODataEntitySet(Constants.RouteSchedules)
                    .setODataEntityType(Constants.RouteScheduleEntity)
                    .setUIListener(this)
                    .setQuery(routeQry)
                    .returnODataList(OfflineManager.offlineStore);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return beatSelectionBeanList;
    }


    */
/**
     * This method will return Retailer List based on Beat GUID.
     * @param routeSchGUID
     *//*

    private HashMap<String, String> mapInvCPList = null;
    private List getRetailersRemoteData(String routeSchGUID){
        try {
            String routeQry;
            if (!TextUtils.isEmpty(routeSchGUID)) {
                routeQry = Constants.RouteSchedulePlans+"?$select=VisitCPGUID &$filter=RouteSchGUID eq guid'"+routeSchGUID+"'";
            } else {
                routeQry = Constants.RouteSchedulePlans+"?$select=VisitCPGUID";
            }

            List list = new ArrayList();
            list = new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new RouteSchPlansBean())
                    .setODataEntitySet(Constants.RouteSchedulePlans)
                    .setODataEntityType(Constants.RouteSchedulePlanEntity)
                    .setUIListener(this)
                    .setQuery(routeQry)
                    .returnODataList(OfflineManager.offlineStore);
            if (list!=null&&!list.isEmpty()){
                String routeCPQry =Constants.ChannelPartners+"?$select=Name,PostalCode,OwnerName,CPUID,CPGUID,Address1,Address2,Address3,Mobile1,CPTypeID,CPNo,CPTypeDesc,ActivationDate,Group8,Group2,Group8Desc,Group2Desc,Group1,Landmark &$filter=CPTypeID eq '20' and (";
                for (int i = 0; i < list.size(); i++) {
                    RouteSchPlansBean bean = (RouteSchPlansBean) list.get(i);
                    if (i==list.size()-1){
                        routeCPQry = routeCPQry.concat("CPGUID eq guid'"+ ConstantsUtils.string32To36(bean.getVisitCPGUID())+"')");
                    }else{
                        routeCPQry = routeCPQry.concat("CPGUID eq guid'"+ConstantsUtils.string32To36(bean.getVisitCPGUID())+"' or ");
                    }
                }

                beatRetailersList =new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(new com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean())
                        .setODataEntitySet(Constants.ChannelPartners)
                        .setODataEntityType(Constants.ChannelPartnerEntity)
                        .setUIListener(this)
                        .setQuery(routeCPQry)
                        .returnODataList(OfflineManager.offlineStore);
                mapInvCPList = OfflineManager.getRetailerSSInvSoldID2((ArrayList<com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean>) beatRetailersList);
                for(int i=0;i<beatRetailersList.size();i++){
                    com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean item= (com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) beatRetailersList.get(i);
                    String saleOrderQry = Constants.SSSOs + "?$select=SSSOGuid &$filter=SoldToCPGUID eq guid'" + item.getCPGUID() + "' and " +Constants.OrderDate+" eq datetime'"+ UtilConstants.getNewDate() + "' and "+Constants.SPGUID+" eq guid'"+ MSFAApplication.SPGUID+"'";
                    try {
                        boolean isSaleOrderAval=OfflineManager.getSaleOrderAval(saleOrderQry);
                        if(isSaleOrderAval){
                            item.setSaleOrderAval("X");
                        }else {
                            item.setSaleOrderAval("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if(mapInvCPList.containsKey(item.getCPGUID().toUpperCase())){
                            item.setInvoiceAval("X");
                        }else{
                            item.setInvoiceAval("");
                        }
                        beatRetailersList.set(i,item);
                    } catch (Exception e) {
                        item.setInvoiceAval("");
                        e.printStackTrace();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return beatRetailersList;
    }

    public void getFilteredRetailers(String routeSchGUID,String orderBy,boolean isSequnceMaintained){
        try {
            beatRetailersList.clear();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int sort = orderBy==null||TextUtils.isEmpty(orderBy)?1: Integer.parseInt(orderBy);
                    try {
                        beatRetailersList = (List<com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean>) Stream.of(DashBoardPresenter.retailersList)
                                .filter(predicate -> ((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean)predicate).getBeatGUID().equalsIgnoreCase(routeSchGUID))
                                .collect(Collectors.toList());
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    if (!isSequnceMaintained&&!(isComingFrom.equals(Constants.INTENT_EXTRA_ORDER_BROWSER) || isComingFrom.equals(Constants.INTENT_EXTRA_BILLING))){
                        if (sort==1) {
                            Collections.sort(beatRetailersList, new Comparator<com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean>() {
                                @Override
                                public int compare(com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean one, com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean other) {
                                    int indexSeq = ((Integer) one.getIndexSequenceNo()).compareTo(other.getIndexSequenceNo());
                                    if (indexSeq != 0) {
                                        return indexSeq;
                                    }
                                    int name = one.getName().compareTo(other.getName());
                                    return name;
                                }
                            });
                        }else if(sort==2){
                            for (com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean bean:beatRetailersList) {
                                if (bean.getIndexSequenceNo()==999){
                                    bean.setIndexSequenceNo(0);
                                }
                            }
                            Collections.sort(beatRetailersList, new Comparator<com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean>() {
                                @Override
                                public int compare(com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean one, com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean other) {
                                    int indexSeq = -((Integer) one.getIndexSequenceNo()).compareTo(other.getIndexSequenceNo());
                                    if (indexSeq != 0) {
                                        return indexSeq;
                                    }
                                    int name = one.getName().compareTo(other.getName());
                                    return name;
                                }
                            });
                        }
                    }else if(isSequnceMaintained&&!(isComingFrom.equals(Constants.INTENT_EXTRA_ORDER_BROWSER) || isComingFrom.equals(Constants.INTENT_EXTRA_BILLING))){
                        if (sort==1) {
                            Collections.sort(beatRetailersList, new Comparator<com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean>() {
                                @Override
                                public int compare(com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean one, com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean other) {
                                    int indexSeq = ((Integer) one.getIndexSequenceNo()).compareTo(other.getIndexSequenceNo());
                                    if (indexSeq != 0) {
                                        return indexSeq;
                                    }
                                    int name = one.getName().compareTo(other.getName());
                                    return name;
                                }
                            });
                        }else if(sort==2){
                            Collections.sort(beatRetailersList, new Comparator<com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean>() {
                                @Override
                                public int compare(com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean one, com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean other) {
                                    int indexSeq = -((Integer) one.getIndexSequenceNo()).compareTo(other.getIndexSequenceNo());
                                    if (indexSeq != 0) {
                                        return indexSeq;
                                    }
                                    int name = one.getName().compareTo(other.getName());
                                    return name;
                                }
                            });
                        }
                    }else if(isSequnceMaintained&&(isComingFrom.equals("KATSOutlets"))){
                        if (sort==1) {
                            Collections.sort(beatRetailersList, new Comparator<com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean>() {
                                @Override
                                public int compare(com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean one, com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean other) {
                                    int indexSeq = ((Integer) one.getIndexSequenceNo()).compareTo(other.getIndexSequenceNo());
                                    if (indexSeq != 0) {
                                        return indexSeq;
                                    }
                                    int name = one.getName().compareTo(other.getName());
                                    return name;
                                }
                            });
                        }else if(sort==2){
                            Collections.sort(beatRetailersList, new Comparator<com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean>() {
                                @Override
                                public int compare(com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean one, com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean other) {
                                    int indexSeq = -((Integer) one.getIndexSequenceNo()).compareTo(other.getIndexSequenceNo());
                                    if (indexSeq != 0) {
                                        return indexSeq;
                                    }
                                    int name = one.getName().compareTo(other.getName());
                                    return name;
                                }
                            });
                        }
                    }
                    for (int i = 0; i < beatRetailersList.size(); i++) {
                        com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean bean = beatRetailersList.get(i);
                        bean.setSequenceNo(i+1);
                    }
                    if((isComingFrom.equals("KATSOutlets"))){
                        List<com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean> beatRetailersKATSList = new ArrayList();
                        for (int i = 0; i < beatRetailersList.size(); i++) {
                            com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean bean = beatRetailersList.get(i);
                            if (bean.getIsKeyCP().equalsIgnoreCase("X")) {
                                beatRetailersKATSList.add(bean);
                            }
                        }
                        beatRetailersList.clear();
                        beatRetailersList.addAll(beatRetailersKATSList);
                    }
                    if (beatRetailersList != null) {
                        try {
                            if (!beatRetailersList.isEmpty()) {
                                OutletListFragment.totalOutlets=beatRetailersList.size();
                            }
                            if (beatRetailersList.size() > 0 && beatRetailersList != null && (isComingFrom.equals(Constants.INTENT_EXTRA_ORDER_BROWSER) || isComingFrom.equals(Constants.INTENT_EXTRA_BILL_BROWSER))) {
                                com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean retailerSelectionBean = new com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean();
                                retailerSelectionBean.setName("Select All");
                                beatRetailersList.add(0, retailerSelectionBean);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.refreshListOutlet(beatRetailersList);
                            }
                        });
                    }
                }
            }).start();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public static void getSplitBeatData(String routeSchGUID, String beatCatID, String AWCode) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String routeQry = "";
                        String routeSPQry = "";
                        if (productDetList == null) {
                            productDetList = new ArrayList();
                        } else {
                            productDetList.clear();
                        }
                        try {
                            LogManager.writeLogError("beatCat ID : " + beatCatID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (!TextUtils.isEmpty(routeSchGUID) && beatCatID != null && TextUtils.equals(beatCatID, "02")) {
                            if (MSFAApplication.SLIPT_BEAT_FLAG.equalsIgnoreCase("X")) {
                                LogManager.writeLogDebug("BEAT :  splitbeat  flag X");
                                LogManager.writeLogError("Fetching stockmaterials without beatCatID");
                                LogManager.writeLogDebug("Fetching stockmaterials started with no split");
                                if (MSFAApplication.isAWSM) {
                                    fetchStockMaterials();
                                } else if (MSFAApplication.isBCRVAN) {
                                    fetchStockMaterials();
                                } else if (MSFAApplication.isVAN) {
                                    fetchSPStockMaterials();
                                }else if(MSFAApplication.isBVAN){
                                    fetchBreadSPStockMaterials(AWCode);
                                }
                            }else {
                                LogManager.writeLogDebug("BEAT :  splitbeat  flag empty");
                                LogManager.writeLogDebug("BEAT :  fetching splitbeat  data");
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
                                            routeProdDetQry = "RouteProductDetTypes?$select=PrdDet1GUID,ProductID,PrdCatgr,PrdDetType &$filter=PrdDetGUID eq guid'" + bean.getProdDetGUID() + "'";
                                            productDetList = new OfflineUtils.ODataOfflineBuilder<>()
                                                    .setHeaderPayloadObject(new RouteProductDetTypeBean())
                                                    .setODataEntitySet(Constants.RouteProductDetTypes)
                                                    .setODataEntityType(Constants.RouteProductDetTypeEntity)
                                                    .setQuery(routeProdDetQry)
                                                    .returnODataList(OfflineManager.offlineStore);
                                        }
                                        LogManager.writeLogDebug("BEAT :  fetched RouteProductDetTypes  data");
                                    }
                                }
                                LogManager.writeLogError("Fetching stockmaterials with beatCatID : " + beatCatID);
                                LogManager.writeLogDebug("BEAT :  fetching Materials based on Prod det ");
                                if (MSFAApplication.isAWSM) {
                                    fetchStockMaterials();
                                }else if (MSFAApplication.isBCRVAN) {
                                    fetchStockMaterials();
                                } else if (MSFAApplication.isVAN) {
                                    fetchSPStockMaterials();
                                }else if(MSFAApplication.isBVAN){
                                    fetchBreadSPStockMaterials(AWCode);
                                }
                                LogManager.writeLogDebug("BEAT :  fetched Materials based on Prod det ");
                            }
                        }else{
                            LogManager.writeLogError("Fetching stockmaterials without beatCatID");
                            LogManager.writeLogDebug("Fetching stockmaterials started with no split");
                            if (MSFAApplication.isAWSM) {
                                fetchStockMaterials();
                            }else if (MSFAApplication.isBCRVAN) {
                                fetchStockMaterials();
                            }else if(MSFAApplication.isVAN){
                                fetchSPStockMaterials();
                            }else if(MSFAApplication.isBVAN){
                                fetchBreadSPStockMaterials(AWCode);
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public static boolean isProductDef = false;
    private static String cpguidUID="";
    public static void getSplitBeatCPData(String routeSchGUID,String beatCatID,String cpguid){
        try {
            cpguidUID=cpguid;
            isProductDef = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String routeQry="";
                        String routeSPQry="";
                        if (productDetList==null) {
                            productDetList = new ArrayList();
                        }else{
                            productDetList.clear();
                        }
                        try {
                            LogManager.writeLogError("beatCat ID : "+beatCatID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        if (!TextUtils.isEmpty(routeSchGUID)&&beatCatID!=null&&TextUtils.equals(beatCatID,"02")) {
                            if(MSFAApplication.SLIPT_BEAT_FLAG.equalsIgnoreCase("X")){
                                isProductDef = true;
                                LogManager.writeLogDebug("BEAT :  splitbeat  flag X");
                                LogManager.writeLogError("Fetching stockmaterials without beatCatID");
                                LogManager.writeLogDebug("Fetching stockmaterials started with no split");
                                if (MSFAApplication.isAWSM) {
                                    fetchStockMaterials();
                                }else if (MSFAApplication.isBCRVAN) {
                                    fetchStockMaterials();
                                }else if(MSFAApplication.isBVAN){
                                    fetchBreadSPStockMaterials("");
                                }
                            }else {
                                LogManager.writeLogDebug("BEAT :  splitbeat  flag empty");
                                LogManager.writeLogDebug("BEAT :  fetching splitbeat  data");
                                routeSPQry = Constants.CPProductDetTypes + "?$select=PrdDetGuid &$filter=EntityKey eq 'SPGUID:" + MSFAApplication.SPGUID.replaceAll("-","").toUpperCase()+";CPGUID:"+cpguid.replaceAll("-","").toUpperCase() + "'";
                                List cpProductDetTypesList = new ArrayList();
                                cpProductDetTypesList = new OfflineUtils.ODataOfflineBuilder<>()
                                        .setHeaderPayloadObject(new CPProductDetTypeBean())
                                        .setODataEntitySet(Constants.CPProductDetTypes)
                                        .setODataEntityType(Constants.CPProductDetTypeEntity)
                                        .setQuery(routeSPQry)
                                        .returnODataList(OfflineManager.offlineStore);

                                if(cpProductDetTypesList.size()>0){
                                    if (cpProductDetTypesList.get(0) instanceof CPProductDetTypeBean) {
                                        CPProductDetTypeBean bean = (CPProductDetTypeBean) cpProductDetTypesList.get(0);
                                        String routeProdDetQry = "RouteProductDetTypes?$select=PrdDet1GUID,ProductID,PrdCatgr,PrdDetType &$filter=PrdDetGUID eq guid'" + bean.getPrdDetGuid() + "'";
                                        productDetList = new OfflineUtils.ODataOfflineBuilder<>()
                                                .setHeaderPayloadObject(new RouteProductDetTypeBean())
                                                .setODataEntitySet(Constants.RouteProductDetTypes)
                                                .setODataEntityType(Constants.RouteProductDetTypeEntity)
                                                .setQuery(routeProdDetQry)
                                                .returnODataList(OfflineManager.offlineStore);
                                    }
                                    LogManager.writeLogDebug("BEAT :  fetched RouteProductDetTypes  data");
                                }else {
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
                                                routeProdDetQry = "RouteProductDetTypes?$select=PrdDet1GUID,ProductID,PrdCatgr,PrdDetType &$filter=PrdDetGUID eq guid'" + bean.getProdDetGUID() + "'";
                                                productDetList = new OfflineUtils.ODataOfflineBuilder<>()
                                                        .setHeaderPayloadObject(new RouteProductDetTypeBean())
                                                        .setODataEntitySet(Constants.RouteProductDetTypes)
                                                        .setODataEntityType(Constants.RouteProductDetTypeEntity)
                                                        .setQuery(routeProdDetQry)
                                                        .returnODataList(OfflineManager.offlineStore);
                                            }
                                            LogManager.writeLogDebug("BEAT :  fetched RouteProductDetTypes  data");
                                        }
                                    }
                                }
                                isProductDef = true;
                                LogManager.writeLogError("Fetching stockmaterials with beatCatID : " + beatCatID);
                                LogManager.writeLogDebug("BEAT :  fetching Materials based on Prod det ");
                                if (MSFAApplication.isAWSM) {
                                    fetchStockMaterials();
                                }else if (MSFAApplication.isBCRVAN) {
                                    fetchStockMaterials();
                                } else if (MSFAApplication.isVAN) {
                                    fetchSPStockMaterials();
                                }else if(MSFAApplication.isBVAN){
                                    fetchBreadSPStockMaterials("");
                                }
                                LogManager.writeLogDebug("BEAT :  fetched Materials based on Prod det ");
                            }
//                        }else{
//                            isProductDef = true;
//                            LogManager.writeLogError("Fetching stockmaterials without beatCatID");
//                            LogManager.writeLogDebug("Fetching stockmaterials started with no split");
//                            if (MSFAApplication.isAWSM) {
//                                fetchStockMaterials();
//                            }else if (MSFAApplication.isBCRVAN) {
//                                fetchStockMaterials();
//                            }else if(MSFAApplication.isVAN){
//                                fetchSPStockMaterials();
//                            }
//                        }
                    } catch (Throwable e) {
                        isProductDef = true;
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Throwable e) {
            isProductDef = true;
            e.printStackTrace();
        }

    }
    @Override
    public void onRequestError(int operation, Exception e) {
//        view.showMessage("failed to Skip Order",1);
        System.out.println(e.toString());
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cancelTimer();
                view.hideProgress();
            }
        });
        activity.runOnUiThread(() -> view.showMessage(e.toString(), 1));
    }

    @Override
    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
//        view.showMessage("Order Skipped",1);
        if (operation == Operation.OfflineFlush.getValue()) {
            if (UtilConstants.isNetworkAvailable(activity)) {
                OfflineManager.refreshStoreSync(activity, this,Constants.Fresh, concatFlushCollStr);
                view.showProgress("Refreshing Retailer, Please wait..",1);

            }else{
                view.hideProgress();
                cancelTimer();
                view.showMessage("Synchronization not completed due to network unavailablity",1);
            }
        }else if (operation == Operation.OfflineRefresh.getValue()) {

            navigateToAddress();

        }
    }
    private class GetBeatsAsyncTask  extends AsyncTask<Void,Void,List>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.showProgress();
        }

        @Override
        protected List doInBackground(Void... voids) {
            try {
                return getBeatsRemoteData();
            } catch (Exception e) {
                e.printStackTrace();
                activity.runOnUiThread(() -> {
                    view.hideProgress();
                    cancelTimer();
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            try {
                Collections.sort(list, (Comparator<BeatSelectionBean>) (one, other) -> -one.getDescription().compareTo(other.getDescription()));
                view.hideProgress();
                try {
                    if (list.size()>0 && list != null && isComingFrom.equals(Constants.INTENT_EXTRA_ORDER_BROWSER)){
                        BeatSelectionBean beatSelectionBean = new BeatSelectionBean();
                        beatSelectionBean.setDescription("Select All");
                        list.add(0,beatSelectionBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                view.refreshSpinners(list,1);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    private class GetRetailerAsyncTask  extends AsyncTask<Void,Void,List>{
        String routeSchGUID;

        public GetRetailerAsyncTask(String routeSchGUID) {
            this.routeSchGUID = routeSchGUID;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            view.showProgress();
        }

        @Override
        protected List doInBackground(Void... voids) {
            try {
               */
/* if (TextUtils.isEmpty(routeSchGUID)) {
                    com.arteriatech.ss.msec.bil.v2.pda.retailerselection.RetailerSelectionBean retailerSelectionBean = new com.arteriatech.ss.msec.bil.v2.pda.retailerselection.RetailerSelectionBean();
                    retailerSelectionBean.setName("Select All");
                    tempList.add(0,retailerSelectionBean);
                    return tempList;
                }else{*//*

                return getRetailersRemoteData(routeSchGUID);
//                }
            } catch (Exception e) {
                e.printStackTrace();
                activity.runOnUiThread(() -> {
//                    view.hideProgress();
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            if (list != null) {
                Collections.sort(list, (Comparator<com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean>) (one, other) -> one.getName().toUpperCase().compareTo(other.getName().toUpperCase()));
//                view.hideProgress();
                try {
                    if (list.size() > 0 && list != null && isComingFrom.equals(Constants.INTENT_EXTRA_ORDER_BROWSER)) {
                        com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean retailerSelectionBean = new com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean();
                        retailerSelectionBean.setName("Select All");
                        list.add(0, retailerSelectionBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                view.refreshSpinners(list, 2);
//                view.hideProgress();
                view.refreshListOutlet(list);

            } else {
//                view.hideProgress();
            }
        }
    }
    public void onSkipOrder(VisitBean visitBean) {
        try {
//            visitBean.setSPGUID(MSFAApplication.SPGUID.toUpperCase());
            visitBean.setVisitGUID(GUID.newRandom().toString36().toUpperCase());
            visitBean.setCPGUID(visitBean.getCPGUID().replaceAll("-","").toUpperCase());
            visitBean.setStartDate((ConstantsUtils.returnCurrentDate()));
            visitBean.setVisitDate(ConstantsUtils.returnCurrentDate());
            visitBean.setEndDate((ConstantsUtils.returnCurrentDate()));
            try {
                visitBean.setStartLat(String.valueOf(UtilConstants.round(UtilConstants.latitude,12)));
                visitBean.setStartLong(String.valueOf(UtilConstants.round(UtilConstants.longitude,12)));
                visitBean.setEndLat(String.valueOf(UtilConstants.round(UtilConstants.latitude,12)));
                visitBean.setEndLong(String.valueOf(UtilConstants.round(UtilConstants.longitude,12)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            visitBean.setStartTime(ConstantsUtils.oDataTimeFormat());
            visitBean.setEndTime(ConstantsUtils.oDataTimeFormat());
            visitBean.setVisitCatID(Constants.str_01);
            visitBean.setStatusID(Constants.str_01);
            visitBean.setCPTypeID(Constants.str_02);
            visitBean.setSPGUID(Constants.getSPGUID());
            new OfflineUtils.ODataOfflineBuilder()
                    .setODataEntitySet(Constants.Visits)
                    .setODataEntityType(Constants.VISITENTITY)
                    .setUIListener(this)
                    .setCreate(true)
                    .setHeaderPayloadObject(visitBean)
                    .build((errorAtField, errorMessage) -> Toast.makeText(activity, "Error at field "+errorAtField.concat(" "+errorMessage), Toast.LENGTH_SHORT).show());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    */
/**
     * fetching the Target details for ULPO based on the KPISet API provided.
     * @param CPGUID to be passed to filter the targetItems.
     *//*

    public void getULPODetails(String CPGUID){
        try {
            if (CPGUID!=null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        targetItemsBean = new TargetItemsBean();
                        String kpiQuery = Constants.ConfigTypsetTypeValues+"?$filter=Typeset eq 'ZKPICD' and Types eq '01'";
                        List kpiList = new OfflineUtils.ODataOfflineBuilder<>()
                                .setHeaderPayloadObject(new ConfigTypsetTypeValuesBean())
                                .setODataEntitySet(Constants.ConfigTypsetTypeValues)
                                .setODataEntityType(".ConfigTypsetTypeValue")
                                .setUIListener(OutletListViewModel.this)
                                .setQuery(kpiQuery)
                                .returnODataList(OfflineManager.offlineStore);
                        String code="";
                        if (kpiList!=null&&!kpiList.isEmpty()) {
                            code = ((ConfigTypsetTypeValuesBean)kpiList.get(0)).getTypeValue();
                            code =UtilConstants.removeLeadingZeros(code);
                        }

                        if (!TextUtils.isEmpty(code)) {
                            String query = Constants.KPISet+"?$select=KPIGUID &$filter=ValidTo ge datetime'"+ UtilConstants.getNewDate()+"' and KPICode eq '"+code+"'";
                            String targetQuery = Constants.Targets+"?$select=TargetGUID &$filter=Period eq '"+getCurrentMonthPeriod()+"' and KPIGUID eq ";
                            String targetItemQuery = Constants.TargetItems+"?$select=TargetGUID,TargetItemGUID,KPICode,Period,Periodicity,PartnerGUID,PartnerType,MaterialNo,TargetQty," +
                                    "TargetValue,ActualQty,ActualValue,TargetAchivement,IncentiveAmount,ActualIncentiveAmt,LotSize &$filter=Period eq '"+getCurrentMonthPeriod()+"' and PartnerGUID eq '"+CPGUID.replaceAll("-","").toUpperCase()+"' and SPGUID eq guid'"+ Constants.getSPGUID() +"' and TargetGUID eq ";
                            List list = new ArrayList();
                            */
/**
                             * fetching the KPI Header based on the Typeset value ULPO provided in query.
                             *//*

                            list = new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new KPISetBean())
                                    .setODataEntitySet(Constants.KPISet)
                                    .setODataEntityType(".KPI")
                                    .setUIListener(OutletListViewModel.this)
                                    .setQuery(query)
                                    .returnODataList(OfflineManager.offlineStore);
                            if (list!=null&&!list.isEmpty()){
                                if (list.get(0) instanceof KPISetBean){
                                    KPISetBean bean = (KPISetBean) list.get(0);
                                    targetQuery=targetQuery.concat("guid'"+bean.getKPIGUID()+"'");
                                }
                            }
                            */
/**
                             * fetching the Targets based on the KPIGUID provided in query.
                             *//*

                            List targetList = new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new TargetsBean())
                                    .setODataEntitySet(Constants.Targets)
                                    .setODataEntityType(".Target")
                                    .setUIListener(OutletListViewModel.this)
                                    .setQuery(targetQuery)
                                    .returnODataList(OfflineManager.offlineStore);
                            if (targetList!=null&&!targetList.isEmpty()){
                                if (targetList.get(0) instanceof TargetsBean){
                                    TargetsBean bean = (TargetsBean) targetList.get(0);
                                    targetItemQuery=targetItemQuery.concat("guid'"+bean.getTargetGUID()+"'");
                                }
                            }
                            */
/**
                             * fetching the Target Items based on the TargetGUID provided in query.
                             *//*

                            List targetItemList =null;
                            if (!targetList.isEmpty()) {
                                targetItemList = new OfflineUtils.ODataOfflineBuilder<>()
                                        .setHeaderPayloadObject(new TargetItemsBean())
                                        .setODataEntitySet(Constants.TargetItems)
                                        .setODataEntityType(".TargetItem")
                                        .setUIListener(OutletListViewModel.this)
                                        .setQuery(targetItemQuery)
                                        .returnODataList(OfflineManager.offlineStore);
                            }
                            calculateLines(targetItemList);
                        }
                    }
                }).start();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    */
/**
     * calculating the total target lines and the target value to be shown in ULPO Targets
     * @param targetItemList
     *//*

    private void calculateLines(List targetItemList){
        try {
            double targetLines = 0;
            double actualLines = 0;
            double actualValue = 0;
            double targetValue = 0;
            try {
                if (targetItemList != null && !targetItemList.isEmpty()) {
                    for (Object object : targetItemList) {
                        if (object instanceof TargetItemsBean) {
                            TargetItemsBean bean = (TargetItemsBean) object;
                            if (bean.getTargetQty() != null && !TextUtils.isEmpty(bean.getTargetQty())) {
                                try {
                                    targetLines = targetLines + Double.parseDouble(bean.getTargetQty());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }

//                            if (bean.getTargetValue()!=null&&!TextUtils.isEmpty(bean.getTargetValue())){
//                                try {
//                                    targetValue = targetValue+Double.parseDouble(bean.getTargetValue());
//                                } catch (NumberFormatException e) {
//                                    e.printStackTrace();
//                                }
//                            }

                            if (bean.getActualQty() != null && !TextUtils.isEmpty(bean.getActualQty())) {
                                try {
                                    actualLines = actualLines + Double.parseDouble(bean.getActualQty());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }

//                            if (bean.getActualValue()!=null&&!TextUtils.isEmpty(bean.getActualValue())){
//                                try {
//                                    actualValue = actualValue+Double.parseDouble(bean.getActualValue());
//                                } catch (NumberFormatException e) {
//                                    e.printStackTrace();
//                                }
//                            }
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                try {
                    actualLines = MTDMaterialList.size();
                    targetItemsBean.setTargetQty(String.valueOf((int) targetLines));
                    targetItemsBean.setActualQty(String.valueOf((int) actualLines));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    */
/**
     * fetching the Target details for GREEN OUTLET based on the KPISet API provided.
     * @param CPGUID to be passed to filter the targetItems.
     *//*

    public void getValueTargetDetails(String CPGUID){
        try {
            if (CPGUID!=null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        targetItemsBean = new TargetItemsBean();
                        String kpiQuery = Constants.ConfigTypsetTypeValues+"?$filter=Typeset eq 'ZKPICD' and Types eq '02'";
                        List kpiList = new OfflineUtils.ODataOfflineBuilder<>()
                                .setHeaderPayloadObject(new ConfigTypsetTypeValuesBean())
                                .setODataEntitySet(Constants.ConfigTypsetTypeValues)
                                .setODataEntityType(".ConfigTypsetTypeValue")
                                .setUIListener(OutletListViewModel.this)
                                .setQuery(kpiQuery)
                                .returnODataList(OfflineManager.offlineStore);
                        String code="";
                        if (kpiList!=null&&!kpiList.isEmpty()) {
                            code = ((ConfigTypsetTypeValuesBean)kpiList.get(0)).getTypeValue();
                            code =UtilConstants.removeLeadingZeros(code);
                        }

                        if (!TextUtils.isEmpty(code)) {
                            String query = Constants.KPISet+"?$select=KPIGUID &$filter=ValidTo ge datetime'"+ UtilConstants.getNewDate()+"' and KPICode eq '"+code+"'";
                            String targetQuery = Constants.Targets+"?$select=TargetGUID &$filter=Period eq '"+getCurrentMonthPeriod()+"' and KPIGUID eq ";
                            String targetItemQuery = Constants.TargetItems+"?$select=TargetGUID,TargetItemGUID,KPICode,Period,Periodicity,PartnerGUID,PartnerType,MaterialNo,TargetQty," +
                                    "TargetValue,ActualQty,ActualValue,TargetAchivement,IncentiveAmount,ActualIncentiveAmt,LotSize &$filter=Period eq '"+getCurrentMonthPeriod()+"' and PartnerGUID eq '"+CPGUID.replaceAll("-","").toUpperCase()+"' and SPGUID eq guid'"+Constants.getSPGUID()+"' and TargetGUID eq ";
                            List list = new ArrayList();
                            */
/**
                             * fetching the KPI Header based on the Typeset value ULPO provided in query.
                             *//*

                            list = new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new KPISetBean())
                                    .setODataEntitySet(Constants.KPISet)
                                    .setODataEntityType(".KPI")
                                    .setUIListener(OutletListViewModel.this)
                                    .setQuery(query)
                                    .returnODataList(OfflineManager.offlineStore);
                            if (list!=null&&!list.isEmpty()){
                                if (list.get(0) instanceof KPISetBean){
                                    KPISetBean bean = (KPISetBean) list.get(0);
                                    targetQuery=targetQuery.concat("guid'"+bean.getKPIGUID()+"'");
                                }
                            }
                            */
/**
                             * fetching the Targets based on the KPIGUID provided in query.
                             *//*

                            List targetList = new OfflineUtils.ODataOfflineBuilder<>()
                                    .setHeaderPayloadObject(new TargetsBean())
                                    .setODataEntitySet(Constants.Targets)
                                    .setODataEntityType(".Target")
                                    .setUIListener(OutletListViewModel.this)
                                    .setQuery(targetQuery)
                                    .returnODataList(OfflineManager.offlineStore);
                            if (targetList!=null&&!targetList.isEmpty()){
                                if (targetList.get(0) instanceof TargetsBean){
                                    TargetsBean bean = (TargetsBean) targetList.get(0);
                                    targetItemQuery=targetItemQuery.concat("guid'"+bean.getTargetGUID()+"'");
                                }
                            }
                            */
/**
                             * fetching the Target Items based on the TargetGUID provided in query.
                             *//*

                            List targetItemList=null;
                            if (!targetList.isEmpty()) {
                                targetItemList = new OfflineUtils.ODataOfflineBuilder<>()
                                        .setHeaderPayloadObject(new TargetItemsBean())
                                        .setODataEntitySet(Constants.TargetItems)
                                        .setODataEntityType(".TargetItem")
                                        .setUIListener(OutletListViewModel.this)
                                        .setQuery(targetItemQuery)
                                        .returnODataList(OfflineManager.offlineStore);
                            }
                            calculateTargetValue(targetItemList,CPGUID);
                        }
                    }
                }).start();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    */
/**
     * calculating the total target lines and the target value to be shown in ULPO Targets
     * @param targetItemList
     *//*

    private void calculateTargetValue(List targetItemList,String CPGUID){
        try {
            double targetValue=0;
            double actualValue=0;
            try {
                if (targetItemList!=null&&!targetItemList.isEmpty()) {
                    for (Object object:targetItemList) {
                        if (object instanceof TargetItemsBean){
                            TargetItemsBean bean = (TargetItemsBean) object;
                            if (bean.getTargetValue()!=null&&!TextUtils.isEmpty(bean.getTargetValue())){
                                try {
                                    targetValue = targetValue+Double.parseDouble(bean.getTargetValue());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (bean.getActualValue()!=null&&!TextUtils.isEmpty(bean.getActualValue())){
                                try {
                                    actualValue = actualValue+Double.parseDouble(bean.getActualValue());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (bean.getActualValue()!=null&&!TextUtils.isEmpty(bean.getActualValue())&&Double.parseDouble(bean.getActualValue())>0){
                                isSurveyEnabled =true;
                            }else{
                                isSurveyEnabled =false;
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            String saleOrderQry = Constants.SSSOs + "?$select=SoldToCPGUID,SSSOGuid,NetPrice &$filter=OrderDate eq datetime'" + UtilConstants.getNewDate() + "' and " + Constants.SoldToCPGUID + " eq guid'" + CPGUID+ "'";
            List SSSOColorList = new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new ColorBean())
                    .setODataEntitySet("SSSOs")
                    .setODataEntityType(".SSSO")
                    .setQuery(saleOrderQry)
                    .returnODataList(OfflineManager.offlineStore);
            try {
                double soHeaderValue =0;
                double soLocalHeaderValue =OfflineManager.getSOLocalNetAmount(context,CPGUID);
                if (SSSOColorList!=null&&!SSSOColorList.isEmpty()){
                    for (Object bean :SSSOColorList) {
                        ColorBean bean1 = (ColorBean) bean;
                        if (bean1.getNetPrice()!=null&&!TextUtils.isEmpty(bean1.getNetPrice())){
                            soHeaderValue =soHeaderValue+ Double.parseDouble(bean1.getNetPrice());
                        }
                    }
                }
                soHeaderValue =soHeaderValue+soLocalHeaderValue;
                actualValue = actualValue+soHeaderValue;

                targetItemsBean.setTargetValue(String.valueOf(targetValue));
                targetItemsBean.setActualValue(String.valueOf(actualValue));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    */
/**
     * fetching the Target details for GREEN OUTLET based on the KPISet API provided.
     * @param CPGUID to be passed to filter the targetItems.
     *//*

    public void getL3MDetails(String CPGUID){
        try {
            if (CPGUID!=null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        targetItemsBean = new TargetItemsBean();
                        String kpiQuery = Constants.ConfigTypsetTypeValues+"?$filter=(Typeset eq 'ZKPICD' and Types eq '01') or (Typeset eq 'ZKPICD' and Types eq '02')";
                        List kpiList = new OfflineUtils.ODataOfflineBuilder<>()
                                .setHeaderPayloadObject(new ConfigTypsetTypeValuesBean())
                                .setODataEntitySet(Constants.ConfigTypsetTypeValues)
                                .setODataEntityType(".ConfigTypsetTypeValue")
                                .setUIListener(OutletListViewModel.this)
                                .setQuery(kpiQuery)
                                .returnODataList(OfflineManager.offlineStore);
                        String code="";

                        for (Object object:kpiList) {
                            if (object instanceof ConfigTypsetTypeValuesBean ) {
                                ConfigTypsetTypeValuesBean typeValuesBean = (ConfigTypsetTypeValuesBean) object;
                                code =UtilConstants.removeLeadingZeros(typeValuesBean.getTypeValue());
                                if (!TextUtils.isEmpty(code)) {
                                    String query = Constants.KPISet+"?$select=KPIGUID &$filter=ValidTo ge datetime'"+ UtilConstants.getNewDate()+"' and KPICode eq '"+code+"'";
                                    String targetQuery = Constants.Targets+"?$select=TargetGUID &$filter=Period eq '"+getLastMonthPeriod()+"' and KPIGUID eq ";
                                    String targetItemQuery = Constants.TargetItems+"?$select=TargetGUID,TargetItemGUID,KPICode,Period,Periodicity,PartnerGUID,PartnerType,MaterialNo,TargetQty," +
                                            "TargetValue,ActualQty,ActualValue,TargetAchivement,IncentiveAmount,ActualIncentiveAmt,LotSize &$filter=Period eq '"+getLastMonthPeriod()+"' and PartnerGUID eq '"+CPGUID.replaceAll("-","").toUpperCase()+"' and TargetGUID eq ";
                                    List list = new ArrayList();
                                    */
/**
                                     * fetching the KPI Header based on the Typeset value ULPO provided in query.
                                     *//*

                                    list = new OfflineUtils.ODataOfflineBuilder<>()
                                            .setHeaderPayloadObject(new KPISetBean())
                                            .setODataEntitySet(Constants.KPISet)
                                            .setODataEntityType(".KPI")
                                            .setUIListener(OutletListViewModel.this)
                                            .setQuery(query)
                                            .returnODataList(OfflineManager.offlineStore);
                                    if (list!=null&&!list.isEmpty()){
                                        if (list.get(0) instanceof KPISetBean){
                                            KPISetBean bean = (KPISetBean) list.get(0);
                                            targetQuery=targetQuery.concat("guid'"+bean.getKPIGUID()+"'");
                                        }
                                    }
                                    */
/**
                                     * fetching the Targets based on the KPIGUID provided in query.
                                     *//*

                                    List targetList = new OfflineUtils.ODataOfflineBuilder<>()
                                            .setHeaderPayloadObject(new TargetsBean())
                                            .setODataEntitySet(Constants.Targets)
                                            .setODataEntityType(".Target")
                                            .setUIListener(OutletListViewModel.this)
                                            .setQuery(targetQuery)
                                            .returnODataList(OfflineManager.offlineStore);
                                    if (targetList!=null&&!targetList.isEmpty()){
                                        if (targetList.get(0) instanceof TargetsBean){
                                            TargetsBean bean = (TargetsBean) targetList.get(0);
                                            targetItemQuery=targetItemQuery.concat("guid'"+bean.getTargetGUID()+"'");
                                        }
                                    }
                                    */
/**
                                     * fetching the Target Items based on the TargetGUID provided in query.
                                     *//*

                                    if (!targetList.isEmpty()) {
                                        List targetItemList = new OfflineUtils.ODataOfflineBuilder<>()
                                                .setHeaderPayloadObject(new TargetItemsBean())
                                                .setODataEntitySet(Constants.TargetItems)
                                                .setODataEntityType(".TargetItem")
                                                .setUIListener(OutletListViewModel.this)
                                                .setQuery(targetItemQuery)
                                                .returnODataList(OfflineManager.offlineStore);
                                        if (typeValuesBean.getTypeset()!=null&&TextUtils.equals(typeValuesBean.getTypes(),"01")) {
                                            calculateLMLines(targetItemList);
                                        }else{
                                            calculateLMValue(targetItemList);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }).start();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    */
/**
     * calculating the total target lines and the target value to be shown in ULPO Targets
     * @param targetItemList
     *//*

    private void calculateLMValue(List targetItemList){
        try {
            if (targetItemList!=null&&!targetItemList.isEmpty()){
                double targetValue=0;
                double actualValue=0;
                try {
                    for (Object object:targetItemList) {
                        if (object instanceof TargetItemsBean){
                            TargetItemsBean bean = (TargetItemsBean) object;
                            if (bean.getTargetValue()!=null&&!TextUtils.isEmpty(bean.getTargetValue())){
                                try {
                                    targetValue = targetValue+Double.parseDouble(bean.getTargetValue());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (bean.getActualValue()!=null&&!TextUtils.isEmpty(bean.getActualValue())){
                                try {
                                    actualValue = actualValue+Double.parseDouble(bean.getActualValue());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                try {
                    targetItemsBean.setLMTargetValue(targetValue);
                    targetItemsBean.setLMActualValue(actualValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    */
/**
     * calculating the total target lines and the target value to be shown in ULPO Targets
     * @param targetItemList
     *//*

    private void calculateLMLines(List targetItemList){
        try {
            if (targetItemList!=null&&!targetItemList.isEmpty()){
                double targetLines=0;
                double actualLines=0;
                try {
                    for (Object object:targetItemList) {
                        if (object instanceof TargetItemsBean){
                            TargetItemsBean bean = (TargetItemsBean) object;
                            if (bean.getTargetQty()!=null&&!TextUtils.isEmpty(bean.getTargetQty())){
                                try {
                                    targetLines = targetLines+Double.parseDouble(bean.getTargetQty());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (bean.getActualQty()!=null&&!TextUtils.isEmpty(bean.getActualQty())){
                                try {
                                    actualLines = actualLines+Double.parseDouble(bean.getActualQty());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    try {
                        targetItemsBean.setLMTargetQty((int) targetLines);
                        targetItemsBean.setLMActualQty((int)actualLines);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public static String getCurrentMonthPeriod(){
        String currentMonth="";
        String currentYear="";
        try {
            Calendar calendar = Calendar.getInstance();
            java.text.SimpleDateFormat month = new java.text.SimpleDateFormat("MM");
            java.text.SimpleDateFormat year = new java.text.SimpleDateFormat("yyyy");
            currentMonth = month.format(calendar.getTime());
            currentYear = year.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentMonth+currentYear;
    }
    private String getWeekOfMonth(){
        String weekMonth="";
        try {
            Calendar calendar = Calendar.getInstance();
            java.text.SimpleDateFormat week = new java.text.SimpleDateFormat("WW");
            weekMonth = week.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weekMonth;
    }
    private String getDayInWeek(){
        String dayInWeek="";
        try {
            Calendar calendar = Calendar.getInstance();
            java.text.SimpleDateFormat week = new java.text.SimpleDateFormat("uu");
            dayInWeek = week.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dayInWeek;
    }
    private String getLastMonthPeriod(){
        String currentMonth="";
        String currentYear="";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            java.text.SimpleDateFormat month = new java.text.SimpleDateFormat("MM");
            java.text.SimpleDateFormat year = new java.text.SimpleDateFormat("yyyy");
            currentMonth = month.format(calendar.getTime());
            currentYear = year.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentMonth+currentYear;
    }

    public void getCPPerformanceData(String CPGUID){
        try {
            new Thread(() -> {
                MTDMaterialList.clear();
                List list = null;
                CommonDB commonDB = new CommonDB(context);
                CPPerformanceBean cpPerformanceBean = null;
                try {
                    if (commonDB.getRecordExist(CPGUID.replaceAll("-","").toUpperCase(), DashBoard.distributorBean.getCPNo())) {
                         list = new CommonDB(context).getCPData(CPGUID.replaceAll("-","").toUpperCase(),DashBoard.distributorBean.getCPNo());
                    }else
                    {
                        String qry = Constants.CPPerformances + "?$filter=SPGUID eq guid'" + MSFAApplication.SPGUID + "' and CPGUID eq '" + CPGUID.toUpperCase().replace("-", "") + "' and ParentNo eq '" + DashBoard.distributorBean.getCPNo() + "'";

                         list = new OfflineUtils.ODataOfflineBuilder<>()
                                .setHeaderPayloadObject(new CPPerformanceBean())
                                .setODataEntitySet(Constants.CPPerformances)
                                .setODataEntityType(Constants.CPPerformanceEntity)
                                .setQuery(qry)
                                .returnODataList(OfflineManager.offlineStore);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                if (list!=null&&!list.isEmpty()){
                    cpPerformanceBean = (CPPerformanceBean) list.get(0);
                    try {
                        if(cpPerformanceBean.getString1()!=null&&!TextUtils.isEmpty(cpPerformanceBean.getString1())){
                            JSONObject jsonObject = new JSONObject(cpPerformanceBean.getString1());
                            if (jsonObject.getJSONObject("cmbilledmat")!=null) {
                                String materialValue =jsonObject.getJSONObject("cmbilledmat").getString("material");
                                if (materialValue!=null&&!TextUtils.isEmpty(materialValue)) {
                                    MTDMaterialList.addAll(Arrays.asList(materialValue.split(",")));
                                }
                            }
                            if (jsonObject.has("billmaterial")) {
                                JSONArray materialValue =jsonObject.getJSONArray("billmaterial");
                                for(int i=0;i<materialValue.length();i++){
                                    JSONObject object = materialValue.getJSONObject(i);
                                    billedMaterialList.put(object.optString("material"),object.optString("actqty"));
                                }
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    */
/**
     * This method will return CPStockItems Materials (literally All Materials)
     * the same will be utilized in NOtOrdered Screen.
     *//*

    public static String DEFAULT_UOM="PAK";
    public static void fetchStockMaterials() {
        try {
            isAllSKU = true;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    stockMaterialList = new CopyOnWriteArrayList<>();
                    schemeStockList = new CopyOnWriteArrayList();

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

                        String splitbeatQry = getSplitCPStockQry("");

                        if (splitbeatQry!=null&&!TextUtils.isEmpty(splitbeatQry)){
                            stockQry = stockQry+"&$filter= ("+splitbeatQry+")"+" and "+cpStockItemGuidQry;
                        }else
                        {
                            stockQry = stockQry+"&$filter="+cpStockItemGuidQry;
                        }
                        LogManager.writeLogDebug("BEAT :  formed query CPStockItems with prod det");
                        LogManager.writeLogError("Split beat with stock Qry : "+stockQry);
                        List materialList = new OfflineUtils.ODataOfflineBuilder<>()
                                .setHeaderPayloadObject(new CPStockItemBean())
                                .setODataEntitySet(Constants.CPStockItems)
                                .setODataEntityType(Constants.CPStockItemEntity)
                                .setQuery(stockQry)
                                .returnODataList(OfflineManager.offlineStore);
                        if (materialList!=null&&!materialList.isEmpty()){
                            DEFAULT_UOM = ((CPStockItemBean)materialList.get(0)).getUOM();
                        }
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
                                for (CPStockItemBean bean:(List<CPStockItemBean>)materialList) {
                                    */
/**
                                     * After fetching CPStockItems we need to get verrified that, material has invoiced / not.
                                     * This is the requirement to highlight materials which are invoiced.
                                     *//*

                                    boolean isInvoiced=false;
                                    if (Build.VERSION.SDK_INT >= 24) {
                                        isInvoiced = DashBoardPresenter.SSInovoiceDetailsList.stream().anyMatch(predicate -> ((SSInvoiceItemDetailsBean) predicate)
                                                .getMaterialNo().equalsIgnoreCase(bean.getMaterialNo()));
                                    }else{
                                        isInvoiced = Stream.of(DashBoardPresenter.SSInovoiceDetailsList)
                                                .anyMatch(predicate -> ((SSInvoiceItemDetailsBean)predicate).getMaterialNo().equalsIgnoreCase(bean.getMaterialNo()));
                                    }
                                    bean.setInvoicedMaterial(isInvoiced);
                                    List CPstockSKUList = null;

                                    if (bean.getSKUGroup()!=null&&!TextUtils.isEmpty(bean.getSKUGroup())) {
                                        CPstockSKUList = new ArrayList();
                                        String skuGroupQry=Constants.CPStockItems + "?$select=CatalogInfo,SubBrand,UnrestrictedQty,SKUGroup,MaterialNo &$filter=SKUGroup eq '"+bean.getSKUGroup()+"' and "+cpStockItemGuidQry;
                                        CPstockSKUList = new OfflineUtils.ODataOfflineBuilder<>()
                                                .setHeaderPayloadObject(new CPStockItemBean())
                                                .setODataEntitySet(Constants.CPStockItems)
                                                .setODataEntityType(Constants.CPStockItemEntity)
                                                .setQuery(skuGroupQry)
                                                .returnODataList(OfflineManager.offlineStore);
                                    }

                                    String CPStockSnosQry = Constants.CPStockItemSnos+ "?$select=AltUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,Tax3,Tax4,Tax5,Tax6,Tax7,Tax8,Tax9,Tax10,IntermUnitPrice,Batch,ManufacturingDate,ExpiryDate,MaterialNo,MaterialDesc " +
                                            "&$filter=StockTypeID eq '1' and CPStockItemGUID eq guid'"+bean.getCPStockItemGUID().toUpperCase()+"' and ";
                                    if (CPstockSKUList!=null&&!CPstockSKUList.isEmpty()) {
                                        double UnrestrictedQty = 0.0;
                                        for (int i = 0; i < CPstockSKUList.size(); i++) {
                                            Object object1 =CPstockSKUList.get(i);
                                            if (object1 instanceof CPStockItemBean){
                                                CPStockItemBean bean1 = (CPStockItemBean) object1;
                                                if (!TextUtils.isEmpty(bean1.getMaterialNo())){
                                                    if (i!=CPstockSKUList.size()-1) {
                                                        CPStockSnosQry =CPStockSnosQry.concat("MaterialNo eq '"+bean1.getMaterialNo()+"' or ");
                                                    }else{
                                                        CPStockSnosQry =CPStockSnosQry.concat("MaterialNo eq '"+bean1.getMaterialNo()+"' &$orderby=ManufacturingDate desc &$top=1");
                                                    }
                                                }
                                                if(!TextUtils.isEmpty(bean1.getUnrestrictedQty())){
                                                    UnrestrictedQty = UnrestrictedQty+Double.parseDouble(bean1.getUnrestrictedQty());
                                                }
                                            }
                                        }
                                        bean.setUnrestrictedQty(UnrestrictedQty+"");
                                        CPStockItemSnoBean snoBean=null;
                                        try {
                                            snoBean=OfflineManager.getMRP(CPStockSnosQry);
                                        } catch (OfflineODataStoreException e) {
                                            e.printStackTrace();
                                        }
                                        if(snoBean!=null) {
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
                                        }
                                        ArrayList<String> strUOM = new ArrayList<>();
                                        strUOM.add(bean.getUOM());
                                        strUOM.add(bean.getAlternativeUOM1());
                                        bean.setStrUOM(strUOM);
                                        stockMaterialList.add(bean);
                                    }else{
                                        CPStockItemSnoBean snoBean=null;
                                        try {
                                            String query= Constants.CPStockItemSnos+ "?$select=AltUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,Tax3,Tax4,Tax5,Tax6,Tax7,Tax8,Tax9,Tax10,IntermUnitPrice,Batch,ManufacturingDate,ExpiryDate,MaterialNo,MaterialDesc " +
                                                    "&$filter=StockTypeID eq '1' and CPStockItemGUID eq guid'"+bean.getCPStockItemGUID().toUpperCase()+"'";
                                            snoBean=OfflineManager.getMRP(query);
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                        if(snoBean!=null) {
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
                                        stockMaterialList.add(bean);
                                    }
                                }
                                LogManager.writeLogDebug("BEAT :  fetched CPStockItems and CPStockItemDetails with SKUGrp");
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }

                    }
                    String summaryQry = Constants.CPSummary1Set;
                    List salesSummaryList = new OfflineUtils.ODataOfflineBuilder<>()
                            .setHeaderPayloadObject(new CPSummary1SetBean())
                            .setODataEntitySet(Constants.CPSummary1Set)
                            .setODataEntityType(Constants.CPSummary1)
                            .setQuery(summaryQry)
                            .returnODataList(OfflineManager.offlineStore);
                    String strSalesSummary = "";
                    try {
                        HashMap<String, JSONObject> whieListHashMap = new HashMap<>();
                        HashMap<String, JSONObject> blackListHashMap = new HashMap<>();
                        JSONArray whiteListArray = new JSONArray();
                        JSONArray blackListArray = new JSONArray();
                        if (!salesSummaryList.isEmpty()) {
                            CPSummary1SetBean cpSummary1SetBean = (CPSummary1SetBean) salesSummaryList.get(0);
                            strSalesSummary = cpSummary1SetBean.getSummary();
                            JSONArray jsonArray = new JSONArray(strSalesSummary);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    String strCpUid = jsonObject.optString("cpGuid").replaceAll("-","").toUpperCase();
                                    if (strCpUid.equalsIgnoreCase(cpguidUID.replaceAll("-","").toUpperCase())) {
                                        whiteListArray = new JSONArray(jsonObject.getString("whitelist"));
                                        blackListArray = new JSONArray(jsonObject.getString("blacklist"));
                                        for(int l=0;l<whiteListArray.length();l++){
                                            JSONObject object = whiteListArray.getJSONObject(l);
                                            try {
                                                int materialNo =Integer.parseInt( object.optString("matnr"));
                                                whieListHashMap.put(""+materialNo,object);
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        for(int l=0;l<blackListArray.length();l++){
                                            JSONObject object = blackListArray.getJSONObject(l);
                                            try {
                                                int materialNo =Integer.parseInt( object.optString("matnr"));
                                                blackListHashMap.put(""+materialNo,object);
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if(!whieListHashMap.isEmpty()){
                            List<Object> stockMaterialListTemp = new ArrayList<>();
                            for(int ll=0;ll<stockMaterialList.size();ll++){
                                CPStockItemBean cpStockItemBean = (CPStockItemBean) stockMaterialList.get(ll);
                                if(whieListHashMap.containsKey(cpStockItemBean.getSKUGroup())){
                                    JSONObject object = whieListHashMap.get(cpStockItemBean.getSKUGroup());
                                    if(object.has(Constants.minQty)){
                                        cpStockItemBean.setMinQty(object.optString(Constants.minQty));
                                    }
                                    if(object.has(Constants.uom)){
                                        cpStockItemBean.setMinuom(object.optString(Constants.uom));
                                    }
                                    if(object.has(Constants.maxQty)){
                                        cpStockItemBean.setMaxQty(object.optString(Constants.maxQty));
                                    }
//                                    stockMaterialListTemp.add(cpStockItemBean);
                                }
                            }
//                            if(!stockMaterialListTemp.isEmpty()) {
//                                stockMaterialList.clear();
//                                stockMaterialList.addAll(stockMaterialListTemp);
//                            }
                        }*/
/* else {*//*

                            if (!blackListHashMap.isEmpty()) {
                                List<Object> stockMaterialListTemp = new ArrayList<>();
                                for (int ll = 0; ll < stockMaterialList.size(); ll++) {
                                    CPStockItemBean cpStockItemBean = (CPStockItemBean) stockMaterialList.get(ll);
                                    if (!blackListHashMap.containsKey(cpStockItemBean.getSKUGroup())) {
                                        stockMaterialListTemp.add(cpStockItemBean);
                                    }
                                }
                                stockMaterialList.clear();
                                stockMaterialList.addAll(stockMaterialListTemp);
                            }
                        */
/*}*//*

                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (stockMaterialList != null && !stockMaterialList.isEmpty()) {
                            Set<Object> set = new HashSet<>(stockMaterialList);
                            stockMaterialList.clear();
                            stockMaterialList.addAll(set);
                            Collections.sort(stockMaterialList, new Comparator<Object>() {
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
                    LogManager.writeLogDebug("BEAT :  fetch CPStock materials completed");
                    for (String value : MTDMaterialList) {
                        if (Build.VERSION.SDK_INT >= 24) {
                            if (stockMaterialList!=null&&!stockMaterialList.isEmpty()) {
                                stockMaterialList.stream().filter(predicate -> ((CPStockItemBean) predicate)
                                        .getSKUGroup().equalsIgnoreCase(value))
                                        .forEach(predicate -> {
                                            ((CPStockItemBean) predicate).setBilledMaterial(true);
                                        });
                            }
                        } else {
                            if (stockMaterialList!=null&&!stockMaterialList.isEmpty()) {
                                Stream.of(stockMaterialList)
                                        .filter(predicate -> ((CPStockItemBean) predicate).getSKUGroup().equalsIgnoreCase(value))
                                        .forEach(predicate -> {
                                            ((CPStockItemBean) predicate).setBilledMaterial(true);
                                        });
                            }
                        }
                    }
                    schemeStockList = stockMaterialList;
                    isAllSKU = false;
                }
            });
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void fetchBreadSPStockMaterials(String AWCode) {
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    spstockMaterialList = new CopyOnWriteArrayList<>();
                    spstockMaterialList.clear();
                    schemeStockList = new CopyOnWriteArrayList();
                    schemeStockList.clear();
                    String stockQry = "";
                    if (!TextUtils.isEmpty(AWCode)) {
                        int desiredLength = 10;
                        // Pad the string with leading zeros
                        String distributorCode = String.format("%0" + desiredLength + "d", Integer.parseInt(AWCode));
                        String additionalQuery = "StorageLoc eq '" + distributorCode + "'";
                        stockQry = Constants.SPStockItems + "?$select=CatalogInfo,HSNCode,ProdHier7,MatGrpId,MatGrpDesc,SkuGroup,AlternativeUOM2,AlternativeUOM2Num,AlternativeUOM2Den,AlternativeUOM1,AlternativeUOM1Num,AlternativeUOM1Den,StockValue,StockTypeID,StockTypeDesc,SPStockItemGUID,MaterialNo,MaterialDesc,MRP,UnrestrictedQty,UOM,Currency,LandingPrice,Brand,BrandDesc,OrderMaterialGroupDesc,OrderMaterialGroupID,StockOwner,StockOwnerDesc &$filter=" + additionalQuery;
                    } else {
                        stockQry = Constants.SPStockItems + "?$select=CatalogInfo,HSNCode,ProdHier7,MatGrpId,MatGrpDesc,SkuGroup,AlternativeUOM2,AlternativeUOM2Num,AlternativeUOM2Den,AlternativeUOM1,AlternativeUOM1Num,AlternativeUOM1Den,StockValue,StockTypeID,StockTypeDesc,SPStockItemGUID,MaterialNo,MaterialDesc,MRP,UnrestrictedQty,UOM,Currency,LandingPrice,Brand,BrandDesc,OrderMaterialGroupDesc,OrderMaterialGroupID,StockOwner,StockOwnerDesc";
                    }

                    */
/**
                     * Appending split beat query if it is splitbeat
                     *//*

                    String splitbeatQry = getSplitCPStockQry("");

                    if (splitbeatQry != null && !TextUtils.isEmpty(splitbeatQry)) {
                        stockQry = stockQry + " &$filter= (" + splitbeatQry + ")";
                    }
                    LogManager.writeLogDebug("BEAT :  formed query CPStockItems with prod det");
                    LogManager.writeLogError("Split beat with stock Qry : " + stockQry);
                    List materialList = new OfflineUtils.ODataOfflineBuilder<>()
                            .setHeaderPayloadObject(new SPStockItemBean())
                            .setODataEntitySet(Constants.SPStockItems)
                            .setODataEntityType(Constants.SPStockItemsEntity)
                            .setQuery(stockQry)
                            .returnODataList(OfflineManager.offlineStore);
                    if (materialList != null && !materialList.isEmpty()) {
                        DEFAULT_UOM = ((SPStockItemBean) materialList.get(0)).getUOM();
                    }
                    LogManager.writeLogDebug("BEAT :  fetched CPStockItems ");
                    try {
                        if (materialList!=null&&!materialList.isEmpty()) {
                            LogManager.writeLogDebug("BEAT :  fetching CPStockItems with SKUGrp");
                            for (SPStockItemBean bean:(List<SPStockItemBean>)materialList) {
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
                                String CPStockSnosQry = Constants.SPStockItemSNos+ "?$select=SPSNoGUID,Quantity,IntrmUnitPrice,AlternateUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,Tax3,Tax4,Tax5,Tax6,Tax7,Tax8,Tax9,Tax10,Batch,MFD,ExpiryDate,MaterialNo,MaterialDesc " +
                                        "&$filter=(StockTypeID eq '1' and SPStockItemGUID eq guid'"+bean.getSPStockItemGUID().toUpperCase()+"') and (ExpiryDate ge datetime'"+date+"' or ExpiryDate eq null) &$orderby=MFD desc ";
                                if (materialList!=null&&!materialList.isEmpty()) {
                                    ArrayList<SPStockItemSNoBean> itemSNOList = new ArrayList<>();
                                    SPStockItemSNoBean snoBean=null;
                                    try {
                                        itemSNOList.addAll(OfflineManager.getVANMRPStockList(CPStockSnosQry));
                                    } catch (OfflineODataStoreException e) {
                                        e.printStackTrace();
                                    }
                                    for(SPStockItemSNoBean itemSNoBean : itemSNOList){
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
                                        if(itemSNoBean!=null) {
                                            beanTemp.setRate(itemSNoBean.getUnitPrice());
                                            beanTemp.setMRP(itemSNoBean.getMRP());
                                            beanTemp.setGross(itemSNoBean.getIntermUnitPrice());
                                            beanTemp.setTax1(itemSNoBean.getTax1());
                                            beanTemp.setTax2(itemSNoBean.getTax2());
                                            beanTemp.setRateTax(itemSNoBean.getRateTax());
                                            beanTemp.setMaterialDesc(itemSNoBean.getMaterialDesc());
                                            beanTemp.setMaterialNo(itemSNoBean.getMaterialNo());
                                            beanTemp.setAlternativeUOM1(itemSNoBean.getAlternateUom());
                                            beanTemp.setAlternativeUOM1Num(itemSNoBean.getAltUomDen());
                                            beanTemp.setAlternativeUOM1Den(itemSNoBean.getAltUomNum());
                                            beanTemp.setSPSNoGUID(itemSNoBean.getSPSNoGUID());
                                            beanTemp.setUnrestrictedQty(itemSNoBean.getQuantity());
                                            String qty = "";
                                            int number = 0;
                                            String calculatedValue = "";
                                            String removedZerosValue = "";
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
                                                    if (bean.getAlternativeUOM2Den() != null) {
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
                                                                            beanTemp.setPieces(UtilConstants.removeLeadingZero(pieces));
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
                                            ArrayList<String> strUOM = new ArrayList<>();
                                             */
/* //CS
//                                            strUOM.add(beanTemp.getAlternativeUOM1());
                                            //NOS
                                            strUOM.add(beanTemp.getUOM());*//*

                                            String strAltUom = bean.getAlternativeUOM1();
                                            String strUom = bean.getUOM();
                                            String uom1 = "";
                                            String uom2 = "";
                                            if (strAltUom.equalsIgnoreCase("CS")) {
                                                uom1 = Constants.Tray;
                                            } else {
                                                uom1 = Constants.Loaf;
                                            }
                                            if (strUom.equalsIgnoreCase("CS")) {
                                                uom2 = Constants.Tray;
                                            } else {
                                                uom2 = Constants.Loaf;
                                            }
                                            strUOM.add(uom1);
                                            strUOM.add(uom2);
                                            beanTemp.setStrUOM(strUOM);

                                            spstockMaterialList.add(beanTemp);
                                        }
                                    }
                                }else{
                                    ArrayList<SPStockItemSNoBean> itemSNOList = new ArrayList<>();
                                    SPStockItemSNoBean snoBean=null;
                                    try {
                                        String query = Constants.SPStockItemSNos+ "?$select=SPSNoGUID,IntrmUnitPrice,AlternateUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,Tax3,Tax4,Tax5,Tax6,Tax7,Tax8,Tax9,Tax10,Batch,MFD,ExpiryDate,MaterialNo,MaterialDesc " +
                                                "&$filter=StockTypeID eq '1' and SPStockItemGUID eq guid'"+bean.getSPStockItemGUID().toUpperCase()+"' ";
                                        itemSNOList.addAll(OfflineManager.getVANMRPStockList(CPStockSnosQry));
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }

                                    for(SPStockItemSNoBean itemSNoBean : itemSNOList){
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
                                        if(itemSNoBean!=null) {
                                            beanTemp.setRate(itemSNoBean.getUnitPrice());
                                            beanTemp.setMRP(itemSNoBean.getMRP());
                                            beanTemp.setGross(itemSNoBean.getIntermUnitPrice());
                                            beanTemp.setTax1(itemSNoBean.getTax1());
                                            beanTemp.setTax2(itemSNoBean.getTax2());
                                            beanTemp.setRateTax(itemSNoBean.getRateTax());
                                            beanTemp.setSPSNoGUID(itemSNoBean.getSPSNoGUID());
                                            beanTemp.setMaterialDesc(itemSNoBean.getMaterialDesc());
                                            beanTemp.setMaterialNo(itemSNoBean.getMaterialNo());
                                            beanTemp.setAlternativeUOM1(itemSNoBean.getAlternateUom());
                                            beanTemp.setAlternativeUOM1Num(itemSNoBean.getAltUomDen());
                                            beanTemp.setAlternativeUOM1Den(itemSNoBean.getAltUomNum());
                                            beanTemp.setUnrestrictedQty(itemSNoBean.getQuantity());
                                            ArrayList<String> strUOM = new ArrayList<>();
                                            strUOM.add(beanTemp.getAlternativeUOM1());
                                            strUOM.add(beanTemp.getUOM());
                                            String strAltUom = bean.getAlternativeUOM1();
                                            String strUom = bean.getUOM();
                                            String uom1 = "";
                                            String uom2 = "";
                                            if (strAltUom.equalsIgnoreCase("CS")) {
                                                uom1 = Constants.Tray;
                                            } else {
                                                uom1 = Constants.Loaf;
                                            }
                                            if (strUom.equalsIgnoreCase("CS")) {
                                                uom2 = Constants.Tray;
                                            } else {
                                                uom2 = Constants.Loaf;
                                            }
                                            strUOM.add(uom1);
                                            strUOM.add(uom2);
                                            beanTemp.setStrUOM(strUOM);
                                            spstockMaterialList.add(beanTemp);
                                        }
                                    }
                                }
                            }
                            LogManager.writeLogDebug("BEAT :  fetched SPStockItems and SPStockItemDetails with SKUGrp");
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (spstockMaterialList != null && !spstockMaterialList.isEmpty()) {
                            ArrayList<String> materialNoList = new ArrayList<>();

                            List<Object> spstockMaterialListTemp = new CopyOnWriteArrayList<>();
                            for(Object Ob : spstockMaterialList){
                                SPStockItemBean beanTemp = (SPStockItemBean) Ob;
                                if(!materialNoList.contains(beanTemp.getMaterialNo()+"_"+beanTemp.getMRP())){
                                    spstockMaterialListTemp.add(Ob);
                                    materialNoList.add(beanTemp.getMaterialNo()+"_"+beanTemp.getMRP());
                                }
                            }
//                            Set<Object> set = new HashSet<>(spstockMaterialList);
                            spstockMaterialList.clear();
                            spstockMaterialList.addAll(spstockMaterialListTemp);
                            Collections.sort(spstockMaterialList, new Comparator<Object>() {
                                @Override
                                public int compare(Object o1, Object o2) {
                                    SPStockItemBean bean1 = (SPStockItemBean) o1;
                                    SPStockItemBean bean2 = (SPStockItemBean) o2;
                                   */
/* int group = 0;
                                    if ((bean1.getMatGrpId()!=null&&!bean1.getMatGrpId().isEmpty())&&(bean2.getMatGrpId()!=null&&!bean2.getMatGrpId().isEmpty())) {
                                        group = bean1.getMatGrpId().compareTo(bean2.getMatGrpId());
                                    }
                                    if (group != 0) {
                                        return group;
                                    }
                                    int brand = bean1.getBrandDesc().compareTo(bean2.getBrandDesc());
                                    if (brand != 0) return brand;*//*

                                    Double value1 = Double.valueOf(bean1.getMRP());
                                    Double value2 = Double.valueOf(bean2.getMRP());
                                    return Double.compare(value1,value2);
                                }
                            });
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    LogManager.writeLogDebug("BEAT :  fetch SPStock materials completed");
                    schemeStockList = spstockMaterialList;
                }
            });
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void clearQtyinStockMaterials(){
        if (orderedArrayList != null) orderedArrayList.clear();
        try {
            if (stockMaterialList!=null&&!stockMaterialList.isEmpty()) {
                if (Build.VERSION.SDK_INT >= 24) {
                    stockMaterialList.stream()
                            .forEach(predicate -> {
                                ((CPStockItemBean) predicate).setMinuom("");
                                ((CPStockItemBean) predicate).setMinQty("");
                                ((CPStockItemBean) predicate).setMaxQty("");
                                ((CPStockItemBean) predicate).setQAQtyTemp("");
                                ((CPStockItemBean) predicate).setQAQty("");
                                ((CPStockItemBean) predicate).setSelectedUOM("");
                                ((CPStockItemBean) predicate).setUOM(DEFAULT_UOM);
                                ((CPStockItemBean) predicate).setEditable(true);
                                String value = ((CPStockItemBean) predicate).getMaterialDesc().replace("* ","");
                                ((CPStockItemBean) predicate).setMaterialDesc(value);
                                ((CPStockItemBean) predicate).setBilledMaterial(false);
                            });
                }else{
                    Stream.of(stockMaterialList)
                            .forEach(predicate -> {
                                ((CPStockItemBean) predicate).setMinuom("");
                                ((CPStockItemBean) predicate).setMinQty("");
                                ((CPStockItemBean) predicate).setMaxQty("");
                                ((CPStockItemBean) predicate).setQAQty("");
                                ((CPStockItemBean) predicate).setSelectedUOM("");
                                ((CPStockItemBean) predicate).setUOM(DEFAULT_UOM);
                                ((CPStockItemBean) predicate).setEditable(true);
                                String value = ((CPStockItemBean) predicate).getMaterialDesc().replace("* ","");
                                ((CPStockItemBean) predicate).setMaterialDesc(value);
                                ((CPStockItemBean) predicate).setBilledMaterial(false);
                            });
                }
            }
            if (schemeStockList!=null&&!schemeStockList.isEmpty()) {
                if (Build.VERSION.SDK_INT >= 24) {
                    schemeStockList.stream()
                            .forEach(predicate -> {
                                ((CPStockItemBean) predicate).setMinuom("");
                                ((CPStockItemBean) predicate).setMinQty("");
                                ((CPStockItemBean) predicate).setMaxQty("");
                                ((CPStockItemBean) predicate).setQAQty("");
                                ((CPStockItemBean) predicate).setSelectedUOM("");
                                ((CPStockItemBean) predicate).setUOM(DEFAULT_UOM);
                                ((CPStockItemBean) predicate).setEditable(true);
                                String value = ((CPStockItemBean) predicate).getMaterialDesc().replace("* ","");
                                ((CPStockItemBean) predicate).setMaterialDesc(value);
                                ((CPStockItemBean) predicate).setBilledMaterial(false);
                            });
                }else{
                    Stream.of(schemeStockList)
                            .forEach(predicate -> {
                                ((CPStockItemBean) predicate).setMinuom("");
                                ((CPStockItemBean) predicate).setMinQty("");
                                ((CPStockItemBean) predicate).setMaxQty("");
                                ((CPStockItemBean) predicate).setQAQty("");
                                ((CPStockItemBean) predicate).setSelectedUOM("");
                                ((CPStockItemBean) predicate).setUOM(DEFAULT_UOM);
                                ((CPStockItemBean) predicate).setEditable(true);
                                String value = ((CPStockItemBean) predicate).getMaterialDesc().replace("* ","");
                                ((CPStockItemBean) predicate).setMaterialDesc(value);
                                ((CPStockItemBean) predicate).setBilledMaterial(false);
                            });
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void clearQtyinMainStockMaterials(){
        try {
            if (allStockMaterialList!=null&&!allStockMaterialList.isEmpty()) {
                if (Build.VERSION.SDK_INT >= 24) {
                    allStockMaterialList.stream()
                            .forEach(predicate -> {
                                ((CPStockItemBean) predicate).setMinuom("");
                                ((CPStockItemBean) predicate).setMinQty("");
                                ((CPStockItemBean) predicate).setMaxQty("");
                                ((CPStockItemBean) predicate).setQAQtyTemp("");
                                ((CPStockItemBean) predicate).setQAQty("");
                                ((CPStockItemBean) predicate).setSelectedUOM("");
                                ((CPStockItemBean) predicate).setUOM(DEFAULT_UOM);
                                ((CPStockItemBean) predicate).setEditable(true);
                                String value = ((CPStockItemBean) predicate).getMaterialDesc().replace("* ","");
                                ((CPStockItemBean) predicate).setMaterialDesc(value);
                                ((CPStockItemBean) predicate).setBilledMaterial(false);
                            });
                }else{
                    Stream.of(allStockMaterialList)
                            .forEach(predicate -> {
                                ((CPStockItemBean) predicate).setMinuom("");
                                ((CPStockItemBean) predicate).setMinQty("");
                                ((CPStockItemBean) predicate).setMaxQty("");
                                ((CPStockItemBean) predicate).setQAQty("");
                                ((CPStockItemBean) predicate).setSelectedUOM("");
                                ((CPStockItemBean) predicate).setUOM(DEFAULT_UOM);
                                ((CPStockItemBean) predicate).setEditable(true);
                                String value = ((CPStockItemBean) predicate).getMaterialDesc().replace("* ","");
                                ((CPStockItemBean) predicate).setMaterialDesc(value);
                                ((CPStockItemBean) predicate).setBilledMaterial(false);
                            });
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static String getSplitCPStockQry(String stockQry){
        try {

            if (productDetList!=null&&!productDetList.isEmpty()) {
                LogManager.writeLogError("fetching splitbeat data with ProdetList : "+productDetList.size());
                for (int i = 0; i < productDetList.size(); i++) {
                    Object object1 =productDetList.get(i);
                    if (object1 instanceof RouteProductDetTypeBean){
                        RouteProductDetTypeBean bean1 = (RouteProductDetTypeBean) object1;
                        if (bean1.getPrdCatgr()!=null&&TextUtils.equals(bean1.getPrdCatgr(),"000001")){
                            if (i!=productDetList.size()-1) {
                                stockQry =stockQry.concat("Brand eq '"+bean1.getProductID()+"' or ");
                            }else{
                                LogManager.writeLogError("Prd cat : "+bean1.getPrdCatgr());
                                stockQry =stockQry.concat("Brand eq '"+bean1.getProductID()+"'");
                            }

                        }else if (bean1.getPrdCatgr()!=null&&TextUtils.equals(bean1.getPrdCatgr(),"000002")){
                            if (i!=productDetList.size()-1) {
                                stockQry =stockQry.concat("SubBrand eq '"+bean1.getProductID()+"' or ");
                            }else{
                                LogManager.writeLogError("Prd cat : "+bean1.getPrdCatgr());
                                stockQry =stockQry.concat("SubBrand eq '"+bean1.getProductID()+"'");
                            }
                        }else if (bean1.getPrdCatgr()!=null&&TextUtils.equals(bean1.getPrdCatgr(),"000003")){
                            // TODO productType which is not confirmed from backend
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

    ODataPropMap oDataProperties;
    ODataProperty oDataProperty;
    public void onSave(com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean retailerSelectionBean) {
        LogManager.writeLogInfo("Before Update Retailer in offline");
        try {
            Hashtable table = new Hashtable();
            String retDetgry = Constants.ChannelPartners + "(guid'" + retailerSelectionBean.getCPGUID().toUpperCase() + "')";
            ODataEntity retilerEntity = null;
            try {
                retilerEntity = OfflineManager.getRetDetails(retDetgry);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            try {
                oDataProperties = retilerEntity.getProperties();
                oDataProperty = oDataProperties.get(Constants.CPGUID);
                table.put(Constants.CPGUID, retailerSelectionBean.getCPGUID().toUpperCase());
                try {
                    ODataProperty oDataProperty1 = oDataProperties.get(Constants.Mobile3);
                    LogManager.writeLogInfo("Update Retailer Mobile3 Value : "+oDataProperty1.getValue().toString());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    table.put(Constants.MobileNo, retailerSelectionBean.getMobile1());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    table.put(Constants.VerifiedBy, Constants.getSPGUID().replaceAll("-",""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    table.put(Constants.VerificationDat, UtilConstants.getNewDateTimeFormat());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if(!TextUtils.isEmpty(retailerSelectionBean.getOwnerName())) {
                        table.put(Constants.OwnerName, retailerSelectionBean.getOwnerName());
                    }
                    if(!TextUtils.isEmpty(retailerSelectionBean.getStateID())) {
                        table.put(Constants.StateID, retailerSelectionBean.getStateID());
                    }
                    if(!TextUtils.isEmpty(retailerSelectionBean.getStateDesc())) {
                        table.put(Constants.StateDesc, retailerSelectionBean.getStateDesc());
                    }
                    if(!TextUtils.isEmpty(retailerSelectionBean.getDistrictID())) {
                        table.put(Constants.DistrictID, retailerSelectionBean.getDistrictID());
                    }
                    if(!TextUtils.isEmpty(retailerSelectionBean.getDistrictDesc())) {
                        table.put(Constants.DistrictDesc, retailerSelectionBean.getDistrictDesc());
                    }
                    if(!TextUtils.isEmpty(retailerSelectionBean.getSubDistrictID())) {
                        table.put(Constants.CityID, retailerSelectionBean.getSubDistrictID());
                    }
                    if(!TextUtils.isEmpty(retailerSelectionBean.getSubDistrictDesc())) {
                        table.put(Constants.CityDesc, retailerSelectionBean.getSubDistrictDesc());
                    }
                    if(!TextUtils.isEmpty(retailerSelectionBean.getTownID())) {
                        table.put(Constants.TownID, retailerSelectionBean.getTownID());
                    }
                    if(!TextUtils.isEmpty(retailerSelectionBean.getTownDesc())) {
                        table.put(Constants.TownDesc, retailerSelectionBean.getTownDesc());
                    }
                    if(!TextUtils.isEmpty(retailerSelectionBean.getSubDistrictGUID())) {
                        table.put(Constants.SubDistrictGUID, retailerSelectionBean.getSubDistrictGUID());
                    }
                    if(!TextUtils.isEmpty(retailerSelectionBean.getAddress1())) {
                        table.put(Constants.Address1, retailerSelectionBean.getAddress1());
                    }

                    if(!TextUtils.isEmpty(retailerSelectionBean.getLandline())) {
                        table.put(Constants.Landline, retailerSelectionBean.getLandline());
                    }
                    if(!TextUtils.isEmpty(retailerSelectionBean.getMobileVerifed())) {
                        LogManager.writeLogInfo("Update Retailer Mobile Verifed Value : true");
                        table.put(Constants.MobileVerifed, retailerSelectionBean.getMobileVerifed());
                    }else {
                        LogManager.writeLogInfo("Update Retailer Mobile Verifed Value : false");
                        table.put(Constants.MobileVerifed, "false");
                    }
                    if(retailerSelectionBean.getURL1()!=null&&!TextUtils.isEmpty(retailerSelectionBean.getURL1())) {
                        table.put(Constants.URL1, retailerSelectionBean.getURL1());
                    }
                    if(retailerSelectionBean.getURL2()!=null&&!TextUtils.isEmpty(retailerSelectionBean.getURL2())) {
                        table.put(Constants.URL2, retailerSelectionBean.getURL2());
                    }
                    if(retailerSelectionBean.getURL3()!=null&&!TextUtils.isEmpty(retailerSelectionBean.getURL3())) {
                        table.put(Constants.URL3, retailerSelectionBean.getURL3());
                    }
                    if(retailerSelectionBean.getURL4()!=null&&!TextUtils.isEmpty(retailerSelectionBean.getURL4())) {
                        table.put(Constants.URL4, retailerSelectionBean.getURL4());
                    }
                    if(retailerSelectionBean.getURL5()!=null&&!TextUtils.isEmpty(retailerSelectionBean.getURL5())) {
                        table.put(Constants.URL5, retailerSelectionBean.getURL5());
                    }
                    if(retailerSelectionBean.getURL6()!=null&&!TextUtils.isEmpty(retailerSelectionBean.getURL6())) {
                        table.put(Constants.URL6, retailerSelectionBean.getURL6());
                    }
                    if(retailerSelectionBean.getGeoAccuracy()!=null&&!TextUtils.isEmpty(retailerSelectionBean.getGeoAccuracy())) {
                        table.put(Constants.GeoAccuracy, retailerSelectionBean.getGeoAccuracy());
                    }
                    if(retailerSelectionBean.getTimeTaken()!=null&&!TextUtils.isEmpty(retailerSelectionBean.getTimeTaken())) {
                        table.put(Constants.TimeTaken, retailerSelectionBean.getTimeTaken());
                    }
                    if(retailerSelectionBean.getAddDetails()!=null&&!TextUtils.isEmpty(retailerSelectionBean.getAddDetails())) {
                        table.put(Constants.AddDetails, retailerSelectionBean.getAddDetails());
                    }
                    if(retailerSelectionBean.getBusinessID1()!=null&&!TextUtils.isEmpty(retailerSelectionBean.getBusinessID1())) {
                        table.put(Constants.BusinessID1, retailerSelectionBean.getBusinessID1());
                    }
                    if(retailerSelectionBean.getTax1()!=null&&!TextUtils.isEmpty(retailerSelectionBean.getTax1())) {
                        table.put(Constants.Tax1, retailerSelectionBean.getTax1());
                    }

                    try {
                        if (retailerSelectionBean.getLatitude()!=null&&!TextUtils.isEmpty(retailerSelectionBean.getLatitude())) {
                            table.put(Constants.Latitude, retailerSelectionBean.getLatitude());
                        }else{
                            table.put(Constants.Latitude, "0.0");
                        }
                        if (retailerSelectionBean.getLongitude()!=null&&!TextUtils.isEmpty(retailerSelectionBean.getLongitude())) {
                            table.put(Constants.Longitude, retailerSelectionBean.getLongitude());
                        }else{
                            table.put(Constants.Longitude, "0.0");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(!TextUtils.isEmpty(retailerSelectionBean.getTIN())) {
                        table.put(Constants.PostalCode, retailerSelectionBean.getTIN());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    table.put(Constants.ParentID, "");
                    table.put(Constants.ParentName, "");
                    table.put(Constants.ParentTypeID, "");
                    table.put(Constants.ParentTypDesc, "");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                table.put(Constants.SetResourcePath, Constants.ChannelPartners + "(guid'" + retailerSelectionBean.getCPGUID().toUpperCase() + "')");
                if (retilerEntity.getEtag() != null) {
                    table.put(Constants.Etag, retilerEntity.getEtag());
                }

            } catch (Exception e) {
                e.printStackTrace();
                LogManager.writeLogError(Constants.error_txt + e.getMessage());
            }


            if (retilerEntity != null) {
                try {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.showProgress();
                        }
                    });
                    OfflineManager.updateRetilerBatchReqListners(table, Constants.Latitude, retilerEntity.getProperties(), new UIListener() {
                        @Override
                        public void onRequestError(int i, Exception e) {
                            LogManager.writeLogError("Error in Reverification outlet update listener : "+e.getMessage());
                            navigateToAddress();
                        }

                        @Override
                        public void onRequestSuccess(int i, String s) throws ODataException, OfflineODataStoreException {
                            LogManager.writeLogInfo("Success in Reverification outlet update Successfully");
                            navigateToAddress();
                        }
                    });
//                    navigateToAddress();
//                    createOnline();
                } catch (ODataParserException e) {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.hideProgress();
                        }
                    });
                    e.printStackTrace();
                }
                //            char[] requestBatch = OnlineManager.getRetailerBatchChangeSetReq(table,retilerEntity.getProperties());
                */
/*new Thread(()->{
                    OnlineManager.createRetilerBatchReq(getContext(),mStrCPGUID.toUpperCase());
                }).start();*//*


            }
        } catch (Throwable e) {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.hideProgress();
                }
            });
            e.printStackTrace();
        }
    }

    private void navigateToAddress() {
        LogManager.writeLogInfo("After Update Retailer in offline");
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.hideProgress();
                cancelTimer();
                updateRetailerData();
                view.showMessage("Retailer Updated Successfully", 1);
            }
        });
    }
    public void onReview(com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean retailerSelectionBean, String mobile1){
        LogManager.writeLogInfo("Before Online call Retailer");
        onReviewCPData(retailerSelectionBean,mobile1);
    }
    private void createOnline(){
        try {
            if (UtilConstants.isNetworkAvailable(context)) {
                if (!Constants.iSAutoSync) {
                    ArrayList<String> list = new ArrayList<>(SyncUtils.getRetailer());
                    new UpdateFlushDataAsyncTask(list).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                }else{
                    LogManager.writeLogInfo("AutoSync is in progress, Background Sync cannot be performed.");
                }
            }else{
                LogManager.writeLogInfo("Flush failed due to network unavailablity");
            }
        } catch (Throwable e) {
            LogManager.writeLogInfo("Unable to Update retailer due to an error \n"+e.toString());
        }
    }

    public class UpdateFlushDataAsyncTask extends AsyncTask<Void, Void, Void>implements UIListener {
        private ArrayList<String> alFlushColl;
        private String concatFlushCollStr = "";

        public UpdateFlushDataAsyncTask( ArrayList<String> flushColl) {
            this.alFlushColl = flushColl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.showProgress();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);

                for (int incVal = 0; incVal < alFlushColl.size(); incVal++) {
                    if (incVal == 0 && incVal == alFlushColl.size() - 1) {
                        concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal);
                    } else if (incVal == 0) {
                        concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal) + ", ";
                    } else if (incVal == alFlushColl.size() - 1) {
                        concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal);
                    } else {
                        concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal) + ", ";
                    }
                }

                try {
                    if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                        try {
                            OfflineManager.flushQueuedRequests(this, concatFlushCollStr);
                        } catch (OfflineODataStoreException e) {
                            view.hideProgress();
                        }
                    }
                } catch (Throwable e) {
                    view.hideProgress();
                }

            } catch (Throwable e) {
                view.hideProgress();
            }
            return null;
        }

        @Override
        public void onRequestError(int operation, Exception e) {
            view.hideProgress();
            view.showMessage(e.toString(),1);
        }

        @Override
        public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
            if (operation == Operation.OfflineFlush.getValue()) {
                if (UtilConstants.isNetworkAvailable(context)) {
                    OfflineManager.refreshStoreSync(context, this,Constants.Fresh, concatFlushCollStr);
                }else{
                    view.showMessage("Synchronization not completed due to network unavailablity",1);
                }
            }else if (operation == Operation.OfflineRefresh.getValue()) {
                updateRetailerData();
                view.showMessage("Retailer Updated Successfully", 1);
            }
        }
    }

    */
/**
     * This method will update retailer data we fetch from CP after re-verify of customer.
     *//*

    private void updateRetailerData(){
        try {
            String CPGUID = OutletListFragment.selectedID;
            String routeCPQry = Constants.ChannelPartners + "?$select=Mobile3,Group3Desc,Landline,Name,PostalCode,MobileVerifed,OwnerName,CPUID,CPGUID,Address1,Address2,Address3,Mobile1,CPTypeID,CPNo,CPTypeDesc,ActivationDate,DOB,Group8,Group2,Group8Desc,Group2Desc,StateID,StateDesc,DistrictID,DistrictDesc,CityID,CityDesc,TownID,TownDesc,WardID,WardName,WardGUID,SubDistrictGUID,Group1 " +
                    "&$filter=(ApprvlStatusID eq '03' and StatusID eq '01' and (CPTypeID eq '20' or CPTypeID eq '10') and CPGUID eq guid'"+CPGUID+"')";
            final List list = new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean())
                    .setODataEntitySet(Constants.ChannelPartners)
                    .setODataEntityType(Constants.ChannelPartnerEntity)
                    .setQuery(routeCPQry)
                    .returnODataList(OfflineManager.offlineStore);
            OptionalInt indexOpt = null;
            int value = -1;
            try {
                if (Build.VERSION.SDK_INT >= 24) {
                    indexOpt = IntStream.range(0, DashBoardPresenter.retailersList.size())
                            .filter(i -> CPGUID.equals(((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean)DashBoardPresenter.retailersList.get(i)).getCPGUID()))
                            .findFirst();
                    if (indexOpt.isPresent()) {
                        value = indexOpt.getAsInt();
                    }
                } else {
                    com.annimon.stream.OptionalInt index = com.annimon.stream.IntStream.range(0, DashBoardPresenter.retailersList.size())
                            .filter(i -> CPGUID.equals(((com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean)DashBoardPresenter.retailersList.get(i)).getCPGUID()))
                            .findFirst();
                    if (!index.isEmpty()&&index.isPresent()) {
                        value = index.getAsInt();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (value!=-1&&(list!=null&&!list.isEmpty())) {
                try {
                    com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean bean = (com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean) list.get(0);
                    bean.setBeatGUID(OutletListFragment.beatGUID);
                    bean.setSequenceNo(OutletListFragment.sequenceNo_update);
                    bean.setColor(OutletListFragment.color);
                    bean.setStatePosition(OutletListFragment.statePosition);
                    bean.setOLocked(OutletListFragment.isOLocked);
                    bean.setOrdered(OutletListFragment.isOrdered);
                    bean.setWarned(OutletListFragment.isWarned);
                    DashBoardPresenter.retailersList.set(value,bean);
                    LogManager.writeLogInfo("Read After Inserting Mobile3 Value : "+bean.getMobile3());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.updateRetailerData(bean);
                        }
                    });
                } catch (Throwable e) {
                    view.hideProgress();
                }
                view.hideProgress();
            }else{
                view.hideProgress();
            }
        } catch (Throwable e) {
            view.hideProgress();
        }
    }

    private GUID refguid =null;
    JSONObject fetchJsonHeaderObject;
    RetailerVerifyAsyncTask asyncTask=null;
    List<ValueHelpBean> dmsDivisionBeanArrayList=new ArrayList<>();
    List<ValueHelpBean> dmsDivisionArrayList=new ArrayList<>();
    com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean retailerCreateBean;
    String mobile1;
    public void onReviewCPData(com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean retailerCreateBean, String mobile1) {
        try {
            this.retailerCreateBean=retailerCreateBean;
            this.mobile1=mobile1;
            fetchJsonHeaderObject = new JSONObject();
            refguid =  GUID.newRandom();
            SyncUtils.updatingDaySyncStartTime(context,Constants.add_outlet_start,Constants.StartSync,refguid.toString36().toUpperCase());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        String doc_no = (System.currentTimeMillis() + "");
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        GUID mStrGuide = null;
        if (retailerCreateBean.getCPGUID()==null||TextUtils.isEmpty(retailerCreateBean.getCPGUID())) {
            mStrGuide = GUID.newRandom();
            retailerCreateBean.setCPGUID(mStrGuide.toString());
        }
        LogManager.writeLogInfo("Verify Outlet : Accumulating CP Data ");
        try {
            fetchJsonHeaderObject.put(Constants.CPGUID, retailerCreateBean.getCPGUID());
            //noinspection unchecked
            //noinspection unchecked
            fetchJsonHeaderObject.put(Constants.CPNo, "");
            fetchJsonHeaderObject.put(Constants.PartnerMgrName,"");
            fetchJsonHeaderObject.put(Constants.PartnerMgrNo,"");
            fetchJsonHeaderObject.put(Constants.Name,retailerCreateBean.getName());
            fetchJsonHeaderObject.put(Constants.CPUID,"");
            fetchJsonHeaderObject.put(Constants.AccountGrp,"");
            if (retailerCreateBean.getURL1()!=null&&!TextUtils.isEmpty(retailerCreateBean.getURL1())) {
                fetchJsonHeaderObject.put(Constants.URL1,retailerCreateBean.getURL1());
            }
            if (retailerCreateBean.getURL2()!=null&&!TextUtils.isEmpty(retailerCreateBean.getURL2())) {
                fetchJsonHeaderObject.put(Constants.URL2,retailerCreateBean.getURL2());
            }
            if (retailerCreateBean.getURL3()!=null&&!TextUtils.isEmpty(retailerCreateBean.getURL3())) {
                fetchJsonHeaderObject.put(Constants.URL3,retailerCreateBean.getURL3());
            }
            if (retailerCreateBean.getURL4()!=null&&!TextUtils.isEmpty(retailerCreateBean.getURL4())) {
                fetchJsonHeaderObject.put(Constants.URL4,retailerCreateBean.getURL4());
            }
            if (retailerCreateBean.getURL5()!=null&&!TextUtils.isEmpty(retailerCreateBean.getURL5())) {
                fetchJsonHeaderObject.put(Constants.URL5,retailerCreateBean.getURL5());
            }
            if (retailerCreateBean.getURL6()!=null&&!TextUtils.isEmpty(retailerCreateBean.getURL6())) {
                fetchJsonHeaderObject.put(Constants.URL6,retailerCreateBean.getURL6());
            }
            if (retailerCreateBean.getAddDetails()!=null&&!TextUtils.isEmpty(retailerCreateBean.getAddDetails())) {
                fetchJsonHeaderObject.put(Constants.AddDetails,retailerCreateBean.getAddDetails());
            }
            if (retailerCreateBean.getGeoAccuracy()!=null&&!TextUtils.isEmpty(retailerCreateBean.getGeoAccuracy())) {
                fetchJsonHeaderObject.put(Constants.GeoAccuracy,retailerCreateBean.getGeoAccuracy());
            }
            if (retailerCreateBean.getTimeTaken()!=null&&!TextUtils.isEmpty(retailerCreateBean.getTimeTaken())) {
                fetchJsonHeaderObject.put(Constants.TimeTaken,retailerCreateBean.getTimeTaken());
            }
            if (retailerCreateBean.getMobileVerifed()!=null&&!TextUtils.isEmpty(retailerCreateBean.getMobileVerifed())) {
                fetchJsonHeaderObject.put(Constants.MobileVerifed,retailerCreateBean.getMobileVerifed());
            }else {
                fetchJsonHeaderObject.put(Constants.MobileVerifed,"false");
            }

            if (fetchJsonHeaderObject.getString(Constants.MobileVerifed).equalsIgnoreCase("true")) {
                fetchJsonHeaderObject.put(Constants.MobileVerifed,"Y");
            }else if(fetchJsonHeaderObject.getString(Constants.MobileVerifed).equalsIgnoreCase("false")){
                fetchJsonHeaderObject.put(Constants.MobileVerifed,"N");
            }

            fetchJsonHeaderObject.put(Constants.Source, "MOBILE");
            fetchJsonHeaderObject.put(Constants.SearchTerm, "");
            fetchJsonHeaderObject.put(Constants.CPTypeID,"20");
            fetchJsonHeaderObject.put(Constants.CPTypeDesc,"");
            fetchJsonHeaderObject.put(Constants.Group1,"");
            fetchJsonHeaderObject.put(Constants.Group2,"");
            fetchJsonHeaderObject.put(Constants.Group3,"");
            fetchJsonHeaderObject.put(Constants.Group4,"");

            fetchJsonHeaderObject.put(Constants.UOM,"");

            try {
                if (retailerCreateBean.getLatitude()!=null&&!TextUtils.isEmpty(retailerCreateBean.getLatitude())) {
                    fetchJsonHeaderObject.put(Constants.Latitude, retailerCreateBean.getLatitude());
                }else{
                    fetchJsonHeaderObject.put(Constants.Latitude, "0.0");
                }
                if (retailerCreateBean.getLongitude()!=null&&!TextUtils.isEmpty(retailerCreateBean.getLongitude())) {
                    fetchJsonHeaderObject.put(Constants.Latitude, retailerCreateBean.getLatitude());
                }else{
                    fetchJsonHeaderObject.put(Constants.Latitude, "0.0");
                }
                fetchJsonHeaderObject.put(Constants.Longitude, retailerCreateBean.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (retailerCreateBean.getPAN()!=null&&!TextUtils.isEmpty(retailerCreateBean.getPAN())) {
                fetchJsonHeaderObject.put(Constants.PAN, retailerCreateBean.getPAN());
            }
            if (retailerCreateBean.getAddress1()!=null) {
                fetchJsonHeaderObject.put(Constants.Address1, retailerCreateBean.getAddress1());
            }
            if (retailerCreateBean.getAddress2()!=null) {
                fetchJsonHeaderObject.put(Constants.Address2, retailerCreateBean.getAddress2());
            }

            fetchJsonHeaderObject.put(Constants.Landmark,"");
            fetchJsonHeaderObject.put(Constants.ZoneID,"");
            fetchJsonHeaderObject.put(Constants.ZoneDesc,"");
       */
/* if (retailerCreateBean.getAddress1()!=null) {
            fetchJsonHeaderObject.put(Constants.WardGUID,retailerCreateBean.getWardGUID());
        }*//*

           */
/* if (retailerCreateBean.getTownID()!=null) {
                fetchJsonHeaderObject.put(Constants.TownID,retailerCreateBean.getTownID());
                fetchJsonHeaderObject.put(Constants.TownDesc,retailerCreateBean.getTownDesc());
                fetchJsonHeaderObject.put(Constants.TownGuid,retailerCreateBean.getTownGuid());
            }*//*

            if (retailerCreateBean.getCityID()!=null) {
                fetchJsonHeaderObject.put(Constants.CityID,retailerCreateBean.getCityID());
            }
            if (retailerCreateBean.getSubDistrictGUID()!=null) {
                fetchJsonHeaderObject.put(Constants.SubDistrictGUID,retailerCreateBean.getSubDistrictGUID());
            }
            if (retailerCreateBean.getCityDesc()!=null) {
                fetchJsonHeaderObject.put(Constants.CityDesc,retailerCreateBean.getCityDesc());
            }
            if (retailerCreateBean.getStateID()!=null) {
                fetchJsonHeaderObject.put(Constants.StateID,retailerCreateBean.getStateID());
            }
            if (retailerCreateBean.getStateDesc()!=null) {
                fetchJsonHeaderObject.put(Constants.StateDesc,retailerCreateBean.getStateDesc());
            }
            if (retailerCreateBean.getDistrictID()!=null) {
                fetchJsonHeaderObject.put(Constants.DistrictID,retailerCreateBean.getDistrictID());
            }
            if (retailerCreateBean.getDistrictDesc()!=null) {
                fetchJsonHeaderObject.put(Constants.DistrictDesc,retailerCreateBean.getDistrictDesc());
            }
            fetchJsonHeaderObject.put(Constants.Country,"IN");
            fetchJsonHeaderObject.put(Constants.CountryName,"India");
            if (retailerCreateBean.getPostalCode()!=null) {
                fetchJsonHeaderObject.put(Constants.PostalCode,retailerCreateBean.getPostalCode());
            }
            if (retailerCreateBean.getMobile1()!=null) {
                fetchJsonHeaderObject.put(Constants.Mobile1,retailerCreateBean.getMobile1());
            }
            fetchJsonHeaderObject.put(Constants.Mobile2,"");
            if (retailerCreateBean.getLandline()!=null) {
                fetchJsonHeaderObject.put(Constants.Landline,retailerCreateBean.getLandline());
            }
            if (retailerCreateBean.getEmailID()!=null) {
                fetchJsonHeaderObject.put(Constants.EmailID,retailerCreateBean.getEmailID());
            }
           */
/* fetchJsonHeaderObject.put(Constants.Currency,"INR");
            fetchJsonHeaderObject.put(Constants.CreditLimit,"");
            fetchJsonHeaderObject.put(Constants.CreditDays,"0");
            fetchJsonHeaderObject.put(Constants.StatusID,"02");
            fetchJsonHeaderObject.put(Constants.StatusDesc,"");
            fetchJsonHeaderObject.put(Constants.ApprvlStatusID,"01");
            fetchJsonHeaderObject.put(Constants.ApprvlStatusDesc,"");*//*

            if (retailerCreateBean.getOwnerName()!=null) {
                fetchJsonHeaderObject.put(Constants.OwnerName,retailerCreateBean.getOwnerName());
            }
          */
/*  fetchJsonHeaderObject.put(Constants.SalesOfficeID,"");
            fetchJsonHeaderObject.put(Constants.SalesGrpDesc,"");
            fetchJsonHeaderObject.put(Constants.SalesGroupID,"");
            fetchJsonHeaderObject.put(Constants.SalesOffDesc,"");
            fetchJsonHeaderObject.put(Constants.IsKeyCP,"");
            fetchJsonHeaderObject.put(Constants.WeeklyOff,"");
            fetchJsonHeaderObject.put(Constants.WeeklyOffDesc,"");*//*


            */
/*if (retailerCreateBean.getBusinessID1()!=null&&!TextUtils.isEmpty(retailerCreateBean.getBusinessID1())) {
                fetchJsonHeaderObject.put(Constants.BusinessID1,retailerCreateBean.getBusinessID1());
            }
            if (retailerCreateBean.getTax1()!=null&&!TextUtils.isEmpty(retailerCreateBean.getTax1())) {
                fetchJsonHeaderObject.put(Constants.Tax1,retailerCreateBean.getTax1());
            }*//*

//            fetchJsonHeaderObject.put(Constants.DeactivatedOn,"");
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            */
/*if (retailerCreateBean.getTaxRegStatus()!=null) {
                fetchJsonHeaderObject.put(Constants.TaxRegStatus,retailerCreateBean.getTaxRegStatus());
                fetchJsonHeaderObject.put(Constants.TaxRegStatusDesc,retailerCreateBean.getTaxRegStatusDesc());
            }*//*

        } catch (Exception e) {
            e.printStackTrace();
        }
        String qry = Constants.ValueHelps+"?$filter=EntityType eq 'ChannelPartner' and PropName eq 'DMSDiv'";
        Thread thread =null;
        try {
            thread = new Thread(() -> {
                dmsDivisionArrayList = new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(new ValueHelpBean())
                        .setODataEntitySet(Constants.ValueHelps)
                        .setODataEntityType(Constants.ValueHelpEntity)
                        .setUIListener(this)
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

        String query = Constants.ValueHelps+"?$filter=EntityType eq 'ChannelPartner' and PropName eq 'CPGRP1'";
//        UserProfileAuthBean userProfileAuthBean=new UserProfileAuthBean();
        Thread threadCommon =null;
        try {
            threadCommon = new Thread(() -> {
                dmsDivisionBeanArrayList = new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(new ValueHelpBean())
                        .setODataEntitySet(Constants.ValueHelps)
                        .setODataEntityType(Constants.ValueHelpEntity)
                        .setUIListener(this)
                        .setQuery(query)
                        .returnODataList(OfflineManager.offlineStore);
            });
            threadCommon.start();
            try {
                threadCommon.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (threadCommon != null && threadCommon.isAlive()) {
                threadCommon.interrupt();
            }
        }
        LogManager.writeLogInfo("Verify Outlet : CP Headere data collected ");
        JSONArray jsonArrayDMS = new JSONArray();
        JSONObject jsonObjectDMS = new JSONObject();
        try {
            // CPDMSDIVISION COMMON DIVISION

            GUID cmnDivisionGuid = GUID.newRandom();
            jsonObjectDMS.put(Constants.CPGUID, retailerCreateBean.getCPGUID());
            jsonObjectDMS.put(Constants.CP1GUID, cmnDivisionGuid.toString36().toUpperCase());
            jsonObjectDMS.put(Constants.DMSDivision, "COMMON");
            jsonObjectDMS.put(Constants.DMSDivisionDesc, "COMMON");
            jsonObjectDMS.put(Constants.StatusID, "01");
//        dmsHeaderTable.put(Constants.ParentID, MSFAApplication.DISTRIBUTOR_GUID);
            if (MSFAApplication.DISTRIBUTOR_NAME!=null) {
                jsonObjectDMS.put(Constants.ParentName, MSFAApplication.DISTRIBUTOR_NAME);
            }
            if (MSFAApplication.PARNET_TYPE_ID!=null) {
                jsonObjectDMS.put(Constants.ParentTypeID, MSFAApplication.PARNET_TYPE_ID);
            }
            jsonObjectDMS.put(Constants.Group3, "10055");
            jsonObjectDMS.put(Constants.Group3Desc, "None");
            jsonArrayDMS.put(jsonObjectDMS);
            LogManager.writeLogInfo("Verify Outlet : common division collected ");
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //CPDMSDivision
        try {
            for(ValueHelpBean valueHelpBean:dmsDivisionArrayList){
                GUID dmsDivisionGuid = GUID.newRandom();
                jsonObjectDMS = new JSONObject();
                jsonObjectDMS.put(Constants.CPGUID,retailerCreateBean.getCPGUID());
                jsonObjectDMS.put(Constants.CP1GUID,dmsDivisionGuid.toString36().toUpperCase());
                if (MSFAApplication.DISTRIBUTOR_GUID!=null) {
                    jsonObjectDMS.put(Constants.ParentID,MSFAApplication.DISTRIBUTOR_GUID);
                }
                if (MSFAApplication.PARNET_TYPE_ID!=null) {
                    jsonObjectDMS.put(Constants.ParentTypeID,MSFAApplication.PARNET_TYPE_ID);
                }
                if (valueHelpBean.getID()!=null) {
                    jsonObjectDMS.put(Constants.DMSDivision,valueHelpBean.getID());
                }
                //            jsonObjectDMS.put(Constants.PartnerMgrGUID, Constants.getSPGUID());
                if (MSFAApplication.DISTRIBUTOR_NAME!=null) {
                    jsonObjectDMS.put(Constants.ParentName, MSFAApplication.DISTRIBUTOR_NAME);
                }
                if (valueHelpBean.getDescription()!=null) {
                    jsonObjectDMS.put(Constants.DMSDivisionDesc,valueHelpBean.getDescription());
                }
                if (retailerCreateBean.getBeatGUID()!=null&&!retailerCreateBean.getBeatGUID().equalsIgnoreCase("")) {
                    jsonObjectDMS.put(Constants.RouteGUID, retailerCreateBean.getBeatGUID());
                }
                if (retailerCreateBean.getRouteID()!=null&&!retailerCreateBean.getRouteID().equalsIgnoreCase("")) {
                    jsonObjectDMS.put(Constants.RouteID, retailerCreateBean.getRouteID());
                }
                jsonObjectDMS.put(Constants.Currency,"INR");
                jsonObjectDMS.put(Constants.StatusID,"01");
                jsonArrayDMS.put(jsonObjectDMS);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        JSONArray jsonArrayRoute = new JSONArray();
        JSONObject jsonObjectRoute = new JSONObject();

        try {
            jsonObjectRoute.put(Constants.CPGuid, retailerCreateBean.getCPGUID());
            if (retailerCreateBean.getBeatGUID()!=null&&!retailerCreateBean.getBeatGUID().isEmpty()) {
                jsonObjectRoute.put(Constants.RouteGuid,retailerCreateBean.getBeatGUID().toUpperCase());
            }
            if (retailerCreateBean.getRouteDesc()!=null&&!retailerCreateBean.getRouteDesc().isEmpty()) {
                jsonObjectRoute.put(Constants.RouteDesc,retailerCreateBean.getRouteDesc());
            }
            if (retailerCreateBean.getRouteID()!=null&&!retailerCreateBean.getRouteID().isEmpty()) {
                jsonObjectRoute.put(Constants.RouteId,retailerCreateBean.getRouteID());
            }
            jsonArrayRoute.put(jsonObjectRoute);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            fetchJsonHeaderObject.put(Constants.TestRun, "X");
            LogManager.writeLogInfo("Verify Outlet : Added review flag ");
            fetchJsonHeaderObject.put(Constants.CPDMSDivisions, jsonArrayDMS);
//            fetchJsonHeaderObject.put(Constants.CPRoutes, jsonArrayRoute);
            countdown="";
            messageProgess = "Verifying Mobile number, Please wait..";
            view.showProgress(messageProgess,1);
            setTimer();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    uploadDataWithAvailableServer("","",true);
                }
            }).start();
//            asyncTask = new RetailerVerifyAsyncTask();
//            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            ConstantsUtils.checkNetwork(context, isFailed -> {
                if (isFailed) {
                    view.hideProgress();
                    cancelTimer();
                    try {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (asyncTask.getStatus()==AsyncTask.Status.RUNNING) {
                                    asyncTask.cancel(true);
                                    LogManager.writeLogError("Task cancelled while retailer verify due to poor internet connectivity");
                                }
                            }
                        });
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    LogManager.writeLogError("Unable to Verify retailer due to internet connectivity. please check internet connectivity");
                }
            },false,3);

        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
    private class RetailerVerifyAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            countdown="";
//            messageProgess = "Verifying Mobile number, Please wait..";
//            view.showProgress(messageProgess,1);
//            setTimer();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                OnlineManager.createEntity(REPEATABLE_REQUEST_ID, fetchJsonHeaderObject.toString(), Constants.ChannelPartners, new UIListener() {
                    @Override
                    public void onRequestError(int operation, Exception e) {
                        System.out.println(e.toString());
                        LogManager.writeLogInfo("Verify Outlet : error occured while outlet review\n "+e.getMessage());
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.hideProgress();
                                cancelTimer();
                                view.showMessage(e.getMessage().toString(),0);


                                try {
                                    LocationModel locationModel=null;

                                    try {
                                        locationModel = OfflineManager.getLocation(Constants.ChannelPartners+"?$filter= CPGUID eq guid'" + retailerCreateBean.getCPGUID().toUpperCase()+"'");
                                    } catch (OfflineODataStoreException e) {
                                        e.printStackTrace();
                                    }
                                    if(locationModel!=null){
                                        retailerCreateBean.setLatitude(locationModel.getLatitude());
                                        retailerCreateBean.setLongitude(locationModel.getLongitude());
                                    }
                                } catch (Throwable ex) {
                                    ex.printStackTrace();
                                }
                                retailerCreateBean.setMobile1(mobile1);
                            }
                        });
                    }

                    @Override
                    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LogManager.writeLogInfo("Verify Outlet : outlet review is successful saving outlet in offline");
//                                view.hideProgress();
//                                cancelTimer();
//                                onSave(retailerCreateBean);
                                try {
                                    saveData(retailerCreateBean,false);
                                } catch (ODataParserException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }, context);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    String countdown = "";
    long second = 0;
    String messageProgess = "";
    long milliSec = 2400000;
    CountDownTimer countDownTimer = null;

    private void startTimer(){

        ((Activity)context).runOnUiThread((new Runnable() {
            @Override
            public void run() {
                try {
                    if (countDownTimer != null) {
                        LogManager.writeLogInfo("Login Timer count Down is cancel");
                        countDownTimer.cancel();
                        countDownTimer=null;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                countDownTimer = new CountDownTimer(milliSec, 1000) {

                    public void onTick(long millisUntilFinished) {
                        long seconds = milliSec/ 1000 -millisUntilFinished / 1000;
                        long minutes = ((milliSec / 1000) / 60) -((millisUntilFinished / 1000) / 60)-1;
                        int sec = (int )seconds- (int)minutes*60;
                        countdown = String.format("%02d",minutes) + ":" + String.format("%02d",sec);
                        String timeStamp = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm", new Date());
                        view.showProgress(messageProgess+"\t\t"+countdown,1);
                        if (String.format("%02d", minutes).equalsIgnoreCase("10")) {
                            view.hideProgress();
                            if (asyncTask != null)
                                asyncTask.cancel(true);
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    context, R.style.MyTheme);
                            builder.setMessage(R.string.error_sync_msg)
                                    .setCancelable(false)
                                    .setPositiveButton(
                                            R.string.ok,
                                            (Dialog1, id) -> {
//                                                ((Activity) context).finish();
                                                if (countDownTimer != null) {
                                                    LogManager.writeLogInfo("Login Timer count Down is cancel");
                                                    countDownTimer.cancel();
                                                    countDownTimer = null;
                                                }
                                            });
                            builder.show();
                        }
                    }

                    @Override
                    public void onFinish() {

                    }

                };
                countDownTimer.start();
            }
        }));
    }
    private void cancelTimer(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (countDownTimer != null) {
                        LogManager.writeLogInfo("Login Timer count Down is cancel");
                        countDownTimer.cancel();
                        countDownTimer=null;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    */
/**
     * This method will return CPStockItems Materials (literally All Materials)
     * the same will be utilized in NOtOrdered Screen.
     *//*

    public static String DEFAULT_SP_UOM="PAK";
    public static void fetchBCRPStockMaterials() {
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    spstockMaterialList = new CopyOnWriteArrayList<>();
                    stockMaterialList = new CopyOnWriteArrayList<>();
                    stockMaterialList.clear();
                    schemeStockList = new CopyOnWriteArrayList();
                    schemeStockList.clear();
                    String stockQry = Constants.SPStockItems + "?$select="+"CatalogInfo,"+"HSNCode,ProdHier7,MatGrpId,MatGrpDesc,SkuGroup,AlternativeUOM2,AlternativeUOM2Num,AlternativeUOM2Den,AlternativeUOM1,AlternativeUOM1Num,AlternativeUOM1Den,StockValue,StockTypeID,StockTypeDesc,SPStockItemGUID,MaterialNo,MaterialDesc,MRP,UnrestrictedQty,UOM,Currency,LandingPrice,Brand,BrandDesc,OrderMaterialGroupDesc,OrderMaterialGroupID,StockOwner,StockOwnerDesc";

                    */
/**
                     * Appending split beat query if it is splitbeat
                     *//*

                    String splitbeatQry = getSplitCPStockQry("");

                    if (splitbeatQry!=null&&!TextUtils.isEmpty(splitbeatQry)){
                        stockQry = stockQry+" &$filter= ("+splitbeatQry+")";
                    }
                    LogManager.writeLogDebug("BEAT :  formed query CPStockItems with prod det");
                    LogManager.writeLogError("Split beat with stock Qry : "+stockQry);
                    List materialList = new OfflineUtils.ODataOfflineBuilder<>()
                            .setHeaderPayloadObject(new SPStockItemBean())
                            .setODataEntitySet(Constants.SPStockItems)
                            .setODataEntityType(Constants.SPStockItemsEntity)
                            .setQuery(stockQry)
                            .returnODataList(OfflineManager.offlineStore);
                    if (materialList!=null&&!materialList.isEmpty()){
                        DEFAULT_UOM = ((SPStockItemBean)materialList.get(0)).getUOM();
                    }
                    LogManager.writeLogDebug("BEAT :  fetched CPStockItems ");
                    try {
                        if (materialList!=null&&!materialList.isEmpty()) {
                            LogManager.writeLogDebug("BEAT :  fetching CPStockItems with SKUGrp");
                            for (SPStockItemBean bean:(List<SPStockItemBean>)materialList) {
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
                                String CPStockSnosQry = Constants.SPStockItemSNos+ "?$select=SPSNoGUID,Quantity,IntrmUnitPrice,AlternateUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,Tax3,Tax4,Tax5,Tax6,Tax7,Tax8,Tax9,Tax10,Batch,MFD,ExpiryDate,MaterialNo,MaterialDesc " +
                                        "&$filter=(StockTypeID eq '1' and SPStockItemGUID eq guid'"+bean.getSPStockItemGUID().toUpperCase()+"') and (ExpiryDate gt datetime'"+date+"' or ExpiryDate eq null) &$orderby=MFD desc ";
                                if (materialList!=null&&!materialList.isEmpty()) {
                                    ArrayList<SPStockItemSNoBean> itemSNOList = new ArrayList<>();
                                    SPStockItemSNoBean snoBean=null;
                                    try {
                                        itemSNOList.addAll(OfflineManager.getVANMRPStockList(CPStockSnosQry));
                                    } catch (OfflineODataStoreException e) {
                                        e.printStackTrace();
                                    }
                                    for(SPStockItemSNoBean itemSNoBean : itemSNOList){
                                        CPStockItemBean beanTemp = new CPStockItemBean();
//                                        beanTemp.setHSNCode(bean.getHSNCode());
                                        beanTemp.setCatalogInfo(bean.getCatalogInfo());
                                        beanTemp.setMatGrpID(bean.getMatGrpId());
                                        beanTemp.setProdHier7ID(bean.getProdHier7());
                                        beanTemp.setOrderMaterialGroupID(bean.getMatGrpId());
                                        beanTemp.setMatGrpDesc(bean.getMatGrpDesc());
                                        beanTemp.setSKUGroup(bean.getSkuGroup());
                                        beanTemp.setAlternativeUOM2(bean.getAlternativeUOM2());
                                        beanTemp.setAlternativeUOM2Num(bean.getAlternativeUOM2Num());
                                        beanTemp.setAlternativeUOM2Den(bean.getAlternativeUOM2Den());
                                        beanTemp.setAlternativeUOM1(bean.getAlternativeUOM1());
                                        beanTemp.setAlternativeUOM1Num(bean.getAlternativeUOM1Num());
                                        beanTemp.setAlternativeUOM1Den(bean.getAlternativeUOM1Den());
                                        beanTemp.setStockValue(bean.getStockValue());
                                        beanTemp.setStockTypeID(bean.getStockTypeID());
                                        beanTemp.setStockTypeDesc(bean.getStockTypeDesc());
                                        beanTemp.setCPStockItemGUID(bean.getSPStockItemGUID());
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
                                        if(itemSNoBean!=null) {
                                            beanTemp.setRate(itemSNoBean.getUnitPrice());
                                            beanTemp.setMRP(itemSNoBean.getMRP());
                                            beanTemp.setGross(itemSNoBean.getUnitPrice());
                                            beanTemp.setTax1(itemSNoBean.getTax1());
                                            beanTemp.setTax2(itemSNoBean.getTax2());
                                            beanTemp.setRateTax(itemSNoBean.getRateTax());
                                            beanTemp.setMaterialDesc(itemSNoBean.getMaterialDesc());
                                            beanTemp.setMaterialNo(itemSNoBean.getMaterialNo());
                                            beanTemp.setAlternativeUOM1(itemSNoBean.getAlternateUom());
                                            beanTemp.setAlternativeUOM1Num(itemSNoBean.getAltUomDen());
                                            beanTemp.setAlternativeUOM1Den(itemSNoBean.getAltUomNum());
                                            beanTemp.setCPSnoGUID(itemSNoBean.getSPSNoGUID());
                                            beanTemp.setUnrestrictedQty(itemSNoBean.getQuantity());
                                            ArrayList<String> strUOM = new ArrayList<>();
                                            strUOM.add(beanTemp.getUOM());
                                            strUOM.add(beanTemp.getAlternativeUOM1());
                                            beanTemp.setStrUOM(strUOM);
                                            spstockMaterialList.add(beanTemp);
                                        }
                                    }
                                }else{
                                    ArrayList<SPStockItemSNoBean> itemSNOList = new ArrayList<>();
                                    SPStockItemSNoBean snoBean=null;
                                    try {
                                        String query = Constants.SPStockItemSNos+ "?$select=SPSNoGUID,IntrmUnitPrice,AlternateUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,Tax3,Tax4,Tax5,Tax6,Tax7,Tax8,Tax9,Tax10,Batch,MFD,ExpiryDate,MaterialNo,MaterialDesc " +
                                                "&$filter=StockTypeID eq '1' and SPStockItemGUID eq guid'"+bean.getSPStockItemGUID().toUpperCase()+"' ";
                                        itemSNOList.addAll(OfflineManager.getVANMRPStockList(CPStockSnosQry));
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }

                                    for(SPStockItemSNoBean itemSNoBean : itemSNOList){
                                        CPStockItemBean beanTemp = new CPStockItemBean();
//                                        beanTemp.setHSNCode(bean.getHSNCode());
                                        beanTemp.setMatGrpID(bean.getMatGrpId());
                                        beanTemp.setCatalogInfo(bean.getCatalogInfo());
                                        beanTemp.setProdHier7ID(bean.getProdHier7());
                                        beanTemp.setOrderMaterialGroupID(bean.getMatGrpId());
                                        beanTemp.setMatGrpDesc(bean.getMatGrpDesc());
                                        beanTemp.setSKUGroup(bean.getSkuGroup());
                                        beanTemp.setAlternativeUOM2(bean.getAlternativeUOM2());
                                        beanTemp.setAlternativeUOM2Num(bean.getAlternativeUOM2Num());
                                        beanTemp.setAlternativeUOM2Den(bean.getAlternativeUOM2Den());
                                        beanTemp.setAlternativeUOM1(bean.getAlternativeUOM1());
                                        beanTemp.setAlternativeUOM1Num(bean.getAlternativeUOM1Num());
                                        beanTemp.setAlternativeUOM1Den(bean.getAlternativeUOM1Den());
                                        beanTemp.setStockValue(bean.getStockValue());
                                        beanTemp.setStockTypeID(bean.getStockTypeID());
                                        beanTemp.setStockTypeDesc(bean.getStockTypeDesc());
                                        beanTemp.setCPStockItemGUID(bean.getSPStockItemGUID());
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
                                        if(itemSNoBean!=null) {
                                            beanTemp.setRate(itemSNoBean.getUnitPrice());
                                            beanTemp.setMRP(itemSNoBean.getMRP());
                                            beanTemp.setGross(itemSNoBean.getUnitPrice());
                                            beanTemp.setTax1(itemSNoBean.getTax1());
                                            beanTemp.setTax2(itemSNoBean.getTax2());
                                            beanTemp.setRateTax(itemSNoBean.getRateTax());
                                            beanTemp.setCPSnoGUID(itemSNoBean.getSPSNoGUID());
                                            beanTemp.setMaterialDesc(itemSNoBean.getMaterialDesc());
                                            beanTemp.setMaterialNo(itemSNoBean.getMaterialNo());
                                            beanTemp.setAlternativeUOM1(itemSNoBean.getAlternateUom());
                                            beanTemp.setAlternativeUOM1Num(itemSNoBean.getAltUomDen());
                                            beanTemp.setAlternativeUOM1Den(itemSNoBean.getAltUomNum());
                                            beanTemp.setUnrestrictedQty(itemSNoBean.getQuantity());
                                            ArrayList<String> strUOM = new ArrayList<>();
                                            strUOM.add(beanTemp.getUOM());
                                            strUOM.add(beanTemp.getAlternativeUOM1());
                                            beanTemp.setStrUOM(strUOM);
                                            spstockMaterialList.add(beanTemp);
                                        }
                                    }
                                }
                            }
                            LogManager.writeLogDebug("BEAT :  fetched SPStockItems and SPStockItemDetails with SKUGrp");
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (spstockMaterialList != null && !spstockMaterialList.isEmpty()) {
                            ArrayList<String> materialNoList = new ArrayList<>();

                            List<Object> spstockMaterialListTemp = new CopyOnWriteArrayList<>();
                            for(Object Ob : spstockMaterialList){
                                CPStockItemBean beanTemp = (CPStockItemBean) Ob;
                                if(!materialNoList.contains(beanTemp.getMaterialNo()+"_"+beanTemp.getMRP())){
                                    spstockMaterialListTemp.add(Ob);
                                    materialNoList.add(beanTemp.getMaterialNo()+"_"+beanTemp.getMRP());
                                }
                            }
//                            Set<Object> set = new HashSet<>(spstockMaterialList);
                            stockMaterialList.clear();
                            stockMaterialList.addAll(spstockMaterialListTemp);
                            Collections.sort(spstockMaterialList, new Comparator<Object>() {
                                @Override
                                public int compare(Object o1, Object o2) {
                                    CPStockItemBean bean1 = (CPStockItemBean) o1;
                                    CPStockItemBean bean2 = (CPStockItemBean) o2;
                                    int group = 0;
                                    if ((bean1.getOrderMaterialGroupID()!=null&&!bean1.getOrderMaterialGroupID().isEmpty())&&(bean2.getOrderMaterialGroupID()!=null&&!bean2.getOrderMaterialGroupID().isEmpty())) {
                                        group = bean1.getOrderMaterialGroupID().compareTo(bean2.getOrderMaterialGroupID());
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
                    LogManager.writeLogDebug("BEAT :  fetch SPStock materials completed");
                    schemeStockList = stockMaterialList;
                }
            });
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public static void fetchSPStockMaterials() {
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    spstockMaterialList = new CopyOnWriteArrayList<>();
                    spstockMaterialList.clear();
                    schemeStockList = new CopyOnWriteArrayList();
                    schemeStockList.clear();
                    String stockQry = Constants.SPStockItems + "?$select="+"CatalogInfo,"+"HSNCode,ProdHier7,MatGrpId,MatGrpDesc,SkuGroup,AlternativeUOM2,AlternativeUOM2Num,AlternativeUOM2Den,AlternativeUOM1,AlternativeUOM1Num,AlternativeUOM1Den,StockValue,StockTypeID,StockTypeDesc,SPStockItemGUID,MaterialNo,MaterialDesc,MRP,UnrestrictedQty,UOM,Currency,LandingPrice,Brand,BrandDesc,OrderMaterialGroupDesc,OrderMaterialGroupID,StockOwner,StockOwnerDesc";

                    */
/**
                     * Appending split beat query if it is splitbeat
                     *//*

                    String splitbeatQry = getSplitCPStockQry("");

                    if (splitbeatQry!=null&&!TextUtils.isEmpty(splitbeatQry)){
                        stockQry = stockQry+" &$filter= ("+splitbeatQry+")";
                    }
                    LogManager.writeLogDebug("BEAT :  formed query CPStockItems with prod det");
                    LogManager.writeLogError("Split beat with stock Qry : "+stockQry);
                    List materialList = new OfflineUtils.ODataOfflineBuilder<>()
                            .setHeaderPayloadObject(new SPStockItemBean())
                            .setODataEntitySet(Constants.SPStockItems)
                            .setODataEntityType(Constants.SPStockItemsEntity)
                            .setQuery(stockQry)
                            .returnODataList(OfflineManager.offlineStore);
                    if (materialList!=null&&!materialList.isEmpty()){
                        DEFAULT_UOM = ((SPStockItemBean)materialList.get(0)).getUOM();
                    }
                    LogManager.writeLogDebug("BEAT :  fetched CPStockItems ");
                    try {
                        if (materialList!=null&&!materialList.isEmpty()) {
                            LogManager.writeLogDebug("BEAT :  fetching CPStockItems with SKUGrp");
                            for (SPStockItemBean bean:(List<SPStockItemBean>)materialList) {
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
                                String CPStockSnosQry = Constants.SPStockItemSNos+ "?$select=SPSNoGUID,Quantity,IntrmUnitPrice,AlternateUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,Tax3,Tax4,Tax5,Tax6,Tax7,Tax8,Tax9,Tax10,Batch,MFD,ExpiryDate,MaterialNo,MaterialDesc " +
                                        "&$filter=(StockTypeID eq '1' and SPStockItemGUID eq guid'"+bean.getSPStockItemGUID().toUpperCase()+"') and (ExpiryDate gt datetime'"+date+"' or ExpiryDate eq null) &$orderby=MFD desc ";
                                if (materialList!=null&&!materialList.isEmpty()) {
                                    ArrayList<SPStockItemSNoBean> itemSNOList = new ArrayList<>();
                                    SPStockItemSNoBean snoBean=null;
                                    try {
                                        itemSNOList.addAll(OfflineManager.getVANMRPStockList(CPStockSnosQry));
                                    } catch (OfflineODataStoreException e) {
                                        e.printStackTrace();
                                    }
                                    for(SPStockItemSNoBean itemSNoBean : itemSNOList){
                                        SPStockItemBean beanTemp = new SPStockItemBean();
                                        beanTemp.setHSNCode(bean.getHSNCode());
                                        beanTemp.setCatalogInfo(bean.getCatalogInfo());
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
                                        if(itemSNoBean!=null) {
                                            beanTemp.setRate(itemSNoBean.getUnitPrice());
                                            beanTemp.setMRP(itemSNoBean.getMRP());
                                            beanTemp.setGross(itemSNoBean.getIntermUnitPrice());
                                            beanTemp.setTax1(itemSNoBean.getTax1());
                                            beanTemp.setTax2(itemSNoBean.getTax2());
                                            beanTemp.setRateTax(itemSNoBean.getRateTax());
                                            beanTemp.setMaterialDesc(itemSNoBean.getMaterialDesc());
                                            beanTemp.setMaterialNo(itemSNoBean.getMaterialNo());
                                            beanTemp.setAlternativeUOM1(itemSNoBean.getAlternateUom());
                                            beanTemp.setAlternativeUOM1Num(itemSNoBean.getAltUomDen());
                                            beanTemp.setAlternativeUOM1Den(itemSNoBean.getAltUomNum());
                                            beanTemp.setSPSNoGUID(itemSNoBean.getSPSNoGUID());
                                            beanTemp.setUnrestrictedQty(itemSNoBean.getQuantity());
                                            ArrayList<String> strUOM = new ArrayList<>();
                                           */
/* strUOM.add(beanTemp.getUOM());
                                            strUOM.add(beanTemp.getAlternativeUOM1());
                                            beanTemp.setStrUOM(strUOM);*//*

                                            if (MSFAApplication.isBVAN) {
                                                String strAltUom = bean.getAlternativeUOM1();
                                                String strUom = bean.getUOM();
                                                String uom1 = "";
                                                String uom2 = "";
                                                if (strAltUom.equalsIgnoreCase("CS")) {
                                                    uom1 = Constants.Tray;
                                                } else {
                                                    uom1 = Constants.Loaf;
                                                }
                                                if (strUom.equalsIgnoreCase("CS")) {
                                                    uom2 = Constants.Tray;
                                                } else {
                                                    uom2 = Constants.Loaf;
                                                }
                                                strUOM.add(uom1);
                                                strUOM.add(uom2);
                                                beanTemp.setStrUOM(strUOM);

                                            } else {
                                                strUOM.add(beanTemp.getUOM());
                                                strUOM.add(beanTemp.getAlternativeUOM1());
                                                beanTemp.setStrUOM(strUOM);
                                            }
                                            spstockMaterialList.add(beanTemp);
                                        }
                                    }
                                }else{
                                    ArrayList<SPStockItemSNoBean> itemSNOList = new ArrayList<>();
                                    SPStockItemSNoBean snoBean=null;
                                    try {
                                        String query = Constants.SPStockItemSNos+ "?$select=SPSNoGUID,IntrmUnitPrice,AlternateUom,AltUomNum,AltUomDen,UnitPrice,MRP,Tax1,Tax2,Tax3,Tax4,Tax5,Tax6,Tax7,Tax8,Tax9,Tax10,Batch,MFD,ExpiryDate,MaterialNo,MaterialDesc " +
                                                "&$filter=StockTypeID eq '1' and SPStockItemGUID eq guid'"+bean.getSPStockItemGUID().toUpperCase()+"' ";
                                        itemSNOList.addAll(OfflineManager.getVANMRPStockList(CPStockSnosQry));
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }

                                    for(SPStockItemSNoBean itemSNoBean : itemSNOList){
                                        SPStockItemBean beanTemp = new SPStockItemBean();
                                        beanTemp.setHSNCode(bean.getHSNCode());
                                        beanTemp.setCatalogInfo(bean.getCatalogInfo());
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
                                        if(itemSNoBean!=null) {
                                            beanTemp.setRate(itemSNoBean.getUnitPrice());
                                            beanTemp.setMRP(itemSNoBean.getMRP());
                                            beanTemp.setGross(itemSNoBean.getIntermUnitPrice());
                                            beanTemp.setTax1(itemSNoBean.getTax1());
                                            beanTemp.setTax2(itemSNoBean.getTax2());
                                            beanTemp.setRateTax(itemSNoBean.getRateTax());
                                            beanTemp.setSPSNoGUID(itemSNoBean.getSPSNoGUID());
                                            beanTemp.setMaterialDesc(itemSNoBean.getMaterialDesc());
                                            beanTemp.setMaterialNo(itemSNoBean.getMaterialNo());
                                            beanTemp.setAlternativeUOM1(itemSNoBean.getAlternateUom());
                                            beanTemp.setAlternativeUOM1Num(itemSNoBean.getAltUomDen());
                                            beanTemp.setAlternativeUOM1Den(itemSNoBean.getAltUomNum());
                                            beanTemp.setUnrestrictedQty(itemSNoBean.getQuantity());
                                            ArrayList<String> strUOM = new ArrayList<>();
                                            if(MSFAApplication.isBVAN){
                                                String strAltUom = bean.getAlternativeUOM1();
                                                String strUom = bean.getUOM();
                                                String uom1 = "";
                                                String uom2 = "";
                                                if (strAltUom.equalsIgnoreCase("CS")) {
                                                    uom1 = Constants.Tray;
                                                } else {
                                                    uom1 = Constants.Loaf;
                                                }
                                                if (strUom.equalsIgnoreCase("CS")) {
                                                    uom2 = Constants.Tray;
                                                } else {
                                                    uom2 = Constants.Loaf;
                                                }
                                                strUOM.add(uom1);
                                                strUOM.add(uom2);
                                                beanTemp.setStrUOM(strUOM);
                                            }else {
                                                strUOM.add(beanTemp.getUOM());
                                                strUOM.add(beanTemp.getAlternativeUOM1());
                                                beanTemp.setStrUOM(strUOM);
                                            }
                                            spstockMaterialList.add(beanTemp);
                                        }
                                    }
                                }
                            }
                            LogManager.writeLogDebug("BEAT :  fetched SPStockItems and SPStockItemDetails with SKUGrp");
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (spstockMaterialList != null && !spstockMaterialList.isEmpty()) {
                            ArrayList<String> materialNoList = new ArrayList<>();

                            List<Object> spstockMaterialListTemp = new CopyOnWriteArrayList<>();
                            for(Object Ob : spstockMaterialList){
                                SPStockItemBean beanTemp = (SPStockItemBean) Ob;
                                if(!materialNoList.contains(beanTemp.getMaterialNo()+"_"+beanTemp.getMRP())){
                                    spstockMaterialListTemp.add(Ob);
                                    materialNoList.add(beanTemp.getMaterialNo()+"_"+beanTemp.getMRP());
                                }
                            }
//                            Set<Object> set = new HashSet<>(spstockMaterialList);
                            spstockMaterialList.clear();
                            spstockMaterialList.addAll(spstockMaterialListTemp);
                            Collections.sort(spstockMaterialList, new Comparator<Object>() {
                                @Override
                                public int compare(Object o1, Object o2) {
                                    SPStockItemBean bean1 = (SPStockItemBean) o1;
                                    SPStockItemBean bean2 = (SPStockItemBean) o2;
                                    int group = 0;
                                    if ((bean1.getMatGrpId()!=null&&!bean1.getMatGrpId().isEmpty())&&(bean2.getMatGrpId()!=null&&!bean2.getMatGrpId().isEmpty())) {
                                        group = bean1.getMatGrpId().compareTo(bean2.getMatGrpId());
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
                    LogManager.writeLogDebug("BEAT :  fetch SPStock materials completed");
                    schemeStockList = spstockMaterialList;
                }
            });
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void clearQtyinSPStockMaterials(){
        try {
            if (spstockMaterialList!=null&&!spstockMaterialList.isEmpty()) {
                if (Build.VERSION.SDK_INT >= 24) {
                    spstockMaterialList.stream()
                            .forEach(predicate -> {
                                ((SPStockItemBean) predicate).setQAQty("");
                                ((SPStockItemBean) predicate).setSelectedUOM("");
                                ((SPStockItemBean) predicate).setUOM(DEFAULT_UOM);
                                ((SPStockItemBean) predicate).setEditable(true);
                                String value = ((SPStockItemBean) predicate).getMaterialDesc().replace("* ","");
                                ((SPStockItemBean) predicate).setMaterialDesc(value);
                                ((SPStockItemBean) predicate).setBilledMaterial(false);
                            });
                }else{
                    Stream.of(spstockMaterialList)
                            .forEach(predicate -> {
                                ((SPStockItemBean) predicate).setQAQty("");
                                ((SPStockItemBean) predicate).setSelectedUOM("");
                                ((SPStockItemBean) predicate).setUOM(DEFAULT_UOM);
                                ((SPStockItemBean) predicate).setEditable(true);
                                String value = ((SPStockItemBean) predicate).getMaterialDesc().replace("* ","");
                                ((SPStockItemBean) predicate).setMaterialDesc(value);
                                ((SPStockItemBean) predicate).setBilledMaterial(false);
                            });
                }
            }
            if (schemeStockList!=null&&!schemeStockList.isEmpty()) {
                if (Build.VERSION.SDK_INT >= 24) {
                    schemeStockList.stream()
                            .forEach(predicate -> {
                                ((SPStockItemBean) predicate).setQAQty("");
                                ((SPStockItemBean) predicate).setSelectedUOM("");
                                ((SPStockItemBean) predicate).setUOM(DEFAULT_UOM);
                                ((SPStockItemBean) predicate).setEditable(true);
                                String value = ((SPStockItemBean) predicate).getMaterialDesc().replace("* ","");
                                ((SPStockItemBean) predicate).setMaterialDesc(value);
                                ((SPStockItemBean) predicate).setBilledMaterial(false);
                            });
                }else{
                    Stream.of(schemeStockList)
                            .forEach(predicate -> {
                                ((SPStockItemBean) predicate).setQAQty("");
                                ((SPStockItemBean) predicate).setSelectedUOM("");
                                ((SPStockItemBean) predicate).setUOM(DEFAULT_UOM);
                                ((SPStockItemBean) predicate).setEditable(true);
                                String value = ((SPStockItemBean) predicate).getMaterialDesc().replace("* ","");
                                ((SPStockItemBean) predicate).setMaterialDesc(value);
                                ((SPStockItemBean) predicate).setBilledMaterial(false);
                            });
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public void saveData(com.arteriatech.ss.msec.bil.v4.pda.retailerselection.RetailerSelectionBean retailerCreateBean, boolean isProgress) throws ODataParserException {
        messageProgess = "Verifying outlet, Please wait..";
        if(isProgress) {
            view.showProgress(messageProgess, 1);
            setTimer();
        }
        Hashtable<String, String> headerTableChannel = new Hashtable<>();
        Hashtable<String, String> headerTableDMS = new Hashtable<>();
        Hashtable<String, String> headerTableCPFunt = new Hashtable<>();

        ODataEntity channelPartnerEntity = null, cpDmsDivisionEntity = null,cpPArtnerEntity=null,infraEntity=null,bankMasterEntity=null,salepersonEntity=null,cpspEntity=null;
        ODataPropMap oDataProperties = null;
        String retDetgry = Constants.ChannelPartners + "(guid'" + retailerCreateBean.getCPGUID() + "')";
        ODataEntity retilerEntity = null;
        try {
            retilerEntity = OfflineManager.getRetDetails(retDetgry);
        } catch (OfflineODataStoreException e) {
            e.printStackTrace();
        }
        try {
            oDataProperties = retilerEntity.getProperties();
        } catch (Exception e) {
            e.printStackTrace();
            LogManager.writeLogError(Constants.error_txt + e.getMessage());
        }

        try {
            Set keySet = oDataProperties.keySet();
            Iterator itr = keySet.iterator();
            while (itr.hasNext()) {
                String key = itr.next().toString();
                try {
                    if (oDataProperties.get(key).getValue() instanceof ODataGuid) {
                        ODataGuid oDataGuid = (ODataGuid) oDataProperties.get(key).getValue();
                        String value = oDataGuid.guidAsString36();
                        if (value.equalsIgnoreCase("0000-0000")) {
                            if (oDataProperties.get(key).getName().equalsIgnoreCase("NearbyTown") || oDataProperties.get(key).getName().equalsIgnoreCase("WardGUID") || oDataProperties.get(key).getName().equalsIgnoreCase("RouteGUID") || oDataProperties.get(key).getName().equalsIgnoreCase("PartnerMgrGUID")) {
                                headerTableChannel.put(key, "00000000-0000-0000-0000-000000000000");
                            }
                        }else {
                            headerTableChannel.put(key, oDataGuid.guidAsString36());
                        }
                    } else if (oDataProperties.get(key).getValue() instanceof BigDecimal) {
                        BigDecimal mDecimalLatitude = (BigDecimal) oDataProperties.get(key).getValue();
                        headerTableChannel.put(key, "" + mDecimalLatitude.doubleValue());
                    } else if (oDataProperties.get(key).getValue() instanceof GregorianCalendar) {
                        headerTableChannel.put(key, UtilConstants.convertCalenderToStringFormat1((GregorianCalendar) oDataProperties.get(key).getValue()));
                    } else if (oDataProperties.get(key).getValue() instanceof ODataDuration) {
                        try {
                            ODataDuration oDataDuration = (ODataDuration) oDataProperties.get(key).getValue();
                            if (oDataDuration.toString().equalsIgnoreCase("P0Y")) {
                                headerTableChannel.put(key, "PT00H00M00S");
                            } else {
                                headerTableChannel.put(key, oDataDuration.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        headerTableChannel.put(key, oDataProperties.get(key).getValue().toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                headerTableChannel.remove("CreditDays");
                headerTableChannel.remove("VATNo");
                try {
                    double crdLimit= Double.parseDouble(headerTableChannel.get("CreditLimit"));
                    headerTableChannel.put("CreditLimit",""+ConstantsUtils.decimalRoundOff(BigDecimal.valueOf(crdLimit),2));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                try {
                    double openAdvanceAmt= Double.parseDouble(headerTableChannel.get("OpenAdvanceAmt"));
                    headerTableChannel.put("OpenAdvanceAmt",""+ConstantsUtils.decimalRoundOff(BigDecimal.valueOf(openAdvanceAmt),2));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                try {
                    headerTableChannel.put(Constants.MobileNo, retailerCreateBean.getMobile1());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    headerTableChannel.put(Constants.VerifiedBy, Constants.getSPGUID().replaceAll("-", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    headerTableChannel.put(Constants.VerificationDat, UtilConstants.getNewDateTimeFormat());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (!TextUtils.isEmpty(retailerCreateBean.getOwnerName())) {
                        headerTableChannel.put(Constants.OwnerName, retailerCreateBean.getOwnerName());
                    }
                    if (!TextUtils.isEmpty(retailerCreateBean.getName())) {
                        headerTableChannel.put(Constants.Name, retailerCreateBean.getName());
                    }
                    if (!TextUtils.isEmpty(retailerCreateBean.getStateID())) {
                        headerTableChannel.put(Constants.StateID, retailerCreateBean.getStateID());
                    }
                    if (!TextUtils.isEmpty(retailerCreateBean.getStateDesc())) {
                        headerTableChannel.put(Constants.StateDesc, retailerCreateBean.getStateDesc());
                    }
                    if (!TextUtils.isEmpty(retailerCreateBean.getDistrictID())) {
                        headerTableChannel.put(Constants.DistrictID, retailerCreateBean.getDistrictID());
                    }
                    if (!TextUtils.isEmpty(retailerCreateBean.getDistrictDesc())) {
                        headerTableChannel.put(Constants.DistrictDesc, retailerCreateBean.getDistrictDesc());
                    }
                    if (!TextUtils.isEmpty(retailerCreateBean.getSubDistrictID())) {
                        headerTableChannel.put(Constants.CityID, retailerCreateBean.getSubDistrictID());
                    }
                    if (!TextUtils.isEmpty(retailerCreateBean.getSubDistrictDesc())) {
                        headerTableChannel.put(Constants.CityDesc, retailerCreateBean.getSubDistrictDesc());
                    }
                    if (!TextUtils.isEmpty(retailerCreateBean.getTownID())) {
                        headerTableChannel.put(Constants.TownID, retailerCreateBean.getTownID());
                    }
                    if (!TextUtils.isEmpty(retailerCreateBean.getTownDesc())) {
                        headerTableChannel.put(Constants.TownDesc, retailerCreateBean.getTownDesc());
                    }
                    if (!TextUtils.isEmpty(retailerCreateBean.getSubDistrictGUID())) {
                        headerTableChannel.put(Constants.SubDistrictGUID, retailerCreateBean.getSubDistrictGUID());
                    }
                    if (!TextUtils.isEmpty(retailerCreateBean.getAddress1())) {
                        headerTableChannel.put(Constants.Address1, retailerCreateBean.getAddress1());
                    }

                    if (retailerCreateBean.getBusinessID1() != null && !TextUtils.isEmpty(retailerCreateBean.getBusinessID1())) {
                        headerTableChannel.put(Constants.BusinessID1, retailerCreateBean.getBusinessID1());
                    }
                    if (retailerCreateBean.getTax1() != null && !TextUtils.isEmpty(retailerCreateBean.getTax1())) {
                        headerTableChannel.put(Constants.Tax1, retailerCreateBean.getTax1());
                    }
                    double lat = Double.parseDouble(retailerCreateBean.getLatitude());
                    double lng = Double.parseDouble(retailerCreateBean.getLongitude());
                    try {
                        if (String.valueOf(lat) != null && !TextUtils.isEmpty(String.valueOf(lat))) {
                            headerTableChannel.put(Constants.Latitude, String.valueOf(lat));
                        } else {
                            headerTableChannel.put(Constants.Latitude, "0.0");
                        }
                        if (String.valueOf(lng) != null && !TextUtils.isEmpty(String.valueOf(lng))) {
                            headerTableChannel.put(Constants.Longitude, String.valueOf(lng));
                        } else {
                            headerTableChannel.put(Constants.Longitude, "0.0");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (!TextUtils.isEmpty(retailerCreateBean.getPostalCode())) {
                        headerTableChannel.put(Constants.PostalCode, retailerCreateBean.getPostalCode());
                    }
                    headerTableChannel.put(Constants.Mobile3, "Z");
                    if(!TextUtils.isEmpty(retailerCreateBean.getMobileVerifed())) {
                        LogManager.writeLogInfo("Update Retailer Mobile Verifed Value : true");
                        headerTableChannel.put(Constants.MobileVerifed, "Y");
                    }else {
                        LogManager.writeLogInfo("Update Retailer Mobile Verifed Value : false");
                        headerTableChannel.put(Constants.MobileVerifed, "N");
                    }
                    if(retailerCreateBean.getURL1()!=null&&!TextUtils.isEmpty(retailerCreateBean.getURL1())) {
                        headerTableChannel.put(Constants.URL1, retailerCreateBean.getURL1());
                    }
                    if(retailerCreateBean.getURL2()!=null&&!TextUtils.isEmpty(retailerCreateBean.getURL2())) {
                        headerTableChannel.put(Constants.URL2, retailerCreateBean.getURL2());
                    }
                    if(retailerCreateBean.getURL3()!=null&&!TextUtils.isEmpty(retailerCreateBean.getURL3())) {
                        headerTableChannel.put(Constants.URL3, retailerCreateBean.getURL3());
                    }
                    if(retailerCreateBean.getURL4()!=null&&!TextUtils.isEmpty(retailerCreateBean.getURL4())) {
                        headerTableChannel.put(Constants.URL4, retailerCreateBean.getURL4());
                    }
                    if(retailerCreateBean.getURL5()!=null&&!TextUtils.isEmpty(retailerCreateBean.getURL5())) {
                        headerTableChannel.put(Constants.URL5, retailerCreateBean.getURL5());
                    }
                    if(retailerCreateBean.getURL6()!=null&&!TextUtils.isEmpty(retailerCreateBean.getURL6())) {
                        headerTableChannel.put(Constants.URL6, retailerCreateBean.getURL6());
                    }
                    if(retailerCreateBean.getGeoAccuracy()!=null&&!TextUtils.isEmpty(retailerCreateBean.getGeoAccuracy())) {
                        headerTableChannel.put(Constants.GeoAccuracy, retailerCreateBean.getGeoAccuracy());
                    }
                    if(retailerCreateBean.getTimeTaken()!=null&&!TextUtils.isEmpty(retailerCreateBean.getTimeTaken())) {
                        headerTableChannel.put(Constants.TimeTaken, retailerCreateBean.getTimeTaken());
                    }
                    if(retailerCreateBean.getAddDetails()!=null&&!TextUtils.isEmpty(retailerCreateBean.getAddDetails())) {
                        headerTableChannel.put(Constants.AddDetails, retailerCreateBean.getAddDetails());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                headerTableChannel.put(Constants.Group1, retailerCreateBean.getGroup1());
                headerTableChannel.put(Constants.Group1Desc, retailerCreateBean.getGroup1Desc());
                headerTableChannel.put(Constants.Group2, retailerCreateBean.getGroup2());
                headerTableChannel.put(Constants.Group2Desc, retailerCreateBean.getGroup2Desc());
//                headerTableChannel.put(Constants.PAN, "");
            } catch (Exception e) {
                e.printStackTrace();
            }


            JSONObject jsonObjectChannel = new JSONObject(headerTableChannel);


            //generate uniqueId for a batch boundary
            String batchGuid = GUID.newRandom().toString32();

            //generate uniqueId for each item to be inserted
            String changeSetId = "1";

            //Begin of: Prepare Bulk Request Format for SharePoint Bulk-Insert-Query ----------------
            String batchContents = "";
            String batchCnt_Update = "";
            String endpoint_Update = null;
            try {
                batchContents = "";

                endpoint_Update = Constants.ChannelPartners + "(guid'" + retailerCreateBean.getCPGUID().toUpperCase() + "')";

                batchCnt_Update = batchCnt_Update
                        + "--changeset_" + changeSetId + "\n"
                        + "Content-Type: application/http" + "\n"
                        + "Content-Transfer-Encoding: binary" + "\n"
                        + "" + "\n"
                        + "PUT " + endpoint_Update + " HTTP/1.1" + "\n"
                        + "Content-Type: application/json" + "\n"
                        + "Accept: application/json" + "\n"
                        + "Content-Length: " + batchCnt_Update.length() + "\n"
                        + "" + "\n"
                        + jsonObjectChannel.toString() + "\n";


                //END:   changeset to update data ----------
                batchContents = batchContents + "--batch_" + batchGuid + "\n"
                        + "Content-Type: multipart/mixed; boundary=changeset_" + changeSetId + "\n"
                        + "Content-Transfer-Encoding: binary" + "\n"
                        + "" + "\n"
                        + batchCnt_Update + "\n";
            } catch (Throwable e) {
                e.printStackTrace();
            }

            String cpDMSDivDetgry = Constants.CPDMSDivisions + "?$filter=" + Constants.CPGUID + " eq guid'" + retailerCreateBean.getCPGUID() + "'";
//            String cpDMSDivDetgry = Constants.CPDMSDivisions + "?$filter=" + Constants.CPGUID + " eq guid'" + retailerCreateBean.getCPGUID() + "' and ParentID eq '"+MSFAApplication.DISTRIBUTOR_GUID+"'";
            List<ODataEntity> entities = null;

            try {
                entities = OfflineManager.getEntities(cpDMSDivDetgry);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }

            if (entities != null && entities.size() > 0) {
                for (ODataEntity cpDmsDivEntity : entities) {
                    batchCnt_Update = "";
                    headerTableDMS = new Hashtable<>();
                    try {
                        oDataProperties = cpDmsDivEntity.getProperties();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogManager.writeLogError(Constants.error_txt + e.getMessage());
                    }
                    try {
                        keySet = oDataProperties.keySet();
                        itr = keySet.iterator();
                        while (itr.hasNext()) {

                            String key = itr.next().toString();
                            try {
                                if (oDataProperties.get(key).getValue() instanceof ODataGuid) {
                                    ODataGuid oDataGuid = (ODataGuid) oDataProperties.get(key).getValue();
                                    String value = oDataGuid.guidAsString36();
                                    if (value.equalsIgnoreCase("0000-0000")) {
                                        if (oDataProperties.get(key).getName().equalsIgnoreCase("WardGUID") || oDataProperties.get(key).getName().equalsIgnoreCase("RouteGUID") || oDataProperties.get(key).getName().equalsIgnoreCase("PartnerMgrGUID")) {
                                            headerTableDMS.put(key, "00000000-0000-0000-0000-000000000000");
                                        }
                                    }else {
                                        headerTableDMS.put(key, oDataGuid.guidAsString36());
                                    }
                                } else if (oDataProperties.get(key).getValue() instanceof BigDecimal) {
                                    BigDecimal mDecimalLatitude = (BigDecimal) oDataProperties.get(key).getValue();
                                    headerTableDMS.put(key, "" + mDecimalLatitude.doubleValue());
                                } else if (oDataProperties.get(key).getValue() instanceof GregorianCalendar) {
                                    headerTableDMS.put(key, UtilConstants.convertCalenderToStringFormat1((GregorianCalendar) oDataProperties.get(key).getValue()));
                                } else if (oDataProperties.get(key).getValue() instanceof ODataDuration) {
                                    try {
                                        ODataDuration oDataDuration = (ODataDuration) oDataProperties.get(key).getValue();
                                        if (oDataDuration.toString().equalsIgnoreCase("P0Y")) {
                                            headerTableDMS.put(key, "PT00H00M00S");
                                        } else {
                                            headerTableDMS.put(key, oDataDuration.toString());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    headerTableDMS.put(key, oDataProperties.get(key).getValue().toString());
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    headerTableDMS.remove("CreditDays");
                    headerTableDMS.remove("CreditBills");
                    try {
                        double crdLimit= Double.parseDouble(headerTableDMS.get("CreditLimit"));
                        headerTableDMS.put("CreditLimit",""+ConstantsUtils.decimalRoundOff(BigDecimal.valueOf(crdLimit),2));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                   */
/* if(headerTableDMS.get("")){
                        table.put(Constants.Group1, retailerSelectionBean.getGroup1());
                        table.put(Constants.Group1Desc, retailerSelectionBean.getGroup1Desc());
                        table.put(Constants.Group2, retailerSelectionBean.getGroup2());
                        table.put(Constants.Group2Desc, retailerSelectionBean.getGroup2Desc());

                    }*//*

                    JSONObject jsonObjectDMS = new JSONObject(headerTableDMS);
                    endpoint_Update = "CPDMSDivisions(CP1GUID=guid'" + headerTableDMS.get(Constants.CP1GUID) + "')";

                    batchCnt_Update = batchCnt_Update
                            + "--changeset_" + changeSetId + "\n"
                            + "Content-Type: application/http" + "\n"
                            + "Content-Transfer-Encoding: binary" + "\n"
                            + "" + "\n"
                            + "PUT " + endpoint_Update + " HTTP/1.1" + "\n"
                            + "Content-Type: application/json" + "\n"
                            + "Accept: application/json" + "\n"
                            + "Content-Length: " + batchCnt_Update.length() + "\n"
                            + "" + "\n"
                            + jsonObjectDMS.toString() + "\n";

                    //END:   changeset to update data ----------

                    //create batch for creating items
                    batchContents = batchContents + batchCnt_Update + "\n";
                }
            }
            String cpPartnerDivDetgry = Constants.CPPartnerFunctions + "?$filter=" + Constants.CPGUID + " eq guid'" + retailerCreateBean.getCPGUID() + "' ";

            try {
                entities = OfflineManager.getEntities(cpPartnerDivDetgry);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }

            if (entities != null && entities.size() > 0) {
                for (ODataEntity cpDmsDivEntity : entities) {
                    headerTableCPFunt = new Hashtable<>();
                    try {
                        oDataProperties = cpDmsDivEntity.getProperties();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogManager.writeLogError(Constants.error_txt + e.getMessage());
                    }
                    try {
                        keySet = oDataProperties.keySet();
                        itr = keySet.iterator();
                        while (itr.hasNext()) {
                            String key = itr.next().toString();
                            try {
                                if (oDataProperties.get(key).getValue() instanceof ODataGuid) {
                                    ODataGuid oDataGuid = (ODataGuid) oDataProperties.get(key).getValue();
                                    headerTableCPFunt.put(key, oDataGuid.guidAsString36());
                                } else if (oDataProperties.get(key).getValue() instanceof BigDecimal) {
                                    BigDecimal mDecimalLatitude = (BigDecimal) oDataProperties.get(key).getValue();
                                    headerTableCPFunt.put(key, "" + mDecimalLatitude.doubleValue());
                                } else if (oDataProperties.get(key).getValue() instanceof GregorianCalendar) {
                                    headerTableCPFunt.put(key, UtilConstants.convertCalenderToStringFormat1((GregorianCalendar) oDataProperties.get(key).getValue()));
                                } else if (oDataProperties.get(key).getValue() instanceof ODataDuration) {
                                    try {
                                        ODataDuration oDataDuration = (ODataDuration) oDataProperties.get(key).getValue();
                                        if (oDataDuration.toString().equalsIgnoreCase("P0Y")) {
                                            headerTableCPFunt.put(key, "PT00H00M00S");
                                        } else {
                                            headerTableCPFunt.put(key, oDataDuration.toString());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    headerTableCPFunt.put(key, oDataProperties.get(key).getValue().toString());
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    batchCnt_Update = "";
                    JSONObject jsonObjectCpFunt = new JSONObject(headerTableCPFunt);
                    endpoint_Update = "CPPartnerFunctions(PFGUID=guid'" + headerTableCPFunt.get(Constants.PFGUID) + "')";

                    batchCnt_Update = batchCnt_Update
                            + "--changeset_" + changeSetId + "\n"
                            + "Content-Type: application/http" + "\n"
                            + "Content-Transfer-Encoding: binary" + "\n"
                            + "" + "\n"
                            + "PUT " + endpoint_Update + " HTTP/1.1" + "\n"
                            + "Content-Type: application/json" + "\n"
                            + "Accept: application/json" + "\n"
                            + "Content-Length: " + batchCnt_Update.length() + "\n"
                            + "" + "\n"
                            + jsonObjectCpFunt.toString() + "\n";

                    //END:   changeset to update data ----------

                    //create batch for creating items
                    batchContents = batchContents + batchCnt_Update + "\n";
                }
            }
            String cpInfraDetgry = Constants.CPInfras + "?$filter=" + Constants.CPGUID + " eq guid'" + retailerCreateBean.getCPGUID() + "' ";

            try {
                entities = OfflineManager.getEntities(cpInfraDetgry);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }

            if (entities != null && entities.size() > 0) {
                for (ODataEntity cpDmsDivEntity : entities) {
                    headerTableCPFunt = new Hashtable<>();
                    try {
                        oDataProperties = cpDmsDivEntity.getProperties();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogManager.writeLogError(Constants.error_txt + e.getMessage());
                    }
                    try {
                        keySet = oDataProperties.keySet();
                        itr = keySet.iterator();
                        while (itr.hasNext()) {
                            String key = itr.next().toString();
                            try {
                                if (oDataProperties.get(key).getValue() instanceof ODataGuid) {
                                    ODataGuid oDataGuid = (ODataGuid) oDataProperties.get(key).getValue();
                                    headerTableCPFunt.put(key, oDataGuid.guidAsString36());
                                } else if (oDataProperties.get(key).getValue() instanceof BigDecimal) {
                                    BigDecimal mDecimalLatitude = (BigDecimal) oDataProperties.get(key).getValue();
                                    headerTableCPFunt.put(key, "" + mDecimalLatitude.doubleValue());
                                } else if (oDataProperties.get(key).getValue() instanceof GregorianCalendar) {
                                    headerTableCPFunt.put(key, UtilConstants.convertCalenderToStringFormat1((GregorianCalendar) oDataProperties.get(key).getValue()));
                                } else if (oDataProperties.get(key).getValue() instanceof ODataDuration) {
                                    try {
                                        ODataDuration oDataDuration = (ODataDuration) oDataProperties.get(key).getValue();
                                        if (oDataDuration.toString().equalsIgnoreCase("P0Y")) {
                                            headerTableCPFunt.put(key, "PT00H00M00S");
                                        } else {
                                            headerTableCPFunt.put(key, oDataDuration.toString());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    headerTableCPFunt.put(key, oDataProperties.get(key).getValue().toString());
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    batchCnt_Update = "";
                    JSONObject jsonObjectCpFunt = new JSONObject(headerTableCPFunt);
                    endpoint_Update = "CPInfras(CP2GUID=guid'" + headerTableCPFunt.get(Constants.CP2GUID) + "')";

                    batchCnt_Update = batchCnt_Update
                            + "--changeset_" + changeSetId + "\n"
                            + "Content-Type: application/http" + "\n"
                            + "Content-Transfer-Encoding: binary" + "\n"
                            + "" + "\n"
                            + "PUT " + endpoint_Update + " HTTP/1.1" + "\n"
                            + "Content-Type: application/json" + "\n"
                            + "Accept: application/json" + "\n"
                            + "Content-Length: " + batchCnt_Update.length() + "\n"
                            + "" + "\n"
                            + jsonObjectCpFunt.toString() + "\n";

                    //END:   changeset to update data ----------

                    //create batch for creating items
                    batchContents = batchContents + batchCnt_Update + "\n";
                }
            }
            if(UtilConstants.isNetworkAvailable(activity)) {
                if(isProgress) {
                    String finalBatchContents = batchContents;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uploadDataWithAvailableServer(finalBatchContents, batchGuid, false);
                        }
                    }).start();
                }else {
                    RetailerVerifyAsyncTask1 retailerVerifyAsyncTask1 = new RetailerVerifyAsyncTask1(batchContents, batchGuid);
                    retailerVerifyAsyncTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }else {
                navigateToAddress();
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
    private void uploadDataWithAvailableServer(String batchContents,String batchGuid,boolean isTrue) {
        if (UtilConstants.isNetworkAvailable(context)) {
            String hostName = context.getSharedPreferences(Constants.PREFS_NAME, 0).getString(Constants.ActiveHost, "");
//            server_Text = hostName;
            try {
                AvailableServer.clearCookies();
                if (AvailableServer.pingServer(server_Text)) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.ActiveHost, server_Text);
                    editor.apply();
                    server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                    JSONObject resourceFileReadresponse = AvailableServer.getResourceFile(server_Text,context);
                    if (resourceFileReadresponse != null) {
                        JSONObject serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                        boolean status = serverResponse.getBoolean(Constants.Status);
                        int responseCode = serverResponse.getInt(Constants.ResponseCode);
                        String messageError = serverResponse.getString(Constants.Message);
                        if (responseCode == 200) {
                            if (status) {
                                ((Activity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(!isTrue) {
                                            RetailerVerifyAsyncTask1 retailerVerifyAsyncTask1 = new RetailerVerifyAsyncTask1(batchContents, batchGuid);
                                            retailerVerifyAsyncTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        }else {
                                            asyncTask = new RetailerVerifyAsyncTask();
                                            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        }
                                    }
                                });
                            } else {
                                String finalMessageError9 = messageError;
                                ((Activity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.hideProgress();
                                        cancelTimer();
                                        UtilConstants.showAlert("getResouce file check request failed : " + finalMessageError9,context);
                                    }
                                });
                                LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                            }
                        } else if (responseCode == 503 || responseCode == 404 || responseCode == 998 || responseCode == 999 || responseCode == 996) {
                            // 503 - service unavailable , 404 - Resource file not maintained , 998 - Backend unavailable , 999 - Resource file is empty
                            //996 - Resource file available but backend property missing
                            if (!server_Text.equalsIgnoreCase(server_Text_default)) {
//                                server_Text = server_Text_default;
//                                AvailableServer.clearCookies();
                                if (AvailableServer.pingServer(server_Text_default)) {
                                    resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_default,context);
                                    if (resourceFileReadresponse != null) {
                                        serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                                        status = serverResponse.getBoolean(Constants.Status);
                                        responseCode = serverResponse.getInt(Constants.ResponseCode);
                                        messageError = serverResponse.getString(Constants.Message);
                                        if (responseCode == 200) {
                                            if (status) {
//                                                editor.putString(Constants.ActiveHost, server_Text);
//                                                editor.apply();
//                                                server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                                                ((Activity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if(!isTrue) {
                                                            RetailerVerifyAsyncTask1 retailerVerifyAsyncTask1 = new RetailerVerifyAsyncTask1(batchContents, batchGuid);
                                                            retailerVerifyAsyncTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                        }else {
                                                            asyncTask = new RetailerVerifyAsyncTask();
                                                            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                        }
                                                    }
                                                });
                                            } else {
                                                String finalMessageError8 = messageError;
                                                ((Activity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        view.hideProgress();
                                                        cancelTimer();
                                                        UtilConstants.showAlert("getResouce file check request failed : " + finalMessageError8,context);
                                                    }
                                                });
                                                LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                                LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                                            }
                                        } else if (responseCode == 401) {
                                            String finalMessageError7 = messageError;
                                            ((Activity)context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    view.hideProgress();
                                                    cancelTimer();
                                                    UtilConstants.showAlert("getResouce file check request failed : " + finalMessageError7,context);
                                                }
                                            });
                                            LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                            LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                                        } else {
                                            String finalMessageError6 = messageError;
                                            ((Activity)context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    view.hideProgress();
                                                    cancelTimer();
                                                    UtilConstants.showAlert("getResouce file check request failed : " + finalMessageError6,context);
                                                }
                                            });
                                            LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                            LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                                        }
                                    }
                                } else {
                                    ((Activity)context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            view.hideProgress();
                                            cancelTimer();
                                            UtilConstants.showAlert("Unable to read resource file from servers. Registration can't be performed now",context);
                                        }
                                    });
                                    LogManager.writeLogError("Unable to read resource file from servers. Registration can't be performed now");
                                }
                            } else if (!server_Text.equalsIgnoreCase(server_Text_DR)) {
//                                AvailableServer.clearCookies();
//                                server_Text = server_Text_DR;
                                if (AvailableServer.pingServer(server_Text_DR)) {
                                    resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_DR,context);
                                    if (resourceFileReadresponse != null) {
                                        serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                                        status = serverResponse.getBoolean(Constants.Status);
                                        responseCode = serverResponse.getInt(Constants.ResponseCode);
                                        messageError = serverResponse.getString(Constants.Message);
                                        if (responseCode == 200) {
                                            if (status) {
//                                                editor.putString(Constants.ActiveHost, server_Text);
//                                                editor.apply();
//                                                server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                                                ((Activity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if(!isTrue) {
                                                            RetailerVerifyAsyncTask1 retailerVerifyAsyncTask1 = new RetailerVerifyAsyncTask1(batchContents, batchGuid);
                                                            retailerVerifyAsyncTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                        }else {
                                                            asyncTask = new RetailerVerifyAsyncTask();
                                                            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                        }
                                                    }
                                                });
                                            } else {
                                                String finalMessageError5 = messageError;
                                                ((Activity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        view.hideProgress();
                                                        cancelTimer();
                                                        UtilConstants.showAlert("getResouce file check request failed : " + finalMessageError5,context);
                                                    }
                                                });
                                                LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                                            }
                                        } else if (responseCode == 401) {
                                            String finalMessageError4 = messageError;
                                            ((Activity)context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    view.hideProgress();
                                                    cancelTimer();
                                                    UtilConstants.showAlert("getResouce file check request failed : " + finalMessageError4,context);
                                                }
                                            });
                                            LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                            LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                                        } else {
                                            String finalMessageError3 = messageError;
                                            ((Activity)context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    view.hideProgress();
                                                    cancelTimer();
                                                    UtilConstants.showAlert("getResouce file check request failed : " + finalMessageError3,context);
                                                }
                                            });
                                            LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                            LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                                        }
                                    }
                                } else {
                                    String finalMessageError2 = messageError;
                                    ((Activity)context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            view.hideProgress();
                                            cancelTimer();
                                            UtilConstants.showAlert("getResouce file check request failed : " + finalMessageError2,context);
                                        }
                                    });
                                    LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                    LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                                }
                            }
                        } else if (responseCode == 401) {
                            String finalMessageError1 = messageError;
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    view.hideProgress();
                                    cancelTimer();
                                    UtilConstants.showAlert("getResouce file check request failed : " + finalMessageError1,context);
                                }
                            });
                            LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                            LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                        } else {
                            String finalMessageError = messageError;
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    view.hideProgress();
                                    cancelTimer();
                                    UtilConstants.showAlert("getResouce file check request failed : " + finalMessageError,context);
                                }
                            });
                            LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                            LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                        }
                    }
                } else {
                    //xumu != xumu
                    if (!server_Text.equalsIgnoreCase(server_Text_default)) {
//                        server_Text = server_Text_default;
                        AvailableServer.clearCookies();
                        if (AvailableServer.pingServer(server_Text_default)) {
                            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.ActiveHost, server_Text_default);
                            editor.apply();
                            server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                            JSONObject resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_default,context);
                            if (resourceFileReadresponse != null) {
                                JSONObject serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                                boolean status = serverResponse.getBoolean(Constants.Status);
                                int responseCode = serverResponse.getInt(Constants.ResponseCode);
                                String messageError = serverResponse.getString(Constants.Message);
                                if (responseCode == 200) {
                                    if (status) {
                                        AvailableServer.registerWithAvailableServer(context, new AvailableServer.RegisterServer() {
                                                    @Override
                                                    public void requestSuccess() {
                                                        AvailableServer.openStoreWithAvailableServer(context, new UIListener() {
                                                            @Override
                                                            public void onRequestError(int operation, Exception e) {
                                                                ((Activity)context).runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        view.hideProgress();
                                                                        cancelTimer();
                                                                        UtilConstants.showAlert(e.getMessage(),context);
                                                                    }
                                                                });
                                                                LogManager.writeLogInfo("Offline Store SO Summary : "+e.toString());
                                                            }

                                                            @Override
                                                            public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {

                                                                try {
                                                                    ((Activity)context).runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            if(!isTrue) {
                                                                                RetailerVerifyAsyncTask1 retailerVerifyAsyncTask1 = new RetailerVerifyAsyncTask1(batchContents, batchGuid);
                                                                                retailerVerifyAsyncTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                                            }else {
                                                                                asyncTask = new RetailerVerifyAsyncTask();
                                                                                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                                            }
                                                                        }
                                                                    });
                                                                } catch (Throwable e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                        LogManager.writeLogInfo("Registered successfully SO Summary");
                                                    }

                                                    @Override
                                                    public void requestError(String errorMessage) {
                                                        ((Activity)context).runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                view.hideProgress();
                                                                cancelTimer();
                                                                UtilConstants.showAlert(errorMessage,context);
                                                            }
                                                        });
                                                        LogManager.writeLogInfo("Registered Error SO Summary : "+errorMessage);
                                                        editor.putString(Constants.ActiveHost, hostName);
                                                        editor.apply();
                                                        server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                                                    }
                                                }
                                        );
                                    } else {
                                        ((Activity)context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                view.hideProgress();
                                                cancelTimer();
                                                UtilConstants.showAlert("getResouce file check request failed : " + messageError,context);
                                            }
                                        });
                                        LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                        LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                                    }
                                } else if (responseCode == 503 || responseCode == 404 || responseCode == 998 || responseCode == 999 || responseCode == 996) {
                                    // 503 - service unavailable , 404 - Resource file not maintained , 998 - Backend unavailable , 999 - Resource file is empty
                                    //996 - Resource file available but backend property missing
                                    ((Activity)context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            view.hideProgress();
                                            cancelTimer();
                                            UtilConstants.showAlert("getResouce file check request failed : " + messageError,context);
                                        }
                                    });
                                    LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                    LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                                } else if (responseCode == 401) {
                                    ((Activity)context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            view.hideProgress();
                                            cancelTimer();
                                            UtilConstants.showAlert("getResouce file check request failed : " + messageError,context);
                                        }
                                    });
                                    LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                    LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                                } else {
                                    ((Activity)context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            view.hideProgress();
                                            cancelTimer();
                                            UtilConstants.showAlert("getResouce file check request failed : " + messageError,context);
                                        }
                                    });
                                    LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                    LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                                }
                            }
                        } else {
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    view.hideProgress();
                                    cancelTimer();
                                    UtilConstants.showAlert("Registration can't be performed due to server unavailability. Please try later",context);
                                }
                            });
                            LogManager.writeLogError("Registration can't be performed due to server unavailability. Please try later");
                        }
                    }
                    //Xumu != Ap1
                    else if (!server_Text.equalsIgnoreCase(server_Text_DR)) {
                        AvailableServer.clearCookies();
//                        server_Text = server_Text_DR;
                        if (AvailableServer.pingServer(server_Text_DR)) {
                            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.ActiveHost, server_Text_DR);
                            editor.apply();
                            server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                            JSONObject resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_DR,context);
                            if (resourceFileReadresponse != null) {
                                JSONObject serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                                boolean status = serverResponse.getBoolean(Constants.Status);
                                int responseCode = serverResponse.getInt(Constants.ResponseCode);
                                String messageError = serverResponse.getString(Constants.Message);
                                if (responseCode == 200) {
                                    if (status) {
                                        AvailableServer.registerWithAvailableServer(context, new AvailableServer.RegisterServer() {
                                                    @Override
                                                    public void requestSuccess() {
                                                        AvailableServer.openStoreWithAvailableServer(context, new UIListener() {
                                                            @Override
                                                            public void onRequestError(int operation, Exception e) {
                                                                ((Activity)context).runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        view.hideProgress();
                                                                        cancelTimer();
                                                                        UtilConstants.showAlert(e.getMessage(),context);
                                                                    }
                                                                });
                                                                LogManager.writeLogInfo("Offline Store SO Summary : "+e.toString());
                                                            }

                                                            @Override
                                                            public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {

                                                                try {
                                                                    ((Activity)context).runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            RetailerVerifyAsyncTask1 retailerVerifyAsyncTask1 = new RetailerVerifyAsyncTask1(batchContents,batchGuid);
                                                                            retailerVerifyAsyncTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                                        }
                                                                    });
                                                                } catch (Throwable e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        });
                                                        LogManager.writeLogInfo("Registered successfully SO Summary");
                                                    }

                                                    @Override
                                                    public void requestError(String errorMessage) {
                                                        ((Activity)context).runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                view.hideProgress();
                                                                cancelTimer();
                                                                UtilConstants.showAlert(errorMessage,context);
                                                            }
                                                        });
                                                        LogManager.writeLogInfo("Registered Error SO Summary : "+errorMessage);
                                                        editor.putString(Constants.ActiveHost, hostName);
                                                        editor.apply();
                                                        server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                                                    }
                                                }
                                        );
                                    } else {
                                        ((Activity)context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                view.hideProgress();
                                                cancelTimer();
                                                UtilConstants.showAlert("getResouce file check request failed : " + messageError,context);
                                            }
                                        });
                                        LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                        LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                                    }
                                } else if (responseCode == 503 || responseCode == 404 || responseCode == 998 || responseCode == 999 || responseCode == 996) {
                                    // 503 - service unavailable , 404 - Resource file not maintained , 998 - Backend unavailable , 999 - Resource file is empty
                                    //996 - Resource file available but backend property missing
                                    ((Activity)context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            view.hideProgress();
                                            cancelTimer();
                                            UtilConstants.showAlert("getResouce file check request failed : " + messageError,context);
                                        }
                                    });
                                    LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                    LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                                } else if (responseCode == 401) {
                                    ((Activity)context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            view.hideProgress();
                                            cancelTimer();
                                            UtilConstants.showAlert("getResouce file check request failed : " + messageError,context);
                                        }
                                    });
                                    LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                    LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                                } else {
                                    ((Activity)context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            view.hideProgress();
                                            cancelTimer();
                                            UtilConstants.showAlert("getResouce file check request failed : " + messageError,context);
                                        }
                                    });
                                    LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                    LogManager.writeLogInfo("getResouce file check request failed : " + messageError);
                                }
                            }
                        } else {
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    view.hideProgress();
                                    cancelTimer();
                                    UtilConstants.showAlert("Registration can't be performed due to server unavailability. Please try later",context);
                                }
                            });
                            LogManager.writeLogError("Registration can't be performed due to server unavailability. Please try later");
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else{
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        view.hideProgress();
                        cancelTimer();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    UtilConstants.showAlert("Unable to perform background sync due to network unavailability",context);
                }
            });
            LogManager.writeLogError("Unable to perform background sync due to network unavailability");
        }
    }
    private String concatFlushCollStr = "";
    private ArrayList<String> alFlushColl=new ArrayList<>();

    private class RetailerVerifyAsyncTask1 extends AsyncTask<Void,Void,Void>{

        String batchContents = "";
        String batchGuid = "";
        public RetailerVerifyAsyncTask1(String batchContents, String batchGuid){
            this.batchContents=batchContents;
            this.batchGuid=batchGuid;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                try {
                    alFlushColl.clear();
                    concatFlushCollStr="";
                    alFlushColl.addAll(SyncUtils.getRetailer());

                    for (int incVal = 0; incVal < alFlushColl.size(); incVal++) {
                        if (incVal == 0 && incVal == alFlushColl.size() - 1) {
                            concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal);
                        } else if (incVal == 0) {
                            concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal) + ", ";
                        } else if (incVal == alFlushColl.size() - 1) {
                            concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal);
                        } else {
                            concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal) + ", ";
                        }
                    }
                    OnlineManager.postBatchRequest(batchContents, batchGuid, context, new PDAOrderBrowserDetailsPresentImpl.IOrderUpdateListener() {
                        @Override
                        public void onUpdateError(String errorMessage) {
                            LogManager.writeLogInfo("Verify Outlet : outlet review is successful saved outlet with error");
//                            try {
//                                if (UtilConstants.isNetworkAvailable(activity)) {
//                                    messageProgess = "Refreshing data, Please wait..";
//                                    OfflineManager.refreshStoreSync(activity, RetailerSelectionViewModel.this,Constants.Fresh, concatFlushCollStr);
////                                    view.hideProgress();
////                                    view.showProgress(messageProgess,1);
//
//                                }else{
                            view.hideProgress();
                            cancelTimer();
                            view.showMessage("Synchronization not completed due to network unavailablity",1);
//                                }
//                            } catch (OfflineODataStoreException ex) {
//                                ex.printStackTrace();
//                            }
                        }

                        @Override
                        public void onUpdateSuccess(String s){
                            LogManager.writeLogInfo("Verify Outlet : outlet review is successful saved outlet");
                            if (UtilConstants.isNetworkAvailable(activity)) {
                                try {
                                    messageProgess = "Refreshing data, Please wait..";
                                    OfflineManager.refreshStoreSync(activity, OutletListViewModel.this,Constants.Fresh, concatFlushCollStr);
                                } catch (OfflineODataStoreException e) {
                                    e.printStackTrace();
                                }
//                                view.hideProgress();
//                                view.showProgress(messageProgess,1);

                            }else{
                                view.hideProgress();
                                cancelTimer();
                                view.showMessage("Synchronization not completed due to network unavailablity",1);
                            }
                        }
                    });
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
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
                view.showProgress(messageProgess + "\t\t" + countdown,1);
            }

            @Override
            public void onFinish() {

            }

        };
        countDownTimer.start();
    }

    public static ArrayList<SSSOItemDetailsBean> sssoMaterailList = new ArrayList<>();
    public static void getSSSOMaterialList(String cpGuid){
        sssoMaterailList = new ArrayList<>();
        String sssoListQry="";
        ArrayList<SSSOsHeaderBean> sssOsHeaderBeansList = new ArrayList<>();
        sssoListQry = Constants.SSSOs + "?$select=SSSOGuid,SoldToCPGUID,OrderDate,SoldToDesc,OrderNo &$filter=" + Constants.SoldToCPGUID + " eq guid'" + cpGuid + "' and " +Constants.FromCPNo+" eq '"+ MSFAApplication.SDA_FROM_CPNO+"' and "+ Constants.OrderDate + " ge datetime'" + UtilConstants.getLastTwoDay()+"' and "+Constants.Source+" eq 'DIRECTWEB" + "' and "+Constants.Status +" eq '000001'"+" &$orderby=OrderDate desc  &$top=1";
        sssoMaterailList = new ArrayList<>();
        SSSOsHeaderBean sssOsHeaderBean = new SSSOsHeaderBean();
        sssOsHeaderBeansList = (ArrayList) new OfflineUtils.ODataOfflineBuilder<>()
                .setHeaderPayloadObject(sssOsHeaderBean)
                .setODataEntitySet(Constants.SSSOs)
                .setODataEntityType(Constants.SSSOEntity)
                .setQuery(sssoListQry)
                .returnODataList(OfflineManager.offlineStore);
        if(sssOsHeaderBeansList.size()>0){
            for(SSSOsHeaderBean osHeaderBean : sssOsHeaderBeansList) {
                String sssoItemQry = Constants.SSSoItemDetails + "?$filter=" + Constants.SSSOGuid + " eq guid'" + osHeaderBean.getSSSOGuid() + "'";
                SSSOItemDetailsBean itemDetailsBean = new SSSOItemDetailsBean();
                sssoMaterailList.addAll((ArrayList) new OfflineUtils.ODataOfflineBuilder<>()
                        .setHeaderPayloadObject(itemDetailsBean)
                        .setODataEntitySet(Constants.SSSoItemDetails)
                        .setODataEntityType(Constants.SSSoItemDetailEntity)
                        .setQuery(sssoItemQry)
                        .returnODataList(OfflineManager.offlineStore));
            }
        }
        LogManager.writeLogError("get SSSO material list size : "+sssoMaterailList.size());
    }
}
*/

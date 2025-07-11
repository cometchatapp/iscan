package com.arteriatech.ss.msec.iscan.v4.sync;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.BuildConfig;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.sync.SyncHistoryDB;
import com.arteriatech.ss.msec.iscan.v4.mutils.sync.SyncHistoryModel;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncInfo.RefreshListInterface;
import com.arteriatech.ss.msec.iscan.v4.sync.syncSelectionView.SyncSelectionViewBean;
import com.sap.client.odata.v4.core.GUID;
import com.sap.smp.client.odata.ODataDuration;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.store.ODataRequestChangeSet;
import com.sap.smp.client.odata.store.ODataRequestParamBatch;
import com.sap.smp.client.odata.store.ODataRequestParamSingle;
import com.sap.smp.client.odata.store.impl.ODataRequestChangeSetDefaultImpl;
import com.sap.smp.client.odata.store.impl.ODataRequestParamBatchDefaultImpl;
import com.sap.smp.client.odata.store.impl.ODataRequestParamSingleDefaultImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by e10769 on 04-07-2017.
 */

public class SyncUtils {
    public static boolean isRetailerOrBeatSync =false;
    public static ArrayList<SyncSelectionViewBean> getSyncSelectionView(Context mContext) {
        ArrayList<SyncSelectionViewBean> syncSelectionViewBeanArrayList = new ArrayList<>();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        String rollID = ConstantsUtils.getAuthInformation(mContext);

        SyncSelectionViewBean syncSelectionViewBean;

        /*syncSelectionViewBean = new SyncSelectionViewBean();
        syncSelectionViewBean.setChecked(false);
        syncSelectionViewBean.setDisplayName(mContext.getString(R.string.alerts));
        syncSelectionViewBean.setCollectionName(SyncUtils.getAlerts());
        syncSelectionViewBeanArrayList.add(syncSelectionViewBean);*/

        syncSelectionViewBean = new SyncSelectionViewBean();
        syncSelectionViewBean.setChecked(false);
        syncSelectionViewBean.setDisplayName("Attendance");
        syncSelectionViewBean.setCollectionName(SyncUtils.getAttendanceCollection());
        syncSelectionViewBeanArrayList.add(syncSelectionViewBean);

        syncSelectionViewBean = new SyncSelectionViewBean();
        syncSelectionViewBean.setChecked(false);
        syncSelectionViewBean.setDisplayName("Authorizations");
        syncSelectionViewBean.setCollectionName(SyncUtils.getAuthorizationCollection());
        syncSelectionViewBeanArrayList.add(syncSelectionViewBean);


        syncSelectionViewBean = new SyncSelectionViewBean();
        syncSelectionViewBean.setChecked(false);
        syncSelectionViewBean.setDisplayName("Beat");
        syncSelectionViewBean.setCollectionName(SyncUtils.getBeat(mContext));
        syncSelectionViewBeanArrayList.add(syncSelectionViewBean);



       /* if (sharedPreferences.getString(Constants.isBehaviourEnabled, "").equalsIgnoreCase(Constants.isBehaviourTcode)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("Behavior");
            syncSelectionViewBean.setCollectionName(SyncUtils.getBehaviorList());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }*/


//        syncSelectionViewBean = new SyncSelectionViewBean();
//        syncSelectionViewBean.setChecked(false);
//        syncSelectionViewBean.setDisplayName("Customers");
//        syncSelectionViewBean.setCollectionName(SyncUtils.getCustomersCollection());
//        syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
//        if (sharedPreferences.getString(Constants.isDBStockEnabled, "").equalsIgnoreCase(Constants.isDBStockTcode)) {
        if(rollID.equalsIgnoreCase(Constants.PSM)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("Stock");
            syncSelectionViewBean.setCollectionName(SyncUtils.getStock());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }
//        }

        /*if (sharedPreferences.getString(Constants.isCollCreateEnabledKey, "").equalsIgnoreCase(Constants.isCollCreateTcode) || sharedPreferences.getString(Constants.isCollListKey, "").equalsIgnoreCase(Constants.isCollListTcode)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName(mContext.getString(R.string.title_coll_history));
            syncSelectionViewBean.setCollectionName(SyncUtils.getFIPCollection());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }
        if (sharedPreferences.getString(Constants.isDigitalProductEntryEnabled, "").equalsIgnoreCase(Constants.isDigitalProductEntryTcode)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName(mContext.getString(R.string.digital_product_title));
            syncSelectionViewBean.setCollectionName(SyncUtils.getVisualAid());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }
        if (sharedPreferences.getString(Constants.isExpenseEntryKey, "").equalsIgnoreCase(Constants.isExpenseEntryTcode) || sharedPreferences.getString(Constants.isExpenseListKey, "").equalsIgnoreCase(Constants.isExpenseListTcode)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName(mContext.getString(R.string.title_expense));
            syncSelectionViewBean.setCollectionName(SyncUtils.getExpenseListCollection());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }


        if (sharedPreferences.getString(Constants.isFeedBackListKey, "").equalsIgnoreCase(Constants.isFeedBackListTcode)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName(mContext.getString(R.string.title_feed_back));
            syncSelectionViewBean.setCollectionName(SyncUtils.getFeedBack());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }*/

//        if (sharedPreferences.getString(Constants.isSecondaryInvoiceListKey, "").equalsIgnoreCase(Constants.isSecondaryInvoiceListTcode)) {
        if(!rollID.equalsIgnoreCase(Constants.PSM)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("Invoice");
            syncSelectionViewBean.setCollectionName(SyncUtils.getInvoice());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }
//        }

       /* if (sharedPreferences.getString(Constants.isMerchReviewListKey, "").equalsIgnoreCase(Constants.isMerchReviewListTcode)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName(mContext.getString(R.string.lbl_merchndising_list));
            syncSelectionViewBean.setCollectionName(SyncUtils.getMerchandising());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }*/

//        if (sharedPreferences.getString(Constants.isRetailerListEnabled, "").equalsIgnoreCase(Constants.isRetailerListTcode)) {
        if(rollID.equalsIgnoreCase(Constants.PSM)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("Masters");
            syncSelectionViewBean.setCollectionName(SyncUtils.getMasters());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }else if(rollID.equalsIgnoreCase(Constants.VAN)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("Masters");
            syncSelectionViewBean.setCollectionName(SyncUtils.getMasters2());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }else {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("Masters");
            syncSelectionViewBean.setCollectionName(SyncUtils.getMasters1());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }
//        }
        if(rollID.equalsIgnoreCase(Constants.PSM)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("Segmented Materials");
            syncSelectionViewBean.setCollectionName(SyncUtils.getMaterialCollection());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }
//        if (sharedPreferences.getString(Constants.isRetailerStockKey, "").equalsIgnoreCase(Constants.isRetailerStockTcode)) {
          /*  syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("Stock Detail");
            syncSelectionViewBean.setCollectionName(SyncUtils.getRetailerStock());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);*/
//        }

//        if (sharedPreferences.getString(Constants.isSecondarySalesListKey, "").equalsIgnoreCase(Constants.isSecondarySalesListTcode)) {
        if(rollID.equalsIgnoreCase(Constants.PSM)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("Sales Order");
            syncSelectionViewBean.setCollectionName(SyncUtils.getSalesOrder());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }
//        }

        if(!rollID.equalsIgnoreCase(Constants.PSM)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("Scheme");
            syncSelectionViewBean.setCollectionName(SyncUtils.getSchemes());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }


        if(rollID.equalsIgnoreCase(Constants.VAN)){
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("Retailer");
            syncSelectionViewBean.setCollectionName(SyncUtils.getRetailer1());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }else {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("Retailer");
            syncSelectionViewBean.setCollectionName(SyncUtils.getRetailer());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }

        if(!rollID.equalsIgnoreCase(Constants.PSM)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("Visit");
            syncSelectionViewBean.setCollectionName(SyncUtils.getVisit());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }

       /* syncSelectionViewBean = new SyncSelectionViewBean();
        syncSelectionViewBean.setChecked(false);
        syncSelectionViewBean.setDisplayName("Survey");
        syncSelectionViewBean.setCollectionName(SyncUtils.getSurveyCollection());
        syncSelectionViewBeanArrayList.add(syncSelectionViewBean);*/

      /*  if (sharedPreferences.getString(Constants.isReturnOrderListKey, "").equalsIgnoreCase(Constants.isReturnOrderListTcode)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName(mContext.getString(R.string.title_return_order_list));
            syncSelectionViewBean.setCollectionName(SyncUtils.getROsCollection());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }*/

//        if (sharedPreferences.getString(Constants.isMyTargetsEnabled, "").equalsIgnoreCase(Constants.isMyTargetsTcode)) {
        if(rollID.equalsIgnoreCase(Constants.PSM)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("Targets");
            syncSelectionViewBean.setCollectionName(SyncUtils.getTargets());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }
//        }

       /* if (sharedPreferences.getString(Constants.isVisualAidEnabled, "").equalsIgnoreCase(Constants.isVisualAidTcode)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName(mContext.getString(R.string.visual_aidl_title));
            syncSelectionViewBean.setCollectionName(SyncUtils.getVisualAid());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }*/


        syncSelectionViewBean = new SyncSelectionViewBean();
        syncSelectionViewBean.setChecked(false);
        syncSelectionViewBean.setDisplayName("Value Help");
        syncSelectionViewBean.setCollectionName(SyncUtils.getValueHelps());
        syncSelectionViewBeanArrayList.add(syncSelectionViewBean);





       /* if (sharedPreferences.getString(Constants.isSOCreateKey, "").equalsIgnoreCase(Constants.isSOCreateTcode) || sharedPreferences.getString(Constants.isSOCreateCCKey, "").equalsIgnoreCase(Constants.isSOCreateCCTcode) || sharedPreferences.getString(Constants.isSOListKey, "").equalsIgnoreCase(Constants.isSOListTcode) || sharedPreferences.getString(Constants.isSOListCCKey, "").equalsIgnoreCase(Constants.isSOListCCTcode)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("SOs");
            syncSelectionViewBean.setCollectionName(SyncUtils.getSOsCollection());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }*/

       /* if (sharedPreferences.getString(Constants.isInvoiceListKey, "").equalsIgnoreCase(Constants.isInvoiceListTcode)) {
            syncSelectionViewBean = new SyncSelectionViewBean();
            syncSelectionViewBean.setChecked(false);
            syncSelectionViewBean.setDisplayName("Invoice");
            syncSelectionViewBean.setCollectionName(SyncUtils.getInvoiceCollection());
            syncSelectionViewBeanArrayList.add(syncSelectionViewBean);
        }*/
        java.util.Collections.sort(syncSelectionViewBeanArrayList, (Comparator<SyncSelectionViewBean>) (one, other) -> one.getDisplayName().compareTo(other.getDisplayName()));


        return syncSelectionViewBeanArrayList;
    }

    public static ArrayList<String> getBehaviorList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.SPChannelEvaluationList);
        return arrayList;
    }

    public static ArrayList<String> getTargets() {
        ArrayList<String> alAssignColl = new ArrayList<>();
        alAssignColl.add(Constants.Targets);
        alAssignColl.add(Constants.TargetItems);
        alAssignColl.add(Constants.KPISet);
        alAssignColl.add(Constants.KPIItems);
        return alAssignColl;
    }
    public static ArrayList<String> getMasters(){
        ArrayList<String> alAssignColl = new ArrayList<>();
        alAssignColl.add(Constants.VisitQuestionnaires);
        alAssignColl.add(Constants.UserSalesPersons);
//        alAssignColl.add(Constants.UserCustomers);
        alAssignColl.add(Constants.SalesPersons);
//        alAssignColl.add(Constants.OrderMaterialGroups);
//        alAssignColl.add(Constants.Customers);
        alAssignColl.add(Constants.BrandsCategories);
        alAssignColl.add(Constants.Brands);
        alAssignColl.add(Constants.Questions);
        alAssignColl.add(Constants.CPConfigs);
        return alAssignColl;
    }

    public static ArrayList<String> getMasters2(){
        ArrayList<String> alAssignColl = new ArrayList<>();
//        alAssignColl.add(Constants.VisitQuestionnaires);
        alAssignColl.add(Constants.UserSalesPersons);
//        alAssignColl.add(Constants.UserCustomers);
        alAssignColl.add(Constants.SalesPersons);
//        alAssignColl.add(Constants.OrderMaterialGroups);
//        alAssignColl.add(Constants.Customers);
//        alAssignColl.add(Constants.BrandsCategories);
//        alAssignColl.add(Constants.Brands);
//        alAssignColl.add(Constants.Questions);
//        alAssignColl.add(Constants.CPConfigs);
        return alAssignColl;
    }

    public static ArrayList<String> getMasters1(){
        ArrayList<String> alAssignColl = new ArrayList<>();
//        alAssignColl.add(Constants.VisitQuestionnaires);
        alAssignColl.add(Constants.UserSalesPersons);
//        alAssignColl.add(Constants.UserCustomers);
        alAssignColl.add(Constants.SalesPersons);
//        alAssignColl.add(Constants.OrderMaterialGroups);
//        alAssignColl.add(Constants.Customers);
//        alAssignColl.add(Constants.BrandsCategories);
//        alAssignColl.add(Constants.Brands);
//        alAssignColl.add(Constants.Questions);
        alAssignColl.add(Constants.CPConfigs);
        return alAssignColl;
    }

    public static ArrayList<String> getVisualAid() {
        ArrayList<String> alAssignColl = new ArrayList<>();
        alAssignColl.add(Constants.Documents);
        return alAssignColl;
    }

    public static ArrayList<String> getMerchandising() {
        ArrayList<String> alAssignColl = new ArrayList<>();
        alAssignColl.add(Constants.MerchReviews);
        alAssignColl.add(Constants.MerchReviewImages);
        return alAssignColl;
    }

    public static ArrayList<String> getOutStandings() {
        ArrayList<String> alAssignColl = new ArrayList<>();
        alAssignColl.add(Constants.SSOutstandingInvoices);
        alAssignColl.add(Constants.SSOutstandingInvoiceItemDetails);
        return alAssignColl;
    }

    public static ArrayList<String> getFeedBack() {
        ArrayList<String> alAssignColl = new ArrayList<>();
        alAssignColl.add(Constants.Feedbacks);
        alAssignColl.add(Constants.FeedbackItemDetails);
        return alAssignColl;
    }

    public static ArrayList<String> getCompetitors() {
        ArrayList<String> alAssignColl = new ArrayList<>();
        alAssignColl.add(Constants.CompetitorInfos);
        return alAssignColl;
    }

    public static ArrayList<String> getInvoice() {
        ArrayList<String> alAssignColl = new ArrayList<>();
        alAssignColl.add(Constants.SSINVOICES);
//        alAssignColl.add(Constants.SSInvoiceItemDetails);
//        alAssignColl.add(Constants.SSInvoiceTypes);
        return alAssignColl;
    }
    public static ArrayList<String> getSalesOrder() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.SSSoItemDetails);
        arrayList.add(Constants.SSSOs);
        return arrayList;
    }

    public static ArrayList<String> getVisit() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.Visits);
        arrayList.add(Constants.VisitActivities);
        return arrayList;
    }


    public static ArrayList<String> getSOsCollection() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.SSSoItemDetails);
        arrayList.add(Constants.SSSOs);
        arrayList.add(Constants.Visits);
        arrayList.add(Constants.VisitActivities);
        return arrayList;
    }

    public static ArrayList<String> getGRCollection() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.MaterialDocItemDetails);
        arrayList.add(Constants.MaterialDocItems);
//                            alAssignColl.add(Constants.MaterialDocItemCatSplits);
        arrayList.add(Constants.MaterialDocs);
        arrayList.add(Constants.SSPurchaseInvoiceItems);
        arrayList.add(Constants.SSPurchaseInvoices);
        arrayList.add(Constants.CPStockItems);
        arrayList.add(Constants.CPStockItemSnos);
        return arrayList;
    }
    public static ArrayList<String> getBillCollection() {
        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add(Constants.MaterialDocItemDetails);
        arrayList.add(Constants.SSInvoiceItemDetails);
        arrayList.add(Constants.SSINVOICES);
        arrayList.add(Constants.CPStockItems);
        arrayList.add(Constants.CPStockItemSnos);
        arrayList.add(Constants.SSSOs);
        arrayList.add(Constants.SSSoItemDetails);
        return arrayList;
    }

    public static ArrayList<String> getBillCollection1() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.SSInvoiceItemDetails);
        arrayList.add(Constants.SSINVOICES);
        arrayList.add(Constants.SPStockItems);
        arrayList.add(Constants.SPStockItemSNos);
        return arrayList;
    }

    public static ArrayList<String> getROsCollection() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.SSROItemDetails);
        arrayList.add(Constants.SSROs);
        return arrayList;
    }
    public static ArrayList<String> getMaterialCollection() {
        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add(Constants.MaterialDocItemDetails);
//        arrayList.add(Constants.MaterialDocItems);
        arrayList.add(Constants.SegmentedMaterials);
        return arrayList;
    }

    public static ArrayList<String> getSurveyCollection() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.VisitQuestionnaires);
        arrayList.add(Constants.Questions);
        return arrayList;
    }

    public static ArrayList<String> getValueHelps() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.ValueHelps);
        arrayList.add(Constants.ConfigTypsetTypeValues);
        arrayList.add(Constants.ConfigTypesetTypes);
        // arrayList.add(Constants.ExpenseConfigs);
        return arrayList;
    }

    public static ArrayList<String> getAuthorizationCollection() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.UserProfileAuthSet);
        return arrayList;
    }
    public static ArrayList<String> getBeat(Context context){
        isRetailerOrBeatSync =true;
        ArrayList<String> arrayList = new ArrayList<>();
        if(Arrays.asList(Constants.getDefiningReq(context)).contains(Constants.RoutePlans)){
            arrayList.add(Constants.RoutePlans);
        }
        arrayList.add(Constants.RouteSchedules);
        arrayList.add(Constants.RouteSchedulePlans);
        arrayList.add(Constants.RouteProductDetTypes);
        arrayList.add(Constants.CPProductDetTypes);
        arrayList.add(Constants.RouteScheduleDetails);
        arrayList.add(Constants.RouteScheduleSPs);
        return arrayList;
    }

    public static ArrayList<String> getBeatCollection() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.RoutePlans);
        arrayList.add(Constants.RouteSchedules);
        arrayList.add(Constants.RouteSchedulePlans);
        return arrayList;
    }
    public  static ArrayList<String> getRetailer(){
        isRetailerOrBeatSync =true;
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.ChannelPartners);
        arrayList.add(Constants.CPDMSDivisions);
        arrayList.add(Constants.CPSPRelations);
        arrayList.add(Constants.CPPartnerFunctions);
        return arrayList;
    }

    public  static ArrayList<String> getRetailer1(){
        isRetailerOrBeatSync =true;
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.ChannelPartners);
        arrayList.add(Constants.CPDMSDivisions);
        arrayList.add(Constants.CPSPRelations);
        arrayList.add(Constants.CPPartnerFunctions);
        return arrayList;
    }

    public static ArrayList<String> getFOS() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.ChannelPartners);
        arrayList.add(Constants.CPDMSDivisions);
        arrayList.add(Constants.CPSPRelations);
//        arrayList.add(Constants.CPDocuments);
//        arrayList.add(Constants.UserCustomers);
//        arrayList.add(Constants.VisitActivities);
        arrayList.add(Constants.Visits);
        arrayList.add(Constants.UserSalesPersons);
        arrayList.add(Constants.SalesPersons);
        return arrayList;
    }

    public static ArrayList<String> getRetailerStock() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("CPStockItems?$filter=(StockOwner eq '01')");
        arrayList.add("CPStockItemSnos?$filter=(StockOwner eq '01')");
        return arrayList;
    }

    public static ArrayList<String> getAlerts() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.ChannelPartners);
        arrayList.add(Constants.Visits);
        arrayList.add(Constants.Alerts);
        return arrayList;
    }

    public static ArrayList<String> getAttendanceCollection() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.Attendances);
//        arrayList.add(Constants.AttendanceDocuments);
        return arrayList;
    }

    public static ArrayList<String> getCustomersCollection() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.Customers);
        arrayList.add(Constants.UserCustomers);
        return arrayList;
    }

    public static ArrayList<String> getInvoiceCollection() {
        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add(Constants.Invoices);
//        arrayList.add(Constants.InvoiceItemDetails);
        return arrayList;
    }

    public static ArrayList<String> getStock(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.CPStockItems);
        arrayList.add(Constants.CPStockItemSnos);
        return arrayList;
    }
    public static ArrayList<String> getDBStockCollection() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("CPStockItems?$filter=(StockOwner eq '01' or StockOwner eq '02')");
        arrayList.add("CPStockItemSnos?$filter=(StockOwner eq '01' or StockOwner eq '02')");
        arrayList.add(Constants.Brands);
        arrayList.add(Constants.BrandsCategories);
//        arrayList.add(Constants.SegmentedMaterials);
//        arrayList.add(Constants.MaterialCategories);
        arrayList.add(Constants.OrderMaterialGroups);
        return arrayList;
    }

    public static ArrayList<String> getFIPCollection() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.FinancialPostingItemDetails);
        arrayList.add(Constants.FinancialPostings);
//        arrayList.add(Constants.SSOutstandingInvoices);
//        arrayList.add(Constants.SSOutstandingInvoiceItemDetails);
        return arrayList;
    }

    public static ArrayList<String> getExpenseListCollection() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.Expenses);
        arrayList.add(Constants.ExpenseItemDetails);
        arrayList.add(Constants.ExpenseDocuments);
        return arrayList;
    }
    public static ArrayList<String> getGRNListCollection() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.MaterialDocs);
        arrayList.add(Constants.MaterialDocItemDetails);
        arrayList.add(Constants.MaterialDocItems);
        arrayList.add(Constants.MaterialDocItemCatSplits);
        return arrayList;
    }

    public static ArrayList<String> getSchemes() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.Schemes);
        arrayList.add(Constants.SchemeItemDetails);
        arrayList.add(Constants.SchemeSlabs);
        arrayList.add(Constants.SchemeGeographies);
        arrayList.add(Constants.SchemeCPs);
        arrayList.add(Constants.SchemeSalesAreas);
//        arrayList.add(Constants.SchemeCostObjects);
//        arrayList.add(Constants.SchemeFreeMatGrpMaterials);
//        arrayList.add(Constants.SchemeCPDocuments);
        return arrayList;
    }

    /*public static ArrayList<MainMenuBean> getSyncMenu(Context mContext) {
        ArrayList<MainMenuBean> syncMenuList = new ArrayList<>();
        MainMenuBean mainMenuBean;
//        if (sharedPreferences.getString(Constants.isAllSyncKey, "").equalsIgnoreCase(Constants.isAllSyncTcode)) {
        mainMenuBean = new MainMenuBean();
        mainMenuBean.setMenuName(mContext.getString(R.string.sync_all));
        mainMenuBean.setId(1);
        mainMenuBean.setMenuImage(R.drawable.ic_sync);
        syncMenuList.add(mainMenuBean);
//        }
//        if (sharedPreferences.getString(Constants.isFreshSyncKey, "").equalsIgnoreCase(Constants.isFreshSyncTcode)) {
        mainMenuBean = new MainMenuBean();
        mainMenuBean.setMenuName(mContext.getString(R.string.sync_fresh));
        mainMenuBean.setId(2);
        mainMenuBean.setMenuImage(R.drawable.ic_sync);
        syncMenuList.add(mainMenuBean);
//        }
//        if (sharedPreferences.getString(Constants.isUpdateSyncKey, "").equalsIgnoreCase(Constants.isUpdateSyncTcode)) {
        mainMenuBean = new MainMenuBean();
        mainMenuBean.setMenuName(mContext.getString(R.string.sync_update_system));
        mainMenuBean.setId(3);
        mainMenuBean.setMenuImage(R.drawable.ic_sync);
        syncMenuList.add(mainMenuBean);
//        }
//        if (sharedPreferences.getString(Constants.isSyncHistKey, "").equalsIgnoreCase(Constants.isSyncHistTcode)) {
        mainMenuBean = new MainMenuBean();
        mainMenuBean.setMenuName(mContext.getString(R.string.sync_history));
        mainMenuBean.setId(4);
        mainMenuBean.setMenuImage(R.drawable.ic_sync_history);
        syncMenuList.add(mainMenuBean);
//        }
        return syncMenuList;
    }*/

    public static String getCollectionSyncTime(Context mContext, String collectionName) {
        SyncHistoryDB syncHistoryDB = new SyncHistoryDB(mContext);
        List<SyncHistoryModel> syncHistoryModelList = syncHistoryDB.getSyncTimeBySpecificColl(SyncHistoryDB.COL_COLLECTION, collectionName);
        if (!syncHistoryModelList.isEmpty()) {
            long timeMillSec = ConstantsUtils.getMilliSeconds(syncHistoryModelList.get(0).getTimeStamp());
            return UtilConstants.getLastSeenDateFormat(mContext, timeMillSec);
        }
        return "";
    }

    /*get all sync value from defining req*/
    public static ArrayList<String> getAllSyncValue(Context mContext) {
        ArrayList<String> alAssignColl = new ArrayList<>();
        String[] DEFINGREQARRAY = Constants.getDefiningReq(mContext);
        for (String collectionName : DEFINGREQARRAY) {
            if (collectionName.contains("?")) {
                String splitCollName[] = collectionName.split("\\?");
                collectionName = splitCollName[0];
            }
            alAssignColl.add(collectionName);
        }
        return alAssignColl;
    }

    public static void initialInsert(Context context) {
        String[] definingReqArray = Constants.getDefiningReq(context);
        SyncHistoryDB syncHistoryDB = new SyncHistoryDB(context);
        syncHistoryDB.deleteAll();
        for (String aDefiningReqArray : definingReqArray) {
            String colName = aDefiningReqArray;
            if (colName.contains("?$")) {
                String splitCollName[] = colName.split("\\?");
                colName = splitCollName[0];
            }
            try {
                syncHistoryDB.createRecord(new SyncHistoryModel("", colName, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<SyncHistoryModel> getOneRecord(SyncHistoryDB syncHistoryDB) {
        SQLiteDatabase db = syncHistoryDB.getReadableDatabase();
        List<SyncHistoryModel> syncHistoryModelList = new ArrayList();
        String selectQuery = "select * from syncHistory limit 1";
        Cursor cursor = db.rawQuery(selectQuery, (String[]) null);
        if (cursor.moveToFirst()) {
            do {
                SyncHistoryModel syncHistoryModel = new SyncHistoryModel();
                syncHistoryModel.setCollections(cursor.getString(1));
                syncHistoryModel.setTimeStamp(cursor.getString(2));
                syncHistoryModelList.add(syncHistoryModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return syncHistoryModelList;
    }

    public static void checkAndCreateDB(Context mContext) {
        SyncHistoryDB syncHistoryDB = new SyncHistoryDB(mContext);
        if (getOneRecord(syncHistoryDB).isEmpty()) {
            initialInsert(mContext);
        }
    }


    public static void updateAllSyncHistory(Context context) {
        SyncHistoryDB syncHistoryDB = new SyncHistoryDB(context);
        String syncTime = UtilConstants.getSyncHistoryddmmyyyyTime();
        String[] definingReqArray = Constants.getDefiningReq(context);
        for (String aDefiningReqArray : definingReqArray) {
            String collection = aDefiningReqArray;
            if (collection.contains("?$")) {
                String splitCollName[] = collection.split("\\?");
                collection = splitCollName[0];
            }
            if (!syncHistoryDB.getSyncTimeBySpecificColl(SyncHistoryDB.COL_COLLECTION, collection).isEmpty())
                syncHistoryDB.updateRecord(collection, syncTime);
            else
                syncHistoryDB.createRecord(new SyncHistoryModel("", collection, syncTime));
        }
    }

    //check synchistory collection is there or not in defining request
    public static boolean getSyncHistoryColl(Context context){
        boolean check = Arrays.asList(Constants.getDefiningReq(context)).contains(Constants.SyncHistorys);
        return check;
    }
    /*update sync time in sqlite db*/
    public static void updatingSyncTime(final Context mContext, final ArrayList<String> alAssignColl, final String syncType,final RefreshListInterface listInterface,final String refguid) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SyncHistoryDB syncHistoryDB = new SyncHistoryDB(mContext);
                if (getOneRecord(syncHistoryDB).isEmpty()) {
                    initialInsert(mContext);
                }

                boolean checkSyncTcode = false;
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
                String syncTcode = sharedPreferences.getString(Constants.isSyncHistroyKey,"");
                if (syncTcode.equalsIgnoreCase(Constants.isSyncHistroyTcode)){
                    checkSyncTcode = true;
                }else{
                    checkSyncTcode = false;
                }
                boolean checkSyncHistoryColl = getSyncHistoryColl(mContext);
                try {
                    String syncTime = UtilConstants.getSyncHistoryddmmyyyyTime();
                    String strSPGUID = Constants.getSPGUID();
                    String StrSPGUID32 = "";
                    String parternTypeID = "";
                    if (!TextUtils.isEmpty(strSPGUID)) {
                        StrSPGUID32 = strSPGUID.replaceAll("-", "").toLowerCase();
                       // parternTypeID = OfflineManager.getPartnerTypeID(Constants.UserPartners + "?$select=PartnerTypeID &$filter=tolower(PartnerID) eq '" + StrSPGUID32 + "'");
                    }

                    if (parternTypeID!=null&&!TextUtils.isEmpty(parternTypeID)) {
                        String loginId = sharedPreferences.getString("username", "");
                        final ODataRequestParamBatch requestParamBatch = new ODataRequestParamBatchDefaultImpl();
                        for (int incReq =0; incReq<alAssignColl.size();incReq++) {
                            String colName =  alAssignColl.get(incReq);
                            if (colName.contains("?$")) {
                                String splitCollName[] = colName.split("\\?");
                                colName = splitCollName[0];
                            }
                            try {
                                LogManager.writeLogError("updatingSyncTime Sync History Collection Name : "+colName+" "+((Activity)mContext).getClass().toString());
                            } catch (Throwable e) {
                               e.printStackTrace();
                            }
                            syncHistoryDB.updateRecord(colName, syncTime);
                            //                try {
                            //                    String collectionQry = Constants.SyncHistorys + "?$filter=" +Constants.Collection + " eq'" + colName +"'";
                            //                    ODataEntity entity = OfflineManager.checkCollectionIsExist(collectionQry);
                            //                        //                        ODataEntity oDataEntity = OfflineManager.getSyncHistroyByCollection(collection);

                            String type="";
                            if (syncType.equalsIgnoreCase(Constants.Sync_All)) {
                                type = Constants.Sync_All;
                            } else if (syncType.equalsIgnoreCase(Constants.DownLoad)) {
                                type = Constants.DownLoad;
                            } else if (syncType.equalsIgnoreCase(Constants.UpLoad)) {
                                type = Constants.UpLoad;
                            } else if (syncType.equalsIgnoreCase(Constants.Auto_Sync)) {
                                type = Constants.Auto_Sync;
                            }else if (syncType.equalsIgnoreCase(Constants.Sync_initial)) {
                                type = Constants.Sync_All;
                            }else if (syncType.equalsIgnoreCase(Constants.Sync_Day_login)) {
                                type = Constants.Sync_All;
                            }
                            if (checkSyncTcode){
                                if(checkSyncHistoryColl) {
                                    //                            SyncUtils.createSyncHistory(colName, syncTime, type, StrSPGUID32, parternTypeID, loginId);
                                    Hashtable hashtable = SyncUtils.createSyncHistoryBatch(colName, syncTime, type, StrSPGUID32.toUpperCase(), parternTypeID, loginId.toUpperCase(),refguid);
                                    ODataEntity channelPartnerEntity = null;

                                    int id = incReq+1;
                                    String contentId = String.valueOf(id);
                                    ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();
                                    // Create change set
                                    batchItem.setResourcePath(Constants.SyncHistroy);
                                    batchItem.setMode(ODataRequestParamSingle.Mode.Create);
                                    batchItem.setContentID(contentId);
                                    batchItem.setPayload(channelPartnerEntity);
                                    ODataRequestChangeSet changeSetItem = new ODataRequestChangeSetDefaultImpl();
                                    changeSetItem.add(batchItem);
                                    try {
                                        requestParamBatch.add(changeSetItem);
                                    } catch (ODataException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }


                            //                } catch (Exception e) {
                            //                    e.printStackTrace();
                            //                }
                        }
                        try {
                            if (checkSyncTcode) {
                                OfflineManager.offlineStore.executeRequest(requestParamBatch);
                            }
                        } catch (Exception e) {
                            try {
                                throw new OfflineODataStoreException(e);
                            } catch (OfflineODataStoreException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                } catch (Exception exce) {
                    exce.printStackTrace();
                    LogManager.writeLogError(Constants.sync_table_history_txt + exce.getMessage());
                }
                try {
                    updatingSyncStartTime(mContext,syncType,Constants.EndSync,refguid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(listInterface != null){
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listInterface.refreshList();
                        }
                    });
                }
            }
        }).start();
    }

    /*update sync time in sqlite db*/
    public static void updatingSyncTime(Context mContext, ArrayList<String> alAssignColl) {
        SyncHistoryDB syncHistoryDB = new SyncHistoryDB(mContext);
        if (getOneRecord(syncHistoryDB).isEmpty()) {
            initialInsert(mContext);
        }
        try {
            String syncTime = UtilConstants.getSyncHistoryddmmyyyyTime();
            for (String colName : alAssignColl) {
                if (colName.contains("?$")) {
                    String splitCollName[] = colName.split("\\?");
                    colName = splitCollName[0];
                }
                syncHistoryDB.updateRecord(colName, syncTime);
            }
        } catch (Exception exce) {
            exce.printStackTrace();
            LogManager.writeLogError(Constants.sync_table_history_txt + exce.getMessage());
        }
    }
    /*update sync time in sync history through batch*/
    public static void updatingSyncHistroyTime(Context mContext, ArrayList<String> alAssignColl, String syncType, RefreshListInterface listInterface,String refGuid) {
        /*SyncHistoryDB syncHistoryDB = new SyncHistoryDB(mContext);
        if (getOneRecord(syncHistoryDB).isEmpty()) {
            initialInsert(mContext);
        }
        try {
            String syncTime = UtilConstants.getSyncHistoryddmmyyyyTime();
            for (String colName : alAssignColl) {
                if (colName.contains("?$")) {
                    String splitCollName[] = colName.split("\\?");
                    colName = splitCollName[0];
                }
                syncHistoryDB.updateRecord(colName, syncTime);
            }
        } catch (Exception exce) {
            exce.printStackTrace();
            LogManager.writeLogError(Constants.sync_table_history_txt + exce.getMessage());
        }*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                SyncHistoryDB syncHistoryDB = new SyncHistoryDB(mContext);
                if (getOneRecord(syncHistoryDB).isEmpty()) {
                    initialInsert(mContext);
                }
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME,0);
                String syncTcode = sharedPreferences.getString(Constants.isSyncHistroyKey,"");
                boolean checkSyncTCode = false;
                if(syncTcode.equalsIgnoreCase(Constants.isSyncHistroyTcode)) {
                    checkSyncTCode = true;
                }else {
                    checkSyncTCode = false;
                }
                boolean checkSyncHistoryColl = getSyncHistoryColl(mContext);
                try {
                    String syncTime = UtilConstants.getSyncHistoryddmmyyyyTime();
                    String strSPGUID = Constants.getSPGUID();
                    String StrSPGUID32 = "";
                    String parternTypeID = "";
                    if (!TextUtils.isEmpty(strSPGUID)) {
                        StrSPGUID32 = strSPGUID.replaceAll("-", "").toLowerCase();
                     //   parternTypeID = OfflineManager.getPartnerTypeID(Constants.UserPartners + "?$select=PartnerTypeID &$filter=tolower(PartnerID) eq '" + StrSPGUID32 + "'");
                    }
                    if (parternTypeID!=null&&!TextUtils.isEmpty(parternTypeID)) {
                        String loginId = sharedPreferences.getString("username", "");
                        final ODataRequestParamBatch requestParamBatch = new ODataRequestParamBatchDefaultImpl();
                        for (int incReq =0; incReq<alAssignColl.size();incReq++) {
                            String colName =  alAssignColl.get(incReq);
                            if (colName.contains("?$")) {
                                String splitCollName[] = colName.split("\\?");
                                colName = splitCollName[0];
                            }
                            try {
                                LogManager.writeLogError("updatingSyncHistroyTime Sync History Collection Name : "+colName+" "+((Activity)mContext).getClass().toString());
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            syncHistoryDB.updateRecord(colName, syncTime);
                            //                try {
                            //                    String collectionQry = Constants.SyncHistorys + "?$filter=" +Constants.Collection + " eq'" + colName +"'";
                            //                    ODataEntity entity = OfflineManager.checkCollectionIsExist(collectionQry);
                            //                        //                        ODataEntity oDataEntity = OfflineManager.getSyncHistroyByCollection(collection);
//                            if(checkSyncTCode) {
                                if (checkSyncHistoryColl) {
                                    //                            SyncUtils.createSyncHistory(colName, syncTime, syncType, StrSPGUID32, parternTypeID, loginId);
                                    Hashtable hashtable = SyncUtils.createSyncHistoryBatch(colName, syncTime, syncType, StrSPGUID32.toUpperCase(), parternTypeID, loginId.toUpperCase(), refGuid);
                                    ODataEntity channelPartnerEntity = null;

                                    int id = incReq + 1;
                                    String contentId = String.valueOf(id);
                                    ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();
                                    // Create change set
                                    batchItem.setResourcePath(Constants.SyncHistroy);
                                    batchItem.setMode(ODataRequestParamSingle.Mode.Create);
                                    batchItem.setContentID(contentId);
                                    batchItem.setPayload(channelPartnerEntity);

                                    Map<String, String> createHeaders = new HashMap<String, String>();
                                    createHeaders.put("OfflineOData.RemoveAfterUpload", "true");
                                    batchItem.getCustomHeaders().putAll(createHeaders);

                                    ODataRequestChangeSet changeSetItem = new ODataRequestChangeSetDefaultImpl();
                                    changeSetItem.add(batchItem);
                                    try {
                                        requestParamBatch.add(changeSetItem);
                                    } catch (ODataException e) {
                                        e.printStackTrace();
                                    }
                                }
//                            }

                            //                } catch (Exception e) {
                            //                    e.printStackTrace();
                            //                }
                        }
                        try {
//                            if(checkSyncTCode) {
                                OfflineManager.offlineStore.executeRequest(requestParamBatch);
//                            }
                        } catch (Exception e) {
                            try {
                                throw new OfflineODataStoreException(e);
                            } catch (OfflineODataStoreException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                } catch (Exception exce) {
                    exce.printStackTrace();
                    LogManager.writeLogError(Constants.sync_table_history_txt + exce.getMessage());
                }
                try {
                    updatingSyncStartTime(mContext,syncType,Constants.EndSync,refGuid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(listInterface != null){
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listInterface.refreshList();
                        }
                    });
                }
            }
        }).start();
    }
    public static void SyncHistroyTime(Context mContext, ArrayList<String> alAssignColl, String syncType, RefreshListInterface listInterface,String refGuid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SyncHistoryDB syncHistoryDB = new SyncHistoryDB(mContext);
                if (getOneRecord(syncHistoryDB).isEmpty()) {
                    initialInsert(mContext);
                }
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME,0);
                String syncTcode = sharedPreferences.getString(Constants.isSyncHistroyKey,"");
                boolean checkSyncTCode = false;
                if(syncTcode.equalsIgnoreCase(Constants.isSyncHistroyTcode)) {
                    checkSyncTCode = true;
                }else {
                    checkSyncTCode = false;
                }
                boolean checkSyncHistoryColl = getSyncHistoryColl(mContext);
                try {
                    String syncTime = UtilConstants.getSyncHistoryddmmyyyyTime();
                    String strSPGUID = Constants.getSPGUID();
                    String StrSPGUID32 = "";
                    String parternTypeID = "";
                    if (!TextUtils.isEmpty(strSPGUID)) {
                        StrSPGUID32 = strSPGUID.replaceAll("-", "").toLowerCase();
                     //   parternTypeID = OfflineManager.getPartnerTypeID(Constants.UserPartners + "?$select=PartnerTypeID &$filter=tolower(PartnerID) eq '" + StrSPGUID32 + "'");
                    }
                    if (parternTypeID!=null&&!TextUtils.isEmpty(parternTypeID)) {
                        String loginId = sharedPreferences.getString("username", "");
                        final ODataRequestParamBatch requestParamBatch = new ODataRequestParamBatchDefaultImpl();
                        for (int incReq =0; incReq<alAssignColl.size();incReq++) {
                            String colName =  alAssignColl.get(incReq);
                            if (colName.contains("?$")) {
                                String splitCollName[] = colName.split("\\?");
                                colName = splitCollName[0];
                            }
                            try {
                                LogManager.writeLogError("SyncHistroyTime Sync History Collection Name : "+colName+" "+((Activity)mContext).getClass().toString());
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            syncHistoryDB.updateRecord(colName, syncTime);
                            //                try {
                            //                    String collectionQry = Constants.SyncHistorys + "?$filter=" +Constants.Collection + " eq'" + colName +"'";
                            //                    ODataEntity entity = OfflineManager.checkCollectionIsExist(collectionQry);
                            //                        //                        ODataEntity oDataEntity = OfflineManager.getSyncHistroyByCollection(collection);
                            if(checkSyncTCode) {
                                if (checkSyncHistoryColl) {
                                    //                            SyncUtils.createSyncHistory(colName, syncTime, syncType, StrSPGUID32, parternTypeID, loginId);
                                    Hashtable hashtable = SyncUtils.createSyncHistoryBatch(colName, syncTime, syncType, StrSPGUID32, parternTypeID, loginId, refGuid);
                                    ODataEntity channelPartnerEntity = null;

                                    int id = incReq + 1;
                                    String contentId = String.valueOf(id);
                                    ODataRequestParamSingle batchItem = new ODataRequestParamSingleDefaultImpl();
                                    // Create change set
                                    batchItem.setResourcePath(Constants.SyncHistroy);
                                    batchItem.setMode(ODataRequestParamSingle.Mode.Create);
                                    batchItem.setContentID(contentId);
                                    batchItem.setPayload(channelPartnerEntity);

                                    Map<String, String> createHeaders = new HashMap<String, String>();
                                    createHeaders.put("OfflineOData.RemoveAfterUpload", "true");
                                    batchItem.getCustomHeaders().putAll(createHeaders);

                                    ODataRequestChangeSet changeSetItem = new ODataRequestChangeSetDefaultImpl();
                                    changeSetItem.add(batchItem);
                                    try {
                                        requestParamBatch.add(changeSetItem);
                                    } catch (ODataException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            //                } catch (Exception e) {
                            //                    e.printStackTrace();
                            //                }
                        }
                        try {
                            if(checkSyncTCode) {
                                OfflineManager.offlineStore.executeRequest(requestParamBatch);
                            }
                        } catch (Exception e) {
                            try {
                                throw new OfflineODataStoreException(e);
                            } catch (OfflineODataStoreException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                } catch (Exception exce) {
                    exce.printStackTrace();
                    LogManager.writeLogError(Constants.sync_table_history_txt + exce.getMessage());
                }
                try {
                    updatingSyncStartTime(mContext,syncType,Constants.EndSync,refGuid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(listInterface != null){
                    listInterface.refreshList();

                }
            }
        }).start();
    }

    public static void updatingSyncStartTime(final Context mContext, final String syncType, final String syncMsg,String refGuid) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME,0);
        String syncTcode = sharedPreferences.getString(Constants.isSyncHistroyKey,"");
//        if(syncTcode.equalsIgnoreCase(Constants.isSyncHistroyTcode)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SyncHistoryDB syncHistoryDB = new SyncHistoryDB(mContext);
                    if (getOneRecord(syncHistoryDB).isEmpty()) {
                        initialInsert(mContext);
                    }

                    boolean checkSyncHistoryColl = getSyncHistoryColl(mContext);
                    try {
                        String syncTime = "";
                        if (syncMsg.equalsIgnoreCase(Constants.StartSync)) {
                            if (syncType.equalsIgnoreCase(Constants.Sync_initial)){
                                syncTime = Constants.syncStartTime;
                            }else{
                                syncTime = UtilConstants.getSyncHistoryddmmyyyyTime();
                            }
                        } else {
                            syncTime = Constants.getSyncHistoryddmmyyyyTimeDelay();
                        }
                        String strSPGUID = Constants.getSPGUID();
                        String StrSPGUID32 = "";
                        String parternTypeID = "";
                        String loginId = "";
                        if (!TextUtils.isEmpty(strSPGUID)) {
                            StrSPGUID32 = strSPGUID.replaceAll("-", "").toLowerCase();
                            //parternTypeID = OfflineManager.getPartnerTypeID(Constants.UserPartners + "?$select=PartnerTypeID &$filter=tolower(PartnerID) eq '" + StrSPGUID32 + "'");
                           // loginId = OfflineManager.getLoginID(Constants.UserPartners + "?$select=LoginID &$filter=tolower(PartnerID) eq '" + StrSPGUID32 + "'");
                        }

                        if(TextUtils.isEmpty(loginId)) {
                            loginId = sharedPreferences.getString("username", "");
                        }
                        if (parternTypeID!=null&&!TextUtils.isEmpty(parternTypeID)) {
                            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
                            String startColl = "";
                            String type = "";
                            try {
                                if (syncMsg.equalsIgnoreCase(Constants.StartSync)) {
                                    if (syncType.equalsIgnoreCase(Constants.Sync_All)) {
                                        startColl = "All Download Start";
                                        type = Constants.Sync_All;
                                    } else if (syncType.equalsIgnoreCase(Constants.DownLoad)) {
                                        startColl = "Download Start";
                                        type = Constants.DownLoad;
                                    } else if (syncType.equalsIgnoreCase(Constants.UpLoad)) {
                                        startColl = "Upload Start";
                                        type = Constants.UpLoad;
                                    } else if (syncType.equalsIgnoreCase(Constants.Sync_FR_START)) {
                                        startColl = "FR - Start";
                                        type = Constants.UpLoad;
                                    } else if (syncType.equalsIgnoreCase(Constants.Sync_FR_CAMERA)) {
                                        startColl = "FR - Camera Opened";
                                        type = Constants.UpLoad;
                                    }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_CAMERA_CAPUTURED)) {
                                        startColl = "FR - Photo Captured";
                                        type = Constants.UpLoad;
                                    }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_POST)) {
                                        startColl = "FR - Photo to API POST Start";
                                        type = Constants.UpLoad;
                                    }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_POST_END)) {
                                        startColl = "FR - Photo to API POST End";
                                        type = Constants.UpLoad;
                                    }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_STATUS_601)) {
                                        startColl = "FR - 601";
                                        type = Constants.UpLoad;
                                    }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_STATUS_602)) {
                                        startColl = "FR - 602";
                                        type = Constants.UpLoad;
                                    } else if (syncType.equalsIgnoreCase(Constants.Sync_FR_STATUS_603)) {
                                        startColl = "FR - 603";
                                        type = Constants.UpLoad;
                                    }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_STATUS_604)) {
                                        startColl = "FR - 604";
                                        type = Constants.UpLoad;
                                    }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_STATUS_606)) {
                                        startColl = "FR - 606";
                                        type = Constants.UpLoad;
                                    }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_STATUS_607)) {
                                        startColl = "FR - Unknown Error";
                                        type = Constants.UpLoad;
                                    }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_RETRY)) {
                                        startColl = "FR - Retry Start";
                                        type = Constants.UpLoad;
                                    }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_ATT_MARK)) {
                                        startColl = "FR - Attendance Marked Offline";
                                        type = Constants.UpLoad;
                                    }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_ATT_POST)) {
                                        startColl = "FR - Attendance POST Start";
                                        type = Constants.UpLoad;
                                    } else if (syncType.equalsIgnoreCase(Constants.Sync_FR_ATT_POST_END)) {
                                        startColl = "FR - Attendance POST End";
                                        type = Constants.UpLoad;
                                    } else if (syncType.equalsIgnoreCase(Constants.Sync_FR_END)) {
                                        startColl = "FR - End";
                                        type = Constants.UpLoad;
                                    } else if (syncType.equalsIgnoreCase(Constants.Auto_Sync)) {
                                        startColl = "Auto Sync Start";
                                        type = Constants.Auto_Sync;
                                    }else if (syncType.equalsIgnoreCase(Constants.Sync_initial)) {
                                        startColl = "Initial Sync Start";
                                        type = Constants.Sync_All;
                                    }else if (syncType.equalsIgnoreCase(Constants.Sync_Day_login)) {
                                        startColl = "Day Login Sync Start";
                                        type = Constants.Sync_All;
                                    }
                                } else {
                                    if (syncType.equalsIgnoreCase(Constants.Sync_All)) {
                                        startColl = "All Download End";
                                        type = Constants.Sync_All;
                                    } else if (syncType.equalsIgnoreCase(Constants.DownLoad)) {
                                        startColl = "Download End";
                                        type = Constants.DownLoad;
                                    } else if (syncType.equalsIgnoreCase(Constants.UpLoad)) {
                                        startColl = "Upload End";
                                        type = Constants.UpLoad;
                                    } else if (syncType.equalsIgnoreCase(Constants.Auto_Sync)) {
                                        startColl = "Auto Sync End";
                                        type = Constants.Auto_Sync;
                                    }else if (syncType.equalsIgnoreCase(Constants.Sync_initial)) {
                                        startColl = "Initial Sync End";
                                        type = Constants.Sync_All;
                                    }else if (syncType.equalsIgnoreCase(Constants.Sync_Day_login)) {
                                        startColl = "Day Login Sync End";
                                        type = Constants.Sync_All;
                                    }else if (syncType.equalsIgnoreCase(Constants.SO_SYNC_BG)) {
                                        startColl = "BackGround Sync End";
                                        type = Constants.UpLoad;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (checkSyncHistoryColl) {
                                try {
                                    LogManager.writeLogError("updatingSyncStartTime Sync History Collection Name : "+startColl+" "+((Activity)mContext).getClass().toString());
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                                SyncUtils.createSyncHistory(startColl, syncTime, type, StrSPGUID32.toUpperCase(), parternTypeID, loginId.toUpperCase(), mContext, refGuid);
                            }
                        }
                    } catch (Exception exce) {
                        exce.printStackTrace();
                        LogManager.writeLogError(Constants.sync_table_history_txt + exce.getMessage());
                    }
                }
            }).start();
//        }
    }

    public static void updatingSyncStartTime1(final Context mContext, final String syncType, final String syncMsg,String refGuid) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME,0);
        String syncTcode = sharedPreferences.getString(Constants.isSyncHistroyKey,"");
//        if(syncTcode.equalsIgnoreCase(Constants.isSyncHistroyTcode)) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                SyncHistoryDB syncHistoryDB = new SyncHistoryDB(mContext);
                if (getOneRecord(syncHistoryDB).isEmpty()) {
                    initialInsert(mContext);
                }

                boolean checkSyncHistoryColl = getSyncHistoryColl(mContext);
                try {
                    String syncTime = "";
                    if (syncMsg.equalsIgnoreCase(Constants.StartSync)) {
                        if (syncType.equalsIgnoreCase(Constants.Sync_initial)){
                            syncTime = Constants.syncStartTime;
                        }else{
                            syncTime = UtilConstants.getSyncHistoryddmmyyyyTime();
                        }
                    } else {
                        syncTime = Constants.getSyncHistoryddmmyyyyTimeDelay();
                    }
                    String strSPGUID = Constants.getSPGUID();
                    String StrSPGUID32 = "";
                    String parternTypeID = "";
                    String loginId = "";
                    if (!TextUtils.isEmpty(strSPGUID)) {
                        StrSPGUID32 = strSPGUID.replaceAll("-", "").toLowerCase();
                      //  parternTypeID = OfflineManager.getPartnerTypeID(Constants.UserPartners + "?$select=PartnerTypeID &$filter=tolower(PartnerID) eq '" + StrSPGUID32 + "'");
                       // loginId = OfflineManager.getLoginID(Constants.UserPartners + "?$select=LoginID &$filter=tolower(PartnerID) eq '" + StrSPGUID32 + "'");
                    }

                    if(TextUtils.isEmpty(loginId)) {
                        loginId = sharedPreferences.getString("username", "");
                    }
                    if (parternTypeID!=null&&!TextUtils.isEmpty(parternTypeID)) {
                        String startColl = "";
                        String type = "";
                        try {
                            if (syncMsg.equalsIgnoreCase(Constants.StartSync)) {
                                if (syncType.equalsIgnoreCase(Constants.Sync_All)) {
                                    startColl = "All Download Start";
                                    type = Constants.Sync_All;
                                } else if (syncType.equalsIgnoreCase(Constants.DownLoad)) {
                                    startColl = "Download Start";
                                    type = Constants.DownLoad;
                                } else if (syncType.equalsIgnoreCase(Constants.UpLoad)) {
                                    startColl = "Upload Start";
                                    type = Constants.UpLoad;
                                } else if (syncType.equalsIgnoreCase(Constants.Sync_FR_START)) {
                                    startColl = "FR - Start";
                                    type = Constants.UpLoad;
                                } else if (syncType.equalsIgnoreCase(Constants.Sync_FR_CAMERA)) {
                                    startColl = "FR - Camera Opened";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_CAMERA_CAPUTURED)) {
                                    startColl = "FR - Photo Captured";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_POST)) {
                                    startColl = "FR - Photo to API POST Start";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_POST_END)) {
                                    startColl = "FR - Photo to API POST End";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_STATUS_601)) {
                                    startColl = "FR - 601";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_STATUS_602)) {
                                    startColl = "FR - 602";
                                    type = Constants.UpLoad;
                                } else if (syncType.equalsIgnoreCase(Constants.Sync_FR_STATUS_603)) {
                                    startColl = "FR - 603";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_STATUS_604)) {
                                    startColl = "FR - 604";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_STATUS_606)) {
                                    startColl = "FR - 606";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_STATUS_607)) {
                                    startColl = "FR - Unknown Error";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_RETRY)) {
                                    startColl = "FR - Retry Start";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_ATT_MARK)) {
                                    startColl = "FR - Attendance Marked Offline";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_ATT_SAVING)) {
                                    startColl = "FR - Attendance Saving in Offline";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_ATT_POST)) {
                                    startColl = "FR - Attendance POST Start";
                                    type = Constants.UpLoad;
                                } else if (syncType.equalsIgnoreCase(Constants.Sync_FR_ATT_POST_END)) {
                                    startColl = "FR - Attendance POST End";
                                    type = Constants.UpLoad;
                                } else if (syncType.equalsIgnoreCase(Constants.Sync_FR_END)) {
                                    startColl = "FR - End";
                                    type = Constants.UpLoad;
                                } else if (syncType.equalsIgnoreCase(Constants.Sync_FR_LOCATION_PERM)) {
                                    startColl = "FR - LOCATION PER GRANTED";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_ATT_SAVING_LOC)) {
                                    startColl = "FR - ATT DATA NOT POSTED DUE NETWORK ISSUE";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_GPS_PERM)) {
                                    startColl = "FR - LOCATION GPS GRANTED";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_AUTODATE_PERM)) {
                                    startColl = "FR - AUTO DATE PER GRANTED";
                                    type = Constants.UpLoad;
                                } else if (syncType.equalsIgnoreCase(Constants.Sync_FR_LOCATION_NOT_PERM)) {
                                    startColl = "FR - LOCATION PER NOT GRANTED";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_GPS_NOT_PERM)) {
                                    startColl = "FR - LOCATION GPS NOT GRANTED";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_FR_AUTODATE_NOT_PERM)) {
                                    startColl = "FR - AUTO DATE PER NOT GRANTED";
                                    type = Constants.UpLoad;
                                } else if (syncType.equalsIgnoreCase(Constants.Auto_Sync)) {
                                    startColl = "Auto Sync Start";
                                    type = Constants.Auto_Sync;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_initial)) {
                                    startColl = "Initial Sync Start";
                                    type = Constants.Sync_All;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_Day_login)) {
                                    startColl = "Day Login Sync Start";
                                    type = Constants.Sync_All;
                                }
                            } else {
                                if (syncType.equalsIgnoreCase(Constants.Sync_All)) {
                                    startColl = "All Download End";
                                    type = Constants.Sync_All;
                                } else if (syncType.equalsIgnoreCase(Constants.DownLoad)) {
                                    startColl = "Download End";
                                    type = Constants.DownLoad;
                                } else if (syncType.equalsIgnoreCase(Constants.UpLoad)) {
                                    startColl = "Upload End";
                                    type = Constants.UpLoad;
                                } else if (syncType.equalsIgnoreCase(Constants.Auto_Sync)) {
                                    startColl = "Auto Sync End";
                                    type = Constants.Auto_Sync;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_initial)) {
                                    startColl = "Initial Sync End";
                                    type = Constants.Sync_All;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_Day_login)) {
                                    startColl = "Day Login Sync End";
                                    type = Constants.Sync_All;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (checkSyncHistoryColl) {
                            try {
                                LogManager.writeLogError("updatingSyncStartTime1 Sync History Collection Name : "+startColl+" "+((Activity)mContext).getClass().toString());
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            SyncUtils.createSyncHistory(startColl, syncTime, type, StrSPGUID32.toUpperCase(), parternTypeID, loginId.toUpperCase(), mContext, refGuid);
                        }
                    }
                } catch (Exception exce) {
                    exce.printStackTrace();
                    LogManager.writeLogError(Constants.sync_table_history_txt + exce.getMessage());
                }
//            }
//        }).start();
//        }
    }

    public static void updatingDaySyncStartTime(final Context mContext, final String syncType, final String syncMsg,String refGuid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SyncHistoryDB syncHistoryDB = new SyncHistoryDB(mContext);
                if (getOneRecord(syncHistoryDB).isEmpty()) {
                    initialInsert(mContext);
                }

                boolean checkSyncHistoryColl = getSyncHistoryColl(mContext);
                try {
                    String syncTime = "";
//                        if (syncMsg.equalsIgnoreCase(Constants.StartSync)) {
//                            if (syncType.equalsIgnoreCase(Constants.Sync_initial)){
//                                syncTime = Constants.syncStartTime;
//                            }else{
//                                syncTime = UtilConstants.getSyncHistoryddmmyyyyTime();
//                            }
//                        } else {
                    syncTime = Constants.getSyncHistoryddmmyyyyTimeDelay();
//                        }
                    String strSPGUID = Constants.getSPGUID();
                    String StrSPGUID32 = "";
                    String parternTypeID = "";
                    if (!TextUtils.isEmpty(strSPGUID)) {
                        StrSPGUID32 = strSPGUID.replaceAll("-", "").toLowerCase();
                       // parternTypeID = OfflineManager.getPartnerTypeID(Constants.UserPartners + "?$select=PartnerTypeID &$filter=tolower(PartnerID) eq '" + StrSPGUID32 + "'");
                    }
                    if (parternTypeID!=null&&!TextUtils.isEmpty(parternTypeID)) {
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
                        String loginId = sharedPreferences.getString("username", "");
                        String startColl = "";
                        String type = "";
                        try {
                            if (syncMsg.equalsIgnoreCase(Constants.StartSync)) {
                                if (syncType.equalsIgnoreCase(Constants.Sync_All)) {
                                    startColl = "All Download Start";
                                    type = Constants.Sync_All;
                                } else if (syncType.equalsIgnoreCase(Constants.DownLoad)) {
                                    startColl = "Download Start";
                                    type = Constants.DownLoad;
                                } else if (syncType.equalsIgnoreCase(Constants.UpLoad)) {
                                    startColl = "Upload Start";
                                    type = Constants.UpLoad;
                                } else if (syncType.equalsIgnoreCase(Constants.Auto_Sync)) {
                                    startColl = "Auto Sync Start";
                                    type = Constants.Auto_Sync;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_initial)) {
                                    startColl = "Initial Sync Start";
                                    type = Constants.Sync_All;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_Day_login)) {
                                    startColl = "Day Login Sync Start";
                                    type = Constants.Sync_All;
                                }else if (syncType.equalsIgnoreCase(Constants.add_outlet_start)) {
                                    startColl = "Add Outlet Start";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.add_outlet_mob_start)) {
                                    startColl = "Add Outlet Mobile Ver Start";
                                    type = Constants.DownLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.add_outlet_post_start)) {
                                    startColl = "Add Outlet POST Start";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.add_outlet_refresh_start)) {
                                    startColl = "Add Outlet Refresh Start";
                                    type = Constants.DownLoad;
                                }
                            } else {
                                if (syncType.equalsIgnoreCase(Constants.Sync_All)) {
                                    startColl = "All Download End";
                                    type = Constants.Sync_All;
                                } else if (syncType.equalsIgnoreCase(Constants.DownLoad)) {
                                    startColl = "Download End";
                                    type = Constants.DownLoad;
                                } else if (syncType.equalsIgnoreCase(Constants.UpLoad)) {
                                    startColl = "Upload End";
                                    type = Constants.UpLoad;
                                } else if (syncType.equalsIgnoreCase(Constants.Auto_Sync)) {
                                    startColl = "Auto Sync End";
                                    type = Constants.Auto_Sync;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_initial)) {
                                    startColl = "Initial Sync End";
                                    type = Constants.Sync_All;
                                }else if (syncType.equalsIgnoreCase(Constants.Sync_Day_login)) {
                                    startColl = "Day Login Sync End";
                                    type = Constants.Sync_All;
                                }else if (syncType.equalsIgnoreCase(Constants.add_outlet_start)) {
                                    startColl = "Add Outlet End";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.add_outlet_mob_start)) {
                                    startColl = "Add Outlet Mobile Ver End";
                                    type = Constants.DownLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.add_outlet_post_start)) {
                                    startColl = "Add Outlet POST End";
                                    type = Constants.UpLoad;
                                }else if (syncType.equalsIgnoreCase(Constants.add_outlet_refresh_start)) {
                                    startColl = "Add Outlet Refresh End";
                                    type = Constants.DownLoad;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (checkSyncHistoryColl) {
                            try {
                                LogManager.writeLogError("updatingDaySyncStartTime Sync History Collection Name : "+startColl+" "+((Activity)mContext).getClass().toString());
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            SyncUtils.createDaySyncHistory(startColl, syncTime, type,syncType, StrSPGUID32.toUpperCase(), parternTypeID, loginId.toUpperCase(), mContext, refGuid);
                        }
                    }
                } catch (Exception exce) {
                    exce.printStackTrace();
                    LogManager.writeLogError(Constants.sync_table_history_txt + exce.getMessage());
                }
            }
        }).start();
    }

    public static void createSyncHistory(String collectionName, String syncTime, String syncType, String StrSPGUID32, String parternTypeID, String loginId,Context context, String refGuid) {
        try {
            Thread.sleep(100);

            if (collectionName.equalsIgnoreCase("ConfigTypsetTypeValues") && syncType.equals(Constants.UpLoad)) {
                syncType = Constants.DownLoad;
            }

            GUID guid = GUID.newRandom();
            Hashtable hashtable = new Hashtable();
            hashtable.put(Constants.SyncHisGuid, guid.toString().toUpperCase());
            if (!collectionName.equals("") && collectionName != null) {
                hashtable.put(Constants.Collection, collectionName);
            }
            try {
                LogManager.writeLogError("createSyncHistory Sync History Collection Name : "+collectionName+" "+((Activity)context).getClass().toString());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if(!TextUtils.isEmpty(collectionName) && collectionName.contains("End"))
                hashtable.put(Constants.SyncHisGuid, guid);
            else
                hashtable.put(Constants.SyncHisGuid, refGuid);
            hashtable.put(Constants.SyncApplication, "com.arteriatech.ss.msec.bil.v4");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
            String time = "";
            String strDate = "";
            try {
                Date date = dateFormat.parse(syncTime);
                strDate = dateFormat.format(date);
                time = timeFormat.format(date.parse(syncTime));

            } catch (ParseException ex) {
                ex.printStackTrace();
                Log.v("Exception", ex.getLocalizedMessage());
            }
            ODataDuration startDuration = null;
            try {
                if (!time.isEmpty()) {
                    startDuration = Constants.getTimeAsODataDurationConvertion(time);
                    hashtable.put(Constants.SyncTime, startDuration);
                } else {
                    hashtable.put(Constants.SyncTime, startDuration);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            hashtable.put(Constants.SyncDate, strDate);

            hashtable.put(Constants.SyncType, syncType);

            hashtable.put(Constants.PartnerId, StrSPGUID32);
            hashtable.put(Constants.PartnerType, parternTypeID);
            hashtable.put(Constants.LoginId, loginId);
            hashtable.put(Constants.RefGUID, refGuid);
//            hashtable.put(Constants.Remarks,getDeviceName() + " (" + mapTable.get(Constants.AppVisibility) + ")");
          //  OfflineManager.CreateSyncHistroy(hashtable,context);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createDaySyncHistory(String collectionName, String syncTime, String syncType, String syncTypeID, String StrSPGUID32, String parternTypeID, String loginId,Context context, String refGuid) {
        try {
            Thread.sleep(100);

            if (collectionName.equalsIgnoreCase("ConfigTypsetTypeValues") && syncType.equals(Constants.UpLoad)) {
                syncType = Constants.DownLoad;
            }

            GUID guid = GUID.newRandom();
            Hashtable hashtable = new Hashtable();
            hashtable.put(Constants.SyncHisGuid, guid.toString().toUpperCase());
            if (!collectionName.equals("") && collectionName != null) {
                hashtable.put(Constants.Collection, collectionName);
            }
            try {
                LogManager.writeLogError("createDaySyncHistory Sync History Collection Name : "+collectionName+" "+((Activity)context).getClass().toString());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if(!TextUtils.isEmpty(collectionName) && collectionName.contains("End")) {
                hashtable.put(Constants.SyncHisGuid, guid);
            }else{
                if(syncTypeID.equalsIgnoreCase(Constants.add_outlet_mob_start) || syncTypeID.equalsIgnoreCase(Constants.add_outlet_post_start) || syncTypeID.equalsIgnoreCase(Constants.add_outlet_refresh_start)){
                    hashtable.put(Constants.SyncHisGuid, guid);
                }else {
                    hashtable.put(Constants.SyncHisGuid, refGuid);
                }
            }

            hashtable.put(Constants.SyncApplication, "com.arteriatech.ss.msec.bil.v4");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
            String time = "";
            String strDate = "";
            try {
                Date date = dateFormat.parse(syncTime);
                strDate = dateFormat.format(date);
                time = timeFormat.format(date.parse(syncTime));

            } catch (ParseException ex) {
                ex.printStackTrace();
                Log.v("Exception", ex.getLocalizedMessage());
            }
            ODataDuration startDuration = null;
            try {
                if (!time.isEmpty()) {
                    startDuration = Constants.getTimeAsODataDurationConvertion(time);
                    hashtable.put(Constants.SyncTime, startDuration);
                } else {
                    hashtable.put(Constants.SyncTime, startDuration);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            hashtable.put(Constants.SyncDate, strDate);

            hashtable.put(Constants.SyncType, syncType);

            hashtable.put(Constants.PartnerId, StrSPGUID32);
            hashtable.put(Constants.PartnerType, parternTypeID);
            hashtable.put(Constants.LoginId, loginId);
            hashtable.put(Constants.RefGUID, refGuid);
//            hashtable.put(Constants.Remarks,getDeviceName() + " (" + mapTable.get(Constants.AppVisibility) + ")");
           // OfflineManager.CreateSyncHistroy(hashtable,context);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Calendar convertDateFormat(String dateVal) {
        Date date = null;
        Calendar curCal = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        try {
            date = format.parse(dateVal);
            curCal.setTime(date);
            System.out.println("Date" + curCal.getTime());
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return curCal;
    }

    //sync history offline batch post
    public static Hashtable createSyncHistoryBatch(String collectionName, String syncTime, String syncType, String StrSPGUID32, String parternTypeID, String loginId, String refGuid) {
        Hashtable hashtable = new Hashtable();
        try {
            Thread.sleep(100);

            if (collectionName.equalsIgnoreCase("ConfigTypsetTypeValues") && syncType.equals(Constants.UpLoad)) {
                syncType = Constants.DownLoad;
            }

            GUID guid = GUID.newRandom();
            hashtable.put(Constants.SyncHisGuid, guid.toString().toUpperCase());
            if (!collectionName.equals("") && collectionName != null) {
                hashtable.put(Constants.Collection, collectionName);
            }
            hashtable.put(Constants.SyncApplication, "com.arteriatech.ss.msec.bil.v4");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
            String time = "";
            String strDate = "";
            try {
                Date date = dateFormat.parse(syncTime);
                strDate = dateFormat.format(date);
                time = timeFormat.format(date.parse(syncTime));

            } catch (ParseException ex) {
                ex.printStackTrace();
                Log.v("Exception", ex.getLocalizedMessage());
            }
            ODataDuration startDuration = null;
            try {
                if (!time.isEmpty()) {
                    startDuration = Constants.getTimeAsODataDurationConvertion(time);
                    hashtable.put(Constants.SyncTime, startDuration);
                } else {
                    hashtable.put(Constants.SyncTime, startDuration);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            hashtable.put(Constants.SyncDate, strDate);

            hashtable.put(Constants.SyncType, syncType);

            hashtable.put(Constants.PartnerId, StrSPGUID32);
            hashtable.put(Constants.PartnerType, parternTypeID);
            hashtable.put(Constants.LoginId, loginId);
            hashtable.put(Constants.RefGUID,refGuid );
//            hashtable.put(Constants.Remarks,getDeviceName() + " (" + mapTable.get(Constants.AppVisibility) + ")");
//            OfflineManager.CreateSyncHistroy(hashtable);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashtable;
    }
    public static ArrayList<String> getComplintsList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Constants.Complaints);
        return arrayList;
    }

}

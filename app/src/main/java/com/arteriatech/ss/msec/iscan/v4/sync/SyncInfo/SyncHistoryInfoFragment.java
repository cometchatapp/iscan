package com.arteriatech.ss.msec.iscan.v4.sync.SyncInfo;


import static com.arteriatech.ss.msec.iscan.v4.common.Constants.REPEATABLE_REQUEST_ID;
import static com.arteriatech.ss.msec.iscan.v4.common.Constants.isAllSync;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.server_Text;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.server_Text_DR;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.server_Text_default;
import static com.arteriatech.ss.msec.iscan.v4.registration.RegistrationActivity.registrationModel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.AvailableServer;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.asyncTask.RefreshAsyncTask;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.common.MSFAApplication;
import com.arteriatech.ss.msec.iscan.v4.log.ErrorCode;
import com.arteriatech.ss.msec.iscan.v4.mbo.ErrorBean;
import com.arteriatech.ss.msec.iscan.v4.mutils.adapter.SimpleRecyclerViewAdapter;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.Operation;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.sync.SyncHistoryDB;
import com.arteriatech.ss.msec.iscan.v4.mutils.sync.SyncHistoryModel;

import com.arteriatech.ss.msec.iscan.v4.registration.Configuration;

import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.store.OnlineManager;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncSelectionActivity;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncUtils;
import com.sap.client.odata.v4.core.GUID;
import com.sap.smp.client.odata.exception.ODataException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class SyncHistoryInfoFragment extends Fragment implements UIListener, View.OnClickListener, CollectionSyncInterface {

    ArrayList<String> tempCPList = new ArrayList<>();
    int updateCancelSOCount = 0;
    int cancelSoPos = 0;
    int mIntPendingCollVal = 0;
    Hashtable dbHeadTable;
    ArrayList<HashMap<String, String>> arrtable;
    String[][] invKeyValues;
    ArrayList<String> alAssignColl = new ArrayList<>();
    private  JSONObject fetchJsonHeaderObject=null;
    ArrayList<String> alFlushColl = new ArrayList<>();
    String concatCollectionStr = "";
    String concatFlushCollStr = "";
    String endPointURL = "";
    String appConnID = "";
    String syncType = "";
    boolean onlineStoreOpen = false;
    PendingCountAdapter pendingCountAdapter;
    private RecyclerView recycler_view_His, rvSyncTime;
    private int pendingCount = 0;
    private boolean mBoolIsNetWorkNotAval = false;
    private boolean mBoolIsReqResAval = false;
    private boolean isBatchReqs = false;
    private boolean tokenFlag = false;
    private int penReqCount = 0;
    private ProgressDialog syncProgDialog = null;
    private boolean dialogCancelled = false;
    private int mError = 0;
    private List<PendingCountBean> pendingCountBeanList = new ArrayList<>();
    private ArrayList<SyncHistoryModel> syncHistoryModelList = new ArrayList<>();
    private ImageView ivUploadDownload, ivSyncAll;
    private TextView tvPendingCount, tvPendingStatus;
    private NestedScrollView nestedScroll;
    private LinearLayout cvUpdatePending;
    private SimpleRecyclerViewAdapter<SyncHistoryModel> simpleUpdateHistoryAdapter;
    private GUID refguid=null;

    public SyncHistoryInfoFragment() {
        // Required empty public constructor
    }

    private static int getPendingListSize(Context mContext) {
        int size = 0;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);

        Set<String> set = new HashSet<>();
        set = sharedPreferences.getStringSet(Constants.Feedbacks, null);
        if (set != null && !set.isEmpty()) {
            size = size + set.size();
        }
        set = sharedPreferences.getStringSet(Constants.SecondarySOCreate, null);
        if (set != null && !set.isEmpty()) {
            size = size + set.size();
        }
        set = sharedPreferences.getStringSet(Constants.FinancialPostings, null);
        if (set != null && !set.isEmpty()) {
            size = size + set.size();
        }
        set = sharedPreferences.getStringSet(Constants.SampleDisbursement, null);
        if (set != null && !set.isEmpty()) {
            size = size + set.size();
        }
        set = sharedPreferences.getStringSet(Constants.CPList, null);
        if (set != null && !set.isEmpty()) {
            size = size + set.size();
        }
        set = sharedPreferences.getStringSet(Constants.ROList, null);
        if (set != null && !set.isEmpty()) {
            size = size + set.size();
        }
        set = sharedPreferences.getStringSet(Constants.Expenses, null);
        if (set != null && !set.isEmpty()) {
            size = size + set.size();
        }
        return size;
    }

    public static ArrayList<Object> getPendingCollList(Context mContext, boolean isFromAutoSync) {
        ArrayList<Object> objectsArrayList = new ArrayList<>();
        int mIntPendingCollVal = 0;
        String[][] invKeyValues = null;
        Set<String> set = new HashSet<>();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        invKeyValues = new String[getPendingListSize(mContext)][2];
        set = sharedPreferences.getStringSet(Constants.Feedbacks, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.Feedbacks;
                mIntPendingCollVal++;
            }
        }

        set = sharedPreferences.getStringSet(Constants.SecondarySOCreate, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.SecondarySOCreate;
                mIntPendingCollVal++;
            }
        }
        set = sharedPreferences.getStringSet(Constants.FinancialPostings, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.FinancialPostings;
                mIntPendingCollVal++;
            }
        }
        set = sharedPreferences.getStringSet(Constants.SampleDisbursement, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.SampleDisbursement;
                mIntPendingCollVal++;
            }
        }

        set = sharedPreferences.getStringSet(Constants.CPList, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.CPList;
                mIntPendingCollVal++;
            }
        }

        set = sharedPreferences.getStringSet(Constants.ROList, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.ROList;
                mIntPendingCollVal++;
            }
        }

        set = sharedPreferences.getStringSet(Constants.Expenses, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.Expenses;
                mIntPendingCollVal++;
            }
        }

        if (mIntPendingCollVal > 0) {
            Arrays.sort(invKeyValues, new SyncSelectionActivity.ArrayComarator());
            objectsArrayList.add(mIntPendingCollVal);
            objectsArrayList.add(invKeyValues);
        }

        return objectsArrayList;

    }

    public static ArrayList<String> getRefreshList(Context context) {
        ArrayList<String> alAssignColl = new ArrayList<>();
        try {
            if (OfflineManager.getVisitStatusForCustomer(Constants.ChannelPartners + "?$filter= sap.islocal() ")) {
                alAssignColl.add(Constants.ChannelPartners);
            }
            if (OfflineManager.getVisitStatusForCustomer(Constants.CPDMSDivisions + "?$filter= sap.islocal() ")) {
                alAssignColl.add(Constants.CPDMSDivisions);
            }

            if (OfflineManager.getVisitStatusForCustomer(Constants.Attendances + "?$filter= sap.islocal() ")) {
                alAssignColl.add(Constants.Attendances);
            }
            if (OfflineManager.getVisitStatusForCustomer(Constants.Visits + "?$filter= sap.islocal() ")) {
                alAssignColl.add(Constants.Visits);
            }
            if (OfflineManager.getVisitStatusForCustomer(Constants.CompetitorInfos + "?$filter= sap.islocal() ")) {
                alAssignColl.add(Constants.CompetitorInfos);
            }

            if (OfflineManager.getVisitStatusForCustomer(Constants.MerchReviews + "?$filter= sap.islocal() ")) {
                alAssignColl.add(Constants.MerchReviews);
                alAssignColl.add(Constants.MerchReviewImages);
            }

            if (OfflineManager.getVisitStatusForCustomer(Constants.VisitActivities + "?$filter= sap.islocal() ")) {
                alAssignColl.add(Constants.VisitActivities);
            }
            if (OfflineManager.getVisitStatusForCustomer(Constants.SchemeCPDocuments + "?$filter= sap.islocal() ")) {
                alAssignColl.add(Constants.SchemeCPDocuments);
            }
            if (OfflineManager.getVisitStatusForCustomer(Constants.Claims + "?$filter= sap.islocal() ")) {
                alAssignColl.add(Constants.Claims);
            }
            if (OfflineManager.getVisitStatusForCustomer(Constants.ClaimDocuments + "?$filter= sap.islocal() ")) {
                alAssignColl.add(Constants.ClaimDocuments);
            }
            if (OfflineManager.getVisitStatusForCustomer(Constants.ClaimDocuments + "?$filter= sap.islocal() ")) {
                alAssignColl.add(Constants.ClaimDocuments);
            }
            if (OfflineManager.getVisitStatusForCustomer(Constants.CPStockItems + "?$filter= sap.islocal() ")) {
                alAssignColl.addAll(SyncUtils.getRetailerStock());
            }
            if (OfflineManager.getVisitStatusForCustomer(Constants.SchemeCPs + "?$filter= sap.islocal() ")) {
                alAssignColl.add(Constants.SchemeCPs);
            }

            if (OfflineManager.getVisitStatusForCustomer(Constants.Complaints + "?$filter= sap.islocal() ")) {
                alAssignColl.add(Constants.Complaints);
                alAssignColl.add(Constants.ComplaintDocuments);
            }
            if (OfflineManager.getVisitStatusForCustomer(Constants.ExpenseDocuments + "?$filter= sap.islocal() ")) {
                alAssignColl.add(Constants.Expenses);
                alAssignColl.add(Constants.ExpenseItemDetails);
                alAssignColl.add(Constants.ExpenseDocuments);
            }
            if (OfflineManager.getVisitStatusForCustomer(Constants.SyncHistorys + Constants.isLocalFilterQry)) {
                alAssignColl.add(Constants.SyncHistorys);
            }

        } catch (OfflineODataStoreException e) {
            LogManager.writeLogError("Error : " + e.getMessage());
        }
        return alAssignColl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the item_history_time for this fragment
        View view = inflater.inflate(R.layout.fragment_sync_history_info, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
        recycler_view_His = view.findViewById(R.id.recycler_view_His);
        rvSyncTime = view.findViewById(R.id.rvSyncTime);
        ivUploadDownload = view.findViewById(R.id.ivUploadDownload);
        tvPendingStatus = view.findViewById(R.id.tvPendingStatus);
        cvUpdatePending = view.findViewById(R.id.cvUpdatePending);
        tvPendingCount = view.findViewById(R.id.tvPendingCount);
        nestedScroll = view.findViewById(R.id.nestedScroll);
        ivSyncAll = view.findViewById(R.id.ivSyncAll);
        ivUploadDownload.setOnClickListener(SyncHistoryInfoFragment.this);
        ivSyncAll.setOnClickListener(SyncHistoryInfoFragment.this);
        recycler_view_His.setHasFixedSize(false);
        rvSyncTime.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        ViewCompat.setNestedScrollingEnabled(recycler_view_His, false);
        recycler_view_His.setLayoutManager(linearLayoutManager);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        ViewCompat.setNestedScrollingEnabled(rvSyncTime, false);
        rvSyncTime.setLayoutManager(linearLayoutManager);
        pendingCountAdapter = new PendingCountAdapter(pendingCountBeanList, getActivity(), this);
        recycler_view_His.setAdapter(pendingCountAdapter);
        setSyncTimeAdapter();
//        pendingCountBeanList = getRecordInfo();
        initRecyclerView();
        ConstantsUtils.focusOnView(nestedScroll);
        return view;
    }

    private void setSyncTimeAdapter() {

    }

    private void initRecyclerView() {
        pendingCountBeanList.clear();
        pendingCountBeanList.addAll(getRecordInfo(getActivity()));
        pendingCountAdapter.notifyDataSetChanged();

        simpleUpdateHistoryAdapter.refreshAdapter(syncHistoryModelList);
        tvPendingCount.setText(String.valueOf(pendingCount));
        if (pendingCount > 0) {
            cvUpdatePending.setVisibility(View.VISIBLE);
            tvPendingStatus.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.RejectedColor));
        } else {
            cvUpdatePending.setVisibility(View.GONE);
            tvPendingStatus.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ApprovedColor));
        }
    }

    private List<PendingCountBean> getRecordInfo(Context mContext) {
        pendingCount = 0;
        syncHistoryModelList.clear();
        syncHistoryModelList.addAll((new SyncHistoryDB(mContext)).getAllRecord());
        Collections.sort(syncHistoryModelList, new Comparator<SyncHistoryModel>() {
            public int compare(SyncHistoryModel one, SyncHistoryModel other) {
                return one.getCollections().compareTo(other.getCollections());
            }
        });
        PendingCountBean countBean = null;
        int count = 0;
        List<PendingCountBean> pendingCountBeans = new ArrayList();
        List<PendingCountBean> temppendingList = new ArrayList();
        List<PendingCountBean> tempNonpendingList = new ArrayList();
        Set<String> set = new HashSet<>();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        ArrayList<String> alTempList = new ArrayList<>();
        ArrayList<String> alCollectionList = null;

        Collections.sort(tempNonpendingList, new Comparator<PendingCountBean>() {
            public int compare(PendingCountBean one, PendingCountBean other) {
                return one.getCollection().compareTo(other.getCollection());
            }
        });
        Collections.sort(temppendingList, new Comparator<PendingCountBean>() {
            public int compare(PendingCountBean one, PendingCountBean other) {
                return one.getCollection().compareTo(other.getCollection());
            }
        });

        pendingCountBeans.addAll(temppendingList);
        pendingCountBeans.addAll(tempNonpendingList);

        return pendingCountBeans;
    }
    int cpCount = 0;
    int soCount = 0;
	boolean isUpdateSyncing=false;
    public void removeFromDatavault(Context context){
        Set<String> set = new HashSet<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
        set = sharedPreferences.getStringSet(Constants.SecondarySOCreate, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, context);
//                    LogManager.writeLogInfo("Remove SO orders from Datavault");
                    if (store == null) {
                        LogManager.writeLogInfo("Remove SO orders from Datavault key value null");
                        Constants.removeDataValtFromSharedPref(context, Constants.SecondarySOCreate, deviceNo, false);
//                        Constants.removeDataValtFromSharedPref(context, Constants.SecondarySOCreateTemp, deviceNo, false);
                    }else {
                        if (TextUtils.isEmpty(store)) {
                            LogManager.writeLogInfo("Remove SO orders from Datavault key value empty");
                            Constants.removeDataValtFromSharedPref(context, Constants.SecondarySOCreate, deviceNo, false);
//                            Constants.removeDataValtFromSharedPref(context, Constants.SecondarySOCreateTemp, deviceNo, false);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        set = sharedPreferences.getStringSet(Constants.Feedbacks, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, context);
//                    LogManager.writeLogInfo("Remove Feedbacks from Datavault");
                    if (store == null) {
                        LogManager.writeLogInfo("Remove Feedbacks from Datavault key value null");
                        Constants.removeDataValtFromSharedPref(context, Constants.Feedbacks, deviceNo, false);
                    }else {
                        if (TextUtils.isEmpty(store)) {
                            LogManager.writeLogInfo("Remove Feedbacks from Datavault key value empty");
                            Constants.removeDataValtFromSharedPref(context, Constants.Feedbacks, deviceNo, false);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        set = sharedPreferences.getStringSet(Constants.FinancialPostings, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, context);
//                    LogManager.writeLogInfo("Remove Feedbacks from Datavault");
                    if (store == null) {
                        LogManager.writeLogInfo("Remove FinancialPostings from Datavault key value null");
                        Constants.removeDataValtFromSharedPref(context, Constants.FinancialPostings, deviceNo, false);
                    }else {
                        if (TextUtils.isEmpty(store)) {
                            LogManager.writeLogInfo("Remove FinancialPostings from Datavault key value empty");
                            Constants.removeDataValtFromSharedPref(context, Constants.FinancialPostings, deviceNo, false);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        set = sharedPreferences.getStringSet(Constants.SampleDisbursement, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, context);
//                    LogManager.writeLogInfo("Remove Feedbacks from Datavault");
                    if (store == null) {
                        LogManager.writeLogInfo("Remove SampleDisbursement from Datavault key value null");
                        Constants.removeDataValtFromSharedPref(context, Constants.SampleDisbursement, deviceNo, false);
                    }else {
                        if (TextUtils.isEmpty(store)) {
                            LogManager.writeLogInfo("Remove SampleDisbursement from Datavault key value empty");
                            Constants.removeDataValtFromSharedPref(context, Constants.SampleDisbursement, deviceNo, false);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        set = sharedPreferences.getStringSet(Constants.CPList, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, context);
//                    LogManager.writeLogInfo("Remove Feedbacks from Datavault");
                    if (store == null) {
                        LogManager.writeLogInfo("Remove CPList from Datavault key value null");
                        Constants.removeDataValtFromSharedPref(context, Constants.CPList, deviceNo, false);
                    }else {
                        if (TextUtils.isEmpty(store)) {
                            LogManager.writeLogInfo("Remove CPList from Datavault key value empty");
                            Constants.removeDataValtFromSharedPref(context, Constants.CPList, deviceNo, false);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        set = sharedPreferences.getStringSet(Constants.ROList, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, context);
//                    LogManager.writeLogInfo("Remove Feedbacks from Datavault");
                    if (store == null) {
                        LogManager.writeLogInfo("Remove ROList from Datavault key value null");
                        Constants.removeDataValtFromSharedPref(context, Constants.ROList, deviceNo, false);
                    }else {
                        if (TextUtils.isEmpty(store)) {
                            LogManager.writeLogInfo("Remove ROList from Datavault key value empty");
                            Constants.removeDataValtFromSharedPref(context, Constants.ROList, deviceNo, false);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        set = sharedPreferences.getStringSet(Constants.Expenses, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, context);
//                    LogManager.writeLogInfo("Remove Feedbacks from Datavault");
                    if (store == null) {
                        LogManager.writeLogInfo("Remove Expenses from Datavault key value null");
                        Constants.removeDataValtFromSharedPref(context, Constants.Expenses, deviceNo, false);
                    }else {
                        if (TextUtils.isEmpty(store)) {
                            LogManager.writeLogInfo("Remove Expenses from Datavault key value empty");
                            Constants.removeDataValtFromSharedPref(context, Constants.Expenses, deviceNo, false);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void AvailableServerAllSync(boolean isUploadData,boolean isUpload, PendingCountBean countBean, String syncType){
        if (UtilConstants.isNetworkAvailable(getActivity())) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    syncProgDialog = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
                    syncProgDialog.setTitle("Verifying server. Please wait...");
                    if(isUploadData){
                        setTimer("02");
                    }else {
                        setTimer("01");
                    }
                    messageProgess = "Verifying server. Please wait...";
                    syncProgDialog.setMessage(messageProgess);
                    syncProgDialog.setCancelable(false);
                    syncProgDialog.setCanceledOnTouchOutside(false);
                    syncProgDialog.show();
                }
            });

            String hostName = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0).getString(Constants.ActiveHost, "");
            try {
                AvailableServer.clearCookies();
                if (AvailableServer.pingServer(server_Text)) {
                    JSONObject resourceFileReadresponse = AvailableServer.getResourceFile(server_Text,getActivity());
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.ActiveHost, server_Text);
                    editor.apply();
                    server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                    if (resourceFileReadresponse != null) {
                        JSONObject serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                        boolean status = serverResponse.getBoolean(Constants.Status);
                        int responseCode = serverResponse.getInt(Constants.ResponseCode);
                        String messageError = serverResponse.getString(Constants.Message);
                        if (responseCode == 200) {
                            if (status) {
                                if(isUploadData){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            onUpdateSync();
                                        }
                                    });
                                }else {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            download(isUpload,countBean,syncType);
                                        }
                                    });
                                }
                            } else {
                                closingProgressDialog();
                                LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                showAlert(messageError);
                            }
                        } else if (responseCode == 503 || responseCode == 404 || responseCode == 998 || responseCode == 999 || responseCode == 996) {
                            // 503 - service unavailable , 404 - Resource file not maintained , 998 - Backend unavailable , 999 - Resource file is empty
                            //996 - Resource file available but backend property missing
                            if (!server_Text.equalsIgnoreCase(server_Text_default)) {
//                                AvailableServer.clearCookies();
                                if (AvailableServer.pingServer(server_Text_default)) {
//                                    editor.putString(Constants.ActiveHost, server_Text_default);
//                                    editor.apply();
//                                    server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                                    resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_default,getActivity());
                                    if (resourceFileReadresponse != null) {
                                        serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                                        status = serverResponse.getBoolean(Constants.Status);
                                        responseCode = serverResponse.getInt(Constants.ResponseCode);
                                        messageError = serverResponse.getString(Constants.Message);
                                        if (responseCode == 200) {
                                            if (status) {
                                                if(isUploadData){
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            onUpdateSync();
                                                        }
                                                    });
                                                }else {
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            download(isUpload,countBean,syncType);
                                                        }
                                                    });
                                                }
                                            } else {
                                                closingProgressDialog();
                                                LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                                LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                                showAlert(messageError);
                                            }
                                        } else if (responseCode == 401) {
                                            LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                            LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                            try {
                                                sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
                                                String loginUser = sharedPreferences.getString("username", "");
                                                String login_pwd = sharedPreferences.getString("password", "");
                                                Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                                                    @Override
                                                    public void passwordStatus(final JSONObject jsonObject) {
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                closingProgressDialog();
                                                                Constants.passwordStatusErrorMessage(getActivity(), jsonObject, registrationModel, loginUser);
                                                            }
                                                        });

                                                    }
                                                });
                                            } catch (Throwable throwable) {
                                                throwable.printStackTrace();
                                            }
                                        } else {
                                            closingProgressDialog();
                                            LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                            LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                            showAlert(messageError);
                                        }
                                    }
                                } else {
                                    closingProgressDialog();
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    showAlert(messageError);
                                }
                            } else if (!server_Text.equalsIgnoreCase(server_Text_DR)) {
//                                AvailableServer.clearCookies();
                                if (AvailableServer.pingServer(server_Text_DR)) {
//                                    editor.putString(Constants.ActiveHost, server_Text_DR);
//                                    editor.apply();
//                                    server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                                    resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_DR,getActivity());
                                    if (resourceFileReadresponse != null) {
                                        serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                                        status = serverResponse.getBoolean(Constants.Status);
                                        responseCode = serverResponse.getInt(Constants.ResponseCode);
                                        messageError = serverResponse.getString(Constants.Message);
                                        if (responseCode == 200) {
                                            if (status) {
                                                if(isUploadData){
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            onUpdateSync();
                                                        }
                                                    });
                                                }else {
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            download(isUpload,countBean,syncType);
                                                        }
                                                    });
                                                }
                                            } else {
                                                closingProgressDialog();
                                                LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                                LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                                showAlert(messageError);
                                            }
                                        } else if (responseCode == 401) {
                                            LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                            LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                            try {
                                                sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
                                                String loginUser = sharedPreferences.getString("username", "");
                                                String login_pwd = sharedPreferences.getString("password", "");
                                                Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                                                    @Override
                                                    public void passwordStatus(final JSONObject jsonObject) {
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                closingProgressDialog();
                                                                Constants.passwordStatusErrorMessage(getActivity(), jsonObject, registrationModel, loginUser);
                                                            }
                                                        });

                                                    }
                                                });
                                            } catch (Throwable throwable) {
                                                throwable.printStackTrace();
                                            }
                                        } else {
                                            closingProgressDialog();
                                            LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                            LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                            showAlert(messageError);
                                        }
                                    }
                                } else {
                                    closingProgressDialog();
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    showAlert(messageError);
                                }
                            }
                        } else if (responseCode == 401) {
                            LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                            LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                            try {
                                sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
                                String loginUser = sharedPreferences.getString("username", "");
                                String login_pwd = sharedPreferences.getString("password", "");
                                Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                                    @Override
                                    public void passwordStatus(final JSONObject jsonObject) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                closingProgressDialog();
                                                Constants.passwordStatusErrorMessage(getActivity(), jsonObject, registrationModel, loginUser);
                                            }
                                        });

                                    }
                                });
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        } else {
                            closingProgressDialog();
                            LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                            LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                            showAlert(messageError);
                        }
                    }
                } else {
                    //xumu != xumu
                    if (!server_Text.equalsIgnoreCase(server_Text_default)) {
                        AvailableServer.clearCookies();
                        if (AvailableServer.pingServer(server_Text_default)) {
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.ActiveHost, server_Text_default);
                            editor.apply();
                            server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                            JSONObject resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_default,getActivity());
                            if (resourceFileReadresponse != null) {
                                JSONObject serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                                boolean status = serverResponse.getBoolean(Constants.Status);
                                int responseCode = serverResponse.getInt(Constants.ResponseCode);
                                String messageError = serverResponse.getString(Constants.Message);
                                if (responseCode == 200) {
                                    if (status) {
                                        try {
                                            if (syncProgDialog!=null) {
                                                messageProgess = "Registering user. Please wait...";
                                                syncProgDialog.setMessage("Registering user. Please wait...");
                                            }
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                        AvailableServer.registerWithAvailableServer(getActivity(), new AvailableServer.RegisterServer() {
                                            @Override
                                            public void requestSuccess() {getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        messageProgess = getString(R.string.msg_sync_progress_msg_plz_wait);
                                                        syncProgDialog.setMessage(messageProgess);
                                                    } catch (Throwable e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                                AvailableServer.openStoreWithAvailableServer(getActivity(), new UIListener() {
                                                    @Override
                                                    public void onRequestError(int operation, Exception e) {
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                onError(operation, e);
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                onSuccess(operation,key,isUploadData);
                                                            }
                                                        });
                                                    }
                                                });
                                            }

                                            @Override
                                            public void requestError(String errorMessage) {
                                                closingProgressDialog();
                                                showAlert(errorMessage);
                                            }
                                        });
                                    } else {
                                        closingProgressDialog();
                                        LogManager.writeLogInfo("getResouce file check request failed : " + responseCode);
                                        LogManager.writeLogInfo("getResouce file check request failed : " + messageError);LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                        LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                        showAlert(messageError);
                                    }
                                } else if (responseCode == 503 || responseCode == 404 || responseCode == 998 || responseCode == 999 || responseCode == 996) {
                                    // 503 - service unavailable , 404 - Resource file not maintained , 998 - Backend unavailable , 999 - Resource file is empty
                                    //996 - Resource file available but backend property missing
                                    closingProgressDialog();
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    showAlert(messageError);
                                } else if (responseCode == 401) {
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    try {
                                        sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
                                        String loginUser = sharedPreferences.getString("username", "");
                                        String login_pwd = sharedPreferences.getString("password", "");
                                        Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                                            @Override
                                            public void passwordStatus(final JSONObject jsonObject) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        closingProgressDialog();
                                                        Constants.passwordStatusErrorMessage(getActivity(), jsonObject, registrationModel, loginUser);
                                                    }
                                                });

                                            }
                                        });
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                } else {
                                    closingProgressDialog();
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    showAlert(messageError);
                                }
                            }
                        } else {
                            closingProgressDialog();
                            LogManager.writeLogError("Sync can't be performed due to server unavailability. Please try later");
                            showAlert("Sync can't be performed due to server unavailability. Please try later");
                        }
                    }
                    //Xumu != Ap1
                    else if (!server_Text.equalsIgnoreCase(server_Text_DR)) {
                        AvailableServer.clearCookies();
                        server_Text = server_Text_DR;
                        if (AvailableServer.pingServer(server_Text_DR)) {
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.ActiveHost, server_Text_DR);
                            editor.apply();
                            server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                            JSONObject resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_DR,getActivity());
                            if (resourceFileReadresponse != null) {
                                JSONObject serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                                boolean status = serverResponse.getBoolean(Constants.Status);
                                int responseCode = serverResponse.getInt(Constants.ResponseCode);
                                String messageError = serverResponse.getString(Constants.Message);
                                if (responseCode == 200) {
                                    if (status) {
                                        try {
                                            if (syncProgDialog!=null) {
                                                messageProgess = "Registering user. Please wait...";
                                                syncProgDialog.setMessage("Registering user. Please wait...");
                                            }
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                        AvailableServer.registerWithAvailableServer(getActivity(), new AvailableServer.RegisterServer() {
                                            @Override
                                            public void requestSuccess() {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            messageProgess = getString(R.string.msg_sync_progress_msg_plz_wait);
                                                            syncProgDialog.setMessage(messageProgess);
                                                        } catch (Throwable e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                                AvailableServer.openStoreWithAvailableServer(getActivity(), new UIListener() {
                                                    @Override
                                                    public void onRequestError(int operation, Exception e) {
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                onError(operation, e);
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                onSuccess(operation,key,isUploadData);
                                                            }
                                                        });
                                                    }
                                                });
                                            }

                                            @Override
                                            public void requestError(String errorMessage) {
                                                closingProgressDialog();
                                                showAlert(errorMessage);
                                            }
                                        });
                                    } else {
                                        closingProgressDialog();
                                        LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                        LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                        showAlert(messageError);
                                    }
                                } else if (responseCode == 503 || responseCode == 404 || responseCode == 998 || responseCode == 999 || responseCode == 996) {
                                    // 503 - service unavailable , 404 - Resource file not maintained , 998 - Backend unavailable , 999 - Resource file is empty
                                    //996 - Resource file available but backend property missing
                                    closingProgressDialog();
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    showAlert(messageError);
                                } else if (responseCode == 401) {
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    try {
                                        sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
                                        String loginUser = sharedPreferences.getString("username", "");
                                        String login_pwd = sharedPreferences.getString("password", "");
                                        Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                                            @Override
                                            public void passwordStatus(final JSONObject jsonObject) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        closingProgressDialog();
                                                        Constants.passwordStatusErrorMessage(getActivity(), jsonObject, registrationModel, loginUser);
                                                    }
                                                });

                                            }
                                        });
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                } else {
                                    closingProgressDialog();
                                    LogManager.writeLogInfo("All Sync request failed : " + responseCode);
                                    LogManager.writeLogInfo("All Sync request request failed : " + messageError);
                                    showAlert(messageError);
                                }
                            }
                        } else {
                            closingProgressDialog();
                            LogManager.writeLogError("Sync can't be performed due to server unavailability. Please try later");
                            showAlert("Sync can't be performed due to server unavailability. Please try later");
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Constants.showAlert(ErrorCode.NETWORK_ERROR, getActivity());
                }
            });

            isAllSync = false;
        }
    }

    private void onUpdateSync() {
        LogManager.writeLogInfo("Update sync started");
        setEmpty();
        Constants.Entity_Set.clear();
        Constants.AL_ERROR_MSG.clear();
        tempCPList.clear();
        mBoolIsNetWorkNotAval = false;
        isBatchReqs = false;
        mBoolIsReqResAval = true;
        updateCancelSOCount = 0;
        cancelSoPos = 0;
        try {
            removeFromDatavault(getActivity());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Set<String> set = new HashSet<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
        set = sharedPreferences.getStringSet(Constants.SecondarySOCreate, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String store = null, deviceNo = "";
                deviceNo = itr.next().toString();
                try {
                    store = ConstantsUtils.getFromDataVault(deviceNo, getActivity());
                    if (store != null) {
                        if(!TextUtils.isEmpty(store)){
                            JSONObject fetchJsonHeaderObject = new JSONObject(store);
                            if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                                if(!fetchJsonHeaderObject.getString(Constants.Status).equalsIgnoreCase("X")) {
                                    Constants.removeDataValtFromSharedPref(getActivity(), Constants.SecondarySOCreate, deviceNo, false);
                                }

                            }
                        }else {
                            Constants.removeDataValtFromSharedPref(getActivity(), Constants.SecondarySOCreate, deviceNo, false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            LogManager.writeLogInfo("Check Pending Request");
            checkPendingReqIsAval();

            if (OfflineManager.offlineStore.getRequestQueueIsEmpty() && mIntPendingCollVal == 0) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closingProgressDialog();
                        Constants.showAlert(getString(R.string.no_req_to_update_sap), getActivity());
                    }
                });
            } else {
                if (Constants.iSAutoSync /*|| Constants.isBackGroundSync*/) {
                    if (getActivity()!=null) {
                        /*if(Constants.iSAutoSync) {
                            Constants.showAlert(getString(R.string.alert_auto_sync_is_progress), getActivity());
                        }*//*else if(Constants.isBackGroundSync){
                            Constants.showAlert(getString(R.string.alert_backgrounf_sync_is_progress), getActivity());
                        }*/
                    }
                }else {
                    if (mIntPendingCollVal > 0) {
                        LogManager.writeLogInfo("Pending Request(s) : "+mIntPendingCollVal);

                        if (!alAssignColl.contains(Constants.ConfigTypsetTypeValues))
                            alAssignColl.add(Constants.ConfigTypsetTypeValues);

                        if (UtilConstants.isNetworkAvailable(getActivity())) {
                            Constants.isSync = true;
                            refguid = GUID.newRandom();
                            SyncUtils.updatingSyncStartTime(getActivity(),Constants.UpLoad,Constants.StartSync,refguid.toString().toUpperCase());
                            onPostOnlineData();
                        } else {
                            Constants.showAlert(getString(R.string.no_network_conn), getActivity());
                        }
                    } else if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                        if (UtilConstants.isNetworkAvailable(getActivity())) {
                            refguid = GUID.newRandom();
                            SyncUtils.updatingSyncStartTime(getActivity(),Constants.UpLoad,Constants.StartSync,refguid.toString().toUpperCase());
                            onPostOfflineData();
                        } else {
                            Constants.showAlert(getString(R.string.no_network_conn), getActivity());
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void checkPendingReqIsAval() {
        try {
            mIntPendingCollVal = 0;
            invKeyValues = null;
            ArrayList<Object> objectArrayList = getPendingCollList(getActivity(), false);
            if (!objectArrayList.isEmpty()) {
                mIntPendingCollVal = (int) objectArrayList.get(0);
                invKeyValues = (String[][]) objectArrayList.get(1);
            }

            penReqCount = 0;


            alAssignColl.clear();
            alFlushColl.clear();
            concatCollectionStr = "";
            concatFlushCollStr = "";
            ArrayList<String> allAssignColl = getRefreshList(getActivity());
            if (!allAssignColl.isEmpty()) {
                alAssignColl.addAll(allAssignColl);
                alFlushColl.addAll(allAssignColl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setEmpty() {
        /*HashSet companySet = new HashSet();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.NOT_POSTED_RETAILERS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(Constants.duplicateCPList, companySet);
        editor.commit();*/
    }
    PostingDataValutData postingDataValutData;
    private void onPostOnlineData() {
        try {
//            setTimer("02");
            postingDataValutData=  new PostingDataValutData();
            postingDataValutData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(int operation, Exception exception) {
//        isUpdateClicked =false;
//        isAllSyncClicked=false;
        isUpdateSyncing=false;
        ErrorBean errorBean = Constants.getErrorCode(operation, exception, getActivity());

        try {
            if (exception.toString().contains("HTTP Status 401 ? Unauthorized") || exception.toString().contains("invalid authentication")) {
                try {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
                    String loginUser = sharedPreferences.getString("username", "");
                    String login_pwd = sharedPreferences.getString("password", "");
                    Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                        @Override
                        public void passwordStatus(final JSONObject jsonObject) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    closingProgressDialog();
                                    Constants.passwordStatusErrorMessage(getActivity(), jsonObject, registrationModel, loginUser);
                                }
                            });

                        }
                    });
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            } else {
                if (errorBean.hasNoError()) {
                    mError++;
                    penReqCount++;
                    mBoolIsReqResAval = true;
                    if ((operation == Operation.Create.getValue()) && (penReqCount == mIntPendingCollVal)) {
                        postOfflineData();
                    }
                    if (operation == Operation.OfflineFlush.getValue()) {
                        if (UtilConstants.isNetworkAvailable(getActivity())) {
                            if (!alAssignColl.contains(Constants.ConfigTypsetTypeValues))
                                alAssignColl.add(Constants.ConfigTypsetTypeValues);
                            concatCollectionStr = Constants.getConcatinatinFlushCollectios(alAssignColl);
                            LogManager.writeLogInfo(concatCollectionStr + " refresh started");
                            messageProgess = "Refreshing data. Please wait...";
                            new RefreshAsyncTask(getActivity(), concatCollectionStr, this).execute();
                        } else {
                            closingProgressDialog();
                            Constants.showAlert(getString(R.string.data_conn_lost_during_sync), getActivity());
                        }

                    } else if (operation == Operation.OfflineRefresh.getValue()) {
                        LogManager.writeLogError("Error : " + exception.getMessage());
                        Constants.isSync = false;
                        String mErrorMsg = "";
                        if (Constants.AL_ERROR_MSG.size() > 0) {
                            mErrorMsg = Constants.convertALBussinessMsgToString(Constants.AL_ERROR_MSG);
                        }
                        closingProgressDialog();
                        if (mErrorMsg.equalsIgnoreCase("")) {
                            Constants.showAlert(errorBean.getErrorMsg(), getActivity());
                        } else {
                            Constants.customAlertDialogWithScroll(getActivity(), mErrorMsg);
                        }

                    } else if (operation == Operation.GetStoreOpen.getValue()) {
                        closingProgressDialog();
                        Constants.showAlert(getString(R.string.msg_offline_store_failure),
                                getActivity());
                    }
                } else {
                    mBoolIsReqResAval = true;
                    mBoolIsNetWorkNotAval = true;
                    Constants.isSync = false;
                    if (errorBean.isStoreFailed()) {
                        OfflineManager.offlineStore = null;
                        OfflineManager.options = null;
                        try {
                            if (!OfflineManager.isOfflineStoreOpen()) {
                                closingProgressDialog();
                                try {
                                    new OpenOfflineStore().execute();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                closingProgressDialog();
                                Constants.displayMsgReqError(errorBean.getErrorCode(), getActivity());
                            }
                        } catch (Exception e) {
                            closingProgressDialog();
                            Constants.displayMsgReqError(errorBean.getErrorCode(), getActivity());
                            e.printStackTrace();
                        }
                    } else {
                        closingProgressDialog();
                        Constants.displayMsgReqError(errorBean.getErrorCode(), getActivity());
                    }


                }
            }
        } catch (Exception e) {
            mBoolIsReqResAval = true;
            mBoolIsNetWorkNotAval = true;
            Constants.isSync = false;
            closingProgressDialog();
            Constants.displayMsgReqError(errorBean.getErrorCode(), getActivity());
        }

    }

    @Override
    public void onRequestSuccess(int operation, String key) {
//        isUpdateClicked =false;
//        isAllSyncClicked=false;
        isUpdateSyncing=false;
        try {
            if (operation == Operation.Create.getValue() && mIntPendingCollVal > 0) {
                mBoolIsReqResAval = true;
                Constants.removeDataValtFromSharedPref(getActivity(), invKeyValues[penReqCount][1], invKeyValues[penReqCount][0], false);
                try {
                    if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.SecondarySOCreate)) {
                        JSONObject jsonObject1 = null;
                        String orderNo="";
                        try {
                            jsonObject1 = Constants.getJSONBody(key);
                            orderNo=jsonObject1.getString("OrderNo");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        LogManager.writeLogInfo("Sale Order No - "+orderNo+" posted successfull");
                        Constants.saveDeviceDocNoToSharedPref(getActivity(), Constants.SecondarySOCreate, invKeyValues[penReqCount][0]);
                        fetchJsonHeaderObject.put(Constants.Status,"000000");
                        fetchJsonHeaderObject.put(Constants.OrderNo,orderNo);
                        ConstantsUtils.storeInDataVault(invKeyValues[penReqCount][0], fetchJsonHeaderObject.toString(), getActivity());
                        LogManager.writeLogInfo("Device order no. "+invKeyValues[penReqCount][0]+" updated with sale Order no. "+orderNo+" and status updated  after post ");
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                /*
                 if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.FinancialPostings)) {
                    Constants.removeDeviceDocNoFromSharedPref(getActivity(), Constants.FinancialPostings, invKeyValues[penReqCount][0]);
                }
                if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.CollList)) {
                    Constants.removeDeviceDocNoFromSharedPref(getActivity(), Constants.CollList, invKeyValues[penReqCount][0]);
                } else */
               /* if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.SecondarySOCreate)) {
                    Constants.removeDeviceDocNoFromSharedPref(getActivity(), Constants.SecondarySOCreate, invKeyValues[penReqCount][0]);
                } else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.Feedbacks)) {
                    Constants.removeDeviceDocNoFromSharedPref(getActivity(), Constants.Feedbacks, invKeyValues[penReqCount][0]);
                }*/ /*else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.InvList)) {
                    Constants.removeDeviceDocNoFromSharedPref(getActivity(), Constants.InvList, invKeyValues[penReqCount][0]);
                } else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.ROList)) {
                    Constants.removeDeviceDocNoFromSharedPref(getActivity(), Constants.ROList, invKeyValues[penReqCount][0]);
                } else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.SampleDisbursement)) {
                    Constants.removeDeviceDocNoFromSharedPref(getActivity(), Constants.SampleDisbursement, invKeyValues[penReqCount][0]);
                }else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.Expenses)) {
                    Constants.removeDeviceDocNoFromSharedPref(getActivity(), Constants.Expenses, invKeyValues[penReqCount][0]);
                }else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.CPList)) {
                    Constants.removeDeviceDocNoFromSharedPref(getActivity(), Constants.CPList, invKeyValues[penReqCount][0]);
                }*/
//                ConstantsUtils.storeInDataVault(invKeyValues[penReqCount][0], "", getActivity());
                try {
                    SyncHistoryDB.deleteInvoiceDocID(getActivity(), invKeyValues[penReqCount][0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                penReqCount++;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if ((operation == Operation.Create.getValue()) && (penReqCount == mIntPendingCollVal)) {
            postOfflineData();
        } else if (operation == Operation.OfflineFlush.getValue()) {
            if (UtilConstants.isNetworkAvailable(getActivity())) {
                if (!alAssignColl.contains(Constants.ConfigTypsetTypeValues))
                    alAssignColl.add(Constants.ConfigTypsetTypeValues);
                concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(alAssignColl);
                messageProgess = "Refreshing data. Please wait...";
                new RefreshAsyncTask(getActivity(), concatCollectionStr, this).execute();
            } else {
                closingProgressDialog();
                Constants.showAlert(getString(R.string.data_conn_lost_during_sync), getActivity());
            }


        } else if (operation == Operation.OfflineRefresh.getValue()) {
//            try {
//                LogManager.writeLogDebug("Sync History View DB Export");
//                SyncSelectionActivity.exportDB(getActivity());
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//            setUI();
            try {
                Constants.getTodayDashboardAch(getActivity());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });

        } else if (operation == Operation.GetStoreOpen.getValue() && OfflineManager.isOfflineStoreOpen()) {
//                Constants.ReIntilizeStore =false;
            Constants.isSync = false;
            try {
                OfflineManager.getAuthorizations(getActivity());
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            Constants.setSyncTime(getActivity());
            setStoreOpenUI();
        }
    }

    public void getSchemList(){

    }


    private void setStoreOpenUI() {
        closingProgressDialog();
        Constants.showAlert(getString(R.string.msg_offline_store_success),
                getActivity());
    }

    @Override
    public void onClick(View v) {
       /* switch (v.getId()) {
            case R.id.ivUploadDownload:
                if (UtilConstants.isNetworkAvailable(getActivity())) {
                    syncType = Constants.UpLoad;
//                    onUpdateSync();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AvailableServerAllSync(true,false,new PendingCountBean(),syncType);
                        }
                    }).start();

                }else{
                    Constants.showAlert(getString(R.string.no_network_conn),getContext());
                }
                break;
            case R.id.ivSyncAll:
                onAllSync();
                break;
        }*/
    }

    private void onBackPressed() {
        getActivity().finish();
    }

    private void closingProgressDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    isUpdateSyncing=false;
                    if (syncProgDialog!=null) {
                        syncProgDialog.dismiss();
                        syncProgDialog = null;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                initRecyclerView();
            }
        });

        /*FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();*/
    }

    private void readValuesFromDataVault() {
        if (mIntPendingCollVal > 0) {
            for (int k = 0; k < invKeyValues.length; k++) {

                while (!mBoolIsReqResAval) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (mBoolIsNetWorkNotAval) {
                    break;
                }
                mBoolIsReqResAval = false;
                String value= k+1+"/"+invKeyValues.length;
                if(MSFAApplication.isVAN){
                    if(invKeyValues.length>1){
                        messageProgess = "Uploading Bills " + (value) + ". Please wait... ";
                    }else {
                        messageProgess = "Uploading Bill " + (value) + ". Please wait... ";
                    }
                }else {
                    if (cpCount == 0 && soCount != 0) {
                        messageProgess = "Uploading SO " + (value) + ". Please wait... ";
                    } else if (cpCount != 0 && soCount == 0) {
                        messageProgess = "Uploading Retailer " + (value) + ". Please wait... ";
                    } else if (cpCount != 0 && soCount != 0) {
                        messageProgess = "Uploading SO and Retailer " + (value) + ". Please wait... ";
                    } else {
                        messageProgess = "Uploading SO " + (value) + ". Please wait... ";
                    }
                }

                String store = ConstantsUtils.getFromDataVault(invKeyValues[k][0].toString(),getActivity());
                //Fetch object from data vault
                try {

                    JSONObject fetchJsonHeaderObject = new JSONObject(store);
                    this.fetchJsonHeaderObject=fetchJsonHeaderObject;
                    try {
                        if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                            if (fetchJsonHeaderObject.getString(Constants.Status).equalsIgnoreCase("000000")) {
                                mBoolIsReqResAval =true;
                                penReqCount++;
                                if (!alAssignColl.contains(Constants.SSSOs)) {
                                    alAssignColl.add(Constants.SSSoItemDetails);
                                    alAssignColl.add(Constants.SSSOs);
                                }
                                if ((penReqCount == mIntPendingCollVal)) {
                                    postOfflineData();
                                }
                                continue;
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    dbHeadTable = new Hashtable();
                    arrtable = new ArrayList<>();

                    if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.Feedbacks)) {
                        // preparing entity pending
                        if (!alAssignColl.contains(Constants.Feedbacks)) {
                            alAssignColl.add(Constants.Feedbacks);
                            alAssignColl.add(Constants.FeedbackItemDetails);
                        }
                        /*dbHeadTable = Constants.getFeedbackHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                        String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);
                        arrtable = UtilConstants.convertToArrayListMap(itemsString);
                        try {
                            OnlineManager.createFeedBack(dbHeadTable, arrtable, getActivity());
                        } catch (OnlineODataStoreException e) {
                            e.printStackTrace();
                        }*/
                        JSONObject headerObject = Constants.getFeedbackHeaderValuesFromJsonObject(fetchJsonHeaderObject);
//                        OnlineManager.createEntity(headerObject.toString(), Constants.Feedbacks, this, getActivity());
                    } else if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                        if (!alAssignColl.contains(Constants.SSINVOICES)) {
//                            alAssignColl.add(Constants.SSInvoiceItemDetails);
                            alAssignColl.add(Constants.SSINVOICES);
                        }
                        if (!alAssignColl.contains(Constants.SSSOs)) {
                            alAssignColl.add(Constants.SSSoItemDetails);
                            alAssignColl.add(Constants.SSSOs);
                        }
//                        dbHeadTable = Constants.getSOHeaderValuesFromJsonObject(fetchJsonHeaderObject);
//                        String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);
//                        arrtable = UtilConstants.convertToArrayListMap(itemsString);
//                        OnlineManager.createSOEntity(dbHeadTable, arrtable, getActivity(), getActivity());
//                        dbHeadTable = Constants.getSOHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                        JSONObject headerObject = Constants.getSOHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                        LogManager.writeLogInfo("Starting SO Post "+k);
                        OnlineManager.createEntity(REPEATABLE_REQUEST_ID,headerObject.toString(), Constants.SSSOs,this, getActivity());
                        LogManager.writeLogInfo("After SO Post "+k);
                    }else if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.MaterialDocs)) {
                        if (!alAssignColl.contains(Constants.MaterialDocs)) {
                            alAssignColl.add(Constants.MaterialDocItemDetails);
                            alAssignColl.add(Constants.MaterialDocItems);
                            alAssignColl.add(Constants.SSPurchaseInvoiceItems);
                            alAssignColl.add(Constants.SSPurchaseInvoices);
                            alAssignColl.add(Constants.CPStockItems);
                            alAssignColl.add(Constants.CPStockItemSnos);
//                            alAssignColl.add(Constants.MaterialDocItemCatSplits);
                            alAssignColl.add(Constants.MaterialDocs);
                        }
//                        dbHeadTable = Constants.getSOHeaderValuesFromJsonObject(fetchJsonHeaderObject);
//                        String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);
//                        arrtable = UtilConstants.convertToArrayListMap(itemsString);
//                        OnlineManager.createSOEntity(dbHeadTable, arrtable, getActivity(), getActivity());
//                        dbHeadTable = Constants.getSOHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                        JSONObject headerObject = Constants.getMaterialDocsHeaderValuesFromJsonObject(fetchJsonHeaderObject);
//                        OnlineManager.createEntity(headerObject.toString(), Constants.MaterialDocs,this, getActivity());
                    }
                    else if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.FinancialPostings)) {

                        if (!alAssignColl.contains(Constants.SSINVOICES)) {
                            alAssignColl.add(Constants.SSInvoiceItemDetails);
                            alAssignColl.add(Constants.SSINVOICES);
                        }
                        if (!alAssignColl.contains(Constants.FinancialPostings)) {
                            alAssignColl.add(Constants.FinancialPostings);
                            alAssignColl.add(Constants.FinancialPostingItemDetails);
                        }
                        if (!alAssignColl.contains(Constants.SSOutstandingInvoices)) {
                            alAssignColl.add(Constants.SSOutstandingInvoiceItemDetails);
                            alAssignColl.add(Constants.SSOutstandingInvoices);
                        }
                       /* dbHeadTable = Constants.getCollHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                        String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);

                        arrtable = UtilConstants.convertToArrayListMap(itemsString);

                        try {
                            OnlineManager.createCollectionEntry(dbHeadTable, arrtable, getActivity());

                        } catch (OnlineODataStoreException e) {
                            e.printStackTrace();
//                        }*///UtilConstants.convertDateFormat(hashtable.get(Constants.FIPDate))
                        JSONObject headerObject = Constants.getCollHeaderValuesFromJsonObject(fetchJsonHeaderObject);
//                        OnlineManager.createEntity(headerObject.toString(), Constants.FinancialPostings, this, getActivity());

                    }else if(fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SampleDisbursement)){
                        if (!alAssignColl.contains(Constants.SSINVOICES)) {
                            alAssignColl.add(Constants.SSInvoiceItemDetails);
                            alAssignColl.add(Constants.SSINVOICES);
                            alAssignColl.add(Constants.SPStockItemSNos);
                            alAssignColl.add(Constants.SPStockItems);
                        }
                        /*dbHeadTable = Constants.getSSInvoiceHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                        String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);
                        arrtable = UtilConstants.convertToArrayListMap(itemsString);
                        try {
                            OnlineManager.createSSInvoiceEntity(dbHeadTable, arrtable, getActivity());
                        } catch (OnlineODataStoreException e) {
                            e.printStackTrace();
                        }*/
                        JSONObject headerObject = Constants.getSSInvoiceHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                        OnlineManager.createEntity(REPEATABLE_REQUEST_ID,headerObject.toString(), Constants.SSINVOICES, this, getActivity());
                    }else if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.ChannelPartners)) {
                        // preparing entity pending
                        if (!alAssignColl.contains(Constants.ChannelPartners)) {
                            alAssignColl.add(Constants.ChannelPartners);
                            alAssignColl.add(Constants.CPDMSDivisions);
//                            alAssignColl.add(Constants.CPDocuments);
                        }


                       /* dbHeadTable = Constants.getCPHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                        String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);
                        arrtable = UtilConstants.convertToArrayListMap(itemsString);
                        try {
                            OnlineManager.createCP(dbHeadTable, arrtable, getActivity());
                        } catch (OnlineODataStoreException e) {
                            e.printStackTrace();
                        }*/
                        JSONObject headerObject = Constants.getCPHeaderValuesFromJsonObject(fetchJsonHeaderObject,getActivity());
                        OnlineManager.createEntity(REPEATABLE_REQUEST_ID,headerObject.toString(), Constants.ChannelPartners, this, getActivity());
                    } else if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.ReturnOrderCreate)) {

                        // preparing entity pending
                        if (!alAssignColl.contains(Constants.SSROs)) {
                            alAssignColl.add(Constants.SSROs);
//                            alAssignColl.add(Constants.SSSoItemDetails);
                        }
                        JSONObject headerObject = Constants.getROHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                        OnlineManager.createEntity(REPEATABLE_REQUEST_ID, headerObject.toString(), Constants.SSROs, this, getActivity());
                    } else if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.Expenses)) {
                        if (!alAssignColl.contains(Constants.Expenses)) {
                            alAssignColl.add(Constants.ExpenseItemDetails);
                            alAssignColl.add(Constants.Expenses);
                        }
                       /* dbHeadTable = Constants.getExpenseHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                        String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);
                        arrtable = UtilConstants.convertToArrayListMap(itemsString);
                        try {
                            OnlineManager.createDailyExpense(dbHeadTable, arrtable, getActivity());
                        } catch (OnlineODataStoreException e) {
                            e.printStackTrace();
                        }*/
                        JSONObject headerObject = Constants.getExpenseHeaderValuesFromJsonObject(fetchJsonHeaderObject);
//                        OnlineManager.createEntity(headerObject.toString(), Constants.Expenses, this, getActivity());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    mBoolIsReqResAval = true;
                    getActivity().runOnUiThread(this::closingProgressDialog);
                    getActivity().runOnUiThread(() -> {
                        showAlert(e.getMessage());
                    });
                }

            }
        }
    }

    private void onPostOfflineData() {
        Constants.isSync = true;
        try {
            new AsyncPostOfflineData().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayProgressDialog() {
        try {
            syncProgDialog
                    .show();
            syncProgDialog
                    .setCancelable(true);
            syncProgDialog
                    .setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialogCancelled = false;
    }
    RefreshAsyncTask refreshAsyncTask;
    @Override
    public void onUploadDownload(boolean isUpload, PendingCountBean countBean, String syncType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AvailableServerAllSync(false,isUpload,countBean,syncType);
            }
        }).start();

    }

    public void download(boolean isUpload, PendingCountBean countBean, String syncType){
        this.syncType = "";
        alAssignColl.clear();
        Constants.AL_ERROR_MSG.clear();
        Constants.Entity_Set.clear();
        Constants.isSync = true;
        dialogCancelled = false;
        this.syncType = syncType;
        Constants.isStoreClosed = false;
        mError = 0;
        if (UtilConstants.isNetworkAvailable(getActivity())) {
            if (Constants.iSAutoSync) {
                showAlert(getString(R.string.alert_auto_sync_is_progress));
            }else{
                alAssignColl.addAll(countBean.getAlCollectionList());
                if (!alAssignColl.contains(Constants.ConfigTypsetTypeValues))
                    alAssignColl.add(Constants.ConfigTypsetTypeValues);
                concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(alAssignColl);
//                setTimer("01");
//                syncProgDialog = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
                syncProgDialog.setTitle("Loading..");
                messageProgess="Refreshing "+countBean.getCollection()+", Please wait..";
                syncProgDialog.setMessage(getString(R.string.app_loading));
                syncProgDialog.setCancelable(false);
//                syncProgDialog.show();
                refguid = GUID.newRandom();
                SyncUtils.updatingSyncStartTime(getActivity(),Constants.DownLoad,Constants.StartSync,refguid.toString().toUpperCase());
                refreshAsyncTask =new RefreshAsyncTask(getActivity(), concatCollectionStr, this);
                refreshAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } else {
            showAlert(getString(R.string.data_conn_lost_during_sync));
        }
    }


    private void assignCollToArrayList() {
        alAssignColl.clear();
        concatCollectionStr = "";
        alAssignColl.addAll(SyncUtils.getAllSyncValue(getActivity()));
        concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(alAssignColl);
    }

    private void onAllSync() {
        if (UtilConstants.isNetworkAvailable(getActivity())) {
            onSyncAll();
        } else {
            Constants.showAlert(getActivity().getString(R.string.no_network_conn), getActivity());
        }
    }

    private void onSyncAll() {
        try {
            Constants.AL_ERROR_MSG.clear();
            Constants.Entity_Set.clear();
            Constants.isSync = true;
            dialogCancelled = false;
            Constants.isStoreClosed = false;
            mError = 0;
            new LoadingData().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class LoadingData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            syncProgDialog = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
            syncProgDialog.setTitle("Loading..");
            syncProgDialog.setMessage(getString(R.string.msg_sync_progress_msg_plz_wait));
            syncProgDialog.setCancelable(true);
            syncProgDialog.setCanceledOnTouchOutside(false);
//            syncProgDialog.show();

            syncProgDialog
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface Dialog) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    getActivity(), R.style.MyTheme);
                            builder.setMessage(R.string.do_want_cancel_sync)
                                    .setCancelable(false)
                                    .setPositiveButton(
                                            R.string.yes,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface Dialog,
                                                        int id) {
                                                    dialogCancelled = true;
                                                    onBackPressed();
                                                }
                                            })
                                    .setNegativeButton(
                                            R.string.no,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface Dialog,
                                                        int id) {

                                                    displayProgressDialog();

                                                }
                                            });
                            builder.show();
                        }
                    });
        }

        @Override
        protected Void doInBackground(Void... params) {
//            Constants.printLogInfo("check store is opened or not");
            if (!OfflineManager.isOfflineStoreOpen()) {
//                Constants.printLogInfo("check store is failed");
                try {
//                    OfflineManager.openOfflineStore(getActivity(), SyncHistoryInfoFragment.this);
                    OfflineManager.openOfflineStoreInternal(getActivity(), SyncHistoryInfoFragment.this);
                } catch (Throwable e) {
                    LogManager.writeLogError(Constants.error_txt + e.getMessage());
                }
            } else {
                Constants.isStoreClosed = false;
                assignCollToArrayList();
//                Constants.printLogInfo("check store is opened");
                try {
                    OfflineManager.refreshStoreSync(getActivity().getApplicationContext(), SyncHistoryInfoFragment.this, Constants.ALL, "");
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    public class PostingDataValutData extends AsyncTask<Void, Boolean, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            if (syncProgDialog == null) {
//            syncProgDialog = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
            syncProgDialog.setTitle("Loading..");
            if(MSFAApplication.isVAN){
                messageProgess = "Uploading Bill. Please wait... ";
            }else {
                if (cpCount == 0 && soCount != 0) {
                    messageProgess = "Uploading SO. Please wait... ";
                } else if (cpCount != 0 && soCount == 0) {
                    messageProgess = "Uploading Retailer. Please wait... ";
                } else if (cpCount != 0 && soCount != 0) {
                    messageProgess = "Uploading SO and Retailer. Please wait... ";
                } else {
                    messageProgess = "Uploading SO. Please wait... ";
                }
            }
            syncProgDialog.setMessage(messageProgess);
            syncProgDialog.setCancelable(false);
            syncProgDialog.setCanceledOnTouchOutside(false);
//            syncProgDialog.show();
           /* }else{
                syncProgDialog.setMessage(getString(R.string.updating_data_plz_wait));
            }*/
            syncProgDialog
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface Dialog) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    getActivity(), R.style.MyTheme);
                            builder.setMessage(R.string.do_want_cancel_sync)
                                    .setCancelable(false)
                                    .setPositiveButton(
                                            R.string.yes,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface Dialog,
                                                        int id) {
                                                    dialogCancelled = true;
                                                    onBackPressed();
                                                }
                                            })
                                    .setNegativeButton(
                                            R.string.no,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface Dialog,
                                                        int id) {

                                                    try {
                                                        syncProgDialog
                                                                .show();
                                                        syncProgDialog
                                                                .setCancelable(true);
                                                        syncProgDialog
                                                                .setCanceledOnTouchOutside(false);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    dialogCancelled = false;

                                                }
                                            });
                            builder.show();
                        }
                    });
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(1000);

               /* try {
                    // get Application Connection ID
                    LogonCoreContext lgCtx = LogonCore.getInstance().getLogonContext();
                    endPointURL = lgCtx.getAppEndPointUrl();
                    appConnID = LogonCore.getInstance().getLogonContext()
                            .getConnId();
                } catch (LogonCoreException e) {
                    LogManager.writeLogError("PostingDataValutData ", e);
                }*/

                tokenFlag = false;

                Constants.x_csrf_token = "";
                Constants.ErrorCode = 0;
                Constants.ErrorNo = 0;
                Constants.ErrorName = "";
                Constants.IsOnlineStoreFailed = false;
                Constants.IsOnlineStoreStarted =false;
                mBoolIsReqResAval = true;
                LogManager.writeLogInfo("Posting data to backend started");
//                try {
                onlineStoreOpen = true;//OnlineManager.openOnlineStore(getActivity());
//                } catch (OnlineODataStoreException e) {
//                    e.printStackTrace();
//                    Constants.printLog("Get online store ended with error(1) " + e.getMessage());
//                }
                if (onlineStoreOpen) {
                    try {
                        readValuesFromDataVault();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    return onlineStoreOpen;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return onlineStoreOpen;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {
                closingProgressDialog();
                syncProgDialog = null;

                if (!onlineStoreOpen) {
                    if (Constants.ErrorNo == Constants.Network_Error_Code && Constants.ErrorName.equalsIgnoreCase(Constants.NetworkError_Name)) {
                        Constants.showAlert(getString(R.string.data_conn_lost_during_sync_error_code, Constants.ErrorNo + ""), getActivity());

                    } else if (Constants.ErrorNo == Constants.UnAuthorized_Error_Code && Constants.ErrorName.equalsIgnoreCase(Constants.NetworkError_Name)) {
                        Constants.showAlert(getString(R.string.auth_fail_plz_contact_admin, Constants.ErrorNo + ""), getActivity());
                    } else if (Constants.ErrorNo == Constants.Comm_Error_Code) {
                        Constants.showAlert(getString(R.string.data_conn_lost_during_sync_error_code, Constants.ErrorNo + ""), getActivity());
                    } else {
                        Constants.showAlert(getString(R.string.data_conn_lost_during_sync_error_code, Constants.ErrorNo + ""), getActivity());
                    }
                } else if (!tokenFlag) {
                    Constants.displayMsgINet(Constants.ErrorNo_Get_Token, getActivity());
                } else if (Constants.x_csrf_token == null || Constants.x_csrf_token.equalsIgnoreCase("")) {
                    Constants.showAlert(getString(R.string.data_conn_lost_during_sync_error_code, -2 + ""), getActivity());
                }
            }
        }
    }

    public class AsyncPostOfflineData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (syncProgDialog == null) {
                syncProgDialog = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
                syncProgDialog.setTitle("Loading..");
            }
            if (countDownTimer==null) {
                setTimer("01");
            }
            messageProgess = "Uploading Retailer, Sync, Visit. Please wait...";
            syncProgDialog.setMessage(messageProgess);
            syncProgDialog.setCancelable(false);
            syncProgDialog.setCanceledOnTouchOutside(false);
//            syncProgDialog.show();
            syncProgDialog
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface Dialog) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    getActivity(), R.style.MyTheme);
                            builder.setMessage(R.string.do_want_cancel_sync)
                                    .setCancelable(false)
                                    .setPositiveButton(
                                            R.string.yes,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface Dialog,
                                                        int id) {
                                                    dialogCancelled = true;
                                                    onBackPressed();
                                                }
                                            })
                                    .setNegativeButton(
                                            R.string.no,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface Dialog,
                                                        int id) {

                                                    displayProgressDialog();

                                                }
                                            });
                            builder.show();
                        }
                    });
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
                concatFlushCollStr = UtilConstants.getConcatinatinFlushCollectios(alFlushColl);
                try {
                    if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                        try {
                            dialogCancelled = false;
                            OfflineManager.flushQueuedRequests(SyncHistoryInfoFragment.this, concatFlushCollStr);
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (ODataException e) {
                    e.printStackTrace();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    /*private List<SyncHistoryModel> getRecordInfo() {
        List<SyncHistoryModel> syncHistoryModelList = (new SyncHistoryDB(this.getActivity())).getAllRecord();
        List<SyncHistoryModel> duplicateSyncHistoryModelList = new ArrayList();
        ArrayList<String> alEntity = new ArrayList<>();
        for (int k=0; k<syncHistoryModelList.size(); k++){
            SyncHistoryModel historyModel = syncHistoryModelList.get(k);

            if((historyModel.getCollections().equalsIgnoreCase("RoutePlans") || historyModel.getCollections().equalsIgnoreCase("RouteSchedulePlans") || historyModel.getCollections().equalsIgnoreCase("RouteSchedules")) && !alEntity.contains("Beat")){
                SyncHistoryModel model = new SyncHistoryModel();
                model.setCollections("Beat");
                model.setTimeStamp(historyModel.getTimeStamp());
                duplicateSyncHistoryModelList.add(model);
                alEntity.add("Beat");
            }else if((historyModel.getCollections().equalsIgnoreCase("ChannelPartners") || historyModel.getCollections().equalsIgnoreCase("CPDMSDivisons") ) && !alEntity.contains("Retailers")){
                SyncHistoryModel model = new SyncHistoryModel();
                model.setCollections("Retailers");
                model.setTimeStamp(historyModel.getTimeStamp());
                duplicateSyncHistoryModelList.add(model);
                alEntity.add("Retailers");

            }else if((historyModel.getCollections().equalsIgnoreCase("SSSOs")  || historyModel.getCollections().equalsIgnoreCase("SSSOItemDetails")) && !alEntity.contains("Sales Order")){
                SyncHistoryModel model = new SyncHistoryModel();
                model.setCollections("Sales Order");
                model.setTimeStamp(historyModel.getTimeStamp());
                duplicateSyncHistoryModelList.add(model);
                alEntity.add("Sales Order");
            }else if((historyModel.getCollections().equalsIgnoreCase("FinancialPostings") || historyModel.getCollections().equalsIgnoreCase("FinancialPostingItemDetails")) && !alEntity.contains("Collections")){
                SyncHistoryModel model = new SyncHistoryModel();
                model.setCollections("Collections");
                model.setTimeStamp(historyModel.getTimeStamp());
                duplicateSyncHistoryModelList.add(model);
                alEntity.add("Collections");
            }else if((historyModel.getCollections().equalsIgnoreCase("Visits") || historyModel.getCollections().equalsIgnoreCase("VisitActivities")) && !alEntity.contains("Visits")){
                SyncHistoryModel model = new SyncHistoryModel();
                model.setCollections("Visits");
                model.setTimeStamp(historyModel.getTimeStamp());
                duplicateSyncHistoryModelList.add(model);
                alEntity.add("Visits");
            }else if((historyModel.getCollections().equalsIgnoreCase("Attendances")) && !alEntity.contains("Attendances")){
                SyncHistoryModel model = new SyncHistoryModel();
                model.setCollections("Attendances");
                model.setTimeStamp(historyModel.getTimeStamp());
                duplicateSyncHistoryModelList.add(model);
                alEntity.add("Attendances");
            }
        }

        Collections.sort(duplicateSyncHistoryModelList, new Comparator<SyncHistoryModel>() {
            @Override
            public int compare(SyncHistoryModel historyModel, SyncHistoryModel historyMode2) {
                return historyModel.getCollections().compareTo(historyMode2.getCollections());
            }
        } );
        return duplicateSyncHistoryModelList;
    }*/
    private class OpenOfflineStore extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            syncProgDialog = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
            syncProgDialog.setTitle("Loading..");
            syncProgDialog.setMessage(getString(R.string.app_loading));
            syncProgDialog.setCancelable(false);
            syncProgDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
                try {
                    if (!OfflineManager.isOfflineStoreOpen()) {
                        try {
//                            OfflineManager.openOfflineStore(getActivity(), SyncHistoryInfoFragment.this);
                            OfflineManager.openOfflineStoreInternal(getActivity(), SyncHistoryInfoFragment.this);
                        } catch (OfflineODataStoreException e) {
                            LogManager.writeLogError(Constants.error_txt + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (InterruptedException e) {
                LogManager.writeLogError(Constants.error_txt + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private void showAlert(String message){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ConstantsUtils.showAlert(message, getContext(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
            }
        });

    }

    private void postOfflineData(){
        try {
            if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                if (UtilConstants.isNetworkAvailable(getActivity())) {
                    try {
                        messageProgess = "Uploading Retailer, Sync, Visit. Please wait...";
                        new AsyncPostOfflineData().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    closingProgressDialog();
                    Constants.showAlert(getString(R.string.data_conn_lost_during_sync), getActivity());
                }
            } else {
                if (UtilConstants.isNetworkAvailable(getActivity())) {
                    if (!alAssignColl.contains(Constants.ConfigTypsetTypeValues))
                        alAssignColl.add(Constants.ConfigTypsetTypeValues);
                    messageProgess = "Refreshing data. Please wait...";
                    concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(alAssignColl);
                    new RefreshAsyncTask(getActivity(), concatCollectionStr, this).execute();
                } else {
                    closingProgressDialog();
                    Constants.showAlert(getString(R.string.data_conn_lost_during_sync), getActivity());
                }
            }

        } catch (ODataException e) {
            e.printStackTrace();
        }
    }
    String countdown = "";
    long second = 0;
    String messageProgess = "";
    long milliSec = 2400000;
    CountDownTimer countDownTimer = null;
    String synctype = "";
    private void setTimer(String syncHistoryType) {
        countdown = "";
        synctype = "";
        synctype = syncHistoryType;
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
                String timeStamp = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm aa", new Date());
                if (syncProgDialog != null && syncProgDialog.isShowing()) {
                    syncProgDialog.setTitle(timeStamp);
                    syncProgDialog.setMessage(messageProgess + "\t\t" + countdown);
                    if (synctype.equalsIgnoreCase("01")) {
                        if (String.format("%02d", minutes).equalsIgnoreCase("10")) {
                            syncProgDialog.dismiss();
                            if (refreshAsyncTask != null)
                                refreshAsyncTask.cancel(true);
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    getContext(), R.style.MyTheme);
                            builder.setMessage(R.string.error_sync_msg)
                                    .setCancelable(false)
                                    .setPositiveButton(
                                            R.string.ok,
                                            (Dialog1, id) -> {
                                                dialogCancelled = true;

                                                onBackPressed();
                                                if (countDownTimer != null) {
                                                    LogManager.writeLogInfo("Login Timer count Down is cancel");
                                                    countDownTimer.cancel();
                                                    countDownTimer = null;
                                                }
                                            });
                            builder.show();
                        }
                    }
                    else if (synctype.equalsIgnoreCase("02") && isUpdateSyncing) {
                        if (minutes >= 2) {
                            syncProgDialog.dismiss();
                            if (postingDataValutData != null)
                                postingDataValutData.cancel(true);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyTheme);
                            builder.setMessage(R.string.error_sync_upload_msg)
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.ok, (Dialog1, id) -> {dialogCancelled = true;onBackPressed();
                                                if (countDownTimer != null) {
                                                    LogManager.writeLogInfo("Login Timer count Down is cancel");
                                                    countDownTimer.cancel();
                                                    countDownTimer = null;
                                                }
                                            });
                            builder.show();
                        }
                    }
                }
            }

            @Override
            public void onFinish() {

            }

        };
        countDownTimer.start();
    }

    private void cancelTimer() {
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

    private void onError(int operation, Exception exception){
        //        isUpdateClicked =false;
//        isAllSyncClicked=false;
        isUpdateSyncing=false;
        ErrorBean errorBean = Constants.getErrorCode(operation, exception, getActivity());

        try {
            if (exception.toString().contains("HTTP Status 401 ? Unauthorized") || exception.toString().contains("invalid authentication")) {
                try {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
                    String loginUser = sharedPreferences.getString("username", "");
                    String login_pwd = sharedPreferences.getString("password", "");
                    Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                        @Override
                        public void passwordStatus(final JSONObject jsonObject) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    closingProgressDialog();
                                    Constants.passwordStatusErrorMessage(getActivity(), jsonObject, registrationModel, loginUser);
                                }
                            });

                        }
                    });
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            } else {
                if (errorBean.hasNoError()) {
                    mError++;
                    penReqCount++;
                    mBoolIsReqResAval = true;
                    if ((operation == Operation.Create.getValue()) && (penReqCount == mIntPendingCollVal)) {
                        postOfflineData();
                    }
                    if (operation == Operation.OfflineFlush.getValue()) {
                        if (UtilConstants.isNetworkAvailable(getActivity())) {
                            if (!alAssignColl.contains(Constants.ConfigTypsetTypeValues))
                                alAssignColl.add(Constants.ConfigTypsetTypeValues);
                            concatCollectionStr = Constants.getConcatinatinFlushCollectios(alAssignColl);
                            LogManager.writeLogInfo(concatCollectionStr + " refresh started");
                            messageProgess = "Refreshing data. Please wait...";
                            new RefreshAsyncTask(getActivity(), concatCollectionStr, this).execute();
                        } else {
                            closingProgressDialog();
                            Constants.showAlert(getString(R.string.data_conn_lost_during_sync), getActivity());
                        }

                    } else if (operation == Operation.OfflineRefresh.getValue()) {
                        LogManager.writeLogError("Error : " + exception.getMessage());
                        Constants.isSync = false;
                        String mErrorMsg = "";
                        if (Constants.AL_ERROR_MSG.size() > 0) {
                            mErrorMsg = Constants.convertALBussinessMsgToString(Constants.AL_ERROR_MSG);
                        }
                        closingProgressDialog();
                        if (mErrorMsg.equalsIgnoreCase("")) {
                            Constants.showAlert(errorBean.getErrorMsg(), getActivity());
                        } else {
                            Constants.customAlertDialogWithScroll(getActivity(), mErrorMsg);
                        }

                    } else if (operation == Operation.GetStoreOpen.getValue()) {
                        closingProgressDialog();
                        Constants.showAlert(getString(R.string.msg_offline_store_failure),
                                getActivity());
                    }
                } else {
                    mBoolIsReqResAval = true;
                    mBoolIsNetWorkNotAval = true;
                    Constants.isSync = false;
                    if (errorBean.isStoreFailed()) {
                        OfflineManager.offlineStore = null;
                        OfflineManager.options = null;
                        try {
                            if (!OfflineManager.isOfflineStoreOpen()) {
                                closingProgressDialog();
                                try {
                                    new OpenOfflineStore().execute();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                closingProgressDialog();
                                Constants.displayMsgReqError(errorBean.getErrorCode(), getActivity());
                            }
                        } catch (Exception e) {
                            closingProgressDialog();
                            Constants.displayMsgReqError(errorBean.getErrorCode(), getActivity());
                            e.printStackTrace();
                        }
                    } else {
                        closingProgressDialog();
                        Constants.displayMsgReqError(errorBean.getErrorCode(), getActivity());
                    }


                }
            }
        } catch (Exception e) {
            mBoolIsReqResAval = true;
            mBoolIsNetWorkNotAval = true;
            Constants.isSync = false;
            closingProgressDialog();
            Constants.displayMsgReqError(errorBean.getErrorCode(), getActivity());
        }

    }

    private void onSuccess(int operation, String key,boolean isUploadData){
//        closingProgressDialog();
        if(isUploadData){
            onUpdateSync();
        }else {
            onAllSync();
        }
    }
}


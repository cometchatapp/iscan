package com.arteriatech.ss.msec.iscan.v4.sync;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.AvailableServer;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.asyncTask.RefreshAsyncTask;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.common.MSFAApplication;
import com.arteriatech.ss.msec.iscan.v4.log.ErrorCode;
import com.arteriatech.ss.msec.iscan.v4.mbo.ErrorBean;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.Operation;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack;
import com.arteriatech.ss.msec.iscan.v4.mutils.location.LocationInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.location.LocationModel;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity;
import com.arteriatech.ss.msec.iscan.v4.mutils.store.OnlineODataInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.sync.SyncHistoryDB;

import com.arteriatech.ss.msec.iscan.v4.registration.Configuration;
import com.arteriatech.ss.msec.iscan.v4.registration.RegistrationActivity;

import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.store.OnlineManager;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncInfo.RefreshListInterface;
import com.arteriatech.ss.msec.iscan.v4.sync.syncSelectionView.SyncSelectionViewActivity;
import com.sap.client.odata.v4.core.GUID;
import com.sap.smp.client.odata.ODataDuration;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.impl.ODataDurationDefaultImpl;
import com.sap.smp.client.odata.store.ODataRequestExecution;

import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.arteriatech.ss.msec.iscan.v4.common.Constants.REPEATABLE_REQUEST_ID;
import static com.arteriatech.ss.msec.iscan.v4.common.Constants.isAllSync;

import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.server_Text;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.server_Text_DR;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.server_Text_default;
import static com.arteriatech.ss.msec.iscan.v4.registration.RegistrationActivity.registrationModel;

/**
 * This class shows selection type of sync icons in grid manner.
 */

@SuppressLint("NewApi")
public class SyncSelectionActivity extends AppCompatActivity implements UIListener, OnlineODataInterface, OnClickListener {
    GridView grid_main;
    TextView toolbar_text;
    //    String iconName[] = {"All Download", "Download", "Upload", "Sync History","Day End Activity","Log"};
//    String iconName[] = {"All Download", "Download", "Upload", "Sync History"};
//    int OriginalStatus[] = {1, 1, 1, 1};
    int TempStatus[] = {1, 1, 1, 1, 1};
    String[] iconName = null;
    int[] OriginalStatus = null;
    String[][] invKeyValues;
    int mIntPendingCollVal = 0;
    String mErrorMsg = "";
    ProgressDialog syncProgDialog;
    Hashtable dbHeadTable;
    ArrayList<HashMap<String, String>> arrtable;
    ArrayList<String> alAssignColl = new ArrayList<>();
    ArrayList<String> alFlushColl = new ArrayList<>();
    String concatCollectionStr = "";
    String concatFlushCollStr = "";
    int[] cancelSOCount = new int[0];
    int updateCancelSOCount = 0;
    int cancelSoPos = 0;
    String endPointURL = "";
    String appConnID = "";
    int mError = 0;
    boolean onlineStoreOpen = false;
    private boolean dialogCancelled = false;
    private int penReqCount = 0;
    private boolean mBoolIsNetWorkNotAval = false;
    private boolean mBoolIsReqResAval = false;
    private JSONObject fetchJsonHeaderObject = null;
    private boolean isBatchReqs = false;
    private boolean tokenFlag = false;
    TextView textViewSubTitle, textViewTitle;
    String syncHistoryType = "";
    ImageView imageViewHome;
    public static volatile boolean isLocalSync;
    private GUID refguid = null;

    public static ArrayList<String> getRefreshList() {
        ArrayList<String> alAssignColl = new ArrayList<>();
        try {

            try {
                    /*if (OfflineManager.getVisitStatusForCustomer(Constants.ChannelPartners + "?$select=CPGUID &$filter= sap.islocal() ")) {
                        alAssignColl.add(Constants.ChannelPartners);
                        alAssignColl.add(Constants.CPDMSDivisions);
                        alAssignColl.add(Constants.CPSPRelations);

                        if(!rollID.equalsIgnoreCase(Constants.VAN)) {
                            alAssignColl.add(Constants.RoutePlans);
                        }
                        alAssignColl.add(Constants.RouteSchedulePlans);
                        alAssignColl.add(Constants.RouteScheduleSPs);
                        alAssignColl.add(Constants.RouteScheduleDetails);
                    }*/

                if (OfflineManager.getVisitStatusForCustomer(Constants.Visits + "?$select=VisitGUID &$filter= sap.islocal() ")) {
                    alAssignColl.add(Constants.Visits);
                }
                if (OfflineManager.getVisitStatusForCustomer(Constants.VisitActivities + "?$select=VisitActivityGUID &$filter= sap.islocal() ")) {
                    alAssignColl.add(Constants.VisitActivities);
                }
                if (OfflineManager.getVisitStatusForCustomer(Constants.SyncHistorys + "?$select=SyncHisGuid &$filter= sap.islocal() ")) {
                    alAssignColl.add(Constants.SyncHistorys);
                }
                if (OfflineManager.getVisitStatusForCustomer(Constants.Attendances + "?$filter= sap.islocal() ")) {
                    alAssignColl.add(Constants.Attendances);
                }
                if (OfflineManager.getVisitStatusForCustomer(Constants.SPNumberRangeSet + "?$filter= sap.islocal() ")) {
                    alAssignColl.add(Constants.SPNumberRangeSet);
                }

            } catch (OfflineODataStoreException e) {
                LogManager.writeLogError("Error : " + e.getMessage());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return alAssignColl;
    }
    public static ArrayList<String> getRefreshList(boolean runonService) {
        ArrayList<String> alAssignColl = new ArrayList<>();
        try {
            try {
                if (OfflineManager.getVisitStatusForCustomer(Constants.ChannelPartners + "?$select=CPGUID &$filter= sap.islocal() ")) {
                    alAssignColl.add(Constants.ChannelPartners);
                    alAssignColl.add(Constants.CPDMSDivisions);
                    alAssignColl.add(Constants.CPSPRelations);

                    if(!rollID.equalsIgnoreCase(Constants.VAN)) {
                        alAssignColl.add(Constants.RoutePlans);
                    }
                    alAssignColl.add(Constants.RouteSchedulePlans);
                    alAssignColl.add(Constants.RouteScheduleSPs);
                    alAssignColl.add(Constants.RouteScheduleDetails);
                }

                if (OfflineManager.getVisitStatusForCustomer(Constants.Visits + "?$select=VisitGUID &$filter= sap.islocal() ")) {
                    alAssignColl.add(Constants.Visits);
                }
                if (OfflineManager.getVisitStatusForCustomer(Constants.VisitActivities + "?$select=VisitActivityGUID &$filter= sap.islocal() ")) {
                    alAssignColl.add(Constants.VisitActivities);
                }
                if (OfflineManager.getVisitStatusForCustomer(Constants.SyncHistorys + "?$select=SyncHisGuid &$filter= sap.islocal() ")) {
                    alAssignColl.add(Constants.SyncHistorys);
                }
                if (OfflineManager.getVisitStatusForCustomer(Constants.Attendances + "?$filter= sap.islocal() ")) {
                    alAssignColl.add(Constants.Attendances);
                }
                if (OfflineManager.getVisitStatusForCustomer(Constants.SPNumberRangeSet + "?$filter= sap.islocal() ")) {
                    alAssignColl.add(Constants.SPNumberRangeSet);
                }

            } catch (OfflineODataStoreException e) {
                LogManager.writeLogError("Error : " + e.getMessage());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return alAssignColl;
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
        set = sharedPreferences.getStringSet(Constants.MaterialDocs, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.MaterialDocs;
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
            Arrays.sort(invKeyValues, new ArrayComarator());
            objectsArrayList.add(mIntPendingCollVal);
            objectsArrayList.add(invKeyValues);
        }

        return objectsArrayList;

    }

    public static ArrayList<Object> getPendingCollListOrderBill(Context mContext, boolean isFromAutoSync) {
        ArrayList<Object> objectsArrayList = new ArrayList<>();
        int mIntPendingCollVal = 0;
        String[][] invKeyValues = null;
        Set<String> set = new HashSet<>();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        invKeyValues = new String[getPendingListSize(mContext)][2];

        set = sharedPreferences.getStringSet(Constants.SampleDisbursement, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.SampleDisbursement;
                mIntPendingCollVal++;
            }
        }
        if (mIntPendingCollVal > 0) {
            Arrays.sort(invKeyValues, new ArrayComarator());
            objectsArrayList.add(mIntPendingCollVal);
            objectsArrayList.add(invKeyValues);
        }

        return objectsArrayList;

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
        set = sharedPreferences.getStringSet(Constants.MaterialDocs, null);
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
    static String rollID="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize action bar with back button(true)
        setContentView(R.layout.activity_sync_selection);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
        rollID = ConstantsUtils.getAuthInformation(this);
        //        getIDPUrl(this);
        if (!Constants.restartApp(SyncSelectionActivity.this)) {
            onInitUI();
            setIconVisiblity();
            setValuesToUI();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       // MSFAApplication.setAnalytics(this,MSFAApplication.SP_UID,this.getClass().getSimpleName(),"GPRS sync");
    }

    /*
     * This method initialize UI
     */
    private void onInitUI() {
        grid_main = (GridView) findViewById(R.id.GridView01);
        toolbar_text = (TextView) findViewById(R.id.toolbar_text);
        toolbar_text.setText("Sync");
        toolbar_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /*
  This method set values to UI
  */
    private void setValuesToUI() {
        grid_main.setAdapter(new SyncAdapter(this));
    }

    private void setIconVisiblity() {
//        OriginalStatus[0] = 1;
//        OriginalStatus[1] = 1;
//        OriginalStatus[2] = 1;
//        OriginalStatus[3] = 1;
        if (MSFAApplication.isSDA) {
            iconName = new String[5];
            iconName[0] = "All Download";
            iconName[1] = "Download";
            iconName[2] = "Upload";
            iconName[3] = "Day End Activity";
            iconName[4] = "Sync History";
//            iconName[5] = "Support";
//            iconName[6] = "Settings";
            OriginalStatus = new int[5];
            OriginalStatus[0] = 1;
            OriginalStatus[1] = 1;
            OriginalStatus[2] = 1;
            OriginalStatus[3] = 1;
            OriginalStatus[4] = 1;
//            OriginalStatus[5] = 1;
//            OriginalStatus[6] = 1;
        }else if(MSFAApplication.isVAN){
            iconName = new String[5];
            iconName[0] = "All Download";
            iconName[1] = "Download";
            iconName[2] = "Upload";
            iconName[3] = "Sync History";
            iconName[4] = "Day End Activity";
//            iconName[5] = "Support";
//            iconName[6] = "Settings";
            OriginalStatus = new int[5];
            OriginalStatus[0] = 1;
            OriginalStatus[1] = 1;
            OriginalStatus[2] = 1;
            OriginalStatus[3] = 1;
            OriginalStatus[4] = 1;
//            OriginalStatus[5] = 1;
//            OriginalStatus[6] = 1;
        } else {
            iconName = new String[4];
            iconName[0] = "All Download";
            iconName[1] = "Download";
            iconName[2] = "Upload";
            iconName[3] = "Sync History";
//            iconName[4] = "Storage Log";
//            iconName[5] = "Support";
//            iconName[6] = "Settings";

            OriginalStatus = new int[4];
            OriginalStatus[0] = 1;
            OriginalStatus[1] = 1;
            OriginalStatus[2] = 1;
            OriginalStatus[3] = 1;
//            OriginalStatus[4] = 1;
//            OriginalStatus[5] = 1;
//            OriginalStatus[6] = 0;
        }
        int countStatus = 0;
        int len = OriginalStatus.length;
        for (int countOriginalStaus = 0; countOriginalStaus < len; countOriginalStaus++) {
            if (OriginalStatus[countOriginalStaus] == 1) {
                TempStatus[countStatus] = countOriginalStaus;
                countStatus++;
            }
        }
    }

    private void checkPendingReqIsAval() {
        try {
            mIntPendingCollVal = 0;
            invKeyValues = null;
            ArrayList<Object> objectArrayList = getPendingCollList(SyncSelectionActivity.this, false);
            if (!objectArrayList.isEmpty()) {
                mIntPendingCollVal = (int) objectArrayList.get(0);
                invKeyValues = (String[][]) objectArrayList.get(1);
            }
            penReqCount = 0;
            alAssignColl.clear();
            alFlushColl.clear();
            concatCollectionStr = "";
            concatFlushCollStr = "";
            ArrayList<String> allAssignColl = getRefreshList();
            if (!allAssignColl.isEmpty()) {
                alAssignColl.addAll(allAssignColl);
                alFlushColl.addAll(allAssignColl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This method update pending requests.
     */
    void showAlertDialog(String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton(this.getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        isUpdateClicked = false;
                    }
                });
        builder.show();
    }

    boolean isUpdateClicked = false;
    boolean isUpdate = false;
    int cpCount = 0;
    int soCount = 0;
    boolean isUpdateSyncing=false;
    private void onUpdateSync() {
        try {
            LogManager.writeLogInfo("Update sync started");
            if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                LogManager.writeLogInfo("Internet available");

                if (!isUpdateClicked) {
                    try {
                        removeFromDatavault(this);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    isUpdateClicked = true;
                    Set<String> set = new HashSet<>();
                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                    set = sharedPreferences.getStringSet(Constants.SecondarySOCreate, null);
                    if (set != null && !set.isEmpty()) {
                        Iterator itr = set.iterator();
                        while (itr.hasNext()) {
                            String store = null, deviceNo = "";
                            deviceNo = itr.next().toString();
                            try {
                                store = ConstantsUtils.getFromDataVault(deviceNo, SyncSelectionActivity.this);
                                LogManager.writeLogInfo("Remove reversed orders");
                                if (store != null) {
                                    if (!TextUtils.isEmpty(store)) {
                                        JSONObject fetchJsonHeaderObject = new JSONObject(store);
                                        if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                                            if (!fetchJsonHeaderObject.getString(Constants.Status).equalsIgnoreCase("X") && !fetchJsonHeaderObject.getString(Constants.Status).equalsIgnoreCase("000000")) {
                                                Constants.removeDataValtFromSharedPref(SyncSelectionActivity.this, Constants.SecondarySOCreate, deviceNo, false);
                                            }

                                        }
                                    } else {
                                        Constants.removeDataValtFromSharedPref(this, Constants.SecondarySOCreate, deviceNo, false);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    try {
                        if (Constants.Entity_Set != null) {
                            Constants.Entity_Set.clear();
                        }
                        if (Constants.AL_ERROR_MSG != null) {
                            Constants.AL_ERROR_MSG.clear();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    mBoolIsNetWorkNotAval = false;
                    isBatchReqs = false;
                    mBoolIsReqResAval = true;
                    updateCancelSOCount = 0;
                    cancelSoPos = 0;
                    try {
                        LogManager.writeLogInfo("Check Pending Request");
                        checkPendingReqIsAval();

                        if (OfflineManager.offlineStore.getRequestQueueIsEmpty() && mIntPendingCollVal == 0) {
                            closingProgressDialog();
                            isUpdateClicked = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Constants.showAlert(getString(R.string.no_req_to_update_sap), SyncSelectionActivity.this);
                                }
                            });
                        } else {
                            if (mIntPendingCollVal > 0) {
                                LogManager.writeLogInfo("Pending Request(s) : " + mIntPendingCollVal);

                                if (!alAssignColl.contains(Constants.ConfigTypsetTypeValues)) {
                                    alAssignColl.add(Constants.ConfigTypsetTypeValues);
                                }

                                if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                                    refguid = GUID.newRandom();
                                    SyncUtils.updatingSyncStartTime(SyncSelectionActivity.this, Constants.UpLoad, Constants.StartSync, refguid.toString().toUpperCase());
//                                    countdown = "";
                                    isUpdateSyncing=true;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            onPostOnlineData();
                                        }
                                    });
                                } else {
                                    Constants.showAlert(ErrorCode.NETWORK_ERROR, this);
                                }
                            } else if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                                if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                                    refguid = GUID.newRandom();
                                    SyncUtils.updatingSyncStartTime(SyncSelectionActivity.this, Constants.UpLoad, Constants.StartSync, refguid.toString().toUpperCase());
//                                    countdown = "";
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            onPostOfflineData();
                                        }
                                    });
                                } else {
                                    Constants.showAlert(ErrorCode.NETWORK_ERROR, this);
                                }
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        isUpdateClicked = false;
                    }
                }
            } else {
                isUpdateClicked = false;
                LogManager.writeLogInfo("Internet not available. Upload sync ended");
                Constants.showAlert(ErrorCode.NETWORK_ERROR, this);
            }
        } catch (Throwable e) {
            isUpdateClicked = false;
            e.printStackTrace();
        }
    }

    private void onPostOfflineData() {
        Constants.isSync = true;
        try {
            new AsyncPostOfflineData().execute();
        } catch (Exception e) {
            e.printStackTrace();
            isUpdateClicked = false;
        }
    }

    PostingDataValutData asyncTask;

    private void onPostOnlineData() {
        try {
//            setTimer("02");
            asyncTask = new PostingDataValutData();
            asyncTask.execute();
            ConstantsUtils.checkNetwork(this, isFailed -> {
                if (isFailed) {
                    runOnUiThread(() -> {
                        asyncTask.cancel(true);
                        if (!asyncTask.isCancelled()) {
                            while (asyncTask.isCancelled()) {
                                asyncTask.cancel(true);
                            }
                        }
                        closingProgressDialog();
                        SyncSelectionActivity.this.runOnUiThread(() -> Constants.showAlert(ErrorCode.NETWORK_ERROR, SyncSelectionActivity.this));
                    });
                }
            }, false);
        } catch (Exception e) {
            e.printStackTrace();
            isUpdateClicked = false;
        }
    }


    private LoadingData loadingData=null;
    private void onSyncAll() {
        try {
            try {
                if (Constants.AL_ERROR_MSG != null) {
                    Constants.AL_ERROR_MSG.clear();
                }
                if (Constants.Entity_Set != null) {
                    Constants.Entity_Set.clear();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            Constants.isSync = true;
            dialogCancelled = false;
            Constants.isStoreClosed = false;
            loadingData = new LoadingData();
            loadingData.execute();
            /*ConstantsUtils.checkNetwork(this, isFailed -> {
                if (isFailed) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingData.cancel(true);
                            if (!loadingData.isCancelled()){
                                while (loadingData.isCancelled()){
                                    loadingData.cancel(true);
                                }
                            }
                            closingProgressDialog();
                            SyncSelectionActivity.this.runOnUiThread(() -> Constants.showAlert(ErrorCode.NETWORK_ERROR, SyncSelectionActivity.this));
                            isAllSyncClicked = false;

                        }
                    });
                }
            },false);*/
        } catch (Exception e) {
            e.printStackTrace();
            isAllSyncClicked = false;
        }
    }

    /**
     * This method calls sync all collections for the selected "All" icon
     */
    boolean isAllSyncClicked = false;

    private void AvailableServerAllSync(boolean isUploadData){
        if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    countdown = "";
                    syncProgDialog = new ProgressDialog(SyncSelectionActivity.this, R.style.ProgressDialogTheme);
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

            String hostName = getSharedPreferences(Constants.PREFS_NAME, 0).getString(Constants.ActiveHost, "");
            try {
                AvailableServer.clearCookies();
                if (AvailableServer.pingServer(server_Text)) {
                    JSONObject resourceFileReadresponse = AvailableServer.getResourceFile(server_Text,this);
                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
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
                                    onUpdateSync();
                                }else {
                                    onAllSync();
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
                                    resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_default,this);
                                    if (resourceFileReadresponse != null) {
                                        serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                                        status = serverResponse.getBoolean(Constants.Status);
                                        responseCode = serverResponse.getInt(Constants.ResponseCode);
                                        messageError = serverResponse.getString(Constants.Message);
                                        if (responseCode == 200) {
                                            if (status) {
                                                if(isUploadData){
                                                    onUpdateSync();
                                                }else {
                                                    onAllSync();
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
                                                sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                                String loginUser = sharedPreferences.getString("username", "");
                                                String login_pwd = sharedPreferences.getString("password", "");
                                                Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                                                    @Override
                                                    public void passwordStatus(final JSONObject jsonObject) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                closingProgressDialog();
                                                                Constants.passwordStatusErrorMessage(SyncSelectionActivity.this, jsonObject, registrationModel, loginUser);
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
                                    resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_DR,this);
                                    if (resourceFileReadresponse != null) {
                                        serverResponse = AvailableServer.getJSONServerResponse(resourceFileReadresponse);
                                        status = serverResponse.getBoolean(Constants.Status);
                                        responseCode = serverResponse.getInt(Constants.ResponseCode);
                                        messageError = serverResponse.getString(Constants.Message);
                                        if (responseCode == 200) {
                                            if (status) {
                                                if(isUploadData){
                                                    onUpdateSync();
                                                }else {
                                                    onAllSync();
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
                                                sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                                String loginUser = sharedPreferences.getString("username", "");
                                                String login_pwd = sharedPreferences.getString("password", "");
                                                Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                                                    @Override
                                                    public void passwordStatus(final JSONObject jsonObject) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                closingProgressDialog();
                                                                Constants.passwordStatusErrorMessage(SyncSelectionActivity.this, jsonObject, registrationModel, loginUser);
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
                                sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                String loginUser = sharedPreferences.getString("username", "");
                                String login_pwd = sharedPreferences.getString("password", "");
                                Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                                    @Override
                                    public void passwordStatus(final JSONObject jsonObject) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                closingProgressDialog();
                                                Constants.passwordStatusErrorMessage(SyncSelectionActivity.this, jsonObject, registrationModel, loginUser);
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
                            SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.ActiveHost, server_Text_default);
                            editor.apply();
                            server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                            JSONObject resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_default,this);
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
                                        AvailableServer.registerWithAvailableServer(this, new AvailableServer.RegisterServer() {
                                            @Override
                                            public void requestSuccess() {runOnUiThread(new Runnable() {
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
                                                AvailableServer.openStoreWithAvailableServer(SyncSelectionActivity.this, new UIListener() {
                                                    @Override
                                                    public void onRequestError(int operation, Exception e) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                onError(operation, e);
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                                                        runOnUiThread(new Runnable() {
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
                                        sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                        String loginUser = sharedPreferences.getString("username", "");
                                        String login_pwd = sharedPreferences.getString("password", "");
                                        Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                                            @Override
                                            public void passwordStatus(final JSONObject jsonObject) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        closingProgressDialog();
                                                        Constants.passwordStatusErrorMessage(SyncSelectionActivity.this, jsonObject, registrationModel, loginUser);
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
                            SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.ActiveHost, server_Text_DR);
                            editor.apply();
                            server_Text = sharedPreferences.getString(Constants.ActiveHost, server_Text);
                            JSONObject resourceFileReadresponse = AvailableServer.getResourceFile(server_Text_DR,this);
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
                                        AvailableServer.registerWithAvailableServer(this, new AvailableServer.RegisterServer() {
                                            @Override
                                            public void requestSuccess() {
                                                runOnUiThread(new Runnable() {
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
                                                AvailableServer.openStoreWithAvailableServer(SyncSelectionActivity.this, new UIListener() {
                                                    @Override
                                                    public void onRequestError(int operation, Exception e) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                onError(operation, e);
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException {
                                                        runOnUiThread(new Runnable() {
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
                                        sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                                        String loginUser = sharedPreferences.getString("username", "");
                                        String login_pwd = sharedPreferences.getString("password", "");
                                        Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                                            @Override
                                            public void passwordStatus(final JSONObject jsonObject) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        closingProgressDialog();
                                                        Constants.passwordStatusErrorMessage(SyncSelectionActivity.this, jsonObject, registrationModel, loginUser);
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Constants.showAlert(ErrorCode.NETWORK_ERROR, SyncSelectionActivity.this);
                }
            });

            isAllSyncClicked = false;
            isAllSync = false;
        }
    }

    private void onAllSync() {
        mError=0;
        mErrorMsg="";
        if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
            if (!isAllSyncClicked) {
                isAllSyncClicked = true;
                isAllSync = true;
                isUpdate = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onSyncAll();
                    }
                });
            }
        } else {
            Constants.showAlert(ErrorCode.NETWORK_ERROR, SyncSelectionActivity.this);
            isAllSyncClicked = false;
            isAllSync = false;
        }
    }

    /**
     * This method calls fresh sync for the selected "Fresh" icon
     */
    private void onFreshSync() {
        if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
            Intent intent = new Intent(this, SyncSelectionViewActivity.class);
            startActivity(intent);
        } else {
            Constants.showAlert(ErrorCode.NETWORK_ERROR, SyncSelectionActivity.this);
        }
    }

    private void assignCollToArrayList() {
        alAssignColl.clear();
        concatCollectionStr = "";
        alAssignColl.addAll(SyncUtils.getAllSyncValue(SyncSelectionActivity.this));
        concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(alAssignColl);
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

    private void setAppointmentNotification() {
//        new NotificationSetClass(this);//TODO need to enable

    }

    private void onError(int operation, Exception exception){
        ConstantsUtils.checkNetwork(this, null, true);
        isUpdateClicked = false;
        isAllSyncClicked = false;
        isUpdateSyncing=false;
        String errorCode = ConstantsUtils.getErrorCode(String.valueOf(exception.getCause()));
        ErrorBean errorBean = Constants.getErrorCode(operation, exception, SyncSelectionActivity.this);
        String strErrorMsg = exception.toString();

        if (exception.toString().contains("HTTP Status 401 ? Unauthorized") || exception.toString().contains("invalid authentication")) {
            try {
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                String loginUser = sharedPreferences.getString("username", "");
                String login_pwd = sharedPreferences.getString("password", "");
                Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                    @Override
                    public void passwordStatus(final JSONObject jsonObject) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closingProgressDialog();
                                Constants.passwordStatusErrorMessage(SyncSelectionActivity.this, jsonObject, registrationModel, loginUser);
                            }
                        });

                    }
                });
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            try {
                if (errorBean.hasNoError()) {
                    LogManager.writeLogError("Error : " + exception.getMessage());
                    Constants.isSync = false;
                    String mErrorMsg = "";
                    if (Constants.AL_ERROR_MSG.size() > 0) {
                        mErrorMsg = Constants.convertALBussinessMsgToString(Constants.AL_ERROR_MSG);
                    }
                    final String finalMErrorMsg = mErrorMsg;
                    Constants.updateLastSyncTimeToTable(SyncSelectionActivity.this, alAssignColl, refguid.toString().toUpperCase(), syncHistoryType, new RefreshListInterface() {
                        @Override
                        public void refreshList() {
                            closingProgressDialog();
                            if (finalMErrorMsg.equalsIgnoreCase("")) {
                                showAlert(errorBean.getErrorMsg());
                            } else {
                                Constants.customAlertDialogWithScrollError(SyncSelectionActivity.this, finalMErrorMsg, SyncSelectionActivity.this);
                            }
                        }
                    });
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
                                Constants.displayMsgReqError(errorBean.getErrorCode(), SyncSelectionActivity.this);
                            }
                        } catch (Exception e) {
                            closingProgressDialog();
                            Constants.displayMsgReqError(errorBean.getErrorCode(), SyncSelectionActivity.this);
                            e.printStackTrace();
                        }
                    } else {
                        closingProgressDialog();
                        Constants.displayMsgReqError(errorBean.getErrorCode(), SyncSelectionActivity.this);
                    }
                }
            } catch (Exception e) {
                mBoolIsReqResAval = true;
                mBoolIsNetWorkNotAval = true;
                Constants.isSync = false;
                closingProgressDialog();
                Constants.displayMsgReqError(errorBean.getErrorCode(), SyncSelectionActivity.this);
            }
        }
    }

    @Override
    public void onRequestError(int operation, Exception exception) {
        ConstantsUtils.checkNetwork(this, null, true);
        isUpdateClicked = false;
        isAllSyncClicked = false;
        isUpdateSyncing=false;
        String errorCode = ConstantsUtils.getErrorCode(String.valueOf(exception.getCause()));
        ErrorBean errorBean = Constants.getErrorCode(operation, exception, SyncSelectionActivity.this);
        String strErrorMsg = exception.toString();

        if (exception.toString().contains("HTTP Status 401 ? Unauthorized") || exception.toString().contains("invalid authentication")) {
            try {
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
                String loginUser = sharedPreferences.getString("username", "");
                String login_pwd = sharedPreferences.getString("password", "");
                Constants.getPasswordStatus(Configuration.IDPURL, loginUser, login_pwd, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                    @Override
                    public void passwordStatus(final JSONObject jsonObject) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closingProgressDialog();
                                Constants.passwordStatusErrorMessage(SyncSelectionActivity.this, jsonObject, registrationModel, loginUser);
                            }
                        });

                    }
                });
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            try {
                if (errorBean.hasNoError()) {
                    mError++;
                    penReqCount++;
                    mBoolIsReqResAval = true;
                    if ((operation == Operation.Create.getValue()) && (penReqCount == mIntPendingCollVal)) {
                        postOfflineData();
                    }
                    if (operation == Operation.OfflineFlush.getValue()) {
                        if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                            if (!alAssignColl.contains(Constants.ConfigTypsetTypeValues))
                                alAssignColl.add(Constants.ConfigTypsetTypeValues);
                            concatCollectionStr = Constants.getConcatinatinFlushCollectios(alAssignColl);
                            LogManager.writeLogInfo(concatCollectionStr + " refresh started");
                            messageProgess = "Refreshing data. Please wait...";
                            new RefreshAsyncTask(SyncSelectionActivity.this, concatCollectionStr, this).execute();
                        } else {
                            closingProgressDialog();
                            Constants.showAlert(getString(R.string.data_conn_lost_during_sync), SyncSelectionActivity.this);
                        }

                    } else if (operation == Operation.OfflineRefresh.getValue()) {
                        LogManager.writeLogError("Error : " + exception.getMessage());
                        Constants.isSync = false;
                        String mErrorMsg = "";
                        if (Constants.AL_ERROR_MSG.size() > 0) {
                            mErrorMsg = Constants.convertALBussinessMsgToString(Constants.AL_ERROR_MSG);
                        }
                        final String finalMErrorMsg = mErrorMsg;
                        Constants.updateLastSyncTimeToTable(SyncSelectionActivity.this, alAssignColl, refguid.toString().toUpperCase(), syncHistoryType, new RefreshListInterface() {
                            @Override
                            public void refreshList() {
                                closingProgressDialog();
                                if (finalMErrorMsg.equalsIgnoreCase("")) {
                                    showAlert(errorBean.getErrorMsg());
                                } else {
                                    Constants.customAlertDialogWithScrollError(SyncSelectionActivity.this, finalMErrorMsg, SyncSelectionActivity.this);
                                }
                            }
                        });
                        /*closingProgressDialog();
                        if (mErrorMsg.equalsIgnoreCase("")) {
                            Constants.showAlert(errorBean.getErrorMsg(), SyncSelectionActivity.this);
                        } else {
                            Constants.showAlert(String.valueOf(exception.getCause()), SyncSelectionActivity.this);
    //                        Constants.customAlertDialogWithScroll(SyncSelectionActivity.this, mErrorMsg);
                        }*/

                    } else if (operation == Operation.GetStoreOpen.getValue()) {
                        closingProgressDialog();
                        Constants.showAlert(getString(R.string.msg_offline_store_failure),
                                SyncSelectionActivity.this);
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
                                Constants.displayMsgReqError(errorBean.getErrorCode(), SyncSelectionActivity.this);
                            }
                        } catch (Exception e) {
                            closingProgressDialog();
                            Constants.displayMsgReqError(errorBean.getErrorCode(), SyncSelectionActivity.this);
                            e.printStackTrace();
                        }
                    } else {
                        closingProgressDialog();
                        Constants.displayMsgReqError(errorBean.getErrorCode(), SyncSelectionActivity.this);
                    }


                }
            } catch (Exception e) {
                mBoolIsReqResAval = true;
                mBoolIsNetWorkNotAval = true;
                Constants.isSync = false;
                closingProgressDialog();
                Constants.displayMsgReqError(errorBean.getErrorCode(), SyncSelectionActivity.this);
            }
        }
    }

    private void onSuccess(int operation, String key,boolean isUploadData){
//        closingProgressDialog();
        if(isUploadData){
            onUpdateSync();
        }else {
            onAllSync();
        }
//        isUpdateClicked = false;
//        isAllSyncClicked = false;
//        isUpdateSyncing = false;
//            try {
//                LogManager.writeLogDebug("Sync Selection DB Export");
//                exportDB(SyncSelectionActivity.this);
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//        try {
//            if (alAssignColl.contains(Constants.UserProfileAuthSet)) {
//                try {
//                    OfflineManager.getAuthorizations(getApplicationContext());
//                } catch (OfflineODataStoreException e) {
//                    e.printStackTrace();
//                }
//            }
//            try {
//                if (OfflineManager.isOfflineStoreOpen()) {
//                    MSFAApplication.getGAFields(this);
//                    MSFAApplication.setGAFields(this);
//                }
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//            if (isAllSync || isUpdate) {
//                try {
//                    OutletSQLHelper helper = new OutletSQLHelper(this);
//                    helper.deleteChannelPartners();
//                    helper.deleteCPDMSDivisions();
//                    helper.deleteRouteSchPlans();
//                    try {
//                        SyncHistoryDB.deleteAllInvoices(SyncSelectionActivity.this);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } catch (Throwable e) {
//                    isAllSync = false;
//                    isUpdate = false;
//                }
//            }
//
//
//            ConstantsUtils.checkNetwork(this, null, true);
//            try {
//                ConstantsUtils.deletePostedSOData(this);
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//            try {
//                ConstantsUtils.deletePostedROData(this);
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
////            new LoadData(this).execute();
//            LogManager.writeLogInfo("refresh sync completed");
//            Constants.updateLastSyncTimeToTable(SyncSelectionActivity.this, alAssignColl, refguid.toString().toUpperCase(), syncHistoryType, new RefreshListInterface() {
//                @Override
//                public void refreshList() {
//                    try {
//                        new LoadData(SyncSelectionActivity.this).execute();
//                    } catch (Throwable e) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        });
//                    }
//                }
//            });
//            isAllSync = false;
//            isUpdate = false;
//            isLocalSync = false;
//            if (Constants.AL_ERROR_MSG.size() > 0) {
//                mErrorMsg = Constants.convertALBussinessMsgToString(Constants.AL_ERROR_MSG);
//            }
//            deleteReturnOrderVaultPendingSync(this);
//        } catch (Exception e) {
//            closingProgressDialog();
//            LogManager.writeLogError("error while refresh sync " + e.toString());
//        }

    }

    @Override
    public void onRequestSuccess(int operation, String key) {
        isUpdateClicked = false;
        isAllSyncClicked = false;
        isUpdateSyncing=false;
        try {
            if (operation == Operation.Create.getValue() && mIntPendingCollVal > 0) {
                mBoolIsReqResAval = true;
                Constants.removeDataValtFromSharedPref(SyncSelectionActivity.this, invKeyValues[penReqCount][1], invKeyValues[penReqCount][0], false);

                try {
                    if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.SecondarySOCreate)) {

                        JSONObject jsonObject1 = null;
                        String orderNo = "";
                        try {
                            jsonObject1 = Constants.getJSONBody(key);
                            orderNo = jsonObject1.getString("OrderNo");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        LogManager.writeLogInfo("Sale Order " + orderNo + " created successfully");
                        Constants.saveDeviceDocNoToSharedPref(SyncSelectionActivity.this, Constants.SecondarySOCreate, invKeyValues[penReqCount][0]);
                        fetchJsonHeaderObject.put(Constants.Status, "000000");
                        fetchJsonHeaderObject.put(Constants.OrderNo, orderNo);
                        LogManager.writeLogInfo("Device order no. " + invKeyValues[penReqCount][0] + " updated with sale Order no. " + orderNo + " and status updated  after post ");
                        ConstantsUtils.storeInDataVault(invKeyValues[penReqCount][0], fetchJsonHeaderObject.toString(), SyncSelectionActivity.this);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                /*
                 if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.FinancialPostings)) {
                    Constants.removeDeviceDocNoFromSharedPref(SyncSelectionActivity.this, Constants.FinancialPostings, invKeyValues[penReqCount][0]);
                }
                if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.CollList)) {
                    Constants.removeDeviceDocNoFromSharedPref(SyncSelectionActivity.this, Constants.CollList, invKeyValues[penReqCount][0]);
                } else */
               /* if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.SecondarySOCreate)) {
                    Constants.removeDeviceDocNoFromSharedPref(SyncSelectionActivity.this, Constants.SecondarySOCreate, invKeyValues[penReqCount][0]);
                } else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.Feedbacks)) {
                    Constants.removeDeviceDocNoFromSharedPref(SyncSelectionActivity.this, Constants.Feedbacks, invKeyValues[penReqCount][0]);
                }*/ /*else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.InvList)) {
                    Constants.removeDeviceDocNoFromSharedPref(SyncSelectionActivity.this, Constants.InvList, invKeyValues[penReqCount][0]);
                } else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.ROList)) {
                    Constants.removeDeviceDocNoFromSharedPref(SyncSelectionActivity.this, Constants.ROList, invKeyValues[penReqCount][0]);
                } else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.SampleDisbursement)) {
                    Constants.removeDeviceDocNoFromSharedPref(SyncSelectionActivity.this, Constants.SampleDisbursement, invKeyValues[penReqCount][0]);
                }else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.Expenses)) {
                    Constants.removeDeviceDocNoFromSharedPref(SyncSelectionActivity.this, Constants.Expenses, invKeyValues[penReqCount][0]);
                }else if (invKeyValues[penReqCount][1].equalsIgnoreCase(Constants.CPList)) {
                    Constants.removeDeviceDocNoFromSharedPref(SyncSelectionActivity.this, Constants.CPList, invKeyValues[penReqCount][0]);
                }*/
//                ConstantsUtils.storeInDataVault(invKeyValues[penReqCount][0], "", SyncSelectionActivity.this);
                try {
                    SyncHistoryDB.deleteInvoiceDocID(SyncSelectionActivity.this, invKeyValues[penReqCount][0]);
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
        }else if (operation == Operation.Update.getValue()) {
            postOfflineData();
        }else if (operation == Operation.OfflineFlush.getValue()) {
            LogManager.writeLogInfo("offline flush completed");
            if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                if (!alAssignColl.contains(Constants.ConfigTypsetTypeValues))
                    alAssignColl.add(Constants.ConfigTypsetTypeValues);
                if (MSFAApplication.isVAN) {
                    alAssignColl.add(Constants.Attendances);
                }
                messageProgess = "Refreshing data. Please wait...";
                concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(alAssignColl);
                new RefreshAsyncTask(SyncSelectionActivity.this, concatCollectionStr, this).execute();
            } else {
                closingProgressDialog();
                Constants.showAlert(getString(R.string.data_conn_lost_during_sync), SyncSelectionActivity.this);
            }


        } else if (operation == Operation.OfflineRefresh.getValue()) {
            try {
                Constants.getTodayDashboardAch(SyncSelectionActivity.this);
            } catch (Throwable e) {
                e.printStackTrace();
            }
//            setUI();
            //commented distance post because of app crash

        } else if (operation == Operation.GetStoreOpen.getValue() && OfflineManager.isOfflineStoreOpen()) {
//                Constants.ReIntilizeStore =false;
            Constants.isSync = false;
            try {
                OfflineManager.getAuthorizations(getApplicationContext());
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            Constants.setSyncTime(SyncSelectionActivity.this);
            setStoreOpenUI();
        }
    }

    private void setUI(){
        try {
            if (alAssignColl.contains(Constants.UserProfileAuthSet)) {
                try {
                    OfflineManager.getAuthorizations(getApplicationContext());
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
            }


            if (isAllSync || isUpdate) {

            }


            ConstantsUtils.checkNetwork(this, null, true);
            try {
                ConstantsUtils.deletePostedSOData(this);
            } catch (Throwable e) {
                e.printStackTrace();
            }try {
                ConstantsUtils.deletePostedROData(this);
            } catch (Throwable e) {
                e.printStackTrace();
            }
//            new LoadData(this).execute();
            LogManager.writeLogInfo("refresh sync completed");
            Constants.updateLastSyncTimeToTable(SyncSelectionActivity.this, alAssignColl, refguid.toString().toUpperCase(), syncHistoryType, new RefreshListInterface() {
                @Override
                public void refreshList() {
                    try {
                        new LoadData(SyncSelectionActivity.this).execute();
                    } catch (Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                }
            });
            isAllSync = false;
            isUpdate = false;
            if (Constants.AL_ERROR_MSG.size() > 0) {
                mErrorMsg = Constants.convertALBussinessMsgToString(Constants.AL_ERROR_MSG);
            }
        } catch (Exception e) {
            closingProgressDialog();
            LogManager.writeLogError("error while refresh sync " + e.toString());
        }
    }

    public static void showAlert(String message, Context ctxt, Activity activity) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctxt, R.style.MyTheme);
            builder.setMessage(message).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           /* try {
                                AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, activity, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }*/
                            dialog.cancel();
                        }
                    });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getSchemList() {

    }

    private void setStoreOpenUI() {
        closingProgressDialog();
        Constants.showAlert(getString(R.string.msg_offline_store_success),
                SyncSelectionActivity.this);
    }

    private void closingProgressDialog() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (syncProgDialog != null && syncProgDialog.isShowing()) {
                        syncProgDialog.dismiss();
                        cancelTimer();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closingProgressDialog();
    }

    /*
         Update Last Sync time into DB table
         */
    private void updatingSyncTime() {
        SyncUtils.updatingSyncTime(SyncSelectionActivity.this, alAssignColl);
    }

    private void onSyncHist() {
        Intent intent = new Intent(this, SyncHistoryActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void responseSuccess(ODataRequestExecution oDataRequestExecution, List<ODataEntity> list, Bundle bundle) {
//        String type = bundle != null ? bundle.getString(Constants.BUNDLE_RESOURCE_PATH) : "";
//        Log.d("responseSuccess", "responseSuccess: " + type);
       /* if (!isBatchReqs) {
            switch (type) {
                case Constants.RouteSchedules:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onRequestSuccess(Operation.Update.getValue(), "");
                        }
                    });
                    break;
                case Constants.CollectionPlan:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onRequestSuccess(Operation.Update.getValue(), "");
                        }
                    });
                    break;
            }
            isBatchReqs = true;
        }*/
    }

    @Override
    public void responseFailed(final ODataRequestExecution request, String s, Bundle bundle) {
        Log.d("SyncError", "responseFailed: " + s);
        /*if (!isBatchReqs) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TraceLog.scoped(this).d(Constants.RequestFailed);
                    if (request != null && request.getResponse() != null) {
                        ODataPayload payload = ((ODataResponseSingle) request.getResponse()).getPayload();
                        if (payload != null && payload instanceof ODataError) {
                            ODataError oError = (ODataError) payload;
                            TraceLog.d(Constants.RequestFailed_status_message + oError.getMessage());
                            try {
                                ODataRequestParamSingle oDataResponseSingle = (ODataRequestParamSingleDefaultImpl) request.getRequest();
                                ODataEntity oDataEntity = (ODataEntity) oDataResponseSingle.getPayload();
                                Constants.Entity_Set.add(oDataEntity.getResourcePath());
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                            LogManager.writeLogError(Constants.Error + " :" + oError.getMessage());
                            Constants.AL_ERROR_MSG.add(oError.getMessage());
                            onRequestError(Operation.Update.getValue(), new OnlineODataStoreException(oError.getMessage()));
                            return;
                        }
                    }
                    onRequestError(Operation.Update.getValue(), null);
                }
            });
            isBatchReqs = true;
        }*/
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
                isUpdateSyncing =true;
                String value = k + 1 + "/" + invKeyValues.length;
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

                String store = ConstantsUtils.getFromDataVault(invKeyValues[k][0].toString(), SyncSelectionActivity.this);
                //Fetch object from data vault
                if (store != null && !TextUtils.isEmpty(store)) {
                    try {
                        JSONObject fetchJsonHeaderObject = new JSONObject(store);
                        this.fetchJsonHeaderObject = fetchJsonHeaderObject;
                        try {
                            if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                                if (fetchJsonHeaderObject.getString(Constants.Status).equalsIgnoreCase("000000")) {
                                    mBoolIsReqResAval = true;
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
                        isUpdateClicked = true;
                        if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                            if (!alAssignColl.contains(Constants.SSINVOICES)) {
                                //                            alAssignColl.add(Constants.SSInvoiceItemDetails);
                                alAssignColl.add(Constants.SSINVOICES);
                            }
                            if (!alAssignColl.contains(Constants.SSSOs)) {
                                alAssignColl.add(Constants.SSSoItemDetails);
                                alAssignColl.add(Constants.SSSOs);
                            }
                            soCount += 1;
                            JSONObject headerObject = Constants.getSOHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                            LogManager.writeLogInfo("Starting SO Post " + k);
                            OnlineManager.createEntity(REPEATABLE_REQUEST_ID, headerObject.toString(), Constants.SSSOs, SyncSelectionActivity.this, SyncSelectionActivity.this);
                            LogManager.writeLogInfo("After SO Post " + k);
                        } else if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.ChannelPartners)) {
                            // preparing entity pending
                            if (!alAssignColl.contains(Constants.ChannelPartners)) {
                                alAssignColl.add(Constants.ChannelPartners);
                                alAssignColl.add(Constants.CPDMSDivisions);
                                alAssignColl.add(Constants.RoutePlans);
                                alAssignColl.add(Constants.RouteSchedulePlans);
                                alAssignColl.add(Constants.RouteScheduleSPs);
                                alAssignColl.add(Constants.RouteScheduleDetails);
                                //                            alAssignColl.add(Constants.CPDocuments);
                            }
                            cpCount += 1;
                            JSONObject headerObject = Constants.getCPHeaderValuesFromJsonObject(fetchJsonHeaderObject,SyncSelectionActivity.this);
                            LogManager.writeLogInfo("Starting Retailer Post " + k);
                            OnlineManager.createEntity(REPEATABLE_REQUEST_ID, headerObject.toString(), Constants.ChannelPartners, SyncSelectionActivity.this, SyncSelectionActivity.this);
                            LogManager.writeLogInfo("After Retailer Post " + k);
                        } else if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.ReturnOrderCreate)) {

                            // preparing entity pending
                            if (!alAssignColl.contains(Constants.SSROs)) {
                                alAssignColl.add(Constants.SSROs);
//                                alAssignColl.add(Constants.SSSoItemDetails);
                            }
                            JSONObject headerObject = Constants.getROHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                            OnlineManager.createEntity(REPEATABLE_REQUEST_ID, headerObject.toString(), Constants.SSROs, this, this);
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
                            OnlineManager.createSSInvoiceEntity(dbHeadTable, arrtable, SyncSelectionActivity.this);
                        } catch (OnlineODataStoreException e) {
                            e.printStackTrace();
                        }*/
                            JSONObject headerObject = Constants.getSSInvoiceHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                            OnlineManager.createEntity(REPEATABLE_REQUEST_ID,headerObject.toString(), Constants.SSINVOICES, SyncSelectionActivity.this, SyncSelectionActivity.this);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        mBoolIsReqResAval = true;
                        LogManager.writeLogError(e.toString());
                        runOnUiThread(this::closingProgressDialog);
                        runOnUiThread(() -> {
                            showAlert(e.getMessage());
                        });
                    }
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.imageViewHome:
                startActivity(new Intent(this, BILMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;*/
        }
    }

    public static class ArrayComarator implements Comparator<String[]> {

        @Override
        public int compare(String s1[], String s2[]) {
            BigInteger i1 = null;
            BigInteger i2 = null;
            try {
                i1 = new BigInteger(s1[0]);
            } catch (NumberFormatException e) {
            }

            try {
                i2 = new BigInteger(s2[0]);
            } catch (NumberFormatException e) {
            }

            if (i1 != null && i2 != null) {
                return i1.compareTo(i2);
            } else {
                return s1[0].compareTo(s2[0]);
            }
        }

    }

    /**
     * This adapter show arrange icons and text in grid view manner.
     */
    public class SyncAdapter extends BaseAdapter {
        Context mContext;

        SyncAdapter(Context c) {
            mContext = c;
        }

        @Override
        public int getCount() {
            int counttemp = 0;
            for (int OriginalStatu : OriginalStatus) {
                if (OriginalStatu == 1) {
                    counttemp++;
                }
            }
            return counttemp;
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int iconposition = TempStatus[position];
            View view;
            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                view = li.inflate(R.layout.retailer_menu_inside, null);
                view.requestFocus();
                TextView tvIconName = (TextView) view.findViewById(R.id.icon_text);
//                tvIconName.setTextColor(getResources().getColor(R.color.icon_text_blue));
                /*if (MSFAApplication.isAWSM && iconName[iconposition].equalsIgnoreCase("Day End Activity")){
                    tvIconName.setText("Log");
                }else{
                    tvIconName.setText(iconName[iconposition]);
                }*/
                tvIconName.setText(iconName[iconposition]);
                ImageView ivIcon = (ImageView) view.findViewById(R.id.ib_must_sell);
                if (iconposition == 0) {
                    ivIcon.setImageResource(R.drawable.ic_sync_black_24dp);
                    ivIcon.setColorFilter(ContextCompat.getColor(SyncSelectionActivity.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String userName = SyncSelectionActivity.this.getSharedPreferences(Constants.PREFS_NAME,0).getString(com.arteriatech.mutils.registration.UtilRegistrationActivity.KEY_username, "");
                            String password = SyncSelectionActivity.this.getSharedPreferences(Constants.PREFS_NAME,0).getString(com.arteriatech.mutils.registration.UtilRegistrationActivity.KEY_password, "");
                            if (userName !=null&&password != null) {
                                RegistrationActivity.validateMetadata(Configuration.APP_ID,userName,password);
                            }
                            if (!Constants.iSAutoSync && !Constants.isBGSync) {
                                syncHistoryType = Constants.Sync_All;
                                try {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AvailableServerAllSync(false);
                                        }
                                    }).start();
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (Constants.iSAutoSync) {
                                    LogManager.writeLogInfo("AutoSync is in progress, Sync cannot be performed");
                                    Constants.showAlert("Auto Sync is in progress, Sync cannot be performed", SyncSelectionActivity.this);
                                } else {
                                    LogManager.writeLogInfo("Background Sync is in progress, Sync cannot be performed");
                                    Constants.showAlert("Background Sync is in progress, Sync cannot be performed", SyncSelectionActivity.this);
                                }
                            }
                        }
                    });
                } else if (iconposition == 1) {
                    ivIcon.setImageResource(R.drawable.ic_sync_black_24dp);
                    ivIcon.setColorFilter(ContextCompat.getColor(SyncSelectionActivity.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                    view.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {

                            if (!Constants.iSAutoSync && !Constants.isBGSync) {
                                isAllSync = false;
                                isUpdate = false;
                                onFreshSync();
                            } else {
                                if (Constants.iSAutoSync) {
                                    LogManager.writeLogInfo("AutoSync is in progress, Sync cannot be performed");
                                    Constants.showAlert("Auto Sync is in progress, Sync cannot be performed", SyncSelectionActivity.this);
                                } else {
                                    LogManager.writeLogInfo("Background Sync is in progress, Sync cannot be performed");
                                    Constants.showAlert("Background Sync is in progress, Sync cannot be performed", SyncSelectionActivity.this);
                                }

                            }
                        }
                    });
                } else if (iconposition == 2) {
                    ivIcon.setImageResource(R.drawable.ic_sync_black_24dp);
                    ivIcon.setColorFilter(ContextCompat.getColor(SyncSelectionActivity.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                    view.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            String userName = SyncSelectionActivity.this.getSharedPreferences(Constants.PREFS_NAME,0).getString(com.arteriatech.mutils.registration.UtilRegistrationActivity.KEY_username, "");
                            String password = SyncSelectionActivity.this.getSharedPreferences(Constants.PREFS_NAME,0).getString(com.arteriatech.mutils.registration.UtilRegistrationActivity.KEY_password, "");
                            if (userName !=null&&password != null) {
                                RegistrationActivity.validateMetadata(Configuration.APP_ID,userName,password);
                            }
                            if (!Constants.iSAutoSync && !Constants.isBGSync) {
                                syncHistoryType = Constants.UpLoad;
                                try {
                                    isAllSync = false;
                                    try {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AvailableServerAllSync(true);
                                            }
                                        }).start();
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (Constants.iSAutoSync) {
                                    LogManager.writeLogInfo("AutoSync is in progress, Sync cannot be performed");
                                    Constants.showAlert("Auto Sync is in progress, Sync cannot be performed", SyncSelectionActivity.this);
                                } else {
                                    LogManager.writeLogInfo("Background Sync is in progress, Sync cannot be performed");
                                    Constants.showAlert("Background Sync is in progress, Sync cannot be performed", SyncSelectionActivity.this);
                                }

                            }
                        }
                    });
                } else if (iconposition == 3) {
                    ivIcon.setImageResource(R.drawable.ic_history_black_24dp);
                    ivIcon.setColorFilter(ContextCompat.getColor(SyncSelectionActivity.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                    view.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            if (!Constants.iSAutoSync && !Constants.isBGSync) {
                                onSyncHist();
                            } else {
                                if (Constants.iSAutoSync) {
                                    LogManager.writeLogInfo("AutoSync is in progress, Sync cannot be performed");
                                    Constants.showAlert("Auto Sync is in progress, Sync cannot be performed", SyncSelectionActivity.this);
                                } else {
                                    LogManager.writeLogInfo("Background Sync is in progress, Sync cannot be performed");
                                    Constants.showAlert("Background Sync is in progress, Sync cannot be performed", SyncSelectionActivity.this);
                                }

                            }
                        }
                    });
                } else if (iconposition == 4) {
                    if (MSFAApplication.isSDA) {
                        ivIcon.setImageResource(R.drawable.restart_2);
//                    ivIcon.setColorFilter(ContextCompat.getColor(SyncSelectionActivity.this, R.color.secondaryColor), android.graphics.PorterDuff.Mode.SRC_IN);
                        view.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                dayEndActivity();
                            }
                        });
                    }
                    if (MSFAApplication.isVAN) {
                        ivIcon.setImageResource(R.drawable.restart_2);
//                    ivIcon.setColorFilter(ContextCompat.getColor(SyncSelectionActivity.this, R.color.secondaryColor), android.graphics.PorterDuff.Mode.SRC_IN);
                        view.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                dayEnd();
                            }
                        });
                    }
                }
                view.setId(position);
            } else {
                view = convertView;
            }
            return view;
        }

    }

    private void dayEnd(){
        Set<String> set = new HashSet<>();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
        set = sharedPreferences.getStringSet(Constants.SampleDisbursement, null);
        if (set != null && !set.isEmpty()) {
            showAlert("Please sync pending invoices to DMS");
        }else {
            String alrtConfMsg = "", alrtNegtiveMsg = "";
            String message = getString(R.string.msg_confirm_day_end);
            alrtConfMsg = getString(R.string.yes);
            alrtNegtiveMsg = getString(R.string.no);
            AlertDialog.Builder alertDialogDayEnd = new AlertDialog.Builder(
                    this, R.style.MyTheme);
            alertDialogDayEnd.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(
                            alrtConfMsg,
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {
                                    dialog.cancel();
                                    String qry = Constants.Attendances + "?$filter=EndDate eq null and StartDate eq datetime'" + UtilConstants.getNewDate() + "' and "+ Constants.SPGUID + " eq guid'" + Constants.getSPGUID() + "'";
                                    try {
                                        OfflineManager.getAttendance(qry);
                                    } catch (OfflineODataStoreException e) {
                                        e.printStackTrace();
                                        ConstantsUtils.printErrorLog(e.getMessage());
                                    }

                                    if(!Constants.MapEntityVal.isEmpty()) {
                                        Constants.MapEntityVal.clear();
                                        syncProgDialog = Constants.showProgressDialog(SyncSelectionActivity.this, "", getString(R.string.gps_progress));
                                        Constants.getLocation(SyncSelectionActivity.this, new LocationInterface() {
                                            @Override
                                            public void location(boolean status, LocationModel locationModel, String errorMsg, int errorCode) {
                                                closingProgressDialog();
                                                if (status) {
                                                    if (ConstantsUtils.isAutomaticTimeZone(SyncSelectionActivity.this)) {
                                                        String mStrPopUpText = getString(R.string.msg_update_end);
                                                        onSaveClose();
                                                    } else {
                                                        ConstantsUtils.showAutoDateSetDialog(SyncSelectionActivity.this);
                                                    }
                                                }
                                            }
                                        });
                                    }else {
                                        Constants.MapEntityVal.clear();
                                        closingProgressDialog();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showAlert("Day End Activity completed already");
                                            }
                                        });
                                    }
                                }

                            })
                    .setNegativeButton(alrtNegtiveMsg,
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {
                                    dialog.cancel();
                                }

                            });
            alertDialogDayEnd.show();
        }
    }

    /*Ends day*/
    private void onSaveClose() {
        try {
            new ClosingDate().execute();
        } catch (Exception e) {
            e.printStackTrace();
            ConstantsUtils.printErrorLog(e.getMessage());
        }
    }

    /*AsyncTask to Close Attendance for day*/
    private class ClosingDate extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            syncProgDialog = new ProgressDialog(SyncSelectionActivity.this, R.style.ProgressDialogTheme);
            syncProgDialog.setMessage(getString(R.string.msg_update_end));
            syncProgDialog.setCancelable(false);
            syncProgDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String mStrAttendanceId = "";
            try {
                Thread.sleep(1000);

                Constants.MapEntityVal.clear();

                String qry = Constants.Attendances + "?$filter=EndDate eq null and StartDate eq datetime'" + UtilConstants.getNewDate() + "' and "+ Constants.SPGUID + " eq guid'" + Constants.getSPGUID() + "'";
                try {
                    mStrAttendanceId = OfflineManager.getAttendance(qry);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                    ConstantsUtils.printErrorLog(e.getMessage());
                }

                if(!Constants.MapEntityVal.isEmpty()) {
                    Hashtable hashTableAttendanceValues;

                    hashTableAttendanceValues = new Hashtable();
                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);

                    String loginIdVal = sharedPreferences.getString(UtilRegistrationActivity.KEY_username, "");
                    //noinspection unchecked
                    hashTableAttendanceValues.put(Constants.LOGINID, loginIdVal);
                    //noinspection unchecked
                    hashTableAttendanceValues.put(Constants.AttendanceGUID, Constants.MapEntityVal.get(Constants.AttendanceGUID));
                    //noinspection unchecked
                    hashTableAttendanceValues.put(Constants.StartDate, Constants.MapEntityVal.get(Constants.StartDate));
                    //noinspection unchecked
                    hashTableAttendanceValues.put(Constants.StartTime, Constants.MapEntityVal.get(Constants.StartTime));
                    //noinspection unchecked
                    hashTableAttendanceValues.put(Constants.StartLat, Constants.MapEntityVal.get(Constants.StartLat));
                    //noinspection unchecked
                    hashTableAttendanceValues.put(Constants.StartLong, Constants.MapEntityVal.get(Constants.StartLong));
                    try {
                        //noinspection unchecked
                        hashTableAttendanceValues.put(Constants.EndLat, BigDecimal.valueOf(UtilConstants.round(UtilConstants.latitude, 12)));
                        //noinspection unchecked
                        hashTableAttendanceValues.put(Constants.EndLong, BigDecimal.valueOf(UtilConstants.round(UtilConstants.longitude, 12)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //noinspection unchecked
                    hashTableAttendanceValues.put(Constants.EndDate, UtilConstants.getNewDateTimeFormat());

                    hashTableAttendanceValues.put(Constants.SPGUID, Constants.getSPGUID());

                    hashTableAttendanceValues.put(Constants.SetResourcePath, Constants.MapEntityVal.get(Constants.SetResourcePath));

                    if (Constants.MapEntityVal.get(Constants.Etag) != null) {
                        hashTableAttendanceValues.put(Constants.Etag, Constants.MapEntityVal.get(Constants.Etag));
                    } else {
                        hashTableAttendanceValues.put(Constants.Etag, "");
                    }

                    hashTableAttendanceValues.put(Constants.Remarks, Constants.MapEntityVal.get(Constants.Remarks));
                    hashTableAttendanceValues.put(Constants.AttendanceTypeH1, Constants.MapEntityVal.get(Constants.AttendanceTypeH1));
                    hashTableAttendanceValues.put(Constants.AttendanceTypeH2, Constants.MapEntityVal.get(Constants.AttendanceTypeH2));

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
                    hashTableAttendanceValues.put(Constants.EndTime, oDataDuration);

                    //noinspection unchecked

                    SharedPreferences sharedPreferencesVal = getSharedPreferences(Constants.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferencesVal.edit();
                    editor.putInt("VisitSeqId", 0);
                    editor.commit();

                    try {
                        //noinspection unchecked
                        OfflineManager.updateAttendance(hashTableAttendanceValues, SyncSelectionActivity.this, SyncSelectionActivity.this);
                    } catch (OfflineODataStoreException e) {
                        e.printStackTrace();
                        ConstantsUtils.printErrorLog(e.getMessage());
                    }
                }else {
                    closingProgressDialog();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showAlert("Day End Activity completed already");
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                ConstantsUtils.printErrorLog(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private void dayEndActivity() {

    }

    public class LoadingData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            syncProgDialog = new ProgressDialog(SyncSelectionActivity.this, R.style.ProgressDialogTheme);
            syncProgDialog.setTitle("Loading..");
//            setTimer("01");
            messageProgess = getString(R.string.msg_sync_progress_msg_plz_wait);
            syncProgDialog.setMessage(messageProgess);
            syncProgDialog.setCancelable(true);
//            syncProgDialog.setCanceledOnTouchOutside(false);
//            syncProgDialog.show();

            syncProgDialog
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface Dialog) {
                            UtilConstants.dialogBoxWithCallBack(SyncSelectionActivity.this, "", getString(R.string.do_want_cancel_sync), getString(R.string.yes)
                                    , getString(R.string.no), false, new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                    if (clickedStatus) {
                                        dialogCancelled = true;
                                        onBackPressed();
                                    } else {
                                        displayProgressDialog();
                                    }
                                }
                            });
//                            AlertDialog.Builder builder = new AlertDialog.Builder(
//                                    SyncSelectionActivity.this, R.style.MaterialAlertDialog);
//                            builder.setMessage(R.string.do_want_cancel_sync)
//                                    .setCancelable(false)
//                                    .setPositiveButton(
//                                            R.string.yes,
//                                            (Dialog1, id) -> {
//                                                dialogCancelled = true;
//                                                onBackPressed();
//                                            })
//                                    .setNegativeButton(
//                                            R.string.no,
//                                            (Dialog12, id) -> displayProgressDialog());
//                            builder.show();
                        }
                    });
        }

        @Override
        protected Void doInBackground(Void... params) {
//            Constants.printLogInfo("check store is opened or not");
            try {
                if (!OfflineManager.isOfflineStoreOpen()) {
                    //                Constants.printLogInfo("check store is failed");
                    try {
//                        OfflineManager.openOfflineStore(SyncSelectionActivity.this, SyncSelectionActivity.this);
                        OfflineManager.openOfflineStoreInternal(SyncSelectionActivity.this, SyncSelectionActivity.this);
                        Constants.isStoreClosed = false;
                        assignCollToArrayList();
                        //                Constants.printLogInfo("check store is opened");
                        try {
                            try {
                                refguid = GUID.newRandom();
                                SyncUtils.updatingSyncStartTime(SyncSelectionActivity.this, Constants.Sync_All, Constants.StartSync, refguid.toString().toUpperCase());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            isAllSync = true;
                            OfflineManager.refreshStoreSync(getApplicationContext(), SyncSelectionActivity.this, Constants.ALL, "");
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                            isAllSyncClicked = false;
                        }
                    } catch (OfflineODataStoreException e) {
                        LogManager.writeLogError(Constants.error_txt + e.getMessage());
                        isAllSyncClicked = false;
                    }
                } else {
                    Constants.isStoreClosed = false;
                    assignCollToArrayList();
                    //                Constants.printLogInfo("check store is opened");
                    try {
                        try {
                            refguid = GUID.newRandom();
                            SyncUtils.updatingSyncStartTime(SyncSelectionActivity.this, Constants.Sync_All, Constants.StartSync, refguid.toString().toUpperCase());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        OfflineManager.refreshStoreSync(getApplicationContext(), SyncSelectionActivity.this, Constants.ALL, "");
                    } catch (OfflineODataStoreException e) {
                        e.printStackTrace();
                        isAllSyncClicked = false;
                    }
                }
            } catch (Throwable e) {
                isAllSyncClicked = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    public class AsyncPostOfflineData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            if (syncProgDialog == null) {
//                syncProgDialog = new ProgressDialog(SyncSelectionActivity.this, R.style.ProgressDialogTheme);
//                syncProgDialog.setTitle("Loading..");
//            }
            if (countDownTimer == null) {
                setTimer("02");
            }
            if(MSFAApplication.isVAN){
                messageProgess = "Uploading Attendence. Please wait...";
            }else {
                messageProgess = "Uploading Retailer, Sync, Visit. Please wait...";
            }
            syncProgDialog.setMessage(messageProgess);
            syncProgDialog.setCancelable(false);
            syncProgDialog.setCanceledOnTouchOutside(false);
//            syncProgDialog.show();
            syncProgDialog
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface Dialog) {
                            UtilConstants.dialogBoxWithCallBack(SyncSelectionActivity.this, "", getString(R.string.do_want_cancel_sync), getString(R.string.yes)
                                    , getString(R.string.no), false, new DialogCallBack() {
                                        @Override
                                        public void clickedStatus(boolean clickedStatus) {
                                            if (clickedStatus) {
                                                dialogCancelled = true;
                                                onBackPressed();
                                            } else {
                                                displayProgressDialog();
                                            }
                                        }
                                    });
//                            AlertDialog.Builder builder = new AlertDialog.Builder(
//                                    SyncSelectionActivity.this, R.style.MyTheme);
//                            builder.setMessage(R.string.do_want_cancel_sync)
//                                    .setCancelable(false)
//                                    .setPositiveButton(
//                                            R.string.yes,
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(
//                                                        DialogInterface Dialog,
//                                                        int id) {
//                                                    dialogCancelled = true;
//                                                    onBackPressed();
//                                                }
//                                            })
//                                    .setNegativeButton(
//                                            R.string.no,
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(
//                                                        DialogInterface Dialog,
//                                                        int id) {
//
//                                                    displayProgressDialog();
//
//                                                }
//                                            });
//                            builder.show();
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
                            if (concatCollectionStr.contains("ChannelPartners")) {
                                isUpdate = true;
                            }
                            OfflineManager.flushQueuedRequests(SyncSelectionActivity.this, concatFlushCollStr);
                            isUpdateClicked = false;
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (ODataException e) {
                    e.printStackTrace();
                    isUpdateClicked = false;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                isUpdateClicked = false;
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
//            syncProgDialog = new ProgressDialog(SyncSelectionActivity.this, R.style.ProgressDialogTheme);
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
            syncProgDialog
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface Dialog) {
                            UtilConstants.dialogBoxWithCallBack(SyncSelectionActivity.this, "", getString(R.string.do_want_cancel_sync), getString(R.string.yes)
                                    , getString(R.string.no), false, new DialogCallBack() {
                                        @Override
                                        public void clickedStatus(boolean clickedStatus) {
                                            if (clickedStatus) {
                                                dialogCancelled = true;
                                                onBackPressed();
                                            } else {
                                                displayProgressDialog();
                                            }
                                        }
                                    });
//                            AlertDialog.Builder builder = new AlertDialog.Builder(
//                                    SyncSelectionActivity.this, R.style.MyTheme);
//                            builder.setMessage(R.string.do_want_cancel_sync)
//                                    .setCancelable(false)
//                                    .setPositiveButton(
//                                            R.string.yes,
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(
//                                                        DialogInterface Dialog,
//                                                        int id) {
//                                                    dialogCancelled = true;
//                                                    onBackPressed();
//                                                }
//                                            })
//                                    .setNegativeButton(
//                                            R.string.no,
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(
//                                                        DialogInterface Dialog,
//                                                        int id) {
//
//                                                    try {
//                                                        syncProgDialog
//                                                                .show();
//                                                        syncProgDialog
//                                                                .setCancelable(true);
//                                                        syncProgDialog
//                                                                .setCanceledOnTouchOutside(false);
//                                                    } catch (Exception e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                    dialogCancelled = false;
//
//                                                }
//                                            });
//                            builder.show();
                        }
                    });
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
                tokenFlag = false;

                Constants.x_csrf_token = "";
                Constants.ErrorCode = 0;
                Constants.ErrorNo = 0;
                Constants.ErrorName = "";
                Constants.IsOnlineStoreFailed = false;
                Constants.IsOnlineStoreStarted = false;
                mBoolIsReqResAval = true;
                LogManager.writeLogInfo("Posting data to backend started");
                try {
                    readValuesFromDataVault();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(SyncSelectionActivity.this::closingProgressDialog);

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return onlineStoreOpen = true;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {
                closingProgressDialog();
                syncProgDialog = null;

                if (!onlineStoreOpen) {
                    if (Constants.ErrorNo == Constants.Network_Error_Code && Constants.ErrorName.equalsIgnoreCase(Constants.NetworkError_Name)) {
                        Constants.showAlert(getString(R.string.data_conn_lost_during_sync_error_code, Constants.ErrorNo + ""), SyncSelectionActivity.this);

                    } else if (Constants.ErrorNo == Constants.UnAuthorized_Error_Code && Constants.ErrorName.equalsIgnoreCase(Constants.NetworkError_Name)) {
                        Constants.showAlert(getString(R.string.auth_fail_plz_contact_admin, Constants.ErrorNo + ""), SyncSelectionActivity.this);
                    } else if (Constants.ErrorNo == Constants.Comm_Error_Code) {
                        Constants.showAlert(getString(R.string.data_conn_lost_during_sync_error_code, Constants.ErrorNo + ""), SyncSelectionActivity.this);
                    } else {
                        Constants.showAlert(getString(R.string.data_conn_lost_during_sync_error_code, Constants.ErrorNo + ""), SyncSelectionActivity.this);
                    }
                } else if (!tokenFlag) {
                    Constants.displayMsgINet(Constants.ErrorNo_Get_Token, SyncSelectionActivity.this);
                } else if (Constants.x_csrf_token == null || Constants.x_csrf_token.equalsIgnoreCase("")) {
                    Constants.showAlert(getString(R.string.data_conn_lost_during_sync_error_code, -2 + ""), SyncSelectionActivity.this);
                }
            }
        }
    }

    private class OpenOfflineStore extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            syncProgDialog = new ProgressDialog(SyncSelectionActivity.this, R.style.ProgressDialogTheme);
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
//                            OfflineManager.openOfflineStore(SyncSelectionActivity.this, SyncSelectionActivity.this);
                            OfflineManager.openOfflineStoreInternal(SyncSelectionActivity.this, SyncSelectionActivity.this);
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

    private void showAlert(String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(SyncSelectionActivity.this, R.style.MyTheme);
//        builder.setMessage(message)
//                .setCancelable(true)
//                .setPositiveButton(R.string.OK, (Dialog, id) -> {
//                    Dialog.cancel();
//                });
//        builder.show();

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SyncSelectionActivity.this, R.style.DialogTheme);
                    String timeStamp = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm aa", new Date());
                    builder.setTitle(timeStamp);
//            builder.setMessage(message+"\n\n"+timeStamp+"").setCancelable(false)
                    builder.setMessage(message).setCancelable(false)
                            .setPositiveButton(getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    class LoadData extends AsyncTask<Void, Void, Void> {
        Context context;

        public LoadData(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                isLocalSync = false;
                String rollID = ConstantsUtils.getAuthInformation(SyncSelectionActivity.this);
                if(rollID.equalsIgnoreCase(Constants.PSM)) {
                   // DashBoardPresenter.getBeatsRemoteData(context, DashBoard.distributorBean.getCPGUID());
                }else {


                }
                while (!isLocalSync) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closingProgressDialog();
                        if (mError == 0) {
                            ConstantsUtils.startAutoSync(SyncSelectionActivity.this, false);
                            Constants.dialogBoxWithCallBack(SyncSelectionActivity.this, "", getString(R.string.msg_sync_successfully_completed), getString(R.string.ok), "", false, new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean b) {
                                    //AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, SyncSelectionActivity.this, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE);
                                }
                            });
                        } else {
                            if (mErrorMsg.equalsIgnoreCase("")) {
                                Constants.showAlert(getString(R.string.error_occured_during_post), SyncSelectionActivity.this);
                            } else {
                                Constants.customAlertDialogWithScroll(SyncSelectionActivity.this, mErrorMsg);
                            }
                        }
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            closingProgressDialog();
            if (mError == 0) {
                ConstantsUtils.startAutoSync(SyncSelectionActivity.this, false);
                Constants.dialogBoxWithCallBack(SyncSelectionActivity.this, "", getString(R.string.msg_sync_successfully_completed), getString(R.string.ok), "", false, new DialogCallBack() {
                    @Override
                    public void clickedStatus(boolean b) {
                       // AppUpgradeConfig.getUpdateAvlUsingVerCode(OfflineManager.offlineStore, SyncSelectionActivity.this, BuildConfig.APPLICATION_ID, false, Constants.APP_UPGRADE_TYPESET_VALUE);
                    }
                });
            } else {
                if (mErrorMsg.equalsIgnoreCase("")) {
                    Constants.showAlert(getString(R.string.error_occured_during_post), SyncSelectionActivity.this);
                } else {
                    Constants.customAlertDialogWithScroll(SyncSelectionActivity.this, mErrorMsg);
                }
            }
        }
    }

    private void postOfflineData() {
        try {
            if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                    try {
                        if(MSFAApplication.isVAN){
                            messageProgess = "Uploading Attendence. Please wait...";
                        }else {
                            messageProgess = "Uploading Retailer, Sync, Visit. Please wait...";
                        }
                        new AsyncPostOfflineData().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    closingProgressDialog();
                    Constants.showAlert(getString(R.string.data_conn_lost_during_sync), SyncSelectionActivity.this);
                }
            } else {
                if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                    if (!alAssignColl.contains(Constants.ConfigTypsetTypeValues))
                        alAssignColl.add(Constants.ConfigTypsetTypeValues);
                    messageProgess = "Refreshing data. Please wait...";
                    concatCollectionStr = UtilConstants.getConcatinatinFlushCollectios(alAssignColl);
                    new RefreshAsyncTask(SyncSelectionActivity.this, concatCollectionStr, this).execute();
                } else {
                    closingProgressDialog();
                    Constants.showAlert(getString(R.string.data_conn_lost_during_sync), SyncSelectionActivity.this);
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
                            if (loadingData != null)
                                loadingData.cancel(true);
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    SyncSelectionActivity.this, R.style.MyTheme);
                            String timeStamp1 = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm aa", new Date());
                            builder.setTitle(timeStamp1);
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
                            if (asyncTask != null)
                                asyncTask.cancel(true);
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    SyncSelectionActivity.this, R.style.MyTheme);
                            builder.setMessage(R.string.error_sync_upload_msg)
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

    public void removeFromDatavault(Context context){
        Set<String> set = new HashSet<>();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
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
    public static void removeSavedInvoices(Context context,List invoiceList){

    }

}

package com.arteriatech.ss.msec.iscan.v4.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.AvailableServer;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.asyncTask.GetSessionIdAsyncTask;
import com.arteriatech.ss.msec.iscan.v4.asyncTask.SessionIDAsyncTask;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.common.MyUtils;
import com.arteriatech.ss.msec.iscan.v4.interfaces.AsyncTaskCallBack;
import com.arteriatech.ss.msec.iscan.v4.mbo.ErrorBean;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OnlineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.Operation;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.AsyncTaskCallBackInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity;
import com.arteriatech.ss.msec.iscan.v4.mutils.store.OnlineODataInterface;
import com.arteriatech.ss.msec.iscan.v4.registration.Configuration;
import com.sap.client.odata.v4.ChangeSet;
import com.sap.client.odata.v4.DataQuery;
import com.sap.client.odata.v4.DataService;
import com.sap.client.odata.v4.EntityKey;
import com.sap.client.odata.v4.EntitySet;
import com.sap.client.odata.v4.EntityType;
import com.sap.client.odata.v4.EntityValue;
import com.sap.client.odata.v4.EntityValueList;
import com.sap.client.odata.v4.GuidValue;
import com.sap.client.odata.v4.OnlineODataProvider;
import com.sap.client.odata.v4.Property;
import com.sap.client.odata.v4.QueryResult;
import com.sap.client.odata.v4.RequestBatch;
import com.sap.client.odata.v4.core.AndroidSystem;
import com.sap.client.odata.v4.core.GUID;
import com.sap.client.odata.v4.http.SDKHttpHandler;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.HttpMethod;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.httpc.listeners.ICommunicationErrorListener;
import com.sap.smp.client.httpc.listeners.IConversationFlowListener;
import com.sap.smp.client.httpc.listeners.IResponseListener;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.exception.ODataParserException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import static com.arteriatech.ss.msec.iscan.v4.common.Constants.PREFS_NAME;
import static com.arteriatech.ss.msec.iscan.v4.common.Constants.REPEATABLE_REQUEST_ID;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.USER_NAME_DEVICE_ID;
import static com.arteriatech.ss.msec.iscan.v4.registration.Configuration.USER_PASSWORD_DEVICE_ID;
import static com.arteriatech.ss.msec.iscan.v4.registration.RegistrationActivity.registrationModel;

/**
 * Created by e10769 on 12-Apr-18.
 */

public class OnlineManager {


    public static boolean openOnlineStore(Context context) throws OnlineODataStoreException {
       /* SDKHttpHandler sdkHttpHandler = new SDKHttpHandler(context, conversationManager);
        OnlineODataProvider serviceProvider = new OnlineODataProvider("flightService","https://serverHost:serverPort/yourApplicationID");
        serviceProvider.getNetworkOptions().setHttpHandler(sdkHttpHandler);
        FlightService flightService = new FlightService(serviceProvider);
        flightService.getTravelagencyCollection();*/

       
       /* OnlineStoreListener openListener = OnlineStoreListener.getInstance();
        URL url = new URL("");
        CredentialsProvider credProvider = CredentialsProvider.getInstance(context);
        HttpConversationManager manager = new CommonAuthFlowsConfigurator(context).supportBasicAuthUsing(credProvider).configure(new HttpConversationManager(context));
        XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(context);
        XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(context,
                requestFilter);
        manager.addFilter(requestFilter);
        manager.addFilter(responseFilter);
        OnlineODataStore.OnlineStoreOptions onlineOptions = new OnlineODataStore.OnlineStoreOptions();
            onlineOptions.forceMetadataDownload=true;
        OnlineODataStore.open(context, url, manager, openListener, onlineOptions);*/

        //OnlineOpenListener implements OpenListener interface
        //Listener to be invoked when the opening process of an OnlineODataStore object finishes
        /*Constants.onlineStore = null;
        OnlineStoreListener.instance = null;
        Constants.IsOnlineStoreFailed = false;
        Constants.IsOnlineStoreStarted = true;
        try {
            Constants.printLogInfo("Get online store instance");
            OnlineStoreListener openListener = OnlineStoreListener.getInstance();
            Constants.printLogInfo("online store instance assigned");
            LogonCoreContext lgCtx = LogonCore.getInstance().getLogonContext();
            Constants.printLogInfo("logon core context instance assigned :"+lgCtx);

            //The logon configurator uses the information obtained in the registration
            IManagerConfigurator configurator = LogonUIFacade.getInstance().getLogonConfigurator(context);
            HttpConversationManager manager = new HttpConversationManager(context);
            configurator.configure(manager);

           *//* OnlineODataStore.OnlineStoreOptions onlineOptions = new OnlineODataStore.OnlineStoreOptions();
            onlineOptions.cacheEncryptionKey = Constants.EncryptKey;
            if (ConstantsUtils.getFirstTimeRun(context)==2){
                onlineOptions.forceMetadataDownload=true;
            }else {
                onlineOptions.forceMetadataDownload = false;
            }*//*

            //XCSRFTokenRequestFilter implements IRequestFilter
            //Request filter that is allowed to preprocess the request before sending
            XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgCtx);
            XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(context,
                    requestFilter);
            manager.addFilter(requestFilter);
            manager.addFilter(responseFilter);

            try {
                String endPointURL = lgCtx.getAppEndPointUrl();
//                Constants.printLogInfo("end point url "+endPointURL);
//                Constants.printLogInfo("appid "+lgCtx.getAppId());
//                Constants.printLogInfo("connection id "+lgCtx.getConnId());
//                Constants.printLogInfo("resource path "+lgCtx.getResourcePath());
//                Constants.printLogInfo("backend user "+lgCtx.getBackendUser());
//                Constants.printLogInfo("host name "+lgCtx.getHost());



                URL url = new URL(endPointURL);
                //Method to open a new online store asynchronously
                Constants.printLogInfo("request for open online store");
                OnlineODataStore.open(context, url, manager, openListener, null);

                Constants.printLogInfo("request for open online store completed");
                //            openListener.waitForCompletion();

                if (openListener.getError() != null) {
                    Constants.printLog("open online store ended with error");
                    throw openListener.getError();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Constants.printLog("open online store ended with exception "+e.getMessage());
                throw new OnlineODataStoreException(e);
            }
            //Check if OnlineODataStore opened successfully

            while (!Constants.IsOnlineStoreFailed) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Constants.IsOnlineStoreFailed = false;


            if (Constants.onlineStore != null) {
                return true;
            } else {
                return false;
            }
        } catch (OnlineODataStoreException e) {
            e.printStackTrace();
            return false;
        }*/
        return false;
    }
    public static void requestQuery(final OnlineODataInterface onlineODataInterface, final Bundle bundle, final Context mContext) {
        String resourcePath = "";
        String sessionId = "";
        boolean isSessionRequired = false;
        int sessionType = 0;
        try {
            if (bundle == null) {
//            throw new IllegalArgumentException("bundle is null");
                if (onlineODataInterface != null)
                    onlineODataInterface.responseFailed(null, "bundle is null", bundle);
            } else {
                resourcePath = bundle.getString(Constants.BUNDLE_RESOURCE_PATH, "");
                sessionId = bundle.getString(Constants.BUNDLE_SESSION_ID, "");
                isSessionRequired = bundle.getBoolean(Constants.BUNDLE_SESSION_REQUIRED, false);
                sessionType = bundle.getInt(Constants.BUNDLE_SESSION_TYPE, 0);
            }
            if (TextUtils.isEmpty(resourcePath)) {
                if (onlineODataInterface != null)
                    onlineODataInterface.responseFailed(null, "resource path is null", bundle);
            } else {
                final Map<String, String> createHeaders = new HashMap<String, String>();
                if (isSessionRequired) {
                    if (!TextUtils.isEmpty(sessionId)) {
                        if (sessionType == ConstantsUtils.SESSION_HEADER) {
                            createHeaders.put(Constants.arteria_session_header, sessionId);
                        } else if (sessionType == ConstantsUtils.SESSION_QRY) {
                            resourcePath = getSessionResourcePath(resourcePath, sessionId);
                        } else if (sessionType == ConstantsUtils.SESSION_QRY_HEADER) {
                            createHeaders.put(Constants.arteria_session_header, sessionId);
                            resourcePath = getSessionResourcePath(resourcePath, sessionId);
                        }
                        requestScheduled(resourcePath, createHeaders, onlineODataInterface, bundle);
                    } else {
                        final String finalResourcePath = resourcePath;
                        final int finalsessionType = sessionType;
                        final Bundle finalBundle = bundle;
                        new SessionIDAsyncTask(mContext, new AsyncTaskCallBackInterface<ErrorBean>() {
                            @Override
                            public void asyncResponse(boolean status, ErrorBean errorBean, String values) {
                                String resourcePath = finalResourcePath;
                                if (status) {
                                    if (UtilConstants.isNetworkAvailable(mContext)) {
                                        if (finalsessionType == ConstantsUtils.SESSION_HEADER) {
                                            createHeaders.put(Constants.arteria_session_header, values);
                                        } else if (finalsessionType == ConstantsUtils.SESSION_QRY) {
//                                            resourcePath = String.format(resourcePath, values);
                                            resourcePath = getSessionResourcePath(resourcePath, values);
                                        } else if (finalsessionType == ConstantsUtils.SESSION_QRY_HEADER) {
                                            createHeaders.put(Constants.arteria_session_header, values);
                                            resourcePath = getSessionResourcePath(resourcePath, values);
                                        }
                                        try {
                                            bundle.putString(Constants.BUNDLE_SESSION_ID, values);
                                            requestScheduled(resourcePath, createHeaders, onlineODataInterface, finalBundle);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            if (onlineODataInterface != null)
                                                onlineODataInterface.responseFailed(null, e.getMessage(), finalBundle);
                                        }
                                    } else {
                                        if (onlineODataInterface != null)
                                            onlineODataInterface.responseFailed(null, mContext.getString(R.string.msg_no_network), finalBundle);
                                    }
                                } else {
                                    if (onlineODataInterface != null)
                                        onlineODataInterface.responseFailed(null, values, finalBundle);
                                }
                            }
                        }).execute();
                    }
                } else {
                    requestScheduled(resourcePath, createHeaders, onlineODataInterface, bundle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (onlineODataInterface != null)
                onlineODataInterface.responseFailed(null, e.getMessage(), bundle);
        }
    }

    private static void requestScheduled(String resourcePath, Map<String, String> createHeaders, OnlineODataInterface onlineODataInterface, Bundle bundle) {
       /* OnlineStoreListener openListener = OnlineStoreListener.getInstance();
        OnlineODataStore store = openListener.getStore();
        if (store != null) {
            boolean isTechnicalCacheEnable = false;
            if (store.isOpenCache()) {
                if (bundle != null)
                    isTechnicalCacheEnable = bundle.getBoolean(UtilConstants.BUNDLE_READ_FROM_TECHNICAL_CACHE, false);
                store.setPassive(isTechnicalCacheEnable);
            } else {
                if (bundle != null)
                    bundle.putBoolean(UtilConstants.BUNDLE_READ_FROM_TECHNICAL_CACHE, false);
            }
            OnlineRequestListeners getOnlineRequestListener = new OnlineRequestListeners(onlineODataInterface, bundle);
            scheduledReqEntity(resourcePath, getOnlineRequestListener, createHeaders, store);

        } else {
            throw new IllegalArgumentException("Store not opened");
        }*/
    }
/*
    private static ODataRequestExecution scheduledReqEntity(String resourcePath, ODataRequestListener listener, Map<String, String> options, OnlineODataStore store) throws ODataContractViolationException {
        if (TextUtils.isEmpty(resourcePath)) {
            throw new IllegalArgumentException("resourcePath is null");
        } else if (listener == null) {
            throw new IllegalArgumentException("listener is null");
        } else {
            ODataRequestParamSingleDefaultImpl requestParam = new ODataRequestParamSingleDefaultImpl();
            requestParam.setMode(ODataRequestParamSingle.Mode.Read);
            requestParam.setResourcePath(resourcePath);
            requestParam.setOptions(options);
            requestParam.getCustomHeaders().putAll(options);

            return store.scheduleRequest(requestParam, listener);
        }
    }*/
    private static String getSessionResourcePath(String resourcePath, String sessionId) {
        if (resourcePath.contains("%1$s")) {
            resourcePath = String.format(resourcePath, sessionId);
        } else if (resourcePath.contains("?")) {
            resourcePath = resourcePath + "+and+LoginID+eq+'" + sessionId + "'";
        } else {
            resourcePath = resourcePath + "?$filter=LoginID+eq+'" + sessionId + "'";
        }
        return resourcePath;
    }


   /* public static void createFeedBack(Hashtable<String, String> table, ArrayList<HashMap<String, String>> itemtable, UIListener uiListener, Context mContext) {
        OnlineRequestListener onlineReqListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);
        SharedPreferences sharedPref = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        if (TextUtils.isEmpty(sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))){
            createCSRFToken(mContext, iReceiveEvent -> {
                if (iReceiveEvent.getResponseStatusCode() == 200) {
                    createSOEntity(table, itemtable, uiListener, mContext);
                }
            });
        }else {
            HttpConversationManager manager = new HttpConversationManager(mContext);
            // Create the conversation.
            manager.create(Uri.parse(MyUtils.getDefaultOnlineQryURL() + Constants.Feedbacks))
//                .addHeader("Content-Type", "application/xml")
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Content-Type", "multipart/mixed")
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                    .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                    .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                    .addHeader("Accept", "application/json")
                    .setMethod(HttpMethod.POST)
                    .setRequestListener(event -> {
                        JSONObject jsonHeaderObject = new JSONObject();
                        try {
                            jsonHeaderObject.put(Constants.NetPrice, table.get(Constants.NetPrice));
                            jsonHeaderObject.put(Constants.GrossAmt, table.get(Constants.GrossAmt));
                            jsonHeaderObject.put(Constants.OrderType, table.get(Constants.OrderType));
                            jsonHeaderObject.put(Constants.OrderTypeDesc, table.get(Constants.OrderTypeDesc));
                            jsonHeaderObject.put(Constants.Incoterm1, table.get(Constants.Incoterm1));
                            jsonHeaderObject.put(Constants.Incoterm1Desc, table.get(Constants.Incoterm1Desc));
                            jsonHeaderObject.put(Constants.CPTypeDesc, table.get(Constants.CPTypeDesc));
                            jsonHeaderObject.put(Constants.DmsDivision, table.get(Constants.DmsDivision));
                            jsonHeaderObject.put(Constants.CPNo, table.get(Constants.CPNo));
                            jsonHeaderObject.put(Constants.CPName, table.get(Constants.CPName));
                            jsonHeaderObject.put(Constants.CPGUID, table.get(Constants.CPGUID));
                            jsonHeaderObject.put(Constants.SPNo, table.get(Constants.SPNo));
                            jsonHeaderObject.put(Constants.SSSOGuid, table.get(Constants.SSSOGuid));
                            jsonHeaderObject.put(Constants.CPType, table.get(Constants.CPType));
                            jsonHeaderObject.put(Constants.TestRun, table.get(Constants.TestRun));
                            jsonHeaderObject.put(Constants.SoldToCPGUID, table.get(Constants.SoldToCPGUID));
                            jsonHeaderObject.put(Constants.SoldToId, table.get(Constants.SoldToId));
                            jsonHeaderObject.put(Constants.SoldToUID, table.get(Constants.SoldToUID));
                            jsonHeaderObject.put(Constants.SoldToType, table.get(Constants.SoldToType));
                            jsonHeaderObject.put(Constants.DmsDivisionDesc, table.get(Constants.DmsDivisionDesc));
                            jsonHeaderObject.put(Constants.Currency, table.get(Constants.Currency));
                            jsonHeaderObject.put(Constants.FromCPGUID, table.get(Constants.FromCPGUID));
                            jsonHeaderObject.put(Constants.FromCPName, table.get(Constants.FromCPName));
                            jsonHeaderObject.put(Constants.FromCPTypId, table.get(Constants.FromCPTypId));
                            jsonHeaderObject.put(Constants.SPGUID, table.get(Constants.SPGUID));
                            jsonHeaderObject.put(Constants.FirstName, table.get(Constants.FirstName));
                            JSONArray jsonArray = new JSONArray();
                            for (int incrementVal = 0; incrementVal < itemtable.size(); incrementVal++) {

                                HashMap<String, String> singleRow = itemtable.get(incrementVal);
                                if (!singleRow.get(Constants.IsfreeGoodsItem).equalsIgnoreCase("X")) {
                                    JSONObject itemObject = new JSONObject();
                                    itemObject.put(Constants.SSSOItemGUID, singleRow.get(Constants.SSSOItemGUID));
                                    itemObject.put(Constants.SSSOGuid, singleRow.get(Constants.SSSOGuid));
                                    itemObject.put(Constants.ItemNo, singleRow.get(Constants.ItemNo));
                                    itemObject.put(Constants.MaterialNo, singleRow.get(Constants.MaterialNo));
                                    itemObject.put(Constants.MaterialDesc, singleRow.get(Constants.MaterialDesc));
                                    itemObject.put(Constants.OrderMatGrp, singleRow.get(Constants.OrderMatGrp));
                                    itemObject.put(Constants.OrderMatGrpDesc, singleRow.get(Constants.OrderMatGrpDesc));
                                    itemObject.put(Constants.Currency, singleRow.get(Constants.Currency));
                                    itemObject.put(Constants.Uom, singleRow.get(Constants.Uom));
                                    itemObject.put(Constants.NetPrice, singleRow.get(Constants.NetPrice));
                                    itemObject.put(Constants.MRP, singleRow.get(Constants.MRP));
                                    itemObject.put(Constants.UnitPrice, singleRow.get(Constants.UnitPrice));
                                    itemObject.put(Constants.Quantity, singleRow.get(Constants.Quantity));
                                    itemObject.put(Constants.PriDiscount, singleRow.get(Constants.PriDiscount));
                                    itemObject.put(Constants.SecDiscount, singleRow.get(Constants.SecDiscount));
                                    itemObject.put(Constants.CashDiscount, singleRow.get(Constants.CashDiscount));
                                    itemObject.put(Constants.PrimaryDiscountPerc, singleRow.get(Constants.PrimaryDiscountPerc));
                                    itemObject.put(Constants.SecondaryDiscountPerc, singleRow.get(Constants.SecondaryDiscountPerc));
                                    itemObject.put(Constants.CashDiscountPerc, singleRow.get(Constants.CashDiscountPerc));
                                    itemObject.put(Constants.TAX, singleRow.get(Constants.TAX));
                                    itemObject.put(Constants.MFD, singleRow.get(Constants.MFD));
                                    itemObject.put(Constants.IsfreeGoodsItem, singleRow.get(Constants.IsfreeGoodsItem));
                                    itemObject.put(Constants.Batch, singleRow.get(Constants.Batch));
                                    itemObject.put(Constants.TransRefTypeID, singleRow.get(Constants.TransRefTypeID));
                                    itemObject.put(Constants.TransRefNo, singleRow.get(Constants.TransRefNo));
                                    itemObject.put(Constants.TransRefItemNo, singleRow.get(Constants.TransRefItemNo));
                                    jsonArray.put(itemObject);
                                }
                            }
                            jsonHeaderObject.put(Constants.SSSoItemDetails, jsonArray);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        String request = jsonHeaderObject.toString();
                        event.getWriter().write(request);
                        return null;
                    }).setResponseListener(event -> {
                // Process the results.
                if (event.getReader()!=null) {
                    String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                    if (event.getResponseStatusCode() == 201) {
                        onlineReqListener.notifySuccessToListener(null);
                    } else if (event.getResponseStatusCode() == 403) {
                        createCSRFToken(mContext, iReceiveEvent -> {
                            if (iReceiveEvent.getResponseStatusCode() == 200) {
                                createSOEntity(table, itemtable, uiListener, mContext);
                            } else {
                                onlineReqListener.notifyErrorToListener(iReceiveEvent);
                            }
                        });
                    } else {
                        onlineReqListener.notifyErrorToListener(event);
                    }
                }
            }).start();
        }

        OnlineStoreListener openListener = OnlineStoreListener.getInstance();
        OnlineODataStore store = openListener.getStore();

        if (store != null) {
            try {
                //Creates the entity payload

                ODataEntity feedBackEntity = createFeedBackEntity(tableHdr, itemtable, store);

                OnlineRequestListener feedbackListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);


                String feedbackGUID32 = tableHdr.get(Constants.FeebackGUID).replace("-", "");

                String feedbackCreatedOn = tableHdr.get(Constants.CreatedOn);
                String feedbackCreatedAt = tableHdr.get(Constants.CreatedAt);

                String mStrDateTime = UtilConstants.getReArrangeDateFormat(feedbackCreatedOn) + "T" + UtilConstants.convertTimeOnly(feedbackCreatedAt);


                Map<String, String> collHeaders = new HashMap<String, String>();
                collHeaders.put("RequestID", feedbackGUID32);
                collHeaders.put("RepeatabilityCreation", mStrDateTime);

                ODataRequestParamSingle feedbackReq = new ODataRequestParamSingleDefaultImpl();
                feedbackReq.setMode(ODataRequestParamSingle.Mode.Create);
                feedbackReq.setResourcePath(feedBackEntity.getResourcePath());
                feedbackReq.setPayload(feedBackEntity);
                feedbackReq.getCustomHeaders().putAll(collHeaders);

                store.scheduleRequest(feedbackReq, feedbackListener);

            } catch (Exception e) {
                throw new OnlineODataStoreException(e);
            }
        }
        //END

    }

    private static ODataEntity createFeedBackEntity(Hashtable<String, String> hashtable, ArrayList<HashMap<String, String>> itemhashtable, OnlineODataStore store) throws ODataParserException {
        ODataEntity headerEntity = null;
        ArrayList<ODataEntity> tempArray = new ArrayList();
        try {
            if (hashtable != null) {
                // CreateOperation the parent Entity
                headerEntity = new ODataEntityDefaultImpl(UtilConstants.getNameSpaceOnline(store)+""+Constants.FeedbackEntity);

                headerEntity.setResourcePath(Constants.Feedbacks, Constants.Feedbacks);


                try {
                    store.allocateProperties(headerEntity, ODataStore.PropMode.All);
                } catch (ODataException e) {
                    e.printStackTrace();
                }

                store.allocateNavigationProperties(headerEntity);

                headerEntity.getProperties().put(Constants.FeebackGUID,
                        new ODataPropertyDefaultImpl(Constants.FeebackGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.FeebackGUID))));
                headerEntity.getProperties().put(Constants.Remarks,
                        new ODataPropertyDefaultImpl(Constants.Remarks, hashtable.get(Constants.Remarks)));
                headerEntity.getProperties().put(Constants.CPNo,
                        new ODataPropertyDefaultImpl(Constants.CPNo, hashtable.get(Constants.CPNo)));
                headerEntity.getProperties().put(Constants.FromCPGUID,
                        new ODataPropertyDefaultImpl(Constants.FromCPGUID, hashtable.get(Constants.CPGUID)));

                headerEntity.getProperties().put(Constants.FeedbackType,
                        new ODataPropertyDefaultImpl(Constants.FeedbackType, hashtable.get(Constants.FeedbackType)));

                headerEntity.getProperties().put(Constants.FeedbackTypeDesc,
                        new ODataPropertyDefaultImpl(Constants.FeedbackTypeDesc, hashtable.get(Constants.FeedbackTypeDesc)));

                headerEntity.getProperties().put(Constants.FromCPTypeID,
                        new ODataPropertyDefaultImpl(Constants.FromCPTypeID, hashtable.get(Constants.CPTypeID)));

                headerEntity.getProperties().put(Constants.ParentID,
                        new ODataPropertyDefaultImpl(Constants.ParentID, hashtable.get(Constants.ParentID)));

                headerEntity.getProperties().put(Constants.ParentName,
                        new ODataPropertyDefaultImpl(Constants.ParentName, hashtable.get(Constants.ParentName)));

                headerEntity.getProperties().put(Constants.ParentTypeID,
                        new ODataPropertyDefaultImpl(Constants.ParentTypeID, hashtable.get(Constants.ParentTypeID)));

                headerEntity.getProperties().put(Constants.ParentTypDesc,
                        new ODataPropertyDefaultImpl(Constants.ParentTypDesc, hashtable.get(Constants.ParentTypDesc)));

                if (hashtable.get(Constants.FeedbackDate)!=null) {
                    headerEntity.getProperties().put(Constants.FeedbackDate,
                            new ODataPropertyDefaultImpl(Constants.FeedbackDate, UtilConstants.convertDateFormat(hashtable.get(Constants.FeedbackDate))));
                }

                try {
                    headerEntity.getProperties().put(Constants.SPGUID,
                            new ODataPropertyDefaultImpl(Constants.SPGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.SPGUID))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                headerEntity.getProperties().put(Constants.SPNo,
                        new ODataPropertyDefaultImpl(Constants.SPNo, hashtable.get(Constants.SPNo)));

                // CreateOperation the item Entity

                ODataEntity itemEntity = new ODataEntityDefaultImpl(UtilConstants.getNameSpaceOnline(store)+""+Constants.FeedbackItemDetailEntity);
//

                try {
                    store.allocateProperties(itemEntity, ODataStore.PropMode.All);
                } catch (ODataException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < itemhashtable.size(); i++) {
                    HashMap<String, String> singleRow = itemhashtable.get(i);
                    try {
                        store.allocateProperties(itemEntity, ODataStore.PropMode.All);
                    } catch (ODataException e) {
                        e.printStackTrace();
                    }


                    itemEntity.getProperties().put(Constants.Remarks, new ODataPropertyDefaultImpl(Constants.Remarks, hashtable.get(Constants.Remarks)));
                    itemEntity.getProperties().put(Constants.FeebackGUID, new ODataPropertyDefaultImpl(Constants.FeebackGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.FeebackGUID))));
                    itemEntity.getProperties().put(Constants.FeebackItemGUID, new ODataPropertyDefaultImpl(Constants.FeebackItemGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.FeebackItemGUID))));
                    itemEntity.getProperties().put(Constants.FeedbackType, new ODataPropertyDefaultImpl(Constants.FeedbackType, singleRow.get(Constants.FeedbackType)));
                    itemEntity.getProperties().put(Constants.FeedbackTypeDesc, new ODataPropertyDefaultImpl(Constants.FeedbackTypeDesc, singleRow.get(Constants.FeedbackTypeDesc)));
                    itemEntity.getProperties().put(Constants.FeedbackSubTypeID, new ODataPropertyDefaultImpl(Constants.FeedbackSubTypeID, singleRow.get(Constants.FeedbackSubTypeID)));
                    itemEntity.getProperties().put(Constants.FeedbackSubTypeDesc, new ODataPropertyDefaultImpl(Constants.FeedbackSubTypeDesc, singleRow.get(Constants.FeedbackSubTypeDesc)));
                    itemEntity.getProperties().put(Constants.ParentID,
                            new ODataPropertyDefaultImpl(Constants.ParentID, hashtable.get(Constants.ParentID)));

                    itemEntity.getProperties().put(Constants.ParentName,
                            new ODataPropertyDefaultImpl(Constants.ParentName, hashtable.get(Constants.ParentName)));

                    itemEntity.getProperties().put(Constants.ParentTypeID,
                            new ODataPropertyDefaultImpl(Constants.ParentTypeID, hashtable.get(Constants.ParentTypeID)));

                    itemEntity.getProperties().put(Constants.ParentTypDesc,
                            new ODataPropertyDefaultImpl(Constants.ParentTypDesc, hashtable.get(Constants.ParentTypDesc)));
                    tempArray.add(i, itemEntity);
                }

                ODataEntitySetDefaultImpl itmEntity = new ODataEntitySetDefaultImpl(tempArray.size(), null, null);
                for (ODataEntity entity : tempArray) {
                    itmEntity.getEntities().add(entity);
                }
                itmEntity.setResourcePath(Constants.FeedbackItemDetails);


                ODataNavigationProperty navProp = headerEntity.getNavigationProperty(Constants.FeedbackItemDetails);
                navProp.setNavigationContent(itmEntity);
                headerEntity.setNavigationProperty(Constants.FeedbackItemDetails, navProp);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return headerEntity;

    }*/
/*public static String getSORequestPayload(){

}*/
    public static void createSOEntity(final Hashtable<String, String> table, final ArrayList<HashMap<String, String>> itemtable, UIListener uiListener, Context mContext) {
        OnlineRequestListener onlineReqListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        if (TextUtils.isEmpty(sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))){
            createCSRFToken(mContext, iReceiveEvent -> {
                if (iReceiveEvent.getResponseStatusCode() == 200) {
                    createSOEntity(table, itemtable, uiListener, mContext);
                }
            });
        }else {
            HttpConversationManager manager = new HttpConversationManager(mContext);
            // Create the conversation.
            manager.create(Uri.parse(MyUtils.getDefaultOnlineQryURL() + Constants.SSSOs))
//                .addHeader("Content-Type", "application/xml")
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Content-Type", "multipart/mixed")
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                    .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                    .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                    .addHeader("Accept", "application/json")
                    .setMethod(HttpMethod.POST)
                    .setRequestListener(event -> {
                        JSONObject jsonHeaderObject = new JSONObject();
                        try {
//                            jsonHeaderObject.put(Constants.OrderDate, "/\\Date(1546854673593)/\\");//UtilConstants.convertDateFormat(table.get(Constants.OrderDate))
                            jsonHeaderObject.put(Constants.NetPrice, table.get(Constants.NetPrice));
                            jsonHeaderObject.put(Constants.GrossAmt, table.get(Constants.GrossAmt));
                            jsonHeaderObject.put(Constants.OrderType, table.get(Constants.OrderType));
                            jsonHeaderObject.put(Constants.OrderTypeDesc, table.get(Constants.OrderTypeDesc));
                            jsonHeaderObject.put(Constants.Incoterm1, table.get(Constants.Incoterm1));
                            jsonHeaderObject.put(Constants.Incoterm1Desc, table.get(Constants.Incoterm1Desc));
                            jsonHeaderObject.put(Constants.CPTypeDesc, table.get(Constants.CPTypeDesc));
                            jsonHeaderObject.put(Constants.DmsDivision, table.get(Constants.DmsDivision));
                            jsonHeaderObject.put(Constants.CPNo, table.get(Constants.CPNo));
                            jsonHeaderObject.put(Constants.CPName, table.get(Constants.CPName));
                            jsonHeaderObject.put(Constants.CPGUID, table.get(Constants.CPGUID));
                            jsonHeaderObject.put(Constants.SPNo, table.get(Constants.SPNo));
                            jsonHeaderObject.put(Constants.SSSOGuid, table.get(Constants.SSSOGuid));
                            jsonHeaderObject.put(Constants.CPType, table.get(Constants.CPType));
                            jsonHeaderObject.put(Constants.TestRun, table.get(Constants.TestRun));
                            jsonHeaderObject.put(Constants.SoldToCPGUID, table.get(Constants.SoldToCPGUID));
                            jsonHeaderObject.put(Constants.SoldToId, table.get(Constants.SoldToId));
                            jsonHeaderObject.put(Constants.SoldToUID, table.get(Constants.SoldToUID));
                            jsonHeaderObject.put(Constants.SoldToType, table.get(Constants.SoldToType));
                            jsonHeaderObject.put(Constants.DmsDivisionDesc, table.get(Constants.DmsDivisionDesc));
                            jsonHeaderObject.put(Constants.Currency, table.get(Constants.Currency));
                            jsonHeaderObject.put(Constants.FromCPGUID, table.get(Constants.FromCPGUID));
                            jsonHeaderObject.put(Constants.FromCPName, table.get(Constants.FromCPName));
                            jsonHeaderObject.put(Constants.FromCPTypId, table.get(Constants.FromCPTypId));
                            jsonHeaderObject.put(Constants.SPGUID, table.get(Constants.SPGUID));
                            jsonHeaderObject.put(Constants.FirstName, table.get(Constants.FirstName));
                            JSONArray jsonArray = new JSONArray();
                            for (int incrementVal = 0; incrementVal < itemtable.size(); incrementVal++) {

                                HashMap<String, String> singleRow = itemtable.get(incrementVal);
                                if (!singleRow.get(Constants.IsfreeGoodsItem).equalsIgnoreCase("X")) {
                                    JSONObject itemObject = new JSONObject();
                                    itemObject.put(Constants.SSSOItemGUID, singleRow.get(Constants.SSSOItemGUID));
                                    itemObject.put(Constants.SSSOGuid, singleRow.get(Constants.SSSOGuid));
                                    itemObject.put(Constants.ItemNo, singleRow.get(Constants.ItemNo));
                                    itemObject.put(Constants.MaterialNo, singleRow.get(Constants.MaterialNo));
                                    itemObject.put(Constants.MaterialDesc, singleRow.get(Constants.MaterialDesc));
                                    itemObject.put(Constants.OrderMatGrp, singleRow.get(Constants.OrderMatGrp));
                                    itemObject.put(Constants.OrderMatGrpDesc, singleRow.get(Constants.OrderMatGrpDesc));
                                    itemObject.put(Constants.Currency, singleRow.get(Constants.Currency));
                                    itemObject.put(Constants.Uom, singleRow.get(Constants.Uom));
                                    itemObject.put(Constants.NetPrice, singleRow.get(Constants.NetPrice));
                                    itemObject.put(Constants.MRP, singleRow.get(Constants.MRP));
                                    itemObject.put(Constants.UnitPrice, singleRow.get(Constants.UnitPrice));
                                    itemObject.put(Constants.Quantity, singleRow.get(Constants.Quantity));
                                    itemObject.put(Constants.PriDiscount, singleRow.get(Constants.PriDiscount));
                                    itemObject.put(Constants.SecDiscount, singleRow.get(Constants.SecDiscount));
                                    itemObject.put(Constants.CashDiscount, singleRow.get(Constants.CashDiscount));
                                    itemObject.put(Constants.PrimaryDiscountPerc, singleRow.get(Constants.PrimaryDiscountPerc));
                                    itemObject.put(Constants.SecondaryDiscountPerc, singleRow.get(Constants.SecondaryDiscountPerc));
                                    itemObject.put(Constants.CashDiscountPerc, singleRow.get(Constants.CashDiscountPerc));
                                    itemObject.put(Constants.TAX, singleRow.get(Constants.TAX));
                                    itemObject.put(Constants.MFD, singleRow.get(Constants.MFD));
                                    itemObject.put(Constants.IsfreeGoodsItem, singleRow.get(Constants.IsfreeGoodsItem));
                                    itemObject.put(Constants.Batch, singleRow.get(Constants.Batch));
                                    itemObject.put(Constants.TransRefTypeID, singleRow.get(Constants.TransRefTypeID));
                                    itemObject.put(Constants.TransRefNo, singleRow.get(Constants.TransRefNo));
                                    itemObject.put(Constants.TransRefItemNo, singleRow.get(Constants.TransRefItemNo));
                                    jsonArray.put(itemObject);
                                }
                            }
                            jsonHeaderObject.put(Constants.SSSoItemDetails, jsonArray);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        String request = jsonHeaderObject.toString();
                        event.getWriter().write(request);
                        return null;
                    }).setResponseListener(event -> {
                        // Process the results.
                if (event.getReader()!=null) {
                    String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                    if (event.getResponseStatusCode() == 201) {
                        onlineReqListener.notifySuccessToListener(null);
                    } else if (event.getResponseStatusCode() == 403) {
                        createCSRFToken(mContext, iReceiveEvent -> {
                            if (iReceiveEvent.getResponseStatusCode() == 200) {
                                createSOEntity(table, itemtable, uiListener, mContext);
                            } else {
                                onlineReqListener.notifyErrorToListener(iReceiveEvent);
                            }
                        });
                    } else {
                        onlineReqListener.notifyErrorToListener(event);
                    }
                }
                    }).start();
        }

      /*  try {
            //Creates the entity payload
            ODataEntity soCreateEntity = createSOCreateEntity(table, itemtable);

            OnlineRequestListener onlineReqListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);
            onlineReqListener.notifyErrorToListener(new OnlineODataStoreException("Error"));

            String ssoGUID32 = table.get(Constants.SSSOGuid).replace("-", "");

            String soCreatedOn = table.get(Constants.CreatedOn);
            String soCreatedAt = table.get(Constants.CreatedAt);

            String mStrDateTime = UtilConstants.getReArrangeDateFormat(soCreatedOn) + Constants.T + UtilConstants.convertTimeOnly(soCreatedAt);

            Map<String, String> createHeaders = new HashMap<String, String>();
            createHeaders.put(Constants.RequestID, ssoGUID32);
            createHeaders.put(Constants.RepeatabilityCreation, mStrDateTime);

            ODataRequestParamSingle collectionReq = new ODataRequestParamSingleDefaultImpl();
            collectionReq.setMode(ODataRequestParamSingle.Mode.Create);
            collectionReq.setResourcePath(soCreateEntity.getResourcePath());
            collectionReq.setPayload(soCreateEntity);
            collectionReq.getCustomHeaders().putAll(createHeaders);

//                store.scheduleRequest(collectionReq, onlineReqListener);

        } catch (Exception e) {
            e.printStackTrace();
//                throw new OnlineODataStoreException(e);
        }*/
//        }
        //END

    }

    private static String CSRF_TOKEN ="";
    private static final int TIME_OUT = 15000;
    public static void createEntity(final String requestID,final String requestString, final String resourcePath, UIListener uiListener, Context mContext) {
        OnlineRequestListener onlineReqListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        if (TextUtils.isEmpty(sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))){
            createCSRFToken(mContext, iReceiveEvent -> {
                if (iReceiveEvent.getResponseStatusCode() == 200) {
                    createEntity(requestID, requestString,resourcePath, uiListener, mContext);
                }else if (iReceiveEvent.getResponseStatusCode() == 404) {
                    LogManager.writeLogInfo("Response received from server with code 404 ");
                    AvailableServer.registerServer(mContext, new AvailableServer.RegisterServer() {
                        @Override
                        public void requestSuccess() {
                            createEntity(requestID, requestString, resourcePath, uiListener, mContext);
                            LogManager.writeLogInfo("Registered successfully create entity");
                        }

                        @Override
                        public void requestError(String errorMessage) {
                            onlineReqListener.notifyErrorToListenerMsg("");
                        }
                    });
                }else{
                    LogManager.writeLogInfo("Token fetch response code !=200 : "+iReceiveEvent.getResponseStatusCode());
                }
            });
        }else {
            JSONObject jsonObject = null;
            String createdOn = "",createdAt="";
            String resukltId = "";
            try {
                jsonObject = new JSONObject(requestString);
                if(jsonObject.has(Constants.CreatedOn)&&!TextUtils.isEmpty(jsonObject.getString(Constants.CreatedOn)))
                    createdOn = jsonObject.getString(Constants.CreatedOn);
                if(jsonObject.has(Constants.CreatedAt)&&!TextUtils.isEmpty(jsonObject.getString(Constants.CreatedAt)))
                    createdAt = jsonObject.getString(Constants.CreatedAt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!TextUtils.isEmpty(createdOn) && !TextUtils.isEmpty(createdAt)) {
                LogManager.writeLogInfo("CSRF token fetched, Posting data to server.");
                String mStrDateTime = com.arteriatech.mutils.common.UtilConstants.getReArrangeDateFormat(createdOn) + Constants.T + com.arteriatech.mutils.common.UtilConstants.convertTimeOnly(createdAt);

                HttpConversationManager manager = new HttpConversationManager(mContext);
                // Create the conversation.
                manager.create(Uri.parse(MyUtils.getDefaultOnlineQryURL() + resourcePath))
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                        .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                        .addHeader("RequestID", requestID)
                        .addHeader("RepeatabilityCreation", mStrDateTime)
                        .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                        .addHeader("Accept", "application/json")
                        .setTimeout(TIME_OUT)
                        .setMethod(HttpMethod.POST)
                        .setRequestListener(event -> {
                            if (!TextUtils.isEmpty(requestString))
                                event.getWriter().write(requestString);
                            return null;
                        }).setResponseListener(event -> {
                    // Process the results.
                    if (event.getReader() != null) {
                        String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                        //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                        if (event.getResponseStatusCode() == 201) {
                            LogManager.writeLogInfo("Response received from server with code 201 ");
                            onlineReqListener.notifySuccessToListener(responseBody);
                        }else if (event.getResponseStatusCode() == 404) {
                            LogManager.writeLogInfo("Response received from server with code 404 ");
                            AvailableServer.registerServer(mContext, new AvailableServer.RegisterServer() {
                                @Override
                                public void requestSuccess() {
                                    createEntity(requestID, requestString, resourcePath, uiListener, mContext);
                                    LogManager.writeLogInfo("Registered successfully create entity");
                                }

                                @Override
                                public void requestError(String errorMessage) {
                                    onlineReqListener.notifyErrorToListenerMsg(responseBody);
                                }
                            });
                        }  else if (event.getResponseStatusCode() == 403) {
                            LogManager.writeLogInfo("CSRF token verification failed, Retrying to fetch CSRF token");
                            createCSRFToken(mContext, iReceiveEvent -> {
                                if (iReceiveEvent.getResponseStatusCode() == 200) {
                                    LogManager.writeLogInfo("Token fetched in retry. posting to server");
                                    createEntity(requestID, requestString, resourcePath, uiListener, mContext);
                                } else {
                                    if (iReceiveEvent.getReader()!=null) {
                                        String responseBodytemp = IReceiveEvent.Util.getResponseBody(iReceiveEvent.getReader());
                                        //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                                        LogManager.writeLogInfo("Token fetched failed in retry");
                                        onlineReqListener.notifyErrorToListenerMsg(responseBodytemp);
                                    }
                                }
                            });
                        } else {
                            onlineReqListener.notifyErrorToListenerMsg(responseBody);
                        }
                    }
                }).start();
            }else{
                LogManager.writeLogInfo("CSRF token fetched, Posting data to server.");
                HttpConversationManager manager = new HttpConversationManager(mContext);
                // Create the conversation.
                manager.create(Uri.parse(MyUtils.getDefaultOnlineQryURL() + resourcePath))
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                        .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                        .addHeader("RequestID", requestID)
                        .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                        .addHeader("Accept", "application/json")
                        .setTimeout(TIME_OUT)
                        .setMethod(HttpMethod.POST)
                        .setRequestListener(event -> {
                            if (!TextUtils.isEmpty(requestString))
                                event.getWriter().write(requestString);
                            return null;
                        }).setResponseListener(event -> {
                    // Process the results.
                    if (event.getReader() != null) {
                                            String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                        //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                        if (event.getResponseStatusCode() == 201) {
                            CSRF_TOKEN = "";
                            LogManager.writeLogInfo("Response received from server with code 201 ");
                            onlineReqListener.notifySuccessToListener(responseBody);
                        } else if (event.getResponseStatusCode() == 403) {
                            LogManager.writeLogInfo("CSRF token verification failed, Retrying to fetch CSRF token");
                            createCSRFToken(mContext, iReceiveEvent -> {
                                if (iReceiveEvent.getResponseStatusCode() == 200) {
                                    createEntity(requestID, requestString, resourcePath, uiListener, mContext);
                                } else {
                                    onlineReqListener.notifyErrorToListener(iReceiveEvent);
                                }
                            });
                        } else {
                            onlineReqListener.notifyErrorToListenerMsg(responseBody);
                        }
                    }
                }).start();
            }
        }
    }
    public static void createEntityRetailer(final String requestID,final String requestString, final String resourcePath, UIListener uiListener, Context mContext) {
        OnlineRequestListener onlineReqListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        if (TextUtils.isEmpty(sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))){
            createCSRFToken(mContext, iReceiveEvent -> {
                if (iReceiveEvent.getResponseStatusCode() == 200) {
                    createEntity(requestID, requestString,resourcePath, uiListener, mContext);
                }else if (iReceiveEvent.getResponseStatusCode() == 404) {
                    LogManager.writeLogInfo("Response received from server with code 404 ");
                    AvailableServer.registerServer(mContext, new AvailableServer.RegisterServer() {
                        @Override
                        public void requestSuccess() {
                            createEntity(requestID, requestString, resourcePath, uiListener, mContext);
                            LogManager.writeLogInfo("Registered successfully create entity");
                        }

                        @Override
                        public void requestError(String errorMessage) {
                            onlineReqListener.notifyErrorToListenerMsg("");
                        }
                    });
                }else{
                    LogManager.writeLogInfo("Token fetch response code !=200 : "+iReceiveEvent.getResponseStatusCode());
                }
            });
        }else {
            JSONObject jsonObject = null;
            String createdOn = "",createdAt="";
            String resukltId = "";
            try {
                jsonObject = new JSONObject(requestString);
                if(jsonObject.has(Constants.CreatedOn)&&!TextUtils.isEmpty(jsonObject.getString(Constants.CreatedOn)))
                    createdOn = jsonObject.getString(Constants.CreatedOn);
                if(jsonObject.has(Constants.CreatedAt)&&!TextUtils.isEmpty(jsonObject.getString(Constants.CreatedAt)))
                    createdAt = jsonObject.getString(Constants.CreatedAt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!TextUtils.isEmpty(createdOn) && !TextUtils.isEmpty(createdAt)) {
                LogManager.writeLogInfo("CSRF token fetched, Posting data to server.");
                String mStrDateTime = com.arteriatech.mutils.common.UtilConstants.getReArrangeDateFormat(createdOn) + Constants.T + com.arteriatech.mutils.common.UtilConstants.convertTimeOnly(createdAt);

                HttpConversationManager manager = new HttpConversationManager(mContext);
                // Create the conversation.
                manager.create(Uri.parse(MyUtils.getDefaultEndPointURL1() + resourcePath))
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                        .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                        .addHeader("RequestID", requestID)
                        .addHeader("RepeatabilityCreation", mStrDateTime)
                        .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                        .addHeader("Accept", "application/json")
                        .setTimeout(TIME_OUT)
                        .setMethod(HttpMethod.POST)
                        .setRequestListener(event -> {
                            if (!TextUtils.isEmpty(requestString))
                                event.getWriter().write(requestString);
                            return null;
                        }).setResponseListener(event -> {
                    // Process the results.
                    if (event.getReader() != null) {
                        String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                        //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                        if (event.getResponseStatusCode() == 201) {
                            LogManager.writeLogInfo("Response received from server with code 201 ");
                            onlineReqListener.notifySuccessToListener(responseBody);
                        }else if (event.getResponseStatusCode() == 404) {
                            LogManager.writeLogInfo("Response received from server with code 404 ");
                            AvailableServer.registerServer(mContext, new AvailableServer.RegisterServer() {
                                @Override
                                public void requestSuccess() {
                                    createEntity(requestID, requestString, resourcePath, uiListener, mContext);
                                    LogManager.writeLogInfo("Registered successfully create entity");
                                }

                                @Override
                                public void requestError(String errorMessage) {
                                    onlineReqListener.notifyErrorToListenerMsg(responseBody);
                                }
                            });
                        }  else if (event.getResponseStatusCode() == 403) {
                            LogManager.writeLogInfo("CSRF token verification failed, Retrying to fetch CSRF token");
                            createCSRFToken(mContext, iReceiveEvent -> {
                                if (iReceiveEvent.getResponseStatusCode() == 200) {
                                    LogManager.writeLogInfo("Token fetched in retry. posting to server");
                                    createEntity(requestID, requestString, resourcePath, uiListener, mContext);
                                } else {
                                    if (iReceiveEvent.getReader()!=null) {
                                        String responseBodytemp = IReceiveEvent.Util.getResponseBody(iReceiveEvent.getReader());
                                        //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                                        LogManager.writeLogInfo("Token fetched failed in retry");
                                        onlineReqListener.notifyErrorToListenerMsg(responseBodytemp);
                                    }
                                }
                            });
                        } else {
                            onlineReqListener.notifyErrorToListenerMsg(responseBody);
                        }
                    }
                }).start();
            }else{
                LogManager.writeLogInfo("CSRF token fetched, Posting data to server.");
                HttpConversationManager manager = new HttpConversationManager(mContext);
                // Create the conversation.
                manager.create(Uri.parse(MyUtils.getDefaultEndPointURL1() + resourcePath))
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                        .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                        .addHeader("RequestID", requestID)
                        .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                        .addHeader("Accept", "application/json")
                        .setTimeout(TIME_OUT)
                        .setMethod(HttpMethod.POST)
                        .setRequestListener(event -> {
                            if (!TextUtils.isEmpty(requestString))
                                event.getWriter().write(requestString);
                            return null;
                        }).setResponseListener(event -> {
                    // Process the results.
                    if (event.getReader() != null) {
                                            String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                        //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                        if (event.getResponseStatusCode() == 201) {
                            CSRF_TOKEN = "";
                            LogManager.writeLogInfo("Response received from server with code 201 ");
                            onlineReqListener.notifySuccessToListener(responseBody);
                        } else if (event.getResponseStatusCode() == 403) {
                            LogManager.writeLogInfo("CSRF token verification failed, Retrying to fetch CSRF token");
                            createCSRFToken(mContext, iReceiveEvent -> {
                                if (iReceiveEvent.getResponseStatusCode() == 200) {
                                    createEntity(requestID, requestString, resourcePath, uiListener, mContext);
                                } else {
                                    onlineReqListener.notifyErrorToListener(iReceiveEvent);
                                }
                            });
                        } else {
                            onlineReqListener.notifyErrorToListenerMsg(responseBody);
                        }
                    }
                }).start();
            }
        }
    }

    public static void createEntityCould(final String requestID,final String requestString, final String resourcePath, UIListener uiListener, Context mContext) {
        OnlineRequestListener onlineReqListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        if (TextUtils.isEmpty(sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))){
            createCSRFToken(mContext, iReceiveEvent -> {
                if (iReceiveEvent.getResponseStatusCode() == 200) {
                    createEntityCould(requestID, requestString,resourcePath, uiListener, mContext);
                }else if (iReceiveEvent.getResponseStatusCode() == 404) {
                    LogManager.writeLogInfo("Response received from server with code 404 ");
                    AvailableServer.registerServer(mContext, new AvailableServer.RegisterServer() {
                        @Override
                        public void requestSuccess() {
                            createEntityCould(requestID, requestString, resourcePath, uiListener, mContext);
                            LogManager.writeLogInfo("Registered successfully create entity");
                        }

                        @Override
                        public void requestError(String errorMessage) {
                            onlineReqListener.notifyErrorToListenerMsg("");
                        }
                    });
                }else{
                    LogManager.writeLogInfo("Token fetch response code !=200 : "+iReceiveEvent.getResponseStatusCode());
                }
            });
        }else {
            JSONObject jsonObject = null;
            String createdOn = "",createdAt="";
            String resukltId = "";
            try {
                jsonObject = new JSONObject(requestString);
                if(jsonObject.has(Constants.CreatedOn)&&!TextUtils.isEmpty(jsonObject.getString(Constants.CreatedOn)))
                    createdOn = jsonObject.getString(Constants.CreatedOn);
                if(jsonObject.has(Constants.CreatedAt)&&!TextUtils.isEmpty(jsonObject.getString(Constants.CreatedAt)))
                    createdAt = jsonObject.getString(Constants.CreatedAt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!TextUtils.isEmpty(createdOn) && !TextUtils.isEmpty(createdAt)) {
                LogManager.writeLogInfo("CSRF token fetched, Posting data to server.");
                String mStrDateTime = com.arteriatech.mutils.common.UtilConstants.getReArrangeDateFormat(createdOn) + Constants.T + com.arteriatech.mutils.common.UtilConstants.convertTimeOnly(createdAt);

                HttpConversationManager manager = new HttpConversationManager(mContext);
                // Create the conversation.
                manager.create(Uri.parse(MyUtils.getCloudOnlineQryURL() + resourcePath))
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                        .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                        .addHeader("RequestID", requestID)
                        .addHeader("RepeatabilityCreation", mStrDateTime)
                        .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                        .addHeader("Accept", "application/json")
                        .setTimeout(TIME_OUT)
                        .setMethod(HttpMethod.POST)
                        .setRequestListener(event -> {
                            if (!TextUtils.isEmpty(requestString))
                                event.getWriter().write(requestString);
                            return null;
                        }).setResponseListener(event -> {
                            // Process the results.
                            if (event.getReader() != null) {
                                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                                //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                                if (event.getResponseStatusCode() == 201) {
                                    LogManager.writeLogInfo("Response received from server with code 201 ");
                                    onlineReqListener.notifySuccessToListener(responseBody);
                                }else if (event.getResponseStatusCode() == 404) {
                                    LogManager.writeLogInfo("Response received from server with code 404 ");
                                    AvailableServer.registerServer(mContext, new AvailableServer.RegisterServer() {
                                        @Override
                                        public void requestSuccess() {
                                            createEntityCould(requestID, requestString, resourcePath, uiListener, mContext);
                                            LogManager.writeLogInfo("Registered successfully create entity");
                                        }

                                        @Override
                                        public void requestError(String errorMessage) {
                                            onlineReqListener.notifyErrorToListenerMsg(responseBody);
                                        }
                                    });
                                }  else if (event.getResponseStatusCode() == 403) {
                                    LogManager.writeLogInfo("CSRF token verification failed, Retrying to fetch CSRF token");
                                    createCSRFToken(mContext, iReceiveEvent -> {
                                        if (iReceiveEvent.getResponseStatusCode() == 200) {
                                            LogManager.writeLogInfo("Token fetched in retry. posting to server");
                                            createEntityCould(requestID, requestString, resourcePath, uiListener, mContext);
                                        } else {
                                            if (iReceiveEvent.getReader()!=null) {
                                                String responseBodytemp = IReceiveEvent.Util.getResponseBody(iReceiveEvent.getReader());
                                                //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                                                LogManager.writeLogInfo("Token fetched failed in retry");
                                                onlineReqListener.notifyErrorToListenerMsg(responseBodytemp);
                                            }
                                        }
                                    });
                                } else {
                                    onlineReqListener.notifyErrorToListenerMsg(responseBody);
                                }
                            }
                        }).start();
            }else{
                LogManager.writeLogInfo("CSRF token fetched, Posting data to server.");
                HttpConversationManager manager = new HttpConversationManager(mContext);
                // Create the conversation.
                manager.create(Uri.parse(MyUtils.getCloudOnlineQryURL() + resourcePath))
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                        .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                        .addHeader("RequestID", requestID)
                        .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                        .addHeader("Accept", "application/json")
                        .setTimeout(TIME_OUT)
                        .setMethod(HttpMethod.POST)
                        .setRequestListener(event -> {
                            if (!TextUtils.isEmpty(requestString))
                                event.getWriter().write(requestString);
                            return null;
                        }).setResponseListener(event -> {
                            // Process the results.
                            if (event.getReader() != null) {
                                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                                //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                                if (event.getResponseStatusCode() == 201) {
                                    CSRF_TOKEN = "";
                                    LogManager.writeLogInfo("Response received from server with code 201 ");
                                    onlineReqListener.notifySuccessToListener(responseBody);
                                } else if (event.getResponseStatusCode() == 403) {
                                    LogManager.writeLogInfo("CSRF token verification failed, Retrying to fetch CSRF token");
                                    createCSRFToken(mContext, iReceiveEvent -> {
                                        if (iReceiveEvent.getResponseStatusCode() == 200) {
                                            createEntityCould(requestID, requestString, resourcePath, uiListener, mContext);
                                        } else {
                                            onlineReqListener.notifyErrorToListener(iReceiveEvent);
                                        }
                                    });
                                } else {
                                    onlineReqListener.notifyErrorToListenerMsg(responseBody);
                                }
                            }
                        }).start();
            }
        }
    }

    public static void createCPEntity(final String requestID,final String requestString, final String resourcePath, OnlineResponseListener onlineResponseListener, Context mContext) {
//        OnlineRequestListener onlineReqListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        if (TextUtils.isEmpty(sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))){
            createCSRFToken(mContext, iReceiveEvent -> {
                if (iReceiveEvent.getResponseStatusCode() == 200) {
                    createCPEntity(requestID, requestString,resourcePath, onlineResponseListener, mContext);
                }else if (iReceiveEvent.getResponseStatusCode() == 404) {
                    LogManager.writeLogInfo("Response received from server with code 404 ");
                    AvailableServer.registerServer(mContext, new AvailableServer.RegisterServer() {
                        @Override
                        public void requestSuccess() {
                            createCPEntity(requestID, requestString,resourcePath, onlineResponseListener, mContext);
                            LogManager.writeLogInfo("Registered successfully create entity");
                        }

                        @Override
                        public void requestError(String errorMessage) {
                            onlineResponseListener.onRequestError(Operation.Create.getValue(),"");
                        }
                    });
                }else{
                    LogManager.writeLogInfo("Token fetch response code !=200 : "+iReceiveEvent.getResponseStatusCode());
                }
            });
        }else {
            JSONObject jsonObject = null;
            String createdOn = "",createdAt="";
            String resukltId = "";
            try {
                jsonObject = new JSONObject(requestString);
                if(jsonObject.has(Constants.CreatedOn)&&!TextUtils.isEmpty(jsonObject.getString(Constants.CreatedOn)))
                    createdOn = jsonObject.getString(Constants.CreatedOn);
                if(jsonObject.has(Constants.CreatedAt)&&!TextUtils.isEmpty(jsonObject.getString(Constants.CreatedAt)))
                    createdAt = jsonObject.getString(Constants.CreatedAt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!TextUtils.isEmpty(createdOn) && !TextUtils.isEmpty(createdAt)) {
                LogManager.writeLogInfo("CSRF token fetched, Posting data to server.");
                String mStrDateTime = com.arteriatech.mutils.common.UtilConstants.getReArrangeDateFormat(createdOn) + Constants.T + com.arteriatech.mutils.common.UtilConstants.convertTimeOnly(createdAt);

                HttpConversationManager manager = new HttpConversationManager(mContext);
                // Create the conversation.
                manager.create(Uri.parse(MyUtils.getDefaultOnlineQryURL() + resourcePath))
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                        .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                        .addHeader("RequestID", requestID)
                        .addHeader("RepeatabilityCreation", mStrDateTime)
                        .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                        .addHeader("Accept", "application/json")
                        .setTimeout(TIME_OUT)
                        .setMethod(HttpMethod.POST)
                        .setRequestListener(event -> {
                            if (!TextUtils.isEmpty(requestString))
                                event.getWriter().write(requestString);
                            return null;
                        }).setResponseListener(event -> {
                    // Process the results.
                    if (event.getReader() != null) {
                        String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                        //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                        if (event.getResponseStatusCode() == 201) {
                            LogManager.writeLogInfo("Response received from server with code 201 ");
                            onlineResponseListener.onRequestSuccess(Operation.Create.getValue(),responseBody);
                        } else if (event.getResponseStatusCode() == 404) {
                            LogManager.writeLogInfo("Response received from server with code 404 ");
                            AvailableServer.registerServer(mContext, new AvailableServer.RegisterServer() {
                                @Override
                                public void requestSuccess() {
                                    createCPEntity(requestID, requestString,resourcePath, onlineResponseListener, mContext);
                                    LogManager.writeLogInfo("Registered successfully create entity");
                                }

                                @Override
                                public void requestError(String errorMessage) {
                                    onlineResponseListener.onRequestError(Operation.Create.getValue(),responseBody);
                                }
                            });
                        } else if (event.getResponseStatusCode() == 403) {
                            LogManager.writeLogInfo("CSRF token verification failed, Retrying to fetch CSRF token");
                            createCSRFToken(mContext, iReceiveEvent -> {
                                if (iReceiveEvent.getResponseStatusCode() == 200) {
                                    LogManager.writeLogInfo("Token fetched in retry. posting to server");
                                    createCPEntity(requestID, requestString, resourcePath, onlineResponseListener, mContext);
                                } else {
                                    if (iReceiveEvent.getReader()!=null) {
                                        String responseBodytemp = IReceiveEvent.Util.getResponseBody(iReceiveEvent.getReader());
                                        //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                                        LogManager.writeLogInfo("Token fetched failed in retry");
                                        onlineResponseListener.onRequestError(Operation.Create.getValue(),responseBodytemp);
                                    }
                                }
                            });
                        } else {
                            onlineResponseListener.onRequestError(Operation.Create.getValue(),responseBody);
//                            onlineReqListener.notifyErrorToListenerMsg(responseBody);
                        }
                    }
                }).start();
            }else{
                LogManager.writeLogInfo("CSRF token fetched, Posting data to server.");
                HttpConversationManager manager = new HttpConversationManager(mContext);
                // Create the conversation.
                manager.create(Uri.parse(MyUtils.getDefaultOnlineQryURL() + resourcePath))
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                        .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                        .addHeader("RequestID", requestID)
                        .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                        .addHeader("Accept", "application/json")
                        .setTimeout(TIME_OUT)
                        .setMethod(HttpMethod.POST)
                        .setRequestListener(event -> {
                            if (!TextUtils.isEmpty(requestString))
                                event.getWriter().write(requestString);
                            return null;
                        }).setResponseListener(event -> {
                    // Process the results.
                    if (event.getReader() != null) {
                        String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                        //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                        if (event.getResponseStatusCode() == 201) {
                            CSRF_TOKEN = "";
                            LogManager.writeLogInfo("Response received from server with code 201 ");
                            onlineResponseListener.onRequestSuccess(Operation.Create.getValue(),responseBody);
                        } else if (event.getResponseStatusCode() == 403) {
                            LogManager.writeLogInfo("CSRF token verification failed, Retrying to fetch CSRF token");
                            createCSRFToken(mContext, iReceiveEvent -> {
                                if (iReceiveEvent.getResponseStatusCode() == 200) {
                                    createCPEntity(requestID, requestString, resourcePath, onlineResponseListener, mContext);
                                } else {
                                    onlineResponseListener.onRequestError(Operation.Create.getValue(),responseBody);
                                }
                            });
                        } else {
                            onlineResponseListener.onRequestError(Operation.Create.getValue(),responseBody);
                        }
                    }
                }).start();
            }
        }
    }

    private static void createZCommonCSRFToken(Context mContext, IResponseListener var1) {
        LogManager.writeLogInfo("fetching CSRF token started.");
        HttpConversationManager manager = new HttpConversationManager(mContext);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        manager.create(Uri.parse(MyUtils.getCommonEndPointURL()))
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                .addHeader("x-csrf-token", "fetch")
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("Accept", "application/json")
                .setMethod(HttpMethod.GET)
                .setResponseListener(event1 -> {
                    // Process the results.
//                    String responseBody1 = IReceiveEvent.Util.getResponseBody(event1.getReader());
//                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody1 + " " + event1.getResponseStatusCode());
                    LogManager.writeLogInfo("Token listener called : "+event1.getResponseStatusCode());
                    if (event1.getResponseStatusCode() == 200) {
                        LogManager.writeLogInfo("fetching CSRF token successful.");
                        Map<String, List<String>> mapList = event1.getResponseHeaders();
                        if (mapList != null && mapList.size() > 0) {
                            List<String> arrayList = mapList.get("x-csrf-token");
                            if (arrayList != null && !arrayList.isEmpty()) {
//                                CSRF_TOKEN = arrayList.get(0);
                                SharedPreferences.Editor spEdit = sharedPref.edit();
                                spEdit.putString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, arrayList.get(0));
                                spEdit.apply();
                            }
                        }
                    }
                    var1.onResponseReceived(event1);
                }).start();
    }


    private static void createCSRFToken(Context mContext, IResponseListener var1) {
        LogManager.writeLogInfo("fetching CSRF token started.");
        HttpConversationManager manager = new HttpConversationManager(mContext);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        manager.create(Uri.parse(MyUtils.getDefaultOnlineQryURL()))
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                .addHeader("x-csrf-token", "fetch")
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("Accept", "application/json")
                .setMethod(HttpMethod.GET)
                .setResponseListener(event1 -> {
                    // Process the results.
//                    String responseBody1 = IReceiveEvent.Util.getResponseBody(event1.getReader());
//                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody1 + " " + event1.getResponseStatusCode());
                    LogManager.writeLogInfo("Token listener called : "+event1.getResponseStatusCode());
                    if (event1.getResponseStatusCode() == 200) {
                        LogManager.writeLogInfo("fetching CSRF token successful.");
                        Map<String, List<String>> mapList = event1.getResponseHeaders();
                        if (mapList != null && mapList.size() > 0) {
                            List<String> arrayList = mapList.get("x-csrf-token");
                            if (arrayList != null && !arrayList.isEmpty()) {
//                                CSRF_TOKEN = arrayList.get(0);
                                SharedPreferences.Editor spEdit = sharedPref.edit();
                                spEdit.putString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, arrayList.get(0));
                                spEdit.apply();
                            }
                        }
                    }
                    var1.onResponseReceived(event1);
                }).start();
    }

    private static void createExjdCSRFToken(Context mContext, IResponseListener var1) {
        LogManager.writeLogInfo("fetching CSRF token started.");
        HttpConversationManager manager = new HttpConversationManager(mContext);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        manager.create(Uri.parse(MyUtils.getPostjdsOnlineQryURL()))
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                .addHeader("x-csrf-token", "fetch")
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("Accept", "application/json")
                .setMethod(HttpMethod.GET)
                .setResponseListener(event1 -> {
                    // Process the results.
//                    String responseBody1 = IReceiveEvent.Util.getResponseBody(event1.getReader());
//                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody1 + " " + event1.getResponseStatusCode());
                    LogManager.writeLogInfo("Token listener called : "+event1.getResponseStatusCode());
//                    if (event1.getResponseStatusCode() == 200) {
                        LogManager.writeLogInfo("fetching CSRF token successful.");
                        Map<String, List<String>> mapList = event1.getResponseHeaders();
                        if (mapList != null && mapList.size() > 0) {
                            List<String> arrayList = mapList.get("x-csrf-token");
                            if (arrayList != null && !arrayList.isEmpty()) {
//                                CSRF_TOKEN = arrayList.get(0);
                                SharedPreferences.Editor spEdit = sharedPref.edit();
                                spEdit.putString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, arrayList.get(0));
                                spEdit.apply();
                            }
                        }
//                    }
                    var1.onResponseReceived(event1);
                }).start();
    }

    private static void createCSRFTokenCloud(Context mContext, IResponseListener var1) {
        LogManager.writeLogInfo("fetching CSRF token started.");
        HttpConversationManager manager = new HttpConversationManager(mContext);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        manager.create(Uri.parse(MyUtils.getCloudOnlineQryURL()))
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                .addHeader("x-csrf-token", "fetch")
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("Accept", "application/json")
                .setMethod(HttpMethod.GET)
                .setResponseListener(event1 -> {
                    // Process the results.
//                    String responseBody1 = IReceiveEvent.Util.getResponseBody(event1.getReader());
//                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody1 + " " + event1.getResponseStatusCode());
                    LogManager.writeLogInfo("Token listener called : "+event1.getResponseStatusCode());
                    if (event1.getResponseStatusCode() == 200) {
                        LogManager.writeLogInfo("fetching CSRF token successful.");
                        Map<String, List<String>> mapList = event1.getResponseHeaders();
                        if (mapList != null && mapList.size() > 0) {
                            List<String> arrayList = mapList.get("x-csrf-token");
                            if (arrayList != null && !arrayList.isEmpty()) {
//                                CSRF_TOKEN = arrayList.get(0);
                                SharedPreferences.Editor spEdit = sharedPref.edit();
                                spEdit.putString(UtilRegistrationActivity.KEY_xCSRF_TOKEN_CLOUD, arrayList.get(0));
                                spEdit.apply();
                            }
                        }
                    }
                    var1.onResponseReceived(event1);
                }).start();
    }

    public interface OnlineResponseListener{
        void onRequestError(int operation, String error);
        void onRequestSuccess(int operation, String responseBody);
    }

    public static void createZCommonEntity(final String requestID,final String requestString, final String resourcePath, UIListener uiListener, Context mContext) {
        OnlineRequestListener onlineReqListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        if (TextUtils.isEmpty(sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))){
            createZCommonCSRFToken(mContext, iReceiveEvent -> {
                if (iReceiveEvent.getResponseStatusCode() == 200) {
                    createZCommonEntity(requestID, requestString,resourcePath, uiListener, mContext);
                }else if (iReceiveEvent.getResponseStatusCode() == 404) {
                    LogManager.writeLogInfo("Response received from server with code 404 ");
                    AvailableServer.registerServer(mContext, new AvailableServer.RegisterServer() {
                        @Override
                        public void requestSuccess() {
                            createZCommonEntity(requestID, requestString, resourcePath, uiListener, mContext);
                            LogManager.writeLogInfo("Registered successfully create entity");
                        }

                        @Override
                        public void requestError(String errorMessage) {
                            onlineReqListener.notifyErrorToListenerMsg("");
                        }
                    });
                }else{
                    LogManager.writeLogInfo("Token fetch response code !=200 : "+iReceiveEvent.getResponseStatusCode());
                }
            });
        }else {
            JSONObject jsonObject = null;
            String createdOn = "",createdAt="";
            String resukltId = "";
            try {
                jsonObject = new JSONObject(requestString);
                if(jsonObject.has(Constants.CreatedOn)&&!TextUtils.isEmpty(jsonObject.getString(Constants.CreatedOn)))
                    createdOn = jsonObject.getString(Constants.CreatedOn);
                if(jsonObject.has(Constants.CreatedAt)&&!TextUtils.isEmpty(jsonObject.getString(Constants.CreatedAt)))
                    createdAt = jsonObject.getString(Constants.CreatedAt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!TextUtils.isEmpty(createdOn) && !TextUtils.isEmpty(createdAt)) {
                LogManager.writeLogInfo("CSRF token fetched, Posting data to server.");
                String mStrDateTime = com.arteriatech.mutils.common.UtilConstants.getReArrangeDateFormat(createdOn) + Constants.T + com.arteriatech.mutils.common.UtilConstants.convertTimeOnly(createdAt);
                HttpConversationManager manager = new HttpConversationManager(mContext);
                manager.create(Uri.parse(MyUtils.getCommonEndPointURL() + resourcePath))
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                        .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
//                        .addHeader("RequestID", requestID)
                        .addHeader("RepeatabilityCreation", mStrDateTime)
                        .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                        .addHeader("Accept", "application/json")
                        .addHeader("x-smp-appid", Configuration.APP_ID)
                        .setTimeout(TIME_OUT)
                        .setMethod(HttpMethod.POST)
                        .setRequestListener(event -> {
                            if (!TextUtils.isEmpty(requestString))
                                event.getWriter().write(requestString);
                            return null;
                        }).setResponseListener(event -> {
                            // Process the results.
                            if (event.getReader() != null) {
                                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                                if (event.getResponseStatusCode() == 201) {
                                    LogManager.writeLogInfo("Response received from server with code 201 ");
                                    onlineReqListener.notifySuccessToListener(responseBody);
                                }else if (event.getResponseStatusCode() == 404) {
                                    LogManager.writeLogInfo("Response received from server with code 404 ");
                                    AvailableServer.registerServer(mContext, new AvailableServer.RegisterServer() {
                                        @Override
                                        public void requestSuccess() {
                                            createZCommonEntity(requestID, requestString, resourcePath, uiListener, mContext);
                                            LogManager.writeLogInfo("Registered successfully create entity");
                                        }

                                        @Override
                                        public void requestError(String errorMessage) {
                                            onlineReqListener.notifyErrorToListenerMsg(responseBody);
                                        }
                                    });
                                }  else if (event.getResponseStatusCode() == 403) {
                                    LogManager.writeLogInfo("CSRF token verification failed, Retrying to fetch CSRF token");
                                    createZCommonCSRFToken(mContext, iReceiveEvent -> {
                                        if (iReceiveEvent.getResponseStatusCode() == 200) {
                                            LogManager.writeLogInfo("Token fetched in retry. posting to server");
                                            createZCommonEntity(requestID, requestString, resourcePath, uiListener, mContext);
                                        } else {
                                            if (iReceiveEvent.getReader()!=null) {
                                                String responseBodytemp = IReceiveEvent.Util.getResponseBody(iReceiveEvent.getReader());
                                                //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                                                LogManager.writeLogInfo("Token fetched failed in retry");
                                                onlineReqListener.notifyErrorToListenerMsg(responseBodytemp);
                                            }
                                        }
                                    });
                                } else {
                                    onlineReqListener.notifyErrorToListenerMsg(responseBody);
                                }
                            }
                        }).start();
            }else{
                LogManager.writeLogInfo("CSRF token fetched, Posting data to server.");
                HttpConversationManager manager = new HttpConversationManager(mContext);
                // Create the conversation.
                manager.create(Uri.parse(MyUtils.getCommonEndPointURL() + resourcePath))
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                        .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
//                        .addHeader("RequestID", requestID)
                        .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                        .addHeader("Accept", "application/json")
                        .addHeader("x-smp-appid", Configuration.APP_ID)
                        .setTimeout(TIME_OUT)
                        .setMethod(HttpMethod.POST)
                        .setRequestListener(event -> {
                            if (!TextUtils.isEmpty(requestString))
                                event.getWriter().write(requestString);
                            return null;
                        }).setResponseListener(event -> {
                            // Process the results.
                            if (event.getReader() != null) {
                                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                                //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                                if (event.getResponseStatusCode() == 201) {
                                    CSRF_TOKEN = "";
                                    LogManager.writeLogInfo("Response received from server with code 201 ");
                                    onlineReqListener.notifySuccessToListener(responseBody);
                                } else if (event.getResponseStatusCode() == 403) {
                                    LogManager.writeLogInfo("CSRF token verification failed, Retrying to fetch CSRF token");
                                    createZCommonCSRFToken(mContext, iReceiveEvent -> {
                                        if (iReceiveEvent.getResponseStatusCode() == 200) {
                                            createZCommonEntity(requestID, requestString, resourcePath, uiListener, mContext);
                                        } else {
                                            onlineReqListener.notifyErrorToListener(iReceiveEvent);
                                        }
                                    });
                                } else {
                                    onlineReqListener.notifyErrorToListenerMsg(responseBody);
                                }
                            }
                        }).start();
            }
        }
    }

    public static String doOnlineGetRequest(String resourcePath, Context mContext, IResponseListener iResponseListener, ICommunicationErrorListener iCommunicationErrorListener){

        HttpConversationManager manager = new HttpConversationManager(mContext);
        manager.create(Uri.parse(MyUtils.getDefaultOnlineQryURL() + resourcePath))
                .setMethod(HttpMethod.GET)
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("Accept", "application/json")
                .addHeader("x-smp-appid", Configuration.APP_ID)
                .setResponseListener(iResponseListener).setFlowListener(IConversationFlowListener
                .prepareFor()
                .communicationError(iCommunicationErrorListener).build())
                .start();

        return "";
    }

    public static String doZCommonGetRequest(String resourcePath, Context mContext, IResponseListener iResponseListener, ICommunicationErrorListener iCommunicationErrorListener){
        HttpConversationManager manager = new HttpConversationManager(mContext);
        manager.create(Uri.parse(MyUtils.getCommonEndPointURL() + resourcePath))
                .setMethod(HttpMethod.GET)
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("Accept", "application/json")
                .addHeader("x-smp-appid", Configuration.APP_ID)
                .setResponseListener(iResponseListener).setFlowListener(IConversationFlowListener
                        .prepareFor()
                        .communicationError(iCommunicationErrorListener).build())
                .start();

        return "";
    }
    public static String doOnlineEntityDataGetRequest(String resourcePath, Context mContext, IResponseListener iResponseListener, ICommunicationErrorListener iCommunicationErrorListener){

        HttpConversationManager manager = new HttpConversationManager(mContext);
        manager.create(Uri.parse(MyUtils.getEntityDataOnlineQryURL() + resourcePath))
                .setMethod(HttpMethod.GET)
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("Accept", "application/json")
                .addHeader("x-smp-appid", Configuration.APP_ID)
                .setResponseListener(iResponseListener).setFlowListener(IConversationFlowListener
                        .prepareFor()
                        .communicationError(iCommunicationErrorListener).build())
                .start();

        return "";
    }

    public static String doOnlineGetRequestCloud(String resourcePath, Context mContext, IResponseListener iResponseListener, ICommunicationErrorListener iCommunicationErrorListener){

        HttpConversationManager manager = new HttpConversationManager(mContext);
        manager.create(Uri.parse(MyUtils.getCloudOnlineQryURL() + resourcePath))
                .setMethod(HttpMethod.GET)
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("Accept", "application/json")
                .addHeader("x-smp-appid", Configuration.APP_ID)
                .setResponseListener(iResponseListener).setFlowListener(IConversationFlowListener
                .prepareFor()
                .communicationError(iCommunicationErrorListener).build())
                .start();

        return "";
    }

    public static String validateIMEI(String resourcePath, Context mContext, IResponseListener iResponseListener, ICommunicationErrorListener iCommunicationErrorListener){
        HttpConversationManager manager = new HttpConversationManager(mContext);
        manager.create(Uri.parse(MyUtils.getDefaultOnlineQryURL() + resourcePath))
                .setMethod(HttpMethod.GET)
                .addHeader("Authorization", MyUtils.getBasicAuthForIMEI(mContext))
                .addHeader("x-smp-appid","com.arteriatech.mSecSales")
                .addHeader("Accept", "application/json")
                .setResponseListener(iResponseListener).setFlowListener(IConversationFlowListener
                .prepareFor()
                .communicationError(iCommunicationErrorListener).build())
                .start();
        return "";
    }
    public static void validateIMEILocal(AsyncTaskCallBack asyncTaskCallBack,Context context){
        new Thread(() -> {
            HttpsURLConnection connection = null;
            String resultJson = "";
            String fetchQry = "Application eq 'MSEC'";
//            String fetchQry = "Application eq 'MSEC' and AuthOrgTypeID eq '000038'";
//          String query = "Application eq 'MSEC' and AuthOrgTypeID eq '000038' and AuthOrgValue eq '"+ MSFAApplication.APP_DEVICEID+"'";
            String URL = "";
            try {
                URL = MyUtils.getDefaultOnlineQryURL()+"UserProfileAuthSet?$filter="+ URLEncoder.encode(fetchQry,"UTF-8");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            int responseCode = 0;
            try {
                URL url =  new URL(URL);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setReadTimeout(Configuration.connectionTimeOut);
                connection.setConnectTimeout(Configuration.connectionTimeOut);
                String userCredentials = USER_NAME_DEVICE_ID + ":" + USER_PASSWORD_DEVICE_ID;
                String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8), 2);
                connection.setRequestProperty("Authorization", basicAuth);
                connection.setRequestProperty("x-smp-appid", "com.arteriatech.mSecSales");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setDefaultUseCaches(false);
                connection.setAllowUserInteraction(false);
                connection.connect();
                responseCode = connection.getResponseCode();
                connection.getResponseMessage();
                InputStream stream = null;

                if (responseCode == 200) {
                    LogManager.writeLogInfo("Device ID validation success with 200 code");
                    stream = connection.getInputStream();
                    if (stream != null) {
                        resultJson = readResponse(stream);
                    }
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(resultJson);
                        JSONObject objectD = jsonObj.optJSONObject("d");
                        String value = "";
                        if (objectD!=null) {
                            JSONArray jsonArray = objectD.getJSONArray("results");
                            if(jsonArray.length()>0) {
                                JSONObject object = (JSONObject) jsonArray.get(0);
                                String applicationID = object.optString("Application");
                                String LoginID = object.optString("LoginID");
                                String ERPLoginID = object.optString("ERPSystemID");
                                String AuthOrgValue = "";
                                String APP_DEVICEID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    if (object1.optString("AuthOrgTypeID") != null && object1.optString("AuthOrgTypeID").equalsIgnoreCase("000038")) {
                                        if (object1.optString("AuthOrgValue") != null && object1.optString("AuthOrgValue").equalsIgnoreCase(APP_DEVICEID)) {
                                            AuthOrgValue = object1.optString("AuthOrgValue");
                                        }
                                    }
                                }
                                if (TextUtils.isEmpty(AuthOrgValue)){
                                    postUserAuthSet(context,applicationID,ERPLoginID);
                                }
//                                asyncTaskCallBack.onStatus(true, "");
                            }else {
                                asyncTaskCallBack.onStatus(false, "Device-ID not mapped. Please retry after mapping..!");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogManager.writeLogError("Device ID validation failed with error :"+e.toString());
                        asyncTaskCallBack.onStatus(false, e.getMessage());
                    }
                }else if(responseCode==401){
                    try {
                        Constants.getPasswordStatus(Configuration.IDPURL, USER_NAME_DEVICE_ID, USER_PASSWORD_DEVICE_ID, Configuration.APP_ID, new Constants.PasswordStatusCallback() {
                            @Override
                            public void passwordStatus(final JSONObject jsonObject) {
                                asyncTaskCallBack.onStatus(false, Constants.passwordStatusForIMEI(context, jsonObject,registrationModel,""));

                            }
                        });
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        asyncTaskCallBack.onStatus(false, String.valueOf(responseCode) +" "+connection.getResponseMessage());
                        LogManager.writeLogError("Device ID validation failed with response code :"+responseCode +" with error \n "+readResponse(connection.getErrorStream()));
                    } catch (Throwable e) {
                        if(e instanceof FileNotFoundException){
                            asyncTaskCallBack.onStatus(false, "Device-ID not mapped. Please retry after mapping..!");
                        }
                        LogManager.writeLogError("Device ID validation failed with response code :"+responseCode +" with error \n "+connection.getInputStream().toString());

                    }
                }

            } catch (Throwable e) {
                LogManager.writeLogError("Device ID validation failed with error :"+e.toString());
                asyncTaskCallBack.onStatus(false, e.toString());

            }
        }).start();
    }

    private static void postUserAuthSet(Context context,String applicationID, String ERPID) {
        String APP_DEVICEID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Application", applicationID);
            jsonObject.put("ErpSystemID", ERPID);
            jsonObject.put("LoginID", "");
            jsonObject.put("AuthOrgTypeID", "000038");
            jsonObject.put("AuthOrgValue", APP_DEVICEID);
            jsonObject.put("AuthOrgValDsc", "Device ID");
            /*OnlineManager.createEntity("", jsonObject.toString(), "DeviceIDSet", new UIListener() {
                @Override
                public void onRequestError(int i, Exception e) {
                    LogManager.writeLogError(e.toString());
                    if (e.getMessage().contains("entry already exists")) {
                        setAuthPrefs();
                    }
                }

                @Override
                public void onRequestSuccess(int i, String s) {
                    LogManager.writeLogInfo("Device-ID synced successfully");
                    setAuthPrefs();
                }
            }, this);*/
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static String readResponse(InputStream stream) throws IOException {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder buffer = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line).append('\n');
        }

        return buffer.toString();
    }
    /*public static ArrayList<RetailerBean> getRetailerList(String retListQry) throws OfflineODataStoreException {

        ArrayList<RetailerBean> retailerList = new ArrayList<>();
        OnlineStoreListener openListener = OnlineStoreListener.getInstance();
        OnlineODataStore store = openListener.getStore();
        if (store != null) {
            try {
                //Define the resource path

                ODataRequestParamSingle request = new ODataRequestParamSingleDefaultImpl();
                request.setMode(ODataRequestParamSingle.Mode.Read);
                request.setResourcePath(retListQry);
                //Send a request to read the ChannelPartners from the local database
                ODataResponseSingle response = (ODataResponseSingle) store.
                        executeRequest(request);
                //Check if the response is an error
                if (response.getPayloadType() == ODataPayload.Type.Error) {
                    ODataErrorDefaultImpl error = (ODataErrorDefaultImpl)
                            response.getPayload();
                    throw new OfflineODataStoreException(error.getMessage());
                    //Check if the response contains EntitySet
                } else if (response.getPayloadType() == ODataPayload.Type.EntitySet) {
                    ODataEntitySet feed = (ODataEntitySet) response.getPayload();
                    List<ODataEntity> entities = feed.getEntities();
                    retailerList.addAll(OfflineManager.getRetailerList(entities));
                } else {
                    throw new OfflineODataStoreException(Constants.invalid_payload_entityset_expected + response.getPayloadType().name());
                }
            } catch (Exception e) {
                throw new OfflineODataStoreException(e);
            }
        }
        return retailerList;

    }

    public static void createCollectionEntry(Hashtable<String, String> table, ArrayList<HashMap<String, String>> itemtable, UIListener uiListener) throws OnlineODataStoreException {
        OnlineStoreListener openListener = OnlineStoreListener.getInstance();
        OnlineODataStore store = openListener.getStore();

        if (store != null) {
            try {
                //Creates the entity payload
                ODataEntity collectionCreateEntity = createCollectionEntryEntity(table, itemtable, store);

                OnlineRequestListener collectionListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);

                String fipGUID32 = table.get(Constants.FIPGUID).replace("-", "");

                String collCreatedOn = table.get(Constants.CreatedOn);
                String collCreatedAt = table.get(Constants.CreatedAt);

                String mStrDateTime = UtilConstants.getReArrangeDateFormat(collCreatedOn) + Constants.T + UtilConstants.convertTimeOnly(collCreatedAt);

                Map<String, String> createHeaders = new HashMap<String, String>();
                createHeaders.put(Constants.RequestID, fipGUID32);
                createHeaders.put(Constants.RepeatabilityCreation, mStrDateTime);

                ODataRequestParamSingle collectionReq = new ODataRequestParamSingleDefaultImpl();
                collectionReq.setMode(ODataRequestParamSingle.Mode.Create);
                collectionReq.setResourcePath(collectionCreateEntity.getResourcePath());
                collectionReq.setPayload(collectionCreateEntity);
                collectionReq.getCustomHeaders().putAll(createHeaders);

                store.scheduleRequest(collectionReq, collectionListener);

            } catch (Exception e) {
                throw new OnlineODataStoreException(e);
            }
        }
        //END

    }

    *//**
     * Create Entity for collection creation
     *
     * @throws ODataParserException
     *//*
    private static ODataEntity createCollectionEntryEntity(Hashtable<String, String> hashtable, ArrayList<HashMap<String, String>> itemhashtable, OnlineODataStore store) throws ODataParserException {
        ODataEntity newHeaderEntity = null;
        ODataEntity newItemEntity = null;
        ArrayList<ODataEntity> tempArray = new ArrayList();
        try {
            if (hashtable != null) {
                newHeaderEntity = new ODataEntityDefaultImpl(UtilConstants.getNameSpace(OfflineManager.offlineStore)+""+Constants.FinancialPostingsEntity);

                newHeaderEntity.setResourcePath(Constants.FinancialPostings, Constants.FinancialPostings);

                try {
                    store.allocateProperties(newHeaderEntity, ODataStore.PropMode.All);
                } catch (ODataException e) {
                    e.printStackTrace();
                }
                //If available, it populates the navigation properties of an OData Entity
                store.allocateNavigationProperties(newHeaderEntity);

                newHeaderEntity.getProperties().put(Constants.FIPGUID,
                        new ODataPropertyDefaultImpl(Constants.FIPGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.FIPGUID))));

                try {
                    newHeaderEntity.getProperties().put(Constants.ExtRefID,
                            new ODataPropertyDefaultImpl(Constants.ExtRefID, hashtable.get(Constants.FIPGUID).replace("-","")));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                newHeaderEntity.getProperties().put(Constants.CPGUID,
                        new ODataPropertyDefaultImpl(Constants.CPGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.CPGUID))));

                try {
                    newHeaderEntity.getProperties().put(Constants.SPGUID,
                            new ODataPropertyDefaultImpl(Constants.SPGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.SPGuid))));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                newHeaderEntity.getProperties().put(Constants.CPNo,
                        new ODataPropertyDefaultImpl(Constants.CPNo, hashtable.get(Constants.CPNo)));
                if (!hashtable.get(Constants.BankID).equalsIgnoreCase("")) {
                    newHeaderEntity.getProperties().put(Constants.BankID,
                            new ODataPropertyDefaultImpl(Constants.BankID, hashtable.get(Constants.BankID)));

                    newHeaderEntity.getProperties().put(Constants.BankName,
                            new ODataPropertyDefaultImpl(Constants.BankName, hashtable.get(Constants.BankName)));
                }
                if (!hashtable.get(Constants.InstrumentNo).equalsIgnoreCase("")) {
                    newHeaderEntity.getProperties().put(Constants.InstrumentNo,
                            new ODataPropertyDefaultImpl(Constants.InstrumentNo, hashtable.get(Constants.InstrumentNo)));
                }

                if (!hashtable.get(Constants.InstrumentDate).equalsIgnoreCase("")) {
                    newHeaderEntity.getProperties().put(Constants.InstrumentDate,
                            new ODataPropertyDefaultImpl(Constants.InstrumentDate, UtilConstants.convertDateFormat(hashtable.get(Constants.InstrumentDate))));
                }
                newHeaderEntity.getProperties().put(Constants.Amount,
                        new ODataPropertyDefaultImpl(Constants.Amount, new BigDecimal(hashtable.get(Constants.Amount))));

                if (!hashtable.get(Constants.Remarks).equalsIgnoreCase("")) {
                    newHeaderEntity.getProperties().put(Constants.Remarks,
                            new ODataPropertyDefaultImpl(Constants.Remarks, hashtable.get(Constants.Remarks)));
                }
                newHeaderEntity.getProperties().put(Constants.FIPDocType,
                        new ODataPropertyDefaultImpl(Constants.FIPDocType, hashtable.get(Constants.FIPDocType)));

                try {
                    newHeaderEntity.getProperties().put(Constants.Source,
                            new ODataPropertyDefaultImpl(Constants.Source, hashtable.get(Constants.Source)));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                newHeaderEntity.getProperties().put(Constants.PaymentModeID,
                        new ODataPropertyDefaultImpl(Constants.PaymentModeID, hashtable.get(Constants.PaymentModeID)));

                newHeaderEntity.getProperties().put(Constants.FIPDate,
                        new ODataPropertyDefaultImpl(Constants.FIPDate, UtilConstants.convertDateFormat(hashtable.get(Constants.FIPDate))));

          *//*      newHeaderEntity.getProperties().put(Constants.LOGINID,
                        new ODataPropertyDefaultImpl(Constants.LOGINID, hashtable.get(Constants.LOGINID)));*//*
                newHeaderEntity.getProperties().put(Constants.Currency,
                        new ODataPropertyDefaultImpl(Constants.Currency, hashtable.get(Constants.Currency)));
                newHeaderEntity.getProperties().put(Constants.BranchName,
                        new ODataPropertyDefaultImpl(Constants.BranchName, hashtable.get(Constants.BranchName)));
                newHeaderEntity.getProperties().put(Constants.CPName,
                        new ODataPropertyDefaultImpl(Constants.CPName, hashtable.get(Constants.CPName)));
                newHeaderEntity.getProperties().put(Constants.ParentNo,
                        new ODataPropertyDefaultImpl(Constants.ParentNo, hashtable.get(Constants.ParentNo)));
                newHeaderEntity.getProperties().put(Constants.SPNo,
                        new ODataPropertyDefaultImpl(Constants.SPNo, hashtable.get(Constants.SPNo)));
                newHeaderEntity.getProperties().put(Constants.SPFirstName,
                        new ODataPropertyDefaultImpl(Constants.SPFirstName, hashtable.get(Constants.SPFirstName)));

                newHeaderEntity.getProperties().put(Constants.CPTypeID,
                        new ODataPropertyDefaultImpl(Constants.CPTypeID, hashtable.get(Constants.CPTypeID)));

                try {
                    if (!hashtable.get(Constants.BeatGUID).equalsIgnoreCase("")) {
                        newHeaderEntity.getProperties().put(Constants.BeatGUID,
                                new ODataPropertyDefaultImpl(Constants.BeatGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.BeatGUID))));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                int incremntVal = 0;
                for (int i = 0; i < itemhashtable.size(); i++) {

                    HashMap<String, String> singleRow = itemhashtable.get(i);

                    incremntVal = i + 1;
                    String itemNo = ConstantsUtils.addZeroBeforeValue(incremntVal, ConstantsUtils.ITEM_MAX_LENGTH);
                    newItemEntity = new ODataEntityDefaultImpl(UtilConstants.getNameSpace(OfflineManager.offlineStore)+""+Constants.FinancialPostingsItemEntity);

                    newItemEntity.setResourcePath(Constants.FinancialPostingItemDetails + "(" + itemNo + ")", Constants.FinancialPostingItemDetails + "(" + itemNo + ")");
                    try {
                        store.allocateProperties(newItemEntity, ODataStore.PropMode.Keys);
                    } catch (ODataException e) {
                        e.printStackTrace();
                    }


                    newItemEntity.getProperties().put(Constants.FIPItemGUID,
                            new ODataPropertyDefaultImpl(Constants.FIPItemGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.FIPItemGUID))));

                    newItemEntity.getProperties().put(Constants.FIPGUID,
                            new ODataPropertyDefaultImpl(Constants.FIPGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.FIPGUID))));

                    newItemEntity.getProperties().put(Constants.FIPItemNo,
                            new ODataPropertyDefaultImpl(Constants.FIPItemNo, itemNo));


                    newItemEntity.getProperties().put(Constants.ReferenceTypeID,
                            new ODataPropertyDefaultImpl(Constants.ReferenceTypeID, singleRow.get(Constants.ReferenceTypeID)));

*//*
                    newItemEntity.getProperties().put(Constants.LOGINID,
                            new ODataPropertyDefaultImpl(Constants.LOGINID, hashtable.get(Constants.LOGINID)));*//*


                    if (!singleRow.get(Constants.ReferenceID).equalsIgnoreCase("")) {
                        newItemEntity.getProperties().put(Constants.FIPAmount,
                                new ODataPropertyDefaultImpl(Constants.FIPAmount, new BigDecimal(singleRow.get(Constants.FIPAmount))));
                        newItemEntity.getProperties().put(Constants.ReferenceID,
                                new ODataPropertyDefaultImpl(Constants.ReferenceID, singleRow.get(Constants.ReferenceID).toUpperCase()));
                        newItemEntity.getProperties().put(Constants.Amount,
                                new ODataPropertyDefaultImpl(Constants.Amount, new BigDecimal(singleRow.get(Constants.Amount))));

                        if (singleRow.get(Constants.CashDiscountPercentage)!=null && !TextUtils.isEmpty(singleRow.get(Constants.CashDiscountPercentage))) {
                            newItemEntity.getProperties().put(Constants.CashDiscountPercentage,
                                    new ODataPropertyDefaultImpl(Constants.CashDiscountPercentage, new BigDecimal(singleRow.get(Constants.CashDiscountPercentage))));
                        }
                        if (singleRow.get(Constants.CashDiscount)!=null && !TextUtils.isEmpty(singleRow.get(Constants.CashDiscount))) {
                            newItemEntity.getProperties().put(Constants.CashDiscount,
                                    new ODataPropertyDefaultImpl(Constants.CashDiscount, new BigDecimal(singleRow.get(Constants.CashDiscount))));
                        }
                    } else {
                        newItemEntity.getProperties().put(Constants.FIPAmount,
                                new ODataPropertyDefaultImpl(Constants.FIPAmount, new BigDecimal(singleRow.get(Constants.FIPAmount))));
                    }

                    if (!singleRow.get(Constants.InstrumentDate).equalsIgnoreCase("")) {
                        newItemEntity.getProperties().put(Constants.InstrumentDate,
                                new ODataPropertyDefaultImpl(Constants.InstrumentDate, UtilConstants.convertDateFormat(singleRow.get(Constants.InstrumentDate))));
                    }

                    newItemEntity.getProperties().put(Constants.Currency,
                            new ODataPropertyDefaultImpl(Constants.Currency, singleRow.get(Constants.Currency)));

                    newItemEntity.getProperties().put(Constants.InstrumentNo,
                            new ODataPropertyDefaultImpl(Constants.InstrumentNo, singleRow.get(Constants.InstrumentNo)));

                    newItemEntity.getProperties().put(Constants.PaymentMode,
                            new ODataPropertyDefaultImpl(Constants.PaymentMode, singleRow.get(Constants.PaymentModeID)));

                    newItemEntity.getProperties().put(Constants.PaymetModeDesc,
                            new ODataPropertyDefaultImpl(Constants.PaymetModeDesc, singleRow.get(Constants.PaymetModeDesc)));

                    try {
                        if (!singleRow.get(Constants.BeatGUID).equalsIgnoreCase("")) {
                            newItemEntity.getProperties().put(Constants.BeatGUID,
                                    new ODataPropertyDefaultImpl(Constants.BeatGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.BeatGUID))));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    tempArray.add(i, newItemEntity);

                }

                ODataEntitySetDefaultImpl itemEntity = new ODataEntitySetDefaultImpl(tempArray.size(), null, null);
                for (ODataEntity entity : tempArray) {
                    itemEntity.getEntities().add(entity);
                }
                itemEntity.setResourcePath(Constants.FinancialPostingItemDetails);

                ODataNavigationProperty navProp = newHeaderEntity.getNavigationProperty(Constants.FinancialPostingItemDetails);
                navProp.setNavigationContent(itemEntity);
                newHeaderEntity.setNavigationProperty(Constants.FinancialPostingItemDetails, navProp);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newHeaderEntity;
    }*/
    public static void requestOnline(final GetOnlineODataInterface getOnlineODataInterface, final Bundle bundle, final Context mContext) throws Exception {
        String resourcePath = "";
        String sessionId = "";
        int operation = 0;
        int requestCode = 0;
        boolean isSessionRequired = false;
        boolean isSessionInResourceRequired = false;
        if (bundle == null) {
            throw new IllegalArgumentException("bundle is null");
        } else {
            resourcePath = bundle.getString(Constants.BUNDLE_RESOURCE_PATH, "");
            sessionId = bundle.getString(Constants.BUNDLE_SESSION_ID, "");
            operation = bundle.getInt(Constants.BUNDLE_OPERATION, 0);
            requestCode = bundle.getInt(Constants.BUNDLE_REQUEST_CODE, 0);
            //   isSessionRequired = bundle.getBoolean(Constants.BUNDLE_SESSION_REQUIRED, false);
            isSessionRequired = false;
            isSessionInResourceRequired = bundle.getBoolean(Constants.BUNDLE_SESSION_URL_REQUIRED, false);
        }
        if (TextUtils.isEmpty(resourcePath)) {
            throw new IllegalArgumentException("resource path is null");
        } else {
            final Map<String, String> createHeaders = new HashMap<String, String>();
//            createHeaders.put(Constants.arteria_dayfilter, Constants.NO_OF_DAYS);
            if (!TextUtils.isEmpty(sessionId)) {
                createHeaders.put(Constants.arteria_session_header, sessionId);
                if (isSessionInResourceRequired) {
                    resourcePath = String.format(resourcePath, sessionId);
                }
//                requestOnlineWithSessionId(resourcePath, operation, requestCode, createHeaders, getOnlineODataInterface, bundle);
            } else if (isSessionRequired) {
                final int finalOperation = operation;
                final String finalResourcePath = resourcePath;
                final int finalRequestCode = requestCode;
                final boolean finalIsSessionInResourceRequired = isSessionInResourceRequired;
                new GetSessionIdAsyncTask(mContext, new AsyncTaskCallBack() {
                    @Override
                    public void onStatus(boolean status, String values) {
                        String resourcePath = finalResourcePath;
                        if (status) {
                            if (UtilConstants.isNetworkAvailable(mContext)) {
                                if (finalIsSessionInResourceRequired) {
                                    resourcePath = String.format(resourcePath, values);
                                }
                                createHeaders.put(Constants.arteria_session_header, values);
                                try {
                                    bundle.putString(Constants.BUNDLE_SESSION_ID, values);
//                                    requestOnlineWithSessionId(resourcePath, finalOperation, finalRequestCode, createHeaders, getOnlineODataInterface, bundle);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    if (getOnlineODataInterface != null)
                                        getOnlineODataInterface.responseFailed(null, finalOperation, finalRequestCode, resourcePath, e.getMessage(), bundle);
                                }
                            } else {
                                if (getOnlineODataInterface != null)
                                    getOnlineODataInterface.responseFailed(null, finalOperation, finalRequestCode, resourcePath, mContext.getString(R.string.msg_no_network), bundle);
//                                throw new IllegalArgumentException(mContext.getString(R.string.msg_no_network));
                            }
                        } else {
                            if (getOnlineODataInterface != null)
                                getOnlineODataInterface.responseFailed(null, finalOperation, finalRequestCode, resourcePath, values, bundle);
//                            throw new IllegalArgumentException(values);
                        }
                    }
                }).execute();

            } else {
//                requestOnlineWithSessionId(resourcePath, operation, requestCode, createHeaders, getOnlineODataInterface, bundle);
            }
        }
    }

   /* private static void requestOnlineWithSessionId(String resourcePath, int operation, int requestCode, Map<String, String> createHeaders, GetOnlineODataInterface getOnlineODataInterface, Bundle bundle) throws ODataContractViolationException {
        OnlineStoreListener openListener = OnlineStoreListener.getInstance();
        OnlineODataStore store = openListener.getStore();
//        LogManager.writeLogInfo(Constants.ERROR_ARCHIVE_ENTRY_REQUEST_URL + " : " + resourcePath);
        if (store != null) {
            GetOnlineRequestListener getOnlineRequestListener = new GetOnlineRequestListener(getOnlineODataInterface, operation, requestCode, resourcePath, bundle);
            scheduleReadEntity(resourcePath, getOnlineRequestListener, createHeaders, store);
        } else {
            throw new IllegalArgumentException("Store not opened");
        }
    }

    private static ODataRequestExecution scheduleReadEntity(String resourcePath, ODataRequestListener listener, Map<String, String> options, OnlineODataStore store) throws ODataContractViolationException {
        if (TextUtils.isEmpty(resourcePath)) {
            throw new IllegalArgumentException("resourcePath is null");
        } else if (listener == null) {
            throw new IllegalArgumentException("listener is null");
        } else {
            ODataRequestParamSingleDefaultImpl requestParam = new ODataRequestParamSingleDefaultImpl();
            requestParam.setMode(ODataRequestParamSingle.Mode.Read);
            requestParam.setResourcePath(resourcePath);
            requestParam.setOptions(options);

            requestParam.getCustomHeaders().putAll(options);

            return store.scheduleRequest(requestParam, listener);
        }
    }*//*
    public static RetailerBean getRetailerApprovalDetails(ODataRequestExecution oDataRequestExecution,RetailerBean retailerBean) throws Exception {

        ODataProperty property;
        ODataPropMap properties;
        try {
            ODataResponseSingle oDataResponseSingle = (ODataResponseSingleDefaultImpl) oDataRequestExecution.getResponse();
            ODataEntity entity = (ODataEntity) oDataResponseSingle.getPayload();
            retailerBean = new RetailerBean();
            properties = entity.getProperties();
            property = properties.get(Constants.OwnerName);
            retailerBean.setOwnerName((String) property.getValue());

            property = properties.get(Constants.EmailID);
            if (property != null)
                retailerBean.setEmailId((String) property.getValue());

            property = properties.get(Constants.MobileNo);
            retailerBean.setMobileNumber((String) property.getValue());

            property = properties.get(Constants.WeeklyOffDesc);
            retailerBean.setWeeklyOffDesc((String) property.getValue());

            property = properties.get(Constants.Tax1);
            retailerBean.setTax1((String) property.getValue());

            String strAddress = SOUtils.getCustomerDetailsAddressValue(properties,null);
            retailerBean.setAddress1(strAddress);

//            property = properties.get(Constants.NetAmount);
//            String invAmtStr = "0";
//            if (property != null) {
//                BigDecimal mStrAmount = (BigDecimal) property.getValue();
//                invAmtStr = mStrAmount.toString();
//            }
//            retailerBean.setNetAmount(invAmtStr);

            property = properties.get(Constants.Latitude);
            BigDecimal mDecimalLatitude = (BigDecimal) property.getValue();  //---------> Decimal property
            double mDouLatVal;
            try {

                if (mDecimalLatitude != null) {
                    mDouLatVal = mDecimalLatitude.doubleValue();
                } else {
                    mDouLatVal = 0.0;
                }
            } catch (Exception e) {
                mDouLatVal = 0.0;
            }
            retailerBean.setLatVal(mDouLatVal);

            property = properties.get(Constants.Longitude);
            BigDecimal mDecimalLongitude = (BigDecimal) property.getValue();  //---------> Decimal property

            double mDouLongVal;

            try {
                if (mDecimalLongitude != null) {

                    mDouLongVal = mDecimalLongitude.doubleValue();
                } else {
                    mDouLongVal = 0.0;
                }
            } catch (Exception e) {
                mDouLongVal = 0.0;
            }
            retailerBean.setLatVal(mDouLongVal);

            String mStrLandmark;
            property = properties.get(Constants.Landmark);
            mStrLandmark = property.getValue() != null ? (String) property.getValue() : "";
            retailerBean.setLandMark(mStrLandmark);


            ODataNavigationProperty soItemDetailsProp = entity.getNavigationProperty(Constants.CPDMSDivisions);
            ODataEntitySet feed = (ODataEntitySet) soItemDetailsProp.getNavigationContent();
            List<ODataEntity> entities = feed.getEntities();
            for (ODataEntity soItemEntity : entities) {
                properties = soItemEntity.getProperties();
                property = properties.get(Constants.ParentName);
                retailerBean.setParentName(property.getValue().toString());

                property = properties.get(Constants.Group4Desc);
                retailerBean.setClassification(property.getValue().toString());
                property = properties.get(Constants.Group3Desc);
                retailerBean.setCPTypeDesc(property.getValue().toString());

                property = properties.get(Constants.Currency);
                retailerBean.setCurrency(property.getValue().toString());
            }
            return retailerBean;


        } catch (Exception e) {
            e.printStackTrace();
            throw new OnlineODataStoreException(e.getMessage());
        }
    }

    public static void updateTasksEntity(Hashtable<String, String> table, UIListener uiListener) throws com.arteriatech.ss.msec.bil.v2.mutils.common.OnlineODataStoreException {
        OnlineStoreListener openListener = OnlineStoreListener.getInstance();
        OnlineODataStore store = openListener.getStore();

        if (store != null) {
            try {
                //Creates the entity payload
                ODataEntity soCreateEntity = createTaskEntity(store, table);

                OnlineRequestListener collectionListener = new OnlineRequestListener(Operation.Update.getValue(), uiListener);

                ODataRequestParamSingle collectionReq = new ODataRequestParamSingleDefaultImpl();
                collectionReq.setMode(ODataRequestParamSingle.Mode.Update);
                collectionReq.setResourcePath(soCreateEntity.getResourcePath());
                collectionReq.setPayload(soCreateEntity);

                final Map<String, String> createHeaders = new HashMap<>();
            *//*    if (!TextUtils.isEmpty(sessionId)) {
                    createHeaders.put(Constants.arteria_session_header, sessionId);
                }*//*
                collectionReq.getCustomHeaders().putAll(createHeaders);

                store.scheduleRequest(collectionReq, collectionListener);

            } catch (Exception e) {
                throw new com.arteriatech.ss.msec.bil.v2.mutils.common.OnlineODataStoreException(e);
            }
        }

    }
    private static ODataEntity createTaskEntity(OnlineODataStore store, Hashtable<String, String> hashtable) throws ODataParserException {
        ODataEntity headerEntity = null;
//        ArrayList<ODataEntity> tempArray = new ArrayList();
        try {
            if (hashtable != null) {
                // CreateOperation the parent Entity
                headerEntity = new ODataEntityDefaultImpl(UtilConstants.getNameSpaceOnline(store) + "" + Constants.SOS_SO_TASK_ENTITY);

                headerEntity.setResourcePath(Constants.Tasks + "(InstanceID='" + hashtable.get(Constants.InstanceID) + "',EntityType='SO')", Constants.Tasks + "(InstanceID='" + hashtable.get(Constants.InstanceID) + "',EntityType='CP')");


                try {
                    store.allocateProperties(headerEntity, ODataStore.PropMode.All);
                } catch (ODataException e) {
                    e.printStackTrace();
                }

                store.allocateNavigationProperties(headerEntity);

                headerEntity.getProperties().put(Constants.InstanceID,
                        new ODataPropertyDefaultImpl(Constants.InstanceID, hashtable.get(Constants.InstanceID)));
                headerEntity.getProperties().put(Constants.EntityType,
                        new ODataPropertyDefaultImpl(Constants.EntityType, hashtable.get(Constants.EntityType)));
                headerEntity.getProperties().put(Constants.DecisionKey,
                        new ODataPropertyDefaultImpl(Constants.DecisionKey, hashtable.get(Constants.DecisionKey)));

                headerEntity.getProperties().put(Constants.LoginID,
                        new ODataPropertyDefaultImpl(Constants.LoginID, hashtable.get(Constants.LoginID)));
                headerEntity.getProperties().put(Constants.EntityKey,
                        new ODataPropertyDefaultImpl(Constants.EntityKey, hashtable.get(Constants.EntityKey)));

                headerEntity.getProperties().put(Constants.Comments,
                        new ODataPropertyDefaultImpl(Constants.Comments, hashtable.get(Constants.Comments)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return headerEntity;

    }

    public static void createSSInvoiceEntity(Hashtable<String, String> table, ArrayList<HashMap<String, String>> itemtable, UIListener uiListener) throws OnlineODataStoreException {
        OnlineStoreListener openListener = OnlineStoreListener.getInstance();
        OnlineODataStore store = openListener.getStore();

        if (store != null) {
            try {
                //Creates the entity payload
                ODataEntity soCreateEntity = createSSInvoiceCreateEntity(table, itemtable, store);

                OnlineRequestListener collectionListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);

                ODataRequestParamSingle collectionReq = new ODataRequestParamSingleDefaultImpl();
                collectionReq.setMode(ODataRequestParamSingle.Mode.Create);
                collectionReq.setResourcePath(soCreateEntity.getResourcePath());
                collectionReq.setPayload(soCreateEntity);

                store.scheduleRequest(collectionReq, collectionListener);

            } catch (Exception e) {
                throw new OnlineODataStoreException(e);
            }
        }
        //END

    }

    private static ODataEntity createSSInvoiceCreateEntity(Hashtable<String, String> hashtable, ArrayList<HashMap<String, String>> itemhashtable, OnlineODataStore store) throws ODataParserException {
        ODataEntity newHeaderEntity = null;
        ODataEntity newItemEntity = null;
        ArrayList<ODataEntity> tempArray = new ArrayList();
        try {
            if (hashtable != null) {
                newHeaderEntity = new ODataEntityDefaultImpl(Constants.getNameSpaceOnline(store)+""+Constants.InvoiceEntity);

                newHeaderEntity.setResourcePath(Constants.SSINVOICES, Constants.SSINVOICES);

                try {
                    store.allocateProperties(newHeaderEntity, ODataStore.PropMode.All);
                } catch (ODataException e) {
                    e.printStackTrace();
                }
                //If available, it populates the navigation properties of an OData Entity
                store.allocateNavigationProperties(newHeaderEntity);

                newHeaderEntity.getProperties().put(Constants.InvoiceGUID,
                        new ODataPropertyDefaultImpl(Constants.InvoiceGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.InvoiceGUID))));

                try {
                    newHeaderEntity.getProperties().put(Constants.SPGUID,
                            new ODataPropertyDefaultImpl(Constants.SPGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.SPGUID))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
          *//*      newHeaderEntity.getProperties().put(Constants.LoginID,
                        new ODataPropertyDefaultImpl(Constants.LoginID, hashtable.get(Constants.LoginID)));*//*
                newHeaderEntity.getProperties().put(Constants.CPGUID,
                        new ODataPropertyDefaultImpl(Constants.CPGUID, hashtable.get(Constants.CPGUID)));
                newHeaderEntity.getProperties().put(Constants.CPNo,
                        new ODataPropertyDefaultImpl(Constants.CPNo, hashtable.get(Constants.CPNo)));
                newHeaderEntity.getProperties().put(Constants.CPName,
                        new ODataPropertyDefaultImpl(Constants.CPName, hashtable.get(Constants.CPName)));

                newHeaderEntity.getProperties().put(Constants.DmsDivision,
                        new ODataPropertyDefaultImpl(Constants.DmsDivision, hashtable.get(Constants.DmsDivision)));
                newHeaderEntity.getProperties().put(Constants.DmsDivisionDesc,
                        new ODataPropertyDefaultImpl(Constants.DmsDivisionDesc, hashtable.get(Constants.DmsDivisionDesc)));

                newHeaderEntity.getProperties().put(Constants.CPTypeDesc,
                        new ODataPropertyDefaultImpl(Constants.CPTypeDesc, hashtable.get(Constants.CPTypeDesc)));
                newHeaderEntity.getProperties().put(Constants.CPTypeID,
                        new ODataPropertyDefaultImpl(Constants.CPTypeID, hashtable.get(Constants.CPTypeID)));
//
                newHeaderEntity.getProperties().put(Constants.SPNo,
                        new ODataPropertyDefaultImpl(Constants.SPNo, hashtable.get(Constants.SPNo)));
                newHeaderEntity.getProperties().put(Constants.SPName,
                        new ODataPropertyDefaultImpl(Constants.SPName, hashtable.get(Constants.SPName)));
                newHeaderEntity.getProperties().put(Constants.InvoiceNo,
                        new ODataPropertyDefaultImpl(Constants.InvoiceNo, hashtable.get(Constants.InvoiceNo)));
                newHeaderEntity.getProperties().put(Constants.InvoiceTypeID,
                        new ODataPropertyDefaultImpl(Constants.InvoiceTypeID, hashtable.get(Constants.InvoiceTypeID)));
                newHeaderEntity.getProperties().put(Constants.InvoiceTypeDesc,
                        new ODataPropertyDefaultImpl(Constants.InvoiceTypeDesc, hashtable.get(Constants.InvoiceTypeDesc)));
                newHeaderEntity.getProperties().put(Constants.InvoiceDate,
                        new ODataPropertyDefaultImpl(Constants.InvoiceDate, UtilConstants.convertDateFormat(hashtable.get(Constants.InvoiceDate))));

                newHeaderEntity.getProperties().put(Constants.PONo,
                        new ODataPropertyDefaultImpl(Constants.PONo, hashtable.get(Constants.PONo)));
                newHeaderEntity.getProperties().put(Constants.PODate,
                        new ODataPropertyDefaultImpl(Constants.PODate, UtilConstants.convertDateFormat(hashtable.get(Constants.PODate))));


                newHeaderEntity.getProperties().put(Constants.SoldToCPGUID,
                        new ODataPropertyDefaultImpl(Constants.SoldToCPGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.SoldToCPGUID))));


                newHeaderEntity.getProperties().put(Constants.SoldToID,
                        new ODataPropertyDefaultImpl(Constants.SoldToID, hashtable.get(Constants.SoldToID)));
                newHeaderEntity.getProperties().put(Constants.SoldToName,
                        new ODataPropertyDefaultImpl(Constants.SoldToName, hashtable.get(Constants.SoldToName)));
                newHeaderEntity.getProperties().put(Constants.SoldToTypeID,
                        new ODataPropertyDefaultImpl(Constants.SoldToTypeID, hashtable.get(Constants.SoldToTypeID)));
                newHeaderEntity.getProperties().put(Constants.SoldToTypeDesc,
                        new ODataPropertyDefaultImpl(Constants.SoldToTypeDesc, hashtable.get(Constants.SoldToTypeDesc)));
                newHeaderEntity.getProperties().put(Constants.Currency,
                        new ODataPropertyDefaultImpl(Constants.Currency, hashtable.get(Constants.Currency)));

                try {
                    if (!hashtable.get(Constants.BeatGUID).equalsIgnoreCase("")) {
                        newHeaderEntity.getProperties().put(Constants.BeatGUID,
                                new ODataPropertyDefaultImpl(Constants.BeatGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.BeatGUID))));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                int incremntVal = 0;
                for (int incrementVal = 0; incrementVal < itemhashtable.size(); incrementVal++) {

                    HashMap<String, String> singleRow = itemhashtable.get(incrementVal);

                    incremntVal = incrementVal + 1;

                    newItemEntity = new ODataEntityDefaultImpl(Constants.getNameSpaceOnline(store)+""+Constants.InvoiceItemEntity);

                    newItemEntity.setResourcePath(Constants.SSInvoiceItemDetails + "(" + incremntVal + ")", Constants.SSInvoiceItemDetails + "(" + incremntVal + ")");
                    try {
                        store.allocateProperties(newItemEntity, ODataStore.PropMode.Keys);
                    } catch (ODataException e) {
                        e.printStackTrace();
                    }


                    newItemEntity.getProperties().put(Constants.InvoiceItemGUID,
                            new ODataPropertyDefaultImpl(Constants.InvoiceItemGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.InvoiceItemGUID))));

                    newItemEntity.getProperties().put(Constants.InvoiceGUID,
                            new ODataPropertyDefaultImpl(Constants.InvoiceGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.InvoiceGUID))));

                    newItemEntity.getProperties().put(Constants.StockGuid,
                            new ODataPropertyDefaultImpl(Constants.StockGuid, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.StockGuid))));

                    newItemEntity.getProperties().put(Constants.ItemNo,
                            new ODataPropertyDefaultImpl(Constants.ItemNo, singleRow.get(Constants.ItemNo)));

                    newItemEntity.getProperties().put(Constants.InvoiceNo,
                            new ODataPropertyDefaultImpl(Constants.InvoiceNo, singleRow.get(Constants.InvoiceNo)));

                    newItemEntity.getProperties().put(Constants.Remarks,
                            new ODataPropertyDefaultImpl(Constants.Remarks, singleRow.get(Constants.Remarks)));

                    newItemEntity.getProperties().put(Constants.Quantity,
                            new ODataPropertyDefaultImpl(Constants.Quantity, BigDecimal.valueOf(Double.parseDouble(singleRow.get(Constants.Quantity)))));


                    newItemEntity.getProperties().put(Constants.MaterialNo,
                            new ODataPropertyDefaultImpl(Constants.MaterialNo, singleRow.get(Constants.MaterialNo)));

                    newItemEntity.getProperties().put(Constants.MaterialDesc,
                            new ODataPropertyDefaultImpl(Constants.MaterialDesc, singleRow.get(Constants.MaterialDesc)));

                    newItemEntity.getProperties().put(Constants.UOM,
                            new ODataPropertyDefaultImpl(Constants.UOM, singleRow.get(Constants.UOM)));

                    newItemEntity.getProperties().put(Constants.Currency,
                            new ODataPropertyDefaultImpl(Constants.Currency, singleRow.get(Constants.Currency)));

                    newItemEntity.getProperties().put(Constants.InvoiceDate,
                            new ODataPropertyDefaultImpl(Constants.InvoiceDate, UtilConstants.convertDateFormat(singleRow.get(Constants.InvoiceDate))));

                    try {
                        if (!singleRow.get(Constants.BeatGUID).equalsIgnoreCase("")) {
                            newItemEntity.getProperties().put(Constants.BeatGUID,
                                    new ODataPropertyDefaultImpl(Constants.BeatGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.BeatGUID))));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    tempArray.add(incrementVal, newItemEntity);

                }

                ODataEntitySetDefaultImpl itemEntity = new ODataEntitySetDefaultImpl(tempArray.size(), null, null);
                for (ODataEntity entity : tempArray) {
                    itemEntity.getEntities().add(entity);
                }
                itemEntity.setResourcePath(Constants.SSInvoiceItemDetails);

                ODataNavigationProperty navProp = newHeaderEntity.getNavigationProperty(Constants.SSInvoiceItemDetails);
                navProp.setNavigationContent(itemEntity);
                newHeaderEntity.setNavigationProperty(Constants.SSInvoiceItemDetails, navProp);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newHeaderEntity;
    }

    public static void createCP(Hashtable<String, String> tableHdr, ArrayList<HashMap<String, String>> itemtable, UIListener uiListener) throws OnlineODataStoreException {
        OnlineStoreListener openListener = OnlineStoreListener.getInstance();
        OnlineODataStore store = openListener.getStore();

        if (store != null) {
            try {
                //Creates the entity payload
                ODataEntity cpEntity = createCPEntity(tableHdr, itemtable, store);

                OnlineRequestListener CPListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);


                String cpGUID32 = tableHdr.get(Constants.CPGUID).replace("-", "");

                String mStrCreatedOn = tableHdr.get(Constants.CreatedOn);
                String mStrCreatedAt = tableHdr.get(Constants.CreatedAt);

                String mStrDateTime = UtilConstants.getReArrangeDateFormat(mStrCreatedOn) + "T" + UtilConstants.convertTimeOnly(mStrCreatedAt);


                Map<String, String> collHeaders = new HashMap<String, String>();
                collHeaders.put(Constants.RequestID, cpGUID32);
                collHeaders.put(Constants.RepeatabilityCreation, mStrDateTime);

                ODataRequestParamSingle cpReq = new ODataRequestParamSingleDefaultImpl();
                cpReq.setMode(ODataRequestParamSingle.Mode.Create);
                cpReq.setResourcePath(cpEntity.getResourcePath());
                cpReq.setPayload(cpEntity);
                cpReq.getCustomHeaders().putAll(collHeaders);

                store.scheduleRequest(cpReq, CPListener);

            } catch (Exception e) {
                throw new OnlineODataStoreException(e);
            }
        }
        //END

    }
    private static ODataEntity createCPEntity(Hashtable<String, String> hashtable, ArrayList<HashMap<String, String>> itemhashtable, OnlineODataStore store) throws ODataParserException {
        ODataEntity headerEntity = null;
        ArrayList<ODataEntity> tempArray = new ArrayList();
        ArrayList<ODataEntity> tempCPPartnerArray = new ArrayList();
        ArrayList<String> mALCPFunctionID = new ArrayList<>();
        mALCPFunctionID.add("01");
        mALCPFunctionID.add("02");
        try {
            if (hashtable != null) {
                // CreateOperation the parent Entity
                headerEntity = new ODataEntityDefaultImpl(UtilConstants.getNameSpaceOnline(store)+""+Constants.ChannelPartnerEntity);
                headerEntity.setResourcePath(Constants.ChannelPartners, Constants.ChannelPartners);
                try {
                    store.allocateProperties(headerEntity, ODataStore.PropMode.All);
                } catch (ODataException e) {
                    e.printStackTrace();
                }
                store.allocateNavigationProperties(headerEntity);

                headerEntity.getProperties().put(Constants.CPGUID,
                        new ODataPropertyDefaultImpl(Constants.CPGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.CPGUID).toUpperCase())));
                headerEntity.getProperties().put(Constants.Address1,
                        new ODataPropertyDefaultImpl(Constants.Address1, hashtable.get(Constants.Address1)));
                headerEntity.getProperties().put(Constants.Address2,
                        new ODataPropertyDefaultImpl(Constants.Address2, hashtable.get(Constants.Address2)));
                headerEntity.getProperties().put(Constants.Address3,
                        new ODataPropertyDefaultImpl(Constants.Address3, hashtable.get(Constants.Address3)));
                headerEntity.getProperties().put(Constants.Address4,
                        new ODataPropertyDefaultImpl(Constants.Address4, hashtable.get(Constants.Address4)));
                headerEntity.getProperties().put(Constants.Currency,
                        new ODataPropertyDefaultImpl(Constants.Currency, hashtable.get(Constants.Currency)));
                headerEntity.getProperties().put(Constants.Country,
                        new ODataPropertyDefaultImpl(Constants.Country, hashtable.get(Constants.Country)));
                headerEntity.getProperties().put(Constants.DistrictDesc,
                        new ODataPropertyDefaultImpl(Constants.DistrictDesc, hashtable.get(Constants.DistrictDesc)));
                headerEntity.getProperties().put(Constants.DistrictID,
                        new ODataPropertyDefaultImpl(Constants.DistrictID, hashtable.get(Constants.DistrictID)));
                headerEntity.getProperties().put(Constants.StateID,
                        new ODataPropertyDefaultImpl(Constants.StateID, hashtable.get(Constants.StateID)));
                headerEntity.getProperties().put(Constants.StateDesc,
                        new ODataPropertyDefaultImpl(Constants.StateDesc, hashtable.get(Constants.StateDesc)));
                headerEntity.getProperties().put(Constants.CityID,
                        new ODataPropertyDefaultImpl(Constants.CityID, hashtable.get(Constants.CityID)));
                headerEntity.getProperties().put(Constants.CityDesc,
                        new ODataPropertyDefaultImpl(Constants.CityDesc, hashtable.get(Constants.CityDesc)));
                headerEntity.getProperties().put(Constants.Landmark,
                        new ODataPropertyDefaultImpl(Constants.Landmark, hashtable.get(Constants.Landmark)));
                headerEntity.getProperties().put(Constants.PostalCode,
                        new ODataPropertyDefaultImpl(Constants.PostalCode, hashtable.get(Constants.PostalCode)));
                headerEntity.getProperties().put(Constants.MobileNo,
                        new ODataPropertyDefaultImpl(Constants.MobileNo, hashtable.get(Constants.MobileNo)));
                headerEntity.getProperties().put(Constants.Mobile2,
                        new ODataPropertyDefaultImpl(Constants.Mobile2, hashtable.get(Constants.Mobile2)));
                headerEntity.getProperties().put(Constants.Landline,
                        new ODataPropertyDefaultImpl(Constants.Landline, hashtable.get(Constants.Landline)));
                headerEntity.getProperties().put(Constants.EmailID,
                        new ODataPropertyDefaultImpl(Constants.EmailID, hashtable.get(Constants.EmailID)));
                headerEntity.getProperties().put(Constants.PAN,
                        new ODataPropertyDefaultImpl(Constants.PAN, hashtable.get(Constants.PAN)));
                headerEntity.getProperties().put(Constants.VATNo,
                        new ODataPropertyDefaultImpl(Constants.VATNo, hashtable.get(Constants.VATNo)));
                headerEntity.getProperties().put(Constants.OutletName,
                        new ODataPropertyDefaultImpl(Constants.OutletName, hashtable.get(Constants.OutletName)));
                headerEntity.getProperties().put(Constants.OwnerName,
                        new ODataPropertyDefaultImpl(Constants.OwnerName, hashtable.get(Constants.OwnerName)));
                headerEntity.getProperties().put(Constants.RetailerProfile,
                        new ODataPropertyDefaultImpl(Constants.RetailerProfile, hashtable.get(Constants.RetailerProfile)));
                try {
                    if(!hashtable.get(Constants.DOB).equalsIgnoreCase("")) {
                        headerEntity.getProperties().put(Constants.DOB,
                                new ODataPropertyDefaultImpl(Constants.DOB, UtilConstants.convertDateFormat(hashtable.get(Constants.DOB))));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    headerEntity.getProperties().put(Constants.Latitude,
                            new ODataPropertyDefaultImpl(Constants.Latitude, BigDecimal.valueOf(Double.parseDouble(hashtable.get(Constants.Latitude)))));
                    headerEntity.getProperties().put(Constants.Longitude,
                            new ODataPropertyDefaultImpl(Constants.Longitude, BigDecimal.valueOf(Double.parseDouble(hashtable.get(Constants.Longitude)))));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    if(!hashtable.get(Constants.Anniversary).equalsIgnoreCase("")) {
                        headerEntity.getProperties().put(Constants.Anniversary,
                                new ODataPropertyDefaultImpl(Constants.Anniversary, UtilConstants.convertDateFormat(hashtable.get(Constants.Anniversary))));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    headerEntity.getProperties().put(Constants.PartnerMgrGUID,
                            new ODataPropertyDefaultImpl(Constants.PartnerMgrGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.PartnerMgrGUID).toUpperCase())));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                headerEntity.getProperties().put(Constants.CPTypeID,
                        new ODataPropertyDefaultImpl(Constants.CPTypeID, hashtable.get(Constants.CPTypeID)));
                headerEntity.getProperties().put(Constants.CPTypeDesc,
                        new ODataPropertyDefaultImpl(Constants.CPTypeDesc, hashtable.get(Constants.CPTypeDesc)));

                headerEntity.getProperties().put(Constants.RouteID,
                        new ODataPropertyDefaultImpl(Constants.RouteID, hashtable.get(Constants.RouteID)));
                headerEntity.getProperties().put(Constants.ParentID,
                        new ODataPropertyDefaultImpl(Constants.ParentID, hashtable.get(Constants.ParentID)));
                headerEntity.getProperties().put(Constants.ParentTypeID,
                        new ODataPropertyDefaultImpl(Constants.ParentTypeID, hashtable.get(Constants.ParentTypeID)));
                headerEntity.getProperties().put(Constants.ParentName,
                        new ODataPropertyDefaultImpl(Constants.ParentName, hashtable.get(Constants.ParentName)));

                headerEntity.getProperties().put(Constants.WeeklyOff,
                        new ODataPropertyDefaultImpl(Constants.WeeklyOff, hashtable.get(Constants.WeeklyOff)));
                headerEntity.getProperties().put(Constants.Tax1,
                        new ODataPropertyDefaultImpl(Constants.Tax1, hashtable.get(Constants.Tax1)));
                headerEntity.getProperties().put(Constants.Tax2,
                        new ODataPropertyDefaultImpl(Constants.Tax2, hashtable.get(Constants.Tax2)));
                headerEntity.getProperties().put(Constants.Tax3,
                        new ODataPropertyDefaultImpl(Constants.Tax3, hashtable.get(Constants.Tax3)));
                headerEntity.getProperties().put(Constants.Tax4,
                        new ODataPropertyDefaultImpl(Constants.Tax4, hashtable.get(Constants.Tax4)));
                headerEntity.getProperties().put(Constants.CPUID,
                        new ODataPropertyDefaultImpl(Constants.CPUID, hashtable.get(Constants.CPUID)));
                headerEntity.getProperties().put(Constants.TaxRegStatus,
                        new ODataPropertyDefaultImpl(Constants.TaxRegStatus, hashtable.get(Constants.TaxRegStatus)));
                headerEntity.getProperties().put(Constants.IsKeyCP,
                        new ODataPropertyDefaultImpl(Constants.IsKeyCP, hashtable.get(Constants.IsKeyCP)));

                headerEntity.getProperties().put(Constants.ID1,
                        new ODataPropertyDefaultImpl(Constants.ID1, hashtable.get(Constants.ID1)));
                headerEntity.getProperties().put(Constants.ID2,
                        new ODataPropertyDefaultImpl(Constants.ID2, hashtable.get(Constants.ID2)));
                headerEntity.getProperties().put(Constants.BusinessID1,
                        new ODataPropertyDefaultImpl(Constants.BusinessID1, hashtable.get(Constants.BusinessID1)));
                headerEntity.getProperties().put(Constants.BusinessID2,
                        new ODataPropertyDefaultImpl(Constants.BusinessID2, hashtable.get(Constants.BusinessID2)));

                // CreateOperation the item Entity



                for (int i = 0; i < itemhashtable.size(); i++) {
                    ODataEntity itemEntity = new ODataEntityDefaultImpl(UtilConstants.getNameSpaceOnline(store)+""+Constants.CPDMSDivisionEntity);
                    try {
                        store.allocateProperties(itemEntity, ODataStore.PropMode.All);
                    } catch (ODataException e) {
                        e.printStackTrace();
                    }

                    HashMap<String, String> singleRow = itemhashtable.get(i);
                    try {
                        store.allocateProperties(itemEntity, ODataStore.PropMode.All);
                    } catch (ODataException e) {
                        e.printStackTrace();
                    }
                    itemEntity.getProperties().put(Constants.CPGUID,
                            new ODataPropertyDefaultImpl(Constants.CPGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.CPGUID).toUpperCase())));
                    itemEntity.getProperties().put(Constants.CP1GUID,
                            new ODataPropertyDefaultImpl(Constants.CP1GUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.CP1GUID).toString().toUpperCase())));

                    try {
                        itemEntity.getProperties().put(Constants.PartnerMgrGUID,
                                new ODataPropertyDefaultImpl(Constants.PartnerMgrGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.PartnerMgrGUID).toUpperCase())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        itemEntity.getProperties().put(Constants.RouteGUID,
                                new ODataPropertyDefaultImpl(Constants.RouteGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.RouteGUID).toUpperCase())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    itemEntity.getProperties().put(Constants.DMSDivision,
                            new ODataPropertyDefaultImpl(Constants.DMSDivision, singleRow.get(Constants.DMSDivision)));
                    itemEntity.getProperties().put(Constants.StatusID,
                            new ODataPropertyDefaultImpl(Constants.StatusID, singleRow.get(Constants.StatusID)));
                    itemEntity.getProperties().put(Constants.PartnerMgrNo,
                            new ODataPropertyDefaultImpl(Constants.PartnerMgrNo, singleRow.get(Constants.PartnerMgrNo)));
                    itemEntity.getProperties().put(Constants.ParentID,
                            new ODataPropertyDefaultImpl(Constants.ParentID, singleRow.get(Constants.ParentID)));
                    itemEntity.getProperties().put(Constants.ParentTypeID,
                            new ODataPropertyDefaultImpl(Constants.ParentTypeID, singleRow.get(Constants.ParentTypeID)));
                    itemEntity.getProperties().put(Constants.ParentName,
                            new ODataPropertyDefaultImpl(Constants.ParentName, singleRow.get(Constants.ParentName)));
                    itemEntity.getProperties().put(Constants.Group1,
                            new ODataPropertyDefaultImpl(Constants.Group1, singleRow.get(Constants.Group1)));
                    itemEntity.getProperties().put(Constants.Group2,
                            new ODataPropertyDefaultImpl(Constants.Group2, singleRow.get(Constants.Group2)));
                    itemEntity.getProperties().put(Constants.Group3,
                            new ODataPropertyDefaultImpl(Constants.Group3, singleRow.get(Constants.Group3)));
                    itemEntity.getProperties().put(Constants.Group4,
                            new ODataPropertyDefaultImpl(Constants.Group4, singleRow.get(Constants.Group4)));
                    itemEntity.getProperties().put(Constants.Group5,
                            new ODataPropertyDefaultImpl(Constants.Group5, singleRow.get(Constants.Group5)));
                    itemEntity.getProperties().put(Constants.Currency,
                            new ODataPropertyDefaultImpl(Constants.Currency, singleRow.get(Constants.Currency)));
                    itemEntity.getProperties().put(Constants.RouteID,
                            new ODataPropertyDefaultImpl(Constants.RouteID, singleRow.get(Constants.RouteID)));
                    itemEntity.getProperties().put(Constants.RouteDesc,
                            new ODataPropertyDefaultImpl(Constants.RouteDesc, singleRow.get(Constants.RouteDesc)));

                    try {
                        itemEntity.getProperties().put(Constants.DiscountPer,
                                new ODataPropertyDefaultImpl(Constants.DiscountPer, BigDecimal.valueOf(Double.parseDouble(singleRow.get(Constants.DiscountPer)))));
                    } catch (NumberFormatException e) {
                        itemEntity.getProperties().put(Constants.DiscountPer,
                                new ODataPropertyDefaultImpl(Constants.DiscountPer, BigDecimal.valueOf(Double.parseDouble("0.00"))));
                        e.printStackTrace();
                    }
                    try {
                        itemEntity.getProperties().put(Constants.CreditLimit,
                                new ODataPropertyDefaultImpl(Constants.CreditLimit, BigDecimal.valueOf(Double.parseDouble(singleRow.get(Constants.CreditLimit)))));
                    } catch (NumberFormatException e) {
                        itemEntity.getProperties().put(Constants.CreditLimit,
                                new ODataPropertyDefaultImpl(Constants.CreditLimit, BigDecimal.valueOf(Double.parseDouble("0.00"))));
                        e.printStackTrace();
                    }
                    try {
                        itemEntity.getProperties().put(Constants.CreditDays,
                                new ODataPropertyDefaultImpl(Constants.CreditDays, Integer.valueOf(Integer.parseInt(singleRow.get(Constants.CreditDays)))));
                    } catch (NumberFormatException e) {
                        itemEntity.getProperties().put(Constants.CreditDays,
                                new ODataPropertyDefaultImpl(Constants.CreditDays, Integer.valueOf(Integer.parseInt("0"))));
                        e.printStackTrace();
                    }
                    try {
                        itemEntity.getProperties().put(Constants.CreditBills,
                                new ODataPropertyDefaultImpl(Constants.CreditBills, Integer.valueOf(Integer.parseInt(singleRow.get(Constants.CreditBills)))));
                    } catch (NumberFormatException e) {
                        itemEntity.getProperties().put(Constants.CreditBills,
                                new ODataPropertyDefaultImpl(Constants.CreditBills, Integer.valueOf(Integer.parseInt("0"))));
                        e.printStackTrace();
                    }

                    tempArray.add(i, itemEntity);
                }

                ODataEntitySetDefaultImpl itmEntity = new ODataEntitySetDefaultImpl(tempArray.size(), null, null);
                for (ODataEntity entity : tempArray) {
                    itmEntity.getEntities().add(entity);
                }
                itmEntity.setResourcePath(Constants.CPDMSDivisions);


                ODataNavigationProperty navProp = headerEntity.getNavigationProperty(Constants.CPDMSDivisions);
                navProp.setNavigationContent(itmEntity);
                headerEntity.setNavigationProperty(Constants.CPDMSDivisions, navProp);


                try {

                    for (int i = 0; i < mALCPFunctionID.size(); i++) {
                        ODataEntity itemCPPartnerEntity = new ODataEntityDefaultImpl(UtilConstants.getNameSpaceOnline(store)+""+Constants.CPPartnerFunctionEntity);
                        try {
                            store.allocateProperties(itemCPPartnerEntity, ODataStore.PropMode.All);
                        } catch (ODataException e) {
                            e.printStackTrace();
                        }

                        String cpID= mALCPFunctionID.get(i);
                        try {
                            store.allocateProperties(itemCPPartnerEntity, ODataStore.PropMode.All);
                        } catch (ODataException e) {
                            e.printStackTrace();
                        }
                        itemCPPartnerEntity.getProperties().put(Constants.CPGUID,
                                new ODataPropertyDefaultImpl(Constants.CPGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.CPGUID).toUpperCase())));

                        GUID guidItem = GUID.newRandom();
                        itemCPPartnerEntity.getProperties().put(Constants.PFGUID,
                                new ODataPropertyDefaultImpl(Constants.PFGUID, ODataGuidDefaultImpl.initWithString32(guidItem.toString().toUpperCase())));

                        itemCPPartnerEntity.getProperties().put(Constants.PartnerFunction,
                                new ODataPropertyDefaultImpl(Constants.PartnerFunction, cpID));

                        itemCPPartnerEntity.getProperties().put(Constants.PartnarName,
                                new ODataPropertyDefaultImpl(Constants.PartnarName, hashtable.get(Constants.OutletName)));

                        itemCPPartnerEntity.getProperties().put(Constants.PartnerMobileNo,
                                new ODataPropertyDefaultImpl(Constants.PartnerMobileNo,hashtable.get(Constants.MobileNo)));

                        try {
                            itemCPPartnerEntity.getProperties().put(Constants.PartnarCPGUID,
                                    new ODataPropertyDefaultImpl(Constants.PartnarCPGUID, hashtable.get(Constants.CPGUID).replace("-","").toUpperCase()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        itemCPPartnerEntity.getProperties().put(Constants.StatusID,
                                new ODataPropertyDefaultImpl(Constants.StatusID, Constants.str_01));

                        tempCPPartnerArray.add(i, itemCPPartnerEntity);
                    }

                    ODataEntitySetDefaultImpl itmCPPartnerEntity = new ODataEntitySetDefaultImpl(tempCPPartnerArray.size(), null, null);
                    for (ODataEntity entity : tempCPPartnerArray) {
                        itmCPPartnerEntity.getEntities().add(entity);
                    }
                    itmCPPartnerEntity.setResourcePath(Constants.CPPartnerFunctions);


                    ODataNavigationProperty navPropCPPartner = headerEntity.getNavigationProperty(Constants.CPPartnerFunctions);
                    navPropCPPartner.setNavigationContent(itmCPPartnerEntity);
                    headerEntity.setNavigationProperty(Constants.CPPartnerFunctions, navPropCPPartner);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return headerEntity;

    }
    *//**
     * Create Entity for collection creation and Schedule in Online Manager
     *
     * @throws OnlineODataStoreException
     *//*
    public static void createROEntity(Hashtable<String, String> table, ArrayList<HashMap<String, String>> itemtable, UIListener uiListener) throws OnlineODataStoreException {
        OnlineStoreListener openListener = OnlineStoreListener.getInstance();
        OnlineODataStore store = openListener.getStore();

        if (store != null) {
            try {
                //Creates the entity payload
                ODataEntity soCreateEntity = createROCreateEntity(table, itemtable, store);

                OnlineRequestListener collectionListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);

                String ssoGUID32 = table.get(Constants.SSROGUID).replace("-", "");

                String soCreatedOn = table.get(Constants.CreatedOn);
                String soCreatedAt = table.get(Constants.CreatedAt);

                String mStrDateTime = UtilConstants.getReArrangeDateFormat(soCreatedOn) + Constants.T + UtilConstants.convertTimeOnly(soCreatedAt);

                Map<String, String> createHeaders = new HashMap<String, String>();
                createHeaders.put(Constants.RequestID, ssoGUID32);
                createHeaders.put(Constants.RepeatabilityCreation, mStrDateTime);

                ODataRequestParamSingle collectionReq = new ODataRequestParamSingleDefaultImpl();
                collectionReq.setMode(ODataRequestParamSingle.Mode.Create);
                collectionReq.setResourcePath(soCreateEntity.getResourcePath());
                collectionReq.setPayload(soCreateEntity);
                collectionReq.getCustomHeaders().putAll(createHeaders);

                store.scheduleRequest(collectionReq, collectionListener);

            } catch (Exception e) {
                throw new OnlineODataStoreException(e);
            }
        }
        //END

    }
    *//**
     * Create Entity for collection creation
     *
     * @throws ODataParserException
     *//*
    private static ODataEntity createROCreateEntity(Hashtable<String, String> hashtable, ArrayList<HashMap<String, String>> itemhashtable, OnlineODataStore store) throws ODataParserException {
        ODataEntity newHeaderEntity = null;
        ODataEntity newItemEntity = null;
        ArrayList<ODataEntity> tempArray = new ArrayList();
        try {
            if (hashtable != null) {
                newHeaderEntity = new ODataEntityDefaultImpl(Constants.getNameSpaceOnline(store)+""+Constants.ReturnOrderEntity);

                newHeaderEntity.setResourcePath(Constants.SSROs, Constants.SSROs);

                try {
                    store.allocateProperties(newHeaderEntity, ODataStore.PropMode.All);
                } catch (ODataException e) {
                    e.printStackTrace();
                }
                //If available, it populates the navigation properties of an OData Entity
                store.allocateNavigationProperties(newHeaderEntity);

                newHeaderEntity.getProperties().put(Constants.SSROGUID,
                        new ODataPropertyDefaultImpl(Constants.SSROGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.SSROGUID))));
//                newHeaderEntity.getProperties().put(Constants.OrderNo,
//                        new ODataPropertyDefaultImpl(Constants.OrderNo, hashtable.get(Constants.OrderNo)));
                newHeaderEntity.getProperties().put(Constants.OrderType,
                        new ODataPropertyDefaultImpl(Constants.OrderType, hashtable.get(Constants.OrderType)));
                newHeaderEntity.getProperties().put(Constants.OrderTypeDesc,
                        new ODataPropertyDefaultImpl(Constants.OrderTypeDesc, hashtable.get(Constants.OrderTypeDesc)));
                newHeaderEntity.getProperties().put(Constants.OrderDate,
                        new ODataPropertyDefaultImpl(Constants.OrderDate, UtilConstants.convertDateFormat(hashtable.get(Constants.OrderDate))));
                newHeaderEntity.getProperties().put(Constants.DmsDivision,
                        new ODataPropertyDefaultImpl(Constants.DmsDivision, hashtable.get(Constants.DmsDivision)));
                newHeaderEntity.getProperties().put(Constants.DmsDivisionDesc,
                        new ODataPropertyDefaultImpl(Constants.DmsDivisionDesc, hashtable.get(Constants.DmsDivisionDesc)));

                newHeaderEntity.getProperties().put(Constants.FromCPGUID,
                        new ODataPropertyDefaultImpl(Constants.FromCPGUID, hashtable.get(Constants.FromCPGUID).replace("-", "")));
//                newHeaderEntity.getProperties().put(Constants.FromCPNo,
//                        new ODataPropertyDefaultImpl(Constants.FromCPNo, hashtable.get(Constants.FromCPNo)));
                newHeaderEntity.getProperties().put(Constants.FromCPName,
                        new ODataPropertyDefaultImpl(Constants.FromCPName, hashtable.get(Constants.FromCPName)));
//                newHeaderEntity.getProperties().put("FromCPTypID",
//                        new ODataPropertyDefaultImpl("FromCPTypID", hashtable.get(Constants.FromCPTypId)));
                newHeaderEntity.getProperties().put(Constants.FromCPTypDs,
                        new ODataPropertyDefaultImpl(Constants.FromCPTypDs, hashtable.get(Constants.FromCPTypId)));


                newHeaderEntity.getProperties().put(Constants.CPGUID,
                        new ODataPropertyDefaultImpl(Constants.CPGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.CPGUID))));
                newHeaderEntity.getProperties().put(Constants.CPNo,
                        new ODataPropertyDefaultImpl(Constants.CPNo, hashtable.get(Constants.CPNo)));
                newHeaderEntity.getProperties().put(Constants.CPName,
                        new ODataPropertyDefaultImpl(Constants.CPName, hashtable.get(Constants.CPName)));
                newHeaderEntity.getProperties().put(Constants.CPTypeID,
                        new ODataPropertyDefaultImpl(Constants.CPTypeID, hashtable.get(Constants.CPTypeID)));
                newHeaderEntity.getProperties().put(Constants.CPTypeDesc,
                        new ODataPropertyDefaultImpl(Constants.CPTypeDesc, hashtable.get(Constants.CPTypeDesc)));


                newHeaderEntity.getProperties().put(Constants.SoldToCPGUID,
                        new ODataPropertyDefaultImpl(Constants.SoldToCPGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.SoldToCPGUID))));
                newHeaderEntity.getProperties().put(Constants.SoldToID,
                        new ODataPropertyDefaultImpl(Constants.SoldToID, hashtable.get(Constants.SoldToID)));
      *//*          newHeaderEntity.getProperties().put(Constants.SoldToUID,
                        new ODataPropertyDefaultImpl(Constants.SoldToUID, hashtable.get(Constants.SoldToUID)));*//*
                newHeaderEntity.getProperties().put(Constants.SoldToDesc,
                        new ODataPropertyDefaultImpl(Constants.SoldToDesc, hashtable.get(Constants.SoldToDesc)));

                newHeaderEntity.getProperties().put(Constants.SoldToTypeID,
                        new ODataPropertyDefaultImpl(Constants.SoldToTypeID, hashtable.get(Constants.SoldToTypeID)));
                newHeaderEntity.getProperties().put(Constants.SoldToTypDs,
                        new ODataPropertyDefaultImpl(Constants.SoldToTypDs, hashtable.get(Constants.SoldToTypDesc)));


                newHeaderEntity.getProperties().put(Constants.Currency,
                        new ODataPropertyDefaultImpl(Constants.Currency, hashtable.get(Constants.Currency)));


                newHeaderEntity.getProperties().put(Constants.SPGUID,
                        new ODataPropertyDefaultImpl(Constants.SPGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.SPGUID))));
                newHeaderEntity.getProperties().put(Constants.SPNo,
                        new ODataPropertyDefaultImpl(Constants.SPNo, hashtable.get(Constants.SPNo)));
                newHeaderEntity.getProperties().put(Constants.FirstName,
                        new ODataPropertyDefaultImpl(Constants.FirstName, hashtable.get(Constants.FirstName)));
             *//*   newHeaderEntity.getProperties().put(Constants.LOGINID,
                        new ODataPropertyDefaultImpl(Constants.LOGINID, hashtable.get(Constants.LOGINID)));*//*
                newHeaderEntity.getProperties().put(Constants.StatusID,
                        new ODataPropertyDefaultImpl(Constants.StatusID, hashtable.get(Constants.StatusID)));
                newHeaderEntity.getProperties().put(Constants.ApprovalStatusID,
                        new ODataPropertyDefaultImpl(Constants.ApprovalStatusID, hashtable.get(Constants.ApprovalStatusID)));

                newHeaderEntity.getProperties().put(Constants.TestRun,
                        new ODataPropertyDefaultImpl(Constants.TestRun, hashtable.get(Constants.TestRun)));


                int incremntVal = 0;
                for (int incrementVal = 0; incrementVal < itemhashtable.size(); incrementVal++) {

                    HashMap<String, String> singleRow = itemhashtable.get(incrementVal);

                    incremntVal = incrementVal + 1;

                    newItemEntity = new ODataEntityDefaultImpl(Constants.getNameSpaceOnline(store)+""+Constants.ReturnOrderItemEntity);

                    newItemEntity.setResourcePath(Constants.SSROItemDetails + "(" + incremntVal + ")", Constants.SSROItemDetails + "(" + incremntVal + ")");
                    try {
                        store.allocateProperties(newItemEntity, ODataStore.PropMode.Keys);
                    } catch (ODataException e) {
                        e.printStackTrace();
                    }


                    newItemEntity.getProperties().put(Constants.SSROItemGUID,
                            new ODataPropertyDefaultImpl(Constants.SSROItemGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.SSROItemGUID))));

                    newItemEntity.getProperties().put(Constants.SSROGUID,
                            new ODataPropertyDefaultImpl(Constants.SSROGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.SSROGUID))));

                    newItemEntity.getProperties().put(Constants.ItemNo,
                            new ODataPropertyDefaultImpl(Constants.ItemNo, singleRow.get(Constants.ItemNo)));


                    newItemEntity.getProperties().put(Constants.MaterialNo,
                            new ODataPropertyDefaultImpl(Constants.MaterialNo, singleRow.get(Constants.MaterialNo)));

                    newItemEntity.getProperties().put(Constants.MaterialDesc,
                            new ODataPropertyDefaultImpl(Constants.MaterialDesc, singleRow.get(Constants.MaterialDesc)));

                    newItemEntity.getProperties().put(Constants.OrdMatGrp,
                            new ODataPropertyDefaultImpl(Constants.OrdMatGrp, singleRow.get(Constants.OrdMatGrp)));
//
                    newItemEntity.getProperties().put(Constants.OrdMatGrpDesc,
                            new ODataPropertyDefaultImpl(Constants.OrdMatGrpDesc, singleRow.get(Constants.OrdMatGrpDesc)));
//
                    newItemEntity.getProperties().put(Constants.Quantity,
                            new ODataPropertyDefaultImpl(Constants.Quantity, BigDecimal.valueOf(Double.parseDouble(singleRow.get(Constants.Quantity)))));
                    try {
                        newItemEntity.getProperties().put(Constants.MRP,
                                new ODataPropertyDefaultImpl(Constants.MRP, BigDecimal.valueOf(Double.parseDouble(singleRow.get(Constants.MRP)))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        newItemEntity.getProperties().put(Constants.UnitPrice,
                                new ODataPropertyDefaultImpl(Constants.UnitPrice, BigDecimal.valueOf(Double.parseDouble(singleRow.get(Constants.UnitPrice)))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    newItemEntity.getProperties().put(Constants.Currency,
                            new ODataPropertyDefaultImpl(Constants.Currency, singleRow.get(Constants.Currency)));
                    newItemEntity.getProperties().put(Constants.UOM,
                            new ODataPropertyDefaultImpl(Constants.UOM, singleRow.get(Constants.Uom)));

                    newItemEntity.getProperties().put(Constants.Batch,
                            new ODataPropertyDefaultImpl(Constants.Batch, singleRow.get(Constants.Batch)));

                    try {
                        newItemEntity.getProperties().put(Constants.RefDocNo,
                                new ODataPropertyDefaultImpl(Constants.RefDocNo, singleRow.get(Constants.RefDocNo)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    newItemEntity.getProperties().put(Constants.RejectionReasonID,
                            new ODataPropertyDefaultImpl(Constants.RejectionReasonID, singleRow.get(Constants.RejectionReasonID)));

                    newItemEntity.getProperties().put(Constants.RejectionReasonDesc,
                            new ODataPropertyDefaultImpl(Constants.RejectionReasonDesc, singleRow.get(Constants.RejectionReasonDesc)));

                    newItemEntity.getProperties().put(Constants.RefdocItmGUID,
                            new ODataPropertyDefaultImpl(Constants.RefdocItmGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.RefdocItmGUID))));
                    tempArray.add(incrementVal, newItemEntity);

                }

                ODataEntitySetDefaultImpl itemEntity = new ODataEntitySetDefaultImpl(tempArray.size(), null, null);
                for (ODataEntity entity : tempArray) {
                    itemEntity.getEntities().add(entity);
                }
                itemEntity.setResourcePath(Constants.SSROItemDetails);

                ODataNavigationProperty navProp = newHeaderEntity.getNavigationProperty(Constants.SSROItemDetails);
                navProp.setNavigationContent(itemEntity);
                newHeaderEntity.setNavigationProperty(Constants.SSROItemDetails, navProp);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newHeaderEntity;
    }
    *//*create daily expense*//*
    public static void createDailyExpense(Hashtable<String, String> table, ArrayList<HashMap<String, String>> itemtable, UIListener uiListener) throws OnlineODataStoreException {
        OnlineStoreListener openListener = OnlineStoreListener.getInstance();
        OnlineODataStore store = openListener.getStore();

        if (store != null) {
            try {
                ODataEntity soCreateEntity = createDailyExpenseCreateEntity(table, itemtable, store);

                OnlineRequestListener collectionListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);

                ODataRequestParamSingle collectionReq = new ODataRequestParamSingleDefaultImpl();
                collectionReq.setMode(ODataRequestParamSingle.Mode.Create);
                collectionReq.setResourcePath(soCreateEntity.getResourcePath());
                collectionReq.setPayload(soCreateEntity);
//                collectionReq.getCustomHeaders().putAll(createHeaders);

                store.scheduleRequest(collectionReq, collectionListener);

            } catch (Exception e) {
                throw new OnlineODataStoreException(e);
            }
        }

    }

    *//*entity for expense *//*
    public static ODataEntity createDailyExpenseCreateEntity(Hashtable<String, String> hashtable, ArrayList<HashMap<String, String>> itemhashtable, OnlineODataStore store) throws ODataParserException {
        ODataEntity newHeaderEntity = null;
        ODataEntity newItemEntity = null;
        ODataEntity newItemImageEntity = null;
        ArrayList<ODataEntity> tempArray = new ArrayList();
        ArrayList<ODataEntity> docmentArray = new ArrayList();
        try {
            if (hashtable != null) {
                newHeaderEntity = new ODataEntityDefaultImpl(Constants.getNameSpaceOnline(store)+""+Constants.ExpenseEntity);

                newHeaderEntity.setResourcePath(Constants.Expenses, Constants.Expenses);

                try {
                    store.allocateProperties(newHeaderEntity, ODataStore.PropMode.All);
                } catch (ODataException e) {
                    e.printStackTrace();
                }
                //If available, it populates the navigation properties of an OData Entity
                store.allocateNavigationProperties(newHeaderEntity);

                newHeaderEntity.getProperties().put(Constants.ExpenseGUID,
                        new ODataPropertyDefaultImpl(Constants.ExpenseGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.ExpenseGUID))));
//                newHeaderEntity.getProperties().put(Constants.OrderNo,
//                        new ODataPropertyDefaultImpl(Constants.OrderNo, hashtable.get(Constants.OrderNo)));
                newHeaderEntity.getProperties().put(Constants.ExpenseNo,
                        new ODataPropertyDefaultImpl(Constants.ExpenseNo, hashtable.get(Constants.ExpenseNo)));
                newHeaderEntity.getProperties().put(Constants.FiscalYear,
                        new ODataPropertyDefaultImpl(Constants.FiscalYear, hashtable.get(Constants.FiscalYear)));
             *//*   newHeaderEntity.getProperties().put(Constants.LoginID,
                        new ODataPropertyDefaultImpl(Constants.LoginID, hashtable.get(Constants.LoginID)));*//*
               *//* newHeaderEntity.getProperties().put(Constants.OrderDate,
                        new ODataPropertyDefaultImpl(Constants.OrderDate, UtilConstants.convertDateFormat(hashtable.get(Constants.OrderDate))));*//*

                newHeaderEntity.getProperties().put(Constants.CPGUID,
                        new ODataPropertyDefaultImpl(Constants.CPGUID, hashtable.get(Constants.CPGUID)));
                newHeaderEntity.getProperties().put(Constants.CPNo,
                        new ODataPropertyDefaultImpl(Constants.CPNo, hashtable.get(Constants.CPNo)));
                newHeaderEntity.getProperties().put(Constants.CPName,
                        new ODataPropertyDefaultImpl(Constants.CPName, hashtable.get(Constants.CPName)));
                newHeaderEntity.getProperties().put(Constants.CPType,
                        new ODataPropertyDefaultImpl(Constants.CPType, hashtable.get(Constants.CPType)));
                newHeaderEntity.getProperties().put(Constants.CPTypeDesc,
                        new ODataPropertyDefaultImpl(Constants.CPTypeDesc, hashtable.get(Constants.CPTypeDesc)));


                newHeaderEntity.getProperties().put(Constants.ExpenseType,
                        new ODataPropertyDefaultImpl(Constants.ExpenseType, hashtable.get(Constants.ExpenseType)));
                newHeaderEntity.getProperties().put(Constants.ExpenseTypeDesc,
                        new ODataPropertyDefaultImpl(Constants.ExpenseTypeDesc, hashtable.get(Constants.ExpenseTypeDesc)));
                try {
                    newHeaderEntity.getProperties().put(Constants.ExpenseDate,
                            new ODataPropertyDefaultImpl(Constants.ExpenseDate, UtilConstants.convertDateFormat(hashtable.get(Constants.ExpenseDate))));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                newHeaderEntity.getProperties().put(Constants.Status,
                        new ODataPropertyDefaultImpl(Constants.Status, hashtable.get(Constants.Status)));
                newHeaderEntity.getProperties().put(Constants.StatusDesc,
                        new ODataPropertyDefaultImpl(Constants.StatusDesc, hashtable.get(Constants.StatusDesc)));

                newHeaderEntity.getProperties().put(Constants.Amount,
                        new ODataPropertyDefaultImpl(Constants.Amount, BigDecimal.valueOf(Double.parseDouble(hashtable.get(Constants.Amount)))));
               *//* newHeaderEntity.getProperties().put(Constants.CreatedOn,
                        new ODataPropertyDefaultImpl(Constants.CreatedOn, hashtable.get(Constants.CreatedOn)));*//*
//                newHeaderEntity.getProperties().put(Constants.CreatedBy,
//                        new ODataPropertyDefaultImpl(Constants.CreatedBy, hashtable.get(Constants.CreatedBy)));


                newHeaderEntity.getProperties().put(Constants.Currency,
                        new ODataPropertyDefaultImpl(Constants.Currency, hashtable.get(Constants.Currency)));


                newHeaderEntity.getProperties().put(Constants.SPGUID,
                        new ODataPropertyDefaultImpl(Constants.SPGUID, ODataGuidDefaultImpl.initWithString32(hashtable.get(Constants.SPGUID))));
                newHeaderEntity.getProperties().put(Constants.SPNo,
                        new ODataPropertyDefaultImpl(Constants.SPNo, hashtable.get(Constants.SPNo)));
                newHeaderEntity.getProperties().put(Constants.SPName,
                        new ODataPropertyDefaultImpl(Constants.SPName, hashtable.get(Constants.SPName)));


                int incremntVal = 0;
                for (int incrementVal = 0; incrementVal < itemhashtable.size(); incrementVal++) {

                    HashMap<String, String> singleRow = itemhashtable.get(incrementVal);

                    incremntVal = incrementVal + 1;

                    newItemEntity = new ODataEntityDefaultImpl(Constants.getNameSpaceOnline(store)+""+Constants.ExpenseItemEntity);

                    newItemEntity.setResourcePath(Constants.ExpenseItemDetails + "(" + incremntVal + ")", Constants.ExpenseItemDetails + "(" + incremntVal + ")");
                    try {
                        store.allocateProperties(newItemEntity, ODataStore.PropMode.Keys);
                    } catch (ODataException e) {
                        e.printStackTrace();
                    }
                    *//*try {
                        store.allocateProperties(newItemEntity, PropMode.All);
                    } catch (ODataException e) {
                        e.printStackTrace();
                    }*//*
                    //If available, it populates the navigation properties of an OData Entity
                    store.allocateNavigationProperties(newItemEntity);

                    newItemEntity.getProperties().put(Constants.ExpenseItemGUID,
                            new ODataPropertyDefaultImpl(Constants.ExpenseItemGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.ExpenseItemGUID))));

                    newItemEntity.getProperties().put(Constants.ExpenseGUID,
                            new ODataPropertyDefaultImpl(Constants.ExpenseGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.ExpenseGUID))));

                    newItemEntity.getProperties().put(Constants.ExpeseItemNo,
                            new ODataPropertyDefaultImpl(Constants.ExpeseItemNo, singleRow.get(Constants.ExpeseItemNo)));


                    *//*newItemEntity.getProperties().put(Constants.LoginID,
                            new ODataPropertyDefaultImpl(Constants.LoginID, singleRow.get(Constants.LoginID)));*//*

//                    newItemEntity.getProperties().put(Constants.MaterialDesc,
//                            new ODataPropertyDefaultImpl(Constants.MaterialDesc, singleRow.get(Constants.MaterialDesc)));

                    newItemEntity.getProperties().put(Constants.ExpenseItemType,
                            new ODataPropertyDefaultImpl(Constants.ExpenseItemType, singleRow.get(Constants.ExpenseItemType)));
//
                    newItemEntity.getProperties().put(Constants.ExpenseItemTypeDesc,
                            new ODataPropertyDefaultImpl(Constants.ExpenseItemTypeDesc, singleRow.get(Constants.ExpenseItemTypeDesc)));
                    if (!singleRow.get(Constants.BeatGUID).equalsIgnoreCase("")) {
                        newItemEntity.getProperties().put(Constants.BeatGUID,
                                new ODataPropertyDefaultImpl(Constants.BeatGUID, ODataGuidDefaultImpl.initWithString32(singleRow.get(Constants.BeatGUID))));
                    }
                    newItemEntity.getProperties().put(Constants.Location,
                            new ODataPropertyDefaultImpl(Constants.Location, singleRow.get(Constants.Location)));
                    if(!singleRow.get(Constants.ConvenyanceMode).equals("")) {
                        newItemEntity.getProperties().put(Constants.ConvenyanceMode,
                                new ODataPropertyDefaultImpl(Constants.ConvenyanceMode, singleRow.get(Constants.ConvenyanceMode)));
                        newItemEntity.getProperties().put(Constants.ConvenyanceModeDs,
                                new ODataPropertyDefaultImpl(Constants.ConvenyanceModeDs, singleRow.get(Constants.ConvenyanceModeDs)));
                    }
                    if(!singleRow.get(Constants.BeatDistance).equals("")) {
                        newItemEntity.getProperties().put(Constants.BeatDistance,
                                new ODataPropertyDefaultImpl(Constants.BeatDistance, BigDecimal.valueOf(Double.parseDouble(singleRow.get(Constants.BeatDistance)))));
                    }
                    if (!singleRow.get(Constants.Amount).equalsIgnoreCase("")) {
                        newItemEntity.getProperties().put(Constants.Amount,
                                new ODataPropertyDefaultImpl(Constants.Amount, BigDecimal.valueOf(Double.parseDouble(singleRow.get(Constants.Amount)))));
                    }
                    newItemEntity.getProperties().put(Constants.UOM,
                            new ODataPropertyDefaultImpl(Constants.UOM, singleRow.get(Constants.UOM)));
                    newItemEntity.getProperties().put(Constants.Currency,
                            new ODataPropertyDefaultImpl(Constants.Currency, singleRow.get(Constants.Currency)));
                    newItemEntity.getProperties().put(Constants.Remarks,
                            new ODataPropertyDefaultImpl(Constants.Remarks, singleRow.get(Constants.Remarks)));



                    *//*image document*//*
                    *//*String imageStringArray = singleRow.get("item_no" + incrementVal);
                    ArrayList<HashMap<String, String>> convertedImage = UtilConstants.convertToArrayListMap(imageStringArray);
                    int incremntImgVal = 0;
                    for (int incrementImgVal = 0; incrementImgVal < convertedImage.size(); incrementImgVal++) {
                        HashMap<String, String> singleImgRow = convertedImage.get(incrementImgVal);
                        incremntImgVal = incrementImgVal + 1;

                        newItemImageEntity = new ODataEntityDefaultImpl(Constants.ExpenseItemDocumentEntity);

                        newItemImageEntity.setResourcePath(Constants.ExpenseDocuments + "(" + incremntImgVal + ")", Constants.ExpenseDocuments + "(" + incremntImgVal + ")");
                        try {
                            store.allocateProperties(newItemImageEntity, PropMode.Keys);
                        } catch (ODataException e) {
                            e.printStackTrace();
                        }
                        newItemImageEntity.getProperties().put(Constants.ExpenseImgGUID,
                                new ODataPropertyDefaultImpl(Constants.ExpenseImgGUID, singleImgRow.get(Constants.ExpenseImgGUID)));

                        newItemImageEntity.getProperties().put(Constants.ExpenseItemGUID,
                                new ODataPropertyDefaultImpl(Constants.ExpenseItemGUID, ODataGuidDefaultImpl.initWithString32(singleImgRow.get(Constants.ExpenseItemGUID))));

                        newItemImageEntity.getProperties().put(Constants.LoginID,
                                new ODataPropertyDefaultImpl(Constants.LoginID, singleImgRow.get(Constants.LoginID)));

                        newItemImageEntity.getProperties().put(Constants.DocumentTypeID,
                                new ODataPropertyDefaultImpl(Constants.DocumentTypeID, singleImgRow.get(Constants.DocumentTypeID)));

                        newItemImageEntity.getProperties().put(Constants.DocumentTypeDesc,
                                new ODataPropertyDefaultImpl(Constants.DocumentTypeDesc, singleImgRow.get(Constants.DocumentTypeDesc)));

                        newItemImageEntity.getProperties().put(Constants.DocumentStatusID,
                                new ODataPropertyDefaultImpl(Constants.DocumentStatusID, singleImgRow.get(Constants.DocumentStatusID)));

                        newItemImageEntity.getProperties().put(Constants.DocumentStatusDesc,
                                new ODataPropertyDefaultImpl(Constants.DocumentStatusDesc, singleImgRow.get(Constants.DocumentStatusDesc)));

                        newItemImageEntity.getProperties().put(Constants.ValidFrom,
                                new ODataPropertyDefaultImpl(Constants.ValidFrom, UtilConstants.convertDateFormat(singleImgRow.get(Constants.ValidFrom))));

                        newItemImageEntity.getProperties().put(Constants.ValidTo,
                                new ODataPropertyDefaultImpl(Constants.ValidTo, UtilConstants.convertDateFormat(singleImgRow.get(Constants.ValidTo))));

                        newItemImageEntity.getProperties().put(Constants.DocumentLink,
                                new ODataPropertyDefaultImpl(Constants.DocumentLink, singleImgRow.get(Constants.DocumentLink)));

                        newItemImageEntity.getProperties().put(Constants.FileName,
                                new ODataPropertyDefaultImpl(Constants.FileName, singleImgRow.get(Constants.FileName)));

                        newItemImageEntity.getProperties().put(Constants.DocumentMimeType,
                                new ODataPropertyDefaultImpl(Constants.DocumentMimeType, singleImgRow.get(Constants.DocumentMimeType)));

                        newItemImageEntity.getProperties().put(Constants.DocumentSize,
                                new ODataPropertyDefaultImpl(Constants.DocumentSize, singleImgRow.get(Constants.DocumentSize)));

                        docmentArray.add(incrementImgVal,newItemImageEntity);
                    }

                    ODataEntitySetDefaultImpl itemImageEntity = new ODataEntitySetDefaultImpl(docmentArray.size(), null, null);
                    for (ODataEntity entity : docmentArray) {
                        itemImageEntity.getEntities().add(entity);
                    }
                    itemImageEntity.setResourcePath(Constants.ExpenseDocuments);

                    ODataNavigationProperty navProp = newItemEntity.getNavigationProperty(Constants.ExpenseDocuments);
                    navProp.setNavigationContent(itemImageEntity);
                    newItemEntity.setNavigationProperty(Constants.ExpenseDocuments, navProp);*//*

                    tempArray.add(incrementVal, newItemEntity);


                }

                ODataEntitySetDefaultImpl itemEntity = new ODataEntitySetDefaultImpl(tempArray.size(), null, null);
                for (ODataEntity entity : tempArray) {
                    itemEntity.getEntities().add(entity);
                }
                itemEntity.setResourcePath(Constants.ExpenseItemDetails);

                ODataNavigationProperty navProp = newHeaderEntity.getNavigationProperty(Constants.ExpenseItemDetails);
                navProp.setNavigationContent(itemEntity);
                newHeaderEntity.setNavigationProperty(Constants.ExpenseItemDetails, navProp);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newHeaderEntity;
    }*/
    public static void updateTasksEntity(Hashtable<String, String> table, UIListener uiListener,Context mContext) {

        OnlineRequestListener onlineReqListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        if (TextUtils.isEmpty(sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))){
            createCSRFToken(mContext, iReceiveEvent -> {
                if (iReceiveEvent.getResponseStatusCode() == 200) {
                    updateTasksEntity(table, uiListener, mContext);
                }
            });
        }else {
            HttpConversationManager manager = new HttpConversationManager(mContext);
            // Create the conversation.
            manager.create(Uri.parse(MyUtils.getDefaultOnlineQryURL() + Constants.Tasks + "(InstanceID='" + table.get(Constants.InstanceID) + "',EntityType='SO')"))
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                    .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                    .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                    .addHeader("Accept", "application/json")
                    .setMethod(HttpMethod.PUT)
                    .setRequestListener(event -> {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put(Constants.InstanceID, table.get(Constants.InstanceID));
                            jsonObject.put(Constants.EntityType, table.get(Constants.EntityType));
                            jsonObject.put(Constants.DecisionKey, table.get(Constants.DecisionKey));
                            jsonObject.put(Constants.LoginID, table.get(Constants.LoginID));
                            jsonObject.put(Constants.EntityKey, table.get(Constants.EntityKey));
                            jsonObject.put(Constants.Comments, table.get(Constants.Comments));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String request = jsonObject.toString();
                        event.getWriter().write(request);
                        return null;
                    }).setResponseListener(event -> {
                if (event.getReader()!=null) {
                    if (event.getResponseStatusCode() == 201) {
                        onlineReqListener.notifySuccessToListener(null);
                    } else if (event.getResponseStatusCode() == 403) {
                        createCSRFToken(mContext, iReceiveEvent -> {
                            if (iReceiveEvent.getResponseStatusCode() == 200) {
                                updateTasksEntity(table, uiListener, mContext);
                            } else {
                                onlineReqListener.notifyErrorToListener(iReceiveEvent);
                            }
                        });
                    } else {
                        onlineReqListener.notifyErrorToListener(event);
                    }
                }
            }).start();
        }
    }
    public static char[] getRetailerBatchChangeSetReq (Hashtable<String, String> hashtable, ODataPropMap oDataProperties){
        String changeSetReq="";
        //Header object
        JSONObject headerObject = new JSONObject();
        Set keySet = oDataProperties.keySet();
        Iterator itr = keySet.iterator();
        while (itr.hasNext()) {
            String key = itr.next().toString();
            try {
                headerObject.put(key,oDataProperties.get(key).getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            headerObject.put(Constants.Latitude,hashtable.get(Constants.Latitude));
            headerObject.put(Constants.Longitude,hashtable.get(Constants.Longitude));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Item object
        JSONObject itemObject = new JSONObject();
        String cpDMSDivDetgry = Constants.CPDMSDivisions + "?$filter=" + Constants.CPGUID + " eq guid'" + hashtable.get(Constants.CPGUID) + "' ";
        List<ODataEntity> entities = null;

        try {
            entities = OfflineManager.getEntities(cpDMSDivDetgry);
        } catch (OfflineODataStoreException e) {
            e.printStackTrace();
        }
        if (entities!=null && entities.size()>0){
            for (ODataEntity oDataEntity : entities){
                ODataPropMap itemPropertys =oDataEntity.getProperties();
                keySet = itemPropertys.keySet();
                itr = keySet.iterator();
                while (itr.hasNext()) {
                    String key = itr.next().toString();
                    try {
                        itemObject.put(key,itemPropertys.get(key).getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        String xmlData ="--batch\n" +" Content-Type: multipart/mixed; boundary=changeset\n" +"\n";

            String headerXml = "--changeset\n" +
                    "Content-Type: application/http\n" +
                    "Content-Transfer-Encoding: binary\n" +
                    "\n" +
                    "POST ChannelPartners HTTP/1.1\n" +
                    "sap-contextid-accept: header\n" +
                    "Accept: application/json\n" +
                    "Accept-Language: en\n" +
                    "DataServiceVersion: 2.0\n" +
                    "MaxDataServiceVersion: 2.0\n" +
                    "sap-cancel-on-close: true\n" +
                    "Content-Type: application/json\n" +
                    "Content-Length: 358\n" +
                    "\n" + headerObject.toString() + "\n";

            String itemXml = "--changeset\n" +
                    "Content-Type: application/http\n" +
                    "Content-Transfer-Encoding: binary\n" +
                    "\n" +
                    "POST CPDMSDivisions HTTP/1.1\n" +
                    "sap-contextid-accept: header\n" +
                    "Accept: application/json\n" +
                    "Accept-Language: en\n" +
                    "DataServiceVersion: 2.0\n" +
                    "MaxDataServiceVersion: 2.0\n" +
                    "sap-cancel-on-close: true\n" +
                    "Content-Type: application/json\n" +
                    "Content-Length: 358\n" +
                    "\n" + itemObject.toString() + "\n";
        xmlData = xmlData + headerXml + itemXml + "--changeset--\n" +"\n" +"--batch--";

        return xmlData.toCharArray();

    }
    public static void createRetilerBatchReq(Context mContext, String cpGuid) {
        try {
            AndroidSystem.setContext(mContext);
            HttpConversationManager conversationManager = new HttpConversationManager(mContext);
            OnlineODataProvider provider = new OnlineODataProvider("SSGW_ALL", MyUtils.getDefaultOnlineQryURL());
            SDKHttpHandler sdkHttpHandler = new SDKHttpHandler(mContext, conversationManager);
            provider.getNetworkOptions().setHttpHandler(sdkHttpHandler);
            provider.getHttpHeaders().set("Authorization", MyUtils.getBasicAuthCredential(mContext));
            DataService service = new DataService(provider);
            EntitySet customersEntitySet = service.getEntitySet(Constants.ChannelPartners);
            EntityType customerEntityType = customersEntitySet.getEntityType();
            Property cpguidProperty = customerEntityType.getProperty("CPGUID");
            Property cpDmsProperty =   customerEntityType.getProperty("CPDMSDivisions");

            //Customer
            DataQuery query = new DataQuery().from(customersEntitySet).withKey(new EntityKey().withProperty(cpguidProperty,GuidValue.parse(cpGuid))).expand(cpDmsProperty);
            QueryResult queryResult = service.executeQuery(query);
            EntityValue customer = queryResult.getRequiredEntity();
            customer.setOldEntity(customer);
            GUID guidItem = GUID.newRandom();
            cpguidProperty.setGuid(customer, GuidValue.of(guidItem));


            //CPDMS division
            EntitySet cpDMSEntitySet = service.getEntitySet(Constants.CPDMSDivisions);
            EntityType cpDMSEntityType = cpDMSEntitySet.getEntityType();
            Property cpdmscpGuidProperty = cpDMSEntityType.getProperty("CPGUID");
            DataQuery query1 = new DataQuery().from(cpDMSEntitySet).where(cpdmscpGuidProperty.equal(GuidValue.parse(cpGuid)));
            EntityValueList cPDMSDivisions = service.executeQuery(query1).getEntityList();
            EntityValue cPDMSDivision = cPDMSDivisions.first().copyEntity();
            cPDMSDivision.setOldEntity(cPDMSDivision);
            cpdmscpGuidProperty.setGuid(cPDMSDivision, GuidValue.of(guidItem));


            RequestBatch batch = new RequestBatch();
            ChangeSet changes = new ChangeSet();
            changes.createLink(customer,cpDmsProperty,cPDMSDivision);
//            changes.createEntity(customer);
//            changes.createRelatedEntity(customer, cPDMSDivision,cpDmsDivision);
            batch.addChanges(changes);
            service.processBatch(batch);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    static List<String> setCookies= new ArrayList<>();
    public static void postBatchRequest(String batchRequest, String batchGuid, Context context, UIListener listener) throws IOException {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
            String loginUser = sharedPreferences.getString("username", "");
            String login_pwd = sharedPreferences.getString("password", "");

            String hostBatch = "https://" + Configuration.server_Text + "/" + Configuration.APP_ID + "/$batch";
            String host = "https://" + Configuration.server_Text + "/" + Configuration.APP_ID + "/$metadata"/*+CollectionStr*/;
            final HttpsURLConnection[] connection = {null};
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            String APPCID = sharedPref.getString(com.arteriatech.mutils.registration.UtilRegistrationActivity.KEY_appConnID, "");
            createCSRFToken(context, iReceiveEvent -> {
                if (iReceiveEvent.getResponseStatusCode() == 200) {
                    try {
                        connection[0] = (HttpsURLConnection) new URL(hostBatch).openConnection();
                        connection[0].setReadTimeout(30000);
                        connection[0].setConnectTimeout(30000);
                        String userCredentials = loginUser + ":" + login_pwd;
                        String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes("UTF-8"), 2);
                        connection[0].setRequestProperty("Authorization", basicAuth);
                        connection[0].setRequestProperty("x-smp-appid", Configuration.APP_ID);
                        connection[0].setRequestProperty("X-SMP-APPCID", APPCID);
                        connection[0].setRequestProperty("X-CSRF-Token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""));
                               /* for (int i = 0; i < setCookies.size(); i++) {
                                    connection[0].addRequestProperty("Cookie", setCookies.get(i));
                                }*/
                        connection[0].addRequestProperty("Content-Type", "application/json");
                        connection[0].setRequestProperty("Accept", "multipart/mixed");
                        connection[0].setRequestMethod("POST");
                        connection[0].setRequestProperty("Content-Type", "multipart/mixed; boundary=batch_" + batchGuid + "");
                        connection[0].setDoOutput(true);
//                                connection[0].setDoInput(true);
                        DataOutputStream wr = new DataOutputStream(connection[0].getOutputStream());
                        wr.writeBytes(batchRequest);
                        wr.flush();
                        wr.close();
                        int responseCode = connection[0].getResponseCode();
                        if (responseCode != 200 && responseCode != 400 && responseCode != 201 && responseCode != 202) {
                            try {
                                String errorMessage = readResponse(connection[0].getErrorStream());
                                listener.onRequestError(responseCode, new Exception(errorMessage));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (responseCode == 201 || responseCode == 202) {
                            String s = "";
                            String message="";
                            try {
                                BufferedReader reader = null;
                                reader = new BufferedReader(new InputStreamReader(connection[0].getInputStream()));
                                String line;
                                StringBuilder buffer = new StringBuilder();
                                while ((line = reader.readLine()) != null) {
                                    buffer.append(line).append('\n');
                                }
                                s = buffer.toString();
                                if (s.contains("400 Bad Request")) {
                                    message = "Error";
                                    listener.onRequestSuccess(responseCode, (message));
                                  /*  JSONObject jsonObject = new JSONObject(s);
                                    JSONObject obj = jsonObject.getJSONObject("error").getJSONObject("code").getJSONObject("message");
                                    String errorMessage = obj.getString("value");
//                                    message = "Error";
                                    listener.onRequestSuccess(responseCode, (errorMessage));*/

                                } else {
                                    message = "";
                                    listener.onRequestSuccess(responseCode, s);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
//                        if (responseCode == 201) {
//                            String successMessage = readResponse(connection[0].getInputStream());
                        } else {
                            String errorMessage = readResponse(connection[0].getErrorStream());
                            listener.onRequestError(responseCode, new Exception(errorMessage));
                        }
                    } catch (Exception var14) {
                        var14.printStackTrace();
                    } finally {
                        if (connection[0] != null) {
                            connection[0].disconnect();
                        }
                    }
                } else {
                    createCSRFToken(context, iReceiveEvent1 -> {
                        if (iReceiveEvent.getResponseStatusCode() == 200) {
                            try {
                                connection[0] = (HttpsURLConnection) new URL(hostBatch).openConnection();
                                connection[0].setReadTimeout(30000);
                                connection[0].setConnectTimeout(30000);
                                String userCredentials = loginUser + ":" + login_pwd;
                                String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes("UTF-8"), 2);
                                connection[0].setRequestProperty("Authorization", basicAuth);
                                connection[0].setRequestProperty("x-smp-appid", Configuration.APP_ID);
                                connection[0].setRequestProperty("X-SMP-APPCID", APPCID);
                                connection[0].setRequestProperty("X-CSRF-Token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""));
                                for (int i = 0; i < setCookies.size(); i++) {
                                    connection[0].addRequestProperty("Cookie", setCookies.get(i));
                                }
                                connection[0].addRequestProperty("Content-Type", "application/json");
                                connection[0].setRequestProperty("Accept", "multipart/mixed");
                                connection[0].setRequestMethod("POST");
                                connection[0].setRequestProperty("Content-Type", "multipart/mixed; boundary=batch_" + batchGuid + "");
                                connection[0].setDoOutput(true);
                                connection[0].setDoInput(true);
                                DataOutputStream wr = new DataOutputStream(connection[0].getOutputStream());
                                wr.writeBytes(batchRequest);
                                wr.flush();
                                wr.close();
                                int responseCode = connection[0].getResponseCode();
                                if (responseCode != 200 && responseCode != 400 && responseCode != 201 && responseCode != 202) {
                                    try {
                                        String errorMessage = readResponse(connection[0].getErrorStream());
//                                        listener.onUpdateError(connection[0].getResponseMessage());
                                        listener.onRequestError(responseCode, new Exception(errorMessage));

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (responseCode == 201 || responseCode == 202) {
                                    listener.onRequestSuccess(responseCode, "Order Updated successfully");
                                } else {
                                    String errorMessage = readResponse(connection[0].getErrorStream());
                                    listener.onRequestError(responseCode, new Exception(errorMessage));
                                }
                            } catch (Exception var14) {
                                var14.printStackTrace();
                            } finally {
                                if (connection[0] != null) {
                                    connection[0].disconnect();
                                }
                            }
                        }
                    });
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void updateEntityCloud(final String requestID,final String requestString, final String resourcePath, UIListener uiListener, Context mContext) {
        OnlineRequestListener onlineReqListener = new OnlineRequestListener(com.arteriatech.mutils.common.Operation.Create.getValue(), uiListener);
        SharedPreferences sharedPref = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        if ( TextUtils.isEmpty(sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))){
            createCSRFToken(mContext, iReceiveEvent -> {
                if (iReceiveEvent.getResponseStatusCode() == 200) {
                    updateEntityCloud(requestID,requestString, resourcePath, uiListener, mContext);
                }
            });
        }else {
            HttpConversationManager manager = new HttpConversationManager(mContext);
            // Create the conversation.
            manager.create(Uri.parse(MyUtils.getCloudOnlineQryURL() + resourcePath))
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                    .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                    .addHeader("RequestID", requestID)
                    .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                    .addHeader("Accept", "application/json")
                    .setMethod(HttpMethod.PUT)
                    .setRequestListener(event -> {
                        if (!TextUtils.isEmpty(requestString))
                            event.getWriter().write(requestString);
                        return null;
                    }).setResponseListener(event -> {
                        // Process the results.
//                if (event.getReader()!=null) {
                        //                    String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                        //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                        if (event.getResponseStatusCode() == 201 || event.getResponseStatusCode() == 204) {
                            CSRF_TOKEN="";
                            onlineReqListener.notifySuccessToListener(null);
                        } else if (event.getResponseStatusCode() == 403) {
                            createCSRFToken(mContext, iReceiveEvent -> {
                                if (iReceiveEvent.getResponseStatusCode() == 200) {
                                    updateEntityCloud(requestID,requestString, resourcePath, uiListener, mContext);
                                } else {
                                    onlineReqListener.notifyErrorToListener(iReceiveEvent);
                                }
                            });
                        } else {
                            onlineReqListener.notifyErrorToListener(event);
                        }
//                }
                    }).start();
        }
    }

    public static String fetchpugwGetRequest(String resourcePath, Context mContext, IResponseListener iResponseListener, ICommunicationErrorListener iCommunicationErrorListener) {

        HttpConversationManager manager = new HttpConversationManager(mContext);
        manager.create(Uri.parse(MyUtils.getDefaultPUGWOnlineQryURL() + resourcePath))
                .setMethod(HttpMethod.GET)
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("Accept", "application/json")
                .addHeader("x-smp-appid", Configuration.APP_ID)
                .setResponseListener(iResponseListener).setFlowListener(IConversationFlowListener
                        .prepareFor()
                        .communicationError(iCommunicationErrorListener).build())
                .start();
        return "";
    }

    public static String fetchDocGetRequest(String resourcePath, Context mContext, IResponseListener iResponseListener, ICommunicationErrorListener iCommunicationErrorListener) {

        HttpConversationManager manager = new HttpConversationManager(mContext);
        manager.create(Uri.parse(MyUtils.getDefaultOnlineProcessDocURL() + resourcePath))
                .setMethod(HttpMethod.GET)
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("x-smp-appid", Configuration.APP_ID)
                .setResponseListener(iResponseListener).setFlowListener(IConversationFlowListener
                        .prepareFor()
                        .communicationError(iCommunicationErrorListener).build())
                .start();
        return "";
    }

    public static String fetchDocAP1GetRequest(String resourcePath, Context mContext, IResponseListener iResponseListener, ICommunicationErrorListener iCommunicationErrorListener) {

        HttpConversationManager manager = new HttpConversationManager(mContext);
        manager.create(Uri.parse(MyUtils.getDefaultOnlineProcessDocAP1URL() + resourcePath))
                .setMethod(HttpMethod.GET)
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("x-smp-appid", Configuration.APP_ID)
                .setResponseListener(iResponseListener).setFlowListener(IConversationFlowListener
                        .prepareFor()
                        .communicationError(iCommunicationErrorListener).build())
                .start();
        return "";
    }
    public static JSONObject getJSONBody(final IReceiveEvent event) throws IOException {
        try {
            JSONObject jsonObject = new JSONObject(IReceiveEvent.Util.getResponseBody(event.getReader()));
            return jsonObject.getJSONObject("d");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
    public static JSONArray getJSONArrayBody(JSONObject jsonObject) throws IOException {
        try {
            return jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }


    private static void createPUGWCSRFToken(Context mContext, IResponseListener var1) {
        LogManager.writeLogInfo("fetching CSRF token started.");
        HttpConversationManager manager = new HttpConversationManager(mContext);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        manager.create(Uri.parse(MyUtils.getDefaultPUGWOnlineQryURL()))
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                .addHeader("x-csrf-token", "fetch")
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("Accept", "application/json")
                .setMethod(HttpMethod.GET)
                .setResponseListener(event1 -> {
                    // Process the results.
//                    String responseBody1 = IReceiveEvent.Util.getResponseBody(event1.getReader());
//                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody1 + " " + event1.getResponseStatusCode());
                    LogManager.writeLogInfo("Token listener called : "+event1.getResponseStatusCode());
                    if (event1.getResponseStatusCode() == 200) {
                        LogManager.writeLogInfo("fetching CSRF token successful.");
                        Map<String, List<String>> mapList = event1.getResponseHeaders();
                        if (mapList != null && mapList.size() > 0) {
                            List<String> arrayList = mapList.get("x-csrf-token");
                            if (arrayList != null && !arrayList.isEmpty()) {
//                                CSRF_TOKEN = arrayList.get(0);
                                SharedPreferences.Editor spEdit = sharedPref.edit();
                                spEdit.putString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, arrayList.get(0));
                                spEdit.apply();
                            }
                        }
                    }
                    var1.onResponseReceived(event1);
                }).start();
    }

    public static void createEntityProcessDocument(final String requestString, final String resourcePath, UIListener uiListener, Context mContext) {
        OnlineRequestListener onlineReqListener = new OnlineRequestListener(com.arteriatech.mutils.common.Operation.Create.getValue(), uiListener);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);

        HttpConversationManager manager = new HttpConversationManager(mContext);
        // Create the conversation.
        manager.create(Uri.parse(MyUtils.getDefaultOnlineProcessDocURL() + resourcePath))
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
//                    .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("RequestID", REPEATABLE_REQUEST_ID)
                .addHeader("Accept", "application/json")
                .setMethod(HttpMethod.POST)
                .setRequestListener(event -> {
                    if (!TextUtils.isEmpty(requestString))
                        event.getWriter().write(requestString);
                    return null;
                }).setResponseListener(event -> {
                    // Process the results.
                    String responseBody = "";
                    try {
                        responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    if (event.getResponseStatusCode() == 201) {
                        onlineReqListener.notifySuccessToListener(responseBody);
                    }else if (event.getResponseStatusCode() == 200) {
                        onlineReqListener.notifySuccessToListener(responseBody);
                    } else if (event.getResponseStatusCode() == 403) {
                        createCSRFToken(mContext, iReceiveEvent -> {
                            if (iReceiveEvent.getResponseStatusCode() == 200) {
                                createEntityProcessDocument(requestString, resourcePath, uiListener, mContext);
                            } else {
                                String responseBodyTemp="";
                                try {
                                    responseBodyTemp = IReceiveEvent.Util.getResponseBody(iReceiveEvent.getReader());
                                    Log.d("OnlineManager", "getUserRollInfo: " + responseBodyTemp + " " + iReceiveEvent.getResponseStatusCode());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                onlineReqListener.notifyErrorToListenerMsg(responseBodyTemp);

                            }
                        });
                    } else {
                        onlineReqListener.notifyErrorToListenerMsg(responseBody);
                    }
//                }
                }).start();
//        }
    }

    public static void createInvoiceEntity(final String requestID, final String requestString, final String resourcePath, UIListener uiListener, Context mContext) {
        OnlineRequestListener onlineReqListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        if (TextUtils.isEmpty(sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))) {
            createNewCSRFToken(mContext, iReceiveEvent -> {
                if (iReceiveEvent.getResponseStatusCode() == 200) {
                    createInvoiceEntity(requestID, requestString, resourcePath, uiListener, mContext);
                } else if (iReceiveEvent.getResponseStatusCode() == 404) {
                    LogManager.writeLogInfo("Response received from server with code 404 ");
                    AvailableServer.registerServer(mContext, new AvailableServer.RegisterServer() {
                        @Override
                        public void requestSuccess() {
                            createInvoiceEntity(requestID, requestString, resourcePath, uiListener, mContext);
                            LogManager.writeLogInfo("Registered successfully create entity");
                        }

                        @Override
                        public void requestError(String errorMessage) {
                            onlineReqListener.notifyErrorToListenerMsg("");
                        }
                    });
                } else {
                    LogManager.writeLogInfo("Token fetch response code !=200 : " + iReceiveEvent.getResponseStatusCode());
                }
            });
        } else {
            JSONObject jsonObject = null;
            String createdOn = "", createdAt = "";
            String resukltId = "";
            try {
                jsonObject = new JSONObject(requestString);
                if (jsonObject.has(Constants.CreatedOn) && !TextUtils.isEmpty(jsonObject.getString(Constants.CreatedOn)))
                    createdOn = jsonObject.getString(Constants.CreatedOn);
                if (jsonObject.has(Constants.CreatedAt) && !TextUtils.isEmpty(jsonObject.getString(Constants.CreatedAt)))
                    createdAt = jsonObject.getString(Constants.CreatedAt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(createdOn) && !TextUtils.isEmpty(createdAt)) {
                LogManager.writeLogInfo("CSRF token fetched, Posting data to server.");
                String mStrDateTime = com.arteriatech.mutils.common.UtilConstants.getReArrangeDateFormat(createdOn) + Constants.T + com.arteriatech.mutils.common.UtilConstants.convertTimeOnly(createdAt);

                HttpConversationManager manager = new HttpConversationManager(mContext);
                // Create the conversation.
                manager.create(Uri.parse(MyUtils.getInvoiceEndPointURL() + resourcePath))
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                        .addHeader("X-SMP-APPID", Configuration.APP_ID)
                        .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                        .addHeader("RequestID", requestID)
                        .addHeader("RepeatabilityCreation", mStrDateTime)
                        .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                        .addHeader("Accept", "application/json")
                        .setTimeout(TIME_OUT)
                        .setMethod(HttpMethod.POST)
                        .setRequestListener(event -> {
                            if (!TextUtils.isEmpty(requestString))
                                event.getWriter().write(requestString);
                            return null;
                        }).setResponseListener(event -> {
                            // Process the results.
                            if (event.getReader() != null) {
                                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                                //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                                if (event.getResponseStatusCode() == 201) {
                                    LogManager.writeLogInfo("Response received from server with code 201 ");
                                    onlineReqListener.notifySuccessToListener(responseBody);
                                } else if (event.getResponseStatusCode() == 404) {
                                    LogManager.writeLogInfo("Response received from server with code 404 ");
                                    AvailableServer.registerServer(mContext, new AvailableServer.RegisterServer() {
                                        @Override
                                        public void requestSuccess() {
                                            createInvoiceEntity(requestID, requestString, resourcePath, uiListener, mContext);
                                            LogManager.writeLogInfo("Registered successfully create entity");
                                        }

                                        @Override
                                        public void requestError(String errorMessage) {
                                            onlineReqListener.notifyErrorToListenerMsg(responseBody);
                                        }
                                    });
                                } else if (event.getResponseStatusCode() == 403) {
                                    LogManager.writeLogInfo("CSRF token verification failed, Retrying to fetch CSRF token");
                                    createNewCSRFToken(mContext, iReceiveEvent -> {
                                        if (iReceiveEvent.getResponseStatusCode() == 200) {
                                            LogManager.writeLogInfo("Token fetched in retry. posting to server");
                                            createInvoiceEntity(requestID, requestString, resourcePath, uiListener, mContext);
                                        } else {
                                            if (iReceiveEvent.getReader() != null) {
                                                String responseBodytemp = IReceiveEvent.Util.getResponseBody(iReceiveEvent.getReader());
                                                //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                                                LogManager.writeLogInfo("Token fetched failed in retry");
                                                onlineReqListener.notifyErrorToListenerMsg(responseBodytemp);
                                            }
                                        }
                                    });
                                } else {
                                    onlineReqListener.notifyErrorToListenerMsg(responseBody);
                                }
                            }
                        }).start();
            } else {
                LogManager.writeLogInfo("CSRF token fetched, Posting data to server.");
                HttpConversationManager manager = new HttpConversationManager(mContext);
                // Create the conversation.
                manager.create(Uri.parse(MyUtils.getInvoiceEndPointURL() + resourcePath))
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("X-SMP-APPID", Configuration.APP_ID)
                        .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                        .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                        .addHeader("RequestID", requestID)
                        .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                        .addHeader("Accept", "application/json")
                        .setTimeout(TIME_OUT)
                        .setMethod(HttpMethod.POST)
                        .setRequestListener(event -> {
                            if (!TextUtils.isEmpty(requestString))
                                event.getWriter().write(requestString);
                            return null;
                        }).setResponseListener(event -> {
                            // Process the results.
                            if (event.getReader() != null) {
                                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                                //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                                if (event.getResponseStatusCode() == 201) {
                                    CSRF_TOKEN = "";
                                    LogManager.writeLogInfo("Response received from server with code 201 ");
                                    onlineReqListener.notifySuccessToListener(responseBody);
                                } else if (event.getResponseStatusCode() == 403) {
                                    LogManager.writeLogInfo("CSRF token verification failed, Retrying to fetch CSRF token");
                                    createNewCSRFToken(mContext, iReceiveEvent -> {
                                        if (iReceiveEvent.getResponseStatusCode() == 200) {
                                            createInvoiceEntity(requestID, requestString, resourcePath, uiListener, mContext);
                                        } else {
                                            onlineReqListener.notifyErrorToListener(iReceiveEvent);
                                        }
                                    });
                                } else {
                                    onlineReqListener.notifyErrorToListenerMsg(responseBody);
                                }
                            }
                        }).start();
            }
        }
    }


    private static void createNewCSRFToken(Context mContext, IResponseListener var1) {
        LogManager.writeLogInfo("fetching CSRF token started.");
        HttpConversationManager manager = new HttpConversationManager(mContext);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        manager.create(Uri.parse(MyUtils.getInvoiceEndPointURL()))
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("X-SMP-APPID", Configuration.APP_ID)
                .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                .addHeader("x-csrf-token", "fetch")
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("Accept", "application/json")
                .setMethod(HttpMethod.GET)
                .setResponseListener(event1 -> {
                    // Process the results.
//                    String responseBody1 = IReceiveEvent.Util.getResponseBody(event1.getReader());
//                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody1 + " " + event1.getResponseStatusCode());
                    LogManager.writeLogInfo("Token listener called : " + event1.getResponseStatusCode());
                    if (event1.getResponseStatusCode() == 200) {
                        LogManager.writeLogInfo("fetching CSRF token successful.");
                        Map<String, List<String>> mapList = event1.getResponseHeaders();
                        if (mapList != null && mapList.size() > 0) {
                            List<String> arrayList = mapList.get("x-csrf-token");
                            if (arrayList != null && !arrayList.isEmpty()) {
                                CSRF_TOKEN = arrayList.get(0);
                                SharedPreferences.Editor spEdit = sharedPref.edit();
                                spEdit.putString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, arrayList.get(0));
                                spEdit.apply();
                            }
                        }
                    }
                    var1.onResponseReceived(event1);
                }).start();
    }

    public static String doCRSOnlineGetRequest(String resourcePath, Context mContext, IResponseListener iResponseListener, ICommunicationErrorListener iCommunicationErrorListener) {

        HttpConversationManager manager = new HttpConversationManager(mContext);
        manager.create(Uri.parse(MyUtils.getCRSOnlineQryURL() + resourcePath))
                .setMethod(HttpMethod.GET)
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("Accept", "application/json")
                .addHeader("x-smp-appid", Configuration.APP_ID)
                .setResponseListener(iResponseListener).setFlowListener(IConversationFlowListener
                        .prepareFor()
                        .communicationError(iCommunicationErrorListener).build())
                .start();

        return "";
    }

    public static String fetchmSFAGetRequest(String resourcePath, Context mContext, IResponseListener iResponseListener, ICommunicationErrorListener iCommunicationErrorListener) {

        HttpConversationManager manager = new HttpConversationManager(mContext);
        manager.create(Uri.parse(MyUtils.getDefaultmSFAWOnlineQryURL() + resourcePath))
                .setMethod(HttpMethod.GET)
                .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                .addHeader("Accept", "application/json")
                .addHeader("x-smp-appid", Configuration.APP_ID)
                .setResponseListener(iResponseListener).setFlowListener(IConversationFlowListener
                        .prepareFor()
                        .communicationError(iCommunicationErrorListener).build())
                .start();
        return "";
    }


    public static void createEntityPostjds(final String requestID,final String requestString, final String resourcePath, UIListener uiListener, Context mContext) {
        OnlineRequestListener onlineReqListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        if (TextUtils.isEmpty(sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))){
            createExjdCSRFToken(mContext, iReceiveEvent -> {
//                if (iReceiveEvent.getResponseStatusCode() == 200) {
                    createEntityPostjds(requestID, requestString,resourcePath, uiListener, mContext);
//                }else if (iReceiveEvent.getResponseStatusCode() == 404) {
//                    LogManager.writeLogInfo("Response received from server with code 404 ");
//                    AvailableServer.registerServer(mContext, new AvailableServer.RegisterServer() {
//                        @Override
//                        public void requestSuccess() {
//                            createEntityPostjds(requestID, requestString, resourcePath, uiListener, mContext);
//                            LogManager.writeLogInfo("Registered successfully create entity");
//                        }
//
//                        @Override
//                        public void requestError(String errorMessage) {
//                            onlineReqListener.notifyErrorToListenerMsg("");
//                        }
//                    });
//                }else{
//                    LogManager.writeLogInfo("Token fetch response code !=200 : "+iReceiveEvent.getResponseStatusCode());
//                }
            });
        }else {
            LogManager.writeLogInfo("CSRF token fetched, Posting data to server.");
            HttpConversationManager manager = new HttpConversationManager(mContext);
            // Create the conversation.
            manager.create(Uri.parse(MyUtils.getPostjdsOnlineQryURL()))
//                    .addHeader("Content-Type", "application/json; charset=utf-8")
//                    .addHeader("X-SMP-APPCID", sharedPref.getString(UtilRegistrationActivity.KEY_appConnID, ""))
                    .addHeader("x-csrf-token", sharedPref.getString(UtilRegistrationActivity.KEY_xCSRF_TOKEN, ""))
                    .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                    .addHeader("Accept", "application/json")
                    .setTimeout(TIME_OUT)
                    .setMethod(HttpMethod.POST)
                    .setRequestListener(event -> {
                        if (!TextUtils.isEmpty(requestString))
                            event.getWriter().write(requestString);
                        return null;
                    }).setResponseListener(event -> {
                        // Process the results.
                        if (event.getReader() != null) {
                            String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                            //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                            if (event.getResponseStatusCode() == 200) {
                                CSRF_TOKEN = "";
                                LogManager.writeLogInfo("Response received from server with code 201 ");
                                onlineReqListener.notifySuccessToListener(responseBody);
                            } else if (event.getResponseStatusCode() == 403) {
                                LogManager.writeLogInfo("CSRF token verification failed, Retrying to fetch CSRF token");
                                createExjdCSRFToken(mContext, iReceiveEvent -> {
//                                    if (iReceiveEvent.getResponseStatusCode() == 200) {
                                        createEntityPostjds(requestID, requestString, resourcePath, uiListener, mContext);
//                                    } else {
//                                        onlineReqListener.notifyErrorToListener(iReceiveEvent);
//                                    }
                                });
                            } else {
                                onlineReqListener.notifyErrorToListenerMsg(responseBody);
                            }
                        }
                    }).start();

        }
    }


    public static void createEntity(final String requestID, final String requestDate, final String requestString, final String resourcePath, UIListener uiListener, Context mContext) {
        OnlineRequestListener onlineReqListener = new OnlineRequestListener(Operation.Create.getValue(), uiListener);
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFS_NAME, 0);
        if (TextUtils.isEmpty(CSRF_TOKEN)){
            createCSRFToken(mContext, iReceiveEvent -> {
                if (iReceiveEvent.getResponseStatusCode() == 200) {
                    createEntity(requestID,requestDate,requestString, resourcePath, uiListener, mContext);
                }
            });
        }else {
            HttpConversationManager manager = new HttpConversationManager(mContext);
            // Create the conversation.
            manager.create(Uri.parse(MyUtils.getDefaultOnlineQryURL() + resourcePath))
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("X-SMP-APPCID", sharedPref.getString(com.arteriatech.mutils.registration.UtilRegistrationActivity.KEY_appConnID, ""))
                    .addHeader("x-csrf-token", CSRF_TOKEN)
                    .addHeader("RequestID", requestID)
                    .addHeader(Constants.RepeatabilityCreation, requestDate)
                    .addHeader("Authorization", MyUtils.getBasicAuthCredential(mContext))
                    .addHeader("Accept", "application/json")
                    .setMethod(HttpMethod.POST)
                    .setRequestListener(event -> {
                        if (!TextUtils.isEmpty(requestString))
                            event.getWriter().write(requestString);
                        return null;
                    }).setResponseListener(event -> {
                        // Process the results.
                        if (event.getReader()!=null) {
                            String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                            //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                            if (event.getResponseStatusCode() == 201) {
                                CSRF_TOKEN="";
                                onlineReqListener.notifySuccessToListener(null);
                            } else if (event.getResponseStatusCode() == 403) {
                                createCSRFToken(mContext, iReceiveEvent -> {
                                    if (iReceiveEvent.getResponseStatusCode() == 200) {
                                        createEntity(requestID,requestDate,requestString, resourcePath, uiListener, mContext);
                                    } else {
                                        if (iReceiveEvent.getReader()!=null) {
                                            String responseBodytemp = IReceiveEvent.Util.getResponseBody(iReceiveEvent.getReader());
                                            //                    Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                                            onlineReqListener.notifyErrorToListenerMsg(responseBodytemp);
                                        }
                                    }
                                });
                            } else {
                                onlineReqListener.notifyErrorToListenerMsg(responseBody);
                            }
                        }
                    }).start();
        }
    }


}

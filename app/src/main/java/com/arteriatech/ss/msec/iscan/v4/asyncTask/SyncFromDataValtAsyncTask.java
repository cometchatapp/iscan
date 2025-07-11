package com.arteriatech.ss.msec.iscan.v4.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.interfaces.MessageWithBooleanCallBack;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.store.OnlineManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.arteriatech.ss.msec.iscan.v4.common.Constants.REPEATABLE_REQUEST_ID;

/**
 * Created by e10769 on 04-03-2017.
 */

public class SyncFromDataValtAsyncTask extends AsyncTask<String, Boolean, Boolean> {
    private Context mContext;
    private UIListener uiListener;
    private JSONObject dbHeadTable;
    private ArrayList<HashMap<String, String>> arrtable;
    private String[] invKeyValues = null;
    public static JSONObject fetchJsonHeaderObject=null;
    private MessageWithBooleanCallBack dialogCallBack = null;
    private String errorMsg = "";

    public SyncFromDataValtAsyncTask(Context context, String[] invKeyValues, UIListener uiListener,
                                     MessageWithBooleanCallBack dialogCallBack) {
        this.mContext = context;
        this.uiListener = uiListener;
        this.invKeyValues = invKeyValues;
        this.dialogCallBack = dialogCallBack;
    }

    @Override
    protected Boolean doInBackground(String... params) {
//        boolean onlineStoreOpen = false;
        try {
            Constants.IsOnlineStoreFailed = false;
//            Constants.onlineStore = null;
//            OnlineStoreListener.instance = null;
            Constants.AL_ERROR_MSG.clear();

            Constants.ErrorCode = 0;
            Constants.ErrorNo = 0;
            Constants.ErrorName = "";
            Constants.mBoolIsReqResAval=true;
//            onlineStoreOpen = OnlineManager.openOnlineStore(mContext);

//            if (onlineStoreOpen) {
            if (invKeyValues != null) {
                for (int k = 0; k < invKeyValues.length; k++) {
                    String store = ConstantsUtils.getFromDataVault(invKeyValues[k].toString(), mContext);
                    //Fetch object from data vault
                    while (!Constants.mBoolIsReqResAval) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

//                    if (Constants.mBoolIsNetWorkNotAval) {
//                        break;
//                    }
                    Constants.mBoolIsReqResAval = false;
                    try {
                        JSONObject fetchJsonHeaderObject = new JSONObject(store);
                        SyncFromDataValtAsyncTask.fetchJsonHeaderObject = fetchJsonHeaderObject;
                        try {
                            if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
                                if (fetchJsonHeaderObject.getString(Constants.Status).equalsIgnoreCase("000000")) {
                                    Constants.mBoolIsReqResAval =true;
                                    continue;
                                }
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        arrtable = new ArrayList<>();
                        if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SecondarySOCreate)) {
//                                dbHeadTable = Constants.getSOHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                               /* String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);
                                arrtable = UtilConstants.convertToArrayListMap(itemsString);
                                try {
                                    OnlineManager.createSOEntity(dbHeadTable, arrtable, uiListener);
                                } catch (OnlineODataStoreException e) {
                                    e.printStackTrace();
                                    errorMsg = e.getMessage();
                                }*/
                            JSONObject headerObject = Constants.getSOHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                            OnlineManager.createEntity(REPEATABLE_REQUEST_ID,headerObject.toString(), Constants.SSSOs, uiListener, mContext);
                        } else if (fetchJsonHeaderObject.getString(Constants.EntityType).equalsIgnoreCase(Constants.FinancialPostings)) {
                               /* dbHeadTable = Constants.getCollHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                                String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);

                                arrtable = UtilConstants.convertToArrayListMap(itemsString);

                                try {
                                    OnlineManager.createCollectionEntry(dbHeadTable, arrtable, uiListener);

                                } catch (OnlineODataStoreException e) {
                                    e.printStackTrace();
                                    errorMsg = e.getMessage();
                                }*/
                            JSONObject headerObject = Constants.getCollHeaderValuesFromJsonObject(fetchJsonHeaderObject);
//                            OnlineManager.createEntity(headerObject.toString(), Constants.FinancialPostings, uiListener, mContext);
                        } else if (fetchJsonHeaderObject.getString(Constants.EntityType).equalsIgnoreCase(Constants.Feedbacks)) {
                             /*   dbHeadTable = Constants.getFeedbackHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                                String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);

                                arrtable = UtilConstants.convertToArrayListMap(itemsString);

                                try {
                                    OnlineManager.createFeedBack(dbHeadTable, arrtable, uiListener);
                                } catch (OnlineODataStoreException e) {
                                    e.printStackTrace();
                                    errorMsg = e.getMessage();
                                }*/
                            JSONObject headerObject = Constants.getFeedbackHeaderValuesFromJsonObject(fetchJsonHeaderObject);
//                            OnlineManager.createEntity(headerObject.toString(), Constants.Feedbacks, uiListener, mContext);
                        } else if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.SampleDisbursement)) {
                               /* dbHeadTable = Constants.getSSInvoiceHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                                String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);
                                arrtable = UtilConstants.convertToArrayListMap(itemsString);
                                try {
                                    OnlineManager.createSSInvoiceEntity(dbHeadTable, arrtable, uiListener);
                                } catch (OnlineODataStoreException e) {
                                    e.printStackTrace();
                                }*/
                            JSONObject headerObject = Constants.getSSInvoiceHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                            OnlineManager.createEntity(Constants.REPEATABLE_REQUEST_ID,headerObject.toString(), Constants.SSINVOICES, uiListener, mContext);
                        } else if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.ChannelPartners)) {

                              /*  dbHeadTable = Constants.getCPHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                                String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);
                                arrtable = UtilConstants.convertToArrayListMap(itemsString);
                                try {
                                    OnlineManager.createCP(dbHeadTable, arrtable, uiListener);
                                } catch (OnlineODataStoreException e) {
                                    e.printStackTrace();
                                }*/
                            JSONObject headerObject = Constants.getCPHeaderValuesFromJsonObject(fetchJsonHeaderObject,mContext);
                            OnlineManager.createEntity(REPEATABLE_REQUEST_ID,headerObject.toString(), Constants.ChannelPartners, uiListener, mContext);
                        } else if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.ReturnOrderCreate)) {
                            JSONObject headerObject = Constants.getROHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                            OnlineManager.createEntity(REPEATABLE_REQUEST_ID,headerObject.toString(), Constants.SSROs, uiListener, mContext);
                        } else if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.Expenses)) {
                               /* dbHeadTable = Constants.getExpenseHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                                String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);
                                arrtable = UtilConstants.convertToArrayListMap(itemsString);
                                try {
                                    OnlineManager.createDailyExpense(dbHeadTable, arrtable, uiListener);
                                } catch (OnlineODataStoreException e) {
                                    e.printStackTrace();
                                }*/
                            JSONObject headerObject = Constants.getExpenseHeaderValuesFromJsonObject(fetchJsonHeaderObject);
//                            OnlineManager.createEntity(headerObject.toString(), Constants.Expenses, uiListener, mContext);
                        }else if (fetchJsonHeaderObject.getString(Constants.entityType).equalsIgnoreCase(Constants.MaterialDocs)) {
//                        dbHeadTable = Constants.getSOHeaderValuesFromJsonObject(fetchJsonHeaderObject);
//                        String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);
//                        arrtable = UtilConstants.convertToArrayListMap(itemsString);
//                        OnlineManager.createSOEntity(dbHeadTable, arrtable, SyncSelectionActivity.this, SyncSelectionActivity.this);
//                        dbHeadTable = Constants.getSOHeaderValuesFromJsonObject(fetchJsonHeaderObject);
                            JSONObject headerObject = Constants.getMaterialDocsHeaderValuesFromJsonObject(fetchJsonHeaderObject);
//                            OnlineManager.createEntity(headerObject.toString(), Constants.MaterialDocs, uiListener, mContext);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorMsg = e.getMessage();
                    }
                }
            }
//            } else {
//                return onlineStoreOpen;
//            }


        } catch (Exception e) {
            e.printStackTrace();
            errorMsg = e.getMessage();
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (!TextUtils.isEmpty(errorMsg)) {
            setCallBackToUI(false, errorMsg);
        } else if (!aBoolean) {
            setCallBackToUI(aBoolean, Constants.makeMsgReqError(Constants.ErrorNo, mContext, false));
        }

    }


    private void setCallBackToUI(boolean status, String error_Msg) {
        if (dialogCallBack != null) {
            dialogCallBack.clickedStatus(status, error_Msg, null);
        }
    }

}

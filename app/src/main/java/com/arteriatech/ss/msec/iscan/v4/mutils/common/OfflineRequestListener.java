package com.arteriatech.ss.msec.iscan.v4.mutils.common;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.mutils.log.TraceLog;
import com.sap.smp.client.odata.ODataError;
import com.sap.smp.client.odata.ODataPayload;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.store.ODataRequestExecution;
import com.sap.smp.client.odata.store.ODataRequestListener;
import com.sap.smp.client.odata.store.ODataResponse;
import com.sap.smp.client.odata.store.ODataResponseSingle;

import java.util.Map;

/**
 * Created by e10526 on 14-03-2016.
 */
public class OfflineRequestListener implements ODataRequestListener {

    private UIListener uiListener;
    private int operation;
    private  String collectionName="";

    private final int SUCCESS = 0;
    private final int ERROR = -1;

    private Handler uiHandler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == SUCCESS) {
                // Notify the Activity the is complete
                String key = (String) msg.obj;
                try {
                    try {
                        if(uiListener!=null) {
                            uiListener.onRequestSuccess(operation, key);
                        }
                    } catch (OfflineODataStoreException e) {
                        e.printStackTrace();
                    }
                } catch (ODataException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == ERROR) {
                Exception e = (Exception) msg.obj;
                if(uiListener!=null) {
                    uiListener.onRequestError(operation, e);
                }
            }
        }
    };

    public OfflineRequestListener(int operation, UIListener uiListener, String collectionName) {
        super();
        this.operation = operation;
        this.uiListener = uiListener;
        this.collectionName = collectionName;

    }

    public OfflineRequestListener(int operation, String collectionName) {
        super();
        this.operation = operation;
        this.collectionName = collectionName;

    }

    /*****************
     * Methods that implements ODataRequestListener interface
     *****************/

    @Override
    public void requestCacheResponse(ODataRequestExecution request) {
        TraceLog.scoped(this).d(this.collectionName+" : requestCacheResponse");
//        LogManager.writeLogDebug(this.collectionName + " : requestCacheResponse");

    }

    @Override
    public void requestFailed(ODataRequestExecution request, ODataException e) {
        TraceLog.scoped(this).d(this.collectionName+" : requestFailed");
        LogManager.writeLogError(this.collectionName + " : requestFailed " + e.getMessage() + "");
        if (request!=null && request.getResponse() !=null) {
            ODataPayload payload = ((ODataResponseSingle) request.getResponse()).getPayload();
            if (payload!=null && payload instanceof ODataError) {
                ODataError oError = (ODataError) payload;
                TraceLog.d(this.collectionName + " : requestFailed - status message " + oError.getMessage());
                notifyErrorToListener(new OfflineODataStoreException(oError.getMessage()));
                return;
            }
        }
        notifyErrorToListener(e);

    }

    @Override
    public void requestFinished(ODataRequestExecution request) {
        TraceLog.scoped(this).d(this.collectionName+" : requestFinished");
//        LogManager.writeLogDebug(this.collectionName+" : requestFinished");
    }

    @Override
    public void requestServerResponse(ODataRequestExecution request) {
        TraceLog.scoped(this).d(this.collectionName+" : requestServerResponse");

        if (request!=null && request.getResponse() !=null) {
            ODataResponseSingle response = (ODataResponseSingle) request.getResponse();
            Map<ODataResponse.Headers, String> headerMap = response.getHeaders();
            String code  = headerMap.get(ODataResponse.Headers.Code);
            TraceLog.d(this.collectionName + " : requestServerResponse - status code " + code);
            String eTag = headerMap.get(ODataResponse.Headers.ETag);
            TraceLog.scoped(this).d(this.collectionName+" : Before Read requestServerResponse");
            if (!TextUtils.isEmpty(eTag)){
                notifySuccessToListener(eTag);
                return;
            }
//            } else {
//                TraceLog.scoped(this).d(this.collectionName+" : Before Read entity");
//                ODataPayload payload = ((ODataResponseSingle) request.getResponse()).getPayload();
//                if (payload!=null && payload instanceof ODataEntity) {
//                    ODataEntity oEntity = (ODataEntity) payload;
//                    ODataPropMap properties = oEntity.getProperties();
//                    ODataProperty property = properties.get("VisitGUID");
//
//                    TraceLog.scoped(this).d("After Read entity");
//
//                    notifySuccessToListener((String)property.getValue());
//                    return;
//                }
//            }
        }

        notifySuccessToListener(null);
    }

    @Override
    public void requestStarted(ODataRequestExecution request) {
        TraceLog.scoped(this).d(this.collectionName+" : requestStarted");
//        LogManager.writeLogDebug(this.collectionName+" : requestStarted");
    }



    /*****************
     * Utils Methods
     *****************/


    /**
     * Notify the OnlineUIListener that the request was successful.
     */
    protected void notifySuccessToListener(String key) {

//        LogManager.writeLogDebug(this.collectionName+" : Read Odata Key value "+key);
        Message msg = uiHandler.obtainMessage();
        msg.what = SUCCESS;
        msg.obj = key;
        uiHandler.sendMessage(msg);
//        LogManager.writeLogDebug("OfflineRequestListener::notifySuccess");
//        LogManager.writeLogDebug(this.collectionName+" : Request completed successfully");
    }

    /**
     * Notify the OnlineUIListener that the request has an error.
     *
     * @param exception an Exception that denotes the error that occurred.
     */
    protected void notifyErrorToListener(Exception exception) {
        Message msg = uiHandler.obtainMessage();
        msg.what = ERROR;
        msg.obj = exception;
        uiHandler.sendMessage(msg);
        TraceLog.e(this.collectionName + " : OfflineRequestListener::notifyError", exception);
        LogManager.writeLogError(this.collectionName + " : Error while request " + exception.getMessage());
    }

}

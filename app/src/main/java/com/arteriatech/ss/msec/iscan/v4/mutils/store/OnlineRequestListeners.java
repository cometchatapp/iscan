package com.arteriatech.ss.msec.iscan.v4.mutils.store;

import android.os.Bundle;
import android.util.Log;

import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.mutils.log.TraceLog;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataEntitySet;
import com.sap.smp.client.odata.ODataError;
import com.sap.smp.client.odata.ODataPayload;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.store.ODataRequestExecution;
import com.sap.smp.client.odata.store.ODataRequestListener;
import com.sap.smp.client.odata.store.ODataResponseSingle;

import java.util.List;

/**
 * Created by e10769 on 04-09-2017.
 */

public class OnlineRequestListeners implements ODataRequestListener {
    private OnlineODataInterface onlineODataInterface;
    private String TAG = OnlineRequestListeners.class.getSimpleName();
    private Bundle bundle = null;

    public OnlineRequestListeners(OnlineODataInterface onlineODataInterface, Bundle bundle) {
        this.onlineODataInterface = onlineODataInterface;
        this.bundle = bundle;
    }

    @Override
    public void requestStarted(ODataRequestExecution oDataRequestExecution) {
        Log.d(TAG, "requestStarted: ");
        TraceLog.scoped(this).d("requestStarted");
        /*LogManager.writeLogError("requestStarted");*/
    }

    @Override
    public void requestCacheResponse(ODataRequestExecution oDataRequestExecution) {
        Log.d(TAG, "requestCacheResponse: ");
        TraceLog.scoped(this).d("requestCacheResponse");
       /* LogManager.writeLogError("requestCacheResponse");*/
        if (onlineODataInterface != null && bundle != null && bundle.getBoolean(UtilConstants.BUNDLE_READ_FROM_TECHNICAL_CACHE, false)) {
            List<ODataEntity> entities = null;
            try {
                ODataEntitySet payload = (ODataEntitySet) ((ODataResponseSingle) oDataRequestExecution.getResponse()).getPayload();
                entities = payload.getEntities();
            } catch (Exception e) {
                e.printStackTrace();
            }
            onlineODataInterface.responseSuccess(oDataRequestExecution, entities, bundle);
        }
    }

    @Override
    public void requestServerResponse(ODataRequestExecution oDataRequestExecution) {
        Log.d(TAG, "requestServerResponse: ");
        TraceLog.scoped(this).d("requestServerResponse");
       /* LogManager.writeLogError("requestServerResponse");*/
        List<ODataEntity> entities = null;
        try {
            ODataEntitySet payload = (ODataEntitySet) ((ODataResponseSingle) oDataRequestExecution.getResponse()).getPayload();
            entities = payload.getEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        ConstantsUtils.insertSyncTimes(resourcePath);
        boolean isFromTechnicalCache = false;
        if (bundle != null && bundle.getBoolean(UtilConstants.BUNDLE_READ_FROM_TECHNICAL_CACHE, false)) {
            isFromTechnicalCache = true;
        }
        if (onlineODataInterface != null && !isFromTechnicalCache)
            onlineODataInterface.responseSuccess(oDataRequestExecution, entities, bundle);
    }

    @Override
    public void requestFailed(ODataRequestExecution request, ODataException e) {
        Log.d(TAG, "requestFailed: ");
        TraceLog.scoped(this).d("requestFailed");
        String resourcePath = "";
        if (bundle != null)
            resourcePath = bundle.getString("bundle", "");
        LogManager.writeLogError("requestFailed :" + e.getMessage() + " " + resourcePath);
        String errorMsg = e.getMessage();
        if (request != null && request.getResponse() != null) {
            if (request.getResponse().isBatch()) {

            } else {
                ODataPayload payload = ((ODataResponseSingle) request.getResponse()).getPayload();
                if (payload != null && payload instanceof ODataError) {
                    ODataError oError = (ODataError) payload;
                    TraceLog.d("requestFailed - status message " + oError.getMessage());
//                    LogManager.writeLogError("requestFailed");
                    LogManager.writeLogError("Error :" + oError.getMessage());
                    errorMsg = oError.getMessage();
                }
            }
        }
        if (onlineODataInterface != null)
            onlineODataInterface.responseFailed(request, errorMsg, bundle);
    }

    @Override
    public void requestFinished(ODataRequestExecution oDataRequestExecution) {
        Log.d(TAG, "requestFinished: ");
        TraceLog.scoped(this).d("requestFinished");
    }
}

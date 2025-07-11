package com.arteriatech.ss.msec.iscan.v4.mutils.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.arteriatech.mutils.log.LogManager;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataEntitySet;
import com.sap.smp.client.odata.ODataPayload;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.ODataProperty;
import com.sap.smp.client.odata.impl.ODataErrorDefaultImpl;
import com.sap.smp.client.odata.impl.ODataRawValueDefaultImpl;
import com.sap.smp.client.odata.offline.ODataOfflineStore;
import com.sap.smp.client.odata.offline.ODataOfflineStoreOptions;
import com.sap.smp.client.odata.offline.ODataOfflineStoreState;
import com.sap.smp.client.odata.store.ODataRequestParamSingle;
import com.sap.smp.client.odata.store.ODataResponseSingle;
import com.sap.smp.client.odata.store.impl.ODataRequestParamSingleDefaultImpl;

import java.util.HashMap;
import java.util.List;

/**
 * Created by e10742 on 22-11-2016.
 */
public class UtilOfflineManager {
//    public static ODataOfflineStore offlineStore;

    /**
     * CreateOperation a new entity in the local database
     *
     * @param uiListener the activity that will receive the response to notify the user
     * @throws OfflineODataStoreException
     */
    public static void createEntity(ODataOfflineStore offlineStore, ODataEntity newEntity, UIListener uiListener, String collectionName) throws OfflineODataStoreException {
        if (offlineStore == null) return;
        try {
            //Creates the entity payload
//            ODataEntity newEntity = null;
            //createAttendanceEntity(table);
            //Send the request to create the new attendance in the local database

            offlineStore.scheduleCreateEntity(newEntity, collectionName,
                    new OfflineRequestListener(Operation.Create.getValue(), uiListener, collectionName),
                    null);
        } catch (Exception e) {
            throw new OfflineODataStoreException(e);
        }
        //END

    }

    public static void updateEntity(ODataOfflineStore offlineStore, ODataEntity newEntity, UIListener uiListener, String collectionName) throws OfflineODataStoreException {
//Check if the offline oData store has been initialized
        if (offlineStore == null) return;
        try {
//Creates the entity payload
//            ODataEntity newEntity = updateAttendanceEntity(table);
//Send the request to create the new attendance in the local database
//			offlineStore.scheduleCreateEntity(newEntity, Constants.Attendances, new OfflineRequestListener(Operation.CreateOperation.getValue(), uiListener,Constants.Attendances), null);

            offlineStore.scheduleUpdateEntity(newEntity, new OfflineRequestListener(Operation.Update.getValue(), uiListener, collectionName), null);
        } catch (Exception e) {
            throw new OfflineODataStoreException(e);
        }
//END

    }

    @SuppressLint({"NewApi"})
    public static List<ODataEntity> getEntities(ODataOfflineStore offlineStore, String mStrResourcePath) throws OfflineODataStoreException {
        List entities = null;
        if (offlineStore != null) {
            try {
                ODataRequestParamSingleDefaultImpl e = new ODataRequestParamSingleDefaultImpl();
                e.setMode(ODataRequestParamSingle.Mode.Read);
                e.setResourcePath(mStrResourcePath);
                ODataResponseSingle response = (ODataResponseSingle) offlineStore.executeRequest(e);
                if (response.getPayloadType() == ODataPayload.Type.Error) {
                    ODataErrorDefaultImpl feed1 = (ODataErrorDefaultImpl) response.getPayload();
                    throw new OfflineODataStoreException(feed1.getMessage());
                }

                if (response.getPayloadType() != ODataPayload.Type.EntitySet) {
                    throw new OfflineODataStoreException("Invalid payload:EntitySet expected but got " + response.getPayloadType().name());
                }

                ODataEntitySet feed = (ODataEntitySet) response.getPayload();
                entities = feed.getEntities();
            } catch (Exception var7) {
                throw new OfflineODataStoreException(var7);
            }
        }

        return entities;
    }
    @SuppressLint({"NewApi"})
    public static String getEntityCount(ODataOfflineStore offlineStore, String mStrResourcePath) throws OfflineODataStoreException {
        if (offlineStore != null) {
            try {
                ODataRequestParamSingleDefaultImpl e = new ODataRequestParamSingleDefaultImpl();
                e.setMode(ODataRequestParamSingle.Mode.Read);
                e.setResourcePath(mStrResourcePath);
                ODataResponseSingle response = (ODataResponseSingle) offlineStore.executeRequest(e);
                ODataRawValueDefaultImpl valueDefault = (ODataRawValueDefaultImpl) response.getPayload();
                return String.valueOf(valueDefault.getValue());
            } catch (Throwable var7) {
                throw new OfflineODataStoreException(var7);
            }
        }
        return "";
    }

    /**
     * Returns value for selected column name from offline db based on query
     *
     * @throws OfflineODataStoreException
     */
    public static String getColumnVal(ODataOfflineStore offlineStore, String mQry, String columnName) throws OfflineODataStoreException {
        String mStrVal = "";
        if (offlineStore != null) {
            try {
                //Define the resource path
                ODataProperty property;
                ODataPropMap properties;
                ODataRequestParamSingle request = new ODataRequestParamSingleDefaultImpl();
                request.setMode(ODataRequestParamSingle.Mode.Read);
                request.setResourcePath(mQry);
                //Send a request to read the Distributors from the local database
                ODataResponseSingle response = (ODataResponseSingle) offlineStore.
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

                    for (ODataEntity entity : entities) {
                        properties = entity.getProperties();
                        property = properties.get(columnName);
                        mStrVal = String.valueOf(property.getValue());
                        if (!TextUtils.isEmpty(mStrVal)) {
                            return mStrVal;
                        }
                    }


                } else {
                    throw new OfflineODataStoreException("Invalid payload:EntitySet expected but got " + response.getPayloadType().name());
                }
            } catch (Exception e) {
                throw new OfflineODataStoreException(e);
            }
        }
        return mStrVal;

    }
    public static HashMap<String,String> getBackendVersionName(ODataOfflineStore offlineStore, String mQry) throws OfflineODataStoreException{
        String mStrBackEndVersionName="0";
        HashMap<String, String> typesetValues= new HashMap<>();
        if (offlineStore!=null){
            try {
                ODataProperty property;
                ODataPropMap properties;
                //Define the resource path
                ODataRequestParamSingle request = new ODataRequestParamSingleDefaultImpl();
                request.setMode(ODataRequestParamSingle.Mode.Read);
                request.setResourcePath(mQry);
                //Send a request to read the Distributors from the local database
                ODataResponseSingle response = (ODataResponseSingle) offlineStore.
                        executeRequest(request);
                //Check if the response is an error
                if (response.getPayloadType() == ODataPayload.Type.Error) {
                    ODataErrorDefaultImpl error = (ODataErrorDefaultImpl)
                            response.getPayload();
                    throw new OfflineODataStoreException(error.getMessage());
                    //Check if the response contains EntitySet
                }else if (response.getPayloadType() == ODataPayload.Type.EntitySet) {
                    ODataEntitySet feed = (ODataEntitySet) response.getPayload();
                    List<ODataEntity> entities = feed.getEntities();
                    for (ODataEntity entity: entities) {
                        properties = entity.getProperties();
                        try {
                            property = properties.get("TypeValue");
                            mStrBackEndVersionName = String .valueOf(property.getValue());
                        } catch (NumberFormatException exce) {
                            exce.printStackTrace();
                            mStrBackEndVersionName = "0";
                        }
                        property = properties.get("Types");
                        typesetValues.put(property.getValue().toString(),mStrBackEndVersionName);
                    }
                } else {
                    throw new OfflineODataStoreException("Invalid payload:EntitySet expected"
                            + "but got " + response.getPayloadType().name());
                }
            } catch (Exception e) {
                throw new OfflineODataStoreException(e);
            }
        }
        return typesetValues;

    }

    public static boolean closeOfflineStore(Context context, ODataOfflineStoreOptions options,
                                            ODataOfflineStore offlineStore,String sharedPrefName) {
        try {
            UtilOfflineManager.setStoreState(ODataOfflineStoreState.ODataOfflineStoreClosed.name());
            if(offlineStore!=null) {
                offlineStore.closeStore();
                offlineStore.removeStore(context, options);
                offlineStore =null;
                SharedPreferences settings = context.getSharedPreferences(sharedPrefName,
                        0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(UtilConstants.isReIntilizeDB, true);
                editor.commit();
            }
            return true;
        } catch (Exception e) {
            LogManager.writeLogError(UtilConstants.offline_store_not_closed + e.getMessage());
            return false;
        }
    }

    private static String storeState;
    public static String getStoreState() {
        return storeState;
    }

    public static void setStoreState(String state) {
        storeState = state;
    }
}

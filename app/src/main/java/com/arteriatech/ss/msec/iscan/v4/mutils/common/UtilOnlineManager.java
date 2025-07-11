package com.arteriatech.ss.msec.iscan.v4.mutils.common;

import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.ODataProperty;

import java.util.HashMap;
import java.util.List;

/**
 * Created by e10742 on 23-11-2016.
 */
public class UtilOnlineManager {

  /*  public static void createEntity(OnlineODataStore store, ODataEntity newEntity, UIListener uiListener, String collName, String entityType, String docKey) throws OnlineODataStoreException {
        if (store==null) return;
        try {
            //Creates the entity payload
//            ODataEntity newEntity = createMerchndisingEntity(tableHdr, tableItm, store);

            //Send the request to create the new visit in the local database
            store.scheduleCreateEntity(newEntity, collName,
                    new OnlineRequestListener(Operation.Create.getValue(), uiListener, collName, entityType, docKey),
                    null);



        } catch (Exception e) {
            throw new OnlineODataStoreException(e);
        }
        //END

    }

    public static void updateEntity(OnlineODataStore store, ODataEntity newEntity, UIListener uiListener, String collName, String entityType, String docKey) throws OnlineODataStoreException {
        if (store==null) return;
        try {
            //Creates the entity payload
//            ODataEntity newEntity = createMerchndisingEntity(tableHdr, tableItm, store);

            //Send the request to create the new visit in the local database
            store.scheduleUpdateEntity(newEntity,
                    new OnlineRequestListener(Operation.Update.getValue(), uiListener, collName, entityType, docKey),
                    null);



        } catch (Exception e) {
            throw new OnlineODataStoreException(e);
        }
        //END

    }*/


    public static HashMap<String, String> getAppVersionDetails(List<ODataEntity> list){
        String mStrBackEndVersionName="0";
        HashMap<String, String> typesetValues= new HashMap<>();
        ODataProperty property;
        ODataPropMap properties;
        for (ODataEntity entity : list) {
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

        return typesetValues;
    }


}

package com.arteriatech.ss.msec.iscan.v4.utils;


import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineRequestListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.Operation;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.AnnotationUtils;
import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataNavigationProperty;
import com.sap.smp.client.odata.ODataDuration;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataEntitySet;
import com.sap.smp.client.odata.ODataGuid;
import com.sap.smp.client.odata.ODataPayload;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.ODataProperty;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.impl.ODataDurationDefaultImpl;
import com.sap.smp.client.odata.impl.ODataEntityDefaultImpl;
import com.sap.smp.client.odata.impl.ODataEntitySetDefaultImpl;
import com.sap.smp.client.odata.impl.ODataErrorDefaultImpl;
import com.sap.smp.client.odata.impl.ODataGuidDefaultImpl;
import com.sap.smp.client.odata.impl.ODataPropertyDefaultImpl;
import com.sap.smp.client.odata.metadata.ODataMetaProperty;
import com.sap.smp.client.odata.offline.ODataOfflineStore;
import com.sap.smp.client.odata.store.ODataRequestParamSingle;
import com.sap.smp.client.odata.store.ODataResponseSingle;
import com.sap.smp.client.odata.store.ODataStore;
import com.sap.smp.client.odata.store.impl.ODataRequestParamSingleDefaultImpl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

/**
 * Created by e10847.
 */
@SuppressWarnings("all")
public class OfflineUtils<T>{
    //final
    private static final String DEFAULT_GUID="11111111-1111-1111-1111-111111111111";
    private static final String ERROR_GUID="Invalid GUID Found";

    // variables
    private Hashtable<String, ODataProperty> oDataProperties;
    private String ENTITY_SET;
    private String ENTITY_TYPE;
    @Deprecated
    private String NAVIGATION_PROPERTY_ENTITY_SET;
    @Deprecated
    private String NAVIGATION_PROPERTY_ENTITY_TYPE;
    @Deprecated
    private List<T> navigationPropertiesList;
    private UIListener uiListener;
    @Deprecated
    private ArrayList<ODataTable<String, String, ODataMetaProperty.EDMType>> oDataTables;
    private boolean isCreate;
    private boolean isUpdate;
    private boolean isDelete;
    private boolean isETAG;
    private String ETAG;
    private String RESOURCE_PATH;
    private String EDIT_RESOURCE_PATH;
    private ErrorListener errorListener;
    private boolean isErrorOccurred =true;
    private T headerPropertyObject;
    @Deprecated
    private T navigationPropertyObject;
    private String QUERY;
    @Deprecated
    private boolean isNavigationProperty;
    private OfflineUtils() {
    }

    public static class ODataOfflineBuilder<T> {
        private String ENTITY_SET;
        private String ENTITY_TYPE;
        @Deprecated
        private Hashtable<String, ODataProperty> oDataProperties = new Hashtable<>();
        private UIListener uiListener;
        @Deprecated
        private ArrayList<ODataTable<String, String, ODataMetaProperty.EDMType>> oDataTables;
        private ErrorListener errorListener;
        private boolean isCreate;
        private boolean isUpdate;
        private boolean isDelete;
        private boolean isETAG;
        @Deprecated
        private boolean isNavigationProperty;
        private String ETAG;
        private String RESOURCE_PATH;
        private String EDIT_RESOURCE_PATH;
        private String QUERY;
        private T headerPayLoadObject,navigationPropertyObject;
        @Deprecated
        private String NAVIGATION_PROPERTY_ENTITY_SET;
        @Deprecated
        private String NAVIGATION_PROPERTY_ENTITY_TYPE;
        @Deprecated
        private List<T>navigationPropertiesList;

        public ODataOfflineBuilder() {
        }

        public ODataOfflineBuilder setODataEntitySet(String ENTITY_SET) {
            this.ENTITY_SET = ENTITY_SET;
            return this;
        }

        public ODataOfflineBuilder setODataEntityType(String ENTITY_TYPE) {
            this.ENTITY_TYPE = ENTITY_TYPE;
            return this;
        }

        public ODataOfflineBuilder setUIListener(UIListener uiListener) {
            this.uiListener = uiListener;
            return this;
        }

        public ODataOfflineBuilder setCreate(boolean isCreate) {
            this.isCreate = isCreate;
            return this;
        }
        public ODataOfflineBuilder setUpdate(boolean isUpdate) {
            this.isUpdate = isUpdate;
            return this;
        }
        public ODataOfflineBuilder setDelete(boolean isDelete) {
            this.isDelete = isDelete;
            return this;
        }
        public ODataOfflineBuilder setETag(boolean isETagRequired) {
            this.isETAG = isETagRequired;
            return this;
        }
        public ODataOfflineBuilder setResourcePath(String RESOURCE_PATH,String EDIT_RESOURCE_PATH) {
            this.RESOURCE_PATH = RESOURCE_PATH;
            this.EDIT_RESOURCE_PATH = EDIT_RESOURCE_PATH;
            return this;
        }
        public ODataOfflineBuilder setETag(String ETAG) {
            this.ETAG = ETAG;
            return this;
        }
        @Deprecated
        public ODataOfflineBuilder setODataProperties(ArrayList<ODataTable<String, String, ODataMetaProperty.EDMType>> oDataTables) {
            this.oDataTables = oDataTables;
            return this;
        }
        public ODataOfflineBuilder setHeaderPayloadObject(T beanObject) {
            this.headerPayLoadObject = beanObject;
            return this;
        }
        public ODataOfflineBuilder setQuery(String QUERY) {
            this.QUERY = QUERY;
            return this;
        }
        @Deprecated
        public ODataOfflineBuilder setNavigationProperty(boolean isNavigationProperty) {
            this.isNavigationProperty=isNavigationProperty;
            return this;
        }
        @Deprecated
        public ODataOfflineBuilder setNavigationPropertyEntitySet(String enitySet) {
            this.NAVIGATION_PROPERTY_ENTITY_SET=enitySet;
            return this;
        }
        @Deprecated
        public ODataOfflineBuilder setNavigationPropertyEntityType(String enityType) {
            this.NAVIGATION_PROPERTY_ENTITY_TYPE=enityType;
            return this;
        }
        @Deprecated
        public ODataOfflineBuilder setNavigationPropertyList(List<T> list) {
            this.navigationPropertiesList=list;
            return this;
        }
        @Deprecated
        public ODataOfflineBuilder setNavigationPropertyDataObject(T navigationObject) {
            this.navigationPropertyObject=navigationObject;
            return this;
        }

        public OfflineUtils<T> build(ErrorListener errorListener) {
            OfflineUtils<T> offlineUtils = new OfflineUtils<T>();
            offlineUtils.oDataProperties = this.oDataProperties;
            offlineUtils.ENTITY_SET = this.ENTITY_SET;
            offlineUtils.ENTITY_TYPE = this.ENTITY_TYPE;
            offlineUtils.uiListener = this.uiListener;
            offlineUtils.oDataTables =this.oDataTables;
            offlineUtils.errorListener =errorListener;
            offlineUtils.isCreate =this.isCreate;
            offlineUtils.isUpdate =this.isUpdate;
            offlineUtils.isDelete =this.isDelete;
            offlineUtils.RESOURCE_PATH =this.RESOURCE_PATH;
            offlineUtils.EDIT_RESOURCE_PATH =this.EDIT_RESOURCE_PATH;
            offlineUtils.ETAG =ETAG;
            offlineUtils.headerPropertyObject = this.headerPayLoadObject;
            offlineUtils.navigationPropertyObject = this.navigationPropertyObject;
            offlineUtils.isNavigationProperty = this.isNavigationProperty;
            offlineUtils.NAVIGATION_PROPERTY_ENTITY_SET = this.NAVIGATION_PROPERTY_ENTITY_SET;
            offlineUtils.NAVIGATION_PROPERTY_ENTITY_TYPE = this.NAVIGATION_PROPERTY_ENTITY_TYPE;
            offlineUtils.navigationPropertiesList = this.navigationPropertiesList;
            if (oDataTables!=null&&!oDataTables.isEmpty()) {
                offlineUtils.getODataProperties(oDataTables);
            } else if (isNavigationProperty) {
                offlineUtils.oDataGSON(headerPayLoadObject, navigationPropertyObject);
            } else {
                if (headerPayLoadObject != null) {
                    offlineUtils.oDataGSON(headerPayLoadObject);
                }else {
                    errorListener.error("hearder Object","HeaderObject received null");
                }
            }
            return offlineUtils;
        }

        public List<T> returnODataList(ODataOfflineStore offlineStore){
            OfflineUtils<T> offlineUtils = new OfflineUtils<T>();
            offlineUtils.ENTITY_SET = this.ENTITY_SET;
            offlineUtils.ENTITY_TYPE = this.ENTITY_TYPE;
            offlineUtils.uiListener = this.uiListener;
            offlineUtils.isETAG = this.isETAG;
            offlineUtils.headerPropertyObject = headerPayLoadObject;
            offlineUtils.QUERY = QUERY;
            return offlineUtils.getODataList(offlineStore,QUERY, headerPayLoadObject,ENTITY_TYPE);
        }
    }

    private void oDataGSON(T t) {
        String value = "";
        Field field = null;
        List<String> keysList = AnnotationUtils.getAnnotationFields(t.getClass());
        ODataEntity oDataEntity = new ODataEntityDefaultImpl(UtilConstants.getNameSpace(OfflineManager.offlineStore).concat(ENTITY_TYPE));
        for (final String key : keysList) {
            ODataMetaProperty property = OfflineManager.offlineStore.getMetadata().getMetaEntity(UtilConstants.getNameSpace(OfflineManager.offlineStore).concat(ENTITY_TYPE)).getProperty(key);
            if (property != null) {
                try {
                    field = t.getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    field.get(t);
                    try {
                        value = (String) field.get(t);
                    } catch (IllegalAccessException | IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    oDataEntity.getProperties().put(key, convertToODataProperty(OfflineManager.offlineStore, UtilConstants.getNameSpace(OfflineManager.offlineStore).concat(ENTITY_TYPE), key, value));
                } catch (Throwable e) {
                    e.printStackTrace();
                    errorListener.error(String.valueOf(Objects.requireNonNull(field).getName()),e.toString());
                }
            }
        }
        if (TextUtils.isEmpty(ENTITY_SET)) {
            errorListener.error("ODataEntitySet","EntitySet Name required");
            isErrorOccurred =false;
        }else if(TextUtils.isEmpty(ENTITY_TYPE)){
            errorListener.error("ODataEntityType","EntityType Name required");
            isErrorOccurred =false;
        }else{
            if (isErrorOccurred) {
                if (isCreate) {
                    offlineSimpleCreate(ENTITY_SET,oDataEntity,uiListener);
                }else if(isUpdate){
                    if (RESOURCE_PATH !=null&&EDIT_RESOURCE_PATH!=null) {
                        oDataEntity.setResourcePath(RESOURCE_PATH, EDIT_RESOURCE_PATH);
                    }
                    if (ETAG!=null) {
                        oDataEntity.setEtag(ETAG);
                    }
                    offlineSimpleUpdate(ENTITY_SET,oDataEntity,uiListener);
                }else if(isDelete){
                    if (RESOURCE_PATH !=null&&EDIT_RESOURCE_PATH!=null) {
                        oDataEntity.setResourcePath(RESOURCE_PATH, EDIT_RESOURCE_PATH);
                    }
                    if (ETAG!=null) {
                        oDataEntity.setEtag(ETAG);
                    }
                    offlineSimpleDelete(ENTITY_SET,oDataEntity,uiListener);
                }
            }
        }
    }
    @Deprecated
    private void oDataGSON(T t,T navigationPropertyObject) {
        String value = "";
        Field field = null;
        try {
            List<String> keysList = AnnotationUtils.getAnnotationFields(t.getClass(), com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty.class);
            List<String> navigationPropertyKeyList = AnnotationUtils.getAnnotationFields(navigationPropertyObject.getClass(), ODataNavigationProperty.class);
            List<ODataEntity>itemEnityList = new ArrayList<>();
            ODataEntity oDataEntity = new ODataEntityDefaultImpl(UtilConstants.getNameSpace(OfflineManager.offlineStore).concat(ENTITY_TYPE));
            OfflineManager.offlineStore.allocateNavigationProperties(oDataEntity);
            for (final String key : keysList) {
                ODataMetaProperty property = OfflineManager.offlineStore.getMetadata().getMetaEntity(UtilConstants.getNameSpace(OfflineManager.offlineStore).concat(ENTITY_TYPE)).getProperty(key);
                if (property != null) {
                    try {
                        field = t.getClass().getDeclaredField(key);
                        field.setAccessible(true);
                        field.get(t);
                        try {
                            value = (String) field.get(t);
                        } catch (IllegalAccessException | IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        oDataEntity.getProperties().put(key, convertToODataProperty(OfflineManager.offlineStore, UtilConstants.getNameSpace(OfflineManager.offlineStore).concat(ENTITY_TYPE), key, value));
                    } catch (Throwable e) {
                        e.printStackTrace();
                        errorListener.error(String.valueOf(Objects.requireNonNull(field).getName()),e.toString());
                    }
                }
            }
            for (T object:navigationPropertiesList) {
                ODataEntity itemODataEntity = new ODataEntityDefaultImpl(UtilConstants.getNameSpace(OfflineManager.offlineStore).concat(ENTITY_TYPE));
                for (final String key : navigationPropertyKeyList) {
                    ODataMetaProperty property = OfflineManager.offlineStore.getMetadata().getMetaEntity(UtilConstants.getNameSpace(OfflineManager.offlineStore).concat(NAVIGATION_PROPERTY_ENTITY_TYPE)).getProperty(key);
                    if (property != null) {
                        try {
                            field = object.getClass().getDeclaredField(key);
                            field.setAccessible(true);
                            field.get(object);
                            try {
                                value = (String) field.get(object);
                            } catch (IllegalAccessException | IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                            itemODataEntity.getProperties().put(key, convertToODataProperty(OfflineManager.offlineStore, UtilConstants.getNameSpace(OfflineManager.offlineStore).concat(NAVIGATION_PROPERTY_ENTITY_TYPE), key, value));
                        } catch (Throwable e) {
                            e.printStackTrace();
                            errorListener.error(String.valueOf(Objects.requireNonNull(field).getName()),e.toString());
                        }
                    }
                }
                itemEnityList.add(itemODataEntity);
            }
            if (!itemEnityList.isEmpty()){
                ODataEntitySetDefaultImpl itemEntity = new ODataEntitySetDefaultImpl(itemEnityList.size(), null, null);
                for (ODataEntity entity : itemEnityList) {
                    itemEntity.getEntities().add(entity);
                }
                itemEntity.setResourcePath(NAVIGATION_PROPERTY_ENTITY_SET);
                com.sap.smp.client.odata.ODataNavigationProperty navigationProperty = oDataEntity.getNavigationProperty(NAVIGATION_PROPERTY_ENTITY_SET);
                navigationProperty.setNavigationContent(itemEntity);
                oDataEntity.setNavigationProperty(NAVIGATION_PROPERTY_ENTITY_SET,navigationProperty);
            }

            if (TextUtils.isEmpty(NAVIGATION_PROPERTY_ENTITY_SET)) {
                errorListener.error("NavigationPropertyEntitySet","EntitySet Name required");
                isErrorOccurred =false;
            }else if(TextUtils.isEmpty(NAVIGATION_PROPERTY_ENTITY_TYPE)){
                errorListener.error("NavigationPropertyEntityType","EntityType Name required");
                isErrorOccurred =false;
            }else{
                if (isErrorOccurred) {
                    if (isCreate) {
                        offlineSimpleCreate(ENTITY_SET,oDataEntity,uiListener);
                    }else if(isUpdate){
    //                    offlineSimpleUpdate(ENTITY_SET,ENTITY_TYPE,oDataProperties,uiListener);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    /**
     * This method will get all the properties and will convert string to OData properties
     * @param oDataTables
     */
    @Deprecated
    private void getODataProperties(ArrayList<ODataTable<String, String, ODataMetaProperty.EDMType>> oDataTables){
        ODataProperty oDataProperty;
        if (!oDataTables.isEmpty()) {
            for (ODataTable<String, String, ODataMetaProperty.EDMType> table :oDataTables) {
                ODataMetaProperty.EDMType type = table.getType();
                switch (type){
                    case Null:
                        break;
                    case Binary:
                        break;
                    case Boolean:
                        break;
                    case SByte:
                        break;
                    case Byte:
                        break;
                    case Int16:
                        break;
                    case Int32:
                        break;
                    case Int64:
                        break;
                    case Single:
                        break;
                    case Double:
                        try {
                            oDataProperties.put(table.getKey(),returnODataDecimal(table.getKey(), table.getValue()));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            errorListener.error(table.getKey(),e.toString());
                        }
                        break;
                    case Decimal:
                        try {
                            oDataProperties.put(table.getKey(),returnODataDecimal(table.getKey(), table.getValue()));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            errorListener.error(table.getKey(),e.toString());
                        }
                        break;
                    case String:
                        oDataProperties.put(table.getKey(),returnODataString(table.getKey(),table.getValue()));
                        break;
                    case Guid:
                        oDataProperty =returnODataGUID(table.getKey(), table.getValue());
                        if (oDataProperty!=null) {
                            oDataProperties.put(table.getKey(),oDataProperty);
                        }
                        break;
                    case DateTime:
                        oDataProperty = returnODataDateTime(table.getKey(), table.getValue());
                        if (oDataProperty!=null) {
                            oDataProperties.put(table.getKey(),oDataProperty);
                        }
                        break;
                    case Time:
                        oDataProperty = returnODataTime(table.getKey(), table.getValue());
                        if (oDataProperty!=null) {
                            oDataProperties.put(table.getKey(),oDataProperty);
                        }
                        break;
                    case DateTimeOffset:
                        break;
                    case Complex:
                        break;
                }
            }
        }else{
            errorListener.error("ODataProperties","unable to process, Properties are empty");
            isErrorOccurred =false;
        }
        if (TextUtils.isEmpty(ENTITY_SET)) {
            errorListener.error("ODataEntitySet","EntitySet Name required");
            isErrorOccurred =false;
        }else if(TextUtils.isEmpty(ENTITY_TYPE)){
            errorListener.error("ODataEntityType","EntityType Name required");
            isErrorOccurred =false;
        }else{
            if (isErrorOccurred) {
                if (isCreate) {
                    offlineSimpleCreate(ENTITY_SET,ENTITY_TYPE,oDataProperties,uiListener);
                }else if(isUpdate){
                    offlineSimpleUpdate(ENTITY_SET,ENTITY_TYPE,oDataProperties,uiListener);
                }
            }
        }
    }

    /**
     * This is simple create method for offline Mode.
     * @param entitySet entitySet name needs to be passed here
     * @param entityType entityType to be passed.
     * @param hashTable this will throw all the available properties belongs to the entity. here we need to pass the string and ODataProperty pair values
     * @param uiListener this will give error or success listener
     * @throws Exception
     */
    @Deprecated
    private void offlineSimpleCreate(@NonNull String entitySet, @NonNull String entityType, @NonNull Hashtable<String, ODataProperty> hashTable, @NonNull UIListener uiListener) {
        ODataEntity oDataEntity = new ODataEntityDefaultImpl(UtilConstants.getNameSpace(OfflineManager.offlineStore)+entityType);
        try {
            OfflineManager.offlineStore.allocateProperties(oDataEntity, ODataStore.PropMode.Keys).getProperties().putAll(hashTable);
        } catch (ODataException e) {
            e.printStackTrace();
        }
        if (OfflineManager.offlineStore != null) {
            OfflineManager.offlineStore.scheduleCreateEntity(oDataEntity, entitySet, new OfflineRequestListener(Operation.Create.getValue(), uiListener, entitySet), null);
        }
    }
    /**
     * This is simple create method for offline Mode.
     * @param entitySet entitySet name needs to be passed here
     * @param uiListener this will give error or success listener
     * @throws Exception
     */
    private void offlineSimpleCreate(@NonNull String entitySet, @NonNull ODataEntity oDataEntity,@NonNull UIListener uiListener) {
        if (OfflineManager.offlineStore != null) {
            OfflineManager.offlineStore.scheduleCreateEntity(oDataEntity, entitySet, new OfflineRequestListener(Operation.Create.getValue(), uiListener, entitySet), null);
        }
    }

    /**
     * This is simple update method for offline Mode.
     * @param entitySet  entitySet name needs to be passed here
     * @param oDataEntity  entityName name needs to be passed here
     * @param uiListener this will give error or success listener
     */
    private void offlineSimpleUpdate(@NonNull String entitySet, @NonNull ODataEntity oDataEntity,@NonNull UIListener uiListener) {
        if (OfflineManager.offlineStore != null) {
            OfflineManager.offlineStore.scheduleUpdateEntity(oDataEntity, new OfflineRequestListener(Operation.Update.getValue(), uiListener, entitySet), null);
        }
    }
    /**
     * This is simple update method for offline Mode.
     * @param entitySet  entitySet name needs to be passed here
     * @param oDataEntity  entityName name needs to be passed here
     * @param uiListener this will give error or success listener
     */
    private void offlineSimpleDelete(@NonNull String entitySet, @NonNull ODataEntity oDataEntity,@NonNull UIListener uiListener) {
        if (OfflineManager.offlineStore != null) {
            OfflineManager.offlineStore.scheduleDeleteEntity(oDataEntity, new OfflineRequestListener(Operation.Delete.getValue(), uiListener, entitySet), null);
        }
    }

    /**
     * This is simple update method for offline Mode.
     * @param entitySet entitySet name needs to be passed here
     * @param entityType entityType to be passed.
     * @param hashTable this will throw all the available properties belongs to the entity. here we need to pass the string and ODataProperty pair values
     * @param uiListener this will give error or success listener
     * @throws Exception
     */
    @Deprecated
    private void offlineSimpleUpdate(@NonNull String entitySet, @NonNull String entityType, @NonNull Hashtable<String, ODataProperty> hashTable, @NonNull UIListener uiListener) {
        ODataEntity oDataEntity = new ODataEntityDefaultImpl(UtilConstants.getNameSpace(OfflineManager.offlineStore)+entityType);
        try {
            OfflineManager.offlineStore.allocateProperties(oDataEntity, ODataStore.PropMode.Keys).getProperties().putAll(hashTable);
            if (RESOURCE_PATH !=null&&EDIT_RESOURCE_PATH!=null) {
                oDataEntity.setResourcePath(RESOURCE_PATH, EDIT_RESOURCE_PATH);
            }
            if (ETAG!=null) {
                oDataEntity.setEtag(ETAG);
            }
        } catch (ODataException e) {
            e.printStackTrace();
        }
        if (OfflineManager.offlineStore != null) {
            OfflineManager.offlineStore.scheduleUpdateEntity(oDataEntity,new OfflineRequestListener(Operation.Update.getValue(), uiListener, entitySet), null);
        }
    }

    /**
     *
     * @param offlineStore
     * @param entityName  ex: ZART_SFGW_ALL.ZZPriceTracker this must be declared as namespace entityType
     * @param key
     */
    public ODataProperty convertToODataProperty(@NonNull ODataOfflineStore offlineStore, @NonNull String entityName, @NonNull String key, @NonNull String value){
        ODataProperty oDataValue=null;
        try {
            ODataMetaProperty.EDMType type= offlineStore.getMetadata().getMetaEntity(entityName).getProperty(key).getType();
            switch (ODataMetaProperty.EDMType.fromString(type.getText())){
                case Null:
                    break;
                case Binary:
                    break;
                case Boolean:
                    break;
                case SByte:
                    break;
                case Byte:
                    break;
                case Int16:
                    break;
                case Int32:
                    break;
                case Int64:
                    break;
                case Single:
                    break;
                case Double:
                    oDataValue = returnODataDecimal(key, value);
                    break;
                case Decimal:
                    oDataValue = returnODataDecimal(key, value);
                    break;
                case String:
                    oDataValue = returnODataString(key, value);
                    break;
                case Guid:
                    oDataValue = returnODataGUID(key, value);
                    break;
                case DateTime:
                    oDataValue = returnODataDateTime(key, value);
                    break;
                case Time:
                    oDataValue = returnODataTime(key,value);
                    break;
                case DateTimeOffset:
                    break;
                case Complex:
                    break;
            }
        } catch (Throwable  e) {
            e.printStackTrace();
        }
        return oDataValue;
    }


    /**
     *
     * @param key  String Key Field
     * @param value String Value Field, and this will be converted as ODataProperty Edm String
     * @return key,value pair will be returned as ODataProperty
     */
    private ODataProperty returnODataString(String key,String value){
        if (!TextUtils.isEmpty(key)&&!TextUtils.isEmpty(value)){
            return new ODataPropertyDefaultImpl(key,value);
        }else{
            return new ODataPropertyDefaultImpl(key,"");
        }
    }
    /**
     *
     * @param key  String Key Field
     * @param value String Value Field, and this will be converted as ODataProperty Edm Decimal
     * @return key,value pair will be returned as ODataProperty
     */
    private ODataProperty returnODataDecimal(String key,String value) throws NumberFormatException{
        if (!TextUtils.isEmpty(key)&&!TextUtils.isEmpty(value)){
            return new ODataPropertyDefaultImpl(key, BigDecimal.valueOf(Double.parseDouble(value)));
        }else{
            return new ODataPropertyDefaultImpl(key,BigDecimal.valueOf(0.0));
        }
    }
    /**
     *
     * @param key  String Key Field
     * @param value String Value Field, and this will be converted as ODataProperty Edm GUID
     * @return key,value pair will be returned as ODataProperty
     */
    private ODataProperty returnODataGUID(String key,String value){
        if (key!=null&&value!=null) {
            if (!TextUtils.isEmpty(key)&&!TextUtils.isEmpty(value)&&value.length()==32||value.length()==36){
                return new ODataPropertyDefaultImpl(key, ODataGuidDefaultImpl.initWithString32(value));
            }else if (!TextUtils.isEmpty(key)&&!TextUtils.isEmpty(value)&&value.length()<32){
                errorListener.error(key,ERROR_GUID);
                isErrorOccurred =false;
                return null;
            }else{
                return new ODataPropertyDefaultImpl(key,null);
            }
        }else{
            return new ODataPropertyDefaultImpl(key,null);
        }
    }
    /**
     *
     * @param key  String Key Field
     * @param value String Value Field, and this will be converted as ODataProperty Edm DateTime
     * @return key,value pair will be returned as ODataProperty
     */
    private ODataProperty returnODataDateTime(String key,String value){
        if (!TextUtils.isEmpty(key)&&!TextUtils.isEmpty(value)){
            try {
                return new ODataPropertyDefaultImpl(key, ODataDateTimeFormat(value));
            } catch (Exception e) {
                e.printStackTrace();
                errorListener.error(key,"Invalid Date");
                isErrorOccurred =false;
                return null;
            }
        }else{
            return new ODataPropertyDefaultImpl(key,null);
        }
    }
    /**
     *
     * @param key  String Key Field
     * @param value String Value Field, and this will be converted as ODataProperty Edm Time
     * @return key,value pair will be returned as ODataProperty
     */
    private ODataProperty returnODataTime(String key,String value){
        if (!TextUtils.isEmpty(key)&&!TextUtils.isEmpty(value)){
            try {
                return new ODataPropertyDefaultImpl(key, getTimeAsODataDuration(value));
            } catch (Exception e) {
                e.printStackTrace();
                errorListener.error(key,"Invalid Time");
                isErrorOccurred =false;
                return null;
            }
        }else{
            return new ODataPropertyDefaultImpl(key,null);
        }
    }

// Offline Get methods and members
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private List<T>getODataList(@NonNull ODataOfflineStore offlineStore,String query,T t,String entityName) {
        List<T>arrayList = new ArrayList<>();
        try {

            if (entityName!=null&&entityName.contains(".SchemeSalesArea")) {
                LogManager.writeLogDebug("DIST : Annot : "+entityName);
            }
            List<String> keysList = AnnotationUtils.getAnnotationFields(t.getClass());
            ODataProperty property;
            ODataPropMap properties;
            ODataRequestParamSingle request = new ODataRequestParamSingleDefaultImpl();
            request.setMode(ODataRequestParamSingle.Mode.Read);
            request.setResourcePath(query);
            try {
                if (entityName!=null&&entityName.contains(".SchemeSalesArea")) {
                    LogManager.writeLogDebug("DIST : Before Query execution : "+entityName);
                    Log.e("DIST: ","Before Query execution : "+entityName);
                }
                ODataResponseSingle response = (ODataResponseSingle) offlineStore.executeRequest(request);
                if (entityName!=null&&entityName.contains(".SchemeSalesArea")) {
                    LogManager.writeLogDebug("DIST : After Query execution : "+entityName);
                    Log.e("DIST: ","After Query execution : "+entityName);
                }
                if (response.getPayloadType() == ODataPayload.Type.Error) {
                    ODataErrorDefaultImpl error = (ODataErrorDefaultImpl) response.getPayload();
                    throw new OfflineODataStoreException(error.getMessage());
                }else if (response.getPayloadType() == ODataPayload.Type.EntitySet) {
                    ODataEntitySet feed = (ODataEntitySet) response.getPayload();
                    List<ODataEntity> entities = feed.getEntities();
                    if (entityName!=null&&entityName.contains(".SchemeSalesArea")) {
                        LogManager.writeLogDebug("DIST : Before Loop : "+entityName);
                    }
                    for (ODataEntity entity : entities) {
                        properties = entity.getProperties();
                        t = (T) t.getClass().newInstance();
                        for (final String key:keysList) {
                            property = properties.get(key);
                            if (property!=null) {
                                try {
                                    Field field = t.getClass().getDeclaredField(key);
                                    field.setAccessible(true);
                                    field.set(t, validateEdmType(offlineStore,entityName,key,property));
                                    if (isETAG) {
                                        Field etagField = AnnotationUtils.getAnnotationETagField(t.getClass());
                                        etagField.setAccessible(true);
                                        etagField.set(t,entity.getEtag());
                                    }
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        arrayList.add(t);
                    }
                    if (entityName!=null&&entityName.contains(".SchemeSalesArea")) {
                        LogManager.writeLogDebug("DIST : After Loop : "+entityName);
                    }
                } else {
                    throw new OfflineODataStoreException(Constants.invalid_payload_entityset_expected + response.getPayloadType().name());
                }
            } catch (ODataException | OfflineODataStoreException e) {
                e.printStackTrace();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return arrayList;
    }


    /**
     *
     * @param offlineStore
     * @param entityName  ex: ZART_SFGW_ALL.ZZPriceTracker this must be declared as namespace entityType
     * @param key
     */
    private String validateEdmType(@NonNull ODataOfflineStore offlineStore,@NonNull String entityName,@NonNull String key,@NonNull ODataProperty property){
        String value="";
        try {
            ODataMetaProperty.EDMType type= offlineStore.getMetadata().getMetaEntity(UtilConstants.getNameSpace(offlineStore).concat(entityName)).getProperty(key).getType();
            switch (ODataMetaProperty.EDMType.fromString(type.getText())){
                case Null:
                    break;
                case Binary:
                    break;
                case Boolean:
                    break;
                case SByte:
                    break;
                case Byte:
                    break;
                case Int16:
                    break;
                case Int32:
                    break;
                case Int64:
                    break;
                case Single:
                    break;
                case Double:
                    break;
                case Decimal:
                    value = getDecimalValue(property);
                    break;
                case String:
                    value = property.getValue().toString();
                    break;
                case Guid:
                    value = getGuidValue(property);
                    break;
                case DateTime:
                    value = getDateTimeValue(property);
                    break;
                case Time:
                    value = property.getValue().toString();
//                    value = getTime(property);
                    break;
                case DateTimeOffset:
                    break;
                case Complex:
                    break;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * returns the string value for BigDecimal
     * @param property
     * @return
     */
    private String getDecimalValue(ODataProperty property){
        String value ="";
        try {
            BigDecimal decimal = (BigDecimal) property.getValue();
           value = decimal.toString();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }
    /**
     * returns the string value for BigDecimal
     * @param property
     * @return
     */
    private String getGuidValue(ODataProperty property){
        String value ="";
        try {
            if (property.getValue()!=null) {
                ODataGuid cpguid = (ODataGuid) property.getValue();
                value = cpguid.guidAsString36();
                if (value!=null&&value.equalsIgnoreCase("0000-0000")){
                    value = DEFAULT_GUID;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value;
    }
    /**
     * returns the string value for DateTime
     * @param property
     * @return
     */
    private String  getDateTimeValue(ODataProperty property){
        String value ="";
        try {
            value = UtilConstants.convertCalenderToStringFormat((GregorianCalendar) property.getValue());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value;
    }
    /**
     * returns the string value for time
     * @param property
     * @return
     */
    private String getTime(ODataProperty property){
        String value ="";
        try {
            value = UtilConstants.convertCalenderToStringFormat((GregorianCalendar) property.getValue());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value;
    }
    private static ODataDuration getTimeAsODataDuration(String timeString) {

        List<String> timeDuration = null;
        if (timeString.contains("-")) {
            timeDuration = Arrays.asList(timeString.split("-"));
        }else if(timeString.contains(":")){
            timeDuration = Arrays.asList(timeString.split(":"));
        }
        int hour = Integer.parseInt(timeDuration.get(0));
        int minute = Integer.parseInt(timeDuration.get(1));
        int seconds = Integer.parseInt(timeDuration.get(2));
        ODataDuration oDataDuration = null;
        try {
            oDataDuration = new ODataDurationDefaultImpl();
            oDataDuration.setHours(hour);
            oDataDuration.setMinutes(minute);
            oDataDuration.setSeconds(BigDecimal.valueOf(seconds));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oDataDuration;
    }
    private static Calendar ODataDateTimeFormat(String inputDate) {
        Date date = null;
        if (inputDate.contains("/")){
            inputDate = inputDate.replace("/","-");
        }
        Calendar curCal = new GregorianCalendar();

        try {
            String newDate = formatDate("dd-MM-yyyy",inputDate,"yyyy-MM-dd");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date = format.parse(newDate);
            curCal.setTime(date);
            System.out.println("Date" + curCal.getTime());
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return curCal;
    }

    private static String formatDate(String existingFormat, String inputDate, String inputFormat) {
        SimpleDateFormat currentFormat = null;
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(existingFormat);
            date = simpleDateFormat.parse(inputDate);
            currentFormat = new SimpleDateFormat(inputFormat);
            currentFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentFormat.format(date);
    }
}

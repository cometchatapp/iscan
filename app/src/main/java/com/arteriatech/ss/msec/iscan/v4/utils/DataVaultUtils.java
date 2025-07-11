package com.arteriatech.ss.msec.iscan.v4.utils;

import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
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
import java.util.List;
import java.util.Objects;

public class DataVaultUtils<T> {
    //final
    private static final String DEFAULT_GUID="00000000-0000-0000-0000-000000000000";
    private static final String ERROR_GUID="Invalid GUID Found";

    // variables
    private String ENTITY_SET;
    private String ENTITY_TYPE;
   
    private String NAVIGATION_PROPERTY_ENTITY_SET;
    private String NAVIGATION_PROPERTY_ENTITY_TYPE;
    
    private List<T> navigationPropertiesList;
    private UIListener uiListener;
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
    private T navigationPropertyObject;
    private String QUERY;
    private boolean isNavigationProperty;
    private DataVaultUtils() {
    }

    public static class DataVaultBuilder<T> {
        private String ENTITY_SET;
        private String ENTITY_TYPE;
        private UIListener uiListener;
        private ErrorListener errorListener;
        private boolean isCreate;
        private boolean isUpdate;
        private boolean isDelete;
        private boolean isETAG;
        private boolean isNavigationProperty;
        private String ETAG;
        private String RESOURCE_PATH;
        private String EDIT_RESOURCE_PATH;
        private String QUERY;
        private T headerPayLoadObject,navigationPropertyObject;
        private String NAVIGATION_PROPERTY_ENTITY_SET;
        private String NAVIGATION_PROPERTY_ENTITY_TYPE;
        private List<T> navigationPropertiesList;

        public DataVaultBuilder() {
        }

        public DataVaultBuilder setODataEntitySet(String ENTITY_SET) {
            this.ENTITY_SET = ENTITY_SET;
            return this;
        }

        public DataVaultBuilder setODataEntityType(String ENTITY_TYPE) {
            this.ENTITY_TYPE = ENTITY_TYPE;
            return this;
        }

        public DataVaultBuilder setUIListener(UIListener uiListener) {
            this.uiListener = uiListener;
            return this;
        }

        public DataVaultBuilder setCreate(boolean isCreate) {
            this.isCreate = isCreate;
            return this;
        }
        public DataVaultBuilder setUpdate(boolean isUpdate) {
            this.isUpdate = isUpdate;
            return this;
        }
        public DataVaultBuilder setDelete(boolean isDelete) {
            this.isDelete = isDelete;
            return this;
        }
        public DataVaultBuilder setETag(boolean isETagRequired) {
            this.isETAG = isETagRequired;
            return this;
        }
        public DataVaultBuilder setResourcePath(String RESOURCE_PATH,String EDIT_RESOURCE_PATH) {
            this.RESOURCE_PATH = RESOURCE_PATH;
            this.EDIT_RESOURCE_PATH = EDIT_RESOURCE_PATH;
            return this;
        }
        public DataVaultBuilder setETag(String ETAG) {
            this.ETAG = ETAG;
            return this;
        }

        public DataVaultBuilder setHeaderPayloadObject(T beanObject) {
            this.headerPayLoadObject = beanObject;
            return this;
        }
        public DataVaultBuilder setQuery(String QUERY) {
            this.QUERY = QUERY;
            return this;
        }
        public DataVaultBuilder setNavigationProperty(boolean isNavigationProperty) {
            this.isNavigationProperty=isNavigationProperty;
            return this;
        }

        public DataVaultBuilder setNavigationPropertyEntitySet(String enitySet) {
            this.NAVIGATION_PROPERTY_ENTITY_SET=enitySet;
            return this;
        }

        public DataVaultBuilder setNavigationPropertyEntityType(String enityType) {
            this.NAVIGATION_PROPERTY_ENTITY_TYPE=enityType;
            return this;
        }

        public DataVaultBuilder setNavigationPropertyList(List<T> list) {
            this.navigationPropertiesList=list;
            return this;
        }

        public DataVaultBuilder setNavigationPropertyDataObject(T navigationObject) {
            this.navigationPropertyObject=navigationObject;
            return this;
        }

        public DataVaultUtils<T> build(ErrorListener errorListener) {
            DataVaultUtils<T> DataVaultUtils = new DataVaultUtils<T>();
            DataVaultUtils.ENTITY_SET = this.ENTITY_SET;
            DataVaultUtils.ENTITY_TYPE = this.ENTITY_TYPE;
            DataVaultUtils.uiListener = this.uiListener;
            DataVaultUtils.errorListener =errorListener;
            DataVaultUtils.isCreate =this.isCreate;
            DataVaultUtils.isUpdate =this.isUpdate;
            DataVaultUtils.isDelete =this.isDelete;
            DataVaultUtils.RESOURCE_PATH =this.RESOURCE_PATH;
            DataVaultUtils.EDIT_RESOURCE_PATH =this.EDIT_RESOURCE_PATH;
            DataVaultUtils.ETAG =ETAG;
            DataVaultUtils.headerPropertyObject = this.headerPayLoadObject;
            DataVaultUtils.navigationPropertyObject = this.navigationPropertyObject;
            DataVaultUtils.isNavigationProperty = this.isNavigationProperty;
            DataVaultUtils.NAVIGATION_PROPERTY_ENTITY_SET = this.NAVIGATION_PROPERTY_ENTITY_SET;
            DataVaultUtils.NAVIGATION_PROPERTY_ENTITY_TYPE = this.NAVIGATION_PROPERTY_ENTITY_TYPE;
            DataVaultUtils.navigationPropertiesList = this.navigationPropertiesList;
             if (isNavigationProperty) {
                DataVaultUtils.oDataGSON(headerPayLoadObject, navigationPropertyObject);
            } else {
                if (headerPayLoadObject != null) {
                    DataVaultUtils.oDataGSON(headerPayLoadObject);
                }else {
                    errorListener.error("hearder Object","HeaderObject received null");
                }
            }
            return DataVaultUtils;
        }

        public List<T> returnODataList(ODataOfflineStore offlineStore){
            DataVaultUtils<T> DataVaultUtils = new DataVaultUtils<T>();
            DataVaultUtils.ENTITY_SET = this.ENTITY_SET;
            DataVaultUtils.ENTITY_TYPE = this.ENTITY_TYPE;
            DataVaultUtils.uiListener = this.uiListener;
            DataVaultUtils.isETAG = this.isETAG;
            DataVaultUtils.headerPropertyObject = headerPayLoadObject;
            DataVaultUtils.QUERY = QUERY;
            return DataVaultUtils.getODataList(offlineStore,QUERY, headerPayLoadObject,ENTITY_TYPE);
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
//                    offlineSimpleCreate(ENTITY_SET,oDataEntity,uiListener);
                }else if(isUpdate){
                    if (RESOURCE_PATH !=null&&EDIT_RESOURCE_PATH!=null) {
                        oDataEntity.setResourcePath(RESOURCE_PATH, EDIT_RESOURCE_PATH);
                    }
                    if (ETAG!=null) {
                        oDataEntity.setEtag(ETAG);
                    }
//                    offlineSimpleUpdate(ENTITY_SET,oDataEntity,uiListener);
                }else if(isDelete){
                    if (RESOURCE_PATH !=null&&EDIT_RESOURCE_PATH!=null) {
                        oDataEntity.setResourcePath(RESOURCE_PATH, EDIT_RESOURCE_PATH);
                    }
                    if (ETAG!=null) {
                        oDataEntity.setEtag(ETAG);
                    }
//                    offlineSimpleDelete(ENTITY_SET,oDataEntity,uiListener);
                }
            }
        }
    }

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
                        //offlineSimpleCreate(ENTITY_SET,oDataEntity,uiListener);
                    }else if(isUpdate){
                        //offlineSimpleUpdate(ENTITY_SET,ENTITY_TYPE,oDataProperties,uiListener);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
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
        } catch (Throwable e) {
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
        if (!TextUtils.isEmpty(key)&&!TextUtils.isEmpty(value)&&value.length()==32||value.length()==36){
            return new ODataPropertyDefaultImpl(key, ODataGuidDefaultImpl.initWithString32(value));
        }else if (!TextUtils.isEmpty(key)&&!TextUtils.isEmpty(value)&&value.length()<32){
            errorListener.error(key,ERROR_GUID);
            isErrorOccurred =false;
            return null;
        }else{
            return new ODataPropertyDefaultImpl(key,ODataGuidDefaultImpl.initWithString32(DEFAULT_GUID));
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
            List<String> keysList = AnnotationUtils.getAnnotationFields(t.getClass());
            ODataProperty property;
            ODataPropMap properties;
            ODataRequestParamSingle request = new ODataRequestParamSingleDefaultImpl();
            request.setMode(ODataRequestParamSingle.Mode.Read);
            request.setResourcePath(query);
            try {
                ODataResponseSingle response = (ODataResponseSingle) offlineStore.executeRequest(request);
                if (response.getPayloadType() == ODataPayload.Type.Error) {
                    ODataErrorDefaultImpl error = (ODataErrorDefaultImpl) response.getPayload();
                    throw new OfflineODataStoreException(error.getMessage());
                }else if (response.getPayloadType() == ODataPayload.Type.EntitySet) {
                    ODataEntitySet feed = (ODataEntitySet) response.getPayload();
                    List<ODataEntity> entities = feed.getEntities();
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
    private static ODataDuration getTimeAsODataDuration(String timeString) {

        List<String> timeDuration = null;
        if (timeString.contains("-")) {
            timeDuration = Arrays.asList(timeString.split("-"));
        }else if(timeString.contains(":")){
            timeDuration = Arrays.asList(timeString.split(":"));
        }
        int hour = Integer.parseInt(timeDuration.get(0));
        int minute = Integer.parseInt(timeDuration.get(1));
        int seconds = 00;
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

package com.arteriatech.ss.msec.iscan.v4.utils;

import androidx.annotation.NonNull;

import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.AnnotationUtils;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataGuid;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.ODataProperty;
import com.sap.smp.client.odata.metadata.ODataMetaProperty;
import com.sap.smp.client.odata.offline.ODataOfflineStore;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class OnlineUtils<T> {

    public List<T>returnEntityList(@NonNull ODataOfflineStore offlineStore,T t, String entityName, List<ODataEntity>entities) {
        List<T>arrayList = new ArrayList<>();
            List<String> keysList = AnnotationUtils.getAnnotationFields(t.getClass());
            ODataProperty property;
            ODataPropMap properties;
        try {
            for (ODataEntity entity : entities) {
                properties = entity.getProperties();
                try {
                    t = (T) t.getClass().newInstance();
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
                for (final String key:keysList) {
                    property = properties.get(key);
                    if (property!=null) {
                        try {
                            Field field = t.getClass().getDeclaredField(key);
                            field.setAccessible(true);
                            field.set(t, validateEdmType(offlineStore,entityName,key,property));
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
                arrayList.add(t);
            }
        } catch (Exception e) {
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
            ODataGuid cpguid = (ODataGuid) property.getValue();
            value = cpguid.guidAsString36();
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
    private String getDateTimeValue(ODataProperty property){
        String value ="";
        try {
            value = UtilConstants.convertCalenderToStringFormat((GregorianCalendar) property.getValue());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value;
    }
}

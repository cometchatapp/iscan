package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataETag;
import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class SPNumberRangeBean implements Serializable {
    @ODataProperty
    private String SpGuid = "";
    @ODataProperty
    private String SpNo = "";
    @ODataProperty
    private String SpName = "";
    @ODataProperty
    private String ObjectType = "";
    @ODataProperty
    private String ObjectTypeDs = "";
    @ODataProperty
    private String CurrentNumber = "";
    @ODataETag
    private String ETAG;

    public String getETAG() {
        return ETAG;
    }

    public void setETAG(String ETAG) {
        this.ETAG = ETAG;
    }

    public String getSpGuid() {
        return SpGuid;
    }

    public void setSpGuid(String spGuid) {
        SpGuid = spGuid;
    }

    public String getSpNo() {
        return SpNo;
    }

    public void setSpNo(String spNo) {
        SpNo = spNo;
    }

    public String getSpName() {
        return SpName;
    }

    public void setSpName(String spName) {
        SpName = spName;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public String getObjectTypeDs() {
        return ObjectTypeDs;
    }

    public void setObjectTypeDs(String objectTypeDs) {
        ObjectTypeDs = objectTypeDs;
    }

    public String getCurrentNumber() {
        return CurrentNumber;
    }

    public void setCurrentNumber(String currentNumber) {
        CurrentNumber = currentNumber;
    }
}

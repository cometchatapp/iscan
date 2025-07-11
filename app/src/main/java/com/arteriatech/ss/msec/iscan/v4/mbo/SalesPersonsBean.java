package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class SalesPersonsBean implements Serializable {
    @ODataProperty
    private String FirstName;
    @ODataProperty
    private String SPNo;
    @ODataProperty
    private String LastName;
    @ODataProperty
    private String StateDesc;

    public String getGroup4() {
        return Group4;
    }

    public void setGroup4(String group4) {
        Group4 = group4;
    }

    @ODataProperty
    private String Group4="";

    @ODataProperty
    private String GeofenceFlag="";
    @ODataProperty
    private String ZoneID="";

    public String getZoneID() {
        return ZoneID;
    }

    public void setZoneID(String zoneID) {
        ZoneID = zoneID;
    }

    @ODataProperty
    private String Url2;
    @ODataProperty
    private String Url1;

    public String getUrl3() {
        return Url3;
    }

    public void setUrl3(String url3) {
        Url3 = url3;
    }

    public String getGeofenceFlag() {
        return GeofenceFlag;
    }

    public void setGeofenceFlag(String geofenceFlag) {
        GeofenceFlag = geofenceFlag;
    }

    @ODataProperty
    private String Url3;
    @ODataProperty
    private String KYCStatus;

    public String getSPGUID() {
        return SPGUID;
    }

    public void setSPGUID(String SPGUID) {
        this.SPGUID = SPGUID;
    }

    @ODataProperty
    private String SPGUID;

    public String getKYCStatus() {
        return KYCStatus;
    }

    public void setKYCStatus(String KYCStatus) {
        this.KYCStatus = KYCStatus;
    }

    public String getKYCStatusDesc() {
        return KYCStatusDesc;
    }

    public void setKYCStatusDesc(String KYCStatusDesc) {
        this.KYCStatusDesc = KYCStatusDesc;
    }

    @ODataProperty
    private String KYCStatusDesc;

    public String getUrl2() {
        return Url2;
    }

    public void setUrl2(String url2) {
        Url2 = url2;
    }

    public String getUrl1() {
        return Url1;
    }

    public void setUrl1(String url1) {
        Url1 = url1;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getSPNo() {
        return SPNo;
    }

    public void setSPNo(String SPNo) {
        this.SPNo = SPNo;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getStateDesc() {
        return StateDesc;
    }

    public void setStateDesc(String stateDesc) {
        StateDesc = stateDesc;
    }
}

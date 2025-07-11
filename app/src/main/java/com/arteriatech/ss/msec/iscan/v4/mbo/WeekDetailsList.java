package com.arteriatech.ss.msec.iscan.v4.mbo;

import java.io.Serializable;

/**
 * Created by e10860 on 2/16/2018.
 */

public class WeekDetailsList implements Serializable {

    private String date = "";

    public String getDeviceNo() {
        return DeviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        DeviceNo = deviceNo;
    }

    private String DeviceNo = "";
    private String remarks = "";

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    private String totalAmount = "";

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    private String day = "";

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getsPGUID() {
        return sPGUID;
    }

    public void setsPGUID(String sPGUID) {
        this.sPGUID = sPGUID;
    }

    public String getsPNo() {
        return sPNo;
    }

    public void setsPNo(String sPNo) {
        this.sPNo = sPNo;
    }

    public String getsPName() {
        return sPName;
    }

    public void setsPName(String sPName) {
        this.sPName = sPName;
    }

    public String getcPGUID() {
        return cPGUID;
    }

    public void setcPGUID(String cPGUID) {
        this.cPGUID = cPGUID;
    }

    public String getcPNo() {
        return cPNo;
    }

    public void setcPNo(String cPNo) {
        this.cPNo = cPNo;
    }

    public String getcPName() {
        return cPName;
    }

    public void setcPName(String cPName) {
        this.cPName = cPName;
    }

    public String getcPType() {
        return cPType;
    }

    public void setcPType(String cPType) {
        this.cPType = cPType;
    }

    public String getcPTypeDesc() {
        return cPTypeDesc;
    }

    public void setcPTypeDesc(String cPTypeDesc) {
        this.cPTypeDesc = cPTypeDesc;
    }

    private String currentDate = "";


    private String collectionPlanItemGUID;

    private String collectionPlanGUID;

    private String itemNo;

    private String loginID;

    private String sPGUID;

    private String sPNo;

    private String sPName;

    private String cPGUID;

    private String cPNo;

    private String cPName;

    private String cPType;

    private String cPTypeDesc;

    private String collectionPlanDate;

    private String collectionType;

    private String collectionTypeDesc;

    private String instrumentNo;

    private String instrumentDate;

    private String plannedValue="0";

    private String achievedValue;

    private String currency;


    private String status;

    private String statusDesc;

    private String apprvlStatusID;

    private String apprvlStatusDesc;

    private String createdBy;

    private String createdOn;

    private String createdAt;

    private String changedBy;

    private String changedOn;

    private String changedAt;
    private String VisitDate ="";
    private String CollectionPlanItemGUID ="";

    public String getCollectionPlanItemGUID() {
        return collectionPlanItemGUID;
    }

    public void setCollectionPlanItemGUID(String collectionPlanItemGUID) {
        this.collectionPlanItemGUID = collectionPlanItemGUID;
    }

    public String getCollectionPlanGUID() {
        return collectionPlanGUID;
    }

    public void setCollectionPlanGUID(String collectionPlanGUID) {
        this.collectionPlanGUID = collectionPlanGUID;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getLoginID() {
        return loginID;
    }

    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }

    public String getSPGUID() {
        return sPGUID;
    }

    public void setSPGUID(String sPGUID) {
        this.sPGUID = sPGUID;
    }

    public String getSPNo() {
        return sPNo;
    }

    public void setSPNo(String sPNo) {
        this.sPNo = sPNo;
    }

    public String getSPName() {
        return sPName;
    }

    public void setSPName(String sPName) {
        this.sPName = sPName;
    }

    public String getCPGUID() {
        return cPGUID;
    }

    public void setCPGUID(String cPGUID) {
        this.cPGUID = cPGUID;
    }

    public String getCPNo() {
        return cPNo;
    }

    public void setCPNo(String cPNo) {
        this.cPNo = cPNo;
    }

    public String getCPName() {
        return cPName;
    }

    public void setCPName(String cPName) {
        this.cPName = cPName;
    }

    public String getCPType() {
        return cPType;
    }

    public void setCPType(String cPType) {
        this.cPType = cPType;
    }

    public String getCPTypeDesc() {
        return cPTypeDesc;
    }

    public void setCPTypeDesc(String cPTypeDesc) {
        this.cPTypeDesc = cPTypeDesc;
    }

    public String getCollectionPlanDate() {
        return collectionPlanDate;
    }

    public void setCollectionPlanDate(String collectionPlanDate) {
        this.collectionPlanDate = collectionPlanDate;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public String getCollectionTypeDesc() {
        return collectionTypeDesc;
    }

    public void setCollectionTypeDesc(String collectionTypeDesc) {
        this.collectionTypeDesc = collectionTypeDesc;
    }

    public String getInstrumentNo() {
        return instrumentNo;
    }

    public void setInstrumentNo(String instrumentNo) {
        this.instrumentNo = instrumentNo;
    }

    public String getInstrumentDate() {
        return instrumentDate;
    }

    public void setInstrumentDate(String instrumentDate) {
        this.instrumentDate = instrumentDate;
    }

    public String getPlannedValue() {
        return plannedValue;
    }

    public void setPlannedValue(String plannedValue) {
        this.plannedValue = plannedValue;
    }

    public String getAchievedValue() {
        return achievedValue;
    }

    public void setAchievedValue(String achievedValue) {
        this.achievedValue = achievedValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getApprvlStatusID() {
        return apprvlStatusID;
    }

    public void setApprvlStatusID(String apprvlStatusID) {
        this.apprvlStatusID = apprvlStatusID;
    }

    public String getApprvlStatusDesc() {
        return apprvlStatusDesc;
    }

    public void setApprvlStatusDesc(String apprvlStatusDesc) {
        this.apprvlStatusDesc = apprvlStatusDesc;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getChangedOn() {
        return changedOn;
    }

    public void setChangedOn(String changedOn) {
        this.changedOn = changedOn;
    }

    public String getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(String changedAt) {
        this.changedAt = changedAt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public String getVisitDate() {
        return VisitDate;
    }

    public void setVisitDate(String visitDate) {
        VisitDate = visitDate;
    }
}

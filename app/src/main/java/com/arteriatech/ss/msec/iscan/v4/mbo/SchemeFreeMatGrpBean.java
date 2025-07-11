package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class SchemeFreeMatGrpBean implements Serializable {

    @ODataProperty
    private String ChangedAt;
    @ODataProperty
    private String ChangedBy;
    @ODataProperty
    private String ChangedOn;
    @ODataProperty
    private String CreatedAt;
    @ODataProperty
    private String CreatedBy;
    @ODataProperty
    private String CreatedOn;
    @ODataProperty
    private String LoginID;
    @ODataProperty
    private String MaterialGrp;
    @ODataProperty
    private String MaterialGrpDesc;
    @ODataProperty
    private String SchFreeMatGrpGUID;
    @ODataProperty
    private String Status;
    @ODataProperty
    private String StatusDesc;
    @ODataProperty
    private String TestRun;
    @ODataProperty
    private String ValidFrom;
    @ODataProperty
    private String ValidTo;

    public String getChangedAt() {
        return ChangedAt;
    }

    public void setChangedAt(String changedAt) {
        ChangedAt = changedAt;
    }

    public String getChangedBy() {
        return ChangedBy;
    }

    public void setChangedBy(String changedBy) {
        ChangedBy = changedBy;
    }

    public String getChangedOn() {
        return ChangedOn;
    }

    public void setChangedOn(String changedOn) {
        ChangedOn = changedOn;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getMaterialGrp() {
        return MaterialGrp;
    }

    public void setMaterialGrp(String materialGrp) {
        MaterialGrp = materialGrp;
    }

    public String getMaterialGrpDesc() {
        return MaterialGrpDesc;
    }

    public void setMaterialGrpDesc(String materialGrpDesc) {
        MaterialGrpDesc = materialGrpDesc;
    }

    public String getSchFreeMatGrpGUID() {
        return SchFreeMatGrpGUID;
    }

    public void setSchFreeMatGrpGUID(String schFreeMatGrpGUID) {
        SchFreeMatGrpGUID = schFreeMatGrpGUID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatusDesc() {
        return StatusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        StatusDesc = statusDesc;
    }

    public String getTestRun() {
        return TestRun;
    }

    public void setTestRun(String testRun) {
        TestRun = testRun;
    }

    public String getValidFrom() {
        return ValidFrom;
    }

    public void setValidFrom(String validFrom) {
        ValidFrom = validFrom;
    }

    public String getValidTo() {
        return ValidTo;
    }

    public void setValidTo(String validTo) {
        ValidTo = validTo;
    }
}

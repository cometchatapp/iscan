package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class KPISetBean implements Serializable {
    @ODataProperty
    private String AuthGrp;
    @ODataProperty
    private String AuthGrpDesc;
    @ODataProperty
    private String CalculationBase;
    @ODataProperty
    private String CalculationBaseDesc;
    @ODataProperty
    private String CalculationSource;
    @ODataProperty
    private String CalculationSourceDesc;
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
    private String KPICategory;
    @ODataProperty
    private String KPICategoryDesc;
    @ODataProperty
    private String KPICode;
    @ODataProperty
    private String KPIFor;
    @ODataProperty
    private String KPIForDesc;
    @ODataProperty
    private String KPIGUID;
    @ODataProperty
    private String LoginID;
    @ODataProperty
    private String Month;
    @ODataProperty
    private String Name;
    @ODataProperty
    private String Periodicity;
    @ODataProperty
    private String PeriodicityDesc;
    @ODataProperty
    private String StatusDesc;
    @ODataProperty
    private String StatusID;
    @ODataProperty
    private String TestRun;
    @ODataProperty
    private String ValidFrom;
    @ODataProperty
    private String ValidTo;
    @ODataProperty
    private String Year;

    public String getAuthGrp() {
        return AuthGrp;
    }

    public void setAuthGrp(String authGrp) {
        AuthGrp = authGrp;
    }

    public String getAuthGrpDesc() {
        return AuthGrpDesc;
    }

    public void setAuthGrpDesc(String authGrpDesc) {
        AuthGrpDesc = authGrpDesc;
    }

    public String getCalculationBase() {
        return CalculationBase;
    }

    public void setCalculationBase(String calculationBase) {
        CalculationBase = calculationBase;
    }

    public String getCalculationBaseDesc() {
        return CalculationBaseDesc;
    }

    public void setCalculationBaseDesc(String calculationBaseDesc) {
        CalculationBaseDesc = calculationBaseDesc;
    }

    public String getCalculationSource() {
        return CalculationSource;
    }

    public void setCalculationSource(String calculationSource) {
        CalculationSource = calculationSource;
    }

    public String getCalculationSourceDesc() {
        return CalculationSourceDesc;
    }

    public void setCalculationSourceDesc(String calculationSourceDesc) {
        CalculationSourceDesc = calculationSourceDesc;
    }

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

    public String getKPICategory() {
        return KPICategory;
    }

    public void setKPICategory(String KPICategory) {
        this.KPICategory = KPICategory;
    }

    public String getKPICategoryDesc() {
        return KPICategoryDesc;
    }

    public void setKPICategoryDesc(String KPICategoryDesc) {
        this.KPICategoryDesc = KPICategoryDesc;
    }

    public String getKPICode() {
        return KPICode;
    }

    public void setKPICode(String KPICode) {
        this.KPICode = KPICode;
    }

    public String getKPIFor() {
        return KPIFor;
    }

    public void setKPIFor(String KPIFor) {
        this.KPIFor = KPIFor;
    }

    public String getKPIForDesc() {
        return KPIForDesc;
    }

    public void setKPIForDesc(String KPIForDesc) {
        this.KPIForDesc = KPIForDesc;
    }

    public String getKPIGUID() {
        return KPIGUID;
    }

    public void setKPIGUID(String KPIGUID) {
        this.KPIGUID = KPIGUID;
    }

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPeriodicity() {
        return Periodicity;
    }

    public void setPeriodicity(String periodicity) {
        Periodicity = periodicity;
    }

    public String getPeriodicityDesc() {
        return PeriodicityDesc;
    }

    public void setPeriodicityDesc(String periodicityDesc) {
        PeriodicityDesc = periodicityDesc;
    }

    public String getStatusDesc() {
        return StatusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        StatusDesc = statusDesc;
    }

    public String getStatusID() {
        return StatusID;
    }

    public void setStatusID(String statusID) {
        StatusID = statusID;
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

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }
}

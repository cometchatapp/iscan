package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class TargetsBean implements Serializable {
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
    private String KPICode;
    @ODataProperty
    private String KPIGUID;
    @ODataProperty
    private String KPIName;
    @ODataProperty
    private String LoginID;
    @ODataProperty
    private String Name;
    @ODataProperty
    private String Period;
    @ODataProperty
    private String Periodicity;
    @ODataProperty
    private String PeriodicityDesc;
    @ODataProperty
    private String TargetGUID;
    @ODataProperty
    private String TestRun;
    @ODataProperty
    private String Year;

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

    public String getKPICode() {
        return KPICode;
    }

    public void setKPICode(String KPICode) {
        this.KPICode = KPICode;
    }

    public String getKPIGUID() {
        return KPIGUID;
    }

    public void setKPIGUID(String KPIGUID) {
        this.KPIGUID = KPIGUID;
    }

    public String getKPIName() {
        return KPIName;
    }

    public void setKPIName(String KPIName) {
        this.KPIName = KPIName;
    }

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
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

    public String getTargetGUID() {
        return TargetGUID;
    }

    public void setTargetGUID(String targetGUID) {
        TargetGUID = targetGUID;
    }

    public String getTestRun() {
        return TestRun;
    }

    public void setTestRun(String testRun) {
        TestRun = testRun;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }
}

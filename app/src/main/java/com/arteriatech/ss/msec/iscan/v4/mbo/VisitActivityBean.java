package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataETag;
import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class VisitActivityBean implements Serializable {
    @ODataETag
    private String ETAG;
    @ODataProperty
    private String ActivityRefID;
    @ODataProperty
    private String ActivityType;
    @ODataProperty
    private String ActivityTypeDesc;
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
    private String EndTime;
    @ODataProperty
    private String Latitude;
    @ODataProperty
    private String LoginID;
    @ODataProperty
    private String Longitude;
    @ODataProperty
    private String Source;
    @ODataProperty
    private String StartTime;
    @ODataProperty
    private String TestRun;
    @ODataProperty
    private String VisitActivityGUID;
    @ODataProperty
    private String VisitGUID;

    public String getETAG() {
        return ETAG;
    }

    public void setETAG(String ETAG) {
        this.ETAG = ETAG;
    }

    public String getActivityRefID() {
        return ActivityRefID;
    }

    public void setActivityRefID(String activityRefID) {
        ActivityRefID = activityRefID;
    }

    public String getActivityType() {
        return ActivityType;
    }

    public void setActivityType(String activityType) {
        ActivityType = activityType;
    }

    public String getActivityTypeDesc() {
        return ActivityTypeDesc;
    }

    public void setActivityTypeDesc(String activityTypeDesc) {
        ActivityTypeDesc = activityTypeDesc;
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

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getTestRun() {
        return TestRun;
    }

    public void setTestRun(String testRun) {
        TestRun = testRun;
    }

    public String getVisitActivityGUID() {
        return VisitActivityGUID;
    }

    public void setVisitActivityGUID(String visitActivityGUID) {
        VisitActivityGUID = visitActivityGUID;
    }

    public String getVisitGUID() {
        return VisitGUID;
    }

    public void setVisitGUID(String visitGUID) {
        VisitGUID = visitGUID;
    }
}

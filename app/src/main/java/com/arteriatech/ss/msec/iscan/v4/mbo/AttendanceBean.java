package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class AttendanceBean implements Serializable {

    @ODataProperty
    private String AttendanceGUID="";
    @ODataProperty
    private String StartDate="";
    @ODataProperty
    private String SPGUID="";

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    @ODataProperty
    private String StartTime="";

    public String getAttendanceGUID() {
        return AttendanceGUID;
    }

    public void setAttendanceGUID(String attendanceGUID) {
        AttendanceGUID = attendanceGUID;
    }
}

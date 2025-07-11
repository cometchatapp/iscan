package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class RouteScheduleDetailBean implements Serializable {
    @ODataProperty
    private String DayOfMonth;
    @ODataProperty
    private String DayOfWeek;
    @ODataProperty
    private String LoginID;
    @ODataProperty
    private String ProdDetDesc;
    @ODataProperty
    private String ProdDetGUID;
    @ODataProperty
    private String ProdDetID;
    @ODataProperty
    private String RouteSchDetailGUID;
    @ODataProperty
    private String RouteSchGUID;
    @ODataProperty
    private String RouteSchSPGUID;
    @ODataProperty
    private String Source;
    @ODataProperty
    private String WeekOfMonth;


    public String getDayOfMonth() {
        return DayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        DayOfMonth = dayOfMonth;
    }

    public String getDayOfWeek() {
        return DayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        DayOfWeek = dayOfWeek;
    }

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getProdDetDesc() {
        return ProdDetDesc;
    }

    public void setProdDetDesc(String prodDetDesc) {
        ProdDetDesc = prodDetDesc;
    }

    public String getProdDetGUID() {
        return ProdDetGUID;
    }

    public void setProdDetGUID(String prodDetGUID) {
        ProdDetGUID = prodDetGUID;
    }

    public String getProdDetID() {
        return ProdDetID;
    }

    public void setProdDetID(String prodDetID) {
        ProdDetID = prodDetID;
    }

    public String getRouteSchDetailGUID() {
        return RouteSchDetailGUID;
    }

    public void setRouteSchDetailGUID(String routeSchDetailGUID) {
        RouteSchDetailGUID = routeSchDetailGUID;
    }

    public String getRouteSchGUID() {
        return RouteSchGUID;
    }

    public void setRouteSchGUID(String routeSchGUID) {
        RouteSchGUID = routeSchGUID;
    }

    public String getRouteSchSPGUID() {
        return RouteSchSPGUID;
    }

    public void setRouteSchSPGUID(String routeSchSPGUID) {
        RouteSchSPGUID = routeSchSPGUID;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getWeekOfMonth() {
        return WeekOfMonth;
    }

    public void setWeekOfMonth(String weekOfMonth) {
        WeekOfMonth = weekOfMonth;
    }
}

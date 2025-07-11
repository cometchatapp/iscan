package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class RouteScheduleSPBean implements Serializable {
    @ODataProperty
    private String RouteSchSPGUID;
    @ODataProperty
    private String OrderSeqID;

    public String getParentNo() {
        return ParentNo;
    }

    public void setParentNo(String parentNo) {
        ParentNo = parentNo;
    }

    @ODataProperty
    private String RouteSchGUID;

    @ODataProperty
    private String ParentNo;

    public String getParentName() {
        return ParentName;
    }

    public void setParentName(String parentName) {
        ParentName = parentName;
    }

    @ODataProperty
    private String ParentName;

    public String getOrderSeqID() {
        return OrderSeqID;
    }

    public void setOrderSeqID(String orderSeqID) {
        OrderSeqID = orderSeqID;
    }

    public String getRouteSchSPGUID() {
        return RouteSchSPGUID;
    }

    public void setRouteSchSPGUID(String routeSchSPGUID) {
        RouteSchSPGUID = routeSchSPGUID;
    }

    public String getRouteSchGUID() {
        return RouteSchGUID;
    }

    public void setRouteSchGUID(String routeSchGUID) {
        RouteSchGUID = routeSchGUID;
    }
}

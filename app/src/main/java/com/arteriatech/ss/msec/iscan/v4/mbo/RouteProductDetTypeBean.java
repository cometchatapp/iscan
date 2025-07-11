package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class RouteProductDetTypeBean implements Serializable {

    @ODataProperty
    private String LoginID;
    @ODataProperty
    private String ParentName;
    @ODataProperty
    private String ParentNo;
    @ODataProperty
    private String ParentType;
    @ODataProperty
    private String ParntTypDesc;
    @ODataProperty
    private String PrdCatgr;
    @ODataProperty
    private String PrdCatgrDesc;
    @ODataProperty
    private String PrdDet1GUID;
    @ODataProperty
    private String PrdDetGUID;
    @ODataProperty
    private String PrdDetType;
    @ODataProperty
    private String PrdDetTypeDesc;
    @ODataProperty
    private String ProductDesc;
    @ODataProperty
    private String ProductID;

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getParentName() {
        return ParentName;
    }

    public void setParentName(String parentName) {
        ParentName = parentName;
    }

    public String getParentNo() {
        return ParentNo;
    }

    public void setParentNo(String parentNo) {
        ParentNo = parentNo;
    }

    public String getParentType() {
        return ParentType;
    }

    public void setParentType(String parentType) {
        ParentType = parentType;
    }

    public String getParntTypDesc() {
        return ParntTypDesc;
    }

    public void setParntTypDesc(String parntTypDesc) {
        ParntTypDesc = parntTypDesc;
    }

    public String getPrdCatgr() {
        return PrdCatgr;
    }

    public void setPrdCatgr(String prdCatgr) {
        PrdCatgr = prdCatgr;
    }

    public String getPrdCatgrDesc() {
        return PrdCatgrDesc;
    }

    public void setPrdCatgrDesc(String prdCatgrDesc) {
        PrdCatgrDesc = prdCatgrDesc;
    }

    public String getPrdDet1GUID() {
        return PrdDet1GUID;
    }

    public void setPrdDet1GUID(String prdDet1GUID) {
        PrdDet1GUID = prdDet1GUID;
    }

    public String getPrdDetGUID() {
        return PrdDetGUID;
    }

    public void setPrdDetGUID(String prdDetGUID) {
        PrdDetGUID = prdDetGUID;
    }

    public String getPrdDetType() {
        return PrdDetType;
    }

    public void setPrdDetType(String prdDetType) {
        PrdDetType = prdDetType;
    }

    public String getPrdDetTypeDesc() {
        return PrdDetTypeDesc;
    }

    public void setPrdDetTypeDesc(String prdDetTypeDesc) {
        PrdDetTypeDesc = prdDetTypeDesc;
    }

    public String getProductDesc() {
        return ProductDesc;
    }

    public void setProductDesc(String productDesc) {
        ProductDesc = productDesc;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    @Override
    public String toString() {
        return "RouteProductDetTypeBean{" +
                "PrdCatgr='" + PrdCatgr + '\'' +
                ", PrdDet1GUID='" + PrdDet1GUID + '\'' +
                ", PrdDetType='" + PrdDetType + '\'' +
                ", PrdDetTypeDesc='" + PrdDetTypeDesc + '\'' +
                '}';
    }
}

package com.arteriatech.ss.msec.iscan.v4.mutils.registration;

import java.util.ArrayList;

public class MaterialBean {
    String MaterialDesc="";
    String MaterialNo="";
    String MRP="";
    String LandingPrice="";
    String StkConvQty1="";
    String StkConvQty2="";
    String StkConvUom1="";
    String StkConvUom2="";
    String MerdiseGuid="";
    String qty="";
    String CatalogInfo="";
    String uom="";
    String clickable="";
    ArrayList<String> uomList=new ArrayList<>();

    public String getClickable() {
        return clickable;
    }

    public void setClickable(String clickable) {
        this.clickable = clickable;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getCatalogInfo() {
        return CatalogInfo;
    }

    public void setCatalogInfo(String catalogInfo) {
        CatalogInfo = catalogInfo;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setUomList(ArrayList<String> uomList) {
        this.uomList = uomList;
    }

    public ArrayList<String> getUomList() {
        return uomList;
    }

    public String getMaterialDesc() {
        return MaterialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        MaterialDesc = materialDesc;
    }

    public String getMaterialNo() {
        return MaterialNo;
    }

    public void setMaterialNo(String materialNo) {
        MaterialNo = materialNo;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getLandingPrice() {
        return LandingPrice;
    }

    public void setLandingPrice(String landingPrice) {
        LandingPrice = landingPrice;
    }

    public String getStkConvQty1() {
        return StkConvQty1;
    }

    public void setStkConvQty1(String stkConvQty1) {
        StkConvQty1 = stkConvQty1;
    }

    public String getStkConvQty2() {
        return StkConvQty2;
    }

    public void setStkConvQty2(String stkConvQty2) {
        StkConvQty2 = stkConvQty2;
    }

    public String getStkConvUom1() {
        return StkConvUom1;
    }

    public void setStkConvUom1(String stkConvUom1) {
        StkConvUom1 = stkConvUom1;
    }

    public String getStkConvUom2() {
        return StkConvUom2;
    }

    public void setStkConvUom2(String stkConvUom2) {
        StkConvUom2 = stkConvUom2;
    }

    public String getMerdiseGuid() {
        return MerdiseGuid;
    }

    public void setMerdiseGuid(String merdiseGuid) {
        MerdiseGuid = merdiseGuid;
    }


}

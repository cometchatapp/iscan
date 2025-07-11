package com.arteriatech.ss.msec.iscan.v4.mbo;

import java.io.Serializable;

/**
 * Created by e10526 on 23-04-2018.
 */

public class SOCreateBean implements Serializable {

    private String PriceBatchCalType = "";
    private String CPNo = "";
    private String CPUID = "";
    private String CPName = "";
    private String CPGUID = "";
    private String CPGUID36 = "";
    private String SPGUID = "";
    private String SPNo = "";
    private String ParentID = "";
    private String ParentTypeID = "";
    private String CPTypeID = "";
    private String DMSDivision = "";
    private String ParentName = "";
    private String CPGUID32 = "";
    private String BeatName = "";

    public String getBeatName() {
        return BeatName;
    }

    public void setBeatName(String beatName) {
        BeatName = beatName;
    }

    public String getTotalNetAmount() {
        return totalNetAmount;
    }

    public void setTotalNetAmount(String totalNetAmount) {
        this.totalNetAmount = totalNetAmount;
    }

    private String totalNetAmount = "";

    private DmsDivQryBean dmsDivQryBean = new DmsDivQryBean();

    public String getSPName() {
        return SPName;
    }

    public void setSPName(String SPName) {
        this.SPName = SPName;
    }

    private String SPName = "";
    public String getParentName() {
        return ParentName;
    }

    public void setParentName(String parentName) {
        ParentName = parentName;
    }



    public String getDMSDivision() {
        return DMSDivision;
    }

    public void setDMSDivision(String DMSDivision) {
        this.DMSDivision = DMSDivision;
    }

    public String getDMSDivisionDesc() {
        return DMSDivisionDesc;
    }

    public void setDMSDivisionDesc(String DMSDivisionDesc) {
        this.DMSDivisionDesc = DMSDivisionDesc;
    }

    private String DMSDivisionDesc = "";

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    private String Currency = "";
    public String getSPGUID() {
        return SPGUID;
    }

    public void setSPGUID(String SPGUID) {
        this.SPGUID = SPGUID;
    }

    public String getSPNo() {
        return SPNo;
    }

    public void setSPNo(String SPNo) {
        this.SPNo = SPNo;
    }
    public String getParentID() {
        return ParentID;
    }

    public void setParentID(String parentID) {
        ParentID = parentID;
    }

    public String getParentTypeID() {
        return ParentTypeID;
    }

    public void setParentTypeID(String parentTypeID) {
        ParentTypeID = parentTypeID;
    }

    public String getCPTypeID() {
        return CPTypeID;
    }

    public void setCPTypeID(String CPTypeID) {
        this.CPTypeID = CPTypeID;
    }
    public String getCPGUID() {
        return CPGUID;
    }

    public void setCPGUID(String CPGUID) {
        this.CPGUID = CPGUID;
    }

    public String getCPNo() {
        return CPNo;
    }

    public void setCPNo(String CPNo) {
        this.CPNo = CPNo;
    }
    public String getCPGUID36() {
        return CPGUID36;
    }

    public void setCPGUID36(String CPGUID36) {
        this.CPGUID36 = CPGUID36;
    }

    public String getCPName() {
        return CPName;
    }

    public void setCPName(String CPName) {
        this.CPName = CPName;
    }


    public String getCPUID() {
        return CPUID;
    }

    public void setCPUID(String CPUID) {
        this.CPUID = CPUID;
    }

    public DmsDivQryBean getDmsDivQryBean() {
        return dmsDivQryBean;
    }

    public void setDmsDivQryBean(DmsDivQryBean dmsDivQryBean) {
        this.dmsDivQryBean = dmsDivQryBean;
    }

    public String getPriceBatchCalType() {
        return PriceBatchCalType;
    }

    public void setPriceBatchCalType(String priceBatchCalType) {
        PriceBatchCalType = priceBatchCalType;
    }

    public String getCPGUID32() {
        return CPGUID32;
    }

    public void setCPGUID32(String CPGUID32) {
        this.CPGUID32 = CPGUID32;
    }
}

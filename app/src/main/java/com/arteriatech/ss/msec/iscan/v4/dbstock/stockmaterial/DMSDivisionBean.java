package com.arteriatech.ss.msec.iscan.v4.dbstock.stockmaterial;

import java.io.Serializable;
import java.util.ArrayList;

public class DMSDivisionBean implements Serializable {
    private String distributorId = "";
    private String distributorGuid = "";
    private String distributorName = "";
    private String DMSDivisionQuery = "";
    private String stockOwner = "";
    private String CPUID = "";
    private String CPTypeDesc = "";

    public String getCPUID() {
        return CPUID;
    }

    public void setCPUID(String CPUID) {
        this.CPUID = CPUID;
    }

    public String getCPTypeDesc() {
        return CPTypeDesc;
    }

    public void setCPTypeDesc(String CPTypeDesc) {
        this.CPTypeDesc = CPTypeDesc;
    }

    private ArrayList<String> dmsDIVList = new ArrayList<>();

    public ArrayList<String> getDmsDIVList() {
        return dmsDIVList;
    }

    public void setDmsDIVList(ArrayList<String> dmsDIVList) {
        this.dmsDIVList = dmsDIVList;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getDistributorGuid() {
        return distributorGuid;
    }

    public void setDistributorGuid(String distributorGuid) {
        this.distributorGuid = distributorGuid;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getDMSDivisionQuery() {
        return DMSDivisionQuery;
    }

    public void setDMSDivisionQuery(String DMSDivisionQuery) {
        this.DMSDivisionQuery = DMSDivisionQuery;
    }

    public String getStockOwner() {
        return stockOwner;
    }

    public void setStockOwner(String stockOwner) {
        this.stockOwner = stockOwner;
    }

    @Override
    public String toString() {
        return distributorName;
    }
}

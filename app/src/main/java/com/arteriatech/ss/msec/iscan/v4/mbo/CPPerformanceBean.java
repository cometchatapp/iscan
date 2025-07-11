package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataETag;
import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CPPerformanceBean implements Serializable {
    @ODataProperty
    private String ParentNo;
    @ODataProperty
    private String SPGUID;
    @ODataProperty
    private String String1;
    @ODataProperty
    private String ParentName;
    @ODataProperty
    private String CPGUID;
    @ODataProperty
    private String CPNo;
    @ODataProperty
    private String CPName;
    @ODataProperty
    private String CPPerfmGUID;
    @ODataETag
    private String ETAG;


    //---------------------non meta fields-----------------------------
    private double target;
    private double achvmnt;
    private double earning;
    private String paramName;
    private String cleared;
    private double mised;
    private String HighUPLO;
    private String rpdGUID;
    private String rpdName;
    private double SaleValue;
    private boolean GO;
    private boolean taken;
    private List rpdlist;

    public String getCPPerfmGUID() {
        return CPPerfmGUID;
    }

    public void setCPPerfmGUID(String CPPerfmGUID) {
        this.CPPerfmGUID = CPPerfmGUID;
    }

    public String getETAG() {
        return ETAG;
    }

    public void setETAG(String ETAG) {
        this.ETAG = ETAG;
    }

    public List getRpdlist() {
        return rpdlist;
    }

    public void setRpdlist(List rpdlist) {
        this.rpdlist = rpdlist;
    }

    public String getRpdGUID() {
        return rpdGUID;
    }

    public void setRpdGUID(String rpdGUID) {
        this.rpdGUID = rpdGUID;
    }

    public String getRpdName() {
        return rpdName;
    }

    public void setRpdName(String rpdName) {
        this.rpdName = rpdName;
    }

    ArrayList<CPPerformanceBean> arrayList = new ArrayList<>();

    public ArrayList<CPPerformanceBean> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<CPPerformanceBean> arrayList) {
        this.arrayList = arrayList;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public String getHighUPLO() {
        return HighUPLO;
    }

    public void setHighUPLO(String highUPLO) {
        HighUPLO = highUPLO;
    }

    public double getSaleValue() {
        return SaleValue;
    }

    public void setSaleValue(double saleValue) {
        SaleValue = saleValue;
    }

    public boolean isGO() {
        return GO;
    }

    public void setGO(boolean GO) {
        this.GO = GO;
    }

    public double getMised() {
        return mised;
    }

    public void setMised(double mised) {
        this.mised = mised;
    }

    public String getCleared() {
        return cleared;
    }

    public void setCleared(String cleared) {
        this.cleared = cleared;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double getAchvmnt() {
        return achvmnt;
    }

    public void setAchvmnt(double achvmnt) {
        this.achvmnt = achvmnt;
    }

    public double getEarning() {
        return earning;
    }

    public void setEarning(double earning) {
        this.earning = earning;
    }

    public String getParentNo() {
        return ParentNo;
    }

    public void setParentNo(String parentNo) {
        ParentNo = parentNo;
    }

    public String getSPGUID() {
        return SPGUID;
    }

    public void setSPGUID(String SPGUID) {
        this.SPGUID = SPGUID;
    }

    public String getString1() {
        return String1;
    }

    public void setString1(String string1) {
        String1 = string1;
    }

    public String getParentName() {
        return ParentName;
    }

    public void setParentName(String parentName) {
        ParentName = parentName;
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

    public String getCPName() {
        return CPName;
    }

    public void setCPName(String CPName) {
        this.CPName = CPName;
    }
}

package com.arteriatech.ss.msec.iscan.v4.outlet.outletdetails;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class CPSummary1SetBean implements Serializable {
    @ODataProperty
    private String CPType;
    @ODataProperty
    private String CPGuid;
    @ODataProperty
    private String CPNo;
    @ODataProperty
    private String Summary;


    public String getCPType() {
        return CPType;
    }

    public void setCPType(String CPType) {
        this.CPType = CPType;
    }

    public String getCPGuid() {
        return CPGuid;
    }

    public void setCPGuid(String CPGuid) {
        this.CPGuid = CPGuid;
    }

    public String getCPNo() {
        return CPNo;
    }

    public void setCPNo(String CPNo) {
        this.CPNo = CPNo;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }
}

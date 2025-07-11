package com.arteriatech.ss.msec.iscan.v4.mbo;

/**
 * Created by e10762 on 23-12-2016.
 *
 */

public class RoutePlanBean {

    public String getRschGuid() {
        return RschGuid;
    }

    public void setRschGuid(String rschGuid) {
        RschGuid = rschGuid;
    }

    public String getDOM() {
        return DOM;
    }

    public void setDOM(String DOM) {
        this.DOM = DOM;
    }

    public String getDOW() {
        return DOW;
    }

    public void setDOW(String DOW) {
        this.DOW = DOW;
    }

    private String RschGuid = "";
    private String RoutId = "";
    private String Description = "";

    public String getRoutId() {
        return RoutId;
    }

    public void setRoutId(String routId) {
        RoutId = routId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDisplayData() {
        return DisplayData;
    }

    public void setDisplayData(String displayData) {
        DisplayData = displayData;
    }

    private String DisplayData = "";
    private String DOM = "";
    private String DOW = "";
    public String getCPGUID() {
        return CPGUID;
    }

    public void setCPGUID(String CPGUID) {
        this.CPGUID = CPGUID;
    }

    private String CPGUID = "";

    public String getDistName() {
        return DistName;
    }

    public void setDistName(String distName) {
        DistName = distName;
    }

    private String DistName = "";
    @Override
    public String toString() {
        return DisplayData.toString();
    }

}

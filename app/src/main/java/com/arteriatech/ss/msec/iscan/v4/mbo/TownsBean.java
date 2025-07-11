package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class TownsBean implements Serializable {
    @ODataProperty
    private String TownGuid;
    @ODataProperty
    private String Dstrctid;
    @ODataProperty
    private String SubDistGuid;
    @ODataProperty
    private String TownCode;
    @ODataProperty
    private String CnsTownCode;
    @ODataProperty
    private String TownLevel;
    @ODataProperty
    private String TownPopulation;
    @ODataProperty
    private String TownName;
    @ODataProperty
    private String LoginID;
    @ODataProperty
    private String State;

    public String getTownGuid() {
        return TownGuid;
    }

    public void setTownGuid(String townGuid) {
        TownGuid = townGuid;
    }

    public String getDstrctid() {
        return Dstrctid;
    }

    public void setDstrctid(String dstrctid) {
        Dstrctid = dstrctid;
    }

    public String getSubDistGuid() {
        return SubDistGuid;
    }

    public void setSubDistGuid(String subDistGuid) {
        SubDistGuid = subDistGuid;
    }

    public String getTownCode() {
        return TownCode;
    }

    public void setTownCode(String townCode) {
        TownCode = townCode;
    }

    public String getCnsTownCode() {
        return CnsTownCode;
    }

    public void setCnsTownCode(String cnsTownCode) {
        CnsTownCode = cnsTownCode;
    }

    public String getTownLevel() {
        return TownLevel;
    }

    public void setTownLevel(String townLevel) {
        TownLevel = townLevel;
    }

    public String getTownPopulation() {
        return TownPopulation;
    }

    public void setTownPopulation(String townPopulation) {
        TownPopulation = townPopulation;
    }

    public String getTownName() {
        return TownName;
    }

    public void setTownName(String townName) {
        TownName = townName;
    }

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    @Override
    public String toString() {
        return TownName;
    }
}

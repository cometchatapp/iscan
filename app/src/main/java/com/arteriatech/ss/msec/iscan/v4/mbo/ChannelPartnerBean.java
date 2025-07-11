package com.arteriatech.ss.msec.iscan.v4.mbo;

import androidx.annotation.NonNull;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;


public class ChannelPartnerBean {
    @ODataProperty
    private String CPNo;
    @ODataProperty
    private String Name;
    @ODataProperty
    private String CPGUID;
    @ODataProperty
    private String CPUID;

    @ODataProperty
    private String Latitude;
    @ODataProperty
    private String Longitude;

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getCPNo() {
        return CPNo;
    }

    public void setCPNo(String CPNo) {
        this.CPNo = CPNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCPGUID() {
        return CPGUID;
    }

    public void setCPGUID(String CPGUID) {
        this.CPGUID = CPGUID;
    }

    public String getCPUID() {
        return CPUID;
    }

    public void setCPUID(String CPUID) {
        this.CPUID = CPUID;
    }


    @NonNull
    @Override
    public String toString() {
        return Name+" - "+CPUID;
    }
}

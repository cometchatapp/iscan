package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class UserChannelPartnerBean implements Serializable {
    @ODataProperty
    private String GSTIN;
    @ODataProperty
    private String FSSAI;

    public String getGSTIN() {
        return GSTIN;
    }

    public void setGSTIN(String GSTIN) {
        this.GSTIN = GSTIN;
    }

    public String getFSSAI() {
        return FSSAI;
    }

    public void setFSSAI(String FSSAI) {
        this.FSSAI = FSSAI;
    }
}


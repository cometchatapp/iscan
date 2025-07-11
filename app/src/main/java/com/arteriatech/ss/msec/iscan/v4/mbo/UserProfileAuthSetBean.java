package com.arteriatech.ss.msec.iscan.v4.mbo;


import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

public class UserProfileAuthSetBean {
    @ODataProperty
    private String AuthOrgValue;

    public String getAuthOrgTypeID() {
        return AuthOrgTypeID;
    }

    public void setAuthOrgTypeID(String authOrgTypeID) {
        AuthOrgTypeID = authOrgTypeID;
    }

    @ODataProperty
    private String AuthOrgTypeID;
    @ODataProperty
    private String AuthOrgValueDesc;


    public String getAuthOrgValue() {
        return AuthOrgValue;
    }

    public void setAuthOrgValue(String authOrgValue) {
        AuthOrgValue = authOrgValue;
    }

    public String getAuthOrgValueDesc() {
        return AuthOrgValueDesc;
    }

    public void setAuthOrgValueDesc(String authOrgValueDesc) {
        AuthOrgValueDesc = authOrgValueDesc;
    }
    @Override
    public String toString() {
        String desc = getAuthOrgValueDesc()+" - "+getAuthOrgValue();
        return desc;
    }
}

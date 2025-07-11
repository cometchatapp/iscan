package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class ConfigTypsetTypeValuesBean implements Serializable {
    @ODataProperty
    private String Typeset;
    @ODataProperty
    private String Types;
    @ODataProperty
    private String TypeValue;
    @ODataProperty
    private String Typesname;
    @ODataProperty
    private String LoginId;
    @ODataProperty
    private String TypesName;


    public String getTypesName() {
        return TypesName;
    }

    public void setTypesName(String typesName) {
        TypesName = typesName;
    }

    public String getTypeset() {
        return Typeset;
    }

    public void setTypeset(String typeset) {
        Typeset = typeset;
    }

    public String getTypes() {
        return Types;
    }

    public void setTypes(String types) {
        Types = types;
    }

    public String getTypeValue() {
        return TypeValue;
    }

    public void setTypeValue(String typeValue) {
        TypeValue = typeValue;
    }

    public String getTypesname() {
        return Typesname;
    }

    public void setTypesname(String typesname) {
        Typesname = typesname;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    @Override
    public String toString() {
        return TypesName==null?Typesname:TypesName;
    }
}

package com.arteriatech.ss.msec.iscan.v4.mbo;

/**
 * Created by e10769 on 29-06-2017.
 */

public class ConfigTypesetTypesBean {
    private String Types="";
    private String TypesName="";

    public String getParentID() {
        return ParentID;
    }

    public void setParentID(String parentID) {
        ParentID = parentID;
    }

    private String ParentID="";

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    private String typeName="";

    public String getTypes() {
        return Types;
    }

    public void setTypes(String types) {
        Types = types;
    }

    public String getTypesName() {
        return TypesName;
    }

    public void setTypesName(String typesName) {
        TypesName = typesName;
    }

    @Override
    public String toString() {
        return TypesName.toString();
    }
}

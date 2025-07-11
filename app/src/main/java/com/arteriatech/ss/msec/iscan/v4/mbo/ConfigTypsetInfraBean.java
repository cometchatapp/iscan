package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class ConfigTypsetInfraBean implements Serializable {
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

    private String OwnerBY="";

    private boolean isCheckBoxChecked;
    private int selectedPosition;

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public boolean isCheckBoxChecked() {
        return isCheckBoxChecked;
    }

    public void setCheckBoxChecked(boolean checkBoxChecked) {
        isCheckBoxChecked = checkBoxChecked;
    }

    public String getOwnerBYDesc() {
        return OwnerBYDesc;
    }

    public void setOwnerBYDesc(String ownerBYDesc) {
        OwnerBYDesc = ownerBYDesc;
    }

    private String OwnerBYDesc="";
    private String infraUOM="";

    public String getOwnerBY() {
        return OwnerBY;
    }

    public void setOwnerBY(String ownerBY) {
        OwnerBY = ownerBY;
    }

    public String getInfraUOM() {
        return infraUOM;
    }

    public void setInfraUOM(String infraUOM) {
        this.infraUOM = infraUOM;
    }

    public String getInfraQTY() {
        return infraQTY;
    }

    public void setInfraQTY(String infraQTY) {
        this.infraQTY = infraQTY;
    }

    private String infraQTY="";
    public InfraBean getInfraBean() {
        return infraBean;
    }

    public void setInfraBean(InfraBean infraBean) {
        this.infraBean = infraBean;
    }

    private InfraBean infraBean = null;

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
}

package com.arteriatech.ss.msec.iscan.v4.mbo;

public class InfraBean {
    String infraUOM="";
    String infraQTY="";
    String infraCode="";

    public String getOwnerBY() {
        return OwnerBY;
    }

    public void setOwnerBY(String ownerBY) {
        OwnerBY = ownerBY;
    }

    String OwnerBY="";

    public String getOwnerByDesc() {
        return OwnerByDesc;
    }

    public void setOwnerByDesc(String ownerByDesc) {
        OwnerByDesc = ownerByDesc;
    }

    String OwnerByDesc="";

    public InfraBean(String infraCode, String infraCodeDesc, String OwnerBY,String OwnerByDesc, String infraQTY, String infraUOM) {
        this.infraUOM = infraUOM;
        this.infraQTY = infraQTY;
        this.infraCode = infraCode;
        this.infraCodeDesc = infraCodeDesc;
        this.OwnerBY = OwnerBY;
        this.OwnerByDesc = OwnerByDesc;
    }

    public InfraBean(String infraCode) {
        this.infraCode = infraCode;
    }

    public InfraBean() {
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

    public String getInfraCode() {
        return infraCode;
    }

    public void setInfraCode(String infraCode) {
        this.infraCode = infraCode;
    }

    public String getInfraCodeDesc() {
        return infraCodeDesc;
    }

    public void setInfraCodeDesc(String infraCodeDesc) {
        this.infraCodeDesc = infraCodeDesc;
    }

    String infraCodeDesc="";
}

package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class CPProductDetTypeBean implements Serializable {
    @ODataProperty
    private String PrdDetGuid="";
    @ODataProperty
    private String ParentNo="";
    @ODataProperty
    private String ParentType="";
    @ODataProperty
    private String BeatGuid="";
    @ODataProperty
    private String ProdDetType="";
    @ODataProperty
    private String EntityKey="";

    public String getEntityType() {
        return EntityType;
    }

    public void setEntityType(String entityType) {
        EntityType = entityType;
    }

    public String getEntityKey() {
        return EntityKey;
    }

    public void setEntityKey(String entityKey) {
        EntityKey = entityKey;
    }

    public String getProdDetType() {
        return ProdDetType;
    }

    public void setProdDetType(String prodDetType) {
        ProdDetType = prodDetType;
    }

    public String getBeatGuid() {
        return BeatGuid;
    }

    public void setBeatGuid(String beatGuid) {
        BeatGuid = beatGuid;
    }

    public String getParentType() {
        return ParentType;
    }

    public void setParentType(String parentType) {
        ParentType = parentType;
    }

    public String getParentNo() {
        return ParentNo;
    }

    public void setParentNo(String parentNo) {
        ParentNo = parentNo;
    }

    public String getPrdDetGuid() {
        return PrdDetGuid;
    }

    public void setPrdDetGuid(String prdDetGuid) {
        PrdDetGuid = prdDetGuid;
    }

    @ODataProperty
    private String EntityType="";
}

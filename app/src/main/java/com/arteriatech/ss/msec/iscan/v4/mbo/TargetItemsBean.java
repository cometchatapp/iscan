package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class TargetItemsBean implements Serializable {
    @ODataProperty
    private String ActualIncentiveAmt;
    @ODataProperty
    private String ActualQty="";
    @ODataProperty
    private String ActualValue="";
    @ODataProperty
    private String Brand;
    @ODataProperty
    private String BrandDesc;
    @ODataProperty
    private String ChangedAt;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @ODataProperty
    private String Status="";
    @ODataProperty
    private String ChangedBy;
    @ODataProperty
    private String ChangedOn;
    @ODataProperty
    private String CreatedAt;
    @ODataProperty
    private String CreatedBy;
    @ODataProperty
    private String CreatedOn;
    @ODataProperty
    private String Currency;
    @ODataProperty
    private String IncentiveAmount;
    @ODataProperty
    private String ItemNo;
    @ODataProperty
    private String KPICode;
    @ODataProperty
    private String KPIRollUpGUID;
    @ODataProperty
    private String LoginID;
    @ODataProperty
    private String LotSize;
    @ODataProperty
    private String MaterialDesc;
    @ODataProperty
    private String MaterialGroup;
    @ODataProperty
    private String MaterialGrpDesc;
    @ODataProperty
    private String MaterialNo;
    @ODataProperty
    private String OrderMaterialGroupDesc;
    @ODataProperty
    private String OrderMaterialGroupID;
    @ODataProperty
    private String OtherRef;
    @ODataProperty
    private String OtherRefDesc="";
    @ODataProperty
    private String PartnerGUID;
    @ODataProperty
    private String PartnerName;
    @ODataProperty
    private String PartnerNo;
    @ODataProperty
    private String PartnerType;
    @ODataProperty
    private String PartnerTypeDesc;
    @ODataProperty
    private String Period;
    @ODataProperty
    private String Periodicity;
    @ODataProperty
    private String PeriodicityDesc;
    @ODataProperty
    private String TargetAchivement;
    @ODataProperty
    private String TargetGUID;
    @ODataProperty
    private String TargetItemGUID;
    @ODataProperty
    private String TargetName;
    @ODataProperty
    private String TargetQty="";
    @ODataProperty
    private String TargetValue="";
    @ODataProperty
    private String UOM;
    @ODataProperty
    private String Year;
    @ODataProperty
    private String MaxEarning;
    @ODataProperty
    private String Earning;

    public int getActualQtyTemp() {
        return ActualQtyTemp;
    }

    public void setActualQtyTemp(int actualQtyTemp) {
        ActualQtyTemp = actualQtyTemp;
    }

    private int ActualQtyTemp=0;

    public String getMaxEarning() {
        return MaxEarning;
    }

    public void setMaxEarning(String maxEarning) {
        MaxEarning = maxEarning;
    }

    public String getEarning() {
        return Earning;
    }

    public void setEarning(String earning) {
        Earning = earning;
    }

    // Non-MetaProperties
    private boolean isLMGO =false;
    private boolean isMTDGO =false;
    private double LMTargetValue =0;
    private double LMActualValue =0;
    private int LMTargetQty =0;
    private int LMActualQty =0;

    public double getLMTargetValue() {
        return LMTargetValue;
    }

    public void setLMTargetValue(double LMTargetValue) {
        this.LMTargetValue = LMTargetValue;
    }

    public double getLMActualValue() {
        return LMActualValue;
    }

    public void setLMActualValue(double LMActualValue) {
        this.LMActualValue = LMActualValue;
    }

    public int getLMTargetQty() {
        return LMTargetQty;
    }

    public void setLMTargetQty(int LMTargetQty) {
        this.LMTargetQty = LMTargetQty;
    }

    public int getLMActualQty() {
        return LMActualQty;
    }

    public void setLMActualQty(int LMActualQty) {
        this.LMActualQty = LMActualQty;
    }

    public boolean isMTDGO() {
        return isMTDGO;
    }

    public void setMTDGO(boolean MTDGO) {
        isMTDGO = MTDGO;
    }

    public boolean isLMGO() {
        return isLMGO;
    }

    public void setLMGO(boolean LMGO) {
        isLMGO = LMGO;
    }

    public String getActualIncentiveAmt() {
        return ActualIncentiveAmt;
    }

    public void setActualIncentiveAmt(String actualIncentiveAmt) {
        ActualIncentiveAmt = actualIncentiveAmt;
    }

    public String getActualQty() {
        return ActualQty;
    }

    public void setActualQty(String actualQty) {
        ActualQty = actualQty;
    }

    public String getActualValue() {
        return ActualValue;
    }

    public void setActualValue(String actualValue) {
        ActualValue = actualValue;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getBrandDesc() {
        return BrandDesc;
    }

    public void setBrandDesc(String brandDesc) {
        BrandDesc = brandDesc;
    }

    public String getChangedAt() {
        return ChangedAt;
    }

    public void setChangedAt(String changedAt) {
        ChangedAt = changedAt;
    }

    public String getChangedBy() {
        return ChangedBy;
    }

    public void setChangedBy(String changedBy) {
        ChangedBy = changedBy;
    }

    public String getChangedOn() {
        return ChangedOn;
    }

    public void setChangedOn(String changedOn) {
        ChangedOn = changedOn;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getIncentiveAmount() {
        return IncentiveAmount;
    }

    public void setIncentiveAmount(String incentiveAmount) {
        IncentiveAmount = incentiveAmount;
    }

    public String getItemNo() {
        return ItemNo;
    }

    public void setItemNo(String itemNo) {
        ItemNo = itemNo;
    }

    public String getKPICode() {
        return KPICode;
    }

    public void setKPICode(String KPICode) {
        this.KPICode = KPICode;
    }

    public String getKPIRollUpGUID() {
        return KPIRollUpGUID;
    }

    public void setKPIRollUpGUID(String KPIRollUpGUID) {
        this.KPIRollUpGUID = KPIRollUpGUID;
    }

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getLotSize() {
        return LotSize;
    }

    public void setLotSize(String lotSize) {
        LotSize = lotSize;
    }

    public String getMaterialDesc() {
        return MaterialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        MaterialDesc = materialDesc;
    }

    public String getMaterialGroup() {
        return MaterialGroup;
    }

    public void setMaterialGroup(String materialGroup) {
        MaterialGroup = materialGroup;
    }

    public String getMaterialGrpDesc() {
        return MaterialGrpDesc;
    }

    public void setMaterialGrpDesc(String materialGrpDesc) {
        MaterialGrpDesc = materialGrpDesc;
    }

    public String getMaterialNo() {
        return MaterialNo;
    }

    public void setMaterialNo(String materialNo) {
        MaterialNo = materialNo;
    }

    public String getOrderMaterialGroupDesc() {
        return OrderMaterialGroupDesc;
    }

    public void setOrderMaterialGroupDesc(String orderMaterialGroupDesc) {
        OrderMaterialGroupDesc = orderMaterialGroupDesc;
    }

    public String getOrderMaterialGroupID() {
        return OrderMaterialGroupID;
    }

    public void setOrderMaterialGroupID(String orderMaterialGroupID) {
        OrderMaterialGroupID = orderMaterialGroupID;
    }

    public String getOtherRef() {
        return OtherRef;
    }

    public void setOtherRef(String otherRef) {
        OtherRef = otherRef;
    }

    public String getOtherRefDesc() {
        return OtherRefDesc;
    }

    public void setOtherRefDesc(String otherRefDesc) {
        OtherRefDesc = otherRefDesc;
    }

    public String getPartnerGUID() {
        return PartnerGUID;
    }

    public void setPartnerGUID(String partnerGUID) {
        PartnerGUID = partnerGUID;
    }

    public String getPartnerName() {
        return PartnerName;
    }

    public void setPartnerName(String partnerName) {
        PartnerName = partnerName;
    }

    public String getPartnerNo() {
        return PartnerNo;
    }

    public void setPartnerNo(String partnerNo) {
        PartnerNo = partnerNo;
    }

    public String getPartnerType() {
        return PartnerType;
    }

    public void setPartnerType(String partnerType) {
        PartnerType = partnerType;
    }

    public String getPartnerTypeDesc() {
        return PartnerTypeDesc;
    }

    public void setPartnerTypeDesc(String partnerTypeDesc) {
        PartnerTypeDesc = partnerTypeDesc;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getPeriodicity() {
        return Periodicity;
    }

    public void setPeriodicity(String periodicity) {
        Periodicity = periodicity;
    }

    public String getPeriodicityDesc() {
        return PeriodicityDesc;
    }

    public void setPeriodicityDesc(String periodicityDesc) {
        PeriodicityDesc = periodicityDesc;
    }

    public String getTargetAchivement() {
        return TargetAchivement;
    }

    public void setTargetAchivement(String targetAchivement) {
        TargetAchivement = targetAchivement;
    }

    public String getTargetGUID() {
        return TargetGUID;
    }

    public void setTargetGUID(String targetGUID) {
        TargetGUID = targetGUID;
    }

    public String getTargetItemGUID() {
        return TargetItemGUID;
    }

    public void setTargetItemGUID(String targetItemGUID) {
        TargetItemGUID = targetItemGUID;
    }

    public String getTargetName() {
        return TargetName;
    }

    public void setTargetName(String targetName) {
        TargetName = targetName;
    }

    public String getTargetQty() {
        return TargetQty;
    }

    public void setTargetQty(String targetQty) {
        TargetQty = targetQty;
    }

    public String getTargetValue() {
        return TargetValue;
    }

    public void setTargetValue(String targetValue) {
        TargetValue = targetValue;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }
}

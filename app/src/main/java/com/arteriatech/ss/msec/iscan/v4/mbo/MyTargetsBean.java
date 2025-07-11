package com.arteriatech.ss.msec.iscan.v4.mbo;

import java.util.HashSet;

/**
 * Created by e10526 on 02-07-2016.
 */
public class MyTargetsBean {

    private String KPICode = "";

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    private String Status = "";
    private String h1 = "";
    private String h2 = "";
    private String h3 = "";

    public String getH1() {
        return h1;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public String getH2() {
        return h2;
    }

    public void setH2(String h2) {
        this.h2 = h2;
    }

    public String getH3() {
        return h3;
    }

    public void setH3(String h3) {
        this.h3 = h3;
    }

    public String getH4() {
        return h4;
    }

    public void setH4(String h4) {
        this.h4 = h4;
    }

    private String h4 = "";

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    private String header = "";

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    private String createdOn = "";

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    private String sno = "";

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private String data = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title = "";

    private String KPIName = "";

    public String getOtherRefDesc() {
        return OtherRefDesc;
    }

    public void setOtherRefDesc(String otherRefDesc) {
        OtherRefDesc = otherRefDesc;
    }

    private String OtherRefDesc = "";
    private String MonthTarget = "";
    private String MTDA = "";

    public String getActualQty() {
        return ActualQty;
    }

    public void setActualQty(String actualQty) {
        ActualQty = actualQty;
    }

    private String ActualQty = "";

    public String getActualValue() {
        return ActualValue;
    }

    public void setActualValue(String actualValue) {
        ActualValue = actualValue;
    }

    private String ActualValue = "";

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    private String Type = "";

    public String getTargetQty() {
        return TargetQty;
    }

    public void setTargetQty(String targetQty) {
        TargetQty = targetQty;
    }

    private String TargetQty = "";

    public String getTargetValue() {
        return TargetValue;
    }

    public void setTargetValue(String targetValue) {
        TargetValue = targetValue;
    }

    private String TargetValue = "";
    private String CRR = "";
    private String ARR = "";
    private String NetAmount = "";
    private String matCat = "";
    private String kpiGuid = "";
    private String MaterialNo = "";
    private String MaterialDesc = "";
    private String AmtLMTD = "";
    private String AmtMTD = "";
    private String AmtMonth1PrevPerf = "";
    private String AmtMonth2PrevPerf = "";
    private String AmtMonth3PrevPerf = "";
    private String GrPer = "";
    private String KPIFor = "";
    private String CalculationBase = "";
    private String CalculationSource = "";

    private String OrderMaterialGroupID = "";
    private String OrderMaterialGroupDesc = "";
    private String MaterialGroup = "";
    private String MaterialGrpDesc = "";
    private HashSet<String> kpiNames = new HashSet<>();
    private String AchivedPercentage = "";
    private String RollUpTo = "";
    private String BTD = "";
    private String kpiGuid32 = "";
    private String MatNo = "";
    private String MatDesc = "";
    private String KPICategory = "";
    private String Earning = "";
    private String IsearningAdj = "";
    private String MaxEarning = "";

    public String getMaxEarning() {
        return MaxEarning;
    }

    public void setMaxEarning(String maxEarning) {
        MaxEarning = maxEarning;
    }

    public String getIsearningAdj() {
        return IsearningAdj;
    }

    public void setIsearningAdj(String isearningAdj) {
        IsearningAdj = isearningAdj;
    }

    public String getEarning() {
        return Earning;
    }

    public void setEarning(String earning) {
        Earning = earning;
    }

    public String getTcVSPC() {
        return TcVSPC;
    }

    public void setTcVSPC(String tcVSPC) {
        TcVSPC = tcVSPC;
    }

    private String TcVSPC = "";
    public HashSet<String> getKpiNames() {
        return kpiNames;
    }

    public void setKpiNames(HashSet<String> kpiNames) {
        this.kpiNames = kpiNames;
    }

    public String getMaterialGrpDesc() {
        return MaterialGrpDesc;
    }

    public void setMaterialGrpDesc(String materialGrpDesc) {
        MaterialGrpDesc = materialGrpDesc;
    }

    public String getMaterialGroup() {
        return MaterialGroup;
    }

    public void setMaterialGroup(String materialGroup) {
        MaterialGroup = materialGroup;
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

    public String getAchivedPercentage() {
        return AchivedPercentage;
    }

    public void setAchivedPercentage(String achivedPercentage) {
        AchivedPercentage = achivedPercentage;
    }

    public String getRollUpTo() {
        return RollUpTo;
    }

    public void setRollUpTo(String rollUpTo) {
        RollUpTo = rollUpTo;
    }

    public String getCalculationSource() {
        return CalculationSource;
    }

    public void setCalculationSource(String calculationSource) {
        CalculationSource = calculationSource;
    }

    public String getCalculationBase() {
        return CalculationBase;
    }

    public void setCalculationBase(String calculationBase) {
        CalculationBase = calculationBase;
    }

    public String getKPIFor() {
        return KPIFor;
    }

    public void setKPIFor(String KPIFor) {
        this.KPIFor = KPIFor;
    }

    public String getBTD() {
        return BTD;
    }

    public void setBTD(String BTD) {
        this.BTD = BTD;
    }

    public String getGrPer() {
        return GrPer;
    }

    public void setGrPer(String grPer) {
        GrPer = grPer;
    }

    public String getMaterialNo() {
        return MaterialNo;
    }

    public void setMaterialNo(String materialNo) {
        MaterialNo = materialNo;
    }

    public String getMaterialDesc() {
        return MaterialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        MaterialDesc = materialDesc;
    }

    public String getAmtLMTD() {
        return AmtLMTD;
    }

    public void setAmtLMTD(String amtLMTD) {
        AmtLMTD = amtLMTD;
    }

    public String getAmtMTD() {
        return AmtMTD;
    }

    public void setAmtMTD(String amtMTD) {
        AmtMTD = amtMTD;
    }

    public String getAmtMonth1PrevPerf() {
        return AmtMonth1PrevPerf;
    }

    public void setAmtMonth1PrevPerf(String amtMonth1PrevPerf) {
        AmtMonth1PrevPerf = amtMonth1PrevPerf;
    }

    public String getAmtMonth2PrevPerf() {
        return AmtMonth2PrevPerf;
    }

    public void setAmtMonth2PrevPerf(String amtMonth2PrevPerf) {
        AmtMonth2PrevPerf = amtMonth2PrevPerf;
    }

    public String getAmtMonth3PrevPerf() {
        return AmtMonth3PrevPerf;
    }

    public void setAmtMonth3PrevPerf(String amtMonth3PrevPerf) {
        AmtMonth3PrevPerf = amtMonth3PrevPerf;
    }

    public String getKpiGuid() {
        return kpiGuid;
    }

    public void setKpiGuid(String kpiGuid) {
        this.kpiGuid = kpiGuid;
    }

    public String getKpiGuid32() {
        return kpiGuid32;
    }

    public void setKpiGuid32(String kpiGuid32) {
        this.kpiGuid32 = kpiGuid32;
    }

    public String getMatDesc() {
        return MatDesc;
    }

    public void setMatDesc(String matDesc) {
        MatDesc = matDesc;
    }

    public String getMatNo() {
        return MatNo;
    }

    public void setMatNo(String matNo) {
        MatNo = matNo;
    }

    public String getNetAmount() {
        return NetAmount;
    }

    public void setNetAmount(String netAmount) {
        NetAmount = netAmount;
    }

    public String getMatCat() {
        return matCat;
    }

    public void setMatCat(String matCat) {
        this.matCat = matCat;
    }

    public String getARR() {
        return ARR;
    }

    public void setARR(String ARR) {
        this.ARR = ARR;
    }

    public String getKPIName() {
        return KPIName;
    }

    public void setKPIName(String KPIName) {
        this.KPIName = KPIName;
    }

    public String getMonthTarget() {
        return MonthTarget;
    }

    public void setMonthTarget(String monthTarget) {
        MonthTarget = monthTarget;
    }

    public String getMTDA() {
        return MTDA;
    }

    public void setMTDA(String MTDA) {
        this.MTDA = MTDA;
    }

    public String getCRR() {
        return CRR;
    }

    public void setCRR(String CRR) {
        this.CRR = CRR;
    }

    public String getKPICode() {
        return KPICode;
    }

    public void setKPICode(String KPICode) {
        this.KPICode = KPICode;
    }

    public String getKPICategory() {
        return KPICategory;
    }

    public void setKPICategory(String KPICategory) {
        this.KPICategory = KPICategory;
    }
}

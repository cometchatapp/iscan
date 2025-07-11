/*



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CPStockItemBean implements Serializable {
    
    private String AltBlocked;

    public String getUpSellQry() {
        return upSellQry;
    }

    public void setUpSellQry(String upSellQry) {
        this.upSellQry = upSellQry;
    }

    private String minQty="";
    private String minuom="";
    private String maxQty="";

    public String getMinQty() {
        return minQty;
    }

    public void setMinQty(String minQty) {
        this.minQty = minQty;
    }

    public String getMinuom() {
        return minuom;
    }

    public void setMinuom(String minuom) {
        this.minuom = minuom;
    }

    public String getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(String maxQty) {
        this.maxQty = maxQty;
    }

    private String upSellQry="";

    public String getOtherDesc() {
        return otherDesc;
    }

    public void setOtherDesc(String otherDesc) {
        this.otherDesc = otherDesc;
    }

    private String otherDesc="";
    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    private String targetValue="";
    public String getActualvalue() {
        return actualvalue;
    }

    public void setActualvalue(String actualvalue) {
        this.actualvalue = actualvalue;
    }

    private String actualvalue="";
    private boolean isFocusBrandSelected = false;
    private List<SchemeSlabsBean> schemeSlabsBeanList;
    String RejReason="";
    String RejReasonDesc="";

    public String getRejReason() {
        return RejReason;
    }

    public void setRejReason(String rejReason) {
        RejReason = rejReason;
    }

    public String getRejReasonDesc() {
        return RejReasonDesc;
    }

    public void setRejReasonDesc(String rejReasonDesc) {
        RejReasonDesc = rejReasonDesc;
    }
    public List<SchemeSlabsBean> getSchemeSlabsBeanList() {
        return schemeSlabsBeanList;
    }

    public void setSchemeSlabsBeanList(List<SchemeSlabsBean> schemeSlabsBeanList) {
        this.schemeSlabsBeanList = schemeSlabsBeanList;
    }

    public boolean isFocusBrandSelected() {
        return isFocusBrandSelected;
    }

    public void setFocusBrandSelected(boolean focusBrandSelected) {
        isFocusBrandSelected = focusBrandSelected;
    }

    public ArrayList<String> getStrUOM() {
        return strUOM;
    }

    public void setStrUOM(ArrayList<String> strUOM) {
        this.strUOM = strUOM;
    }

    private ArrayList<String> strUOM = new ArrayList<>();

    public String getOtherRef() {
        return OtherRef;
    }

    public void setOtherRef(String otherRef) {
        OtherRef = otherRef;
    }

    private String OtherRef="";

    private String AltFree;

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    private String itemNo = "";

    public String getQAQtyDummy() {
        return QAQtyDummy;
    }

    public void setQAQtyDummy(String QAQtyDummy) {
        this.QAQtyDummy = QAQtyDummy;
    }

    private String QAQtyDummy = "";

    public String getQAQtyTemp() {
        return QAQtyTemp;
    }

    public void setQAQtyTemp(String QAQtyTemp) {
        this.QAQtyTemp = QAQtyTemp;
    }

    private String QAQtyTemp = "";

    private String AltUnrestricted;

    public String getSubBrand() {
        return SubBrand;
    }

    public void setSubBrand(String subBrand) {
        SubBrand = subBrand;
    }

    @ODataProperty
    private String SubBrand;
    @ODataProperty
    private String AlternativeUOM1;
    @ODataProperty
    private String AlternativeUOM1Den;
    @ODataProperty
    private String AlternativeUOM1Num;
    @ODataProperty
    private String AlternativeUOM2;
    @ODataProperty
    private String AlternativeUOM2Den;
    @ODataProperty
    private String AlternativeUOM2Num;
    
    private String AsOnDate;
    
    private String Banner;
    
    private String BannerDesc;
    @ODataProperty
    private String Batch;
    
    private String BatchOrSerial;
    
    private String BlockedQty;
    @ODataProperty
    private String Brand;
    @ODataProperty
    private String BrandDesc;
    
    private String CPGUID;
    
    private String CPName;
    
    private String CPNo;
    @ODataProperty
    private String CPStockItemGUID;
    
    private String CPTypeDesc;
    
    private String CPTypeID;
    @ODataProperty
    private String Currency;
    
    private String DMSDivision;
    
    private String DmsDivisionDesc;
    
    private String ExpiryDate;
    
    private String FreeQty;
    
    private String IsCalcPrice;
    @ODataProperty
    private String LandingPrice;
    
    private String LoginID;
    @ODataProperty
    private String MRP;
    @ODataProperty
    private String ManufacturingDate;
    @ODataProperty
    private String MaterialDesc;
    @ODataProperty
    private String MaterialNo;

    @ODataProperty
    private String CatalogInfo;
    public String getCatalogInfo() {
        return CatalogInfo;
    }

    public void setCatalogInfo(String catalogInfo) {
        CatalogInfo = catalogInfo;
    }


    @ODataProperty
    private String OrderMaterialGroupDesc;
    @ODataProperty
    private String OrderMaterialGroupID;
    
    private String ProdHier1Desc;
    
    private String ProdHier1ID;
    
    private String ProdHier2Desc;
    
    private String ProdHier2ID;
    
    private String ProdHier3Desc;
    
    private String ProdHier3ID;
    
    private String ProdHier4Desc;
    
    private String ProdHier4ID;
    
    private String ProdHier5Desc;
    
    private String ProdHier5ID;
    
    private String ProdHier6Desc;
    
    private String ProdHier6ID;
    
    private String ProdHier7Desc;
    
    private String ProdHier7ID;
    
    private String ProdHier8Desc;
    
    private String ProdHier8ID;
    
    private String ProdHier9Desc;
    @ODataProperty
    private String ProdHier9ID="";

    public String getRateTax() {
        return RateTax;
    }

    public void setRateTax(String rateTax) {
        RateTax = rateTax;
    }

    private String RateTax;

    private String ProductCategoryDesc;
    
    private String ProductCategoryID;
    @ODataProperty
    private String QAQty="";

    private ArrayList<CPStockItemBean> stockItemBeans = new ArrayList<>();

    public ArrayList<CPStockItemBean> getStockItemBeans() {
        return stockItemBeans;
    }

    public void setStockItemBeans(ArrayList<CPStockItemBean> stockItemBeans) {
        this.stockItemBeans = stockItemBeans;
    }

    public String getProposedDlvQty() {
        return ProposedDlvQty;
    }

    public void setProposedDlvQty(String proposedDlvQty) {
        ProposedDlvQty = proposedDlvQty;
    }

    private String ProposedDlvQty="";

    public String getMslQty() {
        return mslQty;
    }

    public void setMslQty(String mslQty) {
        this.mslQty = mslQty;
    }

    private String mslQty="";
    public String getMslflag() {
        return Mslflag;
    }

    public void setMslflag(String mslflag) {
        Mslflag = mslflag;
    }

    private String Mslflag="";
    private String RouteDesc;
    
    private String RouteID;
    
    private String RouteSchGUID;
    @ODataProperty
    private String SKUGroup;
    @ODataProperty
    private String SKUGroupDesc;
    
    private String SPUnrstrctdQty;
    
    private String SalesPersonGUID;
    
    private String SalesPersonID;
    
    private String SalesPersonName;
    
    private String Source;
    @ODataProperty
    private String StockOwner;
    @ODataProperty
    private String StockOwnerDesc;
    
    private String StockSubTypeDesc;
    
    private String StockSubTypeID;
    @ODataProperty
    private String StockTypeDesc;
    @ODataProperty
    private String StockTypeID;
    @ODataProperty
    private String StockValue;
    
    private String StorageLoc;
    @ODataProperty
    private String Tax1;
    @ODataProperty
    private String Tax2;
    @ODataProperty
    private String Tax3;
    public String getDeleteIndicator() {
        return DeleteIndicator;
    }

    public void setDeleteIndicator(String deleteIndicator) {
        DeleteIndicator = deleteIndicator;
    }

    private String DeleteIndicator="";
    
    private String TestRun;
    
    private String Tier1Margin;
    
    private String Tier2Margin;
    
    private String Tier3Margin;
    
    private String Tier4Margin;
    
    private String Tier5Margin;
    @ODataProperty
    private String UOM;
    @ODataProperty
    private String UnrestrictedQty;
    @ODataProperty
    private String MatGrpID;
    @ODataProperty
    private String ExpiryMonth;
    @ODataProperty
    private String ExpiryYear;
    @ODataProperty
    private String BomMatIndicator="";
    @ODataProperty
    private String MatGrpDesc = "";
    private String retailerStock;

    public String getRank() {
        return Rank;
    }

    public void setRank(String rank) {
        Rank = rank;
    }

    private String Rank="";

    public String getIsMustSell() {
        return isMustSell;
    }

    public void setIsMustSell(String isMustSell) {
        this.isMustSell = isMustSell;
    }

    private String isMustSell="";

    */
/**
     * NON META variables
     *//*

    private String taxAmount = "";
    private String disCountAmount = "";
    private String discountPercent = "";
    private String totalDiscountAmount = "";
    private String totalOrderAmount = "";
    private String schemeAmount = "";
    private String rate = "";

    public String getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(String unitValue) {
        this.unitValue = unitValue;
    }

    private String unitValue = "";
    private String gross = "";
    private String selectedUOM = "";
    private boolean isMSL = false;

    public boolean isMSL() {
        return isMSL;
    }

    public void setMSL(boolean MSL) {
        isMSL = MSL;
    }
    public String getSuggestiveQty() {
        return SuggestiveQty;
    }

    public void setSuggestiveQty(String suggestiveQty) {
        SuggestiveQty = suggestiveQty;
    }

    private String SuggestiveQty = "";
    private String StockRefGUID = "";
    private String DBStock="";
    private String isSchemeActive="";
    private String isMaterialActive="";
    private List schemeGUIDList;
    private String pieces = "";
    private String SegID4 = "";
    private String LotSize = "";
    private String TargetAchivement = "";
    private String TargetQuantity = "";
    private String AchievedQuantity = "";
    private String Slab1 = "";
    private String Slab2 = "";
    private String SegmentId = "";
    private String parentMaterial = "";
    private boolean isInvoicedMaterial;
    private boolean isEditable =true;
    private boolean isInnovationMaterial =false;
    private boolean isBilledMaterial =false;
    private int isGap;
    private double incentive1=0;
    private double incentive2;
    private double incentive3;
    private int updatePosition;

    private String cashDiscPercAmt = "";
    private int adapterPosition;
    private String DeviceNo = "";
    private String UnrestrictedQtyTemp="";
    private String reason = "";

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    private String reasonDesc = "";
    private String SSSOItemGUID="";
    private String CPSnoGUID;
    //-----------------------------------------------------------------------end of non meta variables---------------------------------------------

    public CPStockItemBean() {
    }
    public CPStockItemBean(String MaterialNo,String MatDesc, boolean isEditable) {
        this.MaterialNo=MaterialNo;
        this.MaterialDesc=MatDesc;
        this.isEditable=isEditable;
    }

    public CPStockItemBean(String matGrpID, String matGrpDesc) {
        MatGrpID = matGrpID;
        MatGrpDesc = matGrpDesc;
    }

    public String getCPSnoGUID() {
        return CPSnoGUID;
    }

    public void setCPSnoGUID(String CPSnoGUID) {
        this.CPSnoGUID = CPSnoGUID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUnrestrictedQtyTemp() {
        return UnrestrictedQtyTemp;
    }

    public void setUnrestrictedQtyTemp(String unrestrictedQtyTemp) {
        UnrestrictedQtyTemp = unrestrictedQtyTemp;
    }

    public String getBomMatIndicator() {
        return BomMatIndicator;
    }

    public void setBomMatIndicator(String bomMatIndicator) {
        BomMatIndicator = bomMatIndicator;
    }

    public String getSSSOItemGUID() {
        return SSSOItemGUID;
    }

    public int getIsGap() {
        return isGap;
    }

    public void setIsGap(int isGap) {
        this.isGap = isGap;
    }

    public int getUpdatePosition() {
        return updatePosition;
    }

    public void setUpdatePosition(int updatePosition) {
        this.updatePosition = updatePosition;
    }

    public String getCashDiscPercAmt() {
        return cashDiscPercAmt;
    }

    public void setCashDiscPercAmt(String cashDiscPercAmt) {
        this.cashDiscPercAmt = cashDiscPercAmt;
    }

    public String getExpiryMonth() {
        return ExpiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        ExpiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return ExpiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        ExpiryYear = expiryYear;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public String getDeviceNo() {
        return DeviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        DeviceNo = deviceNo;
    }

    public void setSSSOItemGUID(String SSSOItemGUID) {
        this.SSSOItemGUID = SSSOItemGUID;
    }
    public boolean isBilledMaterial() {
        return isBilledMaterial;
    }

    public void setBilledMaterial(boolean billedMaterial) {
        isBilledMaterial = billedMaterial;
    }

    public double getIncentive1() {
        return incentive1;
    }

    public void setIncentive1(double incentive1) {
        this.incentive1 = incentive1;
    }

    public double getIncentive2() {
        return incentive2;
    }

    public void setIncentive2(double incentive2) {
        this.incentive2 = incentive2;
    }

    public double getIncentive3() {
        return incentive3;
    }

    public void setIncentive3(double incentive3) {
        this.incentive3 = incentive3;
    }

    public boolean isInnovationMaterial() {
        return isInnovationMaterial;
    }

    public void setInnovationMaterial(boolean innovationMaterial) {
        isInnovationMaterial = innovationMaterial;
    }

    public String getParentMaterial() {
        return parentMaterial;
    }

    public void setParentMaterial(String parentMaterial) {
        this.parentMaterial = parentMaterial;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getMatGrpID() {
        return MatGrpID;
    }

    public void setMatGrpID(String matGrpID) {
        MatGrpID = matGrpID;
    }

    public int getGap() {
        return isGap;
    }

    public void setGap(int gap) {
        isGap = gap;
    }

    public String getMatGrpDesc() {
        return MatGrpDesc;
    }

    public void setMatGrpDesc(String matGrpDesc) {
        MatGrpDesc = matGrpDesc;
    }

    public String getSegmentId() {
        return SegmentId;
    }

    public void setSegmentId(String segmentId) {
        SegmentId = segmentId;
    }

    public boolean isInvoicedMaterial() {
        return isInvoicedMaterial;
    }

    public void setInvoicedMaterial(boolean invoicedMaterial) {
        isInvoicedMaterial = invoicedMaterial;
    }

    public String getSegID4() {
        return SegID4;
    }

    public void setSegID4(String segID4) {
        SegID4 = segID4;
    }

    public String getLotSize() {
        return LotSize;
    }

    public void setLotSize(String lotSize) {
        LotSize = lotSize;
    }

    public String getTargetAchivement() {
        return TargetAchivement;
    }

    public void setTargetAchivement(String targetAchivement) {
        TargetAchivement = targetAchivement;
    }

    public String getTargetQuantity() {
        return TargetQuantity;
    }

    public void setTargetQuantity(String targetQuantity) {
        TargetQuantity = targetQuantity;
    }

    public String getAchievedQuantity() {
        return AchievedQuantity;
    }

    public void setAchievedQuantity(String achievedQuantity) {
        AchievedQuantity = achievedQuantity;
    }

    public String getSlab1() {
        return Slab1;
    }

    public void setSlab1(String slab1) {
        Slab1 = slab1;
    }

    public String getSlab2() {
        return Slab2;
    }

    public void setSlab2(String slab2) {
        Slab2 = slab2;
    }

    public List getSchemeGUIDList() {
        return schemeGUIDList;
    }

    public void setSchemeGUIDList(List schemeGUIDList) {
        this.schemeGUIDList = schemeGUIDList;
    }

    public String getIsMaterialActive() {
        return isMaterialActive;
    }

    public void setIsMaterialActive(String isMaterialActive) {
        this.isMaterialActive = isMaterialActive;
    }

    public String getIsSchemeActive() {
        return isSchemeActive;
    }

    public void setIsSchemeActive(String isSchemeActive) {
        this.isSchemeActive = isSchemeActive;
    }

    public String getDBStock() {
        return DBStock;
    }

    public void setDBStock(String DBStock) {
        this.DBStock = DBStock;
    }

    public String getRetailerStock() {
        return retailerStock;
    }

    public void setRetailerStock(String retailerStock) {
        this.retailerStock = retailerStock;
    }

    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getAltBlocked() {
        return AltBlocked;
    }

    public void setAltBlocked(String altBlocked) {
        AltBlocked = altBlocked;
    }

    public String getAltFree() {
        return AltFree;
    }

    public void setAltFree(String altFree) {
        AltFree = altFree;
    }

    public String getAltUnrestricted() {
        return AltUnrestricted;
    }

    public void setAltUnrestricted(String altUnrestricted) {
        AltUnrestricted = altUnrestricted;
    }

    public String getAlternativeUOM1() {
        return AlternativeUOM1;
    }

    public void setAlternativeUOM1(String alternativeUOM1) {
        AlternativeUOM1 = alternativeUOM1;
    }

    public String getAlternativeUOM1Den() {
        return AlternativeUOM1Den;
    }

    public void setAlternativeUOM1Den(String alternativeUOM1Den) {
        AlternativeUOM1Den = alternativeUOM1Den;
    }

    public String getAlternativeUOM1Num() {
        return AlternativeUOM1Num;
    }

    public void setAlternativeUOM1Num(String alternativeUOM1Num) {
        AlternativeUOM1Num = alternativeUOM1Num;
    }

    public String getAlternativeUOM2() {
        return AlternativeUOM2;
    }

    public void setAlternativeUOM2(String alternativeUOM2) {
        AlternativeUOM2 = alternativeUOM2;
    }

    public String getAlternativeUOM2Den() {
        return AlternativeUOM2Den;
    }

    public void setAlternativeUOM2Den(String alternativeUOM2Den) {
        AlternativeUOM2Den = alternativeUOM2Den;
    }

    public String getAlternativeUOM2Num() {
        return AlternativeUOM2Num;
    }

    public void setAlternativeUOM2Num(String alternativeUOM2Num) {
        AlternativeUOM2Num = alternativeUOM2Num;
    }

    public String getAsOnDate() {
        return AsOnDate;
    }

    public void setAsOnDate(String asOnDate) {
        AsOnDate = asOnDate;
    }

    public String getBanner() {
        return Banner;
    }

    public void setBanner(String banner) {
        Banner = banner;
    }

    public String getBannerDesc() {
        return BannerDesc;
    }

    public void setBannerDesc(String bannerDesc) {
        BannerDesc = bannerDesc;
    }

    public String getBatch() {
        return Batch;
    }

    public void setBatch(String batch) {
        Batch = batch;
    }

    public String getBatchOrSerial() {
        return BatchOrSerial;
    }

    public void setBatchOrSerial(String batchOrSerial) {
        BatchOrSerial = batchOrSerial;
    }

    public String getBlockedQty() {
        return BlockedQty;
    }

    public void setBlockedQty(String blockedQty) {
        BlockedQty = blockedQty;
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

    public String getCPGUID() {
        return CPGUID;
    }

    public void setCPGUID(String CPGUID) {
        this.CPGUID = CPGUID;
    }

    public String getCPName() {
        return CPName;
    }

    public void setCPName(String CPName) {
        this.CPName = CPName;
    }

    public String getCPNo() {
        return CPNo;
    }

    public void setCPNo(String CPNo) {
        this.CPNo = CPNo;
    }

    public String getCPStockItemGUID() {
        return CPStockItemGUID;
    }

    public void setCPStockItemGUID(String CPStockItemGUID) {
        this.CPStockItemGUID = CPStockItemGUID;
    }

    public String getCPTypeDesc() {
        return CPTypeDesc;
    }

    public void setCPTypeDesc(String CPTypeDesc) {
        this.CPTypeDesc = CPTypeDesc;
    }

    public String getCPTypeID() {
        return CPTypeID;
    }

    public void setCPTypeID(String CPTypeID) {
        this.CPTypeID = CPTypeID;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getDMSDivision() {
        return DMSDivision;
    }

    public void setDMSDivision(String DMSDivision) {
        this.DMSDivision = DMSDivision;
    }

    public String getDmsDivisionDesc() {
        return DmsDivisionDesc;
    }

    public void setDmsDivisionDesc(String dmsDivisionDesc) {
        DmsDivisionDesc = dmsDivisionDesc;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public String getFreeQty() {
        return FreeQty;
    }

    public void setFreeQty(String freeQty) {
        FreeQty = freeQty;
    }

    public String getIsCalcPrice() {
        return IsCalcPrice;
    }

    public void setIsCalcPrice(String isCalcPrice) {
        IsCalcPrice = isCalcPrice;
    }

    public String getLandingPrice() {
        return LandingPrice;
    }

    public void setLandingPrice(String landingPrice) {
        LandingPrice = landingPrice;
    }

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getManufacturingDate() {
        return ManufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        ManufacturingDate = manufacturingDate;
    }

    public String getMaterialDesc() {
        return MaterialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        MaterialDesc = materialDesc;
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

    public String getProdHier1Desc() {
        return ProdHier1Desc;
    }

    public void setProdHier1Desc(String prodHier1Desc) {
        ProdHier1Desc = prodHier1Desc;
    }

    public String getProdHier1ID() {
        return ProdHier1ID;
    }

    public void setProdHier1ID(String prodHier1ID) {
        ProdHier1ID = prodHier1ID;
    }

    public String getProdHier2Desc() {
        return ProdHier2Desc;
    }

    public void setProdHier2Desc(String prodHier2Desc) {
        ProdHier2Desc = prodHier2Desc;
    }

    public String getProdHier2ID() {
        return ProdHier2ID;
    }

    public void setProdHier2ID(String prodHier2ID) {
        ProdHier2ID = prodHier2ID;
    }

    public String getProdHier3Desc() {
        return ProdHier3Desc;
    }

    public void setProdHier3Desc(String prodHier3Desc) {
        ProdHier3Desc = prodHier3Desc;
    }

    public String getProdHier3ID() {
        return ProdHier3ID;
    }

    public void setProdHier3ID(String prodHier3ID) {
        ProdHier3ID = prodHier3ID;
    }

    public String getProdHier4Desc() {
        return ProdHier4Desc;
    }

    public void setProdHier4Desc(String prodHier4Desc) {
        ProdHier4Desc = prodHier4Desc;
    }

    public String getProdHier4ID() {
        return ProdHier4ID;
    }

    public void setProdHier4ID(String prodHier4ID) {
        ProdHier4ID = prodHier4ID;
    }

    public String getProdHier5Desc() {
        return ProdHier5Desc;
    }

    public void setProdHier5Desc(String prodHier5Desc) {
        ProdHier5Desc = prodHier5Desc;
    }

    public String getProdHier5ID() {
        return ProdHier5ID;
    }

    public void setProdHier5ID(String prodHier5ID) {
        ProdHier5ID = prodHier5ID;
    }

    public String getProdHier6Desc() {
        return ProdHier6Desc;
    }

    public void setProdHier6Desc(String prodHier6Desc) {
        ProdHier6Desc = prodHier6Desc;
    }

    public String getProdHier6ID() {
        return ProdHier6ID;
    }

    public void setProdHier6ID(String prodHier6ID) {
        ProdHier6ID = prodHier6ID;
    }

    public String getProdHier7Desc() {
        return ProdHier7Desc;
    }

    public void setProdHier7Desc(String prodHier7Desc) {
        ProdHier7Desc = prodHier7Desc;
    }

    public String getProdHier7ID() {
        return ProdHier7ID;
    }

    public void setProdHier7ID(String prodHier7ID) {
        ProdHier7ID = prodHier7ID;
    }

    public String getProdHier8Desc() {
        return ProdHier8Desc;
    }

    public void setProdHier8Desc(String prodHier8Desc) {
        ProdHier8Desc = prodHier8Desc;
    }

    public String getProdHier8ID() {
        return ProdHier8ID;
    }

    public void setProdHier8ID(String prodHier8ID) {
        ProdHier8ID = prodHier8ID;
    }

    public String getProdHier9Desc() {
        return ProdHier9Desc;
    }

    public void setProdHier9Desc(String prodHier9Desc) {
        ProdHier9Desc = prodHier9Desc;
    }

    public String getProdHier9ID() {
        return ProdHier9ID;
    }

    public void setProdHier9ID(String prodHier9ID) {
        ProdHier9ID = prodHier9ID;
    }

    public String getProductCategoryDesc() {
        return ProductCategoryDesc;
    }

    public void setProductCategoryDesc(String productCategoryDesc) {
        ProductCategoryDesc = productCategoryDesc;
    }

    public String getProductCategoryID() {
        return ProductCategoryID;
    }

    public void setProductCategoryID(String productCategoryID) {
        ProductCategoryID = productCategoryID;
    }

    public String getQAQty() {
        return QAQty;
    }

    public void setQAQty(String QAQty) {
        this.QAQty = QAQty;
    }

    public String getRouteDesc() {
        return RouteDesc;
    }

    public void setRouteDesc(String routeDesc) {
        RouteDesc = routeDesc;
    }

    public String getRouteID() {
        return RouteID;
    }

    public void setRouteID(String routeID) {
        RouteID = routeID;
    }

    public String getRouteSchGUID() {
        return RouteSchGUID;
    }

    public void setRouteSchGUID(String routeSchGUID) {
        RouteSchGUID = routeSchGUID;
    }

    public String getSKUGroup() {
        return SKUGroup;
    }

    public void setSKUGroup(String SKUGroup) {
        this.SKUGroup = SKUGroup;
    }

    public String getSKUGroupDesc() {
        return SKUGroupDesc;
    }

    public void setSKUGroupDesc(String SKUGroupDesc) {
        this.SKUGroupDesc = SKUGroupDesc;
    }

    public String getSPUnrstrctdQty() {
        return SPUnrstrctdQty;
    }

    public void setSPUnrstrctdQty(String SPUnrstrctdQty) {
        this.SPUnrstrctdQty = SPUnrstrctdQty;
    }

    public String getSalesPersonGUID() {
        return SalesPersonGUID;
    }

    public void setSalesPersonGUID(String salesPersonGUID) {
        SalesPersonGUID = salesPersonGUID;
    }

    public String getSalesPersonID() {
        return SalesPersonID;
    }

    public void setSalesPersonID(String salesPersonID) {
        SalesPersonID = salesPersonID;
    }

    public String getSalesPersonName() {
        return SalesPersonName;
    }

    public void setSalesPersonName(String salesPersonName) {
        SalesPersonName = salesPersonName;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getStockOwner() {
        return StockOwner;
    }

    public void setStockOwner(String stockOwner) {
        StockOwner = stockOwner;
    }

    public String getStockOwnerDesc() {
        return StockOwnerDesc;
    }

    public void setStockOwnerDesc(String stockOwnerDesc) {
        StockOwnerDesc = stockOwnerDesc;
    }

    public String getStockSubTypeDesc() {
        return StockSubTypeDesc;
    }

    public void setStockSubTypeDesc(String stockSubTypeDesc) {
        StockSubTypeDesc = stockSubTypeDesc;
    }

    public String getStockSubTypeID() {
        return StockSubTypeID;
    }

    public void setStockSubTypeID(String stockSubTypeID) {
        StockSubTypeID = stockSubTypeID;
    }

    public String getStockTypeDesc() {
        return StockTypeDesc;
    }

    public void setStockTypeDesc(String stockTypeDesc) {
        StockTypeDesc = stockTypeDesc;
    }

    public String getStockTypeID() {
        return StockTypeID;
    }

    public void setStockTypeID(String stockTypeID) {
        StockTypeID = stockTypeID;
    }

    public String getStockValue() {
        return StockValue;
    }

    public void setStockValue(String stockValue) {
        StockValue = stockValue;
    }

    public String getStorageLoc() {
        return StorageLoc;
    }

    public void setStorageLoc(String storageLoc) {
        StorageLoc = storageLoc;
    }

    public String getTax1() {
        return Tax1;
    }

    public void setTax1(String tax1) {
        Tax1 = tax1;
    }

    public String getTax2() {
        return Tax2;
    }

    public void setTax2(String tax2) {
        Tax2 = tax2;
    }

    public String getTax3() {
        return Tax3;
    }

    public void setTax3(String tax3) {
        Tax3 = tax3;
    }

    public String getTestRun() {
        return TestRun;
    }

    public void setTestRun(String testRun) {
        TestRun = testRun;
    }

    public String getTier1Margin() {
        return Tier1Margin;
    }

    public void setTier1Margin(String tier1Margin) {
        Tier1Margin = tier1Margin;
    }

    public String getTier2Margin() {
        return Tier2Margin;
    }

    public void setTier2Margin(String tier2Margin) {
        Tier2Margin = tier2Margin;
    }

    public String getTier3Margin() {
        return Tier3Margin;
    }

    public void setTier3Margin(String tier3Margin) {
        Tier3Margin = tier3Margin;
    }

    public String getTier4Margin() {
        return Tier4Margin;
    }

    public void setTier4Margin(String tier4Margin) {
        Tier4Margin = tier4Margin;
    }

    public String getTier5Margin() {
        return Tier5Margin;
    }

    public void setTier5Margin(String tier5Margin) {
        Tier5Margin = tier5Margin;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public String getUnrestrictedQty() {
        return UnrestrictedQty;
    }

    public void setUnrestrictedQty(String unrestrictedQty) {
        UnrestrictedQty = unrestrictedQty;
    }

    public String getStockRefGUID() {
        return StockRefGUID;
    }

    public void setStockRefGUID(String stockRefGUID) {
        StockRefGUID = stockRefGUID;
    }

    public String getSelectedUOM() {
        return selectedUOM;
    }

    public void setSelectedUOM(String selectedUOM) {
        this.selectedUOM = selectedUOM;
    }

    public String getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(String totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getGross() {
        return gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getDisCountAmount() {
        return disCountAmount;
    }

    public void setDisCountAmount(String disCountAmount) {
        this.disCountAmount = disCountAmount;
    }

    public String getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(String totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public String getSchemeAmount() {
        return schemeAmount;
    }

    public void setSchemeAmount(String schemeAmount) {
        this.schemeAmount = schemeAmount;
    }


    public String getPieces() {
        return pieces;
    }

    public void setPieces(String pieces) {
        this.pieces = pieces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CPStockItemBean that = (CPStockItemBean) o;
        return MaterialNo.equals(that.MaterialNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(MaterialNo);
    }

    @Override
    public String toString() {
        return MaterialNo + " " +MatGrpID +"  "+Brand+" "+BrandDesc+" "+MaterialDesc+" "+MRP+" QTY: "+QAQty + "   matGrp : "+OrderMaterialGroupID+ "   ProdHier9ID : "+ProdHier9ID;
    }

    public String generateUniqueKey() {
        return SKUGroup;
    }
}
*/

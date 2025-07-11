package com.arteriatech.ss.msec.iscan.v4.mbo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by e10526 on 02-08-2018.
 */

public class RetailerCreateBean implements Serializable {
    public String CPGUID = "";
    public String CPNo = "";
    public String PartnerMgrGUID = "";
    public String PartnerMgrName = "";
    public String PartnerMgrNo = "";
    public String Name = "";
    public String CPUID = "";
    public String AccountGrp = "";
    public String ParentName = "";
    public String SearchTerm = "";
    public String CPTypeID = "";
    public String CPTypeDesc = "";
    public String Group1 = "";
    public String Group2 = "";
    public String Group3 = "";
    public String Group4 = "";
    public String CPStock = "";
    public String UOM = "";
    public String DOB = "";
    public String Anniversary = "";
    public String RouteID = "";
    public String RouteSchGUID = "";
    public String RouteDesc = "";
    public String Latitude = "0.00";
    public String Longitude = "0.00";
    public String Address1 = "";
    public String Address2 = "";

    public String getTOTMargin() {
        return TOTMargin;
    }

    public void setTOTMargin(String TOTMargin) {
        this.TOTMargin = TOTMargin;
    }

    public String TOTMargin = "";
    public String Address3 = "";
    public String Address4 = "";
    public String Landmark = "";
    public String ZoneID = "";
    public String ZoneDesc = "";
    public String TownID = "";
    public String TownDesc = "";
    public String CityID = "";
    public String CityDesc = "";
    public String StateID = "";
    public String StateDesc = "";
    public String DistrictID = "";
    public String DistrictDesc = "";
    public String Country = "";
    public String CountryName = "";
    public String PostalCode = "";
    public String Mobile1 = "";
    public String Mobile2 = "";
    public String Landline = "";
    public String EmailID = "";
    public String Currency = "";
    public String CreditLimit = "";
    public String CreditDays = "";
    public String VATNo = "";
    public String TIN = "";
    public String PAN = "";
    public String OwnerName = "";
    public String SalesOfficeID = "";
    public String SalesGrpDesc = "";
    public String SalesGroupID = "";
    public String SalesOffDesc = "";
    public boolean IsKeyCP = false;
    public String WeeklyOff = "";
    public String WeeklyOffDesc = "";
    public String ID1 = "";
    public String ID2 = "";
    public String FaxNo = "";
    public String BusinessID1 = "";
    public String BusinessID2 = "";
    public String Tax1 = "";
    public String Tax2 = "";
    public String Tax3 = "";
    public String Tax4 = "";
    public String TaxClassification = "";
    public String TaxRegStatus = "";
    public String TaxRegStatusDesc = "";
    public String DOBTemp = "";
    public String AnniversaryTemp = "";
    private String ImageMimeType = "";
    private String ImageSize = "";
    private String ImagePath = "";
    private String FileName = "";
    private String DOA="";
    private String DOATemp="";
    private String gstDate="";
    private String channelDescription="";
    private String channelId="";
    private String subChannelId="";

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    private String accountId="";

    public String getPricingId() {
        return pricingId;
    }

    public void setPricingId(String pricingId) {
        this.pricingId = pricingId;
    }

    private String pricingId="";

    public String getMicroChannelId() {
        return microChannelId;
    }

    public void setMicroChannelId(String microChannelId) {
        this.microChannelId = microChannelId;
    }

    private String microChannelId="";

    public String getAdChannelId() {
        return adChannelId;
    }

    public void setAdChannelId(String adChannelId) {
        this.adChannelId = adChannelId;
    }

    private String adChannelId="";
    private String day1="";
    private String day2="";
    private String day3="";
    private String day4="";
    private String day5="";
    private String day6="";
    private String SubDistrictID="";
    private String VillageID="";
    private String VillageDesc="";
    private String WardDesc="";
    private String WardGUID="";
    private String SubDistrictGUID="";
    private String WardID="";
    private String outletCode="";
    private String GSTImage="";
    private String FSSAIImage="";
    private String AddDetails="";
    private String TimeTaken="";
    private String GeoAccuracy="";
    private String URL1="";
    private String URL2="";
    private String URL3="";
    private String URL4="";
    private String URL5="";

    public String getURL6() {
        return URL6;
    }

    public void setURL6(String URL6) {
        this.URL6 = URL6;
    }

    private String URL6="";
    private boolean isRefrigrator=false;

    public boolean isRefrigrator() {
        return isRefrigrator;
    }

    public void setRefrigrator(boolean refrigrator) {
        isRefrigrator = refrigrator;
    }

    private String mobileVerified="";
    private String TownGuid="";

    public String getTownGuid() {
        return TownGuid;
    }

    public void setTownGuid(String townGuid) {
        TownGuid = townGuid;
    }

    public String getMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(String mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public String getAddDetails() {
        return AddDetails;
    }

    public void setAddDetails(String addDetails) {
        AddDetails = addDetails;
    }

    public String getTimeTaken() {
        return TimeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        TimeTaken = timeTaken;
    }

    public String getGeoAccuracy() {
        return GeoAccuracy;
    }

    public void setGeoAccuracy(String geoAccuracy) {
        GeoAccuracy = geoAccuracy;
    }

    public String getURL1() {
        return URL1;
    }

    public void setURL1(String URL1) {
        this.URL1 = URL1;
    }

    public String getURL2() {
        return URL2;
    }

    public void setURL2(String URL2) {
        this.URL2 = URL2;
    }

    public String getURL3() {
        return URL3;
    }

    public void setURL3(String URL3) {
        this.URL3 = URL3;
    }

    public String getURL4() {
        return URL4;
    }

    public void setURL4(String URL4) {
        this.URL4 = URL4;
    }

    public String getURL5() {
        return URL5;
    }

    public void setURL5(String URL5) {
        this.URL5 = URL5;
    }

    public String getGSTImage() {
        return GSTImage;
    }

    public void setGSTImage(String GSTImage) {
        this.GSTImage = GSTImage;
    }

    public String getFSSAIImage() {
        return FSSAIImage;
    }

    public void setFSSAIImage(String FSSAIImage) {
        this.FSSAIImage = FSSAIImage;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getWardID() {
        return WardID;
    }

    public void setWardID(String wardID) {
        WardID = wardID;
    }

    public String getWardGUID() {
        return WardGUID;
    }

    public void setWardGUID(String wardGUID) {
        WardGUID = wardGUID;
    }

    public String getSubDistrictID() {
        return SubDistrictID;
    }

    public void setSubDistrictID(String subDistrictID) {
        SubDistrictID = subDistrictID;
    }

    public String getVillageID() {
        return VillageID;
    }

    public void setVillageID(String villageID) {
        VillageID = villageID;
    }

    public String getVillageDesc() {
        return VillageDesc;
    }

    public void setVillageDesc(String villageDesc) {
        VillageDesc = villageDesc;
    }

    public String getWardDesc() {
        return WardDesc;
    }

    public void setWardDesc(String wardDesc) {
        WardDesc = wardDesc;
    }

    public String getSubDistrictGUID() {
        return SubDistrictGUID;
    }

    public void setSubDistrictGUID(String subDistrictGUID) {
        SubDistrictGUID = subDistrictGUID;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    private boolean check=true;

    public String getDay1() {
        return day1;
    }

    public void setDay1(String day1) {
        this.day1 = day1;
    }

    public String getDay2() {
        return day2;
    }

    public void setDay2(String day2) {
        this.day2 = day2;
    }

    public String getDay3() {
        return day3;
    }

    public void setDay3(String day3) {
        this.day3 = day3;
    }

    public String getDay4() {
        return day4;
    }

    public void setDay4(String day4) {
        this.day4 = day4;
    }

    public String getDay5() {
        return day5;
    }

    public void setDay5(String day5) {
        this.day5 = day5;
    }

    public String getDay6() {
        return day6;
    }

    public void setDay6(String day6) {
        this.day6 = day6;
    }

    public String getDay7() {
        return day7;
    }

    public void setDay7(String day7) {
        this.day7 = day7;
    }

    private String day7="";

    public int getDaycount() {
        return daycount;
    }

    public void setDaycount(int daycount) {
        this.daycount = daycount;
    }

    private int daycount=0;

    public ArrayList<ConfigTypsetInfraBean> getTypsetInfraBeans() {
        return typsetInfraBeans;
    }

    public void setTypsetInfraBeans(ArrayList<ConfigTypsetInfraBean> typsetInfraBeans) {
        this.typsetInfraBeans = typsetInfraBeans;
    }

    private ArrayList<ConfigTypsetInfraBean> typsetInfraBeans=null;

    public String getOutletSizeId() {
        return OutletSizeId;
    }

    public void setOutletSizeId(String outletSizeId) {
        OutletSizeId = outletSizeId;
    }

    private String OutletSizeId ="";

    public String getNoOfEmployee() {
        return NoOfEmployee;
    }

    public void setNoOfEmployee(String noOfEmployee) {
        NoOfEmployee = noOfEmployee;
    }

    private String NoOfEmployee ="";

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    private String distance ="";

    public String getSaleMan() {
        return saleMan;
    }

    public void setSaleMan(String saleMan) {
        this.saleMan = saleMan;
    }

    private String saleMan ="";

    public String getWholeSale() {
        return wholeSale;
    }

    public void setWholeSale(String wholeSale) {
        this.wholeSale = wholeSale;
    }

    private String wholeSale ="";

    public String getNoOfOutlet() {
        return noOfOutlet;
    }

    public void setNoOfOutlet(String noOfOutlet) {
        this.noOfOutlet = noOfOutlet;
    }

    private String noOfOutlet ="";

    public String getOutletShapeId() {
        return OutletShapeId;
    }

    public void setOutletShapeId(String outletShapeId) {
        OutletShapeId = outletShapeId;
    }

    private String OutletShapeId ="";

    public String getBillValueId() {
        return BillValueId;
    }

    public void setBillValueId(String billValueId) {
        BillValueId = billValueId;
    }

    private String BillValueId ="";

    public String getBillCustomerProgramID() {
        return BillCustomerProgramID;
    }

    public void setBillCustomerProgramID(String billCustomerProgramID) {
        BillCustomerProgramID = billCustomerProgramID;
    }

    private String BillCustomerProgramID ="";

    public String getBillCustomerProgram() {
        return BillCustomerProgram;
    }

    public void setBillCustomerProgram(String billCustomerProgram) {
        BillCustomerProgram = billCustomerProgram;
    }

    private String BillCustomerProgram ="";

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    private String typeID ="";

    public String getKMs() {
        return KMs;
    }

    public void setKMs(String KMs) {
        this.KMs = KMs;
    }

    private String KMs ="";

    public String getStockRating() {
        return stockRating;
    }

    public void setStockRating(String stockRating) {
        this.stockRating = stockRating;
    }

    private String stockRating ="";
    private String subChannelDescription="";

    public String getAccountDescription() {
        return accountDescription;
    }

    public void setAccountDescription(String accountDescription) {
        this.accountDescription = accountDescription;
    }

    private String accountDescription="";

    public String getPricingDescription() {
        return pricingDescription;
    }

    public void setPricingDescription(String pricingDescription) {
        this.pricingDescription = pricingDescription;
    }

    private String pricingDescription="";

    public String getMicroChannelDescription() {
        return microChannelDescription;
    }

    public void setMicroChannelDescription(String microChannelDescription) {
        this.microChannelDescription = microChannelDescription;
    }

    private String microChannelDescription="";

    public String getAdChannelDescription() {
        return adChannelDescription;
    }

    public void setAdChannelDescription(String adChannelDescription) {
        this.adChannelDescription = adChannelDescription;
    }

    private String adChannelDescription="";
    private String frequencyId="";
    private String frequencyDesc="";


    public String getChannelDescription() {
        return channelDescription;
    }

    public void setChannelDescription(String channelDescription) {
        this.channelDescription = channelDescription;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSubChannelId() {
        return subChannelId;
    }

    public void setSubChannelId(String subChannelId) {
        this.subChannelId = subChannelId;
    }

    public String getSubChannelDescription() {
        return subChannelDescription;
    }

    public void setSubChannelDescription(String subChannelDescription) {
        this.subChannelDescription = subChannelDescription;
    }

    public String getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(String frequencyId) {
        this.frequencyId = frequencyId;
    }

    public String getFrequencyDesc() {
        return frequencyDesc;
    }

    public void setFrequencyDesc(String frequencyDesc) {
        this.frequencyDesc = frequencyDesc;
    }

    public String getGstDate() {
        return gstDate;
    }

    public void setGstDate(String gstDate) {
        this.gstDate = gstDate;
    }

    public String getDOA() {
        return DOA;
    }

    public void setDOA(String DOA) {
        this.DOA = DOA;
    }

    public String getDOATemp() {
        return DOATemp;
    }

    public void setDOATemp(String DOATemp) {
        this.DOATemp = DOATemp;
    }

    public String getRouteDist() {
        return RouteDist;
    }

    public void setRouteDist(String routeDist) {
        RouteDist = routeDist;
    }

    public String RouteDist = "";

    public String getRouteDistName() {
        return RouteDistName;
    }

    public void setRouteDistName(String routeDistName) {
        RouteDistName = routeDistName;
    }

    public String RouteDistName = "";
    public ArrayList<RetailerClassificationBean> alRetClassfication = new ArrayList<>();

    public String getDOBTemp() {
        return DOBTemp;
    }

    public void setDOBTemp(String DOBTemp) {
        this.DOBTemp = DOBTemp;
    }

    public String getAnniversaryTemp() {
        return AnniversaryTemp;
    }

    public void setAnniversaryTemp(String anniversaryTemp) {
        AnniversaryTemp = anniversaryTemp;
    }

    public ArrayList<RetailerClassificationBean> getAlRetClassfication() {
        return alRetClassfication;
    }

    public void setAlRetClassfication(ArrayList<RetailerClassificationBean> alRetClassfication) {
        this.alRetClassfication = alRetClassfication;
    }

    public String getPartnerMgrName() {
        return PartnerMgrName;
    }

    public void setPartnerMgrName(String partnerMgrName) {
        PartnerMgrName = partnerMgrName;
    }

    public String getPartnerMgrNo() {
        return PartnerMgrNo;
    }

    public void setPartnerMgrNo(String partnerMgrNo) {
        PartnerMgrNo = partnerMgrNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCPUID() {
        return CPUID;
    }

    public void setCPUID(String CPUID) {
        this.CPUID = CPUID;
    }

    public String getAccountGrp() {
        return AccountGrp;
    }

    public void setAccountGrp(String accountGrp) {
        AccountGrp = accountGrp;
    }

    public String getParentName() {
        return ParentName;
    }

    public void setParentName(String parentName) {
        ParentName = parentName;
    }

    public String getSearchTerm() {
        return SearchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        SearchTerm = searchTerm;
    }

    public String getCPTypeID() {
        return CPTypeID;
    }

    public void setCPTypeID(String CPTypeID) {
        this.CPTypeID = CPTypeID;
    }

    public String getCPTypeDesc() {
        return CPTypeDesc;
    }

    public void setCPTypeDesc(String CPTypeDesc) {
        this.CPTypeDesc = CPTypeDesc;
    }

    public String getGroup1() {
        return Group1;
    }

    public void setGroup1(String group1) {
        Group1 = group1;
    }

    public String getGroup2() {
        return Group2;
    }

    public void setGroup2(String group2) {
        Group2 = group2;
    }

    public String getGroup3() {
        return Group3;
    }

    public void setGroup3(String group3) {
        Group3 = group3;
    }

    public String getGroup4() {
        return Group4;
    }

    public void setGroup4(String group4) {
        Group4 = group4;
    }

    public String getCPStock() {
        return CPStock;
    }

    public void setCPStock(String CPStock) {
        this.CPStock = CPStock;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAnniversary() {
        return Anniversary;
    }

    public void setAnniversary(String anniversary) {
        Anniversary = anniversary;
    }

    public String getRouteID() {
        return RouteID;
    }

    public void setRouteID(String routeID) {
        RouteID = routeID;
    }

    public String getRouteDesc() {
        return RouteDesc;
    }

    public void setRouteDesc(String routeDesc) {
        RouteDesc = routeDesc;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getAddress3() {
        return Address3;
    }

    public void setAddress3(String address3) {
        Address3 = address3;
    }

    public String getAddress4() {
        return Address4;
    }

    public void setAddress4(String address4) {
        Address4 = address4;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String landmark) {
        Landmark = landmark;
    }

    public String getZoneID() {
        return ZoneID;
    }

    public void setZoneID(String zoneID) {
        ZoneID = zoneID;
    }

    public String getZoneDesc() {
        return ZoneDesc;
    }

    public void setZoneDesc(String zoneDesc) {
        ZoneDesc = zoneDesc;
    }

    public String getTownID() {
        return TownID;
    }

    public void setTownID(String townID) {
        TownID = townID;
    }

    public String getTownDesc() {
        return TownDesc;
    }

    public void setTownDesc(String townDesc) {
        TownDesc = townDesc;
    }

    public String getCityID() {
        return CityID;
    }

    public void setCityID(String cityID) {
        CityID = cityID;
    }

    public String getCityDesc() {
        return CityDesc;
    }

    public void setCityDesc(String cityDesc) {
        CityDesc = cityDesc;
    }

    public String getStateID() {
        return StateID;
    }

    public void setStateID(String stateID) {
        StateID = stateID;
    }

    public String getStateDesc() {
        return StateDesc;
    }

    public void setStateDesc(String stateDesc) {
        StateDesc = stateDesc;
    }

    public String getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(String districtID) {
        DistrictID = districtID;
    }

    public String getDistrictDesc() {
        return DistrictDesc;
    }

    public void setDistrictDesc(String districtDesc) {
        DistrictDesc = districtDesc;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getMobile1() {
        return Mobile1;
    }

    public void setMobile1(String mobile1) {
        Mobile1 = mobile1;
    }

    public String getMobile2() {
        return Mobile2;
    }

    public void setMobile2(String mobile2) {
        Mobile2 = mobile2;
    }

    public String getLandline() {
        return Landline;
    }

    public void setLandline(String landline) {
        Landline = landline;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getCreditLimit() {
        return CreditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        CreditLimit = creditLimit;
    }

    public String getCreditDays() {
        return CreditDays;
    }

    public void setCreditDays(String creditDays) {
        CreditDays = creditDays;
    }

    public String getVATNo() {
        return VATNo;
    }

    public void setVATNo(String VATNo) {
        this.VATNo = VATNo;
    }

    public String getTIN() {
        return TIN;
    }

    public void setTIN(String TIN) {
        this.TIN = TIN;
    }

    public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getSalesOfficeID() {
        return SalesOfficeID;
    }

    public void setSalesOfficeID(String salesOfficeID) {
        SalesOfficeID = salesOfficeID;
    }

    public String getSalesGrpDesc() {
        return SalesGrpDesc;
    }

    public void setSalesGrpDesc(String salesGrpDesc) {
        SalesGrpDesc = salesGrpDesc;
    }

    public String getSalesGroupID() {
        return SalesGroupID;
    }

    public void setSalesGroupID(String salesGroupID) {
        SalesGroupID = salesGroupID;
    }

    public String getSalesOffDesc() {
        return SalesOffDesc;
    }

    public void setSalesOffDesc(String salesOffDesc) {
        SalesOffDesc = salesOffDesc;
    }

    public boolean isKeyCP() {
        return IsKeyCP;
    }

    public void setKeyCP(boolean keyCP) {
        IsKeyCP = keyCP;
    }

    public String getWeeklyOff() {
        return WeeklyOff;
    }

    public void setWeeklyOff(String weeklyOff) {
        WeeklyOff = weeklyOff;
    }

    public String getWeeklyOffDesc() {
        return WeeklyOffDesc;
    }

    public void setWeeklyOffDesc(String weeklyOffDesc) {
        WeeklyOffDesc = weeklyOffDesc;
    }

    public String getID1() {
        return ID1;
    }

    public void setID1(String ID1) {
        this.ID1 = ID1;
    }

    public String getID2() {
        return ID2;
    }

    public void setID2(String ID2) {
        this.ID2 = ID2;
    }

    public String getFaxNo() {
        return FaxNo;
    }

    public void setFaxNo(String faxNo) {
        FaxNo = faxNo;
    }

    public String getBusinessID1() {
        return BusinessID1;
    }

    public void setBusinessID1(String businessID1) {
        BusinessID1 = businessID1;
    }

    public String getBusinessID2() {
        return BusinessID2;
    }

    public void setBusinessID2(String businessID2) {
        BusinessID2 = businessID2;
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

    public String getTax4() {
        return Tax4;
    }

    public void setTax4(String tax4) {
        Tax4 = tax4;
    }

    public String getTaxClassification() {
        return TaxClassification;
    }

    public void setTaxClassification(String taxClassification) {
        TaxClassification = taxClassification;
    }

    public String getTaxRegStatus() {
        return TaxRegStatus;
    }

    public void setTaxRegStatus(String taxRegStatus) {
        TaxRegStatus = taxRegStatus;
    }

    public String getTaxRegStatusDesc() {
        return TaxRegStatusDesc;
    }

    public void setTaxRegStatusDesc(String taxRegStatusDesc) {
        TaxRegStatusDesc = taxRegStatusDesc;
    }

    public String getCPGUID() {
        return CPGUID;
    }

    public void setCPGUID(String CPGUID) {
        this.CPGUID = CPGUID;
    }

    public String getCPNo() {
        return CPNo;
    }

    public void setCPNo(String CPNo) {
        this.CPNo = CPNo;
    }

    public String getPartnerMgrGUID() {
        return PartnerMgrGUID;
    }

    public void setPartnerMgrGUID(String partnerMgrGUID) {
        PartnerMgrGUID = partnerMgrGUID;
    }


    public String getRouteSchGUID() {
        return RouteSchGUID;
    }

    public void setRouteSchGUID(String routeSchGUID) {
        RouteSchGUID = routeSchGUID;
    }

    public String getImageMimeType() {
        return ImageMimeType;
    }

    public void setImageMimeType(String imageMimeType) {
        ImageMimeType = imageMimeType;
    }

    public String getImageSize() {
        return ImageSize;
    }

    public void setImageSize(String imageSize) {
        ImageSize = imageSize;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }
}

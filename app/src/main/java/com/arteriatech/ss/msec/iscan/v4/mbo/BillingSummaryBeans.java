package com.arteriatech.ss.msec.iscan.v4.mbo;

public class BillingSummaryBeans {
    private int billingOutletsCount=0;
    private int totalOutletsCount=0;
    private int totalKATsOutletsCount=0;

    public int getBillingOutletsCount() {
        return billingOutletsCount;
    }

    public void setBillingOutletsCount(int billingOutletsCount) {
        this.billingOutletsCount = billingOutletsCount;
    }

    public int getTotalOutletsCount() {
        return totalOutletsCount;
    }

    public void setTotalOutletsCount(int totalOutletsCount) {
        this.totalOutletsCount = totalOutletsCount;
    }

    public int getTotalKATsOutletsCount() {
        return totalKATsOutletsCount;
    }

    public void setTotalKATsOutletsCount(int totalKATsOutletsCount) {
        this.totalKATsOutletsCount = totalKATsOutletsCount;
    }

    public int getBillingKATsOutletsCount() {
        return billingKATsOutletsCount;
    }

    public void setBillingKATsOutletsCount(int billingKATsOutletsCount) {
        this.billingKATsOutletsCount = billingKATsOutletsCount;
    }

    private int billingKATsOutletsCount=0;

    public String getBiscuitTodayAch() {
        return biscuitTodayAch;
    }

    public void setBiscuitTodayAch(String biscuitTodayAch) {
        this.biscuitTodayAch = biscuitTodayAch;
    }

    private String biscuitTodayAch="0";
    private String gate1TodayAch="0";

    public String getGate1TodayAch() {
        return gate1TodayAch;
    }

    public void setGate1TodayAch(String gate1TodayAch) {
        this.gate1TodayAch = gate1TodayAch;
    }

    public String getGate2TodayAch() {
        return gate2TodayAch;
    }

    public void setGate2TodayAch(String gate2TodayAch) {
        this.gate2TodayAch = gate2TodayAch;
    }

    private String gate2TodayAch="0";
    private String craTodayAch="0";

    public String getCraTodayAch() {
        return craTodayAch;
    }

    public void setCraTodayAch(String craTodayAch) {
        this.craTodayAch = craTodayAch;
    }

    public String getDairyTodayAch() {
        return dairyTodayAch;
    }

    public void setDairyTodayAch(String dairyTodayAch) {
        this.dairyTodayAch = dairyTodayAch;
    }

    private String dairyTodayAch="0";
}

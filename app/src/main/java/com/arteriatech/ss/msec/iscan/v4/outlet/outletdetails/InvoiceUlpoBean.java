package com.arteriatech.ss.msec.iscan.v4.outlet.outletdetails;

public class InvoiceUlpoBean {

    private String MonthId = "";
    private String year = "";
    private String billval = "";
    private String ulpo = "";
    private  String billIndicator = "";
    private  String ulpoIndicator = "";

    public String getYear() {
        return year;
    }

    public String getBillIndicator() {
        return billIndicator;
    }

    public void setBillIndicator(String billIndicator) {
        this.billIndicator = billIndicator;
    }

    public String getUlpoIndicator() {
        return ulpoIndicator;
    }

    public void setUlpoIndicator(String ulpoIndicator) {
        this.ulpoIndicator = ulpoIndicator;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonthId() {
        return MonthId;
    }

    public void setMonthId(String monthId) {
        MonthId = monthId;
    }

    public String getBillval() {
        return billval;
    }

    public void setBillval(String billval) {
        this.billval = billval;
    }

    public String getUlpo() {
        return ulpo;
    }

    public void setUlpo(String ulpo) {
        this.ulpo = ulpo;
    }
}

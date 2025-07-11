package com.arteriatech.ss.msec.iscan.v4.outlet.outletdetails;

import java.util.ArrayList;

public class SummaryBean {

    private String cpGuid="";
    private String cpType="";
    private String cpUid="";
    private String lastVisit="";
    private String ulpomtd="";
    private String monthavgsale="";
    private String currency="";
    private String saleSummary="";
    private String invoiceulpo="";

    private ArrayList<SummaryGraphBean> alSummaryGraph=new ArrayList<>();
    private ArrayList<InvoiceUlpoBean> alInvoiceUlpo=new ArrayList<>();

    public ArrayList<InvoiceUlpoBean> getAlInvoiceUlpo() {
        return alInvoiceUlpo;
    }

    public void setAlInvoiceUlpo(ArrayList<InvoiceUlpoBean> alInvoiceUlpo) {
        this.alInvoiceUlpo = alInvoiceUlpo;
    }

    public ArrayList<SummaryGraphBean> getAlSummaryGraph() {
        return alSummaryGraph;
    }

    public void setAlSummaryGraph(ArrayList<SummaryGraphBean> alSummaryGraph) {
        this.alSummaryGraph = alSummaryGraph;
    }

    public String getSaleSummary() {
        return saleSummary;
    }

    public void setSaleSummary(String saleSummary) {
        this.saleSummary = saleSummary;
    }

    public String getInvoiceulpo() {
        return invoiceulpo;
    }

    public void setInvoiceulpo(String invoiceulpo) {
        this.invoiceulpo = invoiceulpo;
    }

    public String getCpGuid() {
        return cpGuid;
    }

    public void setCpGuid(String cpGuid) {
        this.cpGuid = cpGuid;
    }

    public String getCpType() {
        return cpType;
    }

    public void setCpType(String cpType) {
        this.cpType = cpType;
    }

    public String getCpUid() {
        return cpUid;
    }

    public void setCpUid(String cpUid) {
        this.cpUid = cpUid;
    }

    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getUlpomtd() {
        return ulpomtd;
    }

    public void setUlpomtd(String ulpomtd) {
        this.ulpomtd = ulpomtd;
    }

    public String getMonthavgsale() {
        return monthavgsale;
    }

    public void setMonthavgsale(String monthavgsale) {
        this.monthavgsale = monthavgsale;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

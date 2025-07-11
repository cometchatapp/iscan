package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

public class SSInvoiceItemBean {
    @ODataProperty
    private String InvoiceGUID;


//-------------------------------------------------------end of meta properties----------------------------------------------------------------------


    public String getInvoiceGUID() {
        return InvoiceGUID;
    }

    public void setInvoiceGUID(String invoiceGUID) {
        InvoiceGUID = invoiceGUID;
    }
}

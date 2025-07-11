package com.arteriatech.ss.msec.iscan.v4.store;

import com.sap.smp.client.httpc.events.ISendEvent;
import com.sap.smp.client.httpc.filters.IRequestFilter;
import com.sap.smp.client.httpc.filters.IRequestFilterChain;

public class XSmpAppCIDRequestFilter implements IRequestFilter {
    private final String xSmpAppCID;

    public XSmpAppCIDRequestFilter(String xSmpAppCID) {
        this.xSmpAppCID = xSmpAppCID;
    }

    @Override
    public Object filter(ISendEvent iSendEvent, IRequestFilterChain iRequestFilterChain) {
        iSendEvent.getRequestHeaders().put("X-SMP-APPCID", xSmpAppCID);
        return iRequestFilterChain.filter();
    }

    @Override
    public Object getDescriptor() {
        return this.getClass();
    }
}

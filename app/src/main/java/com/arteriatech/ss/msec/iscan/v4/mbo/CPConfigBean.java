package com.arteriatech.ss.msec.iscan.v4.mbo;

import com.arteriatech.ss.msec.iscan.v4.utils.annotationutils.ODataProperty;

import java.io.Serializable;

public class CPConfigBean implements Serializable {
    @ODataProperty
    private String CFGAtt1="";
    @ODataProperty
    private String CFGAtt2="";
    @ODataProperty
    private String CFGAtt3="";
    @ODataProperty
    private String STKEXPUPTO="";

    public String getCFGAtt6() {
        return CFGAtt6;
    }

    public void setCFGAtt6(String CFGAtt6) {
        this.CFGAtt6 = CFGAtt6;
    }

    @ODataProperty
    private String CFGAtt6="";

    public String getCFGAtt5() {
        return CFGAtt5;
    }

    public void setCFGAtt5(String CFGAtt5) {
        this.CFGAtt5 = CFGAtt5;
    }

    @ODataProperty
    private String CFGAtt5="";

    public String getCFGAtt3() {
        return CFGAtt3;
    }

    public void setCFGAtt3(String CFGAtt3) {
        this.CFGAtt3 = CFGAtt3;
    }

    public String getCFGAtt2() {
        return CFGAtt2;
    }

    public void setCFGAtt2(String CFGAtt2) {
        this.CFGAtt2 = CFGAtt2;
    }

    public String getCFGAtt1() {
        return CFGAtt1;
    }

    public void setCFGAtt1(String CFGAtt1) {
        this.CFGAtt1 = CFGAtt1;
    }

    public String getSTKEXPUPTO() {
        return STKEXPUPTO;
    }

    public void setSTKEXPUPTO(String STKEXPUPTO) {
        this.STKEXPUPTO = STKEXPUPTO;
    }

    @Override
    public String toString() {
        return CFGAtt1;
    }
}

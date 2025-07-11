package com.arteriatech.ss.msec.iscan.v4.mbo;

import java.io.Serializable;

/**
 * Created by e10604 on 4/1/2018.
 */

public class RemarkReasonBean implements Serializable{

    String reasonCode;
    String reasonDesc;
    boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    public RemarkReasonBean(String resCode, String resDesc){
        this.reasonCode=resCode;
        this.reasonDesc=resDesc;
    }

   public RemarkReasonBean(){

   }

    @Override
    public String toString() {
        return reasonDesc.toString();
    }
}

package com.arteriatech.ss.msec.iscan.v4.interfaces;


import com.arteriatech.ss.msec.iscan.v4.mbo.ErrorBean;

/**
 * Created by e10526 on 6/27/2017.
 */

public interface MessageWithBooleanCallBack {
    void clickedStatus(boolean clickedStatus, String errorMsg, ErrorBean errorBean);
}

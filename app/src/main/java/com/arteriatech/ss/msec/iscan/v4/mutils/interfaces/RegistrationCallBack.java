package com.arteriatech.ss.msec.iscan.v4.mutils.interfaces;

import java.io.Serializable;

/**
 * Created by e10526 on 9/13/2017.
 */

public interface RegistrationCallBack extends Serializable {
    void onRegistrationCallBack(boolean isStatus, String mErrorMsg,String mErrorCode);
    void onNavigateToMainMenu(boolean isStatus);
    void onNavigateToAboutUS(boolean isStatus);
    void onNavigateToLog(boolean isStatus);
}

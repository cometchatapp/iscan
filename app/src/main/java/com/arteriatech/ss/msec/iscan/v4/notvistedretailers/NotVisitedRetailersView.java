package com.arteriatech.ss.msec.iscan.v4.notvistedretailers;

import com.arteriatech.ss.msec.iscan.v4.mbo.RemarkReasonBean;

import java.util.ArrayList;

/**
 * Created by e10526 on 21-04-2018.
 */

public interface NotVisitedRetailersView {
    void showProgressDialog(String message);

    void hideProgressDialog();

    void displayMessage(String message);
    void displayMessageAndNavigateToPreviosScreen(String message);
    void remarksNotEnterdSpecificRetailer(String message);

    void displayByDealers(String[][] arrayDealers, ArrayList<RemarkReasonBean> alReasons);

    void errorFeedBackType(String message);

    void errorReason(String message);

    void errorRemarks(String message);

    void errorOthers(String message);
    void conformationDialog(String message, int from);
    void showMessage(String message, boolean isSimpleDialog);
    void menuVisible(boolean isNextEnable);
}

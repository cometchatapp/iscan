package com.arteriatech.ss.msec.iscan.v4.reports.returnorder.review;

public interface ROReviewView {
    void showProgressDialog(String message);

    void hideProgressDialog();

    void displayMessage(String message);

    void showMessage(String message, boolean isSimpleDialog);
}

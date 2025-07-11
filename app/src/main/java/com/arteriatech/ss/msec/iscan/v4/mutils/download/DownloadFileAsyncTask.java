package com.arteriatech.ss.msec.iscan.v4.mutils.download;

import android.content.Context;
import android.os.AsyncTask;

import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.AsyncTaskCallBackInterface;
import com.arteriatech.mutils.log.LogManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by e10769 on 09-10-2017.
 */

public class DownloadFileAsyncTask extends AsyncTask<Void, String, String> {
    public static final String SO_DETAILS = "SODetails";
    public static final String INVOICES = "Invoices";

    boolean isStatus = false;
    private Context mContext;
    private AsyncTaskCallBackInterface asyncTaskCallBack;
    private String downloadUrl = "";
    private String objectNo = "";
    private String fileExtension = "pdf";
    private String mineType = "pdf";
    private String sharedPrefKey = "";
    private String resoursePath = "";
    private String sessionId = "";
    private boolean isSessionRequired = false;
/*
    @Deprecated
    public DownloadFileAsyncTask(Context mContext, AsyncTaskCallBackInterface asyncTaskCallBack, String downloadUrl, String objectNo, String mineType, String userId, String psw, String connectionId, String resoursePath, String sessionId, boolean isSessionRequired) {
        this.mContext = mContext;
        this.downloadUrl = downloadUrl;
        this.asyncTaskCallBack = asyncTaskCallBack;
        this.objectNo = objectNo;
        this.mineType = mineType;
        this.userId = userId;
        this.psw = psw;
        this.connectionId = connectionId;
        this.resoursePath = resoursePath;
        this.sessionId = sessionId;
        this.isSessionRequired = isSessionRequired;

    }*/
    public DownloadFileAsyncTask(Context mContext, AsyncTaskCallBackInterface asyncTaskCallBack, String downloadUrl, String objectNo, String mineType, String sharedPrefKey, String resoursePath, String sessionId, boolean isSessionRequired) {
        this.mContext = mContext;
        this.downloadUrl = downloadUrl;
        this.asyncTaskCallBack = asyncTaskCallBack;
        this.objectNo = objectNo;
        this.mineType = mineType;
        this.sharedPrefKey = sharedPrefKey;
        this.resoursePath = resoursePath;
        this.sessionId = sessionId;
        this.isSessionRequired = isSessionRequired;

    }

    @Override
    protected String doInBackground(Void... params) {
        String downloadedPath = "";
        try {
//            if (downloadType.equalsIgnoreCase(SO_DETAILS)) {
            switch (mineType) {
                case UtilConstants.XLS_MINE_TYPE:
                    fileExtension = "xls";
                    break;
                case UtilConstants.XLS_MINE_TYPE_1:
                    fileExtension = "xls";
                    break;
                case UtilConstants.DOC_MINE_TYPE:
                    fileExtension = "doc";
                    break;
                case UtilConstants.JPG_MINE_TYPE:
                    fileExtension = "JPEG";
                    break;
                case UtilConstants.POWERPOINT_MINE_TYPE:
                    fileExtension = "PPT";
                    break;
                case UtilConstants.TXT_MINE_TYPE:
                    fileExtension = "txt";
                    break;
                case UtilConstants.TXT_MINE_TYPE_1:
                    fileExtension = "txt";
                    break;
                case UtilConstants.PDF_MINE_TYPE:
                    fileExtension = "pdf";
                    break;
                default:
                    fileExtension = "txt";
                    break;

            }
            File filePath = UtilConstants.downloadSalesOrderFiles(mContext, objectNo, downloadUrl, sharedPrefKey, resoursePath, sessionId, isSessionRequired, fileExtension);
            if (filePath != null) {
                downloadedPath = String.valueOf(filePath.toString());
                isStatus = true;
            } else {
                LogManager.writeLogError("Download Error : not able to get filePath");
                return "Not able to get filePath";
            }
//            }
            /* else if (downloadType.equalsIgnoreCase(INVOICES)) {

                File filePath = UtilConstants.downloadSalesOrderFiles(mContext, objectNo, soItemDetailsURL, userId, psw, connectionId, resoursePath, sessionId, isSessionRequired);
                if (filePath != null) {
                    downloadedPath = String.valueOf(filePath.toString());
                    isStatus = true;
                } else {
                    LogManager.writeLogError("Download Error : not able to get filePath");
                    return "Not able to get filePath";
                }
            }*/
            return downloadedPath;
        } catch (IOException e) {
            e.printStackTrace();
            LogManager.writeLogError("Download Error : " + e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (asyncTaskCallBack != null) {
            asyncTaskCallBack.asyncResponse(isStatus, null, s);
        }
    }
}

package com.arteriatech.ss.msec.iscan.v4.sync.SyncInfo;

public interface CollectionSyncInterface {
    void onUploadDownload(boolean isUpload, PendingCountBean countBean, String syncType);

}

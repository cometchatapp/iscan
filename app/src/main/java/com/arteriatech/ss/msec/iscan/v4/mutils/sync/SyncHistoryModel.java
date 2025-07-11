package com.arteriatech.ss.msec.iscan.v4.mutils.sync;

/**
 * Created by e10769 on 13-01-2017.
 */

public class SyncHistoryModel {
    private String syncGroup;
    private String collections;
    private String timeStamp;

    public SyncHistoryModel(String syncGroup, String collections, String timeStamp) {
        this.syncGroup = syncGroup;
        this.collections = collections;
        this.timeStamp = timeStamp;
    }

    public SyncHistoryModel() {

    }

    public String getSyncGroup() {
        return syncGroup;
    }

    public void setSyncGroup(String syncGroup) {
        this.syncGroup = syncGroup;
    }

    public String getCollections() {
        return collections;
    }

    public void setCollections(String collections) {
        this.collections = collections;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}

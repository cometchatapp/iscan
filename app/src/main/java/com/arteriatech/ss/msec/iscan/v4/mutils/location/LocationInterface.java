package com.arteriatech.ss.msec.iscan.v4.mutils.location;

/**
 * Created by User on 8/22/2017.
 */

public interface LocationInterface {
    void location(boolean status, LocationModel locationModel, String errorMsg, int errorCode);
}

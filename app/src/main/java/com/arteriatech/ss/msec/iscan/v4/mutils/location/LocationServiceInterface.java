package com.arteriatech.ss.msec.iscan.v4.mutils.location;

import android.location.Location;

/**
 * Created by e10526 on 9/6/2017.
 */

public interface LocationServiceInterface {
    void location(boolean status, Location location, String errorMsg, int errorCode, int currentAttempt);
}

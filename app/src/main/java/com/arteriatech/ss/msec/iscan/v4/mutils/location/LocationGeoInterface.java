package com.arteriatech.ss.msec.iscan.v4.mutils.location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;

/**
 * Created by User on 8/22/2017.
 */

public interface LocationGeoInterface {
    void location(boolean status, LocationModel locationModel, String errorMsg, LocationCallback locationCallback, FusedLocationProviderClient mFusedLocationClient);
}

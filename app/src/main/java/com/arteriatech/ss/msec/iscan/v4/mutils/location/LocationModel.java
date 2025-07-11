package com.arteriatech.ss.msec.iscan.v4.mutils.location;

import android.location.Location;

/**
 * Created by e10769 on 15-09-2017.
 */

public class LocationModel {
    private Location location=null;
    private String locationFrom="";

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    private String Latitude="";

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    private String Longitude="";
    private boolean isInternetAvailable=false;

    public boolean isInternetAvailable() {
        return isInternetAvailable;
    }

    public void setInternetAvailable(boolean internetAvailable) {
        isInternetAvailable = internetAvailable;
    }

    public String getLocationFrom() {
        return locationFrom;
    }

    public void setLocationFrom(String locationFrom) {
        this.locationFrom = locationFrom;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

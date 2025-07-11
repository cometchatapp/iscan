package com.arteriatech.ss.msec.iscan.v4.fusedlocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class FusedLocationUtil {
    FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    public static FusedLocationUtil fusedLocationUtil;

    public static FusedLocationUtil getInstance() {
        if (fusedLocationUtil == null) fusedLocationUtil = new FusedLocationUtil();
        return fusedLocationUtil;
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates(Context context) {
        try {
            // Create the location request to start receiving updates
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(20 * 1000);
            locationRequest.setFastestInterval(10 * 1000);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                }

                @Override
                public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                    super.onLocationAvailability(locationAvailability);
                }
            };
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public void fetchLastLocation(Context context,OnLatLng response){
        try {
            if (fusedLocationClient==null){
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location!=null) {
                    response.onRetrieveLatLng(location.getLatitude(),location.getLongitude());
                }else{
                    startLocationUpdates(context);
                    fetchLastLocation(context,response);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void removeLocationUpdates(){
        try {
            if (fusedLocationClient!=null&&locationCallback!=null) {
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public interface OnLatLng{
        void onRetrieveLatLng(double latitude, double longitude);
    }
}

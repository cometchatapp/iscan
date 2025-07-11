package com.arteriatech.ss.msec.iscan.v4.mutils.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


/**
 * Created by User on 8/23/2017.
 */

public class LocationUsingGoogleAPI implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 5 * 1000;
    private static String TAG = LocationUsingGoogleAPI.class.getSimpleName();
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient = null;
    private Activity mContext;
    private LocationServiceInterface locationInterface = null;
    private Handler handler1 = new Handler();
    private Runnable runnable1 = null;
    private Handler handler = new Handler();
    private Runnable runnable = null;
    private boolean locationChanged = false;
    private boolean gotExactLocation = false;
    private Location altLocation = null;
    private float oldAccuracy = 1000;
    private int totalAttempt = 1;
    private int currentAttempt = 1;

    public LocationUsingGoogleAPI(Activity mContext, LocationServiceInterface locationInterface, int totalAttempt) {
        this.mContext = mContext;
        this.locationInterface = locationInterface;
        this.totalAttempt = totalAttempt;
        initiLocationService(mContext);
    }

    private void initiLocationService(Context mContext) {
        if (!LocationUtils.isGPSEnabled(mContext)) {
            setError(LocationUtils.ERROR_GPS_NOT_ENABLE, "location disabled");
        } else {
            boolean gApiOk = (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mContext) == ConnectionResult.SUCCESS);
            if (gApiOk) {
                createLocationRequest();
                mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
                Log.d(TAG, "onStart fired ..............");
                mGoogleApiClient.connect();
            } else {
                setError(LocationUtils.ERROR_PLAY_SERVICE_NOT_THERE, "Please install google play service");
            }
        }
    }

    /*send error msg*/
    private void setError(int errorCode, String msg) {
        Log.d(TAG, "err=>" + msg);
        disConnect();
        if (totalAttempt > currentAttempt) {
            if (errorCode == LocationUtils.ERROR_TIME_OUT) {
                setError(errorCode, msg, currentAttempt);
                currentAttempt++;
                initiLocationService(mContext);
            } else {
                currentAttempt = totalAttempt;
                setError(errorCode, msg, currentAttempt);
                locationInterface = null;
            }
        } else {
            setError(errorCode, msg, currentAttempt);
            locationInterface = null;
        }
    }

    private void setError(int errorCode, String msg, int currentAttempt) {
        if (locationInterface != null) {
            locationInterface.location(false, null, msg, errorCode, currentAttempt);
        }

    }

    /*send location*/
    private void setSuccess(Location location) {
        if (locationInterface != null && location != null) {
            locationInterface.location(true, location, "", LocationUtils.LOCATION_SUCCESS, currentAttempt);
            locationInterface = null;
        } else if (locationInterface != null) {
            locationInterface.location(false, null, "Not able to get location ", LocationUtils.ERROR_OTHER_ERROR, currentAttempt);
            locationInterface = null;
        }
        disConnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mGoogleApiClient != null) {
            Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        setError(LocationUtils.ERROR_CONNECTION_SUSPENDED, "connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        setError(LocationUtils.ERROR_PLAY_SERVICE_LOCATION_FAILED, "connection failed");
    }

    @Override
    public void onLocationChanged(final Location location) {
        Log.d(TAG, "onLocationChanged..............");
        locationChanged = true;
        gotExactLocation = true;
        setSuccess(location);
    }

    /*disconnect location service and handler*/
    private void disConnect() {
        if (mGoogleApiClient != null) {
            Log.d(TAG, "onStop fired ..............");
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
            Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
            mGoogleApiClient = null;
        }
        if (runnable1 != null) {
            handler1.removeCallbacks(runnable1);
            runnable1 = null;
        }
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            runnable = null;
        }
    }

    /*creating location request*/
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest().create();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /*start location update*/
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            setError(LocationUtils.ERROR_PERMISSION_NOT_ENABLE, "Please grant permission for Location in app settings");
            return;
        }
        if (mGoogleApiClient != null) {
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
//            FusedLocationProviderClient pendingResult = LocationServices.getFusedLocationProviderClient(mContext);
//            pendingResult.requestLocationUpdates(mLocationRequest,this,null);
            Log.d(TAG, "Location update started ..............: ");

            if (runnable != null) {
                handler.removeCallbacks(runnable);
                runnable = null;
            }
            Log.d(TAG, "runnable started");
            runnable = new Runnable() {

                @Override
                public void run() {
                    if (!locationChanged) {
                        setError(LocationUtils.ERROR_TIME_OUT, mContext.getString(R.string.unable_to_get_location));
                    }
                }
            };
            handler.postDelayed(runnable, 30 * 1000);
        }
    }

}

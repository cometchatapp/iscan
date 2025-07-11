package com.arteriatech.ss.msec.iscan.v4.mutils.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack;
import com.arteriatech.mutils.log.LogManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by User on 8/22/2017.
 */

public class LocationUtils {
    public static final int ERROR_OTHER_ERROR = 400;
    public static final int ERROR_GPS_NOT_ENABLE = 500;
    public static final int ERROR_PLAY_SERVICE_NOT_THERE = 501;
    public static final int ERROR_PLAY_SERVICE_LOCATION_FAILED = 502;
    public static final int ERROR_PERMISSION_NOT_ENABLE = 503;
    public static final int ERROR_CONNECTION_SUSPENDED = 504;
    public static final int ERROR_NOT_ENABLE_HIGH_ACCURACY = 505;
    public static final int ERROR_NOT_ABLE_TO_GET_HIGH_ACCURACY = 506;
    public static final int ERROR_CANCELED = 507;
    public static final int ERROR_TIME_OUT = 508;
    public static final int LOCATION_SUCCESS = 200;
    public static final int ERROR_SETTINGS_ACCESS_FAILED = 509;
    public static final int REQUEST_CHECK_SETTINGS = 9871;
    private static String TAG = LocationUtils.class.getSimpleName();

    public static boolean isGPSEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    public static boolean isGooglePlayServicesAvailable(Activity mContext) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, mContext, 0).show();
            return false;
        }
    }

    public static boolean isHighAccuracy(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int locationMode = 0;
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return (locationMode != Settings.Secure.LOCATION_MODE_OFF && locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY); //check location mode

        } else {
            String locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }


    public static void getCustomLocation(final Activity activity, final LocationInterface locationInterface) {
        boolean isPermissionEnable = false;
        if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((activity), Manifest.permission.ACCESS_COARSE_LOCATION)
                        && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, UtilConstants.Location_PERMISSION_CONSTANT);
                } else if (UtilConstants.getPermissionStatus(activity, Manifest.permission.ACCESS_COARSE_LOCATION) && UtilConstants.getPermissionStatus(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    UtilConstants.dialogBoxWithButton(activity, "",
                            activity.getString(R.string.this_app_needs_location_permission), activity.getString(R.string.msg_go_to_settings),
                            activity.getString(R.string.cancel), new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                    if (clickedStatus) {
                                        UtilConstants.navigateToAppSettingsScreen(activity);
                                    }
                                }
                            });
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            UtilConstants.Location_PERMISSION_CONSTANT);
                }
                UtilConstants.setPermissionStatus(activity, Manifest.permission.ACCESS_COARSE_LOCATION, true);
                UtilConstants.setPermissionStatus(activity, Manifest.permission.ACCESS_FINE_LOCATION, true);
            } else {
                //You already have the permission, just go ahead check the accuracy settings.
                isPermissionEnable = true;
            }
        } else {
            isPermissionEnable = true;
        }
        if (isPermissionEnable) {
            if (LocationUtils.isGooglePlayServicesAvailable(activity)) {
                //Without Google API Client Auto Location Dialog will not work
                GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(activity)
                        .addApi(LocationServices.API)
                        .build();
                mGoogleApiClient.connect();
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
                locationRequest.setInterval(30 * 1000);
                locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);
                builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

                PendingResult<LocationSettingsResult> result =
                        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        final LocationSettingsStates state = result.getLocationSettingsStates();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                // All location settings are satisfied. The client can initialize location
                                Log.d(TAG, "onResult: success");
                                new LocationUsingGoogleAPI(activity, new LocationServiceInterface() {
                                    @Override
                                    public void location(boolean status, Location location, String errorMsg, int errorCode, int currentAttempt) {
                                        boolean isNetworkAvailable = UtilConstants.isNetworkAvailable(activity);
                                        if (status) {
                                            Log.d("LocationUtils", "latitude: " + location.getLatitude() + " longitude: " + location.getLongitude() + " Accuracy :" + location.getAccuracy());
                                            LocationModel locationModel = new LocationModel();
                                            locationModel.setLocation(location);
                                            locationModel.setInternetAvailable(isNetworkAvailable);
                                            locationModel.setLocationFrom("G");
                                            if (locationInterface != null) {
                                                locationInterface.location(status, locationModel, errorMsg, errorCode);
                                            }
                                        } else {
                                            if (errorCode==ERROR_PLAY_SERVICE_LOCATION_FAILED){
                                                LogManager.writeLogError(UtilConstants.error_txt_location +"Unable to connect google play service");
                                            }else if (errorCode==ERROR_CONNECTION_SUSPENDED){
                                                LogManager.writeLogError(UtilConstants.error_txt_location +"Connection with google play service is suspended");
                                            }else if (errorCode==ERROR_TIME_OUT){
                                                String networkMsg = isNetworkAvailable ? "with mobile data" : "without mobile data ";
                                                LogManager.writeLogError(UtilConstants.error_txt_location + "Unable to get location from google play service " + networkMsg);
                                            }else{
                                                LogManager.writeLogError(UtilConstants.error_txt_location + "other google play service error "+ errorMsg);
                                            }
                                            if (isGPSEnabled(activity)) {
                                                if (isHighAccuracy(activity)) {
                                                    Location locations = UtilConstants.getLocationNoDialog(activity);
                                                    if (locations!=null){
                                                        LocationModel locationModel = new LocationModel();
                                                        locationModel.setLocation(locations);
                                                        locationModel.setInternetAvailable(isNetworkAvailable);
                                                        locationModel.setLocationFrom("L");
                                                        if (locationInterface != null) {
                                                            locationInterface.location(true, locationModel, "", 0);
                                                            return;
                                                        }
                                                    }else {
                                                        LogManager.writeLogError(UtilConstants.error_txt_location +"Unable to get location from Location Manager");
                                                    }
                                                }
                                            }
                                            if (!isNetworkAvailable){
                                                errorMsg = activity.getString(R.string.unable_to_get_location_offline);
                                            }else {
                                                errorMsg = activity.getString(R.string.unable_to_get_location);
                                            }
                                            if (locationInterface != null) {
                                                locationInterface.location(false, null, errorMsg, errorCode);
                                            }
                                            UtilConstants.dialogBoxWithButton(activity,"",errorMsg,activity.getString(R.string.ok),"",null);
                                        }
                                    }

                                }, 1);
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the user
                                // a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                    // Ignore the error.
                                }
                                if (locationInterface != null) {
                                    locationInterface.location(false, null, activity.getString(R.string.alert_gps_not_enabled), ERROR_GPS_NOT_ENABLE);
                                }
                                Log.d(TAG, "onResult: result send");
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                Log.d(TAG, "onResult: settings not satisfied");
                                if (locationInterface != null) {
                                    locationInterface.location(false, null, "Not able to change your settings", ERROR_SETTINGS_ACCESS_FAILED);
                                }
                                UtilConstants.alertLocationPopup(ERROR_PLAY_SERVICE_NOT_THERE, "Not able to change your settings", activity, activity);
                                break;
                        }
                    }
                });
            } else {
                if (locationInterface != null) {
                    locationInterface.location(false, null, "Play service not available", ERROR_PLAY_SERVICE_NOT_THERE);
                }
//                Constants.alertLocationPopup(ERROR_PLAY_SERVICE_NOT_THERE, activity.getString(R.string.install_play_services), activity, activity);
            }
        } else {
            Log.d(TAG, "onResult: settings not satisfied");
            if (locationInterface != null) {
                locationInterface.location(false, null, "Permission not enabled", ERROR_PERMISSION_NOT_ENABLE);
            }
//            Constants.alertLocationPopup(ERROR_PLAY_SERVICE_NOT_THERE, "Permission not enabled", activity, activity);
        }
    }

    public static void checkLocationPermission(final Activity activity, final LocationInterface locationInterface) {
        Log.d("checkLocationPermission", "called");
        boolean isPermissionEnable = false;
        if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((activity), Manifest.permission.ACCESS_COARSE_LOCATION)
                        && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, UtilConstants.Location_PERMISSION_CONSTANT);
                } else if (UtilConstants.getPermissionStatus(activity, Manifest.permission.ACCESS_COARSE_LOCATION) && UtilConstants.getPermissionStatus(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    UtilConstants.dialogBoxWithButton(activity, "",
                            activity.getString(R.string.this_app_needs_location_permission), activity.getString(R.string.msg_go_to_settings),
                            activity.getString(R.string.cancel), new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                    if (clickedStatus) {
                                        UtilConstants.navigateToAppSettingsScreen(activity);
                                    }
                                }
                            });
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            UtilConstants.Location_PERMISSION_CONSTANT);
                }
                UtilConstants.setPermissionStatus(activity, Manifest.permission.ACCESS_COARSE_LOCATION, true);
                UtilConstants.setPermissionStatus(activity, Manifest.permission.ACCESS_FINE_LOCATION, true);
            } else {
                //You already have the permission, just go ahead check the accuracy settings.
                isPermissionEnable = true;
            }
        } else {
            isPermissionEnable = true;
        }
        if (isPermissionEnable) {
            if (LocationUtils.isGooglePlayServicesAvailable(activity)) {
                if (isGPSEnabled(activity)) {
                    if (isHighAccuracy(activity)) {
                        if (locationInterface != null) {
                            locationInterface.location(true, null, "", 0);
                            return;
                        }
                    }
                }
                //Without Google API Client Auto Location Dialog will not work
                GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(activity)
                        .addApi(LocationServices.API)
                        .build();
                mGoogleApiClient.connect();
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
                locationRequest.setInterval(30 * 1000);
                locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);
                builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

                PendingResult<LocationSettingsResult> result =
                        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        final LocationSettingsStates state = result.getLocationSettingsStates();
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                if (locationInterface != null) {
                                    locationInterface.location(true, null, "", 0);
                                }
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the user
                                // a dialog.
                                LogManager.writeLogError(UtilConstants.error_txt_location +"GPS not enabled");
                                if (locationInterface != null) {
                                    locationInterface.location(false, null, activity.getString(R.string.alert_gps_not_enabled), ERROR_GPS_NOT_ENABLE);
                                }
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                    // Ignore the error.
                                }

                                Log.d(TAG, "onResult: result send");
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                Log.d(TAG, "onResult: settings not satisfied");

                                if (locationInterface != null) {
                                    locationInterface.location(false, null, "Not able to change your settings", ERROR_SETTINGS_ACCESS_FAILED);
                                }
                                LogManager.writeLogError(UtilConstants.error_txt_location +"Not able to show settings");
                                UtilConstants.alertLocationPopup(ERROR_PLAY_SERVICE_NOT_THERE, "Not able to change your settings", activity, activity);
                                break;
                        }
                    }
                });
            } else {
                if (locationInterface != null) {
                    locationInterface.location(false, null, "Play service not available", ERROR_PLAY_SERVICE_NOT_THERE);
                }
                LogManager.writeLogError(UtilConstants.error_txt_location +"Google play service not available");
            }
        } else {
            Log.d(TAG, "onResult: settings not satisfied");
            LogManager.writeLogError(UtilConstants.error_txt_location +"Permission not enabled");
            if (locationInterface != null) {
                locationInterface.location(false, null, "Permission not enabled", ERROR_PERMISSION_NOT_ENABLE);
            }
        }
    }

    static LocationCallback locationCallback = null;
    static FusedLocationProviderClient mFusedLocationClient = null;
    @SuppressLint("MissingPermission")
    public static void getLocation(Context context,final LocationGeoInterface locationInterface){
        boolean isPermissionEnable = false;
        if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
            if (ActivityCompat.checkSelfPermission((Activity)context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission((Activity)context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(((Activity)context), Manifest.permission.ACCESS_COARSE_LOCATION)
                        && ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, UtilConstants.Location_PERMISSION_CONSTANT);
                } else if (UtilConstants.getPermissionStatus((Activity)context, Manifest.permission.ACCESS_COARSE_LOCATION) && UtilConstants.getPermissionStatus((Activity)context, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    UtilConstants.dialogBoxWithButton((Activity)context, "",
                            context.getString(R.string.this_app_needs_location_permission), context.getString(R.string.msg_go_to_settings),
                            context.getString(R.string.cancel), new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                    if (clickedStatus) {
                                        UtilConstants.navigateToAppSettingsScreen((Activity)context);
                                    }
                                }
                            });
                } else {
                    ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            UtilConstants.Location_PERMISSION_CONSTANT);
                }
                UtilConstants.setPermissionStatus((Activity)context, Manifest.permission.ACCESS_COARSE_LOCATION, true);
                UtilConstants.setPermissionStatus((Activity)context, Manifest.permission.ACCESS_FINE_LOCATION, true);
            } else {
                //You already have the permission, just go ahead check the accuracy settings.
                isPermissionEnable = true;
            }
        } else {
            isPermissionEnable = true;
        }
        if(isPermissionEnable) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setNumUpdates(3);
            locationRequest.setInterval(1 * 1000);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        locationInterface.location(false, null, "Unable to get location. Check and retry again", locationCallback,mFusedLocationClient);
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (locationInterface != null) {
                            /*if (mFusedLocationClient != null) {
                                mFusedLocationClient.removeLocationUpdates(locationCallback);
                            }*/
                            LocationModel locationModel = new LocationModel();
                            locationModel.setLocation(location);
                            locationInterface.location(true, locationModel, "", locationCallback,mFusedLocationClient);
                            break;
                        }
                    }
                }
            };
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }else {
            Log.d(TAG, "onResult: settings not satisfied");
            LogManager.writeLogError(UtilConstants.error_txt_location +"Permission not enabled");
            if (locationInterface != null) {
                locationInterface.location(false, null, "Permission not enabled", locationCallback,mFusedLocationClient);
            }
        }
    }
}

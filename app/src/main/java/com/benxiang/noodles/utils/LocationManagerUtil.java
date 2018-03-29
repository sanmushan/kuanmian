package com.benxiang.noodles.utils;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.benxiang.noodles.AppApplication;
import com.benxiang.noodles.contants.Constants;

import java.util.List;

/**
 * Created by 刘圣如 on 2017/9/5.
 */

public class LocationManagerUtil {
    private static LocationManagerUtil locationManagerUtil;
    private LocationManager locationManager;
    private String locationProvider;
    private Location location;
    private LocationManagerUtil() {
        locationManager = (LocationManager) AppApplication.getAppContext()
                .getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider=LocationManager.GPS_PROVIDER;
        }
        else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider=LocationManager.NETWORK_PROVIDER;
        }
        Log.e("位置", "LocationManagerUtil: "+ locationProvider);
        location = locationManager.getLastKnownLocation(locationProvider);
     /*   if (location != null) {
            saveLocation(location);
        }*/
        // 监视地理位置变化
       /* locationManager.requestLocationUpdates(locationProvider, 3000, 1,
                locationListener);
*/
    }

    public void saveLocation(Location location) {
        PreferenceUtil.config().setFloatValue(Constants.LOCATIONLAT, (float) location.getLongitude());
        PreferenceUtil.config().setFloatValue(Constants.LOCATIONLONG, (float) location.getLatitude());
    }

    public static LocationManagerUtil getLocationManager() {
        if (locationManagerUtil == null) {
            synchronized (LocationManagerUtil.class) {
                if (locationManagerUtil == null) {
                    locationManagerUtil=new LocationManagerUtil();
                }
            }
        }
        return locationManagerUtil;
    }
    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            // 如果位置发生变化,重新显示
            saveLocation(location);

        }
    };
}

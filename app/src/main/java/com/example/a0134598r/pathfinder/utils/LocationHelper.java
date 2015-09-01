package com.example.a0134598r.pathfinder.utils;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;

/**
 * Created by jixiang on 1/9/15.
 */
public class LocationHelper {


    public static boolean isProviderEnabled(Activity activity) {

        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }
}

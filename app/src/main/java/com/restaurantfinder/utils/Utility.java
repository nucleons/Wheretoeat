package com.restaurantfinder.utils;

import android.location.Location;

import java.text.DecimalFormat;

/**
 * Created by Aditya on 14-Apr-16.
 */
 public class Utility {

    public static double getDistance(String startLatitude, String startLongitude, String endLatitude, String endLongitude) {

        double sLatitude = Double.parseDouble(startLatitude);
        double sLongitude = Double.parseDouble(startLongitude);
        double eLatitude = Double.parseDouble(endLatitude);
        double eLongitude = Double.parseDouble(endLongitude);
        Location locationA = new Location("point A");

        locationA.setLatitude(sLatitude);
        locationA.setLongitude(sLongitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(eLatitude);
        locationB.setLongitude(eLongitude);

        float distance = locationA.distanceTo(locationB);

        return distance/1000;

    }
}

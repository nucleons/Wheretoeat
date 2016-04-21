package com.restaurantfinder.startup;

import android.app.Application;
import android.content.Context;

/**
 * Created by Aditya on 11-Apr-16.
 */
public class RestaurantApplication extends Application {

    private static Context applicationContext;

    public void onCreate() {
        super.onCreate();
        applicationContext = this.getApplicationContext();
    }

    public static Context getContext() {
        return applicationContext;
    }
}

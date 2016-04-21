package com.restaurantfinder.net;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.restaurantfinder.startup.RestaurantApplication;

/**
 * Helper class that is used to provide references to initialized RequestQueue(s) and ImageLoader(s)
 *
 */
public class VolleyHelper {

    private static VolleyHelper sInstance;
    private RequestQueue mRequestQueue;

    private VolleyHelper(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        }
        throw new IllegalStateException("Request Queue not initialised");

    }

    public static synchronized VolleyHelper getInstance() {
        if (sInstance == null) {
            sInstance = new VolleyHelper(RestaurantApplication.getContext());
        }
        return sInstance;
    }

    public <T>void addToRequestQueue(GsonRequest<T> req) {
        getRequestQueue().add(req);
    }

}
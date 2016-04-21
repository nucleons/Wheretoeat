package com.restaurantfinder.manager;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.restaurantfinder.model.RestaurantList;
import com.restaurantfinder.net.GsonRequest;
import com.restaurantfinder.net.VolleyHelper;

/**
 * Created by Aditya on 13-Apr-16.
 */
public class DownloadManager {

    public interface ContentDownloadListener {

        void onContentDownloadSuccess(Object response);

        void onContentDownloadFailure();
    }

    public void getContent(final String url,
                              final ContentDownloadListener listener) {

        final GsonRequest<RestaurantList> request;

        request = new GsonRequest<RestaurantList>(url,
                RestaurantList.class,
                new Response.Listener<RestaurantList>() {
                    @Override
                    public void onResponse(RestaurantList response) {
                        if (response != null) {
                            listener.onContentDownloadSuccess(response);
                        } else {
                            listener.onContentDownloadFailure();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Throwable cause = error.getCause();
                if (cause != null)
                    cause.printStackTrace();
                if (listener != null)
                    listener.onContentDownloadFailure();
            }
        }, Request.Method.GET, null,true);
        request.setShouldCache(true);
        VolleyHelper.getInstance().addToRequestQueue(request);

    }
}

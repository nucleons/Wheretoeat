package com.restaurantfinder.net;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import android.os.AsyncTask;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Aditya on 13-Apr-16.
 */
public class GsonRequest<T> extends JsonRequest<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Listener<T> listener;
    public static final int TIMEOUT_MS = 60 * 1000;
    private boolean isList = false;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url
     *            URL of the request to make
     * @param clazz
     *            Relevant class object, for Gson's reflection

     *            Map of request headers
     * @param method
     * @param requestBody
     */
    public GsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener, int method, String requestBody
    ,boolean islist) {
        super(method, url, requestBody, listener, errorListener);
        this.clazz = clazz;
        this.isList = islist;
        this.listener = listener;
        setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            if(isList){
                String customJson = "{\"restaurantList\":"+json+"}";
                return Response.success(gson.fromJson(customJson, clazz),
                        HttpHeaderParser.parseCacheHeaders(response));
            }
            return Response.success(gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}

package com.restaurantfinder.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Aditya on 12-Apr-16.
 */
public class Restaurant {

    @SerializedName("restaurant_name")
    public String name;

    @SerializedName("address")
    public String address;

    public String latitude;

    public String longitude;

    @SerializedName("phone_number")
    public String phoneNumber;

    @SerializedName("logo_url")
    public String logo;
}

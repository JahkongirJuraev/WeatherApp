package com.example.weatherapp.core.cache;

import android.content.Context;
import android.content.SharedPreferences;

public class Cache {

    private static final String CITY_NAME = "city_name";
    public static Cache cache;
    public static SharedPreferences preferences;

    public Cache(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences("cache", Context.MODE_PRIVATE);
        }
    }

    public static Cache getInstance() {
        return cache;
    }

    public static void init(Context context) {
        if (cache == null) {
            cache = new Cache(context);
        }
    }

    public String getCityName() {
        return preferences.getString(CITY_NAME, "Samarqand");
    }

    public void setCityName(String cityName) {
        preferences.edit().putString(CITY_NAME, cityName).apply();
    }

    public String getDefaultCity() {
        return preferences.getString("DEFAULT_CITY_NAME", "Samarqand");
    }

    public void setDefaultCity(String cityName) {
        preferences.edit().putString("DEFAULT_CITY_NAME", cityName).apply();
    }


    public boolean getLocationGranted() {
        return preferences.getBoolean("isLocationGranted", false);
    }

    public void setLocationGranted(boolean isLocationGranted) {
        preferences.edit().putBoolean("isLocationGranted", isLocationGranted).apply();
    }

}
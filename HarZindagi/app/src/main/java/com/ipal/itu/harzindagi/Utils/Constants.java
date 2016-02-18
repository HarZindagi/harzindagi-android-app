package com.ipal.itu.harzindagi.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

/**
 * Created by Ali on 2/16/2016.
 */
public class Constants {

    public static final String get_device_info = "http://103.226.216.170:3000/get_device_info.json";
    public static final String visits = "http://103.226.216.170:3000/admin/visits.json";
    public static final String injections = "http://103.226.216.170:3000/admin/injections.json";
    public static final String vaccinations = "http://103.226.216.170:3000/admin/vaccinations.json";
    public static final String kids = "http://103.226.216.170:3000/admin/kids.json";
    public static final String kid_vaccinations = "http://103.226.216.170:3000/admin/kid_vaccinations.json";

    public static final String login = "http://103.226.216.170:3000/login";

    public static final String token = "token";
    public static final String uc = "uc";

    public static String getToken(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.token, "");
    }

    public static void setToken(Context c, String token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.token, token).commit();
    }

    public static String getUC(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.uc, "");
    }

    public static void setUC(Context c, String token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.uc, token).commit();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

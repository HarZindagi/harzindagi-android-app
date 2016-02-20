package com.ipal.itu.harzindagi.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

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
    public static final String password = "password";
    public static final String name = "name";
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

    public static void setUC(Context c, String uc) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.uc, uc).commit();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static  String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
    public static String getPassword(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.password, "");
    }

    public static void setPassword(Context c, String pasword) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.password, pasword).commit();
    }
    public static String getUserName(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.name, "");
    }

    public static void setUserName(Context c, String userName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.name, userName).commit();
    }
}

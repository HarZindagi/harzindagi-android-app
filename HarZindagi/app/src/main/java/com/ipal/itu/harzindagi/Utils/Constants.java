package com.ipal.itu.harzindagi.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Ali on 2/16/2016.
 */
public class Constants {

    public static final String get_device_info = "http://103.226.216.170:3000/get_device_info.json";

    public static final String login = "http://103.226.216.170:3000/login";

    public static final String token = "token";

    public static String getToken(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.token, "");
    }
    public static void setToken(Context c,String token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.token,token);
    }
}

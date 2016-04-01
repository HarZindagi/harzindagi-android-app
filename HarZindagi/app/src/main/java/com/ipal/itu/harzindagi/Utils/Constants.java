package com.ipal.itu.harzindagi.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ali on 2/16/2016.
 */
public class Constants {

    public static final String get_device_info = "http://103.226.216.170:3000/get_device_info.json";
    public static final String visits = "http://103.226.216.170:3000/admin/visits.json";
    public static final String vaccinationsItems = "http://103.226.216.170:3000//admin/kid_vaccinations/";
    public static final String injections = "http://103.226.216.170:3000/admin/injections.json";
    public static final String vaccinations = "http://103.226.216.170:3000/admin/vaccinations.json";
    public static final String kids = "http://103.226.216.170:3000/admin/kids.json";
    public static final String kid_vaccinations = "http://103.226.216.170:3000/admin/kid_vaccinations.json";

    public static final String login = "http://103.226.216.170:3000/login";
    public static final String logout = "http://103.226.216.170:3000/logout.json";

    public static final String search = "http://103.226.216.170:3000/admin/kids/kid_search";

    public static final String photos = "http://103.226.216.170:3000/photos";
    public static final String imageDownload =  "http://103.226.216.170:3000/images/";

    public static final String checkouts   = "http://103.226.216.170:3000/admin/checkouts";
    public static final String checkins   = "http://103.226.216.170:3000/admin/checkins";

    public static final String token = "token";
    public static final String password = "password";
    public static final String name = "name";
    public static final String uc = "uc";
    public static final String isTableLoaded = "isTableLoaded";

    public static final String checkIn = "checkIn";
    public static final String checkOut = "checkOut";

    public static final String location = "location";
    public static final String day = "cDay";

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

    public static String getIMEI(Context context) {
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

    public static boolean getIsTableLoaded(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getBoolean(Constants.isTableLoaded, false);
    }

    public static void setIsTableLoaded(Context c, boolean isLoaded) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putBoolean(Constants.isTableLoaded, isLoaded).commit();
    }

    public static String getCheckIn(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.checkIn, "");
    }

    public static void setCheckIn(Context c, String checkin) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.checkIn, checkin).commit();
    }
    public static String getCheckOut(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.checkOut, "");
    }

    public static void setCheckOut(Context c, String checkin) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.checkOut, checkin).commit();
    }

    public static String getLocation(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.location, "0.0000,0.0000");
    }

    public static void setLocation(Context c, String location) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.location, location).commit();
    }

    public static String getDay(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.day, "");
    }

    public static void setDay(Context c, String day) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.day, day).commit();
    }
    public static String getVersionName(Context c) {
        PackageInfo pInfo = null;
        try {
            pInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo.versionName;
    }


    public static String getNextDueDate(int visit, String vaccs) {

        int[] Arry = {0, 42, 28, 28, 154, 168, 0};  // should better be made dynamic input through Database.


        Calendar c = Calendar.getInstance();

        if (isVaccOfVisitCompleted(vaccs)) {
            c.add(Calendar.DATE, Arry[visit]);
        } else {
            c.add(Calendar.DATE, 10);
        }

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
        return sdf1.format(c.getTime());

    }
    public static long getNextDueDateNew(int visit, String vaccs) {

        int[] Arry = {0, 42, 28, 28, 154, 168, 36000};  // should better be made dynamic input through Database.


        Calendar c = Calendar.getInstance();

        if (isVaccOfVisitCompleted(vaccs)) {
            c.add(Calendar.DATE, Arry[visit]);
        } else {
            c.add(Calendar.DATE, 10);
        }

       // SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
        return c.getTimeInMillis()/1000;

    }



    public static Boolean isVaccOfVisitCompleted(String vaccs) {

        String[] arry = vaccs.split(",");

        for (int i = 0; i < arry.length; i++) {
            if (arry[i].equals("0"))
                return false;

        }
        return true;


    }
    public static Bitmap getBitmapFromFile(String file) {
        File f = new File(file);
        Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return bmp;
    }
    public static byte[] getBitmapBytesFromFile(String file) {
        File f = new File(file);
        Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();

    }
    public static byte[] getBytesFromBitmao( Bitmap bmp) {
     ;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();

    }
    public static String getApplicationName(Context context) {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }
    public static int pxToDp(Context context,int px) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics());
       /* DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;*/
    }
    public static String getFortmattedDate(long date){


        java.util.Date time=new java.util.Date(date*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        String formatedDate = sdf.format(time);
        return  formatedDate;
    }
}

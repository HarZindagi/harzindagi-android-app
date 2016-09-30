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
import android.util.TypedValue;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ipal.itu.harzindagi.Activities.RegisterChildActivity;
import com.ipal.itu.harzindagi.Application.HarZindagiApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Project Har Zindagi
 * Created by Ali on 2/16/2016.
 */
@SuppressWarnings("WeakerAccess")
public class Constants {
    //http://103.226.216.9/
    //http://10.52.96.4/
    public static final String baseURL = "http://103.226.216.9/";
    public static final String get_device_info = baseURL + "get_device_info.json";
    public static final String visits = baseURL + "admin/visits.json";
    public static final String vaccinationsItems = baseURL + "/admin/kid_vaccinations/";
    public static final String injections = baseURL + "admin/injections.json";
    public static final String vaccinations = baseURL + "admin/vaccinations.json";
    public static final String kids = baseURL + "admin/kids.json";
    public static final String kids_evaccs = baseURL + "admin/evacs_epis";
    public static final String kids_evaccsNonEPI = baseURL + "admin/evacs_nonepis";
    public static final String kid_vaccinations = baseURL + "admin/kid_vaccinations.json";
    public static final String login = baseURL + "login";
    public static final String logout = baseURL + "logout.json";
    public static final String search = baseURL + "admin/kids/kid_search";
    public static final String photos = baseURL + "photos";
    public static final String imageDownload = baseURL + "images/";
    public static final String checkouts = baseURL + "admin/checkouts";
    public static final String kitStation = baseURL + "admin/kid_stations";
    public static final String checkins = baseURL + "admin/checkins";
    public static final String areas = baseURL + "admin/areas.json";
    public static final String books = baseURL + "admin/books";
    public static final String token = "token";
    public static final String password = "password";
    public static final String name = "name";
    public static final String uc = "uc";
    public static final String uc_id = "uc_id";
    public static final String isTableLoaded = "isTableLoaded";
    public static final String checkIn = "checkIn";
    public static final String locationSource = "locationSource";
    public static final String locationSync = "locationSync";
    public static final String checkOut = "checkOut";
    public static final String location = "location";
    public static final String day = "cDay";
    private static final boolean isTracking = true;

    public static String getToken(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.token, "");
    }

    public static void setToken(Context c, String token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.token, token).apply();
    }

    public static String getUC(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.uc, "");
    }

    public static void setUC(Context c, String uc) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.uc, uc).apply();
    }

    public static int getUCID(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getInt(Constants.uc_id, 0);
    }

    public static void setUCID(Context c, int uc_id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putInt(Constants.uc_id, uc_id).apply();
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
        prefs.edit().putString(Constants.password, pasword).apply();
    }

    public static String getUserName(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.name, "");
    }

    public static void setUserName(Context c, String userName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.name, userName).apply();
    }

    public static boolean getIsTableLoaded(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getBoolean(Constants.isTableLoaded, false);
    }

    public static void setIsTableLoaded(Context c, boolean isLoaded) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putBoolean(Constants.isTableLoaded, isLoaded).apply();
    }

    /* public static String getLocationSource(Context c) {
         SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
         return prefs.getString(Constants.locationSource, "0.0000,0.0000");
     }
     public static void setLocationSource(Context c, String location_source) {
         SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
         prefs.edit().putString(Constants.locationSource, location_source).commit();
     }*/
    public static String getLocationSync(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.locationSync, "");
    }

    public static void setLocationSync(Context c, String location_sync) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.locationSync, location_sync).apply();
    }

    public static String getCheckIn(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.checkIn, "");
    }

    public static void setCheckIn(Context c, String checkin) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.checkIn, checkin).apply();
    }

    public static String getCheckOut(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.checkOut, "");
    }

    public static void setCheckOut(Context c, String checkin) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.checkOut, checkin).apply();
    }

    public static String getLocation(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.location, "0.0000,0.0000");
    }

    public static void setLocation(Context c, String location) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.location, location).apply();
    }

    public static String getDay(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(Constants.day, "");
    }

    public static void setDay(Context c, String day) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString(Constants.day, day).apply();
    }

    public static String getVersionName(Context c) {
        PackageInfo pInfo = null;
        try {
            pInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(pInfo!=null){
            return pInfo.versionName;
        }else{
            return "not found";
        }

    }
    public static void logTime(Activity c ,long time,String lable){
        String avergatTime = time+"";
        if (time <= 5) {
            avergatTime = ""+5;
        } else if (time <= 10) {
            avergatTime = ""+ 10;
        } else if (time <= 15) {
            avergatTime = ""+ 15;
        } else if (time <= 20) {
            avergatTime = ""+ 20;
        } else if (time <= 30) {
            avergatTime = ""+ 30;
        } else if (time <= 40) {
            avergatTime = ""+ 40;
        } else if (time <= 50) {
            avergatTime = ""+ 50;
        } else if (time <= 60) {
            avergatTime = ""+ 60;
        } else if (time <= 70) {
            avergatTime = ""+ 70;
        } else if (time <= 80) {
            avergatTime = ""+ 80;
        } else if (time <= 90) {
            avergatTime = ""+ 90;
        } else if (time <= 100) {
            avergatTime = ""+ 100;
        }
        else{
            avergatTime = "100+";
        }

        Constants.sendGAEvent(c, Constants.getUserName(c), lable, avergatTime + " S", 0);
    }
    public static String addDate(String dateStr) {
        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy",Locale.US);
        Date date = null;
        Calendar c = Calendar.getInstance();
        try {
            date = df.parse(dateStr);
            c.setTime(date);
            c.add(Calendar.DAY_OF_YEAR, 28);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String nextDate = df.format(c.getTime());
        return nextDate;
    }
    public static String getNextDueDate(int visit, String vaccs) {

        int[] Arry = {0, 42, 28, 28, 154, 168, 36000};  // should better be made dynamic input through Database.


        Calendar c = Calendar.getInstance();

        if (isVaccOfVisitCompleted(vaccs)) {
            c.add(Calendar.DATE, Arry[visit]);
        } else {
            c.add(Calendar.DATE, 7);
        }

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        return sdf1.format(c.getTime());

    }

   /* public static long getNextDueDateNew(int visit, String vaccs) {

        int[] Arry = {0, 42, 28, 28, 154, 168, 36000};  // should better be made dynamic input through Database.


        Calendar c = Calendar.getInstance();

        if (isVaccOfVisitCompleted(vaccs)) {
            c.add(Calendar.DATE, Arry[visit]);
        } else {
            c.add(Calendar.DATE, 10);
        }

        // SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
        return c.getTimeInMillis() / 1000;

    }*/


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

    public static byte[] getBytesFromBitmao(Bitmap bmp) {
        ;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();

    }

    public static String getApplicationName(Context context) {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }

    public static int pxToDp(Context context, int px) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics());
       /* DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;*/
    }

    public static String getFortmattedDate(long date) {


        java.util.Date time = new java.util.Date(date * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.US);

        return sdf.format(time);
    }



    public static void sendGAScrenn(Activity c, String screen) {
        HarZindagiApp myApp = (HarZindagiApp) c.getApplication();
        Tracker mTracker = myApp.getDefaultTracker();
        mTracker.setScreenName(screen);

        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    public static void sendGAEvent(Activity c, String category, String action, String lable, long value) {
        if (isTracking) {
            HarZindagiApp myApp = (HarZindagiApp) c.getApplication();
            Tracker mTracker = myApp.getDefaultTracker();
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setAction(action).setLabel(lable).setValue(value)
                    .build());
        }
    }

    public static class GaEvent {

        public static final String LOGIN_TOTAL_TIME = "Login_Total_Time";
        public static final String DOWNLOAD_DATA_TOTAL_TIME = "Download_Data_Total_Time";
        public static final String BACK_NAVIGATION = "Back_Navigation";

        public static final String CARD_WRITE_TIME = "Card_Write_Time";
        public static final String CARD_WRITE_ERROR = "Card_Write_Error";
        public static final String CARD_WRITE_Back = "Card_Write_Back";

        public static final String TAKE_PICTURE_TIME = "Take_Picture_Time";
        public static final String TAKE_PICTURE_ERROR = "Take_Picture_Error";
        public static final String TAKE_PICTURE_BACK = "Take_Picture_Back";

        public static final String EVACCS_TIME = "Evaccs_Time";
        public static final String EVACCS_BACK = "Evaccs_Back";

        public static final String ALL_UC_LIST_TIME = "All_UC_List_Time";
        public static final String ALL_UC_BACK = "All_UC_Back";

        public static final String EVACCS_NON_EPI_TIME = "Evaccs_Time";
        public static final String EVACCS_NON_EPI_BACK = "Evaccs_Back";
        public static final String REGISTER_TOTAL_TIME = "Register_Total_Time";
        public static final String EDIT_REGISTER_TOTAL_TIME = "Edit_Register_Total_Time";
        public static final String REGISTERED_TOTAL_TIME = "Registered_Total_Time";
        public static final String REGISTER_ERROR = "Register_Error";
        public static final String EDIT_REGISTER_ERROR = "Edit_Register_Error";
        public static final String REGISTER_BACK = "Register_Back";
        public static final String REGISTER_FIELD_TIME = "Register_Field_Time";

        public static final String BOOK_TIME = "Book_ID_Time";
        public static final String REGISTER_EPI_TIME = "EPI_Time";
        public static final String REGISTER_CENTER_NAME_TIME = "Center_Name_Time";
        public static final String REGISTER_NAME_TIME = "Register_Name_Time";

        public static final String REGISTER_GUARDIAN_TIME = "Register_Guardian_Time";
        public static final String REGISTER_CNIC_TIME = "Register_CNIC_Time";
        public static final String REGISTER_PHONE_TIME = "Register_Phone_Time";
        public static final String REGISTER_REGION_TIME = "Register_Region_Time";
        public static final String REGISTER_ADDRESS_TIME = "Register_Address_Time";

        public static final String VAC_TIME = "VAC_Time";
        public static final String VAC_BACK = "VAC_Back";

        public static final String KIT_IMAGE_TIME = "KIT_Time";
        public static final String KIT_IMAGE_ERROR = "KIT_Error";
        public static final String KIT_IMAGE_BACK = "KIT_Back";

        public static final String KID_SEARCH_TIME = "Kid_Search_Time";
        public static final String KID_SEARCH_ERROR = "KIT_Error";
        public static final String KID_SEARCH_BACK = "KIT_Back";


    }

}

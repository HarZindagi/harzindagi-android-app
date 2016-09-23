package com.ipal.itu.harzindagi.Application;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.ipal.itu.harzindagi.Entity.Books;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.Evaccs;
import com.ipal.itu.harzindagi.Entity.EvaccsNonEPI;
import com.ipal.itu.harzindagi.Entity.FemaleName;
import com.ipal.itu.harzindagi.Entity.Images;
import com.ipal.itu.harzindagi.Entity.Injections;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Entity.MaleName;
import com.ipal.itu.harzindagi.Entity.Towns;
import com.ipal.itu.harzindagi.Entity.UserInfo;
import com.ipal.itu.harzindagi.Entity.Vaccinations;
import com.ipal.itu.harzindagi.Entity.Visit;
import com.ipal.itu.harzindagi.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Ali on 1/14/2016.
 */
public class HarZindagiApp extends android.app.Application {

    private Tracker mTracker;
    private static HarZindagiApp mInstance;
    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
    public static synchronized HarZindagiApp getInstance() {
        return mInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Jameel_Noori_Nastaleeq.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        mInstance = this;
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        configurationBuilder.addModelClass(ChildInfo.class);
        configurationBuilder.addModelClass(UserInfo.class);
        configurationBuilder.addModelClass(Visit.class);
        configurationBuilder.addModelClass(Injections.class);
        configurationBuilder.addModelClass(Vaccinations.class);
        configurationBuilder.addModelClass(KidVaccinations.class);
        configurationBuilder.addModelClass(Evaccs.class);
        configurationBuilder.addModelClass(EvaccsNonEPI.class);
        configurationBuilder.addModelClass(Towns.class);
        configurationBuilder.addModelClass(Books.class);
        configurationBuilder.addModelClass(MaleName.class);
        configurationBuilder.addModelClass(FemaleName.class);
        configurationBuilder.addModelClass(Images.class);
        ActiveAndroid.initialize(configurationBuilder.create());
        ActiveAndroid.initialize(this);



    }


}

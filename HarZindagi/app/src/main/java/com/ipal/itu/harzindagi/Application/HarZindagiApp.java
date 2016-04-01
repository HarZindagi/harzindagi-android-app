package com.ipal.itu.harzindagi.Application;

import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.Injections;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Entity.UserInfo;
import com.ipal.itu.harzindagi.Entity.Vaccinations;
import com.ipal.itu.harzindagi.Entity.Visit;

/**
 * Created by Ali on 1/14/2016.
 */
public class HarZindagiApp extends android.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        configurationBuilder.addModelClass(ChildInfo.class);
        configurationBuilder.addModelClass(UserInfo.class);
        configurationBuilder.addModelClass(Visit.class);
        configurationBuilder.addModelClass(Injections.class);
        configurationBuilder.addModelClass(Vaccinations.class);
        configurationBuilder.addModelClass(KidVaccinations.class);
        ActiveAndroid.initialize(configurationBuilder.create());
        ActiveAndroid.initialize(this);



    }


}

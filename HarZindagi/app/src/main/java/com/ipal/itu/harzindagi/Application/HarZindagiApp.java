package com.ipal.itu.harzindagi.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by Ali on 1/14/2016.
 */
public class HarZindagiApp extends  com.activeandroid.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}

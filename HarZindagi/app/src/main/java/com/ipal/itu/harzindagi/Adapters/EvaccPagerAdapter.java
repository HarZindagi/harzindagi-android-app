package com.ipal.itu.harzindagi.Adapters;

/**
 * Created by Wahab on 2/17/2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ipal.itu.harzindagi.Fragments.TabFragment5;
import com.ipal.itu.harzindagi.Fragments.TabFragment6;

public class EvaccPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public EvaccPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragment5 tab1 = new TabFragment5();
                return tab1;
            case 1:
                TabFragment6 tab2 = new TabFragment6();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
package com.ipal.itu.harzindagi.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.ipal.itu.harzindagi.Fragments.*;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.ipal.itu.harzindagi.Activities.VaccinationActivity;

/**
 * Created by aliabbasjaffri on 08/07/15.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private  Context context;
    public static ViewPagerAdapter instance;
    public static VaccinationActivity vaccinationActivity;

    private String tabTitles[] = new String[] { "After Birth", "After 6 Weeks", "After 10 Weeks" , "After 14 Weeks" , "After 16 Weeks" , "After 9 Months" };

    public ViewPagerAdapter(FragmentManager fm , Context context , VaccinationActivity activity )
    {
        super(fm);
        this.context = context;
        instance = this;
        vaccinationActivity = activity;
    }


    @Override
    public int getCount() {
        // Returns the number of tabs
        return 6;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                return VaccinationOneFragment.newInstance("" , "");
            case 1:
                return VaccinationTwoFragment.newInstance("", "");
            case 2:
                return VaccinationThreeFragment.newInstance("", "");
            case 3:
                return VaccinationFourFragment.newInstance("", "");
            case 4:
                return VaccinationFiveFragment.newInstance("" , "");
            case 5:
                return VaccinationSixFragment.newInstance("" , "");
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return tabTitles[position];
    }
}
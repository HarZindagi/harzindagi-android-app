package com.ipal.itu.harzindagi.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.ipal.itu.harzindagi.Dao.InjectionsDao;
import com.ipal.itu.harzindagi.Entity.Injections;
import com.ipal.itu.harzindagi.Fragments.*;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ipal.itu.harzindagi.Activities.VaccinationActivity;
import com.ipal.itu.harzindagi.GJson.GInjection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aliabbasjaffri on 08/07/15.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    public static ViewPagerAdapter instance;
    public static VaccinationActivity vaccinationActivity;
    ArrayList<ArrayList<GInjection>> injections = new ArrayList<>();
    public int curr_visit;

    private String tabTitles[] = new String[]{"After Birth", "After 6 Weeks", "After 10 Weeks", "After 14 Weeks", "After 16 Weeks", "After 9 Months"};

    public ViewPagerAdapter(FragmentManager fm, Context context, VaccinationActivity activity, String Vaccs_done, String Curr_visit) {
        super(fm);
        this.context = context;
        instance = this;
        vaccinationActivity = activity;
        String[] done_list = Vaccs_done.split(",");


        curr_visit = Integer.parseInt(Curr_visit);
        ArrayList<GInjection> _injections = new ArrayList<>();
        List<Injections>  arr_inj= InjectionsDao.getInjectionsByVisit(curr_visit);

        for (int i = 0; i < arr_inj.size(); i++) {
            GInjection inj = new GInjection();
            inj.name=arr_inj.get(i).name;
            inj.is_done = Integer.parseInt(done_list[i]);
            inj.is_drop = arr_inj.get(i).is_drop;

            _injections.add(inj);
        }
        for (int i = 0; i < 6; i++) {
            if (curr_visit == i+1) {
                injections.add(_injections);
            } else {
                if(i<curr_visit-1) {
                    ArrayList<GInjection> _inj = new ArrayList<>();
                    List<Injections>  arry_inj= InjectionsDao.getInjectionsByVisit(i+1);
                    for (int j = 0; j <arry_inj.size(); j++) {
                        GInjection inj = new GInjection();
                        inj.name=arry_inj.get(j).name;
                        inj.is_done = 1;
                        inj.is_drop = arry_inj.get(j).is_drop;
                        _inj.add(inj);

                    }
                    injections.add(_inj);
                }else{
                    ArrayList<GInjection> _inj = new ArrayList<>();
                    List<Injections>  arry_inj= InjectionsDao.getInjectionsByVisit(i+1);

                    for (int j = 0; j <arry_inj.size(); j++) {
                        GInjection inj = new GInjection();
                        inj.name=arry_inj.get(j).name;
                        inj.is_done = 0;
                        inj.is_drop = arry_inj.get(j).is_drop;
                        _inj.add(inj);

                    }
                    injections.add(_inj);
                }
            }
        }
    }


    @Override
    public int getCount() {
        // Returns the number of tabs
        return 6;
    }

    @Override
    public Fragment getItem(int position) {

        return VaccinationOneFragment.newInstance(position, injections.get(position),curr_visit);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
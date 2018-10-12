package com.ipal.itu.harzindagi.Fragments;

/**
 * Created by Wahab on 2/17/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ipal.itu.harzindagi.Activities.RegisteredChildActivity;
import com.ipal.itu.harzindagi.Adapters.ChildListAdapter;
import com.ipal.itu.harzindagi.Adapters.EvaccNonEPIListAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.EvaccsNonEPIDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.EvaccsNonEPI;
import com.ipal.itu.harzindagi.R;

import java.util.Calendar;
import java.util.List;

public class TabFragment6 extends Fragment {

    String app_name;
    List<EvaccsNonEPI> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_frag_layout, container, false);

        app_name = getResources().getString(R.string.app_name);

        final ChildInfoDao dao = new ChildInfoDao();
        final Calendar calendar= Calendar.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
               data = EvaccsNonEPIDao.getByflag();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setListView(rootView);
                    }
                });

            }
        }).start();


        return rootView;
    }
    private  void setListView(View rootView){
        if (data.size() != 0) {

            ListView listView = (ListView) rootView.findViewById(R.id.tab_list);
            EvaccNonEPIListAdapter childListAdapter = new EvaccNonEPIListAdapter(getActivity(), R.layout.evacdata_list, data, app_name);
            listView.setAdapter(childListAdapter);

        }
    }
}
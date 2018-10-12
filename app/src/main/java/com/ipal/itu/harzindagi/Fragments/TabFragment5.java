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
import com.ipal.itu.harzindagi.Adapters.EvaccEPIListAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.EvaccsDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.Evaccs;
import com.ipal.itu.harzindagi.R;

import java.util.Calendar;
import java.util.List;

public class TabFragment5 extends Fragment {

    String app_name;
    List<Evaccs> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_frag_layout, container, false);

        app_name = getResources().getString(R.string.app_name);

        final ChildInfoDao dao = new ChildInfoDao();
        final Calendar calendar= Calendar.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                data = EvaccsDao.getByFlag();
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
            EvaccEPIListAdapter childListAdapter = new EvaccEPIListAdapter(getActivity(), R.layout.evacdata_list, data, app_name);
            listView.setAdapter(childListAdapter);

        }
    }
}
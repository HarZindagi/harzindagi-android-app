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
import com.ipal.itu.harzindagi.Activities.VaccinationActivity;
import com.ipal.itu.harzindagi.Adapters.ChildListAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.util.Calendar;
import java.util.List;

public class TabFragment1 extends Fragment {

    String app_name;
    List<ChildInfo> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_frag_layout, container, false);

        app_name = getResources().getString(R.string.app_name);

        final ChildInfoDao dao = new ChildInfoDao();
        final Calendar calendar= Calendar.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                data = dao.getToday(calendar.getTimeInMillis());
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
            ChildListAdapter childListAdapter = new ChildListAdapter(getActivity(), R.layout.listactivity_row, data, app_name);
            listView.setAdapter(childListAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                   /* Intent intent = new Intent(getActivity(), VaccinationActivity.class);

                    Bundle bnd = KidVaccinationDao.get_visit_details_db(data.get(position).mobile_id);
                    intent.putExtra("childid", data.get(position).epi_number);
                    intent.putExtras(bnd);
                    startActivity(intent);*/

                    Intent myintent = new Intent(getActivity(), RegisteredChildActivity.class);
                    myintent.putExtra("childid", data.get(position).kid_id);
                    myintent.putExtra("imei", data.get(position).imei_number);
                    myintent.putExtra("EPIname",data.get(position).epi_name );
                    startActivity(myintent);

                }
            });
        }
    }
}
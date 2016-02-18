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

import com.ipal.itu.harzindagi.Activities.VaccinationActivity;
import com.ipal.itu.harzindagi.Adapters.ChildListAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;

import java.util.List;

public class TabFragment1 extends Fragment {

    String app_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_frag_layout, container, false);

        app_name = getResources().getString(R.string.app_name);

        ChildInfoDao dao = new ChildInfoDao();
        if( dao.getAll() != null ){

            final List<ChildInfo> data= ChildInfoDao.getAll();

            ListView listView = (ListView) rootView.findViewById(R.id.tab_list);
            ChildListAdapter childListAdapter = new ChildListAdapter(getActivity(), R.layout.listactivity_row, data,app_name);
            listView.setAdapter(childListAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
                {    Intent intent=new Intent(getActivity(),VaccinationActivity.class);

                    intent.putExtra("childID",data.get(position).epi_number);
                    startActivity(intent);

                }
            });
        }
        return rootView;
    }
}
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

import java.util.Calendar;
import java.util.List;

public class TabFragment2 extends Fragment {

    String app_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_frag_layout, container, false);

        app_name = getResources().getString(R.string.app_name);

        ChildInfoDao dao = new ChildInfoDao();

        Calendar calendar= Calendar.getInstance();

        final List<ChildInfo> data = dao.getDefaulter(calendar.getTimeInMillis());
        if( data.size()!=0 ){

            ListView listView = (ListView) rootView.findViewById(R.id.tab_list);
            ChildListAdapter childListAdapter = new ChildListAdapter(getActivity(), R.layout.listactivity_row, data,app_name);
            listView.setAdapter(childListAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
                {
                   /* Intent intent=new Intent(getActivity(),VaccinationActivity.class);
                    Bundle bnd= KidVaccinationDao.get_visit_details_db(data.get(position).mobile_id);
                    intent.putExtra("childid",data.get(position).epi_number);
                    intent.putExtras(bnd);
                    startActivity(intent);*/

                    Intent myintent = new Intent(getActivity(), RegisteredChildActivity.class);
                    myintent.putExtra("childid", data.get(position).kid_id);
                    myintent.putExtra("EPIname",data.get(position).epi_name );
                    startActivity(myintent);

                }
            });
        }
        return rootView;
    }
}
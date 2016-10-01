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

import com.ipal.itu.harzindagi.Activities.ChildInfoToday;
import com.ipal.itu.harzindagi.Activities.RegisteredChildActivity;
import com.ipal.itu.harzindagi.Adapters.ChildListAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Dao.VaccinationsDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.VaccInfoList;
import com.ipal.itu.harzindagi.R;


import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TabFragment4 extends Fragment {

    String app_name;
    List<ChildInfo> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_frag_layout, container, false);

        app_name = getResources().getString(R.string.app_name);

        final ChildInfoDao dao = new ChildInfoDao();
        final Calendar calendar= Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date today = new Date(calendar.getTimeInMillis());
        Date todayWithZeroTime=null;
        try {
            todayWithZeroTime = formatter.parse(formatter.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Date finalTodayWithZeroTime = todayWithZeroTime;
        new Thread(new Runnable() {
            @Override
            public void run() {
                data = dao.getTodayCompleted(finalTodayWithZeroTime.getTime());
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
                    Calendar calendar = Calendar.getInstance();
                    VaccInfoList vdb = new VaccInfoList();
                    Bundle bnd = KidVaccinationDao.get_visit_details_db(data.get(position).kid_id);
                    String[] ayy = bnd.getString("vacc_details").toString().split(",");
                    for (int i = 0; i < vdb.vaccinfo.size(); i++) {

                        if (ayy[i].equals("0")) {

                            vdb.vaccinfo.get(i).day = "--";
                            vdb.vaccinfo.get(i).month = "--";
                            vdb.vaccinfo.get(i).year = "--";
                        } else {

                            vdb.vaccinfo.get(i).day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                            vdb.vaccinfo.get(i).month = String.valueOf(calendar.get(Calendar.MONTH));
                            vdb.vaccinfo.get(i).year = String.valueOf(calendar.get(Calendar.YEAR));

                        }

                    }
                    Intent myintent = new Intent(getActivity(), ChildInfoToday.class);

                    VaccinationsDao.get_VaccinationID_Vaccs_details(Integer.parseInt(bnd.getString("visit_num")), bnd.getString("vacc_details"), vdb);
                    myintent.putExtra("visit_num_",Integer.parseInt(bnd.getString("visit_num")));
                    myintent.putExtra("vacc_details",bnd.getString("vacc_details"));
                    for (int i = 0; i < vdb.vaccinfo.size(); i++) {

                        if (ayy[i].equals("0")) {

                            vdb.vaccinfo.get(i).day = "--";
                            vdb.vaccinfo.get(i).month = "--";
                            vdb.vaccinfo.get(i).year = "--";
                        } else {

                            vdb.vaccinfo.get(i).day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                            vdb.vaccinfo.get(i).month = String.valueOf(calendar.get(Calendar.MONTH));
                            vdb.vaccinfo.get(i).year = String.valueOf(calendar.get(Calendar.YEAR));

                        }

                    }
                    myintent.putExtra("VaccDetInfo", vdb);
                    myintent.putExtra("childid", data.get(position).kid_id);
                    myintent.putExtra("imei", data.get(position).imei_number);
                    myintent.putExtra("EPIname",data.get(position).epi_name );
                    myintent.putExtra("bookid",Integer.parseInt(data.get(position).book_id) );
                    myintent.putExtra("isSync", data.get(position).record_update_flag);
                    startActivity(myintent);

                }
            });
        }
    }
}
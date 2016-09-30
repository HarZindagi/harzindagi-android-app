package com.ipal.itu.harzindagi.Fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipal.itu.harzindagi.Activities.CustomCamera;
import com.ipal.itu.harzindagi.Activities.VaccinationActivity;
import com.ipal.itu.harzindagi.Adapters.VaccineListAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Dao.VaccinationsDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.VaccInfoList;
import com.ipal.itu.harzindagi.GJson.GInjection;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VaccinationOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VaccinationOneFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final int CALENDAR_CODE = 100;
    public CheckBox ch;
    boolean isActive = false;
    TextView date_vac;
    Button nxt_tab;
    LinearLayout skip_vst;
    List<ChildInfo> data;
    Button btn;
    String det_vacs;
    String DateOfBirth;
    Calendar calendar;
    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;
    private ArrayList<GInjection> injection;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog;

    public VaccinationOneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pos Parameter 2.
     * @return A new instance of fragment VaccinationOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VaccinationOneFragment newInstance(int pos, ArrayList<GInjection> injection, int curVisit) {
        VaccinationOneFragment fragment = new VaccinationOneFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_PARAM2, pos);
        args.putInt(ARG_PARAM1, curVisit);
        args.putSerializable(ARG_PARAM3, injection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
            injection = (ArrayList<GInjection>) getArguments().getSerializable(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_vaccination_one, container, false);
        btn = (Button) v.findViewById(R.id.btn_v1);
        skip_vst = (LinearLayout) v.findViewById(R.id.skip_visit);
        // have to this dynamic in future

        //List<Injections> data = InjectionsDao.getInjectionsByVisit(1);
        // Injections ij=new Injections();
        //ij.SetInjections(1,"abc","aaaaaa",true);
        //data.add(ij);
        LinearLayout list;
        list = (LinearLayout) v.findViewById(R.id.list_v1);
        ch = (CheckBox) v.findViewById(R.id.dont_care);
        if (mParam2 > mParam1) {

            v.findViewById(R.id.dimmLayout).setVisibility(View.VISIBLE);
            v.findViewById(R.id.dimmLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        final VaccineListAdapter adapter = new VaccineListAdapter(getActivity(), injection, 0, btn, skip_vst,mParam2);

        for (int i = 0; i < injection.size(); i++) {
            adapter.getView(i, null, list);
            if (injection.get(i).is_done == 0) {
                isActive = true;

            }
            if (injection.get(i).is_done == 1) {
                ch.setVisibility(View.GONE);

            }
        }
        // list.setAdapter(adapter);


        date_vac = (TextView) v.findViewById(R.id.date_vac);
        nxt_tab = (Button) v.findViewById(R.id.nxt_tab);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isActive) {
                    det_vacs = adapter.get_vaccs_details();
                    if (!det_vacs.equals("")) {
                        ((VaccinationActivity) getActivity()).logTime();
                        Intent cameraIntent = new Intent(getActivity(), CustomCamera.class);
                        cameraIntent.putExtra("filename", ((VaccinationActivity) getActivity()).fpath);
                        cameraIntent.putExtra("vacc_details", det_vacs);
                        cameraIntent.putExtra("visit_num", (mParam2 + 1) + "");
                        startActivityForResult(cameraIntent, ((VaccinationActivity) getActivity()).CAMERA_REQUEST);
                    }
                }
            }
        });

        if (((VaccinationActivity) getActivity()).isVaccCompleted) {
            ch.setVisibility(View.GONE);
        }

        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    date_vac.setVisibility(View.VISIBLE);
                    if (!date_vac.getText().equals("")) {

                        nxt_tab.setVisibility(View.VISIBLE);
                    }else{
                        nxt_tab.setVisibility(View.GONE);
                    }
                    date_vac.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int mYear, mMonth, mDay;
                            final DateFormat df = new SimpleDateFormat("dd-MM-yyyy",Locale.US);
                            final Calendar c = Calendar.getInstance();
                            mYear = c.get(Calendar.YEAR);
                            mMonth = c.get(Calendar.MONTH);
                            mDay = c.get(Calendar.DAY_OF_MONTH);
                            // final String prvDate = df.format(c.getTime());
                            // Launch Date Picker Dialog
                            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {
                                            try {
                                                Date pDate = df.parse(dayOfMonth + "-"
                                                        + (monthOfYear + 1) + "-" + year);
                                                DateFormat dff = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                                                String dd = dff.format(pDate);
                                                date_vac.setText(dd);
                                                nxt_tab.setVisibility(View.VISIBLE);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            // Display Selected date in textbox


                                        }
                                    }, mYear, mMonth, mDay);
                            dpd.show();

                        }
                    });

                    nxt_tab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String det_vacs = adapter.get_comp_vaccs_details();
                            writeToDB(mParam2 + 1, det_vacs);
                        }
                    });
                } else {
                    ch.setChecked(false);
                    date_vac.setVisibility(View.INVISIBLE);
                    nxt_tab.setVisibility(View.GONE);

                }

            }
        });

        return v;
    }

    public void writeToDB(int visitnum, String vacc_details) {
        VaccInfoList vdb = new VaccInfoList();
        List<Integer> lst = VaccinationsDao.get_VaccinationID_Vaccs_details(visitnum, vacc_details, vdb);
        ChildInfoDao childInfo = new ChildInfoDao();
        data = ChildInfoDao.getByKId(((VaccinationActivity) getActivity()).childID);
        calendar = Calendar.getInstance();
        String imei = Constants.getIMEI(getActivity());
        long time = getLongDate(date_vac.getText().toString());
        for (int i = 0; i < lst.size(); i++) {

            KidVaccinationDao kd = new KidVaccinationDao();


            long kId = 0;
            if (data.get(0).kid_id != null) {
                kId = data.get(0).kid_id;
            } else {
                kId = data.get(0).kid_id;
            }
            if (imei.equals(data.get(0).imei_number)) {
                kd.save(VaccinationActivity.location, kId, (int) lst.get(i), data.get(0).image_path, time, false, data.get(0).imei_number);
            } else {
                kd.save(VaccinationActivity.location, kId, (int) lst.get(i), data.get(0).image_path, time, false, data.get(0).imei_number, imei);
            }

        }
        ((VaccinationActivity) getActivity()).logTime();
        getActivity().finish();
        Bundle b = new Bundle();
        b.putLong("childid", ((VaccinationActivity) getActivity()).childID);
        b.putString("imei", ((VaccinationActivity) getActivity()).imei);
        b.putInt("bookid", ((VaccinationActivity) getActivity()).bookid);
        b.putBoolean("isSync", ((VaccinationActivity) getActivity()).record_sync);
        b.putString("visit_num", (mParam2 + 1 + ""));
        b.putString("vacc_details", vacc_details);
        Intent i = new Intent(getActivity(), VaccinationActivity.class);
        i.putExtras(b);
        getActivity().startActivity(i);
        // getActivity().startActivity(new Intent(getActivity(),VaccinationActivity.class));
    }

    /*  final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
              calendar = Calendar.getInstance();
              calendar.set(Calendar.YEAR, year);
              calendar.set(Calendar.MONTH, monthOfYear);
              calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
              updateLabel();
          }
      };
      private void updateLabel() {
          String myFormat = "dd-MMM-yyyy"; //In which you need put here
          SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
          date_vac.setText(DateOfBirth = sdf.format(calendar.getTime()));
      }*/
/*public void setDateText(String date){

    date_vac.setText(date);
}*/

    public long getLongDate(String date) {
        long longDate = 0;
        DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.US);
        try {
            Date parsedDate = sdf.parse(date);
            longDate = parsedDate.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return longDate;
    }

}

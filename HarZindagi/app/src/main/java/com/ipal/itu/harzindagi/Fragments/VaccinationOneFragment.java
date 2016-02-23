package com.ipal.itu.harzindagi.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ipal.itu.harzindagi.Activities.CustomCamera;
import com.ipal.itu.harzindagi.Activities.VaccinationActivity;
import com.ipal.itu.harzindagi.Adapters.VaccineListAdapter;
import com.ipal.itu.harzindagi.Dao.InjectionsDao;
import com.ipal.itu.harzindagi.Entity.Injections;
import com.ipal.itu.harzindagi.GJson.GInjection;
import com.ipal.itu.harzindagi.R;

import java.util.ArrayList;
import java.util.List;

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

    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;
    private ArrayList<GInjection> injection;

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
    public static VaccinationOneFragment newInstance( int pos,ArrayList<GInjection> injection,int curVisit) {
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
        View v  = inflater.inflate(R.layout.fragment_vaccination_one, container, false);

        // have to this dynamic in future

        List<Injections> data= InjectionsDao.getInjectionsByVisit(1);
       // Injections ij=new Injections();
        //ij.SetInjections(1,"abc","aaaaaa",true);
        //data.add(ij);
        LinearLayout list;
        list = (LinearLayout) v.findViewById(R.id.list_v1);
        if(mParam2>mParam1) {
            v.findViewById(R.id.dimmLayout).setVisibility(View.VISIBLE);
            v.findViewById(R.id.dimmLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
       final VaccineListAdapter adapter = new VaccineListAdapter(getActivity(), injection
               ,0);

        for (int i = 0; i < injection.size(); i++) {
            adapter.getView(i,null,list);

        }
       // list.setAdapter(adapter);


      Button btn=(Button)v.findViewById(R.id.btn_v1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String det_vacs=adapter.get_vaccs_details();

                Intent cameraIntent = new Intent(getActivity(), CustomCamera.class);
                cameraIntent.putExtra("filename",((VaccinationActivity)getActivity()).fpath );
                cameraIntent.putExtra("vacc_details",det_vacs);
                cameraIntent.putExtra("visit_num",(mParam2+1)+"");
                startActivityForResult(cameraIntent,((VaccinationActivity)getActivity()).CAMERA_REQUEST );
            }
        });


        return  v;
    }




}

package com.ipal.itu.harzindagi.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ipal.itu.harzindagi.Activities.CardScanWrite;
import com.ipal.itu.harzindagi.Activities.CustomCamera;
import com.ipal.itu.harzindagi.Activities.VaccinationActivity;
import com.ipal.itu.harzindagi.Adapters.VaccineAdapter;
import com.ipal.itu.harzindagi.Dao.InjectionsDao;
import com.ipal.itu.harzindagi.Entity.Injections;
import com.ipal.itu.harzindagi.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VaccinationOneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VaccinationOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VaccinationOneFragment newInstance(String param1, String param2) {
        VaccinationOneFragment fragment = new VaccinationOneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        ListView list;
        list = (ListView) v.findViewById(R.id.list_v1);
       final VaccineAdapter adapter = new VaccineAdapter(getActivity(),R.layout.vaccinelist_item, data, "Har Zindagi");
        list.setAdapter(adapter);


      Button btn=(Button)v.findViewById(R.id.btn_v1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String det_vacs=adapter.get_vaccs_details();

                Intent cameraIntent = new Intent(getActivity(), CustomCamera.class);
                cameraIntent.putExtra("filename",((VaccinationActivity)getActivity()).fpath );
                cameraIntent.putExtra("vacc_details",det_vacs);
                cameraIntent.putExtra("visit_num","1");
                startActivityForResult(cameraIntent,((VaccinationActivity)getActivity()).CAMERA_REQUEST );
            }
        });


        return  v;
    }




}

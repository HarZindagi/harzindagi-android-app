package com.ipal.itu.harzindagi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipal.itu.harzindagi.GJson.GInjection;
import com.ipal.itu.harzindagi.R;

import java.util.ArrayList;

/**
 * Created by Ali on 1/14/2016.
 */
public class VaccineListAdapter {


    Context context;
    ArrayList<GInjection> data;
    int curr_frag;
    int aarr[];
    Button bn;
    LinearLayout skp_vst;


    public VaccineListAdapter(Context context, ArrayList<GInjection> _injections, int Curr_frag, Button bn,LinearLayout skp_vst) {

        this.context = context;

        this.data = _injections;


        curr_frag = Curr_frag;
        this.bn=bn;
        this.skp_vst=skp_vst;
        aarr=new int[_injections.size()];
        for(int i=0;i<aarr.length;i++)
        {
            aarr[i]=0;

        }

    }


    public String get_vaccs_details() {

        String s = "" + aarr[0];
        for (int i = 1; i < data.size(); i++) {
            s = s + "," + aarr[i];
        }

        return s;
    }
    public String get_comp_vaccs_details() {

        String s = "" + 1;
        for (int i = 1; i < data.size(); i++) {
            s = s + "," + 1;
            data.get(i).is_done=1;
        }

        return s;
    }


    public void getView(final int position, View convertView, LinearLayout parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.vaccinelist_item, null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.inj_name);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.inj_chk);


        if (data.get(position).is_done == 1) {
            cb.setEnabled(false);
            cb.setChecked(true);
            aarr[position]=1;
            // isChecked[position] = 1;
            // Log.e("" + position + "|" + curr_frag, isChecked[position] + "");

        } else {
            cb.setChecked(false);
            // isChecked[position] = 0;
            aarr[position]=0;
            // Log.e("" + position + "|" + curr_frag, isChecked[position] + "");

        }

        final int currentPost = position;
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean _isChecked) {
                if (_isChecked) {
                    data.get(currentPost).is_done = 1;
                    aarr[position]=1;
                    bn.setVisibility(View.VISIBLE);

                    skp_vst.setVisibility(View.GONE);

                } else {

                    data.get(currentPost).is_done = 0;
                    aarr[position]=0;
                    bn.setVisibility(View.GONE);
                    boolean isAllFalse = true;
                    for (int i = 0; i <aarr.length ; i++) {
                        if(aarr[i]==1){
                            isAllFalse = false;
                        }
                    }
                    if(isAllFalse) {
                        skp_vst.setVisibility(View.VISIBLE);
                    }else{
                        bn.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
        tv.setText(data.get(position).name);

        parent.addView(convertView);

    }
}

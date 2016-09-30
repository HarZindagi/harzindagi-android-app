package com.ipal.itu.harzindagi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipal.itu.harzindagi.GJson.GInjection;
import com.ipal.itu.harzindagi.R;

import java.util.ArrayList;

/**
 * Created by Ali on 1/14/2016.
 */
public class VaccineListAdapter {
    private int[][] colr_b_2d={{ R.drawable.rectangle_a,
            R.drawable.rectangle_b,
            R.drawable.rectangle_c},{ R.drawable.rectangle_b,
            R.drawable.rectangle_d, R.drawable.rectangle_e,R.drawable.rectangle_f},{ R.drawable.rectangle_b,
            R.drawable.rectangle_d,
            R.drawable.rectangle_e,R.drawable.rectangle_f},{ R.drawable.rectangle_b, R.drawable.rectangle_d, R.drawable.rectangle_b, R.drawable.rectangle_g},{ R.drawable.rectangle_h},{ R.drawable.rectangle_h}};

    private int[][] chek_box_2d = {{ R.drawable.customdrawablecheckbox_a,
            R.drawable.customdrawablecheckbox_b,
            R.drawable.customdrawablecheckbox_c},{ R.drawable.customdrawablecheckbox_b,
            R.drawable.customdrawablecheckbox_d,
            R.drawable.customdrawablecheckbox_e,R.drawable.customdrawablecheckbox_f},{ R.drawable.customdrawablecheckbox_b,
            R.drawable.customdrawablecheckbox_d,
            R.drawable.customdrawablecheckbox_e,R.drawable.customdrawablecheckbox_f},{ R.drawable.customdrawablecheckbox_b,
            R.drawable.customdrawablecheckbox_d,
            R.drawable.customdrawablecheckbox_b,
            R.drawable.customdrawablecheckbox_g},{R.drawable.customdrawablecheckbox_h},{ R.drawable.customdrawablecheckbox_h}};

    private int[][] boarder_2d={{ R.drawable.a_border,
            R.drawable.b_border,
            R.drawable.c_border},{ R.drawable.b_border,
            R.drawable.d_border,
            R.drawable.e_border,R.drawable.f_border},{ R.drawable.b_border,
            R.drawable.d_border,
            R.drawable.e_border,R.drawable.f_border},{  R.drawable.b_border,
            R.drawable.d_border,
            R.drawable.b_border,
            R.drawable.g_border},{R.drawable.h_border},{   R.drawable.h_border}};

    Context context;
    ArrayList<GInjection> data;
    int curr_frag;
    int aarr[];
    Button bn;
    LinearLayout skp_vst;
    private int[][] colr_2d = {{R.color.a_color,R.color.b_color,R.color.c_color},{ R.color.b_color, R.color.d_color, R.color.e_color,R.color.f_color},{ R.color.b_color, R.color.d_color,
            R.color.e_color,R.color.f_color},{ R.color.b_color, R.color.d_color, R.color.b_color, R.color.g_color},{ R.color.h_color},{ R.color.h_color}};
   /* private int[] colr = {

            R.color.a_color,
            R.color.b_color,
            R.color.c_color,
            R.color.b_color,
            R.color.d_color,
            R.color.e_color,
            R.color.b_color,
            R.color.d_color,
            R.color.e_color,
            R.color.b_color,
            R.color.d_color,
            R.color.b_color,
            R.color.g_color,
            R.color.h_color,
            R.color.h_color


    };*/
    int param;
    public VaccineListAdapter(Context context, ArrayList<GInjection> _injections, int Curr_frag, Button bn,LinearLayout skp_vst,int param) {

        this.context = context;

        this.data = _injections;

this.param=param;
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
LinearLayout name_injc=(LinearLayout)convertView.findViewById(R.id.name_injc);
        TextView tv = (TextView) convertView.findViewById(R.id.inj_name);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.inj_chk);
        final RelativeLayout bodr=(RelativeLayout)convertView.findViewById(R.id.bodr);
        name_injc.setBackgroundResource(colr_2d[param][position]);
        bodr.setBackgroundResource(boarder_2d[param][position]);
        cb.setButtonDrawable(chek_box_2d[param][position]);

        if(data.get(position).is_drop){
            tv.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.droper_one,0);
        }else{
            tv.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.injection_one,0);
        }

        if (data.get(position).is_done == 1) {
            cb.setEnabled(false);
            cb.setChecked(true);
            aarr[position]=1;
            // isChecked[position] = 1;
            // Log.e("" + position + "|" + curr_frag, isChecked[position] + "");
            bodr.setBackgroundResource(colr_b_2d[param][position]);
        } else {
            cb.setChecked(false);
            // isChecked[position] = 0;
            aarr[position]=0;
            bodr.setBackgroundResource(boarder_2d[param][position]);

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
                    bodr.setBackgroundResource(colr_b_2d[param][position]);

                } else {

                    data.get(currentPost).is_done = 0;
                    aarr[position]=0;
                    bn.setVisibility(View.GONE);
                    boolean isAllFalse = true;
                    bodr.setBackgroundResource(boarder_2d[param][position]);
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

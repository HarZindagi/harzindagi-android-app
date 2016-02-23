package com.ipal.itu.harzindagi.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.Injections;
import com.ipal.itu.harzindagi.GJson.GInjection;
import com.ipal.itu.harzindagi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali on 1/14/2016.
 */
public class VaccineListAdapter {


    Context context;
    ArrayList<GInjection> data;
    int curr_frag;

    public VaccineListAdapter(Context context, ArrayList<GInjection> _injections, int Curr_frag) {

        this.context = context;

        this.data = _injections;


        curr_frag = Curr_frag;


    }


    public String get_vaccs_details() {

        String s = "" + data.get(0).is_done;
        for (int i = 1; i < data.size(); i++) {
            s = s + "," + data.get(i).is_done;

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
            // isChecked[position] = 1;
            // Log.e("" + position + "|" + curr_frag, isChecked[position] + "");

        } else {
            cb.setChecked(false);
            // isChecked[position] = 0;

            // Log.e("" + position + "|" + curr_frag, isChecked[position] + "");

        }

        final int currentPost = position;
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean _isChecked) {
                if (_isChecked) {
                    data.get(currentPost).is_done = 1;

                } else {

                    data.get(currentPost).is_done = 0;
                }
            }
        });
        tv.setText(data.get(position).name);

        parent.addView(convertView);

    }
}

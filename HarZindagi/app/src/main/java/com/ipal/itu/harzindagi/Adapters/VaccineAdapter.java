package com.ipal.itu.harzindagi.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.Injections;
import com.ipal.itu.harzindagi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali on 1/14/2016.
 */
public class VaccineAdapter extends BaseAdapter {

    int xmlRowID;
    Context context;
    List<Injections> data;
    String app_name;

    public VaccineAdapter(Context context, int xmlRowID, List<Injections> data, String app_name) {
        super();
        this.context = context;
        this.xmlRowID = xmlRowID;
        this.data = data;
        this.app_name = app_name;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(xmlRowID, null);
        }

        TextView tv=(TextView)convertView.findViewById(R.id.inj_name);
        CheckBox cb=(CheckBox)convertView.findViewById(R.id.inj_chk);

        tv.setText(data.get(position).name);



        return convertView;
    }
}

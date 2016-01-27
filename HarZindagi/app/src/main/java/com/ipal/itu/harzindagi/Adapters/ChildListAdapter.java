package com.ipal.itu.harzindagi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali on 1/14/2016.
 */
public class ChildListAdapter extends BaseAdapter{

    int xmlRowID;
    Context context;
    List<ChildInfo> data;

    public ChildListAdapter( Context context , int xmlRowID , List<ChildInfo> data )
    {
        super();
        this.context = context;
        this.xmlRowID = xmlRowID;
        this.data = data;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(xmlRowID , null);
        }

        TextView childName = (TextView) convertView.findViewById(R.id.listActivityRowChildName);
        TextView guardianName = (TextView) convertView.findViewById(R.id.listActivityRowGuardianName);
        TextView address = (TextView) convertView.findViewById(R.id.listActivityRowAddress);

        childName.setText(data.get(position).name);
        guardianName.setText(data.get(position).fatherName);
        address.setText(data.get(position).address);

        return convertView;
    }
}

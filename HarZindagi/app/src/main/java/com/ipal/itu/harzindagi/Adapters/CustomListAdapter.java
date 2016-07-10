package com.ipal.itu.harzindagi.Adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipal.itu.harzindagi.GJson.VaccineInfo;
import com.ipal.itu.harzindagi.R;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private Context mcontext;
    ArrayList<VaccineInfo> vaccinfo;
    
    public CustomListAdapter(Activity context, ArrayList<VaccineInfo> vaccinfo) {
        mcontext = context;
        this.vaccinfo= vaccinfo;
    }

    @Override
    public int getCount() {
        return vaccinfo.size();
    }

    @Override
    public Object getItem(int position) {
        return vaccinfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mcontext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.listview_item_layout, parent, false);
            holder = new ViewHolder();
            holder.day = (TextView) row.findViewById(R.id.day);
            holder.month = (TextView) row.findViewById(R.id.month);
            holder.year = (TextView) row.findViewById(R.id.year);

            holder.vaccName = (TextView) row.findViewById(R.id.vaccine_name);
            holder.image = (ImageView) row.findViewById(R.id.vaccine_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.day.setText(""+ vaccinfo.get(position).day);
        int foo = Integer.parseInt(vaccinfo.get(position).month);
        holder.month.setText(""+ (foo+1));
        holder.year.setText("" + vaccinfo.get(position).year);

        holder.vaccName.setText(""+ vaccinfo.get(position).vac_name);
       if(vaccinfo.get(position).vac_type==true){
           holder.image.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.dropper));
       }else {
           holder.image.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.syringe)); // Use injection image
       }
        return row;

    }

    static class ViewHolder {
        TextView day;
        TextView month;
        TextView year;


        TextView vaccName;
        ImageView image;
    }
}
package com.ipal.itu.harzindagi.Adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipal.itu.harzindagi.GJson.VaccineInfo;
import com.ipal.itu.harzindagi.R;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private Context mcontext;
    ArrayList<VaccineInfo> vaccinfo;
    private int[][] colr_d={{ R.drawable.rectangle_a,
            R.drawable.rectangle_b,
            R.drawable.rectangle_c},{ R.drawable.rectangle_b,
            R.drawable.rectangle_d, R.drawable.rectangle_e,R.drawable.rectangle_f},{ R.drawable.rectangle_b,
            R.drawable.rectangle_d,
            R.drawable.rectangle_e,R.drawable.rectangle_f},{ R.drawable.rectangle_b, R.drawable.rectangle_d, R.drawable.rectangle_b, R.drawable.rectangle_g},{ R.drawable.rectangle_h},{ R.drawable.rectangle_h}};
    private int[][] colr_dd = {{R.color.a_color,R.color.b_color,R.color.c_color},{ R.color.b_color, R.color.d_color, R.color.e_color,R.color.f_color},{ R.color.b_color, R.color.d_color,
            R.color.e_color,R.color.f_color},{ R.color.b_color, R.color.d_color, R.color.b_color, R.color.g_color},{ R.color.h_color},{ R.color.h_color}};

    int visit_numbr;
    boolean from_visit_list;
    public CustomListAdapter(Activity context, ArrayList<VaccineInfo> vaccinfo,int visit_numbr,boolean from_visit_list) {
        mcontext = context;
        this.vaccinfo= vaccinfo;
        this.visit_numbr=visit_numbr;
       if(!from_visit_list){
           this.visit_numbr = visit_numbr-1;
       }

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
            holder.mm=(LinearLayout)row.findViewById(R.id.mm);
            holder.mm.setBackgroundResource(colr_d[visit_numbr][position]);
            holder.day = (TextView) row.findViewById(R.id.day);
            holder.day.setTextColor(colr_dd[visit_numbr][position]);
            holder.month = (TextView) row.findViewById(R.id.month);
            holder.month.setTextColor(colr_dd[visit_numbr][position]);
            holder.year = (TextView) row.findViewById(R.id.year);
            holder.year.setTextColor(colr_dd[visit_numbr][position]);

            holder.vaccName = (TextView) row.findViewById(R.id.vaccine_name);
            holder.image = (ImageView) row.findViewById(R.id.vaccine_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        if((""+ vaccinfo.get(position).day).equals("--")){
            holder.day.setText("" + vaccinfo.get(position).day);
            holder.month.setText("" + vaccinfo.get(position).month);
            holder.year.setText("" + vaccinfo.get(position).year);
            holder.vaccName.setText(""+ vaccinfo.get(position).vac_name);
        }else {
            holder.day.setText("" + vaccinfo.get(position).day);
            int foo = Integer.parseInt(vaccinfo.get(position).month);
            holder.month.setText("" + (foo + 1));
            holder.year.setText("" + vaccinfo.get(position).year);
            holder.vaccName.setText(""+ vaccinfo.get(position).vac_name);
        }

       if(vaccinfo.get(position).vac_type==true){
           holder.image.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.droper_one));
       }else {
           holder.image.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.injection_one)); // Use injection image
       }
        return row;

    }

    static class ViewHolder {
        TextView day;
        TextView month;
        TextView year;
LinearLayout mm;

        TextView vaccName;
        ImageView image;
    }
}
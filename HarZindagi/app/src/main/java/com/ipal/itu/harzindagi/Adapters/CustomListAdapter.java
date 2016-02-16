package com.ipal.itu.harzindagi.Adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipal.itu.harzindagi.R;

public class CustomListAdapter extends BaseAdapter {
    private Context mcontext;
    
    public CustomListAdapter(Activity context) {
        mcontext = context;
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
            holder.vaccType = (TextView) row.findViewById(R.id.vaccine_type);
            holder.vaccName = (TextView) row.findViewById(R.id.vaccine_name);
            holder.image = (ImageView) row.findViewById(R.id.vaccine_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.day.setText("");
        holder.day.setText("");
        holder.day.setText("");
        holder.day.setText("");
        holder.day.setText("");
     //   holder.image.setBackgroundDrawable();
        return row;

    }

    static class ViewHolder {
        TextView day;
        TextView month;
        TextView year;
        TextView vaccType;
        TextView vaccName;
        ImageView image;
    }
}
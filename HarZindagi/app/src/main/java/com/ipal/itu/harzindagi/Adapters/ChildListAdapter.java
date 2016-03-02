package com.ipal.itu.harzindagi.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;

import java.util.List;

/**
 * Created by Ali on 1/14/2016.
 */
public class ChildListAdapter extends BaseAdapter {

    int xmlRowID;
    Context context;
    List<ChildInfo> data;
    String app_name;

    public ChildListAdapter(Context context, int xmlRowID, List<ChildInfo> data, String app_name) {
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

        TextView childName = (TextView) convertView.findViewById(R.id.listActivityRowChildName);
        TextView guardianName = (TextView) convertView.findViewById(R.id.listActivityRowGuardianName);
        TextView address = (TextView) convertView.findViewById(R.id.listActivityRowAddress);
        ImageView pic = (ImageView) convertView.findViewById(R.id.listActivityRowImage);

        childName.setText(data.get(position).kid_name);
        guardianName.setText(data.get(position).guardian_name);
        String imagePath = "/sdcard/" + app_name + "/" + data.get(position).image_path + ".jpg";
        Bitmap bmp_read = BitmapFactory.decodeFile(imagePath);
        pic.setImageBitmap(bmp_read);

        return convertView;
    }
}

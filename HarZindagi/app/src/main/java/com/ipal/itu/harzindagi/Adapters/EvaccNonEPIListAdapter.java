package com.ipal.itu.harzindagi.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.ChildInfoDelete;
import com.ipal.itu.harzindagi.Entity.Evaccs;
import com.ipal.itu.harzindagi.Entity.EvaccsNonEPI;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ali on 1/14/2016.
 */
public class EvaccNonEPIListAdapter extends BaseAdapter {

    int xmlRowID;
    Context context;
    List<EvaccsNonEPI> data;
    String app_name;

    public EvaccNonEPIListAdapter(Context context, int xmlRowID, List<EvaccsNonEPI> data, String app_name) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(xmlRowID, null);
        }

       // TextView childName = (TextView) convertView.findViewById(R.id.listActivityRowChildName);
        TextView guardianName = (TextView) convertView.findViewById(R.id.listActivityRowGuardianName);
        TextView address = (TextView) convertView.findViewById(R.id.listActivityRowAddress);
        CircleImageView pic = (CircleImageView) convertView.findViewById(R.id.listActivityRowImage);

       /* delte_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder adb = new AlertDialog.Builder(context);

                adb.setTitle("Want To Delete User");


                adb.setIcon(R.drawable.info_circle);


                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        List<KidVaccinations> items=KidVaccinationDao.getById(data.get(position).kid_id);
                        for (int i = 0; i < items.size(); i++) {
                            items.get(i).delete();
                        }
                        ChildInfoDelete childInfoDelete=new ChildInfoDelete();
                        *//*childInfoDelete.setChildInfoDelete(data.get(position).book_id,data.get(position).epi_number,data.get(position).kid_name,data.get(position).gender,data.get(position).date_of_birth,data.get(position).mother_name,data.get(position).guardian_name,data.get(position).guardian_cnic,data.get(position).phone_number,data.get(position).created_timestamp,data.get(position).location,data.get(position).epi_name,data.get(position).kids_station,data.get(position).image_path,data.get(position).nfc_number,data.get(position).image_update_flag,true,data.get(position).child_address,data.get(position).imei_number,data.get(position).next_due_date,data.get(position).next_visit_date);
                        childInfoDelete.save();*//*
                        data.get(position).delete();
                        data.remove(position);

                        notifyDataSetChanged();


                    }
                });


                adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                adb.show();

            }
        });*/

       // childName.setText(data.get(position).name);
        guardianName.setText(data.get(position).vaccination);
        address.setText(data.get(position).epi_no);
        String imagePath = "/sdcard/" + app_name + "/"  + "EvacNonEpi" +"/nonepi_" + Constants.getIMEI(context) + "_"+data.get(position).epi_no + ".jpg";
        File f = new File(imagePath);
        if(f.exists()) {
            Bitmap bmp_read = BitmapFactory.decodeFile(imagePath);
            pic.setImageBitmap(bmp_read);
        }else{
            pic.setImageResource(R.drawable.paceholder);
        }
        return convertView;
    }
}

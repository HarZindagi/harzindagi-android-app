package com.ipal.itu.harzindagi.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ipal.itu.harzindagi.Dao.EvaccsDao;
import com.ipal.itu.harzindagi.Entity.*;
import com.ipal.itu.harzindagi.Entity.Evaccs;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class EvacsEPI extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    FileOutputStream fo;
    String Fpath;
    String app_name;
    String Evac;
    TextView ep_txt_view;
    String epiNumber;
    Context context;
    CheckBox[] v_box;
    ArrayList<Integer> selectedCheckboxes = new ArrayList<Integer>();
    ArrayList<String> chkBox_txt = new ArrayList<String>();
    CheckBox bx_BCG,bx_OPV,bx_OPV1,bx_Pentavalent,bx_Pneumococcal,bx_OPV2,bx_Pentavalent2
            ,bx_Pneumococcal2,bx_OPV3,bx_Pentavalent3,bx_Pneumococcal3,bx_Measles,bx_Measles2;
    /*String[] chkBox_txt = new String[]{"BCG", "OPV-O(Polio)","OPV-1","Pentavalent-1","Pneumococcal-1","OPV-2","Pentavalent-2"
    ,"Pneumococcal-2","OPV-3","Pentavalent-3","Pneumococcal-3","Measles-1","Measles-2"};*/
    Button mahfooz_Karain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evacs_epi2);

        context=this;
        ep_txt_view=(TextView)findViewById(R.id.ep_txt_view);

        bx_BCG=(CheckBox)findViewById(R.id.bx_BCG);
        bx_OPV=(CheckBox)findViewById(R.id.bx_OPV);
        bx_OPV1=(CheckBox)findViewById(R.id.bx_OPV1);
        bx_Pentavalent=(CheckBox)findViewById(R.id.bx_Pentavalent);
        bx_Pneumococcal=(CheckBox)findViewById(R.id.bx_Pneumococcal);
        bx_OPV2=(CheckBox)findViewById(R.id.bx_OPV2);
        bx_Pentavalent2=(CheckBox)findViewById(R.id.bx_Pentavalent2);
        bx_Pneumococcal2=(CheckBox)findViewById(R.id.bx_Pneumococcal2);
        bx_OPV3=(CheckBox)findViewById(R.id.bx_OPV3);
        bx_Pentavalent3=(CheckBox)findViewById(R.id.bx_Pentavalent3);
        bx_Pneumococcal3=(CheckBox)findViewById(R.id.bx_Pneumococcal3);
        bx_Measles=(CheckBox)findViewById(R.id.bx_Measles);
        bx_Measles2=(CheckBox)findViewById(R.id.bx_Measles2);

        mahfooz_Karain=(Button)findViewById(R.id.mahfooz_Karain);

        v_box = new CheckBox[]{ bx_BCG,bx_OPV,bx_OPV1,bx_Pentavalent,bx_Pneumococcal,bx_OPV2,bx_Pentavalent2
                ,bx_Pneumococcal2,bx_OPV3,bx_Pentavalent3,bx_Pneumococcal3,bx_Measles,bx_Measles2};


        mahfooz_Karain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < v_box.length; i++){
                    if(v_box[i].isChecked()){
                        selectedCheckboxes.add(i+1);
                        chkBox_txt.add(v_box[i].getText().toString());
                    }else{
                        selectedCheckboxes.add(0);
                    }
                }

                for (int i = 0; i < selectedCheckboxes.size(); i++) {
                    com.ipal.itu.harzindagi.Entity.Evaccs evaccs = new Evaccs();
                    evaccs.created_timestamp = Calendar.getInstance().getTimeInMillis()/1000;
                    evaccs.epi_number = ep_txt_view.getText().toString();
                    evaccs.kid_name = "";
                    evaccs.is_guest = 0;
                    evaccs.image_path = "image_"+ evaccs.epi_number;
                    evaccs.imei_number = Constants.getIMEI(context);
                    evaccs.image_update_flag = false;
                    evaccs.name_of_guest_kid = "";
                    evaccs.record_update_flag = false;
                    evaccs.vaccination_id =""+selectedCheckboxes.get(i);
                    evaccs.vaccination_name =""+chkBox_txt.get(i);
                    evaccs.save();
                }
            }
        });
    }
    public void opencam(View v)
    {
       /* Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);*/


        Intent cameraIntent = new Intent(EvacsEPI.this, CustomCamera.class);
        cameraIntent.putExtra("image_", ep_txt_view.getText().toString());
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == 1888) {
            CustomCamera.progress.dismiss();
            Bitmap photo, resizedImage;
            readEditTexts();

            Fpath = data.getStringExtra("fpath");
            String path = data.getStringExtra("path");
            photo = BitmapFactory.decodeFile(path);
            resizedImage = getResizedBitmap(photo, 256);
            saveBitmap(resizedImage);




        }
        }
    public void saveBitmap(Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);

        //Create a new file in sdcard folder.
        File f = new File("/sdcard/" + app_name + "/" + Evac + "/" + Fpath + ".jpg");
        try {
            try {
              //  f.mkdir();
                f.createNewFile();
                fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray()); //write the bytes in file
            } finally {
                fo.close(); // remember close the FileOutput
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public void readEditTexts() {
        epiNumber = ep_txt_view.getText().toString();

    }
}

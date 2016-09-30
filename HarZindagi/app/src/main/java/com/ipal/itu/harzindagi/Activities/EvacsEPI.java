package com.ipal.itu.harzindagi.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.LocationAjaxCallback;
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

public class EvacsEPI extends BaseActivity {
    private static final int CAMERA_REQUEST = 1888;
    FileOutputStream fo;
    String Fpath;
    String app_name;
    String Evac="evaccsNonEpiFolder";
    TextView ep_txt_view;
    String epiNumber;
    Context context;
    CheckBox[] v_box;
    TextView toolbar_title;
    ArrayList<Integer> selectedCheckboxes = new ArrayList<Integer>();
    ArrayList<String> chkBox_txt = new ArrayList<String>();
    CheckBox bx_BCG,bx_OPV,bx_OPV1,bx_Pentavalent,bx_Pneumococcal,bx_OPV2,bx_Pentavalent2
            ,bx_Pneumococcal2,bx_OPV3,bx_Pentavalent3,bx_Pneumococcal3,bx_Measles,bx_Measles2;
    /*String[] chkBox_txt = new String[]{"BCG", "OPV-O(Polio)","OPV-1","Pentavalent-1","Pneumococcal-1","OPV-2","Pentavalent-2"
    ,"Pneumococcal-2","OPV-3","Pentavalent-3","Pneumococcal-3","Measles-1","Measles-2"};*/
    Button mahfooz_Karain;
    private long activityTime;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void logTime(){
        activityTime = (Calendar.getInstance().getTimeInMillis() / 1000) - activityTime;
        Constants.logTime(this,activityTime,Constants.GaEvent.EVACCS_TIME);
        //Constants.sendGAEvent(this,Constants.getUserName(this), Constants.GaEvent.EVACCS_TIME, activityTime + " S", 0);

    }

    @Override
    public void onBackPressed() {

        Constants.sendGAEvent(this,Constants.getUserName(this), Constants.GaEvent.BACK_NAVIGATION,Constants.GaEvent.EVACCS_BACK , 0);
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTime = Calendar.getInstance().getTimeInMillis() / (1000);
        getLocation();
        setContentView(R.layout.activity_evacs_epi2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title=(TextView)findViewById(R.id.toolbar_title);
        toolbar_title.setText("Evaccs-II");
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
                    }
                }

                for (int i = 0; i < selectedCheckboxes.size(); i++) {
                    com.ipal.itu.harzindagi.Entity.Evaccs evaccs = new Evaccs();
                    evaccs.imei_number = Constants.getIMEI(context);
                    evaccs.location = location;
                    evaccs.location_source = location;
                    evaccs.created_timestamp = Calendar.getInstance().getTimeInMillis()/1000;
                    evaccs.epi_number = ep_txt_view.getText().toString();
                    evaccs.vaccination =""+chkBox_txt.get(i);
                    evaccs.record_update_flag = false;

                    //evaccs.kid_name = "";
                    //evaccs.is_guest = 0;
                    //evaccs.image_path = "image_"+ evaccs.epi_number;

                    //evaccs.image_update_flag = false;
                    //evaccs.name_of_guest_kid = "";

                   // evaccs.vacc_id =""+selectedCheckboxes.get(i);


                    evaccs.save();
                }
                logTime();
                finish();
            }
        });
    }
    public void opencam(View v)
    {
       /* Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);*/


        Intent cameraIntent = new Intent(EvacsEPI.this, CustomCamerEvacEPI.class);
        cameraIntent.putExtra("filename",  "epi_"+ Constants.getIMEI(this)+"_"+ep_txt_view.getText().toString());

        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == 1888) {
            if(CustomCamerEvacEPI.progress!=null){
                CustomCamerEvacEPI.progress.dismiss();
            }
            Bitmap photo, resizedImage;
            readEditTexts();

            Fpath = data.getStringExtra("fpath");
            String path = data.getStringExtra("path");
            photo = BitmapFactory.decodeFile(path);
            resizedImage = getResizedBitmap(photo, 256);
            saveBitmap(resizedImage);
           ImageView img_cam=(ImageView)findViewById(R.id.img_cam);
            img_cam.setImageBitmap(photo);




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
    private void getLocation() {

        LocationAjaxCallback cb = new LocationAjaxCallback();
        //  final ProgressDialog pDialog = new ProgressDialog(this);
        //  pDialog.setMessage("Getting Location");

        cb.weakHandler(this, "locationCb").timeout(20 * 1000).expire(1000*30*5).async(this);
        //  pDialog.setCancelable(false);
        //  pDialog.show();
    }
    String location = "0.00000,0.00000";
    public void locationCb(String url, final Location loc, AjaxStatus status) {

        if (loc != null) {

            double lat = loc.getLatitude();
            double log = loc.getLongitude();
            location = lat + "," + log;

        }
    }
}

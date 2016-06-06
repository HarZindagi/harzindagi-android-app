package com.ipal.itu.harzindagi.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.LocationAjaxCallback;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EvacsNonEPI extends BaseActivity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int CALENDAR_CODE = 100;
    EditText Non_Epi_reg_num_txt, Non_Epi_reg_cnic_txt, Non_Epi_phone_num_txt, Non_Epi_number_txt, Non_Epi_adress_txt, Non_Epi_birth_place_txt;
    FileOutputStream fo;
    TextView Non_Epi_reg_date_birth_txt;
    String Fpath;
    String app_name;
    String evaccsNonEpiFolder = "EvacNonEpi";
    Button non_mahfooz_Karain;
    CheckBox[] nonEPIv_box;
    String non_epiReg_Number;
    String non_epiName;
    String child_typ;
    EditText non_Epi_name;
    Context context;
    ArrayList<Integer> selectedCheckboxes_nonEPI = new ArrayList<Integer>();
    ArrayList<String> nonEPI_chkBox_txt = new ArrayList<String>();
    CheckBox non_bx_BCG, non_bx_OPV, non_bx_OPV1, non_bx_Pentavalent, non_bx_Pneumococcal, non_bx_OPV2, non_bx_Pentavalent2, non_bx_Pneumococcal2, non_bx_OPV3, non_bx_Pentavalent3, non_bx_Pneumococcal3, non_bx_Measles, non_bx_Measles2, child_type;
    String location = "0.00000,0.00000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_evacs_epi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        Non_Epi_reg_num_txt = (EditText) findViewById(R.id.Non_Epi_reg_num_txt);
        non_Epi_name = (EditText) findViewById(R.id.non_ep_txt_view);

        Non_Epi_reg_cnic_txt = (EditText) findViewById(R.id.Non_Epi_reg_cnic_txt);
        Non_Epi_phone_num_txt = (EditText) findViewById(R.id.Non_Epi_phone_num_txt);
        Non_Epi_number_txt = (EditText) findViewById(R.id.Non_Epi_number_txt);
        Non_Epi_reg_date_birth_txt = (TextView) findViewById(R.id.Non_Epi_reg_date_birth_txt);
        Non_Epi_adress_txt = (EditText) findViewById(R.id.Non_Epi_adress_txt);
        Non_Epi_birth_place_txt = (EditText) findViewById(R.id.Non_Epi_birth_place_txt);

        child_type = (CheckBox) findViewById(R.id.child_type);
        if (child_type.isChecked()) {
            child_typ = "1";
        } else {
            child_typ = "0";
        }
        non_bx_BCG = (CheckBox) findViewById(R.id.non_bx_BCG);
        non_bx_OPV = (CheckBox) findViewById(R.id.non_bx_OPV);
        non_bx_OPV1 = (CheckBox) findViewById(R.id.non_bx_OPV1);
        non_bx_Pentavalent = (CheckBox) findViewById(R.id.non_bx_Pentavalent);
        non_bx_Pneumococcal = (CheckBox) findViewById(R.id.non_bx_Pneumococcal);
        non_bx_OPV2 = (CheckBox) findViewById(R.id.non_bx_OPV2);
        non_bx_Pentavalent2 = (CheckBox) findViewById(R.id.non_bx_Pentavalent2);
        non_bx_Pneumococcal2 = (CheckBox) findViewById(R.id.non_bx_Pneumococcal2);
        non_bx_OPV3 = (CheckBox) findViewById(R.id.non_bx_OPV3);
        non_bx_Pentavalent3 = (CheckBox) findViewById(R.id.non_bx_Pentavalent3);
        non_bx_Pneumococcal3 = (CheckBox) findViewById(R.id.non_bx_Pneumococcal3);
        non_bx_Measles = (CheckBox) findViewById(R.id.non_bx_Measles);
        non_bx_Measles2 = (CheckBox) findViewById(R.id.non_bx_Measles2);
        non_mahfooz_Karain = (Button) findViewById(R.id.non_Mahfooz_Karain);
        nonEPIv_box = new CheckBox[]{non_bx_BCG, non_bx_OPV, non_bx_OPV1, non_bx_Pentavalent, non_bx_Pneumococcal, non_bx_OPV2, non_bx_Pentavalent2
                , non_bx_Pneumococcal2, non_bx_OPV3, non_bx_Pentavalent3, non_bx_Pneumococcal3, non_bx_Measles, non_bx_Measles2};

        Non_Epi_reg_date_birth_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EvacsNonEPI.this, CalenderActivity.class);
                startActivityForResult(intent, CALENDAR_CODE);
            }
        });
        non_mahfooz_Karain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < nonEPIv_box.length; i++) {
                    if (nonEPIv_box[i].isChecked()) {
                        selectedCheckboxes_nonEPI.add(i + 1);
                        nonEPI_chkBox_txt.add(nonEPIv_box[i].getText().toString());
                    }
                }
                for (int i = 0; i < selectedCheckboxes_nonEPI.size(); i++) {
                    com.ipal.itu.harzindagi.Entity.EvaccsNonEPI evaccsNonepi = new com.ipal.itu.harzindagi.Entity.EvaccsNonEPI();

                    evaccsNonepi.imei_number = Constants.getIMEI(context);
                    evaccsNonepi.location = location;
                    evaccsNonepi.location_source = location;
                    evaccsNonepi.created_timestamp = Calendar.getInstance().getTimeInMillis() / 1000;
                    evaccsNonepi.child_type = child_typ;
                    evaccsNonepi.name = non_Epi_name.getText().toString();
                    evaccsNonepi.daily_reg_no = Non_Epi_reg_num_txt.getText().toString();
                    evaccsNonepi.cnic = Non_Epi_reg_cnic_txt.getText().toString();
                    evaccsNonepi.phone_number = Non_Epi_phone_num_txt.getText().toString();
                    evaccsNonepi.epi_no = Non_Epi_number_txt.getText().toString();
                    String dt = Non_Epi_reg_date_birth_txt.getText().toString();
                    DateFormat dfm = new SimpleDateFormat("dd-MMM-yyyy");
                    Date date = null;
                    try {
                        date = dfm.parse(dt);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dfm.getCalendar().setTime(date);
                    evaccsNonepi.date_of_birth = (date.getTime() / 1000);//Calendar.getInstance().getTimeInMillis() / 1000;//Integer.parseInt(Non_Epi_birth_place_txt.getText().toString());
                    evaccsNonepi.child_address = Non_Epi_adress_txt.getText().toString();
                    evaccsNonepi.birth_place = Non_Epi_birth_place_txt.getText().toString();
                    // evaccsNonepi.is_guest = 1;
                    // evaccsNonepi.image_path = "image_"+ evaccs.epi_number;

                    // evaccsNonepi.image_update_flag = false;
                    //evaccsNonepi.name_of_guest_kid = non_Epi_name.getText().toString();
                    evaccsNonepi.record_update_flag = false;
                    // evaccsNonepi.vacc_id =""+selectedCheckboxes_nonEPI.get(i);


                    evaccsNonepi.save();
                }
                finish();
            }
        });


    }

    public void opencam(View v) {
        Intent cameraIntent = new Intent(EvacsNonEPI.this, CustomCamerEvacNonEPI.class);
        cameraIntent.putExtra("filename", "nonepi_" + Constants.getIMEI(this) + "_" + Non_Epi_number_txt.getText().toString());

        startActivityForResult(cameraIntent, CAMERA_REQUEST);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == 1888) {
            if (CustomCamerEvacNonEPI.progress != null) {
                CustomCamerEvacNonEPI.progress.dismiss();
            }
            Bitmap photo, resizedImage;
            readEditTexts();
            Fpath = data.getStringExtra("fpath");
            String path = data.getStringExtra("path");
            photo = BitmapFactory.decodeFile(path);
            resizedImage = getResizedBitmap(photo, 256);
            saveBitmap(resizedImage);

        }
        if (requestCode == CALENDAR_CODE && resultCode == 100) {
            String year = data.getStringExtra("year");
            String month = data.getStringExtra("month");
            String day = data.getStringExtra("day");
            Non_Epi_reg_date_birth_txt.setText("" + day + "-" + month + "-" + year);
        }
    }

    public void saveBitmap(Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);

        //Create a new file in sdcard folder.
        File f = new File("/sdcard/" + app_name + "/" + evaccsNonEpiFolder + "/" + Fpath + ".jpg");
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
        non_epiReg_Number = Non_Epi_reg_num_txt.getText().toString();
        non_epiName = non_Epi_name.getText().toString();

    }

    private void getLocation() {

        LocationAjaxCallback cb = new LocationAjaxCallback();
        //  final ProgressDialog pDialog = new ProgressDialog(this);
        //  pDialog.setMessage("Getting Location");

        cb.weakHandler(this, "locationCb").timeout(20 * 1000).expire(1000 * 30 * 5).async(this);
        //  pDialog.setCancelable(false);
        //  pDialog.show();
    }

    public void locationCb(String url, final Location loc, AjaxStatus status) {

        if (loc != null) {

            double lat = loc.getLatitude();
            double log = loc.getLongitude();
            location = lat + "," + log;


        }
    }
}

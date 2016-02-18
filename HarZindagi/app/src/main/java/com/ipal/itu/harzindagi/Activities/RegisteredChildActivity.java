package com.ipal.itu.harzindagi.Activities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;

import java.util.List;

public class RegisteredChildActivity extends AppCompatActivity {

    TextView ucNumber;
    TextView epiCenterName;
    TextView childName;
    TextView Gender;
    TextView DOB;
    TextView motherName;
    TextView guardianName;
    TextView guardianCNIC;
    TextView guardianMobileNumber;
    ImageView childPic;
    String app_name;
    Button NFC_Write;
    double longitude;
    double latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_registered_child);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ucNumber = (TextView) findViewById(R.id.ChildUCNumber);
        epiCenterName = (TextView) findViewById(R.id.ChildEPICenterName);
        childName = (TextView) findViewById(R.id.ChildName);
        DOB = (TextView) findViewById(R.id.ChildDOB);
        Gender = (TextView) findViewById(R.id.ChildGender);
        motherName = (TextView) findViewById(R.id.ChildMotherName);
        guardianName = (TextView) findViewById(R.id.ChildGuardianName);
        guardianCNIC = (TextView) findViewById(R.id.ChildGuardianCNIC);
        guardianMobileNumber = (TextView) findViewById(R.id.ChildGuardianMobileNumber);
        childPic = (ImageView) findViewById(R.id.ChildPic);


       Bundle bundle = getIntent().getExtras();
        String childID = bundle.getString("childid");

        NFC_Write= (Button) findViewById(R.id.NFCWrite);
        NFC_Write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Write on NFC Card", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(RegisteredChildActivity.this, VaccinationActivity.class));
            }
        });



        List<ChildInfo> data = ChildInfoDao.getById(childID);

        if(data!=null) {
            ucNumber.setText("" + "203");
            epiCenterName.setText("" + data.get(0).epi_name);
            childName.setText("" + data.get(0).kid_name);


            Gender.setText("Female");
            if (data.get(0).gender == 1)
                Gender.setText("Male");


            DOB.setText(data.get(0).date_of_birth);
            motherName.setText(data.get(0).mother_name);
            guardianName.setText(data.get(0).guardian_name);
            guardianCNIC.setText(data.get(0).guardian_cnic);
            guardianMobileNumber.setText(data.get(0).phone_number);
            app_name = getResources().getString(R.string.app_name);
            String imagePath = "/sdcard/" + app_name + "/" + data.get(0).image_name + ".jpg";
            Bitmap bmp_read = BitmapFactory.decodeFile(imagePath);
            childPic.setImageBitmap(bmp_read);
        }
        else
        {

            Toast.makeText(this, "No Record Found", Toast.LENGTH_LONG).show();

        }

    }


}

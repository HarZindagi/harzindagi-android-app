package com.ipal.itu.harzindagi.Activities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
        String childID = bundle.getString("ID");
        List<ChildInfo> data = ChildInfoDao.getChild(childID);
        ucNumber.setText("" + data.get(0).UCNumber);
        epiCenterName.setText("" + data.get(0).EPICenterName);
        childName.setText("" + data.get(0).name);
        Gender.setText(""+ data.get(0).gender);
        DOB.setText("" + data.get(0).dob);
        motherName.setText("" + data.get(0).motherName);
        guardianName.setText("" + data.get(0).fatherName);
        guardianCNIC.setText("" + data.get(0).cnic);
        guardianMobileNumber.setText("" + data.get(0).phoneNumber);
        app_name = getResources().getString(R.string.app_name);
        String imagePath = "/sdcard/" + app_name + "/" + data.get(0).ChildID + ".jpg";
        Bitmap bmp_read = BitmapFactory.decodeFile(imagePath);
        childPic.setImageBitmap(bmp_read);
    }
}
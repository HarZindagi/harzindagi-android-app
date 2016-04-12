package com.ipal.itu.harzindagi.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.util.Calendar;
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
    Button vaccination_btn,editChild;
    double longitude;
    double latitude;
    ChildInfoDao dao;
    Calendar calendar;
    String childID;
   final Context curr=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_registered_child);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dao = new ChildInfoDao();

        calendar= Calendar.getInstance();
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
        editChild=(Button)findViewById(R.id.edit_child);
        editChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(RegisteredChildActivity.this,EditRegisterChildActivity.class);
                intent.putExtra("epiNumber",childID);
                startActivity(intent);
                finish();
            }
        });

       Bundle bundle = getIntent().getExtras();
       childID = bundle.getString("childid");

        vaccination_btn = (Button) findViewById(R.id.NFCWrite);
        vaccination_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Snackbar.make(v, "Write on NFC Card", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
               /* Intent myintent = new Intent(curr, VaccinationActivity.class);

                myintent.putExtra("childid", childID);
                startActivity(myintent);*/
                final List<ChildInfo> data = dao.getByEPINum(childID);
                Intent intent = new Intent(curr, VaccinationActivity.class);
                long kid = 0;
                if(data.get(0).kid_id!=null){
                    kid = data.get(0).kid_id;
                }else{
                    kid = data.get(0).mobile_id;
                }
                Bundle bnd= KidVaccinationDao.get_visit_details_db(kid);
                intent.putExtra("childid", data.get(0).epi_number);
                intent.putExtras(bnd);
                startActivity(intent);
                finish();
            }
        });

        ChildInfoDao childInfoDao = new ChildInfoDao();

        List<ChildInfo> data = childInfoDao.getByEPINum(childID);

        if(data!=null) {
            ucNumber.setText("" + Constants.getUCID(this));
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
            String imagePath = "/sdcard/" + app_name + "/" + data.get(0).image_path + ".jpg";
            Bitmap bmp_read = BitmapFactory.decodeFile(imagePath);
            childPic.setImageBitmap(bmp_read);
        }
        else
        {

            Toast.makeText(this, "No Record Found", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == android.R.id.home) {
           // startActivity(new Intent(getApplication(),RegisterChildActivity.class).putExtra("epiNumber",childID));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

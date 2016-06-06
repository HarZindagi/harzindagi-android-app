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

import java.util.Calendar;
import java.util.List;

public class RegisteredChildActivity extends AppCompatActivity {

    final Context curr = this;
    TextView ChildEPINumber;
    TextView ChildBookNumberText;
    TextView childName;
    TextView Gender;
    TextView DOB;
    TextView motherName;
    TextView guardianName;
    TextView guardianCNIC;
    TextView guardianMobileNumber;
    ImageView childPic;
    String app_name;
    Button vaccination_btn, editChild;
    double longitude;
    double latitude;
    ChildInfoDao dao;
    Calendar calendar;
    long childID;
    private int bookid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_registered_child);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dao = new ChildInfoDao();

        calendar = Calendar.getInstance();
        ChildEPINumber = (TextView) findViewById(R.id.ChildEpiNumberText);
        ChildBookNumberText = (TextView) findViewById(R.id.ChildBookNumber);
        childName = (TextView) findViewById(R.id.ChildName);
        DOB = (TextView) findViewById(R.id.ChildDOB);
        Gender = (TextView) findViewById(R.id.ChildGender);
        motherName = (TextView) findViewById(R.id.ChildMotherName);
        guardianName = (TextView) findViewById(R.id.ChildGuardianName);
        guardianCNIC = (TextView) findViewById(R.id.ChildGuardianCNIC);
        guardianMobileNumber = (TextView) findViewById(R.id.ChildGuardianMobileNumber);
        childPic = (ImageView) findViewById(R.id.ChildPic);
        editChild = (Button) findViewById(R.id.edit_child);
        editChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisteredChildActivity.this, EditRegisterChildActivity.class);
                intent.putExtra("childid", childID);
                startActivity(intent);
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        childID = bundle.getLong("childid");
        final String imei = bundle.getString("imei");
        if(getIntent().hasExtra("bookid")){
            bookid = bundle.getInt("bookid");
        }else{
            bookid=0;
        }
        vaccination_btn = (Button) findViewById(R.id.NFCWrite);
        vaccination_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Snackbar.make(v, "Write on NFC Card", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
               /* Intent myintent = new Intent(curr, VaccinationActivity.class);

                myintent.putExtra("childid", childID);
                startActivity(myintent);*/
                final List<ChildInfo> data = ChildInfoDao.getByKIdAndIMEI(childID, imei);
                Intent intent = new Intent(curr, VaccinationActivity.class);
                long kid = 0;
                if (data.get(0).kid_id != null) {
                    kid = data.get(0).kid_id;
                } else {
                    finish();
                    return;
                }
                Bundle bnd = KidVaccinationDao.get_visit_details_db(kid);
                intent.putExtra("childid", data.get(0).kid_id);
                intent.putExtra("imei", data.get(0).imei_number);
                intent.putExtra("isSync", data.get(0).record_update_flag);
                intent.putExtra("bookid",bookid);

                intent.putExtras(bnd);
                startActivity(intent);
                finish();
            }
        });

        List<ChildInfo> data = ChildInfoDao.getByKId(childID);

        if (data != null) {
            ChildEPINumber.setText("" + data.get(0).epi_number);
            ChildBookNumberText.setText("" + data.get(0).book_id);
            childName.setText("" + data.get(0).kid_name);


            Gender.setText("لڑکی");
            if (data.get(0).gender == 1)
                Gender.setText("لڑکا");


            DOB.setText(data.get(0).date_of_birth);
            motherName.setText(data.get(0).mother_name);
            guardianName.setText(data.get(0).guardian_name);
            guardianCNIC.setText(data.get(0).guardian_cnic);
            guardianMobileNumber.setText(data.get(0).phone_number);
            app_name = getResources().getString(R.string.app_name);
            String imagePath = "/sdcard/" + app_name + "/" + data.get(0).image_path + ".jpg";
            Bitmap bmp_read = BitmapFactory.decodeFile(imagePath);
            childPic.setImageBitmap(bmp_read);
        } else {

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

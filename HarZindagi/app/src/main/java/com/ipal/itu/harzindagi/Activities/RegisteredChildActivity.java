package com.ipal.itu.harzindagi.Activities;

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
        NFC_Write= (Button) findViewById(R.id.NFCWrite);
        NFC_Write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Write on NFC Card", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(RegisteredChildActivity.this, Card_Scan.class));
            }
        });

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

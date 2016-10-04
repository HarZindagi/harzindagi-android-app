package com.ipal.itu.harzindagi.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ipal.itu.harzindagi.Adapters.CustomListAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.VaccInfoList;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChildInfoToday extends BaseActivity {

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
    TextView nxtDatetxt;
    CircleImageView childPic;
    String app_name;
    Button  editChild;
    double longitude;
    double latitude;
    ChildInfoDao dao;
    Calendar calendar;
    long childID;
    private int bookid;
    TextView toolbar_title;
    long activityTime;
    ListView list;
    int visitNumber;
    CustomListAdapter adapter;
    VaccInfoList obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_child_info);
        activityTime = Calendar.getInstance().getTimeInMillis() / (1000);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("بچے کی معلومات");
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
        childPic = (CircleImageView) findViewById(R.id.ChildPic);
        nxtDatetxt = (TextView) findViewById(R.id.nxtDatetxt);
        visitNumber = getIntent().getExtras().getInt("visit_num_");
        list = (ListView) findViewById(R.id.list);
        obj = (VaccInfoList) getIntent().getSerializableExtra("VaccDetInfo");
        ViewGroup.LayoutParams params = list.getLayoutParams();
        Resources r = getResources();
        int px =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
        params.height = obj.vaccinfo.size()*px;
        list.setLayoutParams(params);
        list.requestLayout();
        adapter = new CustomListAdapter(this, obj.vaccinfo,visitNumber);
        list.setAdapter(adapter);
        Bundle bundle = getIntent().getExtras();
        childID = bundle.getLong("childid");
        final String imei = bundle.getString("imei");
        if (getIntent().hasExtra("bookid")) {
            bookid = bundle.getInt("bookid");
        } else {
            bookid = 0;
        }
        editChild = (Button) findViewById(R.id.edit_child);
        editChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChildInfoToday.this, EditRegisterChildActivity.class);
                intent.putExtra("childid", childID);
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
            String date_String = Constants.getNextDueDate((getIntent().getExtras().getInt("visit_num_")),getIntent().getExtras().getString("vacc_details")); // index wise it is correct

             nxtDatetxt.setText(date_String);

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

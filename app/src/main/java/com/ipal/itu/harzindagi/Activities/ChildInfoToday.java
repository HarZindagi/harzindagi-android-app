package com.ipal.itu.harzindagi.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ipal.itu.harzindagi.Adapters.CustomListAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.Books;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Entity.VaccInfoList;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    String compelte1="مبارک ہو آپ کےبچے کا حفاظتی ٹیکوں کا کورس مکمل ہو چکا ہے";
    String compelte2="برائے مہربانی اس ٹیکے کے بعد یاد سے ویکسینیٹر سے اپنےبچےکا";
    String compelte3=" سرٹیفیکیٹ برائے تکمیل حفاظتی ٹیکہ جات";
    String compelte4="ہمراہ لیتے جائیں";

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
        if(visitNumber<6) {
            adapter = new CustomListAdapter(this, obj.vaccinfo, visitNumber,false);
            list.setAdapter(adapter);

        }else{
            ((TextView)findViewById(R.id.six_time)).setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
            ((TextView)findViewById(R.id.six_time)).setText(compelte1 + compelte2 + "\"" + compelte3 + "\"" + compelte4);
        }
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
                if (imei.equals( Constants.getIMEI(ChildInfoToday.this))) {

                    Intent intent = new Intent(ChildInfoToday.this, EditRegisterChildActivity.class);
                    intent.putExtra("childid", childID);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(ChildInfoToday.this, "Child From Other UC Not Editable", Toast.LENGTH_LONG).show();
                }
            }
        });
        Button delte_child=(Button) findViewById(R.id.delte_child);
        delte_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder adb = new AlertDialog.Builder(ChildInfoToday.this);

                adb.setTitle("کیاآپ بچے کاڈیٹا ختم کرناچاہتے ہیں؟");


                adb.setIcon(R.drawable.info_circle);


                adb.setPositiveButton("ہاں", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final List<ChildInfo> data = ChildInfoDao.getByKIdAndIMEI(childID, imei);
                        List<KidVaccinations> items= KidVaccinationDao.getById(data.get(0).kid_id);
                        List<Books>bookses=Books.getByBookId(Integer.parseInt(data.get(0).book_id));
                        for (int i = 0; i < items.size(); i++) {
                            items.get(i).delete();
                        }

                        if (data.size()>0)
                        {
                            if(bookses.size()>0)
                            {
                                bookses.get(0).delete();
                            }
                            data.get(0).delete();

                            Intent in=new Intent(ChildInfoToday.this,DashboardActivity.class);
                            startActivity(in);
                            finish();

                        }


                    }
                });


                adb.setNegativeButton("نہیں", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                adb.show();

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
          //  String date_String = Constants.getNextDueDate((getIntent().getExtras().getInt("visit_num_")),getIntent().getExtras().getString("vacc_details")); // index wise it is correct
            String dateString = new SimpleDateFormat("dd-MMM-yyyy").format(new Date(data.get(0).next_due_date));
             nxtDatetxt.setText(dateString);

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

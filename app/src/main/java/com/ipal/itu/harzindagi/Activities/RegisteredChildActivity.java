package com.ipal.itu.harzindagi.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.ipal.itu.harzindagi.Entity.Books;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.ChildInfoDelete;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisteredChildActivity extends BaseActivity {

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
    CircleImageView childPic;
    String app_name;
    Button vaccination_btn, editChild;
    double longitude;
    double latitude;
    ChildInfoDao dao;
    Calendar calendar;
    long childID;
    private int bookid;
    TextView toolbar_title;
    long activityTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_registered_child);
        activityTime = Calendar.getInstance().getTimeInMillis() / (1000);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("رجسٹرڈ معلومات");
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
        editChild = (Button) findViewById(R.id.edit_child);


        final Bundle bundle = getIntent().getExtras();
        childID = bundle.getLong("childid");
        final String imei = bundle.getString("imei");
        if (getIntent().hasExtra("bookid")) {
            bookid = bundle.getInt("bookid");
        } else {
            bookid = 0;
        }
        editChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imei.equals(Constants.getIMEI(RegisteredChildActivity.this))) {
                    Intent intent = new Intent(RegisteredChildActivity.this, EditRegisterChildActivity.class);
                    intent.putExtra("childid", childID);
                    startActivity(intent);
                    finish();
                    activityTime = (Calendar.getInstance().getTimeInMillis() / 1000) - activityTime;
                    Constants.logTime(RegisteredChildActivity.this, activityTime, Constants.GaEvent.REGISTERED_TOTAL_TIME);

                } else {

                    Toast.makeText(RegisteredChildActivity.this, "Child From Other UC Not Editable", Toast.LENGTH_LONG).show();
                }
            }
        });

        /*    new Thread(new Runnable() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final List<ChildInfoDelete> childInfoDelete = dao.getAlll();
                                if (childInfoDelete.size() > 0) {
                                    Toast.makeText(RegisteredChildActivity.this, childInfoDelete.get(0).date_of_birth, Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception c)
                            {
                                Toast.makeText(RegisteredChildActivity.this, c.toString(), Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                }
            }).start();*/

        Button delte_child=(Button) findViewById(R.id.delte_child);
        boolean visii = bundle.getBoolean("visibility");
        if(visii)
        {
            delte_child.setVisibility(View.VISIBLE);
        }
        delte_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder adb = new AlertDialog.Builder(RegisteredChildActivity.this);

                adb.setTitle("کیاآپ بچے کاڈیٹا ختم کرناچاہتے ہیں؟");


                adb.setIcon(R.drawable.info_circle);


                adb.setPositiveButton("ہاں", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final List<ChildInfo> data = ChildInfoDao.getByKIdAndIMEI(childID, imei);
                        List<KidVaccinations> items=KidVaccinationDao.getById(data.get(0).kid_id);
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
                            Intent in=new Intent(RegisteredChildActivity.this,DashboardActivity.class);
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
        vaccination_btn = (Button) findViewById(R.id.NFCWrite);
        vaccination_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Snackbar.make(tabBg, "Write on NFC Card", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
               /* Intent myintent = new Intent(curr, VaccinationActivity.class);

                myintent.putExtra("childid", childID);
                startActivity(myintent);*/
                final List<ChildInfo> data = ChildInfoDao.getByKIdAndIMEI(childID, imei);

                Intent intent = new Intent(curr, VaccinationActivity.class);
                long kid = 0;
                if (data.get(0).kid_id != null) {
                    kid = data.get(0).kid_id;
                    if (data.get(0).mobile_id == null && data.get(0).record_update_flag == false) {
                        data.get(0).mobile_id = data.get(0).kid_id;
                        data.get(0).save();
                    }
                } else {
                    finish();
                    return;
                }
                Bundle bnd = KidVaccinationDao.get_visit_details_db(kid, data.get(0).record_update_flag);
                intent.putExtra("childid", data.get(0).kid_id);
                intent.putExtra("imei", data.get(0).imei_number);
                intent.putExtra("isSync", data.get(0).record_update_flag);
                intent.putExtra("bookid", bookid);
                intent.putExtra("cnic", data.get(0).guardian_cnic);
                intent.putExtra("phone", data.get(0).phone_number);
                intent.putExtras(bnd);
                startActivity(intent);
                activityTime = (Calendar.getInstance().getTimeInMillis() / 1000) - activityTime;
                Constants.logTime(RegisteredChildActivity.this, activityTime, Constants.GaEvent.REGISTERED_TOTAL_TIME);
                finish();
            }
        });

        final List<ChildInfo> data = ChildInfoDao.getByKId(childID);
   /*     childPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.size()>0)
                Toast.makeText(RegisteredChildActivity.this, "No Record Found, Child ID" + data.get(0).kid_id + "Bundle Id :" + childID + "mobile_id" + data.get(0).mobile_id, Toast.LENGTH_LONG).show();
            }
        });*/
        if (data != null && data.size()>0) {
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

            Toast.makeText(this, "No Record Found! Kid ID:"+childID, Toast.LENGTH_LONG).show();
            finish();

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

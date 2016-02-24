package com.ipal.itu.harzindagi.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ipal.itu.harzindagi.Adapters.CustomViewPager;
import com.ipal.itu.harzindagi.Adapters.ViewPagerAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class VaccinationActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST = 1888;
    public String fpath;
    public String childID;
    public ImageView sixthTabTickMark;
    public int load_frag;
    public String vaccs_done;
    String app_name = "Har Zindagi";
    FileOutputStream fo;
    List<ChildInfo> data;
    Bundle bundle;
    private CustomViewPager mViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private View firstTab;
    private View secondTab;
    private View thirdTab;
    private View fourthTab;
    private View fifthTab;
    private View sixthTab;
    View[] v ;
    private ImageView firstTabTickMark;
    private ImageView secondTabTickMark;
    private ImageView thirdTabTickMark;
    private ImageView fourthTabTickMark;
    private ImageView fifthTabTickMark;
    ImageView[] vt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        load_frag = 0;
        vaccs_done = "0,0,0";

        bundle = getIntent().getExtras();
        try {
            childID = bundle.getString("childid");
        } catch (Exception e) {
            Toast.makeText(this, "ID not found", Toast.LENGTH_LONG).show();
            finish();
            e.printStackTrace();
        }
        if (bundle.size() >= 3) {
            try {
                load_frag = Integer.parseInt(bundle.getString("visit_num").toString()) - 1;

                if (Constants.isVaccOfVisitCompleted(bundle.getString("vacc_details").toString())) {

                    load_frag = Integer.parseInt(bundle.getString("visit_num").toString());


                }

                vaccs_done = bundle.getString("vacc_details").toString();
            }catch (Exception e){
                Toast.makeText(this,"Card Corrupted",Toast.LENGTH_LONG).show();
                finish();
            }

        }

        ChildInfoDao childInfoDao = new ChildInfoDao();

        data = childInfoDao.getById(bundle.getString("childid").toString());
        fpath = data.get(0).image_name;

        firstTab = findViewById(R.id.vaccinationActivityFirstTab);
        secondTab = findViewById(R.id.vaccinationActivitySecondTab);
        thirdTab = findViewById(R.id.vaccinationActivityThirdTab);
        fourthTab = findViewById(R.id.vaccinationActivityFourthTab);
        fifthTab = findViewById(R.id.vaccinationActivityFifthTab);
        sixthTab = findViewById(R.id.vaccinationActivitySixthTab);

        firstTabTickMark = (ImageView) findViewById(R.id.vaccinationActivityFirstTabTick);
        secondTabTickMark = (ImageView) findViewById(R.id.vaccinationActivitySecondTabTick);
        thirdTabTickMark = (ImageView) findViewById(R.id.vaccinationActivityThirdTabTick);
        fourthTabTickMark = (ImageView) findViewById(R.id.vaccinationActivityFourthTabTick);
        fifthTabTickMark = (ImageView) findViewById(R.id.vaccinationActivityFifthTabTick);
        sixthTabTickMark = (ImageView) findViewById(R.id.vaccinationActivitySixthTabTick);
        v = new View[]{firstTab, secondTab, thirdTab, fourthTab, fifthTab, sixthTab};
        vt = new ImageView[]{firstTabTickMark, secondTabTickMark, thirdTabTickMark, fourthTabTickMark, fifthTabTickMark,sixthTabTickMark};
        mViewPager = (CustomViewPager) findViewById(R.id.vaccinationActivityVaccinationsPager);
        if (Constants.isVaccOfVisitCompleted(vaccs_done)) {
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, VaccinationActivity.this, vaccs_done, (load_frag) + "");

        } else {
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, VaccinationActivity.this, vaccs_done, (load_frag + 1) + "");

        }
        mViewPager.setPagingEnabled(true);
        mViewPager.setAdapter(viewPagerAdapter);


        mViewPager.setCurrentItem(load_frag);
        mViewPager.setOffscreenPageLimit(5);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int curVisit = load_frag;//Integer.parseInt(bundle.getString("visit_num").toString());
                switch (position + 1) {
                    case 1:
                       // selectPrevious(0);
                        firstTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                        break;
                    case 2:

                        if (curVisit >= position) {
                            selectPrevious(0);
                            secondTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                            firstTabTickMark.setImageResource(R.drawable.ic_action_tick);

                            firstTabTickMark.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 3:

                        if (curVisit >= position) {
                            selectPrevious(1);
                            thirdTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                            secondTabTickMark.setImageResource(R.drawable.ic_action_tick);
                            secondTabTickMark.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 4:

                        if (curVisit >= position) {
                            selectPrevious(2);
                            fourthTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                            thirdTabTickMark.setImageResource(R.drawable.ic_action_tick);
                            thirdTabTickMark.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 5:

                        if (curVisit >= position) {
                            selectPrevious(3);
                            fifthTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                            fourthTabTickMark.setImageResource(R.drawable.ic_action_tick);
                            fourthTabTickMark.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 6:

                        if (curVisit >= position) {
                            selectPrevious(4);
                            sixthTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                            fifthTabTickMark.setImageResource(R.drawable.ic_action_tick);
                            fifthTabTickMark.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void selectPrevious(int index) {
        for (int i = 0; i <= index; i++) {
            v[i].setBackgroundResource(R.drawable.vaccinationtab_filled);
            vt[i].setImageResource(R.drawable.ic_action_tick);
            vt[i].setVisibility(View.VISIBLE);
        }
    }




    public void SetVaccineInfo() {


    }


    public void Take_Vaccine_Picture(View v) {


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Toast.makeText(this, "Clicked main", Toast.LENGTH_SHORT).show();
        if (resultCode == 1888) {
            CustomCamera.progress.dismiss();
            Bitmap photo, resizedImage;

            fpath = data.getStringExtra("fpath");
            String path = data.getStringExtra("path");
            photo = BitmapFactory.decodeFile(path);
            resizedImage = getResizedBitmap(photo, 256);
            saveBitmap(resizedImage);


            Bundle bndl = data.getExtras();

            Intent intent = new Intent(VaccinationActivity.this, CardScanWriteVaccine.class);
            intent.putExtra("childid", childID);
            if (bndl.size() >= 3) {
                intent.putExtra("vacc_details", bndl.getString("vacc_details"));
                intent.putExtra("visit_num", bndl.getString("visit_num"));

            }

            String date_String = Constants.getNextDueDate(Integer.parseInt(bndl.getString("visit_num")), bndl.getString("vacc_details").toString()); // index wise it is correct

            intent.putExtra("next_date", date_String);
            this.finish();
            startActivity(intent);
            //imageView.setImageBitmap(photo);*/
        }


    }


    public void readEditTexts() {
       /* UCNumber = "213";
        EPICenterName = CenterName.getText().toString();
        ChildName = childName.getText().toString();
        MotherName = motherName.getText().toString();
        GuardianName = guardianName.getText().toString();
        GuardianCNIC = guardianCNIC.getText().toString();
        GuardianMobileNumber = guardianMobileNumber.getText().toString();*/
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

    public void saveBitmap(Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);

        //Create a new file in sdcard folder.
        File f = new File("/sdcard/" + app_name + "/" + fpath + ".jpg");
        try {
            try {
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


}

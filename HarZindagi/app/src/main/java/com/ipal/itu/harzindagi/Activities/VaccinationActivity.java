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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class VaccinationActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST = 1888;

    String app_name="Har Zindagi";
   public String fpath;
    public  String childID;
    FileOutputStream fo;
    List<ChildInfo> data;

    private CustomViewPager mViewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private View firstTab;
    private View secondTab;
    private View thirdTab;
    private View fourthTab;
    private View fifthTab;
    private View sixthTab;

    private ImageView firstTabTickMark;
    private ImageView secondTabTickMark;
    private ImageView thirdTabTickMark;
    private ImageView fourthTabTickMark;
    private ImageView fifthTabTickMark;
    public ImageView sixthTabTickMark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
         childID = bundle.getString("childid");
        ChildInfoDao childInfoDao = new ChildInfoDao();

        data = childInfoDao.getById( bundle.getString("childid").toString());
        fpath=data.get(0).image_name;

        firstTab = findViewById(R.id.vaccinationActivityFirstTab);
        secondTab = findViewById(R.id.vaccinationActivitySecondTab);
        thirdTab =  findViewById(R.id.vaccinationActivityThirdTab);
        fourthTab = findViewById(R.id.vaccinationActivityFourthTab);
        fifthTab =  findViewById(R.id.vaccinationActivityFifthTab);
        sixthTab = findViewById(R.id.vaccinationActivitySixthTab);

        firstTabTickMark = (ImageView) findViewById(R.id.vaccinationActivityFirstTabTick);
        secondTabTickMark = (ImageView) findViewById(R.id.vaccinationActivitySecondTabTick);
        thirdTabTickMark = (ImageView) findViewById(R.id.vaccinationActivityThirdTabTick);
        fourthTabTickMark = (ImageView) findViewById(R.id.vaccinationActivityFourthTabTick);
        fifthTabTickMark = (ImageView) findViewById(R.id.vaccinationActivityFifthTabTick);
        sixthTabTickMark = (ImageView) findViewById(R.id.vaccinationActivitySixthTabTick);

        mViewPager = (CustomViewPager) findViewById(R.id.vaccinationActivityVaccinationsPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, VaccinationActivity.this);
        mViewPager.setPagingEnabled(true);
        mViewPager.setAdapter(viewPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position + 1) {
                    case 1:
                        firstTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                        break;
                    case 2:
                        secondTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                        firstTabTickMark.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        thirdTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                        secondTabTickMark.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        fourthTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                        thirdTabTickMark.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        fifthTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                        fourthTabTickMark.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        sixthTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                        fifthTabTickMark.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

public void SetVaccineInfo()
    {



    }




    public void Take_Vaccine_Picture(View v){



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



            Bundle bndl=data.getExtras();

            Intent intent = new Intent(VaccinationActivity.this, CardScanWriteVaccine.class);
           intent.putExtra("childid", childID);
            if(bndl.size()>=3)
            {
                intent.putExtra("vacc_details", bndl.getString("vacc_details"));
                intent.putExtra("visit_num", bndl.getString("visit_num"));

            }
             Calendar c = Calendar.getInstance();

            c.add(Calendar.DATE, 40);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
            String date_String=  sdf1.format(c.getTime());
            intent.putExtra("next_date",date_String);
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

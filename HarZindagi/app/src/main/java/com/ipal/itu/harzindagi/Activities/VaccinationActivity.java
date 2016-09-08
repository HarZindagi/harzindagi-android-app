package com.ipal.itu.harzindagi.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.LocationAjaxCallback;
import com.ipal.itu.harzindagi.Adapters.CustomViewPager;
import com.ipal.itu.harzindagi.Adapters.ViewPagerAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

public class VaccinationActivity extends BaseActivity {

    public static final int CAMERA_REQUEST = 1888;

    public String fpath;
    public long childID;
    public int load_frag;
    public String vaccs_done;
    public TextView sixthTabTickMark;
    public boolean isVaccCompleted = false;
    String app_name = "Har Zindagi";
    FileOutputStream fo;
    List<ChildInfo> data;
    Bundle bundle;
    View[] v;
    TextView[] vt;
    Toolbar toolbar;
    int[] array = new int[]{R.drawable.vactab_fill1,
            R.drawable.vactab_fill2,
            R.drawable.vactab_fill3,
            R.drawable.vactab_fill4,
            R.drawable.vactab_fill5,
            R.drawable.vactab_fill6};
    private CustomViewPager mViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout firstTab;
    private LinearLayout secondTab;
    private LinearLayout thirdTab;
    private LinearLayout fourthTab;
    private LinearLayout fifthTab;
    private LinearLayout sixthTab;
    private TextView firstTabTickMark;
    private TextView secondTabTickMark;
    private TextView thirdTabTickMark;
    private TextView fourthTabTickMark;
    private TextView fifthTabTickMark;
    public String imei;
    TextView toolbar_title;
    private int[] toolbar_color = {

            R.color.dark_red,
            R.color.red,
            R.color.purple,
            R.color.yellow_green,
            R.color.blue,
            R.color.dark_green
    };
    private int[] circle_colr = {

            R.drawable.dark_red_cir,
            R.drawable.red_cir,
            R.drawable.purple_cir,
            R.drawable.yel_grn_cir,
            R.drawable.blue_cir,
            R.drawable.dark_green_cir
    };
    public int bookid;
    public static String location;
    private long activityTime;

    private void getLocation() {

        LocationAjaxCallback cb = new LocationAjaxCallback();
        //  final ProgressDialog pDialog = new ProgressDialog(this);
        //  pDialog.setMessage("Getting Location");

        cb.weakHandler(this, "locationCb").timeout(20 * 1000).expire(1000 * 30 * 5).async(this);
        //  pDialog.setCancelable(false);
        //  pDialog.show();
    }
    public void logTime(){
        activityTime = (Calendar.getInstance().getTimeInMillis() / 1000) - activityTime;
       // Constants.sendGAEvent(this,Constants.getUserName(this), Constants.GaEvent.VAC_TIME, activityTime + " S", 0);
        Constants.logTime(this,activityTime,Constants.GaEvent.VAC_TIME);
    }

    @Override
    public void onBackPressed() {

        Constants.sendGAEvent(this,Constants.getUserName(this), Constants.GaEvent.BACK_NAVIGATION,Constants.GaEvent.VAC_BACK , 0);
        super.onBackPressed();
    }

    public void locationCb(String url, final Location loc, AjaxStatus status) {

        if (loc != null) {

            double lat = loc.getLatitude();
            double log = loc.getLongitude();
            location = lat + "," + log;


        } else {
            location = "0.0000:0.0000";


        }
    }
   public boolean record_sync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination);
        activityTime = Calendar.getInstance().getTimeInMillis() / (1000);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getLocation();
        toolbar_title=(TextView)findViewById(R.id.toolbar_title);
        load_frag = 0;
        vaccs_done = "0,0,0";
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        bundle = getIntent().getExtras();
        try {
            childID = bundle.getLong("childid");
            imei =  bundle.getString("imei");
            bookid =  bundle.getInt("bookid");
          
        } catch (Exception e) {
            Toast.makeText(this, "Try again!", Toast.LENGTH_LONG).show();
            finish();
            e.printStackTrace();
        }
        if (bundle.size() >= 3) {
            try {
                load_frag = Integer.parseInt(bundle.getString("visit_num").toString()) - 1;

                if (Constants.isVaccOfVisitCompleted(bundle.getString("vacc_details").toString())) {

                    load_frag = Integer.parseInt(bundle.getString("visit_num").toString());
                    if (load_frag == 6) {
                        isVaccCompleted = true;
                    }

                }

                vaccs_done = bundle.getString("vacc_details").toString();
            } catch (Exception e) {
                Toast.makeText(this, "Try again!", Toast.LENGTH_LONG).show();
                finish();
            }

        }
         record_sync = bundle.getBoolean("isSync",false);
        if (record_sync) {
            data = ChildInfoDao.getByKIdAndIMEI(bundle.getLong("childid"),bundle.getString("imei"));

        } else {
            data = ChildInfoDao.getByLocalKIdandIMEI(bundle.getLong("childid"),bundle.getString("imei"));
        }


        if (data.size() > 0) {
            fpath = data.get(0).image_path;
        }
        if (data.size() == 0) {
            finish();
            Toast.makeText(this, "Try again!", Toast.LENGTH_LONG).show();
            return;
        }
        //setTitleImage(toolbar,fpath);
        ((TextView) findViewById(R.id.ChildName)).setText(data.get(0).kid_name);
        ((TextView) findViewById(R.id.EPINumber)).setText(data.get(0).epi_number + "");
        firstTab =(LinearLayout) findViewById(R.id.vaccinationActivityFirstTab);
        secondTab =(LinearLayout) findViewById(R.id.vaccinationActivitySecondTab);
        thirdTab = (LinearLayout)findViewById(R.id.vaccinationActivityThirdTab);
        fourthTab = (LinearLayout)findViewById(R.id.vaccinationActivityFourthTab);
        fifthTab = (LinearLayout)findViewById(R.id.vaccinationActivityFifthTab);
        sixthTab = (LinearLayout)findViewById(R.id.vaccinationActivitySixthTab);

        firstTabTickMark = (TextView) findViewById(R.id.vaccinationActivityFirstTabTick);
        secondTabTickMark = (TextView) findViewById(R.id.vaccinationActivitySecondTabTick);
        thirdTabTickMark = (TextView) findViewById(R.id.vaccinationActivityThirdTabTick);
        fourthTabTickMark = (TextView) findViewById(R.id.vaccinationActivityFourthTabTick);
        fifthTabTickMark = (TextView) findViewById(R.id.vaccinationActivityFifthTabTick);
        sixthTabTickMark = (TextView) findViewById(R.id.vaccinationActivitySixthTabTick);
        v = new View[]{firstTab, secondTab, thirdTab, fourthTab, fifthTab, sixthTab};
        vt = new TextView[]{firstTabTickMark, secondTabTickMark, thirdTabTickMark, fourthTabTickMark, fifthTabTickMark, sixthTabTickMark};
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
        int index = load_frag - 1;
        if (index < 0) {

            v[0].setBackgroundResource(array[0]);
        } else {
            selectPrevious(index);
        }


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               // toolbar.setBackgroundResource(toolbar_color[position]);

                switch (position + 1) {
                    case 1:

                        toolbar_title.setText(getString(R.string.padaish_forun_baad));
                        break;
                    case 2:

                        toolbar_title.setText(getString(R.string.six_huftay));
                        break;
                    case 3:

                        toolbar_title.setText(getString(R.string.ten_huftay));
                        break;
                    case 4:

                        toolbar_title.setText(getString(R.string.fourtheen_huftay));
                        break;
                    case 5:

                        toolbar_title.setText(getString(R.string.nine_month_baad));
                        break;
                    case 6:

                        toolbar_title.setText(getString(R.string.fifteen_month_baad));
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


    public void setTitleImage(Toolbar toolbar, String fpath) {
        String imagePath = "/sdcard/" + app_name + "/" + fpath + ".jpg";
        // Bitmap bmp_read = BitmapFactory.decodeFile(imagePath);
        Drawable d = Drawable.createFromPath(imagePath);
        //Drawable d = new BitmapDrawable(getResources(), bmp_read);
        toolbar.setNavigationIcon(d);
    }

    public void selectPrevious(int index) {
        for (int i = 0; i <= index; i++) {
            v[i].setBackgroundResource(R.drawable.text_filled_green);
            //vt[i].setBackgroundResource(R.drawable.ic_action_tick);
            // v[i].setVisibility(View.VISIBLE);
            vt[i].setTextColor(Color.WHITE);

        }
        if (!isVaccCompleted) {
           // v[load_frag].setBackgroundResource(array[load_frag]);
            v[load_frag].setBackgroundResource(R.drawable.text_filled_green);
            //vt[i].setBackgroundResource(R.drawable.ic_action_tick);
            // v[i].setVisibility(View.VISIBLE);
            vt[load_frag].setTextColor(Color.WHITE);
        } else {
            v[5].setBackgroundResource(R.drawable.text_filled_green);
            vt[5].setTextColor(Color.WHITE);
           /* v[5].setBackgroundResource(circle_colr[5]);
            vt[5].setVisibility(View.GONE);*/
        }

    }


    public void SetVaccineInfo() {


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Toast.makeText(this, "Clicked main", Toast.LENGTH_SHORT).show();
        if (resultCode == 1888) {

            Bitmap photo, resizedImage;

            fpath = data.getStringExtra("fpath");
            String path = data.getStringExtra("path");
            photo = BitmapFactory.decodeFile(path);
            resizedImage = getResizedBitmap(photo, 256);
            saveBitmap(resizedImage);


            Bundle bndl = data.getExtras();

            Intent intent = new Intent(VaccinationActivity.this, CardScanWriteVaccine.class);
            intent.putExtra("childid", childID);
            intent.putExtra("bookid", bookid);
            if (bndl.size() >= 3) {

                intent.putExtra("vacc_details", bndl.getString("vacc_details"));
                intent.putExtra("visit_num", bndl.getString("visit_num"));

            }

            String date_String = Constants.getNextDueDate(Integer.parseInt(bndl.getString("visit_num")), bndl.getString("vacc_details").toString()); // index wise it is correct
            intent.putExtra("curr_visit_num", load_frag);
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

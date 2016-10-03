package com.ipal.itu.harzindagi.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.LocationAjaxCallback;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ipal.itu.harzindagi.Application.HarZindagiApp;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.CheckIn;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import static com.ipal.itu.harzindagi.R.id.homeActivityHarZindagiButtonR;

public class HomeActivity extends BaseActivity {
    private static final int CAMERA_REQUEST = 1887;
    String location = "0.0000,0.0000";
    FileOutputStream fo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Constants.sendGAScrenn(this, this.getClass().getName() + "Opened");


        ((RippleView) findViewById(R.id.homeActivityEVACCSButtonR)).setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(HomeActivity.this, Evaccs.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                Constants.sendGAEvent(HomeActivity.this, "Button", "Click", "EVVACCS", 0);
            }

        });


        ((RippleView) findViewById(R.id.homeActivityHarZindagiButtonR)).setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                startActivity(new Intent(HomeActivity.this, DashboardActivity.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                Constants.sendGAEvent(HomeActivity.this, "Button", "Click", "Har Zindagi", 0);
            }

        });
        getLocation();


        ((RippleView) findViewById(R.id.homeActivitykidstationiButtonR)).setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                Intent cameraIntent = new Intent(HomeActivity.this, CustomCameraKidstation.class);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                Constants.sendGAEvent(HomeActivity.this, "Button", "Click", "Kit Station", 0);
            }

        });
    }

    private void getLocation() {
        LocationAjaxCallback cb = new LocationAjaxCallback();
        cb.weakHandler(this, "locationCb").timeout(20 * 1000).expire(1000 * 30 * 5).async(this);
    }

    public void locationCb(String url, final Location loc, AjaxStatus status) {

        if (loc != null) {

            double lat = loc.getLatitude();
            double log = loc.getLongitude();
            location = lat + "," + log;
            Constants.setLocation(this, location);

        } else {
            Constants.setLocation(this, "0.0000,0.0000");
            HomeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(), "Location Not Found", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == 1887) {
            Bitmap photo, resizedImage;
            CustomCameraKidstation.progress.dismiss();
            String path = data.getStringExtra("path");
            photo = BitmapFactory.decodeFile(path);
            resizedImage = getResizedBitmap(photo, 256);
            saveBitmap(resizedImage);

            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);

            startActivity(intent);
            finish();

            //imageView.setImageBitmap(photo);
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (Constants.getCheckIn(this).equals("") || !Constants.getDay(this).equals(day + "")) {
                Constants.setkitTime(this, Calendar.getInstance().getTimeInMillis() / 1000);
                String created_time = "" + Calendar.getInstance().getTimeInMillis() / 1000;
                Constants.setCheckIn(this, created_time);
                Constants.setDay(this, day + "");
                Constants.setCheckOut(this, "");
                CheckIn checkIn = new CheckIn();
                checkIn.is_sync = false;
                checkIn.location = location;
                checkIn.created_timestamp = created_time;
                checkIn.save();

            }
        }

    }

    public void saveBitmap(Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);

        //Create a new file in sdcard folder.
        File f = new File("abcd.jpg"); // this needs to be set
        try {
            try {
                f.createNewFile();
                fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray()); //write the bytes in file
            } finally {
                if (fo != null) {
                    fo.close(); // remember close the FileOutput
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

}

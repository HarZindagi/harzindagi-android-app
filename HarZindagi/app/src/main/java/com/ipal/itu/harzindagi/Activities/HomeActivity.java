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

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.LocationAjaxCallback;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ipal.itu.harzindagi.Application.HarZindagiApp;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class HomeActivity extends BaseActivity {
    private static final int CAMERA_REQUEST = 1887;
    Button evaccsButton;
    Button harZindagiButton;
    Button kidStationPicture;
    String location = "0.0000,0.0000";
    FileOutputStream fo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Constants.sendGAScrenn(this,this.getClass().getName()+"Opened");
        evaccsButton = (Button) findViewById(R.id.homeActivityEVACCSButton);
        evaccsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "EVACCS Button Clicked!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/


                Intent intent=new Intent(HomeActivity.this,Evaccs.class);
                startActivity(intent);
                Constants.sendGAEvent(HomeActivity.this,"Button","Click","EVVACCS",0);
               // finish();
            }
        });

        harZindagiButton = (Button) findViewById(R.id.homeActivityHarZindagiButton);
        harZindagiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Har Zindagi Button Clicked!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivity(new Intent(HomeActivity.this, DashboardActivity.class));
                Constants.sendGAEvent(HomeActivity.this,"Button","Click","Har Zindagi",0);
               // finish();
            }
        });
        getLocation();
    kidStationPicture=(Button)findViewById(R.id.homeActivitykidstationiButton);
        kidStationPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(HomeActivity.this, CustomCameraKidstation.class);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                Constants.sendGAEvent(HomeActivity.this,"Button","Click","Kit Station",0);
            }
        });
    }

    private void getLocation() {

        LocationAjaxCallback cb = new LocationAjaxCallback();
      //  final ProgressDialog pDialog = new ProgressDialog(this);
      //  pDialog.setMessage("Getting Location");

        cb.weakHandler(this, "locationCb").timeout(20 * 1000).expire(1000*30*5).async(this);
      //  pDialog.setCancelable(false);
      //  pDialog.show();
    }

    public void locationCb(String url, final Location loc, AjaxStatus status) {

        if (loc != null) {

            double lat = loc.getLatitude();
            double log = loc.getLongitude();
            location = lat + "," + log;
            Constants.setLocation(this,location);

        } else {
            Constants.setLocation(this,"0.0000:0.0000");
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
           /* try {
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                resizedImage = getResizedBitmap(photo, 256);
                saveBitmap(resizedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);

            startActivity(intent);
            finish();

            //imageView.setImageBitmap(photo);
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (Constants.getCheckIn(this).equals("") || !Constants.getDay(this).equals(day + "")) {
                Constants.setCheckIn(this, (Calendar.getInstance().getTimeInMillis() / 1000) + "");
                Constants.setDay(this, day + "");
                Constants.setCheckOut(this, "");

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

package com.ipal.itu.harzindagi.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.LocationAjaxCallback;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

public class HomeActivity extends AppCompatActivity {

    Button evaccsButton;
    Button harZindagiButton;
    String location = "0.0000,0.0000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        evaccsButton = (Button) findViewById(R.id.homeActivityEVACCSButton);
        evaccsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "EVACCS Button Clicked!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/


                Intent intent=new Intent(HomeActivity.this,Evaccs.class);
                startActivity(intent);
            }
        });

        harZindagiButton = (Button) findViewById(R.id.homeActivityHarZindagiButton);
        harZindagiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Har Zindagi Button Clicked!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivity(new Intent(HomeActivity.this, DashboardActivity.class));
            }
        });
        getLocation();
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

}

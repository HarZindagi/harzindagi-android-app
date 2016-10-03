package com.ipal.itu.harzindagi.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipal.itu.harzindagi.R;

import java.io.File;

public class Evaccs extends BaseActivity {
    String app_name;
    String Evac;
    String EvacNonEpi;
    Boolean isFolderExists;
    TextView toolbar_title;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evacss_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title=(TextView)findViewById(R.id.toolbar_title);
        toolbar_title.setText("Evaccs-II");
       final Context ctx=this;
        app_name = getResources().getString(R.string.app_name);
        Evac =getResources().getString(R.string.evac);
        File appFolder = new File("/sdcard/" + app_name +"/"+ Evac+"/");
        isFolderExists = appFolder.exists();
        if (!isFolderExists) {
            appFolder.mkdirs();
        }
        EvacNonEpi = getResources().getString(R.string.evacnonepi);
        appFolder = new File("/sdcard/" + app_name +"/"+ EvacNonEpi+"/");
        isFolderExists = appFolder.exists();
        if (!isFolderExists) {
            appFolder.mkdirs();
        }
        ImageView epi_btn=(ImageView)findViewById(R.id.evacs_Epi);
        epi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ctx,EvacsEPI.class);
                startActivity(i);
            }
        });

        ImageView non_epi_btn=(ImageView)findViewById(R.id.evacs_non_Epi);
        non_epi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ctx,EvacsNonEPI.class);
                startActivity(i);
            }
        });


    }


}

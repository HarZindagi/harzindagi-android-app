package com.ipal.itu.harzindagi.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.ipal.itu.harzindagi.R;

import java.io.File;

public class Evaccs extends AppCompatActivity {
    String app_name;
    String Evac;
    Boolean isFolderExists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evacss_layout);

       final Context ctx=this;
        app_name = getResources().getString(R.string.app_name);
        Evac =getResources().getString(R.string.evac);
        File appFolder = new File("/sdcard/" + app_name +"/"+ Evac+"/");
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

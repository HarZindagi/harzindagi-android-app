package com.ipal.itu.harzindagi.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.ipal.itu.harzindagi.R;

public class Evaccs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evacss_layout);

       final Context ctx=this;
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

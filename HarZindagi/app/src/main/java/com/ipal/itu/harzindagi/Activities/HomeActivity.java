package com.ipal.itu.harzindagi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ipal.itu.harzindagi.R;

public class HomeActivity extends AppCompatActivity {

    Button evaccsButton;
    Button harZindagiButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        evaccsButton = (Button) findViewById(R.id.homeActivityEVACCSButton);
        evaccsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "EVACCS Button Clicked!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent=new Intent(HomeActivity.this,Evaccs.class);
                startActivity(intent);
            }
        });

        harZindagiButton = (Button) findViewById(R.id.homeActivityHarZindagiButton);
        harZindagiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Har Zindagi Button Clicked!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(HomeActivity.this , DashboardActivity.class));
            }
        });
    }



}

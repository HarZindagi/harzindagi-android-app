package com.ipal.itu.harzindagi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.ipal.itu.harzindagi.R;

public class DashboardActivity extends AppCompatActivity {

    Button registerChildButton;
    Button scanChildButton;
    Button searchChildButton;
    Button allChildrenInUCButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerChildButton = (Button) findViewById(R.id.dashBoardActivityRegisterChildButton);
        registerChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Register Child", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        scanChildButton = (Button) findViewById(R.id.dashBoardActivityScanChildButton);
        scanChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Scan Child", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        searchChildButton = (Button) findViewById(R.id.dashBoardActivitySearchChildButton);
        searchChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Search Child", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent( DashboardActivity.this , SearchActivity.class ));
            }
        });

        allChildrenInUCButton = (Button) findViewById(R.id.dashBoardActivityShowAllChildrenButton);
        allChildrenInUCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "All Children in UC", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}

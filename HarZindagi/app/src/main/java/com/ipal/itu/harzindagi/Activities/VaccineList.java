package com.ipal.itu.harzindagi.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ipal.itu.harzindagi.Adapters.CustomListAdapter;
import com.ipal.itu.harzindagi.R;

/**
 * Created by Wahab on 3/2/2016.
 */
public class VaccineList extends AppCompatActivity {

    String rec_response;
    VaccDetailBook obj;
    ListView list;
    CustomListAdapter adapter;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.vacc_list_layout);
         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);



        obj= (VaccDetailBook) getIntent().getSerializableExtra("VaccDetInfo");


        parseResponse();
        findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void parseResponse() {


        list = (ListView) findViewById(R.id.vacc_list);
        adapter = new CustomListAdapter(this, obj.vaccinfo);
        list.setAdapter(adapter);
    }
}

package com.ipal.itu.harzindagi.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ipal.itu.harzindagi.Adapters.CustomListAdapter;
import com.ipal.itu.harzindagi.Entity.VaccDetailBook;
import com.ipal.itu.harzindagi.R;

/**
 * Created by Wahab on 3/2/2016.
 */
public class VaccineList extends AppCompatActivity {

    String rec_response;
    VaccDetailBook obj;
    ListView list;
    CustomListAdapter adapter;

    private int[] vacc_period = {
            R.string.padaish_forun_baad,
            R.string.padaish_forun_baad,
            R.string.six_huftay,
            R.string.ten_huftay,
            R.string.fourtheen_huftay,
            R.string.nine_month_baad,
            R.string.fifteen_month_baad
    };
    private int[] color_period = {
            R.drawable.color_1,
            R.drawable.color_1,
            R.drawable.color_2,
            R.drawable.color_3,
            R.drawable.color_4,
            R.drawable.color_5,
            R.drawable.color_6
    };
    TextView time_period_txt;
    LinearLayout top_header;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.vacc_list_layout);
         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
         top_header = (LinearLayout) findViewById(R.id.topHeader);
         time_period_txt=(TextView)findViewById(R.id.time_period_txt);
         for (int i = 1; i <= 6; i++) {
             if (VaccinationActivity.currnt_visit == i) {
                 time_period_txt.setText(vacc_period[i]);
                 top_header.setBackgroundResource(color_period[i]);
             }
         }

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

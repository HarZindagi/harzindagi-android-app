package com.ipal.itu.harzindagi.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ipal.itu.harzindagi.Adapters.CustomListAdapter;
import com.ipal.itu.harzindagi.Entity.VaccInfoList;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

/**
 * Created by Wahab on 3/2/2016.
 */
public class VaccineList extends BaseActivity {

    VaccInfoList obj;
    ListView list;
    CustomListAdapter adapter;
    TextView toolbar_title;
    LinearLayout top_header;
    String compelte1="مبارک ہو آپ کےبچے کا حفاظتی ٹیکوں کا کورس مکمل ہو چکا ہے";
    String compelte2="برائے مہربانی اس ٹیکے کے بعد یاد سے ویکسینیٹر سے اپنےبچےکا";
    String compelte3=" سرٹیفیکیٹ برائے تکمیل حفاظتی ٹیکہ جات";
    String compelte4="ہمراہ لیتے جائیں";
    private int[] vacc_period = {

            R.string.padaish_forun_baad,
            R.string.six_huftay,
            R.string.ten_huftay,
            R.string.fourtheen_huftay,
            R.string.nine_month_baad,
            R.string.fifteen_month_baad
    };

    Context context;
    int visitNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacc_list_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        top_header = (LinearLayout) findViewById(R.id.topHeader);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        obj = (VaccInfoList) getIntent().getSerializableExtra("VaccDetInfo");

        try {
            visitNumber = getIntent().getExtras().getInt("visit_num_");
            if(visitNumber == -1 || visitNumber == 0){
                toolbar_title.setText(vacc_period[0]);
                ((TextView) findViewById(R.id.nextDueDateTxt)).setText(getIntent().getExtras().getString("next_due_date"));
                ((TextView) findViewById(R.id.nextDueDateTxbtwn)).setText(Constants.addDate(getIntent().getExtras().getString("next_due_date")));


            }
            if(getIntent().getExtras().getInt("visit_num_")<5 && getIntent().getExtras().getInt("visit_num_")>0){
                toolbar_title.setText(vacc_period[getIntent().getExtras().getInt("visit_num_")]);

                ((TextView) findViewById(R.id.nextDueDateTxt)).setText(getIntent().getExtras().getString("next_due_date"));
                ((TextView) findViewById(R.id.nextDueDateTxbtwn)).setText(Constants.addDate(getIntent().getExtras().getString("next_due_date")));

            }
            if(getIntent().getExtras().getInt("visit_num_")==5){
                toolbar_title.setText(vacc_period[getIntent().getExtras().getInt("visit_num_")]);
                findViewById(R.id.nextDueDateTxt).setVisibility(View.GONE);
                findViewById(R.id.nextDueDateTxbtwn).setVisibility(View.GONE);
                findViewById(R.id.two_arrw_head).setVisibility(View.GONE);
                ((TextView)findViewById(R.id.nxt_txt)).setText(compelte1 + compelte2 + "\"" + compelte3 + "\"" + compelte4);
                ((TextView)findViewById(R.id.nxt_txt)).setTextColor(ContextCompat.getColor(context, R.color.red));


            }

        }catch (Exception e){
            Toast.makeText(this ,"Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
            finish();
        }

        parseResponse();
        findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nn=new Intent(VaccineList.this,DashboardActivity.class);
                startActivity(nn);
                finish();
            }
        });

    }

    public void parseResponse() {


        list = (ListView) findViewById(R.id.vacc_list);
        adapter = new CustomListAdapter(this, obj.vaccinfo,visitNumber,true);
        list.setAdapter(adapter);
    }
}

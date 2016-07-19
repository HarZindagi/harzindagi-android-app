package com.ipal.itu.harzindagi.Activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ipal.itu.harzindagi.Adapters.CustomListAdapter;
import com.ipal.itu.harzindagi.Entity.VaccDetailBook;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

/**
 * Created by Wahab on 3/2/2016.
 */
public class VaccineList extends BaseActivity {

    String rec_response;
    VaccDetailBook obj;
    ListView list;
    CustomListAdapter adapter;
    TextView time_period_txt;
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
    private int[] color_period = {

            R.drawable.color_1,
            R.drawable.color_2,
            R.drawable.color_3,
            R.drawable.color_4,
            R.drawable.color_5,
            R.drawable.color_6
    };
    private int[] nxt_oardr_colr = {

            R.drawable.nxt_date_boardr1,
            R.drawable.nxt_date_boardr2,
            R.drawable.nxt_date_boardr3,
            R.drawable.nxt_date_boardr4,
            R.drawable.nxt_date_boardr5,
            R.drawable.nxt_date_boardr6,
    };

    private int[] nxt_boardr_numbr = {

            R.drawable.next_boardr_full1,
            R.drawable.next_boardr_full2,
            R.drawable.next_boardr_full3,
            R.drawable.next_boardr_full4,
            R.drawable.next_boardr_full5,
            R.drawable.next_boardr_full6
    };
    Context context;
    private String[] numbr={"1","2","3","4","5","6"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacc_list_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;
        top_header = (LinearLayout) findViewById(R.id.topHeader);
        time_period_txt = (TextView) findViewById(R.id.time_period_txt);
        obj = (VaccDetailBook) getIntent().getSerializableExtra("VaccDetInfo");
        boolean notComplete = false;
        for (int i = 0; i <obj.vaccinfo.size() ; i++) {
            if((""+ obj.vaccinfo.get(i).day).equals("--")){
                notComplete = true;
            }
        }
        try {
            if(getIntent().getExtras().getInt("visit_num_") == -1){
                time_period_txt.setText(vacc_period[0]);
                ((TextView) findViewById(R.id.nextDueDateTxt)).setText(getIntent().getExtras().getString("next_due_date"));
                ((TextView) findViewById(R.id.nextDueDateTxt)).setBackgroundResource(nxt_oardr_colr[1]);
                ((TextView) findViewById(R.id.nextDueDateTxbtwn)).setText(Constants.addDate(getIntent().getExtras().getString("next_due_date")));
                ((TextView) findViewById(R.id.nextDueDateTxbtwn)).setBackgroundResource(nxt_oardr_colr[1]);
                ((TextView)findViewById(R.id.nextDueDateNmbr)).setText(numbr[1]);
                ((TextView)findViewById(R.id.nextDueDateNmbr)).setBackgroundResource(nxt_boardr_numbr[1]);
                top_header.setBackgroundResource(color_period[0]);

            }
            if(getIntent().getExtras().getInt("visit_num_")<5 && getIntent().getExtras().getInt("visit_num_")>0){
                time_period_txt.setText(vacc_period[getIntent().getExtras().getInt("visit_num_")]);

                ((TextView) findViewById(R.id.nextDueDateTxt)).setText(getIntent().getExtras().getString("next_due_date"));
                ((TextView) findViewById(R.id.nextDueDateTxt)).setBackgroundResource(nxt_oardr_colr[getIntent().getExtras().getInt("visit_num_")] + 1);
                ((TextView) findViewById(R.id.nextDueDateTxbtwn)).setText(Constants.addDate(getIntent().getExtras().getString("next_due_date")));
                ((TextView) findViewById(R.id.nextDueDateTxbtwn)).setBackgroundResource(nxt_oardr_colr[getIntent().getExtras().getInt("visit_num_")] + 1);
                if(notComplete) {
                    ((TextView) findViewById(R.id.nextDueDateNmbr)).setText(numbr[getIntent().getExtras().getInt("visit_num_")]);
                }else{
                    ((TextView) findViewById(R.id.nextDueDateNmbr)).setText(numbr[getIntent().getExtras().getInt("visit_num_") + 1]);
                }
                ((TextView)findViewById(R.id.nextDueDateNmbr)).setBackgroundResource(nxt_boardr_numbr[getIntent().getExtras().getInt("visit_num_")]+1);
                top_header.setBackgroundResource(color_period[getIntent().getExtras().getInt("visit_num_")]);

            }
            if(getIntent().getExtras().getInt("visit_num_")==5){
                time_period_txt.setText(vacc_period[getIntent().getExtras().getInt("visit_num_")]);
                ((TextView) findViewById(R.id.nextDueDateTxt)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.nextDueDateTxbtwn)).setVisibility(View.GONE);
                ((TextView)findViewById(R.id.nextDueDateNmbr)).setVisibility(View.GONE);
                ((ImageView)findViewById(R.id.two_arrw_head)).setVisibility(View.GONE);
                ((TextView)findViewById(R.id.nxt_txt)).setText(compelte1 + compelte2 + "\"" + compelte3 + "\"" + compelte4);
                ((TextView)findViewById(R.id.nxt_txt)).setTextColor(ContextCompat.getColor(context, R.color.red));
                top_header.setBackgroundResource(color_period[getIntent().getExtras().getInt("visit_num_")]);


            }

        }catch (Exception e){
            Toast.makeText(this ,"Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
            finish();
        }





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

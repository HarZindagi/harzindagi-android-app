package com.ipal.itu.harzindagi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ipal.itu.harzindagi.Adapters.ChildListAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;

import java.util.List;

/**
 * Created by IPAL on 9/29/2016.
 */

public class AfterScanCard extends BaseActivity {
    TextView toolbar_title;
    String app_name;
    List<ChildInfo> data;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_chiled_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().hasExtra("cnic")) {

            data = ChildInfoDao.getByLocalCnicandIMEI(getIntent().getStringExtra("cnic"), getIntent().getStringExtra("imei"));
        } else {
            data = ChildInfoDao.getByLocalPhoneandIMEI(getIntent().getStringExtra("phone"), getIntent().getStringExtra("imei"));
        }


        app_name = getResources().getString(R.string.app_name);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("بچوں کا ریکارڈ");
        ListView listView = (ListView) findViewById(R.id.tab_list);
        ChildListAdapter childListAdapter = new ChildListAdapter(this, R.layout.listactivity_row, data, app_name);
        listView.setAdapter(childListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                   /* Intent intent = new Intent(getActivity(), VaccinationActivity.class);

                    Bundle bnd = KidVaccinationDao.get_visit_details_db(data.get(position).mobile_id);
                    intent.putExtra("childid", data.get(position).epi_number);
                    intent.putExtras(bnd);
                    startActivity(intent);*/

                Intent myintent = new Intent(AfterScanCard.this, RegisteredChildActivity.class);
                myintent.putExtra("childid", data.get(position).kid_id);
                myintent.putExtra("imei", data.get(position).imei_number);
                myintent.putExtra("EPIname", data.get(position).epi_name);
                myintent.putExtra("bookid", Integer.parseInt(data.get(position).book_id));
                myintent.putExtra("isSync", data.get(position).record_update_flag);
                startActivity(myintent);

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == android.R.id.home) {
            // startActivity(new Intent(getApplication(),RegisterChildActivity.class).putExtra("epiNumber",childID));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

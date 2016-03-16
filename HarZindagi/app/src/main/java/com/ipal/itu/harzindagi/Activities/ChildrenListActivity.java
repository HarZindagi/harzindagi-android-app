package com.ipal.itu.harzindagi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ipal.itu.harzindagi.Adapters.ChildListAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;

import java.util.ArrayList;
import java.util.List;

public class ChildrenListActivity extends AppCompatActivity {
    String app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        app_name = getResources().getString(R.string.app_name);
        boolean fromSMS = getIntent().getBooleanExtra("fromSMS", false);


        if (!fromSMS) {


            if (SearchActivity.data != null) {
                if (SearchActivity.data.size()==0) {
                    Toast.makeText(this, "No Record Found", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                ListView listView = (ListView) findViewById(R.id.childrenListActivityListView);
                ChildListAdapter childListAdapter = new ChildListAdapter(this, R.layout.listactivity_row, SearchActivity.data, app_name);
                listView.setAdapter(childListAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                        Intent intent = new Intent(getApplication(), VaccinationActivity.class);

                        Bundle bnd = KidVaccinationDao.get_visit_details_db(SearchActivity.data.get(position).mobile_id);
                        intent.putExtra("childid", SearchActivity.data.get(position).epi_number);
                        intent.putExtras(bnd);
                        startActivity(intent);

                    }
                });
            }
        } else {
            ChildInfoDao dao = new ChildInfoDao();


            String ID = getIntent().getStringExtra("ID");

            String CHILD_NAME = getIntent().getStringExtra("CHILD_NAME");
            if (CHILD_NAME.equals("0")) {
                Toast.makeText(this, "No Record Found", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            String guardian_name = getIntent().getStringExtra("Guardian_name");
            String Address = getIntent().getStringExtra("Address");
            String VisitNum = getIntent().getStringExtra("VisitNum");
            String VAC_LIST = getIntent().getStringExtra("VAC_LIST");

            final List<ChildInfo> list = new ArrayList<>();
            ChildInfo childInfo = new ChildInfo();
            childInfo.mobile_id = Long.parseLong(ID);
            childInfo.kid_name = CHILD_NAME;
            childInfo.guardian_name = guardian_name;
            childInfo.child_address = Address;

            list.add(childInfo);
            ListView listView = (ListView) findViewById(R.id.childrenListActivityListView);
            ChildListAdapter childListAdapter = new ChildListAdapter(this, R.layout.listactivity_row, list, app_name);
            listView.setAdapter(childListAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    Intent intent = new Intent(getApplication(), VaccinationActivity.class);

                    Bundle bnd = KidVaccinationDao.get_visit_details_db(list.get(position).mobile_id);
                    intent.putExtra("childid", list.get(position).epi_number);
                    intent.putExtras(bnd);
                    startActivity(intent);

                }

            });
        }

    }
}

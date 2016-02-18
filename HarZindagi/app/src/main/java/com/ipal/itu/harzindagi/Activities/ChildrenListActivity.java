package com.ipal.itu.harzindagi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ipal.itu.harzindagi.Adapters.ChildListAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
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

        ChildInfoDao dao = new ChildInfoDao();
       final List<ChildInfo> data = dao.getAll();
        if (data != null) {

            ListView listView = (ListView) findViewById(R.id.childrenListActivityListView);
            ChildListAdapter childListAdapter = new ChildListAdapter(this, R.layout.listactivity_row, data, app_name);
            listView.setAdapter(childListAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    Intent intent = new Intent(ChildrenListActivity.this, VaccinationActivity.class);

                    intent.putExtra("childID", data.get(position).epi_number);
                    startActivity(intent);

                }
            });
        }
    }

}

package com.ipal.itu.harzindagi.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

       /* ChildInfoDao dao = new ChildInfoDao();
        //if( ChildInfoDao.getAll() == null )
        //{
            dao.save("112211" , "Big Baby" , "Big Man" , "121212" ,"3520057616317" , "03314960870" , "House Number 1486/2-C, Nagi Road Lahore Cantt");
        //    dao.save("112222" , "Small Baby" , "Small Man", "232323" , "3520057616318" , "03314960871" , "House Number 1487/2-C, Nagi Road Lahore Cantt");
        //    dao.save("112233" , "Huge Baby" , "Huge Man" , "343434" , "3520057616319" , "03314960872" , "House Number 1488/2-C, Nagi Road Lahore Cantt");
        //}*/

        List<ChildInfo> data = ChildInfoDao.getAll();

        ListView listView = (ListView) findViewById(R.id.childrenListActivityListView);
        ChildListAdapter childListAdapter = new ChildListAdapter(this, R.layout.listactivity_row, data,app_name);
        listView.setAdapter(childListAdapter);
    }

}

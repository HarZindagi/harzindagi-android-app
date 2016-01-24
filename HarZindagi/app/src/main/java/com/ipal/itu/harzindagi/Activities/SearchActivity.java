package com.ipal.itu.harzindagi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ipal.itu.harzindagi.R;

public class SearchActivity extends AppCompatActivity {

    EditText childID;
    EditText cellPhone;
    EditText cnic;
    EditText childName;
    EditText guardianName;
    Button searchButton;

    String ChildID , CellPhone, CNIC, ChildName, GuardianName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        childID = (EditText) findViewById(R.id.searchActivityChildID);
        ChildID = childID.getText().toString();

        cellPhone = (EditText) findViewById(R.id.searchActivityCellPhone);
        CellPhone = cellPhone.getText().toString();

        cnic = (EditText) findViewById(R.id.searchActivityCNIC);
        CNIC = cnic.getText().toString();

        childName = (EditText) findViewById(R.id.searchActivityChildName);
        ChildName = childName.getText().toString();

        guardianName = (EditText) findViewById(R.id.searchActivityGuardianName);
        GuardianName = guardianName.getText().toString();

        searchButton = (Button) findViewById(R.id.searchActivitySearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        
                    startActivity(new Intent(SearchActivity.this , ChildrenListActivity.class)
                                .putExtra("ChildID" , ChildID)
                                .putExtra("CellPhone", CellPhone)
                                .putExtra("CNIC", CNIC)
                                .putExtra("ChildName", ChildName)
                                .putExtra("GuardianName", GuardianName));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.ipal.itu.harzindagi.Activities;

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

    EditText cellPhone;
    EditText cnic;
    EditText childName;
    EditText guardianName;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cellPhone = (EditText) findViewById(R.id.searchActivityCellPhone);
        String CellPhone = cellPhone.getText().toString();

        cnic = (EditText) findViewById(R.id.searchActivityCNIC);
        String CNIC = cnic.getText().toString();

        childName = (EditText) findViewById(R.id.searchActivityChildName);
        String ChildName = childName.getText().toString();

        guardianName = (EditText) findViewById(R.id.searchActivityGuardianName);
        String GuardianName = guardianName.getText().toString();

        searchButton = (Button) findViewById(R.id.searchActivitySearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

package com.ipal.itu.harzindagi.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ipal.itu.harzindagi.R;

public class SearchActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    /**/
    public static final String TAG = "MainActivity";
    private static final int REQUEST_SMS = 1;
    private static String[] PERMISSIONS_SMS = {Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};
    EditText childID;
    EditText cellPhone;
    EditText cnic;
    EditText childName;
    EditText guardianName;
    Button searchButton;
    Button sendBtn, recive;
    EditText txt_msg;

    String number;
    String ChildID, CellPhone, CNIC, ChildName, GuardianName;
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        childID = (EditText) findViewById(R.id.searchActivityChildID);


        cellPhone = (EditText) findViewById(R.id.searchActivityCellPhone);


        cnic = (EditText) findViewById(R.id.searchActivityCNIC);

        childName = (EditText) findViewById(R.id.searchActivityChildName);


        guardianName = (EditText) findViewById(R.id.searchActivityGuardianName);


        searchButton = (Button) findViewById(R.id.searchActivitySearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValues();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                startActivity(new Intent(SearchActivity.this, ChildrenListActivity.class)
                        .putExtra("ChildID", ChildID)
                        .putExtra("CellPhone", CellPhone)
                        .putExtra("CNIC", CNIC)
                        .putExtra("ChildName", ChildName)
                        .putExtra("GuardianName", GuardianName).putExtra("fromSMS",false));


            }
        });
        findViewById(R.id.searchOnSMS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValues();

                sendSMS("%" + ChildID + "%" + CellPhone + "%" + CNIC + "%" + ChildName + "%" + GuardianName);
            }
        });


    }
    public void  setValues(){
        ChildID = childID.getText().toString() ;
        if(ChildID.equals("")){
            ChildID = "N/A";
        }
        CellPhone = cellPhone.getText().toString();
        if(CellPhone.equals("")){
            CellPhone = "N/A";
        }
        CNIC = cnic.getText().toString();
        if(CNIC.equals("")){
            CNIC = "N/A";
        }
        ChildName = childName.getText().toString();
        if(ChildName.equals("")){
            ChildName = "N/A";
        }
        GuardianName = guardianName.getText().toString();
        if(GuardianName.equals("")){
            GuardianName = "N/A";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("permissions", permissions.length + ":" + grantResults.length);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void sendSMS(String msg) {


        if (ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Contacts permissions have not been granted.
            Log.i(TAG, "SMS permissions has NOT been granted. Requesting permissions.");
            requestContactsPermissions();

        } else {

            // Contact permissions have been granted. Show the contacts fragment.
            Log.i(TAG,
                    "SMS permissions have already been granted. send SMS");
            sendSMSMessage(msg);
        }


    }

    protected void sendSMSMessage(String msg) {
        Log.i("Send SMS", "");

        String txt = msg;
        number = "03214180972";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, txt, null, null);
  /*  Toast.makeText(getApplicationContext(), "SMS Sent", Toast.LENGTH_LONG).show();
    finish();*/
            // finish();
        } catch (Exception e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        }


    }

    public void displayExceptionMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void requestContactsPermissions() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_SMS)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.SEND_SMS)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECEIVE_SMS)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            Log.i(TAG,
                    "Displaying SMS permission rationale to provide additional context.");

            // Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(mLayout, "SMS permissions are needed to demonstrate access",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(SearchActivity.this, PERMISSIONS_SMS,
                                    REQUEST_SMS);
                        }
                    })
                    .show();
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(this, PERMISSIONS_SMS, REQUEST_SMS);
        }
        // END_INCLUDE(contacts_permission_request)
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

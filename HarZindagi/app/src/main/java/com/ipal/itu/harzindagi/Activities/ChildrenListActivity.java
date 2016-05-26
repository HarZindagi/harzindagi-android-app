package com.ipal.itu.harzindagi.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ipal.itu.harzindagi.Adapters.ChildListAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.GJson.GKidTransactionAry;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChildrenListActivity extends AppCompatActivity {
    private static final int REQUEST_SMS = 1;
    private static String[] PERMISSIONS_SMS = {Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};
    String app_name;
    int selectedPosition = 0;
    boolean child_data = false;
    int bookid;
    public static ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        app_name = getResources().getString(R.string.app_name);
        boolean fromSMS = getIntent().getBooleanExtra("fromSMS", false);
        if(getIntent().hasExtra("bookid")){
            bookid = getIntent().getIntExtra("bookid", 0);
        }
        child_data = getIntent().getBooleanExtra("child_data", false);
        final boolean isOnline = getIntent().getBooleanExtra("isOnline", false);

        if (!fromSMS) {


            if (SearchActivity.data != null) {
                if (SearchActivity.data.size() == 0) {
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

                        selectedPosition = position;
                        if (!child_data) {
                            Intent intent = new Intent(getApplication(), VaccinationActivity.class);
                            long kid = 0;
                            int size = 0;

                            kid = SearchActivity.data.get(position).kid_id;
                            size = ChildInfoDao.getByKId(kid).size();


                            if (size != 0) {
                                if (isOnline) {
                                    getVaccinations(kid);
                                } else {

                                   /* Bundle bnd = KidVaccinationDao.get_visit_details_db(kid);
                                    intent.putExtra("childid", SearchActivity.data.get(position).epi_number);

                                    Bundle bnd = KidVaccinationDao.get_visit_details_db(kid);
                                    intent.putExtra("imei", SearchActivity.data.get(position).imei_number);
                                    intent.putExtra("childid", SearchActivity.data.get(position).kid_id);

                                    intent.putExtras(bnd);
                                    startActivity(intent);*/
                                    Intent myintent = new Intent(ChildrenListActivity.this, RegisteredChildActivity.class);
                                    myintent.putExtra("imei", SearchActivity.data.get(position).imei_number);
                                    myintent.putExtra("childid", SearchActivity.data.get(position).kid_id);
                                    myintent.putExtra("bookid",bookid);
                                    startActivity(myintent);
                                }
                            } else {
                                Toast.makeText(ChildrenListActivity.this, "No Record Found", Toast.LENGTH_LONG).show();
                            }


                        } else {
                            pDialog = new ProgressDialog(ChildrenListActivity.this);
                            pDialog.setMessage("Searching with SMS, Please Wait...");
                            pDialog.setCancelable(false);
                            sendSMS("hz %id%" + SearchActivity.data.get(position).kid_id);
                            Toast.makeText(ChildrenListActivity.this, "Please Wait", Toast.LENGTH_LONG).show();

                        }
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
            childInfo.kid_id = Long.parseLong(ID);
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

                  /*  Intent intent = new Intent(getApplication(), VaccinationActivity.class);
                    long kid = 0;

                    kid = list.get(position).kid_id;

                    Bundle bnd = KidVaccinationDao.get_visit_details_db(kid);
                    intent.putExtra("imei", list.get(position).imei_number);
                    intent.putExtra("childid", list.get(position).kid_id);
                    intent.putExtras(bnd);
                    startActivity(intent);*/
                    Intent myintent = new Intent(ChildrenListActivity.this, RegisteredChildActivity.class);
                    myintent.putExtra("imei", list.get(position).imei_number);
                    myintent.putExtra("childid", list.get(position).kid_id);
                    //myintent.putExtra("childid", list.get(position).epi_number);
                    myintent.putExtra("EPIname",list.get(position).epi_name );
                    startActivity(myintent);

                }

            });
        }

    }

    public void sendSMS(String msg) {


        if (ActivityCompat.checkSelfPermission(ChildrenListActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(ChildrenListActivity.this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(ChildrenListActivity.this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Contacts permissions have not been granted.
            //   Log.i(TAG, "SMS permissions has NOT been granted. Requesting permissions.");
            requestContactsPermissions();

        } else {

            // Contact permissions have been granted. Show the contacts fragment.
         /*   Log.i(TAG,
                    "SMS permissions have already been granted. send SMS");*/
            sendSMSMessage(msg);
        }


    }

    protected void sendSMSMessage(String msg) {
        Log.i("Send SMS", "");

        String txt = msg;
        String number = "9100";
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
            /*Log.i(TAG,
                    "Displaying SMS permission rationale to provide additional context.");*/

            // Display a SnackBar with an explanation and a button to trigger the request.
          /*  Snackbar.make(mLayout, "SMS permissions are needed to demonstrate access",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(SearchActivity.this, PERMISSIONS_SMS,
                                    REQUEST_SMS);
                        }
                    })
                    .show();*/
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(this, PERMISSIONS_SMS, REQUEST_SMS);
        }
        // END_INCLUDE(contacts_permission_request)
    }

    public void getVaccinations(final long kid) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.vaccinationsItems + kid + "/by_kid_id?" + "user[auth_token]=" + Constants.getToken(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Vaccinations");
        pDialog.show();
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        //  Log.d(TAG, response.toString());
                        pDialog.hide();
                        parseVaccs(response, kid);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                pDialog.hide();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }

        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    private void parseVaccs(JSONArray response, long kid) {

        Gson gson = new Gson();
        GKidTransactionAry gVisitAry = gson.fromJson("{\"kidVaccinations\":" + response.toString() + "}", GKidTransactionAry.class);
        String imei = Constants.getIMEI(this);
        ArrayList<KidVaccinations> vaccsList = new ArrayList<>();
        for (int i = 0; i < gVisitAry.kidVaccinations.size(); i++) {
            KidVaccinations vaccs = new KidVaccinations();
            vaccs.location = gVisitAry.kidVaccinations.get(i).location;
            vaccs.kid_id = gVisitAry.kidVaccinations.get(i).kid_id;
            vaccs.image = gVisitAry.kidVaccinations.get(i).image_path;
            vaccs.vaccination_id = gVisitAry.kidVaccinations.get(i).vaccination_id;
            vaccs.created_timestamp = gVisitAry.kidVaccinations.get(i).created_timestamp;
            vaccs.imei_number = gVisitAry.kidVaccinations.get(i).imei_number;
            if (imei.equals(gVisitAry.kidVaccinations.get(i).imei_number)) {
                vaccs.guest_imei_number = "";
            } else {
                vaccs.guest_imei_number = imei;
            }

            vaccs.is_sync = true;

            vaccsList.add(vaccs);
        }

        KidVaccinationDao kidVaccinationDao = new KidVaccinationDao();
        List<KidVaccinations> items = kidVaccinationDao.getById(kid);
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).is_sync == true) {
                items.get(i).delete();
            }
        }
        kidVaccinationDao.bulkInsert(vaccsList);
       /* Intent intent = new Intent(getApplication(), VaccinationActivity.class);
        Bundle bnd = KidVaccinationDao.get_visit_details_db(kid);
        intent.putExtra("childid", SearchActivity.data.get(selectedPosition).kid_id);
        intent.putExtra("isSync", true);
        intent.putExtras(bnd);
        startActivity(intent);*/
        Intent myintent = new Intent(ChildrenListActivity.this, RegisteredChildActivity.class);
        myintent.putExtra("childid",  SearchActivity.data.get(selectedPosition).kid_id);
        myintent.putExtra("imei",  SearchActivity.data.get(selectedPosition).imei_number);
        startActivity(myintent);

    }


}

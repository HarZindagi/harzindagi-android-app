package com.ipal.itu.harzindagi.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.ChildInfoSyncHandler;
import com.ipal.itu.harzindagi.Utils.Constants;
import com.ipal.itu.harzindagi.Utils.ImageUploadHandler;
import com.ipal.itu.harzindagi.Utils.ImageUploader;
import com.ipal.itu.harzindagi.Utils.KidVaccinatioHandler;
import com.ipal.itu.harzindagi.Utils.MultipartUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DashboardActivity extends AppCompatActivity {

    Button registerChildButton;
    Button scanChildButton;
    Button searchChildButton;
    Button allChildrenInUCButton;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    public static String getApplicationName(Context context) {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerChildButton = (Button) findViewById(R.id.dashBoardActivityRegisterChildButton);
        registerChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DashboardActivity.this, RegisterChildActivity.class));

            }
        });

        scanChildButton = (Button) findViewById(R.id.dashBoardActivityScanChildButton);
        scanChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DashboardActivity.this, Card_Scan.class));

            }
        });

        searchChildButton = (Button) findViewById(R.id.dashBoardActivitySearchChildButton);
        searchChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DashboardActivity.this, SearchActivity.class));
            }
        });

        allChildrenInUCButton = (Button) findViewById(R.id.dashBoardActivityShowAllChildrenButton);
        allChildrenInUCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashboardActivity.this, ViewPagerWithTabs.class);
                startActivity(intent);
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

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_logout) {
            // logout();
            Constants.setCheckOut(this, (Calendar.getInstance().getTimeInMillis() / 1000) + "");
            if (!Constants.getCheckIn(this).equals("")) {
                sendCheckIn();
            } else {
                Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
            }


           /* Toast.makeText(this,"Success",Toast.LENGTH_LONG).show();
            Intent mStartActivity = new Intent(this, LoginActivity.class);
            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            System.exit(0);
            return true;*/
        }
        if (id == R.id.action_sync) {
            if (Constants.isOnline(this)) {
                syncData();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //  startActivity(new Intent(this, HomeActivity.class));
        super.onBackPressed();
    }

    public void syncData() {

        List<ChildInfo> childInfo = ChildInfoDao.getNotSync();

        ChildInfoSyncHandler childInfoSyncHandler = new ChildInfoSyncHandler(this, childInfo, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String response) {

                androidImageUpload();
            }
        });
        childInfoSyncHandler.execute();
    }

    public void androidImageUpload() {
        List<ChildInfo> childInfos = ChildInfoDao.getImageNotSync();
        ImageUploadHandler imageUploadHandler = new ImageUploadHandler(this, childInfos, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String reponse) {

                syncVaccinaition();
            }
        });
        imageUploadHandler.execute();

    }

    public void syncVaccinaition() {
        KidVaccinationDao kidVaccinationDao = new KidVaccinationDao();
        List<KidVaccinations> kids = kidVaccinationDao.getNoSync();
      /*  if (kids.size() == 0) {
            Toast.makeText(getApplicationContext(), "Data Already Sync", Toast.LENGTH_LONG).show();
            return;
        }*/
        KidVaccinatioHandler kidVaccinatioHandler = new KidVaccinatioHandler(this, kids, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String reponse) {
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
            }
        });
        kidVaccinatioHandler.execute();
    }


    public void logout() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.logout + "?" + "user[auth_token]=" + Constants.getToken(this) + "location=" + "2.0221,5.0252";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                url, new JSONObject(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        //Log.d(TAG, response.toString());
                        pDialog.hide();
                        if (response.optBoolean("success")) {
                            Constants.setToken(getApplication(), "");
                            finish();
                            startActivity(new Intent(getApplication(), LoginActivity.class));
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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

// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    private void sendCheckIn() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.checkins;
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Saving CheckIn Time...");
        pDialog.show();
        JSONObject obj = null;

        try {
            obj = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("auth_token", Constants.getToken(this));
            obj.put("user", user);

            obj.put("imei_number", Constants.getIMEI(this));
            obj.put("location", Constants.getLocation(this));


            obj.put("version_name", Constants.getVersionName(this));
            obj.put("created_timestamp", Constants.getCheckIn(this));
            obj.put("upload_timestamp", (Calendar.getInstance().getTimeInMillis() / 1000) + "");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Log.d("response",response.toString());
                        if (!response.toString().equals("")) {
                            pDialog.hide();

                            if (!Constants.getCheckOut(DashboardActivity.this).equals("")) {
                                sendCheckOut();
                            }
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DashboardActivity.this, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);
    }

    private void sendCheckOut() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.checkouts;
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Saving CheckOut Time...");
        pDialog.show();
        JSONObject obj = null;

        try {
            obj = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("auth_token", Constants.getToken(this));
            obj.put("user", user);

            obj.put("imei_number", Constants.getIMEI(this));
            obj.put("location", Constants.getLocation(this));


            obj.put("version_name", Constants.getVersionName(this));
            obj.put("created_timestamp", Constants.getCheckOut(this));
            obj.put("upload_timestamp", (Calendar.getInstance().getTimeInMillis() / 1000) + "");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Log.d("response",response.toString());
                        if (!response.toString().equals("")) {
                            pDialog.hide();

                            Constants.setCheckOut(DashboardActivity.this, "");
                            Constants.setCheckIn(DashboardActivity.this, "");
                            uploadKitStationImage();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);
    }

    private void uploadKitStationImage() {
        String imagePath  = "/sdcard/" +  Constants.getApplicationName(this) + "/"
                + "Image_"+ Constants.getUC(this) + ".jpg";
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Saving Kit Station Image...");
        pDialog.show();
        MultipartUtility multipart = new MultipartUtility(Constants.photos, "UTF-8", new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String reponse) {
                pDialog.hide();
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
            }
        });

        multipart.execute(imagePath);
    }

}
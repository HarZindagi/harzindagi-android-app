package com.ipal.itu.harzindagi.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.EvaccsDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.*;
import com.ipal.itu.harzindagi.Entity.Evaccs;
import com.ipal.itu.harzindagi.GJson.GChildInfoAry;
import com.ipal.itu.harzindagi.GJson.GKidTransactionAry;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.ChildInfoSyncHandler;
import com.ipal.itu.harzindagi.Utils.Constants;
import com.ipal.itu.harzindagi.Utils.EvaccsSyncHandler;
import com.ipal.itu.harzindagi.Utils.EvacssImageUploadHandler;
import com.ipal.itu.harzindagi.Utils.ImageUploadHandler;
import com.ipal.itu.harzindagi.Utils.KidVaccinatioHandler;
import com.ipal.itu.harzindagi.Utils.MultipartUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
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
        if (id == R.id.action_download) {
            if (Constants.isOnline(this)) {
                showAlertDialog();
            }
        }

        return super.onOptionsItemSelected(item);
    }
   private  void  showAlertDialog(){
       AlertDialog.Builder adb = new AlertDialog.Builder(this);

       adb.setTitle("کیا آپ ڈیٹا ڈاونلوڈ کرنا چاہحتے ہیں؟");


       adb.setIcon(R.drawable.info_circle);


       adb.setPositiveButton("ہاں", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
               loadChildData();
               dialog.dismiss();

           } });


       adb.setNegativeButton("نہیں", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {

               dialog.dismiss();
           } });
       adb.show();
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

        KidVaccinatioHandler kidVaccinatioHandler = new KidVaccinatioHandler(this, kids, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String reponse) {
                syncEvaccsData();
            }
        });
        kidVaccinatioHandler.execute();
    }

    public void syncEvaccsData() {

        List<com.ipal.itu.harzindagi.Entity.Evaccs> childInfo = EvaccsDao.getAll();

        EvaccsSyncHandler childInfoSyncHandler = new EvaccsSyncHandler(DashboardActivity.this, childInfo, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String response) {

                androidEvaccsImageUpload();
            }
        });
        childInfoSyncHandler.execute();
    }
    public void androidEvaccsImageUpload() {
        List<com.ipal.itu.harzindagi.Entity.Evaccs> childInfo = EvaccsDao.getDistinct();
        EvacssImageUploadHandler  imageUploadHandler = new EvacssImageUploadHandler(this, childInfo, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String reponse) {
                List<Evaccs> list = EvaccsDao.getAll();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).delete();
                }
                Toast.makeText(DashboardActivity.this,"آپلوڈ مکمل ہو گیا ہے",Toast.LENGTH_LONG).show();
            }
        });
        imageUploadHandler.execute();

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

    ///Download data from server

    private void loadChildData() {
        // Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.kids + "?" + "user[auth_token]=" + Constants.getToken(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Child data...");
        pDialog.show();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        pDialog.hide();
                        if (response.toString().contains("Invalid User")) {
                            Toast.makeText(DashboardActivity.this, "Token Expired", Toast.LENGTH_LONG).show();
                            return;
                        }
                        JSONObject json = null;
                        try {
                            json = new JSONObject("{\"childInfoArrayList\":" + response + "}");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        parseKidReponse(json);


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
    public void parseKidReponse(JSONObject response) {

        Gson gson = new Gson();
        GChildInfoAry obj = gson.fromJson(response.toString(), GChildInfoAry.class);
        if(obj.childInfoArrayList.size()==0){
            return;
        }
        ArrayList<ChildInfo> childInfoArrayList = new ArrayList<>();
        for (int i = 0; i < obj.childInfoArrayList.size(); i++) {
            ChildInfo c = new ChildInfo();
            c.mobile_id = obj.childInfoArrayList.get(i).id;


            c.kid_name = obj.childInfoArrayList.get(i).kid_name;
            c.guardian_name = obj.childInfoArrayList.get(i).father_name;

            c.guardian_cnic = obj.childInfoArrayList.get(i).father_cnic;

            c.phone_number = obj.childInfoArrayList.get(i).phone_number;
            c.next_due_date = obj.childInfoArrayList.get(i).next_due_date;

            c.date_of_birth = Constants.getFortmattedDate( Long.parseLong(obj.childInfoArrayList.get(i).date_of_birth));
            c.location = obj.childInfoArrayList.get(i).location;
            c.child_address = obj.childInfoArrayList.get(i).child_address;
            if (obj.childInfoArrayList.get(i).gender == true) {
                c.gender = 1;
            } else {
                c.gender = 0;
            }
            c.epi_number = obj.childInfoArrayList.get(i).epi_number;
            c.epi_name = obj.childInfoArrayList.get(i).itu_epi_number;
            c.record_update_flag = true;
            c.book_update_flag = true;
            c.image_path ="image_"+obj.childInfoArrayList.get(i).id;//obj.childInfoArrayList.get(i).image_path;

            childInfoArrayList.add(c);
        }
        ChildInfoDao childInfoDao = new ChildInfoDao();
        List<ChildInfo> noSync = ChildInfoDao.getNotSync();
        childInfoDao.deleteTable();
        childInfoDao.bulkInsert(childInfoArrayList);
        childInfoDao.bulkInsert(noSync);
        loadKidVaccination();
        //  setViewPagger();
    }
    private void loadKidVaccination() {
        // Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.kid_vaccinations + "?" + "user[auth_token]=" + Constants.getToken(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Vaccination data...");
        pDialog.show();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        pDialog.hide();
                        if (response.toString().contains("Invalid User")) {
                            Toast.makeText(DashboardActivity.this, "Token Expired", Toast.LENGTH_LONG).show();
                            return;
                        }
                        JSONObject json = null;
                        try {
                            json = new JSONObject("{\"kidVaccinations\":" + response + "}");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        parseVaccinationReponse(json);


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
    public void parseVaccinationReponse(JSONObject response) {

        Gson gson = new Gson();
        GKidTransactionAry obj = gson.fromJson(response.toString(), GKidTransactionAry.class);
        if(obj.kidVaccinations.size()==0){
            return;
        }
        ArrayList<KidVaccinations> childInfoArrayList = new ArrayList<>();
        for (int i = 0; i < obj.kidVaccinations.size(); i++) {
            KidVaccinations c = new KidVaccinations();
            c.location = obj.kidVaccinations.get(i).location;


            c.mobile_id = obj.kidVaccinations.get(i).kid_id;
            c.kid_id = obj.kidVaccinations.get(i).kid_id;

            c.vaccination_id = obj.kidVaccinations.get(i).vaccination_id;

            c.image = obj.kidVaccinations.get(i).image_path;

            c.created_timestamp = obj.kidVaccinations.get(i).created_timestamp;

            c.is_sync = true;

            childInfoArrayList.add(c);

        }
        KidVaccinationDao kidVaccinationDao = new KidVaccinationDao();
        List<KidVaccinations> noSync = kidVaccinationDao.getNoSync();
        kidVaccinationDao.deleteTable();
        kidVaccinationDao.bulkInsert(childInfoArrayList);
        kidVaccinationDao.bulkInsert(noSync);

        Toast.makeText(DashboardActivity.this,"ڈاونلوڈ مکمل ہو گیا ہے",Toast.LENGTH_LONG).show();
    }
}
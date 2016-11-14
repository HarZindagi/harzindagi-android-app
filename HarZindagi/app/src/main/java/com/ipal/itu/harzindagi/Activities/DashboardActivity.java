package com.ipal.itu.harzindagi.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.LocationAjaxCallback;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.EvaccsDao;
import com.ipal.itu.harzindagi.Dao.EvaccsNonEPIDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.Books;
import com.ipal.itu.harzindagi.Entity.CheckIn;
import com.ipal.itu.harzindagi.Entity.CheckOut;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.Evaccs;
import com.ipal.itu.harzindagi.Entity.EvaccsNonEPI;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Entity.UpdateChildInfo;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.BooksSyncHandler;
import com.ipal.itu.harzindagi.Utils.CheckInSyncHandler;
import com.ipal.itu.harzindagi.Utils.CheckOutSyncHandler;
import com.ipal.itu.harzindagi.Utils.ChildInfoSyncHandler;
import com.ipal.itu.harzindagi.Utils.Constants;
import com.ipal.itu.harzindagi.Utils.EvaccsNonEPISyncHandler;
import com.ipal.itu.harzindagi.Utils.EvaccsSyncHandler;
import com.ipal.itu.harzindagi.Utils.EvacssImageUploadHandler;
import com.ipal.itu.harzindagi.Utils.EvacssNonEPIImageUploadHandler;
import com.ipal.itu.harzindagi.Utils.GetCurrentLocation;
import com.ipal.itu.harzindagi.Utils.ImageUploadHandler;
import com.ipal.itu.harzindagi.Utils.KidVaccinatioHandler;
import com.ipal.itu.harzindagi.Utils.MultipartUtility;
import com.ipal.itu.harzindagi.Utils.UpdateChildInfoSyncHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DashboardActivity extends BaseActivity {
    TextView toolbar_title;
    String location = "0.0000,0.0000";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    long uploadTime;
    boolean isLocationFound = false;

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
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.title_activity_dashboard));
        ((RippleView) findViewById(R.id.dashBoardActivityRegisterChildButtonR)).setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                startActivity(new Intent(DashboardActivity.this, RegisterChildActivity.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }

        });

        ((RippleView) findViewById(R.id.dashBoardActivityScanChildButtonR)).setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                startActivity(new Intent(DashboardActivity.this, Card_Scan.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }

        });

        ((RippleView) findViewById(R.id.dashBoardActivitySearchChildButtonR)).setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                startActivity(new Intent(DashboardActivity.this, SearchActivity.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }

        });
        ((RippleView) findViewById(R.id.dashBoardActivityShowAllChildrenButtonR)).setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                startActivity(new Intent(DashboardActivity.this, ViewPagerWithTabs.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
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

            if (!Constants.getCheckIn(this).equals("") && Constants.getCheckOut(this).equals("")) {
                isLocationFound = false;
                LocationAjaxCallback cb = new LocationAjaxCallback();
                //  final ProgressDialog pDialog = new ProgressDialog(this);
                //  pDialog.setMessage("Getting Location");

                cb.weakHandler(this, "locationCb").timeout(20 * 1000).expire(1000 * 30 * 5).async(this);

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
                uploadTime = Calendar.getInstance().getTimeInMillis() / (1000);
                getCurrentLocation();

            } else {
                Toast.makeText(DashboardActivity.this, "No Internet!", Toast.LENGTH_LONG).show();
            }
        }
      /*  if (id == R.id.action_download) {
            if (Constants.isOnline(this)) {
                showAlertDialog();
            }
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void locationCb(String url, final Location loc, AjaxStatus status) {
        String created_time = "" + (Calendar.getInstance().getTimeInMillis() / 1000);
        CheckOut checkOut = new CheckOut();
        checkOut.created_timestamp = created_time;
        // checkOut.location =;


        Constants.setCheckOut(this, created_time);
        if (!isLocationFound) {
            isLocationFound = true;
            if (loc != null) {

                double lat = loc.getLatitude();
                double log = loc.getLongitude();
                location = lat + "," + log;
                Constants.setLocationSync(this, location);
                Constants.setLocationInPref(this,location);
                checkOut.location = location;
                checkOut.save();

            } else {
               String loca= Constants.getLocationInPref(this);
                Constants.setLocationSync(this, loca);
                checkOut.location = loca;
                checkOut.save();
                //sendCheckIn();

            }
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("کیا آپ ڈیٹا ڈاونلوڈ کرنا چاہحتے ہیں؟");


        adb.setIcon(R.drawable.info_circle);


        adb.setPositiveButton("ہاں", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });


        adb.setNegativeButton("نہیں", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        adb.show();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    public void syncData() {

        List<ChildInfo> childInfo = ChildInfoDao.getNotSync();

        ChildInfoSyncHandler childInfoSyncHandler = new ChildInfoSyncHandler(this, childInfo, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String response) {
                if (success) {
                    updateChildInfo();
                } else {
                    Constants.sendGAEvent(DashboardActivity.this, "Error", "Uploading Failed", "Child Record", 0);
                    showErrorDialog();
                }
            }
        });
        childInfoSyncHandler.execute();
    }
    public void updateChildInfo() {

        List<UpdateChildInfo> childInfo = UpdateChildInfo.getNotSync();

        UpdateChildInfoSyncHandler childInfoSyncHandler = new UpdateChildInfoSyncHandler(this, childInfo, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String response) {
                if (success) {
                    books();
                } else {
                    Constants.sendGAEvent(DashboardActivity.this, "Error", "Uploading Failed", "Child Record", 0);
                    showErrorDialog();
                }
            }
        });
        childInfoSyncHandler.execute();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public void books() {

        List<Books> childInfo = Books.getNotSync();

        BooksSyncHandler childInfoSyncHandler = new BooksSyncHandler(this, childInfo, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String response) {
                if (success) {
                    androidImageUpload();
                } else {
                    Constants.sendGAEvent(DashboardActivity.this, "Error", "Uploading Failed", "Child Books", 0);
                    showErrorDialog();
                }
            }
        });
        childInfoSyncHandler.execute();
    }

    public void checkIn() {

        List<CheckIn> childInfo = CheckIn.getNotSync();

        CheckInSyncHandler checkInSyncHandler = new CheckInSyncHandler(this, childInfo, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String response) {
                if (success) {
                    checkOut();
                } else {
                    Constants.sendGAEvent(DashboardActivity.this, "Error", "Uploading Failed", "CheckIn", 0);
                    showErrorDialog();
                }
            }
        });
        checkInSyncHandler.execute();
    }

    public void checkOut() {

        List<CheckOut> childInfo = CheckOut.getNotSync();

        CheckOutSyncHandler checkOutSyncHandler = new CheckOutSyncHandler(this, childInfo, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String response) {
                if (success) {
                    //androidImageUpload();
                    Constants.setCheckOut(DashboardActivity.this, "");
                    Constants.setCheckIn(DashboardActivity.this, "");
                    uploadKitStationImage();
                } else {
                    Constants.sendGAEvent(DashboardActivity.this, "Error", "Uploading Failed", "CHeckOut", 0);
                    showErrorDialog();

                }
            }
        });
        checkOutSyncHandler.execute();
    }

    public void androidImageUpload() {
        List<ChildInfo> childInfos = ChildInfoDao.getImageNotSync();
        ImageUploadHandler imageUploadHandler = new ImageUploadHandler(this, childInfos, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String reponse) {
                if (success) {
                    syncVaccinaition();
                } else {
                    Constants.sendGAEvent(DashboardActivity.this, "Error", "Uploading Failed", "Images Upload", 0);
                    showErrorDialog();
                }
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
                if (success) {
                    syncEvaccsData();
                } else {
                    Constants.sendGAEvent(DashboardActivity.this, "Error", "Uploading Failed", "Vaccinations", 0);
                    showErrorDialog();
                }

            }
        });
        kidVaccinatioHandler.execute();
    }

    public void syncEvaccsData() {

        List<com.ipal.itu.harzindagi.Entity.Evaccs> childInfo = EvaccsDao.getNoSync();

        EvaccsSyncHandler childInfoSyncHandler = new EvaccsSyncHandler(DashboardActivity.this, childInfo, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String response) {
                if (success) {

                    syncEvaccsNonEPIData();

                } else {
                    Constants.sendGAEvent(DashboardActivity.this, "Error", "Uploading Failed", "Evaccs", 0);
                    showErrorDialog();
                }

            }
        });
        childInfoSyncHandler.execute();
    }

    public void syncEvaccsNonEPIData() {

        List<com.ipal.itu.harzindagi.Entity.EvaccsNonEPI> childInfo = EvaccsNonEPIDao.getNoSync();

        EvaccsNonEPISyncHandler childInfoSyncHandler = new EvaccsNonEPISyncHandler(DashboardActivity.this, childInfo, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String response) {
                if (success) {

                    androidEvaccsImageUpload();
                } else {
                    Constants.sendGAEvent(DashboardActivity.this, "Error", "Uploading Failed", "Evaccs Non EPI", 0);
                    showErrorDialog();
                }


            }
        });
        childInfoSyncHandler.execute();
    }

    public void androidEvaccsImageUpload() {
        List<com.ipal.itu.harzindagi.Entity.Evaccs> childInfo = EvaccsDao.getAll();
        List<com.ipal.itu.harzindagi.Entity.Evaccs> childInfoDistinc = new ArrayList<>();
        String preEpi = "";
        for (int i = 0; i < childInfo.size(); i++) {

            if (!preEpi.equals(childInfo.get(i).epi_number)) {
                childInfoDistinc.add(childInfo.get(i));
                preEpi = childInfo.get(i).epi_number;
            }
        }
        EvacssImageUploadHandler imageUploadHandler = new EvacssImageUploadHandler(this, childInfoDistinc, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String reponse) {

                if (success) {
                    List<Evaccs> list2 = EvaccsDao.getAll();
                    for (int i = 0; i < list2.size(); i++) {
                        list2.get(i).delete();
                    }
                    androidEvaccsNonEPIImageUpload();
                } else {
                    Constants.sendGAEvent(DashboardActivity.this, "Error", "Uploading Failed", "Evaccs Images", 0);
                    showErrorDialog();
                }


            }
        });
        imageUploadHandler.execute();

    }

    public void androidEvaccsNonEPIImageUpload() {
        List<com.ipal.itu.harzindagi.Entity.EvaccsNonEPI> childInfo = EvaccsNonEPIDao.getAll();
        List<com.ipal.itu.harzindagi.Entity.EvaccsNonEPI> childInfoDistinc = new ArrayList<>();
        String preEpi = "";
        for (int i = 0; i < childInfo.size(); i++) {
            if (!preEpi.equals(childInfo.get(i).epi_no)) {
                childInfoDistinc.add(childInfo.get(i));
                preEpi = childInfo.get(i).epi_no;
            }
        }
        EvacssNonEPIImageUploadHandler imageUploadHandler = new EvacssNonEPIImageUploadHandler(this, childInfoDistinc, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String reponse) {
                if (success) {

                    List<EvaccsNonEPI> list = EvaccsNonEPIDao.getAll();
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).delete();
                    }
                    uploadTime = (Calendar.getInstance().getTimeInMillis() / 1000) - uploadTime;
                    Constants.sendGAEvent(DashboardActivity.this, "Data Upload", Constants.getUserName(DashboardActivity.this), "Time", uploadTime);
                    if (!Constants.getCheckOut(DashboardActivity.this).equals("")) {
                        checkIn();


                    } else {
                        showCompletDialog("اپ لوڈ مکمل ہو گیا ہے");
                    }

                } else {
                    Constants.sendGAEvent(DashboardActivity.this, "Error", "Uploading Failed", "Evaccs Non EPI Images", 0);
                    showErrorDialog();
                }

            }
        });
        imageUploadHandler.execute();

    }
    private void getCurrentLocation(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("getting location ...");
        pDialog.setCancelable(false);
        pDialog.show();
        GetCurrentLocation.LocationResult locationResult = new GetCurrentLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                pDialog.dismiss();
                Constants.setLocationSync(DashboardActivity.this,location.getLatitude()+","+location.getLatitude());
                DashboardActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        syncData();
                    }
                });


            }
        };
        GetCurrentLocation myLocation = new GetCurrentLocation();
        myLocation.getLocation(this, locationResult);
    }

    private void showCompletDialog(String title) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle(title);


        adb.setIcon(R.drawable.info_circle);


        adb.setPositiveButton("ٹھیک ہے", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        adb.show();
    }

    private void showErrorDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("دوبارہ کوشش کریں");


        adb.setIcon(R.drawable.info_circle);


        adb.setPositiveButton("ہاں", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (Constants.isOnline(DashboardActivity.this)) {
                    uploadTime = Calendar.getInstance().getTimeInMillis() / (1000);

                   getCurrentLocation();
                } else {
                    Toast.makeText(DashboardActivity.this, "No Internet!", Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();

            }
        });


        adb.setNegativeButton("نہیں", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        adb.show();
    }


    private void uploadKitStationImage() {
        String imagePath = "/sdcard/" + Constants.getApplicationName(this) + "/"
                + "Image_" + Constants.getUCID(this) + ".jpg";
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Saving Kit Station Image...");
        pDialog.show();
        MultipartUtility multipart = new MultipartUtility(Constants.photos, "UTF-8", new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String reponse) {
                pDialog.dismiss();
                sendKitStationData();

            }
        });

        multipart.execute(imagePath);
    }

    private void sendKitStationData() {
        String imagePath = "/sdcard/" + Constants.getApplicationName(this) + "/"
                + "Image_" + Constants.getUC(this) + ".jpg";
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.kitStation;
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Saving Kit Station...");
        pDialog.show();
        JSONObject obj = null;
        final JSONObject kitStation = new JSONObject();
        try {
            obj = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("auth_token", Constants.getToken(this));
            obj.put("user", user);

            kitStation.put("imei_number", Constants.getIMEI(this));
            kitStation.put("location", Constants.getLocation(this));
            kitStation.put("location_source", "gps");
            obj.put("time_source", "network");
            kitStation.put("image_path ", imagePath);
            obj.put("is_deleted", 0);

            kitStation.put("created_timestamp", Constants.getKitTime(this));
            kitStation.put("upload_timestamp", (Calendar.getInstance().getTimeInMillis() / 1000) + "");
            obj.put("kid_station", kitStation);

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
                            pDialog.dismiss();
                            showCompletDialog("اپلوڈ مکمل ہو گیا ہے");
                            //Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
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
                LoginActivity.MAX_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);
    }
    ///Download data from server


}
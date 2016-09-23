package com.ipal.itu.harzindagi.Activities;

/**
 * Created by Wahab on 2/17/2016.
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.ipal.itu.harzindagi.Adapters.PagerAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.GJson.GChildInfoAry;
import com.ipal.itu.harzindagi.GJson.GKidTransactionAry;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewPagerWithTabs extends BaseActivity {
    TabLayout tabLayout;
    TextView toolbar_title;
    String[] titles = new String[]{"زیر غور", "ڈیفالٹر", "مکمل شدہ", "آج کا کام"};
    private long activityTime;

    public void logTime() {
        activityTime = (Calendar.getInstance().getTimeInMillis() / 1000) - activityTime;
        Constants.sendGAEvent(this, Constants.getUserName(this), Constants.GaEvent.ALL_UC_LIST_TIME, activityTime + " S", 0);

    }

    @Override
    public void onBackPressed() {

        Constants.sendGAEvent(this, Constants.getUserName(this), Constants.GaEvent.BACK_NAVIGATION, Constants.GaEvent.ALL_UC_BACK, 0);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_layout);
        activityTime = Calendar.getInstance().getTimeInMillis() / (1000);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("تمام بچوں کا ریکارڈ");
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setCustomView(getCustView(titles[0], R.drawable.yellew_rectangle)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getCustView(titles[1], R.drawable.red_rectangle)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getCustView(titles[2], R.drawable.green_rectangle)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getCustView(titles[3], R.drawable.blue_rectangle)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


        setViewPagger();
        setIndicatorColor();
    }

    public void setIndicatorColor() {
        try {
            Field field = TabLayout.class.getDeclaredField("mTabStrip");
            field.setAccessible(true);
            Object ob = field.get(tabLayout);
            Class<?> c = Class.forName("android.support.design.widget.TabLayout$SlidingTabStrip");
            Method method = c.getDeclaredMethod("setSelectedIndicatorColor", int.class);
            method.setAccessible(true);
            method.invoke(ob, Color.RED);//now its ok
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public View getCustView(String string, int res) {
        View v = LayoutInflater.from(this).inflate(R.layout.tab_item, null);
        TextView tv = (TextView) v.findViewById(R.id.item);
        // tv.setSelected(true);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        tv.setBackgroundResource(res);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.BLACK);
        tv.setText(string);
        return v;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sync_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.action_sync:
                if (Constants.isOnline(this)) {
                    showAlertDialog();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        logTime();
        super.onDestroy();
    }

    private void showAlertDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("کیا آپ ڈیٹا ڈاون لوڈ کرنا چاہتے ہیں؟");


        adb.setIcon(R.drawable.info_circle);


        adb.setPositiveButton("ہاں", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                loadChildData();
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

    private void setViewPagger() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

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
                            Toast.makeText(ViewPagerWithTabs.this, "Token Expired", Toast.LENGTH_LONG).show();
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                LoginActivity.MAX_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    public void parseKidReponse(JSONObject response) {

        Gson gson = new Gson();
        GChildInfoAry obj = gson.fromJson(response.toString(), GChildInfoAry.class);
        if (obj.childInfoArrayList.size() == 0) {
            return;
        }
        ArrayList<ChildInfo> childInfoArrayList = new ArrayList<>();
        for (int i = 0; i < obj.childInfoArrayList.size(); i++) {
            ChildInfo c = new ChildInfo();
            c.kid_id = obj.childInfoArrayList.get(i).id;

            c.kid_name = obj.childInfoArrayList.get(i).kid_name;
            c.guardian_name = obj.childInfoArrayList.get(i).father_name;

            c.guardian_cnic = obj.childInfoArrayList.get(i).father_cnic;

            c.phone_number = obj.childInfoArrayList.get(i).phone_number;
            c.next_due_date = obj.childInfoArrayList.get(i).next_due_date*1000;
            c.next_visit_date = obj.childInfoArrayList.get(i).next_visit_date*1000;
            c.image_update_flag = true;
            if (obj.childInfoArrayList.get(i).date_of_birth != null) {
                c.date_of_birth = Constants.getFortmattedDate(Long.parseLong(obj.childInfoArrayList.get(i).date_of_birth));
            }
            c.location = obj.childInfoArrayList.get(i).location;
            c.child_address = obj.childInfoArrayList.get(i).child_address;
            if (obj.childInfoArrayList.get(i).gender == true) {
                c.gender = 1;
            } else {
                c.gender = 0;
            }
            c.imei_number = obj.childInfoArrayList.get(i).imei_number;
            c.book_id = obj.childInfoArrayList.get(i).book_id;
            c.epi_number = obj.childInfoArrayList.get(i).epi_number;
            c.epi_name = obj.childInfoArrayList.get(i).itu_epi_number;
            c.record_update_flag = true;
            //c.book_update_flag = true;
            c.image_path = "image_" + obj.childInfoArrayList.get(i).id;//obj.childInfoArrayList.get(i).image_path;

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
                            Toast.makeText(ViewPagerWithTabs.this, "Token Expired", Toast.LENGTH_LONG).show();
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                LoginActivity.MAX_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    public void parseVaccinationReponse(JSONObject response) {

        Gson gson = new Gson();
        GKidTransactionAry obj = gson.fromJson(response.toString(), GKidTransactionAry.class);
        if (obj.kidVaccinations.size() == 0) {
            return;
        }
        ArrayList<KidVaccinations> childInfoArrayList = new ArrayList<>();
        for (int i = 0; i < obj.kidVaccinations.size(); i++) {
            KidVaccinations c = new KidVaccinations();
            c.location = obj.kidVaccinations.get(i).location;


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

        setViewPagger();
    }

}
package com.ipal.itu.harzindagi.Activities;

/**
 * Created by Wahab on 2/17/2016.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ipal.itu.harzindagi.Adapters.PagerAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewPagerWithTabs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Defaulter"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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
    private void loadChildData(String childID) {
        // Instantiate the RequestQueue.
        ChildInfoDao childInfoDao = new ChildInfoDao();
        List<ChildInfo> childInfo = childInfoDao.getById(childID);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.kids;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Saving Child data...");
        pDialog.show();
        JSONObject obj = null;
        try {
            obj = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("auth_token",Constants.getToken(this));
            obj.put("user", user);

            JSONObject kid = new JSONObject();
            kid.put("id",childInfo.get(0).id);
            kid.put("mobile_id",childInfo.get(0).id);
            kid.put("imei_number",Constants.getIMEI(this));
            kid.put("kid_name",childInfo.get(0).kid_name);
            kid.put("father_name",childInfo.get(0).guardian_name);
            kid.put("mother_name",childInfo.get(0).mother_name);
            kid.put("father_cnic",childInfo.get(0).guardian_cnic);
            kid.put("mother_cnic","");
            kid.put("phone_number",childInfo.get(0).phone_number);
            kid.put("date_of_birth",childInfo.get(0).date_of_birth);
            kid.put("location","00000,000000");
            kid.put("child_address","");
            kid.put("gender",childInfo.get(0).gender);
            kid.put("epi_number",childInfo.get(0).epi_number);
            kid.put("itu_epi_number",childInfo.get(0).epi_number+"_itu");

            obj.put("kid",kid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        // Log.d(TAG, response.toString());
                        pDialog.hide();
                        if (response.optBoolean("success")) {
                            JSONObject json = response.optJSONObject("data");
                            parseKidReponse(json);
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

    public void parseKidReponse(JSONObject response) {
        Gson gson = new Gson();
        // obj = gson.fromJson(response.toString(), GUserInfo.class);
    }
}
package com.ipal.itu.harzindagi.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ipal.itu.harzindagi.Adapters.CustomListAdapter;
import com.ipal.itu.harzindagi.R;

/**
 * Created by Wahab on 3/2/2016.
 */
public class VaccineList extends Activity {

    String rec_response;
    JSONObj obj;
    ListView list;
    CustomListAdapter adapter;

    String dummy_response=  "{\"vaccinfo\":[{\"day\":\"1\",\"month\":\"1\",\"year\":\"2016\",\"vac_type\":\"drops\",\"vac_name\":\"OPV-2\"},{\"day\":\"2\",\"month\":\"2\",\"year\":\"2016\",\"vac_type\":\"drops\",\"vac_name\":\"Penta-2\"}]}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacc_list_layout);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ""; // Add API here.

// Request a string response from the provided URL.
        TokenRequest stringRequest = new TokenRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        rec_response = response;
                        parseResponse();
                        //  text.setText("" + rec_response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "That didn't work!",
                        Toast.LENGTH_LONG).show();
            }


        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void parseResponse() {
        Gson gson = new Gson();
        obj = gson.fromJson(dummy_response, JSONObj.class); // CHANGE "dummy_response" WITH "rec_response"

        list = (ListView) findViewById(R.id.vacc_list);
        adapter = new CustomListAdapter(this, obj.vaccinfo);
        list.setAdapter(adapter);
    }
}

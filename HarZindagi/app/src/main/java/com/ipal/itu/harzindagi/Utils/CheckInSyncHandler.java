package com.ipal.itu.harzindagi.Utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ipal.itu.harzindagi.Activities.DashboardActivity;
import com.ipal.itu.harzindagi.Activities.LoginActivity;
import com.ipal.itu.harzindagi.Entity.Books;
import com.ipal.itu.harzindagi.Entity.CheckIn;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ali on 2/25/2016.
 */
public class CheckInSyncHandler {

    Context context;
    List<CheckIn> checkIns;
    ProgressDialog pDialog;
    OnUploadListner onUploadListner;
    int index = 0;
    Calendar calendar;

    public CheckInSyncHandler(Context context, List<CheckIn> checkIns, OnUploadListner onUploadListner) {
        this.checkIns = checkIns;
        this.context = context;
        this.onUploadListner = onUploadListner;
        calendar = Calendar.getInstance();
    }

    public void execute() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Saving CheckIn data...");
        pDialog.setCancelable(false);
        pDialog.show();
        if (checkIns.size() != 0) {
            sendChildData(checkIns.get(index));
        } else {
            pDialog.dismiss();
            onUploadListner.onUpload(true, "");
        }

    }

    private void nextUpload(boolean isUploaded) {
        if (isUploaded) {
            index++;
            if (index < checkIns.size()) {
                sendChildData(checkIns.get(index));
                pDialog.setMessage("Uploading CheckIn... " + index + " of " + checkIns.size());
            } else {
                onUploadListner.onUpload(true, "");
                pDialog.dismiss();
            }
        } else {
            onUploadListner.onUpload(false, "");
            pDialog.dismiss();
        }
    }


    private void sendChildData(final CheckIn checkIns) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url =Constants.checkins;;


        pDialog.setMessage("Saving CheckIn Time...");

        JSONObject obj = null;

        try {
            obj = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("auth_token", Constants.getToken(context));
            obj.put("user", user);


            obj.put("imei_number", Constants.getIMEI(context));
            obj.put("location", checkIns.location);

            obj.put("location_sync", Constants.getLocationSync(context));

            obj.put("version_name", Constants.getVersionName(context));
            obj.put("created_timestamp", checkIns.created_timestamp);
            obj.put("location_source",Constants.getLocationSync(context) );
            obj.put("time_source", (Calendar.getInstance().getTimeInMillis() / 1000) + "");
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
                            pDialog.dismiss();


                            checkIns.is_sync = true;
                            checkIns.save();
                            nextUpload(true);

                        } else {
                            nextUpload(false);
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

}

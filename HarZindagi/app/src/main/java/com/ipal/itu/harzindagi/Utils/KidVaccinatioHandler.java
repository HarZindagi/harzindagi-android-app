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
import com.google.gson.Gson;
import com.ipal.itu.harzindagi.Activities.LoginActivity;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ali on 2/28/2016.
 */
public class KidVaccinatioHandler {


    Context context;
    List<KidVaccinations> kidVaccinations;
    ProgressDialog pDialog;
    OnUploadListner onUploadListner;
    int index = 0;

    public KidVaccinatioHandler(Context context, List<KidVaccinations> kidVaccinations, OnUploadListner onUploadListner) {
        this.kidVaccinations = kidVaccinations;
        this.context = context;
        this.onUploadListner = onUploadListner;
    }

    public void execute() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Saving Child data...");
        pDialog.show();
        if (kidVaccinations.size() != 0) {
            sendVaccinationsData(kidVaccinations.get(index));
        } else {
            pDialog.dismiss();
            onUploadListner.onUpload(true,"");
        }

    }

    private void nextUpload(boolean isUploaded) {
        if (isUploaded) {
            index++;
            if (index < kidVaccinations.size()) {
                sendVaccinationsData(kidVaccinations.get(index));
                pDialog.setMessage("Uploading data... " + index + " of " + kidVaccinations.size());
            } else {
                onUploadListner.onUpload(true, "");
                pDialog.dismiss();
            }
        } else {
            onUploadListner.onUpload(false, "");
            pDialog.dismiss();
        }
    }


    private void sendVaccinationsData(final KidVaccinations kidVaccinations) {
        // Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Constants.kid_vaccinations;
        // final ProgressDialog pDialog = new ProgressDialog(this);
        //  pDialog.setMessage("Saving Vaccination data...");
        //  pDialog.show();
        JSONObject obj = null;

        try {
            obj = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("auth_token", Constants.getToken(context));
            obj.put("user", user);

            JSONObject vaccination = new JSONObject();

            vaccination.put("imei_number", kidVaccinations.imei_number);
            vaccination.put("guest_imei_number", kidVaccinations.guest_imei_number);
            vaccination.put("location", kidVaccinations.location);
            vaccination.put("kid_id", kidVaccinations.kid_id);
            vaccination.put("vaccination_id", kidVaccinations.vaccination_id);
            vaccination.put("version_name", "");
            vaccination.put("location_source", "00.00");
            vaccination.put("upload_timestamp", Calendar.getInstance().getTimeInMillis()/1000);
            vaccination.put("created_timestamp",kidVaccinations.created_timestamp);

            obj.put("kid_vaccination", vaccination);

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
                        // pDialog.hide();
                        if (!response.equals("")) {
                            kidVaccinations.is_sync= true;
                            kidVaccinations.save();
                            nextUpload(true);

                        }
                        if (response.optBoolean("success")) {
                            // JSONObject json = response.optJSONObject("data");
                            //parseKidReponse(json);
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                nextUpload(false);
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                LoginActivity.MAX_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    public void parseKidReponse(JSONObject response) {
        Gson gson = new Gson();
        // obj = gson.fromJson(response.toString(), GUserInfo.class);
    }
}

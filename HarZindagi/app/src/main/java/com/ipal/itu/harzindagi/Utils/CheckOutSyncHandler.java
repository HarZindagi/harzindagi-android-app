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
import com.ipal.itu.harzindagi.Activities.LoginActivity;
import com.ipal.itu.harzindagi.Entity.Books;
import com.ipal.itu.harzindagi.Entity.CheckIn;
import com.ipal.itu.harzindagi.Entity.CheckOut;
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
public class CheckOutSyncHandler {

    Context context;
    List<CheckOut> checkOuts;
    ProgressDialog pDialog;
    OnUploadListner onUploadListner;
    int index = 0;
    Calendar calendar;
    public CheckOutSyncHandler(Context context, List<CheckOut> checkOuts, OnUploadListner onUploadListner) {
        this.checkOuts = checkOuts;
        this.context = context;
        this.onUploadListner = onUploadListner;
        calendar = Calendar.getInstance();
    }

    public void execute() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Saving CheckOut data...");
        pDialog.setCancelable(false);
        pDialog.show();
        if(checkOuts.size()!=0){
            sendChildData(checkOuts.get(index));
        }else{
            pDialog.dismiss();
            onUploadListner.onUpload(true,"");
        }

    }

    private void nextUpload(boolean isUploaded) {
        if (isUploaded) {
            index++;
            if (index < checkOuts.size()) {
                sendChildData(checkOuts.get(index));
                pDialog.setMessage("Uploading CheckOut... " + index+ " of "+ checkOuts.size());
            } else {
                onUploadListner.onUpload(true,"");
                pDialog.dismiss();
            }
        } else {
            onUploadListner.onUpload(false, "");
            pDialog.dismiss();
        }
    }

    private void sendChildData(final CheckOut checkOut) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url =Constants.checkouts;


        pDialog.setMessage("Saving CheckOut Time...");


        JSONObject obj = null;

        try {
            obj = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("auth_token", Constants.getToken(context));

            obj.put("user", user);

            obj.put("imei_number", Constants.getIMEI(context));
            obj.put("location", checkOut.location);

            obj.put("location_sync", Constants.getLocationSync(context));

            obj.put("version_name", Constants.getVersionName(context));
            obj.put("created_timestamp", checkOut.created_timestamp);
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


                            checkOut.is_sync = true;
                            checkOut.save();
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

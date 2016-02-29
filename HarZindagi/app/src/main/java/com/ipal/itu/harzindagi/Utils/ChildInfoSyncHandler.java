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
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ali on 2/25/2016.
 */
public class ChildInfoSyncHandler {

    Context context;
    List<ChildInfo> childInfo;
    ProgressDialog pDialog;
    OnUploadListner onUploadListner;
    int index=0;

    public ChildInfoSyncHandler(Context context, List<ChildInfo> childInfo, OnUploadListner onUploadListner) {
        this.childInfo = childInfo;
        this.context = context;
        this.onUploadListner = onUploadListner;
    }

    public void execute() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Saving Child data...");
        pDialog.show();
        if(childInfo.size()!=0){
            sendChildData(childInfo.get(index));
        }else{
            pDialog.dismiss();

        }

    }

    private void nextUpload(boolean isUploaded) {
        if (isUploaded) {
            index++;
            if (index < childInfo.size()) {
                sendChildData(childInfo.get(index));
                pDialog.setMessage("Uploading data... " + index+ " of "+ childInfo.size());
            } else {
                onUploadListner.onUpload(true,"");
                pDialog.dismiss();
            }
        } else {
            onUploadListner.onUpload(false,"");
            pDialog.dismiss();
        }
    }

    private void sendChildData(final ChildInfo childInfo) {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Constants.kids;

        JSONObject obj = null;
        final JSONObject kid = new JSONObject();
        try {
            obj = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("auth_token", Constants.getToken(context));
            obj.put("user", user);


          //  kid.put("mobile_id", childInfo.mobile_id);
            kid.put("mobile_id", childInfo.mobile_id);
            kid.put("imei_number", Constants.getIMEI(context));
            kid.put("kid_name", childInfo.kid_name);
            kid.put("father_name", childInfo.guardian_name);
            kid.put("mother_name", childInfo.mother_name);
            kid.put("father_cnic", childInfo.guardian_cnic);
            kid.put("mother_cnic", "");
            kid.put("phone_number", childInfo.phone_number);
            kid.put("date_of_birth", childInfo.date_of_birth);
            kid.put("location", childInfo.location);
            kid.put("child_address", "");
            kid.put("gender", childInfo.gender);
            kid.put("epi_number", childInfo.epi_number);
            kid.put("itu_epi_number", childInfo.epi_number + "_itu");

            obj.put("kid", kid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                       // Log.d("response",response.toString());
                        if (response.optString("kid_name").equals(kid.optString("kid_name"))) {
                            ChildInfoDao childInfoDao = new ChildInfoDao();
                            List<ChildInfo> child = childInfoDao.getById(childInfo.mobile_id);
                            child.get(0).record_update_flag = true;
                            child.get(0).save();

                            nextUpload(true);

                        } else {
                            nextUpload(false);
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

}

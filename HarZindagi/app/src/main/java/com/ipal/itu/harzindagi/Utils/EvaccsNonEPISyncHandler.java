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
import com.ipal.itu.harzindagi.Dao.EvaccsDao;
import com.ipal.itu.harzindagi.Dao.EvaccsNonEPIDao;
import com.ipal.itu.harzindagi.Entity.Evaccs;
import com.ipal.itu.harzindagi.Entity.EvaccsNonEPI;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ali on 2/25/2016.
 */
public class EvaccsNonEPISyncHandler {

    Context context;
    List<EvaccsNonEPI> childInfo;
    ProgressDialog pDialog;
    OnUploadListner onUploadListner;
    int index=0;
    Calendar calendar;
    public EvaccsNonEPISyncHandler(Context context, List<EvaccsNonEPI> childInfo, OnUploadListner onUploadListner) {
        this.childInfo = childInfo;
        this.context = context;
        this.onUploadListner = onUploadListner;
        calendar = Calendar.getInstance();
    }

    public void execute() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Saving Child data...");
        pDialog.show();
        if(childInfo.size()!=0){
            sendChildData(childInfo.get(index));
        }else{
            pDialog.dismiss();
            onUploadListner.onUpload(true,"");
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
            onUploadListner.onUpload(false, "");
            pDialog.dismiss();
        }
    }
  public long componentTimeToTimestamp(int year, int month, int day, int hour, int minute) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTimeInMillis();
    }
    private void sendChildData(final EvaccsNonEPI childInfo) {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Constants.kids_evaccsNonEPI;

        JSONObject obj = null;
        final JSONObject kid = new JSONObject();
        Long tsLong =calendar.getTimeInMillis() / 1000;
        try {
            obj = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("auth_token", Constants.getToken(context));
            obj.put("user", user);

            kid.put("imei_number", Constants.getIMEI(context));
            kid.put("location", childInfo.location);
            kid.put("location_source", childInfo.location_source);
            kid.put("created_timestamp",childInfo.created_timestamp);
            kid.put("upload_timestamp",tsLong);
            kid.put("child_type", childInfo.child_type);
            kid.put("name", childInfo.name);
            kid.put("daily_reg_no",childInfo.daily_reg_no);
            kid.put("vaccination",childInfo.vaccination);
            kid.put("cnic",childInfo.cnic);
            kid.put("phone_number",childInfo.phone_number);
            kid.put("epi_no",childInfo.epi_no);
            kid.put("date_of_birth",childInfo.date_of_birth);
            kid.put("child_address",childInfo.child_address);
            kid.put("birth_place",childInfo.birth_place);



            obj.put("evacs_nonepi", kid);

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
                            EvaccsNonEPIDao childInfoDao = new EvaccsNonEPIDao();
                            List<EvaccsNonEPI> child = childInfoDao.getByEPINum(childInfo.epi_no);
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                LoginActivity.MAX_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);
    }
    public  void renameFile(String oldName,String newName){
       String Path = "/sdcard/" + Constants.getApplicationName(context) + "/";


        File from = new File(Path,oldName+ ".jpg");
        File to = new File(Path,newName+ ".jpg");
        from.renameTo(to);
    }

}

package com.ipal.itu.harzindagi.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ipal.itu.harzindagi.Activities.LoginActivity;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    Calendar calendar;
    public ChildInfoSyncHandler(Context context, List<ChildInfo> childInfo, OnUploadListner onUploadListner) {
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
    private void sendChildData(final ChildInfo childInfo) {
        final long oldKidID = childInfo.kid_id;
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Constants.kids;

        JSONObject obj = null;
        final JSONObject kid = new JSONObject();
        try {
            obj = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("auth_token", Constants.getToken(context));
            obj.put("user", user);


            kid.put("mobile_id", childInfo.kid_id);
            kid.put("imei_number",childInfo.imei_number);
            kid.put("kid_name", childInfo.kid_name);
            kid.put("father_name", childInfo.guardian_name);
            kid.put("mother_name", childInfo.mother_name);
            kid.put("father_cnic", childInfo.guardian_cnic);
            kid.put("mother_cnic", "");
            kid.put("phone_number", childInfo.phone_number);
            kid.put("created_timestamp",childInfo.created_timestamp);

            Long tsLong =calendar.getTimeInMillis() / 1000;
            kid.put("upload_timestamp",tsLong);
            DateFormat dfm = new SimpleDateFormat("dd-MMM-yyyy");
            Date date  = dfm.parse(childInfo.date_of_birth);
            dfm.getCalendar().setTime(date);
           // date.getTime();
            kid.put("date_of_birth", (date.getTime()/1000)+"");
            kid.put("location", childInfo.location);
            kid.put("child_address", childInfo.child_address);
            kid.put("gender", childInfo.gender);
            kid.put("epi_number", childInfo.epi_number);
            kid.put("itu_epi_number", childInfo.epi_number + "_itu");
            kid.put("image_path",childInfo.image_path);
            kid.put("next_due_date",childInfo.next_due_date);
            kid.put("book_id",childInfo.book_id);


            obj.put("kid", kid);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                       // Log.d("response",response.toString());
                        if (response.optString("kid_name").equals(kid.optString("kid_name"))) {

                            List<ChildInfo> child = ChildInfoDao.getByKId(childInfo.kid_id);
                            child.get(0).record_update_flag = true;
                            child.get(0).kid_id = response.optLong("id");
                            child.get(0).image_path ="image_"+child.get(0).kid_id;
                            child.get(0).save();
                            long kidID =   child.get(0).kid_id;

                            renameFile(child.get(0).kid_name+child.get(0).epi_number,"image_"+kidID);
                            List<KidVaccinations> kidVaccines = KidVaccinationDao.getById(oldKidID);
                            for (int i = 0; i < kidVaccines.size(); i++) {
                                kidVaccines.get(i).kid_id = kidID;
                                kidVaccines.get(i).save();
                            }

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

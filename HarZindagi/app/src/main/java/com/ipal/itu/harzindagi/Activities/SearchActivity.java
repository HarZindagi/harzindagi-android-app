package com.ipal.itu.harzindagi.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.UserInfoDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.GJson.GChildInfoAry;
import com.ipal.itu.harzindagi.GJson.GUserInfo;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;
import com.ipal.itu.harzindagi.Utils.ImageDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    /**/
    public static final String TAG = "MainActivity";
    private static final int REQUEST_SMS = 1;
    public static List<ChildInfo> data;
    private static String[] PERMISSIONS_SMS = {Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};
    EditText childID;
    EditText cellPhone;
    EditText cnic;
    Button searchButton;
    Button sendBtn, recive;
    EditText txt_msg;
    View searchOneLayout;
    View searchTwoLayout;
    String number;
    String ChildID, CellPhone, CNIC, ChildName, GuardianName;
    boolean isAdvanceSearch = false;
    private View mLayout;
    private PopupWindow pw;
    private View popUpView;
    private EditText newbookText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createContexMenu();
        searchOneLayout = findViewById(R.id.epiSearchLayout);
        searchTwoLayout = findViewById(R.id.advanceSearchLayout);

        childID = (EditText) findViewById(R.id.searchActivityChildID);
        newbookText = (EditText) findViewById(R.id.newbookText);

        cellPhone = (EditText) findViewById(R.id.searchActivityCellPhone);

        cnic = (EditText) findViewById(R.id.searchActivityCNIC);

        searchButton = (Button) findViewById(R.id.searchActivitySearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean inputValid = validateInput(isAdvanceSearch);
                ChildInfoDao childInfoDao = new ChildInfoDao();
                if (inputValid && !isAdvanceSearch) {


                    data = childInfoDao.getByEPINum(ChildID);
                    if (data.size() != 0) {

                        startActivity(new Intent(SearchActivity.this, ChildrenListActivity.class)

                                .putExtra("fromSMS", false).putExtra("bookid",Integer.parseInt(newbookText.getText().toString())));


                    } else if (data.size() == 0) {
                        Toast.makeText(SearchActivity.this,getString(R.string.no_record), Toast.LENGTH_LONG).show();
                    }
                    /* else if (data.size() == 0 && !Constants.isOnline(SearchActivity.this)) {

                        sendSMS("%" + ChildID + "%" + CellPhone + "%" + CNIC);

                    } else {
                        onlineSearch(ChildID, CellPhone, CNIC);
                    }*/


                } else {
                    if (inputValid) {
                        if (!cellPhone.getText().toString().equals("")) {
                            data = childInfoDao.getByEPIPhone(CellPhone);
                        } else if (!cnic.getText().toString().equals("")) {
                            data = childInfoDao.getByCnic(CNIC);
                        }

                        if (data.size() != 0) {

                            startActivity(new Intent(SearchActivity.this, ChildrenListActivity.class).putExtra("fromSMS", false).putExtra("bookid",Integer.parseInt(newbookText.getText().toString())));

                        } else if (data.size() == 0 && !Constants.isOnline(SearchActivity.this)) {
                            if (isAdvanceSearch) {
                                if(cellPhone.length()==12) {
                                    sendSMS("hz %m%" + CellPhone);
                                }else
                                if(CNIC.length()==15){
                                    sendSMS("hz %c%" + CNIC);
                                }
                                Toast.makeText(SearchActivity.this,"Please Wait",Toast.LENGTH_LONG).show();
                            }

                        } else {
                            onlineSearch(ChildID, CellPhone, CNIC);
                        }
                    }
                }

            }
        });
        findViewById(R.id.advanceSearchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAdvanceSearch) {
                    searchOneLayout.setVisibility(View.GONE);
                    searchTwoLayout.setVisibility(View.VISIBLE);
                    isAdvanceSearch = true;

                    ((Button) findViewById(R.id.advanceSearchButton)).setText(getString(R.string.search_one));
                } else {
                    searchOneLayout.setVisibility(View.VISIBLE);
                    searchTwoLayout.setVisibility(View.GONE);

                    ((Button) findViewById(R.id.advanceSearchButton)).setText(getString(R.string.search_two));
                    isAdvanceSearch = false;
                }

            }
        });


    }

    private void createContexMenu() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popUpView = inflater.inflate(R.layout.contex_popup, null, false);
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
        pw = new PopupWindow(popUpView, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw.setOutsideTouchable(true);
        pw.setTouchable(true);
        pw.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_up_bg_drawable));
    }

    public void showError(View v, String error) {

        ((TextView) popUpView.findViewById(R.id.errorText)).setText(error);
        pw.showAsDropDown(v, 0, -Constants.pxToDp(SearchActivity.this, 10));

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        v.startAnimation(shake);
    }

    public boolean validateInput(boolean isAdvanceSearch) {
        boolean isValid = true;
        String error = "";
        if (isAdvanceSearch) {
            ((EditText) findViewById(R.id.searchActivityChildID)).setText("");
        } else {
            ((EditText) findViewById(R.id.searchActivityCellPhone)).setText("");
            ((EditText) findViewById(R.id.searchActivityCNIC)).setText("");
        }
        ChildID = childID.getText().toString();
        if (ChildID.equals("") && !isAdvanceSearch) {
            ChildID = "N/A";
            isValid = false;
            error = "برائے مہربانی ای پی آئی نمبر درج کریں۔";
            showError(childID, error);
            return isValid;
        } else if (!ChildID.equals("") && !isAdvanceSearch) {
            return true;
        }
        CNIC = cnic.getText().toString();
        CellPhone = cellPhone.getText().toString();
        if (CellPhone.trim().length() < 12 && CNIC.length() == 0) {

            CellPhone = "N/A";
            error = "برائے مہربانی سرپرست کا موبائل نمبر درج کریں۔";
            showError(cellPhone, error);
            return false;
        } else if (CNIC.trim().length() < 15 && CellPhone.length() == 0) {
            CNIC = "N/A";
            error = "برائی مہربانی سرپرست کا شناختی کارڈ نمبر درج کریں۔";
            showError(cnic, error);
            return false;
        }


        return isValid;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("permissions", permissions.length + ":" + grantResults.length);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onlineSearch(String epi, String phone, String cnic) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.search;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        JSONObject obj = null;
        try {

            obj = new JSONObject();
            JSONObject user = new JSONObject();
            obj.put("user", user);
            user.put("auth_token", Constants.getToken(SearchActivity.this));
            JSONObject kid = new JSONObject();
            kid.put("epi_number", epi);
            kid.put("phone_number", phone);
            kid.put("father_cnic", cnic);
            obj.put("kid", kid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url, obj,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        pDialog.dismiss();
                        if (response.toString().length() > 20) {

                            parseKidReponse(response);
                        }else{
                            Toast.makeText(SearchActivity.this,getString(R.string.no_record), Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //  Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
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
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    public void parseKidReponse(JSONArray response) {

        Gson gson = new Gson();
        String data = "{\"childInfoArrayList\":" + response + "}";
        GChildInfoAry obj = gson.fromJson(data, GChildInfoAry.class);
        if (obj.childInfoArrayList.size() == 0) {
            Toast.makeText(SearchActivity.this, getString(R.string.no_record), Toast.LENGTH_LONG).show();
            return;
        }
        ArrayList<ChildInfo> childInfoArrayList = new ArrayList<>();
        for (int i = 0; i < obj.childInfoArrayList.size(); i++) {
            ChildInfo c = new ChildInfo();
            c.kid_id = obj.childInfoArrayList.get(i).id;

            c.imei_number =  obj.childInfoArrayList.get(i).imei_number;
            c.kid_name = obj.childInfoArrayList.get(i).kid_name;
            c.guardian_name = obj.childInfoArrayList.get(i).father_name;

            c.guardian_cnic = obj.childInfoArrayList.get(i).father_cnic;

            c.phone_number = obj.childInfoArrayList.get(i).phone_number;
            c.next_due_date = obj.childInfoArrayList.get(i).next_due_date;

            c.date_of_birth = Constants.getFortmattedDate(Long.parseLong(obj.childInfoArrayList.get(i).date_of_birth));
            c.location = obj.childInfoArrayList.get(i).location;
            c.child_address = obj.childInfoArrayList.get(i).child_address;
            if (obj.childInfoArrayList.get(i).gender == true) {
                c.gender = 1;
            } else {
                c.gender = 0;
            }
            c.epi_number = obj.childInfoArrayList.get(i).epi_number;
            c.epi_name = obj.childInfoArrayList.get(i).itu_epi_number;
            c.record_update_flag = true;


            c.image_path = "image_" + obj.childInfoArrayList.get(i).id;//obj.childInfoArrayList.get(i).image_path;
            childInfoArrayList.add(c);
        }
        ChildInfoDao childInfoDao = new ChildInfoDao();
        childInfoDao.bulkInsert(childInfoArrayList);

        SearchActivity.data = childInfoArrayList;

        downloadImages(childInfoArrayList);



    }

    public void sendSMS(String msg) {


        if (ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Contacts permissions have not been granted.
            Log.i(TAG, "SMS permissions has NOT been granted. Requesting permissions.");
            requestContactsPermissions();

        } else {

            // Contact permissions have been granted. Show the contacts fragment.
            Log.i(TAG,
                    "SMS permissions have already been granted. send SMS");
            sendSMSMessage(msg);
        }


    }

    protected void sendSMSMessage(String msg) {
        Log.i("Send SMS", "");

        String txt = msg;
        number = "9100";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, txt, null, null);
  /*  Toast.makeText(getApplicationContext(), "SMS Sent", Toast.LENGTH_LONG).show();
    finish();*/
            // finish();
        } catch (Exception e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        }


    }

    public void displayExceptionMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void requestContactsPermissions() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_SMS)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.SEND_SMS)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECEIVE_SMS)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            Log.i(TAG,
                    "Displaying SMS permission rationale to provide additional context.");

            // Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(mLayout, "SMS permissions are needed to demonstrate access",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(SearchActivity.this, PERMISSIONS_SMS,
                                    REQUEST_SMS);
                        }
                    })
                    .show();
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(this, PERMISSIONS_SMS, REQUEST_SMS);
        }
        // END_INCLUDE(contacts_permission_request)
    }

    private  void downloadImages( List<ChildInfo> childInfo){

        ImageDownloader imageDownloader = new ImageDownloader(this, childInfo, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String reponse) {
                if (SearchActivity.data.size() != 0) {
                    startActivity(new Intent(SearchActivity.this, ChildrenListActivity.class).putExtra("fromSMS", false).putExtra("isOnline", true));
                } else {
                    Toast.makeText(SearchActivity.this, getString(R.string.no_record), Toast.LENGTH_LONG).show();
                }
            }
        });
        imageDownloader.execute();
    }
}

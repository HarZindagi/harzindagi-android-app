package com.ipal.itu.harzindagi.Activities;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.ipal.itu.harzindagi.Dao.InjectionsDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Dao.UserInfoDao;
import com.ipal.itu.harzindagi.Dao.VaccinationsDao;
import com.ipal.itu.harzindagi.Dao.VisitsDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.FemaleName;
import com.ipal.itu.harzindagi.Entity.Injections;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Entity.MaleName;
import com.ipal.itu.harzindagi.Entity.Towns;
import com.ipal.itu.harzindagi.Entity.Vaccinations;
import com.ipal.itu.harzindagi.Entity.Visit;
import com.ipal.itu.harzindagi.GJson.GAreasList;
import com.ipal.itu.harzindagi.GJson.GChildInfoAry;
import com.ipal.itu.harzindagi.GJson.GInjectionAry;
import com.ipal.itu.harzindagi.GJson.GKidTransactionAry;
import com.ipal.itu.harzindagi.GJson.GUserInfo;
import com.ipal.itu.harzindagi.GJson.GVaccinationAry;
import com.ipal.itu.harzindagi.GJson.GVisitAry;
import com.ipal.itu.harzindagi.GJson.Token;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;
import com.ipal.itu.harzindagi.Utils.ImageDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends BaseActivity {
    public static final int MAX_RETRY = 2;
    private static final String TAG = "Volly";
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    public GUserInfo obj;
    String[] str = {
            "Ali", "Ahmed", "Adil", "Ashraf", "Akmal", "Azam", "Arif", "Akhtar", "Babar", "Butt", "Bilal", "Danial", "Farhan", "Gulzar", "Hakim", "Haji", "Khizir", "Mehmood", "Nasir", "Pathan", "Hassan", "Saad",
            "Tahir", "Umer", "Khawer", "Yasir", "Jhangir", "Usman", "Osman", "Waseem",
            "Mannan", "Muhammad", "Majeed", "Manzoor", "Muneeb", "Imran", "Zaheer", "Zeshan"};
    String[] women_str = {
            "Aysha", "Fatima", "Mariam", "Aqsa", "Laiba", "Aiza", "Rabia", "Zainab", "Hina", "Saba", "Amna", "Aleena", "Maria", "Qurat", "IQRA",
            "Shazia", "Zoya", "Sadia", "Anam", "Eshaal", "Mehwish", "Asma", "Haniya",
            "Aiman", "Alishba", "Hareem", "Sidra"};
    TextView userName;
    EditText password;
    String UserName;
    String Password, app_name;
    TextView unionCouncil, EngUC;
    TextView validator;
    Button forgetButton;
    Button checkInButton;
    int b_click = 0;
    private View loginActivityForget_view;
    Boolean isFolderExists;
    Button evaccsButton;
    Button harZindagiButton;
    String rec_response;
    String passwordTxt;

    String location = "0.0,0.0";
    BroadcastReceiver receiver;
    private PopupWindow pw;
    private View popUpView;
    private long enqueue;
    private DownloadManager dm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        app_name = getResources().getString(R.string.app_name);
        File appFolder = new File("/sdcard/" + app_name);
        isFolderExists = appFolder.exists();
        if (!isFolderExists) {

            appFolder.mkdir();
        }


        userName = (TextView) findViewById(R.id.loginActivityUserName);

        UserName = userName.getText().toString();

        password = (EditText) findViewById(R.id.loginActivityPassword);
        Password = password.getText().toString();


        //TODO: get location in SplashActivity ans pass on to LoginActivity to set this TextView
        unionCouncil = (TextView) findViewById(R.id.loginActivityUnionCouncil);

        EngUC = (TextView) findViewById(R.id.UnionCouncil);

        validator = (TextView) findViewById(R.id.loginActivityValidationText);
        validator.setText(" براہ مہربانی درست یوزر کا نام اور پاسورڈ کا انتخاب کریں۔");

        forgetButton = (Button) findViewById(R.id.loginActivityForgetButton);
        loginActivityForget_view=findViewById(R.id.loginActivityForget_view);
        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
                if (b_click == 0) {
                    b_click = 1;
                    //mdanger.setVisibility(View.GONE);
                    loginActivityForget_view.setVisibility(View.VISIBLE);

                } else {
                    b_click = 0;
                    loginActivityForget_view.setVisibility(View.GONE);
                }

            }
        });

        evaccsButton = (Button) findViewById(R.id.ForgetActivitySMS);
        evaccsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:9100"));
                sendIntent.putExtra("sms_body", "مجھےاپناپاس ورڈ بھول گیاہے۔اس لیےبرائےمہربانی مجھے اپناپاس ورڈ بھیج دیں۔");
                startActivity(sendIntent);
            }
        });

        harZindagiButton = (Button) findViewById(R.id.ForgetActivityCall);
        harZindagiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "0321-418972"));
                startActivity(intent);
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.setVisibility(View.INVISIBLE);

            }
        });

        checkInButton = (Button) findViewById(R.id.loginActivityCheckInButton);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isTableLoaded = Constants.getIsTableLoaded(LoginActivity.this);

                if (Constants.getToken(LoginActivity.this).length() > 0 && isTableLoaded) {
                    if (Constants.getPassword(LoginActivity.this).equals(password.getText().toString())) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (password.getText().toString().equals("")) {

                        // validator.setVisibility(View.VISIBLE);
                        inputValidate();
                    } else {
                        String error = "برائےمہربانی درست پاس ورڈ درج کریں";
                        showError(password, error);
                    }

                } else {

                    if (Constants.isOnline(LoginActivity.this)) {
                        if (inputValidate()) {
                            sendUserInfo(userName.getText().toString(), password.getText().toString(), location);
                        }
                    } else {
                        Snackbar.make(view, "No Internet!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                }

            }
        });
        if (Constants.getToken(this).length() == 0) {
            getUserInfo();
        } else {
            EngUC.setText(Constants.getUC(this));
            userName.setText(Constants.getUserName(this));
        }


        createContexMenu();
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
            String gps = "GPS";
            String onkre = "آن کریں GPS";
            Toast.makeText(this, onkre, Toast.LENGTH_LONG).show();
        }
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {


                            String uriString = c
                                    .getString(c
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                           /* Intent promptInstall = new Intent(Intent.ACTION_VIEW)
                                    .setDataAndType(Uri.parse(uriString),
                                            "application/vnd.android.package-archive");
                            startActivity(promptInstall);*/
                            showDownload();

                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void showDownload() {
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    public boolean inputValidate() {

        if (password.getText().length() < 1) {
            String error = "برائےمہربانی پاس ورڈ درج کریں";
            showError(password, error);

            return false;
        }
        return true;
    }

    private void getUserInfo() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.get_device_info + "?" + "device[imei_number]=" + Constants.getIMEI(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        //Log.d(TAG, response.toString());
                        pDialog.dismiss();
                        if (response.optBoolean("success")) {
                            JSONObject json = response.optJSONObject("data");
                            parseUserResponse(json);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                MAX_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    private void sendUserInfo(String userName, final String password, String location) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.login;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        JSONObject obj = null;
        try {

            obj = new JSONObject();
            JSONObject user = new JSONObject();
            obj.put("user", user);
            user.put("username", userName);
            user.put("password", password);
            user.put("location", location);


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
                        pDialog.dismiss();
                        if (response.optBoolean("success")) {
                            JSONObject json = response.optJSONObject("data");
                            if (!json.toString().equals("{}")) {
                                parseTokenResponse(json);
                            } else {
                                showError(LoginActivity.this.password, "برائےمہربانی درست پاس ورڈ درج کریں");
                            }
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                MAX_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    private void createContexMenu() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popUpView = inflater.inflate(R.layout.contex_popup, null, false);
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics());
        pw = new PopupWindow(popUpView, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw.setOutsideTouchable(true);
        pw.setTouchable(true);
        pw.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_up_bg_drawable));
    }

    public void showError(View v, String error) {

        ((TextView) popUpView.findViewById(R.id.errorText)).setText(error);
        pw.showAsDropDown(v, 0, -Constants.pxToDp(LoginActivity.this, 10));

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        v.startAnimation(shake);
    }

    public void parseUserResponse(JSONObject response) {
        Gson gson = new Gson();
        obj = gson.fromJson(response.toString(), GUserInfo.class);
        UserInfoDao userInfoDao = new UserInfoDao();
        userInfoDao.save(obj.unioncouncil, obj.username, obj.Password);
        EngUC.setText(obj.unioncouncil);
        userName.setText(obj.username);
        Constants.setUserName(this, obj.username);
        Constants.setUC(this, obj.unioncouncil);
        Constants.setUCID(this, obj.unioncouncil_id);
    }

    public void parseTokenResponse(JSONObject response) {
        Gson gson = new Gson();
        Token token = gson.fromJson(response.toString(), Token.class);
        Constants.setToken(this, token.auth_token);
        Constants.setPassword(this, password.getText().toString());

        loadVisits();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_app_refresh_btn) {
            showDownloadDialog();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDownloadDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("کیاآپ ایپلیکیشن کوڈاون لوڈ کرناچاہتےہیں");


        adb.setIcon(R.drawable.info_circle);


        adb.setPositiveButton("ہاں", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                downloadFile();
            }
        });


        adb.setNegativeButton("نہیں", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        adb.show();
    }

    public void downloadFile() {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse("http://centsolapps.com/api/apk/app.apk"));
        request.setDestinationInExternalPublicDir("/sdcard/" + "Download" + "/", "app.apk");
        enqueue = dm.enqueue(request);

    }

    public void loadVisits() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.visits + "?" + "user[auth_token]=" + Constants.getToken(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Visits");
        pDialog.setCancelable(false);
        pDialog.show();
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        //  Log.d(TAG, response.toString());
                        pDialog.dismiss();
                        parseVisits(response);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                MAX_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    public void parseVisits(JSONArray array) {
        Gson gson = new Gson();
        GVisitAry gVisitAry = gson.fromJson("{\"visits\":" + array.toString() + "}", GVisitAry.class);
        VisitsDao visitsDao = new VisitsDao();
        ArrayList<Visit> visits = new ArrayList<>();
        if (gVisitAry.visits.size() == 0) {
            Toast.makeText(getApplicationContext(), "Internet Error: visits", Toast.LENGTH_LONG).show();
            return;
        }
        for (int i = 0; i < gVisitAry.visits.size(); i++) {
            Visit visit = new Visit();
            visit.id = gVisitAry.visits.get(i).id;
            visit.visit_number = gVisitAry.visits.get(i).visit_number;
            visit.description = gVisitAry.visits.get(i).description;
            visits.add(visit);
        }
        visitsDao.deleteTable();
        visitsDao.bulkInsert(visits);
        loadInjections();
    }

    public void loadInjections() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.injections + "?" + "user[auth_token]=" + Constants.getToken(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Injections...");
        pDialog.setCancelable(false);
        pDialog.show();
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        // Log.d(TAG, response.toString());
                        parseInjections(response);
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                MAX_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    public void parseInjections(JSONArray array) {
        Gson gson = new Gson();
        GInjectionAry gInjection = gson.fromJson("{\"injections\":" + array.toString() + "}", GInjectionAry.class);
        InjectionsDao injectionsDao = new InjectionsDao();
        ArrayList<Injections> visits = new ArrayList<>();
        if (gInjection.injections.size() == 0) {
            Toast.makeText(getApplicationContext(), "Internet Error: injections", Toast.LENGTH_LONG).show();
            return;
        }
        for (int i = 0; i < gInjection.injections.size(); i++) {
            Injections injections = new Injections();
            injections.id = gInjection.injections.get(i).id;
            injections.name = gInjection.injections.get(i).name;
            injections.description = gInjection.injections.get(i).description;
            injections.is_drop = gInjection.injections.get(i).is_drop;
            visits.add(injections);
        }
        injectionsDao.deleteTable();
        injectionsDao.bulkInsert(visits);
        loadVaccinations();
    }

    public void loadVaccinations() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.vaccinations + "?" + "user[auth_token]=" + Constants.getToken(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Vaccinations...");
        pDialog.setCancelable(false);
        pDialog.show();
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.dismiss();

                        parseVaccinations(response);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                MAX_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    public void parseVaccinations(JSONArray array) {
        Gson gson = new Gson();
        GVaccinationAry gInjection = gson.fromJson("{\"vaccinations\":" + array.toString() + "}", GVaccinationAry.class);
        VaccinationsDao vaccinationsDao = new VaccinationsDao();
        ArrayList<Vaccinations> vac = new ArrayList<>();
        if (gInjection.vaccinations.size() == 0) {
            Toast.makeText(getApplicationContext(), "Internet Error: vaccinations", Toast.LENGTH_LONG).show();
            return;
        }
        for (int i = 0; i < gInjection.vaccinations.size(); i++) {
            Vaccinations vacs = new Vaccinations();
            vacs.id = gInjection.vaccinations.get(i).id;
            vacs.visit_id = gInjection.vaccinations.get(i).visit_id;
            vacs.injection_id = gInjection.vaccinations.get(i).injection_id;
            vac.add(vacs);
        }

        vaccinationsDao.deleteTable();
        vaccinationsDao.bulkInsert(vac);

        loadAreas();


    }

    private void loadAreas() {
        // Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.areas + "?" + "user[auth_token]=" + Constants.getToken(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Areas...");
        pDialog.setCancelable(false);
        pDialog.show();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        pDialog.dismiss();
                        if (response.toString().contains("Invalid User")) {
                            Toast.makeText(LoginActivity.this, "Token Expired", Toast.LENGTH_LONG).show();
                            return;
                        }
                        JSONObject json = null;
                        try {
                            json = new JSONObject("{\"areasList\":" + response + "}");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        parseAreasResponse(json);


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

// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    public void parseAreasResponse(JSONObject response) {

        Gson gson = new Gson();
        GAreasList obj = gson.fromJson(response.toString(), GAreasList.class);

        ArrayList<Towns> townses = new ArrayList<>();
        for (int i = 0; i < obj.areasList.size(); i++) {
            Towns c = new Towns();
            c.name = obj.areasList.get(i).name;


            c.tId = obj.areasList.get(i).id;


            townses.add(c);

        }
        Towns.deleteTable();
        Towns.bulkInsert(townses);

        loadChildData();
    }

    private void loadChildData() {
        // Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.kids + "?" + "user[auth_token]=" + Constants.getToken(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Child data...");
        pDialog.setCancelable(false);
        pDialog.show();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        pDialog.dismiss();
                        if (response.toString().contains("Invalid User")) {
                            Toast.makeText(LoginActivity.this, "Token Expired", Toast.LENGTH_LONG).show();
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

// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    public void parseKidReponse(JSONObject response) {

        Gson gson = new Gson();
        GChildInfoAry obj = gson.fromJson(response.toString(), GChildInfoAry.class);

        ArrayList<ChildInfo> childInfoArrayList = new ArrayList<>();
        for (int i = 0; i < obj.childInfoArrayList.size(); i++) {
            ChildInfo c = new ChildInfo();
            c.kid_id = obj.childInfoArrayList.get(i).id;


            c.kid_name = obj.childInfoArrayList.get(i).kid_name;
            c.guardian_name = obj.childInfoArrayList.get(i).father_name;

            c.guardian_cnic = obj.childInfoArrayList.get(i).father_cnic;

            c.phone_number = obj.childInfoArrayList.get(i).phone_number;
            c.next_due_date = obj.childInfoArrayList.get(i).next_due_date;
            c.next_visit_date = obj.childInfoArrayList.get(i).next_visit_date;


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
            c.epi_number = obj.childInfoArrayList.get(i).epi_number;
            c.book_id = obj.childInfoArrayList.get(i).book_id;
            c.epi_name = obj.childInfoArrayList.get(i).itu_epi_number;
            c.record_update_flag = true;
            c.image_update_flag = true;
            //c.book_update_flag = true;
            c.image_path = "image_" + obj.childInfoArrayList.get(i).id;//obj.childInfoArrayList.get(i).image_path;

            childInfoArrayList.add(c);
        }
        ChildInfoDao childInfoDao = new ChildInfoDao();
        List<ChildInfo> noSync = ChildInfoDao.getNotSync();
        childInfoDao.deleteTable();
        childInfoDao.bulkInsert(childInfoArrayList);
        childInfoDao.bulkInsert(noSync);

        downloadImages(childInfoDao.getAll());
        //  setViewPagger();
    }

    private void downloadImages(List<ChildInfo> childInfo) {

        ImageDownloader imageDownloader = new ImageDownloader(this, childInfo, new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String reponse) {
                loadKidVaccination();
            }
        });
        imageDownloader.execute();
    }

    private void loadKidVaccination() {
        // Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.kid_vaccinations + "?" + "user[auth_token]=" + Constants.getToken(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Vaccination data...");
        pDialog.setCancelable(false);
        pDialog.show();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        pDialog.dismiss();
                        if (response.toString().contains("Invalid User")) {
                            Toast.makeText(LoginActivity.this, "Token Expired", Toast.LENGTH_LONG).show();
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

// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    public void parseVaccinationReponse(JSONObject response) {

        Gson gson = new Gson();
        GKidTransactionAry obj = gson.fromJson(response.toString(), GKidTransactionAry.class);

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
        loadNameLists();
        Toast.makeText(LoginActivity.this, "ڈاونلوڈ مکمل ہو گیا ہے", Toast.LENGTH_LONG).show();

        Constants.setIsTableLoaded(this, true);
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    private void loadNameLists() {
        MaleName.deleteTable();
        FemaleName.deleteTable();
        ArrayList<MaleName> maleNames = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            MaleName maleName = new MaleName();
            maleName.name = str[i];
            maleNames.add(maleName);
        }
        MaleName.bulkInsert(maleNames);
        ArrayList<FemaleName> femaleNames = new ArrayList<>();

        for (int i = 0; i < women_str.length; i++) {
            FemaleName femaleName = new FemaleName();
            femaleName.name = women_str[i];
            femaleNames.add(femaleName);
        }
        FemaleName.bulkInsert(femaleNames);
    }


}

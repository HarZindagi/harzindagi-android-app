package com.ipal.itu.harzindagi.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ipal.itu.harzindagi.Dao.InjectionsDao;
import com.ipal.itu.harzindagi.Dao.UserInfoDao;
import com.ipal.itu.harzindagi.Dao.VaccinationsDao;
import com.ipal.itu.harzindagi.Dao.VisitsDao;
import com.ipal.itu.harzindagi.Entity.Injections;
import com.ipal.itu.harzindagi.Entity.Vaccinations;
import com.ipal.itu.harzindagi.Entity.Visit;
import com.ipal.itu.harzindagi.GJson.GInjection;
import com.ipal.itu.harzindagi.GJson.GInjectionAry;
import com.ipal.itu.harzindagi.GJson.GUserInfo;
import com.ipal.itu.harzindagi.GJson.GVaccinationAry;
import com.ipal.itu.harzindagi.GJson.GVisit;
import com.ipal.itu.harzindagi.GJson.GVisitAry;
import com.ipal.itu.harzindagi.GJson.Token;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1887;
    private static final String TAG = "Volly";

    public GUserInfo obj;
    EditText userName;
    EditText password;
    String UserName;
    String Password, app_name;
    TextView unionCouncil, EngUC;
    TextView validator;
    Button forgetButton;
    Button checkInButton;


    Boolean isFolderExists;
    FileOutputStream fo;
    String rec_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        app_name = getResources().getString(R.string.app_name);
        File appFolder = new File("/sdcard/" + app_name);
        isFolderExists = appFolder.exists();
        if (!isFolderExists) {

            appFolder.mkdir();
        }


        userName = (EditText) findViewById(R.id.loginActivityUserName);

        UserName = userName.getText().toString();

        password = (EditText) findViewById(R.id.loginActivityPassword);
        Password = password.getText().toString();

        //TODO: get location in SplashActivity ans pass on to LoginActivity to set this TextView
        unionCouncil = (TextView) findViewById(R.id.loginActivityUnionCouncil);

        EngUC = (TextView) findViewById(R.id.UnionCouncil);

        validator = (TextView) findViewById(R.id.loginActivityValidationText);
        validator.setText(R.string.loginValidation);

        forgetButton = (Button) findViewById(R.id.loginActivityForgetButton);
        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.loginValidation, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                startActivity(new Intent(LoginActivity.this, ForgetActivity.class));

            }
        });

        checkInButton = (Button) findViewById(R.id.loginActivityCheckInButton);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sendUserInfo(userName.getText().toString(), password.getText().toString());

                Snackbar.make(view, "UserName: " + UserName + " , Password: " + Password, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                Intent cameraIntent = new Intent(LoginActivity.this, CustomCameraKidstation.class);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });

        getUserInfo();


    }

    private void getUserInfo() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.get_device_info + "?" + "device[imei_number]=" + getIMEI(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        //Log.d(TAG, response.toString());
                        pDialog.hide();
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

    private void sendUserInfo(String userName, String password) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.login;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Wait...");
        pDialog.show();
        JSONObject obj = null;
        try {
            obj = new JSONObject("{\"user\":{\"username\":\"" + userName + "\",\"password\":\"" + password + "\"}}");

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
                            parseTokenResponse(json);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
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

    public String getIMEI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public void parseUserResponse(JSONObject response) {
        Gson gson = new Gson();
        obj = gson.fromJson(response.toString(), GUserInfo.class);
        UserInfoDao userInfoDao = new UserInfoDao();
        userInfoDao.save(obj.unioncouncil, obj.username, obj.Password);
        EngUC.setText(obj.unioncouncil);
        userName.setText(obj.username);

    }

    public void parseTokenResponse(JSONObject response) {
        Gson gson = new Gson();
        Token token = gson.fromJson(response.toString(), Token.class);
        Constants.setToken(this, token.auth_token);
        loadVisits();
        //  Snackbar.make(view, "UserName: " + UserName + " , Password: " + Password, Snackbar.LENGTH_LONG)
        //       .setAction("Action", null).show();

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void saveBitmap(Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);

        //Create a new file in sdcard folder.
        File f = new File("abcd.jpg"); // this needs to be set
        try {
            try {
                f.createNewFile();
                fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray()); //write the bytes in file
            } finally {
                fo.close(); // remember close the FileOutput
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == 1887) {
            Bitmap photo, resizedImage;
            CustomCameraKidstation.progress.dismiss();
            String path = data.getStringExtra("path");
            photo = BitmapFactory.decodeFile(path);
            resizedImage = getResizedBitmap(photo, 256);
            saveBitmap(resizedImage);
           /* try {
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                resizedImage = getResizedBitmap(photo, 256);
                saveBitmap(resizedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

            startActivity(intent);
            //imageView.setImageBitmap(photo);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadVisits() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.visits + "?" + "user[auth_token]=" + Constants.getToken(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading GVisit");
        pDialog.show();
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        //  Log.d(TAG, response.toString());
                        pDialog.hide();
                        parseVisits(response);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
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

    public void parseVisits(JSONArray array) {
        Gson gson = new Gson();
        GVisitAry gVisitAry = gson.fromJson("{\"visits\":" + array.toString() + "}", GVisitAry.class);
        VisitsDao visitsDao = new VisitsDao();
        ArrayList<Visit> visits = new ArrayList<>();
        for (int i = 0; i < gVisitAry.visits.size(); i++) {
            Visit visit = new Visit();
            visit.id = gVisitAry.visits.get(i).id;
            visit.visit_number = gVisitAry.visits.get(i).visit_number;
            visit.description = gVisitAry.visits.get(i).description;
            visits.add(visit);
        }
        visitsDao.bulkInsert(visits);
        loadInjections();
    }

    public void loadInjections() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.injections + "?" + "user[auth_token]=" + Constants.getToken(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        // Log.d(TAG, response.toString());
                        parseInjections(response);
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
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

    public void parseInjections(JSONArray array) {
        Gson gson = new Gson();
        GInjectionAry gInjection = gson.fromJson("{\"injections\":" + array.toString() + "}", GInjectionAry.class);
        InjectionsDao injectionsDao = new InjectionsDao();
        ArrayList<Injections> visits = new ArrayList<>();
        for (int i = 0; i < gInjection.injections.size(); i++) {
            Injections injections = new Injections();
            injections.id = gInjection.injections.get(i).id;
            injections.name = gInjection.injections.get(i).name;
            injections.description = gInjection.injections.get(i).description;
            injections.is_drop = gInjection.injections.get(i).is_drop;
            visits.add(injections);
        }
        injectionsDao.bulkInsert(visits);
        loadVaccinations();
    }

    public void loadVaccinations() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.vaccinations + "?" + "user[auth_token]=" + Constants.getToken(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        parseVaccinations(response);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
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

    public void parseVaccinations(JSONArray array) {
        Gson gson = new Gson();
        GVaccinationAry gInjection = gson.fromJson("{\"vaccinations\":" + array.toString() + "}", GVaccinationAry.class);
        VaccinationsDao vaccinationsDao = new VaccinationsDao();
        ArrayList<Vaccinations> vac = new ArrayList<>();
        for (int i = 0; i < gInjection.vaccinations.size(); i++) {
            Vaccinations vacs = new Vaccinations();
            vacs.id = gInjection.vaccinations.get(i).id;
            vacs.visit_id = gInjection.vaccinations.get(i).visit_id;
            vacs.injection_id = gInjection.vaccinations.get(i).injection_id;
            vac.add(vacs);
        }
        vaccinationsDao.bulkInsert(vac);

    }
}

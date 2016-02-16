package com.ipal.itu.harzindagi.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.UserInfoDao;
import com.ipal.itu.harzindagi.Entity.UserInfo;
import com.ipal.itu.harzindagi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class LoginActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1887;

    public JSONObj obj;
    String response = "{\"userinfo\":[{\"UCNumber\":\"78 Lahore\",\"Username\":\"Asif\",\"Password\":\"Asif120\"}]}";
    EditText userName;
    EditText password;
    String UserName;
    String Password, app_name;
    TextView unionCouncil, EngUC;
    TextView validator;
    Button forgetButton;
    Button checkInButton;
<<<<<<< HEAD
    String app_name;
=======
>>>>>>> 8e2a8e99b25397952f3dd247a9744bcc9945e7b2
    Boolean isFolderExists;
    FileOutputStream fo;
    String rec_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

<<<<<<< HEAD

=======
>>>>>>> 8e2a8e99b25397952f3dd247a9744bcc9945e7b2
        app_name = getResources().getString(R.string.app_name);
        File appFolder = new File("/sdcard/" + app_name);
        isFolderExists = appFolder.exists();
        if (!isFolderExists) {

            appFolder.mkdir();
        }

<<<<<<< HEAD


        userName = ( EditText ) findViewById(R.id.loginActivityUserName);
=======
        userName = (EditText) findViewById(R.id.loginActivityUserName);
>>>>>>> 8e2a8e99b25397952f3dd247a9744bcc9945e7b2
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
                Snackbar.make(view, "UserName: " + UserName + " , Password: " + Password, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
               // startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                  Intent cameraIntent = new Intent(LoginActivity.this, CustomCameraKidstation.class);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        parseResponse();

        // Instantiate the RequestQueue.
       RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://103.226.216.170:3000/get_device_info.json";
        JSONObject device = new JSONObject();
        JSONObject imi = new JSONObject();
        try {
            imi.put("imei_number","12345678");
            device.put("device", imi.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,url,obj.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
// Request a string response from the provided URL.
     /*   TokenRequest stringRequest = new TokenRequest(Request.Method.GET, url,
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


        });*/
// Add the request to the RequestQueue.
        queue.add(jsObjRequest);
    }

    public void parseResponse() {
        Gson gson = new Gson();
        obj = gson.fromJson(response, JSONObj.class);
        UserInfoDao userInfoDao = new UserInfoDao();
        userInfoDao.save(obj.userinfo.get(0).UCNumber, obj.userinfo.get(0).Username, obj.userinfo.get(0).Password);
        EngUC.setText("UC-" + obj.userinfo.get(0).UCNumber);
        userName.setText("" + obj.userinfo.get(0).Username);
        password.setText("" + obj.userinfo.get(0).Password);

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
}

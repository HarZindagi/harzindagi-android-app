package com.ipal.itu.harzindagi.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ipal.itu.harzindagi.R;

public class LoginActivity extends AppCompatActivity {

    EditText userName;
    EditText password;
    String UserName;
    String Password;
    TextView unionCouncil;
    TextView validator;
    Button forgetButton;
    Button checkInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userName = ( EditText ) findViewById(R.id.loginActivityUserName);
        UserName = userName.getText().toString();

        password = ( EditText ) findViewById(R.id.loginActivityPassword);
        Password = password.getText().toString();

        //TODO: get location in SplashActivity ans pass on to LoginActivity to set this TextView
        unionCouncil = ( TextView ) findViewById(R.id.loginActivityUnionCouncil);

        validator = ( TextView ) findViewById(R.id.loginActivityValidationText);
        validator.setText(R.string.loginValidation);

        forgetButton = ( Button ) findViewById(R.id.loginActivityForgetButton);
        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.loginValidation , Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        checkInButton = ( Button ) findViewById(R.id.loginActivityCheckInButton);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.loginValidation , Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
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

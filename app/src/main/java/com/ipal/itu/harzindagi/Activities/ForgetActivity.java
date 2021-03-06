package com.ipal.itu.harzindagi.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ipal.itu.harzindagi.R;

public class ForgetActivity extends BaseActivity {

    // Forgot password activity

    Button evaccsButton;
    Button harZindagiButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_forget);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    }




}

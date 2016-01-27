package com.ipal.itu.harzindagi.Activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.ipal.itu.harzindagi.R;

public class Act extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);
        Bitmap photo=(Bitmap)getIntent().getExtras().get("p");

        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        imageView.setImageBitmap(photo);

    }
}

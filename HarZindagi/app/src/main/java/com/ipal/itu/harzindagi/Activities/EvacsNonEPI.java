package com.ipal.itu.harzindagi.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ipal.itu.harzindagi.R;

public class EvacsNonEPI extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;

    Spinner spn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evacs_epi);


        spn=(Spinner)findViewById(R.id.spinner_non_epi);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(parent.getItemAtPosition(position).toString().equals("مقامی بچہ"))
               {
                   TextView tx=(TextView)findViewById(R.id.Non_Epi_reg_num);
                   tx.setVisibility(View.VISIBLE);
                   EditText et=(EditText)findViewById(R.id.Non_Epi_reg_num_txt);
                   et.setVisibility(View.VISIBLE);

               }
                else
               {
                   TextView tx=(TextView)findViewById(R.id.Non_Epi_reg_num);
                   tx.setVisibility(View.INVISIBLE);
                   EditText et=(EditText)findViewById(R.id.Non_Epi_reg_num_txt);
                   et.setVisibility(View.INVISIBLE);
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    public void opencam(View v)
    {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);



    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);


            Intent intent=new Intent(EvacsNonEPI.this,Act.class);
            intent.putExtra("p",photo);
            startActivity(intent);


        }
    }
}

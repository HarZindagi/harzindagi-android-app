package com.ipal.itu.harzindagi.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ipal.itu.harzindagi.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class EvacsNonEPI extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    EditText Non_Epi_reg_num_txt;
    Spinner spn;
    FileOutputStream fo;
    String Fpath;
    String app_name;
    String Evac;
    Button non_mahfooz_Karain;
    CheckBox[] nonEPIv_box;
    String non_epiNumber;
    ArrayList<Integer> selectedCheckboxes_nonEPI = new ArrayList<Integer>();
    ArrayList<String> nonEPI_chkBox_txt = new ArrayList<String>();
    CheckBox non_bx_BCG,non_bx_OPV,non_bx_OPV1,non_bx_Pentavalent,non_bx_Pneumococcal,non_bx_OPV2,non_bx_Pentavalent2
            ,non_bx_Pneumococcal2,non_bx_OPV3,non_bx_Pentavalent3,non_bx_Pneumococcal3,non_bx_Measles,non_bx_Measles2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evacs_epi);

        non_bx_BCG=(CheckBox)findViewById(R.id.non_bx_BCG);
        non_bx_OPV=(CheckBox)findViewById(R.id.non_bx_OPV);
        non_bx_OPV1=(CheckBox)findViewById(R.id.non_bx_OPV1);
        non_bx_Pentavalent=(CheckBox)findViewById(R.id.non_bx_Pentavalent);
        non_bx_Pneumococcal=(CheckBox)findViewById(R.id.non_bx_Pneumococcal);
        non_bx_OPV2=(CheckBox)findViewById(R.id.non_bx_OPV2);
        non_bx_Pentavalent2=(CheckBox)findViewById(R.id.non_bx_Pentavalent2);
        non_bx_Pneumococcal2=(CheckBox)findViewById(R.id.non_bx_Pneumococcal2);
        non_bx_OPV3=(CheckBox)findViewById(R.id.non_bx_OPV3);
        non_bx_Pentavalent3=(CheckBox)findViewById(R.id.non_bx_Pentavalent3);
        non_bx_Pneumococcal3=(CheckBox)findViewById(R.id.non_bx_Pneumococcal3);
        non_bx_Measles=(CheckBox)findViewById(R.id.non_bx_Measles);
        non_bx_Measles2=(CheckBox)findViewById(R.id.non_bx_Measles2);
        non_mahfooz_Karain=(Button)findViewById(R.id.non_Mahfooz_Karain);
        nonEPIv_box = new CheckBox[]{ non_bx_BCG,non_bx_OPV,non_bx_OPV1,non_bx_Pentavalent,non_bx_Pneumococcal,non_bx_OPV2,non_bx_Pentavalent2
                ,non_bx_Pneumococcal2,non_bx_OPV3,non_bx_Pentavalent3,non_bx_Pneumococcal3,non_bx_Measles,non_bx_Measles2};

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

        non_mahfooz_Karain.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               for (int i = 0; i < nonEPIv_box.length; i++) {
                   if (nonEPIv_box[i].isChecked()) {
                       selectedCheckboxes_nonEPI.add(i + 1);
                       nonEPI_chkBox_txt.add(nonEPIv_box[i].getText().toString());
                   } else {
                       selectedCheckboxes_nonEPI.add(0);
                   }
               }
           }
       });




    }
    public void opencam(View v)
    {
        Intent cameraIntent = new Intent(EvacsNonEPI.this, CustomCamera.class);
        cameraIntent.putExtra("filename", Non_Epi_reg_num_txt.getText().toString());
        startActivityForResult(cameraIntent, CAMERA_REQUEST);



    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == 1888) {
            CustomCamera.progress.dismiss();
            Bitmap photo, resizedImage;
            readEditTexts();

            Fpath = data.getStringExtra("fpath");
            String path = data.getStringExtra("path");
            photo = BitmapFactory.decodeFile(path);
            resizedImage = getResizedBitmap(photo, 256);
            saveBitmap(resizedImage);

        }
    }
    public void saveBitmap(Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);

        //Create a new file in sdcard folder.
        File f = new File("/sdcard/" + app_name + "/" + Evac + "/" + Fpath + ".jpg");
        try {
            try {
                //  f.mkdir();
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
    public void readEditTexts() {
        non_epiNumber = Non_Epi_reg_num_txt.getText().toString();

    }
}

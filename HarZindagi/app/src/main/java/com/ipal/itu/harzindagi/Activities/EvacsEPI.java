package com.ipal.itu.harzindagi.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ipal.itu.harzindagi.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class EvacsEPI extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    FileOutputStream fo;
    String Fpath;
    String app_name;
    String Evac;
    TextView ep_txt_view;
    String epiNumber;
    String  childID;
    Boolean isFolderExists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evacs_epi2);
        ep_txt_view=(TextView)findViewById(R.id.ep_txt_view);
       /* File f = new File("/sdcard/" + app_name + "/" + Evac + "/");
        if(!f.isDirectory()){
            f.mkdir();
        }*/
        app_name = getResources().getString(R.string.app_name);
        Evac =getResources().getString(R.string.evac);
        File appFolder = new File("/sdcard/" + app_name +"/"+ Evac+"/");
        isFolderExists = appFolder.exists();
        if (!isFolderExists) {

            appFolder.mkdirs();
        }
    }
    public void opencam(View v)
    {
       /* Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);*/


        Intent cameraIntent = new Intent(EvacsEPI.this, CustomCamera.class);
        cameraIntent.putExtra("filename", ep_txt_view.getText().toString());
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == 1888) {
            CustomCamera.progress.dismiss();
            Bitmap photo, resizedImage;
            readEditTexts();
            childID = epiNumber;
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
        epiNumber = ep_txt_view.getText().toString();

    }
}

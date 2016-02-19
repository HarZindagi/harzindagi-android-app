package com.ipal.itu.harzindagi.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.ipal.itu.harzindagi.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Wahab on 2/3/2016.
 */
public class CustomCamera extends Activity implements SurfaceHolder.Callback {
    private Camera mCamera;
    SurfaceHolder surfaceHolder;
    File mediaFile;
    String Path, app_name;
    DisplayMetrics metrics;
    int Height, Width;
    Bitmap camera_bitmap;
    Canvas camera_canvas;
    Paint p;
    String fpath;
    ImageView CropImageView, captureButton;

    Context ctx;
    public static ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_camera_layout);
        SurfaceView preview = (SurfaceView) findViewById(R.id.camera_preview);
        surfaceHolder = preview.getHolder();
        surfaceHolder.addCallback(this);
        app_name ="Har Zindagi";

        ctx=this;

        fpath= this.getIntent().getStringExtra("filename");

        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(10);
        p.setColor(Color.GREEN);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStyle(Paint.Style.STROKE);
        p.setDither(true);

        //get the screen height and width
        metrics = getResources().getDisplayMetrics();

        Height = metrics.heightPixels;
        Width = metrics.widthPixels;

        captureButton = (ImageView) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, mPicture);
                 progress = new ProgressDialog(ctx);
                progress.setTitle("Loading");

                progress.show();

            }
        });

        CropImageView = (ImageView) findViewById(R.id.CropImageView);

    }

    private void getCameraInstance() {
        try {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap BmpRotate = Bitmap.createBitmap(realImage, 0, 0, realImage.getWidth(), realImage.getHeight(), matrix, true);
            Bitmap cropped_bitmap = cropBitmap(BmpRotate);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            cropped_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
             byte[] byteArray = stream.toByteArray();

            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(byteArray);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finishActivity();
        }
    };

    private File getOutputMediaFile() {
        Path = "/sdcard/" + app_name + "/"
                + fpath + ".jpg";
        mediaFile = new File(Path);

        return mediaFile;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            getCameraInstance();
            drawSquare();
            CropImageView.setImageBitmap(camera_bitmap);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            getCameraInstance();
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.setPreviewCallback(null);
        mCamera.release();
    }

    public void finishActivity() {
        Intent i = new Intent();
        i.putExtra("fpath", fpath);
        i.putExtra("path", Path);
        setResult(1888, i);

        finish();
    }

    public void drawSquare() {

        if (Height > Width) {
            camera_bitmap = Bitmap.createBitmap(Width, Width, Bitmap.Config.ARGB_8888);
        } else {
            camera_bitmap = Bitmap.createBitmap(Height, Height, Bitmap.Config.ARGB_8888);
        }
        camera_canvas = new Canvas(camera_bitmap);
       // camera_canvas.drawARGB(128,100,100,100);
        camera_canvas.drawRect(0, 0, camera_bitmap.getWidth(), camera_bitmap.getHeight(), p);
    }

    public Bitmap cropBitmap(Bitmap bmp) {
        Bitmap cropBmp;
        if (bmp.getWidth() >= bmp.getHeight()) {

            cropBmp = Bitmap.createBitmap(
                    bmp,
                    bmp.getWidth() / 2 - bmp.getHeight() / 2,
                    0,
                    bmp.getHeight(),
                    bmp.getHeight()
            );

        } else {

            cropBmp = Bitmap.createBitmap(bmp, 0, (bmp.getHeight() - bmp.getWidth()) / 2, bmp.getWidth(), bmp.getWidth()
            );
        }
        return cropBmp;
    }
}
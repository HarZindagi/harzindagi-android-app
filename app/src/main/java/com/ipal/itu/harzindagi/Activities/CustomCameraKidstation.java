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
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static com.ipal.itu.harzindagi.R.id.CropImageView;

/**
 * Created by Wahab on 2/3/2016.
 */
public class CustomCameraKidstation extends BaseActivity implements SurfaceHolder.Callback {

    // This is custom camera screen to take Kidstation picture.

    private Camera mCamera;
    SurfaceHolder surfaceHolder;
    File mediaFile;
    String Path, app_name;
    DisplayMetrics metrics;
    int Height, Width;
    Bitmap camera_bitmap;
    Canvas camera_canvas;
    Paint p;
    ImageView  captureButton;
    TextView kd_txt;
    Context ctx;
    LinearLayout done_capture,refresh_capture;
    public static ProgressDialog progress;
    private long activityTime;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_camera_kidstation_layout);

        //We make a custom Camera to take picture of kid station
        //If picture not perfect it retake it otherwise confirm and save

        activityTime = Calendar.getInstance().getTimeInMillis() / (1000);
        SurfaceView preview = (SurfaceView) findViewById(R.id.camera_preview);
        surfaceHolder = preview.getHolder();
        surfaceHolder.addCallback(this);
        app_name = getResources().getString(R.string.app_name);
       /* Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
            //window.setStatusBarTextColor(getResources().getColor(R.color.black));
        }*/
        kd_txt=(TextView)findViewById(R.id.kidstation_txt);

        kd_txt.setText("کٹ اسٹیشن کی تصویر کھینچیں");
        ctx = this;

        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(10);
        p.setColor(Color.parseColor("#17a99c"));
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStyle(Paint.Style.STROKE);
        p.setDither(true);

        //get the screen height and width
        metrics = getResources().getDisplayMetrics();

        Height = metrics.heightPixels;
        Width = metrics.widthPixels;

        done_capture=(LinearLayout) findViewById(R.id.done_capture);

        refresh_capture=(LinearLayout)findViewById(R.id.refresh_capture);

        done_capture.setVisibility(View.INVISIBLE);
        refresh_capture.setVisibility(View.INVISIBLE);

        done_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTime();
                finishActivity();
            }
        });

        refresh_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.startPreview();
                done_capture.setVisibility(View.INVISIBLE);
                refresh_capture.setVisibility(View.INVISIBLE);
                captureButton.setVisibility(View.VISIBLE);
                //CropImageView.setImageBitmap(camera_bitmap);
                Constants.sendGAEvent(CustomCameraKidstation.this,Constants.getUserName(CustomCameraKidstation.this), Constants.GaEvent.KIT_IMAGE_ERROR,"Retry" , 0);
                kd_txt.setText("کٹ اسٹیشن کی تصویر کھینچیں");
            }
        });

        captureButton = (ImageView) findViewById(R.id.button_capture_station);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, mPicture);
                progress = new ProgressDialog(ctx);
                progress.setTitle("Loading");

                progress.show();

                kd_txt.setText("کٹ اسٹیشن کی تصویر بھیجیں");

            }
        });

       // CropImageView = (ImageView) findViewById(R.id.CropImageView_station);

    }
    public void logTime(){
        activityTime = (Calendar.getInstance().getTimeInMillis() / 1000) - activityTime;
        //Constants.sendGAEvent(this,Constants.getUserName(this), Constants.GaEvent.KIT_IMAGE_TIME, activityTime + " S", 0);
        Constants.logTime(this,activityTime,Constants.GaEvent.KIT_IMAGE_TIME);
    }

    @Override
    public void onBackPressed() {

        Constants.sendGAEvent(this,Constants.getUserName(this), Constants.GaEvent.BACK_NAVIGATION,Constants.GaEvent.KIT_IMAGE_BACK , 0);
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }
    private void getCameraInstance() {
        try {
            mCamera = Camera.open();
            Camera.Parameters params = mCamera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            List<Camera.Size> sizes = params.getSupportedPictureSizes();
            int index = 0;
            for (Camera.Size size : sizes) {

                if( size.height==720 ){
                    params.setPictureSize(size.width, size.height);
                    break;
                }
                index++;
            }


            mCamera.setParameters(params);
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
            cropped_bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
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


            Bitmap bmp_read = BitmapFactory.decodeFile(Path);
            //CropImageView.setImageBitmap(bmp_read);

            done_capture.setVisibility(View.VISIBLE);
            refresh_capture.setVisibility(View.VISIBLE);
            captureButton.setVisibility(View.INVISIBLE);
            progress.dismiss();



        }
    };

    private File getOutputMediaFile() {
        Path = "/sdcard/" + app_name + "/"
                + "Image_"+ Constants.getUCID(this) + ".jpg";
        mediaFile = new File(Path);

        return mediaFile;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            getCameraInstance();
            drawSquare();
           // CropImageView.setImageBitmap(camera_bitmap);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            Toast.makeText(getApplication(),"Please Restart Phone",Toast.LENGTH_LONG).show();
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
        i.putExtra("path", Path);
        setResult(1887, i);

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
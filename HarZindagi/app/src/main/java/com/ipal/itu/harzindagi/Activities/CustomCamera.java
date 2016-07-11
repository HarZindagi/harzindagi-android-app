package com.ipal.itu.harzindagi.Activities;

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
import android.util.SparseArray;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.ipal.itu.harzindagi.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Wahab on 2/3/2016.
 */
public class CustomCamera extends BaseActivity implements SurfaceHolder.Callback {
    public  ProgressDialog progress;
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
    Bundle bundle;
    FaceDetector detector;
    private Camera mCamera;
    private SparseArray<Face> mFaces;
    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap bmpRotate = Bitmap.createBitmap(realImage, 0, 0, realImage.getWidth(), realImage.getHeight(), matrix, true);
           /* Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                    bmpRotate, realImage.getWidth()/2,realImage.getHeight()/2, false);*/
            if (detactFace(bmpRotate)) {
                saveImage(bmpRotate, camera);
            } else {
                mCamera.startPreview();
                progress.hide();
                Toast t = Toast.makeText(getApplicationContext(), "بچے کی تصویردوبارہ کھینچیں", Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();

            }

        }
    };

    @Override
    protected void onDestroy() {
        progress.dismiss();
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_camera_layout);
        SurfaceView preview = (SurfaceView) findViewById(R.id.camera_preview);
        surfaceHolder = preview.getHolder();
        surfaceHolder.addCallback(this);
        app_name = getResources().getString(R.string.app_name);

        ctx = this;

        bundle = getIntent().getExtras();
        fpath = this.getIntent().getStringExtra("filename");

        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(10);
        p.setColor(Color.parseColor("#DB4B39"));
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
                progress.setTitle("بچے کی شناخت کی جارہی ہے");

                progress.show();

            }
        });

        CropImageView = (ImageView) findViewById(R.id.CropImageView);

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
        }

         catch (Exception e) {
            // cannot get camera or does not exist
        }
    }

    public boolean detactFace(Bitmap bitmap) {
        detector = new FaceDetector.Builder(this)
                .setTrackingEnabled(true)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.ACCURATE_MODE)
                .build();

        if (!detector.isOperational()) {
            return true;
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            mFaces = detector.detect(frame);
            detector.release();
            if (mFaces.size() == 0 || mFaces.size() > 1) {
                return false;
            } else if (mFaces.size() == 1) {
                return true;
            }
        }
        return false;

    }

    private boolean processFaceData() {
        float smilingProbability;
        float leftEyeOpenProbability;
        float rightEyeOpenProbability;
        float eulerY;
        float eulerZ;
        boolean isFaceDetected = false;
        for (int i = 0; i < mFaces.size(); i++) {
            Face face = mFaces.valueAt(i);

            smilingProbability = face.getIsSmilingProbability();
            leftEyeOpenProbability = face.getIsLeftEyeOpenProbability();
            rightEyeOpenProbability = face.getIsRightEyeOpenProbability();
            eulerY = face.getEulerY();
            eulerZ = face.getEulerZ();

            //Log.e("Tuts+ Face Detection", "Smiling: " + smilingProbability);
            //  Log.e( "Tuts+ Face Detection", "Left eye open: " + leftEyeOpenProbability );
            // Log.e( "Tuts+ Face Detection", "Right eye open: " + rightEyeOpenProbability );
            // Log.e( "Tuts+ Face Detection", "Euler Y: " + eulerY );
            // Log.e( "Tuts+ Face Detection", "Euler Z: " + eulerZ );
            isFaceDetected = true;
            break;
        }
        return isFaceDetected;
    }

    private void saveImage(Bitmap image, Camera camera) {

        Bitmap cropped_bitmap = cropBitmap(image);
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
        if (bundle.size() >= 3) {
            i.putExtra("vacc_details", bundle.getString("vacc_details"));
            i.putExtra("visit_num", bundle.getString("visit_num"));


        }
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
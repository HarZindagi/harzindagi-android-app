package com.ipal.itu.harzindagi.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Ali on 3/31/2016.
 */
public class ImageDownloader {
    Context context;
    List<ChildInfo> childInfo;
    ProgressDialog pDialog;
    OnUploadListner onUploadListner;
    int index = 0;
    RequestQueue rq;

    public ImageDownloader(Context context, List<ChildInfo> childInfo, OnUploadListner onUploadListner) {
        this.childInfo = childInfo;
        this.context = context;
        this.onUploadListner = onUploadListner;
        rq = Volley.newRequestQueue(context);
    }

    public void execute() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Downloading Images...");

        pDialog.show();
        if (childInfo.size() != 0) {
            sendChildData(childInfo.get(index));
        } else {
            pDialog.dismiss();
            onUploadListner.onUpload(true, "");
        }

    }

    private void nextDownload(boolean isUploaded) {
        if (isUploaded) {
            index++;
            if (index < childInfo.size()) {
                sendChildData(childInfo.get(index));
                pDialog.setMessage("Downloading Images... " + index + " of " + childInfo.size());
            } else {
                onUploadListner.onUpload(true, "");
                pDialog.dismiss();
            }
        } else {
            onUploadListner.onUpload(false, "");
            pDialog.dismiss();
        }
    }

    private void sendChildData(final ChildInfo childInfo) {

        ImageLoader imageLoader = new ImageLoader(rq, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });
        File myDir = new File("/sdcard/" + Constants.getApplicationName(context) + "/" +childInfo.image_path+".jpg");
        if (myDir.exists()){
            nextDownload(true);
        }else{
            imageLoader.get(Constants.imageDownload + childInfo.image_path+".jpg", new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    SaveImage(response.getBitmap(),childInfo.image_path);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context,"Error, Try Again",Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                }
            });
        }






/*
// Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(Constants.photos,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {

                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                });
// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(context).addToRequestQueue(request);*/
    }
    private void SaveImage(Bitmap finalBitmap,String fileName) {


        File myDir = new File("/sdcard/" + Constants.getApplicationName(context) + "/" );
        myDir.mkdirs();
        String fname = fileName +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG,100, out);
            out.flush();
            out.close();
            nextDownload(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

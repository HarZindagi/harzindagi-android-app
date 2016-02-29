package com.ipal.itu.harzindagi.Utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ali on 2/28/2016.
 */
public class ImageUploadHandler {
    Context context;
    List<ChildInfo> childInfo;
    ProgressDialog pDialog;
    OnUploadListner onUploadListner;
    int index=0;

    public ImageUploadHandler(Context context, List<ChildInfo> childInfo, OnUploadListner onUploadListner) {
        this.childInfo = childInfo;
        this.context = context;
        this.onUploadListner = onUploadListner;
    }

    public void execute() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Uploading Images...");

        pDialog.show();
        if(childInfo.size()!=0){
            sendChildData(childInfo.get(index));
        }else{
            pDialog.dismiss();

        }

    }

    private void nextUpload(boolean isUploaded) {
        if (isUploaded) {
            index++;
            if (index < childInfo.size()) {
                sendChildData(childInfo.get(index));
                pDialog.setMessage("Uploading Images... " + index+ " of "+ childInfo.size());
            } else {
                onUploadListner.onUpload(true,"");
                pDialog.dismiss();
            }
        } else {
            onUploadListner.onUpload(false,"");
            pDialog.dismiss();
        }
    }

    private void sendChildData(final ChildInfo childInfo) {
        String imagePath = "/sdcard/" + Constants.getApplicationName(context) + "/" + childInfo.image_path + ".jpg";
        MultipartUtility multipart = new MultipartUtility(Constants.photos, "UTF-8", new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String reponse) {
                nextUpload(success);
            }
        });

        multipart.execute(imagePath);
    }
}

package com.ipal.itu.harzindagi.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;

import java.util.List;

/**
 * Created by Ali on 2/28/2016.
 */
public class ImageUploadHandler {
    Context context;
    List<ChildInfo> childInfo;
    ProgressDialog pDialog;
    OnUploadListner onUploadListner;
    int index = 0;

    public ImageUploadHandler(Context context, List<ChildInfo> childInfo, OnUploadListner onUploadListner) {
        this.childInfo = childInfo;
        this.context = context;
        this.onUploadListner = onUploadListner;
    }

    public void execute() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Har Zindagi Uploading Images...");
        pDialog.setCancelable(false);
        pDialog.show();
        if (childInfo.size() != 0) {
            sendChildData(childInfo.get(index));
        } else {
            pDialog.dismiss();
            onUploadListner.onUpload(true, "");
        }

    }

    private void nextUpload(boolean isUploaded) {
        if (isUploaded) {
            index++;
            if (index < childInfo.size()) {
                sendChildData(childInfo.get(index));
                pDialog.setMessage("Uploading Images... " + index + " of " + childInfo.size());
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
        String imagePath = "/sdcard/" + Constants.getApplicationName(context) + "/" + childInfo.image_path + ".jpg";
        MultipartUtility multipart = new MultipartUtility(Constants.photos, "UTF-8", new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String reponse) {


                if (success == false) {
                    Toast.makeText(context, "HarZindagi Images : " + " status :" + success + " Response : " + reponse, Toast.LENGTH_LONG).show();
                    if (reponse.contains("java.io.FileNotFoundException")) {
                        childInfo.image_update_flag = true;
                        childInfo.save();
                        nextUpload(true);
                    }
                } else {
                    childInfo.image_update_flag = true;
                    childInfo.save();
                    nextUpload(success);

                }


            }
        });

        multipart.execute(imagePath);
    }
}

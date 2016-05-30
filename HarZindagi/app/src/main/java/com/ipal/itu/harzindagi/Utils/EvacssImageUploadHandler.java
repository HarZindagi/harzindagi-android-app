package com.ipal.itu.harzindagi.Utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.ipal.itu.harzindagi.Entity.Evaccs;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;

import java.util.List;

/**
 * Created by Ali on 2/28/2016.
 */
public class EvacssImageUploadHandler {
    Context context;
    List<Evaccs> childInfo;
    ProgressDialog pDialog;
    OnUploadListner onUploadListner;
    int index=0;

    public EvacssImageUploadHandler(Context context, List<Evaccs> childInfo, OnUploadListner onUploadListner) {
        this.childInfo = childInfo;
        this.context = context;
        this.onUploadListner = onUploadListner;
    }

    public void execute() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Uploading Images...");
        pDialog.setCancelable(false);
        pDialog.show();
        if(childInfo.size()!=0){
            sendChildData(childInfo.get(index));
        }else{
            pDialog.dismiss();
            onUploadListner.onUpload(true,"");
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

    private void sendChildData(final Evaccs childInfo) {
        String imagePath = "/sdcard/" + Constants.getApplicationName(context) + "/Evac/" +"epi_"+ Constants.getIMEI(context)+"_"+ childInfo.epi_number + ".jpg";
        MultipartUtility multipart = new MultipartUtility(Constants.photos, "UTF-8", new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String reponse) {

                nextUpload(success);
            }
        });

        multipart.execute(imagePath);
    }
}

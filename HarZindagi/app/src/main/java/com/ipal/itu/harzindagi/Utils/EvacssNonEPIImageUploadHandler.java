package com.ipal.itu.harzindagi.Utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.ipal.itu.harzindagi.Entity.Evaccs;
import com.ipal.itu.harzindagi.Entity.EvaccsNonEPI;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;

import java.util.List;

/**
 * Created by Ali on 2/28/2016.
 */
public class EvacssNonEPIImageUploadHandler {
    Context context;
    List<EvaccsNonEPI> childInfo;
    ProgressDialog pDialog;
    OnUploadListner onUploadListner;
    int index=0;

    public EvacssNonEPIImageUploadHandler(Context context, List<EvaccsNonEPI> childInfo, OnUploadListner onUploadListner) {
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

    private void sendChildData(final EvaccsNonEPI childInfo) {
        String imagePath = "/sdcard/" + Constants.getApplicationName(context) + "/EvacNonEpi/"+ "nonepi_"+ Constants.getIMEI(context)+"_"+ childInfo.epi_no + ".jpg";
        MultipartUtility multipart = new MultipartUtility(Constants.photos, "UTF-8", new OnUploadListner() {
            @Override
            public void onUpload(boolean success, String reponse) {

                nextUpload(success);
            }
        });

        multipart.execute(imagePath);
    }
}

package com.ipal.itu.harzindagi.Receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.ipal.itu.harzindagi.Activities.ChildrenListActivity;
import com.ipal.itu.harzindagi.Activities.SearchActivity;
import com.ipal.itu.harzindagi.Activities.VaccinationActivity;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Dao.VaccinationsDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Entity.Vaccinations;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";
    Context mContext;

    public void onReceive(Context context, Intent intent) {
        mContext = context;
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                // SharedPreferences sharedpreferences =context.getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();
                if (smsBody.startsWith("%")) {
                    //smsMessageStr += "SMS From: " + address + "\n";
                    //smsMessageStr += smsBody + "\n";
                    String[] data = smsBody.split("%");
                    if (data[1].equals("c") || data[1].equals("m")) {
                        insertChillInfoToDB(data[2], Long.parseLong(data[3]), data[4]);

                    } else if (data[1].equals("id")) {
                        insertVaccinationToDB(data[2], Integer.parseInt(data[3]), data[4], data[5], Long.parseLong(data[6]));
                    }

                    this.abortBroadcast();


                }else if(smsBody.equals("false")){
                    Toast.makeText(mContext,mContext.getString(R.string.no_record),Toast.LENGTH_LONG).show();
                }

            }


            //this will update the UI with message

        }
    }

    private void insertChillInfoToDB(String name, long kid, String imei) {
        ChildInfo childInfo = new ChildInfo();
        childInfo.kid_name = name;
        childInfo.epi_number = "";
        childInfo.kid_id = kid;
        childInfo.kid_id = kid;
        childInfo.child_address = "";
        childInfo.guardian_name = "";
        childInfo.next_due_date = Calendar.getInstance().getTimeInMillis() / 1000;
        childInfo.record_update_flag = true;
        childInfo.book_update_flag = true;
        childInfo.image_path = "image_" + kid;//obj.childInfoArrayList.get(i).image_path;
        childInfo.imei_number = imei;


        List<ChildInfo> childRec = ChildInfoDao.getByKId(kid);
        for (int i = 0; i < childRec.size(); i++) {
            childRec.get(i).delete();
        }

        childInfo.save();
        SearchActivity.data = ChildInfoDao.getByKId(childInfo.kid_id);
        if (SearchActivity.data.size() != 0) {

            mContext.startActivity(new Intent(mContext, ChildrenListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                    .putExtra("fromSMS", false).putExtra("child_data",true));

        }
    }

    private void insertVaccinationToDB(String imei, int visitNum, String vaccinations, String epi, Long kid) {

        List<ChildInfo> childRec = ChildInfoDao.getByKId(kid);
        childRec.get(0).epi_number = epi;
        childRec.get(0).save();
        List<Vaccinations> vaccs = getVacIds(visitNum);

        List<KidVaccinations> kidVacss = KidVaccinationDao.getById(kid);
        for (int k = 0; k < kidVacss.size(); k++) {
            kidVacss.get(k).delete();
        }


        for (int j = 0; j < vaccs.size(); j++) {
            KidVaccinations kidVaccinations = new KidVaccinations();

            kidVaccinations.guest_imei_number = Constants.getIMEI(mContext);
            kidVaccinations.is_sync = true;
            kidVaccinations.imei_number = imei;
            kidVaccinations.kid_id = kid;
            kidVaccinations.vaccination_id = vaccs.get(j).id;
            kidVaccinations.location = "";
            kidVaccinations.image = "image_" + kid;
            kidVaccinations.created_timestamp = Calendar.getInstance().getTimeInMillis() / 1000;
            kidVaccinations.save();
        }


        Intent act = new Intent(mContext, VaccinationActivity.class);
        Bundle bnd = new Bundle();
        //  act.putExtra("childid", data[1]);

        bnd.putString("childid", epi);
        bnd.putString("visit_num", visitNum + "");
        bnd.putString("vacc_details", vaccinations);
        act.putExtras(bnd);
        act.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(act);
    }

    private List<Vaccinations> getVacIds(int visitNumn) {

        List<Vaccinations> list = VaccinationsDao.getById(visitNumn);
        return list;
    }
}

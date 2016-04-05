package com.ipal.itu.harzindagi.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.ipal.itu.harzindagi.Activities.ChildrenListActivity;
import com.ipal.itu.harzindagi.Activities.SearchActivity;
import com.ipal.itu.harzindagi.Activities.VaccinationActivity;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
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
                    this.abortBroadcast();

                 /*   Intent intentA = new Intent(context, ChildrenListActivity.class);

                    intentA.putExtra("ID", data[0]);
                    intentA.putExtra("CHILD_NAME", data[1]);
                    intentA.putExtra("Guardian_name", data[2]);
                    intentA.putExtra("Address", data[3]);
                    intentA.putExtra("VisitNum", data[4]);
                    intentA.putExtra("VAC_LIST", data[5]);
                    intentA.putExtra("fromSMS", true);
                    intentA.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentA);*/
                    ChildInfo childInfo = new ChildInfo();
                    childInfo.kid_name = data[1];
                    childInfo.epi_number = data[2];
                    childInfo.kid_id = Long.parseLong(data[3]);
                    childInfo.mobile_id = Long.parseLong(data[3]);
                    childInfo.child_address = data[4];
                    childInfo.guardian_name = data[5];
                    childInfo.next_due_date = Long.parseLong(data[6])*1000;
                    childInfo.record_update_flag = true;
                    childInfo.book_update_flag = true;
                    childInfo.image_path = "image_" + Long.parseLong(data[3]);//obj.childInfoArrayList.get(i).image_path;
                    childInfo.imei_number =  data[9];

                    ChildInfoDao childInfoDao = new ChildInfoDao();
                    List<ChildInfo> childRec = childInfoDao.getByEPINum(data[2]);
                    if(childRec.size()>0){
                        childRec.get(0).delete();
                    }
                    childInfo.save();

                 /*   KidVaccinations kidVaccinations = new KidVaccinations();
                    kidVaccinations.guest_imei_number = Constants.getIMEI(context);
                    kidVaccinations.is_sync = true;
                    kidVaccinations.imei_number =  data[9];
                    kidVaccinations.kid_id = Long.parseLong(data[3]);
                    kidVaccinations.mobile_id = Long.parseLong(data[3]);
                    kidVaccinations.vaccination_id = 1;*/

                    Intent act = new Intent(context, VaccinationActivity.class);
                    Bundle bnd = new Bundle();
                  //  act.putExtra("childid", data[1]);

                    bnd.putString("childid", data[2]);
                    bnd.putString("visit_num", data[7]);
                    bnd.putString("vacc_details", data[8]);
                    act.putExtras(bnd);
                    act.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(act);

                }

            }


            //this will update the UI with message

        }
    }
    private ArrayList<String> getVacIds(int visitNumn){
        ArrayList<String> list = new ArrayList<>();

        return list;
    }
}

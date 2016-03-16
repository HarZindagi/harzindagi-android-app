package com.ipal.itu.harzindagi.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.ipal.itu.harzindagi.Activities.ChildrenListActivity;
import com.ipal.itu.harzindagi.Activities.SearchActivity;

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
                    Intent intentA = new Intent(context, ChildrenListActivity.class);

                    intentA.putExtra("ID", data[0]);
                    intentA.putExtra("CHILD_NAME", data[1]);
                    intentA.putExtra("Guardian_name", data[2]);
                    intentA.putExtra("Address", data[3]);
                    intentA.putExtra("VisitNum", data[4]);
                    intentA.putExtra("VAC_LIST", data[5]);
                    intentA.putExtra("fromSMS", true);
                    intentA.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentA);




                }

            }


            //this will update the UI with message

        }
    }
}

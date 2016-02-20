package com.ipal.itu.harzindagi.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

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
                if (smsBody.startsWith("PEL")) {
                    smsMessageStr += "SMS From: " + address + "\n";
                    smsMessageStr += smsBody + "\n";
                    this.abortBroadcast();
                   /* SmsActivity inst = SmsActivity.instance();
                    if (inst != null) {
                        inst.updateList(smsMessageStr);
                    }*/


                }

            }


            //this will update the UI with message

        }
    }
}

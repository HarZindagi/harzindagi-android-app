package com.ipal.itu.harzindagi.Activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.ipal.itu.harzindagi.R;

import java.nio.charset.CharsetEncoder;
import java.util.Arrays;


public class Card_Scan extends AppCompatActivity {

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    TextView Scan_txt;

    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    Tag mytag;
    Context ctx;
    ImageView imgV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardscan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ctx = this;

        imgV = (ImageView) findViewById(R.id.scan_image_view);



        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter != null) {

        } else {

        }


        mPendingIntent = PendingIntent.getActivity(this, 1,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT);

        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[]{ndefIntent};
        } catch (Exception e) {

            Toast.makeText(ctx, "adding ndefintent", Toast.LENGTH_LONG).show();
        }

        mNFCTechLists = new String[][]{new String[]{NfcF.class.getName()}};


    }


    @Override
    public void onNewIntent(Intent intent) {
        String s = "";
        String action = intent.getAction();
        //if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())
        // Tag mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String myData = intent.getStringExtra("mType");
        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (data != null) {

            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage) data[i]).getRecords();

                    for (int j = 0; j < recs.length; j++) {

                       /* if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) */
                        if (recs[j].getTnf() == NdefRecord.TNF_MIME_MEDIA || recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN )
                        {
                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";

                            int langCodeLen = payload[0] & 0077;
                           /* s = new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1,
                                    textEncoding);*/
                             s = new String(payload, 0, payload.length,
                                    textEncoding);
                            if (s.equals("0")) {
                                // Intent myintent = new Intent(this, RegisterChild.class);
                                //myintent.pu
                                // startActivityForResult(myintent, 0);
                            } else {
                                // mTextView.setText(s);

                            }

                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(ctx, "Tag dispatch on new intent", Toast.LENGTH_LONG).show();
            }
        }


        if(s.length()>2)
        {

        String Arry[] = s.split("#");
            if(Arry.length>3) {
                Intent i = new Intent(Card_Scan.this, VaccinationActivity.class);
                i.putExtra("childid", Arry[0]);
                i.putExtra("visit_num", Arry[Arry.length - 2]);
                i.putExtra("vacc_details", Arry[Arry.length - 1]);
                startActivity(i);
                finish();
            }else{
                Intent i = new Intent(Card_Scan.this, VaccinationActivity.class);
                i.putExtra("childid", s.replace("0",""));
                i.putExtra("visit_num", "1");
                i.putExtra("vacc_details", "1,1,1");
                startActivity(i);
                finish();
            }

        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }


    public void parseintent(String s) {
        //imgV.setBackgroundColor(Color.GREEN);
        Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();


    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }


}

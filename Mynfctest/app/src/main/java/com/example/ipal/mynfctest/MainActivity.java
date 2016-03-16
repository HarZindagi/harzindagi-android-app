package com.example.ipal.mynfctest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcF;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;



public class MainActivity extends AppCompatActivity {

    String push_NFC="";
    int check=0;
        boolean mWriteMode = false;
        private NfcAdapter mNfcAdapter;
        private PendingIntent mNfcPendingIntent;
      final  Context ctx=this;
    boolean is_check=false;
    private String card_data = "";
    Tag mytag;
    AlertDialog.Builder alertDialogBuilder;
    IntentFilter[] mWriteTagFilters;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(MainActivity.this);
        mNfcPendingIntent = PendingIntent.getActivity(MainActivity.this, 0,
                new Intent(MainActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");

        } catch (Exception e) {

            Toast.makeText(ctx, "adding ndefintent", Toast.LENGTH_LONG).show();
        }
           mWriteTagFilters = new IntentFilter[] { tagDetected,ndefIntent, new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)};

            ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    enableTagWriteMode();
                    alertDialogBuilder = new AlertDialog.Builder(ctx);
                    alertDialogBuilder.setTitle("Touch tag to Detect");
                    alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            disableTagWriteMode();
                        }
                    });
                    alertDialogBuilder.create().show();

                }
            });
        }

        private void enableTagWriteMode() {
            mWriteMode = true;

            mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
        }

        private void disableTagWriteMode() {
            mWriteMode = false;
            mNfcAdapter.disableForegroundDispatch(this);
        }

        @Override
        protected void onNewIntent(Intent intent) {
            mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            if(check==0) {

                String s = "";
                String action = intent.getAction();

                Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (data != null) {
                    try {
                        for (int i = 0; i < data.length; i++) {
                            NdefRecord[] recs = ((NdefMessage) data[i]).getRecords();
                            for (int j = 0; j < recs.length; j++) {
                                if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                        Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
                                    byte[] payload = recs[j].getPayload();
                                    String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";

                                    int langCodeLen = payload[0] & 0077;
                                    s = new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1,
                                            textEncoding);

//                                mTextView.setTextE(s);


                                    push_NFC = s+"Hello cat";

                                    Toast.makeText(ctx, push_NFC, Toast.LENGTH_LONG).show();
                      }
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(ctx, "Tag dispatch on new intent", Toast.LENGTH_LONG).show();
                    }
                }
                check=1;


                // Tag writing mode

            }
            else {
                Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                String ff = push_NFC+"go";
                NdefRecord record = NdefRecord.createMime(ff, ff.getBytes());
                NdefMessage message = new NdefMessage(new NdefRecord[]{record});
                if (writeTag(message, detectedTag)) {
                    Toast.makeText(this, "Success: Wrote placeid to nfc tag", Toast.LENGTH_LONG)
                            .show();
                }
            }
            }


        /*
        * Writes an NdefMessage to a NFC tag
        */
        public boolean writeTag(NdefMessage message, Tag tag) {
            int size = message.toByteArray().length;
            try {
                Ndef ndef = Ndef.get(tag);
                if (ndef != null) {
                    ndef.connect();
                    if (!ndef.isWritable()) {
                        Toast.makeText(getApplicationContext(),
                                "Error: tag not writable",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (ndef.getMaxSize() < size) {
                        Toast.makeText(getApplicationContext(),
                                "Error: tag too small",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    ndef.writeNdefMessage(message);
                    return true;
                } else {
                    NdefFormatable format = NdefFormatable.get(tag);
                    if (format != null) {
                        try {
                            format.connect();
                            format.format(message);
                            return true;
                        } catch (IOException e) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
    }

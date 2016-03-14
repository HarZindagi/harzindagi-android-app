package com.ipal.itu.harzindagi.Activities;


import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CardScanWrite extends AppCompatActivity {


    Tag mytag;
    String push_NFC;
    Activity ctx;
    Button btn;
    double longitude;
    double latitude;
    Bundle bundle;
    boolean is_check = false;
    boolean clicku = false;
    Long tsLong;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    private String childName;
    private boolean childGender = false;                                //True for Male. False for Female
    private String dateOfBirth;
    private String fatherName;
    private String fatherCNIC;
    private String fatherMobile;
    private String childAddress;
    private String District;
    private String Tehsil;
    private String address;
    private String Child_id;
    private int VisitNum;
    private String NextDueDate;
    private String card_data = "";
    private ImageView imgV;
    boolean mWriteMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cardscanwrite);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ctx = this;
        btn = (Button) findViewById(R.id.Push_nfc_btn);


        bundle = getIntent().getExtras();
        Child_id = bundle.getString("ID");

        address = bundle.getString("address");

        tsLong = System.currentTimeMillis() / 1000;

        push_NFC = "#" + Child_id + "#" + bundle.getString("Name") + "#" + bundle.getInt("Gender") + "#" + bundle.getString("DOB") + "#" + bundle.getString("mName") + "#" + bundle.getString("gName") + "#" + bundle.getString("cnic") + "#" + bundle.getString("pnum") + "#" + tsLong + "#" + "" + longitude + "," + latitude + "#" + bundle.getString("EPIname");


        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[]{ndefIntent, tagDetected, new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)};
        } catch (Exception e) {

            Toast.makeText(ctx, "adding ndefintent", Toast.LENGTH_LONG).show();
        }

        mNFCTechLists = new String[][]{new String[]{NfcF.class.getName()}};


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Push_into_NFC();
            }
        });


    }

    private void enableTagWriteMode() {
        mWriteMode = true;

        mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, null);
    }

    private void disableTagWriteMode() {
        mWriteMode = false;
        mNfcAdapter.disableForegroundDispatch(this);
    }


    public int Push_into_NFC() {


        Toast.makeText(this, "Saved in NFC", Toast.LENGTH_LONG).show();

        Long tsLong = System.currentTimeMillis() / 1000;
        ChildInfoDao childInfoDao = new ChildInfoDao();
        childInfoDao.save(Child_id, bundle.getString("Name"), bundle.getInt("Gender"), bundle.getString("DOB"), bundle.getString("mName"), bundle.getString("gName"), bundle.getString("cnic"), bundle.getString("pnum"), tsLong, "" + longitude + "," + latitude + "", bundle.getString("EPIname"), "abc", bundle.getString("img"), card_data, true, false,address);

        Intent myintent = new Intent(this, RegisteredChildActivity.class);
        myintent.putExtra("childid", Child_id);
        myintent.putExtra("EPIname", bundle.getString("EPIname"));
        startActivity(myintent);
        finish();
        return 0;
    }

    @Override
    public void onNewIntent(Intent intent) {

        if (is_check == false && clicku == true) {
            mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
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
                                btn.setText("Tap Again To Write2");
                                btn.setEnabled(false);
                                String Arry[] = s.split("#");
                                card_data = Arry[0] + "#" + Arry[1];
                                push_NFC = card_data + push_NFC;
                                is_check = true;


                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(ctx, "Tag dispatch on new intent", Toast.LENGTH_LONG).show();
                }
            }

        } else {

            if (mWriteMode && clicku == true) {
                Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                NdefRecord record = NdefRecord.createMime(push_NFC, push_NFC.getBytes());
                NdefMessage message = new NdefMessage(new NdefRecord[]{record});
                if (writeTag(message, detectedTag)) {
                    //   Toast.makeText(this, "Success: Wrotgme placeid to nfc tag", Toast.LENGTH_LONG)
                    //.show();
                    btn.setText("Next");
                    btn.setVisibility(View.VISIBLE);
                    btn.setEnabled(true);
                    mWriteMode = false;


                }
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

    // Functions onwards are for NFC ignore them
    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {

        //create the message in according with the standard
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;

        byte[] payload = new byte[1 + langLength + textLength];
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
        return recordNFC;
    }

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
            btn.setText("Tap Again To Write");
            btn.setEnabled(false);

            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    private void sendChildData(String childID) {
        // Instantiate the RequestQueue.
        ChildInfoDao childInfoDao = new ChildInfoDao();
        List<ChildInfo> childInfo = childInfoDao.getByEPINum(childID);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.kids;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Saving Child data...");
        pDialog.show();
        JSONObject obj = null;
        try {
            obj = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("auth_token", Constants.getToken(this));
            obj.put("user", user);

            JSONObject kid = new JSONObject();
            kid.put("mobile_id", childInfo.get(0).mobile_id);
            kid.put("imei_number", Constants.getIMEI(this));
            kid.put("kid_name", childInfo.get(0).kid_name);
            kid.put("father_name", childInfo.get(0).guardian_name);
            kid.put("mother_name", childInfo.get(0).mother_name);
            kid.put("father_cnic", childInfo.get(0).guardian_cnic);
            kid.put("mother_cnic", "");
            kid.put("phone_number", childInfo.get(0).phone_number);
            kid.put("date_of_birth", childInfo.get(0).date_of_birth);
            kid.put("location", "00000,000000");
            kid.put("child_address", "");
            kid.put("gender", childInfo.get(0).gender);
            kid.put("epi_number", childInfo.get(0).epi_number);
            kid.put("itu_epi_number", childInfo.get(0).epi_number + "_itu");

            obj.put("kid", kid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        pDialog.hide();
                        if (response.optBoolean("success")) {
                            JSONObject json = response.optJSONObject("data");
                            parseKidReponse(json);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }


        };

// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    public void parseKidReponse(JSONObject response) {
        Gson gson = new Gson();
        // obj = gson.fromJson(response.toString(), GUserInfo.class);
    }

}

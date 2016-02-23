package com.ipal.itu.harzindagi.Activities;


import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.UserInfoDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.GJson.GUserInfo;
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


public class CardScanWrite extends Activity {


    Tag mytag;
    String push_NFC;
    Activity ctx;
    Button btn;
    double longitude;
    double latitude;
    Bundle bundle;
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
    private String Child_id;
    private int VisitNum;
    private String NextDueDate;
    private String card_data = "";
    private ImageView imgV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardscanwrite);

        ctx = this;
        btn = (Button) findViewById(R.id.Push_nfc_btn);


        bundle = getIntent().getExtras();
        Child_id = bundle.getString("ID");

        //   BabyInfo info = new Select().from(BabyInfo.class).where("ChildID = ?" , Child_id).executeSingle();
/*        childName=info.childName;
        dateOfBirth=info.childDOB;

        childGender=info.childGender;


        fatherName=info.fatherName;
        fatherCNIC=info.fatherCNIC;
        fatherMobile=info.contactNumber;
        childAddress=info.address;
        District=info.district;
        Tehsil=info.tehsil;
        NextDueDate=info.nextDueDate;
        VisitNum=info.visitNumber;*/


        tsLong = System.currentTimeMillis() / 1000;

        push_NFC = "#" + Child_id + "#" + bundle.getString("Name") + "#" + bundle.getInt("Gender") + "#" + bundle.getString("DOB") + "#" + bundle.getString("mName") + "#" + bundle.getString("gName") + "#" + bundle.getString("cnic") + "#" + bundle.getString("pnum") + "#" + tsLong + "#" + "" + longitude + "," + latitude + "#" + bundle.getString("EPIname");



        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[]{ndefIntent, new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)};
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


    public int Push_into_NFC() {

        int loop_check = 0;

        Toast.makeText(this, "Saved in NFC", Toast.LENGTH_LONG).show();

        Long tsLong = System.currentTimeMillis() / 1000;
        ChildInfoDao childInfoDao = new ChildInfoDao();
        childInfoDao.save(Child_id, bundle.getString("Name"), bundle.getInt("Gender"), bundle.getString("DOB"), bundle.getString("mName"), bundle.getString("gName"), bundle.getString("cnic"), bundle.getString("pnum"), tsLong, "" + longitude + "," + latitude + "", bundle.getString("EPIname"), "abc", bundle.getString("img"), card_data, true, false);


        Intent myintent = new Intent(this, RegisteredChildActivity.class);

        myintent.putExtra("childid", Child_id);
        myintent.putExtra("EPIname", bundle.getString("EPIname"));
        if (Constants.isOnline(this)) {
           sendChildData(Child_id);
        }

        startActivity(myintent);
        return 0;
    }


    @Override
    public void onNewIntent(Intent intent) {
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
                            if (s.equals("0")) {
                                // Intent myintent = new Intent(this, RegisterChild.class);
                                //myintent.pu
                                // startActivityForResult(myintent, 0);
                            } else {
//                                mTextView.setText(s);
                                btn.setText("WAIT");
                                btn.setVisibility(View.VISIBLE);
                                String Arry[] = s.split("#");
                                card_data = Arry[0] + "#" + Arry[1];
                                try {
                                    write(card_data + push_NFC, mytag);
                                    btn.setText("NEXT");
                                    btn.setVisibility(View.VISIBLE);


                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (FormatException e) {

                                    Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                            }

                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(ctx, "Tag dispatch on new intent", Toast.LENGTH_LONG).show();
            }
        }






/*
        // Saving child record in NFC
        if(Push_into_NFC()==1) {

            /// Saving Child Record in DB
            BabyInfo newBabyRegistration = new BabyInfo(Child_id, childName, dateOfBirth, childGender, fatherName,
                    fatherCNIC, fatherMobile, childAddress, District, Tehsil);

            newBabyRegistration.save();


            // Fetching CHild Record from DB

            BabyInfo query = new Select().from(BabyInfo.class).executeSingle();
            Toast.makeText(this, query.childName + " " + query.fatherName + "" + query.district, Toast.LENGTH_LONG).show();


            // Calling Vaccine_record
            Intent intent_2 = new Intent(this, Vaccine_record.class);
            intent_2.putExtra("data", push_NFC);
            intent_2.putExtra("scan_flag", 1);

            startActivity(intent_2);
            ctx.finish();
        }*/
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

    private void write(String text, Tag tag) throws IOException, FormatException {

        NdefRecord[] records = {createRecord(text)};
        NdefMessage message = new NdefMessage(records);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        ndef.writeNdefMessage(message);
        ndef.close();
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
        List<ChildInfo> childInfo = childInfoDao.getById(childID);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.kids;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Saving Child data...");
        pDialog.show();
        JSONObject obj = null;
        try {
            obj = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("auth_token",Constants.getToken(this));
            obj.put("user", user);

            JSONObject kid = new JSONObject();
            kid.put("id",childInfo.get(0).id);
            kid.put("mobile_id",childInfo.get(0).id);
            kid.put("imei_number",Constants.getIMEI(this));
            kid.put("kid_name",childInfo.get(0).kid_name);
            kid.put("father_name",childInfo.get(0).guardian_name);
            kid.put("mother_name",childInfo.get(0).mother_name);
            kid.put("father_cnic",childInfo.get(0).guardian_cnic);
            kid.put("mother_cnic","");
            kid.put("phone_number",childInfo.get(0).phone_number);
            kid.put("date_of_birth",childInfo.get(0).date_of_birth);
            kid.put("location","00000,000000");
            kid.put("child_address","");
            kid.put("gender",childInfo.get(0).gender);
            kid.put("epi_number",childInfo.get(0).epi_number);
            kid.put("itu_epi_number",childInfo.get(0).epi_number+"_itu");

            obj.put("kid",kid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        // Log.d(TAG, response.toString());
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

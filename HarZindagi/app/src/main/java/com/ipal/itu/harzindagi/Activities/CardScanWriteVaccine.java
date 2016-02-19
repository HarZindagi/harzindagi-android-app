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
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.ipal.itu.harzindagi.Dao.VaccinationsDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.Vaccinations;
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


public class CardScanWriteVaccine extends Activity {


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
        Child_id = bundle.getString("childid");


        ChildInfoDao childInfo = new ChildInfoDao();
        List<ChildInfo> data = childInfo.getById(Child_id);


        push_NFC = data.get(0).epi_name + "#" + data.get(0).kid_name + "#" + data.get(0).gender + "#" + data.get(0).date_of_birth + "#" + data.get(0).mother_name + "#" + data.get(0).guardian_name + "#" + data.get(0).guardian_cnic + "#" + data.get(0).phone_number + "#" + data.get(0).created_timestamp + "#" + data.get(0).location + "#" + data.get(0).epi_name + "#" + bundle.getString("next_date");


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


        Toast.makeText(this, "Saved in NFC", Toast.LENGTH_LONG).show();

        Long tsLong = System.currentTimeMillis() / 1000;
        // ChildInfoDao childInfoDao = new ChildInfoDao();
        //childInfoDao.save(Child_id, bundle.getString("Name"), bundle.getInt("Gender"), bundle.getString("DOB"), bundle.getString("mName"), bundle.getString("gName"), bundle.getString("cnic"), bundle.getString("pnum"), tsLong, "" + longitude + "," + latitude + "", bundle.getString("EPIname") ,"abc", bundle.getString("img"),card_data, true, false);
/// @@@@@@@@@@@ CODE OF VACCINATIONS

        Intent myintent = new Intent(this, DashboardActivity.class);


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
                                    write(card_data + "#" + push_NFC, mytag);
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
    private void sendVaccinationsData(int vacID) {
        // Instantiate the RequestQueue.
        VaccinationsDao vaccinationsDao = new VaccinationsDao();
        List<Vaccinations> childInfo = vaccinationsDao.getById(vacID);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.kid_vaccinations;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Saving Vaccination data...");
        pDialog.show();
        JSONObject obj = null;

        try {
            obj = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("auth_token",Constants.getToken(this));
            obj.put("user", user);

            JSONObject vaccination = new JSONObject();

            vaccination.put("imei_number", Constants.getIMEI(this));
            vaccination.put("location", childInfo.get(0).id);
            vaccination.put("kid_id", childInfo.get(0).id);
            vaccination.put("vaccination_id", childInfo.get(0).id);
            vaccination.put("version_name", childInfo.get(0).id);
            vaccination.put("location_source", childInfo.get(0).id);

            obj.put("kid_vaccination",vaccination);

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

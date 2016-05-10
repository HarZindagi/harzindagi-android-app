package com.ipal.itu.harzindagi.Activities;


import android.app.Activity;
import android.app.PendingIntent;
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
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Dao.VaccinationsDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.VaccDetailBook;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CardScanWriteVaccine extends AppCompatActivity {


    Tag mytag;
    String push_NFC;
    Activity ctx;
    Button btn;
    double longitude;
    double latitude;
    Bundle bundle;
    Long tsLong;
    List<ChildInfo> data;
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
    private long Child_id;
    private int VisitNum;
    private String NextDueDate;
    private String card_data = "";
    private ImageView imgV;
    List<Integer> lst;
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
        Child_id = bundle.getLong("childid");



        data = ChildInfoDao.getByKId(Child_id);
        if(data.size()==0){
            data = ChildInfoDao.getByLocalKId(Child_id);
        }
        String isSync = "0";
        if( data.get(0).record_update_flag){
            isSync = "1";
        }else{
            isSync = "0";
        }
        push_NFC = data.get(0).kid_id+ "#" + isSync +"#"+ data.get(0).kid_name + "#"+Constants.getUCID(this)+"#"+  data.get(0).book_id +"#"  + data.get(0).epi_number + "#" + data.get(0).imei_number +    "#" + bundle.getString("visit_num") + "#" + bundle.getString("vacc_details");


//filter work
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


//end
       /* btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Push_into_NFC();
            }
        });
*/

    }
    private void enableTagWriteMode() {
        mWriteMode = true;

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }



    public int Push_into_NFC() {


        Toast.makeText(this, "بچے کی معلومات محفوظ کر دی گئی ہیں", Toast.LENGTH_LONG).show();


        VaccDetailBook vdb=new VaccDetailBook();

       lst = VaccinationsDao.get_VaccinationID_Vaccs_details(Integer.parseInt(bundle.getString("visit_num")), bundle.getString("vacc_details"),vdb);


        Calendar calendar = Calendar.getInstance();
        String imei = Constants.getIMEI(this);
        long time = calendar.getTimeInMillis() / 1000;
        for (int i = 0; i < lst.size(); i++) {

            KidVaccinationDao kd = new KidVaccinationDao();


                long kId  =data.get(0).kid_id;

                if(imei.equals(data.get(0).imei_number)) {
                    kd.save(data.get(0).location, kId, (int) lst.get(i), data.get(0).image_path,time, false, data.get(0).imei_number);
                }else{
                    kd.save(data.get(0).location, kId, (int) lst.get(i), data.get(0).image_path, time, false, data.get(0).imei_number,imei);
                }

        }



        List<ChildInfo> childInfo = ChildInfoDao.getByKId(data.get(0).kid_id);
        DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date date = new Date();
        try {
             date =sdf.parse(bundle.getString("next_date"));

            childInfo.get(0).next_due_date =   date.getTime();
            childInfo.get(0).save();
        } catch (ParseException e) {
            e.printStackTrace();
        }




        String [] ayy=bundle.getString("vacc_details").toString().split(",");
        for(int i=0;i<vdb.vaccinfo.size();i++)
        {

            if(ayy[i].equals("0"))
            {

            vdb.vaccinfo.get(i).day= "--";
            vdb.vaccinfo.get(i).month= "--";
            vdb.vaccinfo.get(i).year= "--";}
            else
            {

                vdb.vaccinfo.get(i).day= String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                vdb.vaccinfo.get(i).month= String.valueOf(calendar.get(Calendar.MONTH));
                vdb.vaccinfo.get(i).year= String.valueOf(calendar.get(Calendar.YEAR));

            }

        }

        Intent myintent = new Intent(this, VaccineList.class);
        myintent.putExtra("VaccDetInfo",vdb);
        myintent.putExtra("next_due_date",bundle.getString("next_date"));
        myintent.putExtra("visit_num_",bundle.getInt("curr_visit_num"));

        startActivity(myintent);
        finish();

        return 0;
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
    public void onNewIntent(Intent intent) {

        mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (mWriteMode) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefRecord record = NdefRecord.createMime(push_NFC, push_NFC.getBytes());
            NdefMessage message = new NdefMessage(new NdefRecord[]{record});
            if (writeTag(message, detectedTag)) {
                //   Toast.makeText(this, "Success: Wrotgme placeid to nfc tag", Toast.LENGTH_LONG)
                //.show();
            /*    btn.setText("آگے چلیں");
                btn.setVisibility(View.VISIBLE);
                btn.setEnabled(true);*/

                mWriteMode = false;
                Push_into_NFC();


            }
        }


    /*    mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
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
*/
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
            //btn.setText("Tap Again To Write");
            Toast.makeText(this,"برائے مہربانی کارڈ کو دوبارہ سکین کریں",Toast.LENGTH_LONG).show();
            //btn.setEnabled(false);

            return false;
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

      enableTagWriteMode();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    private void sendVaccinationsData(String Location, int KidID, int VaccinationID, long CreateTime,final int index) {
        // Instantiate the RequestQueue.
        KidVaccinationDao kidVac = new KidVaccinationDao();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.kid_vaccinations;
       // final ProgressDialog pDialog = new ProgressDialog(this);
      //  pDialog.setMessage("Saving Vaccination data...");
      //  pDialog.show();
        JSONObject obj = null;

        try {
            obj = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("auth_token", Constants.getToken(this));
            obj.put("user", user);

            JSONObject vaccination = new JSONObject();

            vaccination.put("imei_number", Constants.getIMEI(this));
            vaccination.put("location", Location);
            vaccination.put("kid_id", KidID);
            vaccination.put("vaccination_id",VaccinationID);
            vaccination.put("version_name", "");
            vaccination.put("location_source","");
            vaccination.put("vac_time",CreateTime);


            obj.put("kid_vaccination", vaccination);

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
                       // pDialog.hide();
                        if(response!=null)
                        {
                            KidVaccinationDao kd = new KidVaccinationDao();
                            Calendar calendar = Calendar.getInstance();
                            String imei = Constants.getIMEI(CardScanWriteVaccine.this);
                            kd.save(data.get(0).location, data.get(0).kid_id, (int) lst.get(index), data.get(0).image_path, calendar.getTimeInMillis(),true,imei);




                        }
                        if (response.optBoolean("success")) {
                           // JSONObject json = response.optJSONObject("data");
                            //parseKidReponse(json);
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               // pDialog.hide();
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

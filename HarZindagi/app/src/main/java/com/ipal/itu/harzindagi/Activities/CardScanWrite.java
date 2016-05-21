package com.ipal.itu.harzindagi.Activities;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.Books;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;


public class CardScanWrite extends AppCompatActivity {


    Tag mytag;
    String push_NFC;
    Activity ctx;
    Button btn;
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
    private long kid_id;
    private int VisitNum;
    private String NextDueDate;
    private String card_data = "";
    private ImageView imgV;
    boolean mWriteMode = true;
    String bookID;
    String visitNum = "1";
    String vaccsDetails= "0,0,0";
    String isSync = "0";
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

        Child_id = bundle.getString("ID","-1");
        kid_id = bundle.getLong("kid_id");

        address = bundle.getString("address");

        tsLong = System.currentTimeMillis() / 1000;

        bookID =  bundle.getString("bookid");
        writeDataToDB();

        push_NFC = kid_id + "#" + isSync +"#"+bundle.getString("Name") + "#"+Constants.getUCID(this)+"#"+bookID+"#" + bundle.getString("epi")  +"#"+Constants.getIMEI(this) +"#"+visitNum+"#"+vaccsDetails;

// intent invoke filter
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
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    private void enableTagWriteMode() {
        mWriteMode = true;
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }

    private void disableTagWriteMode() {
        mWriteMode = false;
        mNfcAdapter.disableForegroundDispatch(this);
    }


    public int startNewActivity() {


        Toast.makeText(this, "بچے کی معلومات محفوظ کر دی گئی ہیں", Toast.LENGTH_LONG).show();


        Intent myintent = new Intent(this, RegisteredChildActivity.class);
        myintent.putExtra("childid", kid_id);
        myintent.putExtra("imei", Constants.getIMEI(this));
        myintent.putExtra("EPIname", bundle.getString("EPIname"));
        startActivity(myintent);
        finish();
        return 0;
    }
    private  void writeDataToDB(){
        Calendar calendar = Calendar.getInstance();
        Long tsLong = calendar.getTimeInMillis() / 1000;
        ChildInfoDao childInfoDao = new ChildInfoDao();
        List<ChildInfo> item = ChildInfoDao.getByKId(kid_id);
        long mkid_id = -1l;
        if (item.size() == 0) {
            mkid_id = childInfoDao.save(bookID, Child_id, bundle.getString("Name"), bundle.getInt("Gender"), bundle.getString("DOB"), bundle.getString("mName"), bundle.getString("gName"), bundle.getString("cnic"), bundle.getString("pnum"), tsLong, RegisterChildActivity.location, bundle.getString("EPIname"), "abc", bundle.getString("img"), card_data, true, false, address, Constants.getIMEI(this));
            Books books = new Books();
            books.date = Calendar.getInstance().getTimeInMillis()/1000;
            books.kid_id = Long.parseLong(Child_id);
            books.book_number = Integer.parseInt(bookID);
            books.is_sync =false;
            books.save();
        }
         else {
            childInfoDao.save(item.get(0), bundle.getString("Name"), bundle.getString("cnic"), bundle.getString("pnum"));
            mkid_id = kid_id;
            if(item.get(0).record_update_flag==true){
                isSync = "1";
            }else{
                isSync = "0";
            }
        }
        item = ChildInfoDao.getByKId(mkid_id);
        Bundle bnd= KidVaccinationDao.get_visit_details_db(mkid_id);
        if(bnd!=null) {
            visitNum = bnd.getString("visit_num", "1");
            vaccsDetails = bnd.getString("vacc_details", "0,0,0");
        }
        kid_id =  item.get(0).kid_id;
        bookID = item.get(0).book_id;



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
               /* btn.setText("آگے چلیں");
                btn.setVisibility(View.VISIBLE);
                btn.setEnabled(true);*/
                mWriteMode = false;
                startNewActivity();


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
           // btn.setText("برائے مہربانی کارڈ کو دوبارہ سکین کریں");
            Toast.makeText(ctx, "برائے مہربانی کارڈ کو دوبارہ سکین کریں", Toast.LENGTH_LONG).show();

           // btn.setEnabled(false);

            return false;
        }
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


}

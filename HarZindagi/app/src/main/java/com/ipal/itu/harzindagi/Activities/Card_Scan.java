package com.ipal.itu.harzindagi.Activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.util.Calendar;
import java.util.List;


public class Card_Scan extends BaseActivity {

    TextView Scan_txt;
    Tag mytag;
    Context ctx;
    ImageView imgV;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;

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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
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
                        if (recs[j].getTnf() == NdefRecord.TNF_MIME_MEDIA || recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN) {
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


        String Arry[] = s.split("#");
        if (Arry.length > 3) {
            if (!Constants.getIMEI(Card_Scan.this).equals(Arry[Arry.length - 3])) {
                Intent intent1 = new Intent(this, SearchActivity.class);
                intent1.putExtra("book_num", Arry[Arry.length - 4]);
                startActivity(intent1);
                //addNewRecord(Arry);
                Toast.makeText(ctx, "بچہ دوسرے یو سی کا ہے۔ تلاش کریں", Toast.LENGTH_LONG).show();

            } else {

                openVaccinationActivity(Arry[0], Arry[Arry.length - 3], Arry[Arry.length -4], Arry[1], Arry[2], Arry[3]);


            }

        } else {
            Toast.makeText(ctx, "Try Again!", Toast.LENGTH_LONG).show();
        }


    }

    private void openVaccinationActivity(String childID, String imei, String bookid, String isSync, String cnic, String phone) {
        List<ChildInfo> data;
        if (isSync.equals("1")) {
            data = ChildInfoDao.getByKIdAndIMEI(Integer.parseInt(childID), imei);
        } else {
          //  data = ChildInfoDao.getByLocalKIdandIMEI(Integer.parseInt(childID), imei);

                if (!cnic.equals("") && !cnic.equals("null"))
                    data = ChildInfoDao.getByLocalCnicandIMEI(cnic, imei);
                else
                    data = ChildInfoDao.getByLocalPhoneandIMEI(phone, imei);

        }

        Intent intent = new Intent(this, VaccinationActivity.class);
        long kid = 0;
        if (data.size() == 0) {
            Toast.makeText(ctx, "No Record Found!", Toast.LENGTH_LONG).show();
            return;
        }
        if (data.get(0).kid_id != null) {
            kid = data.get(0).kid_id;
        } else {
            finish();
            return;
        }
        if (data.size() == 1) {
            Bundle bnd = KidVaccinationDao.get_visit_details_db(kid);
            intent.putExtra("childid", data.get(0).kid_id);
            intent.putExtra("imei", data.get(0).imei_number);
            intent.putExtra("isSync", data.get(0).record_update_flag);
            intent.putExtra("bookid", Integer.parseInt(bookid));

            intent.putExtras(bnd);
            startActivity(intent);

        }else{
            Intent intent1 = new Intent(this,AfterScanCard.class);
            intent1.putExtra("imei",imei);
            if(cnic.equals(""))
            intent1.putExtra("phone", phone);
            else
            intent1.putExtra("cnic", cnic);
            startActivity(intent1);
        }

        finish();
    }

   /* private void addNewRecord(String[] array) {

        List<ChildInfo> childRec = ChildInfoDao.getByKIdAndIMEI(Long.parseLong(array[0]), array[array.length - 3]);
        if (childRec.size() > 0) {
            return;
        }
        ChildInfo childInfo = new ChildInfo();
        childInfo.kid_name = array[2];
        childInfo.epi_number = array[array.length - 4];
        childInfo.kid_id = Long.parseLong(array[0]);
        childInfo.book_id = array[5];
        childInfo.mobile_id = Long.parseLong(array[0]);
        childInfo.child_address = "";
        childInfo.guardian_name = "";
        childInfo.next_due_date = Calendar.getInstance().getTimeInMillis() / 1000;
        if (array[1].equals("1")) {
            childInfo.record_update_flag = true;
        } else {
            childInfo.record_update_flag = false;
        }

        childInfo.image_path = "image_" + Long.parseLong(array[0]);
        childInfo.imei_number = array[array.length - 3];


      *//*  for (int i = 0; i < childRec.size(); i++) {
            childRec.get(i).delete();
        }*//*

        childInfo.save();
    }
*/
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

package com.ipal.itu.harzindagi.Activities;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.LocationAjaxCallback;
import com.ipal.itu.harzindagi.CustomeViews.MaskedEditText;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Dao.VaccinationsDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;

import com.ipal.itu.harzindagi.Entity.FemaleName;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Entity.MaleName;
import com.ipal.itu.harzindagi.Entity.Towns;
import com.ipal.itu.harzindagi.Entity.VaccDetailBook;
import com.ipal.itu.harzindagi.GJson.GAreasList;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;
import com.ipal.itu.harzindagi.Utils.SpaceTokenizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RegisterChildActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int CALENDAR_CODE = 100;
    public static String location = "0.0000,0.0000";
    EditText CenterName;
    List<Towns> list_Towns;
    MultiAutoCompleteTextView childName;
    MultiAutoCompleteTextView registerChildTown_ET;
    Button boy;
    Button girl;
    View DOB;
    TextView DOBText;
    MultiAutoCompleteTextView motherName;
    MultiAutoCompleteTextView guardianName;
    MaskedEditText guardianCNIC;
    MaskedEditText guardianMobileNumber;
    String Fpath;
    EditText houseAddress;
    EditText EPINumber;
    Button childPicture;
    String epiNumber;
    String EPICenterName, TownName;
    String ChildName;
    String DateOfBirth;
    String MotherName;
    String GuardianName;
    String GuardianCNIC;
    String GuardianMobileNumber;
    int Gender = -1;
    String app_name;
    FileOutputStream fo;
    ArrayAdapter<String> adp;
    ArrayAdapter<String> adp_wm;
    int phn_nm = 1000;
    List<ChildInfo> data;

    Calendar myCalendar = Calendar.getInstance();
    EditText registerboodid;
    long activityTime;
    ArrayList<String> mlist = new ArrayList<>();
    ArrayList<String> flist = new ArrayList<>();
    HashMap<String, String> mhList = new HashMap<>();
    HashMap<String, String> fhList = new HashMap<>();


    private PopupWindow pw;
    private View popUpView;

    private void createContexMenu() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popUpView = inflater.inflate(R.layout.contex_popup, null, false);
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
        pw = new PopupWindow(popUpView, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw.setOutsideTouchable(true);
        pw.setTouchable(true);
        pw.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_up_bg_drawable));
    }

    private void loadNameLists() {

        List<MaleName> mNames = MaleName.getAll();
        List<FemaleName> fNames = FemaleName.getAll();
        for (int i = 0; i < mNames.size(); i++) {
            mlist.add(mNames.get(i).name);
            mhList.put(mNames.get(i).name,"1");
        }
        for (int i = 0; i < fNames.size(); i++) {
            flist.add(fNames.get(i).name);
            fhList.put(fNames.get(i).name,"1");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadNameLists();

        activityTime = Calendar.getInstance().getTimeInMillis() / (1000);
        setContentView(R.layout.activity_register_child);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        app_name = getResources().getString(R.string.app_name);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        registerboodid = (EditText) findViewById(R.id.registerboodid);
        childName = (MultiAutoCompleteTextView) findViewById(R.id.registerChildName);
        childName.setTokenizer(new SpaceTokenizer());

        adp = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, mlist);


        childName.setThreshold(1);
        childName.setAdapter(adp);
        DOB = (View) findViewById(R.id.registerChildDOB);
        DOBText = (TextView) findViewById(R.id.registerChildDOBText);
        EPINumber = (EditText) findViewById(R.id.registerChildUCNumber);

        CenterName = (EditText) findViewById(R.id.registerChildEPICenterName);

        // Spinner item selection Listener
        addItemsOnSpinnerAge_yr();

        houseAddress = (EditText) findViewById(R.id.registerChildAddress);

        boy = (Button) findViewById(R.id.registerChildSexMale);
        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gender = 1;
                boy.setBackgroundResource(R.drawable.roundbutton);
                boy.setTextColor(getResources().getColor(R.color.white));
                girl.setBackgroundResource(R.drawable.registerbuttonborder);
                girl.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        girl = (Button) findViewById(R.id.registerChildSexFemale);
        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gender = 0;
                girl.setBackgroundResource(R.drawable.roundbutton);
                girl.setTextColor(getResources().getColor(R.color.white));
                boy.setBackgroundResource(R.drawable.registerbuttonborder);
                boy.setTextColor(getResources().getColor(R.color.colorPrimary));
                ;
            }
        });


        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterChildActivity.this, CalenderActivity.class);
                startActivityForResult(intent, CALENDAR_CODE);

            }
        });
        DOBText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterChildActivity.this, CalenderActivity.class);
                startActivityForResult(intent, CALENDAR_CODE);
            }
        });
        adp_wm = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, flist);
        motherName = (MultiAutoCompleteTextView) findViewById(R.id.registerChildMotherName);
        motherName.setTokenizer(new SpaceTokenizer());


        motherName.setThreshold(1);
        motherName.setAdapter(adp_wm);

        guardianName = (MultiAutoCompleteTextView) findViewById(R.id.registerChildGuardianName);
        guardianName.setTokenizer(new SpaceTokenizer());


        guardianName.setThreshold(1);
        guardianName.setAdapter(adp);
        guardianCNIC = (MaskedEditText) findViewById(R.id.registerChildGuardianCNIC);


        guardianMobileNumber = (MaskedEditText) findViewById(R.id.registerChildGuardianMobileNumber);
        Button txt_data = (Button) findViewById(R.id.text);
        txt_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ChildInfo> test = new ArrayList<>();
                for (int i = 1; i <= 1000; i++) {
                    phn_nm = phn_nm + i;
                    ChildInfo chInfo = new ChildInfo();
                    chInfo.epi_number = "1000" + i;
                    chInfo.epi_name = "uc" + i;
                    chInfo.kid_name = "ali" + i;
                    chInfo.gender = 1;
                    chInfo.kid_id = 1L + i;

                    chInfo.image_path = "image_" + chInfo.epi_number;
                    chInfo.date_of_birth = "01-jun-2015";
                    chInfo.guardian_name = "Hassan" + 1;
                    if (i > 0 && i < 10) {
                        chInfo.guardian_cnic = "12345-567891" + i + "-4";
                    }
                    if (i > 9 && i < 100) {
                        chInfo.guardian_cnic = "12345-56789" + i + "-4";
                    }
                    if (i > 99 && i < 1000) {
                        chInfo.guardian_cnic = "12345-5678" + i + "-4";
                    }
                    if (i == 1000) {
                        chInfo.guardian_cnic = "12345-567" + i + "-4";
                    }
                    chInfo.phone_number = "1234-567" + phn_nm;
                    chInfo.mother_name = "ABC" + i;
                    chInfo.child_address = "Street" + i;
                    test.add(chInfo);
                }
                ChildInfoDao childInfoDao = new ChildInfoDao();
                childInfoDao.deleteTable();
                childInfoDao.bulkInsert(test);
                String imei = Constants.getIMEI(RegisterChildActivity.this);
                List<KidVaccinations> items = new ArrayList<KidVaccinations>();
                for (int i = 1; i <= 50; i++) {

                    for (int j = 1; j < 4; j++) {


                        Calendar calendar = Calendar.getInstance();

                        long time = calendar.getTimeInMillis() / 1000;


                        long kId = 1L + i;
                        KidVaccinations kidVaccinations = new KidVaccinations();

                        kidVaccinations.location = "00000,00000";
                        kidVaccinations.kid_id = kId;

                        kidVaccinations.vaccination_id = j;
                        kidVaccinations.image = "image_" + "1000" + i;
                        kidVaccinations.created_timestamp = time;
                        kidVaccinations.is_sync = false;
                        kidVaccinations.imei_number = imei;
                        kidVaccinations.guest_imei_number = imei;
                        items.add(kidVaccinations);

                    }

                }
                KidVaccinationDao kd = new KidVaccinationDao();
                kd.deleteTable();
                kd.bulkInsert(items);

            }
        });
        childName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            guardianName.requestFocus();
                            return true;
                        default:
                            break;
                    }

                }
                return false;
            }
        });

        guardianName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            guardianCNIC.requestFocus();
                            return true;
                        default:
                            break;
                    }

                }
                return false;
            }
        });
        childPicture = (Button) findViewById(R.id.registerChildTakePicture);
        childPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = inputValidate();
                if (!msg.equals("")) {
                    return;
                }
                List<ChildInfo> childInfo = ChildInfoDao.getByEpiNumAndIMEI(EPINumber.getText().toString(), Constants.getIMEI(RegisterChildActivity.this));
                if (childInfo.size() > 0) {
                    showError(EPINumber, "نیا ای پی ای نمبر درج کریں");
                    return;
                }

                Intent cameraIntent = new Intent(RegisterChildActivity.this, CustomCamera.class);

                cameraIntent.putExtra("filename", childName.getText().toString() + EPINumber.getText().toString());

                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        createContexMenu();
      /*  if (getIntent().hasExtra("epiNumber")) {
            String epiNum = getIntent().getStringExtra("epiNumber");
            fillValues(epiNum);

        }*/
        getLocation();
    }

    public void addItemsOnSpinnerAge_yr() {
        registerChildTown_ET = (MultiAutoCompleteTextView) findViewById(R.id.registerChildTown_ET);
        registerChildTown_ET.setTokenizer(new SpaceTokenizer());


        registerChildTown_ET.setThreshold(1);


        Towns town = new Towns();
        list_Towns = town.getAll();
        ArrayList<String> items = new ArrayList<>();

        for (int i = 0; i < list_Towns.size(); i++) {

            items.add(list_Towns.get(i).name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items);

        registerChildTown_ET.setAdapter(adapter);
        registerChildTown_ET.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void showError(View v, String error) {

        ((TextView) popUpView.findViewById(R.id.errorText)).setText(error);
        pw.showAsDropDown(v, 0, -Constants.pxToDp(RegisterChildActivity.this, 10));
        Constants.sendGAEvent(RegisterChildActivity.this, "Error", Constants.getUserName(RegisterChildActivity.this), error, 0);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        v.startAnimation(shake);
    }

  /*  public void fillValues(String epiNumber) {
        List<ChildInfo> chidInfo = ChildInfoDao.getByEpiNum(epiNumber);
        if (chidInfo.size() > 0) {
            EPINumber.setText(chidInfo.get(0).epi_number);
            //CenterName.setText(chidInfo.get(0).kids_station);
            childName.setText(chidInfo.get(0).kid_name);
            Gender = chidInfo.get(0).gender;
            DOBText.setText(chidInfo.get(0).date_of_birth);
            guardianName.setText(chidInfo.get(0).guardian_name);
            guardianCNIC.setText(chidInfo.get(0).guardian_cnic);
            guardianMobileNumber.setText(chidInfo.get(0).phone_number);
            motherName.setText(chidInfo.get(0).mother_name);
            houseAddress.setText(chidInfo.get(0).child_address);
        }

    }*/

    public String inputValidate() {
        String error = "";

        if (registerboodid.getText().length() < 1) {
            error = "برائے مہربانی کتاب کا نمبر درج کریں۔";
            showError(registerboodid, error);

            return error;
        }

        if (EPINumber.getText().length() < 1) {
            error = "برائے مہربانی ای پی آئی نمبر درج کریں۔";
            showError(EPINumber, error);

            return error;
        }
        if (CenterName.getText().length() < 1) {
            error = "برائے مہربانی سینٹر کا نام درج کریں۔";
            showError(CenterName, error);

            return error;
        }
        if (childName.getText().length() < 1) {
            error = "برائے مہربانی بچے کا نام درج کریں۔";
            showError(childName, error);

            return error;
        }
        if (Gender == -1) {
            error = "نامکمل جنس";
            showError(boy, error);

            return error;
        }
        if (DOBText.getText().toString().contains("DD")) {
            error = "برائے مہربانی تاریخ پیدائش درج کریں۔";
            showError(DOBText, error);

            return error;
        }

        if (guardianName.getText().length() < 1) {
            error = "برائے مہربانی سرپرست کا نام درج کریں۔";

            showError(guardianName, error);

            return error;
        }

        String cnic = guardianCNIC.getText().toString().trim();
        if (!cnic.equals("")) {
            if (cnic.length() < 15) {
                error = "برائی مہربانی سرپرست کا درست شناختی کارڈ نمبر درج کریں۔";
                showError(guardianCNIC, error);

                return error;
            }
        }
        String phone = guardianMobileNumber.getText().toString().trim();
        if (!phone.equals("")) {
            if (phone.length() < 12) {
                error = "برائے مہربانی سرپرست کا درست موبائل نمبر درج کریں۔";
                showError(guardianMobileNumber, error);

                return error;
            }
        }
      /*  if (motherName.getText().length() < 1) {
            error = "برائے مہربانی والدہ کا نام درج کریں ۔";
            showError(motherName, error);
            return error;
        }*/
        String town = String.valueOf(registerChildTown_ET.getText());

        if (town.equals("Select Town")) {
            town = "";
            error = "Select Town";
            showError(houseAddress, error);
            return error;
        }

        if (houseAddress.getText().length() < 1) {
            error = "خالی گھر کا ایڈریس";
            showError(houseAddress, error);
            return error;
        }
        return error;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        DOBText.setText(DateOfBirth = sdf.format(myCalendar.getTime()));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == 1888) {
            CustomCamera.progress.dismiss();
            Bitmap photo, resizedImage;
            readEditTexts();
            Fpath = data.getStringExtra("fpath");
            String path = data.getStringExtra("path");
            photo = BitmapFactory.decodeFile(path);
            resizedImage = getResizedBitmap(photo, 256);
            saveBitmap(resizedImage);


            DateOfBirth = DOBText.getText().toString();
            Intent intent = new Intent(RegisterChildActivity.this, CardScanWrite.class);
            intent.putExtra("ID", epiNumber);
            intent.putExtra("kid_id", -1l);
            intent.putExtra("Name", ChildName);
            intent.putExtra("Gender", Gender);
            intent.putExtra("DOB", DateOfBirth);
            intent.putExtra("mName", MotherName);
            intent.putExtra("gName", GuardianName);
            intent.putExtra("cnic", GuardianCNIC);
            intent.putExtra("pnum", GuardianMobileNumber);
            intent.putExtra("img", Fpath);
            intent.putExtra("EPIname", EPICenterName);
            intent.putExtra("bookid", registerboodid.getText().toString());
            intent.putExtra("address", houseAddress.getText().toString());

            this.finish();
            activityTime = (Calendar.getInstance().getTimeInMillis() / 1000) - activityTime;
            Constants.sendGAEvent(RegisterChildActivity.this, "Register Child Time", Constants.getUserName(this), activityTime + " S", activityTime);
            startActivity(intent);
            //imageView.setImageBitmap(photo);
        }
        if (requestCode == CALENDAR_CODE && resultCode == 100) {
            String year = data.getStringExtra("year");
            String month = data.getStringExtra("month");
            String day = data.getStringExtra("day");
            DOBText.setText("" + day + "-" + month + "-" + year);
        }


    }

    public void readEditTexts() {
        epiNumber = EPINumber.getText().toString();
        EPICenterName = CenterName.getText().toString();
        //TownName = array_age_yrs[registerChildTown_ET.getSelectedItemPosition()];
        ChildName = childName.getText().toString();
        MotherName = motherName.getText().toString();
        GuardianName = guardianName.getText().toString();
        GuardianCNIC = guardianCNIC.getText().toString();
        GuardianMobileNumber = guardianMobileNumber.getText().toString();

        ArrayList<MaleName> mnlist = new ArrayList<>();
        ArrayList<FemaleName> fnlist = new ArrayList<>();

        String names = ChildName + " " + GuardianName;
        String[] cNames = names.split(" ");

        for (int i = 0; i <cNames.length ; i++) {
            if(mhList.get(cNames[i])==null){
                MaleName maleName = new MaleName();
                maleName.name = cNames[i];
                mnlist.add(maleName);
            }
        }
        MaleName.bulkInsert(mnlist);

        cNames = MotherName.split(" ");
        for (int i = 0; i <cNames.length ; i++) {
            if(fhList.get(cNames[i])==null){
                FemaleName femaleName = new FemaleName();
                femaleName.name = cNames[i];
                fnlist.add(femaleName);
            }
        }
        FemaleName.bulkInsert(fnlist);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void saveBitmap(Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);

        //Create a new file in sdcard folder.
        File f = new File("/sdcard/" + app_name + "/" + Fpath + ".jpg");
        try {
            try {
                f.createNewFile();
                fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray()); //write the bytes in file
            } finally {
                fo.close(); // remember close the FileOutput
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLocation() {

        LocationAjaxCallback cb = new LocationAjaxCallback();
        //  final ProgressDialog pDialog = new ProgressDialog(this);
        //  pDialog.setMessage("Getting Location");

        cb.weakHandler(this, "locationCb").timeout(20 * 1000).expire(1000 * 30 * 5).async(this);
        //  pDialog.setCancelable(false);
        //  pDialog.show();
    }

    public void locationCb(String url, final Location loc, AjaxStatus status) {

        if (loc != null) {

            double lat = loc.getLatitude();
            double log = loc.getLongitude();
            location = lat + "," + log;


        } else {
            location = "0.0000:0.0000";


        }
    }
}

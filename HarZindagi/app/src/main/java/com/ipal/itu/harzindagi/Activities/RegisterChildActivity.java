package com.ipal.itu.harzindagi.Activities;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.LocationAjaxCallback;
import com.ipal.itu.harzindagi.CustomeViews.MaskedEditText;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Entity.Books;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.FemaleName;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Entity.MaleName;
import com.ipal.itu.harzindagi.Entity.Towns;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;
import com.ipal.itu.harzindagi.Utils.Effects;
import com.ipal.itu.harzindagi.Utils.SpaceTokenizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.R.attr.path;
import static com.androidquery.util.AQUtility.post;
import static com.google.android.gms.analytics.internal.zzy.f;
import static com.google.android.gms.analytics.internal.zzy.s;
import static com.google.android.gms.analytics.internal.zzy.t;

public class RegisterChildActivity extends BaseActivity implements View.OnFocusChangeListener {
    private static final int CAMERA_REQUEST = 1888;
    private static final int CALENDAR_CODE = 100;
    public static String location = "0.0000,0.0000";
    EditText CenterName;
    TextView toolbar_title;
    List<Towns> list_Towns;
    MultiAutoCompleteTextView childName;
    MultiAutoCompleteTextView registerChildTown_ET;

    View DOB;
    TextView DOBText;

    MultiAutoCompleteTextView motherName;
    MultiAutoCompleteTextView guardianName;
    MaskedEditText guardianCNIC;
    MaskedEditText guardianMobileNumber;
    String Fpath;
    EditText houseAddress;
    EditText EPINumber;
    String epiNumber;
    String EPICenterName;
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
    EditText registerBookId;
    long activityTime;
    ArrayList<String> mlist = new ArrayList<>();
    ArrayList<String> flist = new ArrayList<>();
    HashMap<String, String> mhList = new HashMap<>();
    HashMap<String, String> fhList = new HashMap<>();


    private PopupWindow pw;
    private View popUpView;
    private boolean isFirstField = true;
    private int previousID = 0;
    private String previousViewName = "";
    private long fieldTime;
    private RadioGroup radioGroup;

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
            mhList.put(mNames.get(i).name, "1");
        }
        for (int i = 0; i < fNames.size(); i++) {
            flist.add(fNames.get(i).name);
            fhList.put(fNames.get(i).name, "1");
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {

        if (previousID != view.getId() && b && !isFirstField) {
            fieldTime = (Calendar.getInstance().getTimeInMillis() / 1000) - fieldTime;

            // Log.d("time",previousViewName +" : "+ fieldTime+ " Seconds");
            logFieldTime(previousViewName,fieldTime);
            fieldTime = (Calendar.getInstance().getTimeInMillis() / 1000);
            previousViewName = view.getTag().toString();
        }
        if (isFirstField) {
            isFirstField = false;
            previousViewName = view.getTag().toString();
        }
    }


    private void logFieldTime(String name, long time) {
        if (time <= 5) {
            time = 5;
        } else if (time <= 10) {
            time = 10;
        } else if (time <= 15) {
            time = 15;
        } else if (time <= 20) {
            time = 20;
        } else if (time <= 30) {
            time = 30;
        } else if (time <= 40) {
            time = 40;
        }else{
            time = 60;
        }
        Constants.sendGAEvent(RegisterChildActivity.this, Constants.getUserName(RegisterChildActivity.this) +"_"+ Constants.GaEvent.REGISTER_FIELD_TIME, name, time+"", 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadNameLists();
        Effects.getInstance().init(this);
        activityTime = Calendar.getInstance().getTimeInMillis() / (1000);
        fieldTime = Calendar.getInstance().getTimeInMillis() / (1000);
        setContentView(R.layout.activity_register_child);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title=(TextView)findViewById(R.id.toolbar_title);
        toolbar_title.setText("بچے کے کوائف");
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
        registerBookId = (EditText) findViewById(R.id.registerboodid);
        registerBookId.setOnFocusChangeListener(this);
        registerBookId.setTag(Constants.GaEvent.BOOK_TIME);
        registerBookId.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                    Toast.makeText(RegisterChildActivity.this,
                            "should not starts with zero(0)",
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        registerBookId.setText(enteredString.substring(1));
                    } else {
                        registerBookId.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        childName = (MultiAutoCompleteTextView) findViewById(R.id.registerChildName);
        childName.setTokenizer(new SpaceTokenizer());
        childName.setOnFocusChangeListener(this);
        childName.setTag(Constants.GaEvent.REGISTER_NAME_TIME);

        adp = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, mlist);


        childName.setThreshold(1);
        childName.setAdapter(adp);
        DOB = (View) findViewById(R.id.registerChildDOB);
        DOBText = (TextView) findViewById(R.id.registerChildDOBText);

        EPINumber = (EditText) findViewById(R.id.registerChildEPINumber);
        EPINumber.setOnFocusChangeListener(this);
        EPINumber.setTag(Constants.GaEvent.REGISTER_EPI_TIME);

        CenterName = (EditText) findViewById(R.id.registerChildEPICenterName);
        CenterName.setOnFocusChangeListener(this);
        CenterName.setTag(Constants.GaEvent.REGISTER_CENTER_NAME_TIME);


        // Spinner item selection Listener
        addItemsOnSpinnerAge_yr();

        houseAddress = (EditText) findViewById(R.id.registerChildAddress);
        houseAddress.setOnFocusChangeListener(this);
        houseAddress.setTag(Constants.GaEvent.REGISTER_ADDRESS_TIME);
         radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.boy:

                        Gender = 1;
                        break;
                    case R.id.girl:
                        Gender = 0;
                        break;

                }
            }
        });

       /* boy = (Button) findViewById(R.id.registerChildSexMale);
        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View tabBg) {
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
            public void onClick(View tabBg) {
                Gender = 0;
                girl.setBackgroundResource(R.drawable.roundbutton);
                girl.setTextColor(getResources().getColor(R.color.white));
                boy.setBackgroundResource(R.drawable.registerbuttonborder);
                boy.setTextColor(getResources().getColor(R.color.colorPrimary));
                ;
            }
        });*/


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
        guardianName.setOnFocusChangeListener(this);
        guardianName.setTag(Constants.GaEvent.REGISTER_GUARDIAN_TIME);

        guardianName.setThreshold(1);
        guardianName.setAdapter(adp);

        guardianCNIC = (MaskedEditText) findViewById(R.id.registerChildGuardianCNIC);
        guardianCNIC.setOnFocusChangeListener(this);
        guardianCNIC.setTag(Constants.GaEvent.REGISTER_CNIC_TIME);


        guardianMobileNumber = (MaskedEditText) findViewById(R.id.registerChildGuardianMobileNumber);
        guardianMobileNumber.setOnFocusChangeListener(this);
        guardianMobileNumber.setTag(Constants.GaEvent.REGISTER_PHONE_TIME);


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

        ((RippleView) findViewById(R.id.registerChildTakePictureR)).setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
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
                onFocusChange(houseAddress, true);
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
        registerChildTown_ET.setOnFocusChangeListener(this);
        registerChildTown_ET.setTag(Constants.GaEvent.REGISTER_REGION_TIME);

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

    public void showError(final View v, final String error) {
        Effects.getInstance().playSound(Effects.SOUND_1);
        (findViewById(R.id.scrollViewR)).post(new Runnable() {
            public void run() {
                ((ScrollView)findViewById(R.id.scrollViewR)).fullScroll(ScrollView.FOCUS_UP);
            }
        });
        (findViewById(R.id.scrollViewR)).postDelayed(new Runnable() {
            @Override
            public void run() {
                ((TextView) popUpView.findViewById(R.id.errorText)).setText(error);
                pw.showAsDropDown(v, 0, -Constants.pxToDp(RegisterChildActivity.this, 10));
                Constants.sendGAEvent(RegisterChildActivity.this, Constants.getUserName(RegisterChildActivity.this), Constants.GaEvent.REGISTER_ERROR, error, 0);

                Animation shake = AnimationUtils.loadAnimation(RegisterChildActivity.this, R.anim.shake);
                v.startAnimation(shake);
            }
        },500);

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


        if (registerBookId.getText().length() < 1) {

            error = "برائے مہربانی کتاب کا نمبر درج کریں۔";
            showError(registerBookId, error);
            return error;
        } else {
            String bookID = registerBookId.getText().toString();
            List<Books> bookList = Books.getByBookId(Integer.parseInt(bookID));
            if (bookList.size() != 0) {
                error = "برائے مہربانی نئی کتاب کا نمبر درج کریں۔";
                showError(registerBookId, error);

                return error;
            }
        }
        if (CenterName.getText().length() < 1) {
            error = "برائے مہربانی سینٹر کا نام درج کریں۔";
            showError(CenterName, error);

            return error;
        }
        String town = String.valueOf(registerChildTown_ET.getText());

        if (town.equals("")) {
            error = "برائے مہربانی علاقے کا نام منتخب کریں";
            showError(registerChildTown_ET, error);
            return error;
        }
      /*  if (houseAddress.getText().length() < 1) {
            error = "خالی گھر کا ایڈریس";
            showError(houseAddress, error);
            return error;
        }*/
        if (EPINumber.getText().length() < 1) {
            error = "برائے مہربانی ای پی آئی نمبر درج کریں۔";
            showError(EPINumber, error);

            return error;
        }

        if (childName.getText().length() < 1) {
            error = "برائے مہربانی بچے کا نام درج کریں۔";
            showError(childName, error);

            return error;
        }
        if (DOBText.getText().toString().contains("DD")) {
            error = "برائے مہربانی تاریخ پیدائش درج کریں۔";
            showError(DOBText, error);

            return error;
        }
        if (Gender == -1) {
            error = "نامکمل جنس";
            showError(radioGroup, error);

            return error;
        }


        if (guardianName.getText().length() < 1) {
            error = "برائے مہربانی سرپرست کا نام درج کریں۔";

            showError(guardianName, error);

            return error;
        }
        String phone = guardianMobileNumber.getText().toString().trim();
        if (!phone.equals("")) {
            if (phone.length() < 12) {
                error = "برائے مہربانی سرپرست کا درست موبائل نمبر درج کریں۔";
                showError(guardianMobileNumber, error);

                return error;
            }
        }
        String cnic = guardianCNIC.getText().toString().trim();
        if (!cnic.equals("")) {
            if (cnic.length() < 15) {
                error = "برائی مہربانی سرپرست کا درست شناختی کارڈ نمبر درج کریں۔";
                showError(guardianCNIC, error);

                return error;
            }
        }

        if (cnic.equals("") && phone.equals("")) {

            error = "برائے مہربانی سرپرست کا موبائل نمبر درج کریں۔";
            showError(guardianMobileNumber, error);

            return error;

        }
      /*  if (motherName.getText().length() < 1) {
            error ="enter mother name!";
            showError(motherName, error);
            return error;
        }*/



        return error;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        DOBText.setText(DateOfBirth = sdf.format(myCalendar.getTime()));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == 1888) {

            Bitmap photo, resizedImage;
            readEditTexts();
            Fpath = data.getStringExtra("fpath");


                String path = data.getStringExtra("path");
                photo = BitmapFactory.decodeFile(path);
                resizedImage = getResizedBitmap(photo, 256);
                saveBitmap(resizedImage);
                File f = new File("/sdcard/" + app_name + "/" + Fpath + ".jpg");

                if(f.exists()) {
                DateOfBirth = DOBText.getText().toString();
                Intent intent = new Intent(RegisterChildActivity.this, CardScanWrite.class);
                intent.putExtra("ID", epiNumber);
                intent.putExtra("kid_id", -1L);
                intent.putExtra("Name", ChildName);
                intent.putExtra("Gender", Gender);
                intent.putExtra("DOB", DateOfBirth);
                intent.putExtra("mName", MotherName);
                intent.putExtra("gName", GuardianName);
                intent.putExtra("cnic", GuardianCNIC);
                intent.putExtra("pnum", GuardianMobileNumber);
                intent.putExtra("img", Fpath);
                intent.putExtra("EPIname", EPICenterName);
                intent.putExtra("bookid", registerBookId.getText().toString());
                intent.putExtra("address", houseAddress.getText().toString());

                this.finish();
                activityTime = (Calendar.getInstance().getTimeInMillis() / 1000) - activityTime;
                Constants.logTime(RegisterChildActivity.this, activityTime, Constants.GaEvent.REGISTER_TOTAL_TIME);
                startActivity(intent);
            }else{
                Toast.makeText(this,"Take Picture again",Toast.LENGTH_LONG).show();
            }
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

        for (int i = 0; i < cNames.length; i++) {
            if (mhList.get(cNames[i]) == null) {
                MaleName maleName = new MaleName();
                maleName.name = cNames[i];
                mnlist.add(maleName);
            }
        }
        MaleName.bulkInsert(mnlist);

        cNames = MotherName.split(" ");
        for (int i = 0; i < cNames.length; i++) {
            if (fhList.get(cNames[i]) == null) {
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

    @Override
    public void onBackPressed() {
        showBackDialoge();
    }

    private void showBackDialoge() {


        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("کیا آپ رجسٹریشن سکرین سے جانا چاہتے ہیں؟");


        adb.setIcon(R.drawable.info_circle);


        adb.setPositiveButton("ہاں", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Constants.sendGAEvent(RegisterChildActivity.this, Constants.getUserName(RegisterChildActivity.this), Constants.GaEvent.BACK_NAVIGATION, Constants.GaEvent.REGISTER_BACK, 0);
                dialog.dismiss();
                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);


            }
        });


        adb.setNegativeButton("نہیں", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        adb.show();

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
            location = "0.0000,0.0000";


        }
    }

}

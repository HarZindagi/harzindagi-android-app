package com.ipal.itu.harzindagi.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.LocationAjaxCallback;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterChildActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int CALENDAR_CODE = 100;
    EditText CenterName;
    EditText childName;
    Button boy;
    Button girl;
    View DOB;
    TextView DOBText;
    EditText motherName;
    EditText guardianName;
    EditText guardianCNIC;
    EditText guardianMobileNumber;
    String Fpath;
    EditText houseAddress;
    EditText EPINumber;
    Button childPicture;

    String epiNumber;

    String EPICenterName;
    String ChildName, childID;
    String DateOfBirth;
    String MotherName;
    String GuardianName;
    String GuardianCNIC;
    String GuardianMobileNumber;
    int Gender = -1;
    String app_name;
    FileOutputStream fo;

    Calendar myCalendar = Calendar.getInstance();
    public  static  String location = "0.0000,0.0000";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_child);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

        childName = (EditText) findViewById(R.id.registerChildName);
        DOB = (View) findViewById(R.id.registerChildDOB);
        DOBText = (TextView) findViewById(R.id.registerChildDOBText);

        EPINumber = (EditText) findViewById(R.id.registerChildUCNumber);

        CenterName = (EditText) findViewById(R.id.registerChildEPICenterName);
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

        motherName = (EditText) findViewById(R.id.registerChildMotherName);


        guardianName = (EditText) findViewById(R.id.registerChildGuardianName);

        guardianCNIC = (EditText) findViewById(R.id.registerChildGuardianCNIC);

        guardianMobileNumber = (EditText) findViewById(R.id.registerChildGuardianMobileNumber);


        childPicture = (Button) findViewById(R.id.registerChildTakePicture);
        childPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = inputValidate();
                if (!msg.equals("")) {
                    return;
                }
                List<ChildInfo> childInfo = ChildInfoDao.getByEpiNum(EPINumber.getText().toString());
                if (childInfo.size() > 0) {
                    showError(EPINumber, "ڈوپلیکیٹ ریکارڈ");
                    return;
                }

                Intent cameraIntent = new Intent(RegisterChildActivity.this, CustomCamera.class);

                cameraIntent.putExtra("filename", childName.getText().toString() + EPINumber.getText().toString());

                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        createContexMenu();
        if (getIntent().hasExtra("epiNumber")) {
            String epiNum = getIntent().getStringExtra("epiNumber");
            fillValues(epiNum);
        }
        getLocation();
    }

    public void showError(View v, String error) {

        ((TextView) popUpView.findViewById(R.id.errorText)).setText(error);
        pw.showAsDropDown(v, 0, -Constants.pxToDp(RegisterChildActivity.this, 10));

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        v.startAnimation(shake);
    }

    public void fillValues(String epiNumber) {
        List<ChildInfo> chidInfo = ChildInfoDao.getByEpiNum(epiNumber);
        if (chidInfo.size() > 0) {
            EPINumber.setText(chidInfo.get(0).epi_number);
            CenterName.setText(chidInfo.get(0).kids_station);
            childName.setText(chidInfo.get(0).kid_name);
            Gender = chidInfo.get(0).gender;
            DOBText.setText(chidInfo.get(0).date_of_birth);
            guardianName.setText(chidInfo.get(0).guardian_name);
            guardianCNIC.setText(chidInfo.get(0).guardian_cnic);
            guardianMobileNumber.setText(chidInfo.get(0).phone_number);
            motherName.setText(chidInfo.get(0).mother_name);
            houseAddress.setText(chidInfo.get(0).child_address);
        }

    }

    public String inputValidate() {
        String error = "";
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
            if (cnic.length() < 16) {
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
            childID = epiNumber;
            Fpath = data.getStringExtra("fpath");
            String path = data.getStringExtra("path");
            photo = BitmapFactory.decodeFile(path);
            resizedImage = getResizedBitmap(photo, 256);
            saveBitmap(resizedImage);


            DateOfBirth = DOBText.getText().toString();
            Intent intent = new Intent(RegisterChildActivity.this, CardScanWrite.class);
            intent.putExtra("ID", childID);
            intent.putExtra("Name", ChildName);
            intent.putExtra("Gender", Gender);
            intent.putExtra("DOB", DateOfBirth);
            intent.putExtra("mName", MotherName);
            intent.putExtra("gName", GuardianName);
            intent.putExtra("cnic", GuardianCNIC);
            intent.putExtra("pnum", GuardianMobileNumber);
            intent.putExtra("img", Fpath);
            intent.putExtra("EPIname", EPICenterName);
            intent.putExtra("address", houseAddress.getText().toString());

            this.finish();
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
        ChildName = childName.getText().toString();
        MotherName = motherName.getText().toString();
        GuardianName = guardianName.getText().toString();
        GuardianCNIC = guardianCNIC.getText().toString();
        GuardianMobileNumber = guardianMobileNumber.getText().toString();

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

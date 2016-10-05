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
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
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

/**
 * Created by IPAL on 4/7/2016.
 */
public class EditRegisterChildActivity extends BaseActivity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int CALENDAR_CODE = 100;
    TextView CenterName;
    EditText childName;
    Button boy;
    Button girl;
    View DOB;
    TextView DOBText;
    TextView registerChildTown_ET;
    TextView motherName;
    TextView guardianName;
    EditText guardianCNIC;
    EditText guardianMobileNumber;
    String Fpath;
    TextView houseAddress;
    TextView EPINumber;
    Button registerEditChildRecord;

    String epiNumber;
    long kid_id;
    String EPICenterName, TownName;
    String ChildName, childID;
    String DateOfBirth;
    String MotherName;
    String GuardianName;
    String GuardianCNIC;
    String GuardianMobileNumber;
    int Gender = -1;
    String app_name;
    TextView ChildGender;
    Calendar myCalendar = Calendar.getInstance();
    public static String location = "0.0000,0.0000";
    private PopupWindow pw;
    private View popUpView;
    TextView toolbar_title;
    long activityTime;

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
        setContentView(R.layout.activity_edit_child_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("معلومات تبدیل کریں");
        app_name = getResources().getString(R.string.app_name);
        activityTime = Calendar.getInstance().getTimeInMillis() / (1000);
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
        EPINumber = (TextView) findViewById(R.id.registerChildUCNumber);
        registerChildTown_ET = (TextView) findViewById(R.id.registerChildTown_ET);
        houseAddress = (TextView) findViewById(R.id.registerChildAddress);
        boy = (Button) findViewById(R.id.registerChildSexMale);
        girl = (Button) findViewById(R.id.registerChildSexFemale);
        motherName = (TextView) findViewById(R.id.registerChildMotherName);
        guardianName = (TextView) findViewById(R.id.registerChildGuardianName);
        guardianCNIC = (EditText) findViewById(R.id.registerChildGuardianCNIC);
        guardianMobileNumber = (EditText) findViewById(R.id.registerChildGuardianMobileNumber);
        CenterName = (TextView) findViewById(R.id.registerChildEPICenterName);
        ChildGender = (TextView) findViewById(R.id.ChildGender);

        registerEditChildRecord = (Button) findViewById(R.id.registerEditChildRecord);
        registerEditChildRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = inputValidate();
                if (!msg.equals("")) {
                    return;
                }
                List<ChildInfo> childInfo = ChildInfoDao.getByEpiNumAndIMEI(EPINumber.getText().toString(),Constants.getIMEI(EditRegisterChildActivity.this));
                readEditTexts();
                childInfo.get(0).kid_name = ChildName;
                childInfo.get(0).guardian_cnic = GuardianCNIC;
                childInfo.get(0).phone_number = GuardianMobileNumber;
                childInfo.get(0).record_update_flag = false;
                childInfo.get(0).save();
                DateOfBirth = DOBText.getText().toString();
                Intent intent = new Intent(EditRegisterChildActivity.this, CardScanWrite.class);
                intent.putExtra("kid_id", kid_id);
                intent.putExtra("Name", ChildName);
                intent.putExtra("Gender", Gender);
                intent.putExtra("DOB", DateOfBirth);
                intent.putExtra("mName", MotherName);
                intent.putExtra("gName", GuardianName);
                if (!GuardianCNIC.equals("")) {
                    intent.putExtra("cnic", GuardianCNIC);
                } else {
                    intent.putExtra("pnum", GuardianMobileNumber);
                }
                intent.putExtra("img", Fpath);
                intent.putExtra("EPIname", EPICenterName);
                intent.putExtra("address", houseAddress.getText().toString());

                startActivity(intent);
               /* Intent regIntent = new Intent(EditRegisterChildActivity.this, RegisteredChildActivity.class);
                regIntent.putExtra("childid", epiNum);
                startActivity(regIntent);*/
                finish();
                activityTime = (Calendar.getInstance().getTimeInMillis() / 1000) - activityTime;
                Constants.logTime(EditRegisterChildActivity.this, activityTime, Constants.GaEvent.EDIT_REGISTER_TOTAL_TIME);

            }
        });
        createContexMenu();
        if (getIntent().hasExtra("childid")) {
            kid_id = getIntent().getLongExtra("childid", 0);

            fillValues(kid_id);
        }
        getLocation();


    }

    public void showError(View v, String error) {

        ((TextView) popUpView.findViewById(R.id.errorText)).setText(error);
        pw.showAsDropDown(v, 0, -Constants.pxToDp(EditRegisterChildActivity.this, 10));
        Constants.sendGAEvent(EditRegisterChildActivity.this, Constants.getUserName(EditRegisterChildActivity.this), Constants.GaEvent.EDIT_REGISTER_ERROR, error, 0);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        v.startAnimation(shake);
    }

    public void fillValues(long kid_id) {
        List<ChildInfo> chidInfo = ChildInfoDao.getByKId(kid_id);
        if (chidInfo.size() > 0) {
            EPINumber.setText(chidInfo.get(0).epi_number);
            CenterName.setText(chidInfo.get(0).epi_name);
            childName.setText(chidInfo.get(0).kid_name);
            Gender = chidInfo.get(0).gender;
            ChildGender.setText("Female");
            if (chidInfo.get(0).gender == 1)
                ChildGender.setText("Male");
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


        if (childName.getText().length() < 1) {
            error = "برائے مہربانی بچے کا نام درج کریں۔";
            showError(childName, error);

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

        return error;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        DOBText.setText(DateOfBirth = sdf.format(myCalendar.getTime()));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if (requestCode == CAMERA_REQUEST && resultCode == 1888) {

            readEditTexts();
            childID = epiNumber;


            DateOfBirth = DOBText.getText().toString();
            Intent intent = new Intent(EditRegisterChildActivity.this, CardScanWrite.class);
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
        }*/
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
        TownName = registerChildTown_ET.getText().toString();
        ChildName = childName.getText().toString();
        MotherName = motherName.getText().toString();
        GuardianName = guardianName.getText().toString();
        GuardianCNIC = guardianCNIC.getText().toString();
        GuardianMobileNumber = guardianMobileNumber.getText().toString();

    }


    private void getLocation() {

        LocationAjaxCallback cb = new LocationAjaxCallback();
        //  final ProgressDialog pDialog = new ProgressDialog(this);
        //  pDialog.setMessage("Getting Location");

        cb.weakHandler(this, "locationCb").timeout(20 * 1000).expire(1000 * 30 * 5).async(this);
        //  pDialog.setCancelable(false);
        //  pDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == android.R.id.home) {
            // startActivity(new Intent(getApplication(),RegisterChildActivity.class).putExtra("epiNumber",childID));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

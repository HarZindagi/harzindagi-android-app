package com.ipal.itu.harzindagi.Activities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
               /* new DatePickerDialog(RegisterChildActivity.this,
                        date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();*/
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
                    Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                        /*
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
                Intent cameraIntent = new Intent(RegisterChildActivity.this, CustomCamera.class);

                cameraIntent.putExtra("filename", childName.getText().toString() + EPINumber.getText().toString());

                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    public String inputValidate() {
        String error = "";
        if (EPINumber.getText().length() < 1) {
            return error = "نامکمل EPI";
        }
        if (CenterName.getText().length() < 1) {
            return error = "نامکل مرکز   کا نام";
        }
        if (childName.getText().length() < 1) {
            return error = "نامکمل بچہ کا نام";
        }
        if (Gender == -1) {
            return error = "نامکمل جنس";
        }
        if (DOBText.getText().toString().contains("DD")) {
            return error = "نامکمل پیدائش کی تاریخ";
        }

        if (guardianName.getText().length() < 1) {
            return error = "نامکمل سرپرست کا نام";
        }
        String cnic = guardianCNIC.getText().toString().trim();
        if (cnic.length() < 16) {
            return error = "نامکمل شناختی کارڈ نمبر";
        }
        String phone = guardianCNIC.getText().toString().trim();
        if (phone.length() < 12) {
            return error = "نامکمل موبائل نمبر";
        }
        if (motherName.getText().length() < 1) {
            return error = "نامکمل والدہ کا نام";
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
}

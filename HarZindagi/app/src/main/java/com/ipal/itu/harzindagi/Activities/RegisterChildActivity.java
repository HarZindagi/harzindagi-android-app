package com.ipal.itu.harzindagi.Activities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

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

    EditText ucNumber;
    EditText epiCenterName;
    EditText childName;
    Button boy;
    Button girl;
    EditText DOB;
    EditText motherName;
    EditText guardianName;
    EditText guardianCNIC;
    EditText guardianMobileNumber;
    Button childPicture;

    String UCNumber;
    String EPICenterName;
    String ChildName, childID;
    String DateOfBirth;
    Boolean isFolderExists;
    String MotherName;
    String GuardianName;
    String GuardianCNIC;
    String GuardianMobileNumber;
    String Gender;
    private static final int CALENDAR_CODE = 100;
    String app_name;
    FileOutputStream fo;
    Uri imageUri;

    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_child);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        app_name = getResources().getString(R.string.app_name);
        File appFolder = new File("/sdcard/" + app_name);
        isFolderExists = appFolder.exists();
        if (!isFolderExists) {

            appFolder.mkdir();
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        ucNumber = (EditText) findViewById(R.id.registerChildUCNumber);


        epiCenterName = (EditText) findViewById(R.id.registerChildEPICenterName);

        childName = (EditText) findViewById(R.id.registerChildName);


        boy = (Button) findViewById(R.id.registerChildSexMale);
        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gender = "larka";
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
                Gender = "larki";
                girl.setBackgroundResource(R.drawable.roundbutton);
                girl.setTextColor(getResources().getColor(R.color.white));
                boy.setBackgroundResource(R.drawable.registerbuttonborder);
                boy.setTextColor(getResources().getColor(R.color.colorPrimary));
                ;
            }
        });

        DOB = (EditText) findViewById(R.id.registerChildDOB);
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
                Snackbar.make(view, "Picture Taken", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);


            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        DOB.setText(DateOfBirth = sdf.format(myCalendar.getTime()));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo, resizedImage;
            readEditTexts();
            childID = ChildName + UCNumber;
            try {
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                resizedImage = getResizedBitmap(photo,256);
                saveBitmap(resizedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ChildInfoDao childInfoDao = new ChildInfoDao();
            DateOfBirth = DOB.getText().toString();
            childInfoDao.save(UCNumber, EPICenterName, childID, ChildName, Gender, GuardianName, MotherName, DateOfBirth, GuardianCNIC, GuardianMobileNumber, "home");
            Intent intent = new Intent(RegisterChildActivity.this, RegisteredChildActivity.class);
            intent.putExtra("ID",childID);
            startActivity(intent);
            //imageView.setImageBitmap(photo);
        }
        if (requestCode == CALENDAR_CODE && resultCode == 100) {
            String year = data.getStringExtra("year");
            String month = data.getStringExtra("month");
            String day = data.getStringExtra("day");
            DOB.setText("" + day + "-" + month + "-" + year);
        }
    }

    public void readEditTexts() {
        UCNumber = ucNumber.getText().toString();
        EPICenterName = epiCenterName.getText().toString();
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

//Create a new file in sdcard folder.
        File f = new File("/sdcard/" + app_name + "/" + childID + ".jpg");
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

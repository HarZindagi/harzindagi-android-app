package com.ipal.itu.harzindagi.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ipal.itu.harzindagi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterChildActivity extends AppCompatActivity {

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
    String ChildName;
    String DateOfBirth;
    Boolean Gender;     //true for male, false for female
    String MotherName;
    String GuardianName;
    String GuardianCNIC;
    String GuardianMobileNumber;

    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_child);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
            {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        ucNumber = (EditText) findViewById(R.id.registerChildUCNumber);
        UCNumber = ucNumber.getText().toString();

        epiCenterName = (EditText) findViewById(R.id.registerChildEPICenterName);
        EPICenterName = epiCenterName.getText().toString();

        childName = (EditText) findViewById(R.id.registerChildName);
        ChildName = childName.getText().toString();

        boy = (Button) findViewById(R.id.registerChildSexMale);
        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gender = true;
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
                Gender = false;
                girl.setBackgroundResource(R.drawable.roundbutton);
                girl.setTextColor(getResources().getColor(R.color.white));
                boy.setBackgroundResource(R.drawable.registerbuttonborder);
                boy.setTextColor(getResources().getColor(R.color.colorPrimary));
                ;
            }
        });

        DOB = (EditText) findViewById(R.id.registerChildDOB);
        DOB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new DatePickerDialog( RegisterChildActivity.this ,
                        date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        motherName = (EditText) findViewById(R.id.registerChildMotherName);
        MotherName = motherName.getText().toString();

        guardianName = (EditText) findViewById(R.id.registerChildGuardianName);
        GuardianName = guardianName.getText().toString();

        guardianCNIC = (EditText) findViewById(R.id.registerChildGuardianCNIC);
        GuardianCNIC = guardianCNIC.getText().toString();

        guardianMobileNumber = (EditText) findViewById(R.id.registerChildGuardianMobileNumber);
        GuardianMobileNumber = guardianMobileNumber.getText().toString();

        childPicture = (Button) findViewById(R.id.registerChildTakePicture);
        childPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Picture Taken", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void updateLabel()
    {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        DOB.setText(DateOfBirth = sdf.format(myCalendar.getTime()));
    }
}

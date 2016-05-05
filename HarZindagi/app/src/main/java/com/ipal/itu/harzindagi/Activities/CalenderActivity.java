package com.ipal.itu.harzindagi.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ipal.itu.harzindagi.R;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Wahab on 1/29/2016.
 */
public class CalenderActivity extends Activity {

    TextView yearText = null, monthText = null, dayText = null, currentYear, previousYear, lastYear;
    Calendar myCalendar = Calendar.getInstance();
    int[] daysIds = {R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.ten, R.id.eleven, R.id.twelve, R.id.thirteen, R.id.fourteen, R.id.fifteen,
            R.id.sixteen, R.id.seventeen, R.id.nineteen, R.id.eighteen, R.id.twenty, R.id.thirtyone, R.id.twentytwo, R.id.twentythree, R.id.twentyfour, R.id.twentyfive, R.id.twentysix, R.id.twentyseven, R.id.twentyeight, R.id.twentynine, R.id.thirty, R.id.thirtyone};
    int[] monthIds = {R.id.jan, R.id.feb, R.id.mar, R.id.apr, R.id.may, R.id.jun, R.id.jul, R.id.aug, R.id.sep, R.id.oct, R.id.nov, R.id.dec};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        int year = myCalendar.get(Calendar.YEAR);
        currentYear = (TextView) findViewById(R.id.currentYear);
        currentYear.setText("" + year);

        previousYear = (TextView) findViewById(R.id.previousYear);
        previousYear.setText("" + (year - 1));

        lastYear = (TextView) findViewById(R.id.lastYear);
        lastYear.setText("" + (year - 2));
        findViewById(R.id.lastYear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearClickListener(v);
            }
        });
        findViewById(R.id.previousYear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearClickListener(v);
            }
        });
        findViewById(R.id.currentYear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearClickListener(v);
            }
        });
        for (int i = 0; i < daysIds.length; i++) {
            findViewById(daysIds[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dayClickListener(v);
                }
            });
        }
        for (int i = 0; i < monthIds.length; i++) {
            findViewById(monthIds[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monthClickListener(v);
                }
            });
        }
    }


    public void checkAndFinishActivity() {
        if (yearText != null && monthText != null && dayText != null) {
            Intent i = new Intent();
            i.putExtra("year", yearText.getText().toString());
            i.putExtra("month", monthText.getText().toString());
            i.putExtra("day", dayText.getText().toString());
            DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            try {
                Date date = sdf.parse(dayText.getText().toString() + "-" + monthText.getText().toString() + "-" + yearText.getText().toString());
                if (date.after(Calendar.getInstance().getTime())) {
                    Toast.makeText(this, "برائے مہربانی درست تاریخ پیدائش درج کریں", Toast.LENGTH_LONG).show();
                } else {
                    setResult(100, i);
                    finish();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    }

    public void clearYearBackground() {
        if (yearText != null) {
            yearText.setBackgroundResource(0);
        }
    }

    public void yearClickListener(View v) {
        switch (v.getId()) {
            case R.id.currentYear:
                clearYearBackground();
                yearText = (TextView) findViewById(R.id.currentYear);
                yearText.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_bg));
                if (monthText != null && monthText.getText().equals("FEB")) {
                    leapYear();
                }
                checkAndFinishActivity();
                break;
            case R.id.previousYear:
                clearYearBackground();
                yearText = (TextView) findViewById(R.id.previousYear);
                yearText.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_bg));
                if (monthText != null && monthText.getText().equals("FEB")) {
                    leapYear();
                }
                checkAndFinishActivity();
                break;
            case R.id.lastYear:
                clearYearBackground();
                yearText = (TextView) findViewById(R.id.lastYear);
                yearText.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_bg));
                if (monthText != null && monthText.getText().equals("FEB")) {
                    leapYear();
                }
                checkAndFinishActivity();
                break;
        }
    }


    public void clearMonthBackground() {
        if (monthText != null) {
            monthText.setBackgroundResource(0);
        }
    }

    public void showDays() {
        findViewById(R.id.thirtyone).setVisibility(View.VISIBLE);
        findViewById(R.id.thirty).setVisibility(View.VISIBLE);
        findViewById(R.id.twentynine).setVisibility(View.VISIBLE);
    }

    public void hideDays() {
        findViewById(R.id.thirtyone).setVisibility(View.GONE);
        findViewById(R.id.thirty).setVisibility(View.GONE);
        findViewById(R.id.twentynine).setVisibility(View.GONE);
    }

    public void leapYear() {
        if (yearText != null && monthText.getText().equals("FEB")) {
            if (Integer.parseInt(yearText.getText().toString()) % 4 == 0) {
                findViewById(R.id.thirtyone).setVisibility(View.GONE);
                findViewById(R.id.thirty).setVisibility(View.GONE);
                findViewById(R.id.twentynine).setVisibility(View.VISIBLE);
            } else {
                hideDays();
            }
        } else {
            hideDays();
        }
    }

    public void monthClickListener(View v) {
        switch (v.getId()) {
            case R.id.jan:
                showDays();
                clearMonthBackground();
                monthText = (TextView) findViewById(R.id.jan);
                monthText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.feb:
                clearMonthBackground();
                monthText = (TextView) findViewById(R.id.feb);
                monthText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                leapYear();
                checkAndFinishActivity();
                break;
            case R.id.mar:
                showDays();
                clearMonthBackground();
                monthText = (TextView) findViewById(R.id.mar);
                monthText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.apr:
                showDays();
                findViewById(R.id.thirtyone).setVisibility(View.GONE);
                clearMonthBackground();
                monthText = (TextView) findViewById(R.id.apr);
                monthText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.may:
                showDays();
                clearMonthBackground();
                monthText = (TextView) findViewById(R.id.may);
                monthText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.jun:
                showDays();
                findViewById(R.id.thirtyone).setVisibility(View.GONE);
                clearMonthBackground();
                monthText = (TextView) findViewById(R.id.jun);
                monthText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.jul:
                showDays();
                clearMonthBackground();
                monthText = (TextView) findViewById(R.id.jul);
                monthText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.aug:
                showDays();
                clearMonthBackground();
                monthText = (TextView) findViewById(R.id.aug);
                monthText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.sep:
                showDays();
                findViewById(R.id.thirtyone).setVisibility(View.GONE);
                clearMonthBackground();
                monthText = (TextView) findViewById(R.id.sep);
                monthText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.oct:
                showDays();
                clearMonthBackground();
                monthText = (TextView) findViewById(R.id.oct);
                monthText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.nov:
                showDays();
                findViewById(R.id.thirtyone).setVisibility(View.GONE);
                clearMonthBackground();
                monthText = (TextView) findViewById(R.id.nov);
                monthText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.dec:
                showDays();
                clearMonthBackground();
                monthText = (TextView) findViewById(R.id.dec);
                monthText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
        }
    }


    public void clearDayBackground() {
        if (dayText != null) {
            dayText.setBackgroundResource(0);
        }
    }

    public void dayClickListener(View v) {
        switch (v.getId()) {
            case R.id.one:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.one);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.two:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.two);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.three:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.three);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.four:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.four);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.five:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.five);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.six:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.six);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.seven:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.seven);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.eight:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.eight);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.nine:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.nine);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.ten:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.ten);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.eleven:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.eleven);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.twelve:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.twelve);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.thirteen:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.thirteen);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.fourteen:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.fourteen);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.fifteen:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.fifteen);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.sixteen:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.sixteen);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.seventeen:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.seventeen);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.eighteen:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.eighteen);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.nineteen:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.nineteen);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.twenty:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.twenty);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.twentyone:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.twentyone);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.twentytwo:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.twentytwo);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.twentythree:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.twentythree);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.twentyfour:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.twentyfour);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.twentyfive:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.twentyfive);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.twentysix:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.twentysix);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.twentyseven:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.twentyseven);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.twentyeight:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.twentyeight);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.twentynine:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.twentynine);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.thirty:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.thirty);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
            case R.id.thirtyone:
                clearDayBackground();
                dayText = (TextView) findViewById(R.id.thirtyone);
                dayText.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                checkAndFinishActivity();
                break;
        }

    }


}

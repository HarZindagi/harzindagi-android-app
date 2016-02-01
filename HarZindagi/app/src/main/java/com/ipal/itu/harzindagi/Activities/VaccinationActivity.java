package com.ipal.itu.harzindagi.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipal.itu.harzindagi.Adapters.CustomViewPager;
import com.ipal.itu.harzindagi.Adapters.ViewPagerAdapter;
import com.ipal.itu.harzindagi.R;

public class VaccinationActivity extends AppCompatActivity {

    private CustomViewPager mViewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private View firstTab;
    private View secondTab;
    private View thirdTab;
    private View fourthTab;
    private View fifthTab;
    private View sixthTab;

    private ImageView firstTabTickMark;
    private ImageView secondTabTickMark;
    private ImageView thirdTabTickMark;
    private ImageView fourthTabTickMark;
    private ImageView fifthTabTickMark;
    public ImageView sixthTabTickMark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firstTab = findViewById(R.id.vaccinationActivityFirstTab);
        secondTab = findViewById(R.id.vaccinationActivitySecondTab);
        thirdTab =  findViewById(R.id.vaccinationActivityThirdTab);
        fourthTab = findViewById(R.id.vaccinationActivityFourthTab);
        fifthTab =  findViewById(R.id.vaccinationActivityFifthTab);
        sixthTab = findViewById(R.id.vaccinationActivitySixthTab);

        firstTabTickMark = (ImageView) findViewById(R.id.vaccinationActivityFirstTabTick);
        secondTabTickMark = (ImageView) findViewById(R.id.vaccinationActivitySecondTabTick);
        thirdTabTickMark = (ImageView) findViewById(R.id.vaccinationActivityThirdTabTick);
        fourthTabTickMark = (ImageView) findViewById(R.id.vaccinationActivityFourthTabTick);
        fifthTabTickMark = (ImageView) findViewById(R.id.vaccinationActivityFifthTabTick);
        sixthTabTickMark = (ImageView) findViewById(R.id.vaccinationActivitySixthTabTick);

        mViewPager = (CustomViewPager) findViewById(R.id.vaccinationActivityVaccinationsPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, VaccinationActivity.this);
        mViewPager.setPagingEnabled(true);
        mViewPager.setAdapter(viewPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position + 1) {
                    case 1:
                        firstTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                        break;
                    case 2:
                        secondTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                        firstTabTickMark.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        thirdTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                        secondTabTickMark.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        fourthTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                        thirdTabTickMark.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        fifthTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                        fourthTabTickMark.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        sixthTab.setBackgroundResource(R.drawable.vaccinationtab_filled);
                        fifthTabTickMark.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

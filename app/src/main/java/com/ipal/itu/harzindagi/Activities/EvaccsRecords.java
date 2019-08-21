package com.ipal.itu.harzindagi.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipal.itu.harzindagi.Adapters.EvaccPagerAdapter;
import com.ipal.itu.harzindagi.Adapters.PagerAdapter;
import com.ipal.itu.harzindagi.R;

public class EvaccsRecords extends BaseActivity {

    //This is EVACC Record screen.
    //All the kid listing show on this screen

    Button[] tabbg;
    TextView toolbar_title;
    ViewPager viewPager;
    Button thirdTab, fourthTab;

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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_evacc_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("Evaccs-II Child Record");
        final Context ctx = this;
        viewPager = (ViewPager) findViewById(R.id.pagerr);
        thirdTab = (Button) findViewById(R.id.thirdTab);
        fourthTab = (Button) findViewById(R.id.fourthTab);
        thirdTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setpage(0);
            }
        });
        fourthTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setpage(1);
            }
        });
        tabbg = new Button[]{ thirdTab, fourthTab};
        final EvaccPagerAdapter adapter = new EvaccPagerAdapter
                (getSupportFragmentManager(), 2);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i=0;i<2;i++)
                {
                    if (i==position)
                    {
                        tabbg[i].setBackgroundResource(R.color.colorPrimary);
                        tabbg[i].setTextColor(getResources().getColor(R.color.white));

                    }
                    else {
                        tabbg[i].setBackgroundResource(R.color.white);
                        tabbg[i].setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void setpage(int a)
    {
        viewPager.setCurrentItem(a);
       /* for (int i=0;i<4;i++)
        {
            if (i==a)
            {
            }
            else {
            }
        }*/

    }

}

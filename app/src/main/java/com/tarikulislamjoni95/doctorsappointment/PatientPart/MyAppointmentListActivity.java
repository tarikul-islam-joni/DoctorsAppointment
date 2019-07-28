package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tarikulislamjoni95.doctorsappointment.R;

public class MyAppointmentListActivity extends AppCompatActivity
{
    MyAppointmentViewPagerAdapter adpter;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);
        mTabLayout=findViewById(R.id.tab_layout);
        mViewPager=findViewById(R.id.view_pager);
        adpter=new MyAppointmentViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adpter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}

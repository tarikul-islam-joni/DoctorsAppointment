package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.R;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    //Class Variable
    private MyToastClass myToastClass;


    private Activity activity;
    private Intent intent;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private PatientMainActivityViewPagerAdapter patientMainActivityViewPagerAdapter;

    private CircleImageView ProfileImageCiv;
    private TextView ProfileNameTv,ProfileContactNoTv;
    private Button SignOutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_patient);
        DrawerAndNavigationStuff();
    }

    private void Initialization()
    {
        activity=PatientMainActivity.this;
    }
    private void InitializationUI()
    {
        viewPager=findViewById(R.id.view_pager);
        tabLayout=findViewById(R.id.table_layout);
        SignOutBtn=findViewById(R.id.signout_btn);
        SignOutBtn.setOnClickListener(this);
    }
    private void InitializationClass()
    {
        patientMainActivityViewPagerAdapter=new PatientMainActivityViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(patientMainActivityViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        myToastClass=new MyToastClass(activity);
    }

    private void DrawerAndNavigationStuff()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View view=navigationView.getHeaderView(0);
        ProfileImageCiv=view.findViewById(R.id.image_civ);
        ProfileImageCiv.setEnabled(false);
        ProfileImageCiv.setOnClickListener(this);
        ProfileNameTv=view.findViewById(R.id.name_tv);
        ProfileContactNoTv=view.findViewById(R.id.contact_no_tv);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patient_main, menu);
                return false;
            }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_appointment_item)
        {
        }
        else if (id == R.id.profile_view_item)
        {
        }
        else if (id==R.id.store_medical_history_item)
        {
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.image_civ:
                break;
            case R.id.signout_btn:
                break;
        }
    }
}

package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.R;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DoctorMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DoctorMainActivityViewPagerAdapter doctorMainActivityViewPagerAdapter;

    private String UID;
    private ArrayList<String> UserInformation;

    private Activity activity;
    private Intent intent;

    private Button SignOutBtn;
    private ImageView ProfileImageCiv;
    private TextView ProfileNameTv,ContactNumberTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_doctor);
        DrawerAndNavigationStuff();
    }

    private void Initialization()
    {
        activity=DoctorMainActivity.this;
        UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserInformation=new ArrayList<>();
    }
    private void InitializationUI()
    {
        SignOutBtn=findViewById(R.id.signout_btn);
        SignOutBtn.setOnClickListener(this);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);

        doctorMainActivityViewPagerAdapter=new DoctorMainActivityViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(doctorMainActivityViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
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

        ProfileImageCiv=navigationView.getHeaderView(0).findViewById(R.id.image_civ);
        ProfileNameTv=navigationView.getHeaderView(0).findViewById(R.id.name_tv);
        ContactNumberTv=navigationView.getHeaderView(0).findViewById(R.id.contact_no_tv);
        ProfileImageCiv.setOnClickListener(this);
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
        getMenuInflater().inflate(R.menu.doctor_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id== R.id.edit_profile_btn)
        {
            intent=new Intent(activity, EditDoctorProfileActivity.class);
            startActivity(intent);
        }
        else if (id==R.id.edit_appointment_info)
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
            case R.id.signout_btn:
                break;
            case R.id.image_civ:
                break;
        }
    }
}

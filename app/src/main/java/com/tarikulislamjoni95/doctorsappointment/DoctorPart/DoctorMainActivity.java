package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.AccountPart.SignInActivity;
import com.tarikulislamjoni95.doctorsappointment.AdminPart.AdminPanelMainActivity;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.ImportantTaskOfDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.ImportantTaskOfDBInterface;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.EditPatientProfileActivity;
import com.tarikulislamjoni95.doctorsappointment.R;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.RowId;
import java.util.ArrayList;

public class DoctorMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ImportantTaskOfDBInterface {

    private ImportantTaskOfDB importantTaskOfDB;

    private MyToastClass myToastClass;
    private DoctorMainActivityViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

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
        setContentView(R.layout.activity_doctor_main);
        Initialization();
        InitializationUI();
        InitializationClass();
        DrawerAndNavigationStuff();
        LoadUserProfileData();
        DatabaseInitialization();
    }

    private void DatabaseInitialization()
    {
        importantTaskOfDB=new ImportantTaskOfDB(activity);
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

        adapter=new DoctorMainActivityViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void InitializationClass()
    {
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

        ProfileImageCiv=navigationView.getHeaderView(0).findViewById(R.id.image_civ);
        ProfileNameTv=navigationView.getHeaderView(0).findViewById(R.id.name_tv);
        ContactNumberTv=navigationView.getHeaderView(0).findViewById(R.id.contact_no_tv);
        ProfileImageCiv.setOnClickListener(this);
    }

    private void LoadUserProfileData()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Doctor).child(UID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!dataSnapshot.child(DBConst.Image).getValue().toString().matches("null"))
                {
                    Picasso.get().load(dataSnapshot.child(DBConst.Image).getValue().toString()).into(ProfileImageCiv);
                }
                ProfileNameTv.setText(dataSnapshot.child(DBConst.Name).getValue().toString());
                ContactNumberTv.setText(dataSnapshot.child(DBConst.ContactNo).getValue().toString());

                UserInformation.add(dataSnapshot.child(DBConst.Image).getValue().toString());
                UserInformation.add(dataSnapshot.child(DBConst.Title).getValue().toString());
                UserInformation.add(dataSnapshot.child(DBConst.Name).getValue().toString());
                UserInformation.add(dataSnapshot.child(DBConst.Degree).getValue().toString());
                UserInformation.add(dataSnapshot.child(DBConst.BMDCRegNo).getValue().toString());
                UserInformation.add(dataSnapshot.child(DBConst.StudiedCollege).getValue().toString());
                UserInformation.add(dataSnapshot.child(DBConst.NoOfPracYear).getValue().toString());
                UserInformation.add(dataSnapshot.child(DBConst.Category).getValue().toString());
                UserInformation.add(dataSnapshot.child(DBConst.AvailableArea).getValue().toString());
                UserInformation.add(dataSnapshot.child(DBConst.ContactNo).getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

            intent=new Intent(activity, AdminPanelMainActivity.class);
            myToastClass.LToast("Experiment only . Should Remove this part later");
            startActivity(intent);
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
            intent=new Intent(activity,EditDoctorAppointmentInfoActivity.class);
            startActivity(intent);
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
                importantTaskOfDB.SignOut();
                intent=new Intent(activity, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.image_civ:
                intent=new Intent(activity,DoctorProfileViewActivity.class);
                intent.putStringArrayListExtra(DBConst.Doctor,UserInformation);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void ImportantTaskResult(boolean result) {

    }

    @Override
    public void ImportantTaskResultAndData(boolean result, String data) {

    }
}

package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.R;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private PatientMainActivityViewPagerAdapter viewPagerAdapter;

    private String UID;
    private ArrayList<String> UserInformation;

    private Activity activity;
    private Intent intent;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CircleImageView ProfileImageCiv;
    private TextView ProfileNameTv,ProfileContactNoTv;
    private Button SignOutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
        DrawerAndNavigationStuff();
        Initialization();
        InitializationUI();
        InitializationClass();
        ShowUserData();
    }

    private void Initialization()
    {
        UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        activity=PatientMainActivity.this;
        UserInformation=new ArrayList<>();
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
        viewPagerAdapter=new PatientMainActivityViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
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
        View view=navigationView.getHeaderView(0);
        ProfileImageCiv=view.findViewById(R.id.image_civ);
        ProfileImageCiv.setEnabled(false);
        ProfileImageCiv.setOnClickListener(this);
        ProfileNameTv=view.findViewById(R.id.name_tv);
        ProfileContactNoTv=view.findViewById(R.id.contact_no_tv);
    }

    private void ShowUserData()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Patient).child(UID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    ProfileImageCiv.setEnabled(true);
                    //Show Profile in navigation bar
                    if (!dataSnapshot.child(DBConst.Image).getValue().toString().matches("null"))
                    {
                        Picasso.get().load(dataSnapshot.child(DBConst.Image).getValue().toString()).into(ProfileImageCiv);
                    }
                    ProfileNameTv.setText(dataSnapshot.child(DBConst.Name).getValue().toString());
                    ProfileContactNoTv.setText(dataSnapshot.child(DBConst.ContactNo).getValue().toString());

                    //Get Data for profile view
                    UserInformation.add(dataSnapshot.child(DBConst.Image).getValue().toString());
                    UserInformation.add(dataSnapshot.child(DBConst.Name).getValue().toString());
                    UserInformation.add(dataSnapshot.child(DBConst.FatherName).getValue().toString());
                    UserInformation.add(dataSnapshot.child(DBConst.MotherName).getValue().toString());
                    UserInformation.add(dataSnapshot.child(DBConst.ContactNo).getValue().toString());
                    UserInformation.add(dataSnapshot.child(DBConst.Address).getValue().toString());
                    UserInformation.add(dataSnapshot.child(DBConst.Gender).getValue().toString());
                    UserInformation.add(dataSnapshot.child(DBConst.BloodGroup).getValue().toString());
                    UserInformation.add(dataSnapshot.child(DBConst.BirthDate).getValue().toString());
                    UserInformation.add(dataSnapshot.child(DBConst.BirthCertificateNo).getValue().toString());
                }
                else
                {
                    intent=new Intent(activity,EditPatientProfileActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                //Disable the ProfileImageCiv
                ProfileImageCiv.setEnabled(false);
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
            //Show my appointment
            intent=new Intent(activity, MyAppointmentListActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.profile_view_item)
        {
            GotoProfileView();
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
                GotoProfileView();
                break;
            case R.id.signout_btn:
                FirebaseAuth.getInstance().signOut();
                intent=new Intent(activity, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    private void GotoProfileView()
    {
        intent=new Intent(activity,PatientProfileView.class);
        intent.putExtra(DBConst.Patient,UserInformation);
        startActivity(intent);
    }
}

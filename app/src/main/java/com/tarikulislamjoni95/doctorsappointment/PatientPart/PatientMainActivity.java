package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.AccountPart.SignInActivity;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.DoctorMainActivity;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.R;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private String UID;
    private ArrayList<String> UserInformation;

    private Activity activity;
    private Intent intent;

    private CircleImageView ProfileImageCiv;
    private TextView ProfileNameTv,ProfileContactNoTv;
    private Button SignOutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
        Initialization();
        InitializationUI();
        InitializationClass();
        DrawerAndNavigationStuff();
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

    }
    private void InitializationClass()
    {

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
        ProfileImageCiv.setOnClickListener(this);
        ProfileNameTv=view.findViewById(R.id.name_tv);
        ProfileContactNoTv=view.findViewById(R.id.contact_no_tv);
        SignOutBtn=findViewById(R.id.signout_btn);
        SignOutBtn.setOnClickListener(this);
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
                    if (!dataSnapshot.child(DBConst.Image).getValue().toString().matches("null"))
                    {
                        Picasso.get().load(dataSnapshot.child(DBConst.Image).getValue().toString());
                    }
                    ProfileNameTv.setText(dataSnapshot.child(DBConst.Name).getValue().toString());
                    ProfileContactNoTv.setText(dataSnapshot.child(DBConst.ContactNo).getValue().toString());

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patient_main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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

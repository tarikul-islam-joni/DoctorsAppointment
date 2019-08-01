package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.os.Parcelable;
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
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountDataModel;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.ImportantTaskOfDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.ImportantTaskOfDBInterface;
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
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AccountDBInterface,
        ImportantTaskOfDBInterface{

    //Database Class Variable
    private AccountDB accountDB;
    private ImportantTaskOfDB importantTaskOfDB;

    //Class Variable
    private MyToastClass myToastClass;
    private PatientMainActivityViewPagerAdapter viewPagerAdapter;

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
        DatabaseInitialization();
    }

    private void Initialization()
    {
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

    private void GetAccountDataFromFirebase(boolean result,AccountDataModel dataModel)
    {
        if (result)
        {
            ProfileImageCiv.setEnabled(true);
            //Show Profile in navigation bar
            if (!dataModel.getProfileImageUrl().matches("null"))
            {
                Picasso.get().load(dataModel.getProfileImageUrl()).into(ProfileImageCiv);
            }
            ProfileNameTv.setText(dataModel.getName());
            ProfileContactNoTv.setText(dataModel.getContactNo());
        }
        else
        {
            ProfileImageCiv.setEnabled(false);
            intent=new Intent(activity,EditPatientProfileActivity.class);
            startActivity(intent);
        }

        //Get Data for profile view
        UserInformation.add(dataModel.getProfileImageUrl());
        UserInformation.add(dataModel.getName());
        UserInformation.add(dataModel.getFatherName());
        UserInformation.add(dataModel.getMotherName());
        UserInformation.add(dataModel.getContactNo());
        UserInformation.add(dataModel.getGender());
        UserInformation.add(dataModel.getBloodGroup());
        UserInformation.add(dataModel.getBirthDate());
        UserInformation.add(dataModel.getAddress());
        UserInformation.add(dataModel.getBirthCertificateNo());

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
            myToastClass.LToast("Under Construction");
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
                importantTaskOfDB.SignOut();
                intent=new Intent(activity,SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    private void GotoProfileView()
    {
        intent=new Intent(activity,PatientProfileView.class);
        intent.putStringArrayListExtra(DBConst.Patient,UserInformation);
        startActivity(intent);
    }


    ///******************************Database Part******************************///
    private void DatabaseInitialization()
    {
        accountDB=new AccountDB(activity);
        accountDB.GetPatientAccountData();

        importantTaskOfDB=new ImportantTaskOfDB(activity);
    }
    @Override
    public void GetAccount(boolean result,ArrayList<AccountDataModel> arrayList)
    {
        GetAccountDataFromFirebase
                (result,new AccountDataModel
                                (
                                        arrayList.get(0).getProfileImageUrl(),
                                        arrayList.get(0).getName(),
                                        arrayList.get(0).getFatherName(),
                                        arrayList.get(0).getMotherName(),
                                        arrayList.get(0).getContactNo(),
                                        arrayList.get(0).getGender(),
                                        arrayList.get(0).getBloodGroup(),
                                        arrayList.get(0).getBirthDate(),
                                        arrayList.get(0).getAddress(),
                                        arrayList.get(0).getBirthCertificateNo(),
                                        arrayList.get(0).getBirthCertificateImageUrl(),
                                        arrayList.get(0).getAnotherDocumentImageUrl()
                                )
                );
    }
    @Override
    public void AccountSavingResult(boolean result) {
        //Not Using
    }

    @Override
    public void ImportantTaskResult(boolean result) {

    }

    @Override
    public void ImportantTaskResultAndData(boolean result, String data) {

    }
}

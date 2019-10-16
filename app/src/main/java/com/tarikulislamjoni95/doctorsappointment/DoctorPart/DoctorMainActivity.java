package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBHelper;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorAccountDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.DoctorProfileVisitActivity;
import com.tarikulislamjoni95.doctorsappointment.R;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener , GetDataFromDBInterface
{
    private DBHelper dbHelper;
    private DoctorAccountDB doctorAccountDB;

    private DoctorMainActivityViewPager doctorMainActivityViewPagerAdapter;
    private DoctorYesterdayAppointmentFragment doctorYesterdayAppointmentFragment;
    private DoctorTodayAppointmentFragment doctorTodayAppointmentFragment;
    private DoctorTomorrowAppointmentFragment doctorTomorrowAppointmentFragment;
    private DoctorCustomDateAppointmentFragment doctorCustomDateAppointmentFragment;

    private Activity activity;
    private Intent intent;

    private String VerificationStatus=DBConst.UNVERIFIED;
    private String DoctorUID;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button SignOutBtn;
    private ImageView ProfileImageCiv;
    private TextView ProfileNameTv,ContactNumberTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_main);
        DrawerAndNavigationStuff();
        Initialization();
        InitializationClass();
        InitializationUI();
        InitializationDB();
        CallDBForUIDFromDB();
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

        ProfileImageCiv=navigationView.getHeaderView(0).findViewById(R.id.image_civ_0);
        ProfileNameTv=navigationView.getHeaderView(0).findViewById(R.id.text_view_0);
        ContactNumberTv=navigationView.getHeaderView(0).findViewById(R.id.text_view_1);

        ProfileImageCiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoDoctorProfileView();
            }
        });
    }
    private void Initialization()
    {
        activity=DoctorMainActivity.this;
    }
    private void InitializationClass()
    {
        doctorMainActivityViewPagerAdapter=new DoctorMainActivityViewPager(getSupportFragmentManager());
    }
    private void InitializationUI()
    {
        SignOutBtn=findViewById(R.id.button_0);
        SignOutBtn.setOnClickListener(this);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);
        viewPager.setAdapter(doctorMainActivityViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        doctorYesterdayAppointmentFragment=new DoctorYesterdayAppointmentFragment();
        doctorTodayAppointmentFragment=new DoctorTodayAppointmentFragment();
        doctorTomorrowAppointmentFragment=new DoctorTomorrowAppointmentFragment();
        doctorCustomDateAppointmentFragment=new DoctorCustomDateAppointmentFragment();
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
        getMenuInflater().inflate(R.menu.doctor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            CallDBForSignOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id== R.id.expanded_menu_0)
        {
            GotoDoctorProfileView();
        }
        else if (id==R.id.expanded_menu_1)
        {
            startActivity(new Intent(activity,EditDoctorProfileActivity.class));
        }
        else if (id==R.id.expanded_menu_2)
        {
            if (VerificationStatus.matches(DBConst.UNVERIFIED))
            {
                startActivity(new Intent(activity,EditDoctorSecureInfoActivity.class));
            }
            else if (VerificationStatus.matches(DBConst.VERIFIED))
            {
                new MyToastClass(activity).MyImageLToast(R.layout.custom_toast__1,"Your account is verified.\nThank you for being with us.");
            }
        }
        else if (id==R.id.expanded_menu_3)
        {
            startActivity(new Intent(DoctorMainActivity.this,EditAppointmentSchedule.class));
        }
        else if (id==R.id.expanded_menu_4)
        {
            startActivity(new Intent(activity,DoctorPaymentHistoryActivity.class));
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
            case R.id.button_0:
                CallDBForSignOut();
                break;
            case R.id.image_civ:
                GotoDoctorProfileView();
                break;
        }
    }

    private void GotoDoctorProfileView()
    {
        Intent intent=new Intent(activity,DoctorProfileVisitActivity.class);
        intent.putExtra(DBConst.UID,DoctorUID);
        intent.putExtra(DBConst.PatientAccount,new HashMap<String,Object>());
        startActivity(intent);
    }
    private void GetAccountUIDFromDB(HashMap<String,Object> hashMap)
    {
        if (hashMap.containsKey(DBConst.RESULT))
        {
            if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.NOT_NULL_USER))
            {
                DoctorUID=hashMap.get(DBConst.UID).toString();
                CallDBForAccountInformation();
            }
        }
    }

    private void GetDoctorAccountInformation(HashMap<String,Object> hashMap)
    {
        if (hashMap.containsKey(DBConst.RESULT))
        {
            if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.DATA_EXIST))
            {
                ProfileNameTv.setText("Dr."+hashMap.get(DBConst.Name).toString());
                ContactNumberTv.setText(hashMap.get(DBConst.PhoneNumber).toString());
                if (!hashMap.get(DBConst.ProfileImageUrl).toString().matches(DBConst.UNKNOWN))
                {
                    Picasso.get().load(hashMap.get(DBConst.ProfileImageUrl).toString()).into(ProfileImageCiv);
                }

                if (hashMap.containsKey(DBConst.AuthorityValidity))
                {
                    VerificationStatus=hashMap.get(DBConst.AuthorityValidity).toString();
                    if (hashMap.get(DBConst.AuthorityValidity).toString().matches(DBConst.UNVERIFIED))
                    {
                        new MyToastClass(activity).MyImageLToast(R.layout.custom_toast__1,"Your account is unverified\nPlease provide BMDC registration information\nFeel trouble ? Inform us support@doctorsappointment.com");
                    }
                }
            }
        }
    }


    private void InitializationDB()
    {
        dbHelper=new DBHelper(activity);
        doctorAccountDB=new DoctorAccountDB(activity);
    }

    private void CallDBForUIDFromDB()
    {
        dbHelper.GetUID();
    }

    private void CallDBForAccountInformation()
    {
        doctorAccountDB.GetDoctorAccountInformation(DoctorUID);
    }
    private void CallDBForSignOut()
    {
        dbHelper.SignOut();
    }

    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        Log.d("DoctorMainActivity",WhichDB+" : "+DataHashMap.toString());
        switch (WhichDB)
        {
            case DBConst.GetAccountUID:
                GetAccountUIDFromDB(DataHashMap);
                break;
            case DBConst.GetDoctorAccountInformation:
                GetDoctorAccountInformation(DataHashMap);
                break;
            case DBConst.GetTodayDoctorScheduledAppointmentList:
                doctorTodayAppointmentFragment.GetDataFromActivity(DBConst.GetTodayDoctorScheduledAppointmentList,DataHashMap);
                break;
            case DBConst.GetTomorrowDoctorScheduledAppointmentList:
                doctorTomorrowAppointmentFragment.GetDataFromActivity(DBConst.GetTomorrowDoctorScheduledAppointmentList,DataHashMap);
                break;
            case DBConst.GetYesterdayDoctorScheduledAppointmentList:
                doctorYesterdayAppointmentFragment.GetDataFromActivity(DBConst.GetYesterdayDoctorScheduledAppointmentList,DataHashMap);
                break;
            case DBConst.GetNextSevenDaysDoctorScheduledAppointmentList:
                doctorCustomDateAppointmentFragment.GetDataFromActivity(DBConst.GetNextSevenDaysDoctorScheduledAppointmentList,DataHashMap);
                break;
            case DBConst.GetEntireMonthDoctorScheduledAppointmentList:
                doctorCustomDateAppointmentFragment.GetDataFromActivity(DBConst.GetEntireMonthDoctorScheduledAppointmentList,DataHashMap);
                break;
            case DBConst.GetSelectedMonthWithYearDateAppointmentHistoryFromDoctorAccount:
                doctorCustomDateAppointmentFragment.GetDataFromActivity(DBConst.GetSelectedMonthWithYearDateAppointmentHistoryFromDoctorAccount,DataHashMap);
                break;
            case DBConst.GetCustomDateDoctorScheduledAppointmentList:
                doctorCustomDateAppointmentFragment.GetDataFromActivity(DBConst.GetCustomDateDoctorScheduledAppointmentList,DataHashMap);
                break;
        }
    }

    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList) {

    }

    class DoctorMainActivityViewPager extends FragmentPagerAdapter
    {

        public DoctorMainActivityViewPager(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    return doctorYesterdayAppointmentFragment;
                case 1:
                    return doctorTodayAppointmentFragment;
                case 2:
                    return doctorTomorrowAppointmentFragment;
                case 3:
                    return doctorCustomDateAppointmentFragment;
                    default:
                        return doctorTodayAppointmentFragment;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position)
            {
                case 0:
                    return "Yesterday";
                case 1:
                    return "Today";
                case 2:
                    return "Tomorrow";
                case 3:
                    return "Custom Date";
                    default:
                        return "Today";
            }
        }
    }
}

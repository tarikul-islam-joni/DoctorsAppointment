package com.tarikulislamjoni95.doctorsappointment.AdminPart;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountMultiplicityDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBHelper;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.PaymentDB;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.DoctorTodayAppointmentFragment;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.DatePicker;
import android.widget.Toast;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AdminMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , GetDataFromDBInterface {

    private MyMainActivityViewPagerAdapter myMainActivityViewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DoctorControlFragment doctorControlFragment;
    private PatientControlFragment patientControlFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);
        InitializationDrawerStuff();
        Initialization();
        InitializationClass();
        InitializationUI();
        InitializationDB();
    }
    private void InitializationDrawerStuff()
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
    }
    private void Initialization()
    {

    }
    private void InitializationClass()
    {

    }

    private void InitializationUI()
    {
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);
        myMainActivityViewPagerAdapter=new MyMainActivityViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myMainActivityViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        doctorControlFragment=new DoctorControlFragment();
        patientControlFragment=new PatientControlFragment();
    }

    private void InitializationDB()
    {

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
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            CallDBForSignOut();
            return true;
        }
        else if (id==R.id.action_settings_1)
        {
            ShowCurrentDateAndCreatePayment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    HashMap<String,Object> hashMap;
    private void ShowCurrentDateAndCreatePayment()
    {
        String Key="ABCDE";
        hashMap=new HashMap<>();
        for(int i=0; i<20; i++)
        {
            hashMap.put(Key+i,"123456");
        }
        DatePickerDialog datePickerDialog=new DatePickerDialog(AdminMainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date)
            {
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(DBConst.PaymentDB).child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(date));
                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isComplete())
                        {
                            Toast.makeText(AdminMainActivity.this,hashMap.toString(),Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(AdminMainActivity.this,"Failed to create payment",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH)+1,Calendar.getInstance().get(Calendar.DATE));
        datePickerDialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            MyToastClass myToastClass=new MyToastClass(AdminMainActivity.this);
            myToastClass.MyImageLToast(R.layout.custom_toast__1,"Payment Distribution System Added To Be Later");
        } else if (id == R.id.nav_gallery)
        {
            MyToastClass myToastClass=new MyToastClass(AdminMainActivity.this);
            myToastClass.MyImageLToast(R.layout.custom_toast__1,"More Fetatures Will be Added In Later");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void CallDBForSignOut()
    {
        DBHelper dbHelper=new DBHelper(AdminMainActivity.this);
        dbHelper.SignOut();
    }

    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        Log.d("myData","AdminMainActivity : "+WhichDB+" :: "+DataHashMap.toString());
        switch (WhichDB)
        {
            case DBConst.GetAccountMultiplicityDoctorAccountListFromDB:
                doctorControlFragment.GetDataFromActivity(WhichDB,DataHashMap);
                break;
            case DBConst.GetLockedDoctorAccountListFromDB:
                doctorControlFragment.GetDataFromActivity(WhichDB,DataHashMap);
                break;
            case DBConst.GetSpecificDoctorBMDCInfo:
                doctorControlFragment.GetDataFromActivity(WhichDB,DataHashMap);
                break;
            case DBConst.GetLockedPatientAccountListFromDB:
                patientControlFragment.GetDataFromActivity(DBConst.GetLockedPatientAccountListFromDB,DataHashMap);
                break;
            case DBConst.GetAccountMultiplicityPatientAccountListFromDB:
                patientControlFragment.GetDataFromActivity(DBConst.GetAccountMultiplicityPatientAccountListFromDB,DataHashMap);
                break;
        }
    }

    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList) {

    }


    class MyMainActivityViewPagerAdapter extends FragmentPagerAdapter
    {

        public MyMainActivityViewPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return doctorControlFragment;
                case 1:
                    return patientControlFragment;
                    default:
                        return doctorControlFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position)
            {
                case 0:
                    return "Doctor Control";
                case 1:
                    return "Patient Control";
                    default:
                        return "Doctor Control";
            }
        }
    }
}

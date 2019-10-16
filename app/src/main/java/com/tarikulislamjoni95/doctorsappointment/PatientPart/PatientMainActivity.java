package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBHelper;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.PatientAccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetLocationInterface;
import com.tarikulislamjoni95.doctorsappointment.MyGoogleMapClass.GoogleMapLocationActivity;
import com.tarikulislamjoni95.doctorsappointment.R;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GetLocationInterface , GetDataFromDBInterface
{


    private DBHelper dbHelper;
    private PatientAccountDB patientAccountDB;

    private Activity activity;

    private PatientMainActivityViewPagerAdapter viewPagerAdapter;
    private FragmentHospitalList fragmentHospitalList;
    private FragmentDiseaseList fragmentDiseaseList;
    private FragmentDoctorList fragmentDoctorList;

    private String PatientUID=DBConst.UNKNOWN;
    private HashMap<String,Object> PatientAccountDataHashMap;

    private CircleImageView circleImageView;
    private TextView[] textViews;
    private Button button;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_main);
        ToolBarAndOtherStaffInitialization();
        Initialization();
        InitializationClass();
        InitializationUI();
        InitializationDB();
        CallDBForUID();
    }
    private void ToolBarAndOtherStaffInitialization()
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

        textViews=new TextView[2];
        circleImageView=navigationView.getHeaderView(0).findViewById(R.id.image_civ_0);
        textViews[0]=navigationView.getHeaderView(0).findViewById(R.id.text_view_0);
        textViews[1]=navigationView.getHeaderView(0).findViewById(R.id.text_view_1);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(activity, PatientProfileVisitActivity.class);
                intent.putExtra(DBConst.UID,PatientUID);
                startActivity(intent);
            }
        });
    }
    private void Initialization()
    {
        activity=PatientMainActivity.this;
    }
    private void InitializationClass()
    {
        viewPagerAdapter=new PatientMainActivityViewPagerAdapter(PatientMainActivity.this,getSupportFragmentManager());
        fragmentDoctorList=new FragmentDoctorList();
        fragmentDiseaseList=new FragmentDiseaseList();
        fragmentHospitalList=new FragmentHospitalList();
    }
    private void InitializationUI()
    {
        viewPager=findViewById(R.id.view_pager);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackground(getResources().getDrawable(R.color.colorPrimaryDark));

        button=findViewById(R.id.button_0);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallDbForSignOut();
            }
        });
    }

    private void InitializationDB()
    {
        dbHelper=new DBHelper(activity);
        patientAccountDB=new PatientAccountDB(activity);
    }


    private void GetUIDFromDB(HashMap<String,Object> hashMap)
    {
        if (hashMap.containsKey(DBConst.RESULT))
        {
            if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.NOT_NULL_USER))
            {
                PatientUID=hashMap.get(DBConst.UID).toString();
                CallDBForMyAccountData();
            }
            else
            {
                CallDbForSignOut();
            }
        }
        else
        {
            CallDbForSignOut();
        }
    }

    private void GetPatientAccountInformationFromPatientDB(HashMap<String,Object> hashMap)
    {
        this.PatientAccountDataHashMap=hashMap;
        fragmentDoctorList.GetDataFromActivity(DBConst.GetPatientAccountInformation,hashMap);
        fragmentHospitalList.GetDataFromActivity(DBConst.GetPatientAccountInformation,hashMap);
        fragmentDiseaseList.GetDataFromActivity(DBConst.GetPatientAccountInformation,hashMap);

        if (hashMap.containsKey(DBConst.Name))
        {
            textViews[0].setText(hashMap.get(DBConst.Name).toString());
        }
        if (hashMap.containsKey(DBConst.PhoneNumber))
        {
            textViews[1].setText(hashMap.get(DBConst.PhoneNumber).toString());
        }
        if (hashMap.containsKey(DBConst.ProfileImageUrl))
        {
            if (!hashMap.get(DBConst.ProfileImageUrl).toString().matches(DBConst.UNKNOWN))
            {
                Picasso.get().load(hashMap.get(DBConst.ProfileImageUrl).toString()).into(circleImageView);
            }
        }

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(activity, PatientProfileVisitActivity.class);
                intent.putExtra(DBConst.UID,PatientUID);
                intent.putExtra(DBConst.BackActivity,DBConst.Patient);
                startActivity(intent);
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
        getMenuInflater().inflate(R.menu.patient_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            CallDbForSignOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.nav_menu_0)
        {
            Intent intent=new Intent(activity, PatientProfileVisitActivity.class);
            intent.putExtra(DBConst.BackActivity,DBConst.Patient);
            intent.putExtra(DBConst.UID,PatientUID);
            startActivity(intent);
        }
        else if (id == R.id.nav_menu_1)
        {
            Intent intent=new Intent(activity,EditPatientProfileActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_menu_2)
        {
            Intent intent=new Intent(activity,PatientAppointmentHistory.class);
            intent.putExtra(DBConst.GetPatientAccountInformation,PatientAccountDataHashMap);
            startActivity(intent);
        }
        else if (id == R.id.nav_menu_3)
        {
            Intent intent=new Intent(activity,StoreMedicalDocumentActivity.class);
            intent.putExtra(DBConst.BackActivity,DBConst.Patient);
            intent.putExtra(DBConst.UID,PatientUID);
            startActivity(intent);
        }
        else if (id==R.id.nav_menu_4)
        {
            Intent intent=new Intent(activity, GoogleMapLocationActivity.class);
            intent.putExtra(VARConst.GET_LOCATION,"NearByHospital");
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    ///*********************************Database Call Method Starting****************************///
    private void CallDBForUID()
    {
        dbHelper.GetUID();
    }
    private void CallDBForMyAccountData()
    {
        patientAccountDB.GetPatientAccountInformation(PatientUID);
    }
    private void CallDbForSignOut()
    {
        dbHelper.SignOut();
    }
    ///*********************************Database Call Method Starting****************************///
    @Override
    public void GetLocation(String FromWhich, Map<Integer, String> map)
    {
        fragmentDoctorList.GetDataFromActivity(VARConst.GET_LOCATION,map);
    }
    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        switch (WhichDB)
        {
            case DBConst.GetAccountUID:
                GetUIDFromDB(DataHashMap);
                break;
            case DBConst.GetPatientAccountInformation:
                GetPatientAccountInformationFromPatientDB(DataHashMap);
                break;
            case DBConst.GetDivisionListWithData:
                fragmentDoctorList.GetDataFromActivity(DBConst.GetDivisionListWithData, DataHashMap);
                break;
            case DBConst.GetDiseaseTypeWithData:
                fragmentDiseaseList.GetDataFromActivity(DBConst.GetDiseaseTypeWithData, DataHashMap);
                break;
            case DBConst.GetHospitalListWithData:
                fragmentHospitalList.GetDataFromActivity(DBConst.GetHospitalListWithData, DataHashMap);
                break;
        }
    }

    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList)
    {
        switch (WhichDB)
        {
            case DBConst.GetDoctorListByDivision:
                fragmentDoctorList.GetDataFromActivity(DBConst.GetDoctorListByDivision,DataHashMapArrayList);
                break;
            case DBConst.GetDoctorListByDisease:
                fragmentDiseaseList.GetDataFromActivity(DBConst.GetDoctorListByDisease,DataHashMapArrayList);
                break;
            case DBConst.GetDoctorListByHospital:
                fragmentHospitalList.GetDataFromActivity(DBConst.GetDoctorListByHospital,DataHashMapArrayList);
                break;
        }
    }


    class PatientMainActivityViewPagerAdapter extends FragmentPagerAdapter
    {
        public PatientMainActivityViewPagerAdapter(Activity activity,FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    return fragmentDoctorList;
                case 1:
                    return fragmentDiseaseList;
                case 2:
                    return fragmentHospitalList;
                default:
                    return fragmentDoctorList;
            }
        }

        @Override
        public int getCount()
        {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position)
            {
                case 0:
                    return "Doctor List";
                case 1:
                    return "Disease Type";
                case 2:
                    return "Hospital List";
                default:
                    return null;
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

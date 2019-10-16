package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorAccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfileVisitActivity extends AppCompatActivity implements GetDataFromDBInterface
{
    private DoctorAccountDB doctorAccountDB;
    private DoctorProfileVisitFragmentViewPagerAdapter ViewPageradapter;
    private InitializationUIHelperClass initializationUIHelperClass;
    private FragmentDoctorProfileDetails fragmentDoctorProfileDetails;
    private FragmentDoctorAppointmentDetails fragmentDoctorAppointmentDetails;
    public String DoctorUID;
    private Activity activity;
    private HashMap<String,Object> PatientAccountInformationHashMap;
    private CircleImageView circleImageView;
    private TextView[] textViews;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_doctor_profile_visit);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);

        ViewPageradapter=new DoctorProfileVisitFragmentViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(ViewPageradapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackground(getResources().getDrawable(R.color.colorPrimaryDark));

        NestedScrollView nestedScrollView=(NestedScrollView) findViewById(R.id.nested_scroll_view);
        nestedScrollView.setFillViewport(true);

        Initialization();
        InitializationClass();
        InitializationUI();
        InitializationDB();
    }

    private void Initialization()
    {
        PatientAccountInformationHashMap=(HashMap<String, Object>) getIntent().getSerializableExtra(DBConst.PatientAccount);
        activity=DoctorProfileVisitActivity.this;
        DoctorUID=getIntent().getStringExtra(DBConst.UID);

        fragmentDoctorProfileDetails=new FragmentDoctorProfileDetails(DoctorUID);
        fragmentDoctorAppointmentDetails=new FragmentDoctorAppointmentDetails(DoctorUID);

        fragmentDoctorAppointmentDetails.GetDataFromActivity(DBConst.GetPatientAccountInformation,PatientAccountInformationHashMap);
    }

    private void InitializationClass()
    {
        initializationUIHelperClass=new InitializationUIHelperClass(getWindow().getDecorView());
    }

    private void InitializationUI()
    {
        int[] id={R.id.text_view_0,R.id.text_view_1,R.id.text_view_2,R.id.text_view_3};
        textViews=initializationUIHelperClass.setTextViews(id);
        circleImageView=findViewById(R.id.image_civ_0);
    }
    private void SetUpRatingAndReviewersNumber(int TotalRatings,int TotalReviews)
    {
        textViews[3].setText((float)((float)TotalRatings/(float)TotalReviews)+"(Rated "+TotalReviews+" users)");
    }
    private void SetUpHeadingData(HashMap<String,Object> hashMap)
    {
        textViews[0].setText("Dr."+hashMap.get(DBConst.Name).toString());
        textViews[1].setText("Specialized On ("+hashMap.get(DBConst.Specialization).toString()+")");
        textViews[2].setText(hashMap.get(DBConst.NoOfPracticingYear).toString()+" Years Experience");
        if (!hashMap.get(DBConst.ProfileImageUrl).toString().matches(DBConst.UNKNOWN))
        {
            Picasso.get().load(hashMap.get(DBConst.ProfileImageUrl).toString()).into(circleImageView);
        }
    }
    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        Log.d("DoctorProfileVisit",WhichDB+":: "+DataHashMap.toString());
        switch (WhichDB)
        {
            case DBConst.GetDoctorAccountInformation:
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap=ParseDataToGetDoctorProfileInfo(DataHashMap);
                SetUpHeadingData(hashMap);
                ArrayList<HashMap<String,Object>> hashMapArrayList=new ArrayList<>();
                hashMapArrayList.add(hashMap);
                fragmentDoctorProfileDetails.GetDataFromActivity(DBConst.GetDoctorAccountInformation,hashMapArrayList);
                fragmentDoctorAppointmentDetails.GetDataFromActivity(DBConst.GetDoctorAccountInformation,hashMapArrayList);
                break;
            case DBConst.GetRatingsAndReviewInfoFromDoctorAccountDB:
                ArrayList<HashMap<String,Object>> arrayList=new DataParseForRatingAndReview(DataHashMap).getArrayList();
                int total_rating=new DataParseForRatingAndReview(DataHashMap).getTotalRatings();
                int total_reiviewers=new DataParseForRatingAndReview(DataHashMap).getTotalReviewer();
                SetUpRatingAndReviewersNumber(total_rating,total_reiviewers);
                fragmentDoctorProfileDetails.GetDataFromActivity(DBConst.GetRatingsAndReviewInfoFromDoctorAccountDB,arrayList);
                break;
            case DBConst.GetAppointmentScheduleInfo:
                ArrayList<HashMap<String,Object>> hashMapArrayList1=new ArrayList<>();
                hashMapArrayList1=DataParseForAppointmentScheduleList(DataHashMap);
                fragmentDoctorAppointmentDetails.GetDataFromActivity(DBConst.GetAppointmentScheduleInfo,hashMapArrayList1);
                break;
            case DBConst.GetDoctorSelectedDateAppointmentHistoryFromDoctorAccount:
                fragmentDoctorAppointmentDetails.GetDataFromActivity(DBConst.GetDoctorSelectedDateAppointmentHistoryFromDoctorAccount,DataHashMap);
                break;
            case DBConst.GetPaymentDataFromPaymentDB:
                fragmentDoctorAppointmentDetails.GetDataFromActivity(DBConst.GetPaymentDataFromPaymentDB,DataHashMap);
                break;
            case DBConst.GetStatusOnSaveAppointmentCreationFromDoctorDB:
                fragmentDoctorAppointmentDetails.GetDataFromActivity(DBConst.GetStatusOnSaveAppointmentCreationFromDoctorDB,DataHashMap);
                break;
            case DBConst.GetStatusOnSaveAppointmentCreationFromPatientDB:
                fragmentDoctorAppointmentDetails.GetDataFromActivity(DBConst.GetStatusOnSaveAppointmentCreationFromPatientDB,DataHashMap);
                break;
        }
    }

    private ArrayList<HashMap<String,Object>> DataParseForAppointmentScheduleList(HashMap<String,Object> hashMap)
    {
        ArrayList<HashMap<String,Object>> hashMapArrayList=new ArrayList<>();
        if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            DataSnapshot dataSnapshot=(DataSnapshot)hashMap.get(DBConst.DataSnapshot);
            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
            {
                for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                {
                    HashMap<String,Object> hashMap1=new HashMap<>();
                    for (DataSnapshot dataSnapshot3:dataSnapshot2.getChildren())
                    {
                        hashMap1.put(dataSnapshot3.getKey(),dataSnapshot3.getValue());
                    }
                    hashMapArrayList.add(hashMap1);
                }
            }
        }
        return hashMapArrayList;
    }
    public HashMap<String ,Object> ParseDataToGetDoctorProfileInfo(HashMap<String,Object> dataHashMap)
    {
        HashMap<String,Object> hashMap=new HashMap<>();
        if (dataHashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            DataSnapshot dataSnapshot=(DataSnapshot) dataHashMap.get(DBConst.DataSnapshot);
            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
            {
                hashMap.put(dataSnapshot1.getKey(),dataSnapshot1.getValue());
            }

        }
        return hashMap;
    }
    private void InitializationDB()
    {
        doctorAccountDB=new DoctorAccountDB(activity);
        CallDBForDoctorInformation();
        CallDBForRatingAndReviews();
        CallDBForAppointmentSchedule();
    }

    private void CallDBForDoctorInformation()
    {
        doctorAccountDB.GetDoctorAccountByKey(DoctorUID,DBConst.AccountInformation,DBConst.GetDoctorAccountInformation);
    }
    private void CallDBForRatingAndReviews()
    {
        doctorAccountDB.GetDoctorReviewAndRatingFromDoctorAccountDB(DoctorUID);
    }
    private void CallDBForAppointmentSchedule()
    {
        doctorAccountDB.GetDoctorAccountByKey(DoctorUID,DBConst.AppointmentSchedule,DBConst.GetAppointmentScheduleInfo);
    }
    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList) {

    }

    class DoctorProfileVisitFragmentViewPagerAdapter extends FragmentPagerAdapter
    {

        public DoctorProfileVisitFragmentViewPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return fragmentDoctorProfileDetails;
                case 1:
                    return fragmentDoctorAppointmentDetails;
                    default:
                        return fragmentDoctorProfileDetails;
            }
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0:
                    return "Details";
                case 1:
                    return "Get Appointment";
                    default:
                        return null;
            }
        }
        @Override
        public int getCount() {
            return 2;
        }
    }
}

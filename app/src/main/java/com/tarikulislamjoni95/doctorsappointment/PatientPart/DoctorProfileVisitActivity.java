package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfileVisitActivity extends AppCompatActivity
{
    private ArrayList<String> UserArrayList;
    private TakeAppointmentAdapter adapter;
    private ArrayList<AppointmentListModel> arrayList;

    private ArrayList<PatientAccountDataModel> ProfileDataArrayList;

    private Activity activity;
    private Intent intent;

    private String AuthorityValidity;
    private String UID;

    private AlertDialog dialog;

    public TextView AppointmentAvailabilityStatusTv;
    private TextView NameTv,StudiedTv,NoOfYearPracTv,SpecialityTv,DegreeCompletedTv,AvailableAreaTv;
    private CircleImageView ImageCiv;
    private ListView AppointmentListView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_visit);
        Initialization();
        InitializationUI();
        InitializationClass();
        AppointmentListView.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        ShowDataIfAvailable();
        ShowAppointmentList();
    }

    private void Initialization()
    {
        UserArrayList=new ArrayList<>();
        UserArrayList= getIntent().getStringArrayListExtra(DBConst.Doctor);
        AuthorityValidity=UserArrayList.get(0);
        UID=UserArrayList.get(1);
        activity= DoctorProfileVisitActivity.this;
        arrayList=new ArrayList<>();

    }
    private void InitializationUI()
    {
        NameTv=findViewById(R.id.name_tv);
        AppointmentAvailabilityStatusTv=findViewById(R.id.appointment_availability_status_tv);
        StudiedTv=findViewById(R.id.studied_tv);
        NoOfYearPracTv=findViewById(R.id.no_of_prac_tv);
        SpecialityTv=findViewById(R.id.speciality_tv);
        ImageCiv=findViewById(R.id.image_civ);
        AvailableAreaTv=findViewById(R.id.available_area_tv);
        DegreeCompletedTv=findViewById(R.id.degree_completed_tv);
        AppointmentListView=findViewById(R.id.list_view);
    }
    private void InitializationClass()
    {
        adapter=new TakeAppointmentAdapter(activity,arrayList);
    }
    private void ShowDataIfAvailable()
    {
        if (!UserArrayList.get(2).matches("null"))
        {
            Picasso.get().load(UserArrayList.get(1)).into(ImageCiv);
        }
        NameTv.setText(UserArrayList.get(3)+UserArrayList.get(4));
        StudiedTv.setText(UserArrayList.get(5));
        DegreeCompletedTv.setText(UserArrayList.get(6));
        SpecialityTv.setText(UserArrayList.get(7));
        NoOfYearPracTv.setText(UserArrayList.get(8));
        AvailableAreaTv.setText(UserArrayList.get(9));
    }

    private void ShowAppointmentList()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child(DBConst.AppointmentShedule).child(UID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                arrayList.clear();
                if (dataSnapshot.exists())
                {
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        String HospitalName=dataSnapshot1.getKey();
                        String AvaialableDay=dataSnapshot1.child(DBConst.AvailableDay).getValue().toString();
                        String AppointmentTime=dataSnapshot1.child(DBConst.AppointmentTime).getValue().toString();
                        String AppointmentFee=dataSnapshot1.child(DBConst.AppointmentFee).getValue().toString();
                        String UnavaiableSDate=dataSnapshot1.child(DBConst.UnavaiableSDate).getValue().toString();
                        String UnavaiableEDate=dataSnapshot1.child(DBConst.UnavaiableEDate).getValue().toString();
                        arrayList.add(new AppointmentListModel(AuthorityValidity,UID,NameTv.getText().toString(),HospitalName,AvaialableDay,AppointmentTime,AppointmentFee,UnavaiableSDate,UnavaiableEDate));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

package com.tarikulislamjoni95.doctorsappointment;

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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfileVisitActivity extends AppCompatActivity
{
    private AppointmentListAdapter adapter;
    private ArrayList<AppointmentListModel> arrayList;

    private Activity activity;
    private Intent intent;

    String UID;

    private AlertDialog dialog;

    private TextView NameTv,BMDCRegTv,StudiedTv,NoOfYearPracTv,SpecialityTv;
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
    }

    private void Initialization()
    {
        UID=getIntent().getStringExtra(DBConst.UID);
        activity= DoctorProfileVisitActivity.this;
        arrayList=new ArrayList<>();

    }
    private void InitializationUI()
    {
        NameTv=findViewById(R.id.name_tv);
        BMDCRegTv=findViewById(R.id.bmdc_reg_tv);
        StudiedTv=findViewById(R.id.studied_tv);
        NoOfYearPracTv=findViewById(R.id.no_of_prac_tv);
        SpecialityTv=findViewById(R.id.speciality_tv);
        ImageCiv=findViewById(R.id.image_civ);
        AppointmentListView=findViewById(R.id.list_view_1);
    }
    private void InitializationClass()
    {
        adapter=new AppointmentListAdapter(activity,arrayList);
    }
    private void ShowDataIfAvailable()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Doctor).child(UID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    NameTv.setText(dataSnapshot.child(DBConst.Name).getValue().toString());
                    BMDCRegTv.setText(dataSnapshot.child(DBConst.BMDCRegNo).getValue().toString());
                    StudiedTv.setText(dataSnapshot.child(DBConst.StudiedCollege).getValue().toString());
                    SpecialityTv.setText(dataSnapshot.child(DBConst.Category).getValue().toString());
                    if (!dataSnapshot.child(DBConst.Image).getValue().toString().matches("null"))
                    {
                        Picasso.get().load(dataSnapshot.child(DBConst.Image).getValue().toString()).into(ImageCiv);
                    }
                    ShowAppointmentList();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
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
                        arrayList.add(new AppointmentListModel(UID,NameTv.getText().toString(),HospitalName,AvaialableDay,AppointmentTime,AppointmentFee,UnavaiableSDate,UnavaiableEDate));
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

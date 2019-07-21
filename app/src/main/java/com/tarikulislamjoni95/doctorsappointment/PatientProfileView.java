package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientProfileView extends AppCompatActivity implements View.OnClickListener
{

    private Activity activity;
    private Intent intent;

    private TextView PatientNameTv,PatientFatherNameTv,PatientMotherNameTv,PatientGenderTv,PatientDatOfBirthTv,PatientAddressTv,PatientBloodGroupTv;
    private TextView PatientBirthNoTv,PatientContactNo;
    private Button PatientProfileEditBtn,PatientPastAppointmentBtn,PatientCurrentAppointment,PatientStoredHistoryBtn;
    CircleImageView PatientImageCIV;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_profile_view);
        Initialization();
        InitializationUI();
        InitializationClass();
        LoadData();
    }
    private void Initialization()
    {
        activity=PatientProfileView.this;
    }
    private void InitializationClass()
    {

    }
    private void InitializationUI()
    {
        PatientImageCIV=findViewById(R.id.patient_image_civ);

        PatientNameTv=findViewById(R.id.patient_name_tv);
        PatientFatherNameTv=findViewById(R.id.patient_father_name_tv);
        PatientMotherNameTv=findViewById(R.id.patient_mother_name_tv);
        PatientGenderTv=findViewById(R.id.patient_gender_tv);
        PatientDatOfBirthTv=findViewById(R.id.patient_dateofbirth_tv);
        PatientAddressTv=findViewById(R.id.patient_address_tv);
        PatientBloodGroupTv=findViewById(R.id.patient_bloodgroup_tv);
        PatientContactNo=findViewById(R.id.patient_contact_no_tv);
        PatientBirthNoTv=findViewById(R.id.patient_birth_no_tv);

        PatientProfileEditBtn=findViewById(R.id.patient_profile_edit_btn);
        PatientProfileEditBtn.setOnClickListener(this);
        PatientPastAppointmentBtn=findViewById(R.id.patient_past_appointment_btn);
        PatientPastAppointmentBtn.setOnClickListener(this);
        PatientCurrentAppointment=findViewById(R.id.patient_current_appointment_btn);
        PatientCurrentAppointment.setOnClickListener(this);
        PatientStoredHistoryBtn=findViewById(R.id.patient_stored_history_btn);
        PatientStoredHistoryBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.patient_profile_edit_btn:
                PatientProfileEditMethod();
                break;
            case R.id.patient_current_appointment_btn:
                PatientCurrentAppointMethod();
                break;
            case R.id.patient_past_appointment_btn:
                PatientPastAppointMethod();
                break;
        }
    }
    private void PatientProfileEditMethod()
    {
        intent=new Intent(activity,PatientProfileOneActivity.class);
        startActivity(intent);
    }
    private void PatientCurrentAppointMethod() {
    }
    private void PatientPastAppointMethod() {
    }
    private void LoadData()
    {
       final MyLoadingDailog myLoadingDailog= new MyLoadingDailog(activity,R.drawable.spinner);
       myLoadingDailog.show();
        String UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Patient).child(UID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (myLoadingDailog.isShowing())
                {
                    myLoadingDailog.dismiss();
                }
                if (dataSnapshot.exists())
                {
                    String image_url=dataSnapshot.child(DBConst.Image).getValue().toString();
                    if (!image_url.matches("null"))
                    {
                        Picasso.get().load(image_url).into(PatientImageCIV);
                    }
                    PatientNameTv.setText(dataSnapshot.child(DBConst.Name).getValue().toString());
                    PatientFatherNameTv.setText(dataSnapshot.child(DBConst.FatherName).getValue().toString());
                    PatientMotherNameTv.setText(dataSnapshot.child(DBConst.MotherName).getValue().toString());
                    PatientAddressTv.setText(dataSnapshot.child(DBConst.Address).getValue().toString());
                    PatientBloodGroupTv.setText(dataSnapshot.child(DBConst.BloodGroup).getValue().toString());
                    PatientDatOfBirthTv.setText(dataSnapshot.child(DBConst.BirthDate).getValue().toString());
                    PatientContactNo.setText(dataSnapshot.child(DBConst.ContactNo).getValue().toString());
                    PatientBirthNoTv.setText(dataSnapshot.child(DBConst.BirthCertificateNo).getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

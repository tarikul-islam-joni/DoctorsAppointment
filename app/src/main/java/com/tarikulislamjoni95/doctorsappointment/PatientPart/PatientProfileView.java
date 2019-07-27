package com.tarikulislamjoni95.doctorsappointment.PatientPart;

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
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientProfileView extends AppCompatActivity implements View.OnClickListener
{
    private ArrayList<String> UserInformation;

    private Activity activity;
    private Intent intent;

    private TextView PatientNameTv,PatientFatherNameTv,PatientMotherNameTv,PatientGenderTv,PatientDatOfBirthTv,PatientAddressTv,PatientBloodGroupTv;
    private TextView PatientBirthNoTv,PatientContactNoTv;
    private Button PatientProfileEditBtn,PatientAppointmentHistory,PatientStoredHistoryBtn;
    CircleImageView PatientImageCIV;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_view);
        Initialization();
        InitializationUI();
        InitializationClass();
        LoadData();
    }
    private void Initialization()
    {
        activity=PatientProfileView.this;
        UserInformation=getIntent().getStringArrayListExtra(DBConst.Patient);
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
        PatientContactNoTv=findViewById(R.id.patient_contact_no_tv);
        PatientBirthNoTv=findViewById(R.id.patient_birth_no_tv);

        PatientProfileEditBtn=findViewById(R.id.patient_profile_edit_btn);
        PatientProfileEditBtn.setOnClickListener(this);
        PatientAppointmentHistory=findViewById(R.id.patient_appointment_history_btn);
        PatientAppointmentHistory.setOnClickListener(this);
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
            case R.id.patient_appointment_history_btn:
                PatientAppointmentHistoryMethod();
                break;
        }
    }
    private void PatientProfileEditMethod()
    {
        intent=new Intent(activity, EditPatientProfileActivity.class);
        startActivity(intent);
    }
    private void PatientAppointmentHistoryMethod()
    {
        intent=new Intent(activity, PatientAppointmentHistoryActivity.class);
        startActivity(intent);
    }
    private void LoadData()
    {
        if (!UserInformation.get(0).matches("null"))
        {
            Picasso.get().load(UserInformation.get(0)).into(PatientImageCIV);
        }
        else
        {
            if (UserInformation.get(6).matches("Male"))
            {
                PatientImageCIV.setImageResource(R.drawable.male_doc);
            }
            else
            {
                PatientImageCIV.setImageResource(R.drawable.femal_doc);
            }
        }
        PatientNameTv.setText(UserInformation.get(1));
        PatientFatherNameTv.setText(UserInformation.get(2));
        PatientMotherNameTv.setText(UserInformation.get(3));
        PatientContactNoTv.setText(UserInformation.get(4));
        PatientAddressTv.setText(UserInformation.get(5));
        PatientGenderTv.setText(UserInformation.get(6));
        PatientBloodGroupTv.setText(UserInformation.get(7));
        PatientDatOfBirthTv.setText(UserInformation.get(8));
        PatientBirthNoTv.setText(UserInformation.get(9));
    }
}

package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDM;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.StorageDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountStatusDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

public class EditPatientSecureInfoActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_secure_info);
    }
}

package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

public class PatientListActivity extends AppCompatActivity
{
    private String UID;
    private String AppointmentDate;
    private String Name;
    private String HospitalName;
    private String AppointmentTime;

    PatientListAdapter adapter;
    ArrayList<DoctorDataModel> arrayList;
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_appointment_list);
        listView=findViewById(R.id.list_view);
        arrayList=new ArrayList<>();
        adapter=new PatientListAdapter(PatientListActivity.this,arrayList);
        listView.setAdapter(adapter);
        ShowData();
    }

    private void ShowData()
    {

    }
}

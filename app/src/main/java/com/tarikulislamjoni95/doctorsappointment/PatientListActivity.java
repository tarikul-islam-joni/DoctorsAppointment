package com.tarikulislamjoni95.doctorsappointment;

import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;

public class PatientListActivity extends AppCompatActivity
{
    private String UID;
    private String AppointmentDate;
    private String Name;
    private String HospitalName;
    private String AppointmentTime;

    PatientListAdapter adapter;
    ArrayList<DataModel> arrayList;
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        listView=findViewById(R.id.list_view);
        arrayList=new ArrayList<>();
        adapter=new PatientListAdapter(PatientListActivity.this,arrayList);
        listView.setAdapter(adapter);
        ShowData();
    }

    private void ShowData()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.PatientList).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        AppointmentDate=dataSnapshot1.getKey();
                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                        {
                            HospitalName=dataSnapshot2.getKey();
                            for(DataSnapshot dataSnapshot3:dataSnapshot2.getChildren())
                            {
                                UID=dataSnapshot3.getKey();
                                Name=dataSnapshot3.child(DBConst.Name).getValue().toString();
                                HospitalName=dataSnapshot3.child(DBConst.HospitalName).getValue().toString();
                                AppointmentTime=dataSnapshot3.child(DBConst.AppointmentTime).getValue().toString();
                                arrayList.add(new DataModel(Name,HospitalName,AppointmentDate,AppointmentTime));
                            }
                        }
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

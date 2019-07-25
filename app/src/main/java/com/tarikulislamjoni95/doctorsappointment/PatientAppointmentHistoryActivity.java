package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

public class PatientAppointmentHistoryActivity extends AppCompatActivity
{
    private Activity activity;
    PatientAppointmentHistoryAdapter adapter1,adapter2;
    ArrayList<DataModel> arrayList1,arrayList2;
    ListView listView1,listView2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_appointment_history_activity);
        activity=PatientAppointmentHistoryActivity.this;
        listView1=findViewById(R.id.list_view_1);
        listView2=findViewById(R.id.list_view_2);
        arrayList1=new ArrayList<>();
        arrayList2=new ArrayList<>();
        adapter1=new PatientAppointmentHistoryAdapter(activity,arrayList1);
        adapter2=new PatientAppointmentHistoryAdapter(activity,arrayList2);


        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);

        GetDataForListView1();
        GetDataForListView2();
    }

    private void GetDataForListView1()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(DBConst.TemporaryAppointment).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                arrayList1.clear();
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        String UID=dataSnapshot1.getKey();
                        String Name=dataSnapshot1.child(DBConst.Name).getValue().toString();
                        String HospitalName=dataSnapshot1.child(DBConst.HospitalName).getValue().toString();
                        String AppointmentDate=dataSnapshot1.child(DBConst.AppointmentDate).getValue().toString();
                        String AppointmentFee=dataSnapshot1.child(DBConst.AppointmentFee).getValue().toString();
                        String AppointmentTime=dataSnapshot1.child(DBConst.AppointmentTime).getValue().toString();
                        long AppointmentValidityTime=(long)dataSnapshot1.child(DBConst.AppointmentValidityTime).getValue();
                        arrayList1.add(new DataModel("1",UID,Name,HospitalName,AppointmentDate,AppointmentTime,AppointmentFee,String.valueOf(AppointmentValidityTime)));
                    }
                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }
    private void GetDataForListView2()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.ConfirmAppointment).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                arrayList2.clear();
                if (dataSnapshot.exists())
                {
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                        {
                            String UID=dataSnapshot2.getKey();
                            String Name=dataSnapshot2.child(DBConst.Name).getValue().toString();
                            String HospitalName=dataSnapshot2.child(DBConst.HospitalName).getValue().toString();
                            Log.d("myError","Hospital Naem : "+HospitalName);
                            String AppointmentValidityTime=dataSnapshot2.child(DBConst.AppointmentCreateDate).getValue().toString();
                            String AppointmentFee=dataSnapshot2.child(DBConst.AppointmentFee).getValue().toString();
                            String AppointmentTime=dataSnapshot2.child(DBConst.AppointmentTime).getValue().toString();
                            String AppointmentDate=dataSnapshot2.child(DBConst.AppointmentValidityTime).getValue().toString();
                            arrayList2.add(new DataModel("2",UID,Name,HospitalName,AppointmentValidityTime,AppointmentTime,AppointmentFee,AppointmentDate));
                        }
                    }
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

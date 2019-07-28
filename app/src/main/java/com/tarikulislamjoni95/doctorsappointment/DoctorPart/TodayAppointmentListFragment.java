package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

public class TodayAppointmentListFragment extends Fragment
{
    private String AppointmentDate,AppointmentTime,HospitalName,UID,Name;
    private ArrayList<DoctorDataModel> arrayList;
    private PatientListAdapter adapter;
    private ListView listView;
    private Activity activity;
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_appointment_list,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Initialization();
        GetDataFromFirebase();
    }
    private void Initialization()
    {
        activity=getActivity();
        listView=view.findViewById(R.id.list_view);
        arrayList=new ArrayList<>();
        adapter=new PatientListAdapter(activity,arrayList);
        listView.setAdapter(adapter);
    }
    private void GetDataFromFirebase()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.PatientList).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                arrayList.clear();
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
                                arrayList.add(new DoctorDataModel(Name,HospitalName,AppointmentDate,AppointmentTime));
                            }
                        }
                    }
                }
                else
                {

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

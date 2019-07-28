package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

public class MyTemporaryAppointmentFragment extends Fragment
{
    private MyToastClass myToastClass;
    private ArrayList<PatientDataModel> arrayList;
    private MyAppointmentListAdapter adapter;

    private String UID;
    private Activity activity;
    private View view;
    private ListView listView;
    private TextView StatusTv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_my_appointment_list,container,false);
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
        UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        activity=getActivity();
        listView=view.findViewById(R.id.list_view);
        StatusTv=view.findViewById(R.id.no_appointment_status_tv);
        arrayList=new ArrayList<>();
        adapter=new MyAppointmentListAdapter(activity,arrayList);
        listView.setAdapter(adapter);
        myToastClass=new MyToastClass(activity);
    }
    private void GetDataFromFirebase()
    {
        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
        ref.child(DBConst.CurrentTime).setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isComplete())
                {
                    ref.child(DBConst.CurrentTime).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            CheckTemporaryValidity((long)dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {
                            myToastClass.LToast("Something error occurred");
                        }
                    });
                }
            }
        });
    }

    private void CheckTemporaryValidity(final long CurrentTime)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(DBConst.TemporaryAppointment).child(UID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                arrayList.clear();
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        String Doctor_UID=dataSnapshot1.getKey();
                        String Name=dataSnapshot1.child(DBConst.Name).getValue().toString();
                        String HospitalName=dataSnapshot1.child(DBConst.HospitalName).getValue().toString();
                        String AppointmentDate=dataSnapshot1.child(DBConst.AppointmentDate).getValue().toString();
                        String AppointmentTime=dataSnapshot1.child(DBConst.AppointmentTime).getValue().toString();
                        String AppointmentFee=dataSnapshot1.child(DBConst.AppointmentFee).getValue().toString();
                        long AppointmentValidity=(long)dataSnapshot1.child(DBConst.AppointmentValidityTime).getValue();
                        AppointmentValidity=((AppointmentValidity)+(6*60*60*1000));
                        if (CurrentTime>AppointmentValidity)
                        {
                            //Time End
                            DeleteTemporaryAppointment(UID,Doctor_UID);
                        }
                        else
                        {
                            AppointmentValidity=(AppointmentValidity-CurrentTime)/(60*60*1000);
                            arrayList.add(new PatientDataModel("0",Doctor_UID,Name,HospitalName,AppointmentDate,AppointmentTime,String.valueOf(AppointmentValidity),AppointmentFee));
                        }
                    }

                    if (arrayList.size()==0)
                    {
                        listView.setVisibility(View.GONE);
                        StatusTv.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        listView.setVisibility(View.VISIBLE);
                        StatusTv.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    listView.setVisibility(View.GONE);
                    StatusTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                listView.setVisibility(View.GONE);
                StatusTv.setVisibility(View.VISIBLE);
            }
        });
    }

    private void DeleteTemporaryAppointment(String uid, String doctor_uid)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.TemporaryAppointment).child(uid);
        reference.child(doctor_uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isComplete())
                {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}

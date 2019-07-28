package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.Calendar;

public class MyCurrentAppointmentFragment extends Fragment
{
    int[] ValidityDate=new int[3];
    private MyToastClass myToastClass;
    private MyAppointmentListAdapter adapter;
    private ArrayList<PatientDataModel> arrayList;

    private int CurrentDate,CurrentMonth,CurrentYear;
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
        arrayList=new ArrayList<>();
        StatusTv=view.findViewById(R.id.no_appointment_status_tv);
        adapter=new MyAppointmentListAdapter(activity,arrayList);
        listView.setAdapter(adapter);
        myToastClass=new MyToastClass(activity);
    }

    private void GetDataFromFirebase()
    {
        Calendar calendar=Calendar.getInstance();
        CurrentDate=calendar.get(Calendar.DAY_OF_MONTH);
        CurrentMonth=calendar.get(Calendar.MONTH)+1;
        CurrentYear=calendar.get(Calendar.YEAR);
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(DBConst.ConfirmAppointment).child(UID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                arrayList.clear();
                if (dataSnapshot.exists())
                {
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        String Doctor_UID=dataSnapshot1.getKey();
                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                        {
                            String Name=dataSnapshot2.child(DBConst.Name).getValue().toString();
                            String HospitalName=dataSnapshot2.child(DBConst.HospitalName).getValue().toString();
                            String AppointmentDate=dataSnapshot2.child(DBConst.AppointmentValidityTime).getValue().toString();
                            String AppointmentTime=dataSnapshot2.child(DBConst.AppointmentTime).getValue().toString();
                            String AppointmentCreateDate=dataSnapshot2.child(DBConst.AppointmentCreateDate).getValue().toString();
                            String AppointmentFee=dataSnapshot2.child(DBConst.AppointmentFee).getValue().toString();


                            String[] AppointmentValidity=AppointmentDate.split("-",0);
                            for(int i=0; i<AppointmentValidity.length; i++)
                            {
                                int kkk=(int)Integer.parseInt(String.valueOf(AppointmentValidity[i]));
                                ValidityDate[i]=kkk;
                            }

                            if (ValidityDate[2]>CurrentYear || ValidityDate[1]>CurrentMonth || ValidityDate[0]>CurrentDate )
                            {
                                arrayList.add(new PatientDataModel("1",Doctor_UID,Name,HospitalName,AppointmentDate,AppointmentTime,AppointmentCreateDate,AppointmentFee));
                            }
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
                    }
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    listView.setVisibility(View.GONE);
                    StatusTv.setVisibility(View.VISIBLE);
                    myToastClass.LToast("You have no appointment");
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
}

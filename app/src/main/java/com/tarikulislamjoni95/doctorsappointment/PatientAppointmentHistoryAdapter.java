package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class PatientAppointmentHistoryAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener
{
    private DataModel dataModel;
    private Activity activity;
    private ArrayList<DataModel> arrayList;
    public PatientAppointmentHistoryAdapter(Activity activity, ArrayList<DataModel> arrayList)
    {
        super(activity, R.layout.patient_appointment_history_model,arrayList);
        this.activity=activity;
        this.arrayList=arrayList;
    }

    @Override
    public void onClick(View view)
    {
        Calendar calendar=Calendar.getInstance();
        int date=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);
        String CurrentDate=date+"/"+month+"/"+year;
        int position=(Integer) view.getTag();
        dataModel=getItem(position);
        switch (view.getId())
        {
            case R.id.pay_btn:
                SaveConfirmAppointment(CurrentDate);
                break;
        }
    }

    private void SaveConfirmAppointment(String CurrentDate)
    {
        HashMap<String,String> hashMap=new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.ConfirmAppointment).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(dataModel.getUID()).push();
        hashMap.put(DBConst.Name,dataModel.getName());
        hashMap.put(DBConst.AppointmentCreateDate,CurrentDate);
        hashMap.put(DBConst.AppointmentFee,dataModel.getAppointmentFee());
        hashMap.put(DBConst.AppointmentTime,dataModel.getAppointmentTime());
        hashMap.put(DBConst.HospitalName,dataModel.getHospitalName());
        hashMap.put(DBConst.AppointmentValidityTime,dataModel.getAppointmentDate());
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            SaveIntoDoctorDB();
                        }
                        else
                        {
                            Toast.makeText(activity,"Appointment confirmation result unsuccessfull",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void SaveIntoDoctorDB()
    {
        HashMap<String,String> hashMap=new HashMap<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child(DBConst.PatientList).child(dataModel.getUID()).child(dataModel.getAppointmentDate()).child(dataModel.getHospitalName()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put(DBConst.Name,dataModel.getName());
        hashMap.put(DBConst.HospitalName,dataModel.getHospitalName());
        hashMap.put(DBConst.AppointmentTime,dataModel.getAppointmentTime());
        ref.setValue(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            RemoveFromTemporary();
                            Log.d("myError","Successfull");
                        }
                        else
                        {
                            Log.d("myError","UnSuccessfull");
                        }
                    }
                });

    }

    private void RemoveFromTemporary()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.TemporaryAppointment).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.child(dataModel.getUID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Log.d("myError","Remove successfull");
                }
                else
                {
                    Log.d("myError","Remove unsuccessfull");
                }
            }
        });
    }

    class ViewHolder
    {
        TextView NameTv,AppointmentDateTv,AppointmentTimeTv,AppointmentFeeTv,HospitaNameTv,AppointmentValidityTv;
        Button PayBtn;
    }
    ViewHolder viewHolder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        dataModel=arrayList.get(position);
        viewHolder=new ViewHolder();
        LayoutInflater inflater=activity.getLayoutInflater();
        convertView=inflater.inflate(R.layout.patient_appointment_history_model,parent,false);
        viewHolder.NameTv=convertView.findViewById(R.id.name_tv);
        viewHolder.HospitaNameTv=convertView.findViewById(R.id.hospital_name_tv);
        viewHolder.AppointmentDateTv=convertView.findViewById(R.id.appointment_date_tv);
        viewHolder.AppointmentTimeTv=convertView.findViewById(R.id.appointment_time_tv);
        viewHolder.AppointmentFeeTv=convertView.findViewById(R.id.appointment_fee_tv);
        viewHolder.AppointmentValidityTv=convertView.findViewById(R.id.appointment_remaining_status);
        viewHolder.PayBtn=convertView.findViewById(R.id.pay_btn);
        viewHolder.PayBtn.setOnClickListener(this);

        if (dataModel.getFrom().matches("1"))
        {
            viewHolder.PayBtn.setTag(position);
            viewHolder.AppointmentDateTv.setText("Appointment Date : "+dataModel.getAppointmentDate());
            viewHolder.AppointmentValidityTv.setText("Remaining : "+dataModel.getAppointmentValidityTime());
        }
        else
        {
            viewHolder.AppointmentValidityTv.setText("Appointment Date : "+dataModel.getAppointmentValidityTime());
            viewHolder.AppointmentDateTv.setText("Appointment Created Date : "+dataModel.getAppointmentDate());
            viewHolder.PayBtn.setBackgroundColor(activity.getResources().getColor(R.color.colorGreen));
            viewHolder.PayBtn.setText("Confirmed");
            viewHolder.PayBtn.setEnabled(false);
        }

        viewHolder.NameTv.setText("Doctor Name : "+dataModel.getName());
        viewHolder.HospitaNameTv.setText("Hospital Name : "+dataModel.getHospitalName());
        viewHolder.AppointmentTimeTv.setText("Appointment Time : "+dataModel.getAppointmentTime());
        viewHolder.AppointmentFeeTv.setText("Fee : "+dataModel.getAppointmentFee());


        return convertView;
    }
}

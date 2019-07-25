package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AppointmentListAdapter extends ArrayAdapter<AppointmentListModel> implements View.OnClickListener
{
    private String FromWhich;
    private AlertDialog dialog;
    private DatePickerDialog datePickerDialog;
    private Activity activity;
    private ArrayList<AppointmentListModel> arrayList;
    private AppointmentListModel dataModel;
    public AppointmentListAdapter(Activity activity, ArrayList<AppointmentListModel> arrayList)
    {
        super(activity,R.layout.appointment_list_model, arrayList);
        this.activity=activity;
        this.arrayList=arrayList;
        this.FromWhich=FromWhich;
    }

    class ViewHolder
    {
        TextView HospitalNameTv;
        TextView AvailableDayTv;
        TextView AppointmentTimeTv;
        TextView AppointmenetOffTv;
        TextView AppointmentFeeTv;
        Button TakeAppointmentBtn;
    }


    ViewHolder viewHolder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder=new ViewHolder();
        dataModel=arrayList.get(position);
        LayoutInflater inflater=activity.getLayoutInflater();
        convertView=inflater.inflate(R.layout.appointment_list_model,parent,false);

        viewHolder.HospitalNameTv=convertView.findViewById(R.id.hospital_name_tv);
        viewHolder.AvailableDayTv=convertView.findViewById(R.id.available_day_tv);
        viewHolder.AppointmentTimeTv=convertView.findViewById(R.id.appointment_time_tv);
        viewHolder.AppointmenetOffTv=convertView.findViewById(R.id.appointment_off_tv);
        viewHolder.AppointmentFeeTv=convertView.findViewById(R.id.appointment_fee_tv);
        viewHolder.TakeAppointmentBtn=convertView.findViewById(R.id.take_appointment_btn);
        viewHolder.TakeAppointmentBtn.setOnClickListener(this);
        viewHolder.TakeAppointmentBtn.setTag(position);
        viewHolder.HospitalNameTv.setText(dataModel.getHospitalName());
        viewHolder.AvailableDayTv.setText(dataModel.getAvailableDay());
        viewHolder.AppointmentTimeTv.setText(dataModel.getAppointmentTime());
        if (dataModel.getAppointmentOffStart().matches(VARConst.Unavailable_Starting_Date) || dataModel.getAppointmentOffEnd().matches(VARConst.Unavailable_Ending_Date))
        {
            viewHolder.AppointmenetOffTv.setVisibility(View.GONE);
        }
        else
        {
            String Start=dataModel.getAppointmentOffStart();
            String End=dataModel.getAppointmentOffEnd();
            viewHolder.AppointmenetOffTv.setVisibility(View.VISIBLE);
            viewHolder.AppointmenetOffTv.setText(Start+"~"+End);
        }
        viewHolder.AppointmentFeeTv.setText("Appointment Fee : "+dataModel.getAppointmentFee());

        return convertView;
    }

    @Override
    public void onClick(View view)
    {
        dataModel=getItem((int)((Integer)view.getTag()));
        switch (view.getId())
        {
            case R.id.take_appointment_btn:
                TakeAppointmentMethod();
                break;
        }
    }

    private void TakeAppointmentMethod()
    {
        Calendar calendar=Calendar.getInstance();
        int date=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);

        DatePickerDialog.OnDateSetListener DatePickerListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date)
            {
                SimpleDateFormat sdf=new SimpleDateFormat("EEEE");
                Date date1=new Date(year,month,date-1);
                String Day=sdf.format(date1);
                ShowPossibility(Day,date,month+1,year);
            }
        };

        datePickerDialog=new DatePickerDialog(activity,DatePickerListener,date,month,year);

        calendar.add(calendar.DATE,+1);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.DATE,+6);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();


    }

    private void ShowPossibility(String Day,int date,int month,int year) {
        String AvailableDayString=dataModel.getAvailableDay();
        ArrayList<String> AvailableDay=new ArrayList<>();
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0; i<AvailableDayString.length(); i++)
        {
            if (AvailableDayString.charAt(i)==',')
            {
                AvailableDay.add(stringBuilder.toString());
                stringBuilder=new StringBuilder();
            }
            else
            {
                stringBuilder.append(AvailableDayString.charAt(i));
            }
        }
        AvailableDay.add(stringBuilder.toString());
        boolean isAvailableDay=false;
        for(int i=0; i<AvailableDay.size(); i++)
        {
            Log.d("myError","Current Day : "+Day+" Available Day"+AvailableDay.get(i));
            if (AvailableDay.get(i).matches(Day))
            {
                Log.d("myError","Current Day matched : "+Day+" Available Day"+AvailableDay.get(i));
                isAvailableDay=true;
            }
        }
        String Start = dataModel.getAppointmentOffStart();
        String End = dataModel.getAppointmentOffEnd();
        ArrayList<Integer> Starting_date = new ArrayList<>();
        ArrayList<Integer> Ending_date = new ArrayList<>();
        int temp = 0;
        for (int i = 0; i < Start.length(); i++) {
            if (Start.charAt(i) == '/') {
                Starting_date.add(temp);
                temp = 0;
            } else {
                char abcd = Start.charAt(i);
                int aaa = Character.getNumericValue(abcd);
                temp = temp * 10 + aaa;
            }
        }
        Starting_date.add(temp);
        temp = 0;
        for (int i = 0; i < End.length(); i++) {
            if (End.charAt(i) == '/') {
                Ending_date.add(temp);
                temp = 0;
            } else {
                char abcd = End.charAt(i);
                int aaa = Character.getNumericValue(abcd);
                temp = temp * 10 + aaa;
            }
        }
        Ending_date.add(temp);
        if (isAvailableDay)
        {
            if (year>Starting_date.get(2)&&year<Ending_date.get(2))
            {
                ShowConfirmDialog(false,date,month,year);
            }
            else if(month>Starting_date.get(1)&&month<Ending_date.get(1))
            {
                ShowConfirmDialog(false,date,month,year);
            }
            else if (month<Starting_date.get(1)&&month<Ending_date.get(1))
            {
                ShowConfirmDialog(true,date,month,year);
            }
            else if (month>Starting_date.get(1)&&month>Ending_date.get(1))
            {
                ShowConfirmDialog(true,date,month,year);
            }
            else if (month==Starting_date.get(1) && date<Starting_date.get(0))
            {
                ShowConfirmDialog(true,date,month,year);
            }
            else if (month==Ending_date.get(1) && date>Ending_date.get(0))
            {
                ShowConfirmDialog(true,date,month,year);
            }
            else
            {
                ShowConfirmDialog(false,date,month,year);
            }
        }
        else
        {
            ShowConfirmDialog(false,0,0,0);
        }
    }
    private void ShowConfirmDialog(boolean Check,final int date, final int month, final int year)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        if (Check)
        {
            builder.setTitle("Confirm Appointment");
            builder.setMessage("Confirm your appointment in "+date+"/"+month+"/"+year+" at "+dataModel.getAppointmentTime());
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    TemporarySavingYourAppointment(date,month,year);
                }
            });
        }
        else
        {
            builder.setTitle("Doctor Unavailable !");
            if (date==0&&month==0&&year==0)
            {
                builder.setMessage("Doctor is available day only "+dataModel.getAvailableDay());
            }
            else
            {
                builder.setMessage("Doctor is in leave "+dataModel.getAppointmentOffStart()+" to "+dataModel.getAppointmentOffEnd());
            }
            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();
                }
            });
        }

        dialog=builder.create();
        builder.show();
    }

    private void TemporarySavingYourAppointment(int day,int month,int year)
    {
        HashMap<String,Object> hashMap=new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.TemporaryAppointment).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(dataModel.getUID());
        hashMap.put(DBConst.Name,dataModel.getName());
        hashMap.put(DBConst.AppointmentDate,day+"-"+month+"-"+year);
        hashMap.put(DBConst.AppointmentTime,dataModel.getAppointmentTime());
        hashMap.put(DBConst.AppointmentFee,dataModel.getAppointmentFee());
        hashMap.put(DBConst.HospitalName,dataModel.getHospitalName());
        hashMap.put(DBConst.AppointmentValidityTime,ServerValue.TIMESTAMP);
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                dialog.dismiss();
                if (task.isSuccessful())
                {
                    Intent intent=new Intent(activity,PatientProfileView.class);
                    activity.startActivity(intent);
                }
                else
                {
                    Toast.makeText(activity,"Appointment Unsuccessfull",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

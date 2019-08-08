package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import androidx.core.content.ContextCompat;

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
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TakeAppointmentAdapter extends ArrayAdapter<AppointmentListModel> implements View.OnClickListener
{
    private boolean AuthorityValidity;
    private int[] Starting_Date=new int[3];
    private int[] Ending_Date=new int[3];
    private String[] AvailableDay;

    private AlertDialog dialog;
    private DatePickerDialog datePickerDialog;
    private Activity activity;
    private ArrayList<AppointmentListModel> arrayList;
    private AppointmentListModel dataModel;
    public TakeAppointmentAdapter(Activity activity, ArrayList<AppointmentListModel> arrayList)
    {
        super(activity, R.layout.model_take_appointment, arrayList);
        this.activity=activity;
        this.arrayList=arrayList;
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
        convertView=inflater.inflate(R.layout.model_take_appointment,parent,false);

        viewHolder.HospitalNameTv=convertView.findViewById(R.id.hospital_name_tv);
        viewHolder.AvailableDayTv=convertView.findViewById(R.id.available_day_tv);
        viewHolder.AppointmentTimeTv=convertView.findViewById(R.id.appointment_time_tv);
        viewHolder.AppointmenetOffTv=convertView.findViewById(R.id.appointment_off_tv);
        viewHolder.AppointmentFeeTv=convertView.findViewById(R.id.appointment_fee_tv);
        viewHolder.TakeAppointmentBtn=convertView.findViewById(R.id.take_appointment_btn);
        viewHolder.TakeAppointmentBtn.setOnClickListener(this);
        viewHolder.TakeAppointmentBtn.setTag(position);
        if (dataModel.getAuthorityValidity().matches("true"))
        {
            viewHolder.TakeAppointmentBtn.setVisibility(View.VISIBLE);
            TextView status=activity.findViewById(R.id.appointment_availability_status_tv);
            status.setText("Authorized");
        }
        else
        {
            viewHolder.TakeAppointmentBtn.setVisibility(View.GONE);
            TextView status=activity.findViewById(R.id.appointment_availability_status_tv);
            status.setText("UnAuthorized");
        }
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
        AvailableDay=AvailableDayString.split(",",0);
        boolean isAvailableDay=false;
        for(int i=0; i<AvailableDay.length; i++)
        {
            if (AvailableDay[i].matches(Day))
            {
                isAvailableDay=true;
            }
        }

        String Start = dataModel.getAppointmentOffStart();
        String End = dataModel.getAppointmentOffEnd();
        String[] Starting_date=Start.split("-",0);
        String[] Ending_date=End.split("-",0);
        for (int i=0; i<Starting_date.length; i++)
        {
            Starting_Date[i]=(int)Integer.parseInt(Starting_date[i]);
            Ending_Date[i]=(int)Integer.parseInt(Ending_date[i]);
        }

        if (isAvailableDay)
        {
            if (year>Starting_Date[2] && Ending_Date[2]<year)
            {
                ShowConfirmDialog(false,date,month,year);
            }
            else if(month>Starting_Date[1] && month<Ending_Date[1])
            {
                ShowConfirmDialog(false,date,month,year);
            }
            else if (month<Starting_Date[1]&&month<Ending_Date[1])
            {
                ShowConfirmDialog(true,date,month,year);
            }
            else if (month>Starting_Date[1]&&month>Ending_Date[1])
            {
                ShowConfirmDialog(true,date,month,year);
            }
            else if (month==Starting_Date[1] && date<Starting_Date[1])
            {
                ShowConfirmDialog(true,date,month,year);
            }
            else if (month==Ending_Date[1] && date>Ending_Date[0])
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
            builder.setMessage("Confirm your appointment in "+date+"-"+month+"-"+year+" at "+dataModel.getAppointmentTime());
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
                    Intent intent=new Intent(activity, PatientMainActivity.class);
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

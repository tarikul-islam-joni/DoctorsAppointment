package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DoctorProfileEditorTwoActivity  extends AppCompatActivity implements View.OnClickListener
{
    private AppointmentDataModel dataModel;
    private ArrayList<AppointmentDataModel> arrayList;
    private AppointmentDataAdapter adapter;
    private MyToastClass myToastClass;

    private boolean c=false;
    private String UID;

    private Activity activity;
    private Intent intent;
    private AlertDialog SelectDayDialog;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private String[] DayStringArray;
    private String CategoryString,AppointmentFeeString;

    private ListView listView;
    private LinearLayout StopAppointmentSection;
    private LinearLayout StartingTimeSection,EndingTimeSection;
    private LinearLayout StartingDateSection,EndingDateSection;
    private AutoCompleteTextView HospitalNameEt;
    private EditText AppointmentFeeEt;
    private TextView SelectDayTv,StartingTimeTv,EndingTimeTv,StartingDateTv,EndingDateTv;
    private Button SaveBtn,ConfirmBtn;
    private RadioButton StopAppointmentRbtn;
    private Button SelectDayBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_editor_two);
        Initialization();
        InitializationUI();
        InitializationClass();

        listView.setAdapter(adapter);
    }

    private void Initialization()
    {
        arrayList=new ArrayList<>();
        CategoryString= getIntent().getStringExtra(DBConst.Category);
        activity=DoctorProfileEditorTwoActivity.this;
        DayStringArray=getResources().getStringArray(R.array.select_day_arrya);
        UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    private void InitializationUI()
    {
        StopAppointmentSection=findViewById(R.id.stop_appointment_section);
        StopAppointmentSection.setVisibility(View.GONE);
        StopAppointmentSection.setEnabled(false);

        StartingTimeSection=findViewById(R.id.av_staring_time_section);
        EndingTimeSection=findViewById(R.id.av_ending_time_section);
        StartingTimeSection.setOnClickListener(this);
        EndingTimeSection.setOnClickListener(this);

        StartingDateSection=findViewById(R.id.starting_date_section);
        StartingDateSection.setOnClickListener(this);
        EndingDateSection=findViewById(R.id.ending_date_section);
        EndingDateSection.setOnClickListener(this);

        StartingTimeTv=findViewById(R.id.av_staring_time_tv);
        EndingTimeTv=findViewById(R.id.av_ending_time_tv);
        StartingDateTv=findViewById(R.id.starting_date_tv);
        EndingDateTv=findViewById(R.id.ending_date_tv);

        AppointmentFeeEt=findViewById(R.id.appointment_fee_et);
        HospitalNameEt=findViewById(R.id.hospital_name_et);
        SelectDayBtn=findViewById(R.id.select_day_btn);
        SelectDayBtn.setOnClickListener(this);
        SelectDayTv=findViewById(R.id.select_day_tv);
        ConfirmBtn=findViewById(R.id.confirm_btn);
        ConfirmBtn.setOnClickListener(this);
        ConfirmBtn.setEnabled(false);
        SaveBtn=findViewById(R.id.save_btn);
        SaveBtn.setOnClickListener(this);

        StopAppointmentRbtn=findViewById(R.id.stop_appointment_rbtn);

        listView=findViewById(R.id.list_view);


       StopAppointmentRbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               if (c)
               {
                   c=false;
                   StopAppointmentRbtn.setChecked(false);
                   StopAppointmentSection.setEnabled(false);
                   StopAppointmentSection.setVisibility(View.GONE);
               }
               else
               {
                   c=true;
                   StopAppointmentRbtn.setChecked(true);
                   StopAppointmentSection.setEnabled(true);
                   StopAppointmentSection.setVisibility(View.VISIBLE);
               }
           }
       });

    }
    private void InitializationClass()
    {
        myToastClass=new MyToastClass(activity);
        adapter=new AppointmentDataAdapter("Edit",activity,arrayList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ShowDataIfAvaiable();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.select_day_btn:
                SelectDayDailog();
                break;
            case R.id.save_btn:
                SaveMethod();
                break;
            case R.id.confirm_btn:
                ConfirmMethod();
                break;
            case R.id.av_staring_time_section:
                GetTimeMethod("Starting");
                break;
            case R.id.av_ending_time_section:
                GetTimeMethod("Ending");
                break;
            case R.id.starting_date_section:
                GetDateMethod("Starting");
                break;
            case R.id.ending_date_section:
                GetDateMethod("Ending");
                break;
        }
    }
    private void GetTimeMethod(final String Which)
    {
        Calendar calendar=Calendar.getInstance();
        final int hour=calendar.get(Calendar.HOUR);
        final int minute=calendar.get(Calendar.MINUTE);

        timePickerDialog=new TimePickerDialog(activity,android.R.style.Theme_Holo_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int setHour, int setMinute)
            {
                if (Which.matches("Starting"))
                {
                    StartingTimeTv.setText(setHour+":"+setMinute);
                }
                if (Which.matches("Ending"))
                {
                    EndingTimeTv.setText(setHour+":"+setMinute);
                }
            }
        },hour,minute,false);
        timePickerDialog.show();
    }
    private void GetDateMethod(final String Which)
    {
        int year,month,day;
        Calendar calendar=Calendar.getInstance();
        day=calendar.get(Calendar.DAY_OF_MONTH);
        month=calendar.get(Calendar.MONTH);
        year=calendar.get(Calendar.YEAR);
        datePickerDialog=new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthofyear, int dayofmonth) {
                if (Which.matches("Starting"))
                {
                    StartingDateTv.setText(dayofmonth+"/"+(monthofyear+1)+"/"+year);
                    EndingDateTv.setText(dayofmonth+"/"+(monthofyear+1)+"/"+year);
                }
                if (Which.matches("Ending"))
                {
                    EndingDateTv.setText(dayofmonth+"/"+(monthofyear+1)+"/"+year);
                }
            }
        },year,month,day);

        datePickerDialog.show();
    }
    private void SaveMethod()
    {
        if (HospitalNameEt.getText().toString().isEmpty())
        {
            HospitalNameEt.setError("Hospital name required");
        } else if (SelectDayTv.getText().toString().matches(VARConst.Select_Available_Day))
        {
            myToastClass.LToast("Select day");
        } else if(StartingTimeTv.getText().toString().matches(VARConst.Appointment_Starting_Time) || EndingTimeTv.getText().toString().matches(VARConst.Appointment_Ending_Time))
        {
            myToastClass.LToast("Select starting and ending time of appointment");
        } else if (AppointmentFeeEt.getText().toString().isEmpty())
        {
            AppointmentFeeEt.setError("Appointment Fee");
        }
        else
        {
            if (StopAppointmentSection.isEnabled())
            {
                if (StartingDateTv.getText().toString().matches(VARConst.Unavailable_Starting_Date) || EndingDateTv.getText().toString().matches(VARConst.Unavailable_Ending_Date))
                {
                    myToastClass.LToast("Input unavailable starting and ending date");
                }
                else
                {
                    SaveIntoDatabase();
                }
            }
            else
            {
                StartingDateTv.setText(VARConst.Unavailable_Starting_Date);
                EndingDateTv.setText(VARConst.Unavailable_Ending_Date);
                SaveIntoDatabase();
            }
        }

    }
    boolean[] checked;
    private void SelectDayDailog()
    {
        final StringBuilder stringBuilder=new StringBuilder();
        checked=new boolean[DayStringArray.length];
        for(int i=0; i<DayStringArray.length; i++)
        {
            checked[i]=false;
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("Select Available Day");
        builder.setMultiChoiceItems(DayStringArray, checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b)
            {
                if (stringBuilder.length()>=1)
                {
                    stringBuilder.append(",");
                }
                stringBuilder.append(DayStringArray[i]);
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SelectDayTv.setText(stringBuilder.toString());
                SelectDayDialog.dismiss();
            }
        });
        SelectDayDialog=builder.create();
        SelectDayDialog.show();
    }

    private void SaveIntoDatabase()
    {
        HashMap<String,String> hashMap=new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.AppointmentShedule).child(UID).child(HospitalNameEt.getText().toString());
        hashMap.put(DBConst.AvailableDay,SelectDayTv.getText().toString());
        hashMap.put(DBConst.AppointmentTime,StartingTimeTv.getText().toString()+"~"+EndingTimeTv.getText().toString());
        hashMap.put(DBConst.AppointmentFee,AppointmentFeeEt.getText().toString());
        hashMap.put(DBConst.UnavaiableSDate,StartingDateTv.getText().toString());
        hashMap.put(DBConst.UnavaiableEDate,EndingDateTv.getText().toString());
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isComplete())
                        {
                            SaveIntoHospitalDirectory();
                        }
                    }
                });
    }
    private void SaveIntoHospitalDirectory()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.HospitalName).child(HospitalNameEt.getText().toString());
        reference.child(UID).setValue(CategoryString).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
            }
        });
    }

    private void ShowDataIfAvaiable()
    {
        final String UID=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child(DBConst.AppointmentShedule).child(UID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                arrayList.clear();
                if (dataSnapshot.exists())
                {
                    ConfirmBtn.setEnabled(true);
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        String HospitalName=dataSnapshot1.getKey();
                        String AvaialableDay=dataSnapshot1.child(DBConst.AvailableDay).getValue().toString();
                        String AppointmentTime=dataSnapshot1.child(DBConst.AppointmentTime).getValue().toString();
                        String AppointmentFee=dataSnapshot1.child(DBConst.AppointmentFee).getValue().toString();
                        String UnavaiableSDate=dataSnapshot1.child(DBConst.UnavaiableSDate).getValue().toString();
                        String UnavaiableEDate=dataSnapshot1.child(DBConst.UnavaiableEDate).getValue().toString();
                        arrayList.add(new AppointmentDataModel(UID,HospitalName,AvaialableDay,AppointmentFee,UnavaiableSDate,AppointmentTime,UnavaiableEDate));
                    }
                }
                else
                {
                    ConfirmBtn.setEnabled(false);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void ConfirmMethod()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).child(UID);
        reference.child(DBConst.AccountCompletion).setValue(true);
        reference.child(DBConst.AccountType).setValue(DBConst.Doctor);
        reference.child(DBConst.AccountValidity).setValue(true);
        reference.child(DBConst.AuthorityValidity).setValue(false)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            intent=new Intent(activity,DoctorProfileViewActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else
                        {
                            myToastClass.LToast("Failed to save data");
                        }
                    }
                });
    }

}

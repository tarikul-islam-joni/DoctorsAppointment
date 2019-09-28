package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.Calendar;

public class EditAppointmentSchedule extends AppCompatActivity implements View.OnClickListener
{
    private AlertDialog CategorySelectionDialog;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;

    private InitializationUIHelperClass initializationUIHelperClass;
    private Activity activity;

    private EditText[] editTexts;
    private TextView[] textViews;
    private Button[] buttons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment_schedule);
        Initialization();
        InitializationClass();
        InitializationUI();
    }

    private void Initialization()
    {
        activity=EditAppointmentSchedule.this;
    }
    private void InitializationClass()
    {
        initializationUIHelperClass=new InitializationUIHelperClass(getWindow().getDecorView());
    }
    private void InitializationUI()
    {
        int[] text_view_id={R.id.text_view_0,R.id.text_view_1,R.id.text_view_2,R.id.text_view_3,R.id.text_view_4,R.id.text_view_5};
        textViews=initializationUIHelperClass.setTextViews(text_view_id);
        int[] edit_text_id={R.id.edit_text_0,R.id.edit_text_1,R.id.edit_text_3};
        editTexts=initializationUIHelperClass.setEditTexts(edit_text_id);
        int[] button_id={R.id.button_0};
        buttons=initializationUIHelperClass.setButtons(button_id);

        for(int i=0; i<10; i++)
        {
            if(i<text_view_id.length)
            {
                textViews[i].setOnClickListener(this);
            }
            if(i<button_id.length)
            {
                buttons[i].setOnClickListener(this);
            }
        }

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.text_view_0:
                ShowSpecializedMenu();
                break;
            case R.id.text_view_1:
                ShowAvailableDayMenu();
                break;
            case R.id.text_view_2:
                GetTimeMethod("Starting");
                break;
            case R.id.text_view_3:
                GetTimeMethod("Ending");
                break;
            case R.id.text_view_4:
                GetDateMethod("Starting");
                break;
            case R.id.text_view_5:
                GetDateMethod("Ending");
                break;

        }
    }

    private void ShowAvailableDayMenu()
    {
        final String[] DayList=getResources().getStringArray(R.array.select_day_arrya);
        final ArrayList<String> DayArrayList=new ArrayList<>();
        boolean[] Specialization_Checked=new boolean[DayList.length];
        for(int i=0; i<DayList.length; i++)
        {
            Specialization_Checked[i]=false;
        }

        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("Select Available Day");
        builder.setMultiChoiceItems(DayList,Specialization_Checked , new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b)
            {
                if (b)
                {
                    DayArrayList.add(DayList[i]);
                }
                else
                {
                    for (int k=0; k<DayArrayList.size(); i++)
                    {
                        if (DayArrayList.get(k).matches(DayList[i]))
                        {
                            DayArrayList.remove(k);
                        }
                    }
                }
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                StringBuilder CategoryStringBuilder=new StringBuilder();
                for(int k=0; k<DayArrayList.size(); k++)
                {
                    if (k!=0)
                    {
                        CategoryStringBuilder.append(",");
                    }
                    CategoryStringBuilder.append(DayArrayList.get(k));
                }
                textViews[1].setText(CategoryStringBuilder.toString());
                CategorySelectionDialog.dismiss();
            }
        });

        CategorySelectionDialog=builder.create();
        CategorySelectionDialog.show();
    }

    private void ShowSpecializedMenu()
    {
        final String[]CategoryResourceStringArray=getResources().getStringArray(R.array.doctor_category_array);
        final ArrayList<String> CategoryArrayList=new ArrayList<>();
        boolean[] Specialization_Checked=new boolean[CategoryResourceStringArray.length];
        for(int i=0; i<CategoryResourceStringArray.length; i++)
        {
            Specialization_Checked[i]=false;
        }

        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("Specialization Selection");
        builder.setMultiChoiceItems(CategoryResourceStringArray,Specialization_Checked , new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b)
            {
                if (b)
                {
                    CategoryArrayList.add(CategoryResourceStringArray[i]);
                }
                else
                {
                    for (int k=0; k<CategoryArrayList.size(); i++)
                    {
                        if (CategoryArrayList.get(k).matches(CategoryResourceStringArray[i]))
                        {
                            CategoryArrayList.remove(k);
                        }
                    }
                }
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                StringBuilder CategoryStringBuilder=new StringBuilder();
                for(int k=0; k<CategoryArrayList.size(); k++)
                {
                    if (k!=0)
                    {
                        CategoryStringBuilder.append(",");
                    }
                    CategoryStringBuilder.append(CategoryArrayList.get(k));
                }
                textViews[0].setText(CategoryStringBuilder.toString());
                CategorySelectionDialog.dismiss();
            }
        });

        CategorySelectionDialog=builder.create();
        CategorySelectionDialog.show();
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
                    textViews[2].setText(setHour+":"+setMinute);
                }
                if (Which.matches("Ending"))
                {
                    textViews[3].setText(setHour+":"+setMinute);
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
        datePickerDialog=new DatePickerDialog(activity,android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthofyear, int dayofmonth) {
                if (Which.matches("Starting"))
                {
                    textViews[4].setText(dayofmonth+"-"+(monthofyear+1)+"-"+year);
                    textViews[5].setText(dayofmonth+"-"+(monthofyear+1)+"-"+year);
                }
                if (Which.matches("Ending"))
                {
                    textViews[5].setText(dayofmonth+"-"+(monthofyear+1)+"-"+year);
                }
            }
        },year,month,day);

        datePickerDialog.show();
    }
}

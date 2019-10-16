package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorAccountDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DoctorCustomDateAppointmentFragment extends Fragment
{

    private String SeventhDay="SeventhDay",CurrentMonthWithYear,SelectedMonthWithYear,SelectedDateWithMonthWithYear;
    private String SelectedMonth,SelectedYear;
    private AlertDialog SelectMonthAndYearDialog;
    private View SelectMonthAndYearView;

    private HashMap<String,Object> DataHashMap;
    private View view;
    private RecyclerView recyclerView;
    private TextView textView;
    private LinearLayout linearLayout;
    private Button[] buttons;
    private String ChooseDate;

    private StringBuilder stringBuilder;

    ArrayList<ArrayList<HashMap<String,Object>>> SeventhDaysDataArrayList;

    private int Counter=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=LayoutInflater.from(getActivity()).inflate(R.layout.doctor_scheduled_appointment_list_show,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView=view.findViewById(R.id.recycler_view_0);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1,RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
        textView=view.findViewById(R.id.text_view_0);
        linearLayout=view.findViewById(R.id.linear_layout_0);
        linearLayout.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        SeventhDaysDataArrayList=new ArrayList<>();

        stringBuilder=new StringBuilder();
        //Init state
        ProcessNextSeventhDays();

        buttons=new InitializationUIHelperClass(view).setButtons(new int[]{R.id.button_0,R.id.button_1,R.id.button_2,R.id.button_3});
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                textView.setText("");
                stringBuilder=new StringBuilder();
                Counter=0;
                SeventhDaysDataArrayList=new ArrayList<>();
                ButtonEffect(0);
                ProcessNextSeventhDays();
            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                textView.setText("");
                stringBuilder=new StringBuilder();
                ButtonEffect(3);
                CallDBForEntireMonthAppointmentList();
            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                textView.setText("");
                stringBuilder=new StringBuilder();
                ButtonEffect(1);
                ShowDialogToGetMonthAndYear();
            }
        });
        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                textView.setText("");
                stringBuilder=new StringBuilder();
                ButtonEffect(2);
                ShowDatePickerDialog();
            }
        });
    }

    private void ButtonEffect(int WhichButton)
    {
        for (int i=0; i<buttons.length; i++)
        {
            if (i==WhichButton)
            {
                buttons[i].setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.button_background_5));
            }
            else
            {
                buttons[i].setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.button_backgrount_4));
            }
        }
    }

    public void GetDataFromActivity(String WhichField, HashMap<String,Object> DataHashMap)
    {
        this.DataHashMap=new HashMap<>();
        this.DataHashMap=DataHashMap;
        switch (WhichField)
        {
            case DBConst.GetNextSevenDaysDoctorScheduledAppointmentList:
                GetNextSeventhDaysDataProcessing();
                break;
            case DBConst.GetEntireMonthDoctorScheduledAppointmentList:
                ArrayList<HashMap<String,Object>> arrayList_1=new ArrayList<>();
                arrayList_1=new DataParseForShowAppointmentList().GetSelectedKeyBasedAppointmentList(DataHashMap,DBConst.MonthWithYear,CurrentMonthWithYear);
                ShowScheduledAppointmentListAdapter showScheduledAppointmentListAdapter_1=new ShowScheduledAppointmentListAdapter("DoctorCustomDateAppointmentFragment",getActivity(),arrayList_1);
                stringBuilder.append("Total Appointment Number : "+arrayList_1.size());
                textView.setText(stringBuilder.toString());
                recyclerView.setAdapter(showScheduledAppointmentListAdapter_1);
                break;
            case DBConst.GetSelectedMonthWithYearDateAppointmentHistoryFromDoctorAccount:
                ArrayList<HashMap<String,Object>> arrayList_2=new ArrayList<>();
                arrayList_2=new DataParseForShowAppointmentList().GetSelectedKeyBasedAppointmentList(DataHashMap,DBConst.MonthWithYear,SelectedMonthWithYear);
                stringBuilder.append("Total Appointment Number : "+arrayList_2.size());
                textView.setText(stringBuilder.toString());
                ShowScheduledAppointmentListAdapter showScheduledAppointmentListAdapter_2=new ShowScheduledAppointmentListAdapter("DoctorCustomDateAppointmentFragment",getActivity(),arrayList_2);
                recyclerView.setAdapter(showScheduledAppointmentListAdapter_2);
                break;
            case DBConst.GetCustomDateDoctorScheduledAppointmentList:
                ArrayList<HashMap<String,Object>> arrayList_3=new ArrayList<>();
                arrayList_3=new DataParseForShowAppointmentList().GetSelectedKeyBasedAppointmentList(DataHashMap,DBConst.AppointmentDate,SelectedDateWithMonthWithYear);
                stringBuilder.append("Total Appointment Number : "+arrayList_3.size());
                textView.setText(stringBuilder.toString());
                ShowScheduledAppointmentListAdapter showScheduledAppointmentListAdapter_3=new ShowScheduledAppointmentListAdapter("DoctorCustomDateAppointmentFragment",getActivity(),arrayList_3);
                recyclerView.setAdapter(showScheduledAppointmentListAdapter_3);
                break;
        }
    }

    private void GetNextSeventhDaysDataProcessing()
    {
        Counter++;
        ArrayList<HashMap<String,Object>> arrayList_0=new ArrayList<>();
        arrayList_0=new DataParseForShowAppointmentList().GetSelectedKeyBasedAppointmentList(DataHashMap,"SeventhDay","SeventhDay");
        SeventhDaysDataArrayList.add(arrayList_0);
        if (Counter==7)
        {
            ArrayList<HashMap<String,Object>> TotalDataArrayList=new ArrayList<>();
            for (int i=0; i<SeventhDaysDataArrayList.size(); i++)
            {
                ArrayList<HashMap<String,Object>> mapArrayList1=new ArrayList<>();
                mapArrayList1=SeventhDaysDataArrayList.get(i);
                for (int k=0; k<mapArrayList1.size(); k++)
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap=mapArrayList1.get(k);
                    TotalDataArrayList.add(hashMap);
                }
            }
            stringBuilder.append("Total Appointment Number : "+TotalDataArrayList.size());
            textView.setText(stringBuilder.toString());
            ShowScheduledAppointmentListAdapter showScheduledAppointmentListAdapter_1=new ShowScheduledAppointmentListAdapter("DoctorCustomDateAppointmentFragment",getActivity(),TotalDataArrayList);
            recyclerView.setAdapter(showScheduledAppointmentListAdapter_1);
        }
    }

    private void ShowDialogToGetMonthAndYear()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        SelectMonthAndYearView=LayoutInflater.from(getActivity()).inflate(R.layout.doctor_choose_month_year_dialog_layout,null,false);
        Spinner spinner_0=SelectMonthAndYearView.findViewById(R.id.spinner_0);
        Spinner spinner_1=SelectMonthAndYearView.findViewById(R.id.spinner_1);
        final ArrayList<String> arrayList=new ArrayList<>();
        for(int i=1; i<=12; i++)
        {
            arrayList.add(String.valueOf(i));
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,arrayList);
        spinner_0.setAdapter(arrayAdapter);
        final ArrayList<String> arrayList1=new ArrayList<>();
        for(int i=2019; i<Calendar.getInstance().get(Calendar.YEAR)+1; i++)
        {
            arrayList1.add(String.valueOf(i));
        }
        ArrayAdapter<String> arrayAdapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,arrayList1);
        spinner_1.setAdapter(arrayAdapter1);

        SelectedMonth=DBConst.UNKNOWN;
        SelectedYear=DBConst.UNKNOWN;
        spinner_0.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                SelectedMonth=arrayList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedYear=arrayList1.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button button=SelectMonthAndYearView.findViewById(R.id.button_0);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!(SelectedMonth.matches(DBConst.UNKNOWN) || SelectedYear.matches(DBConst.UNKNOWN)))
                SelectMonthAndYearDialog.dismiss();
                CallDBForSelectedMonthAppointmentList(SelectedMonth+"-"+SelectedYear);
            }
        });

        builder.setView(SelectMonthAndYearView);
        SelectMonthAndYearDialog=builder.create();
        SelectMonthAndYearDialog.show();
    }

    private void ShowDatePickerDialog()
    {
        Calendar calendar=Calendar.getInstance();
        int date=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);

        DatePickerDialog.OnDateSetListener DatePickerListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date)
            {
                ChooseDate=String.valueOf(date)+"-"+String.valueOf((month+1))+"-"+String.valueOf(year);
                CallDBForCustomDateAppointmentList(ChooseDate);
            }
        };
        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(),DatePickerListener,date,month,year);
        calendar.add(calendar.DATE,+2);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }
    private void ProcessNextSeventhDays()
    {
        stringBuilder.append("AppointmentList Retrieving For Next Seventh Days\n");
        for(int i=0; i<7; i++)
        {
            Calendar calendar=Calendar.getInstance();
            calendar.add(calendar.DATE,+i);
            String Date=String.valueOf(calendar.get(Calendar.DATE));
            String Month=String.valueOf(calendar.get(Calendar.MONTH)+1);
            String Year=String.valueOf(calendar.get(Calendar.YEAR));
            CallDBForNextSeventhDaysAppointmentList(Date+"-"+Month+"-"+Year);
        }

    }

    private void CallDBForNextSeventhDaysAppointmentList(String NewDate)
    {
        DoctorAccountDB doctorAccountDB=new DoctorAccountDB(getActivity());
        doctorAccountDB.GetDoctorScheduledAppointmentListFromDoctorAccount(DBConst.SELF,DBConst.AppointmentDate,NewDate,DBConst.GetNextSevenDaysDoctorScheduledAppointmentList);
    }
    private void CallDBForEntireMonthAppointmentList()
    {
        String CurrentMonthAndYear=String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1)+"-"+String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        stringBuilder.append("AppointmentList For Current Month : "+CurrentMonthAndYear+"\n");
        this.CurrentMonthWithYear=CurrentMonthAndYear;

        DoctorAccountDB doctorAccountDB=new DoctorAccountDB(getActivity());
        doctorAccountDB.GetDoctorScheduledAppointmentListFromDoctorAccount(DBConst.SELF,DBConst.MonthWithYear,CurrentMonthAndYear,DBConst.GetEntireMonthDoctorScheduledAppointmentList);
    }
    private void CallDBForSelectedMonthAppointmentList(String SeletedMonthAndYear)
    {
        this.SelectedMonthWithYear=SeletedMonthAndYear;
        stringBuilder.append("AppointmentList For Selected Month : "+SeletedMonthAndYear+"\n");
        DoctorAccountDB doctorAccountDB=new DoctorAccountDB(getActivity());
        doctorAccountDB.GetDoctorScheduledAppointmentListFromDoctorAccount(DBConst.SELF,DBConst.MonthWithYear,SeletedMonthAndYear,DBConst.GetSelectedMonthWithYearDateAppointmentHistoryFromDoctorAccount);
    }
    private void CallDBForCustomDateAppointmentList(String SelectedDate)
    {
        this.SelectedDateWithMonthWithYear=SelectedDate;
        stringBuilder.append("AppointmentList For Selected Date : "+SelectedDate+"\n");
        DoctorAccountDB doctorAccountDB=new DoctorAccountDB(getActivity());
        doctorAccountDB.GetDoctorScheduledAppointmentListFromDoctorAccount(DBConst.SELF,DBConst.AppointmentDate,SelectedDate,DBConst.GetCustomDateDoctorScheduledAppointmentList);
    }
}
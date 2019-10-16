package com.tarikulislamjoni95.doctorsappointment.AdminPart;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AdminControlDBHelper;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AdminPaymentControl extends AppCompatActivity implements GetDataFromDBInterface
{
    private ArrayList<String> YearList;
    private ArrayList<String> MonthList;
    private String Month,Year;
    private Spinner[] spinners;
    private Button[] buttons;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_doctor_payment_logs);

        spinners=new InitializationUIHelperClass(getWindow().getDecorView()).setSpinner(new int[]{R.id.spinner_0,R.id.spinner_1});
        buttons=new InitializationUIHelperClass(getWindow().getDecorView()).setButtons(new int[]{R.id.button_0,R.id.button_1});
        recyclerView=findViewById(R.id.recycler_view_0);
        recyclerView.setLayoutManager(new GridLayoutManager(AdminPaymentControl.this,1,RecyclerView.VERTICAL,false));

        YearList=new ArrayList<>();
        MonthList=new ArrayList<>();

        for(int i=1; i<=12; i++)
        {
            MonthList.add(String.valueOf(i));
        }

        for(int i=2019; i<Calendar.getInstance().get(Calendar.YEAR)+1; i++)
        {
            YearList.add(String.valueOf(i));
        }
        ArrayAdapter arrayAdapter=new ArrayAdapter(AdminPaymentControl.this,android.R.layout.simple_list_item_1,MonthList);
        spinners[0].setAdapter(arrayAdapter);
        arrayAdapter=new ArrayAdapter(AdminPaymentControl.this,android.R.layout.simple_list_item_1,YearList);
        spinners[1].setAdapter(arrayAdapter);

        spinners[0].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                Month=MonthList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinners[1].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Year=YearList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CallDBForGetTheBillingOnSelectedMonth(Month+"-"+Year);
            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ShowCalenderAndChooseDate();
            }
        });
    }

    private void ShowCalenderAndChooseDate()
    {
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int dayOfMonth=calendar.get(Calendar.DATE);
        DatePickerDialog datePickerDialog=new DatePickerDialog(AdminPaymentControl.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date)
            {
                CallDBForGetTheBillingOnSelectedDate(date,month,year);
            }
        },year,month,dayOfMonth);
        datePickerDialog.show();
    }

    private void CallDBForGetTheBillingOnSelectedDate(int date,int month,int year)
    {
        AdminControlDBHelper adminControlDBHelper =new AdminControlDBHelper(AdminPaymentControl.this);
        adminControlDBHelper.GetBillForTheSelectedDate(String.valueOf(month)+"-"+String.valueOf(year),String.valueOf(date)+"-"+String.valueOf(month)+"-"+String.valueOf(year), DBConst.GetBillForTheSelectedDateFromDB);
    }

    private void CallDBForGetTheBillingOnSelectedMonth(String MonthWithYear)
    {
        AdminControlDBHelper adminControlDBHelper =new AdminControlDBHelper(AdminPaymentControl.this);
        adminControlDBHelper.GetBillForTheSelectedMonth(MonthWithYear,DBConst.GetBillForTheSelectedMonthWithYearFromDB);
    }

    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        Log.d("myEr11ror",DataHashMap.toString());
        switch (WhichDB)
        {
            case DBConst.GetBillForTheSelectedDateFromDB:
                ProcessSelectedData(DataHashMap);
                break;
            case DBConst.GetBillForTheSelectedMonthWithYearFromDB:
                ProcessSelectedData(DataHashMap);
                break;
        }
    }

    HashMap<String,Object> hashMap=new HashMap<>();
    private void ProcessSelectedData(HashMap<String, Object> dataHashMap)
    {
        Set<String> set=new HashSet<>();
        HashMap<String,Object> hashMap=new HashMap<>();
        if (dataHashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            DataSnapshot dataSnapshot=(DataSnapshot)dataHashMap.get(DBConst.DataSnapshot);

            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
            {
                for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                {
                    set.add(dataSnapshot2.getValue().toString());
                }
            }
        }

        Log.d("mySet : ","mmmSet : "+set.toString() );

        hashMap=new HashMap<>();
        Iterator<String> iterator=set.iterator();
        while (iterator.hasNext())
        {
            String UID=iterator.next();
            if (dataHashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
            {
                DataSnapshot dataSnapshot=(DataSnapshot)dataHashMap.get(DBConst.DataSnapshot);

                int Money=0;
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    if (UID.matches(dataSnapshot1.child(UID).toString()))
                    {
                        Money+=Integer.parseInt(dataSnapshot1.child(DBConst.AppointmentFee).toString());
                        Log.d("myError",UID + " :: "+ Money);
                    }
                    else
                    {
                        hashMap.put(UID,Money);
                        Money=0;
                    }
                }
            }

        }
    }

    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList) {

    }
}

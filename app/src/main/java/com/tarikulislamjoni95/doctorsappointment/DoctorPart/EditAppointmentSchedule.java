package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBHelper;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorAccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorFilterDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class EditAppointmentSchedule extends AppCompatActivity implements GetDataFromDBInterface
{
    //Database Variable
    private DBHelper dbHelper;
    private DoctorAccountDB doctorAccountDB;
    private DoctorFilterDB doctorFilterDB;

    //Class Variable
    private MyToastClass myToastClass;
    private MyLoadingDailog myLoadingDailog;
    private InitializationUIHelperClass initializationUIHelperClass;
    private AppointmentScheduleInfoAdapter adapter;

    //Important Variable
    private Activity activity;
    private String DoctorUID;
    private HashMap<String,Object> DeleteDataHashMap;
    private HashMap<String,String> SaveDataHashMap;

    //Data Variable
    private boolean rbtn_flip_flop=false;
    private int e=0,t=0;
    private int s_minute=0,s_hour=0,e_minute=100,e_hour=100,hour,minute;
    private String[] DataKeyEditText,DataKeyTextView;

    //Dialog Variable
    private AlertDialog ShowAppointmentScheduleDialog;
    private AlertDialog CategorySelectionDialog;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;


    private TextView[] textViews;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_appointment_schedule_set);
        Initialization();
        InitializationClass();
        InitializationUI();
        InitializationDB();
    }
    private void Initialization()
    {
        activity=EditAppointmentSchedule.this;

        DataKeyEditText=new String[]{DBConst.HospitalName,DBConst.AppointmentFee,DBConst.AppointmentCapacity};
        DataKeyTextView=new String[]{DBConst.Specialization,DBConst.AvailableDay,DBConst.AppointmentSTime,DBConst.AppointmentETime,DBConst.UnavailableSDate,DBConst.UnavailableEDate};
        DeleteDataHashMap=new HashMap<>();
        SaveDataHashMap=new HashMap<>();
    }
    private void InitializationUI()
    {
        textViews=new TextView[2];
        textViews[0]=findViewById(R.id.text_view_0);
        textViews[1]=findViewById(R.id.text_view_1);
        listView=findViewById(R.id.list_view_0);

        textViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ShowAppointmentScheduleEditorDialog(new HashMap<String, Object>());
            }
        });
    }
    private void InitializationClass()
    {
        myToastClass=new MyToastClass(activity);
        initializationUIHelperClass=new InitializationUIHelperClass(getWindow().getDecorView());
        myLoadingDailog=new MyLoadingDailog(activity,R.drawable.spinner);
    }

    ///********************Starting Point ShowAppointmentScheduleEditorDialog********************///
    private View ShowAppointmentScheduleDialogView;
    private RadioButton radioButtonD;
    private EditText[] editTextsD;
    private TextView[] textViewsD;
    private Button[] buttonsD;
    public void ShowAppointmentScheduleEditorDialog(final HashMap<String,Object> hashMap)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
         ShowAppointmentScheduleDialogView= LayoutInflater.from(activity).inflate(R.layout.doctor_edit_appointment_schedule,null,false);
        builder.setView(ShowAppointmentScheduleDialogView);
        ShowAppointmentScheduleDialog=builder.create();
        ShowAppointmentScheduleDialog.show();
        initializationUIHelperClass=new InitializationUIHelperClass(ShowAppointmentScheduleDialogView);
        int[] text_view_id={R.id.text_view_0,R.id.text_view_1,R.id.text_view_2,R.id.text_view_3,R.id.text_view_4,R.id.text_view_5};
        textViewsD=initializationUIHelperClass.setTextViews(text_view_id);
        int[] edit_text_id={R.id.edit_text_0,R.id.edit_text_1,R.id.edit_text_2};
        editTextsD=initializationUIHelperClass.setEditTexts(edit_text_id);
        int[] button_id={R.id.button_0,R.id.button_1};
        buttonsD=initializationUIHelperClass.setButtons(button_id);
        radioButtonD=ShowAppointmentScheduleDialogView.findViewById(R.id.radio_button_0);
        radioButtonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbtn_flip_flop)
                {
                    rbtn_flip_flop=false;
                    textViewsD[4].setText("");
                    textViewsD[5].setText("");
                    textViewsD[4].setVisibility(View.GONE);
                    textViewsD[5].setVisibility(View.GONE);
                    radioButtonD.setChecked(false);
                } else
                {
                    rbtn_flip_flop=true;
                    textViewsD[4].setText("");
                    textViewsD[5].setText("");
                    textViewsD[4].setVisibility(View.VISIBLE);
                    textViewsD[5].setVisibility(View.VISIBLE);
                    radioButtonD.setChecked(true);
                }
            }
        });

        for(int i=0; i<editTextsD.length; i++)
        {
            if (hashMap.containsKey(DataKeyEditText[i]))
            {
                editTextsD[i].setText(hashMap.get(DataKeyEditText[i]).toString());
            }
        }
        for(int i=0; i<textViewsD.length; i++)
        {
            if (hashMap.containsKey(DataKeyTextView[i]))
            {
                textViewsD[i].setText(hashMap.get(DataKeyTextView[i]).toString());
            }
        }
        if (hashMap.containsKey(DBConst.UnavailableSDate) && hashMap.containsKey(DBConst.UnavailableEDate))
        {
            if (hashMap.get(DBConst.UnavailableSDate).toString().matches(DBConst.UNKNOWN) || hashMap.get(DBConst.UnavailableEDate).toString().matches(DBConst.UNKNOWN))
            {
                rbtn_flip_flop=false;
                radioButtonD.setText("Check the radio button for advance leave");
                radioButtonD.setChecked(false);
                textViewsD[4].setText("");
                textViewsD[5].setText("");
                textViewsD[4].setVisibility(View.GONE);
                textViewsD[5].setVisibility(View.GONE);
            }
            else
            {
                rbtn_flip_flop=true;
                radioButtonD.setText("Set Advance Leave");
                radioButtonD.setChecked(true);
                textViewsD[4].setVisibility(View.VISIBLE);
                textViewsD[5].setVisibility(View.VISIBLE);
                textViewsD[4].setText(hashMap.get(DBConst.UnavailableSDate).toString());
                textViewsD[5].setText(hashMap.get(DBConst.UnavailableEDate).toString());
            }
        }
        textViewsD[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ShowSpecializedMenu();
            }
        });
        textViewsD[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAvailableDayMenu();
            }
        });
        textViewsD[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                GetTimeMethod("Starting");
            }
        });
        textViewsD[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetTimeMethod("Ending");
            }
        });
        textViewsD[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetDateMethod("Starting");
            }
        });
        textViewsD[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetDateMethod("Ending");
            }
        });
        buttonsD[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetAllDataFromField();
            }
        });
        buttonsD[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAppointmentScheduleDialog.dismiss();
            }
        });
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
                textViewsD[0].setText(CategoryStringBuilder.toString());
                CategorySelectionDialog.dismiss();
            }
        });

        CategorySelectionDialog=builder.create();
        CategorySelectionDialog.show();
    }
    private void ShowAvailableDayMenu()
    {
        final String[] DayList=getResources().getStringArray(R.array.select_day_array);
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
                textViewsD[1].setText(CategoryStringBuilder.toString());
                CategorySelectionDialog.dismiss();
            }
        });

        CategorySelectionDialog=builder.create();
        CategorySelectionDialog.show();
    }

    private void GetTimeMethod(final String Which)
    {
        hour=6;
        minute=0;
        timePickerDialog=new TimePickerDialog(activity,android.R.style.Theme_Holo_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int setHour, int setMinute)
            {
                if (Which.matches("Starting"))
                {
                    if ((e_hour==setHour && setMinute<e_minute)|| setHour<e_hour)
                    {
                        textViewsD[2].setText(setHour+":"+setMinute);
                        s_hour=setHour;
                        s_minute=setMinute;
                    }
                    else
                    {
                        s_minute=0;s_hour=0;e_minute=100;e_hour=100;
                        myToastClass.SToast("Appointment Starting Time Must Be Before Appointment Ending Time");
                        textViewsD[2].setText("");
                        textViewsD[3].setText("");
                    }
                }
                if (Which.matches("Ending"))
                {
                    if (setHour>s_hour ||(setHour==s_hour && setMinute>s_minute))
                    {
                        textViewsD[3].setText(setHour+":"+setMinute);
                        e_hour=setHour;
                        e_minute=setMinute;
                    }
                    else
                    {
                        s_minute=0;s_hour=0;e_minute=100;e_hour=100;
                        myToastClass.SToast("Appointment Ending Time Must Be After Appointment Starting Time");
                        textViewsD[3].setText("");
                        textViewsD[2].setText("");
                    }
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
                    textViewsD[4].setText(dayofmonth+"-"+(monthofyear+1)+"-"+year);
                    textViewsD[5].setText(dayofmonth+"-"+(monthofyear+1)+"-"+year);
                }
                if (Which.matches("Ending"))
                {
                    textViewsD[5].setText(dayofmonth+"-"+(monthofyear+1)+"-"+year);
                }
            }
        },year,month,day);

        calendar.add(Calendar.DATE,+1);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }
    private void GetAllDataFromField()
    {
        SaveDataHashMap=new HashMap<>();
        String[] KeyStringE={DBConst.HospitalName,DBConst.AppointmentCapacity,DBConst.AppointmentFee};
        String[] KeyStringT={DBConst.Specialization,DBConst.AvailableDay,DBConst.AppointmentSTime,DBConst.AppointmentETime,DBConst.UnavailableSDate,DBConst.UnavailableEDate};
        if (editTextsD[0].getText().toString().isEmpty())
        {
            editTextsD[0].setError("Input Hospital Name");
        }
        else if (textViewsD[0].getText().toString().isEmpty())
        {
            myToastClass.SToast("Input specialization");
        }
        else if (textViewsD[1].getText().toString().isEmpty())
        {
            myToastClass.SToast("Input available day");
        }
        else if (textViewsD[2].getText().toString().isEmpty() || textViewsD[3].getText().toString().isEmpty())
        {
            myToastClass.SToast("Input time slot");
        }
        else if (editTextsD[1].getText().toString().isEmpty())
        {
            editTextsD[1].setError("Input appointment capacity");
        }
        else if (editTextsD[2].getText().toString().isEmpty())
        {
            editTextsD[2].setError("Input appointment fee");
        }
        else if (textViewsD[4].getText().toString().isEmpty() || textViewsD[5].getText().toString().isEmpty())
        {
            SaveDataHashMap.put(DBConst.UnavailableSDate,DBConst.UNKNOWN);
            SaveDataHashMap.put(DBConst.UnavailableEDate,DBConst.UNKNOWN);
            for(int i=0; i<textViewsD.length-2; i++)
            {
                SaveDataHashMap.put(KeyStringT[i],textViewsD[i].getText().toString());
            }
            for(int i=0; i<editTextsD.length; i++)
            {
                SaveDataHashMap.put(KeyStringE[i],editTextsD[i].getText().toString());
            }
            CloseEditAppointmentScheduleDialog();
            CallDBForDeleteAppointmentScheduleFromAccount();
        }
        else
        {
            for(int i=0; i<textViewsD.length; i++)
            {
                SaveDataHashMap.put(KeyStringT[i],textViewsD[i].getText().toString());
            }
            for(int i=0; i<editTextsD.length; i++)
            {
                SaveDataHashMap.put(KeyStringE[i],editTextsD[i].getText().toString());
            }
            CloseEditAppointmentScheduleDialog();
            CallDBForDeleteAppointmentScheduleFromAccount();
        }
    }
    ///**********************Ending Point ShowAppointmentScheduleEditorDialog********************///

    ///*******************************Get Data From Database***********************************////
    private void GetAppointmentScheduleInfoFromDB(ArrayList<HashMap<String, Object>> dataHashMapArrayList)
    {
        if (dataHashMapArrayList.size()==0)
        {
            listView.setVisibility(View.GONE);
            textViews[0].setVisibility(View.VISIBLE);
            dataHashMapArrayList.clear();
            adapter=new AppointmentScheduleInfoAdapter(activity,dataHashMapArrayList);
            listView.setAdapter(adapter);
        }
        else
        {
            textViews[0].setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            adapter=new AppointmentScheduleInfoAdapter(activity,dataHashMapArrayList);
            listView.setAdapter(adapter);
        }

    }
    private void GetUIDFormDB(HashMap<String,Object> hashMap)
    {
        if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.NOT_NULL_USER))
        {
            DoctorUID=hashMap.get(DBConst.UID).toString();
            CallDBForGetAccountScheduleInfo();
        }
        else
        {
            dbHelper.SignOut();
        }
    }
    private void GetStatusOnDeleteAppointmentScheduleFromAccount(HashMap<String,Object> hashMap)
    {
        CallDBForDeleteDoctorUIDFromHospitalDir();
    }
    private void GetStatusOnDeleteDoctorUIDFromHospitalDir(HashMap<String,Object> hashMap)
    {
        CallDBForSaveAppointmentScheduleInAccount();
    }
    private void GetStatusOnSaveAppointmentScheduleInAccount(HashMap<String,Object> hashMap)
    {
        if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            CallDBForSaveDoctorUIDInHospitalDir();
        }
    }

    private void GetStatusOnSaveDoctorUIDInHospitalDir(HashMap<String,Object> hashMap)
    {
        if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            myToastClass.SToast("Appointment Schedule Saved Successfully");
        }
        else
        {
            myToastClass.LToast("Failed");
        }
    }
    ///*******************************Starting Point Database***********************************////
    private void InitializationDB()
    {
        dbHelper=new DBHelper(activity);
        doctorAccountDB=new DoctorAccountDB(activity);
        doctorFilterDB=new DoctorFilterDB(activity);
        CallDBForUID();
    }
    private void CallDBForUID()
    {
        ShowLoadingDialog();
        dbHelper.GetUID();
    }
    private void CallDBForGetAccountScheduleInfo()
    {
        ShowLoadingDialog();
        doctorAccountDB.GetDoctorAppointmentScheduleInfo(DoctorUID);
    }
    private void CallDBForDeleteAppointmentScheduleFromAccount()
    {
        ShowLoadingDialog();
        doctorAccountDB.DeleteAppointScheduleInfo(DoctorUID,DeleteDataHashMap);
    }
    private void CallDBForDeleteDoctorUIDFromHospitalDir()
    {
        ShowLoadingDialog();
        doctorFilterDB.DeleteDoctorUIDFromHospitalDir(DoctorUID,DeleteDataHashMap);
    }
    private void CallDBForSaveAppointmentScheduleInAccount()
    {
        ShowLoadingDialog();
        doctorAccountDB.SaveAppointmentScheduleInfo(DoctorUID,SaveDataHashMap);
    }
    private void CallDBForSaveDoctorUIDInHospitalDir()
    {
        ShowLoadingDialog();
        doctorFilterDB.SaveDoctorUIDInHospitalDir(DoctorUID,SaveDataHashMap);
    }
    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        CloseLoadingDialog();
        Log.d("EditAppointmentSchedule",WhichDB+" : "+DataHashMap.toString());
        switch (WhichDB)
        {
            case DBConst.GetAccountUID:
                GetUIDFormDB(DataHashMap);
                break;
            case "EditAppointmentSchedule":
                DeleteDataHashMap=DataHashMap;
                ShowAppointmentScheduleEditorDialog(DeleteDataHashMap);
                break;
            case "DeleteAppointmentSchedule":
                DeleteDataHashMap=DataHashMap;
                CallDBForDeleteAppointmentScheduleFromAccount();
                break;
            case DBConst.GetStatusOnDeleteAppointmentScheduleInfoFromAccount:
                GetStatusOnDeleteAppointmentScheduleFromAccount(DataHashMap);
                break;
            case DBConst.GetStatusOnDeleteDoctorUIDFromHospitalDir:
                GetStatusOnDeleteDoctorUIDFromHospitalDir(DataHashMap);
                break;
            case DBConst.GetStatusOnSaveAppointmentScheduleInfoInAccount:
                GetStatusOnSaveAppointmentScheduleInAccount(DataHashMap);
                break;
            case DBConst.GetStatusOnSaveDoctorUIDInHospitalDir:
                GetStatusOnSaveDoctorUIDInHospitalDir(DataHashMap);
                break;
        }
    }
    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList)
    {
        CloseLoadingDialog();
        GetAppointmentScheduleInfoFromDB(DataHashMapArrayList);
    }
    ///********************************Ending Point Database************************************////


    ///*********Starting point Custom Adapter Class For Showing Appointment Schedule List**********///
    static class AppointmentScheduleInfoAdapter extends ArrayAdapter<HashMap<String,Object>> implements View.OnClickListener
    {
        GetDataFromDBInterface getDataFromDBInterface;
        Activity activity;
        ArrayList<HashMap<String,Object>> arrayList;
        InitializationUIHelperClass initializationUIHelperClass;

        public AppointmentScheduleInfoAdapter(Activity activity, ArrayList<HashMap<String, Object>> arrayList) {
            super(activity, R.layout.both_patient_doctor_appointment_schedule_show,arrayList);
            this.activity = activity;
            this.arrayList = arrayList;
            initializationUIHelperClass=new InitializationUIHelperClass(activity.getWindow().getDecorView());
            getDataFromDBInterface=(GetDataFromDBInterface)activity;
        }

        @Override
        public void onClick(View view)
        {
            int position=(Integer) view.getTag();
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap=arrayList.get(position);
            switch (view.getId())
            {
                case R.id.button_0:
                    getDataFromDBInterface.GetSingleDataFromDatabase("DeleteAppointmentSchedule",hashMap);
                    break;
                case R.id.button_1:
                    getDataFromDBInterface.GetSingleDataFromDatabase("EditAppointmentSchedule",hashMap);
                    break;
            }
        }

        MyViewHolder myViewHolder;
        public final class MyViewHolder
        {
            LinearLayout linearLayout;
            TextView[] textViews=new TextView[9];
            Button[] buttons=new Button[2];
            TextView textView;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView==null)
            {
                myViewHolder=new MyViewHolder();
                convertView=LayoutInflater.from(activity).inflate(R.layout.both_patient_doctor_appointment_schedule_show,parent,false);
                myViewHolder.textView=convertView.findViewById(R.id.heading_text_view);

                int[] text_view_id={R.id.text_view_0,R.id.text_view_1,R.id.text_view_2,R.id.text_view_3,R.id.text_view_4,R.id.text_view_5,R.id.text_view_6,R.id.text_view_7,R.id.text_view_8};
                for(int i=0; i<text_view_id.length; i++)
                {
                    myViewHolder.textViews[i]=convertView.findViewById(text_view_id[i]);
                }
                int[] button_id={R.id.button_0,R.id.button_1};
                for(int i=0; i<button_id.length; i++)
                {
                    myViewHolder.buttons[i]=convertView.findViewById(button_id[i]);
                }

                myViewHolder.linearLayout=convertView.findViewById(R.id.linear_layout_0);
                myViewHolder.linearLayout.setVisibility(View.VISIBLE);
                convertView.setTag(myViewHolder);
            }
            else
            {
                myViewHolder=(MyViewHolder) convertView.getTag();
            }

            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap=arrayList.get(position);

            int temp=position+1;

            myViewHolder.textView.setText("Appointment Schedule ( "+ temp +" )");

            String[] DataKey={DBConst.HospitalName,DBConst.Specialization,DBConst.AvailableDay,DBConst.AppointmentSTime,DBConst.AppointmentETime,DBConst.AppointmentCapacity,DBConst.AppointmentFee,DBConst.UnavailableSDate,DBConst.UnavailableEDate};
            for(int i=0; i<myViewHolder.textViews.length; i++)
            {
                myViewHolder.textViews[i].setText(hashMap.get(DataKey[i]).toString());
            }

            for(int i=0; i<myViewHolder.buttons.length; i++)
            {
                myViewHolder.buttons[i].setOnClickListener(this);
                myViewHolder.buttons[i].setTag(position);
            }

            return convertView;
        }
    }
    ///*********Ending point Custom Adapter Class For Showing Appointment Schedule List**********///


    ///***************************Starting Point of  Important Method****************************///
    @Override
    protected void onDestroy() {
        super.onDestroy();
        CloseEditAppointmentScheduleDialog();
        CloseLoadingDialog();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CloseEditAppointmentScheduleDialog();
        CloseLoadingDialog();
    }

    private void CloseEditAppointmentScheduleDialog()
    {
        if (ShowAppointmentScheduleDialog!=null)
        {
            if (ShowAppointmentScheduleDialog.isShowing())
            {
                ShowAppointmentScheduleDialog.dismiss();
            }
        }
    }
    private void ShowLoadingDialog()
    {
        if (myLoadingDailog!=null)
        {
            if (!myLoadingDailog.isShowing())
            {
                myLoadingDailog.show();
            }
        }
    }
    private void CloseLoadingDialog()
    {
        if (myLoadingDailog!=null)
        {
            if (myLoadingDailog.isShowing())
            {
                myLoadingDailog.dismiss();
            }
        }
    }
    ///***************************Starting Point of  Important Method****************************///
}

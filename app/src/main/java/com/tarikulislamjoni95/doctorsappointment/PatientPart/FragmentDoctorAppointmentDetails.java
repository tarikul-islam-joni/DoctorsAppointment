package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AdminControlDBHelper;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorAccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.PatientAccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.PaymentDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class FragmentDoctorAppointmentDetails extends Fragment
{
    private PaymentDB paymentDB;
    private DoctorAccountDB doctorAccountDB;
    private PatientAccountDB patientAccountDB;

    private MyLoadingDailog myLoadingDailog;

    public ArrayList<HashMap<String,Object>> DoctorAccountInformationArrayList;
    public HashMap<String,Object> PatientAccountInformationHashMap;

    private AlertDialog UnavailabilityDialog;
    private AlertDialog ShowAvailabilityDialog;
    private AlertDialog PaymentDialog;

    private AppointmentShowAdapter appointmentShowAdapter;
    private int ClickedPosition=0;
    private int SavingCount=0;
    private Activity activity;
    private View AppointmentFragmentView;

    private RecyclerView recyclerView;

    public String DoctorUID;
    public FragmentDoctorAppointmentDetails(String DoctorUID)
    {
        this.DoctorUID=DoctorUID;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(Activity)context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppointmentFragmentView=LayoutInflater.from(activity).inflate(R.layout.patient_doctor_appointment_details,container,false);
        return AppointmentFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitializationUI();
        InitializationDB();
    }
    private void InitializationUI()
    {
        myLoadingDailog=new MyLoadingDailog(activity,R.drawable.spinner);
        recyclerView=AppointmentFragmentView.findViewById(R.id.recycler_view_0);
        recyclerView.setLayoutManager(new GridLayoutManager(activity,1,RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
    }
    private void InitializationDB()
    {
        paymentDB=new PaymentDB(activity);
        doctorAccountDB=new DoctorAccountDB(activity);
        patientAccountDB=new PatientAccountDB(activity);
    }

    public void ShowLoadingDialog()
    {
        if (myLoadingDailog!=null)
        {
            if (!myLoadingDailog.isShowing())
            {
                myLoadingDailog.show();
            }
        }
    }

    public void CancelLoadingDialog()
    {
        if (myLoadingDailog!=null)
        {
            if (myLoadingDailog.isShowing())
            {
                myLoadingDailog.dismiss();
            }
        }
    }

    //Communicator of Activity And Fragment
    public void GetDataFromActivity(String WhichMethod, Object object)
    {
        CancelLoadingDialog();
        switch (WhichMethod)
        {
            case DBConst.GetDoctorAccountInformation:
                DoctorAccountInformationArrayList=(ArrayList<HashMap<String, Object>>)object;
                break;
            case DBConst.GetPatientAccountInformation:
                PatientAccountInformationHashMap=(HashMap<String, Object>)object;
                break;
            case DBConst.GetAppointmentScheduleInfo:
                appointmentShowAdapter=new AppointmentShowAdapter((Activity) AppointmentFragmentView.getContext(),(ArrayList<HashMap<String,Object>>)object,DoctorAccountInformationArrayList,PatientAccountInformationHashMap);
                recyclerView.setAdapter(appointmentShowAdapter);
                break;
            case DBConst.GetDoctorSelectedDateAppointmentHistoryFromDoctorAccount:
                GetDoctorAppointmentNumberFromAppointmentHistory((HashMap<String,Object>)object);
                break;
            case DBConst.GetPaymentDataFromPaymentDB:
                appointmentShowAdapter.GetPaymentDataFromDBToCheckValidity((HashMap<String, Object>)object);
                break;
            case DBConst.GetStatusOnSaveAppointmentCreationFromDoctorDB:
                CloseDialogAndChangeFragmentToPatientAppointmentHistoryActivity();
                break;
            case DBConst.GetStatusOnSaveAppointmentCreationFromPatientDB:
                CloseDialogAndChangeFragmentToPatientAppointmentHistoryActivity();
                break;
        }
    }

    private void CloseDialogAndChangeFragmentToPatientAppointmentHistoryActivity()
    {
        SavingCount++;
        if (SavingCount==2)
        {
            CancelAllDialog();
            CancelLoadingDialog();
            Intent intent=new Intent(activity,PatientAppointmentHistory.class);
            intent.putExtra(DBConst.GetPatientAccountInformation,PatientAccountInformationHashMap);
            startActivity(intent);
        }
    }

    private void GetDoctorAppointmentNumberFromAppointmentHistory(HashMap<String,Object>  hashMap)
    {
        int NumberOfAppointment=new DataParserForAppointmentHistory(hashMap).getNumberOfAppointments();
        appointmentShowAdapter.ShowAvailableDialog(NumberOfAppointment);
    }

    class AppointmentShowAdapter extends RecyclerView.Adapter<AppointmentShowAdapter.ViewHolder>
    {
        class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView textView;
            private TextView[] textViews;
            private Button button;
            private LinearLayout linearLayout;


            public ViewHolder(View itemView)
            {
                super(itemView);
                InitializationUIHelperClass initializationUIHelperClass=new InitializationUIHelperClass(itemView);
                int[] id={R.id.text_view_0,R.id.text_view_1,R.id.text_view_2,R.id.text_view_3,R.id.text_view_4,R.id.text_view_5,R.id.text_view_6,R.id.text_view_7,R.id.text_view_8};
                textViews=initializationUIHelperClass.setTextViews(id);
                linearLayout=itemView.findViewById(R.id.linear_layout_0);
                button=itemView.findViewById(R.id.button_2);
                textView=itemView.findViewById(R.id.heading_text_view);
            }
        }

        private Activity activity;
        private String SelectionDay;
        private String ReferenceKey,TraxId;
        private View AppointmentShowModelView;
        private int[] UnavailableSDateFormatInt=new int[3];
        private int[] UnavailableEDateFormatInt=new int[3];
        private ArrayList<HashMap<String,Object>> arrayList;
        private HashMap<String,Object> PatientAccountInformationHashMap;
        private CountDownTimer PaymentDialogTimer,ShowAvailabilityTimer;
        private ArrayList<HashMap<String,Object>> DoctorAccountInformationArrayList;
        private int SelectedAppointmentYear,SelectedAppointmentMonth,SelectedAppointmentDate;

        private AppointmentShowAdapter(Activity activity, ArrayList<HashMap<String,Object>> arrayList,ArrayList<HashMap<String,Object>> DoctorAccountInformationArrayList,HashMap<String,Object> PatientAccountInformationHashMap)
        {
            this.activity=activity;
            this.arrayList=arrayList;
            this.DoctorAccountInformationArrayList=DoctorAccountInformationArrayList;
            this.PatientAccountInformationHashMap=PatientAccountInformationHashMap;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            AppointmentShowModelView=LayoutInflater.from(activity).inflate(R.layout.both_patient_doctor_appointment_schedule_show,parent,false);
            return new ViewHolder(AppointmentShowModelView);
        }
        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
        {
            if (arrayList.size()!=0)
            {
                HashMap<String,Object> hashMap=arrayList.get(position);
                int temp=position+1;
                holder.textView.setText("Appointment Schedule ( "+ temp +" )");
                String[] DataKey={DBConst.HospitalName,DBConst.Specialization,DBConst.AvailableDay,DBConst.AppointmentSTime,DBConst.AppointmentETime,DBConst.AppointmentCapacity,DBConst.AppointmentFee,DBConst.UnavailableSDate,DBConst.UnavailableEDate};
                for(int i=0; i<holder.textViews.length; i++)
                {
                    holder.textViews[i].setText(hashMap.get(DataKey[i]).toString());
                }
                holder.linearLayout.setVisibility(View.GONE);
                holder.button.setVisibility(View.VISIBLE);
                if (DoctorAccountInformationArrayList.get(0).get(DBConst.AuthorityValidity).toString().matches(DBConst.UNVERIFIED))
                {
                    holder.button.setText(DBConst.UNVERIFIED+" DOCTOR\n"+"Online Appointment Unavailable");
                    holder.button.setEnabled(false);
                    holder.button.setBackground(ContextCompat.getDrawable(activity,R.drawable.button_background_2));
                }
                else
                {
                    holder.button.setEnabled(true);
                }
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        ClickedPosition=position;
                        CheckPrimaryAvailability();
                    }
                });
            }
        }
        private void CheckPrimaryAvailability()
        {
            Calendar calendar=Calendar.getInstance();
            int date=calendar.get(Calendar.DAY_OF_MONTH);
            int month=calendar.get(Calendar.MONTH)+1;
            int year=calendar.get(Calendar.YEAR);

            DatePickerDialog.OnDateSetListener DatePickerListener=new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int date)
                {
                    SelectedAppointmentYear=year;
                    SelectedAppointmentMonth=month+1;
                    SelectedAppointmentDate=date;

                    SimpleDateFormat sdf=new SimpleDateFormat("EEEE");
                    Date date1=new Date(year,month,date-1);
                    SelectionDay=sdf.format(date1);
                    if (CheckDayAvailability(SelectionDay))
                    {
                        if (arrayList.get(ClickedPosition).get(DBConst.UnavailableSDate).toString().matches("Unknown") || arrayList.get(ClickedPosition).get(DBConst.UnavailableEDate).toString().matches("Unknown"))
                        {
                            CallDBForGetCurrentDateSeatAvailability(date,month+1,year);
                        }
                        else
                        {
                            if (CheckDateAvailability(SelectedAppointmentDate,SelectedAppointmentMonth,SelectedAppointmentYear))
                            {
                                CallDBForGetCurrentDateSeatAvailability(SelectedAppointmentDate,SelectedAppointmentMonth,SelectedAppointmentYear);
                            }
                            else
                            {
                                ShowUnavailabilityDialog("Doctor is in leave "+arrayList.get(ClickedPosition).get(DBConst.UnavailableSDate).toString()+" to "+arrayList.get(ClickedPosition).get(DBConst.UnavailableEDate).toString());
                            }
                        }
                    }
                    else
                    {
                        ShowUnavailabilityDialog("Doctor only available in "+arrayList.get(ClickedPosition).get(DBConst.AvailableDay).toString());
                    }
                }
            };
            DatePickerDialog datePickerDialog=new DatePickerDialog(activity,DatePickerListener,date,month,year);
            calendar.add(calendar.DATE,+1);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            calendar.add(Calendar.DATE,+10);
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        }
        private boolean CheckDayAvailability(String day)
        {
            String AvailableDayString=arrayList.get(ClickedPosition).get(DBConst.AvailableDay).toString();
            String[] AvailableDay=AvailableDayString.split(",",0);
            boolean isAvailableDay=false;
            for(int i=0; i<AvailableDay.length; i++)
            {
                if (AvailableDay[i].matches(day))
                {
                    isAvailableDay=true;
                }
            }

            return isAvailableDay;
        }
        private boolean CheckDateAvailability(int CurrentDate, int CurrentMonth, int CurrentYear)
        {
            String UnavailableSDate = arrayList.get(ClickedPosition).get(DBConst.UnavailableSDate).toString();
            String UnavailableEDate = arrayList.get(ClickedPosition).get(DBConst.UnavailableEDate).toString();
            if (!(arrayList.get(ClickedPosition).get(DBConst.UnavailableSDate).toString().matches(DBConst.UNKNOWN)))
            {
                String[] UnavailableSDateArray=UnavailableSDate.split("-",0);
                String[] UnavailableEDateArray=UnavailableEDate.split("-",0);
                for (int i=0; i<UnavailableSDateArray.length; i++)
                {
                    UnavailableSDateFormatInt[i]=(int)Integer.parseInt(UnavailableSDateArray[i]);
                    UnavailableEDateFormatInt[i]=(int)Integer.parseInt(UnavailableEDateArray[i]);
                }
                Date date=new Date(CurrentYear,CurrentMonth-1,CurrentDate);
                Date S_Date=new Date(UnavailableSDateFormatInt[2],UnavailableSDateFormatInt[1]-1,UnavailableSDateFormatInt[0]-1);
                Date E_Date=new Date(UnavailableEDateFormatInt[2],UnavailableEDateFormatInt[1]-1,UnavailableEDateFormatInt[0]+1);
                return !(date.after(S_Date) && date.before(E_Date));
            }
            else
            {
                return true;
            }
        }
        //********************************Database Call *****************************************///
        private void CallDBForGetCurrentDateSeatAvailability(int date, int month, int year)
        {
            ShowLoadingDialog();
            DoctorAccountDB doctorAccountDB=new DoctorAccountDB(activity);
            String UID=DoctorUID;
            String HospitalName=arrayList.get(ClickedPosition).get(DBConst.HospitalName).toString();
            String AppointmentStartTime=arrayList.get(ClickedPosition).get(DBConst.AppointmentSTime).toString();
            doctorAccountDB.GetDoctorCurrentDateAppointmentNumber(UID,HospitalName,AppointmentStartTime,String.valueOf(year),String.valueOf(month),String.valueOf(date));
        }
        private void ShowUnavailabilityDialog(String Message)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(activity);
            builder.setTitle("Unavailability");
            builder.setIcon(R.drawable.cancel);
            builder.setMessage(Message+"\nTry another date please...");
            builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    UnavailabilityDialog.dismiss();
                }
            });
            UnavailabilityDialog=builder.create();
            UnavailabilityDialog.show();
        }
        private int SeatsAvailability;
        private void ShowAvailableDialog(int SeatsAvailability)
        {
            this.SeatsAvailability=SeatsAvailability;
            int TotalSeats=(int)Integer.valueOf(arrayList.get(ClickedPosition).get(DBConst.AppointmentCapacity).toString());
            final AlertDialog.Builder builder=new AlertDialog.Builder(activity);
            View DialogView=LayoutInflater.from(activity).inflate(R.layout.both_patient_doctor_appointment_schedule_show,null,false);
            builder.setView(DialogView);
            TextView textView=DialogView.findViewById(R.id.heading_text_view);
            textView.setText("Appointment Confirmation");
            int[] text_id={R.id.text_view_0,R.id.text_view_1,R.id.text_view_2,R.id.text_view_3,R.id.text_view_4,R.id.text_view_5,R.id.text_view_6,R.id.text_view_7,R.id.text_view_8};
            TextView[] textViews=new InitializationUIHelperClass(DialogView).setTextViews(text_id);
            int[] text_id_1={R.id.text_view_0_0,R.id.text_view_0_1,R.id.text_view_0_2,R.id.text_view_0_3,R.id.text_view_0_4,R.id.text_view_0_5,R.id.text_view_0_6};
            TextView[] textViews1=new InitializationUIHelperClass(DialogView).setTextViews(text_id_1);
            textViews1[2].setText("Appointment Day");
            textViews1[3].setText("Appointment Time");
            textViews1[4].setText("Appointment Available For");
            LinearLayout linearLayouts=DialogView.findViewById(R.id.linear_layout_0_6);
            linearLayouts.setVisibility(View.GONE);
            LinearLayout linearLayouts1=DialogView.findViewById(R.id.linear_layout_0);
            linearLayouts1.setVisibility(View.VISIBLE);
            int[] button_id={R.id.button_0,R.id.button_1};
            Button[] buttons=new InitializationUIHelperClass(DialogView).setButtons(button_id);
            String[] DataKey={DBConst.HospitalName,DBConst.Specialization,DBConst.AvailableDay,DBConst.AppointmentSTime,DBConst.AppointmentETime,DBConst.AppointmentCapacity,DBConst.AppointmentFee,DBConst.UnavailableSDate,DBConst.UnavailableEDate};
            for(int i=0; i<textViews.length; i++)
            {
                textViews[i].setText(arrayList.get(ClickedPosition).get(DataKey[i]).toString());
            }

            textViews[2].setText(SelectedAppointmentDate+"-"+SelectedAppointmentMonth+"-"+SelectedAppointmentYear+"("+SelectionDay+")");
            textViews[5].setText(String.valueOf(TotalSeats-SeatsAvailability));

            buttons[0].setVisibility(View.VISIBLE);
            buttons[1].setVisibility(View.VISIBLE);

            buttons[0].setText("Go Back");
            buttons[1].setText("Pay Now");
            if ((TotalSeats-SeatsAvailability)<=0)
            {
                buttons[1].setVisibility(View.INVISIBLE);
            }

            ShowAvailabilityTimer=new CountDownTimer(600000,60000)
            {
                @Override
                public void onFinish()
                {
                    if (ShowAvailabilityDialog!=null)
                    {
                        if (ShowAvailabilityDialog.isShowing())
                        {
                            ShowAvailabilityDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onTick(long l) {

                }
            };

            ShowAvailabilityTimer.start();
            buttons[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    ShowAvailabilityDialog.dismiss();
                    ShowAvailabilityTimer.cancel();
                    PaymentDialogShow();
                }
            });

            buttons[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    ShowAvailabilityDialog.dismiss();
                    ShowAvailabilityTimer.cancel();
                }
            });
            ShowAvailabilityDialog=builder.create();
            ShowAvailabilityDialog.show();
        }
        private TextView textView_P;
        private int PaymentCounter=20;
        private void PaymentDialogShow()
        {

            AlertDialog.Builder builder1=new AlertDialog.Builder(activity,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            View PaymentView=LayoutInflater.from(activity).inflate(R.layout.patient_payment_layout,null,false);
            builder1.setView(PaymentView);
            builder1.setView(PaymentView);
            final EditText editText=PaymentView.findViewById(R.id.edit_text_0);
            final TextView textView=PaymentView.findViewById(R.id.text_view_0);
            TextView textView1=PaymentView.findViewById(R.id.text_view_2);
            textView1.setText("Amount : "+arrayList.get(ClickedPosition).get(DBConst.AppointmentFee).toString()+" Taka");
            final String Reference_Key=GetRandomString();
            textView.setText(Reference_Key);
            Button button=PaymentView.findViewById(R.id.button_0);
            textView_P=PaymentView.findViewById(R.id.text_view_1);
            Button button1=PaymentView.findViewById(R.id.button_1);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    PaymentDialogTimer.cancel();
                    ShowAvailabilityTimer.cancel();
                    if (ShowAvailabilityDialog!=null)
                    {
                        if (ShowAvailabilityDialog.isShowing())
                        {
                            ShowAvailabilityDialog.dismiss();
                        }
                    }
                    if (PaymentDialog!=null)
                    {
                        if (PaymentDialog.isShowing())
                        {
                            PaymentDialog.dismiss();
                        }
                    }
                }
            });

            PaymentCounter=20;
            PaymentDialogTimer=new CountDownTimer(1200000,60000)
            {
                @Override
                public void onFinish()
                {
                    if (ShowAvailabilityDialog!=null)
                    {
                        if (ShowAvailabilityDialog.isShowing())
                        {
                            ShowAvailabilityDialog.dismiss();
                        }
                    }
                    if (PaymentDialog!=null)
                    {
                        if (PaymentDialog.isShowing())
                        {
                            PaymentDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onTick(long l)
                {
                    PaymentCounter--;
                    textView_P.setText(String.valueOf(PaymentCounter));
                }
            };

            PaymentDialogTimer.start();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if (!editText.getText().toString().isEmpty())
                    {
                        ReferenceKey=textView.getText().toString();
                        TraxId=editText.getText().toString();
                        Calendar calendar=Calendar.getInstance();
                        int date=calendar.get(Calendar.DAY_OF_MONTH);
                        int month=calendar.get(Calendar.MONTH)+1;
                        int year=calendar.get(Calendar.YEAR);
                        CallDBToCheckPaymentStatus(textView.getText().toString(),String.valueOf(year),String.valueOf(month),String.valueOf(date));
                    }
                }
            });
            PaymentDialog=builder1.create();
            PaymentDialog.show();
        }
        private String GetRandomString()
        {
            String[] MyStringArray=new String[]{"ABCDE1","ABCDE2","ABCDE3","ABCDE4","ABCDE5","ABCDE6","ABCDE7","ABCDE8","ABCDE9","ABCDE10","ABCDE11","ABCDE12"};
            Random random=new Random();
            int rand=random.nextInt(MyStringArray.length);
            return MyStringArray[rand];
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
        private String GetRandomString_1(final int sizeOfRandomString)
        {
            final Random random=new Random();
            final StringBuilder sb=new StringBuilder(sizeOfRandomString);
            for(int i=0;i<sizeOfRandomString;++i)
                sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
            return sb.toString();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////

        private void CallDBToCheckPaymentStatus(String ReferenceKey,String Year,String Month,String Date)
        {
            ShowLoadingDialog();
            paymentDB.GetTodayPayment(ReferenceKey, Year,Month,Date);
        }
        public void GetPaymentDataFromDBToCheckValidity(HashMap<String,Object> hashMap)
        {
            if (hashMap.containsKey(DBConst.RESULT))
            {
                if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
                {
                    if (hashMap.get(DBConst.ReferenceKey).toString().matches(ReferenceKey) && hashMap.get(DBConst.TraxId).toString().matches(TraxId))
                    {
                        PaymentDialogTimer.cancel();
                        ShowAvailabilityTimer.cancel();
                        CancelAllDialog();
                        CallDBForCreateAppointmentToTheDoctor();
                    }
                    else
                    {
                        Toast.makeText(activity,"TraxId Not Matched",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(activity,"Unsuccessful payment",Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(activity,"Unsuccessful payment",Toast.LENGTH_LONG).show();
            }

        }

        private void CallDBForCreateAppointmentToTheDoctor()
        {
            ShowLoadingDialog();
            HashMap<String,Object> hashMap1=new HashMap<>();
            hashMap1.put(DBConst.HospitalName,arrayList.get(ClickedPosition).get(DBConst.HospitalName).toString());
            hashMap1.put(DBConst.AppointmentTime,arrayList.get(ClickedPosition).get(DBConst.AppointmentSTime).toString());
            hashMap1.put(DBConst.AppointmentDate,String.valueOf(SelectedAppointmentDate)+"-"+String.valueOf(SelectedAppointmentMonth)+"-"+String.valueOf(SelectedAppointmentYear));
            hashMap1.put(DBConst.MonthWithYear,String.valueOf(SelectedAppointmentMonth)+"-"+String.valueOf(SelectedAppointmentYear));
            hashMap1.put(DBConst.OnlyYear,String.valueOf(SelectedAppointmentYear));
            hashMap1.put(DBConst.UID,PatientAccountInformationHashMap.get(DBConst.UID).toString());
            hashMap1.put(DBConst.Name,PatientAccountInformationHashMap.get(DBConst.Name).toString());
            hashMap1.put(DBConst.SerialNo,String.valueOf(SeatsAvailability));
            hashMap1.put(DBConst.TraxId,TraxId);
            hashMap1.put(DBConst.AppointmentFee,arrayList.get(ClickedPosition).get(DBConst.AppointmentFee).toString());
            doctorAccountDB.SaveCreateAppointmentToTheDoctorAccount(DoctorUID,hashMap1);

            HashMap<String,Object> hashMap2=new HashMap<>();
            hashMap2.put(DBConst.TraxId,TraxId);
            hashMap2.put(DBConst.UID,DoctorUID);
            hashMap2.put(DBConst.AppointmentTime,arrayList.get(ClickedPosition).get(DBConst.AppointmentSTime).toString()+"~"+arrayList.get(ClickedPosition).get(DBConst.AppointmentETime).toString());
            hashMap2.put(DBConst.SerialNo,String.valueOf(SeatsAvailability));
            hashMap2.put(DBConst.Name,DoctorAccountInformationArrayList.get(0).get(DBConst.Name));
            hashMap2.put(DBConst.HospitalName,arrayList.get(ClickedPosition).get(DBConst.HospitalName));
            hashMap2.put(DBConst.AppointmentDate,SelectedAppointmentDate+"-"+SelectedAppointmentMonth+"-"+SelectedAppointmentYear);
            patientAccountDB.SaveCreateAppointmentToThePatientAccount(hashMap2);

            HashMap<String,Object> hashMap3=new HashMap<>();
            hashMap3.put(DBConst.RESULT,DBConst.SUCCESSFUL);
            hashMap3.put(DBConst.UID,DoctorUID);
            hashMap3.put(DBConst.AppointmentDate,SelectedAppointmentDate+"-"+SelectedAppointmentMonth+"-"+SelectedAppointmentYear);
            hashMap3.put(DBConst.AppointmentFee,arrayList.get(ClickedPosition).get(DBConst.AppointmentFee).toString());
            AdminControlDBHelper adminControlDBHelper=new AdminControlDBHelper(activity);
            adminControlDBHelper.SaveBillingToTheAdminDB(SelectedAppointmentMonth+"-"+SelectedAppointmentYear,hashMap3);

         }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CancelAllDialog();
        CancelLoadingDialog();
    }

    public void CancelAllDialog()
    {
        if (UnavailabilityDialog!=null)
        {
            if (UnavailabilityDialog.isShowing())
            {
                UnavailabilityDialog.dismiss();
            }
        }

        if (ShowAvailabilityDialog!=null)
        {
            if (ShowAvailabilityDialog.isShowing())
            {
                ShowAvailabilityDialog.dismiss();
            }
        }

        if (PaymentDialog!=null)
        {
            if (PaymentDialog.isShowing())
            {
                PaymentDialog.dismiss();
            }
        }
    }
}

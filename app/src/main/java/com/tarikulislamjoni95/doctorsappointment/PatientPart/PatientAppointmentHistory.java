package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorAccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.PatientAccountDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PatientAppointmentHistory extends AppCompatActivity implements GetDataFromDBInterface
{
    private SetAppointmentListAdapter setAppointmentListAdapter;

    private ArrayList<HashMap<String,Object>> AppointmentHashMapArrayList;
    private HashMap<String,Object> PatientAccountInformationHashMap;
    private PatientAccountDB patientAccountDB;
    private InitializationUIHelperClass initializationUIHelperClass;
    private Activity activity;
    private RadioGroup radioGroup;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_appointment_history_list);
        Initialization();
        InitializationClass();
        InitializationUI();
        InitializationDB();
        CallDBForGetPatientAppointmentHistory();
    }

    private void Initialization()
    {
        PatientAccountInformationHashMap=new HashMap<>();
        AppointmentHashMapArrayList=new ArrayList<>();
        activity=PatientAppointmentHistory.this;
        PatientAccountInformationHashMap=(HashMap<String, Object>) getIntent().getSerializableExtra(DBConst.GetPatientAccountInformation);
    }
    private void InitializationClass()
    {
        initializationUIHelperClass=new InitializationUIHelperClass(getWindow().getDecorView());
    }

    private void InitializationUI()
    {
        radioGroup=findViewById(R.id.radio_group_0);
        recyclerView=findViewById(R.id.recycler_view_0);
        recyclerView.setLayoutManager(new GridLayoutManager(activity,1,RecyclerView.VERTICAL,false));
    }

    private void InitializationDB()
    {
        patientAccountDB=new PatientAccountDB(activity);
    }

    private void CallDBForGetPatientAppointmentHistory()
    {
        patientAccountDB.GetPatientAppointmentHistory(DBConst.SELF);
    }


    private void SetAppointmentList() {
        ArrayList<HashMap<String, Object>> ReverseArrayList = GetReverseAppointmentList(AppointmentHashMapArrayList);
        setAppointmentListAdapter = new SetAppointmentListAdapter(activity, ReverseArrayList);
        recyclerView.setAdapter(setAppointmentListAdapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                if (radioGroup.getCheckedRadioButtonId()==R.id.radio_button_0)
                {
                    ArrayList<HashMap<String, Object>> ReverseArrayList = new ArrayList<>();
                    ReverseArrayList = GetReverseAppointmentList(AppointmentHashMapArrayList);
                    setAppointmentListAdapter = new SetAppointmentListAdapter(activity, ReverseArrayList);
                    recyclerView.setAdapter(setAppointmentListAdapter);
                }
                else if (radioGroup.getCheckedRadioButtonId()==R.id.radio_button_1)
                {
                    ArrayList<HashMap<String, Object>> ReverseArrayList_1 = new ArrayList<>();
                    ReverseArrayList_1 = GetFutureAppointmentList(AppointmentHashMapArrayList);
                    setAppointmentListAdapter = new SetAppointmentListAdapter(activity, ReverseArrayList_1);
                    recyclerView.setAdapter(setAppointmentListAdapter);
                }
                else if (radioGroup.getCheckedRadioButtonId()==R.id.radio_button_2)
                {
                    ArrayList<HashMap<String, Object>> ReverseArrayList_2 = new ArrayList<>();
                    ReverseArrayList_2 = GetPassedAppointmentList(AppointmentHashMapArrayList);
                    setAppointmentListAdapter = new SetAppointmentListAdapter(activity, ReverseArrayList_2);
                    recyclerView.setAdapter(setAppointmentListAdapter);
                }
            }
        });
    }

    private ArrayList<HashMap<String, Object>> GetReverseAppointmentList(ArrayList<HashMap<String, Object>> appointmentHashMapArrayList)
    {
        ArrayList<HashMap<String,Object>> ReverseArrayList=new ArrayList<>();
        for(int i=appointmentHashMapArrayList.size()-1; i>=0; i--)
        {
            ReverseArrayList.add(appointmentHashMapArrayList.get(i));
        }
        return ReverseArrayList;
    }
    private ArrayList<HashMap<String, Object>> GetPassedAppointmentList(ArrayList<HashMap<String, Object>> appointmentHashMapArrayList)
    {
        ArrayList<HashMap<String,Object>> arrayList=new ArrayList<>();
        for (int i=appointmentHashMapArrayList.size()-1; i>=0; i--)
        {
            String[] AppointmentDate=(appointmentHashMapArrayList.get(i).get(DBConst.AppointmentDate)).toString().split("-",0);
            Date date_1=new Date(Integer.parseInt(AppointmentDate[2]),Integer.parseInt(AppointmentDate[1])-1,Integer.parseInt(AppointmentDate[0]));
            Date date_2=new Date(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DATE));
            if (date_1.before(date_2))
            {
                arrayList.add(appointmentHashMapArrayList.get(i));
            }
        }
        return arrayList;
    }

    private ArrayList<HashMap<String, Object>> GetFutureAppointmentList(ArrayList<HashMap<String, Object>> appointmentHashMapArrayList)
    {
        ArrayList<HashMap<String,Object>> arrayList=new ArrayList<>();
        for (int i=appointmentHashMapArrayList.size()-1; i>=0; i--)
        {
            String[] AppointmentDate=(appointmentHashMapArrayList.get(i).get(DBConst.AppointmentDate)).toString().split("-",0);
            Date date_1=new Date(Integer.parseInt(AppointmentDate[2]),Integer.parseInt(AppointmentDate[1])-1,Integer.parseInt(AppointmentDate[0]));
            Date date_2=new Date(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DATE));
            if (date_1.after(date_2))
            {
                arrayList.add(appointmentHashMapArrayList.get(i));
            }
        }
        return arrayList;
    }

    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        switch (WhichDB)
        {
            case DBConst.GetPatientAppointmentHistoryFromPatientAccount:
                ParseAndLoadDataIntoRecyclerView(DataHashMap);
                break;
            case DBConst.GetRatingsAndReviewInfoFromDoctorAccountDB:
                DataParseForRatingAndReview(DataHashMap);
                break;
            case DBConst.GetStatusOnSaveReviewAndRatingIntoToDoctorAccountDB:
                CloseReviewDialog();
                break;
        }
    }

    private void CloseReviewDialog()
    {
        if (setAppointmentListAdapter.ShowWritingDialog!=null)
        {
            if (setAppointmentListAdapter.ShowWritingDialog.isShowing())
            {
                setAppointmentListAdapter.ShowWritingDialog.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CloseReviewDialog();
    }

    private void ParseAndLoadDataIntoRecyclerView(HashMap<String,Object> objectHashMap)
    {
        if (objectHashMap.containsKey(DBConst.RESULT))
        {
            if (objectHashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
            {
                DataSnapshot dataSnapshot=(DataSnapshot)objectHashMap.get(DBConst.DataSnapshot);

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    HashMap<String,Object> objectHashMap1=new HashMap<>();
                    for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                    {
                        objectHashMap1.put(dataSnapshot2.getKey(),dataSnapshot2.getValue());
                    }
                    AppointmentHashMapArrayList.add(objectHashMap1);
                }
            }
            else
            {
                AppointmentHashMapArrayList=new ArrayList<>();
            }
        }
        else
        {
            AppointmentHashMapArrayList=new ArrayList<>();
        }

        SetAppointmentList();
    }

    private void DataParseForRatingAndReview(HashMap<String,Object> hashMap )
    {
        DataParseForRatingAndReview dataParseForRatingAndReview=new DataParseForRatingAndReview(hashMap);
        int TotalRating=dataParseForRatingAndReview.getTotalRatings();
        int TotalReviewer=dataParseForRatingAndReview.getTotalReviewer();
        ArrayList<HashMap<String,Object>> arrayList=dataParseForRatingAndReview.getArrayList();

        int oldRating=0;
        boolean Check=false;
        ArrayList<String> OldRating=GetOldRating(arrayList);
        try
        {
            oldRating=Integer.parseInt(OldRating.get(0));
            if ((((int)Integer.parseInt(OldRating.get(1)))==1))
            {
                Check=true;
            }
        }
        catch (Exception e)
        {
        }
        setAppointmentListAdapter.CallDBForSaveReviewAndRating(TotalRating,TotalReviewer,oldRating,Check);
    }

    private ArrayList<String> GetOldRating(ArrayList<HashMap<String, Object>> arrayList)
    {
        ArrayList<String> arrayList1=new ArrayList<>();
        String Check="0";
        String old_rating="0";
        for (int i=0; i<arrayList.size(); i++)
        {
            HashMap<String,Object> hashMap=arrayList.get(i);

            if (hashMap.containsKey(DBConst.UID))
            {
                if (hashMap.get(DBConst.UID).toString().matches(PatientAccountInformationHashMap.get(DBConst.UID).toString()))
                {
                    old_rating=hashMap.get(DBConst.Ratings).toString();
                    Check="1";
                }
            }

        }
        arrayList1.add(old_rating);
        arrayList1.add(Check);
        return arrayList1;
    }

    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList)
    {

    }



    class SetAppointmentListAdapter extends RecyclerView.Adapter<SetAppointmentListAdapter.ViewHolder>
    {
        private Activity activity;
        private ArrayList<HashMap<String,Object>> AppointmentHashMapArrayList;
        public SetAppointmentListAdapter(Activity activity,ArrayList<HashMap<String,Object>> AppointmentHashMapArrayList)
        {
            this.activity=activity;
            this.AppointmentHashMapArrayList=AppointmentHashMapArrayList;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.patient_my_show_appointment_model,parent,false));
        }

        public int ClickedPosition=0;
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
        {
            HashMap<String,Object> objectHashMap=new HashMap<>();
            objectHashMap=(HashMap<String, Object>) AppointmentHashMapArrayList.get(position);
            String[] DataKey={DBConst.HospitalName,DBConst.Name,DBConst.AppointmentDate,DBConst.TraxId,DBConst.SerialNo};
            for (int i=0; i<DataKey.length; i++)
            {
                if (objectHashMap.containsKey(DataKey[i]))
                {
                    holder.textViews[i].setText(DataKey[i]+" : "+objectHashMap.get(DataKey[i]));
                }
                else
                {
                    holder.textViews[i].setText(DataKey[i]+" : "+DBConst.UNKNOWN);
                }
            }

            if (CheckAppointmentDateExperidity(objectHashMap.get(DBConst.AppointmentDate).toString()))
            {
                holder.button.setVisibility(View.VISIBLE);
                holder.linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_0));
            }
            else
            {
                holder.linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_1));
                holder.button.setVisibility(View.GONE);
            }

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    ClickedPosition=position;
                    ShowWritingReviewAndRating();
                }
            });
        }

        private boolean CheckAppointmentDateExperidity(String AppointmentDate)
        {
            Calendar calendar=Calendar.getInstance();
            int day=calendar.get(Calendar.DATE);
            int month=calendar.get(Calendar.MONTH);
            int year=calendar.get(Calendar.YEAR);
            Date date_1=new Date(year,month,day);
            String[] DateStringArray=AppointmentDate.split("-",0);
            Date date_2=new Date(Integer.parseInt(DateStringArray[2]),Integer.parseInt(DateStringArray[1])-1,Integer.parseInt(DateStringArray[0]));
            //Log.d("myError",date.toString()+"\n"+DateStringArray[0]+DateStringArray[1]+DateStringArray[2]+"\n"+new Date().toString()+"\n"+String.valueOf(date.before(new Date()))+String.valueOf(date.after(new Date())));
            return date_1.after(date_2);
        }

        private boolean[] Check=new boolean[5];
        public AlertDialog ShowWritingDialog;
        private int Ratings=0;
        private String Review="";
        private ImageView[] RatingImage;
        private void ShowWritingReviewAndRating()
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(activity);
            View view=LayoutInflater.from(activity).inflate(R.layout.patient_my_show_appointment_model,null,false);
            LinearLayout[] linearLayouts;
            TextView[] textViews;
            Button button;
            final EditText editText;
            int[] text_view_id={R.id.text_view_0,R.id.text_view_1,R.id.text_view_2,R.id.text_view_3,R.id.text_view_4};
            int[] linear_layout_id={R.id.linear_layout_0,R.id.linear_layout_1};
            int[] image_view_id={R.id.image_view_0,R.id.image_view_1,R.id.image_view_2,R.id.image_view_3,R.id.image_view_4};
            editText=view.findViewById(R.id.edit_text_0);
            RatingImage=new InitializationUIHelperClass(view).setImageViews(image_view_id);
            linearLayouts=new InitializationUIHelperClass(view).setLinearLayout(linear_layout_id);
            textViews=new InitializationUIHelperClass(view).setTextViews(text_view_id);
            button=view.findViewById(R.id.button_0);
            button.setText("Save");
            linearLayouts[0].setVisibility(View.VISIBLE);
            linearLayouts[1].setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            textViews[0].setText("Hospital Name : "+AppointmentHashMapArrayList.get(ClickedPosition).get(DBConst.HospitalName).toString());
            textViews[1].setText("Doctor Name : "+AppointmentHashMapArrayList.get(ClickedPosition).get(DBConst.Name).toString());
            textViews[2].setText("Appointment Date : "+AppointmentHashMapArrayList.get(ClickedPosition).get(DBConst.AppointmentDate).toString());
            textViews[3].setText("Appointment Time : "+AppointmentHashMapArrayList.get(ClickedPosition).get(DBConst.AppointmentTime).toString());
            textViews[4].setText("Payment TraxID : "+AppointmentHashMapArrayList.get(ClickedPosition).get(DBConst.TraxId).toString());

            for(int i=0; i<5; i++)
            {
                Check[i]=true;
            }

            RatingImage[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Ratings=SetImageRating(0);
                }
            });
            RatingImage[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Ratings=SetImageRating(1);

                }
            });
            RatingImage[2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Ratings=SetImageRating(2);
                }
            });
            RatingImage[3].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Ratings=SetImageRating(3);
                }
            });
            RatingImage[4].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Ratings=SetImageRating(4);
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Review=editText.getText().toString();
                    CallDBForGetReviewAndRating();
                }
            });

            builder.setView(view);
            ShowWritingDialog=builder.create();
            ShowWritingDialog.show();

        }

        private int SetImageRating(int WhichButtonCliked)
        {
            int Rating=0;
            if (Check[WhichButtonCliked])
            {
                for(int i=0; i<5; i++)
                {
                    if (i<=WhichButtonCliked)
                    {
                        Rating++;
                        Check[i]=false;
                        RatingImage[i].setImageDrawable(AppCompatResources.getDrawable(activity,android.R.drawable.star_big_on));
                    }
                    else
                    {
                        Check[i]=true;
                        RatingImage[i].setImageDrawable(AppCompatResources.getDrawable(activity,android.R.drawable.star_big_off));
                    }
                }
            }
            else
            {
                for(int i=0; i<5; i++)
                {
                    if (i>=WhichButtonCliked)
                    {
                        Check[i]=true;
                        RatingImage[i].setImageDrawable(AppCompatResources.getDrawable(activity,android.R.drawable.star_big_off));
                    }
                    else
                    {
                        Rating++;
                        Check[i]=false;
                        RatingImage[i].setImageDrawable(AppCompatResources.getDrawable(activity,android.R.drawable.star_big_on));
                    }
                }
            }
            return Rating;
        }

        private void CallDBForGetReviewAndRating()
        {
            DoctorAccountDB doctorAccountDB=new DoctorAccountDB(activity);
            doctorAccountDB.GetDoctorReviewAndRatingFromDoctorAccountDB(AppointmentHashMapArrayList.get(ClickedPosition).get(DBConst.UID).toString());
        }

        public void CallDBForSaveReviewAndRating(int TotalRating,int TotalReviewer,int OldRating,boolean OldRatingCheck)
        {
            if (!OldRatingCheck)
            {
                TotalReviewer=TotalReviewer+1;
            }

            if (OldRating>Ratings)
            {
                TotalRating=TotalRating-(OldRating-Ratings);
            }
            else
            {
                TotalRating=TotalRating+(Ratings-OldRating);
            }
            HashMap<String,Object> objectHashMap=new HashMap<>();
            objectHashMap.put(DBConst.Ratings,String.valueOf(Ratings));
            objectHashMap.put(DBConst.Reviews,Review);
            objectHashMap.put(DBConst.Name,PatientAccountInformationHashMap.get(DBConst.Name));
            objectHashMap.put(DBConst.ProfileImageUrl,PatientAccountInformationHashMap.get(DBConst.ProfileImageUrl));
            DoctorAccountDB doctorAccountDB=new DoctorAccountDB(activity);
            doctorAccountDB.SaveReviewAndRatingInToDoctorAccountDB(AppointmentHashMapArrayList.get(ClickedPosition).get(DBConst.UID).toString(),TotalRating,TotalReviewer,PatientAccountInformationHashMap.get(DBConst.UID).toString(),objectHashMap);
        }

        @Override
        public int getItemCount() {
            return AppointmentHashMapArrayList.size();
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            private LinearLayout linearLayout;
            private TextView[] textViews;
            private Button button;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                int[] text_view_id={R.id.text_view_0,R.id.text_view_1,R.id.text_view_2,R.id.text_view_3,R.id.text_view_4};
                textViews=new InitializationUIHelperClass(itemView).setTextViews(text_view_id);
                button=itemView.findViewById(R.id.button_0);
                linearLayout=itemView.findViewById(R.id.linear_layout_0);
            }
        }
    }
}

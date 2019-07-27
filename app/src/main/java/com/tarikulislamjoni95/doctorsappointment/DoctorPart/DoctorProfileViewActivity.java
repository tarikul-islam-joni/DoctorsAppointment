package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.PatientListActivity;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfileViewActivity extends AppCompatActivity implements View.OnClickListener
{
    private MyLoadingDailog myLoadingDailog;
    private AppointmentDataAdapter adapter;

    private Activity activity;
    private Intent intent;
    private ArrayList<AppointmentDataModel> arrayList;
    private AlertDialog dialog;

    private String UID;
    private ArrayList<String> UserInformation;

    private CircleImageView ImageCiv;
    private TextView NameTv,StudiedTv,BMDCRegTv,NoOfYearPracTv,SpecialityTV,DegreeCompletedTv,AvailableAreaTv,ContactNoTv;
    private ListView AppointmentListView;
    private Button EditProfileBtn,EditAppointmentInfoBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_view);
        Initialization();
        InitializationUI();
        InitializationClass();
        AppointmentListView.setAdapter(adapter);
        LoadUserData();
        ShowAppointmentInfo();
    }

    private void Initialization()
    {
        activity=DoctorProfileViewActivity.this;
        arrayList=new ArrayList<>();
        UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserInformation=new ArrayList<>();
        UserInformation=getIntent().getStringArrayListExtra(DBConst.Doctor);
    }
    private void InitializationUI()
    {
        ImageCiv=findViewById(R.id.image_civ);
        NameTv=findViewById(R.id.name_tv);
        StudiedTv=findViewById(R.id.studied_tv);
        BMDCRegTv=findViewById(R.id.bmdc_reg_tv);
        NoOfYearPracTv=findViewById(R.id.no_of_prac_tv);
        SpecialityTV=findViewById(R.id.speciality_tv);
        DegreeCompletedTv=findViewById(R.id.degree_tv);
        ContactNoTv=findViewById(R.id.contact_no_tv);
        AvailableAreaTv=findViewById(R.id.available_area_tv);

        EditProfileBtn=findViewById(R.id.edit_profile_btn);
        EditProfileBtn.setOnClickListener(this);
        EditAppointmentInfoBtn=findViewById(R.id.edit_appointment_info_btn);
        EditAppointmentInfoBtn.setOnClickListener(this);

        AppointmentListView=findViewById(R.id.list_view);
    }
    private void InitializationClass()
    {
        myLoadingDailog=new MyLoadingDailog(activity,R.drawable.spinner);
        adapter=new AppointmentDataAdapter("Show",activity,arrayList);
    }

    private void LoadUserData()
    {
        if (!UserInformation.get(0).matches("null"))
        {
            Picasso.get().load(UserInformation.get(0)).into(ImageCiv);
        }
        NameTv.setText("Name : "+UserInformation.get(1)+" "+UserInformation.get(2));
        DegreeCompletedTv.setText("Completed Degree : "+UserInformation.get(3));
        BMDCRegTv.setText("BMDC Registration Number : "+UserInformation.get(4));
        StudiedTv.setText("Studied At "+UserInformation.get(5));
        NoOfYearPracTv.setText("No Of Year Practicing : "+UserInformation.get(6));
        SpecialityTV.setText("Speciality : "+UserInformation.get(7));
        AvailableAreaTv.setText("Available Area : "+UserInformation.get(8));
        ContactNoTv.setText("Contact Number : "+UserInformation.get(9));
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.edit_appointment_info_btn:
                if (UserInformation.get(4).matches("null"))
                {
                    ShowDialogForBMDCRegNo();
                }
                else
                {
                    intent=new Intent(activity,EditDoctorAppointmentInfoActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.edit_profile_btn:
                intent=new Intent(activity, EditDoctorProfileActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void ShowDialogForBMDCRegNo()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("BMDC Registration number missing !");
        builder.setMessage("You haven't added bangladesh medical and dental council registration number yet." +
                "Please add that information to get online appointment feature.");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                intent=new Intent(activity,EditDoctorSecureInfo.class);
                startActivity(intent);
            }
        });

        dialog=builder.create();
        dialog.show();
    }
    private void ShowAppointmentInfo()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child(DBConst.AppointmentShedule).child(UID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                arrayList.clear();
                if (dataSnapshot.exists())
                {
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
                adapter.notifyDataSetChanged();
                if (myLoadingDailog.isShowing())
                {
                    myLoadingDailog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                if (myLoadingDailog.isShowing())
                {
                    myLoadingDailog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myLoadingDailog.isShowing())
        {
            myLoadingDailog.dismiss();
        }
    }
}

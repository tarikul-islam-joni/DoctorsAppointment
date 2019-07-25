package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfileViewActivity extends AppCompatActivity implements View.OnClickListener
{
    private MyLoadingDailog myLoadingDailog;
    private AppointmentDataAdapter adapter;

    private Activity activity;
    private Intent intent;
    private ArrayList<AppointmentDataModel> arrayList;

    private CircleImageView ImageCiv;
    private TextView NameTv,StudiedTv,BMDCRegTv,NoOfYearPracTv,SpecialityTV,DegreeCompletedTv,AvailableAreaTv;
    private ListView AppointmentListView;
    private Button CheckPatientListBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_view);
        Initialization();
        InitializationUI();
        InitializationClass();
        AppointmentListView.setAdapter(adapter);
        ShowData();
    }

    private void Initialization()
    {
        activity=DoctorProfileViewActivity.this;
        arrayList=new ArrayList<>();
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
        AvailableAreaTv=findViewById(R.id.available_area_tv);
        CheckPatientListBtn=findViewById(R.id.check_patient_list);
        CheckPatientListBtn.setOnClickListener(this);
        AppointmentListView=findViewById(R.id.list_view);
    }
    private void InitializationClass()
    {
        myLoadingDailog=new MyLoadingDailog(activity,R.drawable.spinner);
        adapter=new AppointmentDataAdapter("Show",activity,arrayList);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.change_appointment_info:
                intent=new Intent(activity,DoctorProfileEditorTwoActivity.class);
                startActivity(intent);
                break;
            case R.id.check_patient_list:
                intent=new Intent(activity,PatientListActivity.class);
                startActivity(intent);
                break;
            case R.id.edit_btn:
                intent=new Intent(activity,DoctorProfileEditorOneActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void ShowData()
    {
        myLoadingDailog.show();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Doctor).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                NameTv.setText(dataSnapshot.child(DBConst.Name).getValue().toString());
                StudiedTv.setText(dataSnapshot.child(DBConst.StudiedCollege).getValue().toString());
                BMDCRegTv.setText(dataSnapshot.child(DBConst.BMDCRegNo).getValue().toString());
                NoOfYearPracTv.setText(dataSnapshot.child(DBConst.NoOfPracYear).getValue().toString());
                SpecialityTV.setText(dataSnapshot.child(DBConst.Category).getValue().toString());
                DegreeCompletedTv.setText(dataSnapshot.child(DBConst.Degree).getValue().toString());
                AvailableAreaTv.setText(dataSnapshot.child(DBConst.AvailableArea).getValue().toString());
                if (!dataSnapshot.child(DBConst.Image).getValue().toString().matches("null"))
                {
                    Picasso.get().load(dataSnapshot.child(DBConst.Image).getValue().toString()).into(ImageCiv);
                }
                ShowAppointmentInfo();
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
    private void ShowAppointmentInfo()
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doctor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.change_appointment_info:
                intent=new Intent(activity,DoctorProfileEditorTwoActivity.class);
                startActivity(intent);
                break;
            case R.id.edit_btn:
                intent=new Intent(activity,DoctorProfileEditorOneActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_out_btn:
                FirebaseAuth.getInstance().signOut();
                intent=new Intent(activity,SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

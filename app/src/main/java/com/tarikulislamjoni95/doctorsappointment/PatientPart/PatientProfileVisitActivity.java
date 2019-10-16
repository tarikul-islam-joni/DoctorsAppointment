package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.PatientAccountDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientProfileVisitActivity extends AppCompatActivity implements GetDataFromDBInterface
{
    private PatientAccountDB patientAccountDB;

    private String BackActivity;
    private String PatientUID;

    private Activity activity;

    private TextView[] textViews;
    private CircleImageView circleImageView;
    private Button[] buttons;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_profile_visit);
        Initialization();
        InitializationClass();
        InitializationUI();
        InitializationDB();
        CallDBForPatientAccountData();
    }
    private void Initialization()
    {
        activity= PatientProfileVisitActivity.this;
        PatientUID=getIntent().getExtras().getString(DBConst.UID);
        BackActivity=getIntent().getExtras().getString(DBConst.BackActivity);
    }

    private void InitializationClass()
    {

    }
    private void InitializationUI()
    {
        textViews= new InitializationUIHelperClass(getWindow().getDecorView()).setTextViews(new int[]{R.id.text_view_0,R.id.text_view_1,R.id.text_view_2,R.id.text_view_3,R.id.text_view_4,R.id.text_view_5,R.id.text_view_6,R.id.text_view_7,R.id.text_view_8,R.id.text_view_9,R.id.text_view_10});
        buttons=new InitializationUIHelperClass(getWindow().getDecorView()).setButtons(new int[]{R.id.button_0,R.id.button_1});
        circleImageView=findViewById(R.id.image_civ_0);

        if (BackActivity.matches(DBConst.Patient))
        {
            buttons[1].setVisibility(View.GONE);
        }
        if (BackActivity.matches(DBConst.Doctor))
        {
            buttons[1].setVisibility(View.VISIBLE);
        }

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(activity,StoreMedicalDocumentActivity.class);
                intent.putExtra(DBConst.BackActivity,DBConst.Doctor);
                intent.putExtra(DBConst.UID,PatientUID);
                startActivity(intent);
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String phone = "+88"+textViews[9].getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });
    }

    private void GetPatientAccountInformation(HashMap<String,Object> hashMap)
    {
        if (hashMap.containsKey(DBConst.RESULT))
        {
            if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.DATA_EXIST))
            {
                String[] DataKey={DBConst.Name,DBConst.Name,DBConst.FatherName,DBConst.MotherName,DBConst.DateOfBirth,DBConst.Gender,DBConst.Height,DBConst.Weight,DBConst.BloodGroup,DBConst.PhoneNumber,DBConst.Address};
                for(int i=0; i<DataKey.length; i++)
                {
                    if (hashMap.containsKey(DataKey[i]))
                    {
                        textViews[i].setText(hashMap.get(DataKey[i]).toString());
                    }
                }
                if (hashMap.containsKey(DBConst.ProfileImageUrl))
                {
                    if (!hashMap.get(DBConst.ProfileImageUrl).toString().matches(DBConst.UNKNOWN))
                    {
                        Picasso.get().load(hashMap.get(DBConst.ProfileImageUrl).toString()).into(circleImageView);
                    }
                }
            }
        }
    }


    private void InitializationDB()
    {
        patientAccountDB=new PatientAccountDB(activity);
    }
    private void CallDBForPatientAccountData()
    {
        patientAccountDB.GetPatientAccountInformation(PatientUID);
    }

    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        Log.d("PatientProfileVisit",WhichDB+" : "+DataHashMap.toString());
        switch (WhichDB)
        {
            case DBConst.GetPatientAccountInformation:
                GetPatientAccountInformation(DataHashMap);
                break;
        }
    }

    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList) {

    }
}

package com.tarikulislamjoni95.doctorsappointment.AdminPart;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AdminControlDBHelper;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorAccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.PatientAccountDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminControlActivity extends AppCompatActivity implements GetDataFromDBInterface, View.OnClickListener
{
    private String BackActivity;
    private String Message;
    private ArrayList<String> arrayList;

    private String UID;
    private HashMap<String,Object> AccountDataHashMap;

    private TextView[] textViews;
    private ListView listView;
    private ImageView[] imageViews;
    private Button[] buttons;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_control_activity);
        BackActivity=getIntent().getExtras().getString(DBConst.BackActivity);
        Message=getIntent().getExtras().getString(DBConst.RESULT);
        arrayList=(ArrayList<String>) getIntent().getSerializableExtra(DBConst.DataSnapshot);

        AccountDataHashMap=new HashMap<>();
        InitializationUIHelperClass initializationUIHelperClass=new InitializationUIHelperClass(getWindow().getDecorView());
        textViews=initializationUIHelperClass.setTextViews(new int[]{R.id.text_view_0,R.id.text_view_1});
        imageViews=initializationUIHelperClass.setImageViews(new int[]{R.id.image_view_0,R.id.image_view_1,R.id.image_view_2});
        buttons=initializationUIHelperClass.setButtons(new int[]{R.id.button_0,R.id.button_1,R.id.button_2,R.id.button_3});
        listView=findViewById(R.id.list_view_0);
        textViews[0].setText(Message.toString());
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(AdminControlActivity.this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                UID=arrayList.get(i);
                CallDBForAccountInformation(BackActivity,arrayList.get(i));
                for(int k=0; k<buttons.length; k++)
                {
                    buttons[k].setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void SetDataToTheField(HashMap<String,Object> hashMap)
    {
        AccountDataHashMap=hashMap;
        textViews[1].setText(hashMap.toString());
        if (hashMap.containsKey(DBConst.RESULT))
        {
            if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.DATA_EXIST))
            {
                if (BackActivity.matches(DBConst.Patient))
                {
                    buttons[0].setVisibility(View.GONE);
                    buttons[1].setVisibility(View.GONE);
                    buttons[2].setVisibility(View.VISIBLE);
                    buttons[3].setVisibility(View.VISIBLE);
                }
                else
                {
                    buttons[0].setVisibility(View.VISIBLE);
                    buttons[1].setVisibility(View.VISIBLE);
                    buttons[2].setVisibility(View.VISIBLE);
                    buttons[3].setVisibility(View.VISIBLE);
                }
            }

            if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.DATA_NOT_EXIST))
            {
                for(int i=0; i<buttons.length; i++)
                {
                    buttons[i].setVisibility(View.GONE);
                }
            }
        }
        if (BackActivity.matches(DBConst.Patient))
        {
            SetImageViews(hashMap,DBConst.ProfileImageUrl,0);
            SetImageViews(hashMap,DBConst.BirthCertificateImageUrl,1);
            SetImageViews(hashMap,DBConst.AnotherDocumentImageUrl,2);
        }
        else if (BackActivity.matches(DBConst.Doctor))
        {
            SetImageViews(hashMap,DBConst.ProfileImageUrl,0);
            SetImageViews(hashMap,DBConst.NIDImageUrl,1);
            SetImageViews(hashMap,DBConst.BMDCRegImageUrl,2);
        }

        for (int i=0; i<buttons.length; i++)
        {
            buttons[i].setOnClickListener(this);
        }
    }

    private void SetImageViews(HashMap<String,Object> hashMap,String WhichKey,int WhichImageView)
    {
        if (hashMap.containsKey(WhichKey))
        {
            if (!hashMap.get(WhichKey).toString().matches(DBConst.UNKNOWN))
            {
                Picasso.get().load(hashMap.get(WhichKey).toString()).into(imageViews[WhichImageView]);
            }
        }
    }

    private void CallDBForAccountInformation(String backActivity, String UID)
    {
        if (backActivity.matches(DBConst.Patient))
        {
            PatientAccountDB patientAccountDB=new PatientAccountDB(AdminControlActivity.this);
            patientAccountDB.GetPatientAccountInformation(UID);
        }
        else if (backActivity.matches(DBConst.Doctor))
        {
            DoctorAccountDB doctorAccountDB=new DoctorAccountDB(AdminControlActivity.this);
            doctorAccountDB.GetDoctorAccountInformation(UID);
        }
    }

    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        switch (WhichDB)
        {
            case DBConst.GetPatientAccountInformation:
                SetDataToTheField(DataHashMap);
                break;
            case DBConst.GetDoctorAccountInformation:
                SetDataToTheField(DataHashMap);
                break;
        }
    }

    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList)
    {

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_0:
                VerifyTheDoctor();
                ButtonEffect(0);
                break;
            case R.id.button_1:
                UnverifyTheDoctor();
                ButtonEffect(1);
                break;
            case R.id.button_2:
                LockTheAccount();
                ButtonEffect(2);
                break;
            case R.id.button_3:
                UnLockTheAccount();
                ButtonEffect(3);
                break;
        }
    }

    private void ButtonEffect(int WhichButtonCliked)
    {
        for (int i=0; i<buttons.length; i++)
        {
            if (i==WhichButtonCliked)
            {
                buttons[i].setBackground(ContextCompat.getDrawable(this,R.drawable.button_background_2));
            }
            else
            {
                buttons[i].setBackground(ContextCompat.getDrawable(this,R.drawable.button_background_1));
            }
        }
    }


    private void VerifyTheDoctor()
    {
        AdminControlDBHelper adminControlDBHelper=new AdminControlDBHelper(AdminControlActivity.this);
        adminControlDBHelper.AuthorityValueChangeOfTheDoctorAccountInformation(UID,DBConst.VERIFIED,DBConst.SaveDoctorAccountInformation);
        adminControlDBHelper.AccountMultiplicityChangeOfTheDoctorAccount(DBConst.Doctor,AccountDataHashMap.get(DBConst.BMDCRegNumber).toString(),UID,DBConst.VERIFIED,DBConst.SaveAccountMultiplicityDB);

    }

    private void UnverifyTheDoctor()
    {
        AdminControlDBHelper adminControlDBHelper=new AdminControlDBHelper(AdminControlActivity.this);
        adminControlDBHelper.AuthorityValueChangeOfTheDoctorAccountInformation(UID,DBConst.UNVERIFIED,DBConst.SaveDoctorAccountInformation);
        adminControlDBHelper.AccountMultiplicityChangeOfTheDoctorAccount(DBConst.Doctor,AccountDataHashMap.get(DBConst.BMDCRegNumber).toString(),UID,DBConst.UNVERIFIED,DBConst.SaveAccountMultiplicityDB);
    }

    private void LockTheAccount()
    {
        AdminControlDBHelper adminControlDBHelper=new AdminControlDBHelper(AdminControlActivity.this);
        adminControlDBHelper.LockOrUnlockAccountUID(UID,true,DBConst.GetLockedDoctorAccountListFromDB);
    }

    private void UnLockTheAccount()
    {
        AdminControlDBHelper adminControlDBHelper=new AdminControlDBHelper(AdminControlActivity.this);
        adminControlDBHelper.LockOrUnlockAccountUID(UID,false,DBConst.GetLockedDoctorAccountListFromDB);
    }
}

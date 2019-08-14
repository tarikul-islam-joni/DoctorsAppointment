package com.tarikulislamjoni95.doctorsappointment.AccountPart;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tarikulislamjoni95.doctorsappointment.AdminPart.AdminMainActivity;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDM;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBHelper;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.DoctorMainActivity;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.EditDoctorProfileActivity;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.EditDoctorSecureInfo;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountStatusDBInterface;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.EditPatientProfileActivity;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.EditPatientSecureInfoActivity;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.PatientMainActivity;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity implements AccountStatusDBInterface {
    private AccountStatusDB accountStatusDB;

    private Intent intent;
    private Activity activity;

    private MyLoadingDailog myLoadingDailog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Initialization();
        InitializationUI();
        InitializationClass();
        InitializationDB();
        CheckLoggedInOrNot();
    }

    private void Initialization()
    {
        activity=WelcomeActivity.this;
    }
    private void InitializationUI()
    {

    }
    private void InitializationClass()
    {
        myLoadingDailog=new MyLoadingDailog(WelcomeActivity.this,R.drawable.spinner);
    }
    private void CheckLoggedInOrNot()
    {
        if (!myLoadingDailog.isShowing())
        {
            myLoadingDailog.show();
        }
        //Call Database For Account Status
        CallDBForAccountStatus();
    }
    private void GetAccountValidityStatus(boolean GetResult,String AccountType,boolean AccountCompletion,boolean AccountValidity)
    {
        if (GetResult==true)
        {
            CheckAccountValidityAndCompletion(AccountType,AccountCompletion,AccountValidity);
        }
        else
        {
            GotoSignInActivity();
        }
    }
    private void CheckAccountValidityAndCompletion(String AccountType,boolean AccountCompletion,boolean AccountValidity)
    {
        if (AccountType.matches(DBConst.Patient))
        {
            if (!AccountCompletion)
            {
                intent=new Intent(activity, EditPatientProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else if (!AccountValidity)
            {
                intent=new Intent(activity, EditPatientSecureInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else
            {
                intent=new Intent(activity, PatientMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
        else if (AccountType.matches(DBConst.Doctor))
        {
            if (!AccountCompletion)
            {
                intent=new Intent(WelcomeActivity.this, EditDoctorProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else if (!AccountValidity)
            {
                intent=new Intent(activity, EditDoctorSecureInfo.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else
            {
                intent=new Intent(activity,DoctorMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
        else if (AccountType.matches("Admin"))
        {
            intent=new Intent(activity, AdminMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
    private void GotoSignInActivity()
    {
        intent=new Intent(activity, EntranceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        DelayStartingActivityMethod(intent);
    }
    private void DelayStartingActivityMethod(final Intent intent)
    {
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (myLoadingDailog.isShowing())
                {
                    myLoadingDailog.dismiss();
                }
                startActivity(intent);
            }
        },2000);
    }

    @Override
    protected void onDestroy() {
        if (myLoadingDailog.isShowing() ) {
            myLoadingDailog.dismiss();
        }
        super.onDestroy();
    }






    ///***********************************************************************///
    ///************************Database Part**********************************///
    ///**********************************************************************///

    private void InitializationDB()
    {
        accountStatusDB=new AccountStatusDB(activity);
    }


    ///************************Database Call**********************************///
    private void CallDBForAccountStatus()
    {
        accountStatusDB.GetUIDAccountStatusData();
    }
    ///**********************Interface Implementation*************************///
    @Override
    public void GetAccountStatus(boolean result, ArrayList<AccountStatusDM> arrayList)
    {
        GetAccountValidityStatus(result,arrayList.get(0).getAccountType(),arrayList.get(0).isAccountCompletion(),arrayList.get(0).isAccountValidity());
    }
    @Override
    public void AccountStatusSavingResult(boolean result) {

    }
}

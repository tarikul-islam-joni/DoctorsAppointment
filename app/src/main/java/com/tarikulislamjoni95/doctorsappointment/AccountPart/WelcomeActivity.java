package com.tarikulislamjoni95.doctorsappointment.AccountPart;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.tarikulislamjoni95.doctorsappointment.AdminPart.AdminMainActivity;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBHelper;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.DoctorMainActivity;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.EditDoctorProfileActivity;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.EditDoctorSecureInfoActivity;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyPermissionClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyPermissionGroup;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.EditPatientProfileActivity;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.EditPatientSecureInfoActivity;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.PatientMainActivity;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.HashMap;

public class WelcomeActivity extends AppCompatActivity implements GetDataFromDBInterface {
    private AccountStatusDB accountStatusDB;
    private DBHelper dbHelper;

    private Intent intent;
    private Activity activity;

    private AlertDialog AccountStatusDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_welcome_layout);

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
        activity=WelcomeActivity.this;
    }

    private void InitializationClass()
    {
        //myLoadingDailog=new MyLoadingDailog(WelcomeActivity.this,R.drawable.spinner);
    }

    private void CheckLoggedInOrNot()
    {

        //Call Database For Account UID
        CallDBForAccountUID();
    }

    private void GetAccountUIDFromDB(HashMap<String,Object> DataHashMap)
    {
        //Call Database For Account Status
        String GetResult=(String)DataHashMap.get(DBConst.RESULT);
        if (GetResult.matches(DBConst.NOT_NULL_USER))
        {
            CallDBForAccountStatus((String)DataHashMap.get(DBConst.UID));
        }
        else
        {
            GotoSignInActivity();
        }
    }


    private void GetAccountValidityStatus(HashMap<String,Object> DataHashMap)
    {
        String GetResult=(String)DataHashMap.get(DBConst.RESULT);
        if (GetResult.matches(DBConst.DATA_EXIST))
        {
            String AccountType=(String)DataHashMap.get(DBConst.AccountType);
            if (!(boolean)DataHashMap.get(DBConst.AccountLockState))
            {
                if (AccountType.matches(DBConst.Patient))
                {
                    if (!(boolean)DataHashMap.get(DBConst.AccountCompletion))
                    {
                        intent=new Intent(activity, EditPatientProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else if (!(boolean)DataHashMap.get(DBConst.AccountValidity))
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
                    if (!(boolean)DataHashMap.get(DBConst.AccountCompletion))
                    {
                        intent=new Intent(WelcomeActivity.this, EditDoctorProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else if (!(boolean)DataHashMap.get(DBConst.AccountValidity))
                    {
                        intent=new Intent(activity, EditDoctorSecureInfoActivity.class);
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
                else if (AccountType.matches(DBConst.Admin))
                {
                    intent=new Intent(activity, AdminMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
            else
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                builder.setTitle("App Entrance Status");
                builder.setMessage("Your account was locked automatically\nHave patience,it will be unlocked by admin as soon as possible.\nTry again latter...");
                builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        AccountStatusDialog.dismiss();
                    }
                });
                AccountStatusDialog=builder.create();
                AccountStatusDialog.show();
                CallDBForSignOut();
            }
        }
        else
        {
            CallDBForSignOut();
            startActivity(new Intent(WelcomeActivity.this,EntranceActivity.class));
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
                startActivity(intent);
            }
        },2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    ///***********************************************************************///
    ///************************Database Part**********************************///
    ///**********************************************************************///

    private void InitializationDB()
    {
        dbHelper=new DBHelper(activity);
        accountStatusDB=new AccountStatusDB(activity);
    }


    ///************************Database Call**********************************///
    private void CallDBForAccountUID()
    {
        dbHelper.GetUID();
    }
    private void CallDBForAccountStatus(String UID)
    {
        accountStatusDB.GetAccountStatusData(UID);
    }
    private void CallDBForSignOut()
    {
        dbHelper.SignOut();
    }
    ///**********************Interface Implementation*************************///
    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        Log.d("WelcomeActivity Called",WhichDB+" ::: "+DataHashMap.toString());
        if (WhichDB.matches(DBConst.GetAccountUID))
        {
            GetAccountUIDFromDB(DataHashMap);
        }
        else if (WhichDB.matches(DBConst.GetAccountStatusDB))
        {
            GetAccountValidityStatus(DataHashMap);
        }
    }

    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList)
    {
    }
}

package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;

import com.tarikulislamjoni95.doctorsappointment.AccountPart.SignInOrSignUpHelperClass;

public class MyDatabaseClass
{
    public SignInOrSignUpHelperClass SignIn;
    public SignInOrSignUpHelperClass SignUp;
    public AccountStatusDB accountStatusDB;
    public StorageDB storageDB;
    public ReportDB reportDB;
    public MyDatabaseClass(Activity activity)
    {
        SignIn=new SignInOrSignUpHelperClass(activity);
        SignUp=new SignInOrSignUpHelperClass(activity);
        accountStatusDB=new AccountStatusDB(activity);

        storageDB=new StorageDB(activity);
        reportDB=new ReportDB(activity);
    }
}

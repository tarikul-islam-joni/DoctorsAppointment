package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;
import android.widget.Toast;

public class MyToastClass
{
    private Activity activity;
    public MyToastClass(Activity activity)
    {
        this.activity=activity;
    }
    public void SToast(String message)
    {
        Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
    }
    public void LToast(String message)
    {
        Toast.makeText(activity,message,Toast.LENGTH_LONG).show();
    }
}

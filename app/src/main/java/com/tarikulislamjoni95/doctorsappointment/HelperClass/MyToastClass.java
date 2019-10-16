package com.tarikulislamjoni95.doctorsappointment.HelperClass;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tarikulislamjoni95.doctorsappointment.R;

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

    public void MyImageLToast(int LayoutId,String message)
    {
        View ToastView= LayoutInflater.from(activity).inflate(LayoutId,null,false);
        Toast toast=new Toast(activity);
        toast.setView(ToastView);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        TextView textView=ToastView.findViewById(R.id.text_view_0);
        textView.setText(message);
        toast.show();
    }
    public void MyImageSToast(int LayoutId,String message)
    {
        View ToastView= LayoutInflater.from(activity).inflate(LayoutId,null,false);
        TextView textView=ToastView.findViewById(R.id.text_view_0);
        textView.setText(message);
        Toast toast=new Toast(activity);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(ToastView);
        toast.show();
    }
}

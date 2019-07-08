package com.tarikulislamjoni95.doctorsappointment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private Activity activity;
    private Intent intent=null;
    private FirebaseAuth mAuth=null;
    private FirebaseUser mUser=null;
    Handler handler;

    private String WELCOME_ACTIVITY="WELCOME_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Initialization();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                ActivityFlow();
            }
        },3000);

    }
    private void Initialization()
    {
        activity=WelcomeActivity.this;
        handler=new Handler();
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
    }
    private void ActivityFlow()
    {
        mUser=null;//remove this final completion
        if (mUser==null)
        {
            intent=new Intent(activity,SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            intent=new Intent(activity,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}

package com.tarikulislamjoni95.doctorsappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

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

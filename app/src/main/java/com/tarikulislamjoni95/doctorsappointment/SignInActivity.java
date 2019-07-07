package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener,MyCommunicator
{
    private SignInOrSignUpHelperClass SignInHelperClass;
    private MyToastClass myToast;

    private boolean ChooseEmailOrPhoneSignIn=true;
    private int VALIDATION_GREEN;
    private String EmailString,PasswordString,PhoneString;

    private Activity activity;
    private Intent intent;

    private LinearLayout EmailSignInSection,PhoneSignInSection;
    private EditText SignInEmailEt,SignInPasswordEt,SignInPhoneEt;
    private Button SignInEmailSignInBtn,SignInSendPhoneVerificationBtn,SignInChooseEmailOrPhoneSectionBtn,SignInNeddAccountSignUpBtn;
    private LoginButton SignInFacebookSignInBtn;
    private SignInButton SignInGoogleSignInBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Initialization();
        InitializationUI();
        InitializationClass();
    }
    private void Initialization()
    {
        activity=SignInActivity.this;
        VALIDATION_GREEN=getResources().getColor(R.color.colorGreen);
    }
    private void InitializationUI()
    {
        EmailSignInSection=findViewById(R.id.email_sign_in_section);
        PhoneSignInSection=findViewById(R.id.phone_sign_in_section);

        SignInEmailEt=findViewById(R.id.signin_email_et);
        SignInEmailEt.addTextChangedListener(new MyTextWatcher(activity,CONST_VARIABLE.EMAIL_VALIDITY,R.id.signin_email_et));
        SignInPhoneEt=findViewById(R.id.signin_phone_et);
        SignInPhoneEt.addTextChangedListener(new MyTextWatcher(activity,CONST_VARIABLE.PHONE_VALIDITY,R.id.signin_phone_et));
        SignInPasswordEt=findViewById(R.id.signin_password_et);
        SignInPasswordEt.addTextChangedListener(new MyTextWatcher(activity,CONST_VARIABLE.PASSWORD_VALIDITY,R.id.signin_password_et));

        SignInEmailSignInBtn=findViewById(R.id.signin_email_signin_btn);
        SignInEmailSignInBtn.setOnClickListener(this);
        SignInSendPhoneVerificationBtn=findViewById(R.id.signin_send_verification_code_btn);
        SignInSendPhoneVerificationBtn.setOnClickListener(this);
        SignInGoogleSignInBtn=findViewById(R.id.signin_google_signin_btn);
        SignInGoogleSignInBtn.setOnClickListener(this);
        SignInFacebookSignInBtn=findViewById(R.id.signin_facebook_signin_btn);
        SignInFacebookSignInBtn.setOnClickListener(this);
        SignInChooseEmailOrPhoneSectionBtn=findViewById(R.id.signin_choose_email_or_phone_section_btn);
        SignInChooseEmailOrPhoneSectionBtn.setOnClickListener(this);
        SignInNeddAccountSignUpBtn=findViewById(R.id.signin_need_account_signup_btn);
        SignInNeddAccountSignUpBtn.setOnClickListener(this);
    }
    private void InitializationClass()
    {
        SignInHelperClass=new SignInOrSignUpHelperClass(activity);
        myToast=new MyToastClass(activity);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.signin_email_signin_btn:
                EmailSignInMethod();
                break;
            case R.id.signin_send_verification_code_btn:
                SendPhoneVerificationMethod();
                break;
            case R.id.signin_google_signin_btn:
                GoogleSignInMethod();
                break;
            case R.id.signin_facebook_signin_btn:
                FacebookSignInMethod();
                break;
            case R.id.signin_choose_email_or_phone_section_btn:
                ChooseEmailOrPhoneSectionMethod();
                break;
            case R.id.signin_need_account_signup_btn:
                NeedAccountSignUpMethod();
                break;
        }
    }
    private void EmailSignInMethod()
    {
        if (SignInEmailEt.getCurrentTextColor()!=VALIDATION_GREEN)
        {
            SignInEmailEt.setError("Email is incorrect format");
        } else if (SignInPasswordEt.getCurrentTextColor()!=VALIDATION_GREEN)
        {
            SignInPasswordEt.setError("Password must be atleast 6 digit");
        }
        else
        {
            EmailString=SignInEmailEt.getText().toString();
            PasswordString=SignInPasswordEt.getText().toString();
            SignInHelperClass.EmailSignInOrUp(CONST_VARIABLE.SIGN_IN_ACTIVITY,EmailString,PasswordString);
        }
    }
    private void SendPhoneVerificationMethod()
    {
        if (SignInPhoneEt.getCurrentTextColor()!=VALIDATION_GREEN)
        {
            SignInPhoneEt.setError("Phone number is incorrect format\nIt should be like 01XXXXXXXXX");
        }
        else
        {
            PhoneString="+88"+SignInPhoneEt.getText().toString();
            SignInHelperClass.PhoneSignIn(PhoneString);
        }
    }
    private void GoogleSignInMethod()
    {
        SignInHelperClass.GoogleSignIn(R.id.signin_google_signin_btn);
    }

    private void FacebookSignInMethod()
    {
        SignInHelperClass.FacebookSignIn(R.id.signin_facebook_signin_btn);
    }
    private void ChooseEmailOrPhoneSectionMethod()
    {
        if (ChooseEmailOrPhoneSignIn)
        {
            SignInChooseEmailOrPhoneSectionBtn.setText("Email Sign In");
            EmailSignInSection.setVisibility(View.GONE);
            PhoneSignInSection.setVisibility(View.VISIBLE);
            ChooseEmailOrPhoneSignIn=false;
        }
        else
        {
            SignInChooseEmailOrPhoneSectionBtn.setText("Phone Sign In");
            PhoneSignInSection.setVisibility(View.GONE);
            EmailSignInSection.setVisibility(View.VISIBLE);
            ChooseEmailOrPhoneSignIn=true;
        }
    }
    private void NeedAccountSignUpMethod()
    {
        intent=new Intent(activity,SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SignInHelperClass.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void Communicator(String FromWhichMethod, boolean result)
    {
        myToast.LToast(FromWhichMethod+String.valueOf(result));
        switch (FromWhichMethod)
        {
            case CONST_VARIABLE.EMAIL_VERIFICATION_STATUS:
                if (result) { GotoMainActivity(); }
                else { myToast.LToast("Email is not verified\nCheck your email "+EmailString+" and verify"); }
            case CONST_VARIABLE.EMAIL_SIGN_IN:
                if (!result) { myToast.SToast("Failed to sign in..."); }
                break;
            case CONST_VARIABLE.PHONE_VERIFICATION_ERROR:
                if (result)
                {
                    myToast.LToast("Error Occurred !\nCheck verification code and submit correctly");
                }
                else {
                    myToast.LToast("Error occurred !\nCheck your number and also check internet connection then re-try");
                }
                break;
            case CONST_VARIABLE.PHONE_SIGN_IN:
                if (result) { GotoMainActivity(); }
                else
                {
                    myToast.LToast("code sent to "+PhoneString);
                }
                break;
            case CONST_VARIABLE.GOOGLE_SIGN_IN:
                if (result) { GotoMainActivity(); }
                break;
            case CONST_VARIABLE.FACEBOOK_SIGN_IN:
                if (result) { GotoMainActivity(); }
                break;
        }
    }
    private void GotoMainActivity()
    {
        intent=new Intent(activity,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

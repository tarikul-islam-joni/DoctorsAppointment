package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener
{
    private boolean ChooseEmailOrPhoneSignIn=true;

    private Activity activity;

    private LinearLayout EmailSignInSection,PhoneSignInSection;
    private EditText SignInEmailEt,SignInPasswordEt,SignInPhoneEt;
    private Button SignInEmailSignInBtn,SignInSendPhoneVerificationBtn,SignInGoogleSignInBtn,SignInFacebookSignInBtn,SignInChooseEmailOrPhoneSectionBtn,SignInNeddAccountSignUpBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        InitializationUI();
    }

    private void InitializationUI()
    {
        activity=SignInActivity.this;

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

    }
    private void SendPhoneVerificationMethod()
    {

    }
    private void GoogleSignInMethod()
    {

    }
    private void FacebookSignInMethod()
    {

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

    }

}

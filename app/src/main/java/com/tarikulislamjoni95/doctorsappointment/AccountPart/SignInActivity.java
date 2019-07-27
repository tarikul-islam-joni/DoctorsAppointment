package com.tarikulislamjoni95.doctorsappointment.AccountPart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.DoctorMainActivity;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.EditDoctorProfileActivity;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.EditDoctorAppointmentInfoActivity;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.MyCommunicator;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.EditPatientProfileActivity;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.EditPatientSecureInfoActivity;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.PatientMainActivity;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.Map;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, MyCommunicator
{
    //Class Variable
    private SignInOrSignUpHelperClass SignInHelperClass;
    private MyLoadingDailog myLoadingDailog;
    private MyToastClass myToast;

    //Primitive Variable
    private boolean ChooseEmailOrPhoneSignIn=true;
    private int VALIDATION_GREEN;
    private String EmailString,PasswordString,PhoneString;
    private int RESEND_PHONE_COUNTER=3;
    private int RESEND_EMAIL_COUNTER=1;
    private int DELAY_TIMER=60;
    private int SET_DELAY_TIMER=60000;

    //Component Variable
    private Activity activity;
    private Intent intent;
    private AlertDialog dialog;

    //UI Variable
    private LinearLayout EmailSignInSection,PhoneSignInSection;
    private EditText SignInEmailEt,SignInPasswordEt,SignInPhoneEt,SignInPhoneVerificationCodeEt;
    private Button SignInEmailSignInBtn,SignInSendPhoneVerificationBtn,SignInPhoneConfirmBtn;
    private Button SignInChooseEmailOrPhoneSectionBtn,SignInNeddAccountSignUpBtn;
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
        VALIDATION_GREEN=ContextCompat.getColor(activity,R.color.colorGreen);
    }
    private void InitializationUI()
    {
        EmailSignInSection=findViewById(R.id.email_sign_in_section);
        PhoneSignInSection=findViewById(R.id.phone_sign_in_section);

        SignInEmailEt=findViewById(R.id.sign_in_email_et);
        SignInEmailEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.EMAIL_VALIDITY,R.id.sign_in_email_et));
        SignInPhoneEt=findViewById(R.id.sign_in_phone_et);
        SignInPhoneEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.PHONE_VALIDITY,R.id.sign_in_phone_et));
        SignInPhoneVerificationCodeEt=findViewById(R.id.sign_in_phone_verification_code_et);
        SignInPhoneVerificationCodeEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.VERIFICATION_CODE_VALIDITY,R.id.sign_in_phone_verification_code_et));
        SignInPasswordEt=findViewById(R.id.sign_in_password_et);
        SignInPasswordEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.PASSWORD_VALIDITY,R.id.sign_in_password_et));

        SignInEmailSignInBtn=findViewById(R.id.sign_in_email_signin_btn);
        SignInEmailSignInBtn.setOnClickListener(this);
        SignInSendPhoneVerificationBtn=findViewById(R.id.sign_in_phone_send_verification_code_btn);
        SignInSendPhoneVerificationBtn.setOnClickListener(this);
        SignInPhoneConfirmBtn=findViewById(R.id.sign_in_phone_signin_confirm_btn);
        SignInPhoneConfirmBtn.setOnClickListener(this);
        SignInGoogleSignInBtn=findViewById(R.id.sign_in_google_signin_btn);
        SignInGoogleSignInBtn.setOnClickListener(this);
        SignInFacebookSignInBtn=findViewById(R.id.sign_in_facebook_signin_btn);
        SignInFacebookSignInBtn.setOnClickListener(this);
        SignInChooseEmailOrPhoneSectionBtn=findViewById(R.id.sign_in_choose_email_or_phone_section_btn);
        SignInChooseEmailOrPhoneSectionBtn.setOnClickListener(this);
        SignInNeddAccountSignUpBtn=findViewById(R.id.sign_in_need_account_signup_btn);
        SignInNeddAccountSignUpBtn.setOnClickListener(this);
    }
    private void InitializationClass()
    {
        myToast=new MyToastClass(activity);
        SignInHelperClass=new SignInOrSignUpHelperClass(activity);
        myLoadingDailog=new MyLoadingDailog(activity,R.drawable.spinner);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.sign_in_email_signin_btn:
                EmailSignInMethod();
                break;
            case R.id.sign_in_phone_send_verification_code_btn:
                SendPhoneVerificationMethod();
                break;
            case R.id.sign_in_phone_signin_confirm_btn:
                PhoneSignInConfirmMethod();
                break;
            case R.id.sign_in_google_signin_btn:
                GoogleSignInMethod();
                break;
            case R.id.sign_in_facebook_signin_btn:
                FacebookSignInMethod();
                break;
            case R.id.sign_in_choose_email_or_phone_section_btn:
                ChooseEmailOrPhoneSectionMethod();
                break;
            case R.id.sign_in_need_account_signup_btn:
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
            myLoadingDailog.show();
            EmailString=SignInEmailEt.getText().toString();
            PasswordString=SignInPasswordEt.getText().toString();
            SignInHelperClass.EmailSignInOrUp(VARConst.SIGN_IN_ACTIVITY,EmailString,PasswordString);
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
            myLoadingDailog.show();
            PhoneString="+88"+SignInPhoneEt.getText().toString();
            SignInHelperClass.PhoneSignIn(PhoneString);

            SignInPhoneVerificationCodeEt.setEnabled(true);
            SignInPhoneConfirmBtn.setEnabled(true);

            SignInPhoneEt.setEnabled(false);
            SignInSendPhoneVerificationBtn.setEnabled(false);
            ResendDelayMethod();
        }
    }
    private void ResendDelayMethod()
    {
        RESEND_PHONE_COUNTER--;
        if (RESEND_PHONE_COUNTER>0)
        {
            DELAY_TIMER=60;
            new CountDownTimer(SET_DELAY_TIMER,1000)
            {
                @Override
                public void onTick(long l)
                {
                    DELAY_TIMER--;
                    SignInSendPhoneVerificationBtn.setText(String.valueOf(DELAY_TIMER));
                }

                @Override
                public void onFinish()
                {
                    SignInSendPhoneVerificationBtn.setEnabled(true);
                    SignInSendPhoneVerificationBtn.setText("Resend");
                    DELAY_TIMER=60;
                }
            }.start();
        }
    }
    private void PhoneSignInConfirmMethod()
    {
        myLoadingDailog.show();
        if (SignInPhoneVerificationCodeEt.getCurrentTextColor()==VALIDATION_GREEN)
        {
            SignInHelperClass.PhoneVerificationComplete(SignInPhoneVerificationCodeEt.getText().toString());
        }
    }
    private void GoogleSignInMethod()
    {
        SignInHelperClass.GoogleSignIn();
    }
    private void FacebookSignInMethod()
    {
        SignInHelperClass.FacebookSignIn(R.id.sign_in_facebook_signin_btn);
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
    private void ResendEmailVerificationMethod()
    {
        if (RESEND_EMAIL_COUNTER>0)
        {
            final AlertDialog.Builder builder=new AlertDialog.Builder(activity);
            builder.setTitle("Email Verification Status");
            builder.setMessage(EmailString+" is not verified\nPlease verify first then sign in....");
            builder.setNeutralButton("Resend Verification", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    myLoadingDailog.show();
                    SignInHelperClass.ResendEmailVerification(FirebaseAuth.getInstance().getCurrentUser());
                    dialog.cancel();
                }
            });
            dialog=builder.create();
            dialog.show();
        }
        else
        {
            myToast.LToast("Check email and verify");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SignInHelperClass.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void Communicator(String FromWhichMethod, boolean result)
    {
        if (myLoadingDailog.isShowing())
        {
            myLoadingDailog.dismiss();
        }
        switch (FromWhichMethod)
        {
            case VARConst.EMAIL_SIGN_IN:
                if (result)
                {
                    SignInHelperClass.CheckEmailVerificationStatus(FirebaseAuth.getInstance().getCurrentUser());
                }
                else
                {
                    myToast.LToast("wrong password or email");
                }
                break;
            case VARConst.EMAIL_VERIFICATION_STATUS:
                if (result)
                {
                    GotoNextStep();
                }
                else
                {
                    ResendEmailVerificationMethod();
                }
                break;
            case VARConst.RESEND_EMAIL_VERIFICATION_STATUS:
                if (result)
                {
                    RESEND_EMAIL_COUNTER--;
                    myToast.LToast("Verification code send to "+EmailString);
                }
                else
                {
                    myToast.LToast("Verification couldn't send to "+EmailString);
                }
                break;
            case VARConst.PHONE_SIGN_IN:
                if (result)
                {
                    myToast.LToast("Code sent to "+PhoneString);
                }
                else
                {
                    myToast.LToast("Code couldn't sent to "+PhoneString);
                }
                break;
            case VARConst.PHONE_SIGN_IN_STATUS:
                if (result)
                {
                    GotoNextStep();
                }
                else
                {
                    myToast.LToast("Verification code doesn't match\nFailed to sign in...");
                }
                break;
            case VARConst.GOOGLE_SIGN_IN_STATUS:
                if (result)
                {
                    GotoNextStep();
                }
                else
                {
                    myToast.LToast("Failed to sign in...\nPlease retry...");
                }
                break;
            case VARConst.GOOGLE_SIGN_IN_EXCEPTION:
                myToast.LToast("Google sign in exception occurred.Retry later...");
                break;
            case VARConst.FACEBOOK_SIGN_IN_STATUS:
                if (result)
                {
                    GotoNextStep();
                }
                else
                {
                    myToast.LToast("Failed to sign in...\nPlease retry...");
                }
                break;
            case VARConst.FACEBOOK_SIGN_IN_EXCEEPTION:
                myToast.LToast("Facebook sign in exception occurred.Retry later...");
                break;
        }
    }
    @Override
    public void GetDataFromCommunicator(String FromWhichMethod, Map<Integer, String> map) { }

    private void GotoNextStep()
    {
        myLoadingDailog.show();
        final String UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
        ref.child(DBConst.AccountStatus).child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (myLoadingDailog.isShowing())
                {
                    myLoadingDailog.dismiss();
                }
                if (dataSnapshot.exists())
                {
                    String AccountType=dataSnapshot.child(DBConst.AccountType).getValue().toString();
                    boolean AccountCompletion=(boolean)dataSnapshot.child(DBConst.AccountCompletion).getValue();
                    boolean AccountValidity=(boolean) dataSnapshot.child(DBConst.AccountValidity).getValue();
                    if (AccountType.matches(DBConst.Patient))
                    {
                        //Goto Patient Part
                        if (!AccountCompletion)
                        {
                            Intent intent=new Intent(activity, EditPatientProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else if (!AccountValidity)
                        {
                            Intent intent=new Intent(activity, EditPatientSecureInfoActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else if (AccountCompletion && AccountValidity)
                        {
                            Intent intent=new Intent(activity, PatientMainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                    else if (AccountType.matches(DBConst.Doctor))
                    {
                        //Goto Patient Part
                        if (!AccountCompletion)
                        {
                            Intent intent=new Intent(activity, EditDoctorProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else if (!AccountValidity)
                        {
                            Intent intent=new Intent(activity, EditDoctorAppointmentInfoActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else if (AccountCompletion && AccountValidity)
                        {
                            Intent intent=new Intent(activity, DoctorMainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }
                else
                {
                    FirebaseAuth.getInstance().getCurrentUser().delete();
                    if (myLoadingDailog.isShowing())
                    {
                        myLoadingDailog.dismiss();
                    }
                    myToast.LToast("Sign Up first !!! Goto Sign Up Page...");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                if (myLoadingDailog.isShowing())
                {
                    myLoadingDailog.dismiss();
                }
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }
}

package com.tarikulislamjoni95.doctorsappointment.AccountPart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.EditDoctorProfileActivity;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.MyCommunicator;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.EditPatientProfileActivity;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, MyCommunicator {

    //Class Variable
    private MyToastClass myToast;
    private SignInOrSignUpHelperClass SignUpHelperClass;
    private MyLoadingDailog myLoadingDailog;

    //Database Variable
    FirebaseAuth myAuth;
    DatabaseReference myRef;

    //Important component
    private Activity activity;
    private Intent intent;

    //Primitive Variable
    private int VALIDITY_COLOR;
    private boolean ChooseEmailOrPhone=true;
    private String EmailString,PasswordString,PhoneString,AccoutTypeString=DBConst.Patient;
    private int RESEND_COUNTER=2;
    private int SET_TIMER=60000;
    private int COUNT_DOWN=60;
    //UI Variable
    private LinearLayout SignUpEmailSection,SignUpPhoneSection;
    private CircleImageView SignUpTypeCiv;
    private EditText SignUpEmailEt,SignUpPasswordEt,SignUpRetypePasswordEt,SignupPhoneEt,SignUpPhoneVerificationCodeEt;
    private Button SignUpPatientBtn,SignUpDoctorBtn,SignUpEmailSignUpBtn,SignUpPhoneSignUpBtn,SignUpPhoneSignUpComfirmBtn;
    private Button SignUpChooseEmailOrPhoneBtn,SignUpGoogleSignUpBtn,SignUpAlreadyHasAnAccountSignInBtn;
    private LoginButton SignUpFacebookSignUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Initialization();
        InitializationUI();
        InitializationClass();
    }
    private void Initialization()
    {
        activity=SignUpActivity.this;
        myAuth=FirebaseAuth.getInstance();
        myRef= FirebaseDatabase.getInstance().getReference();
    }
    private void InitializationUI()
    {
        SignUpTypeCiv=findViewById(R.id.signup_type_civ);

        SignUpEmailSection=findViewById(R.id.email_sign_up_section);
        SignUpPhoneSection=findViewById(R.id.phone_sign_up_section);

        SignUpEmailEt=findViewById(R.id.signup_email_et);
        SignUpEmailEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.EMAIL_VALIDITY,R.id.signup_email_et));
        SignUpPasswordEt=findViewById(R.id.signup_password_et);
        SignUpPasswordEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.PASSWORD_VALIDITY,R.id.signup_password_et));
        SignUpRetypePasswordEt=findViewById(R.id.signup_retype_password_et);
        SignUpRetypePasswordEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.PASSWORD_VALIDITY,R.id.signup_retype_password_et));
        SignupPhoneEt=findViewById(R.id.signup_phone_et);
        SignupPhoneEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.PHONE_VALIDITY,R.id.signup_phone_et));
        SignUpPhoneVerificationCodeEt=findViewById(R.id.signup_phone_verification_code_et);
        SignUpPhoneVerificationCodeEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.VERIFICATION_CODE_VALIDITY,R.id.signup_phone_verification_code_et));

        SignUpPatientBtn=findViewById(R.id.signup_patient_btn);
        SignUpPatientBtn.setBackgroundColor(ContextCompat.getColor(activity,R.color.colorGreen));
        SignUpPatientBtn.setOnClickListener(this);
        SignUpDoctorBtn=findViewById(R.id.signup_doctor_btn);
        SignUpDoctorBtn.setOnClickListener(this);
        SignUpEmailSignUpBtn=findViewById(R.id.signup_email_signup_btn);
        SignUpEmailSignUpBtn.setOnClickListener(this);
        SignUpPhoneSignUpBtn=findViewById(R.id.signup_send_verification_code_btn);
        SignUpPhoneSignUpBtn.setOnClickListener(this);
        SignUpPhoneSignUpComfirmBtn=findViewById(R.id.signup_phone_confirm_btn);
        SignUpPhoneSignUpComfirmBtn.setOnClickListener(this);
        SignUpChooseEmailOrPhoneBtn=findViewById(R.id.signup_choose_email_or_phone_section_btn);
        SignUpChooseEmailOrPhoneBtn.setOnClickListener(this);
        SignUpGoogleSignUpBtn=findViewById(R.id.signup_google_signup_btn);
        SignUpGoogleSignUpBtn.setOnClickListener(this);
        SignUpFacebookSignUpBtn=findViewById(R.id.signup_facebook_signup_btn);
        SignUpFacebookSignUpBtn.setOnClickListener(this);
        SignUpAlreadyHasAnAccountSignInBtn=findViewById(R.id.signup_already_have_an_account_signup_btn);
        SignUpAlreadyHasAnAccountSignInBtn.setOnClickListener(this);
    }
    private void InitializationClass()
    {
        VALIDITY_COLOR= ContextCompat.getColor(activity,R.color.colorGreen);
        myToast=new MyToastClass(activity);
        SignUpHelperClass=new SignInOrSignUpHelperClass(activity);
        myLoadingDailog=new MyLoadingDailog(activity,R.drawable.spinner);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.signup_patient_btn:
                AccoutTypeString=DBConst.Patient;
                SignUpTypeCiv.setImageResource(R.drawable.patient);
                SignUpPatientBtn.setBackgroundColor(ContextCompat.getColor(activity,R.color.colorGreen));
                SignUpDoctorBtn.setBackgroundColor(ContextCompat.getColor(activity,R.color.colorWhite));
                break;
            case R.id.signup_doctor_btn:
                AccoutTypeString=DBConst.Doctor;
                SignUpTypeCiv.setImageResource(R.drawable.male_doc);
                SignUpDoctorBtn.setBackgroundColor(ContextCompat.getColor(activity,R.color.colorGreen));
                SignUpPatientBtn.setBackgroundColor(ContextCompat.getColor(activity,R.color.colorWhite));
                break;
            case R.id.signup_email_signup_btn:
                EmailSignUp();
                break;
            case R.id.signup_send_verification_code_btn:
                PhoneSignUp();
                break;
            case R.id.signup_phone_confirm_btn:
                PhoneSignUpCompleting();
                break;
            case R.id.signup_google_signup_btn:
                GoogleSignUp();
                break;
            case R.id.signup_facebook_signup_btn:
                FacebookSignUp();
            case R.id.signup_choose_email_or_phone_section_btn:
                ChooseEmailOrPhoneMethod();
                break;
            case R.id.signup_already_have_an_account_signup_btn:
                GotoSignInActivity();
                break;
        }
    }
    private void EmailSignUp()
    {
        if (SignUpEmailEt.getCurrentTextColor()!=VALIDITY_COLOR)
        {
            SignUpEmailEt.setError("Email is invalid");
        }
        else if (SignUpPasswordEt.getCurrentTextColor()!=VALIDITY_COLOR)
        {
            SignUpPasswordEt.setError("Password atleast 6 digit");
        }
        else if (SignUpRetypePasswordEt.getCurrentTextColor()!=VALIDITY_COLOR)
        {
            SignUpRetypePasswordEt.setError("Password atleast 6 digit");
        }
        else if (!SignUpPasswordEt.getText().toString().matches(SignUpRetypePasswordEt.getText().toString()))
        {
            SignUpRetypePasswordEt.setError("Password not matched");
        }
        else
        {
            if (!myLoadingDailog.isShowing())
            {
                myLoadingDailog.show();
            }
            EmailString=SignUpEmailEt.getText().toString();
            PasswordString=SignUpPasswordEt.getText().toString();
            SignUpHelperClass.EmailSignInOrUp(VARConst.SIGN_UP_ACTIVITY,EmailString,PasswordString);
        }
    }
    private void PhoneSignUp()
    {
        if (SignupPhoneEt.getCurrentTextColor()!=VALIDITY_COLOR)
        {
            SignupPhoneEt.setError("Phone number is incorrect");
        }
        else
        {
            if (!myLoadingDailog.isShowing())
            {
                myLoadingDailog.show();
            }
            PhoneString="+88"+SignupPhoneEt.getText().toString();
            SignUpHelperClass.PhoneSignIn(PhoneString);
            SignUpPhoneVerificationCodeEt.setEnabled(true);
            SignUpPhoneSignUpComfirmBtn.setEnabled(true);
            SignupPhoneEt.setEnabled(false);
            SignUpPhoneSignUpBtn.setEnabled(false);
            if (!ResendDelay())
            {
                myToast.LToast("Code resending limit exceeded");
            }
        }
    }
    private boolean ResendDelay()
    {
        RESEND_COUNTER--;
        if (RESEND_COUNTER>=0)
        {
            SET_TIMER=60000;
            new CountDownTimer(SET_TIMER,1000)
            {
                @Override
                public void onTick(long l)
                {
                    COUNT_DOWN--;
                    SignUpPhoneSignUpBtn.setText(String.valueOf(COUNT_DOWN));
                }

                @Override
                public void onFinish()
                {
                    COUNT_DOWN=60;
                    SignUpPhoneSignUpBtn.setText("RESEND");
                    SignUpPhoneSignUpBtn.setEnabled(true);
                }
            }.start();
            return true;
        }
        else
        {
            return false;
        }
    }
    private void PhoneSignUpCompleting()
    {
        if (SignUpPhoneVerificationCodeEt.getText().toString().matches(""))
        {
            SignUpPhoneVerificationCodeEt.setError("Verification code shouldn't be empty");
        }
        else if(SignUpPhoneVerificationCodeEt.getCurrentTextColor()!=VALIDITY_COLOR)
        {
            SignUpPhoneVerificationCodeEt.setError("Invalid");
        }
        else
        {
            if (!myLoadingDailog.isShowing())
            {
                myLoadingDailog.show();
            }
            String VerificationCodeString=SignUpPhoneVerificationCodeEt.getText().toString();
            SignUpHelperClass.PhoneVerificationComplete(VerificationCodeString);
        }
    }
    private void GoogleSignUp()
    {
        SignUpHelperClass.GoogleSignIn();
    }
    private void FacebookSignUp()
    {
        SignUpHelperClass.FacebookSignIn(R.id.signup_facebook_signup_btn);
    }
    private void GotoSignInActivity()
    {
        intent=new Intent(activity,SignInActivity.class);
        startActivity(intent);
    }
    private void ChooseEmailOrPhoneMethod()
    {
        if (ChooseEmailOrPhone)
        {
            SignUpPhoneSection.setVisibility(View.VISIBLE);
            SignUpEmailSection.setVisibility(View.GONE);
            SignUpChooseEmailOrPhoneBtn.setText("Email Sign Up");
            ChooseEmailOrPhone=false;
        }
        else
        {
            SignUpEmailSection.setVisibility(View.VISIBLE);
            SignUpPhoneSection.setVisibility(View.GONE);
            SignUpChooseEmailOrPhoneBtn.setText("Phone Sign Up");
            ChooseEmailOrPhone=true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SignUpHelperClass.onActivityResult(requestCode,resultCode,data);
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
            case VARConst.EMAIL_SIGN_UP:
                if (result)
                {
                    myToast.LToast("Account successfully created\nPlease verify for next sign in...");
                    GotoNextStep();
                }
                else
                {
                    myToast.LToast("Account creation failed.");
                }
                break;
            case VARConst.PHONE_SIGN_IN:
                if (result)
                {
                    myToast.LToast("Verification code sent to "+PhoneString);
                }
                else
                {
                    myToast.LToast("Verification code couldn't sent to "+PhoneString);
                }
                break;
            case VARConst.PHONE_SIGN_IN_STATUS:
                if (result)
                {
                    GotoNextStep();
                }
                else
                {
                    myToast.LToast("Verification code not matched !");
                }
                break;
            case VARConst.GOOGLE_SIGN_IN_STATUS:
                if (result)
                {
                    GotoNextStep();
                }
                else
                {
                    myToast.LToast("Account creation failed ! ");
                }
                break;
            case VARConst.GOOGLE_SIGN_IN_EXCEPTION:
                myToast.LToast("Google sign in exception occurred.\nPlease try again later");
                break;
            case VARConst.FACEBOOK_SIGN_IN_STATUS:
                if (result)
                {
                    GotoNextStep();
                }
                else
                {
                    myToast.LToast("Account creation failed ! ");
                }
                break;
            case VARConst.FACEBOOK_SIGN_IN_EXCEEPTION:
                myToast.LToast("Facebook sign in exception occurred.\nPlease try again later");
                break;

        }
    }

    private void GotoNextStep()
    {
        if (!myLoadingDailog.isShowing())
        {
            myLoadingDailog.show();
        }
        final String UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).child(UID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (myLoadingDailog.isShowing())
                    {
                        myLoadingDailog.dismiss();
                    }
                    myToast.LToast("Account already created.\nGoto sign in page");
                    FirebaseAuth.getInstance().signOut();
                }
                else
                {
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).child(UID);
                    ref.child(DBConst.AccountType).setValue(AccoutTypeString);
                    ref.child(DBConst.AccountCompletion).setValue(false);
                    ref.child(DBConst.AuthorityValidity).setValue(false);
                    ref.child(DBConst.AccountValidity).setValue(false)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isComplete())
                                    {
                                        if (myLoadingDailog.isShowing())
                                        {
                                            myLoadingDailog.dismiss();
                                        }
                                        if (AccoutTypeString.matches(DBConst.Patient))
                                        {
                                            Intent intent=new Intent(activity, EditPatientProfileActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                        else if (AccoutTypeString.matches(DBConst.Doctor))
                                        {
                                            Intent intent=new Intent(activity, EditDoctorProfileActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                    else
                                    {
                                        FirebaseAuth.getInstance().getCurrentUser().delete();
                                    }
                                }
                            });
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
            }
        });
    }

    @Override
    public void GetDataFromCommunicator(String FromWhichMethod, Map<Integer, String> map) { }
}

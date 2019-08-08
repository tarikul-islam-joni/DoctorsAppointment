package com.tarikulislamjoni95.doctorsappointment.AccountPart;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDM;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.MyDatabaseClass;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.EditDoctorProfileActivity;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountStatusDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountCreationInterface;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.EditPatientProfileActivity;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener ,
        AccountCreationInterface,AccountStatusDBInterface {

    //Class Variable
    private MyDatabaseClass myDatabaseClass;
    private MyToastClass myToast;
    private MyLoadingDailog myLoadingDailog;

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
    //Initialization of some important variable
    private void Initialization()
    {
        activity=SignUpActivity.this;
        VALIDITY_COLOR= ContextCompat.getColor(activity,R.color.colorGreen);
    }
    //Initialization of UI variable
    private void InitializationUI()
    {
        SignUpTypeCiv=findViewById(R.id.image_civ);

        SignUpEmailSection=findViewById(R.id.email_section);
        SignUpPhoneSection=findViewById(R.id.phone_section);

        SignUpEmailEt=findViewById(R.id.email_et);
        SignUpEmailEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        SignUpEmailEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.EMAIL_VALIDITY,R.id.email_et));

        SignUpPasswordEt=findViewById(R.id.password_et);
        SignUpPasswordEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        SignUpPasswordEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.PASSWORD_VALIDITY,R.id.password_et));

        SignUpRetypePasswordEt=findViewById(R.id.retype_password_et);
        SignUpRetypePasswordEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        SignUpRetypePasswordEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.PASSWORD_VALIDITY,R.id.retype_password_et));

        SignupPhoneEt=findViewById(R.id.phone_et);
        SignupPhoneEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        SignupPhoneEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.PHONE_VALIDITY,R.id.phone_et));

        SignUpPhoneVerificationCodeEt=findViewById(R.id.verification_code_et);
        SignUpPhoneVerificationCodeEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        SignUpPhoneVerificationCodeEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.VERIFICATION_CODE_VALIDITY,R.id.verification_code_et));

        SignUpPatientBtn=findViewById(R.id.patient_btn);
        SignUpPatientBtn.setBackgroundColor(ContextCompat.getColor(activity,R.color.colorGreen));
        SignUpPatientBtn.setOnClickListener(this);

        SignUpDoctorBtn=findViewById(R.id.doctor_btn);
        SignUpDoctorBtn.setOnClickListener(this);

        SignUpEmailSignUpBtn=findViewById(R.id.email_confirm_btn);
        SignUpEmailSignUpBtn.setOnClickListener(this);

        SignUpPhoneSignUpBtn=findViewById(R.id.send_verification_code_btn);
        SignUpPhoneSignUpBtn.setOnClickListener(this);

        SignUpPhoneSignUpComfirmBtn=findViewById(R.id.phone_confirm_btn);
        SignUpPhoneSignUpComfirmBtn.setOnClickListener(this);

        SignUpChooseEmailOrPhoneBtn=findViewById(R.id.choose_email_or_phone_section_btn);
        SignUpChooseEmailOrPhoneBtn.setOnClickListener(this);

        SignUpGoogleSignUpBtn=findViewById(R.id.google_sign_in_or_up_btn);
        SignUpGoogleSignUpBtn.setOnClickListener(this);

        SignUpFacebookSignUpBtn=findViewById(R.id.facebook_sign_in_or_up_btn);
        SignUpFacebookSignUpBtn.setOnClickListener(this);

        SignUpAlreadyHasAnAccountSignInBtn=findViewById(R.id.goto_sign_in_or_up_btn);
        SignUpAlreadyHasAnAccountSignInBtn.setOnClickListener(this);
    }
    //Initialization of class variable
    private void InitializationClass()
    {
        myToast=new MyToastClass(activity);
        myLoadingDailog=new MyLoadingDailog(activity,R.drawable.spinner);

        myDatabaseClass=new MyDatabaseClass(activity);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.patient_btn:
                AccoutTypeString=DBConst.Patient;
                SignUpTypeCiv.setImageResource(R.drawable.patient);
                SignUpPatientBtn.setBackgroundColor(ContextCompat.getColor(activity,R.color.colorGreen));
                SignUpDoctorBtn.setBackgroundColor(ContextCompat.getColor(activity,R.color.colorWhite));
                break;
            case R.id.doctor_btn:
                AccoutTypeString=DBConst.Doctor;
                SignUpTypeCiv.setImageResource(R.drawable.male_doc);
                SignUpDoctorBtn.setBackgroundColor(ContextCompat.getColor(activity,R.color.colorGreen));
                SignUpPatientBtn.setBackgroundColor(ContextCompat.getColor(activity,R.color.colorWhite));
                break;
            case R.id.email_confirm_btn:
                EmailSignUp();
                break;
            case R.id.send_verification_code_btn:
                PhoneSignUp();
                break;
            case R.id.phone_confirm_btn:
                PhoneSignUpCompleting();
                break;
            case R.id.google_sign_in_or_up_btn:
                GoogleSignUp();
                break;
            case R.id.facebook_sign_in_or_up_btn:
                FacebookSignUp();
            case R.id.choose_email_or_phone_section_btn:
                ChooseEmailOrPhoneMethod();
                break;
            case R.id.goto_sign_in_or_up_btn:
                GotoSignInActivity();
                break;
        }
    }

    private void ShowLoadingDialog()
    {
        if (!myLoadingDailog.isShowing())
        {
            myLoadingDailog.show();
        }
    }
    private void CancelLoadingDialog()
    {
        if (myLoadingDailog.isShowing())
        {
            myLoadingDailog.dismiss();
        }
    }

    //*** Email or Phone Sign Up Choosing section ***//
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
    private void GotoSignInActivity()
    {
        intent=new Intent(activity,SignInActivity.class);
        startActivity(intent);
    }

    //*** Email SignUp method starting from here and other proccess completing in implementation section ***//
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
            EmailString=SignUpEmailEt.getText().toString();
            PasswordString=SignUpPasswordEt.getText().toString();

            //Database Call For Email SignUp
            CallDBForEmailSignUp(EmailString,PasswordString);
        }
    }

    //*** Phone SignUp method starting from here and other proccess completing in implementation section ***//
    private void PhoneSignUp()
    {
        if (SignupPhoneEt.getCurrentTextColor()!=VALIDITY_COLOR)
        {
            SignupPhoneEt.setError("Phone number is incorrect");
        }
        else
        {
            PhoneString=SignupPhoneEt.getText().toString();
            SignUpPhoneVerificationCodeEt.setEnabled(true);
            SignUpPhoneSignUpComfirmBtn.setEnabled(true);
            SignupPhoneEt.setEnabled(false);
            SignUpPhoneSignUpBtn.setEnabled(false);
            if (!ResendDelay())
            {
                myToast.LToast("Code resending limit exceeded");
            }

            //Call Database For Phone Sign Up
            CallDBForPhoneSignUp(PhoneString);
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

    //*** Phone SignUp code verification method starting from here and other proccess completing in implementation section ***//
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
            String VerificationCodeString=SignUpPhoneVerificationCodeEt.getText().toString();

            //Call Database For Phone Verification
            CallDBForPhoneVerification(VerificationCodeString);
        }
    }

    //*** Google SignUp method starting from here and other proccess completing in implementation section ***//
    private void GoogleSignUp()
    {
        //Call Database For Google SignUp
        CallDBForGoogleSignUp();
    }
    //*** Facebook SignUp method starting from here and other proccess completing in implementation section ***//
    private void FacebookSignUp()
    {
        //Call Database Facebook SignUp
        CallDBForFacebookSignUp(SignUpFacebookSignUpBtn.getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Call Database For Sending OnActivityResult
        CallDBForSendingOnActivityResult(requestCode,resultCode,data);
    }

    private void GetSignUpStatus(String FromWhichMethod,boolean result)
    {
        CancelLoadingDialog();

        switch (FromWhichMethod)
        {
            case VARConst.EMAIL_SIGN_UP:
                if (result)
                {
                    myToast.LToast("Account successfully created\nPlease verify for next sign in...");
                    CallDBForAccountExistanceCheck();
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
                    CallDBForAccountExistanceCheck();
                }
                else
                {
                    myToast.LToast("Verification code not matched !");
                }
                break;
            case VARConst.GOOGLE_SIGN_IN_STATUS:
                if (result)
                {
                    CallDBForAccountExistanceCheck();
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
                    CallDBForAccountExistanceCheck();
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

    private void GetAccountExistanceStatus(boolean AccountExistanceResult)
    {
        CancelLoadingDialog();
        if (AccountExistanceResult)
        {
            myToast.LToast("Account is already created!Please sign in...");
            startActivity(new Intent(activity,SignInActivity.class));
        }
        else
        {
            CallDBForSavingAccountStatus();
        }
    }

    private void GetAccountStatusSavingResult(boolean AccountStatusSavingResult)
    {
        CancelLoadingDialog();
        if (AccountStatusSavingResult)
        {
            GotoNextActivity();
        }
        else
        {
            myToast.LToast("Failed to create the account");
            CallDBForDeletionUnsuccessFullUser();
        }
    }
    private void GotoNextActivity()
    {
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


    ///******************************************************************************************///
    ///*****************************************Database Part************************************///
    ///******************************************************************************************///

    ///**************************************Database Calling Method*****************************///
    private void CallDBForEmailSignUp(String emailString, String passwordString)
    {
        myDatabaseClass.SignUp.EmailSignInOrUp(VARConst.SIGN_UP_ACTIVITY,emailString,passwordString);
    }
    private void CallDBForPhoneSignUp(String phoneString)
    {
        myDatabaseClass.SignUp.PhoneSignIn(phoneString);
    }
    private void CallDBForPhoneVerification(String VerificationCodeString)
    {
        myDatabaseClass.SignUp.PhoneVerificationComplete(VerificationCodeString);
    }
    private void CallDBForFacebookSignUp(int FacebookButtonId)
    {
        myDatabaseClass.SignUp.FacebookSignIn(FacebookButtonId);
    }
    private void CallDBForGoogleSignUp()
    {
        myDatabaseClass.SignUp.GoogleSignIn();
    }
    private void CallDBForSendingOnActivityResult(int requestCode, int resultCode, Intent data)
    {
        myDatabaseClass.SignUp.onActivityResult(requestCode,resultCode,data);
    }

    private void CallDBForAccountExistanceCheck()
    {
        myDatabaseClass.accountStatusDB.GetUIDAccountStatusData();
    }
    private void CallDBForSavingAccountStatus()
    {
        myDatabaseClass.accountStatusDB.SaveIntoAccountStatusDBFromUser(AccoutTypeString,false,false);
    }

    private void CallDBForDeletionUnsuccessFullUser()
    {
        myDatabaseClass.SignUp.DeleteAccount();
    }
    ///************************Account Creation Interface***********************///
    @Override
    public void AccountCreationResult(String FromWhichMethod, boolean result)
    {
        GetSignUpStatus(FromWhichMethod,result);
    }

    ///*******************Account Status DB Interface*********************///
    @Override
    public void GetAccountStatus(boolean result, ArrayList<AccountStatusDM> arrayList)
    {
        GetAccountExistanceStatus(result);
    }


    @Override
    public void AccountStatusSavingResult(boolean result)
    {
        GetAccountStatusSavingResult(result);
    }
}

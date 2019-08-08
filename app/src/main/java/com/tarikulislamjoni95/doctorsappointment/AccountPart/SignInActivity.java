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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.tarikulislamjoni95.doctorsappointment.AdminPart.AdminMainActivity;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDM;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.MyDatabaseClass;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.DoctorMainActivity;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.EditDoctorProfileActivity;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.EditDoctorSecureInfo;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountCreationInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountStatusDBInterface;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.EditPatientProfileActivity;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.EditPatientSecureInfoActivity;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.PatientMainActivity;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, AccountCreationInterface, AccountStatusDBInterface
{
    //Class Variable
    private MyDatabaseClass myDatabaseClass;
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
    private AlertDialog ResendDialog;

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
        SignInEmailEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        SignInEmailEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.EMAIL_VALIDITY,R.id.sign_in_email_et));

        SignInPasswordEt=findViewById(R.id.sign_in_password_et);
        SignInPasswordEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        SignInPasswordEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.PASSWORD_VALIDITY,R.id.sign_in_password_et));

        SignInPhoneEt=findViewById(R.id.sign_in_phone_et);
        SignInPhoneEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        SignInPhoneEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.PHONE_VALIDITY,R.id.sign_in_phone_et));

        SignInPhoneVerificationCodeEt=findViewById(R.id.sign_in_phone_verification_code_et);
        SignInPhoneVerificationCodeEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        SignInPhoneVerificationCodeEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.VERIFICATION_CODE_VALIDITY,R.id.sign_in_phone_verification_code_et));


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
        myLoadingDailog=new MyLoadingDailog(activity,R.drawable.spinner);
        myDatabaseClass=new MyDatabaseClass(activity);
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

    ///**** Email or Phone Sign In Section Choosing method *****//
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

    ///**** Goto directly SignUp Activity method *****//
    private void NeedAccountSignUpMethod()
    {
        intent=new Intent(activity,SignUpActivity.class);
        startActivity(intent);
    }

    ///**** Email Sign In Process get started and next process are in implementation section ****///
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

            ///**** Email Sign In Process get started *****//
            CallDBForEmailSignIn(EmailString,PasswordString);
        }
    }


    ///**** Phone Sign In Process get started and next process are in implementation section ****///
    private void SendPhoneVerificationMethod()
    {
        if (SignInPhoneEt.getCurrentTextColor()!=VALIDATION_GREEN)
        {
            SignInPhoneEt.setError("Phone number is incorrect format\nIt should be like 01XXXXXXXXX");
        }
        else
        {
            PhoneString=SignInPhoneEt.getText().toString();

            ///**** Phone Sign In Process get started *****//
            CallDBForPhoneSignIn(PhoneString);

            SignInPhoneVerificationCodeEt.setEnabled(true);
            SignInPhoneConfirmBtn.setEnabled(true);

            SignInPhoneEt.setEnabled(false);
            SignInSendPhoneVerificationBtn.setEnabled(false);
            ResendDelayMethod();
        }
    }
    ///**** Phone verification resend delay Process get started *****//
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

    ///**** Phone Sign In Completing Process get started and next process are in implementation section ****///
    private void PhoneSignInConfirmMethod()
    {
        if (SignInPhoneVerificationCodeEt.getCurrentTextColor()==VALIDATION_GREEN)
        {
            ///**** Phone Sign In Completing Process get started *****//
            CallDBForPhoneSignInVerification(SignInPhoneVerificationCodeEt.getText().toString());
        }
    }

    ///**** Google Sign In Process get started and next process are in implementation section ****///
    private void GoogleSignInMethod()
    {
        ///**** Google Sign In Process get started *****//
        CallDBForGoogleSignIn();
    }


    ///**** Facebook Sign In Process get started and next process are in implementation section ****///
    private void FacebookSignInMethod()
    {
        ///**** Facebook Sign In Process get started *****//
        CallDBForFacebookSignIn(SignInFacebookSignInBtn.getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CallDBForSendingOnActivityResult(requestCode,resultCode,data);
    }

    private void GetSignInStatus(String FromWhichMethod, boolean result)
    {
        CancelLoadingDialog();
        switch (FromWhichMethod)
        {
            case VARConst.EMAIL_SIGN_IN:
                if (result)
                {
                    ///Check verification status
                    CallDBForCheckEmailVerification();
                }
                else
                {
                    myToast.LToast("wrong password or email");
                }
                break;
            case VARConst.EMAIL_VERIFICATION_STATUS:
                if (result)
                {
                    //Email Verification status true so send him check further database check.
                    CallDBForCheckAccountStatus();
                }
                else
                {
                    ////Email Verification status false so send him an option to resend email verification
                    ShowResendEmailVerificationMethod();
                }
                break;
            case VARConst.RESEND_EMAIL_VERIFICATION_STATUS:
                if (result)
                {
                    ///Email verification resending status get here and counting of resending
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
                    //Phone verification sent to the number and waiting for the verification step
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
                    //If phone verification status get true send hin to further database check
                    CallDBForCheckAccountStatus();
                }
                else
                {
                    myToast.LToast("Verification code doesn't match\nFailed to sign in...");
                }
                break;
            case VARConst.GOOGLE_SIGN_IN_STATUS:
                if (result)
                {
                    //Google Sign In and send him check further database check.
                    CallDBForCheckAccountStatus();
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
                    //Facebook Sign In and send him check further database check.
                    CallDBForCheckAccountStatus();
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


    ///**** Resending Email verification code Process get started *****//
    private void ShowResendEmailVerificationMethod()
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
                    ResendDialog.cancel();

                    //Call Database For Resending Email Verification
                    CallDBForResendEmailVerification();
                }
            });
            ResendDialog=builder.create();
            ResendDialog.show();
        }
        else
        {
            myToast.LToast("Check email and verify");
        }
    }



    ///Check the databse where he should be go and result of this method goes to implementation interface


    private void GetAccountStatus(boolean AccountExistanceStatus,String AccountType,boolean AccountCompletion,boolean AccountValidity)
    {
        if (AccountExistanceStatus)
        {
            NextActivityStarting(AccountType,AccountCompletion,AccountValidity);
        }
        else
        {
            myToast.LToast("Account is not created ! Sign Up first !!! Goto Sign Up Page...");
            ///Delete Account For Proper Sign In
            CallDBForAccountDeletion();
        }
    }
    private void NextActivityStarting(String AccountType,boolean AccountCompletion,boolean AccountValidity)
    {
        CancelLoadingDialog();

        if (AccountType.matches(DBConst.Patient))
        {
            //Goto Patient Part
            if (!AccountCompletion)
            {
                StartActivity(new Intent(activity, EditPatientProfileActivity.class));
            }
            else if (!AccountValidity)
            {
                StartActivity(new Intent(activity, EditPatientSecureInfoActivity.class));
            }
            else if (AccountCompletion && AccountValidity)
            {
                StartActivity(new Intent(activity, PatientMainActivity.class));
            }
        }
        else if (AccountType.matches(DBConst.Doctor))
        {
            //Goto Patient Part
            if (!AccountCompletion)
            {
                StartActivity(new Intent(activity, EditDoctorProfileActivity.class));
            }
            else if (!AccountValidity)
            {
                StartActivity(new Intent(activity, EditDoctorSecureInfo.class));
            }
            else if (AccountCompletion && AccountValidity)
            {
                StartActivity(new Intent(activity, DoctorMainActivity.class));
            }
        }
        else
        {
            StartActivity(new Intent(activity, AdminMainActivity.class));
        }
    }

    private void StartActivity(Intent myIntent)
    {
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myIntent);
    }






    ///***********************************************************************///
    ///***************************Database Part*******************************///
    ///***********************************************************************///




    ///***************************Database Call********************************///
    private void CallDBForEmailSignIn(String emailString, String passwordString)
    {
        ShowLoadingDialog();
        myDatabaseClass.SignIn.EmailSignInOrUp(VARConst.SIGN_IN_ACTIVITY,emailString,passwordString);
    }
    private void CallDBForPhoneSignIn(String phoneString)
    {
        ShowLoadingDialog();
        myDatabaseClass.SignIn.PhoneSignIn(phoneString);
    }
    private void CallDBForPhoneSignInVerification(String verificationCode)
    {
        ShowLoadingDialog();
        myDatabaseClass.SignIn.PhoneVerificationComplete(verificationCode);
    }
    private void CallDBForGoogleSignIn()
    {
        myDatabaseClass.SignIn.GoogleSignIn();
    }
    private void CallDBForFacebookSignIn(int FacebookButtonId)
    {
        myDatabaseClass.SignIn.FacebookSignIn(FacebookButtonId);
    }
    private void CallDBForSendingOnActivityResult(int requestCode, int resultCode, Intent data)
    {
        myDatabaseClass.SignIn.onActivityResult(requestCode,resultCode,data);
    }
    private void CallDBForCheckEmailVerification()
    {
        ShowLoadingDialog();
        myDatabaseClass.SignIn.CheckEmailVerificationStatus();
    }
    private void CallDBForResendEmailVerification()
    {
        ShowLoadingDialog();
        myDatabaseClass.SignIn.ResendEmailVerification();
    }
    private void CallDBForCheckAccountStatus()
    {
        ShowLoadingDialog();
        myDatabaseClass.accountStatusDB.GetUIDAccountStatusData();
    }
    private void CallDBForAccountDeletion()
    {
        myDatabaseClass.SignIn.DeleteAccount();
    }

    ///*********************Account Sign In Interface implementation *********************///
    @Override
    public void AccountCreationResult(String FromWhichMethod, boolean result)
    {
        GetSignInStatus(FromWhichMethod,result);
    }

    ///******************************AccountStatus Part Interface Implementation*******************************///
    @Override
    public void GetAccountStatus(boolean result, ArrayList<AccountStatusDM> arrayList)
    {
        GetAccountStatus(result,arrayList.get(0).getAccountType(),arrayList.get(0).isAccountCompletion(),arrayList.get(0).isAccountValidity());
    }

    @Override
    public void AccountStatusSavingResult(boolean result) { }
}

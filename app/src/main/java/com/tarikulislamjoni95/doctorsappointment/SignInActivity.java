package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Map;
public class SignInActivity extends AppCompatActivity implements View.OnClickListener,MyCommunicator, MyDialogClass.MyAlertDialogCommunicator
{
    //Class Variable
    private SignInOrSignUpHelperClass SignInHelperClass;
    private MyDialogClass myDialogClass;
    private MyToastClass myToast;

    //Primitive Variable
    private boolean ChooseEmailOrPhoneSignIn=true;
    private int VALIDATION_GREEN;
    private String EmailString,PasswordString,PhoneString;

    //Component Variable
    private AlertDialog dialog;
    private Activity activity;
    private Intent intent;

    //UI Variable
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
        EmailSignInSection=findViewById(R.id.email_sign_up_section);
        PhoneSignInSection=findViewById(R.id.phone_sign_up_section);

        SignInEmailEt=findViewById(R.id.signup_email_et);
        SignInEmailEt.addTextChangedListener(new MyTextWatcher(activity,CONST_VARIABLE.EMAIL_VALIDITY,R.id.signup_email_et));
        SignInPhoneEt=findViewById(R.id.signup_phone_et);
        SignInPhoneEt.addTextChangedListener(new MyTextWatcher(activity,CONST_VARIABLE.PHONE_VALIDITY,R.id.signup_phone_et));
        SignInPasswordEt=findViewById(R.id.signup_password_et);
        SignInPasswordEt.addTextChangedListener(new MyTextWatcher(activity,CONST_VARIABLE.PASSWORD_VALIDITY,R.id.signup_password_et));

        SignInEmailSignInBtn=findViewById(R.id.signin_email_signin_btn);
        SignInEmailSignInBtn.setOnClickListener(this);
        SignInSendPhoneVerificationBtn=findViewById(R.id.signup_send_verification_code_btn);
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
        myToast=new MyToastClass(activity);
        myDialogClass=new MyDialogClass(activity);
        SignInHelperClass=new SignInOrSignUpHelperClass(activity);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.signin_email_signin_btn:
                EmailSignInMethod();
                break;
            case R.id.signup_send_verification_code_btn:
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
        SignInHelperClass.GoogleSignIn();
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
    private void GotoMainActivity()
    {
        intent=new Intent(activity,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
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
        switch (FromWhichMethod) {
            case CONST_VARIABLE.EMAIL_SIGN_IN:
                if (result)
                {
                    SignInHelperClass.CheckEmailVerificationStatus(FirebaseAuth.getInstance().getCurrentUser());
                }
                else
                {
                    myToast.LToast("Failed to sign in...");
                }
                break;
            case CONST_VARIABLE.EMAIL_VERIFICATION_STATUS:
                if (result)
                {
                    CancelDialog();
                    GotoMainActivity();
                }
                else
                {
                    ShowEmailVerificationDialog();
                }
                break;
            case CONST_VARIABLE.RESEND_EMAIL_VERIFICATION_STATUS:
                if (result)
                {
                    myToast.SToast("Verification email sent to "+EmailString);
                }
                else
                {
                    myToast.LToast("Failed to send verification email");
                }
            case CONST_VARIABLE.PHONE_VERIFICATION_MANUAL_COMPLETE:
                if (result)
                {
                    ShowPhoneVerificationDialog();
                }
                break;
            case CONST_VARIABLE.PHONE_VERIFICATION_ERROR:
                if (result)
                {
                    myToast.SToast("Verification code not matched");
                }
                break;
            case CONST_VARIABLE.PHONE_SIGN_IN_EXCEPTION:
                if (result)
                {
                    myToast.SToast("Verification code couldn't send\nCheck the phone number that you have provided\nAlso check your internet conncetion...");
                }
            case CONST_VARIABLE.PHONE_SIGN_IN_STATUS:
                if (result)
                {
                    CancelDialog();
                    GotoMainActivity();
                }
                else
                {
                    myToast.SToast("Sign in unsuccessful");
                }
                break;
            case CONST_VARIABLE.GOOGLE_SIGN_IN_EXCEPTION:
                if (result)
                {
                    myToast.SToast("Something error occurred.\nPlease try again...");
                }
                break;
            case CONST_VARIABLE.GOOGLE_SIGN_IN_STATUS:
                if (result)
                {
                    GotoMainActivity();
                }
                else
                {
                    myToast.SToast("Failed to sign in...");
                }
                break;
            case CONST_VARIABLE.FACEBOOK_SIGN_IN_EXCEEPTION:
                if (result)
                {
                    myToast.SToast("Something error occurred\nPlease try again");
                }
                break;
            case CONST_VARIABLE.FACEBOOK_SIGN_IN_STATUS:
                if (result)
                {
                    GotoMainActivity();
                }
                else
                {
                    myToast.SToast("Failed to sign in...");
                }
                break;
        }
    }
    private void ShowPhoneVerificationDialog()
    {
        int[] btn={R.id.confirm_btn,R.id.resend_btn,R.id.cancel_btn};
        int[] Et={R.id.verification_code_et};
        String[] information={"ConfirmBtn","ResendBtn","CancelBtn"};
        dialog=myDialogClass.MyCustomDialog(R.layout.verification_code,btn,Et,information,"Verification Code","Please complete verification step");
        dialog.setCancelable(false);
        dialog.show();
    }
    private void ShowEmailVerificationDialog()
    {
        dialog=myDialogClass.MyAlertDialog(CONST_VARIABLE.EMAIL_SIGN_IN,"Email Verification Status",
                EmailString+" is unverified\nPlease verify first and sign in.","Resend Verification","Cancel");
        dialog.setCancelable(false);
        dialog.show();
    }
    private void CancelDialog()
    {
        dialog.cancel();
    }
    @Override
    public void DialogResultSuccess(String WhichField,String result)
    {
        switch (WhichField)
        {
            case CONST_VARIABLE.EMAIL_SIGN_IN:
                switch (result)
                {
                    case "Yes":
                        SignInHelperClass.ResendEmailVerification(FirebaseAuth.getInstance().getCurrentUser());
                        break;
                    case "No":
                        CancelDialog();
                        break;
                }
                break;
            case CONST_VARIABLE.PHONE_SIGN_IN:
                switch (result)
                {
                    case "CONFIRM":
                        String VerificationCode=myDialogClass.getVerificationCode();
                        SignInHelperClass.PhoneVerificationComplete(VerificationCode);
                        break;
                    case "RESEND":
                        SignInHelperClass.ResendPhoneVerificationCode(PhoneString);
                        break;
                    case "CANCEL":
                        CancelDialog();
                        break;
                }
                break;
        }
    }
    @Override
    public void MyCustomDialogGetData(Map<Integer, String> integerStringMap) { }
}

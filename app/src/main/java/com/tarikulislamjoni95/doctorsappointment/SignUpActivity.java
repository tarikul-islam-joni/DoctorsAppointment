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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,MyCommunicator, MyDialogClass.MyAlertDialogCommunicator
{
    private FirebaseDatabaseHelper firebaseDatabaseHelper;
    private MyToastClass myToast;
    private SignInOrSignUpHelperClass SignUpClass;
    private MyDialogClass myDialog;

    private Activity activity;
    private Intent intent;
    private AlertDialog dialog;

    private int VALIDITY_COLOR;
    private boolean ChooseEmailOrPhone=true,Patient=true,Doctor=false;

    private String EmailString,PasswordString,RetypePasswordString,PhoneString;
    //UI Variable
    private LinearLayout SignUpEmailSection,SignUpPhoneSection;
    private CircleImageView SignUpTypeCiv;
    private EditText SignUpEmailEt,SignUpPasswordEt,SignUpRetypePasswordEt,SignupPhoneEt;
    private Button SignUpPatientBtn,SignUpDoctorBtn,SignUpEmailSignUpBtn,SignUpPhoneSignUpBtn,SignUpChooseEmailOrPhoneBtn,SignUpGoogleSignUpBtn,SignUpAlreadyHasAnAccountSignInBtn;
    private LoginButton SignUpFacebookSignUpBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Initialization();
        InitializationUI();
        InitializationClass();
    }
    private void Initialization()
    {
        activity=SignUpActivity.this;
    }
    private void InitializationUI()
    {
        SignUpTypeCiv=findViewById(R.id.signup_type_civ);
        SignUpEmailSection=findViewById(R.id.email_sign_up_section);
        SignUpPhoneSection=findViewById(R.id.phone_sign_up_section);

        SignUpEmailEt=findViewById(R.id.signup_email_et);
        SignUpEmailEt.addTextChangedListener(new MyTextWatcher(activity,CONST_VARIABLE.EMAIL_VALIDITY,R.id.signup_email_et));
        SignUpPasswordEt=findViewById(R.id.signup_password_et);
        SignUpPasswordEt.addTextChangedListener(new MyTextWatcher(activity,CONST_VARIABLE.PASSWORD_VALIDITY,R.id.signup_password_et));
        SignUpRetypePasswordEt=findViewById(R.id.signup_retype_password_et);
        SignUpRetypePasswordEt.addTextChangedListener(new MyTextWatcher(activity,CONST_VARIABLE.PASSWORD_VALIDITY,R.id.signup_retype_password_et));
        SignupPhoneEt=findViewById(R.id.signup_phone_et);
        SignupPhoneEt.addTextChangedListener(new MyTextWatcher(activity,CONST_VARIABLE.PHONE_VALIDITY,R.id.signup_phone_et));

        SignUpPatientBtn=findViewById(R.id.signup_patient_btn);
        SignUpPatientBtn.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        SignUpPatientBtn.setOnClickListener(this);
        SignUpDoctorBtn=findViewById(R.id.signup_doctor_btn);
        SignUpDoctorBtn.setOnClickListener(this);
        SignUpEmailSignUpBtn=findViewById(R.id.signup_email_signup_btn);
        SignUpEmailSignUpBtn.setOnClickListener(this);
        SignUpPhoneSignUpBtn=findViewById(R.id.signup_send_verification_code_btn);
        SignUpPhoneSignUpBtn.setOnClickListener(this);
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
        VALIDITY_COLOR=getResources().getColor(R.color.colorGreen);
        myToast=new MyToastClass(activity);
        SignUpClass=new SignInOrSignUpHelperClass(activity);
        myDialog=new MyDialogClass(activity);
        firebaseDatabaseHelper=new FirebaseDatabaseHelper(activity);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.signup_patient_btn:
                Patient=true;
                Doctor=false;
                SignUpPatientBtn.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                SignUpDoctorBtn.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                break;
            case R.id.signup_doctor_btn:
                Patient=false;
                Doctor=true;
                SignUpDoctorBtn.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                SignUpPatientBtn.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                break;
            case R.id.signup_email_signup_btn:
                EmailSignUp();
                break;
            case R.id.signup_send_verification_code_btn:
                PhoneSignUp();
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
            EmailString=SignUpEmailEt.getText().toString();
            PasswordString=SignUpPasswordEt.getText().toString();
            SignUpClass.EmailSignInOrUp(CONST_VARIABLE.SIGN_UP_ACTIVITY,EmailString,PasswordString);
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
            PhoneString="+88"+SignupPhoneEt.getText().toString();
            SignUpClass.PhoneSignIn(PhoneString);
        }
    }
    private void GoogleSignUp()
    {
        SignUpClass.GoogleSignIn();
    }
    private void FacebookSignUp()
    {
        SignUpClass.FacebookSignIn(R.id.signup_facebook_signup_btn);
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
            SignUpChooseEmailOrPhoneBtn.setText("Phone Sign Up");
            ChooseEmailOrPhone=false;
        }
        else
        {
            SignUpEmailSection.setVisibility(View.VISIBLE);
            SignUpPhoneSection.setVisibility(View.GONE);
            SignUpChooseEmailOrPhoneBtn.setText("Email Sign Up");
            ChooseEmailOrPhone=true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SignUpClass.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void Communicator(String FromWhichMethod, boolean result)
    {
        switch (FromWhichMethod)
        {
            case CONST_VARIABLE.EMAIL_SIGN_UP:
                if (result)
                {
                    CreateUserData();
                }
                else
                {
                    myToast.SToast("Failed to create the account");
                }
                break;
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
                    CreateUserData();
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
                    CreateUserData();
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
                    CreateUserData();
                }
                else
                {
                    myToast.SToast("Failed to sign in...");
                }
                break;
            case "ProfileDataSaving":
                if (result)
                {
                    intent=new Intent(activity,ProfileCompleteOneActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else
                {
                    myToast.SToast("Failed to create the account");
                    FirebaseAuth.getInstance().getCurrentUser().delete();
                }


        }
    }
    private void ShowPhoneVerificationDialog()
    {
        int[] btn={R.id.confirm_btn,R.id.resend_btn,R.id.cancel_btn};
        int[] Et={R.id.verification_code_et};
        String[] information={"ConfirmBtn","ResendBtn","CancelBtn"};
        dialog=myDialog.MyCustomDialog(R.layout.verification_code,btn,Et,information,"Verification Code","Please complete verification step");
        dialog.setCancelable(false);
        dialog.show();
    }
    private void CancelDialog()
    {
        dialog.cancel();
    }
    private void CreateUserData()
    {

        if (Patient)
        {
            firebaseDatabaseHelper.SaveAccountDataIntoDatabase("Patient",
                    FirebaseAuth.getInstance().getCurrentUser().getUid().toString(),"","","","","","","","");
        }
        if (Doctor)
        {
            firebaseDatabaseHelper.SaveAccountDataIntoDatabase("Doctor",
                    FirebaseAuth.getInstance().getCurrentUser().getUid().toString(),"","","","","","","","");
        }
    }

    @Override
    public void DialogResultSuccess(String WhichField, String result)
    {
        switch (WhichField)
        {
            case CONST_VARIABLE.PHONE_SIGN_IN:
                switch (result)
                {
                    case "CONFIRM":
                        String VerificationCode=myDialog.getVerificationCode();
                        SignUpClass.PhoneVerificationComplete(VerificationCode);
                        break;
                    case "RESEND":
                        SignUpClass.ResendPhoneVerificationCode(PhoneString);
                        break;
                    case "CANCEL":
                        CancelDialog();
                        break;
                }
                break;
        }
    }

    @Override
    public void MyCustomDialogGetData(Map<Integer, String> integerStringMap) {

    }
}

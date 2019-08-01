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
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDataModel;
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
    private MyToastClass myToast;
    private MyLoadingDailog myLoadingDailog;

    private SignInOrSignUpHelperClass SignUpHelperClass;
    private AccountStatusDB accountStatusDB;

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
    //Initialization of some important variable
    private void Initialization()
    {
        activity=SignUpActivity.this;
        myAuth=FirebaseAuth.getInstance();
        myRef= FirebaseDatabase.getInstance().getReference();
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
        SignUpHelperClass=new SignInOrSignUpHelperClass(activity);
        myLoadingDailog=new MyLoadingDailog(activity,R.drawable.spinner);
        accountStatusDB=new AccountStatusDB(activity);
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
            ShowLoadingDialog();
            EmailString=SignUpEmailEt.getText().toString();
            PasswordString=SignUpPasswordEt.getText().toString();

            //Account Creating Part Started and Check the result in implementation section
            SignUpHelperClass.EmailSignInOrUp(VARConst.SIGN_UP_ACTIVITY,EmailString,PasswordString);
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
            ShowLoadingDialog();
            PhoneString=SignupPhoneEt.getText().toString();
            //Account Creating Part Started and Check the result in implementation section
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
    //*** Phone signup resend code option delay method for several seconds ***//
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
            ShowLoadingDialog();
            String VerificationCodeString=SignUpPhoneVerificationCodeEt.getText().toString();

            //Account Creating Part Started and Check the result in implementation section
            SignUpHelperClass.PhoneVerificationComplete(VerificationCodeString);
        }
    }
    //*** Google SignUp method starting from here and other proccess completing in implementation section ***//
    private void GoogleSignUp()
    {
        //Account Creating Part Started and Check the result in implementation section
        SignUpHelperClass.GoogleSignIn();
    }
    //*** Facebook SignUp method starting from here and other proccess completing in implementation section ***//
    private void FacebookSignUp()
    {
        //Account Creating Part Started and Check the result in implementation section
        SignUpHelperClass.FacebookSignIn(R.id.facebook_sign_in_or_up_btn);
    }

    //*** Goto Sign in activity directly ***//
    private void GotoSignInActivity()
    {
        intent=new Intent(activity,SignInActivity.class);
        startActivity(intent);
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

    ///**** This method is called from AccountStatusSavingResult method ****///
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SignUpHelperClass.onActivityResult(requestCode,resultCode,data);
    }




    //Account Creating Part interface implementation section
    ///************************Account Creation Interface***********************///
    @Override
    public void AccountCreationResult(String FromWhichMethod, boolean result)
    {
        switch (FromWhichMethod)
        {
            case VARConst.EMAIL_SIGN_UP:
                if (result)
                {
                    myToast.LToast("Account successfully created\nPlease verify for next sign in...");
                    SaveAccountInformationIntoDatabase();
                }
                else
                {
                    CancelLoadingDialog();
                    myToast.LToast("Account creation failed.");
                }
                break;
            case VARConst.PHONE_SIGN_IN:
                if (result)
                {
                    CancelLoadingDialog();
                    myToast.LToast("Verification code sent to "+PhoneString);
                }
                else
                {
                    CancelLoadingDialog();
                    myToast.LToast("Verification code couldn't sent to "+PhoneString);
                }
                break;
            case VARConst.PHONE_SIGN_IN_STATUS:
                if (result)
                {
                    SaveAccountInformationIntoDatabase();
                }
                else
                {
                    CancelLoadingDialog();
                    myToast.LToast("Verification code not matched !");
                }
                break;
            case VARConst.GOOGLE_SIGN_IN_STATUS:
                if (result)
                {
                    SaveAccountInformationIntoDatabase();
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
                    SaveAccountInformationIntoDatabase();
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

    //**** Save Account information into database ****//
    //*** This method is called from AccountCreationResult method ***///
    private void SaveAccountInformationIntoDatabase()
    {
        accountStatusDB.GetUIDAccountStatusData();
    }

    ///*******************Account Status DB Interface*********************///
    @Override
    public void GetAccountStatus(boolean result, ArrayList<AccountStatusDataModel> arrayList)
    {
        if (result)
        {
            CancelLoadingDialog();
            myToast.LToast("Account is already created!Please sign in...");
        }
        else
        {
            accountStatusDB.SaveIntoAccountStatusDBFromUser(AccoutTypeString,false,false,false);
        }
    }

    @Override
    public void AccountStatusSavingResult(boolean result)
    {
        CancelLoadingDialog();
        if (result)
        {
            GotoNextActivity();
        }
        else
        {
            SignUpHelperClass.DeleteAccount();
        }
    }
}

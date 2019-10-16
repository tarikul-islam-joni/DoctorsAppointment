package com.tarikulislamjoni95.doctorsappointment.AccountPart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;
import com.tarikulislamjoni95.doctorsappointment.AdminPart.AdminMainActivity;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBHelper;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.MyAuthenticationClass;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.DoctorMainActivity;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.EditDoctorProfileActivity;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.EditDoctorSecureInfoActivity;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountCreationInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.EditPatientProfileActivity;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.EditPatientSecureInfoActivity;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.PatientMainActivity;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EntranceActivity extends AppCompatActivity implements View.OnClickListener ,
        AccountCreationInterface, GetDataFromDBInterface {

    //Database Class Variable
    private MyAuthenticationClass myAuthenticationClass;
    private AccountStatusDB accountStatusDB;
    private DBHelper dbHelper;

    //Class Variable
    private MyToastClass myToast;
    private MyLoadingDailog myLoadingDailog;

    //Important component
    private Activity activity;
    private Animation pushupin;

    //Dialog Variable
    private AlertDialog AccountStatusDialog;
    private AlertDialog VerificationDialog;
    private LinearLayout DialogEmailSection;
    private ProgressBar progressBar;
    private TextView CountDownTv;
    private Button ResendBtn;
    private Button NextBtn;
    private Button CancelBtn;
    private EditText VerificationEt;
    private ImageView VerificationBtn;


    //Primitive Variable
    private String UID;
    private int VALIDITY_COLOR;
    private String EmailString,PasswordString,PhoneString,AccoutTypeString=DBConst.Patient;
    private int RESEND_PHONE_COUNTER=2;
    private int RESEND_EMAIL_COUNTER=2;
    private int COUNT_DOWN=60;
    private int SET_DELAY_TIMER=60000;
    //UI Variable
    private LinearLayout EmailSection,PhoneSection;
    private CircleImageView AccountTypeCiv;
    private EditText EmailEt,PasswordEt,PhoneEt;
    private Button PatientBtn,DoctorBtn;
    private Button EmailSectionBtn,PhoneSectionBtn;
    private Button EmailEntranceBtn,PhoneEntranceBtn;
    private TextView ForgetPasswordTv;
    private Button GoogleEntranceBtn;
    private LoginButton FacebookEntranceBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_entrance_layout);
        Initialization();
        InitializationUI();
        InitializationClass();
        InitializationDB();
    }

    //Initialization of some important variable
    private void Initialization()
    {
        activity= EntranceActivity.this;
        VALIDITY_COLOR= ContextCompat.getColor(activity,R.color.colorGreen);
        pushupin = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.push_up_in);

    }
    //Initialization of UI variable
    private void InitializationUI()
    {
        AccountTypeCiv=findViewById(R.id.image_civ);

        EmailSection=findViewById(R.id.email_section);
        PhoneSection=findViewById(R.id.phone_section);

        EmailEt=findViewById(R.id.email_et);
        EmailEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        EmailEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.EMAIL_VALIDITY,R.id.email_et));

        PasswordEt=findViewById(R.id.password_et);
        PasswordEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        PasswordEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.PASSWORD_VALIDITY,R.id.password_et));

        PhoneEt=findViewById(R.id.phone_et);
        PhoneEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        PhoneEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.PHONE_VALIDITY,R.id.phone_et));

        PatientBtn=findViewById(R.id.patient_btn);
        PatientBtn.setOnClickListener(this);

        DoctorBtn=findViewById(R.id.doctor_btn);
        DoctorBtn.setOnClickListener(this);

        EmailEntranceBtn=findViewById(R.id.email_entrance_btn);
        EmailEntranceBtn.setOnClickListener(this);

        PhoneEntranceBtn=findViewById(R.id.phone_entrance_btn);
        PhoneEntranceBtn.setOnClickListener(this);

        EmailSectionBtn=findViewById(R.id.email_section_btn);
        EmailSectionBtn.setOnClickListener(this);

        PhoneSectionBtn=findViewById(R.id.phone_section_btn);
        PhoneSectionBtn.setOnClickListener(this);

        GoogleEntranceBtn=findViewById(R.id.google_entrance_btn);
        GoogleEntranceBtn.setOnClickListener(this);

        FacebookEntranceBtn=findViewById(R.id.facebook_entrance_btn);
        FacebookEntranceBtn.setOnClickListener(this);

        ForgetPasswordTv=findViewById(R.id.forgot_text_tv);
        ForgetPasswordTv.setOnClickListener(this);

    }
    //Initialization of class variable
    private void InitializationClass()
    {
        myToast=new MyToastClass(activity);
        myLoadingDailog=new MyLoadingDailog(activity,R.drawable.spinner);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.patient_btn:
                AccoutTypeString=DBConst.Patient;
                AccountTypeCiv.setImageResource(R.drawable.patient);
                PatientBtn.setBackground(ContextCompat.getDrawable(activity,R.drawable.button_background_3));
                PatientBtn.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(activity,R.drawable.select),null);
                DoctorBtn.setBackground(ContextCompat.getDrawable(activity,R.drawable.button_background_1));
                DoctorBtn.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                break;
            case R.id.doctor_btn:
                AccoutTypeString=DBConst.Doctor;
                AccountTypeCiv.setImageResource(R.drawable.male_doc);
                DoctorBtn.setBackground(ContextCompat.getDrawable(activity,R.drawable.button_background_3));
                DoctorBtn.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(activity,R.drawable.select),null);
                PatientBtn.setBackground(ContextCompat.getDrawable(activity,R.drawable.button_background_1));
                PatientBtn.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                break;
            case R.id.email_entrance_btn:
                EmailSignUp();
                break;
            case R.id.phone_entrance_btn:
                PhoneSignUp();
                break;
            case R.id.google_entrance_btn:
                EmailSectionBtn.setBackground(getResources().getDrawable(R.drawable.button_background_1));
                PhoneSectionBtn.setBackground(getResources().getDrawable(R.drawable.button_background_1));
                GoogleEntranceBtn.setBackground(getResources().getDrawable(R.drawable.button_background_2));
                GoogleSignUp();
                break;
            case R.id.facebook_entrance_btn:
                EmailSectionBtn.setBackground(getResources().getDrawable(R.drawable.button_background_1));
                PhoneSectionBtn.setBackground(getResources().getDrawable(R.drawable.button_background_1));
                GoogleEntranceBtn.setBackground(getResources().getDrawable(R.drawable.button_background_1));
                FacebookSignUp();
                break;
            case R.id.forgot_text_tv:
                ForgetPasswordMethod();
                break;
            case R.id.email_section_btn:
                EmailEt.requestFocus();
                PhoneSection.setVisibility(View.GONE);
                EmailSection.setVisibility(View.VISIBLE);
                EmailSection.startAnimation(pushupin);

                EmailSectionBtn.setBackground(getResources().getDrawable(R.drawable.button_background_2));
                PhoneSectionBtn.setBackground(getResources().getDrawable(R.drawable.button_background_1));
                GoogleEntranceBtn.setBackground(getResources().getDrawable(R.drawable.button_background_1));

                break;
            case R.id.phone_section_btn:
                PhoneEt.requestFocus();
                EmailSection.setVisibility(View.GONE);
                PhoneSection.setVisibility(View.VISIBLE);
                PhoneSection.startAnimation(pushupin);

                EmailSectionBtn.setBackground(getResources().getDrawable(R.drawable.button_background_1));
                PhoneSectionBtn.setBackground(getResources().getDrawable(R.drawable.button_background_2));
                GoogleEntranceBtn.setBackground(getResources().getDrawable(R.drawable.button_background_1));
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
        if (EmailEt.getCurrentTextColor()!=VALIDITY_COLOR)
        {
            EmailEt.setError("Email is invalid");
        }
        else if (PasswordEt.getCurrentTextColor()!=VALIDITY_COLOR)
        {
            PasswordEt.setError("Password atleast 6 digit");
        }
        else
        {
            EmailString=EmailEt.getText().toString();
            PasswordString=PasswordEt.getText().toString();

            //Database Call For Email SignUp
            CallDBForEmailSignIn(EmailString,PasswordString);
        }
    }

    //*** Phone SignUp method starting from here and other proccess completing in implementation section ***//
    private void PhoneSignUp()
    {
        if (PhoneEt.getCurrentTextColor()!=VALIDITY_COLOR)
        {
            PhoneEt.setError("Phone number is incorrect");
        }
        else
        {
            PhoneString=PhoneEt.getText().toString();

            //Call Database For Phone Sign Up
            CallDBForPhoneSignUp(PhoneString);
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
        CallDBForFacebookSignUp(FacebookEntranceBtn.getId());
    }

    private void ForgetPasswordMethod()
    {
        if (EmailEt.getText().toString().isEmpty())
        {
            myToast.LToast("Input registered email without password and the click forgot password for receiving the re-edit password link");
        }
        else
        {
            CallDBForSendingForgetPasswordLink(EmailEt.getText().toString());
        }
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
            case VARConst.EMAIL_SIGN_IN:
                if (result)
                {
                    CallDBForCheckEmailVerification();
                }
                else
                {
                    CallDBForEmailSignUp(EmailString,PasswordString);
                }
                break;
            case VARConst.EMAIL_VERIFICATION_STATUS:
                if (result)
                {
                    CancelVerificationDialog();
                    CallDBForAccountUID();
                }
                else
                {
                    if (VerificationDialog==null)
                    {
                        ShowResendEmailVerificationMethod(EmailEt.getText().toString());
                    }
                    else if (!VerificationDialog.isShowing())
                    {
                        ShowResendEmailVerificationMethod(EmailEt.getText().toString());
                    }
                }
                break;
            case VARConst.EMAIL_SIGN_UP:
                if (result)
                {
                    CallDBForEmailSignIn(EmailString,PasswordString);
                }
                else
                {
                    myToast.LToast("Entrance failure...Please try again...");
                }
                break;
            case VARConst.PHONE_SIGN_IN:
                if (result)
                {
                    myToast.LToast("Verification code sent to "+PhoneString);
                    ShowPhoneVerificationDialog(PhoneString);
                }
                else
                {
                    myToast.LToast("Verification code couldn't sent to "+PhoneString);
                }
                break;
            case VARConst.PHONE_SIGN_IN_STATUS:
                if (result)
                {
                    CancelVerificationDialog();
                    CallDBForAccountUID();
                }
                else
                {
                    myToast.LToast("Verification code not matched !");
                }
                break;
            case VARConst.GOOGLE_SIGN_IN_STATUS:
                if (result)
                {
                    CallDBForAccountUID();
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
                    CallDBForAccountUID();
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

    private void ShowPhoneVerificationDialog(final String PhoneString)
    {
        if (RESEND_PHONE_COUNTER>=0)
        {
            final AlertDialog.Builder builder=new AlertDialog.Builder(activity,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            View view= LayoutInflater.from(activity).inflate(R.layout.app_account_verification_layout,null,false);
            builder.setView(view);
            builder.setCancelable(false);
            VerificationDialog=builder.create();
            ShowVerificationDialog();
            LinearLayout PhoneSection=view.findViewById(R.id.phone_section);
            PhoneSection.setVisibility(View.VISIBLE);
            TextView PhoneTv=view.findViewById(R.id.phone_tv);
            PhoneTv.setText(PhoneString);

            progressBar=view.findViewById(R.id.progress_bar);
            CountDownTv=view.findViewById(R.id.count_down_tv);
            ResendBtn=view.findViewById(R.id.resend_btn);
            ResendDelaySytem();
            ResendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CallDBForPhoneSignUp(PhoneString);
                    ResendDelaySytem();
                }
            });

            VerificationEt=view.findViewById(R.id.verification_et);
            VerificationEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
            VerificationBtn=view.findViewById(R.id.verification_btn);
            VerificationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!VerificationEt.getText().toString().isEmpty())
                    {
                        CallDBForPhoneVerification(VerificationEt.getText().toString());
                    }
                }
            });

            Button CancelBtn=view.findViewById(R.id.cancel_btn);
            CancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CallDBForSignOut();
                    CancelVerificationDialog();
                    startActivity(new Intent(EntranceActivity.this,EntranceActivity.class));
                }
            });
        }
        else
        {
            myToast.LToast("Verification resending limit exceeded");
        }

    }

    private void ShowVerificationDialog()
    {
        if (VerificationDialog!=null)
        {
            if (!VerificationDialog.isShowing())
            {
                VerificationDialog.show();
            }
        }
    }
    private void CancelVerificationDialog()
    {
        if (VerificationDialog!=null)
        {
            if (VerificationDialog.isShowing())
            {
                VerificationDialog.cancel();
            }
        }
    }
    private void ResendDelaySytem()
    {
        progressBar.setMax(60);
        ResendBtn.setVisibility(View.GONE);
        CountDownTv.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        new CountDownTimer(SET_DELAY_TIMER,1000)
        {
            @Override
            public void onTick(long l)
            {
                COUNT_DOWN--;
                progressBar.setProgress(60-(int)l/1000);
                CountDownTv.setText(String.valueOf(COUNT_DOWN));
            }

            @Override
            public void onFinish()
            {
                SET_DELAY_TIMER=60000;
                COUNT_DOWN=60;
                progressBar.setVisibility(View.GONE);
                CountDownTv.setVisibility(View.GONE);
                ResendBtn.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    ///**** Resending Email verification code Process get started *****//
    private void ShowResendEmailVerificationMethod(String EmailString)
    {
        if (RESEND_EMAIL_COUNTER>0)
        {
            final AlertDialog.Builder builder=new AlertDialog.Builder(activity,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            View view= LayoutInflater.from(activity).inflate(R.layout.app_account_verification_layout,null,false);
            builder.setView(view);
            builder.setCancelable(false);
            VerificationDialog=builder.create();
            ShowVerificationDialog();
            DialogEmailSection=view.findViewById(R.id.email_section);
            DialogEmailSection.setVisibility(View.VISIBLE);
            TextView EmailTv=view.findViewById(R.id.email_tv);
            EmailTv.setText(EmailString);

            progressBar=view.findViewById(R.id.email_progress_bar);
            CountDownTv=view.findViewById(R.id.email_count_down_tv);
            ResendBtn=view.findViewById(R.id.email_resend_btn);
            ResendDelaySytem();
            ResendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CallDBForResendEmailVerification();
                    ResendDelaySytem();
                }
            });

            NextBtn=view.findViewById(R.id.goto_next_btn);
            NextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("myError","1 Next Button Clicked");
                    CallDBForEmailSignIn(EmailEt.getText().toString(),PasswordEt.getText().toString());
                }
            });

            CancelBtn=view.findViewById(R.id.cancel_btn);
            CancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CallDBForSignOut();
                    CancelVerificationDialog();
                    startActivity(new Intent(EntranceActivity.this,EntranceActivity.class));
                }
            });
        }
        else
        {
            myToast.LToast("Verification resending limit exceeded\nCheck your email and verify !!! ");
        }
    }

    private void SetUID(String UID)
    {
        this.UID=UID;
    }
    private void GetAccountUIDFromDB(String Result,String  UID)
    {
        //Call Database For Account Status
        if (Result.matches(DBConst.NOT_NULL_USER))
        {
            SetUID(UID);
            CallDBForAccountExistanceCheck(UID);
        }
    }


    private void GetAccountExistanceStatus(HashMap<String,Object> DataHashMap)
    {
        CancelLoadingDialog();
        String AccountExistanceResult=(String) DataHashMap.get(DBConst.RESULT);
        if (AccountExistanceResult.matches(DBConst.DATA_NOT_EXIST))
        {
            CallDBForSavingAccountStatus(UID);
        }
        else if (AccountExistanceResult.matches(DBConst.DATA_EXIST))
        {
            if (!(boolean)DataHashMap.get(DBConst.AccountLockState))
            {
                String AccountType=(String)DataHashMap.get(DBConst.AccountType);
                if (AccountType.matches(DBConst.Patient))
                {
                    //Goto Patient Part
                    if (!(boolean)DataHashMap.get(DBConst.AccountCompletion))
                    {
                        StartActivity(new Intent(activity, EditPatientProfileActivity.class));
                    }
                    else if (!(boolean)DataHashMap.get(DBConst.AccountValidity))
                    {
                        StartActivity(new Intent(activity, EditPatientSecureInfoActivity.class));
                    }
                    else if ((boolean)DataHashMap.get(DBConst.AccountCompletion) && (boolean)DataHashMap.get(DBConst.AccountValidity))
                    {
                        StartActivity(new Intent(activity, PatientMainActivity.class));
                    }
                }
                else if (AccountType.matches(DBConst.Doctor))
                {
                    //Goto Patient Part
                    if (!(boolean)DataHashMap.get(DBConst.AccountCompletion))
                    {
                        StartActivity(new Intent(activity, EditDoctorProfileActivity.class));
                    }
                    else if (!(boolean)DataHashMap.get(DBConst.AccountValidity))
                    {
                        StartActivity(new Intent(activity, EditDoctorSecureInfoActivity.class));
                    }
                    else if ((boolean)DataHashMap.get(DBConst.AccountCompletion) && (boolean)DataHashMap.get(DBConst.AccountValidity))
                    {
                        StartActivity(new Intent(activity, DoctorMainActivity.class));
                    }
                }
                else if (AccountType.matches(DBConst.Admin))
                {
                    StartActivity(new Intent(activity, AdminMainActivity.class));
                }
            }
            else
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                builder.setTitle("App Entrance Status");
                builder.setMessage("Your account was locked automatically\nHave patience,it will be unlocked by admin as soon as possible.\nTry again latter...");
                builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        AccountStatusDialog.dismiss();
                    }
                });
                AccountStatusDialog=builder.create();
                AccountStatusDialog.show();
                CallDBForSignOut();
            }
        }
        else
        {
            CallDBForSignOut();
        }
    }


    private void StartActivity(Intent intent)
    {
        CancelLoadingDialog();
        CancelVerificationDialog();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void GetAccountStatusSavingResult(String AccountStatusSavingResult)
    {
        CancelLoadingDialog();
        if (AccountStatusSavingResult.matches(DBConst.SUCCESSFUL))
        {
            CallDBForAccountExistanceCheck(UID);
        }
        else if (AccountStatusSavingResult.matches(DBConst.UNSUCCESSFUL))
        {
            CallDBForSignOut();
            startActivity(new Intent(EntranceActivity.this,EntranceActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CancelLoadingDialog();
        CancelVerificationDialog();
    }

    ///******************************************************************************************///
    ///*****************************************Database Part************************************///
    ///******************************************************************************************///
    private void InitializationDB()
    {
        myAuthenticationClass=new MyAuthenticationClass(activity);
        accountStatusDB=new AccountStatusDB(activity);
        dbHelper=new DBHelper(activity);
    }


    ///**************************************Database Calling Method*****************************///
    private void CallDBForEmailSignUp(String emailString, String passwordString)
    {
        ShowLoadingDialog();
        myAuthenticationClass.EmailSignInOrUp(VARConst.SIGN_UP_ACTIVITY,emailString,passwordString);
    }
    private void CallDBForEmailSignIn(String emailString, String passwordString)
    {
        ShowLoadingDialog();
        myAuthenticationClass.EmailSignInOrUp(VARConst.SIGN_IN_ACTIVITY,emailString,passwordString);
    }
    private void CallDBForPhoneSignUp(String phoneString)
    {
        ShowLoadingDialog();
        myAuthenticationClass.PhoneSignIn(phoneString);
    }
    private void CallDBForPhoneVerification(String VerificationCodeString)
    {
        ShowLoadingDialog();
        myAuthenticationClass.PhoneVerificationComplete(VerificationCodeString);
    }
    private void CallDBForFacebookSignUp(int FacebookButtonId)
    {
        myAuthenticationClass.FacebookSignIn(FacebookButtonId);
    }
    private void CallDBForSendingForgetPasswordLink(String ForgetEmailString)
    {
        myAuthenticationClass.ForgetPasswordLinkToEmail(ForgetEmailString);
    }
    private void CallDBForGoogleSignUp()
    {
        myAuthenticationClass.GoogleSignIn();
    }
    private void CallDBForSendingOnActivityResult(int requestCode, int resultCode, Intent data)
    {
        myAuthenticationClass.onActivityResult(requestCode,resultCode,data);
    }

    private void CallDBForCheckEmailVerification()
    {
        ShowLoadingDialog();
        myAuthenticationClass.CheckEmailVerificationStatus();
    }
    private void CallDBForResendEmailVerification()
    {
        ShowLoadingDialog();
        myAuthenticationClass.ResendEmailVerification();
    }

    private void CallDBForAccountUID()
    {
        dbHelper.GetUID();
    }
    private void CallDBForAccountExistanceCheck(String UID)
    {
        ShowLoadingDialog();
        accountStatusDB.GetAccountStatusData(UID);
    }
    private void CallDBForSavingAccountStatus(String UID)
    {
        ShowLoadingDialog();
        accountStatusDB.SaveIntoAccountStatusDB(UID,AccoutTypeString,false,false,false);
    }
    private void CallDBForSignOut()
    {
        dbHelper.SignOut();
    }
    ///************************Account Creation Interface***********************///
    @Override
    public void AccountCreationResult(String FromWhichMethod, boolean result)
    {
        CancelLoadingDialog();
        GetSignUpStatus(FromWhichMethod,result);
    }

    ///*******************Account Status DB Interface*********************///
    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        Log.d("EntranceActivity",WhichDB+" ::: "+DataHashMap.toString());
        if (WhichDB.matches(DBConst.GetAccountUID))
        {
            GetAccountUIDFromDB((String)DataHashMap.get(DBConst.RESULT),(String) DataHashMap.get(DBConst.UID));
        }
        else if (WhichDB.matches(DBConst.GetAccountStatusDB))
        {
            GetAccountExistanceStatus(DataHashMap);
        }
        else if (WhichDB.matches(DBConst.SaveAccountStatusDB))
        {
            GetAccountStatusSavingResult((String) DataHashMap.get(DBConst.RESULT));
        }
    }

    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList)
    {
    }
}

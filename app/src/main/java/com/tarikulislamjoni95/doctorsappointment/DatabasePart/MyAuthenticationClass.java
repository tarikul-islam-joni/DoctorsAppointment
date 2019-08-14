package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountCreationInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;

public class MyAuthenticationClass
{
    private MyToastClass myToast;
    private AccountCreationInterface accountCreationInterface;

    private AuthCredential credential;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks PhoneCallBack;
    private CallbackManager FacebookCallBack;
    private Intent intent;
    private Activity activity;
    private String EmailString,PasswordString,PhoneString,PhoneVerificationCodeString,FromWhichMethod;
    private LoginButton SignInFacebookSignInBtn;
    //Constructor
    public MyAuthenticationClass(Activity activity)
    {
        this.activity=activity;
        myToast=new MyToastClass(activity);
        accountCreationInterface =(AccountCreationInterface) activity;
    }
    ///*************************Email SignIn Or SignUp Section Starting*************************///
    public void EmailSignInOrUp(String WhichActivity,String EmailString,String PasswordString)
    {
        this.EmailString=EmailString;
        this.PasswordString=PasswordString;
        if (WhichActivity.matches(VARConst.SIGN_IN_ACTIVITY))
        {
            EmailSignInMethod();
        }
        if (WhichActivity.matches(VARConst.SIGN_UP_ACTIVITY))
        {
            EmailSignUpMethod();
        }
    }
    public void ResendEmailVerification()
    {
        ResendEmailVerificationMethod();
    }
    public void CheckEmailVerificationStatus()
    {
        CheckEmailVerificationStatusMethod();
    }
    private void EmailSignUpMethod()
    {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(EmailString,PasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                    accountCreationInterface.AccountCreationResult(VARConst.EMAIL_SIGN_UP,true);
                }
                else
                {
                    accountCreationInterface.AccountCreationResult(VARConst.EMAIL_SIGN_UP,false);
                }
            }
        });
    }
    //Email SignIn Method
    private void EmailSignInMethod()
    {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(EmailString,PasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    accountCreationInterface.AccountCreationResult(VARConst.EMAIL_SIGN_IN,true);
                }
                else
                {
                    accountCreationInterface.AccountCreationResult(VARConst.EMAIL_SIGN_IN,false);
                }
            }
        });
    }
    private void ResendEmailVerificationMethod()
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        accountCreationInterface.AccountCreationResult(VARConst.RESEND_EMAIL_VERIFICATION_STATUS,true);
                    }
                    else
                    {
                        accountCreationInterface.AccountCreationResult(VARConst.RESEND_EMAIL_VERIFICATION_STATUS,false);
                    }
                }
            });
        }
    }
    private void CheckEmailVerificationStatusMethod()
    {
        Log.d("myError","CheckEmailVerificationStatusMethod : "+FirebaseAuth.getInstance().getCurrentUser().isEmailVerified());
        if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
        {
            accountCreationInterface.AccountCreationResult(VARConst.EMAIL_VERIFICATION_STATUS,true);
        }
        else
        {
            accountCreationInterface.AccountCreationResult(VARConst.EMAIL_VERIFICATION_STATUS,false);
        }
    }
    ///*************************Email SignIn Or SignUp Section Ending*************************///

    ///*************************Phone SignIn Or SignUp Section Starting*************************///
    //Phone SignIn Method
    public void PhoneSignIn(String PhoneString)
    {
        this.PhoneString="+88"+PhoneString;
        InitializationPhoneCallBack();
        PhoneSignInMethod();
    }
    private void PhoneSignInMethod()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(PhoneString,60, TimeUnit.SECONDS,activity,PhoneCallBack);
    }
    private void InitializationPhoneCallBack()
    {
        PhoneCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
                credential=(AuthCredential) phoneAuthCredential;
                accountCreationInterface.AccountCreationResult(VARConst.PHONE_SIGN_IN,true);
                FirebaseAutheticationWithCredential(VARConst.PHONE_SIGN_IN_STATUS,credential);
            }
            @Override
            public void onCodeSent(String ValidationID, PhoneAuthProvider.ForceResendingToken forceResendingToken)
            {
                super.onCodeSent(ValidationID, forceResendingToken);
                SharedPreferences sharedPreferences=activity.getSharedPreferences(VARConst.VALIDATION_ID, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(VARConst.VALIDATION_ID,ValidationID);
                editor.commit();
                accountCreationInterface.AccountCreationResult(VARConst.PHONE_SIGN_IN,true);
            }
            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                accountCreationInterface.AccountCreationResult(VARConst.PHONE_SIGN_IN,false);
            }
        };
    }
    public void PhoneVerificationComplete(String PhoneVerificationCodeString)
    {
        this.PhoneVerificationCodeString=PhoneVerificationCodeString;
        PhoneVerificationCompleteMethod();
    }
    private void  PhoneVerificationCompleteMethod()
    {
        SharedPreferences sharedPreferences=(activity).getSharedPreferences(VARConst.VALIDATION_ID, Context.MODE_PRIVATE);
        String VerificationSavedCode=sharedPreferences.getString(VARConst.VALIDATION_ID,"");
        if (PhoneVerificationCodeString.length()>5  && VerificationSavedCode.length()>5)
        {
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(VerificationSavedCode, PhoneVerificationCodeString);
            credential=(AuthCredential) phoneAuthCredential;
            FirebaseAutheticationWithCredential(VARConst.PHONE_SIGN_IN_STATUS,credential);
        }
        else
        {
            accountCreationInterface.AccountCreationResult(VARConst.PHONE_SIGN_IN_STATUS,true);
        }
    }
    public void ResendPhoneVerificationCode(String PhoneString)
    {
        PhoneSignIn(PhoneString);
    }
    ///*************************Phone SignIn Or SignUp Section Ending*************************///

    ///*******************Google & Facebook SignIn Or SignUp Section Starting*******************///
    public void GoogleSignIn()
    {
        GoogleSignInMethod();
    }
    public void FacebookSignIn(int FacebookSignInId)
    {
        SignInFacebookSignInBtn=activity.findViewById(FacebookSignInId);
        FacebookSignInMethod();
    }
    private void GoogleSignInMethod()
    {
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(activity.getString(R.string.default_web_client_id)).requestEmail().build();
        GoogleSignInClient signInClient= GoogleSignIn.getClient(activity,gso);
        intent=signInClient.getSignInIntent();
        activity.startActivityForResult(intent, VARConst.GOOGLE_SIGN_IN_REQUEST_CODE);
    }
    private void FacebookSignInMethod()
    {
        FacebookCallBack=CallbackManager.Factory.create();
        SignInFacebookSignInBtn.setReadPermissions("email","public_profile");
        SignInFacebookSignInBtn.registerCallback(FacebookCallBack, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                credential= FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                FirebaseAutheticationWithCredential(VARConst.FACEBOOK_SIGN_IN_STATUS,credential);
            }
            @Override
            public void onCancel() { }
            @Override
            public void onError(FacebookException error)
            {
                accountCreationInterface.AccountCreationResult(VARConst.FACEBOOK_SIGN_IN_EXCEEPTION,false);
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode== VARConst.GOOGLE_SIGN_IN_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            try {
                Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account=task.getResult(ApiException.class);
                credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
                FirebaseAutheticationWithCredential(VARConst.GOOGLE_SIGN_IN_STATUS,credential);
            } catch (ApiException e)
            {
                accountCreationInterface.AccountCreationResult(VARConst.GOOGLE_SIGN_IN_EXCEPTION,true);
            }
            return;
        }
        if (resultCode==RESULT_OK && data!=null)
        {
            FacebookCallBack.onActivityResult(requestCode,resultCode,data);
        }
    }
    ///*******************Google & Facebook SignIn Or SignUp Section Starting*******************///

    ///*************************Firebase Authentication Section Starting*************************///
    private void FirebaseAutheticationWithCredential(final String FromWhichMethod, AuthCredential credential)
    {
        this.FromWhichMethod=FromWhichMethod;
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    accountCreationInterface.AccountCreationResult(FromWhichMethod,true);
                }
                else
                {
                    accountCreationInterface.AccountCreationResult(FromWhichMethod,false);
                }
            }
        });
    }
    ///*************************Firebase Authentication Section Starting*************************///

    ///**************************************SignOut And Deletion Start*********************************************///
    public void SignOut()
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            FirebaseAuth.getInstance().signOut();
        }
    }
    public void DeleteAccount()
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            FirebaseAuth.getInstance().getCurrentUser().delete();
        }
    }
    ///**************************************SignOut And Delete End*********************************************///
}

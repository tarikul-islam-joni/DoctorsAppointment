package com.tarikulislamjoni95.doctorsappointment;

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
import com.google.android.gms.common.SignInButton;
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

import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;

public class SignInOrSignUpHelperClass
{
    private FirebaseUser User=null;
    private MyToastClass myToast;
    private MyCommunicator myCommunicator;
    private AuthCredential credential;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks PhoneCallBack;
    private CallbackManager FacebookCallBack;
    private Intent intent;
    private Activity activity;
    private String EmailString,PasswordString,PhoneString,PhoneVerificationCodeString,FromWhichMethod;
    private LoginButton SignInFacebookSignInBtn;
    //Constructor
    public SignInOrSignUpHelperClass(Activity activity)
    {
        this.activity=activity;
        myToast=new MyToastClass(activity);
        myCommunicator=(MyCommunicator)activity;
    }
    ///*************************Email SignIn Or SignUp Section Starting*************************///
    public void EmailSignInOrUp(String WhichActivity,String EmailString,String PasswordString)
    {
        mAuth=FirebaseAuth.getInstance();
        this.EmailString=EmailString;
        this.PasswordString=PasswordString;
        if (WhichActivity.matches(CONST_VARIABLE.SIGN_IN_ACTIVITY))
        {
            EmailSignInMethod();
        }
        if (WhichActivity.matches(CONST_VARIABLE.SIGN_UP_ACTIVITY))
        {
            EmailSignUpMethod();
        }
    }
    public void ResendEmailVerification(FirebaseUser User)
    {
        this.User=User;
        ResendEmailVerificationMethod();
    }
    public void CheckEmailVerificationStatus(FirebaseUser User)
    {
        this.User=User;
        CheckEmailVerificationStatusMethod();
    }
    private void EmailSignUpMethod()
    {
        mAuth.createUserWithEmailAndPassword(EmailString,PasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    mAuth.getCurrentUser().sendEmailVerification();
                    myCommunicator.Communicator(CONST_VARIABLE.EMAIL_SIGN_UP,true);
                }
                else
                {
                    myCommunicator.Communicator(CONST_VARIABLE.EMAIL_SIGN_UP,false);
                }
            }
        });
    }
    //Email SignIn Method
    private void EmailSignInMethod()
    {
        mAuth.signInWithEmailAndPassword(EmailString,PasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    myCommunicator.Communicator(CONST_VARIABLE.EMAIL_SIGN_IN,true);
                }
                else
                {
                    myCommunicator.Communicator(CONST_VARIABLE.EMAIL_SIGN_IN,false);
                }
            }
        });
    }
    private void ResendEmailVerificationMethod()
    {
        User.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    myCommunicator.Communicator(CONST_VARIABLE.RESEND_EMAIL_VERIFICATION_STATUS,true);
                }
                else
                {
                    myCommunicator.Communicator(CONST_VARIABLE.RESEND_EMAIL_VERIFICATION_STATUS,false);
                }
            }
        });
    }
    private void CheckEmailVerificationStatusMethod()
    {
        if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
        {
            myCommunicator.Communicator(CONST_VARIABLE.EMAIL_VERIFICATION_STATUS,true);
        }
        else
        {
            myCommunicator.Communicator(CONST_VARIABLE.EMAIL_VERIFICATION_STATUS,false);
        }
    }
    ///*************************Email SignIn Or SignUp Section Ending*************************///

    ///*************************Phone SignIn Or SignUp Section Starting*************************///
    //Phone SignIn Method
    public void PhoneSignIn(String PhoneString)
    {
        this.PhoneString=PhoneString;
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
                FirebaseAutheticationWithCredential(CONST_VARIABLE.PHONE_SIGN_IN_STATUS,credential);
            }
            @Override
            public void onCodeSent(String ValidationID, PhoneAuthProvider.ForceResendingToken forceResendingToken)
            {
                super.onCodeSent(ValidationID, forceResendingToken);
                SharedPreferences sharedPreferences=activity.getSharedPreferences(CONST_VARIABLE.VALIDATION_ID, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(CONST_VARIABLE.VALIDATION_ID,ValidationID);
                editor.commit();
                myCommunicator.Communicator(CONST_VARIABLE.PHONE_VERIFICATION_MANUAL_COMPLETE,true);
            }
            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                myCommunicator.Communicator(CONST_VARIABLE.PHONE_SIGN_IN_EXCEPTION,true);
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
        SharedPreferences sharedPreferences=(activity).getSharedPreferences(CONST_VARIABLE.VALIDATION_ID, Context.MODE_PRIVATE);
        String VerificationSavedCode=sharedPreferences.getString(CONST_VARIABLE.VALIDATION_ID,"");
        if (PhoneVerificationCodeString.length()>5  && VerificationSavedCode.length()>5)
        {
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(VerificationSavedCode, PhoneVerificationCodeString);
            credential=(AuthCredential) phoneAuthCredential;
            FirebaseAutheticationWithCredential(CONST_VARIABLE.PHONE_SIGN_IN_STATUS,credential);
        }
        else
        {
            myCommunicator.Communicator(CONST_VARIABLE.PHONE_VERIFICATION_ERROR,true);
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
        activity.startActivityForResult(intent,CONST_VARIABLE.GOOGLE_SIGN_IN_REQUEST_CODE);
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
                FirebaseAutheticationWithCredential(CONST_VARIABLE.FACEBOOK_SIGN_IN_STATUS,credential);
            }
            @Override
            public void onCancel() { }
            @Override
            public void onError(FacebookException error)
            {
                myCommunicator.Communicator(CONST_VARIABLE.FACEBOOK_SIGN_IN_EXCEEPTION,false);
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==CONST_VARIABLE.GOOGLE_SIGN_IN_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            try {
                Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account=task.getResult(ApiException.class);
                credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
                FirebaseAutheticationWithCredential(CONST_VARIABLE.GOOGLE_SIGN_IN_STATUS,credential);
            } catch (ApiException e)
            {
                myCommunicator.Communicator(CONST_VARIABLE.GOOGLE_SIGN_IN_EXCEPTION,true);
            }
            return;
        }
        FacebookCallBack.onActivityResult(requestCode,resultCode,data);
    }
    ///*******************Google & Facebook SignIn Or SignUp Section Starting*******************///

    ///*************************Firebase Authentication Section Starting*************************///
    private void FirebaseAutheticationWithCredential(final String FromWhichMethod, AuthCredential credential)
    {
        mAuth=FirebaseAuth.getInstance();
        this.FromWhichMethod=FromWhichMethod;
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    myCommunicator.Communicator(FromWhichMethod,true);
                }
                else
                {
                    myCommunicator.Communicator(FromWhichMethod,false);
                }
            }
        });
    }
    ///*************************Firebase Authentication Section Starting*************************///
}

package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.engine.Initializable;
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
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;

public class SignInOrSignUpHelperClass
{
    private MyCommunicator myCommunicator;
    private AuthCredential credential;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks PhoneCallBack;
    private CallbackManager FacebookCallBack;
    private Intent intent;
    private Activity activity;
    private String EmailString,PasswordString,PhoneString,PhoneVerificationCodeString,FromWhichMethod;
    private LoginButton SignInFacebookSignInBtn;
    private SignInButton GoogleSignInButton;
    //Constructor
    public SignInOrSignUpHelperClass(Activity activity)
    {
        this.activity=activity;
        mAuth=FirebaseAuth.getInstance();
        myCommunicator=(MyCommunicator)activity;
        InitializationCallBack();
    }
    //Public Access Method For SignIn Or SignUp
    //Email SignIn Or SignUp Method
    public void EmailSignInOrUp(String WhichActivity,String EmailString,String PasswordString)
    {
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
    //Phone SignIn Method
    public void PhoneSignIn(String PhoneString)
    {
        this.PhoneString=PhoneString;
        PhoneSignInMethod();
    }
    //Google SignIn Method
    public void GoogleSignIn(int GoogleSignInId)
    {
        Log.d(CONST_VARIABLE.SIGN_IN_ACTIVITY,"SignInOrSignUpClassCalled");
        GoogleSignInButton=activity.findViewById(GoogleSignInId);
        GoogleSignInMethod();
    }
    //Facebook SignIn Method
    public void FacebookSignIn(int FacebookSignInId)
    {
        SignInFacebookSignInBtn=activity.findViewById(FacebookSignInId);
        FacebookSignInMethod();
    }
    //Phone Verification Completing Method
    public void PhoneVerificationComplete(String PhoneVerificationCodeString)
    {
        this.PhoneVerificationCodeString=PhoneVerificationCodeString;
        PhoneVerificationCompleteMethod();
    }
    // Get onActivityResult Method data from Activity
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==CONST_VARIABLE.GOOGLE_SIGN_IN_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            try {
                Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account=task.getResult(ApiException.class);
                credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
                FirebaseAutheticationWithCredential(CONST_VARIABLE.GOOGLE_SIGN_IN,credential);
            } catch (ApiException e)
            {
                Log.d(CONST_VARIABLE.SIGN_IN_ACTIVITY,e.toString());
            }

        }
        FacebookCallBack.onActivityResult(requestCode,resultCode,data);
    }

    //Private Access For Completing All Those Public Method
    //Email SignUp Method
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
                    if (mAuth.getCurrentUser().isEmailVerified())
                    {
                        myCommunicator.Communicator(CONST_VARIABLE.EMAIL_VERIFICATION_STATUS,true);
                    }
                    else
                    {
                        myCommunicator.Communicator(CONST_VARIABLE.EMAIL_VERIFICATION_STATUS,false);
                    }
                }
                else
                {
                    myCommunicator.Communicator(CONST_VARIABLE.EMAIL_SIGN_IN,false);
                }
            }
        });
    }
    //Phone SignIn Method
    private void PhoneSignInMethod()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(PhoneString,60, TimeUnit.SECONDS,activity,PhoneCallBack);
    }
    private void InitializationCallBack()
    {
        PhoneCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
                credential=(AuthCredential) phoneAuthCredential;
                FirebaseAutheticationWithCredential(CONST_VARIABLE.PHONE_SIGN_IN,credential);
            }
            @Override
            public void onCodeSent(String ValidationID, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(ValidationID, forceResendingToken);
                //Saving Code Validation ID into SharedPreference
                SharedPreferences sharedPreferences=activity.getSharedPreferences(CONST_VARIABLE.VALIDATION_ID, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(CONST_VARIABLE.VALIDATION_ID,ValidationID);
                editor.commit();
                myCommunicator.Communicator(CONST_VARIABLE.PHONE_SIGN_IN,false);
            }
            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                myCommunicator.Communicator(CONST_VARIABLE.PHONE_VERIFICATION_ERROR,false);
            }
        };
        FacebookCallBack=CallbackManager.Factory.create();
    }
    private void  PhoneVerificationCompleteMethod()
    {
        SharedPreferences sharedPreferences=(activity).getSharedPreferences(CONST_VARIABLE.VALIDATION_ID, Context.MODE_PRIVATE);
        String VerificationSavedCode=sharedPreferences.getString(CONST_VARIABLE.VALIDATION_ID,"");
        if (PhoneVerificationCodeString.length()>5  && VerificationSavedCode.length()>5)
        {
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(VerificationSavedCode, PhoneVerificationCodeString);
            credential=(AuthCredential) phoneAuthCredential;
            FirebaseAutheticationWithCredential(CONST_VARIABLE.PHONE_SIGN_IN,credential);
        }
        else
        {
            myCommunicator.Communicator(CONST_VARIABLE.PHONE_VERIFICATION_ERROR,true);
        }
    }
    //Google SignIn Method
    private void GoogleSignInMethod()
    {
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(activity.getString(R.string.default_web_client_id)).requestEmail().build();
        GoogleSignInClient signInClient= GoogleSignIn.getClient(activity,gso);
        intent=signInClient.getSignInIntent();
        activity.startActivityForResult(intent,CONST_VARIABLE.GOOGLE_SIGN_IN_REQUEST_CODE);
    }
    //Facebook SignIn Method
    private void FacebookSignInMethod()
    {
        SignInFacebookSignInBtn.setReadPermissions("email","public_profile");
        SignInFacebookSignInBtn.registerCallback(FacebookCallBack, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                credential= FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                FirebaseAutheticationWithCredential(CONST_VARIABLE.FACEBOOK_SIGN_IN,credential);
            }
            @Override
            public void onCancel()
            {
            }
            @Override
            public void onError(FacebookException error)
            {
                myCommunicator.Communicator(CONST_VARIABLE.FACEBOOK_SIGN_IN,false);
            }
        });
    }
    //SignIn With Firebase Credential Method
    private void FirebaseAutheticationWithCredential(final String FromWhichMethod, AuthCredential credential)
    {
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
}

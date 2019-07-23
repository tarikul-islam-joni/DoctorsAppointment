package com.tarikulislamjoni95.doctorsappointment.HelperClass;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class VARConst {
    //MyTextWatcher constant Variable
    public static final String EMAIL_VALIDITY = "EMAIL_VALIDITY";
    public static final String PHONE_VALIDITY = "PHONE_VALIDITY";
    public static final String PASSWORD_VALIDITY = "PASSWORD_VALIDITY";
    public static final String VERIFICATION_CODE_VALIDITY="VERIFICATION_CODE_VALIDITY";
    public static final String NAME_VALIDITY="NAME_VALIDITY";
    public static final String DATE_VALIDITY="DATE_VALIDITY";
    public static final String MONTH_VALIDITY="MONTH_VALIDITY";
    public static final String YEAR_VALIDITY="YEAR_VALIDITY";

    //WelcomeActivity constant Variable
    public static final String WELCOME_ACTIVITY = "WELCOME_ACTIVITY";
    //SignInActivity constant variable
    public static final String SIGN_IN_ACTIVITY = "SIGN_IN_ACTIVITY";
    //SignUpActivity constant Variable
    public static final String SIGN_UP_ACTIVITY = "SIGN_UP_ACTIVITY";


    //SignInOrSignUpHelperClass Variable
    public static final int GOOGLE_SIGN_IN_REQUEST_CODE=100;
    public static final String VALIDATION_ID="VALIDATION_ID";
    public static final String EMAIL_SIGN_UP="EMAIL_SIGN_UP";
    public static final String EMAIL_SIGN_IN="EMAIL_SIGN_IN";
    public static final String PHONE_SIGN_IN="PHONE_SIGN_IN";
    public static final String GOOGLE_SIGN_IN="GOOGLE_SIGN_IN";
    public static final String FACEBOOK_SIGN_IN="FACEBOOK_SIGN_IN";
    public static final String EMAIL_VERIFICATION_STATUS="EMAIL_VERIFICATION_STATUS";
    public static final String PHONE_SIGN_IN_STATUS="PHONE_SIGN_IN_STATUS";
    public static final String GOOGLE_SIGN_IN_STATUS="GOOGLE_SIGN_IN_STATUS";
    public static final String FACEBOOK_SIGN_IN_STATUS="FACEBOOK_SIGN_IN_STATUS";
    public static final String PHONE_SIGN_IN_EXCEPTION="PHONE_SIGN_IN_EXCEPTION";
    public static final String FACEBOOK_SIGN_IN_EXCEEPTION="FACEBOOK_SIGN_IN_EXCEEPTION";
    public static final String GOOGLE_SIGN_IN_EXCEPTION="GOOGLE_SIGN_IN_EXCEPTION";
    public static final String PHONE_VERIFICATION_MANUAL_COMPLETE="PHONE_VERIFICATION_MANUAL_COMPLETE";
    public static final String PHONE_VERIFICATION_ERROR="PHONE_VERIFICATION_ERROR";
    public static final String RESEND_EMAIL_VERIFICATION_STATUS="RESEND_EMAIL_VERIFICATION_STATUS";



    public static final String Select_Available_Day="Select available day";
    public static final String Appointment_Starting_Time="Appointment Starting Time";
    public static final String Appointment_Ending_Time="Appointment Ending Time";
    public static final String Unavailable_Ending_Date="Unavailable Ending Date";
    public static final String Unavailable_Starting_Date="Unavailable Starting Date";

    public static final String RECEIVER="RECEIVER";
    public static final String FETCH_TYPE="FETCH_TYPE";
    public static final int TYPE_01_ADDRESS_NAME=01;
    public static final int TYPE_02_ADDRESS_COORDINATE=02;
    public static final int TYPE_03_ERROR=03;
    public static final String  ADDRESS_NAME="ADDRESS_NAME";
    public static final String ADDRESS_LOCATION="ADDRESS_LOCATION";

    public static final String GET_LOCATION="GET_LOCATION";

    public static final int CAMERA_PERMISSION_REQUEST_CODE=101;
    public static final int STORAGE_PERMISSION_REQUEST_CODE=102;
    public static final int LOCATION_PERMISSION_REQUEST_CODE=103;

    public static final int REQUEST_CAMERA_CODE=200;
    public static final int REQUEST_GALLERY_CODE=201;





}

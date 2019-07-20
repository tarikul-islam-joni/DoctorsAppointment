package com.tarikulislamjoni95.doctorsappointment.HelperClass;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tarikulislamjoni95.doctorsappointment.R;

public class MyTextWatcher implements TextWatcher
{
    private MyValidationPatternClass ValidationPattern;
    private Activity activity;
    private EditText Et;
    private String WhichEditText,CheckString;
    private int EditTextId;
    private boolean CheckTextWatcherResult=false;
    public MyTextWatcher(Activity activity)
    {
        this.activity=activity;
    }
    public MyTextWatcher(Activity activity,String WhichEditText,int EditTextId)
    {
        this.activity=activity;
        this.WhichEditText=WhichEditText;
        this.EditTextId=EditTextId;
        Et=activity.findViewById(EditTextId);
        ValidationPattern=new MyValidationPatternClass();
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
    @Override
    public void afterTextChanged(Editable editable) { }
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {
        CheckString=Et.getText().toString();
        switch (WhichEditText)
        {
            case VARConst.EMAIL_VALIDITY:
                CheckTextWatcherResult=CheckEmailValidityMethod();
                break;
            case VARConst.PHONE_VALIDITY:
                CheckTextWatcherResult=CheckPhoneValidityMethod();
                break;
            case VARConst.VERIFICATION_CODE_VALIDITY:
                CheckTextWatcherResult=CheckVerificationValidityMethod();
            case VARConst.PASSWORD_VALIDITY:
                CheckTextWatcherResult=CheckPasswordValidityMethod();
                break;
            case VARConst.NAME_VALIDITY:
                CheckTextWatcherResult=CheckNameValidity();
                break;
            case VARConst.DATE_VALIDITY:
                CheckTextWatcherResult=CheckDateValidity();
                break;
            case VARConst.MONTH_VALIDITY:
                CheckTextWatcherResult=CheckMonthValidity();
                break;
            case VARConst.YEAR_VALIDITY:
                CheckTextWatcherResult=CheckYearValidity();
                break;
        }
        SetTextWatcherResult();
    }
    private boolean CheckEmailValidityMethod()
    {
        return ValidationPattern.getEmail_Pattern().matcher(CheckString).matches();
    }
    private boolean CheckPhoneValidityMethod()
    {
        return ValidationPattern.getPhone_Pattern().matcher(CheckString).matches() && CheckString.length()==11;
    }
    private boolean CheckVerificationValidityMethod()
    {
        return CheckString.length()>3;
    }
    private boolean CheckPasswordValidityMethod()
    {
        return ValidationPattern.getPassword_Normmal_Pattern().matcher(CheckString).matches();
    }
    private boolean CheckNameValidity()
    {
        return ValidationPattern.getName_Pattern().matcher(CheckString).matches();
    }
    private boolean CheckDateValidity()
    {
        try {
            int date=Integer.parseInt(CheckString);
            if (date>0 && date<32)
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (Exception e)
        {
            return false;
        }
    }
    private boolean CheckMonthValidity()
    {
        try {
            int month=Integer.parseInt(CheckString);
            if (month>0 && month<13)
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (Exception e)
        {
            return false;
        }
    }
    private boolean CheckYearValidity()
    {
        try {
            int year=Integer.parseInt(CheckString);
            if (year>1956 && year<2019)
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (Exception e)
        {
            return false;
        }
    }
    private void SetTextWatcherResult()
    {
        if (CheckTextWatcherResult)
        {
            Et.setTextColor((activity).getResources().getColor(R.color.colorGreen));
        }
        else
        {
            Et.setTextColor((activity).getResources().getColor(R.color.colorRed));
        }
    }

    public TextView.OnEditorActionListener actionListener=new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int ActionId, KeyEvent keyEvent)
        {
            switch (ActionId)
            {
                case EditorInfo.IME_ACTION_NEXT:
                    break;
                case EditorInfo.IME_ACTION_DONE:
                    break;
            }
            return false;
        }
    };
}

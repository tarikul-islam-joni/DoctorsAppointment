package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MyTextWatcher implements TextWatcher
{
    private MyValidationPatternClass ValidationPattern;
    private Activity activity;
    private EditText Et;
    private String WhichEditText,CheckString;
    private int EditTextId;
    private boolean CheckTextWatcherResult=false;
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
            case CONST_VARIABLE.EMAIL_VALIDITY:
                CheckTextWatcherResult=CheckEmailValidityMethod();
                break;
            case CONST_VARIABLE.PHONE_VALIDITY:
                CheckTextWatcherResult=CheckPhoneValidityMethod();
                break;
            case CONST_VARIABLE.PASSWORD_VALIDITY:
                CheckTextWatcherResult=CheckPasswordValidityMethod();
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
    private boolean CheckPasswordValidityMethod()
    {
        return ValidationPattern.getPassword_Normmal_Pattern().matcher(CheckString).matches();
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
}

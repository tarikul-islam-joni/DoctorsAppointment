package com.tarikulislamjoni95.doctorsappointment.HelperClass;

import android.util.Patterns;

import java.util.regex.Pattern;

public class MyValidationPatternClass
{
    private static final Pattern Password_Normmal_Pattern=
            Pattern.compile("^"+
                    //"(?=.*[0-9])"+
                    //"(?=.*[a-z])"+
                    //"(?=.*[A-Z])"+
                    //"(?=.*[!@#$%^&-_=+])"+
                    "(?=\\S+$)"+
                    ".{6,}"+
                    "$");

    private static final Pattern Password_Strong_Pattern=
            Pattern.compile("^"+
                    "(?=.*[0-9])"+
                    "(?=.*[a-z])"+
                    "(?=.*[A-Z])"+
                    "(?=.*[!@#$%^&-_=+])"+
                    "(?=\\S+$)"+
                    ".{8,}"+
                    "$");
    private static final Pattern Name_Pattern=
            Pattern.compile("^[a-zA-Z.\\s]+");

    private static  final  Pattern Email_Pattern= Patterns.EMAIL_ADDRESS;

    private static final Pattern Phone_Pattern=Patterns.PHONE;

    public Pattern getPassword_Normmal_Pattern() {
        return Password_Normmal_Pattern;
    }

    public Pattern getPassword_Strong_Pattern() {
        return Password_Strong_Pattern;
    }

    public Pattern getName_Pattern() {
        return Name_Pattern;
    }

    public Pattern getEmail_Pattern() {
        return Email_Pattern;
    }

    public Pattern getPhone_Pattern() {
        return Phone_Pattern;
    }
}

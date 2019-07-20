package com.tarikulislamjoni95.doctorsappointment.HelperClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.HashMap;
import java.util.Map;

public class MyDialogClass
{
    String VerificationCode;
    int CountResend=0;
    AlertDialog dialog;
    View view;
    Button[] btn;
    EditText[] et;
    String[] informationString;
    MyAlertDialogCommunicator myAlertDialogCommunicator;
    private Activity activity;
    public MyDialogClass(Activity activity)
    {
        this.activity=activity;
        //myAlertDialogCommunicator=(MyAlertDialogCommunicator)activity;
    }


    ///This is my simple alert dialog which works only for take decesion wheather it is right or wrong

    public AlertDialog MyAlertDialog(final String WhichField, String title, String message, String pos, String neg)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(pos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                myAlertDialogCommunicator.DialogResultSuccess(WhichField,"Yes");
            }
        });
        builder.setNegativeButton(neg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                myAlertDialogCommunicator.DialogResultSuccess(WhichField,"No");
            }
        });

        AlertDialog dialog=builder.create();
        this.dialog=dialog;
        return dialog;
    }

    ///This is my custom dialog.It take layout and also it can perform for maximum 5 Button onClick and EditText not limited
    ///And Sending also a information string that can identify the operation for sending and receiving data

    public AlertDialog MyCustomDialog(int layout,int[] ButtonId,int[] EditTextId,String[] information,String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        view=(activity).getLayoutInflater().inflate(layout,null,false);

        btn=new Button[ButtonId.length];
        et=new EditText[EditTextId.length];
        informationString=new String[information.length];
        for(int i=0; i<ButtonId.length; i++)
        {
            btn[i]=(view).findViewById(ButtonId[i]);
        }
        for(int i=0; i<EditTextId.length; i++)
        {
            et[i]=(view).findViewById(EditTextId[i]);
        }
        for(int i=0; i<information.length; i++)
        {
            informationString[i]=information[i];
        }
        builder.setView(view);
        if (ButtonId.length==1)
        {
            FirstButton();
        }
        else if (ButtonId.length==2)
        {
            FirstButton();
            SecondButton();
        }
        else if (ButtonId.length==3)
        {
            FirstButton();
            SecondButton();
            ThirdButton();
        }
        else if (ButtonId.length==4)
        {
            FirstButton();
            SecondButton();
            ThirdButton();
            FourthButton();
        }
        else if (ButtonId.length==5)
        {
            FirstButton();
            SecondButton();
            ThirdButton();
            FourthButton();
            FifthButton();
        }
        AlertDialog dialog=builder.create();
        this.dialog=dialog;
        return dialog;
    }

    private void FirstButton()
    {
        btn[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///Sending all the data of the edit text
                if (informationString[0]=="ConfirmBtn")
                {
                    Map<Integer,String> map=GetAllData();
                    VerificationCode=map.get(0);
                    myAlertDialogCommunicator.DialogResultSuccess(VARConst.PHONE_SIGN_IN,"CONFIRM");
                }
            }
        });
    }
    private void SecondButton()
    {
        btn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///This button work only for take decession
                if (informationString[1]=="ResendBtn")
                {
                    CountResend++;
                    myAlertDialogCommunicator.DialogResultSuccess(VARConst.PHONE_SIGN_IN,"RESEND");

                    if (CountResend>2)
                    {
                        btn[1].setEnabled(false);
                    }
                    else {
                        btn[1].setEnabled(false);
                        btn[1].postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btn[1].setEnabled(true);
                            }
                        }, 60000);
                    }
                }
            }
        });
    }
    private void ThirdButton()
    {
        btn[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (informationString[2]=="CancelBtn")
                {
                    myAlertDialogCommunicator.DialogResultSuccess(VARConst.PHONE_SIGN_IN, "CANCEL");
                }
            }
        });
    }
    private void FourthButton()
    {
        btn[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"ButtonClicked"+String.valueOf(4),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void FifthButton()
    {
        btn[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"ButtonClicked"+String.valueOf(5),Toast.LENGTH_LONG).show();
            }
        });
    }
    private Map<Integer,String> GetAllData()
    {
        Map<Integer,String> map=new HashMap<>();
        for(int i=0; i<et.length; i++)
        {
            map.put(i,et[i].getText().toString());
        }
        return map;
    }

    public String getVerificationCode()
    {
        return VerificationCode;
    }
    interface MyAlertDialogCommunicator
    {
        public void DialogResultSuccess(String WhichField, String result);
        public void MyCustomDialogGetData(Map<Integer, String> integerStringMap);
    }
}

package com.tarikulislamjoni95.doctorsappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;

//Welcome Activity Done
public class WelcomeActivity extends AppCompatActivity{

    private MyLoadingDailog myLoadingDailog;
    private MyToastClass myToastClass;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance();
        myToastClass=new MyToastClass(WelcomeActivity.this);
        myLoadingDailog=new MyLoadingDailog(WelcomeActivity.this,R.drawable.spinner);
        CheckLoggedInOrNot();
    }

    @Override
    protected void onDestroy() {
        if (myLoadingDailog.isShowing() ) {
            myLoadingDailog.dismiss();
        }
        super.onDestroy();
    }


    private void CheckLoggedInOrNot()
    {
        if (!myLoadingDailog.isShowing())
        {
            myLoadingDailog.show();
        }
        if (mUser!=null)
        {
            CheckAccountValidityAndCompletion();
        }
        else
        {
            Intent intent=new Intent(WelcomeActivity.this,SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            DelayStartingActivityMethod(intent);
        }
    }

    private void CheckAccountValidityAndCompletion()
    {
        DatabaseReference ref=mDatabase.getReference();
        ref.child(DBConst.AccountStatus).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String AccountType=dataSnapshot.child(DBConst.AccountType).getValue().toString();
                    boolean AccountCompletion=(boolean)dataSnapshot.child(DBConst.AccountCompletion).getValue();
                    boolean AccountValidity=(boolean) dataSnapshot.child(DBConst.AccountValidity).getValue();
                    if (AccountType.matches(DBConst.Patient))
                    {
                        if (!AccountCompletion)
                        {
                            Intent intent=new Intent(WelcomeActivity.this, PatientProfileOneActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            DelayStartingActivityMethod(intent);
                        }
                        else if (!AccountValidity)
                        {
                            Intent intent=new Intent(WelcomeActivity.this,PatientProfileTwoActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            DelayStartingActivityMethod(intent);
                        }
                        else
                        {
                            Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            DelayStartingActivityMethod(intent);
                        }
                    }
                    else if (AccountType.matches(DBConst.Doctor))
                    {
                       if (AccountValidity&&AccountCompletion)
                       {
                           Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                           DelayStartingActivityMethod(intent);
                       }
                       else if (!AccountCompletion)
                       {
                           Intent intent=new Intent(WelcomeActivity.this, DoctorProfileEditorOneActivity.class);
                           DelayStartingActivityMethod(intent);
                       }
                       else if (!AccountValidity)
                       {
                           myToastClass.LToast("Give us some time that we can verify your credential\nPlease sign In later");
                       }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                if (myLoadingDailog.isShowing())
                {
                    myLoadingDailog.dismiss();
                }
                finish();
            }
        });
    }

    private void DelayStartingActivityMethod(final Intent intent)
    {
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (myLoadingDailog.isShowing())
                {
                    myLoadingDailog.dismiss();
                }
                startActivity(intent);
            }
        },5000);
    }
}

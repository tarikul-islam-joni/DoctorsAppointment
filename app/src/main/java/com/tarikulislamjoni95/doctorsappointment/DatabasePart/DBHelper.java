package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.tarikulislamjoni95.doctorsappointment.AccountPart.WelcomeActivity;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;

import java.util.HashMap;

public class DBHelper
{
    private Activity activity;
    private GetDataFromDBInterface myDataInterface;
    public DBHelper(Activity activity)
    {
        this.activity=activity;
        myDataInterface=(GetDataFromDBInterface) activity;
    }
    public void GetUID()
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put(DBConst.RESULT,DBConst.NOT_NULL_USER);
            hashMap.put(DBConst.UID,FirebaseAuth.getInstance().getCurrentUser().getUid());
            myDataInterface.GetSingleDataFromDatabase(DBConst.GetAccountUID,hashMap);
        }
        else
        {
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put(DBConst.RESULT,DBConst.NULL_USER);
            hashMap.put(DBConst.UID, VARConst.UNKNOWN);
            myDataInterface.GetSingleDataFromDatabase(DBConst.GetAccountUID,hashMap);
        }
    }

    public void SignOut()
    {
        FirebaseAuth.getInstance().signOut();
        activity.startActivity(new Intent(activity, WelcomeActivity.class));
    }

    public void DeleteUID()
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                        SignOut();
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnAccountDeletion,hashMap);
                    }
                    else
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                        SignOut();
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnAccountDeletion,hashMap);
                    }
                }
            });
        }
        else
        {
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
            SignOut();
            myDataInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnAccountDeletion,hashMap);
        }
    }
}

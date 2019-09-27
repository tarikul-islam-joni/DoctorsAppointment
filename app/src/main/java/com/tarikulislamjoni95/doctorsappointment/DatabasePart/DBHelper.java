package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;

import java.util.HashMap;

public class DBHelper
{
    private GetDataFromDBInterface myDataInterface;
    public DBHelper(Activity activity)
    {
        myDataInterface=(GetDataFromDBInterface) activity;
    }
    public void GetUID()
    {
        HashMap<String,Object> hashMap=new HashMap<>();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            hashMap.put(DBConst.RESULT,DBConst.NOT_NULL_USER);
            hashMap.put(DBConst.UID,FirebaseAuth.getInstance().getCurrentUser().getUid());
            myDataInterface.GetSingleDataFromDatabase(DBConst.GetAccountUID,hashMap);
        }
        else
        {
            hashMap.put(DBConst.RESULT,DBConst.NULL_USER);
            hashMap.put(DBConst.UID, VARConst.UNKNOWN);
            myDataInterface.GetSingleDataFromDatabase(DBConst.GetAccountUID,hashMap);
        }
    }

    public void SignOut()
    {
        FirebaseAuth.getInstance().signOut();
    }

    public void DeleteUID()
    {
        Log.d("myError","2 CallDBForAccountDelete");
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Log.d("myError","3 CallDBForAccountDelete");
            FirebaseAuth.getInstance().signOut();
            FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {

                        Log.d("myError","5 CallDBForAccountDelete");
                    }
                    else
                    {
                        Log.d("myError","6 CallDBForAccountDelete "+task.toString());
                    }
                }
            });
            Log.d("myError","4 CallDBForAccountDelete");
        }
    }
}

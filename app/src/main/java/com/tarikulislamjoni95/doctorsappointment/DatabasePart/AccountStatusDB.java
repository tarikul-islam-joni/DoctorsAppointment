package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;

import java.util.HashMap;

public class AccountStatusDB
{

    private GetDataFromDBInterface myDataInterface;

    public AccountStatusDB(Activity activity)
    {
        myDataInterface=(GetDataFromDBInterface) activity;
    }
    public void GetAccountStatusData(String UID)
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference mReference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).child(UID);
            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put(DBConst.RESULT,DBConst.DATA_EXIST);
                        hashMap.put(DBConst.AccountType,dataSnapshot.child(DBConst.AccountType).getValue());
                        hashMap.put(DBConst.AccountCompletion,dataSnapshot.child(DBConst.AccountCompletion).getValue());
                        hashMap.put(DBConst.AccountValidity,dataSnapshot.child(DBConst.AccountValidity).getValue());
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetAccountStatusDB,hashMap);
                    }
                    else
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put(DBConst.RESULT,DBConst.DATA_NOT_EXIST);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetAccountStatusDB,hashMap);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.DATABASE_ERROR);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetAccountStatusDB,hashMap);
                }
            });
        }
        else
        {
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put(DBConst.RESULT,DBConst.NULL_USER);
            myDataInterface.GetSingleDataFromDatabase(DBConst.GetAccountStatusDB,hashMap);
        }
    }

    public void SaveIntoAccountStatusDB(String UID, final String AccountType, final boolean AccountCompletion, final boolean AccountValidity)
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference mReference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus);
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put(DBConst.AccountType,AccountType);
            hashMap.put(DBConst.AccountCompletion,AccountCompletion);
            hashMap.put(DBConst.AccountValidity,AccountValidity);
            mReference.child(UID).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isComplete())
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.SaveAccountStatusDB,hashMap);
                    }
                    else
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.SaveAccountStatusDB,hashMap);
                    }
                }
            });
        }
        else
        {
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put(DBConst.RESULT,DBConst.NULL_USER);
            myDataInterface.GetSingleDataFromDatabase(DBConst.SaveAccountStatusDB,hashMap);
        }
    }
}

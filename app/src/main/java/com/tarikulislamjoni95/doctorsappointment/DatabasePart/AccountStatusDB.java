package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;

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
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountStatusDBInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class AccountStatusDB
{
    private ArrayList<AccountStatusDM> arrayList;

    private AccountStatusDBInterface myInterface;

    public AccountStatusDB(Activity activity)
    {
        ///Note This may causes fault;
        myInterface=(AccountStatusDBInterface)activity;

        arrayList=new ArrayList<>();
    }

    public void GetUIDAccountStatusData()
    {
        arrayList.clear();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference mReference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        arrayList.add(
                                new AccountStatusDM(
                                        dataSnapshot.getKey().toString(),
                                        dataSnapshot.child(DBConst.AccountType).getValue().toString(),
                                        (boolean)dataSnapshot.child(DBConst.AccountCompletion).getValue(),
                                        (boolean)dataSnapshot.child(DBConst.AccountValidity).getValue())
                        );

                        myInterface.GetAccountStatus(true,arrayList);
                    }
                    else
                    {
                        arrayList.add(new AccountStatusDM());
                        myInterface.GetAccountStatus(false,arrayList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    arrayList.add(new AccountStatusDM());
                    myInterface.GetAccountStatus(false,arrayList);
                }
            });
        }
        else
        {
            arrayList.add(new AccountStatusDM());
            myInterface.GetAccountStatus(false,arrayList);
        }
    }

    public void SaveIntoAccountStatusDBFromUser(String AccountType,boolean AccountCompletion,boolean AccountValidity)
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference mReference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus);
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put(DBConst.AccountType,AccountType);
            hashMap.put(DBConst.AccountCompletion,AccountCompletion);
            hashMap.put(DBConst.AccountValidity,AccountValidity);
            mReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isComplete())
                    {
                        myInterface.AccountStatusSavingResult(true);
                    }
                    else
                    {
                        myInterface.AccountStatusSavingResult(false);
                    }
                }
            });
        }
        else
        {
            myInterface.AccountStatusSavingResult(false);
        }
    }

    public void SaveIntoAccountStatusDBFromAdmin(String UID,String AccountType,boolean AccountCompletion,boolean AccountValidity)
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference mReference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).child(UID);
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put(DBConst.AccountCompletion,AccountCompletion);
            hashMap.put(DBConst.AccountType,AccountType);
            hashMap.put(DBConst.AccountValidity,AccountValidity);
            mReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isComplete())
                    {
                        myInterface.AccountStatusSavingResult(true);
                    }
                    else
                    {
                        myInterface.AccountStatusSavingResult(false);
                    }
                }
            });
        }
        else
        {
            myInterface.AccountStatusSavingResult(false);
        }
    }
}

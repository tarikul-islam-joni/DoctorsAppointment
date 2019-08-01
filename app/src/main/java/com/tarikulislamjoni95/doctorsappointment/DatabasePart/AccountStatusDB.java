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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountStatusDBInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class AccountStatusDB
{
    private ArrayList<AccountStatusDataModel> arrayList;

    private AccountStatusDBInterface myInterface;

    public AccountStatusDB(Activity activity)
    {
        myInterface=(AccountStatusDBInterface)activity;

        arrayList=new ArrayList<>();
    }

    public void GetUIDAccountStatusData()
    {
        arrayList.clear();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference mReference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        arrayList.add(
                                new AccountStatusDataModel(
                                        dataSnapshot.getKey().toString(),
                                        (boolean)dataSnapshot.child(DBConst.AccountCompletion).getValue(),
                                        dataSnapshot.child(DBConst.AccountType).getValue().toString(),
                                        (boolean)dataSnapshot.child(DBConst.AccountValidity).getValue(),
                                        (boolean)dataSnapshot.child(DBConst.AuthorityValidity).getValue())
                        );

                        myInterface.GetAccountStatus(true,arrayList);
                    }
                    else
                    {
                        arrayList.add(new AccountStatusDataModel());
                        myInterface.GetAccountStatus(false,arrayList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    arrayList.add(new AccountStatusDataModel());
                    myInterface.GetAccountStatus(false,arrayList);
                }
            });
        }
        else
        {
            arrayList.add(new AccountStatusDataModel());
            myInterface.GetAccountStatus(false,arrayList);
        }
    }

    public void SaveIntoAccountStatusDBFromUser(String AccountType,boolean AccountCompletion,boolean AccountValidity,boolean AuthorizeValidity)
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference mReference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put(DBConst.AccountCompletion,AccountCompletion);
            hashMap.put(DBConst.AccountType,AccountType);
            hashMap.put(DBConst.AccountValidity,AccountValidity);
            hashMap.put(DBConst.AuthorityValidity,AuthorizeValidity);
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

    public void SaveIntoAccountStatusDBFromAdmin(String UID,String AccountType,boolean AccountCompletion,boolean AccountValidity,boolean AuthorizeValidity)
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference mReference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).child(UID);
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put(DBConst.AccountCompletion,AccountCompletion);
            hashMap.put(DBConst.AccountType,AccountType);
            hashMap.put(DBConst.AccountValidity,AccountValidity);
            hashMap.put(DBConst.AuthorityValidity,AuthorizeValidity);
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

    public void GetValidityUID(String AccountType, final boolean AuthorizeType)
    {
        arrayList.clear();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Query query=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).orderByChild(DBConst.AccountType).equalTo(AccountType);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            if ((boolean)dataSnapshot1.child(DBConst.AuthorityValidity).getValue()==AuthorizeType)
                            {
                                arrayList.add(new AccountStatusDataModel(
                                                dataSnapshot1.getKey().toString(),
                                                (boolean)dataSnapshot1.child(DBConst.AccountCompletion).getValue(),
                                                dataSnapshot1.child(DBConst.AccountType).getValue().toString(),
                                                (boolean)dataSnapshot1.child(DBConst.AccountValidity).getValue(),
                                                (boolean)dataSnapshot1.child(DBConst.AuthorityValidity).getValue()
                                        )
                                );
                            }
                        }
                        myInterface.GetAccountStatus(true,arrayList);
                    }
                    else
                    {
                        arrayList.add(new AccountStatusDataModel());
                        myInterface.GetAccountStatus(false,arrayList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    arrayList.add(new AccountStatusDataModel());
                    myInterface.GetAccountStatus(false,arrayList);
                }
            });
        }
        else
        {
            arrayList.add(new AccountStatusDataModel());
            myInterface.GetAccountStatus(false,arrayList);
        }

    }


}

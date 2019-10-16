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
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class AccountMultiplicityDB
{
    private HashMap<String,Object> DataHashMap;
    private GetDataFromDBInterface myDataInterface;
    public AccountMultiplicityDB(Activity activity)
    {
        myDataInterface=(GetDataFromDBInterface) activity;
    }
    public void SaveAccountMultiplicity(String AccountType,String InformationString,HashMap<String,Object> hashMap)
    {
        DataHashMap=new HashMap<>();
        if (hashMap.size()>2)
        {
            hashMap.put(DBConst.MultipleCheck,true);
        }

        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.AccountMultiplicity).child(AccountType).child(InformationString);
            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isComplete())
                    {
                        DataHashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.SaveAccountMultiplicityDB,DataHashMap);
                    }
                    else
                    {
                        DataHashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.SaveAccountMultiplicityDB,DataHashMap);
                    }
                }
            });

        }
        else
        {
            DataHashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
            myDataInterface.GetSingleDataFromDatabase(DBConst.SaveAccountMultiplicityDB,DataHashMap);
        }

    }

    public void GetAccountMultiplicity(String AccountType,String InformationString)
    {
        DataHashMap=new HashMap<>();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountMultiplicity).child(AccountType).child(InformationString);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            DataHashMap.put(dataSnapshot1.getKey(),(Object) dataSnapshot1.getValue());
                        }
                        DataHashMap.put(DBConst.RESULT,DBConst.DATA_EXIST);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetAccountMultiplicityDB,DataHashMap);
                    }
                    else
                    {
                        DataHashMap.put(DBConst.RESULT,DBConst.DATA_NOT_EXIST);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetAccountMultiplicityDB,DataHashMap);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    DataHashMap.put(DBConst.RESULT,DBConst.DATABASE_ERROR);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetAccountMultiplicityDB,DataHashMap);
                }
            });
        }
        else
        {
            DataHashMap.put(DBConst.RESULT,DBConst.NULL_USER);
            myDataInterface.GetSingleDataFromDatabase(DBConst.GetAccountMultiplicityDB,DataHashMap);
        }
    }


    public void GetAccountMultiplicityListByKey(String AccountType,final String OutputKey)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountMultiplicity);
        databaseReference.child(AccountType).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.DATA_EXIST);
                    hashMap.put(DBConst.DataSnapshot,dataSnapshot);
                    myDataInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
                else
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.DATA_NOT_EXIST);
                    myDataInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(DBConst.RESULT,DBConst.DATA_NOT_EXIST);
                myDataInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
            }
        });
    }

    public void GetSpecificAccountBySearchingKey(String WhichOne, String SearchingString, final String OutputKey)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountMultiplicity).child(WhichOne).child(SearchingString);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.DATA_EXIST);
                    hashMap.put(DBConst.DataSnapshot,dataSnapshot);
                    myDataInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
                else
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.DATA_NOT_EXIST);
                    myDataInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(DBConst.RESULT,DBConst.DATA_NOT_EXIST);
                myDataInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
            }
        });
    }
}

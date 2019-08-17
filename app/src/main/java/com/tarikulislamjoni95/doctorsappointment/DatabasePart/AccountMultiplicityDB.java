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
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountMultiplicityInterface;

import java.util.HashMap;

public class AccountMultiplicityDB
{
    private AccountMultiplicityInterface accountMultiplicityInterface;
    public AccountMultiplicityDB(Activity activity)
    {
        accountMultiplicityInterface=(AccountMultiplicityInterface)activity;
    }
    public void SaveAccountMultiplicity(String BirthNumberString,HashMap<String,Object> hashMap)
    {
        if (hashMap.size()>2)
        {
            hashMap.put(DBConst.MultipleCheck,true);
        }

        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.AccountMultiplicity).child(BirthNumberString);
            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isComplete())
                    {
                        accountMultiplicityInterface.SaveAccountMultiplicity(DBConst.SUCCESSFULL);
                    }
                    else
                    {
                        accountMultiplicityInterface.SaveAccountMultiplicity(DBConst.UNSUCCESSFULL);
                    }
                }
            });

        }
        else
        {
            accountMultiplicityInterface.SaveAccountMultiplicity(DBConst.NULL_USER);
        }

    }

    public void GetAccountMultiplicity(String BirthNumberString)
    {
        final HashMap<String,Object> hashMap=new HashMap<>();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountMultiplicity).child(BirthNumberString);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            hashMap.put(dataSnapshot1.getKey(),(Object) dataSnapshot1.getValue());
                        }
                        accountMultiplicityInterface.GetAccountMultiplicityData(DBConst.DATA_EXIST,hashMap);
                    }
                    else
                    {
                        accountMultiplicityInterface.GetAccountMultiplicityData(DBConst.DATA_NOT_EXIST,hashMap);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    accountMultiplicityInterface.GetAccountMultiplicityData(DBConst.DATABASE_ERROR,hashMap);
                }
            });
        }
        else
        {
            accountMultiplicityInterface.GetAccountMultiplicityData(DBConst.NULL_USER,hashMap);
        }
    }
}

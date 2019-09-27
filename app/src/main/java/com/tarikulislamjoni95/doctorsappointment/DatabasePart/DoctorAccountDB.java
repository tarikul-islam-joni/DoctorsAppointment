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
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;

import java.util.HashMap;

public class DoctorAccountDB
{
    private Activity activity;
    private GetDataFromDBInterface myDataInterface;
    private DatabaseReference reference;
    public DoctorAccountDB(Activity activity)
    {
        this.activity=activity;
        myDataInterface =(GetDataFromDBInterface)activity;
        reference= FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.DoctorAccount);
    }
    public void SaveDoctorAccountInformation(String UID, final HashMap<String,String> hashMap)
    {
        final HashMap<String,Object> hashMap1=new HashMap<>();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            reference.child(UID).child(DBConst.AccountInformation).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isComplete())
                    {
                        hashMap1.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.SaveDoctorAccountInformation,hashMap1);
                    }
                    else
                    {
                        hashMap1.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.SaveDoctorAccountInformation,hashMap1);
                    }
                }
            });
        }
        else
        {
            hashMap1.put(DBConst.RESULT,DBConst.NULL_USER);
            myDataInterface.GetSingleDataFromDatabase(DBConst.SaveDoctorAccountInformation,hashMap1);
        }
    }
    public void GetDoctorAccountInformation(final String UID)
    {
        final HashMap<String,Object> hashMap=new HashMap<>();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            reference.child(UID).child(DBConst.AccountInformation).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        hashMap.put(DBConst.RESULT,DBConst.DATA_EXIST);
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            hashMap.put(dataSnapshot1.getKey(),dataSnapshot1.getValue());
                        }
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetDoctorAccountInformation,hashMap);

                    }
                    else
                    {
                        hashMap.put(DBConst.RESULT,DBConst.DATA_NOT_EXIST);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetDoctorAccountInformation,hashMap);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    hashMap.put(DBConst.RESULT,DBConst.DATABASE_ERROR);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetDoctorAccountInformation,hashMap);
                }
            });
        }
        else
        {
            hashMap.put(DBConst.RESULT,DBConst.NULL_USER);
            myDataInterface.GetSingleDataFromDatabase(DBConst.GetDoctorAccountInformation,hashMap);
        }
    }
}

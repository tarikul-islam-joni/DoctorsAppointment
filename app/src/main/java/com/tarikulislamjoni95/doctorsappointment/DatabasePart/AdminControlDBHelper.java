package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;

import java.util.HashMap;

public class AdminControlDBHelper
{
    private  Activity activity;
    GetDataFromDBInterface getDataFromDBInterface;
    public AdminControlDBHelper(Activity activity)
    {
        getDataFromDBInterface=(GetDataFromDBInterface)activity;
    }
    public void SaveBillingToTheAdminDB(String MonthWithYear,HashMap<String,Object> hashMap)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.PaymentDB).child(MonthWithYear);
        databaseReference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }


    public void GetDoctorAccountInformation(String AccountType,String UID,String WhichChild,final String OutputKey)
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(AccountType).child(UID).child(WhichChild);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(DBConst.DataSnapshot,hashMap);
                getDataFromDBInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(DBConst.DataSnapshot,databaseError.toString());
                getDataFromDBInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
            }
        });
    }

    public void GetBillForTheSelectedDate(String MonthWithYear,String Date,final String OutputKey)
    {
        Log.d("myError2",MonthWithYear+Date);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.PaymentDB).child(MonthWithYear);
        databaseReference.orderByValue().equalTo(Date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    HashMap<String,Object> hashMap=new HashMap<String, Object>();
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    hashMap.put(DBConst.DataSnapshot,dataSnapshot);
                    Log.d("myError 10",hashMap.toString());
                    getDataFromDBInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                HashMap<String,Object> hashMap=new HashMap<String, Object>();
                hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                getDataFromDBInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
            }
        });
    }

    public void GetBillForTheSelectedMonth(String MonthWithYear,final String OutputKey)
    {
        Log.d("myError1",MonthWithYear+OutputKey);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.PaymentDB).child(MonthWithYear);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    hashMap.put(DBConst.DataSnapshot,dataSnapshot);
                    getDataFromDBInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
                else
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    getDataFromDBInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                getDataFromDBInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
            }
        });
    }

    public void AuthorityValueChangeOfTheDoctorAccountInformation(String UID, String AuthorityValidityStatus, final String OutputKey)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.DoctorAccount).child(UID).child(DBConst.AccountInformation);
        databaseReference.child(DBConst.AuthorityValidity).setValue(AuthorityValidityStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isComplete())
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    getDataFromDBInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
                else
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    getDataFromDBInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
            }
        });
    }


    public void AccountMultiplicityChangeOfTheDoctorAccount(String AccountType,String DocNo, String UID, Object object, final String Outputkey)
    {
        DatabaseReference databaseReference;
        if (AccountType.matches(DBConst.Doctor))
        {
            databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountMultiplicity).child(AccountType).child(DocNo).child(UID);
        }
        else
        {
            databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountMultiplicity).child(AccountType).child(DocNo).child(DBConst.AccountMultiplicity);
        }
        databaseReference.setValue(object).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isComplete())
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    getDataFromDBInterface.GetSingleDataFromDatabase(Outputkey,hashMap);
                }
                else
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    getDataFromDBInterface.GetSingleDataFromDatabase(Outputkey,hashMap);
                }
            }
        });
    }

    public void LockOrUnlockAccountUID(String UID,boolean LockStatus,final String OutputKey)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).child(UID);
        databaseReference.child(DBConst.AccountLockState).setValue(LockStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isComplete())
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    getDataFromDBInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
                else
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    getDataFromDBInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
            }
        });
    }

}

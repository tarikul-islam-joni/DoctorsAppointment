package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;

import java.util.HashMap;

public class PaymentDB
{
    GetDataFromDBInterface getDataFromDBInterface;
    private Activity activity;
    public PaymentDB(Activity activity)
    {
        getDataFromDBInterface=(GetDataFromDBInterface)activity;
    }

    public void GetTodayPayment(final String ReferenceKey, String Year, String Month, String Date)
    {
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(DBConst.PaymentDB).child(Year).child(Month).child(Date).child(ReferenceKey);
        {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                        hashMap.put(DBConst.ReferenceKey,dataSnapshot.getKey());
                        hashMap.put(DBConst.TraxId,dataSnapshot.getValue());
                        getDataFromDBInterface.GetSingleDataFromDatabase(DBConst.GetPaymentDataFromPaymentDB,hashMap);
                    }
                    else
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        getDataFromDBInterface.GetSingleDataFromDatabase(DBConst.GetPaymentDataFromPaymentDB,hashMap);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    getDataFromDBInterface.GetSingleDataFromDatabase(DBConst.GetPaymentDataFromPaymentDB,hashMap);
                }
            });
        }
    }
}

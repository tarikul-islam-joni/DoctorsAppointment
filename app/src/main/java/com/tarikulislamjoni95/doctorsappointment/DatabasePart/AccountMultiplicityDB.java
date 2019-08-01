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
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountMultiplicityInterface;

public class AccountMultiplicityDB
{
    private AccountMultiplicityInterface accountMultiplicityInterface;
    public AccountMultiplicityDB(Activity activity)
    {
        accountMultiplicityInterface=(AccountMultiplicityInterface) activity;
    }
    public void GetAccountMultiplicity(String AccountType,String DocumentID)
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.AccountMultiplicity).child(AccountType).child(DocumentID);
        reference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    accountMultiplicityInterface.GetAccountMultiplicityResult(true);
                }
                else
                {
                    accountMultiplicityInterface.GetAccountMultiplicityResult(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void SaveAccountMultiplicity(String AccountType,String DocumentID,boolean MultipleCheck)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountMultiplicity).child(AccountType).child(DocumentID);
        reference.child(DBConst.MultipleCheck).setValue(MultipleCheck);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isComplete())
                        {
                            accountMultiplicityInterface.SaveAccountMultiplicityResult(true);
                        }
                        else
                        {
                            accountMultiplicityInterface.SaveAccountMultiplicityResult(false);
                        }
                    }
                });
    }
}

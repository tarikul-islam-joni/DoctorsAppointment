package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseDatabaseHelper
{
    private MyCommunicator communicator;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseStorage mStorage;
    private Activity activity;
    public FirebaseDatabaseHelper(Activity activity)
    {
        this.activity=activity;
        mAuth=FirebaseAuth.getInstance();
        mRef=FirebaseDatabase.getInstance().getReference();
        communicator=(MyCommunicator)activity;
    }
    public void SaveAccountDataIntoDatabase(String AccountType,String UID,String Name,String FatherName,
                                            String MotherName, String ProfileImage,String BirthDate,
                                            String Gender,String Address,String SecureDocument)
    {
        DatabaseReference reference=mRef.child("Account").child(AccountType).child(UID);
        reference.child("Name").setValue(Name);
        reference.child("FatherName").setValue(FatherName);
        reference.child("MotherName").setValue(MotherName);
        reference.child("ProfileImage").setValue(ProfileImage);
        reference.child("BirthDate").setValue(BirthDate);
        reference.child("Gender").setValue(Gender);
        reference.child("Addess").setValue(Address);
        reference.child("SecureDocument").push().setValue(SecureDocument).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    communicator.Communicator("ProfileDataSaving",true);
                }
                else
                {
                    communicator.Communicator("ProfileDataSaving",false);
                }
            }
        });
    }

}

package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportDB
{
    private Activity activity;
    public ReportDB(Activity activity)
    {
        this.activity=activity;
    }
    public void SaveCrushReportIntoDatabase(String message)
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("ReportHistory");
            reference.push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }
    }
}

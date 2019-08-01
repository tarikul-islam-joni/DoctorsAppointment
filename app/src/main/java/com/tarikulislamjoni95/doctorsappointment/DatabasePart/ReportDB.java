package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportDB
{
    public void SaveCrushReportIntoDatabase(String message)
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("ReportHistory");
        reference.push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
}

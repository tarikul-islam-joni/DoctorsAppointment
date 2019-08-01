package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tarikulislamjoni95.doctorsappointment.Interface.ImportantTaskOfDBInterface;

public class ImportantTaskOfDB
{
    private ImportantTaskOfDBInterface importantTaskOfDBInterface;
    public ImportantTaskOfDB(Activity activity)
    {
        importantTaskOfDBInterface=(ImportantTaskOfDBInterface)activity;
    }
    public void DeleteUserAccountFromUser()
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isComplete())
                    {
                        importantTaskOfDBInterface.ImportantTaskResult(true);
                    }
                    else
                    {
                        importantTaskOfDBInterface.ImportantTaskResult(false);
                    }
                }
            });
        }
    }

    public void SignOut()
    {
        FirebaseAuth.getInstance().signOut();
    }
    public void GetUID()
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            importantTaskOfDBInterface.ImportantTaskResultAndData(true,FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        else
        {
            importantTaskOfDBInterface.ImportantTaskResultAndData(false,"User not found");
        }
    }
}

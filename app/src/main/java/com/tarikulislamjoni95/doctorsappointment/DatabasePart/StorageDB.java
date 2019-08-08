package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tarikulislamjoni95.doctorsappointment.Interface.StorageDBInterface;

public class StorageDB
{
    private Activity activity;
    private ProgressDialog progressDialog;
    private StorageDBInterface storageDBInterface;
    public StorageDB(Activity activity)
    {
        storageDBInterface =(StorageDBInterface)activity;
        this.activity=activity;
    }
    public void SaveFileIntoStorage(String FileDirectory,String FileName,byte[] FileBytes)
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMax(100);
            progressDialog.setMessage("Uploading Image ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();

            final StorageReference reference=FirebaseStorage.getInstance().getReference().child(FileDirectory).child(FileName);
            UploadTask uploadTask=reference.putBytes(FileBytes);
            Task<Uri> task=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isComplete())
                    {
                        storageDBInterface.GetResultAndUrl(true,task.getResult().toString());
                    }
                    else
                    {
                        storageDBInterface.GetResultAndUrl(false,"");
                    }
                }
            });
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setProgress((int)progress);
                    if (progress>90)
                    {
                        progressDialog.dismiss();
                    }
                    //
                }
            });
        }
    }
}

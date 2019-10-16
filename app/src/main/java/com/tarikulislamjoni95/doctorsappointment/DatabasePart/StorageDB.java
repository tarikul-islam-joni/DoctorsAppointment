package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;

import java.net.URI;
import java.util.HashMap;

public class StorageDB
{
    private GetDataFromDBInterface myDataInterface;
    private Activity activity;
    private ProgressDialog progressDialog;
    public StorageDB(Activity activity)
    {
        this.activity=activity;
        myDataInterface =(GetDataFromDBInterface) activity;
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
            final UploadTask uploadTask=reference.putBytes(FileBytes);
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
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                        hashMap.put(DBConst.URL,task.getResult().toString());
                        progressDialog.dismiss();
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetSavingFileUrlData,hashMap);
                    }
                    else
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                        hashMap.put(DBConst.URL, VARConst.UNKNOWN);
                        progressDialog.dismiss();
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetSavingFileUrlData,hashMap);
                    }
                }
            });
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setProgress((int)progress);
                }
            });
        }
    }


    public void SaveReportImageToTheMedicalReportDB(String UID, String ReportTitle, String ReportImageTitle, byte[] ReportImageBytes, final String ResultReturnKey)
    {
        if (UID.matches(DBConst.SELF))
        {
            UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        final StorageReference storageReference=FirebaseStorage.getInstance().getReference().child(DBConst.MedicalReportDB).child(UID).child(ReportTitle).child(ReportImageTitle);
        UploadTask uploadTask=storageReference.putBytes(ReportImageBytes);
        Task<Uri> task=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if (task.isComplete())
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    hashMap.put(DBConst.URL,task.getResult().toString());
                    myDataInterface.GetSingleDataFromDatabase(ResultReturnKey,hashMap);
                }
                else
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    hashMap.put(DBConst.URL, VARConst.UNKNOWN);
                    myDataInterface.GetSingleDataFromDatabase(ResultReturnKey,hashMap);
                }
            }
        });
    }
    public void DeleteEntireReportFile(String UID, String ReportTitle, final String ResultReturnKey)
    {
        if (UID.matches(DBConst.SELF))
        {
            UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        StorageReference storageReference=FirebaseStorage.getInstance().getReference().child(DBConst.MedicalReportDB).child(UID);
        storageReference.child(ReportTitle).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                myDataInterface.GetSingleDataFromDatabase(ResultReturnKey,hashMap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                myDataInterface.GetSingleDataFromDatabase(ResultReturnKey,hashMap);
            }
        });
    }

    public void DeleteReportImageByDownloadLink(final String ImageDownloadLink, final String ResultReturnKey)
    {
        StorageReference storageReference=FirebaseStorage.getInstance().getReferenceFromUrl(ImageDownloadLink);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                hashMap.put(DBConst.URL,ImageDownloadLink);
                myDataInterface.GetSingleDataFromDatabase(ResultReturnKey,hashMap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                hashMap.put(DBConst.URL,ImageDownloadLink);
                myDataInterface.GetSingleDataFromDatabase(ResultReturnKey,hashMap);
            }
        });
    }
}

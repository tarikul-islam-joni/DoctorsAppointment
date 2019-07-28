package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.DnsResolver;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class EditPatientSecureInfoActivity extends AppCompatActivity implements View.OnClickListener
{
    private MyToastClass myToastClass;
    private MyImageGettingClass myImageGettingClass;

    private int counting=0;
    private String MyString,ImageUrl1="null",ImageUrl2="null";
    private String UID;

    private ProgressDialog progressDialog;
    private Activity activity;
    private Intent intent;

    private LinearLayout ReportSectionLayout;
    private Button BnUploadBtn,ConfirmBtn,UploadAnotherDocBtn;
    private EditText BnEt;
    private ImageView BnIv,AnotherDocIv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_secure_info);
        Initialization();
        InitializationUI();
        InitializationClass();
    }

    private void Initialization()
    {
        activity= EditPatientSecureInfoActivity.this;
        UID=FirebaseAuth.getInstance().getCurrentUser().getUid();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Saving Data ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
    }
    private void InitializationUI()
    {
        ReportSectionLayout=findViewById(R.id.report_section);
        ReportSectionLayout.setVisibility(View.GONE);
        ReportSectionLayout.setEnabled(false);

        BnUploadBtn=findViewById(R.id.bn_photo_upload_btn);
        BnUploadBtn.setOnClickListener(this);

        ConfirmBtn=findViewById(R.id.confirm_btn);
        ConfirmBtn.setOnClickListener(this);

        UploadAnotherDocBtn=findViewById(R.id.upload_another_doc_btn);
        UploadAnotherDocBtn.setOnClickListener(this);

        BnEt=findViewById(R.id.bn_et);
        BnEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.VERIFICATION_CODE_VALIDITY,R.id.bn_et));
        BnEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);

        BnIv=findViewById(R.id.bn_photo_iv);
        BnIv.setEnabled(false);

        AnotherDocIv=findViewById(R.id.another_doc_iv);
        AnotherDocIv.setEnabled(false);
    }
    private void InitializationClass()
    {
        myToastClass=new MyToastClass(activity);
        myImageGettingClass =new MyImageGettingClass(activity);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.confirm_btn:
                ConfirmMethod();
                break;
            case R.id.bn_photo_upload_btn:
                BNUploadMethod();
                break;
            case R.id.upload_another_doc_btn:
                AnotherUploadMethod();
                break;

        }
    }
    private void BNUploadMethod()
    {
        MyString="ImageView1";
        myImageGettingClass.GetImageFromCameraOrGallery(R.id.bn_photo_iv);
    }
    private void AnotherUploadMethod()
    {
        MyString="ImageView2";
        myImageGettingClass.GetImageFromCameraOrGallery(R.id.another_doc_iv);
    }
    private void ConfirmMethod()
    {
        progressDialog.show();
        if (ReportSectionLayout.isEnabled())
        {
            if (BnIv.isEnabled() && AnotherDocIv.isEnabled())
            {
                Bitmap bitmap1=((BitmapDrawable)BnIv.getDrawable()).getBitmap();
                Bitmap bitmap2=((BitmapDrawable)AnotherDocIv.getDrawable()).getBitmap();
                UploadBothPhoto(bitmap1,bitmap2);
            }
            else
            {
                CancelDialog();
                myToastClass.LToast("Upload Birth certificate and also an another document for authority verification");
            }
        }
        else
        {
            if (BnEt.getCurrentTextColor()== ContextCompat.getColor(activity,R.color.colorGreen))
            {
                //Check the birth number already registered or not
                //If it is not registered yet then upload single photo and save number
                // If it is already registered then goto report section
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(DBConst.AccountMultiplicity).child(DBConst.Patient).child(BnEt.getText().toString());
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            CancelDialog();
                            ReportSectionLayout.setVisibility(View.VISIBLE);
                            ReportSectionLayout.setEnabled(true);
                        }
                        else
                        {
                            if (BnIv.isEnabled())
                            {
                                Bitmap bitmap=((BitmapDrawable)BnIv.getDrawable()).getBitmap();
                                BirthCertificateUpload(bitmap);
                            }
                            else
                            {
                                CancelDialog();
                                myToastClass.LToast("Upload birth certificate");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        CancelDialog();
                        myToastClass.LToast("Something error occurred ! ");
                    }
                });
            }
            else
            {
                BnEt.setError("Birth number must be needed");
                CancelDialog();
            }
        }
    }

    private void BirthCertificateUpload(Bitmap bitmap)
    {
        ByteArrayOutputStream bao=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bao);
        byte[] bytes=bao.toByteArray();
        String BirthCertificateImageName=FirebaseAuth.getInstance().getCurrentUser().getUid()+"BirthCertificate"+".jpg";
        final StorageReference ref= FirebaseStorage.getInstance().getReference().child(DBConst.SecureData).child(BirthCertificateImageName);
        UploadTask task=ref.putBytes(bytes);
        Task<Uri> uriTask=task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if (task.isComplete())
                {
                    Uri url=task.getResult();
                    ImageUrl2="null";
                    ImageUrl1=url.toString();
                    SaveDataIntoFirebase(false);
                }
            }
        });
        task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
            {
                double progress=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setProgress((int)progress);
            }
        });
    }

    private void UploadBothPhoto(Bitmap bitmap1,Bitmap bitmap2)
    {
        ByteArrayOutputStream bao=new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG,100,bao);
        byte[] bytes1=bao.toByteArray();
        bao=new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.JPEG,100,bao);
        byte[] bytes2=bao.toByteArray();

        String BirthCertificateImageName=UID+"BirthCertificate"+".jpg";
        final StorageReference ref= FirebaseStorage.getInstance().getReference().child(DBConst.SecureData).child(BirthCertificateImageName);
        final UploadTask task=ref.putBytes(bytes1);
        Task<Uri> uriTask=task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if (task.isComplete())
                {
                    Uri url=task.getResult();
                    ImageUrl1=url.toString();
                    counting++;
                    if (counting==2)
                    {
                        SaveDataIntoFirebase(true);
                    }
                }
            }
        });
        task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setProgress((int)progress);
            }
        });
        String AnotherDocumentImageName=UID+"AnotherDocument"+".jpg";
        final StorageReference ref1= FirebaseStorage.getInstance().getReference().child(DBConst.SecureData).child(AnotherDocumentImageName);
        UploadTask task1=ref1.putBytes(bytes2);
        Task<Uri> uriTask1=task1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return ref1.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if (task.isComplete())
                {
                    Uri url=task.getResult();
                    ImageUrl2=url.toString();
                    counting++;
                    if (counting==2)
                    {
                        SaveDataIntoFirebase(true);
                    }
                }
            }
        });
        task1.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setProgress((int)progress);
            }
        });
    }
    private void SaveDataIntoFirebase(final boolean MultipleCheck)
    {
        final DatabaseReference AccountRef=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Patient).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        AccountRef.child(DBConst.BirthCertificateNo).setValue(BnEt.getText().toString());
        AccountRef.child(DBConst.BirthCertificateImageUrl).setValue(ImageUrl1);
        AccountRef.child(DBConst.AnotherDocumentImageUrl).setValue(ImageUrl2).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isComplete())
                        {
                            DatabaseReference BCMRef=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountMultiplicity).child(DBConst.Patient).child(BnEt.getText().toString());
                            BCMRef.child(DBConst.MultipleCheck).setValue(MultipleCheck);
                            BCMRef.child(UID).setValue("")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isComplete())
                                            {
                                                DatabaseReference AccountStatusRef=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                AccountStatusRef.child(DBConst.AccountType).setValue(DBConst.Patient);
                                                AccountStatusRef.child(DBConst.AccountCompletion).setValue(true);
                                                AccountStatusRef.child(DBConst.AccountValidity).setValue(true);
                                                AccountStatusRef.child(DBConst.AuthorityValidity).setValue(false)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if (task.isComplete())
                                                                {
                                                                    intent=new Intent(activity, PatientMainActivity.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    startActivity(intent);
                                                                }
                                                                else
                                                                {
                                                                    myToastClass.LToast("Data Saving Failed ! ! !");
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
    private void CancelDialog()
    {
        if (progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myImageGettingClass.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK && data !=null)
        {
            if (MyString.matches("ImageView1"))
            {
                BnIv.setEnabled(true);
            }
            else if (MyString.matches("ImageView2"))
            {
                AnotherDocIv.setEnabled(true);
            }
        }
    }
}

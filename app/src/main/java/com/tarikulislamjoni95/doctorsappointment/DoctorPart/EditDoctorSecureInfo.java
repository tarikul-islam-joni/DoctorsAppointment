package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class EditDoctorSecureInfo extends AppCompatActivity implements View.OnClickListener
{
    private int counter=0;
    private String UID;
    private Activity activity;
    private Intent intent;

    private String WhichImageView="";
    private String Image1Url="null";
    private String Image2Url="null";
    private String BMDCRegString="";

    private MyImageGettingClass myImageGettingClass;
    private MyToastClass myToastClass;

    private EditText BMDCRegEt;
    private ImageView ImageView1,ImageView2;
    private Button UploadImageBtn1,UploadImageBtn2,ConfirmBtn;
    private LinearLayout UploadAnotherImageSection;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor_secure_info);
        Initialization();
        InitializationUI();
        InitializationClass();
    }
    private void Initialization()
    {
        activity= EditDoctorSecureInfo.this;
        UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    private void InitializationUI()
    {
        BMDCRegEt=findViewById(R.id.doctor_bmdc_reg_et);
        ImageView1=findViewById(R.id.image_view_1);
        ImageView1.setEnabled(false);
        ImageView2=findViewById(R.id.image_view_2);
        ImageView2.setEnabled(false);
        UploadImageBtn1=findViewById(R.id.upload_image_1_btn);
        UploadImageBtn2=findViewById(R.id.upload_image_2_btn);
        UploadAnotherImageSection=findViewById(R.id.upload_another_doc_section);
        UploadAnotherImageSection.setEnabled(false);
        ConfirmBtn=findViewById(R.id.confirm_btn);
    }
    private void InitializationClass()
    {
        myImageGettingClass=new MyImageGettingClass(activity);
        myToastClass=new MyToastClass(activity);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.confirm_btn:
                CheckingMethod();
                break;
            case R.id.upload_image_1_btn:
                UploadImageBtn1Method();
                break;
            case R.id.upload_image_2_btn:
                UploadImageBtn2Method();
        }
    }
    private void UploadImageBtn1Method()
    {
        WhichImageView="ImageView1";
        myImageGettingClass.GetImageFromCameraOrGallery(R.id.image_view_1);
    }
    private void UploadImageBtn2Method()
    {
        WhichImageView="ImageView2";
        myImageGettingClass.GetImageFromCameraOrGallery(R.id.image_view_2);
    }
    private void CheckingMethod()
    {
        BMDCRegString=BMDCRegEt.getText().toString();
        if (BMDCRegString.length()<4)
        {
            SetCompletionProcess("Empty");
        }
        else
        {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(DBConst.AccountMultiplicity).child(DBConst.Doctor).child(BMDCRegString);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        AddExtraDocMethod();
                    }
                    else
                    {
                        if (ImageView1.isEnabled())
                        {
                            Bitmap bitmap=((BitmapDrawable)ImageView1.getDrawable()).getBitmap();
                            UploadSingleImageMethod(bitmap);
                        }
                        else
                        {
                            myToastClass.LToast("Please send us a image of your registration certificate that we can verify you");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    myToastClass.LToast("Something error occurred.Please try again...");
                }
            });
        }
    }
    private void UploadSingleImageMethod(Bitmap bitmap)
    {
        ByteArrayOutputStream bao=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bao);
        byte[] bytes=bao.toByteArray();
        final StorageReference ref= FirebaseStorage.getInstance().getReference().child(DBConst.SecureData).child(UID+BMDCRegString+".jpg");
        UploadTask uploadTask=ref.putBytes(bytes);
        Task<Uri> task=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isComplete())
                {
                    Uri uri=task.getResult();
                    Image2Url="null";
                    Image1Url=uri.toString();
                    SaveIntoDatabase(BMDCRegString,Image1Url,Image2Url);
                }
            }
        });
    }
    private void AddExtraDocMethod()
    {
        UploadAnotherImageSection.setVisibility(View.VISIBLE);
        if (ImageView1.isEnabled() && ImageView2.isEnabled())
        {
            Bitmap bitmap1=((BitmapDrawable)ImageView1.getDrawable()).getBitmap();
            Bitmap bitmap2=((BitmapDrawable)ImageView2.getDrawable()).getBitmap();
            UploadBothImage(bitmap1,bitmap2);
        }
        else
        {
            myToastClass.LToast("Upload bmdc registration and also another document that we can verify that it is you...");
        }
    }

    private void UploadBothImage(Bitmap bitmap1,Bitmap bitmap2)
    {
        ByteArrayOutputStream bao=null;
        bao=new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG,100,bao);
        byte[] bytes1=bao.toByteArray();
        bao=new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.JPEG,100,bao);
        byte[] bytes2=bao.toByteArray();
        final StorageReference ref= FirebaseStorage.getInstance().getReference().child(DBConst.SecureData);
        UploadTask uploadTask=ref.child(UID+BMDCRegString+".jpg").putBytes(bytes1);
        Task<Uri> task=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isComplete())
                {
                    counter++;
                    Uri uri=task.getResult();
                    Image1Url=uri.toString();
                    if (counter==2)
                    {
                        SaveIntoDatabase(BMDCRegString,Image1Url,Image2Url);
                    }
                }
            }
        });
        uploadTask=ref.child(UID+"AnotherDoc"+".jpg").putBytes(bytes2);
        task=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isComplete())
                {
                    counter++;
                    Uri uri=task.getResult();
                    Image2Url=uri.toString();
                    if (counter==2)
                    {
                        SaveIntoDatabase(BMDCRegString,Image1Url,Image2Url);
                    }
                }
            }
        });
    }

    private void SaveIntoDatabase(String BMDCRegString, final String ImageUrl1,final String ImageUrl2)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Doctor).child(UID);
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put(DBConst.BMDCRegUrl,ImageUrl1);
        hashMap.put(DBConst.AnotherDocumentImageUrl,ImageUrl2);
        hashMap.put(DBConst.BMDCRegNo,BMDCRegString);
        ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isComplete())
                {
                    if (ImageUrl2.matches("null"))
                    {
                        SaveMultiplicityStatus(false);
                    }
                    else
                    {
                        SaveMultiplicityStatus(true);
                    }
                }
                else
                {
                    myToastClass.LToast("Data not saved successfully");
                }
            }
        });
    }
    private void SaveMultiplicityStatus(boolean MultipleCheck)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountMultiplicity).child(DBConst.Doctor).child(BMDCRegString);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(DBConst.MultipleCheck,MultipleCheck);
        hashMap.put(UID,"");
        ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete())
                {
                    SetCompletionProcess("NotEmpty");
                }
                else
                {
                    myToastClass.LToast("Data not saved successfully");
                }
            }
        });

    }
    private void SetCompletionProcess(final String WhichType)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).child(UID);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(DBConst.AccountType,DBConst.Doctor);
        hashMap.put(DBConst.AccountCompletion,true);
        hashMap.put(DBConst.AccountValidity,true);
        hashMap.put(DBConst.AuthorityValidity,false);
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    if (WhichType.matches("NotEmpty"))
                    {
                        intent=new Intent(activity,EditDoctorAppointmentInfoActivity.class);
                        startActivity(intent);
                    }
                    else if (WhichType.matches("Empty"))
                    {
                        intent=new Intent(activity,DoctorMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        startActivity(intent);
                    }
                }
                else
                {
                    myToastClass.LToast("Data saving unsuccessful");
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myImageGettingClass.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK && data!=null)
        {
            if (WhichImageView.matches("ImageView1"))
            {
                ImageView1.setEnabled(true);
            }
            else if (WhichImageView.matches("ImageView2"))
            {
                ImageView2.setEnabled(true);
            }
        }
    }
}

package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

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
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLocationClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyPermissionClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyPermissionGroup;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.MyCommunicator;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class PatientProfileOneActivity extends AppCompatActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener,MyCommunicator
{
    //Class Variable
    private MyToastClass myToastClass;
    private MyPermissionClass permissionClass;
    private MyPermissionGroup permissionGroup;
    private MyLocationClass myLocationClass;
    private MyImageGettingClass myImageGettingClass;

    //Primitive Variable
    private String ProfileImageUrl,FullName,FatherName,MotherName,PhoneNumber;
    private String DateOfBith,Gender,BloodGroup,CurrentAddress;
    private int VALIDATION_GREEN;
    private String AddressLatitude;
    private String AddressLongitude;
    private Bitmap ProfileImageBitmap;

    //Important Component Variable
    private Activity activity;
    private Intent intent;
    private ProgressDialog progressDialog;
    //UI Variable
    private ImageView ProfileImageCIV,ProfileAddressGoogleBtn;
    private Button ProfileImageUploadBtn,ProfileSaveBtn;
    private EditText ProfileFullNameEt,ProfileFatherNameEt,ProfileMotherNameEt,ProfileContactNumberEt;
    private EditText ProfileAddressManualEt;
    private EditText ProfileBirthDateEt,ProfileBirthMonthEt,ProfileBirthYearEt;
    private RadioGroup ProfileGenderGroup;
    private Spinner ProfileBloodGroupSpinner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_one);
        Initialization();
        InitializationUI();
        InitializationClass();
    }
    private void Initialization()
    {
        this.activity= PatientProfileOneActivity.this;
        VALIDATION_GREEN= ContextCompat.getColor(activity,R.color.colorGreen);
    }
    private void InitializationUI()
    {
        ProfileImageCIV=findViewById(R.id.profile_image_civ);
        ProfileImageCIV.setEnabled(false);

        ProfileFullNameEt=findViewById(R.id.profile_full_name_et);
        ProfileFullNameEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        ProfileFullNameEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.NAME_VALIDITY,R.id.profile_full_name_et));
        ProfileFatherNameEt=findViewById(R.id.profile_father_name_et);
        ProfileFatherNameEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        ProfileFatherNameEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.NAME_VALIDITY,R.id.profile_father_name_et));
        ProfileContactNumberEt=findViewById(R.id.profile_contact_number_et);
        ProfileContactNumberEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        ProfileContactNumberEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.PHONE_VALIDITY,R.id.profile_contact_number_et));
        ProfileMotherNameEt=findViewById(R.id.profile_mother_name_et);
        ProfileMotherNameEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        ProfileMotherNameEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.NAME_VALIDITY,R.id.profile_mother_name_et));
        ProfileAddressManualEt=findViewById(R.id.profile_address_manual_location_et);
        ProfileAddressManualEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);


        ProfileBirthDateEt=findViewById(R.id.profile_birth_date_et);
        ProfileBirthDateEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.DATE_VALIDITY,R.id.profile_birth_date_et));
        ProfileBirthMonthEt=findViewById(R.id.profile_birth_month_et);
        ProfileBirthMonthEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.MONTH_VALIDITY,R.id.profile_birth_month_et));
        ProfileBirthYearEt=findViewById(R.id.profile_birth_year_et);
        ProfileBirthYearEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.YEAR_VALIDITY,R.id.profile_birth_year_et));

        ProfileGenderGroup=findViewById(R.id.profile_gender_group);
        ProfileGenderGroup.setOnCheckedChangeListener(this);

        ProfileImageUploadBtn=findViewById(R.id.profile_image_upload_btn);
        ProfileImageUploadBtn.setOnClickListener(this);
        ProfileAddressGoogleBtn=findViewById(R.id.profile_address_google_location_iv);
        ProfileAddressGoogleBtn.setOnClickListener(this);
        ProfileSaveBtn=findViewById(R.id.profile_save_btn);
        ProfileSaveBtn.setOnClickListener(this);

        ProfileBloodGroupSpinner=findViewById(R.id.profile_blood_group_spinner);
        ProfileBloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BloodGroup=ProfileBloodGroupSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void InitializationClass()
    {
        myToastClass=new MyToastClass(activity);
        permissionGroup=new MyPermissionGroup();
        permissionClass=new MyPermissionClass(activity);
        myLocationClass=new MyLocationClass(activity);
        myImageGettingClass =new MyImageGettingClass(activity);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.profile_image_upload_btn:
                UploadImageBtnMethod();
                break;
            case R.id.profile_address_google_location_iv:
                GetCurrentLocationFromGoogleMapsMethod();
                break;
            case R.id.profile_save_btn:
                ProfileSaveMethod();
                break;
        }
    }
    private void UploadImageBtnMethod()
    {
        myImageGettingClass.GetImageFromCameraOrGallery(R.id.profile_image_civ);
    }
    private void GetCurrentLocationFromGoogleMapsMethod()
    {
        if (permissionClass.CheckAndRequestPermission(permissionGroup.getLocationGroup()))
        {
            myLocationClass.GetCoOrdinateFromMaps();
        }
    }
    private void ProfileSaveMethod()
    {
        if (ProfileFullNameEt.getCurrentTextColor()!=VALIDATION_GREEN)
        {
            ProfileFullNameEt.setError("Type a valid name");
        }
        else if (ProfileFatherNameEt.getCurrentTextColor()!=VALIDATION_GREEN)
        {
            ProfileFatherNameEt.setError("Type a valid name");
        }
        else if (ProfileMotherNameEt.getCurrentTextColor()!=VALIDATION_GREEN)
        {
            ProfileMotherNameEt.setError("Type a valid name");
        }
        else if (ProfileContactNumberEt.getCurrentTextColor()!=VALIDATION_GREEN)
        {
            ProfileContactNumberEt.setError("Type a valid phone number\n01XXXXXXXXX");
        }
        else if (ProfileBirthDateEt.getCurrentTextColor()!=VALIDATION_GREEN)
        {
            ProfileBirthDateEt.setError("Date should be between 01 to 31");
        }
        else if (ProfileBirthMonthEt.getCurrentTextColor()!=VALIDATION_GREEN)
        {
            ProfileBirthMonthEt.setError("Month should be between 01 to 12");
        }
        else if (ProfileBirthYearEt.getCurrentTextColor()!=VALIDATION_GREEN)
        {
            ProfileBirthYearEt.setError("Year should be between 1956 to (Current Year)-1");
        }
        else if (ProfileAddressManualEt.getText().toString().isEmpty())
        {
            ProfileAddressManualEt.setError("Address shouldn't be empty !");
        }
        else
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Saving Data ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            progressDialog.setCancelable(false);

            FullName=ProfileFullNameEt.getText().toString();
            FatherName=ProfileFatherNameEt.getText().toString();
            MotherName=ProfileMotherNameEt.getText().toString();
            PhoneNumber=ProfileContactNumberEt.getText().toString();
            DateOfBith=ProfileBirthDateEt.getText().toString()+"/"+ProfileBirthMonthEt.getText().toString()+"/"+ProfileBirthYearEt.getText().toString();
            if (Gender==null)
            {
                Gender="Male";
            }
            CurrentAddress=ProfileAddressManualEt.getText().toString();
            if (ProfileImageCIV.isEnabled())
            {
                ProfileImageBitmap=((BitmapDrawable)ProfileImageCIV.getDrawable()).getBitmap();
                UploadImage(ProfileImageBitmap);
            }
            else
            {
                ProfileImageUrl="null";
                SaveDataIntoFirebase();
            }
        }
    }
    public void UploadImage(Bitmap bmp)
    {
        //creating and showing progress dialog
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30,bao); // bmp is bitmap from user image file
        byte[] byteArray = bao.toByteArray();

        String UID=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        final StorageReference myStorageRef=FirebaseStorage.getInstance().getReference().child("ProfileImages").child(UID+".jpg");
        UploadTask uploadTask=myStorageRef.putBytes(byteArray);
        Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return myStorageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isComplete())
                {
                    Uri uri=task.getResult();
                    ProfileImageUrl=uri.toString();

                    SaveDataIntoFirebase();
                }
                else
                {
                    progressDialog.dismiss();
                    myToastClass.LToast("Saving failed ! Retry...");
                }
            }
        });
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
            {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                //sets and increments value of progressbar
                progressDialog.incrementProgressBy((int) progress);
            }
        });
    }
    private void SaveDataIntoFirebase()
    {
        FirebaseAuth myAuth=FirebaseAuth.getInstance();
        DatabaseReference myRef=FirebaseDatabase.getInstance().getReference(DBConst.Account).child(DBConst.Patient).child(myAuth.getCurrentUser().getUid().toString());
        myRef.child(DBConst.Image).setValue(ProfileImageUrl);
        myRef.child(DBConst.Name).setValue(FullName);
        myRef.child(DBConst.FatherName).setValue(FatherName);
        myRef.child(DBConst.MotherName).setValue(MotherName);
        myRef.child(DBConst.ContactNo).setValue(PhoneNumber);
        myRef.child(DBConst.Gender).setValue(Gender);
        myRef.child(DBConst.BloodGroup).setValue(BloodGroup);
        myRef.child(DBConst.BirthDate).setValue(DateOfBith);
        myRef.child(DBConst.Address).setValue(CurrentAddress);
        myRef.child(DBConst.AddressLongitude).setValue(AddressLongitude);
        myRef.child(DBConst.AddressLatitude).setValue(AddressLatitude)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete())
                        {
                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                    progressDialog.dismiss();
                                    if (!(boolean)dataSnapshot.child(DBConst.AccountValidity).getValue())
                                    {
                                        intent=new Intent(activity,PatientProfileTwoActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        intent=new Intent(activity,MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
    }
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i)
    {
        int radiobtnid=radioGroup.getCheckedRadioButtonId();
        RadioButton rbtn=findViewById(radiobtnid);
        Gender=rbtn.getText().toString();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myImageGettingClass.onActivityResult(requestCode,resultCode,data);
        if (requestCode == VARConst.REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            ProfileImageCIV.setEnabled(true);
        } else if (requestCode == VARConst.REQUEST_GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            ProfileImageCIV.setEnabled(true);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionClass.onRequestPermissionResult(activity,requestCode,permissions,grantResults);
    }
    @Override
    public void Communicator(String FromWhichMethod, boolean result) { }
    @Override
    public void GetDataFromCommunicator(final String FromWhichMethod, final Map<Integer, String> map)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                if (FromWhichMethod.matches("LocationError"))
                {
                    AddressLatitude="0.0";
                    AddressLongitude="0.0";
                    if (ProfileAddressManualEt.getText().toString().isEmpty())
                    {
                        ProfileAddressManualEt.setEnabled(true);
                        ProfileAddressManualEt.setHint("Type your current address");
                    }
                    myToastClass.LToast(map.get(0));
                }
                else if (FromWhichMethod.matches(VARConst.ADDRESS_LOCATION))
                {
                    ProfileAddressManualEt.setText(map.get(0));
                    AddressLatitude=map.get(1);
                    AddressLongitude=map.get(2);
                }
            }
        });
    }
}
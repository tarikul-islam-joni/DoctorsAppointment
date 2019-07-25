package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfileEditorOneActivity extends AppCompatActivity implements View.OnClickListener
{
    private MyToastClass myToastClass;
    private MyImageGettingClass myImageGettingClass;
    private MyLoadingDailog myLoadingDailog;

    private Intent intent;
    private Activity activity;

    private String[] CategoryStringArray;
    private String AvailableAreaString="";
    private StringBuilder CategoryStringBuilder;
    private String NameString="",TitleString="",DegreeString="",StudiedClgString="",NoOfYearPracString="",BMDCRegNoString="",CategoryString,ImageUrl="null";
    private Bitmap ImageBitmap;

    private AlertDialog CategorySelectionDialog;
    private EditText DoctorCategorySelectionEt;
    private CircleImageView DoctorProfileIv;
    private Button DoctorImageUploadBtn,DoctorNextPageBtn,CategorySelectionBtn;
    private EditText DoctorTitleEt,DoctorNameEt,DoctorStudiedClgEt,DoctorDegreeEt,DoctorBMDCRegEt,DoctorNoOfYearPracEt,AvailableAreaEt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_editor_one);
        Initialization();
        InitializationUI();
        InitializationClass();
        ShowDataIfAvilable();
    }

    private void Initialization()
    {
        activity=DoctorProfileEditorOneActivity.this;

        CategoryStringArray= getResources().getStringArray(R.array.doctor_category_array);

        CategoryStringBuilder=new StringBuilder();
    }
    private void InitializationUI()
    {
        DoctorProfileIv=findViewById(R.id.doctor_profile_iv);
        DoctorProfileIv.setEnabled(false);
        DoctorImageUploadBtn=findViewById(R.id.doctor_profile_upload_btn);
        DoctorImageUploadBtn.setOnClickListener(this);
        DoctorNextPageBtn=findViewById(R.id.doctor_next_page_btn);
        DoctorNextPageBtn.setOnClickListener(this);
        DoctorCategorySelectionEt=findViewById(R.id.doctor_category_et);
        DoctorTitleEt=findViewById(R.id.doctor_name_title_et);
        DoctorNameEt=findViewById(R.id.doctor_name_et);
        DoctorStudiedClgEt=findViewById(R.id.doctor_studied_clg_et);
        DoctorDegreeEt=findViewById(R.id.doctor_degree_et);
        DoctorBMDCRegEt=findViewById(R.id.doctor_bmdc_reg_et);
        DoctorNoOfYearPracEt=findViewById(R.id.doctor_no_of_year_prac_et);
        AvailableAreaEt=findViewById(R.id.available_area_et);

        CategorySelectionBtn=findViewById(R.id.doctor_category_select_btn);
        CategorySelectionBtn.setOnClickListener(this);

    }
    private void InitializationClass()
    {
        myImageGettingClass=new MyImageGettingClass(activity);
        myLoadingDailog=new MyLoadingDailog(activity,R.drawable.spinner);
        myToastClass=new MyToastClass(activity);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.doctor_profile_upload_btn:
                myImageGettingClass.GetImageFromCameraOrGallery(R.id.doctor_profile_iv);
                break;
            case R.id.doctor_next_page_btn:
                SaveDetailsAndGoNext();
                break;
            case R.id.doctor_category_select_btn:
                GettingCatergoryAlertDialog();
                break;
        }
    }

    private void SaveDetailsAndGoNext()
    {
        if (DoctorNameEt.getText().toString().length()<3)
        {
            DoctorNameEt.setError("Name can not be empty");
        }
        else if (DoctorCategorySelectionEt.getText().toString().isEmpty())
        {
            DoctorCategorySelectionEt.setError("Select category");
        } else if (AvailableAreaEt.getText().toString().isEmpty())
        {
            AvailableAreaEt.setError("Available area");
        }
        else
        {
            if (!myLoadingDailog.isShowing())
            {
                myLoadingDailog.show();
            }
            NameString=DoctorNameEt.getText().toString();
            TitleString=DoctorTitleEt.getText().toString();
            DegreeString=DoctorDegreeEt.getText().toString();
            StudiedClgString=DoctorStudiedClgEt.getText().toString();
            BMDCRegNoString=DoctorBMDCRegEt.getText().toString();
            NoOfYearPracString=DoctorNoOfYearPracEt.getText().toString();
            AvailableAreaString=AvailableAreaEt.getText().toString();
            CategoryString=DoctorCategorySelectionEt.getText().toString();
            if (DoctorProfileIv.isEnabled())
            {
                ImageBitmap=((BitmapDrawable)DoctorProfileIv.getDrawable()).getBitmap();
                UploadImageToFirebase(ImageBitmap);
            }
            else
            {
                DataSaveIntoDatabase();
            }
        }
    }

    private void UploadImageToFirebase(Bitmap bitmap)
    {
        String ImageName=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        final StorageReference ref= FirebaseStorage.getInstance().getReference().child(DBConst.ProfileImages).child(ImageName+".jpg");
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
                    ImageUrl=uri.toString();
                    DataSaveIntoDatabase();
                }
                else
                {
                    if (myLoadingDailog.isShowing())
                    {
                        myLoadingDailog.dismiss();
                    }
                    myToastClass.LToast("Saving failed ! Please try again...");
                }
            }
        });

    }
    private void DataSaveIntoDatabase()
    {
        HashMap<String,String> hashMap=new HashMap<>();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Doctor).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put(DBConst.Name,NameString);
        hashMap.put(DBConst.Title,TitleString);
        hashMap.put(DBConst.Degree,DegreeString);
        hashMap.put(DBConst.StudiedCollege,StudiedClgString);
        hashMap.put(DBConst.NoOfPracYear,NoOfYearPracString);
        hashMap.put(DBConst.AvailableArea,AvailableAreaString);
        hashMap.put(DBConst.BMDCRegNo,BMDCRegNoString);
        hashMap.put(DBConst.Image,ImageUrl);
        hashMap.put(DBConst.Category,CategoryString);
        ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isComplete())
                        {
                            if (myLoadingDailog.isShowing())
                            {
                                myLoadingDailog.dismiss();
                            }
                            intent=new Intent(activity,DoctorProfileEditorTwoActivity.class);
                            intent.putExtra(DBConst.Category,CategoryString);
                            startActivity(intent);
                        }
                    }
                });
    }
    boolean[] checked;
    private void GettingCatergoryAlertDialog()
    {
        checked=new boolean[CategoryStringArray.length];
        for(int i=0; i<CategoryStringArray.length; i++)
        {
            checked[i]=false;
        }
        CategoryStringBuilder=new StringBuilder();
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("Catergory Selection");
        builder.setMultiChoiceItems(CategoryStringArray,checked , new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b)
            {
                if (CategoryStringBuilder.length()!=0)
                {
                    CategoryStringBuilder.append(",");
                }
                CategoryStringBuilder.append(CategoryStringArray[i]);
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                DoctorCategorySelectionEt.setText(CategoryStringBuilder.toString());
                CategorySelectionDialog.dismiss();
            }
        });

        CategorySelectionDialog=builder.create();
        CategorySelectionDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myImageGettingClass.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK && data!=null)
        {
            DoctorProfileIv.setEnabled(true);
        }
    }

    private void ShowDataIfAvilable()
    {

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Doctor).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    DoctorTitleEt.setText(dataSnapshot.child(DBConst.Title).getValue().toString());
                    DoctorNameEt.setText(dataSnapshot.child(DBConst.Name).getValue().toString());
                    DoctorDegreeEt.setText(dataSnapshot.child(DBConst.Degree).getValue().toString());
                    DoctorStudiedClgEt.setText(dataSnapshot.child(DBConst.StudiedCollege).getValue().toString());
                    DoctorNoOfYearPracEt.setText(dataSnapshot.child(DBConst.NoOfPracYear).getValue().toString());
                    AvailableAreaEt.setText(dataSnapshot.child(DBConst.AvailableArea).getValue().toString());
                    DoctorBMDCRegEt.setText(dataSnapshot.child(DBConst.BMDCRegNo).getValue().toString());
                    DoctorCategorySelectionEt.setText(dataSnapshot.child(DBConst.Category).getValue().toString());
                    if (!dataSnapshot.child(DBConst.Image).getValue().toString().matches("null"))
                    {
                        Picasso.get().load(dataSnapshot.child(DBConst.Image).getValue().toString()).into(DoctorProfileIv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

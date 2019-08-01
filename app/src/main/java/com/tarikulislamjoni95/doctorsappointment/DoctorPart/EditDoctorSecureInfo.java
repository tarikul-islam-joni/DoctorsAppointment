package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountDataModel;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountMultiplicityDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDataModel;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.ImportantTaskOfDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.StorageDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountMultiplicityInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountStatusDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.ImportantTaskOfDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.StorageDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class EditDoctorSecureInfo extends AppCompatActivity implements View.OnClickListener , ImportantTaskOfDBInterface,
        AccountMultiplicityInterface, StorageDBInterface, AccountDBInterface , AccountStatusDBInterface
{
    //Database variable
    private ImportantTaskOfDB importantTaskOfDB;
    private AccountDB accountDB;
    private AccountStatusDB accountStatusDB;
    private StorageDB storageDB;
    private AccountMultiplicityDB accountMultiplicityDB;


    private AccountDataModel dataModel;
    private ArrayList<String> ImageUrlArrayList;
    private String UID;
    private Activity activity;
    private Intent intent;

    private String WhichImageView="";
    private String NIDNoString="Unknown";
    private String BMDCRegString="Unknown";
    private String NIDImageUrl="null";
    private String BMDCRegImageUrl="null";

    private MyImageGettingClass myImageGettingClass;
    private MyToastClass myToastClass;

    private EditText NIDEt,BMDCRegEt;
    private ImageView NIDIv,BMDCIv;
    private Button UploadNIDBtn,UploadBMDCBtn,ConfirmBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor_secure_info);
        Initialization();
        InitializationUI();
        InitializationClass();
        DatabaseInitialization();
    }

    private void Initialization()
    {
        activity= EditDoctorSecureInfo.this;
    }
    private void InitializationUI()
    {
        NIDEt=findViewById(R.id.nid_number_et);
        NIDEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        NIDEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.VERIFICATION_CODE_VALIDITY,R.id.nid_number_et));

        BMDCRegEt=findViewById(R.id.bmdc_reg_et);
        BMDCRegEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);

        NIDIv=findViewById(R.id.nid_iv);
        NIDIv.setEnabled(false);

        BMDCIv=findViewById(R.id.bmdc_reg_iv);
        BMDCIv.setEnabled(false);


        UploadNIDBtn=findViewById(R.id.upload_nid_btn);
        UploadNIDBtn.setOnClickListener(this);
        UploadBMDCBtn=findViewById(R.id.upload_bmdc_btn);
        UploadBMDCBtn.setOnClickListener(this);
        ConfirmBtn=findViewById(R.id.confirm_btn);
        ConfirmBtn.setOnClickListener(this);
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
            case R.id.upload_nid_btn:
                UploadNIDImageBtnMethod();
                break;
            case R.id.upload_bmdc_btn:
                UploadBMDCImageBtnMethod();
                break;
        }
    }
    private void UploadNIDImageBtnMethod()
    {
        WhichImageView="ImageView1";
        myImageGettingClass.GetImageFromCameraOrGallery(R.id.nid_iv);
    }
    private void UploadBMDCImageBtnMethod()
    {
        WhichImageView="ImageView2";
        myImageGettingClass.GetImageFromCameraOrGallery(R.id.bmdc_reg_iv);
    }
    private void CheckingMethod()
    {
        if ((NIDEt.getCurrentTextColor()== (ContextCompat.getColor(activity,R.color.colorGreen)) && NIDIv.isEnabled()))
        {
            NIDNoString=NIDEt.getText().toString();
            if (BMDCRegEt.getText().toString().isEmpty())
            {
                UploadSingleImage();
            }
            else
            {
                if (BMDCRegEt.getText().toString().length()>3 && BMDCIv.isEnabled())
                {
                    BMDCRegString=BMDCRegEt.getText().toString();
                    CheckBMDCNumber(BMDCRegString);
                }
                else
                {
                    myToastClass.LToast("Enter your BMDC Reg Number properly and also upload the image of your BMDC registration copy");
                }
            }
        }
        else
        {
            myToastClass.LToast("Enter your nid number and also upload the image of your nid copy");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myImageGettingClass.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK && data!=null)
        {
            if (WhichImageView.matches("ImageView1"))
            {
                NIDIv.setEnabled(true);
            }
            else if (WhichImageView.matches("ImageView2"))
            {
                BMDCIv.setEnabled(true);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////Database Part////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private void DatabaseInitialization()
    {
        accountDB=new AccountDB(activity);

        accountStatusDB=new AccountStatusDB(activity);

        storageDB=new StorageDB(activity);

        accountMultiplicityDB=new AccountMultiplicityDB(activity);

        importantTaskOfDB=new ImportantTaskOfDB(activity);
        importantTaskOfDB.GetUID();
    }
    ///****************************Database calling method********************************///
    private void GetAccountDataFromDatabase(String UID)
    {
        accountDB.GetDoctorAccountData(UID);
    }

    private void CheckBMDCNumber(String BMDCRegNo)
    {
        accountMultiplicityDB.GetAccountMultiplicity(DBConst.Doctor,BMDCRegNo);
    }
    private void UploadSingleImage()
    {
        ImageUrlArrayList=new ArrayList<>();
        byte[] Image_Byte=myImageGettingClass.GetFullImageBytes(R.id.nid_iv);
        String Image_Name="NID_"+UID+".jpg";
        storageDB.SaveFileIntoStorage(DBConst.SecureData,Image_Name,Image_Byte);
    }

    private void UploadBothImage()
    {
        ImageUrlArrayList=new ArrayList<>();
        byte[] Image_Byte1=myImageGettingClass.GetFullImageBytes(R.id.nid_iv);
        String Image_Name1="NID_"+UID+".jpg";
        storageDB.SaveFileIntoStorage(DBConst.SecureData,Image_Name1,Image_Byte1);
        byte[] Image_Byte2=myImageGettingClass.GetFullImageBytes(R.id.bmdc_reg_iv);
        String Image_Name2="BMDC_"+UID+".jpg";
        storageDB.SaveFileIntoStorage(DBConst.SecureData,Image_Name2,Image_Byte2);
    }
    private void setImageUrl(String Url)
    {
        ImageUrlArrayList.add(Url);
        if (ImageUrlArrayList.size()==1)
        {
            NIDImageUrl=ImageUrlArrayList.get(0);
            BMDCRegString="unknown";
            BMDCRegImageUrl="null";
        }
        else if (ImageUrlArrayList.size()==2)
        {
            NIDImageUrl=ImageUrlArrayList.get(0);
            BMDCRegImageUrl=ImageUrlArrayList.get(1);
        }
        SaveAccountData();
    }
    private void SaveAccountData()
    {
        ArrayList<AccountDataModel> arrayList=new ArrayList<>();
        arrayList.add(new AccountDataModel(
                dataModel.getProfileImageUrl(),
                dataModel.getTitle(),
                dataModel.getName(),
                dataModel.getStudiedCollege(),
                dataModel.getDegree(),
                dataModel.getCategory(),
                dataModel.getNoOfPracYear(),
                dataModel.getAvailableArea(),
                dataModel.getContactNo(),
                BMDCRegString,
                NIDNoString,
                BMDCRegImageUrl,
                NIDImageUrl));

        accountDB.SaveDoctorData(arrayList);
    }
    private void SaveAccountStatus(boolean result)
    {
        if (result)
        {
            accountStatusDB.SaveIntoAccountStatusDBFromUser(DBConst.Doctor,true,true,false);
        }
    }
    private void SetCompletionProcess()
    {
        intent=new Intent(activity,EditDoctorAppointmentInfoActivity.class);
        startActivity(intent);
    }
    ///*****************************Data Receiving method************************///
    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    ///****************************Database Interface Implementation************************///
    //ImportantTaskDb Result
    @Override
    public void ImportantTaskResult(boolean result)
    {
        if (result)
        {
            myToastClass.LToast("BMDC registration is already registered\nAnd account deleted for this wrong information ! ! !");
        }
    }
    @Override
    public void ImportantTaskResultAndData(boolean result, String data)
    {
        if (result)
        {
            setUID(data);
            GetAccountDataFromDatabase(data);
        }
        else
        {
            importantTaskOfDB.SignOut();
        }
    }

    //AccountMultiplicityDb Result
    @Override
    public void GetAccountMultiplicityResult(boolean Result)
    {
        if (Result)
        {
            importantTaskOfDB.DeleteUserAccountFromUser();
        }
        else
        {
            UploadBothImage();
        }
    }
    @Override
    public void SaveAccountMultiplicityResult(boolean Result)
    {
        SaveAccountStatus(Result);
    }
    //StorageDb Result
    @Override
    public void GetResultAndUrl(boolean result, String Url)
    {
        if (result)
        {
            setImageUrl(Url);
        }
        else
        {
            myToastClass.LToast("Uploading failed\nPlease try again...");
        }
    }

    //AccountDb Result
    @Override
    public void GetAccount(boolean result, ArrayList<AccountDataModel> arrayList)
    {
        dataModel=arrayList.get(0);
    }
    @Override
    public void AccountSavingResult(boolean result)
    {
        if (result)
        {
            accountMultiplicityDB.SaveAccountMultiplicity(DBConst.Doctor,BMDCRegString,false);
        }
    }

    //Account Status DB Result
    @Override
    public void GetAccountStatus(boolean result, ArrayList<AccountStatusDataModel> arrayList)
    {

    }
    @Override
    public void AccountStatusSavingResult(boolean result)
    {
        if (result)
        {
            SetCompletionProcess();
        }
    }
}

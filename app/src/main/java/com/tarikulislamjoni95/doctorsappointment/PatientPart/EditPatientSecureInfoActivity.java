package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
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
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountDataModel;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountMultiplicityDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDataModel;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.ImportantTaskOfDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.StorageDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountMultiplicityInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountStatusDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.ImportantTaskOfDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.StorageDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class EditPatientSecureInfoActivity extends AppCompatActivity implements View.OnClickListener,
        ImportantTaskOfDBInterface, AccountDBInterface, StorageDBInterface , AccountMultiplicityInterface, AccountStatusDBInterface
{
    //Database Variable
    private ImportantTaskOfDB importantTaskOfDB;
    private AccountDB accountDB;
    private AccountStatusDB accountStatusDB;
    private StorageDB storageDB;
    private AccountMultiplicityDB accountMultiplicityDB;

    //Class Variable
    private MyToastClass myToastClass;
    private MyImageGettingClass myImageGettingClass;
    private AccountDataModel dataModel;


    ////Primitive Variable
    private ArrayList<String> ImageArrayList;
    private boolean MultipleCheck=false;
    private String MyString,ImageUrl1="null",ImageUrl2="null";
    private String UID;

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

        DatabaseInitialization();
    }

    private void Initialization()
    {
        activity= EditPatientSecureInfoActivity.this;
        UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
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

        BnIv=findViewById(R.id.image_view_1);
        BnIv.setEnabled(false);

        AnotherDocIv=findViewById(R.id.image_view_2);
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
        myImageGettingClass.GetImageFromCameraOrGallery(R.id.image_view_1);
    }
    private void AnotherUploadMethod()
    {
        MyString="ImageView2";
        myImageGettingClass.GetImageFromCameraOrGallery(R.id.image_view_2);
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

    private void ConfirmMethod()
    {
        if (!(BnEt.getCurrentTextColor() == ContextCompat.getColor(activity, R.color.colorGreen)))
        {
            BnEt.setError("Type birth no correctly...");
        }
        else if (!BnIv.isEnabled())
        {
            myToastClass.LToast("Upload your birth certificate image.");
        }
        else
        {
            //Check the birth number already registered or not
            //If it is not registered yet then upload single photo and save number
            // If it is already registered then goto report section
            CheckMultiplicityStatus(DBConst.Patient, BnEt.getText().toString());
        }
    }
    ///*******************Receiving Data From Database****************///
    //Getter and Setter Method for UID and UID is needed for uploading image title
    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
    //Set ImageUrl1 and ImageUrl2 and this method is called from interface
    private void setImageUrl(String url)
    {
        ImageArrayList.add(url);
        if (ImageArrayList.size()==1)
        {
            ImageUrl1=ImageArrayList.get(0);
            ImageUrl2="null";
            ///Saving account data method called
            SaveAccountData();
        }
        else if (ImageArrayList.size()==2)
        {
            ImageUrl1=ImageArrayList.get(0);
            ImageUrl2=ImageArrayList.get(1);
            ///Saving account data method called
            SaveAccountData();
        }
    }
    //Get and Set the existing data
    private void GetAccountDataFromFirebase(AccountDataModel dataModel)
    {
        this.dataModel=dataModel;
    }
    //Get account multiplicity status from interface and upload image
    private void GetMultiplicityStatusAndSaveData(boolean MultipleCheck)
    {
        this.MultipleCheck=MultipleCheck;
        ImageArrayList=new ArrayList<>();
        if (MultipleCheck)
        {
            ReportSectionLayout.setVisibility(View.VISIBLE);
            if (BnIv.isEnabled() && AnotherDocIv.isEnabled() && BnEt.getText().toString().length()>=8)
            {
                byte[] ImageByte1=myImageGettingClass.GetCompressImageBytes(R.id.image_view_1);
                byte[] ImageByte2=myImageGettingClass.GetCompressImageBytes(R.id.image_view_2);
                UploadBothImage(ImageByte1,ImageByte2);
            }
            else
            {
                myToastClass.LToast("Type birth certificate and upload Birth certificate and also an another document for authority verification");
            }
        }
        else
        {

            ReportSectionLayout.setVisibility(View.GONE);
            if (BnIv.isEnabled()&& BnEt.getText().toString().length()>=8)
            {
                byte[] ImageByte=myImageGettingClass.GetCompressImageBytes(R.id.image_view_1);
                UploadSingleImage(ImageByte);
            }
            else
            {
                myToastClass.LToast("Type birth certificate number and upload Birth certificate for authority verification");
            }
        }
    }


    ///****************************Database Calling Method****************************///
    private void CheckMultiplicityStatus(String patient, String BirthNo)
    {
        accountMultiplicityDB.GetAccountMultiplicity(patient,BirthNo);
    }
    private void UploadSingleImage(byte[] ImageByte)
    {
        String BirthCertificateImageName=getUID()+"BirthCertificate"+".jpg";
        storageDB.SaveFileIntoStorage(DBConst.SecureData,BirthCertificateImageName,ImageByte);
    }
    private void UploadBothImage(byte[] ImageByte1,byte[] ImageByte2)
    {
        String BirthCertificateImageName=getUID()+"BirthCertificate"+".jpg";
        storageDB.SaveFileIntoStorage(DBConst.SecureData,BirthCertificateImageName,ImageByte1);
        String AnotherDocumentImageName=UID+"AnotherDocument"+".jpg";
        storageDB.SaveFileIntoStorage(DBConst.SecureData,AnotherDocumentImageName,ImageByte2);
    }

    //SaveAccountData is called after the image uploaded
    private void SaveAccountData()
    {
        ArrayList<AccountDataModel> arrayList=new ArrayList<>();
        arrayList.add(new AccountDataModel(
                dataModel.getProfileImageUrl(),
                dataModel.getName(),
                dataModel.getFatherName(),
                dataModel.getMotherName(),
                dataModel.getContactNo(),
                dataModel.getGender(),
                dataModel.getBloodGroup(),
                dataModel.getBirthDate(),
                dataModel.getAddress(),
                dataModel.getBirthCertificateNo(),
                ImageUrl1,ImageUrl2
        ));
        accountDB.SavePatientAccountDataIntoDB(arrayList);

    }
    //After saving Account Data with ImageUrl1 and ImageUrl2 , save multiplicity status
    private void SaveMultiplicityStatus()
    {
        accountMultiplicityDB.SaveAccountMultiplicity(DBConst.Patient,BnEt.getText().toString(),MultipleCheck);
    }
    //Finally save the account status
    private void SaveAccountStatus()
    {
        accountStatusDB.SaveIntoAccountStatusDBFromUser(DBConst.Patient,true,true,false);
    }
    private void GotoNextActivity(boolean result)
    {
        if (result)
        {
            intent=new Intent(activity,PatientMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    ///*************************Database Interface Part*************************///
    //Database class initialization
    private void DatabaseInitialization()
    {
        importantTaskOfDB=new ImportantTaskOfDB(activity);
        importantTaskOfDB.GetUID();

        accountDB=new AccountDB(activity);
        accountDB.GetPatientAccountData();

        storageDB=new StorageDB(activity);

        accountMultiplicityDB=new AccountMultiplicityDB(activity);

        accountStatusDB=new AccountStatusDB(activity);
    }

    ///Database Interface Implementation
    @Override
    public void ImportantTaskResult(boolean result) {
        //Not Using
    }

    @Override
    public void ImportantTaskResultAndData(boolean result, String data)
    {
        if (result)
        {
            setUID(data);
        }
        else
        {
            importantTaskOfDB.SignOut();
        }
    }

    @Override
    public void GetAccount(boolean result,ArrayList<AccountDataModel> arrayList)
    {
        if (result)
        {
            GetAccountDataFromFirebase
                    (
                            new AccountDataModel(
                                    arrayList.get(0).getProfileImageUrl(),
                                    arrayList.get(0).getName(),
                                    arrayList.get(0).getFatherName(),
                                    arrayList.get(0).getMotherName(),
                                    arrayList.get(0).getContactNo(),
                                    arrayList.get(0).getGender(),
                                    arrayList.get(0).getBloodGroup(),
                                    arrayList.get(0).getBirthDate(),
                                    arrayList.get(0).getAddress(),
                                    arrayList.get(0).getBirthCertificateNo(),
                                    arrayList.get(0).getBirthCertificateImageUrl(),
                                    arrayList.get(0).getAnotherDocumentImageUrl()
                            )
                    );
        }
    }



    @Override
    public void GetResultAndUrl(boolean result, String Url)
    {
        if (result)
        {
            setImageUrl(Url);
        }
    }

    //***************************Call all the Save Data Method from interface completion one by one*******************************//
    @Override
    public void GetAccountMultiplicityResult(boolean Result)
    {
        GetMultiplicityStatusAndSaveData(Result);
    }
    @Override
    public void AccountSavingResult(boolean result)
    {
        if (result)
        {
            SaveMultiplicityStatus();
        }
    }

    @Override
    public void SaveAccountMultiplicityResult(boolean Result)
    {
        if (Result)
        {
            SaveAccountStatus();
        }
    }

    @Override
    public void GetAccountStatus(boolean result, ArrayList<AccountStatusDataModel> arrayList) {
        //Not Using
    }

    @Override
    public void AccountStatusSavingResult(boolean result)
    {
        GotoNextActivity(result);
    }
}

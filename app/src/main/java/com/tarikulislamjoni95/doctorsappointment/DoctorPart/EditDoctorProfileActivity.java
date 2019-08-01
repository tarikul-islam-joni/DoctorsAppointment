package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountDataModel;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDataModel;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.ImportantTaskOfDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.StorageDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountStatusDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.ImportantTaskOfDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.StorageDBInterface;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.EditPatientSecureInfoActivity;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.PatientMainActivity;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditDoctorProfileActivity extends AppCompatActivity implements View.OnClickListener, ImportantTaskOfDBInterface,
        AccountDBInterface, StorageDBInterface , AccountStatusDBInterface
{
    //Database Variable
    private ImportantTaskOfDB importantTaskOfDB;
    private StorageDB storageDB;
    private AccountDB accountDB;
    private AccountStatusDB accountStatusDB;
    //Class Variable
    private MyToastClass myToastClass;
    private MyImageGettingClass myImageGettingClass;
    private MyLoadingDailog myLoadingDailog;

    //Important Variable
    private Intent intent;
    private Activity activity;

    //Primitive variable
    private boolean[] Specialization_Checked;
    private String UID="null";
    private String ProfileImageUrlString="null",TitleString="Unknown",NameString="Unknown",StudiedClgString="Unknown",DegreeString="Unknown";
    private String NoOfYearPracString="Unknown",CategoryString="Unknown", AvailableAreaString="Unknown",ContactNoString="Unknown";
    private String BMDCRegNoString="Unknown",NIDNoString="Unknown",BMDCRegImageUrlString="null",NIDImageUrlString="null",AnotherDocImageUrlString="null";

    private String[] CategoryResourceStringArray;
    private AlertDialog CategorySelectionDialog;

    private EditText DoctorCategorySelectionEt;
    private ImageView DoctorProfileIv;
    private Button DoctorImageUploadBtn,DoctorNextPageBtn,CategorySelectionBtn;
    private EditText DoctorTitleEt,DoctorNameEt,DoctorStudiedClgEt,DoctorDegreeEt,DoctorNoOfYearPracEt,DoctorContactNoEt,AvailableAreaEt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor_profile);
        Initialization();
        InitializationUI();
        InitializationClass();

        DatabseInitialization();
    }

    private void Initialization()
    {
        activity= EditDoctorProfileActivity.this;

        CategoryResourceStringArray= getResources().getStringArray(R.array.doctor_category_array);
    }
    private void InitializationUI()
    {
        DoctorProfileIv=findViewById(R.id.doctor_profile_iv);
        DoctorProfileIv.setEnabled(false);

        DoctorImageUploadBtn=findViewById(R.id.doctor_profile_upload_btn);
        DoctorImageUploadBtn.setOnClickListener(this);

        DoctorNextPageBtn=findViewById(R.id.doctor_next_page_btn);
        DoctorNextPageBtn.setOnClickListener(this);

        CategorySelectionBtn=findViewById(R.id.doctor_category_select_btn);
        CategorySelectionBtn.setOnClickListener(this);

        DoctorTitleEt=findViewById(R.id.doctor_name_title_et);
        DoctorTitleEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);

        DoctorNameEt=findViewById(R.id.doctor_name_et);
        DoctorNameEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);

        DoctorStudiedClgEt=findViewById(R.id.doctor_studied_clg_et);
        DoctorStudiedClgEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);

        DoctorDegreeEt=findViewById(R.id.doctor_degree_et);
        DoctorDegreeEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);

        DoctorContactNoEt=findViewById(R.id.profile_contact_number_et);
        DoctorContactNoEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        DoctorContactNoEt.addTextChangedListener(new MyTextWatcher(activity, VARConst.PHONE_VALIDITY,R.id.profile_contact_number_et));

        DoctorNoOfYearPracEt=findViewById(R.id.doctor_no_of_year_prac_et);
        DoctorNoOfYearPracEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);

        AvailableAreaEt=findViewById(R.id.available_area_et);
        AvailableAreaEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);

        DoctorCategorySelectionEt=findViewById(R.id.doctor_category_et);
        DoctorCategorySelectionEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);

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
    private void GettingCatergoryAlertDialog()
    {
        final ArrayList<String> CategoryArrayList=new ArrayList<>();
        Specialization_Checked=new boolean[CategoryResourceStringArray.length];
        for(int i=0; i<CategoryResourceStringArray.length; i++)
        {
            Specialization_Checked[i]=false;
        }

        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("Specialization Selection");
        builder.setMultiChoiceItems(CategoryResourceStringArray,Specialization_Checked , new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b)
            {
                if (b)
                {
                    CategoryArrayList.add(CategoryResourceStringArray[i]);
                }
                else
                {
                    for (int k=0; k<CategoryArrayList.size(); i++)
                    {
                        if (CategoryArrayList.get(k).matches(CategoryResourceStringArray[i]))
                        {
                            CategoryArrayList.remove(k);
                        }
                    }
                }
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                StringBuilder CategoryStringBuilder=new StringBuilder();
                for(int k=0; k<CategoryArrayList.size(); k++)
                {
                    if (k!=0)
                    {
                        CategoryStringBuilder.append(",");
                    }
                    CategoryStringBuilder.append(CategoryArrayList.get(k));
                }
                DoctorCategorySelectionEt.setText(CategoryStringBuilder.toString());
                CategoryString=CategoryStringBuilder.toString();
                CategorySelectionDialog.dismiss();
            }
        });

        CategorySelectionDialog=builder.create();
        CategorySelectionDialog.show();
    }
    private void SaveDetailsAndGoNext()
    {
        if (DoctorNameEt.getText().toString().length()<3)
        {
            DoctorNameEt.setError("Name can not be empty");
        }
        else if (DoctorContactNoEt.getCurrentTextColor()!= ContextCompat.getColor(activity,R.color.colorGreen))
        {
            DoctorContactNoEt.setError("Number must be needed and format will be 01XXXXXXXXX");
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
            NameString=DoctorNameEt.getText().toString();
            TitleString=DoctorTitleEt.getText().toString();
            DegreeString=DoctorDegreeEt.getText().toString();
            StudiedClgString=DoctorStudiedClgEt.getText().toString();
            ContactNoString=DoctorContactNoEt.getText().toString();
            NoOfYearPracString=DoctorNoOfYearPracEt.getText().toString();
            AvailableAreaString=AvailableAreaEt.getText().toString();
            CategoryString=DoctorCategorySelectionEt.getText().toString();
            if (DoctorProfileIv.isEnabled())
            {
                if (UID!=null)
                {
                    byte[] Image_Byte=myImageGettingClass.GetCompressImageBytes(R.id.doctor_profile_iv);
                    UploadImageToFirebase(Image_Byte);
                }
            }
            else
            {
                DataSaveIntoDatabase(ProfileImageUrlString,TitleString,NameString,StudiedClgString,DegreeString,CategoryString,NoOfYearPracString,AvailableAreaString,ContactNoString,BMDCRegNoString,NIDNoString,BMDCRegImageUrlString,NIDImageUrlString);
            }
        }
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


    /////////////////////////////////////////////////////////////////////////////
    ///////////////////////**********Database Part***********///////////////////
    ///////////////////////////////////////////////////////////////////////////
    private void DatabseInitialization()
    {
        accountDB=new AccountDB(activity);

        accountStatusDB=new AccountStatusDB(activity);

        storageDB=new StorageDB(activity);

        importantTaskOfDB=new ImportantTaskOfDB(activity);
        importantTaskOfDB.GetUID();
    }
    ///*****************Database Calling Method************************///
    private void UploadImageToFirebase(byte[] Image_Byte)
    {
        String ImageName=UID+".jpg";
        storageDB.SaveFileIntoStorage(DBConst.ProfileImages,ImageName,Image_Byte);
    }
    private void DataSaveIntoDatabase(String profileImageUrlString, String titleString, String nameString, String studiedClgString, String degreeString, String categoryString, String noOfYearPracString, String availableAreaString, String contactNoString, String BMDCRegNoString, String NIDNoString, String BMDCRegImageUrlString, String NIDImageUrlString)
    {
        ArrayList<AccountDataModel> arrayList=new ArrayList<>();
        arrayList.add(new AccountDataModel(profileImageUrlString,titleString,nameString,studiedClgString,degreeString,categoryString,noOfYearPracString,availableAreaString,contactNoString,BMDCRegNoString,NIDNoString,BMDCRegImageUrlString,NIDImageUrlString));

        accountDB.SaveDoctorData(arrayList);
    }

    ///*****************Data Receiving and Calling Method***************************///
    private void GetDataIfAvailable(String UID)
    {
        accountDB.GetDoctorAccountData(UID);
    }
    ///******************************Data Receiving From Database Method********************///
    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
    private void ShowDataIfAvilable(AccountDataModel dataModel)
    {
        Log.d("myError","NAme : "+dataModel.getName());
        ProfileImageUrlString=dataModel.getProfileImageUrl();
        TitleString=dataModel.getTitle();
        NameString=dataModel.getName();
        StudiedClgString=dataModel.getStudiedCollege();
        DegreeString=dataModel.getDegree();
        CategoryString=dataModel.getCategory();
        NoOfYearPracString=dataModel.getNoOfPracYear();
        AvailableAreaString=dataModel.getAvailableArea();
        ContactNoString=dataModel.getContactNo();
        BMDCRegNoString=dataModel.getBMDCRegNo();
        NIDNoString=dataModel.getNIDNo();
        BMDCRegImageUrlString=dataModel.getBMDCRegImageUrl();
        NIDImageUrlString=dataModel.getNIDImageUrl();
        if (!ProfileImageUrlString.matches("null"))
        {
            DoctorProfileIv.setEnabled(true);
            Picasso.get().load(ProfileImageUrlString).into(DoctorProfileIv);
        }

        DoctorTitleEt.setText(TitleString);
        DoctorNameEt.setText(NameString);
        DoctorStudiedClgEt.setText(StudiedClgString);
        DoctorDegreeEt.setText(DegreeString);
        DoctorNoOfYearPracEt.setText(NoOfYearPracString);
        DoctorCategorySelectionEt.setText(CategoryString);
        AvailableAreaEt.setText(AvailableAreaString);
        DoctorContactNoEt.setText(ContactNoString);
    }
    private void CheckValidityAndSendToNextLabel(final boolean AccountValidity)
    {
        if (!AccountValidity)
        {
            intent=new Intent(activity, EditDoctorSecureInfo.class);
            startActivity(intent);
        }
        else
        {
            intent=new Intent(activity, DoctorMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void ImportantTaskResult(boolean result) {
    //Not in Use
    }

    @Override
    public void ImportantTaskResultAndData(boolean result, String data)
    {
        Log.d("myErrror",result+" UID "+data);
        if (result)
        {
            setUID(data);
            GetDataIfAvailable(data);
        }
        else
        {
            setUID("null");
            importantTaskOfDB.SignOut();
        }
    }


    @Override
    public void GetAccount(boolean result, ArrayList<AccountDataModel> arrayList)
    {
        Log.d("myErrror",result+" UID "+" founded");
        if (result)
        {
            AccountDataModel dataModel=new AccountDataModel
                    (
                            arrayList.get(0).getProfileImageUrl(),
                            arrayList.get(0).getTitle(),
                            arrayList.get(0).getName(),
                            arrayList.get(0).getStudiedCollege(),
                            arrayList.get(0).getDegree(),
                            arrayList.get(0).getCategory(),
                            arrayList.get(0).getNoOfPracYear(),
                            arrayList.get(0).getAvailableArea(),
                            arrayList.get(0).getContactNo(),
                            arrayList.get(0).getBMDCRegNo(),
                            arrayList.get(0).getNIDNo(),
                            arrayList.get(0).getBMDCRegImageUrl(),
                            arrayList.get(0).getNIDImageUrl()
                    );
            ShowDataIfAvilable(dataModel);
        }

    }

    @Override
    public void AccountSavingResult(boolean result)
    {
        if (result)
        {
            accountStatusDB.GetUIDAccountStatusData();
        }
        else
        {
            myToastClass.LToast("Data saving unsuccessful,Try again later");
        }
    }

    @Override
    public void GetResultAndUrl(boolean result, String Url)
    {
        if (result)
        {
            ProfileImageUrlString=Url;
        }
        else
        {
            ProfileImageUrlString="null";
        }
        DataSaveIntoDatabase(ProfileImageUrlString,TitleString,NameString,StudiedClgString,DegreeString,CategoryString,NoOfYearPracString,AvailableAreaString,ContactNoString,BMDCRegNoString,NIDNoString,BMDCRegImageUrlString,NIDImageUrlString);
    }

    @Override
    public void GetAccountStatus(boolean result, ArrayList<AccountStatusDataModel> arrayList)
    {
        if (result)
        {
            CheckValidityAndSendToNextLabel(arrayList.get(0).isAccountValidity());
        }
        else
        {
            myToastClass.LToast("Data saving unsuccessful,Try again later");
        }
    }

    @Override
    public void AccountStatusSavingResult(boolean result) {

    }
}

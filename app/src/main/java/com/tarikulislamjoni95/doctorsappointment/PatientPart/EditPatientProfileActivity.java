package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountDataModel;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDataModel;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.ImportantTaskOfDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.StorageDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLocationClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyPermissionClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyPermissionGroup;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountCreationInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountStatusDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetLocationInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.ImportantTaskOfDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.StorageDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.Map;

public class EditPatientProfileActivity extends AppCompatActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener,
        GetLocationInterface,AccountCreationInterface, ImportantTaskOfDBInterface, StorageDBInterface, AccountDBInterface, AccountStatusDBInterface
{
    //Database Class
    private StorageDB storageDB;
    private AccountDB accountDB;
    private AccountStatusDB accountStatusDB;
    private ImportantTaskOfDB importantTaskOfDB;

    //Class Variable
    private MyToastClass myToastClass;
    private MyLoadingDailog myLoadingDailog;
    private MyPermissionClass permissionClass;
    private MyPermissionGroup permissionGroup;
    private MyLocationClass myLocationClass;
    private MyImageGettingClass myImageGettingClass;

    //Primitive Variable
    private boolean AccountValidity;
    private int VALIDATION_GREEN;
    private String UID;
    private String NameString="Unknown";
    private String FatherNameString="Unknown";
    private String MotherNameString="Unknown";
    private String ContactNoString="Unknown";
    private String GenderString="Male";
    private String BirthDateString="Unknown";
    private String BloodGroupString="Unknown";
    private String ImageUrlString="Unknown";
    private String BirthCertificateNoString="Unknown";
    private String BirthCertificateUrlString="Unknown";
    private String AnotherDocUrlString="Unknown";
    private String CurrentAddressString="Unknown";

    //Important Component Variable
    private Activity activity;
    private Intent intent;
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
        setContentView(R.layout.activity_edit_patient_profile);
        Initialization();
        InitializationUI();
        InitializationClass();
    }
    private void Initialization()
    {
        activity= EditPatientProfileActivity.this;
        VALIDATION_GREEN= ContextCompat.getColor(activity,R.color.colorGreen);
    }
    private void InitializationUI()
    {
        ProfileImageCIV=findViewById(R.id.image_iv);
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
                BloodGroupString=ProfileBloodGroupSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }
    private void InitializationClass()
    {
        myToastClass=new MyToastClass(activity);
        myLoadingDailog=new MyLoadingDailog(activity,R.drawable.spinner);
        permissionGroup=new MyPermissionGroup();
        permissionClass=new MyPermissionClass(activity);
        myLocationClass=new MyLocationClass(activity);
        myImageGettingClass =new MyImageGettingClass(activity);

        DatabaseClassInitialization();
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
    //Get Data from database if available and method is called from interface implementation
    private void ShowAvailableData(String name, String fatherName, String motherName, String contactNo, String gender, String bloodGroup, String birthDate, String address, String birthCertificateNo, String birthCertificateImageUrl, String anotherDocumentImageUrl, String profileImageUrl)
    {
        NameString=name;
        FatherNameString=fatherName;
        MotherNameString=motherName;
        ContactNoString=contactNo;
        GenderString=gender;
        BloodGroupString=bloodGroup;
        BirthDateString=birthDate;
        CurrentAddressString=address;
        BirthCertificateNoString=birthCertificateNo;
        BirthCertificateUrlString=birthCertificateImageUrl;
        AnotherDocUrlString=anotherDocumentImageUrl;
        ImageUrlString=profileImageUrl;
        if(!ImageUrlString.matches("null"))
        {
            ProfileImageCIV.setEnabled(true);
            Picasso.get().load(ImageUrlString).into(ProfileImageCIV);
        }
        ProfileFullNameEt.setText(NameString);
        ProfileFatherNameEt.setText(FatherNameString);
        ProfileMotherNameEt.setText(MotherNameString);
        ProfileAddressManualEt.setText(CurrentAddressString);
        ProfileContactNumberEt.setText(ContactNoString);
        if (GenderString.matches("Male"))
        {
            RadioButton rbtn=findViewById(R.id.profile_male_rbtn);
            rbtn.setChecked(true);
        }
        else
        {
            RadioButton rbtn=findViewById(R.id.profile_female_rbtn);
            rbtn.setChecked(true);
        }
        String[] BirthDateMonthYear=BirthDateString.split("-",0);
        ProfileBirthDateEt.setText(BirthDateMonthYear[0]);
        ProfileBirthMonthEt.setText(BirthDateMonthYear[1]);
        ProfileBirthYearEt.setText(BirthDateMonthYear[2]);

        String[] bloodarray=getResources().getStringArray(R.array.blood_group);

        ArrayList<String> arrayList1=new ArrayList<>();
        arrayList1.add(BloodGroupString);
        for(int i=0; i<bloodarray.length; i++)
        {
            if (!bloodarray[i].matches(BloodGroupString))
            {
                arrayList1.add(bloodarray[i]);
            }
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(activity,android.R.layout.simple_list_item_1,arrayList1);
        ProfileBloodGroupSpinner.setAdapter(arrayAdapter);
    }
    //ShowLoading Method
    private void ShowLoadingDialog()
    {
        if (!myLoadingDailog.isShowing())
        {
            myLoadingDailog.show();
        }
    }
    //HideLoading Method
    private void CancelLoadingDialog()
    {
        if (myLoadingDailog.isShowing())
        {
            myLoadingDailog.dismiss();
        }
    }
    //Gender Radio Button OnCheckedChanged Method
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i)
    {
        int radiobtnid=radioGroup.getCheckedRadioButtonId();
        RadioButton rbtn=findViewById(radiobtnid);
        GenderString=rbtn.getText().toString();
    }
    //Get Image From Gallery Or Camera Method
    private void UploadImageBtnMethod()
    {
        myImageGettingClass.GetImageFromCameraOrGallery(R.id.image_iv);
    }
    //Get Image OnActivityResult
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
    //Get Location From Location Class
    private void GetCurrentLocationFromGoogleMapsMethod()
    {
        if (permissionClass.CheckAndRequestPermission(permissionGroup.getLocationGroup()))
        {
            myLocationClass.GetCoOrdinateFromMaps();
        }
    }
    //Get OnRequestPermissionsResult
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionClass.onRequestPermissionResult(activity,requestCode,permissions,grantResults);
    }
    //Getting Location From Location Class
    @Override
    public void GetLocation(final String FromWhichMethod, final Map<Integer, String> map)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                if (FromWhichMethod.matches("LocationError"))
                {
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
                }
            }
        });
    }
    ///Getting AccountValidity and Result From Database Change the state
    private void GotoNextActivity(boolean AccountValidity,boolean Result)
    {
        CancelLoadingDialog();
        if (Result)
        {
            if (AccountValidity)
            {
                intent=new Intent(activity,PatientMainActivity.class);
                startActivity(intent);
            }
            else
            {
                intent=new Intent(activity,EditPatientSecureInfoActivity.class);
                startActivity(intent);
            }
        }
        else
        {
            myToastClass.LToast("Data not saved");
        }
    }

    //********************************Database Calling here in this method*************************//
    private void DatabaseClassInitialization()
    {
        ///*************************Database variable initialization***********************///
        importantTaskOfDB=new ImportantTaskOfDB(activity);
        //Get the UID for saving profile image name
        importantTaskOfDB.GetUID();

        accountDB=new AccountDB(activity);
        //Get the existing data if available
        accountDB.GetPatientAccountData();

        //Get account validity status for check whether it should be go main activity or editing secure info activity
        accountStatusDB=new AccountStatusDB(activity);
        accountStatusDB.GetUIDAccountStatusData();

        storageDB=new StorageDB(activity);
    }
    //********************************Database Calling here in this method*************************//
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
            NameString=ProfileFullNameEt.getText().toString();
            FatherNameString=ProfileFatherNameEt.getText().toString();
            MotherNameString=ProfileMotherNameEt.getText().toString();
            ContactNoString=ProfileContactNumberEt.getText().toString();
            BirthDateString=ProfileBirthDateEt.getText().toString()+"-"+ProfileBirthMonthEt.getText().toString()+"-"+ProfileBirthYearEt.getText().toString();
            CurrentAddressString=ProfileAddressManualEt.getText().toString();
            if (ProfileImageCIV.isEnabled())
            {
                byte[] image_byte=myImageGettingClass.GetCompressImageBytes(R.id.image_iv);
                if (UID!=null)
                {
                    //Upload image and get the download url in implementation section
                    storageDB.SaveFileIntoStorage(DBConst.ProfileImages,UID+".jpg",image_byte);
                }
                else
                {
                    //If the uid is null then signout
                    importantTaskOfDB.SignOut();
                }
            }
            else
            {
                //If there is no image then save other data and keep the imageurl unknown
                ShowLoadingDialog();
                SaveDataIntoFirebase();
            }
        }
    }

    //********************************Database Calling here in this method*************************//
    //SaveDataIntoFirebase Called from ProfileSaveMethod when no image found;
    //If there was a image then first upload the image and get called from interface implementation with getting imageurl
    private void SaveDataIntoFirebase()
    {
        ArrayList<AccountDataModel> arrayList=new ArrayList<>();
        arrayList.add(new AccountDataModel(
                ImageUrlString,NameString,FatherNameString,MotherNameString,ContactNoString,
                GenderString,BloodGroupString,BirthDateString,CurrentAddressString,
                BirthCertificateNoString,BirthCertificateUrlString,AnotherDocUrlString
        ));
        //Saving process called
        accountDB.SavePatientAccountDataIntoDB(arrayList);
    }


    ///************************Database Implementation Starting***************************///
    @Override
    public void ImportantTaskResult(boolean result) {
        //Not Using
    }

    @Override
    public void ImportantTaskResultAndData(boolean result, String data)
    {
        if (result)
        {
            UID=data;
        }
        else
        {
            UID=null;
        }
    }

    @Override
    public void GetResultAndUrl(boolean result, String Url)
    {
        if (result)
        {
            ImageUrlString =Url;
            SaveDataIntoFirebase();
        }
        else
        {
            importantTaskOfDB.SignOut();
        }
    }
    @Override
    public void AccountCreationResult(String FromWhichMethod, boolean result) {

    }

    @Override
    public void GetAccount(boolean result, ArrayList<AccountDataModel> arrayList)
    {
        if (result)
        {
            ShowAvailableData(
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
                    arrayList.get(0).getAnotherDocumentImageUrl(),
                    arrayList.get(0).getProfileImageUrl()
            );
        }
    }

    @Override
    public void AccountSavingResult(boolean result)
    {
        //Goto Next Activity
        GotoNextActivity(AccountValidity,result);
    }

    @Override
    public void GetAccountStatus(boolean result, ArrayList<AccountStatusDataModel> arrayList)
    {
        AccountValidity=arrayList.get(0).isAccountValidity();
    }

    @Override
    public void AccountStatusSavingResult(boolean result) {
        //Not using
    }
}

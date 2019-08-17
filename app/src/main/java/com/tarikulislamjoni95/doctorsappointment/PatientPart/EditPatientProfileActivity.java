package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDM;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.PatientAccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.PatientAccountDM;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBHelper;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.StorageDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLocationClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountStatusDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetLocationInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.PatientAccountDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.DBHelperInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.StorageDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class EditPatientProfileActivity extends AppCompatActivity implements View.OnClickListener, GetLocationInterface, DBHelperInterface, PatientAccountDBInterface, AccountStatusDBInterface, StorageDBInterface
{
    //Database Variable
    private DBHelper dbHelper;
    private PatientAccountDB patientAccountDB;
    private AccountStatusDB accountStatusDB;
    private StorageDB storageDB;

    //Class Variable
    private MyToastClass myToastClass;
    private MyImageGettingClass myImageGettingClass;
    private MyLocationClass myLocationClass;

    //Object Variable
    private Intent intent;
    private Activity activity;

    //Resource Variable
    private String[] BloodGroupRes;
    private int VALIDITY_COLOR;
    //Primitive Variable
    private byte[] ProfileImageByte;
    private String UID;
    private String ProfileImageUrl=VARConst.UNKNOWN,BirthImageUrl=VARConst.UNKNOWN,AnotherDocImageUrl=VARConst.UNKNOWN;
    private String FullNameString=VARConst.UNKNOWN,FatherNameString=VARConst.UNKNOWN,MotherNameString=VARConst.UNKNOWN;
    private String PhoneNumberString=VARConst.UNKNOWN,DateOfBirthString=VARConst.UNKNOWN,BloodGroupString=VARConst.UNKNOWN;
    private String AddressString=VARConst.UNKNOWN,GenderString=VARConst.UNKNOWN,BirthNumberString=VARConst.UNKNOWN;

    //UI Variable
    private ImageView ProfileIv;
    private Button ProfileImageUploadBtn,ProfileImageCancelBtn,SaveBtn;
    private EditText FullNameEt,FatherNameEt,MotherNameEt,PhoneNumberEt,AddressEt;
    private Spinner BloodGroupSpinner;
    private TextView DateOfBirthTv;
    private ImageView DateOfBirthBtnIv,AddressBtnIv;
    private RadioGroup GenderGroup;
    private RadioButton MaleRbtn,FemaleRbtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_profile);
        Initialization();
        InitializationUI();
        InitializationClass();
        InitializationDB();
        CallDBForUID();
        CallDBForExistingData(UID);
    }

    private void Initialization()
    {
        activity=EditPatientProfileActivity.this;
        BloodGroupRes=getResources().getStringArray(R.array.blood_group);
        VALIDITY_COLOR= ContextCompat.getColor(activity,R.color.colorGreen);
    }
    private void InitializationUI()
    {
        //ImageView
        ProfileIv=findViewById(R.id.image_civ);
        ProfileIv.setEnabled(false);
        DateOfBirthBtnIv=findViewById(R.id.date_of_birth_btn_iv);
        DateOfBirthBtnIv.setOnClickListener(this);
        AddressBtnIv=findViewById(R.id.address_btn_iv);
        AddressBtnIv.setOnClickListener(this);

        //Button
        ProfileImageUploadBtn=findViewById(R.id.upload_btn);
        ProfileImageUploadBtn.setOnClickListener(this);
        ProfileImageCancelBtn=findViewById(R.id.cancel_btn);
        ProfileImageCancelBtn.setOnClickListener(this);
        SaveBtn=findViewById(R.id.save_btn);
        SaveBtn.setOnClickListener(this);

        //TextView
        DateOfBirthTv=findViewById(R.id.date_of_birth_tv);

        //EditText
        FullNameEt=findViewById(R.id.full_name_et);
        FullNameEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        FullNameEt.addTextChangedListener(new MyTextWatcher(activity,VARConst.NAME_VALIDITY,R.id.full_name_et));
        FatherNameEt=findViewById(R.id.father_name_et);
        FatherNameEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        FatherNameEt.addTextChangedListener(new MyTextWatcher(activity,VARConst.NAME_VALIDITY,R.id.father_name_et));
        MotherNameEt=findViewById(R.id.mother_name_et);
        MotherNameEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        MotherNameEt.addTextChangedListener(new MyTextWatcher(activity,VARConst.NAME_VALIDITY,R.id.mother_name_et));
        PhoneNumberEt=findViewById(R.id.phone_et);
        PhoneNumberEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);
        PhoneNumberEt.addTextChangedListener(new MyTextWatcher(activity,VARConst.PHONE_VALIDITY,R.id.phone_et));
        AddressEt=findViewById(R.id.address_et);
        AddressEt.setOnEditorActionListener(new MyTextWatcher(activity).actionListener);

        //Spinner
        BloodGroupSpinner=findViewById(R.id.blood_group_spinner);
        BloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                BloodGroupString=BloodGroupRes[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        GenderGroup=findViewById(R.id.radio_group);
        MaleRbtn=findViewById(R.id.male_rbtn);
        FemaleRbtn=findViewById(R.id.female_rbtn);

        GenderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                int RadioButtonID=radioGroup.getCheckedRadioButtonId();
                RadioButton rbtn=findViewById(RadioButtonID);
                GenderString=rbtn.getText().toString();
            }
        });
    }
    private void InitializationClass()
    {
        myToastClass=new MyToastClass(activity);
        myImageGettingClass=new MyImageGettingClass(activity);
        myLocationClass=new MyLocationClass(activity);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.upload_btn:
                GetImageFromCameraOrGallery();
                break;
            case R.id.cancel_btn:
                CancelImageMethod();
                break;
            case R.id.date_of_birth_btn_iv:
                GetDateOfBirthFromCalender();
                break;
            case R.id.address_btn_iv:
                GetAddressFromLocationClass();
                break;
            case R.id.save_btn:
                SaveDataToDatabase();
                break;
        }
    }

    ///Getting Image
    private void GetImageFromCameraOrGallery()
    {
        myImageGettingClass.GetImageFromCameraOrGallery();
    }
    private void CancelImageMethod()
    {
        ProfileIv.setImageResource(R.drawable.user_2);
        ProfileIv.setEnabled(false);
        ProfileImageUrl=VARConst.UNKNOWN;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myImageGettingClass.onActivityResult(VARConst.CIV,R.id.image_civ,requestCode,resultCode,data);
        if (resultCode==RESULT_OK&&data!=null)
        {
            ProfileIv.setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        myLocationClass.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    //Getting Date
    private void GetDateOfBirthFromCalender()
    {
        Calendar calendar=Calendar.getInstance();
        int date=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog=new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date)
            {
                DateOfBirthString=date+"-"+(month+1)+"-"+year;
                DateOfBirthTv.setText(DateOfBirthString);
                DateOfBirthTv.setVisibility(View.VISIBLE);
            }
        },year,month,date);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    //Getting Address
    private void GetAddressFromLocationClass()
    {
        AddressEt.setVisibility(View.VISIBLE);
        myLocationClass.GetCoOrdinateFromMaps();
    }
    private void GetLocationFromGoogleMap(String CurrentLocation)
    {
        AddressString=CurrentLocation;
        AddressEt.setText(AddressString);
    }

    private void SaveDataToDatabase()
    {
        if (!(FullNameEt.getCurrentTextColor()==VALIDITY_COLOR))
        {
            FullNameEt.setError("Type your name correctly");
        }
        else if (!(FatherNameEt.getCurrentTextColor()==VALIDITY_COLOR))
        {
            FatherNameEt.setError("Type your father name correctly");
        }
        else if (!(MotherNameEt.getCurrentTextColor()==VALIDITY_COLOR))
        {
            MotherNameEt.setError("Type your mother name correctly");
        }
        else if (!(PhoneNumberEt.getCurrentTextColor()==VALIDITY_COLOR))
        {
            FatherNameEt.setError("Type your phone number correctly");
        }
        else if (GenderString.matches(VARConst.UNKNOWN))
        {
            myToastClass.LToast("Select your gender");
        }
        else
        {
            FullNameString=FullNameEt.getText().toString();
            FatherNameString=FatherNameEt.getText().toString();
            MotherNameString=MotherNameEt.getText().toString();
            PhoneNumberString=PhoneNumberEt.getText().toString();
            DateOfBirthString=DateOfBirthTv.getText().toString();
            AddressString=AddressEt.getText().toString();
            if (ProfileIv.isEnabled())
            {
                ProfileImageByte=myImageGettingClass.GetCompressImageBytes(VARConst.CIV,R.id.image_civ);
                storageDB.SaveFileIntoStorage(DBConst.ProfileImages,UID+".jpg",ProfileImageByte);
                //CallDBForSavingData is called after uploading finished and getting back with url
            }
            else
            {
                PatientAccountDM patientAccountDM=new PatientAccountDM(UID,ProfileImageUrl,FullNameString,FatherNameString,MotherNameString,PhoneNumberString,GenderString,DateOfBirthString,BloodGroupString, AddressString,BirthNumberString,BirthImageUrl,AnotherDocImageUrl);
                CallDBForSavingData(UID,patientAccountDM);
            }
        }
    }

    //********************************Get Data Return From Database******************************///
    private void GetUIDFromDB(String UID)
    {
        this.UID=UID;
    }
    private void GetExistingDataFromDB(PatientAccountDM patientAccountDM)
    {
        UID= patientAccountDM.getUID();
        ProfileImageUrl= patientAccountDM.getProfileImageUrl();
        FullNameString= patientAccountDM.getName();
        FatherNameString= patientAccountDM.getFatherName();
        MotherNameString= patientAccountDM.getMotherName();
        PhoneNumberString= patientAccountDM.getPhoneNumber();
        GenderString= patientAccountDM.getGender();
        DateOfBirthString= patientAccountDM.getDateOfBirth();
        BloodGroupString= patientAccountDM.getBloodGroup();
        AddressString= patientAccountDM.getAddress();
        BirthNumberString= patientAccountDM.getBirthNumber();
        BirthImageUrl= patientAccountDM.getBirthNumberImageUrl();
        AnotherDocImageUrl= patientAccountDM.getAnotherDocumentImageUrl();

        //Fill All the Data

        if (!ProfileImageUrl.matches(VARConst.UNKNOWN))
        {
            ProfileIv.setEnabled(true);
            Picasso.get().load(ProfileImageUrl).into(ProfileIv);
        }
        FullNameEt.setText(FullNameString);
        FatherNameEt.setText(FatherNameString);
        MotherNameEt.setText(MotherNameString);
        PhoneNumberEt.setText(PhoneNumberString);
        if (!DateOfBirthString.matches(VARConst.UNKNOWN))
        {
            DateOfBirthTv.setVisibility(View.VISIBLE);
            DateOfBirthTv.setText(DateOfBirthString);
        }
        if (!AddressString.matches(VARConst.UNKNOWN))
        {
            AddressEt.setVisibility(View.VISIBLE);
            AddressEt.setText(AddressString);
        }

        if (GenderString.matches(MaleRbtn.getText().toString()))
        {
            MaleRbtn.setChecked(true);
        }
        else if (GenderString.matches(FemaleRbtn.getText().toString()))
        {
            FemaleRbtn.setChecked(true);
        }

        if (!BloodGroupString.matches(VARConst.UNKNOWN))
        {
            ArrayList<String> BloodGroupArrayList=new ArrayList<>();
            BloodGroupArrayList.add(BloodGroupString);
            for(int i=0; i<BloodGroupRes.length; i++)
            {
                if (!BloodGroupRes[i].matches(BloodGroupString))
                {
                    BloodGroupArrayList.add(BloodGroupRes[i]);
                }
            }

            ArrayAdapter<String> adapter=new ArrayAdapter<String >(activity,android.R.layout.simple_list_item_1,BloodGroupArrayList);
            BloodGroupSpinner.setAdapter(adapter);
        }


    }
    private void GetImageUrlFromDB(boolean ImageSavingResult, String url)
    {
        if (ImageSavingResult)
        {
            ProfileImageUrl=url;
            PatientAccountDM patientAccountDM=new PatientAccountDM(UID,ProfileImageUrl,FullNameString,FatherNameString,MotherNameString,PhoneNumberString,GenderString,DateOfBirthString,BloodGroupString,AddressString,BirthNumberString,BirthImageUrl,AnotherDocImageUrl);
            CallDBForSavingData(UID,patientAccountDM);
        }
    }
    private void GetDataSavedResultFromDB(boolean DataSavingResult)
    {
        if (DataSavingResult)
        {
            CallDBForAccountStatus();
        }
    }
    private void GetAccountStatusFromDB(boolean GettingAccountStatusResult, boolean accountValidity)
    {
        if (GettingAccountStatusResult)
        {
            if (accountValidity)
            {
                GotoMainActivity();
            }
            else
            {
                GotoSecureInformationEditActivity();
            }
        }
    }
    private void GotoMainActivity()
    {
        startActivity(new Intent(activity,PatientMainActivity.class));
    }
    private void GotoSecureInformationEditActivity()
    {
        startActivity(new Intent(activity,EditPatientSecureInfoActivity.class));
    }




    ///******************************************************************************************///
    ///*******************************Database Part**********************************************///
    ///******************************************************************************************///

    ///*******************************Database Initialization************************************///
    private void InitializationDB()
    {
        dbHelper=new DBHelper(activity);
        patientAccountDB =new PatientAccountDB(activity);
        accountStatusDB=new AccountStatusDB(activity);
        storageDB=new StorageDB(activity);
    }
    ///*******************************Database Calling Method************************************///
    private void CallDBForUID()
    {
        dbHelper.GetUID();
    }

    private void CallDBForExistingData(String UID)
    {
        patientAccountDB.GetPatientAccountInformation(UID);
    }
    private void CallDBForSavingData(String UID,PatientAccountDM patientAccountDM)
    {
        patientAccountDB.SaveAccountInformation(UID,patientAccountDM);
    }
    private void CallDBForAccountStatus()
    {
        accountStatusDB.GetUIDAccountStatusData();
    }




    ///******************************************************************************************///
    ///************************************Interface Implementation******************************///
    ///******************************************************************************************///


    ///*******************************Database Interface Implementation**************************///
    @Override
    public void GetUID(String UID)
    {
        GetUIDFromDB(UID);
    }

    @Override
    public void GetPatientAccountData(String DataResult, PatientAccountDM patientAccountDM)
    {
        switch (DataResult)
        {
            case DBConst.DATA_EXIST:
                GetExistingDataFromDB(patientAccountDM);
                break;
            case DBConst.SUCCESSFULL:
                GetDataSavedResultFromDB(true);
                break;
            case DBConst.UNSUCCESSFULL:
                GetDataSavedResultFromDB(false);
                break;

        }
    }

    @Override
    public void GetAccountStatus(boolean result, ArrayList<AccountStatusDM> arrayList)
    {
        GetAccountStatusFromDB(result,arrayList.get(0).isAccountValidity());
    }


    @Override
    public void AccountStatusSavingResult(boolean result) {
    }

    @Override
    public void GetResultAndUrl(boolean result, String Url)
    {
        GetImageUrlFromDB(result,Url);
    }

    ///***************************Location Interface Implementation******************************///
    @Override
    public void GetLocation(String FromWhich, Map<Integer, String> map)
    {
        GetLocationFromGoogleMap(map.get(0));
    }

}

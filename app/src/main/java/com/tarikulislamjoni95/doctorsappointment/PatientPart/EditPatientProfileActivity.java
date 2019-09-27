package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DataModel;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.PatientAccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBHelper;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.StorageDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLocationClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetLocationInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditPatientProfileActivity extends AppCompatActivity implements View.OnClickListener, GetLocationInterface, GetDataFromDBInterface
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
    private InitializationUIHelperClass initializationUIHelperClass;
    private DataModel dataModel;

    //Object Variable
    private Intent intent;
    private Activity activity;

    //Resource Variable
    private String[] DataKey;
    private String[] BloodGroupRes;
    private int VALIDITY_COLOR;
    //Primitive Variable
    private String UID;
    private byte[] ProfileImageByte;
    private HashMap<String,String> MyFieldDataHashMap;
    //UI Variable
    private CircleImageView[] circleImageViews;
    private ImageView[] imageViews;
    private Button[] buttons;
    private EditText[] editTexts;
    private TextView[] textViews;
    private Spinner[] spinners;
    private RadioGroup[] radioGroups;
    private RadioButton[] radioButtons;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_profile);
        Initialization();
        InitializationClass();
        InitializationUI();
        InitializationDB();

        SetDataHashMap();
        CallDBForUID();
    }
    private void SetDataHashMap()
    {
        MyFieldDataHashMap=new HashMap<>();
        DataKey=dataModel.GetPatientAccountInformationDataKey();
        for(int i=0; i<DataKey.length; i++)
        {
            MyFieldDataHashMap.put(DataKey[i],DBConst.UNKNOWN);
        }
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
        int [] civImageViewsId={R.id.image_civ_0};
        circleImageViews= initializationUIHelperClass.setCircleImageViews(civImageViewsId);
        circleImageViews[0].setEnabled(false);

        int[] imageViewsId={R.id.image_view_0,R.id.image_view_1};
        imageViews= initializationUIHelperClass.setImageViews(imageViewsId);
        imageViews[0].setOnClickListener(this);
        imageViews[1].setOnClickListener(this);

        int[] textViewsId={R.id.text_view_0,R.id.text_view_1};
        textViews= initializationUIHelperClass.setTextViews(textViewsId);
        textViews[0].setOnClickListener(this);

        int[] editTextId={R.id.edit_text_0,R.id.edit_text_1,R.id.edit_text_2,R.id.edit_text_3,R.id.edit_text_4};
        editTexts= initializationUIHelperClass.setEditTexts(editTextId);
        for(int i=0; i<editTextId.length; i++)
        {
            if (i<3)
            {
                editTexts[i].addTextChangedListener(new MyTextWatcher(activity,VARConst.NAME_VALIDITY,editTexts[i].getId()));
            }
            else if (i==3)
            {
                editTexts[i].addTextChangedListener(new MyTextWatcher(activity,VARConst.PHONE_VALIDITY,editTexts[i].getId()));
            }
        }

        int[] buttonId={R.id.button_0};
        buttons= initializationUIHelperClass.setButtons(buttonId);
        buttons[0].setOnClickListener(this);




        //Spinner
        int[] spinnersId={R.id.spinner_0};
        spinners= initializationUIHelperClass.setSpinner(spinnersId);
        spinners[0].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                MyFieldDataHashMap.put(DBConst.BloodGroup,BloodGroupRes[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        int[] radioGroupsId={R.id.radio_group_0};
        radioGroups= initializationUIHelperClass.setRadioGroup(radioGroupsId);

        int[] radioButtonsId={R.id.radio_button_0,R.id.radio_button_1};
        radioButtons= initializationUIHelperClass.setRadioButton(radioButtonsId);

        radioGroups[0].setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                int RadioButtonID=radioGroup.getCheckedRadioButtonId();
                RadioButton rbtn=findViewById(RadioButtonID);
                MyFieldDataHashMap.put(DBConst.Gender,rbtn.getText().toString());
            }
        });
    }
    private void InitializationClass()
    {
        initializationUIHelperClass =new InitializationUIHelperClass(getWindow().getDecorView());
        myToastClass=new MyToastClass(activity);
        myImageGettingClass=new MyImageGettingClass(activity);
        myLocationClass=new MyLocationClass(activity);
        dataModel=new DataModel();
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.text_view_0:
                myImageGettingClass.GetImageFromCameraOrGallery();
                break;
            case R.id.image_view_0:
                GetDateOfBirthFromCalender();
                break;
            case R.id.image_view_1:
                GetAddressFromLocationClass();
                break;
            case R.id.button_0:
                SaveDataToDatabase();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myImageGettingClass.onActivityResult(VARConst.CIV,R.id.image_civ_0,requestCode,resultCode,data);
        if (resultCode==RESULT_OK&&data!=null)
        {
            circleImageViews[0].setEnabled(true);
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
                MyFieldDataHashMap.put(DBConst.DateOfBirth,date+"-"+(month+1)+"-"+year);
                textViews[1].setText(MyFieldDataHashMap.get(DBConst.DateOfBirth));
                textViews[1].setVisibility(View.VISIBLE);
            }
        },year,month,date);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    //Getting Address
    private void GetAddressFromLocationClass()
    {
        editTexts[4].setVisibility(View.VISIBLE);
        myLocationClass.GetCoOrdinateFromMaps();
    }
    private void GetLocationFromGoogleMap(String CurrentLocation)
    {
        editTexts[4].setText(CurrentLocation);
    }

    private void SaveDataToDatabase()
    {
        if (!(editTexts[0].getCurrentTextColor()==VALIDITY_COLOR))
        {
            editTexts[0].setError("Type your name correctly");
        }
        else if (!(editTexts[1].getCurrentTextColor()==VALIDITY_COLOR))
        {
            editTexts[1].setError("Type your father name correctly");
        }
        else if (!(editTexts[2].getCurrentTextColor()==VALIDITY_COLOR))
        {
            editTexts[2].setError("Type your mother name correctly");
        }
        else if (!(editTexts[3].getCurrentTextColor()==VALIDITY_COLOR))
        {
            editTexts[3].setError("Type your phone number correctly");
        }
        else if (MyFieldDataHashMap.get(DBConst.Gender).matches(VARConst.UNKNOWN))
        {
            myToastClass.LToast("Select your gender");
        }
        else
        {
            for(int i=0; i<4; i++)
            {
                MyFieldDataHashMap.put(DataKey[i+1],editTexts[i].getText().toString());
            }
            MyFieldDataHashMap.put(DataKey[6],textViews[1].getText().toString());
            MyFieldDataHashMap.put(DataKey[8],editTexts[4].getText().toString());

            if (circleImageViews[0].isEnabled())
            {
                ProfileImageByte=myImageGettingClass.GetCompressImageBytes(VARConst.CIV,R.id.image_civ_0);
                ////////////////////////////////////////////
                CallDBForSaveImageIntoStorage(UID+".jpg",ProfileImageByte);
                ////////////////////////////////////////////
            }
            else
            {
                ///////////////////////////////////////////
                CallDBForSavingData(UID,MyFieldDataHashMap);
                //////////////////////////////////////////
            }
        }
    }

    //********************************Get Data Return From Database******************************///
    private void GetAccountUIDFromDB(String GettingResult,String UID)
    {
        if (GettingResult.matches(DBConst.NOT_NULL_USER))
        {
            this.UID=UID;
            //////////////////////////
            CallDBForExistingData(UID);
            //////////////////////////
        }
        else
        {
            finish();
        }
    }
    private void GetExistingDataFromDB(HashMap<String,Object> DataHashMap)
    {

        if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.DATA_EXIST))
        {
            for(int i=0; i<DataKey.length; i++)
            {
                MyFieldDataHashMap.put(DataKey[i],(String) DataHashMap.get(DataKey[i]));
                if (i<4)
                {
                    editTexts[i].setText(DataHashMap.get(DataKey[i+1]).toString());
                }
            }

            if (DataHashMap.get(DataKey[5]).toString().matches("Male"))
            {
                radioButtons[0].setChecked(true);
            }
            else
            {
                radioButtons[1].setChecked(true);
            }

            if (!DataHashMap.get(DataKey[6]).toString().matches(VARConst.UNKNOWN))
            {
                textViews[1].setVisibility(View.VISIBLE);
                textViews[1].setText(DataHashMap.get(DataKey[6]).toString());
            }

            if (!DataHashMap.get(DataKey[7]).toString().matches(VARConst.UNKNOWN))
            {
                ArrayList<String> BloodGroupArrayList=new ArrayList<>();
                BloodGroupArrayList.add(DataHashMap.get(DataKey[7]).toString());
                for(int k=0; k<BloodGroupRes.length; k++)
                {
                    if (!BloodGroupRes[k].matches(DataHashMap.get(DataKey[6]).toString()))
                    {
                        BloodGroupArrayList.add(BloodGroupRes[k]);
                    }
                }

                ArrayAdapter<String> adapter=new ArrayAdapter<String >(activity,android.R.layout.simple_list_item_1,BloodGroupArrayList);
                spinners[0].setAdapter(adapter);
            }

            if (!MyFieldDataHashMap.get(DataKey[8]).matches(DBConst.UNKNOWN))
            {
                editTexts[4].setVisibility(View.VISIBLE);
                editTexts[4].setText(MyFieldDataHashMap.get(DataKey[8]));
            }

            if (!MyFieldDataHashMap.get(DataKey[0]).matches(VARConst.UNKNOWN))
            {
                circleImageViews[0].setEnabled(false);
                Picasso.get().load(MyFieldDataHashMap.get(DataKey[0])).into(circleImageViews[0]);
            }
        }
    }
    private void GetImageUrlFromDB(String ImageSavingResult, String url)
    {
        if (ImageSavingResult.matches(DBConst.SUCCESSFUL))
        {
            MyFieldDataHashMap.put(DBConst.ProfileImageUrl,url);

            ////////////////////////////////////////////
            CallDBForSavingData(UID,MyFieldDataHashMap);
            ////////////////////////////////////////////
        }
    }
    private void GetDataSavedResultFromDB(String DataSavingResult)
    {
        if (DataSavingResult.matches(DBConst.SUCCESSFUL))
        {
            //////////////////////////
            CallDBForAccountStatus(UID);
            //////////////////////////
        }
        else
        {
            myToastClass.LToast("Data not saved\nPlease try again... ");
        }
    }
    private void GetAccountStatusFromDB(String GettingResult,String AccountType,boolean AccountCompletion, boolean AccountValidity)
    {
        if (GettingResult.matches(DBConst.DATA_EXIST))
        {
            if (AccountValidity)
            {
                GotoMainActivity();
            }
            else
            {
                GotoSecureInformationEditActivity();
            }
        }
        else if (GettingResult.matches(DBConst.DATA_NOT_EXIST))
        {
            CallDBForAccountDelete();
        }
        else
        {
            CallDBForSignOut();
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
    private void CallDBForSignOut()
    {
        dbHelper.SignOut();
    }
    private void CallDBForAccountDelete()
    {
        dbHelper.DeleteUID();
    }

    private void CallDBForExistingData(String UID)
    {
        patientAccountDB.GetPatientAccountInformation(UID);
    }
    private void CallDBForSaveImageIntoStorage( String ImageTitle, byte[] ImageBytes)
    {

        storageDB.SaveFileIntoStorage(DBConst.ProfileImages,UID+".jpg",ProfileImageByte);
    }
    private void CallDBForSavingData(String UID,HashMap<String,String> DataHashMap)
    {
        patientAccountDB.SaveAccountInformation(UID,DataHashMap);
    }
    private void CallDBForAccountStatus(String UID)
    {
        accountStatusDB.GetAccountStatusData(UID);
    }




    ///******************************************************************************************///
    ///************************************Interface Implementation******************************///
    ///******************************************************************************************///


    ///*******************************Database Interface Implementation**************************///


    ///***************************Location Interface Implementation******************************///
    @Override
    public void GetLocation(String FromWhich, Map<Integer, String> map)
    {
        GetLocationFromGoogleMap(map.get(0));
    }

    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        Log.d("myError","WhichDB :: "+WhichDB);
        switch (WhichDB)
        {
            case DBConst.GetAccountUID:
                GetAccountUIDFromDB((String)DataHashMap.get(DBConst.RESULT),(String)DataHashMap.get(DBConst.UID));
                break;
            case DBConst.GetSavingFileUrlData:
                GetImageUrlFromDB((String) DataHashMap.get(DBConst.RESULT),(String) DataHashMap.get(DBConst.URL));
                break;
            case DBConst.GetPatientAccountInformation:
                GetExistingDataFromDB(DataHashMap);
                break;
            case DBConst.SavePatientAccountInformation:
                GetDataSavedResultFromDB((String) DataHashMap.get(DBConst.RESULT));
                break;
            case DBConst.GetAccountStatusDB:
                GetAccountStatusFromDB((String) DataHashMap.get(DBConst.RESULT),(String) DataHashMap.get(DBConst.AccountType),(boolean)DataHashMap.get(DBConst.AccountCompletion),(boolean)DataHashMap.get(DBConst.AccountValidity));
                break;
        }
    }

    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList) { }
}

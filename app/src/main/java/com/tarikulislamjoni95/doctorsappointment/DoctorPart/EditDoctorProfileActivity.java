package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBHelper;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DataModel;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorAccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.StorageDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditDoctorProfileActivity extends AppCompatActivity implements View.OnClickListener, GetDataFromDBInterface
{
    private DBHelper dbHelper;
    private StorageDB storageDB;
    private DoctorAccountDB doctorAccountDB;
    private AccountStatusDB accountStatusDB;

    private InitializationUIHelperClass initializationUIHelperClass;
    private MyImageGettingClass myImageGettingClass;
    private MyToastClass myToastClass;
    private DataModel dataModel;


    private Activity activity;

    private HashMap<String,Object> DataHashMap;
    private HashMap<String,String> AccountDataHashMap;
    private String[] DataKey;
    private String UID;
    private int VALIDATION_GREEN;
    private String[] AvailableArea;

    private EditText[] editTexts;
    private TextView[] textViews;
    private Button[] buttons;
    private ImageView[] imageViews;
    private CircleImageView[] circleImageViews;
    private Spinner[] spinners;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor_profile);

        Initialization();
        InitializationClass();
        InitializationUI();
        InitializationDB();

        SetAccountDataField();
        CallDBForAccountUID();
    }

    private void Initialization()
    {
        activity=EditDoctorProfileActivity.this;
        VALIDATION_GREEN=ContextCompat.getColor(activity,R.color.colorGreen);
        AvailableArea=getResources().getStringArray(R.array.division_list);
        DataHashMap=new HashMap<>();
        AccountDataHashMap=new HashMap<>();
    }
    private void InitializationUI()
    {
        //TextView Initialization
        int[] textviewsId={R.id.tex_view_0};
        textViews= initializationUIHelperClass.setTextViews(textviewsId);
        textViews[0].setOnClickListener(this);

        //Edit Text Initialization
        int edit_text_id[]={R.id.edit_text_0,R.id.edit_text_1,R.id.edit_text_2,R.id.edit_text_3,R.id.edit_text_4,R.id.edit_text_5};
        editTexts= initializationUIHelperClass.setEditTexts(edit_text_id);
        editTexts[0].addTextChangedListener(new MyTextWatcher(activity,VARConst.NAME_VALIDITY,editTexts[0].getId()));
        editTexts[5].addTextChangedListener(new MyTextWatcher(activity,VARConst.PHONE_VALIDITY,editTexts[5].getId()));

        //Button Initialization
        int[] buttonsId={R.id.button_0};
        buttons= initializationUIHelperClass.setButtons(buttonsId);
        buttons[0].setOnClickListener(this);

        //Image View Initialization
        int[] imageViewId={R.id.image_view_0};
        imageViews= initializationUIHelperClass.setImageViews(imageViewId);
        imageViews[0].setEnabled(false);

        int[] spinnersId={R.id.spinner_0};
        spinners= initializationUIHelperClass.setSpinner(spinnersId);
        //AccountDataHashMap.put(DBConst.AvailableArea,AvailableArea[0]);
        spinners[0].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                AccountDataHashMap.put(DBConst.AvailableArea,((TextView)findViewById(view.getId())).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void InitializationClass()
    {
        initializationUIHelperClass =new InitializationUIHelperClass(getWindow().getDecorView());
        myImageGettingClass=new MyImageGettingClass(activity);
        myToastClass=new MyToastClass(activity);
        dataModel=new DataModel();
    }
    private void SetAccountDataField()
    {
        DataKey=dataModel.GetDoctorAccountInformationDataKey();
        for(int i=0; i<DataKey.length; i++)
        {
            AccountDataHashMap.put(DataKey[i],VARConst.UNKNOWN);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tex_view_0:
                myImageGettingClass.GetImageFromCameraOrGallery();
                break;
            case R.id.button_0:
                GetAllDataFromField();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE || requestCode==VARConst.REQUEST_CAMERA_CODE || requestCode==VARConst.REQUEST_GALLERY_CODE) &&resultCode==RESULT_OK && data!=null)
        {
            myImageGettingClass.onActivityResult(VARConst.CIV,imageViews[0].getId(),requestCode,resultCode,data);
            imageViews[0].setEnabled(true);
            editTexts[0].requestFocus();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        myImageGettingClass.onRequestPermissionsResult(activity,requestCode,permissions,grantResults);
    }

    private void GetAllDataFromField()
    {
        if (!(editTexts[0].getCurrentTextColor()==VALIDATION_GREEN))
        {
            editTexts[0].setError("Valid name required");
        }
        else  if (!(editTexts[5].getCurrentTextColor()==VALIDATION_GREEN))
        {
            editTexts[5].setError("Phone number format will be 01XXXXXXXXX");
        }
        else
        {
            for(int i=0; i<editTexts.length; i++)
            {
                AccountDataHashMap.put(DataKey[i+1],editTexts[i].getText().toString());
            }

            if (imageViews[0].isEnabled())
            {
                String ImageTitle=UID+".jpg";
                CallDBForSaveImage(ImageTitle,myImageGettingClass.GetCompressImageBytes(VARConst.CIV,imageViews[0].getId()));
            }
            else
            {
                CallDBForSaveDoctorAccountInformation();
            }
        }
    }

    ///**********************************Database Return Start***********************************///
    private void GetAccountUIDFromDB()
    {
        if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.NOT_NULL_USER))
        {
            this.UID=DataHashMap.get(DBConst.UID).toString();
            CallDBForGetDoctorAccountInformation();
        }
        else
        {
            finish();
        }
    }
    private void GetDoctorAccountInfromationFromDB()
    {
        if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.DATA_EXIST))
        {
            for(int i=0; i<DataKey.length; i++)
            {
                AccountDataHashMap.put(DataKey[i],DataHashMap.get(DataKey[i]).toString());
                if (i==0 && !AccountDataHashMap.get(DataKey[i]).matches(DBConst.UNKNOWN))
                {
                    imageViews[0].setEnabled(false);
                    Picasso.get().load(AccountDataHashMap.get(DataKey[i])).into(imageViews[0]);
                }

                if (i<6)
                {
                    editTexts[i].setText(DataHashMap.get(DataKey[i+1]).toString());
                }
                if (i==7)
                {
                    ArrayList<String> arrayList=new ArrayList<>();
                    arrayList.add(AccountDataHashMap.get(DBConst.AvailableArea));
                    for(int k=0; k<AvailableArea.length; k++)
                    {
                        if (!AvailableArea[k].matches(AccountDataHashMap.get(DBConst.AvailableArea)))
                        {
                            arrayList.add(AvailableArea[k]);
                        }
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(activity,android.R.layout.simple_list_item_1,arrayList);
                    spinners[0].setAdapter(arrayAdapter);
                    AccountDataHashMap.put(DBConst.AvailableArea,arrayList.get(0));
                }
            }
        }
    }
    private void GetImageUrlFromDB()
    {
        if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            AccountDataHashMap.put(DBConst.ProfileImageUrl,DataHashMap.get(DBConst.URL).toString());
            CallDBForSaveDoctorAccountInformation();
        }
    }
    private void GetDoctorAccountInformationSavingResultFromDB()
    {
        if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            CallDBForAccountStatus();
        }
        else
        {
            myToastClass.LToast("Please try again...");
        }
    }
    private void GetAccountStatusFromDB()
    {
        if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.DATA_EXIST))
        {
            if ((boolean)DataHashMap.get(DBConst.AccountValidity))
            {
                startActivity(new Intent(activity,DoctorMainActivity.class));
            }
            else
            {
                startActivity(new Intent(activity, EditDoctorSecureInfoActivity.class));
            }
        }
        else if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.DATA_NOT_EXIST))
        {
            CallDBForSignOut();
            finish();
        }
        else
        {
            myToastClass.LToast("Please try again...");
        }
    }

    ///**********************************Database Return End***********************************///

    //********************************************************************************************//
    //**************************************Database Part*****************************************//
    //********************************************************************************************//

    private void InitializationDB()
    {
        dbHelper=new DBHelper(activity);
        storageDB=new StorageDB(activity);
        doctorAccountDB=new DoctorAccountDB(activity);
        accountStatusDB=new AccountStatusDB(activity);
    }

    ///**************************************Calling Database************************************///
    private void CallDBForAccountUID()
    {
        dbHelper.GetUID();
    }
    private void CallDBForGetDoctorAccountInformation()
    {
        doctorAccountDB.GetDoctorAccountInformation(UID);
    }
    private void CallDBForSaveDoctorAccountInformation()
    {
        doctorAccountDB.SaveDoctorAccountInformation(UID,AccountDataHashMap);
    }
    private void CallDBForSaveImage(String ImageTitle,byte[] getCompressImageBytes)
    {

        storageDB.SaveFileIntoStorage(DBConst.ProfileImages,ImageTitle,getCompressImageBytes);
    }

    private void CallDBForAccountStatus()
    {
        accountStatusDB.GetAccountStatusData(UID);
    }
    private void CallDBForSignOut()
    {
        dbHelper.SignOut();
    }


    //*********************************Interface Implementation**********************************///
    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        Log.d("muError","EditorDoctorActivitu   :::: "+DataHashMap.toString());
        this.DataHashMap=DataHashMap;
        switch (WhichDB)
        {
            case DBConst.GetAccountUID:
                GetAccountUIDFromDB();
                break;
            case DBConst.GetDoctorAccountInformation:
                GetDoctorAccountInfromationFromDB();
                break;
            case DBConst.SaveDoctorAccountInformation:
                GetDoctorAccountInformationSavingResultFromDB();
                break;
            case DBConst.GetSavingFileUrlData:
                GetImageUrlFromDB();
                break;
            case DBConst.GetAccountStatusDB:
                GetAccountStatusFromDB();
                break;
        }
    }

    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList)
    {

    }
}

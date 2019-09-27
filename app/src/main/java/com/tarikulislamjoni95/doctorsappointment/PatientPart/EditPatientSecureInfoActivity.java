package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountMultiplicityDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBHelper;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DataModel;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.PatientAccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.StorageDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetProgressInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class EditPatientSecureInfoActivity extends AppCompatActivity implements View.OnClickListener,
        GetDataFromDBInterface
{
    private DBHelper dbHelper;
    private AccountStatusDB accountStatusDB;
    private PatientAccountDB patientAccountDB;
    private AccountMultiplicityDB accountMultiplicityDB;
    private StorageDB storageDB;

    private String UID=VARConst.UNKNOWN;
    private HashMap<String,String> PatientAccountDataHashMap;
    private HashMap<String,Object> AccountMultiplicityData;
    private HashMap<String,Object> DataHashMap;

    private InitializationUIHelperClass initializationUIHelperClass;
    private MyImageGettingClass myImageGettingClass;
    private MyToastClass myToastClass;
    private MyLoadingDailog myLoadingDailog;
    private DataModel dataModel;

    private int WhichImageViewEnable;
    private ArrayList<String> ImageUrlArrayList=new ArrayList<>();

    private Activity activity;
    private Intent intent;
    private int VALIDATION_COLOR;

    private LinearLayout[] linearLayouts;
    private ImageView[] imageViews;
    private EditText[] editTexts;
    private Button[] buttons;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_secure_info);
        Initialization();
        InitializationClass();
        InitializationUI();
        InitializationDB();

        CallDBForGetUID();
    }

    private void Initialization()
    {
        this.activity=EditPatientSecureInfoActivity.this;
        VALIDATION_COLOR=ContextCompat.getColor(activity,R.color.colorGreen);
        DataHashMap=new HashMap<>();
        PatientAccountDataHashMap=new HashMap<>();
        AccountMultiplicityData=new HashMap<>();
    }

    private void InitializationUI()
    {
        int[] editTextId={R.id.edit_text_0};
        int[] buttonId={R.id.button_0,R.id.button_1,R.id.button_2};
        int[] imageViewId={R.id.image_view_0,R.id.image_view_1};
        int[] linearLayoutId={R.id.linear_layout_0};
        editTexts= initializationUIHelperClass.setEditTexts(editTextId);
        editTexts[0].addTextChangedListener(new MyTextWatcher(activity,VARConst.VERIFICATION_CODE_VALIDITY,editTexts[0].getId()));

        buttons= initializationUIHelperClass.setButtons(buttonId);
        for(int i=0; i<buttons.length; i++)
        {
            buttons[i].setOnClickListener(this);
        }

        imageViews= initializationUIHelperClass.setImageViews(imageViewId);
        for(int i=0; i<imageViews.length; i++)
        {
            imageViews[i].setEnabled(false);
        }

        linearLayouts= initializationUIHelperClass.setLinearLayout(linearLayoutId);
        linearLayouts[0].setVisibility(View.GONE);
    }

    private void InitializationClass()
    {
        initializationUIHelperClass =new InitializationUIHelperClass(getWindow().getDecorView());
        myImageGettingClass=new MyImageGettingClass(activity);
        myToastClass=new MyToastClass(activity);
        myLoadingDailog=new MyLoadingDailog(activity,R.drawable.spinner);
        dataModel=new DataModel();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_0:
                WhichImageViewEnable=0;
                myImageGettingClass.GetImageFromCameraOrGallery();
                break;
            case R.id.button_1:
                WhichImageViewEnable=1;
                myImageGettingClass.GetImageFromCameraOrGallery();

                break;
            case R.id.button_2:
                ConfirmButtonMethod();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && data!=null)
        {
            imageViews[WhichImageViewEnable].setEnabled(true);
            myImageGettingClass.onActivityResult(VARConst.IV,imageViews[WhichImageViewEnable].getId(),requestCode,resultCode,data);
        }
    }

    private void ConfirmButtonMethod()
    {
        if (!(editTexts[0].getCurrentTextColor()==VALIDATION_COLOR))
        {
            editTexts[0].setError("Birth number invalid");
        }
        if (editTexts[0].getCurrentTextColor()==VALIDATION_COLOR)
        {
            //Check Birth Number Already registered or not
            CallDBForCheckBirthNumberMultiplicity();
        }
    }
    private void UploadBirthCertificate()
    {
        if (imageViews[0].isEnabled())
        {
            String FileName=DBConst.BirthCertificateImageUrl+"_"+UID+".jpg";
            CallDBForSaveImageAndGetUrl(DBConst.SecureData,FileName,myImageGettingClass.GetFullImageBytes(VARConst.IV,R.id.image_view_0));
        }
        else
        {
            myToastClass.LToast("Upload Required Document Images");
        }
    }
    private void UploadAnotherDocument()
    {
        String FileName=DBConst.AnotherDocumentImageUrl+"_"+UID+".jpg";
        CallDBForSaveImageAndGetUrl(DBConst.SecureData,FileName,myImageGettingClass.GetFullImageBytes(VARConst.IV,R.id.image_view_1));
    }

    public void ShowLoadingDialog()
    {
        if (myLoadingDailog!=null)
        {
            if (!myLoadingDailog.isShowing())
            {
                myLoadingDailog.show();
            }
        }
    }

    private void CancelDialog()
    {
        if (myLoadingDailog!=null)
        {
            if (myLoadingDailog.isShowing())
            {
                myLoadingDailog.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CancelDialog();
    }

    //********************************Database Return********************************************///
    //Get UID From Database
    private void GetAccountUIDFromDB()
    {
        if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.NOT_NULL_USER))
        {
            if (!DataHashMap.get(DBConst.UID).toString().matches(DBConst.UNKNOWN))
            {
                this.UID=DataHashMap.get(DBConst.UID).toString();
                CallDBForGetPatientAccountDB();
            }
        }
        else
        {
            finish();
        }
    }

    //Get Patient Account Information From DB
    private void GetPatientAccountDataFromDB()
    {
        String[] DataKey=dataModel.GetPatientAccountInformationDataKey();
        if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.DATA_EXIST))
        {
            for(int i=0; i<DataKey.length; i++)
            {
                PatientAccountDataHashMap.put(DataKey[i],(String)DataHashMap.get(DataKey[i]));
            }
        }
    }

    //Get Account Multiplicity Result From DB
    private void GetAccountMultiplicityData()
    {
        String GettingDataResult=DataHashMap.get(DBConst.RESULT).toString();
        DataHashMap.remove(DBConst.RESULT);

        AccountMultiplicityData=new HashMap<>();
        Set KeySet=DataHashMap.keySet();
        Iterator iterator1=KeySet.iterator();
        int counter=0;
        int check=0;
        while (iterator1.hasNext())
        {
            String key=(String) iterator1.next();
            if (key.matches(DBConst.MultipleCheck))
            {
                AccountMultiplicityData.put(key,DataHashMap.get(key));
            }
            else
            {
                AccountMultiplicityData.put(key,counter);
                if (key.matches(UID))
                {
                    check=1;
                }
                counter++;
            }
        }
        if (check==0)
        {
            AccountMultiplicityData.put(DBConst.MultipleCheck,false);
            AccountMultiplicityData.put(UID,counter);
        }

        switch (GettingDataResult)
        {
            case DBConst.DATA_EXIST:
                linearLayouts[0].setVisibility(View.VISIBLE);
                if (imageViews[0].isEnabled() && imageViews[1].isEnabled())
                {
                    UploadBirthCertificate();
                }
                else
                {
                    myToastClass.LToast("Upload Required Document Images");
                }
                break;
            case DBConst.DATA_NOT_EXIST:
                UploadBirthCertificate();
                break;
        }
    }

    //Get Image Uploading Result and URL From DB
    private void GetImageUrlFromDB()
    {
        if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            ImageUrlArrayList.add(DataHashMap.get(DBConst.URL).toString());
            if (imageViews[0].isEnabled() && !(imageViews[1].isEnabled()) && ImageUrlArrayList.size()==1)
            {
                PatientAccountDataHashMap.put(DBConst.BirthCertificateNumber,editTexts[0].getText().toString());
                PatientAccountDataHashMap.put(DBConst.BirthCertificateImageUrl,ImageUrlArrayList.get(0));
                PatientAccountDataHashMap.put(DBConst.AnotherDocumentImageUrl,VARConst.UNKNOWN);
                CallDBForSavePatientAccountData();
            }
            if (imageViews[0].isEnabled() && imageViews[1].isEnabled() && ImageUrlArrayList.size()==2)
            {
                PatientAccountDataHashMap.put(DBConst.BirthCertificateNumber,editTexts[0].getText().toString());
                PatientAccountDataHashMap.put(DBConst.BirthCertificateImageUrl,ImageUrlArrayList.get(0));
                PatientAccountDataHashMap.put(DBConst.AnotherDocumentImageUrl,ImageUrlArrayList.get(1));
                CallDBForSavePatientAccountData();
            }

            if (imageViews[0].isEnabled() && imageViews[1].isEnabled() && ImageUrlArrayList.size()==1)
            {
                UploadAnotherDocument();
            }
        }
    }

    private void GetPatientAccountInformationSaveStatusFromDB()
    {
        if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            CallDBForSaveAccountMultiplicity();
        }
    }

    private void GetAccountMultiplicitySavingStatus()
    {
        if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            CallDBForUpdateAccountStatus();
        }
    }

    private void GetAccountStatusSavingStatusFromDB()
    {
        if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            CancelDialog();
            intent=new Intent(activity,PatientMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    ///******************************************************************************************///
    ///***********************************Database Part******************************************///
    ///******************************************************************************************///

    ///***********************************Database Initialization *******************************///
    private void InitializationDB()
    {
        dbHelper=new DBHelper(activity);
        accountStatusDB=new AccountStatusDB(activity);
        patientAccountDB=new PatientAccountDB(activity);
        accountMultiplicityDB=new AccountMultiplicityDB(activity);
        storageDB=new StorageDB(activity);
    }

    //***************************************Database Calling Method ****************************///
    private void CallDBForGetUID()
    {
        ShowLoadingDialog();
        dbHelper.GetUID();
    }

    private void CallDBForGetPatientAccountDB()
    {
        ShowLoadingDialog();
        patientAccountDB.GetPatientAccountInformation(UID);
    }

    private void CallDBForSaveImageAndGetUrl(String secureData, String fileName, byte[] getFullImageBytes)
    {
        storageDB.SaveFileIntoStorage(secureData,fileName,getFullImageBytes);
    }

    private void CallDBForSavePatientAccountData()
    {
        ShowLoadingDialog();
        patientAccountDB.SaveAccountInformation(UID,PatientAccountDataHashMap);
    }

    private void CallDBForCheckBirthNumberMultiplicity()
    {
        ShowLoadingDialog();
        accountMultiplicityDB.GetAccountMultiplicity(DBConst.Patient,editTexts[0].getText().toString());
    }
    private void CallDBForSaveAccountMultiplicity()
    {
        ShowLoadingDialog();
        accountMultiplicityDB.SaveAccountMultiplicity(DBConst.Patient,editTexts[0].getText().toString(),AccountMultiplicityData);
    }

    private void CallDBForUpdateAccountStatus()
    {
        ShowLoadingDialog();
        accountStatusDB.SaveIntoAccountStatusDB(UID,DBConst.Patient,true,true);
    }

    ///******************************************************************************************///
    ///**********************************Interface Implementation********************************///
    ///******************************************************************************************///

    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        CancelDialog();
        this.DataHashMap.clear();
        this.DataHashMap=DataHashMap;
        switch (WhichDB)
        {
            case DBConst.GetAccountUID:
                GetAccountUIDFromDB();
                break;
            case DBConst.GetPatientAccountInformation:
                GetPatientAccountDataFromDB();
                break;
            case DBConst.GetSavingFileUrlData:
                GetImageUrlFromDB();
                break;
            case DBConst.SavePatientAccountInformation:
                GetPatientAccountInformationSaveStatusFromDB();
                break;
            case DBConst.GetAccountMultiplicityDB:
                GetAccountMultiplicityData();
                break;
            case DBConst.SaveAccountMultiplicityDB:
                GetAccountMultiplicitySavingStatus();
                break;
            case DBConst.SaveAccountStatusDB:
                GetAccountStatusSavingStatusFromDB();
                break;

        }
    }

    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList) {

    }
}

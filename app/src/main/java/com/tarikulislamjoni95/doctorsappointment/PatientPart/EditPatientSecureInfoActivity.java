package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
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

import com.google.errorprone.annotations.Var;
import com.google.firebase.auth.FirebaseAuth;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountMultiplicityDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDM;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBHelper;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.PatientAccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.PatientAccountDM;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.StorageDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountMultiplicityInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountStatusDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.DBHelperInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.PatientAccountDBInterface;
import com.tarikulislamjoni95.doctorsappointment.Interface.StorageDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class EditPatientSecureInfoActivity extends AppCompatActivity implements View.OnClickListener,
        DBHelperInterface,AccountStatusDBInterface, PatientAccountDBInterface, StorageDBInterface, AccountMultiplicityInterface
{
    private DBHelper dbHelper;
    private AccountStatusDB accountStatusDB;
    private PatientAccountDB patientAccountDB;
    private AccountMultiplicityDB accountMultiplicityDB;
    private StorageDB storageDB;

    private String UID=VARConst.UNKNOWN;
    private PatientAccountDM patientAccountDM;
    private HashMap<String,Object> AccountMultiplicityData;

    private MyImageGettingClass myImageGettingClass;
    private MyToastClass myToastClass;

    private int WhichImageViewEnable;
    private String BirthNumberString=VARConst.UNKNOWN;
    private String BirthDocImageUrl=VARConst.UNKNOWN;
    private String AnotherDocImageUrl=VARConst.UNKNOWN;
    private ArrayList<String> ImageUrlArrayList=new ArrayList<>();

    private Activity activity;
    private Intent intent;
    private int VALIDATION_COLOR;

    private LinearLayout ReportSection;
    private ImageView[] imageViews=new ImageView[2];
    private EditText editText;
    private Button[] buttons=new Button[3];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_secure_info);
        Initialization();
        InitializationUI();
        InitializationClass();
        InitializationDB();

        CallDBForGetUID();
    }

    private void Initialization()
    {
        this.activity=EditPatientSecureInfoActivity.this;
        VALIDATION_COLOR=ContextCompat.getColor(activity,R.color.colorGreen);
    }

    private void InitializationUI()
    {
        editText=findViewById(R.id.edit_text_1);
        editText.addTextChangedListener(new MyTextWatcher(activity,VARConst.VERIFICATION_CODE_VALIDITY,R.id.edit_text_1));

        buttons[0]=findViewById(R.id.button_0);
        buttons[1]=findViewById(R.id.button_1);
        buttons[2]=findViewById(R.id.button_2);
        for(int i=0; i<buttons.length; i++)
        {
            buttons[i].setOnClickListener(this);
        }

        imageViews[0]=findViewById(R.id.image_view_0);
        imageViews[1]=findViewById(R.id.image_view_1);
        for(int i=0; i<imageViews.length; i++)
        {
            imageViews[i].setEnabled(false);
        }

        ReportSection=findViewById(R.id.report_section);
        ReportSection.setVisibility(View.GONE);
    }

    private void InitializationClass()
    {
        myImageGettingClass=new MyImageGettingClass(activity);
        myToastClass=new MyToastClass(activity);
    }


    private void GetUIDFromDB(String uid)
    {
        this.UID=uid;
        if (!UID.matches(VARConst.UNKNOWN))
        {
            CallDBForGetPatientAccountDB(UID);
        }
    }

    private void GetPatientAccountDataFromDB(PatientAccountDM patientAccountDM)
    {
        this.patientAccountDM=patientAccountDM;
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_0:
                WhichImageViewEnable=0;
                myImageGettingClass.GetImageFromCameraOrGallery();
                Log.d("3 myError","WhichImageViewEnable : "+WhichImageViewEnable+"Image View Id : "+imageViews[WhichImageViewEnable].getId());
                break;
            case R.id.button_1:
                WhichImageViewEnable=1;
                myImageGettingClass.GetImageFromCameraOrGallery();
                Log.d("4 myError","WhichImageViewEnable : "+WhichImageViewEnable+"Image View Id : "+imageViews[WhichImageViewEnable].toString());

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
        if (!(editText.getCurrentTextColor()==VALIDATION_COLOR))
        {
            editText.setError("Birth number invalid");
        }
        if (editText.getCurrentTextColor()==VALIDATION_COLOR)
        {
            BirthNumberString=editText.getText().toString();

            //Check Birth Number Already registered or not
            CallDBForCheckBirthNumberMultiplicity(BirthNumberString);
        }
    }


    private void GetAccountMultiplicityResult(String GettingDataResult,HashMap<String,Object> AccountMultiplicityData)
    {
        this.AccountMultiplicityData=AccountMultiplicityData;
        switch (GettingDataResult)
        {
            case DBConst.DATA_EXIST:
                if (AccountMultiplicityData.size()>2)
                {
                    UploadBothDocument();
                }
                else if (AccountMultiplicityData.size()==2)
                {
                    Set keyset=AccountMultiplicityData.keySet();
                    String[] keys=new String[2];
                    int count=0;
                    Iterator iterator=keyset.iterator();
                    while (iterator.hasNext())
                    {
                        keys[count]=(String) iterator.next();
                        count++;
                    }
                    if (keys[0].matches(UID) || keys[1].matches(UID))
                    {
                        UploadOnlyBirthCertificate();
                    }
                    else
                    {
                        UploadBothDocument();
                    }
                }
                break;
            case DBConst.DATA_NOT_EXIST:
                UploadOnlyBirthCertificate();
                break;
        }
    }
    private void UploadOnlyBirthCertificate()
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
    private void UploadBothDocument()
    {
        ReportSection.setVisibility(View.VISIBLE);
        if (imageViews[0].isEnabled() && imageViews[1].isEnabled())
        {
            String FileName=DBConst.BirthCertificateImageUrl+"_"+UID+".jpg";
            CallDBForSaveImageAndGetUrl(DBConst.SecureData,FileName,myImageGettingClass.GetFullImageBytes(VARConst.IV,R.id.image_view_0));
            FileName=DBConst.AnotherDocumentImageUrl+"_"+UID+".jpg";
            CallDBForSaveImageAndGetUrl(DBConst.SecureData,FileName,myImageGettingClass.GetFullImageBytes(VARConst.IV,R.id.image_view_1));
        }
        else
        {
            myToastClass.LToast("Upload Required Document Images");
        }
    }
    private void GetImageUrlFromDB(String url)
    {
        ImageUrlArrayList.add(url);
        if (ImageUrlArrayList.size()==1)
        {
            BirthDocImageUrl=ImageUrlArrayList.get(0);
            AnotherDocImageUrl=VARConst.UNKNOWN;
            CallDBForSavePatientAccountData(UID,new PatientAccountDM(patientAccountDM.getUID(),patientAccountDM.getProfileImageUrl(),
                    patientAccountDM.getName(),patientAccountDM.getFatherName(),patientAccountDM.getMotherName(),patientAccountDM.getPhoneNumber(),
                    patientAccountDM.getGender(),patientAccountDM.getDateOfBirth(),patientAccountDM.getBloodGroup(),patientAccountDM.getAddress(),
                    BirthNumberString,BirthDocImageUrl,AnotherDocImageUrl));
        }
        else  if (ImageUrlArrayList.size()==2)
        {
            BirthDocImageUrl=ImageUrlArrayList.get(0);
            AnotherDocImageUrl=ImageUrlArrayList.get(1);
            CallDBForSavePatientAccountData(UID,new PatientAccountDM(patientAccountDM.getUID(),patientAccountDM.getProfileImageUrl(),
                    patientAccountDM.getName(),patientAccountDM.getFatherName(),patientAccountDM.getMotherName(),patientAccountDM.getPhoneNumber(),
                    patientAccountDM.getGender(),patientAccountDM.getDateOfBirth(),patientAccountDM.getBloodGroup(),patientAccountDM.getAddress(),
                    BirthNumberString,BirthDocImageUrl,AnotherDocImageUrl));
        }
    }
    private void GetPatientAccountSaveStatusFromDB(String SavingResult)
    {
        if (SavingResult.matches(DBConst.SUCCESSFULL))
        {
            CallDBForSaveAccountMultiplicity();
        }
    }

    private void GetAccountMultiplicitySavingStatus(String SavingResult)
    {
        if (SavingResult.matches(DBConst.SUCCESSFULL))
        {
            CallDBForUpdateAccountStatus();
        }
    }

    private void GetAccountStatusSavingStatus(boolean SavingResult)
    {
        if (SavingResult)
        {
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
        dbHelper.GetUID();
    }

    private void CallDBForGetPatientAccountDB(String uid)
    {
        patientAccountDB.GetPatientAccountInformation(uid);
    }

    private void CallDBForSaveImageAndGetUrl(String secureData, String fileName, byte[] getFullImageBytes)
    {
        storageDB.SaveFileIntoStorage(secureData,fileName,getFullImageBytes);
    }

    private void CallDBForSavePatientAccountData(String uid,PatientAccountDM patientAccountDM)
    {
        patientAccountDB.SaveAccountInformation(uid,patientAccountDM);
    }

    private void CallDBForCheckBirthNumberMultiplicity(String BirthNumberString)
    {
        accountMultiplicityDB.GetAccountMultiplicity(BirthNumberString);
    }
    private void CallDBForSaveAccountMultiplicity()
    {
        AccountMultiplicityData.put(DBConst.MultipleCheck,false);
        AccountMultiplicityData.put(UID,"");
        accountMultiplicityDB.SaveAccountMultiplicity(BirthNumberString,AccountMultiplicityData);
    }

    private void CallDBForUpdateAccountStatus()
    {
        accountStatusDB.SaveIntoAccountStatusDBFromAdmin(UID,DBConst.Patient,true,true);
    }

    ///******************************************************************************************///
    ///**********************************Interface Implementation********************************///
    ///******************************************************************************************///

    ///**********************************DBHelperInterface Implementation************************///
    @Override
    public void GetUID(String UID)
    {
        GetUIDFromDB(UID);
    }
    ///**********************************AccountStatusDBInterface Implementation************************///
    @Override
    public void GetAccountStatus(boolean result, ArrayList<AccountStatusDM> arrayList)
    {

    }

    @Override
    public void AccountStatusSavingResult(boolean result)
    {
        GetAccountStatusSavingStatus(result);
    }

    ///**********************************PatientAccountDBInterface Implementation************************///
    @Override
    public void GetPatientAccountData(String DataResult, PatientAccountDM patientAccountDM)
    {
        switch (DataResult)
        {
            case DBConst.DATA_EXIST:
                GetPatientAccountDataFromDB(patientAccountDM);
                break;
            case DBConst.SUCCESSFULL:
                GetPatientAccountSaveStatusFromDB(DataResult);
                break;
        }
    }

    ///**********************************StorageDBInterface Implementation************************///
    @Override
    public void GetResultAndUrl(boolean result, String Url)
    {
        if (result)
        {
            GetImageUrlFromDB(Url);
        }
    }

    ///**********************************AccountMultiplicityInterface Implementation************************///
    @Override
    public void GetAccountMultiplicityData(String GettingResult, HashMap<String, Object> hashMap)
    {
        GetAccountMultiplicityResult(GettingResult,hashMap);
    }

    @Override
    public void SaveAccountMultiplicity(String SavingResult)
    {
        GetAccountMultiplicitySavingStatus(SavingResult);
    }
}

package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountMultiplicityDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountStatusDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBHelper;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorAccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.StorageDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLoadingDailog;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyTextWatcher;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class EditDoctorSecureInfoActivity extends AppCompatActivity implements View.OnClickListener,
        GetDataFromDBInterface
{
    //Database Variable
    private DBHelper dbHelper;
    private DoctorAccountDB doctorAccountDB;
    private StorageDB storageDB;
    private AccountStatusDB accountStatusDB;
    private AccountMultiplicityDB accountMultiplicityDB;

    //Class Variable
    private InitializationUIHelperClass initializationUIHelperClass;
    private MyImageGettingClass myImageGettingClass;
    private MyToastClass myToastClass;
    private MyLoadingDailog myLoadingDailog;

    //Primitive Variable
    private int WhichImageView;
    private int VALIDATION_COLOR_GREEN;
    private String UID;
    private ArrayList<String> ImageUrlArrayList;

    //Object Variable
    private Activity activity;

    private String[] DataKey;
    private HashMap<String,String> AccountDataHashMap;

    private EditText[] editTexts;
    private ImageView[] imageViews;
    private Button[] buttons;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_edit_secure_info);
        Initialization();
        InitializationClass();
        InitializationUI();
        InitializationDB();

        CallDBForAccountUID();
    }
    private void Initialization()
    {
        activity= EditDoctorSecureInfoActivity.this;
        VALIDATION_COLOR_GREEN= ContextCompat.getColor(activity,R.color.colorGreen);
        AccountDataHashMap=new HashMap<>();
        ImageUrlArrayList=new ArrayList<>();
        DataKey=new String[]{DBConst.ProfileImageUrl,DBConst.Name,DBConst.StudiedCollege,DBConst.PassingYear,
                DBConst.CompletedDegree,DBConst.Specialization,DBConst.NoOfPracticingYear,DBConst.PhoneNumber,DBConst.AvailableArea,
                DBConst.Specialization, DBConst.NIDNumber,DBConst.BMDCRegNumber,
                DBConst.NIDImageUrl,DBConst.BMDCRegImageUrl, DBConst.AuthorityValidity};
    }
    private void InitializationClass()
    {
        initializationUIHelperClass=new InitializationUIHelperClass(getWindow().getDecorView());
        myImageGettingClass=new MyImageGettingClass(activity);
        myToastClass=new MyToastClass(activity);
        myLoadingDailog=new MyLoadingDailog(activity,R.drawable.spinner);
    }
    private void InitializationUI()
    {
        int[] editTextId={R.id.edit_text_0,R.id.edit_text_1};
        int[] imageViewId={R.id.image_view_0,R.id.image_view_1};
        int[] buttonId={R.id.button_0,R.id.button_1,R.id.button_2};

        editTexts=initializationUIHelperClass.setEditTexts(editTextId);
        editTexts[0].addTextChangedListener(new MyTextWatcher(activity, VARConst.VERIFICATION_CODE_VALIDITY,editTexts[0].getId()));
        imageViews=initializationUIHelperClass.setImageViews(imageViewId);
        for(int i=0; i<imageViews.length; i++)
        {
            imageViews[i].setEnabled(false);
        }
        buttons=initializationUIHelperClass.setButtons(buttonId);
        for(int i=0; i<buttons.length; i++)
        {
            buttons[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_0:
                GetImageFromCameraOrGallery(0);
                break;
            case R.id.button_1:
                GetImageFromCameraOrGallery(1);
                break;
            case R.id.button_2:
                GetAllDataFromFieldAndStartSavingProcess();
                break;
        }
    }
    private void GetImageFromCameraOrGallery(int WhichImageView)
    {
        this.WhichImageView=WhichImageView;
        myImageGettingClass.GetImageFromCameraOrGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE||requestCode==VARConst.REQUEST_CAMERA_CODE||requestCode==VARConst.REQUEST_GALLERY_CODE) && resultCode==RESULT_OK && data!=null)
        {
            if (WhichImageView==0)
            {
                imageViews[0].setEnabled(true);
            }
            else
            {
                imageViews[1].setEnabled(true);
            }
            myImageGettingClass.onActivityResult(VARConst.IV,imageViews[WhichImageView].getId(),requestCode,resultCode,data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        myImageGettingClass.onRequestPermissionsResult(activity,requestCode,permissions,grantResults);

    }

    private void GetAllDataFromFieldAndStartSavingProcess()
    {
        if (editTexts[0].getCurrentTextColor()==VALIDATION_COLOR_GREEN)
        {
            if (imageViews[0].isEnabled())
            {
                if (editTexts[1].getText().toString().length()>4)
                {
                    if (imageViews[1].isEnabled())
                    {
                        CallDBForBMDCNumberAlreadyRegisteredOrNot();
                    }
                    else
                    {
                        myToastClass.LToast("Upload a picture of BMDC Registration copy");
                    }
                }
                else
                {
                    UploadOnlyNIDDocument();
                }
            }
            else
            {
                myToastClass.LToast("Upload a picture of NID card");
            }
        }
        else
        {
            editTexts[0].setError("NID number must be needed");
        }
    }
    private void UploadOnlyNIDDocument()
    {
        String ImageTitle_0="NID_"+UID+".jpg";
        CallDBForImageUpload(ImageTitle_0,myImageGettingClass.GetFullImageBytes(VARConst.IV,imageViews[0].getId()));
    }
    private void UploadBMDCDocument()
    {
        //String ImageTitle_0="NID_"+UID+".jpg";
        //CallDBForImageUpload(ImageTitle_0,myImageGettingClass.GetFullImageBytes(VARConst.IV,imageViews[0].getId()));
        String ImageTitle_1="BMDC"+UID+".jpg";
        CallDBForImageUpload(ImageTitle_1,myImageGettingClass.GetFullImageBytes(VARConst.IV,imageViews[1].getId()));
    }





    ///***************************Database Return Section Start************************************///
    private void GetAccountUIDFromDB(HashMap<String,Object> hashMap)
    {
        if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.NOT_NULL_USER))
        {
            this.UID=hashMap.get(DBConst.UID).toString();
            CallDBForGetDoctorAccountInformation();
        }
        else
        {
            finish();
        }
    }
    private void GetDoctorAccountInformationFromDB(HashMap<String,Object> hashMap)
    {
        if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.DATA_EXIST))
        {
            for(int i=0; i<DataKey.length; i++)
            {
                if (hashMap.containsKey(DataKey[i]))
                {
                    AccountDataHashMap.put(DataKey[i],hashMap.get(DataKey[i]).toString());
                }
                else
                {
                    AccountDataHashMap.put(DataKey[i],DBConst.UNKNOWN);
                }
            }
            AccountDataHashMap.put(DBConst.AuthorityValidity,DBConst.UNVERIFIED);
        }
        else if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.DATA_NOT_EXIST))
        {
            startActivity(new Intent(activity,EditDoctorProfileActivity.class));
        }
        else
        {
            finish();
        }
    }

    private void GetBMDCNumberAlreadyRegisteredOrNotResultFromDB(HashMap<String,Object> hashMap)
    {
        String GettingResult=hashMap.get(DBConst.RESULT).toString();
        if (GettingResult.matches(DBConst.DATA_EXIST))
        {
            Set keyset=hashMap.keySet();
            int check=0;
            Iterator iterator=keyset.iterator();
            while (iterator.hasNext())
            {
                String keys=iterator.next().toString();
                if (!(keys.matches(DBConst.RESULT )|| keys.matches(DBConst.MultipleCheck)))
                {
                    if (keys.matches(UID))
                    {
                        check=1;
                    }
                }
            }
            if (check==0)
            {
                editTexts[1].setText("");
                imageViews[1].setImageResource(R.drawable.upload_doc_image);
                imageViews[1].setEnabled(false);
                myToastClass.LToast("Sorry BMDC number already registered ! ! !");
            }
            else
            {
                CallDBForSaveAccountMultiplicity();
            }
        }
        else if (GettingResult.matches(DBConst.DATA_NOT_EXIST))
        {
            CallDBForSaveAccountMultiplicity();
        }
    }

    private void GetImageURLFromDB(HashMap<String,Object> hashMap)
    {
        if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            ImageUrlArrayList.add(hashMap.get(DBConst.URL).toString());
            if (editTexts[1].getText().length()>4 && imageViews[1].isEnabled() && ImageUrlArrayList.size()==2)
            {
                AccountDataHashMap.put(DBConst.NIDNumber,editTexts[0].getText().toString());
                AccountDataHashMap.put(DBConst.NIDImageUrl,ImageUrlArrayList.get(0));
                AccountDataHashMap.put(DBConst.BMDCRegNumber,editTexts[1].getText().toString());
                AccountDataHashMap.put(DBConst.BMDCRegImageUrl,ImageUrlArrayList.get(1));
                CallDBForSaveDoctorAccountInformation();
            }
            if (!(editTexts[1].getText().length()>4) && !(imageViews[1].isEnabled()) && ImageUrlArrayList.size()==1)
            {
                AccountDataHashMap.put(DBConst.NIDNumber,editTexts[0].getText().toString());
                AccountDataHashMap.put(DBConst.NIDImageUrl,ImageUrlArrayList.get(0));
                AccountDataHashMap.put(DBConst.BMDCRegNumber,VARConst.UNKNOWN);
                AccountDataHashMap.put(DBConst.BMDCRegImageUrl,VARConst.UNKNOWN);
                CallDBForSaveDoctorAccountInformation();
            }

            if (editTexts[1].getText().length()>4 && imageViews[1].isEnabled() && ImageUrlArrayList.size()==1)
            {
                UploadBMDCDocument();
            }

        }
        else
        {
            myToastClass.LToast("Please retry...");
        }
    }

    private void GetAccountMultiplicitySavingStatusFromDB(HashMap<String,Object> hashMap)
    {
        if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            UploadOnlyNIDDocument();
        }
        else
        {
            myToastClass.LToast("Please retry...");
        }
    }
    private void GetDoctorAccountInformationSavingStatusFromDB(HashMap<String,Object> hashMap)
    {
        if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            CallDBForSaveAccountStatus();
        }
        else
        {
            myToastClass.LToast("Please retry...");
        }
    }
    private void GetAccountStatusSavingResultFromDB(HashMap<String,Object> hashMap)
    {
        if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            startActivity(new Intent(activity,DoctorMainActivity.class));
            CancelDialog();
        }
    }
    private void ShowLoadingDialog()
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

    ///***************************Database Return Section End************************************///



    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///************************************Database Part*****************************************///
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void InitializationDB()
    {
        dbHelper=new DBHelper(activity);
        storageDB=new StorageDB(activity);
        doctorAccountDB=new DoctorAccountDB(activity);
        accountStatusDB=new AccountStatusDB(activity);
        accountMultiplicityDB=new AccountMultiplicityDB(activity);
    }
    ///************************************Database Calling**************************************///
    private void CallDBForAccountUID()
    {
        ShowLoadingDialog();
        dbHelper.GetUID();
    }
    private void CallDBForGetDoctorAccountInformation()
    {
        ShowLoadingDialog();
        doctorAccountDB.GetDoctorAccountInformation(UID);
    }
    private void CallDBForBMDCNumberAlreadyRegisteredOrNot()
    {
        ShowLoadingDialog();
        accountMultiplicityDB.GetAccountMultiplicity(DBConst.Doctor,editTexts[1].getText().toString());
    }
    private void CallDBForSaveAccountMultiplicity()
    {
        ShowLoadingDialog();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(DBConst.MultipleCheck,false);
        hashMap.put(UID,DBConst.UNVERIFIED);
        accountMultiplicityDB.SaveAccountMultiplicity(DBConst.Doctor,editTexts[1].getText().toString(),hashMap);

    }
    private void CallDBForImageUpload(String ImageTitle, byte[] ImageBytes)
    {
        storageDB.SaveFileIntoStorage(DBConst.SecureData,ImageTitle,ImageBytes);
    }
    private void CallDBForSaveDoctorAccountInformation()
    {
        ShowLoadingDialog();
        doctorAccountDB.SaveDoctorAccountInformation(UID,AccountDataHashMap);
    }
    private void CallDBForSaveAccountStatus()
    {
        ShowLoadingDialog();
        accountStatusDB.SaveIntoAccountStatusDB(UID,DBConst.Doctor,true,true,false);
    }

    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        CancelDialog();
        switch (WhichDB)
        {
            case DBConst.GetAccountUID:
                GetAccountUIDFromDB(DataHashMap);
                break;
            case DBConst.GetDoctorAccountInformation:
                GetDoctorAccountInformationFromDB(DataHashMap);
                break;
            case DBConst.GetAccountMultiplicityDB:
                GetBMDCNumberAlreadyRegisteredOrNotResultFromDB(DataHashMap);
                break;
            case DBConst.SaveAccountMultiplicityDB:
                GetAccountMultiplicitySavingStatusFromDB(DataHashMap);
                break;
            case DBConst.GetSavingFileUrlData:
                GetImageURLFromDB(DataHashMap);
                break;
            case DBConst.SaveDoctorAccountInformation:
                GetDoctorAccountInformationSavingStatusFromDB(DataHashMap);
                break;
            case DBConst.SaveAccountStatusDB:
                GetAccountStatusSavingResultFromDB(DataHashMap);
                break;
        }
    }

    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList) {

    }
}

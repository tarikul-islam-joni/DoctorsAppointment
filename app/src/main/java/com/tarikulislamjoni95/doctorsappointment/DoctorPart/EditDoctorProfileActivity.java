package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorAccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DoctorFilterDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.StorageDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
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
    private DoctorFilterDB doctorFilterDB;

    private InitializationUIHelperClass initializationUIHelperClass;
    private MyImageGettingClass myImageGettingClass;
    private MyToastClass myToastClass;


    private Activity activity;

    private HashMap<String,Object> DataHashMap;
    private HashMap<String,String> AccountDataHashMap;
    private String[] DataKey;
    private String UID;
    private int VALIDATION_GREEN;
    private String[] AvailableArea;

    private AlertDialog CategorySelectionDialog;

    private EditText[] editTexts;
    private TextView[] textViews;
    private Button[] buttons;
    private ImageView[] imageViews;
    private CircleImageView[] circleImageViews;
    private Spinner[] spinners;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_edit_profile);

        Initialization();
        InitializationClass();
        InitializationUI();
        InitializationDB();
        CallDBForAccountUID();
    }

    private void Initialization()
    {
        activity=EditDoctorProfileActivity.this;
        VALIDATION_GREEN=ContextCompat.getColor(activity,R.color.colorGreen);
        AvailableArea=getResources().getStringArray(R.array.division_list);
        DataHashMap=new HashMap<>();
        AccountDataHashMap=new HashMap<>();
        DataKey=new String[]{DBConst.ProfileImageUrl,DBConst.Name,DBConst.StudiedCollege,DBConst.PassingYear,
                DBConst.CompletedDegree,DBConst.Specialization,DBConst.NoOfPracticingYear,DBConst.PhoneNumber,DBConst.AvailableArea,
                DBConst.Specialization, DBConst.NIDNumber,DBConst.BMDCRegNumber,
                DBConst.NIDImageUrl,DBConst.BMDCRegImageUrl, DBConst.AuthorityValidity};

        for(int i=0; i<DataKey.length; i++)
        {
            AccountDataHashMap.put(DataKey[i],VARConst.UNKNOWN);
        }
    }
    private void InitializationUI()
    {
        //TextView Initialization
        int[] textviewsId={R.id.text_view_0,R.id.text_view_1};
        textViews= initializationUIHelperClass.setTextViews(textviewsId);
        textViews[0].setOnClickListener(this);
        textViews[1].setOnClickListener(this);

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
                AccountDataHashMap.put(DBConst.AvailableArea,AvailableArea[i]);
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
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.text_view_0:
                myImageGettingClass.GetImageFromCameraOrGallery();
                break;
            case R.id.text_view_1:
                ShowSpecializationDialog();
                break;
            case R.id.button_0:
                GetAllDataFromField();
                break;
        }
    }

    private void ShowSpecializationDialog()
    {
        final String[]CategoryResourceStringArray=getResources().getStringArray(R.array.doctor_category_array);
        final ArrayList<String> CategoryArrayList=new ArrayList<>();
        boolean[] Specialization_Checked=new boolean[CategoryResourceStringArray.length];
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
                textViews[1].setText(CategoryStringBuilder.toString());
                CategorySelectionDialog.dismiss();
            }
        });

        CategorySelectionDialog=builder.create();
        CategorySelectionDialog.show();
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
        else if (textViews[1].getText().toString().isEmpty())
        {
            myToastClass.SToast("Select your specialization");
        }
        else
        {
            AccountDataHashMap.put(DBConst.Name,editTexts[0].getText().toString());
            AccountDataHashMap.put(DBConst.StudiedCollege,editTexts[1].getText().toString());
            AccountDataHashMap.put(DBConst.PassingYear,editTexts[2].getText().toString());
            AccountDataHashMap.put(DBConst.CompletedDegree,editTexts[3].getText().toString());
            AccountDataHashMap.put(DBConst.Specialization,textViews[1].getText().toString());
            AccountDataHashMap.put(DBConst.NoOfPracticingYear,editTexts[4].getText().toString());
            AccountDataHashMap.put(DBConst.PhoneNumber,editTexts[5].getText().toString());
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
    private void GetDoctorAccountInformationFromDB()
    {
        if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.DATA_EXIST))
        {
            for(int i=0; i<DataKey.length; i++)
            {
                AccountDataHashMap.put(DataKey[i], DataHashMap.get(DataKey[i]).toString());
            }
            if (!AccountDataHashMap.get(DBConst.ProfileImageUrl).matches(DBConst.UNKNOWN))
            {
                imageViews[0].setEnabled(false);
                Picasso.get().load(AccountDataHashMap.get(DBConst.ProfileImageUrl)).into(imageViews[0]);
            }
            editTexts[0].setText(AccountDataHashMap.get(DBConst.Name));
            editTexts[1].setText(AccountDataHashMap.get(DBConst.StudiedCollege));
            editTexts[2].setText(AccountDataHashMap.get(DBConst.PassingYear));
            editTexts[3].setText(AccountDataHashMap.get(DBConst.CompletedDegree));
            textViews[1].setText(AccountDataHashMap.get(DBConst.Specialization));
            editTexts[4].setText(AccountDataHashMap.get(DBConst.NoOfPracticingYear));
            editTexts[5].setText(AccountDataHashMap.get(DBConst.PhoneNumber));
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
            for (int i=0; i<arrayList.size(); i++)
            {
                AvailableArea[i]=arrayList.get(i);
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

            CallDBForSaveFilterData();
        }
        else
        {
            myToastClass.LToast("Please try again...");
        }
    }


    private void CallDBForSaveFilterData()
    {
        doctorFilterDB.SaveDoctorFilterData(UID,AccountDataHashMap);
    }

    private void GetSaveFilterDataResult()
    {
        if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
        {
            CallDBForAccountStatus();
        }
        else
        {
            myToastClass.SToast("Please try again...");
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
        doctorFilterDB=new DoctorFilterDB(activity);
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
        Log.d("EditDoctorProfile",WhichDB+" :::: "+DataHashMap.toString());
        this.DataHashMap=DataHashMap;
        switch (WhichDB)
        {
            case DBConst.GetAccountUID:
                GetAccountUIDFromDB();
                break;
            case DBConst.GetDoctorAccountInformation:
                GetDoctorAccountInformationFromDB();
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
            case DBConst.GetStatusOnSave_SS_SP_AA_InDoctorFiltration:
                GetSaveFilterDataResult();
                break;
        }
    }

    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList)
    {

    }
}

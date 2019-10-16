package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.BuildConfig;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.PatientAccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.StorageDB;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.tarikulislamjoni95.doctorsappointment.MyGoogleMapClass.DownloadUrl;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class StoreMedicalDocumentActivity extends AppCompatActivity implements GetDataFromDBInterface
{
    private Activity activity;


    private MyImageGettingClass myImageGettingClass;

    private HashMap<String,Object> hashMap=new HashMap<>();

    private StoreDocumentRecyclerViewAdapter storeDocumentRecyclerViewAdapter;
    private ArrayList<HashMap<String,Object>> DocumentArrayList;

    private String BackActivity;
    private String PatientUID;

    private EditText editText;
    private Button[] buttons;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_document_store);
        Initialization();
        InitializationUI();

        CallDBForGetEntireMedicalDocumentOfPatient();
    }

    private void Initialization()
    {
        activity=StoreMedicalDocumentActivity.this;
        PatientUID=getIntent().getExtras().getString(DBConst.UID);
        BackActivity=getIntent().getExtras().getString(DBConst.BackActivity);
        DocumentArrayList=new ArrayList<>();
    }

    private void InitializationUI()
    {
        editText=findViewById(R.id.edit_text_0);
        buttons=new InitializationUIHelperClass(getWindow().getDecorView()).setButtons(new int[]{R.id.button_0,R.id.button_1});
        recyclerView=findViewById(R.id.recycler_view_0);
        recyclerView.setLayoutManager(new GridLayoutManager(activity,2,RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SearchDocument();
            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                DocumentArrayList.clear();
                storeDocumentRecyclerViewAdapter=new StoreDocumentRecyclerViewAdapter(activity,DocumentArrayList);
                ArrayList<String> arrayList=new ArrayList<>();
                storeDocumentRecyclerViewAdapter.OpenDocumentEditDialog(DBConst.UNKNOWN,DBConst.UNKNOWN,arrayList);
            }
        });


    }
    private void SetDocumentListIntoRecyclerView(ArrayList<HashMap<String,Object>> DocumentArrayList)
    {
        this.DocumentArrayList=DocumentArrayList;
        storeDocumentRecyclerViewAdapter=new StoreDocumentRecyclerViewAdapter(activity,DocumentArrayList);
        recyclerView.setAdapter(storeDocumentRecyclerViewAdapter);
    }

    private void CallDBForGetEntireMedicalDocumentOfPatient()
    {
        PatientAccountDB patientAccountDB=new PatientAccountDB(activity);
        patientAccountDB.GetReportDetailsFromPatientAccount(DBConst.SELF);
    }

    @Override
    public void GetSingleDataFromDatabase(String WhichDB, HashMap<String, Object> DataHashMap)
    {
        Log.d("myData : ","DB : "+WhichDB+" : "+DataHashMap.toString());
        switch (WhichDB)
        {
            case DBConst.GetReportDetailsFromPatientAccountDB:
                ParseReportDataToGetPatientReport(DataHashMap);
                break;
            case DBConst.GetStatusOnSaveReportImageToTheStorageDB:
                if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
                {
                    storeDocumentRecyclerViewAdapter.GetStatusOnSaveReportImageToTheStorageDB(DataHashMap.get(DBConst.URL).toString());
                }
                else
                {
                    storeDocumentRecyclerViewAdapter.GetStatusOnSaveReportImageToTheStorageDB(DBConst.UNKNOWN);
                }
                break;
            case DBConst.GetStatusOnSaveReportDetailsIntoPatientAccountDB:
                storeDocumentRecyclerViewAdapter.GetStatusOnSaveReportDetailsIntoPatientAccountDB();
                break;
            case DBConst.GetStatusOnDeleteEntireReportFromStorageDB:
                storeDocumentRecyclerViewAdapter.GetStatusOnDeleteEntireReportFromStorageDB();
                break;
            case DBConst.GetStatusOnDeleteEntireReportFromPatientAccount:
                storeDocumentRecyclerViewAdapter.GetStatusOnDeleteEntireReportFromPatientAccount();
                break;
            case DBConst.GetStatusOnDeleteSingleImageFromStorageDB:
                if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
                {
                    storeDocumentRecyclerViewAdapter.GetStatusOnDeleteSingleImageFromStorageDB(DataHashMap.get(DBConst.URL).toString());
                }
                break;
            case DBConst.GetStatusOnDeleteSingleImageFromPatientAccount:
                storeDocumentRecyclerViewAdapter.GetStatusOnDeleteSingleImageFromPatientAccount();
                break;
            case "ImageData":
                if (DataHashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
                {
                    if ((byte[])DataHashMap.get("ImageData")!=null)
                    {
                        byte[] imagebyte=(byte[])DataHashMap.get("ImageData");
                        storeDocumentRecyclerViewAdapter.GetReportImageBytesFromGalleryOrCamera(DataHashMap.get(DBConst.RESULT).toString(),imagebyte);
                    }
                    else
                    {
                        Log.d("myError 12","null images");
                    }
                }
                else
                {
                    Log.d("myError 12","key not found images");
                }
                break;
        }
    }


    private void ParseReportDataToGetPatientReport(HashMap<String,Object> hashMap)
    {
        if (!hashMap.isEmpty())
        {
            if (hashMap.containsKey(DBConst.RESULT))
            {
                if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
                {
                    if (hashMap.containsKey(DBConst.DataSnapshot))
                    {
                        DataSnapshot dataSnapshot=(DataSnapshot) hashMap.get(DBConst.DataSnapshot);
                        if (dataSnapshot.exists())
                        {
                            DocumentArrayList=new ArrayList<>();
                            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                            {
                                HashMap<String,Object> hashMap1=new HashMap<>();
                                for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                                {
                                    hashMap1.put(dataSnapshot2.getKey(),dataSnapshot2.getValue());
                                }
                                DocumentArrayList.add(hashMap1);
                            }

                            SetDocumentListIntoRecyclerView(DocumentArrayList);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void GetMultipleDataFromDatabase(String WhichDB, ArrayList<HashMap<String, Object>> DataHashMapArrayList)
    {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myImageGettingClass.onActivityResult(VARConst.NO_IMAGE_VIEW,0,requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        myImageGettingClass.onRequestPermissionsResult(activity,requestCode,permissions,grantResults);
    }

    class StoreDocumentRecyclerViewAdapter extends RecyclerView.Adapter<StoreDocumentRecyclerViewAdapter.ViewHolder>
    {
        class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView textView;
            private View itemView;
            public ViewHolder(View itemView)
            {
                super(itemView);
                this.itemView=itemView;
                textView=itemView.findViewById(R.id.text_view_0);
            }
        }
        public int ClickedPosition=0;
        private Activity activity;
        private ArrayList<HashMap<String,Object>> documentArrayList;
        public StoreDocumentRecyclerViewAdapter(Activity activity, ArrayList<HashMap<String,Object>> documentArrayList)
        {
            this.activity=activity;
            this.documentArrayList=documentArrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.patient_document_show_model,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
        {
            if (documentArrayList.size()!=0)
            {
                HashMap<String,Object> objectHashMap=new HashMap<>();
                objectHashMap=documentArrayList.get(position);
                if (objectHashMap.containsKey(DBConst.ReportTitle))
                {
                    holder.textView.setText(objectHashMap.get(DBConst.ReportTitle).toString());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view)
                        {
                            ClickedPosition=position;
                            ArrayList<String> DocumentImageUrl=GetReportTitleAndReportDetails(documentArrayList.get(ClickedPosition));
                            if (documentArrayList.get(ClickedPosition).containsKey(DBConst.ReportDetails))
                            {
                                hashMap.put(DBConst.ReportTitle,documentArrayList.get(ClickedPosition).get(DBConst.ReportTitle).toString());
                                hashMap.put(DBConst.ReportDetails,documentArrayList.get(ClickedPosition).get(DBConst.ReportDetails).toString());
                                OpenDocumentDetailsDialog(holder.textView.getText().toString(), documentArrayList.get(ClickedPosition).get(DBConst.ReportDetails).toString(),DocumentImageUrl);
                            }
                            else
                            {
                                OpenDocumentDetailsDialog(holder.textView.getText().toString(),"",DocumentImageUrl);
                            }
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return documentArrayList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        private ArrayList<String> GetReportTitleAndReportDetails(HashMap<String,Object> hashMap)
        {
            ArrayList<String> ImageDownloadLinkArrayList=new ArrayList<>();
            Set KeySet=hashMap.keySet();
            Iterator<String> iterator=KeySet.iterator();
            while (iterator.hasNext())
            {
                String Key=iterator.next();
                if (!(Key.matches(DBConst.ReportTitle) || Key.matches(DBConst.ReportDetails)))
                {
                    ImageDownloadLinkArrayList.add(hashMap.get(Key).toString());
                }
            }

            return ImageDownloadLinkArrayList;
        }

        private String ReportTitle;
        private AlertDialog DocumentDetailsDialog;
        private View DocumentDetailsDialogView;
        private void OpenDocumentDetailsDialog(final String ReportTitle_1, final String ReportDetails_1, final ArrayList<String> arrayList)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(activity,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            DocumentDetailsDialogView=LayoutInflater.from(activity).inflate(R.layout.patient_document_details_show_layout,null,false);
            final TextView[] textViews=new TextView[2];
            textViews[0]=DocumentDetailsDialogView.findViewById(R.id.text_view_0);
            textViews[1]=DocumentDetailsDialogView.findViewById(R.id.text_view_1);
            textViews[0].setText(ReportTitle_1);
            textViews[1].setText(ReportDetails_1);

            ImageView[] imageViews=new ImageView[2];
            imageViews[0]=DocumentDetailsDialogView.findViewById(R.id.image_view_0);
            imageViews[1]=DocumentDetailsDialogView.findViewById(R.id.image_view_1);
            Button[] buttons=new Button[2];
            buttons[0]=DocumentDetailsDialogView.findViewById(R.id.button_0);
            buttons[1]=DocumentDetailsDialogView.findViewById(R.id.button_1);

            ImageRecyclerViewAdapter imageRecyclerViewAdapter=new ImageRecyclerViewAdapter(activity,arrayList);
            RecyclerView recyclerView=DocumentDetailsDialogView.findViewById(R.id.recycler_view_0);
            recyclerView.setLayoutManager(new GridLayoutManager(activity,1,RecyclerView.VERTICAL,false));
            recyclerView.setAdapter(imageRecyclerViewAdapter);


            buttons[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    ReportTitle=textViews[0].getText().toString();
                    DeleteDocumentFromDB();
                    DocumentDetailsDialog.dismiss();

                }
            });

            imageViews[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    DocumentDetailsDialog.dismiss();
                }
            });

            imageViews[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    DocumentDetailsDialog.dismiss();
                    ReportTitle=textViews[0].getText().toString();
                    ReportDetails=textViews[1].getText().toString();
                    hashMap.put(DBConst.ReportTitle,ReportTitle);
                    hashMap.put(DBConst.ReportDetails,ReportDetails);
                    OpenDocumentEditDialog(ReportTitle,ReportDetails,arrayList);
                }
            });

            buttons[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownloadDocumentProcess();
                    DocumentDetailsDialog.dismiss();
                }
            });

            builder.setView(DocumentDetailsDialogView);
            DocumentDetailsDialog=builder.create();
            DocumentDetailsDialog.show();

        }

        private int ClickDeleteItemPosition=0;
        class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageRecyclerViewHolder>
        {
            private Activity activity;
            private ArrayList<String> arrayList;
            public ImageRecyclerViewAdapter(Activity activity,ArrayList<String> arrayList)
            {
                this.activity=activity;
                this.arrayList=arrayList;
                Log.d("myError 1","ArrayList : "+arrayList.toString());
            }

            class ImageRecyclerViewHolder extends RecyclerView.ViewHolder
            {
                private ImageView imageView;
                public ImageRecyclerViewHolder(View ImageRecyclerItemView)
                {
                    super(ImageRecyclerItemView);
                    imageView=ImageRecyclerItemView.findViewById(R.id.image_view_0);
                }
            }

            @NonNull
            @Override
            public ImageRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ImageRecyclerViewHolder(LayoutInflater.from(activity).inflate(R.layout.patient_document_image_show_model,parent,false));
            }

            @Override
            public void onBindViewHolder(@NonNull ImageRecyclerViewHolder holder, final int position)
            {
                if (arrayList.size()!=0)
                {
                    if (!arrayList.get(position).matches(DBConst.UNKNOWN))
                    {
                        Picasso.get().load(arrayList.get(position)).into(holder.imageView);
                        DownloadImage(arrayList.get(position).toString(),DBConst.UNKNOWN);
                    }
                }
            }

            private void DownloadImage(String DownloadUrl,String ImageTitle)
            {
                try {
                    File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                            +"/"+ Uri.parse(ImageTitle).getLastPathSegment());
                    if (file.exists())
                    {
                        Uri uri= FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID,file);
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri,"image/*");
                        PackageManager pm=getPackageManager();
                        if (intent.resolveActivity(pm)!=null)
                        {
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        new ImageDownloadUsingAysnTaskInnerClass().execute(DownloadUrl);
                    }
                } catch (Exception e)
                {
                    Log.d("myError",e.toString());
                }
            }

            @Override
            public int getItemCount() {
                return arrayList.size();
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }
        }


        private void DeleteDocumentFromDB()
        {
            ShowConfirmAlertDialog("Entire",ReportTitle);
        }

        private void DownloadDocumentProcess()
        {

        }

        private EditImageRecyclerViewAdapter editImageRecyclerViewAdapter;
        private String GenerateReportImageTitle;
        private RecyclerView recyclerView_1;
        private TextView textView;
        private EditText[] editTexts=new EditText[2];
        private Button[] buttons=new Button[2];
        private ImageView[] imageViews=new ImageView[2];
        private AlertDialog DocumentEditDialog;
        private View DocumentEditDialogView;
        private boolean DataSavedCheck=false;
        private ArrayList<String> ImageArrayList;
        public String ReportDetails;
        public void OpenDocumentEditDialog(final String ReportTitle_1, String ReportDetails_1, ArrayList<String> arrayList)
        {
            ImageArrayList=new ArrayList<>();
            ImageArrayList=arrayList;
            ReportTitle=ReportTitle_1;
            ReportDetails=ReportDetails_1;

            hashMap.put(DBConst.ReportTitle,ReportTitle_1);
            hashMap.put(DBConst.ReportDetails,ReportDetails_1);

            AlertDialog.Builder builder=new AlertDialog.Builder(activity,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            DocumentEditDialogView=LayoutInflater.from(activity).inflate(R.layout.patient_document_edit_layout,null,false);
            editTexts[0]=DocumentEditDialogView.findViewById(R.id.edit_text_0);
            editTexts[1]=DocumentEditDialogView.findViewById(R.id.edit_text_1);
            buttons[0]=DocumentEditDialogView.findViewById(R.id.button_0);
            buttons[1]=DocumentEditDialogView.findViewById(R.id.button_1);
            imageViews[0]=DocumentEditDialogView.findViewById(R.id.image_view_0);
            imageViews[1]=DocumentEditDialogView.findViewById(R.id.image_view_1);
            textView=DocumentEditDialogView.findViewById(R.id.text_view_edit_text_0);

            editImageRecyclerViewAdapter=new EditImageRecyclerViewAdapter(activity,ImageArrayList);
            recyclerView_1=DocumentEditDialogView.findViewById(R.id.recycler_view_0);
            recyclerView_1.setLayoutManager(new GridLayoutManager(activity,1,RecyclerView.VERTICAL,false));
            recyclerView_1.setAdapter(editImageRecyclerViewAdapter);
            editImageRecyclerViewAdapter.notifyDataSetChanged();

            if (!ReportDetails.matches(DBConst.UNKNOWN))
            {
                editTexts[1].setText(ReportDetails);
            }
            if (ReportTitle.matches(DBConst.UNKNOWN))
            {
                ReportTitle=DBConst.UNKNOWN;
                editTexts[0].setVisibility(View.VISIBLE);
                editTexts[0].setEnabled(true);
                textView.setVisibility(View.GONE);
                textView.setEnabled(false);
            }
            else
            {
                editTexts[0].setVisibility(View.GONE);
                editTexts[0].setEnabled(false);
                textView.setVisibility(View.VISIBLE);
                textView.setEnabled(true);
                textView.setText(ReportTitle);
            }
            imageViews[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentEditDialog.dismiss();
                }
            });
            imageViews[1].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (!ReportTitle.matches(DBConst.UNKNOWN))
                    {
                        if (editTexts[0].isEnabled())
                        {
                            ReportTitle=editTexts[0].getText().toString();
                            hashMap.put(DBConst.ReportTitle,ReportTitle);
                        }
                        else
                        {
                            DataSavedCheck=true;
                            ReportTitle=textView.getText().toString();
                            hashMap.put(DBConst.ReportTitle,ReportTitle);
                        }
                        if (DataSavedCheck)
                        {
                            DocumentEditDialog.dismiss();
                            //////////////////////////////////////////////////
                            ShowConfirmAlertDialog("Entire",ReportTitle);
                            //////////////////////////////////////////////////
                        }
                    }
                }
            });

            buttons[0].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (editTexts[0].isEnabled())
                    {
                        if (!editTexts[0].getText().toString().isEmpty())
                        {
                            ReportTitle=editTexts[0].getText().toString();
                            hashMap.put(DBConst.ReportTitle,ReportTitle);
                        }
                    }
                    else
                    {
                        ReportTitle=textView.getText().toString();
                        hashMap.put(DBConst.ReportTitle,ReportTitle);
                    }

                    ReportDetails=editTexts[1].getText().toString();
                    if (!ReportTitle.matches(DBConst.UNKNOWN))
                    {
                        CallDBForSaveReportDetailsWithoutImage();
                    }
                }
            });

            buttons[1].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    ReportDetails=editTexts[1].getText().toString();
                    if (editTexts[0].isEnabled())
                    {
                        if (!editTexts[0].getText().toString().isEmpty())
                        {
                            ReportTitle=editTexts[0].getText().toString();
                            hashMap.put(DBConst.ReportTitle,ReportTitle);
                        }
                    }
                    else
                    {
                        ReportTitle=textView.getText().toString();
                        hashMap.put(DBConst.ReportTitle,ReportTitle);
                    }

                    ReportDetails=editTexts[1].getText().toString();

                    if (!ReportTitle.matches(DBConst.UNKNOWN))
                    {
                        myImageGettingClass=new MyImageGettingClass(activity);
                        myImageGettingClass.GetImageFromCameraOrGallery();
                        editTexts[0].setVisibility(View.GONE);
                        editTexts[0].setEnabled(false);
                        textView.setText(ReportTitle);
                        hashMap.put(DBConst.ReportTitle,textView.getText().toString());
                        DataSavedCheck=true;
                    }
                }
            });
            builder.setView(DocumentEditDialogView);
            DocumentEditDialog=builder.create();
            DocumentEditDialog.show();
        }

        public void GetReportImageBytesFromGalleryOrCamera(String Result,byte[] ReportImageBytes)
        {
            if (Result.matches(DBConst.SUCCESSFUL))
            {
                GenerateReportImageTitle=GetReportImageTitle();
                CallDBForSaveReportImageToTheStorageDB(GenerateReportImageTitle,ReportImageBytes);
            }
            else
            {
                CallDBForSaveReportDetailsWithoutImage();
                new MyToastClass(activity).LToast("Failed to get image\nPlease try again...");
            }
        }



        public String GetReportImageTitle()
        {
            String timestamp=new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            return "Doctors_Appointment_"+timestamp+"_";
        }


        ///****************************SaveImageFileToStorageDBSystem****************************///
        private void CallDBForSaveReportImageToTheStorageDB(String ReportImageTitle,byte[] ReportImageBytes)
        {
            StorageDB storageDB=new StorageDB(activity);
            storageDB.SaveReportImageToTheMedicalReportDB(DBConst.SELF,hashMap.get(DBConst.ReportTitle).toString(),ReportImageTitle,ReportImageBytes,DBConst.GetStatusOnSaveReportImageToTheStorageDB);
        }

        public void GetStatusOnSaveReportImageToTheStorageDB(String DownloadURL)
        {
            if (!DownloadURL.matches(DBConst.UNKNOWN))
            {

                if (ImageArrayList==null)
                {
                    ImageArrayList=new ArrayList<>();
                }
                else
                {
                    ImageArrayList.add(DownloadURL);
                }

                if (editImageRecyclerViewAdapter!=null)
                {
                    editImageRecyclerViewAdapter.notifyDataSetChanged();
                }

                CallDBForSaveImageDownloadUrlToThePatientAccountDB(DownloadURL);
            }
            else
            {
                CallDBForSaveReportDetailsWithoutImage();
            }
        }
        private void CallDBForSaveImageDownloadUrlToThePatientAccountDB(String SavedDownloadUrl)
        {
            PatientAccountDB patientAccountDB=new PatientAccountDB(activity);
            patientAccountDB.SaveMedicalReportToThePatientAccountDB(DBConst.SELF,hashMap.get(DBConst.ReportTitle).toString(),ReportDetails,SavedDownloadUrl,DBConst.GetStatusOnSaveReportDetailsIntoPatientAccountDB);
        }

        private void CallDBForSaveReportDetailsWithoutImage()
        {
            PatientAccountDB patientAccountDB=new PatientAccountDB(activity);
            patientAccountDB.SaveMedicalReportToThePatientAccountDB(DBConst.SELF,hashMap.get(DBConst.ReportTitle).toString(),ReportDetails,DBConst.UNKNOWN,DBConst.GetStatusOnSaveReportDetailsIntoPatientAccountDB);
        }

        public void GetStatusOnSaveReportDetailsIntoPatientAccountDB()
        {
            if (editImageRecyclerViewAdapter!=null)
            {
                editImageRecyclerViewAdapter.notifyDataSetChanged();
            }
        }

        ///****************************SaveImageFileToStorageDBSystem****************************///

        ///******************************DeleteEntireReportFileSystem****************************///
        private void CallDBForDeleteEntireReportFileFromStorageThenDB()
        {
            Log.d("myObs 1: ",ReportTitle);
            StorageDB storageDB=new StorageDB(activity);
            storageDB.DeleteEntireReportFile(DBConst.SELF,hashMap.get(DBConst.ReportTitle).toString(),DBConst.GetStatusOnDeleteEntireReportFromStorageDB);
        }

        public void GetStatusOnDeleteEntireReportFromStorageDB()
        {

            if (editImageRecyclerViewAdapter!=null)
            {
                editImageRecyclerViewAdapter.notifyDataSetChanged();
            }

            CallDBForDeleteEntireReportFromPatientAccountDB();
        }

        private void CallDBForDeleteEntireReportFromPatientAccountDB()
        {
            Log.d("myObs 2: ",ReportTitle);
            PatientAccountDB patientAccountDB=new PatientAccountDB(activity);
            patientAccountDB.DeleteEntireReportFileFromPatientAccountDB(DBConst.SELF,hashMap.get(DBConst.ReportTitle).toString(),DBConst.GetStatusOnDeleteEntireReportFromPatientAccount);
        }

        public void GetStatusOnDeleteEntireReportFromPatientAccount()
        {
            if (editImageRecyclerViewAdapter!=null)
            {
                editImageRecyclerViewAdapter.notifyDataSetChanged();
            }

            CancelDialog();
        }

        private void CancelDialog()
        {
            if (DocumentEditDialog!=null)
            {
                if (DocumentEditDialog.isShowing())
                {
                    DocumentEditDialog.dismiss();
                }
            }

            if (DocumentDetailsDialog!=null)
            {
                if (DocumentDetailsDialog.isShowing())
                {
                    DocumentDetailsDialog.dismiss();
                }
            }
        }

        ///******************************DeleteEntireReportFileSystem****************************///


        private AlertDialog deleteAlertDialg;
        private void ShowConfirmAlertDialog(final String WhichOne, final String Key)
        {
            final AlertDialog.Builder builder=new AlertDialog.Builder(activity);
            builder.setTitle("Delete Alert !!! ");
            builder.setMessage("Are your sure delete to delete that file ? ");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    if (WhichOne.matches("Entire"))
                    {
                        CallDBForDeleteEntireReportFileFromStorageThenDB();
                    }
                    else if (WhichOne.matches("Single"))
                    {
                        CallDBForDeleteSingleImageByDownloadUrl(Key);
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    deleteAlertDialg.dismiss();
                }
            });
            deleteAlertDialg=builder.create();
            deleteAlertDialg.show();
        }

        ///******************************DeleteSingleReportFileSystem****************************///
        private void CallDBForDeleteSingleImageByDownloadUrl(String ImageDownloadUrl)
        {
            Log.d("myObs 3: ",ImageDownloadUrl);
            StorageDB storageDB=new StorageDB(activity);
            storageDB.DeleteReportImageByDownloadLink(ImageDownloadUrl,DBConst.GetStatusOnDeleteSingleImageFromStorageDB);
        }
        public void GetStatusOnDeleteSingleImageFromStorageDB(String DeletedImageUrl)
        {
            Log.d("myObs 4: ",DeletedImageUrl);
            if (ImageArrayList==null)
            {
                ImageArrayList=new ArrayList<>();
            }
            else
            {
                ImageArrayList.remove(ClickDeleteItemPosition);
            }

            if (editImageRecyclerViewAdapter!=null)
            {
                editImageRecyclerViewAdapter.notifyDataSetChanged();
            }

            DeleteImageLinkFromPatientAccount(DeletedImageUrl);
        }
        public void DeleteImageLinkFromPatientAccount(String DeletedImageUrl)
        {
            Log.d("myObs 5: ",ReportTitle+" : "+DeletedImageUrl);
            PatientAccountDB patientAccountDB=new PatientAccountDB(activity);
            patientAccountDB.DeleteImageUrlFromPatientReportDB(DBConst.SELF,hashMap.get(DBConst.ReportTitle).toString(),DeletedImageUrl,DBConst.GetStatusOnDeleteSingleImageFromPatientAccount);
        }

        public void GetStatusOnDeleteSingleImageFromPatientAccount()
        {
            Log.d("myObs 6: ",ReportTitle+" : DONE");
            if (editImageRecyclerViewAdapter!=null)
            {
                editImageRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
        ///******************************DeleteSingleReportFileSystem****************************///
    }

    class EditImageRecyclerViewAdapter extends RecyclerView.Adapter<EditImageRecyclerViewAdapter.EditImageRecyclerViewHolder>
    {
        private Activity activity;
        private ArrayList<String> arrayList;
        public EditImageRecyclerViewAdapter(Activity activity,ArrayList<String> arrayList)
        {
            this.activity=activity;
            this.arrayList=arrayList;

            Log.d("myError 2","ArrayList : "+arrayList.toString());
        }

        class EditImageRecyclerViewHolder extends RecyclerView.ViewHolder
        {
            private ImageView imageView;
            public EditImageRecyclerViewHolder(View ImageRecyclerItemView)
            {
                super(ImageRecyclerItemView);
                imageView=ImageRecyclerItemView.findViewById(R.id.image_view_0);
            }
        }

        @NonNull
        @Override
        public EditImageRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EditImageRecyclerViewHolder(LayoutInflater.from(activity).inflate(R.layout.patient_document_image_show_model,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull EditImageRecyclerViewHolder holder, final int position)
        {
            if (arrayList.size()!=0)
            {
                if (!arrayList.get(position).matches(DBConst.UNKNOWN))
                {
                    Picasso.get().load(arrayList.get(position)).into(holder.imageView);

                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view)
                        {
                            DownloadImage(arrayList.get(position).toString(),DBConst.UNKNOWN);
                        }
                    });
                }
            }
        }

        public void DownloadImage(String DownloadUrl,String ImageTitle)
        {
            try {
                File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                        +"/"+ Uri.parse(ImageTitle).getLastPathSegment());
                if (file.exists())
                {
                    Uri uri= FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID,file);
                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri,"image/*");
                    PackageManager pm=getPackageManager();
                    if (intent.resolveActivity(pm)!=null)
                    {
                        startActivity(intent);
                    }
                }
                else
                {
                    new ImageDownloadUsingAysnTaskInnerClass().execute(DownloadUrl);
                }
            } catch (Exception e)
            {
                Log.d("myError",e.toString());
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }



    class ImageDownloadUsingAysnTaskInnerClass extends AsyncTask<String , Integer , Boolean>
    {
        double caldouble=0;
        private int counter=0;
        private int calculatingProgress=0;
        private int contentLength=-1;
        private static final String TAG="MyError";

        @Override
        protected void onPreExecute() {
            if (activity.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT)
            {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            else
            {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean success=false;
            String ImageUrl=strings[0];
            URL url=null;
            HttpsURLConnection connection=null;
            InputStream inputStream=null;
            File file=null;
            FileOutputStream outputStream=null;
            int read=-1;
            byte[] buffer=new byte[1024];
            try {
                url=new URL(ImageUrl);
                connection=(HttpsURLConnection) url.openConnection();
                contentLength=connection.getContentLength();
                inputStream=connection.getInputStream();
                file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+ Uri.parse(ImageUrl).getLastPathSegment());
                outputStream=new FileOutputStream(file);
                while ((read=inputStream.read(buffer))!=-1)
                {
                    outputStream.write(buffer,0,read);
                    counter=counter+read;
                    publishProgress(counter);
                }
                success=true;
            } catch (MalformedURLException e) {
                Log.d(TAG,e.toString());
            } catch (IOException e) {
                Log.d(TAG,e.toString());
            } finally {
                if (connection!=null)
                {
                    connection.disconnect();
                }
                if (inputStream!=null)
                {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.d(TAG,e.toString());
                    }
                }
                if (outputStream!=null)
                {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        Log.d(TAG,e.toString());
                    }
                }
            }
            return success;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            caldouble=(((double)values[0])/(double) contentLength)*100;
            calculatingProgress=(int)caldouble;
        }
    }
}

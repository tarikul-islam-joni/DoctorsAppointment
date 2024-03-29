package com.tarikulislamjoni95.doctorsappointment.HelperClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class MyImageGettingClass
{
    private Uri imageFilePath;
    private CircleImageView CIV;
    private ImageView IV;
    private Bitmap ImageBitmap;;

    private Activity activity;
    private MyPermissionClass myPermissionClass;
    private MyPermissionGroup myPermissionGroup;

    private GetDataFromDBInterface getDataFromDBInterface;

    public MyImageGettingClass(Activity activity)
    {
        this.activity=activity;
        myPermissionClass=new MyPermissionClass(activity);
        myPermissionGroup=new MyPermissionGroup();
        getDataFromDBInterface=(GetDataFromDBInterface)activity;
    }
    public void GetImageFromCameraOrGallery()
    {
        final CharSequence[] options = {"Capture Photo", "Select from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Upload Profile Picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item] == "Capture Photo") {
                    if (myPermissionClass.CheckAndRequestPermission(myPermissionGroup.getCameraGroup())) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                            File PhotoFile=null;
                            try {
                                PhotoFile=CreateImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (PhotoFile!=null)
                            {
                                Uri photoUri= FileProvider.getUriForFile(activity,"com.tarikulislamjoni95.doctorsappointment",PhotoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                                activity.startActivityForResult(takePictureIntent, VARConst.REQUEST_CAMERA_CODE);
                            }
                            dialog.dismiss();
                        }
                    }
                } else if (options[item] == "Select from gallery") {
                    if (myPermissionClass.CheckAndRequestPermission(myPermissionGroup.getStorageGroup())) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activity.startActivityForResult(intent, VARConst.REQUEST_GALLERY_CODE);
                        dialog.dismiss();
                    }
                } else if (options[item] == "Cancel") {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(String IVType,int IVId,int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("myError 0","Image Getting Error : "+IVType+IVId+requestCode+resultCode+data.toString());
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK)
            {
                Uri uri=result.getUri();
                if (IVType.matches(VARConst.CIV))
                {
                    CIV=activity.findViewById(IVId);
                    CIV.setImageURI(uri);
                    CIV.setEnabled(true);
                }
                else if (IVType.matches(VARConst.IV))
                {
                    IV=activity.findViewById(IVId);
                    IV.setImageURI(uri);
                    IV.setEnabled(true);
                }
                else if (IVType.matches(VARConst.NO_IMAGE_VIEW))
                {
                    Log.d("myError 1","Image Getting Error : ");
                    try
                    {
                        Log.d("myError 2","Image Getting Error : ");
                        Bitmap bmp=MediaStore.Images.Media.getBitmap(activity.getContentResolver(),uri);
                        ByteArrayOutputStream bao = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100,bao); // bmp is bitmap from user image file
                        byte[] byteArray = bao.toByteArray();
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                        hashMap.put("ImageData",byteArray);
                        getDataFromDBInterface.GetSingleDataFromDatabase("ImageData",hashMap);

                    } catch (Exception e)
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                        getDataFromDBInterface.GetSingleDataFromDatabase("ImageData",hashMap);
                        Log.d("myError","Image Getting Error : "+e.toString());
                    }
                }
            }
        }

        if (requestCode == VARConst.REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.activity(imageFilePath).start(activity);
        } else if (requestCode == VARConst.REQUEST_GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.activity(data.getData()).start(activity);
        }
    }
    byte[] ImageByteArray;
    private void SetFullImageBytesWithoutImageView(byte[] byteArray)
    {
        Log.d("myError 3","Image Getting Error : "+byteArray.toString());
        ImageByteArray=new byte[byteArray.length];
        ImageByteArray=byteArray;
    }

    public byte[] GetFullImageBytesWithoutImageView()
    {
        Log.d("myError 4","Image Getting Error : "+ImageByteArray.toString());
        return ImageByteArray;
    }

    public byte[] GetCompressImageBytes(String IVType,int IV_Id)
    {
        if (IVType.matches(VARConst.IV))
        {
            IV=activity.findViewById(IV_Id);
            ImageBitmap=((BitmapDrawable)IV.getDrawable()).getBitmap();
        }
        else if (IVType.matches(VARConst.CIV))
        {
            CIV=activity.findViewById(IV_Id);
            ImageBitmap=((BitmapDrawable)CIV.getDrawable()).getBitmap();
        }
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageBitmap.compress(Bitmap.CompressFormat.JPEG, 40,bao); // bmp is bitmap from user image file
        byte[] byteArray = bao.toByteArray();
        return byteArray;
    }


    public byte[] GetFullImageBytes(String IVType,int IV_Id)
    {
        if (IVType.matches(VARConst.IV))
        {
            IV=activity.findViewById(IV_Id);
            ImageBitmap=((BitmapDrawable)IV.getDrawable()).getBitmap();
        }
        else if (IVType.matches(VARConst.CIV))
        {
            CIV=activity.findViewById(IV_Id);
            ImageBitmap=((BitmapDrawable)CIV.getDrawable()).getBitmap();
        }
        Bitmap bmp=((BitmapDrawable)IV.getDrawable()).getBitmap();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100,bao); // bmp is bitmap from user image file
        byte[] byteArray = bao.toByteArray();
        return byteArray;
    }

    public File CreateImageFile() throws IOException
    {
        String timestamp=new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String ImageFileName="Doctors_Appointment_"+timestamp+"_";
        File Folder=new File(Environment.getExternalStorageDirectory(),"Doctors_Appointment");
        if (!Folder.exists())
        {
            Folder.mkdirs();
        }
        File storageDir=new File(Folder,ImageFileName+".jpg");
        imageFilePath=Uri.fromFile(storageDir);
        return storageDir;

    }

    public void onRequestPermissionsResult(Activity activity,int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        myPermissionClass.onRequestPermissionResult(activity,requestCode,permissions,grantResults);
    }
}

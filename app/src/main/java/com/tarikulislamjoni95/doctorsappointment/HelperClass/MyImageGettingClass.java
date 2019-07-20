package com.tarikulislamjoni95.doctorsappointment.HelperClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class MyImageGettingClass
{
    private ImageView IV;
    private Activity activity;
    private MyPermissionClass myPermissionClass;
    private MyPermissionGroup myPermissionGroup;

    public MyImageGettingClass(Activity activity)
    {
        this.activity=activity;
        myPermissionClass=new MyPermissionClass(activity);
        myPermissionGroup=new MyPermissionGroup();
    }
    public void GetImageFromCameraOrGallery(int IVId)
    {
        IV=activity.findViewById(IVId);
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
                            activity.startActivityForResult(takePictureIntent, VARConst.REQUEST_CAMERA_CODE);
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

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == VARConst.REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            IV.setImageBitmap(imageBitmap);
        } else if (requestCode == VARConst.REQUEST_GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            Uri resultUri=data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            IV.setImageURI(resultUri);
        }
    }
}

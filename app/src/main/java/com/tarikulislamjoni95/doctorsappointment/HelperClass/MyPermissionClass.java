package com.tarikulislamjoni95.doctorsappointment.HelperClass;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPermissionClass
{
    private static final int PERMISSION_CODE=111;
    private Activity activity;

    public MyPermissionClass(Activity activity)
    {
        this.activity = activity;
    }

    public boolean CheckAndRequestPermission(String... permissions)
    {
        List<String> listofPermissionNeeded=new ArrayList<>();
        for(String permission:permissions)
        {
            if (ContextCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED)
            {
                listofPermissionNeeded.add(permission);
            }
        }
        if (!listofPermissionNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(activity,listofPermissionNeeded.toArray(new String[listofPermissionNeeded.size()]),PERMISSION_CODE);
            return false;
        }
        else
        {
            return true;
        }
    }

    public void onRequestPermissionResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode==PERMISSION_CODE)
        {
            Map<String,Integer> PermissionResultMap=new HashMap<>();
            for(String permission:permissions)
            {
                PermissionResultMap.put(permission,PackageManager.PERMISSION_GRANTED);
            }
            if (grantResults.length>0)
            {
                for(int i=0; i<grantResults.length; i++)
                {
                    PermissionResultMap.put(permissions[i],grantResults[i]);
                }
            }
            boolean allpermissiongranted=true;
            for(String permission:permissions)
            {
                allpermissiongranted=allpermissiongranted&&(PermissionResultMap.get(permission)==PackageManager.PERMISSION_GRANTED);
            }

            if (!allpermissiongranted)
            {
                for (String permission:permissions)
                {
                    if (PermissionResultMap.get(permission)==PackageManager.PERMISSION_GRANTED)
                    {
                        PermissionResultMap.remove(permission);
                    }
                }
                StringBuilder message=new StringBuilder("The app has not been granted permissions:\n\n");
                for(String permission:PermissionResultMap.keySet())
                {
                    message.append(permission+"\n");
                }
                message.append("\nHence, it cannot function properly." +
                        "\nPlease consider granting it this permission in phone Settings.");
                GotoSettingDialog(message);
            }
        }
    }

    public void GotoSettingDialog(StringBuilder message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("Required Permission");
        builder.setMessage(message);
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri=Uri.fromParts("package",activity.getPackageName(),null);
                intent.setData(uri);
                (activity).startActivity(intent);
            }
        });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}

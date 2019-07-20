package com.tarikulislamjoni95.doctorsappointment.HelperClass;

import android.Manifest;

public class MyPermissionGroup
{
    String[] CameraGroup={Manifest.permission.CAMERA};
    String[] StorageGroup={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] ContactGroup={Manifest.permission.READ_CONTACTS};
    String[] LocationGroup={Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};

    public String[] getCameraGroup() {
        return CameraGroup;
    }

    public String[] getStorageGroup() {
        return StorageGroup;
    }

    public String[] getContactGroup() {
        return ContactGroup;
    }

    public String[] getLocationGroup() {
        return LocationGroup;
    }
}

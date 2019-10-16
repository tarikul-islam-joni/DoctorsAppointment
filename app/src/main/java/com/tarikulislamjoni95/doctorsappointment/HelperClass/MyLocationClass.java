package com.tarikulislamjoni95.doctorsappointment.HelperClass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetLocationInterface;

import java.util.HashMap;
import java.util.Map;

public class MyLocationClass {
    private MyPermissionClass myPermissionClass;
    private MyPermissionGroup myPermissionGroup;
    private GetLocationInterface locationInterface;
    private MyResultReceiver resultReceiver;
    private Intent intent;
    private LocationCallback locationCallback;
    private Activity activity;

    public MyLocationClass(Activity activity) {
        this.activity = activity;
        intent = new Intent(activity, MyLocationService.class);
        resultReceiver = new MyResultReceiver(null);
        locationInterface = (GetLocationInterface) activity;
        myPermissionClass=new MyPermissionClass(activity);
        myPermissionGroup=new MyPermissionGroup();
    }

    @SuppressLint("MissingPermission")
    public void GetCoOrdinateFromMaps() {
        if (myPermissionClass.CheckAndRequestPermission(myPermissionGroup.getLocationGroup()))
        {
            final FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(activity);
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(1000);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    intent.putExtra(VARConst.RECEIVER, resultReceiver);
                    intent.putExtra(VARConst.FETCH_TYPE, VARConst.TYPE_02_ADDRESS_COORDINATE);
                    intent.putExtra(VARConst.ADDRESS_LOCATION, locationResult.getLastLocation());
                    if(ServiceProtection())
                    {
                        activity.startService(intent);
                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    }
                    ///BackGround e thakle service start kora jabe na
                }

                @Override
                public void onLocationAvailability(LocationAvailability locationAvailability) {
                    Map<Integer, String> map = new HashMap<>();
                    if (!locationAvailability.isLocationAvailable()) {
                        map.put(0, "Turn on your location");
                        locationInterface.GetLocation("LocationError", map);
                    }
                    super.onLocationAvailability(locationAvailability);
                }
            };


            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }
    

    private boolean ServiceProtection()
    {
        return !(activity.isDestroyed() || activity.isFinishing());
    }

    public void GetCoOrdinateFromAddress(String AddressString)
    {
        intent.putExtra(VARConst.RECEIVER,resultReceiver);
        intent.putExtra(VARConst.FETCH_TYPE, VARConst.TYPE_01_ADDRESS_NAME);
        intent.putExtra(VARConst.ADDRESS_NAME,AddressString);
        activity.startService(intent);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        myPermissionClass.onRequestPermissionResult(activity,requestCode,permissions,grantResults);
    }

    private class MyResultReceiver extends ResultReceiver
    {
        public MyResultReceiver(Handler handler)
        {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode== VARConst.TYPE_01_ADDRESS_NAME)
            {
                Map<Integer,String> map=new HashMap<>();
                Address address=resultData.getParcelable("");
                map.put(0,address.getAddressLine(0));
                map.put(1,String.valueOf(address.getLatitude()));
                map.put(2,String.valueOf(address.getLongitude()));
                map.put(3,String.valueOf(address.getAdminArea()));
                locationInterface.GetLocation(VARConst.ADDRESS_NAME,map);
            }
            else if (resultCode== VARConst.TYPE_02_ADDRESS_COORDINATE)
            {
                Address address=resultData.getParcelable("");
                Map<Integer,String> map=new HashMap<>();
                map.put(0,address.getAddressLine(0));
                map.put(1,String.valueOf(address.getLatitude()));
                map.put(2,String.valueOf(address.getLongitude()));
                map.put(3,String.valueOf(address.getAdminArea()));
                locationInterface.GetLocation(VARConst.ADDRESS_LOCATION,map);
            }
            else if (resultCode== VARConst.TYPE_03_ERROR)
            {
                String error=resultData.getString("");
                Map<Integer,String> map=new HashMap<>();
                map.put(0,error);
                locationInterface.GetLocation("LocationError",map);
            }
        }
    }
}
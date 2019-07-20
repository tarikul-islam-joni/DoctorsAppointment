package com.tarikulislamjoni95.doctorsappointment.HelperClass;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocationService extends IntentService
{
    private ResultReceiver resultReceiver=null;
    private int FetchType;
    public MyLocationService()
    {
        super("MyLocationService");
    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent==null)
        {
            return;
        }
        else
        {
            GetGeoCoderAddress(intent);
        }
    }
    private void GetGeoCoderAddress(Intent intent)
    {
        String Error_Message;
        Bundle bundle=new Bundle();
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        resultReceiver=intent.getParcelableExtra(VARConst.RECEIVER);
        FetchType=intent.getIntExtra(VARConst.FETCH_TYPE,0);
        if (FetchType== VARConst.TYPE_01_ADDRESS_NAME)
        {
            String AddressNameString=intent.getStringExtra(VARConst.ADDRESS_NAME);
            try
            {
                addresses=geocoder.getFromLocationName(AddressNameString,1);
                if (addresses.size()!=0)
                {
                    Address address=addresses.get(0);
                    bundle.putParcelable("",address);
                    resultReceiver.send(VARConst.TYPE_01_ADDRESS_NAME,bundle);
                }
                else
                {
                    bundle.putString("","Address not found");
                    resultReceiver.send(VARConst.TYPE_03_ERROR,bundle);
                }
            }
            catch (IOException e)
            {
                bundle.putString("","Address not found");
                resultReceiver.send(VARConst.TYPE_03_ERROR,bundle);
            }

        }
        else if (FetchType== VARConst.TYPE_02_ADDRESS_COORDINATE)
        {
            Location location=intent.getParcelableExtra(VARConst.ADDRESS_LOCATION);
            try
            {
                addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                if (addresses.size()!=0)
                {
                    Address address=addresses.get(0);
                    bundle.putParcelable("",address);
                    resultReceiver.send(VARConst.TYPE_02_ADDRESS_COORDINATE,bundle);
                }
                else
                {
                    bundle.putString("","Address not found");
                    resultReceiver.send(VARConst.TYPE_03_ERROR,bundle);
                }

            }
            catch (IOException e)
            {
                bundle.putString("","Turn on your internet");
                resultReceiver.send(VARConst.TYPE_03_ERROR,bundle);
            }
            catch (IllegalArgumentException e)
            {
                bundle.putString("","3"+e.toString());
                resultReceiver.send(VARConst.TYPE_03_ERROR,bundle);
            }
        }
        else
        {
            bundle.putString("","Unknown Type");
            resultReceiver.send(VARConst.TYPE_03_ERROR,bundle);
        }
    }
}

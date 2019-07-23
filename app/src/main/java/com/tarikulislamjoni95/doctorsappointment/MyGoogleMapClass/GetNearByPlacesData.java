package com.tarikulislamjoni95.doctorsappointment.MyGoogleMapClass;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearByPlacesData extends AsyncTask<Object,String,String>
{
    String googlePlacesData;
    GoogleMap myGoogleMap;
    String url;
    @Override
    protected String doInBackground(Object... objects) {
        myGoogleMap=(GoogleMap) objects[0];
        url=(String)objects[1];
        DownloadUrl downloadUrl=new DownloadUrl();

        googlePlacesData=downloadUrl.readUrl(url);


        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        List<HashMap<String,String>> hashMapList;
        DataParser parser=new DataParser();
        hashMapList=parser.Parse(s);
        ShowNearByPlaces(hashMapList);
    }

    private void ShowNearByPlaces(List<HashMap<String,String>> hashMapList)
    {
        for(int i=0; i<hashMapList.size(); i++)
        {
            MarkerOptions markerOptions=new MarkerOptions();
            HashMap<String,String> hashMap=hashMapList.get(i);
            String place_name=hashMap.get("place_name");
            String place_vicinity=hashMap.get("place_vicinity");
            double place_latitude=Double.parseDouble(hashMap.get("place_latitude"));
            double place_longitude=Double.parseDouble(hashMap.get("place_longitude"));

            Log.d("myError","My Place "+place_name);

            LatLng latLng=new LatLng(place_latitude,place_longitude);
            markerOptions.position(latLng);
            markerOptions.title(place_name+" : "+place_vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            myGoogleMap.addMarker(markerOptions);
            myGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            myGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));



            String place_reference=hashMap.get("place_reference");


        }
    }
}

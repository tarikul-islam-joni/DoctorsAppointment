package com.tarikulislamjoni95.doctorsappointment.MyGoogleMapClass;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearByPlacesData extends AsyncTask<Object,String,String>
{
    String googlePlacesData;
    GoogleMap myGoogleMap;
    String url;
    Activity activity;
    public GetNearByPlacesData(Activity activity)
    {
        this.activity=activity;
    }
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

            final LatLng latLng=new LatLng(place_latitude,place_longitude);
            markerOptions.position(latLng);
            markerOptions.title(place_name+", "+place_vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            myGoogleMap.addMarker(markerOptions);
            //myGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //myGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));

            myGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    LatLng latLng1=marker.getPosition();//+latLng1.latitude+","+latLng1.longitude
                    Log.d("myError","Marker Cliked :: "+marker.getPosition()+" ::: "+marker.getTitle()+" ::: "+latLng.latitude+" : "+latLng.longitude);
                    try
                    {
                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+marker.getTitle());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        activity.startActivity(mapIntent);

                    }catch (ActivityNotFoundException e)
                    {
                        String url = "https://www.google.com/maps/dir/?api=1&destination=" + marker.getTitle() + "&travelmode=driving";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        activity.startActivity(intent);
                    }


                    return false;
                }
            });



            String place_reference=hashMap.get("place_reference");


        }
    }

}

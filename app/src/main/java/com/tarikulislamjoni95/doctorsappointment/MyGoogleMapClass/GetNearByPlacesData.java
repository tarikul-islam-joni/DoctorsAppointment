package com.tarikulislamjoni95.doctorsappointment.MyGoogleMapClass;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetNearByPlacesData extends AsyncTask<Object,String,String>
{
    ArrayAdapter<String> arrayAdapter;
    Marker marker;
    AlertDialog dialog;
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

    private ArrayList<String> arrayList;
    private void ShowNearByPlaces(List<HashMap<String,String>> hashMapList)
    {
        arrayList=new ArrayList<>();
        for(int i=0; i<hashMapList.size(); i++)
        {
            arrayList.add(hashMapList.get(i).get("place_name"));
            MarkerOptions markerOptions=new MarkerOptions();
            HashMap<String,String> hashMap=hashMapList.get(i);
            final String place_name=hashMap.get("place_name");
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
                public boolean onMarkerClick(Marker amarker) {
                    marker=amarker;
                    LatLng latLng1=marker.getPosition();//+latLng1.latitude+","+latLng1.longitude
                    Log.d("myError","Marker Cliked :: "+marker.getPosition()+" ::: "+marker.getTitle()+" ::: "+latLng.latitude+" : "+latLng.longitude);

                    AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                    builder.setTitle("Hospital Info");
                    builder.setMessage(marker.getTitle());
                    builder.setPositiveButton("Get Direction", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            dialog.dismiss();
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
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            dialog.dismiss();
                        }
                    });
                    dialog=builder.create();
                    dialog.show();

                    return false;
                }
            });



            String place_reference=hashMap.get("place_reference");


        }
    }

    public void ShowAlertHospitalListView(String Places)
    {
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(activity,android.R.layout.simple_list_item_1,arrayList);
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        if (Places.matches(DBConst.UNKNOWN))
        {
            builder.setTitle("Hospital List In Current Area");
        }
        else
        {
            builder.setTitle("Hospital List : "+Places);
        }
        builder.setSingleChoiceItems(arrayAdapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                try
                {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+arrayList.get(i));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    activity.startActivity(mapIntent);

                }catch (ActivityNotFoundException e)
                {
                    String url = "https://www.google.com/maps/dir/?api=1&destination=" + arrayList.get(i) + "&travelmode=driving";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    activity.startActivity(intent);
                }
            }
        });
        builder.create().show();
    }

}

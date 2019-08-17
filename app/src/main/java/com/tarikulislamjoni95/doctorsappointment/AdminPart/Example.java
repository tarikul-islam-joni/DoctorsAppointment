package com.tarikulislamjoni95.doctorsappointment.AdminPart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyImageGettingClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyLocationClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetLocationInterface;
import com.tarikulislamjoni95.doctorsappointment.MyGoogleMapClass.DownloadUrl;
import com.tarikulislamjoni95.doctorsappointment.MyGoogleMapClass.GetNearByPlacesData;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Example extends AppCompatActivity implements OnMapReadyCallback {
    LocationCallback locationCallback;

    EditText SearchEt;
    Button SearchBtn;
    GoogleMap googleMap;
    private Activity activity;

    private AutocompleteSupportFragment autocompleteFragment;
    private PlacesClient placesClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        activity = Example.this;

        // Initialize the SDK
        //Uttara Cresent Hospital
        Places.initialize(getApplicationContext(), "AIzaSyAqdSjpPp_Z3rYwQkxTMqwubdPNDpu7Oi8");
        placesClient = Places.createClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        SearchEt = findViewById(R.id.search_et);
        SearchBtn = findViewById(R.id.search_btn);
        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] search_Item = (SearchEt.getText().toString().split(" ", 0));
                StringBuilder Url = new StringBuilder();
                Url.append("https://maps.googleapis.com/maps/api/place/textsearch/json?query=");
                //https://maps.googleapis.com/maps/api/place/textsearch/json?query=in+BD&key=AIzaSyAqdSjpPp_Z3rYwQkxTMqwubdPNDpu7Oi8
                for (int i = 0; i < search_Item.length; i++) {
                    Url.append(search_Item[i]);
                    if (i != (search_Item.length - 1)) {
                        Url.append("+");
                    }
                }
                Url.append("&key=AIzaSyAqdSjpPp_Z3rYwQkxTMqwubdPNDpu7Oi8");
                Log.d("myError", "Url ::: " + Url.toString());
                GetNearByPlacesData getNearByPlacesData = new GetNearByPlacesData(activity);
                Object[] objects = new Object[2];
                objects[0] = googleMap;
                objects[1] = Url.toString();
                getNearByPlacesData.execute(objects);
            }
        });

        GetNearByHospital();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @SuppressLint("MissingPermission")
    private void GetNearByHospital()
    {
        final FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(this);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                String hospital = "hospital";
                String url = getUrl(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), hospital);
                GetNearByPlacesData getNearByPlacesData = new GetNearByPlacesData(activity);
                Object[] objects = new Object[2];
                objects[0] = googleMap;
                objects[1] = url.toString();
                getNearByPlacesData.execute(objects);

                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");

        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=restaurant&keyword=cruise&key=AIzaSyAqdSjpPp_Z3rYwQkxTMqwubdPNDpu7Oi8
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+5000);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyDcFX0rB6a2sJFpF29pl438mrjPNGF3RXg");
        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());
        return googlePlaceUrl.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleMap.clear();
    }
}

package com.simbazet.mbizvo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.simbazet.mbizvo.directionhelpers.FetchURL;
import com.simbazet.mbizvo.directionhelpers.TaskLoadedCallback;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    Button getDirection;
    private Polyline currentPolyline;
    private String faultLat, faultLong, faultTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        Intent mapsInt = getIntent();
        if (mapsInt.hasExtra("requestLat") ||
                mapsInt.hasExtra("requestLong") ||
                mapsInt.hasExtra("requestTitle")){

            faultLat = mapsInt.getStringExtra("requestLat");
            faultLong = mapsInt.getStringExtra("requestLong");
            faultTitle = mapsInt.getStringExtra("requestTitle");

            place1 = new MarkerOptions().position(new LatLng(Double.parseDouble(faultLat),Double.parseDouble(faultLong))).title(faultTitle);
        }
        else{
            Toast.makeText(this, "Populating sites please wait", Toast.LENGTH_SHORT).show();
        }


        getDirection = findViewById(R.id.btnGetDirection);
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(MapsActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
            }
        });
        //27.658143,85.3199503
        //27.667491,85.3208583

        //place2 = new MarkerOptions().position(new LatLng(-19.4956427,29.8350216)).title("chirandu");
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");

        mMap.addMarker(new MarkerOptions().position(new
                LatLng(-19.5030686,29.8341633))
                .title("Senga shops"));
        if (place1 != null){ mMap.addMarker(place1);} //If action was from Requests fragment
        else{
            for(SitesData sd : MainActivity.sitesDataList){
                mMap.addMarker(new MarkerOptions().position(new
                        LatLng(Double.parseDouble(sd.getMapLat()),
                        Double.parseDouble(sd.getMapLong())))
                        .title(sd.getMapTitle()));
            }
        }

        //mMap.addMarker(place2);
        //LatLng msu = new LatLng(-19.468371,29.8100322);
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(msu));
        googleMap.moveCamera(CameraUpdateFactory.zoomBy(3));
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }


}

package com.casa.myapplication.Model;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.casa.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity/*FragmentActivity*/ implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker;
    private double lat, log;
    private Button mTerrain, mSatellite;

    //int permissionCheck = ContextCompat.checkSelfPermission(MapsActivity.this , Manifest.permission.WRITE_CALENDAR);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mTerrain = (Button) findViewById(R.id.button_terrain);
        mSatellite = (Button) findViewById(R.id.button_satellite);

        //Add back buttons on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //Establece si incluir la aplicación home en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Establece si el home se mopstrará como un UP

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //add zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);


        //Put normal view on map when this button is pressed
        mTerrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(mMap.MAP_TYPE_NORMAL);
            }
        });

        //Put sattelite view on map when this button is pressed
        mSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
            }
        });

        onMarkersRetrieved();
        getDeviceLocation();

    }


    public void onMarkersRetrieved(){
        LatLng cementval = new LatLng(39.646326, -0.227188);
        mMap.addMarker(new MarkerOptions().position(cementval).title("Cementval"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cementval));

    }


    private void getDeviceLocation() {


    }


    //Close actual activity when back button is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

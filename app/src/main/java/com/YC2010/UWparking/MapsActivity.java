package com.YC2010.UWparking;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton myFab = (FloatingActionButton)  findViewById(R.id.refreshFAB);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("MapsActivity", "FAB got pressed");
                // Show Toast
                MainActivity.getFAB().callOnClick();
                ArrayList<ParkingLot> mParkingLots = getParkingFromPref();
                addMarkers(mParkingLots);
//                Toast.makeText(getApplicationContext(), "Data Updated", Toast.LENGTH_LONG).show();
        }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add marker for UWaterloo and move the camera
        LatLng uWaterloo = new LatLng(43.4714, -80.5431);
        //mMap.addMarker(new MarkerOptions().position(uWaterloo).title("uWaterloo"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(uWaterloo));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.1f));

        // TODO: somehow show last update time, snack bar or toast
        // and also add fab and bottom sheet if possible
        // current point: use a whatever layout to contain map and also my fab

        enableMyLocation();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                return true;
            }
        });

        // set map padding due to translucent status and nivigation bar
        int navigationBarId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        int navigationBarHeight = 150;
        if (navigationBarId > 0) {
            navigationBarHeight = getResources().getDimensionPixelSize(navigationBarId);
        }
        int statusBarId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = 72;
        if (statusBarId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(statusBarId);
        }
        mMap.setPadding(0, statusBarHeight, 0, navigationBarHeight);

        // Add Markers for Lots
        ArrayList<ParkingLot> mParkingLots = getParkingFromPref();
        addMarkers(mParkingLots);
    }

    public void addMarkers(ArrayList<ParkingLot> mParkingLots){
        mMap.clear();
        for (ParkingLot parkingLot : mParkingLots){
            LatLng location = parkingLot.getLocation();
            IconGenerator iconGenerator = new IconGenerator(this);
            iconGenerator.setTextAppearance(R.style.NormalIconTextStyle);
            if (parkingLot.getCurrentCount() * 100 / parkingLot.getCapacity() >= 90){
                iconGenerator.setColor(ContextCompat.getColor(getApplicationContext(), R.color.specialIconBackGround));
            }
            else{
                iconGenerator.setColor(ContextCompat.getColor(getApplicationContext(), R.color.normalIconBackGround));
            }

            if (parkingLot.getName().equals("N")){
                iconGenerator.setContentRotation(-90);
                Bitmap lotXBitmap = iconGenerator.makeIcon("Lot " + parkingLot.getName() + ", " +
                        parkingLot.getCurrentCount() * 100 / parkingLot.getCapacity() + "%");
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(lotXBitmap))
                        .position(location)
                        .rotation(90));
            }
            else{
                Bitmap lotXBitmap = iconGenerator.makeIcon("Lot " + parkingLot.getName() + ", " +
                        parkingLot.getCurrentCount() * 100 / parkingLot.getCapacity() + "%");
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(lotXBitmap))
                        .position(location));
            }
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            enableMyLocation();
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    public ArrayList<ParkingLot> getParkingFromPref(){
        SharedPreferences mPrefs = getSharedPreferences("Parking Pref", MODE_PRIVATE);
        int lotLength = mPrefs.getInt("lot_num", 0);
        ArrayList<ParkingLot> mParkingLots = new ArrayList<>();
        for (int i = 0; i < lotLength; i++) {
            String mParkingJSONString = mPrefs.getString("LOT_" + i, "");
            if (!mParkingJSONString.equals("")){
                try {
                    JSONObject mParkingJSON = new JSONObject(mParkingJSONString);
                    mParkingLots.add(new ParkingLot(mParkingJSON.getString("lot_name"),
                            Integer.parseInt(mParkingJSON.getString("capacity")),
                            Integer.parseInt(mParkingJSON.getString("capacity")) - Integer.parseInt(mParkingJSON.getString("current_count")),
                            new LatLng(Float.parseFloat(mParkingJSON.getString("latitude")), Float.parseFloat(mParkingJSON.getString("longitude")))));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return mParkingLots;
    }
}

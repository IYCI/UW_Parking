package com.YC2010.UWparking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // set up toolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.toolBar_title);
            toolbar.setTitleTextColor(Color.BLACK);
            setSupportActionBar(toolbar);
        }

        // set up FAB
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setBackgroundColor(Color.BLACK);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParkingLotsFetchTask parkingLotsFetchTask = new ParkingLotsFetchTask(MainActivity.this, new AsyncTaskCallbackInterface() {
                        @Override
                        public void onOperationComplete(Bundle bundle) {
                            ListView mListView = (ListView) findViewById(R.id.lotListView);
                            if (mListView != null) {
                                ParkingLotAdpater mAdapter = (ParkingLotAdpater) mListView.getAdapter();
                                mAdapter.updateView();
                            }
                        }
                    });
                    parkingLotsFetchTask.execute();
                }
            });
        }

        // set up map button
        Button mapButton = (Button) findViewById(R.id.viewMapButton);
        if (mapButton != null) {
            mapButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openMap(view);
                }
            });
        }

        // put sample data into Bundle
        Bundle sampleDataBundle = new Bundle();
        ArrayList<ParkingLot> mParkingLots = getParkingFromPref();
        if (mParkingLots.size() == 0 && fab != null){
            fab.callOnClick();
        }
        sampleDataBundle.putParcelableArrayList("LOT_LIST", mParkingLots);

        // set adapter
        ListView mListView = (ListView) findViewById(R.id.lotListView);
        if (mListView != null) {
            mListView.setAdapter(new ParkingLotAdpater(getApplicationContext(), sampleDataBundle));
        }
    }

    public void openMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
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

    public static FloatingActionButton getFAB(){
        return fab;
    }

}

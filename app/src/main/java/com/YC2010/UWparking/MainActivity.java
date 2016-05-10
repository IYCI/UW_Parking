package com.YC2010.UWparking;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
                    updateParkingLot();
                }
            });
        }

        // set up map button
        Button mapButton = (Button) findViewById(R.id.viewMapButton);
        if (mapButton != null) {
            ((AppCompatButton)mapButton).setSupportBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openMap(view);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateParkingLot();

        // set adapter
        ListView mListView = (ListView) findViewById(R.id.lotListView);
        if (mListView != null) {
            mListView.setAdapter(new ParkingLotAdpater(getApplicationContext()));
        }
    }

    public void openMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public static FloatingActionButton getFAB(){
        return fab;
    }

    public void updateParkingLot(){
        ParkingLotsFetchTask parkingLotsFetchTask = new ParkingLotsFetchTask(MainActivity.this, new AsyncTaskCallbackInterface() {
            @Override
            public void onOperationComplete(Bundle bundle) {
                ListView mListView = (ListView) findViewById(R.id.lotListView);
                if (mListView != null) {
                    ParkingLotAdpater mAdapter = (ParkingLotAdpater) mListView.getAdapter();
                    mAdapter.updateView();

                    TextView mTimeText = (TextView) findViewById(R.id.lastUpdateText);
                    if (mTimeText != null){
                        mTimeText.setText("Last Refresh: " + Utils.getUpdateDiscription(getApplicationContext()));
                    }
                }
                if (!bundle.getBoolean("valid_return")) {
                    Toast.makeText(getApplicationContext(), "Something went wrong, check your network condition", Toast.LENGTH_SHORT).show();
                }
            }
        });
        parkingLotsFetchTask.execute();
    }

}

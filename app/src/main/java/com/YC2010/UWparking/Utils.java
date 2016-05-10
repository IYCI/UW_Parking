package com.YC2010.UWparking;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jason on 3/22/2016.
 */
public class Utils {
    public static ArrayList<ParkingLot> getParkingFromPref(Context mContext){
        SharedPreferences mPrefs = mContext.getSharedPreferences("Parking Pref", Context.MODE_PRIVATE);
        int lotLength = mPrefs.getInt("lot_num", 0);
        ArrayList<ParkingLot> mParkingLots = new ArrayList<>();
        for (int i = 0; i < lotLength; i++) {
            String mParkingJSONString = mPrefs.getString("LOT_" + i, "");
            if (!mParkingJSONString.equals("")){
                try {
                    JSONObject mParkingJSON = new JSONObject(mParkingJSONString);
                    mParkingLots.add(new ParkingLot(mParkingJSON.getString("lot_name"), Constants.parkingLotLocMap.get(mParkingJSON.getString("lot_name")),
                            Integer.parseInt(mParkingJSON.getString("capacity")),
                            Integer.parseInt(mParkingJSON.getString("capacity")) - Integer.parseInt(mParkingJSON.getString("current_count")),
                            new LatLng(Float.parseFloat(mParkingJSON.getString("latitude")), Float.parseFloat(mParkingJSON.getString("longitude"))),
                            mParkingJSON.getString("last_updated")));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return mParkingLots;
    }

    public static String getUpdateDiscription(Context mContext){
        SharedPreferences mPrefs = mContext.getSharedPreferences("Parking Pref", Context.MODE_PRIVATE);
        Date updateTime = new Date(mPrefs.getLong("last_get_time", (new Date()).getTime()));
        long diffSeconds = ((new Date()).getTime() - updateTime.getTime()) / 1000;
        if (diffSeconds < 60) {
            return "a few seconds ago";
        }
        else if (diffSeconds / 60 < 60){
            return (diffSeconds / 60) + " minutes ago";
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd HH:mm");
            return sdf.format(updateTime);
        }
    }

}

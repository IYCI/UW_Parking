package com.YC2010.UWparking;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by Jason on 2015/6/27.
 */
public class ParkingLotsFetchTask extends AsyncTask<List<String>, Void, Bundle> {

    private Activity mActivity;
    private AsyncTaskCallbackInterface mAsyncTaskCallbackInterface;

    public ParkingLotsFetchTask(Activity activity, AsyncTaskCallbackInterface asyncTaskCallbackInterface) {
        this.mActivity = activity;
        mAsyncTaskCallbackInterface = asyncTaskCallbackInterface;
    }
    ProgressBar progDailog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = (ProgressBar) mActivity.findViewById(R.id.updateProgressBar);
        if (progDailog != null) {
            progDailog.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected Bundle doInBackground(List<String>...params) {
        Bundle result = new Bundle();
        fetchParkingLots(result);
        return result;
    }

    private Bundle fetchParkingLots(Bundle bundle) {
        try{
            bundle.putBoolean("valid_return", false);

            // get exam JSONBObject
            String exam_url = Connections.getParkingURL();
            JSONObject examObject = Connections.getJSON_from_url(exam_url);

            // check valid data return
            if (examObject == null || !examObject.getJSONObject("meta").getString("message").equals("Request successful")) {
                Log.d("FinalsFetchTask",examObject.toString());
                return bundle;
            }
            bundle.putBoolean("valid_return", true);
            JSONArray parkingData = examObject.getJSONArray("data");
            SharedPreferences mPrefs = mActivity.getSharedPreferences("Parking Pref", mActivity.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.putInt("lot_num", parkingData.length());
            prefsEditor.putLong("last_get_time", (new Date()).getTime());
            for (int i = 0; i < parkingData.length(); i++){
                JSONObject parkingLotJSON = parkingData.getJSONObject(i);
                prefsEditor.putString("LOT_" + i, parkingLotJSON.toString());
            }
            prefsEditor.commit();
            return bundle;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bundle;
    }

    @Override
    protected void onPostExecute(final Bundle bundle) {
        if (progDailog != null) {
            progDailog.setVisibility(View.INVISIBLE);
        }
        mAsyncTaskCallbackInterface.onOperationComplete(bundle);
    }

}

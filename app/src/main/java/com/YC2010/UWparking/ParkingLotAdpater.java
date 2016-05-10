package com.YC2010.UWparking;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jason on 3/20/2016.
 */
public class ParkingLotAdpater extends BaseAdapter {
    ArrayList<ParkingLot> mParkingLots;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public ParkingLotAdpater(Context context){
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        updateView();
    }

    @Override
    public int getCount() {
        return mParkingLots != null ? mParkingLots.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mParkingLots.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.parking_lot_layout, null);

        // progress bar
        ProgressBar mProgress = (ProgressBar)  v.findViewById(R.id.progressBar);
        int newProgress = (mParkingLots.get(position).getCapacity() - mParkingLots.get(position).getAvailability()) * 100 /
                mParkingLots.get(position).getCapacity();
        Log.d("ParkingLotAdpater", "new Progress is " + newProgress);
        mProgress.setProgress(newProgress);
        mProgress.setScaleY(2f);
        mProgress.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.progressBarForeground)));

        // text views
        TextView titleTextView = (TextView)  v.findViewById(R.id.parkingLotTitleText);
        titleTextView.setText(mParkingLots.get(position).getName());

        TextView descriptionTextView = (TextView)  v.findViewById(R.id.parkingLotDiscText);
        descriptionTextView.setText("(" + mParkingLots.get(position).getConventionalLocation() + ")");

        TextView middleTextView = (TextView)  v.findViewById(R.id.parkingLotMiddleText);
        middleTextView.setText(mParkingLots.get(position).getCapacity() - mParkingLots.get(position).getAvailability() +
                "/" + mParkingLots.get(position).getCapacity());

        TextView rightTextView = (TextView)  v.findViewById(R.id.parkingLotRightText);
        rightTextView.setText(newProgress + "%");

        return v;
    }

    public void updateView() {
        // update data set
        mParkingLots = Utils.getParkingFromPref(mContext);
        notifyDataSetChanged();
    }
}

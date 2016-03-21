package com.YC2010.UWparking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.GregorianCalendar;

/**
 * Created by jason on 3/20/2016.
 */
public class ParkingLot implements Parcelable {
    private String name;
    private int capacity;
    private int availability;
    private LatLng location;

    public ParkingLot(String name, int capacity, int availability, LatLng location) {
        this.name = name;
        this.capacity = capacity;
        this.availability = availability;
        this.location = location;

    }

    public ParkingLot(Parcel src) {
        this.name = src.readString();
        this.capacity = src.readInt();
        this.availability = src.readInt();
        this.location = (LatLng)src.readValue(LatLng.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(capacity);
        dest.writeInt(availability);
        dest.writeValue(location);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        capacity = capacity;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getCurrentCount(){
        return capacity - availability;
    }
}

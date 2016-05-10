package com.YC2010.UWparking;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jason on 3/20/2016.
 */
public class ParkingLot {
    private String name;
    private String conventionalLocation;
    private int capacity;
    private int availability;
    private LatLng location;
    private Date lastUpdateTime;

    public ParkingLot(String name, String cName, int capacity, int availability, LatLng location, String time) {
        this.name = name;
        this.conventionalLocation = cName;
        this.capacity = capacity;
        this.availability = availability;
        this.location = location;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sszzzzz");
        try {
            lastUpdateTime= sdf.parse(time);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getConventionalLocation() {
        return conventionalLocation;
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

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }
}

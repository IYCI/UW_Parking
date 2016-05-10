package com.YC2010.UWparking;

import java.util.HashMap;

/**
 * Created by Jason on 2016/3/20.
 */

public class Constants {
    public static String UWAPIROOT = "https://api.uwaterloo.ca/v2/";

    public static HashMap<String, String> parkingLotLocMap;
    static {
        parkingLotLocMap = new HashMap<>();
        parkingLotLocMap.put("C", "SCH");
        parkingLotLocMap.put("X", "OPT");
        parkingLotLocMap.put("N", "BMH");
        parkingLotLocMap.put("W", "CIF");
    }
}

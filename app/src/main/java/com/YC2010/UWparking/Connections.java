package com.YC2010.UWparking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jason on 2016/3/20
 */
public class Connections {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getParkingURL() {
        return Constants.UWAPIROOT + "parking/watpark" + ".json" + URLEnding();
    }

    public static JSONObject getJSON_from_url(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        JSONObject jsonObject;

        try {
            Log.d("getJSONObject", "URL is " + url);

            int responseCode = connection.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("Failed: HTTP error code: " + responseCode);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

            String inputLine;
            StringBuilder entityStringBuilder = new StringBuilder();

            while (null != (inputLine = in.readLine())) {
                entityStringBuilder.append(inputLine).append("\n");
            }
            in.close();

            jsonObject = new JSONObject(entityStringBuilder.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }

    public static String URLEnding() {
        return "?key=" + Tools.YOUNEEDTHIS;
    }
}

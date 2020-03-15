package com.example.monitoringurbanproblems.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

public class MyLocationListener implements LocationListener {

    public static Location imHere; // здесь будет всегда доступна самая последняя информация о местоположении пользователя.
    public static double l_latitude=20, l_longitude=20;

    @SuppressLint("MissingPermission")
    public static void SetUpLocationListener(Context context) // это нужно запустить в самом начале работы программы
    {
        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10000,
                0,
                locationListener); // здесь можно указать другие более подходящие вам параметры

        while (imHere== null) {
            Log.e("TAG", "No Location yet");
            imHere = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        l_latitude = imHere.getLatitude();
        l_longitude = imHere.getLongitude();
        Log.e("Location: " , l_latitude+ " "+ l_longitude);
    }

    @Override
    public void onLocationChanged(Location loc) {
        if (loc != null) {
            imHere = loc;
            l_latitude = loc.getLatitude();
            l_longitude = loc.getLongitude();
        }
    }
    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public static double getLatitude() {
        return l_latitude;
    }

    public static double getLongitude() {
        return l_longitude;
    }
}

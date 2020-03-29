package com.example.monitoringurbanproblems;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.firebase.database.annotations.Nullable;

import java.io.IOException;
import java.util.Locale;

public class Problem {
    private String name = "";
    private double id = 0;
    private double latitude = 0;
    private double longitude = 0;
    private String user_id = "";
    private String description;
    /*
    1 - не просмотрена
    2 - просмотрена
    3 - утверждена
    4 - решена
     */
    private int status;
    private String img_url;

    public Problem(){}

    public Problem(double latitude, double longitude, String user_id, String description,
                      String img_url, double id, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.user_id = user_id;
        this.description = description;
        this.img_url = img_url;
        this.id = id;
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public void addImage(Context cur_content, User cur_user) {

    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public static String getStatus(int State){

        if (State == 1){
            return "На рассмотрении";
        }
        else if (State == 2){
            return "Просмотрено";
        }
        else if (State == 3){
            return "Проверена";
        }
        else if (State == 4){
            return "Решена";
        }else {
            return "Ошибка";
        }
    }

    public String getAddressForLocation(Context context, double lat, double lon) throws IOException {
        Geocoder gc = new Geocoder(context, Locale.getDefault());
        return gc.getFromLocation(lat, lon, 1).get(0).getAddressLine(0);
    }

}

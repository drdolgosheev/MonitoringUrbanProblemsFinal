package com.example.monitoringurbanproblems;

import android.content.Context;
import android.location.Geocoder;

import java.io.IOException;
import java.util.Locale;

public class Problem {
    private String name = "";
    private int id = 0;
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
    private String moder_mail = "";
    private String moder_coment = "";

    public Problem(){}

    public Problem(double latitude, double longitude, String user_id, String description,
                      String img_url, int id, String name, String moder_mail, String moder_coment) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.user_id = user_id;
        this.description = description;
        this.img_url = img_url;
        this.id = id;
        this.name = name;
        this.moder_mail = moder_mail;
        this.moder_coment = moder_coment;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
            return "Проверена";
        }
        else if (State == 3){
            return "Проверена";
        }
        else if (State == 4){
            return "Решена";
        }else {
            return "Отклоненно модератором";
        }
    }

    public String getAddressForLocation(Context context, double lat, double lon) throws IOException {
        Geocoder gc = new Geocoder(context, Locale.getDefault());
        return gc.getFromLocation(lat, lon, 1).get(0).getAddressLine(0);
    }

    public String getModer_mail() {
        return moder_mail;
    }

    public void setModer_mail(String moder_mail) {
        this.moder_mail = moder_mail;
    }

    public String getModer_coment() {
        return moder_coment;
    }

    public void setModer_coment(String moder_coment) {
        this.moder_coment = moder_coment;
    }
}

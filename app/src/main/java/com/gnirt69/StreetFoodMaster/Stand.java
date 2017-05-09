package com.gnirt69.StreetFoodMaster;

import java.util.Date;
import java.util.UUID;

/**
 * Created by kyle on 4/12/17.
 */

public class Stand {

    private int mId;
    private String mName;
    private Double mLat;
    private Double mLng;
    private String mAddress;
    private String mCity;
    private String mState;
    private Integer mZip;
    private String mFoodtype;

    public Stand() {mId = -1; }

    public Stand(int id) {

        mId = id;
    }

    public Stand(int id, String name, double lat, double lng, String address, String city,
                 String state, int zip, String foodType){
        mId = id;
        mName = name;
        mLat = lat;
        mLng = lng;
        mAddress = address;
        mCity = city;
        mState = state;
        mZip = zip;
        mFoodtype = foodType;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public Double getLat() {
        return mLat;
    }

    public void setLat(Double lat) {
        mLat = lat;
    }

    public Double getLng() {
        return mLng;
    }

    public void setLng(Double lng) {
        mLng = lng;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public Integer getZip() {
        return mZip;
    }

    public void setZip(Integer zip) {
        mZip = zip;
    }

    public String getFoodtype() {
        return mFoodtype;
    }

    public void setFoodtype(String foodtype) {
        mFoodtype = foodtype;
    }

}

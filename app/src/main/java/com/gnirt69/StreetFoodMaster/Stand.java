package com.gnirt69.StreetFoodMaster;

import java.util.Date;
import java.util.UUID;

/**
 * Created by kyle on 4/12/17.
 */

public class Stand {

    private UUID mId;
    private Date mDate;
    private Double mLat;
    private Double mLng;
    private String mAddress;
    private String mCity;
    private String mState;
    private Integer mZip;
    private String mFoodtype;

    public Stand() { this(UUID.randomUUID()); }

    public Stand(UUID id) {

        mId = id;
        mDate = new Date();
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

    public UUID getId() {
        return mId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
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

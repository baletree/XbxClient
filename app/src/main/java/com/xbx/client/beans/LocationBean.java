package com.xbx.client.beans;

public class LocationBean {

    private String lon;
    private String lat;
    private String city;
    private String markAddress;
    private String detailAddress;

    public String getMarkAddress() {
        return markAddress;
    }

    public void setMarkAddress(String markAddress) {
        this.markAddress = markAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}

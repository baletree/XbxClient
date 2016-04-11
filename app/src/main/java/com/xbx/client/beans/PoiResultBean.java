package com.xbx.client.beans;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by EricYuan on 2016/4/11.
 */
public class PoiResultBean {
    private String poiKey;
    private String poiAddress;
    private LatLng poiLatlng;

    public String getPoiKey() {
        return poiKey;
    }

    public void setPoiKey(String poiKey) {
        this.poiKey = poiKey;
    }

    public String getPoiAddress() {
        return poiAddress;
    }

    public void setPoiAddress(String poiAddress) {
        this.poiAddress = poiAddress;
    }

    public LatLng getPoiLatlng() {
        return poiLatlng;
    }

    public void setPoiLatlng(LatLng poiLatlng) {
        this.poiLatlng = poiLatlng;
    }
}

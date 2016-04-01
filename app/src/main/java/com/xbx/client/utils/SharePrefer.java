package com.xbx.client.utils;

import android.content.Context;

import com.xbx.client.beans.LocationBean;

/**
 * Created by EricYuan on 2016/3/30.
 */
public class SharePrefer {

    public static void saveLocate(Context context, LocationBean locationBean) {
        SpHelper spHelper = new SpHelper(context, Constant.SPLOC_NAME);
        if (spHelper == null)
            return;
        spHelper.setSP("loc_lon", locationBean.getLon()); // 经度
        spHelper.setSP("loc_lat", locationBean.getLat()); // 经度
        spHelper.setSP("loc_city", locationBean.getCity()); // 城市
    }

    public static LocationBean getLocate(Context context) {
        LocationBean locationBean = new LocationBean();
        SpHelper spHelper = new SpHelper(context, Constant.SPLOC_NAME);
        if (spHelper == null)
            return null;
        locationBean.setLon(spHelper.getSP("loc_lon"));
        locationBean.setLat(spHelper.getSP("loc_lat"));
        locationBean.setCity(spHelper.getSP("loc_city"));

        return locationBean;
    }
}

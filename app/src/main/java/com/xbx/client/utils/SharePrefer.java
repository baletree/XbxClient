package com.xbx.client.utils;

import android.content.Context;

import com.xbx.client.beans.LocationBean;
import com.xbx.client.beans.UserInfo;

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

    public static void savePhone(Context context, String phone){
        SpHelper spHelper = new SpHelper(context, Constant.SPUSER_PHONE);
        if (spHelper == null)
            return;
        spHelper.setSP("phone", phone); //用户的手机号码
    }

    public static String getUserPhone(Context context){
        String phone = "";
        SpHelper spHelper = new SpHelper(context, Constant.SPUSER_PHONE);
        if (spHelper == null)
            return null;
        phone = spHelper.getSP("phone");
        return phone;
    }

    public static void saveUserInfo(Context context,UserInfo userInfo){
        SpHelper spHelper = new SpHelper(context, Constant.SPUSER_INFO);
        if (spHelper == null)
            return;
        spHelper.setSP("userUid", userInfo.getUid());
        spHelper.setSP("userPhone", userInfo.getUserPhone());
        spHelper.setSP("userNickname", userInfo.getNickName());
        spHelper.setSP("userHead", userInfo.getUserHead());
        spHelper.setSP("userLoginToken", userInfo.getLoginToken());
    }

    public static UserInfo getUserInfo(Context context){
        UserInfo userInfo = new UserInfo();
        SpHelper spHelper = new SpHelper(context, Constant.SPUSER_INFO);
        if(spHelper == null)
            return null;
        userInfo.setUid(spHelper.getSP("userUid"));
        userInfo.setUserPhone(spHelper.getSP("userPhone"));
        userInfo.setNickName(spHelper.getSP("userNickname"));
        userInfo.setUserHead(spHelper.getSP("userHead"));
        userInfo.setLoginToken(spHelper.getSP("userLoginToken"));
        return userInfo;
    }

    public static void savePhoneId(Context context,String phoneId){
        SpHelper spHelper = new SpHelper(context, Constant.SPPHONE_ID);
        if(spHelper == null)
            return;
        spHelper.setSP("phoneId", phoneId);
    }

    public static String getPhoneId(Context context){
        SpHelper spHelper = new SpHelper(context, Constant.SPPHONE_ID);
        if (spHelper == null)
            return "";
        return spHelper.getSP("phoneId");
    }
}

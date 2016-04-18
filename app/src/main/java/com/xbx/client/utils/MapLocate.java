package com.xbx.client.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xbx.client.beans.LocationBean;

/**
 * Created by EricYuan on 2016/3/30.
 * 地图定位功能
 */
public class MapLocate {
    private Context context;
    public LocationClient mLocationClient;
    public MyLocationListener myListener;
    private boolean isFirstLoc = true; // 是否首次定位

    public MapLocate(Context context){
        this.context = context;
        initLocate();
    }

    /**初始化地图信息*/
    private void initLocate() {
        mLocationClient = new LocationClient(context);
        myListener = new MyLocationListener();
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll");
        option.setScanSpan(15 * 1000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        startLocate();
    }
    /**开启定位*/
    public void startLocate(){
        if (mLocationClient != null) {
            isFirstLoc = true;
            mLocationClient.registerLocationListener(myListener);
            mLocationClient.start();
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                if (isFirstLoc) {
                    Util.pLog("XbxLocate：" + location.getLatitude() + " " + location.getLongitude()
                            + "" + location.getCity() + location.getAddrStr()+location.getCityCode());
                    isFirstLoc = false;
                    LocationBean lB = new LocationBean();
                    lB.setCity(location.getCity());
                    lB.setDetailAddress(location.getAddrStr());
                    lB.setLat("" + location.getLatitude());
                    lB.setLon("" + location.getLongitude());
                    SharePrefer.saveLocate(context,lB);
                }
                stopLocate();
            }else {
                //定位失败
                Util.pLog("定位失败");
            }
        }
    }

    /**停止定位*/
    public void stopLocate() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            Util.pLog("stop定位方法"+System.currentTimeMillis());
            mLocationClient.unRegisterLocationListener(myListener);
            mLocationClient.stop();
            isFirstLoc = false;
        }
    }
}

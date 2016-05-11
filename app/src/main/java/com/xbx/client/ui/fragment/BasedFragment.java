package com.xbx.client.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.utdid2.android.utils.SystemUtils;
import com.xbx.client.R;
import com.xbx.client.db.DBOpere;
import com.xbx.client.linsener.ImageLoaderConfigFactory;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.Util;
import com.xbx.client.view.TipsDialog;

/**
 * Created by EricYuan on 2016/4/13.
 */
public class BasedFragment extends Fragment implements View.OnClickListener, BDLocationListener, BaiduMap.OnMapStatusChangeListener, OnGetGeoCoderResultListener, BaiduMap.OnMapLoadedCallback {
    public static final int accuracyCircleFillColor = 0x00000000;
    public static final int accuracyCircleStrokeColor = 0x00000000;
    public final int outsetReques = 1000;
    public final int callGuideReques = 1002;
    public final int choiceTogether = 1005;
    public final int confimCall = 1999;

    public ImageLoader imageLoader;
    public ImageLoaderConfigFactory configFactory;
    public MyLocationData locData = null;
    public BitmapDescriptor bdMyself = null;//自己的图标
    public BitmapDescriptor guideBdp = null;//导游的图标
    public BitmapDescriptor guideDes = null;
    public LocationClient mLocClient;
    public LocationClientOption option = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initBDinfo();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initBDinfo() {
        mLocClient = new LocationClient(getActivity());
        option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(2000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        bdMyself = BitmapDescriptorFactory
                .fromResource(R.mipmap.myself_locate);
        /*guideBdp = BitmapDescriptorFactory
                .fromResource(R.mipmap.guide_icon);*/
        guideDes = BitmapDescriptorFactory
                .fromResource(R.mipmap.guide_locate);
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null) {
            return;
        }
        locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        SharePrefer.saveLatlng(getActivity(), bdLocation.getLongitude() + "", bdLocation.getLatitude() + "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mLocClient != null && mLocClient.isStarted()) {
            mLocClient.unRegisterLocationListener(this);
            mLocClient.stop();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {

    }
    /**
     * 行程开始的提示
     */
    public void notifyUserStartStroke(){
        final TipsDialog tDialog = new TipsDialog(getActivity());
        tDialog.setBtnTxt("",getString(R.string.txt_sure));
        tDialog.setInfo(getString(R.string.title_StartStroke),getString(R.string.msg_StartStroke));
        tDialog.show();
        tDialog.setClickListener(new TipsDialog.DialogClickListener(){

            @Override
            public void cancelDialog() {
                tDialog.dismiss();
            }

            @Override
            public void confirmDialog() {
                tDialog.dismiss();
            }
        });
    }
}

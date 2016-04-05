package com.xbx.client.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.xbx.client.R;
import com.xbx.client.beans.LocationBean;
import com.xbx.client.ui.activity.ChoicePeoNumActivity;
import com.xbx.client.ui.activity.SearchAddressActivity;
import com.xbx.client.utils.Constant;
import com.xbx.client.utils.MapLocate;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.Util;
import com.xbx.client.view.LoadingFragment;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class GuidesFragment extends BaseFragment implements BDLocationListener,
        BaiduMap.OnMapStatusChangeListener, OnGetGeoCoderResultListener, BaiduMap.OnMapLoadedCallback {
    private static GuidesFragment fragment = null;
    private View view = null;
    private MapLocate mapLocate = null;
    private LocationBean lBean = null;
    private MarkerOptions markOpMyself = null;
    private BitmapDescriptor bdMyself = null;
    private Marker mMarkerMy;
    private GeoCoder geoCoder;
    private LoadingFragment loadingFragment = null;
    private LocationMode mCurrentMode;
//    BitmapDescriptor mCurrentMarker;

    private MapView mapView;
    private RelativeLayout guide_outset_rl;
    private RelativeLayout guide_destination_rl;
    private TextView main_outset_tv; // 出发地
    private TextView main_destination_tv; //目的地
    private ImageView guide_locatemy_img;
    private LinearLayout guide_locate_layout;
    private LinearLayout guide_fuc_layout;
    private RelativeLayout guide_reserve_rl; // 预约导游
    private RelativeLayout guide_call_rl;//呼叫导游
    private LinearLayout guide_call_layout;//确认呼叫
    private RelativeLayout guide_tOrder_layout;//呼叫导游成功后

    private BaiduMap mBaiduMap;
    private final int outsetReques = 1000;
    private final int destReques = 1001;
    private final int callGuideReques = 1002;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mapLocate.startLocate();
                    handler.sendEmptyMessageDelayed(1, 10000);
                    /*if (mMarkerMy != null) {
                        LocationBean nowLbean = SharePrefer.getLocate(getActivity());
                        if (nowLbean == null) {
                            return;
                        }
                        if (!Util.isNull(nowLbean.getLat()) && !Util.isNull(nowLbean.getLon())) {
                            LatLng nowLl = new LatLng(Double.parseDouble(nowLbean.getLat()), Double.parseDouble(nowLbean.getLon()));
                            mMarkerMy.setPosition(nowLl);
                        }
                    }*/
                    break;
                case 10:
                    if (loadingFragment != null) {
                        loadingFragment.dismiss();
                    }
                    guide_tOrder_layout.setVisibility(View.VISIBLE);
                    guide_call_layout.setVisibility(View.GONE);
                    guide_locate_layout.setVisibility(View.GONE);
                    break;
            }
        }
    };

    public GuidesFragment() {
    }

    public static GuidesFragment newInstance() {
        if (fragment == null) {
            fragment = new GuidesFragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_guide, container, false);
        initDatas();
        initViews();
        initLisener();
        return view;
    }

    private void initViews() {
        mapView = (MapView) view.findViewById(R.id.guide_map);
        guide_outset_rl = (RelativeLayout) view.findViewById(R.id.guide_outset_rl);
        guide_destination_rl = (RelativeLayout) view.findViewById(R.id.guide_destination_rl);
        main_outset_tv = (TextView) view.findViewById(R.id.main_outset_tv);
        main_destination_tv = (TextView) view.findViewById(R.id.main_destination_tv);
        guide_locatemy_img = (ImageView) view.findViewById(R.id.guide_locatemy_img);

        guide_locate_layout = (LinearLayout) view.findViewById(R.id.guide_locate_layout);
        guide_fuc_layout = (LinearLayout) view.findViewById(R.id.guide_fuc_layout);
        guide_call_layout = (LinearLayout) view.findViewById(R.id.guide_call_layout);
        guide_tOrder_layout = (RelativeLayout) view.findViewById(R.id.guide_tOrder_layout);

        guide_reserve_rl = (RelativeLayout) view.findViewById(R.id.guide_reserve_rl);
        guide_call_rl = (RelativeLayout) view.findViewById(R.id.guide_call_rl);

        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);

        initBaiduMap();
    }

    private void initBaiduMap() {
        int childCount = mapView.getChildCount();
        View zoom = null;
        for (int i = 0; i < childCount; i++) {
            View child = mapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                zoom = child;
                break;
            }
        }
        if (zoom != null) {
            zoom.setVisibility(View.GONE);
        }
        if (lBean != null) {
            LatLng nowLl = new LatLng(Double.parseDouble(lBean.getLat()), Double.parseDouble(lBean.getLon()));
            MapStatus mMapstatus = new MapStatus.Builder().target(nowLl).zoom(18f)
                    .build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
            mBaiduMap.setMapStatus(u);
            markOpMyself = new MarkerOptions().position(nowLl).icon(bdMyself).zIndex(9).draggable(true);
//            mMarkerMy = (Marker) mBaiduMap.addOverlay(markOpMyself);
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(nowLl));
            mCurrentMode = LocationMode.NORMAL;
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    mCurrentMode, true, bdMyself, accuracyCircleFillColor, accuracyCircleStrokeColor));
        }
    }

    private void initDatas() {
        mapLocate = new MapLocate(getActivity());
        lBean = SharePrefer.getLocate(getActivity());
        mapLocate.startLocate();
        bdMyself = BitmapDescriptorFactory
                .fromResource(R.mipmap.myself_locate);
        geoCoder = GeoCoder.newInstance();
        handler.sendEmptyMessageDelayed(1, 10000);
        loadingFragment = new LoadingFragment();
    }

    private void initLisener() {
        guide_locatemy_img.setOnClickListener(this);
        guide_outset_rl.setOnClickListener(this);
        guide_destination_rl.setOnClickListener(this);
        guide_reserve_rl.setOnClickListener(this);
        guide_call_rl.setOnClickListener(this);
        guide_call_layout.setOnClickListener(this);
        guide_call_layout.setVisibility(View.GONE);
        guide_tOrder_layout.setVisibility(View.GONE);
        mapView.getMap().setOnMapLoadedCallback(this);
        mapView.getMap().setOnMapStatusChangeListener(this);
        geoCoder.setOnGetGeoCodeResultListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        PoiInfo poiInfo = null;
        switch (requestCode) {
            case outsetReques:
                poiInfo = data.getParcelableExtra("outsetResult");
                main_outset_tv.setText(poiInfo.name);
                if (poiInfo != null) {
                    if (poiInfo.location == null)
                        return;
                    MapStatus mMapstatus = new MapStatus.Builder().target(poiInfo.location).zoom(18f)
                            .build();
                    MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
                    mBaiduMap.setMapStatus(u);
                }
                break;
            case destReques:
                poiInfo = data.getParcelableExtra("destResult");
                main_destination_tv.setText(poiInfo.name);
                break;
            case callGuideReques:
                guide_fuc_layout.setVisibility(View.GONE);
                guide_call_layout.setVisibility(View.VISIBLE);
                handler.sendEmptyMessageDelayed(10, 2000);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.guide_locatemy_img:

                break;
            case R.id.guide_outset_rl://出发地
                intent.setClass(getActivity(), SearchAddressActivity.class);
                intent.putExtra("guide_code", Constant.outsetFlag);
                startActivityForResult(intent, outsetReques);
                break;
            case R.id.guide_destination_rl://目的地
                intent.setClass(getActivity(), SearchAddressActivity.class);
                intent.putExtra("guide_code", Constant.destFlag);
                startActivityForResult(intent, destReques);
                break;
            case R.id.guide_reserve_rl://预约导游

                break;
            case R.id.guide_call_rl:
                intent.setClass(getActivity(), ChoicePeoNumActivity.class);
                startActivityForResult(intent, callGuideReques);
//                startActivity(intent);
                break;
            case R.id.guide_call_layout://确认呼叫导游
                loadingFragment.show(getActivity().getSupportFragmentManager(), "Loading");
                loadingFragment.setMsg("正在呼叫导游");
                break;
        }
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        mapView.getMap().setMyLocationData(locData);
        LatLng latLng = new LatLng(bdLocation.getLatitude(),
                bdLocation.getLongitude());
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLng));
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null)
            return;
        if (reverseGeoCodeResult.getPoiList() == null)
            return;
        if (reverseGeoCodeResult.getPoiList().size() == 0)
            return;
        main_outset_tv.setText(reverseGeoCodeResult.getPoiList().get(0).name);
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
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(mapStatus.target));
    }
}

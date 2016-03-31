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

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.xbx.client.R;
import com.xbx.client.beans.LocationBean;
import com.xbx.client.ui.activity.SearchAddressActivity;
import com.xbx.client.utils.MapLocate;
import com.xbx.client.utils.SharePrefer;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class GuidesFragment extends BaseFragment {
    private static GuidesFragment fragment = null;
    private View view = null;
    private MapLocate mapLocate = null;
    private LocationBean lBean = null;
    private MarkerOptions markOpMyself = null;
    private BitmapDescriptor bdMyself = null;
    private Marker mMarkerMy;

    private MapView mapView;
    private RelativeLayout guide_outset_rl;
    private RelativeLayout guide_destination_rl;
    private TextView main_outset_tv; // 出发地
    private TextView main_destination_tv; //目的地
    private ImageView guide_locatemy_img;
    private LinearLayout guide_locate_layout;
    private LinearLayout guide_fuc_layout;

    private BaiduMap mBaiduMap;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mapLocate.startLocate();
                    handler.sendEmptyMessageDelayed(1, 10000);
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

        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);

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
            mMarkerMy = (Marker) mBaiduMap.addOverlay(markOpMyself);
        }
    }

    private void initDatas() {
        mapLocate = new MapLocate(getActivity());
        lBean = SharePrefer.getLocate(getActivity());
        mapLocate.startLocate();
        bdMyself = BitmapDescriptorFactory
                .fromResource(R.mipmap.myself_locate);
        handler.sendEmptyMessageDelayed(1, 10000);
    }

    private void initLisener() {
        guide_locatemy_img.setOnClickListener(this);
        guide_outset_rl.setOnClickListener(this);
        guide_destination_rl.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.guide_locatemy_img:

                break;
            case R.id.guide_outset_rl:
                startActivity(new Intent(getActivity(), SearchAddressActivity.class));
                break;
            case R.id.guide_destination_rl:

                break;
        }
    }
}

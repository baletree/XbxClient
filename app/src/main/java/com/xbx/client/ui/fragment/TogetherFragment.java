package com.xbx.client.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.makeramen.roundedimageview.RoundedImageView;
import com.xbx.client.R;
import com.xbx.client.beans.MyGuideInfoBean;
import com.xbx.client.beans.TogetherBean;
import com.xbx.client.http.Api;
import com.xbx.client.polling.FindGuide;
import com.xbx.client.polling.MyGuideInfo;
import com.xbx.client.polling.PollUploadLag;
import com.xbx.client.ui.activity.TogetherActivity;
import com.xbx.client.utils.Constant;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.Util;
import com.xbx.client.view.LoadingDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/3/29.
 * 随游
 */
public class TogetherFragment extends BasedFragment {
    private static TogetherFragment fragment = null;
    private View view = null;
    private TextureMapView mapView;
    private TextView main_outset_tv; // 出发地
    private ImageView guide_locatemy_img;
    private LinearLayout guide_fuc_layout; //预约和即时呼叫按钮布局
    private RelativeLayout guide_reserve_rl; // 预约导游
    private RelativeLayout guide_call_rl;//呼叫导游
    private LinearLayout guide_call_layout;//确认呼叫布局
    private RelativeLayout onMap_layout;

    private RelativeLayout guide_tOrder_layout;//呼叫导游成功后
    private RoundedImageView guide_head_img;
    private TextView guide_name_tv; //导游名字
    private TextView guide_code_tv; //导游证号码
    private TextView guide_star_tv;//星星评分
    private RatingBar guide_ratingbar;
    private TextView user_stroke_tv;//行程状态
    private RelativeLayout guide_phone_rl;

    private Api api = null;
    private BaiduMap mBaiduMap = null;
    private MyLocationConfiguration.LocationMode mCurrentMode = null;
    private GeoCoder geoCoder = null;
    private LatLng cLatlng = null;
    private String[] peopleNum = null;
    private LoadingDialog loadDialog = null;
    private FindGuide findNativePoll = null;
    private MyGuideInfo nativeInfoPoll = null;
    private LocalBroadcastManager lBManager = null;
    private IntentFilter intentFilter = null;
//    private CancleOrderReceiver canOrderReciver = null;
    private MyGuideInfoBean guideInfoBean = null;
    private PollUploadLag uploadLag = null; //上传经纬度
    private Marker mMarkerGuide = null;//服务于我的导游图标

    private String uid = "";
    private boolean isVisibleToUser = false;
    private boolean isFirstLoc = true;
    private boolean isInOrder = false;
    private boolean isFirstInOrder = false;
    private final int callGuideReques = 1002;
    private String userNums = "";//出行人数
    private String outsetJson = "";//出发地的拼接
    private String nativeOrderNum = "";
    private String nearGuideUrl = "";
    private int countTraval = 0;

    public TogetherFragment() {
    }

    public static TogetherFragment newInstance() {
        if (fragment == null) {
            fragment = new TogetherFragment();
        }
        return fragment;
    }

    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.frag_together, container, false);
        initData();
        return view;
    }

    private void initData() {
        initBroadcast();
        loadDialog = new LoadingDialog(getActivity());
        api = new Api(getActivity(), handler);
        uid = SharePrefer.getUserInfo(getActivity()).getUid();
        geoCoder = GeoCoder.newInstance();
        findNativePoll = new FindGuide(getActivity(), handler);
        nativeInfoPoll = new MyGuideInfo(getActivity(), handler);
        uploadLag = new PollUploadLag(getActivity());
        nearGuideUrl = getString(R.string.url_conIp).concat(getString(R.string.url_nearGuide));
        initView();
    }

    private void initView() {
        mLocClient.registerLocationListener(this);
        mapView = (TextureMapView) view.findViewById(R.id.together_map);
        main_outset_tv = (TextView) view.findViewById(R.id.main_outset_tv);
        guide_locatemy_img = (ImageView) view.findViewById(R.id.together_locatemy_img);
        guide_fuc_layout = (LinearLayout) view.findViewById(R.id.together_fuc_layout);
        guide_call_rl = (RelativeLayout) view.findViewById(R.id.guide_call_rl);
        guide_call_layout = (LinearLayout) view.findViewById(R.id.together_call_layout);
        onMap_layout = (RelativeLayout) view.findViewById(R.id.together_onMap_layout);
        view.findViewById(R.id.guide_reserve_rl).setOnClickListener(this);
        guide_tOrder_layout = (RelativeLayout) view.findViewById(R.id.together_tOrder_layout);
        guide_head_img = (RoundedImageView) view.findViewById(R.id.guide_head_img);
        guide_name_tv = (TextView) view.findViewById(R.id.guide_name_tv);
        guide_code_tv = (TextView) view.findViewById(R.id.guide_code_tv);
        guide_star_tv = (TextView) view.findViewById(R.id.guide_star_tv);
        user_stroke_tv = (TextView) view.findViewById(R.id.user_stroke_tv);
        guide_ratingbar = (RatingBar) view.findViewById(R.id.guide_ratingbar);
        guide_phone_rl = (RelativeLayout) view.findViewById(R.id.guide_phone_rl);

        guide_call_layout.setVisibility(View.GONE);
        guide_tOrder_layout.setVisibility(View.GONE);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);//开启定位图层
        initLisener();
    }

    private void initLisener() {
        mapView.getMap().setOnMapLoadedCallback(this);
        mapView.getMap().setOnMapStatusChangeListener(this);
        geoCoder.setOnGetGeoCodeResultListener(this);
        guide_call_rl.setOnClickListener(this);
        guide_call_layout.setOnClickListener(this);
    }

    private void initBroadcast() {
        lBManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_GCANCELUIDEORDSUC);
        intentFilter.addAction(Constant.ACTION_GUIDEINORDER);
        intentFilter.addAction(Constant.ACTION_GUIDEOVERSERVER);
        canOrderReciver = new CancleOrderReceiver();
        lBManager.registerReceiver(canOrderReciver, intentFilter);
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        super.onReceiveLocation(bdLocation);
        mBaiduMap.setMyLocationData(locData);
        if (isFirstLoc && !Util.isNull(bdLocation.getAddrStr())) {
            isFirstLoc = false;
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            cLatlng = ll;
//            handler.sendEmptyMessage(1);
            MapStatus mMapstatus = new MapStatus.Builder().target(ll).zoom(19f)
                    .build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(ll));
            mBaiduMap.animateMapStatus(u);
            mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    mCurrentMode, true, bdMyself, accuracyCircleFillColor, accuracyCircleStrokeColor));
            Util.pLog("TogetherFrag LocateLisen:" + bdLocation.getLatitude() + "," + bdLocation.getLongitude() + " adr:" + bdLocation.getAddrStr() + " isFirstLoc:" + isFirstLoc);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.guide_call_rl:
                Intent intent = new Intent(getActivity(), TogetherActivity.class);
                List<TogetherBean> list = new ArrayList<>();
                TogetherBean bean1 = new TogetherBean();
                TogetherBean bean2 = new TogetherBean();
                TogetherBean bean3 = new TogetherBean();
                TogetherBean bean4 = new TogetherBean();
                TogetherBean bean5 = new TogetherBean();
                list.add(bean1);
                list.add(bean2);
                list.add(bean3);
                list.add(bean4);
                list.add(bean5);
                intent.putExtra("data", (Serializable) list);
                startActivity(intent);
                break;
            case R.id.guide_reserve_rl:

                break;
        }
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult rGeoCodeResult) {
        if (rGeoCodeResult == null)
            return;
        if (rGeoCodeResult.getPoiList() == null)
            return;
        if (rGeoCodeResult.getPoiList().size() == 0)
            return;
        *//*cLatlng = rGeoCodeResult.getLocation();
        main_outset_tv.setText(rGeoCodeResult.getPoiList().get(0).name);
        outsetJson = rGeoCodeResult.getLocation().latitude + "," + rGeoCodeResult.getLocation().longitude + "," + main_outset_tv.getText().toString();*//*
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
        main_outset_tv.setText("");
        main_outset_tv.setHint(getString(R.string.loading_locate));
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(mapStatus.target));
    }


    class CancleOrderReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.ACTION_GCANCELUIDEORDSUC.equals(action)) {//取消订单
                Util.pLog(getString(R.string.cancel_order_suc));
                cancelOrderSuc();
            } else if (Constant.ACTION_GUIDEOVERSERVER.equals(action)) {//结束行程
                cancelOrderSuc();
            } else if (Constant.ACTION_GUIDEINORDER.equals(action)) {
                isInOrder = true;
                guide_tOrder_layout.setVisibility(View.VISIBLE);
                guide_fuc_layout.setVisibility(View.VISIBLE);
                onMap_layout.setVisibility(View.GONE);
                String stateOderNum = intent.getStringExtra("stateOrderNumber");
                nativeInfoPoll.getMyGuideInfo(stateOderNum);
            }
        }
    }

    private void cancelOrderSuc() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstLoc = true;
        isInOrder = false;
        isFirstInOrder = false;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        super.onPause();
    }*/
}

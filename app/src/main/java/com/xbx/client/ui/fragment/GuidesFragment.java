package com.xbx.client.ui.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx.client.R;
import com.xbx.client.beans.GuideBean;
import com.xbx.client.beans.LocationBean;
import com.xbx.client.beans.MyGuideInfoBean;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.GuideParse;
import com.xbx.client.service.NearGuideService;
import com.xbx.client.ui.activity.ChoicePeoNumActivity;
import com.xbx.client.ui.activity.ImmediatePayActivity;
import com.xbx.client.ui.activity.ReservatGuideActivity;
import com.xbx.client.ui.activity.SearchAddressActivity;
import com.xbx.client.utils.AnimateFirstDisplayListener;
import com.xbx.client.utils.Constant;
import com.xbx.client.utils.ImageLoaderConfigFactory;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;
import com.xbx.client.view.FindGuideFragment;

import java.util.List;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class GuidesFragment extends BasedFragment implements
        BaiduMap.OnMapStatusChangeListener, OnGetGeoCoderResultListener, BaiduMap.OnMapLoadedCallback, FindGuideFragment.OnCancelGuiFindLisener {
    private static GuidesFragment fragment = null;
    private View view = null;
    private BitmapDescriptor bdMyself = null;
    private BitmapDescriptor guideBdp = null;
    private GeoCoder geoCoder;
    private FindGuideFragment loadingFragment = null;
    private LocationMode mCurrentMode;
    private LocationClient mLocClient;
    private BaiduMap mBaiduMap;
    private LocalBroadcastManager lBManager = null;
    private LocationBean lBean = null;
    private MyLocationListenner myListener = null;
    private LatLng currentLalng = null;
    private List<GuideBean> guideList = null;
    private Api api = null;
    private NearGuideService nearGuiService = null;
    private MyGuideInfoBean guideInfoBean = null;
    private BitmapDescriptor guideDes = null;

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
    private RoundedImageView guide_head_img;
    private TextView guide_name_tv; //导游名字
    private TextView guide_code_tv; //导游证号码
    private TextView guide_star_tv;//星星评分
    private RatingBar guide_ratingbar;
    private TextView user_stroke_tv;//行程状态
    private RelativeLayout guide_phone_rl;
    private LinearLayout guide_center_layout; //地图中心点

    private final int outsetReques = 1000;
    private final int destReques = 1001;
    private final int callGuideReques = 1002;
    private static final int accuracyCircleFillColor = 0x00000000;
    private static final int accuracyCircleStrokeColor = 0x00000000;
    private boolean isFirstLoc = true; // 是否是第一次像用户定位
    private String nearGuideUrl = "";
    private boolean isVisibaleTouser = false;
    private boolean isInOrder = false; // 是否处在订单中
    //    private boolean isCancle = false;// 用户是否取消
    private boolean isArrivalTime = false;
    private boolean isFirstInOrder = true;
    private int countFindGui = 0;
    private String uid = "";
    private String outsetJson = "";
    private String destinationJson = null;
    private Marker mMarkerGuide;
    private String guideNums = "";
    private String findGuideorderNum;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (isVisibaleTouser && !isInOrder) {
                        LatLng cLatlng = SharePrefer.getLatlng(getActivity());
                        if (cLatlng == null)
                            return;
                        api.getNearGuide(currentLalng, nearGuideUrl, uid);
                        handler.sendEmptyMessageDelayed(1, 6000);
                    }
                    break;
                case 10:
                    if (!isInOrder && !isArrivalTime) {
                        // 未匹配到导游并且没有到达规定时间继续发送
                        api.isFindGuide(uid, findGuideorderNum);
                        handler.sendEmptyMessageDelayed(10, 5000);
                    }
                    break;
                case 20:
                    api.getMyGuideInfo(findGuideorderNum);
                    handler.sendEmptyMessageDelayed(20, 5000);
                    break;
                case 30:
                    countFindGui++;
                    if(countFindGui < 81){
                        handler.sendEmptyMessage(30);
                    }else {
                        isArrivalTime = true;
                        countFindGui = 0;
                        guide_call_layout.setVisibility(View.GONE);
                        guide_fuc_layout.setVisibility(View.VISIBLE);
                    }
                    break;
                case TaskFlag.REQUESTSUCCESS:
                    String guideData = (String) msg.obj;
                    if (Util.isNull(guideData))
                        return;
                    switch (GuideParse.getDataType(guideData)) {
                        case 1://导游
                            guideList = GuideParse.getGuideList(guideData);
                            if (guideList == null)
                                return;
                            addOverlyGuide();
                            break;
                        case 2://订单信息

                            break;
                    }
                    break;
                case TaskFlag.PAGEREQUESTWO://服务我的导游信息
                    String myGuideData = (String) msg.obj;
                    guideInfoBean = GuideParse.parseMyGuide(myGuideData);
                    if (guideInfoBean != null)
                        if (isFirstInOrder)
                            setMyGuideInfo();
                        else
                            updateGuide();
                    break;
                case TaskFlag.PAGEREQUESTHREE://开始呼叫导游,获取周边是否有导游
                    findGuideorderNum = GuideParse.getImmdiaOrder((String) msg.obj);
                    Util.pLog("oderNum:" + findGuideorderNum);
                    if (Util.isNull(findGuideorderNum))
                        return;
                    loadingFragment.show(getActivity().getSupportFragmentManager(), "Loading");
                    loadingFragment.setMsg("正在呼叫导游");
                    loadingFragment.setOncancelFindLisen(GuidesFragment.this);
                    handler.sendEmptyMessage(10);
                    handler.sendEmptyMessage(30);
                    break;
                case TaskFlag.PAGEREQUESFOUR://系统匹配到了导游
                    isInOrder = true;
                    Util.showToast(getActivity(), getString(R.string.success_find_guide));
                    setPageLayout();
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            isVisibaleTouser = true;
        else
            isVisibaleTouser = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_guide, container, false);
        initDatas();
        return view;
    }

    protected void initDatas() {
        guideDes = BitmapDescriptorFactory
                .fromResource(R.mipmap.guide_locate);
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
        uid = SharePrefer.getUserInfo(getActivity()).getUid();
        api = new Api(getActivity(), handler);
        bdMyself = BitmapDescriptorFactory
                .fromResource(R.mipmap.myself_locate);
        guideBdp = BitmapDescriptorFactory
                .fromResource(R.mipmap.guide_icon);
        geoCoder = GeoCoder.newInstance();
        loadingFragment = new FindGuideFragment();
        lBManager = LocalBroadcastManager.getInstance(getActivity());
        nearGuideUrl = getString(R.string.url_conIp).concat(getString(R.string.url_nearGuide));
        initViews();
        initLisener();
    }

    protected void initViews() {
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

        guide_head_img = (RoundedImageView) view.findViewById(R.id.guide_head_img);
        guide_name_tv = (TextView) view.findViewById(R.id.guide_name_tv);
        guide_code_tv = (TextView) view.findViewById(R.id.guide_code_tv);
        guide_star_tv = (TextView) view.findViewById(R.id.guide_star_tv);
        user_stroke_tv = (TextView) view.findViewById(R.id.user_stroke_tv);
        guide_ratingbar = (RatingBar) view.findViewById(R.id.guide_ratingbar);
        guide_phone_rl = (RelativeLayout) view.findViewById(R.id.guide_phone_rl);
        guide_center_layout = (LinearLayout) view.findViewById(R.id.guide_center_layout);

        mBaiduMap = mapView.getMap();
        lBean = SharePrefer.getLocate(getActivity());
        if (lBean != null) {
            if (!Util.isNull(lBean.getLat()) && !Util.isNull(lBean.getLon())) {
                currentLalng = new LatLng(Double.parseDouble(lBean.getLat()),
                        Double.parseDouble(lBean.getLon()));
                handler.sendEmptyMessage(1);
                LatLng ll = new LatLng(Double.parseDouble(lBean.getLat()),
                        Double.parseDouble(lBean.getLon()));
                MapStatus mMapstatus = new MapStatus.Builder().target(ll).zoom(20f)
                        .build();
                MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
                mBaiduMap.setMapStatus(u);
            }
        }
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
        myListener = new MyLocationListenner();
        //定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    protected void initLisener() {
        guide_locatemy_img.setOnClickListener(this);
        guide_outset_rl.setOnClickListener(this);
        guide_destination_rl.setOnClickListener(this);
        guide_reserve_rl.setOnClickListener(this);
        guide_call_rl.setOnClickListener(this);
        guide_call_layout.setOnClickListener(this);
        guide_head_img.setOnClickListener(this);
        guide_phone_rl.setOnClickListener(this);

        guide_call_layout.setVisibility(View.GONE);
        guide_tOrder_layout.setVisibility(View.GONE);
        mapView.getMap().setOnMapLoadedCallback(this);
        mapView.getMap().setOnMapStatusChangeListener(this);
        geoCoder.setOnGetGeoCodeResultListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstLoc = true;
        isInOrder = false;
        if (mLocClient != null && mLocClient.isStarted())
            if (myListener != null)
                mLocClient.unRegisterLocationListener(myListener);
        mLocClient.stop();
        mapView.onDestroy();
        currentLalng = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        PoiInfo poiInfo = null;
        switch (requestCode) {
            case outsetReques: // 出发地
                poiInfo = data.getParcelableExtra("outsetResult");
                if (poiInfo == null)
                    return;
                main_outset_tv.setText(poiInfo.name);
                if (poiInfo.location == null)
                    return;
                MapStatus mMapstatus = new MapStatus.Builder().target(poiInfo.location).zoom(18f)
                        .build();
                MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
                mBaiduMap.setMapStatus(u);
                break;
            case destReques://目的地
                poiInfo = data.getParcelableExtra("destResult");
                if (poiInfo == null)
                    return;
                main_destination_tv.setText(poiInfo.name);
                destinationJson = poiInfo.location.latitude + "," + poiInfo.location.longitude + "," + poiInfo.name;
                break;
            case callGuideReques:
                guideNums = data.getStringExtra("GuideNum");
                if (Util.isNull(guideNums))
                    return;
                guide_fuc_layout.setVisibility(View.GONE);
                guide_call_layout.setVisibility(View.VISIBLE);
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
                intent.setClass(getActivity(), ReservatGuideActivity.class);
                startActivity(intent);
                break;
            case R.id.guide_call_rl:
                intent.setClass(getActivity(), ChoicePeoNumActivity.class);
                startActivityForResult(intent, callGuideReques);
                break;
            case R.id.guide_call_layout://确认呼叫导游
                isArrivalTime = false;
                String setOff = main_outset_tv.getText().toString();
                if (Util.isNull(setOff)) {
                    Util.showToast(getActivity(), getString(R.string.setoff_null));
                    return;
                }
                api.findGuide(uid, outsetJson, outsetJson, "", "", "0", "1", guideNums);
                break;
            case R.id.guide_head_img://头像

                break;
            case R.id.guide_phone_rl://电话
                startActivity(new Intent(getActivity(), ImmediatePayActivity.class));
                break;
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult rGeoCodeResult) {
        if (rGeoCodeResult == null)
            return;
        if (rGeoCodeResult.getPoiList() == null)
            return;
        if (rGeoCodeResult.getPoiList().size() == 0)
            return;
        currentLalng = rGeoCodeResult.getLocation();
        main_outset_tv.setText(rGeoCodeResult.getPoiList().get(0).name);
        outsetJson = rGeoCodeResult.getLocation().latitude + "," + rGeoCodeResult.getLocation().longitude + "," + main_outset_tv.getText().toString();
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
        main_outset_tv.setText("");
        main_outset_tv.setHint(getString(R.string.loading_locate));
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        Util.pLog("拖动地图结束");
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(mapStatus.target));
    }

    @Override
    public void cancelGuiFind() {

    }

    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            SharePrefer.saveLatlng(getActivity(), location.getLongitude() + "", location.getLatitude() + "");
            if (isFirstLoc && !Util.isNull(location.getAddrStr())) {
                Util.pLog("LocateLisen:" + location.getLatitude() + "," + location.getLongitude() + " adr:" + location.getAddrStr() + " isFirstLoc:" + isFirstLoc);
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                handler.sendEmptyMessage(1);
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(ll));
                MapStatus mMapstatus = new MapStatus.Builder().target(ll).zoom(15f)
                        .build();
                MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
                mBaiduMap.animateMapStatus(u);
                mBaiduMap.setMapStatus(u);
                mCurrentMode = LocationMode.NORMAL;
                mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, bdMyself, accuracyCircleFillColor, accuracyCircleStrokeColor));
                mBaiduMap.setMyLocationEnabled(true);//开启定位图层
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    /**
     * 将附近的导游添加到地图上
     */
    private void addOverlyGuide() {
        mBaiduMap.clear();
        for (int i = 0; i < guideList.size(); i++) {
            GuideBean guideBean = guideList.get(i);
            LatLng latLng = new LatLng(guideBean.getLatitude(), guideBean.getLongitude());
            MarkerOptions mos = new MarkerOptions().position(latLng).icon(guideBdp)
                    .zIndex(9).draggable(true);
            mBaiduMap.addOverlay(mos);
        }
    }

    /**
     * 设置为我服务的导游信息
     */
    private void setMyGuideInfo() {
        isFirstInOrder = false;
        imageLoader.displayImage(guideInfoBean.getGuideHeadImg(), guide_head_img, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        guide_name_tv.setText(guideInfoBean.getGuideName());
        guide_code_tv.setText(guideInfoBean.getGuideNum());
        guide_star_tv.setText(guideInfoBean.getGuideStarts() + getString(R.string.scole));
        if (!Util.isNull(guideInfoBean.getGuideStarts()))
            guide_ratingbar.setRating(Float.valueOf(guideInfoBean.getGuideStarts()));
        LatLng guideLl = new LatLng(guideInfoBean.getGuideLat(), guideInfoBean.getGuideLon());
        MarkerOptions ooA = new MarkerOptions().position(guideLl).icon(guideDes)
                .zIndex(9).draggable(true);
        mMarkerGuide = (Marker) mBaiduMap.addOverlay(ooA);
    }

    private void updateGuide() {
        if (guideInfoBean.getStartTime() != 0) {
            if (user_stroke_tv == null)
                return;
            user_stroke_tv.setText(getString(R.string.stork_time) + getString(R.string.not_start));
        }
        if (mMarkerGuide != null)
            mMarkerGuide.setPosition(new LatLng(guideInfoBean.getGuideLat(), guideInfoBean.getGuideLon()));
    }

    /**
     * 找到导游后将页面图重置
     */
    private void setPageLayout() {
        handler.sendEmptyMessage(20);
        guide_tOrder_layout.setVisibility(View.VISIBLE);
        guide_call_layout.setVisibility(View.GONE);
        guide_locate_layout.setVisibility(View.GONE);
        guide_center_layout.setVisibility(View.GONE);
        Intent intent = new Intent();
        intent.putExtra("theOrderNum", findGuideorderNum);
        intent.putExtra("cancelType", 1);
        intent.setAction(Constant.ACTION_GCANCELORD);
        lBManager.sendBroadcast(intent);
        if (loadingFragment != null) {
            loadingFragment.dismiss();
        }
        if (mBaiduMap != null) {
            mBaiduMap.clear();
        }
    }


    private class NearGuide implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            nearGuiService = (NearGuideService) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}

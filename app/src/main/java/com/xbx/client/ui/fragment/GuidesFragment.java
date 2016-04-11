package com.xbx.client.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.xbx.client.R;
import com.xbx.client.beans.GuideBean;
import com.xbx.client.beans.LocationBean;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.GuideParse;
import com.xbx.client.jsonparse.UtilParse;
import com.xbx.client.ui.activity.ChoicePeoNumActivity;
import com.xbx.client.ui.activity.SearchAddressActivity;
import com.xbx.client.utils.Constant;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;
import com.xbx.client.view.LoadingFragment;

import java.util.List;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class GuidesFragment extends BaseFragment implements
        BaiduMap.OnMapStatusChangeListener, OnGetGeoCoderResultListener, BaiduMap.OnMapLoadedCallback {
    private static GuidesFragment fragment = null;
    private View view = null;
    private BitmapDescriptor bdMyself = null;
    private BitmapDescriptor guideBdp = null;
    private GeoCoder geoCoder;
    private LoadingFragment loadingFragment = null;
    private LocationMode mCurrentMode;
    private LocationClient mLocClient;
    private BaiduMap mBaiduMap;
    private LocalBroadcastManager lBManager = null;
    private LocationBean lBean = null;
    private MyLocationListenner myListener = null;
    private LatLng currentLalng = null;
    private List<GuideBean> guideList = null;
    private Api api = null;

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
    private TextView user_stroke_tv;//行程状态
    private RelativeLayout guide_phone_rl;

    private final int outsetReques = 1000;
    private final int destReques = 1001;
    private final int callGuideReques = 1002;
    private static final int accuracyCircleFillColor = 0x00000000;
    private static final int accuracyCircleStrokeColor = 0x00000000;
    private boolean isFirstLoc = true; // 是否是第一次像用户定位
    private String nearGuideUrl = "";
    private String guideInfoUrl = "";
    private boolean isVisibaleTouser = false;
    private boolean isInOrder = false;
    private String uid = "";


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (isVisibaleTouser && !isInOrder){
                        LatLng cLatlng = SharePrefer.getLatlng(getActivity());
                        if(cLatlng == null)
                            return;
                        currentLalng = cLatlng;
                        api.getNearGuide(currentLalng, nearGuideUrl, uid);
                        handler.sendEmptyMessageDelayed(1, 6000);
                    }
                    break;
                case 10:
                    if (loadingFragment != null) {
                        loadingFragment.dismiss();
                    }
                    guide_tOrder_layout.setVisibility(View.VISIBLE);
                    guide_call_layout.setVisibility(View.GONE);
                    guide_locate_layout.setVisibility(View.GONE);
                    Intent intent = new Intent();
                    intent.setAction(Constant.ACTION_GCANCELORD);
                    lBManager.sendBroadcast(intent);
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

    @Override
    protected void onCreateView(View contentView) {
        this.view = contentView;
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.frag_guide;
    }

    @Override
    protected void initDatas() {
        uid = SharePrefer.getUserInfo(getActivity()).getUid();
        api = new Api(getActivity(), handler);
        bdMyself = BitmapDescriptorFactory
                .fromResource(R.mipmap.myself_locate);
        guideBdp = BitmapDescriptorFactory
                .fromResource(R.mipmap.guide_icon);
        geoCoder = GeoCoder.newInstance();
        loadingFragment = new LoadingFragment();
        lBManager = LocalBroadcastManager.getInstance(getActivity());
        nearGuideUrl = getString(R.string.url_conIp).concat(getString(R.string.url_nearGuide));

    }

    @Override
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
        guide_phone_rl = (RelativeLayout) view.findViewById(R.id.guide_phone_rl);

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

    @Override
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
                break;
            case R.id.guide_call_layout://确认呼叫导游
                loadingFragment.show(getActivity().getSupportFragmentManager(), "Loading");
                loadingFragment.setMsg("正在呼叫导游");
                handler.sendEmptyMessageDelayed(10, 3000);
                break;
            case R.id.guide_head_img://头像

                break;
            case R.id.guide_phone_rl://电话

                break;
        }
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

    private void setMyGuideInfo() {

    }
}

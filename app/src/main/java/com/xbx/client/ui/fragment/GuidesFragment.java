package com.xbx.client.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
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
import com.xbx.client.linsener.AnimateFirstDisplayListener;
import com.xbx.client.linsener.ImageLoaderConfigFactory;
import com.xbx.client.polling.FindGuide;
import com.xbx.client.polling.MyGuideInfo;
import com.xbx.client.polling.PollUploadLag;
import com.xbx.client.ui.activity.ChoicePeoNumActivity;
import com.xbx.client.ui.activity.PayOrderActivity;
import com.xbx.client.ui.activity.ReservatGuideActivity;
import com.xbx.client.ui.activity.SearchAddressActivity;
import com.xbx.client.utils.Constant;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.StringUtil;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;
import com.xbx.client.view.LoadingDialog;

import java.util.List;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class GuidesFragment extends BasedFragment {
    private static GuidesFragment fragment = null;
    private View view = null;
    private GeoCoder geoCoder = null;
    private LoadingDialog loadingDialog = null;
    private LocationMode mCurrentMode;
    private BaiduMap mBaiduMap;
    private LocalBroadcastManager lBManager = null;
    private LatLng currentLalng = null;
    private List<GuideBean> guideList = null;
    private Api api = null;
    private MyGuideInfoBean guideInfoBean = null;

    private PollUploadLag uploadLag = null; //上传经纬度
    private FindGuide findGuide = null;// 获取为我服务的导游
    private MyGuideInfo guideInfo = null;// 获取服务于我的导游信息
    private String[] peopleNum = null;
    private CancleOrderReceiver canOrderReciver = null;
    private IntentFilter intentFilter = null;
    private Marker mMarkerGuide = null;//服务于我的导游图标

    private TextureMapView mapView;
    private RelativeLayout guide_outset_rl;
    private RelativeLayout guide_destination_rl;
    private TextView main_outset_tv; // 出发地
    private TextView main_destination_tv; //目的地
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

    private final int outsetReques = 1000;
    private final int destReques = 1001;
    private final int callGuideReques = 1002;
    private long countTraval = 0;

    private String uid = "";
    private String nearGuideUrl = "";
    private boolean isVisibaleTouser = false;
    private boolean isFirstLoc = true; // 是否是第一次像用户定位
    private boolean isInOrder = false; // 是否处在订单中
    private boolean isFirstInOrder = true;
    private String outsetJson = ""; //当前地址的拼接
    private String userNums = ""; // 出行人数选择
    private String findGuideorderNum;

    public GuidesFragment() {
    }

    public static GuidesFragment newInstance() {
        if (fragment == null) {
            fragment = new GuidesFragment();
        }
        return fragment;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (isVisibaleTouser && !isInOrder && !Util.isNull(main_outset_tv.getText().toString())) {
                        api.getNearGuide(currentLalng, nearGuideUrl, Constant.guideType);
//                        handler.sendEmptyMessageDelayed(1, 6000);
                    }
                    break;
                case 30:
                    guide_call_layout.setVisibility(View.GONE);
                    guide_fuc_layout.setVisibility(View.VISIBLE);
                    Util.showToast(getActivity(), getString(R.string.no_guide));
                    if (loadingDialog != null && loadingDialog.isShowing())
                        loadingDialog.dismiss();
                    break;
                case 99:
                    user_stroke_tv.setText(getString(R.string.stork_time) + StringUtil.getCountTravel((guideInfoBean.getCurrentTime() * 1000 + countTraval * 1000) - guideInfoBean.getStartTime() * 1000));
                    handler.sendEmptyMessageDelayed(99, 1000);
                    countTraval = countTraval + 1;
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
                    Util.pLog("该服务导游:isFirstInOrder=" + isFirstInOrder + " " + myGuideData);
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
                    findGuide.toFindGuide(uid, findGuideorderNum);
                    loadingDialog.setMessage(getString(R.string.call_sure_tips));
                    loadingDialog.show();
                    break;
                case TaskFlag.PAGEREQUESFOUR://系统匹配到了导游
                    Util.showToast(getActivity(), getString(R.string.success_find_guide));
                    findGuide.removeFindGuide();
                    isInOrder = true;
                    setPageLayout();
                    break;
                case TaskFlag.PAGEREQUESFIVE://请求选择的出行人数
                    String peopleData = (String) msg.obj;
                    peopleNum = GuideParse.getChoiceNum(peopleData);
                    if (peopleNum == null)
                        Util.showToast(getActivity(), getString(R.string.choice_null));
                    else {
                        Intent poeIntent = new Intent(getActivity(), ChoicePeoNumActivity.class);
                        poeIntent.putExtra("peopleNumArray", peopleNum);
                        startActivityForResult(poeIntent, callGuideReques);
                    }
                    break;
                case TaskFlag.RMOVEHANDLERONE:
                    handler.removeMessages(1);
                    break;
            }
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibaleTouser = isVisibleToUser;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.frag_guide, container, false);
        initDatas();
        return view;
    }

    protected void initDatas() {
        uploadLag = new PollUploadLag(getActivity());
        findGuide = new FindGuide(getActivity(), handler);
        guideInfo = new MyGuideInfo(getActivity(), handler);
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
        uid = SharePrefer.getUserInfo(getActivity()).getUid();
        api = new Api(getActivity(), handler);
        geoCoder = GeoCoder.newInstance();
        loadingDialog = new LoadingDialog(getActivity());
        lBManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_GCANCELUIDEORDSUC);
        intentFilter.addAction(Constant.ACTION_GUIDEINORDER);
        intentFilter.addAction(Constant.ACTION_GUIDEOVERSERVER);
        canOrderReciver = new CancleOrderReceiver();
        lBManager.registerReceiver(canOrderReciver, intentFilter);
        nearGuideUrl = getString(R.string.url_conIp).concat(getString(R.string.url_nearGuide));
        initViews();
    }

    protected void initViews() {
        mapView = (TextureMapView) view.findViewById(R.id.guide_map);
        guide_outset_rl = (RelativeLayout) view.findViewById(R.id.guide_outset_rl);
        guide_destination_rl = (RelativeLayout) view.findViewById(R.id.guide_destination_rl);
        main_outset_tv = (TextView) view.findViewById(R.id.main_outset_tv);
        main_destination_tv = (TextView) view.findViewById(R.id.main_destination_tv);
        guide_locatemy_img = (ImageView) view.findViewById(R.id.guide_locatemy_img);

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
        onMap_layout = (RelativeLayout) view.findViewById(R.id.onMap_layout);

        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);//开启定位图层
        mapView.showZoomControls(false); //隐藏缩放控件
        initLisener();
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

        mLocClient.registerLocationListener(this);
        guide_call_layout.setVisibility(View.GONE);
        guide_tOrder_layout.setVisibility(View.GONE);
        mapView.getMap().setOnMapLoadedCallback(this);
        mapView.getMap().setOnMapStatusChangeListener(this);
        geoCoder.setOnGetGeoCodeResultListener(this);
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
                break;
            case callGuideReques:
                userNums = data.getStringExtra("userNumType");
                if (Util.isNull(userNums))
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
                api.getSetoffNum();
                break;
            case R.id.guide_call_layout://确认呼叫导游
                String setOff = main_outset_tv.getText().toString();
                String guideType = Constant.guideType;
                if (Util.isNull(setOff)) {
                    Util.showToast(getActivity(), getString(R.string.setoff_null));
                    return;
                }
                api.hasGuide(uid, outsetJson, guideType, userNums, "");
                break;
            case R.id.guide_head_img://头像

                break;
            case R.id.guide_phone_rl://电话
                startActivity(new Intent(getActivity(), PayOrderActivity.class));
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
        currentLalng = rGeoCodeResult.getLocation();
        if (!isInOrder) {
            api.getNearGuide(currentLalng, nearGuideUrl, Constant.guideType);
        }
        main_outset_tv.setText(rGeoCodeResult.getPoiList().get(0).name);
        outsetJson = rGeoCodeResult.getLocation().latitude + "," + rGeoCodeResult.getLocation().longitude + "," + main_outset_tv.getText().toString();
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
        if (guideInfoBean.getStartTime() == 0)
            uploadLag.uploadLatlng();
        imageLoader.displayImage(guideInfoBean.getGuideHeadImg(), guide_head_img, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        guide_name_tv.setText(guideInfoBean.getGuideName());
        guide_code_tv.setText(guideInfoBean.getGuideNum());
        guide_star_tv.setText(guideInfoBean.getGuideStarts() + getString(R.string.scole));
        user_stroke_tv.setText(getString(R.string.stork_time) + getString(R.string.not_start));
        if (!Util.isNull(guideInfoBean.getGuideStarts()))
            guide_ratingbar.setRating(Float.valueOf(guideInfoBean.getGuideStarts()));
        LatLng guideLl = new LatLng(guideInfoBean.getGuideLat(), guideInfoBean.getGuideLon());
        MarkerOptions ooA = new MarkerOptions().position(guideLl).icon(guideDes)
                .zIndex(9).draggable(true);
        mMarkerGuide = (Marker) mBaiduMap.addOverlay(ooA);
    }

    /**
     * 设置为我服务的导游位置和行程
     */
    private void updateGuide() {
        if (mMarkerGuide != null)
            mMarkerGuide.setPosition(new LatLng(guideInfoBean.getGuideLat(), guideInfoBean.getGuideLon()));
        if (guideInfoBean.getStartTime() != 0 && isInOrder) {
//            timeDiff = guideInfoBean.getCurrentTime() - guideInfoBean.getStartTime();
            handler.sendEmptyMessage(99);
            uploadLag.removeUploadLatlng();
            guideInfo.removeGetInfo();
            mBaiduMap.clear();
        }
    }

    /**
     * 找到导游后将页面图重置
     */
    private void setPageLayout() {
        guideInfo.getMyGuideInfo(findGuideorderNum);
        guide_tOrder_layout.setVisibility(View.VISIBLE);
        guide_fuc_layout.setVisibility(View.VISIBLE);
        onMap_layout.setVisibility(View.GONE);
        Intent intent = new Intent();
        intent.putExtra("theOrderNum", findGuideorderNum);
        intent.putExtra("cancelType", 1);
        intent.setAction(Constant.ACTION_GCANCELORD);
        lBManager.sendBroadcast(intent);
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
        if (mBaiduMap != null)
            mBaiduMap.clear();
    }

    /**
     * 取消订单成功后的操作
     */
    private void cancelOrderSuc() {
        mBaiduMap.clear();
        handler.removeMessages(99);
        guideInfo.removeGetInfo();
        uploadLag.removeUploadLatlng();
        isInOrder = false;
        isFirstInOrder = true;
        countTraval = 0;
        guideInfoBean = null;
        findGuideorderNum = "";
        guide_tOrder_layout.setVisibility(View.GONE);
        onMap_layout.setVisibility(View.VISIBLE);
        guide_call_layout.setVisibility(View.GONE);
        LatLng latLng = SharePrefer.getLatlng(getActivity());
        if (latLng != null) {
            currentLalng = latLng;
            handler.sendEmptyMessage(1);
            MapStatus mMapstatus = new MapStatus.Builder().target(currentLalng).zoom(18f)
                    .build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
            mBaiduMap.setMapStatus(u);
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(currentLalng));
        }
    }

    /**
     * 定位的监听类
     */

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        // map view 销毁后不在处理新接收的位置
        super.onReceiveLocation(bdLocation);
        mBaiduMap.setMyLocationData(locData);
        if (isFirstLoc && !Util.isNull(bdLocation.getAddrStr())) {
            isFirstLoc = false;
            handler.removeMessages(1);
            Util.pLog("GuideFrag LocateLisen:" + bdLocation.getLatitude() + "," + bdLocation.getLongitude() + " adr:" + bdLocation.getAddrStr() + " isFirstLoc:" + isFirstLoc);
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatus mMapstatus = new MapStatus.Builder().target(ll).zoom(18f)
                    .build();
            MapStatusUpdate mu = MapStatusUpdateFactory.newMapStatus(mMapstatus);
            handler.sendEmptyMessage(1);
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(ll));
            mCurrentMode = LocationMode.NORMAL;
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    mCurrentMode, true, bdMyself, accuracyCircleFillColor, accuracyCircleStrokeColor));
            mBaiduMap.setMapStatus(mu);
        }
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
                Util.pLog("stateOderNum:" + stateOderNum);
                guideInfo.getMyGuideInfo(stateOderNum);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        guideInfo.removeGetInfo();
        handler.removeMessages(99);
        countTraval = 0;
        isInOrder = false;
        isFirstInOrder = true;
        isFirstLoc = true;
        if (canOrderReciver != null)
            lBManager.unregisterReceiver(canOrderReciver);
        if (mLocClient != null && mLocClient.isStarted()) {
            mLocClient.unRegisterLocationListener(this);
            mLocClient.stop();
        }
        mapView.onDestroy();
        currentLalng = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

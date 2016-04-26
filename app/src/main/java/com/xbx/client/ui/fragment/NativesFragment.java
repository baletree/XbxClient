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
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.makeramen.roundedimageview.RoundedImageView;
import com.xbx.client.R;
import com.xbx.client.beans.MyGuideInfoBean;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.GuideParse;
import com.xbx.client.linsener.AnimateFirstDisplayListener;
import com.xbx.client.polling.FindGuide;
import com.xbx.client.polling.MyGuideInfo;
import com.xbx.client.polling.PollUploadLag;
import com.xbx.client.ui.activity.ChoicePeoNumActivity;
import com.xbx.client.utils.Constant;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.StringUtil;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;
import com.xbx.client.view.LoadingDialog;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class NativesFragment extends BasedFragment {
    private static NativesFragment fragment = null;
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
    private LocationMode mCurrentMode = null;
    private GeoCoder geoCoder = null;
    private LatLng cLatlng = null;
    private String[] peopleNum = null;
    private LoadingDialog loadDialog = null;
    private FindGuide findNativePoll = null;
    private MyGuideInfo nativeInfoPoll = null;
    private LocalBroadcastManager lBManager = null;
    private IntentFilter intentFilter = null;
    private CancleOrderReceiver canOrderReciver = null;
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

    public NativesFragment() {
    }

    public static NativesFragment newInstance() {
        if (fragment == null) {
            fragment = new NativesFragment();
        }
        return fragment;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TaskFlag.HTTPERROR:
                case TaskFlag.REQUESTERROR://返回code为0的情况
                    if (loadDialog.isShowing())
                        loadDialog.dismiss();
                    findNativePoll.removeFindGuide();
                    break;
                case 1:
                    if (isVisibleToUser && !isInOrder && !Util.isNull(main_outset_tv.getText().toString())) {
                        api.getNearGuide(cLatlng, nearGuideUrl, Constant.nativeType);
                    }
                    break;
                case 30://超过时间找导游消失对话框并且取消呼叫
                    guide_call_layout.setVisibility(View.GONE);
                    guide_fuc_layout.setVisibility(View.VISIBLE);
                    Util.showToast(getActivity(), getString(R.string.no_guide));
                    if (loadDialog.isShowing())
                        loadDialog.dismiss();
                    break;
                case 99://计时
                    user_stroke_tv.setText(getString(R.string.stork_time) + StringUtil.getCountTravel((guideInfoBean.getCurrentTime() * 1000 + countTraval * 1000) - guideInfoBean.getStartTime() * 1000));
                    handler.sendEmptyMessageDelayed(99, 1000);
                    countTraval = countTraval + 1;
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
                case TaskFlag.PAGEREQUESTHREE://开始呼叫土著,获取周边是否有土著
                    nativeOrderNum = GuideParse.getImmdiaOrder((String) msg.obj);
                    if (Util.isNull(nativeOrderNum))
                        return;
                    findNativePoll.toFindGuide(uid, nativeOrderNum);
                    break;
                case TaskFlag.PAGEREQUESFOUR://系统匹配到了导游
                    Util.showToast(getActivity(), getString(R.string.success_find_native));
                    isInOrder = true;
                    findNativePoll.removeFindGuide();
                    setPageLayout();
                    break;
                case TaskFlag.PAGEREQUESTWO://服务我的土著信息
                    String myGuideData = (String) msg.obj;
                    Util.pLog("该服务土著:isFirstInOrder=" + isFirstInOrder + " " + myGuideData);
                    guideInfoBean = GuideParse.parseMyGuide(myGuideData);
                    if (guideInfoBean != null)
                        if (isFirstInOrder)
                            setMyGuideInfo();
                        else
                            updateGuide();
                    break;
            }
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.frag_guide, container, false);
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
        nativeInfoPoll = new MyGuideInfo(getActivity(),handler);
        uploadLag = new PollUploadLag(getActivity());
        nearGuideUrl = "http://192.168.1.27/yueyou/Api/".concat(getString(R.string.url_nearGuide));
        initView();
    }

    private void initBroadcast(){
        lBManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter.addAction(Constant.ACTION_GCANCELUIDEORDSUC);
        intentFilter.addAction(Constant.ACTION_GUIDEINORDER);
        intentFilter.addAction(Constant.ACTION_GUIDEOVERSERVER);
        canOrderReciver = new CancleOrderReceiver();
        lBManager.registerReceiver(canOrderReciver, intentFilter);
    }


    private void initView() {
        mLocClient.registerLocationListener(this);
        mapView = (TextureMapView) view.findViewById(R.id.guide_map);
        main_outset_tv = (TextView) view.findViewById(R.id.main_outset_tv);
        guide_locatemy_img = (ImageView) view.findViewById(R.id.guide_locatemy_img);
        guide_fuc_layout = (LinearLayout) view.findViewById(R.id.guide_fuc_layout);
        guide_call_rl = (RelativeLayout) view.findViewById(R.id.guide_call_rl);
        guide_call_layout = (LinearLayout) view.findViewById(R.id.guide_call_layout);
        onMap_layout = (RelativeLayout) view.findViewById(R.id.onMap_layout);
        view.findViewById(R.id.guide_reserve_rl).setVisibility(View.GONE);
        guide_tOrder_layout = (RelativeLayout) view.findViewById(R.id.guide_tOrder_layout);
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

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        super.onReceiveLocation(bdLocation);
        mBaiduMap.setMyLocationData(locData);
        if (isFirstLoc && !Util.isNull(bdLocation.getAddrStr())) {
            isFirstLoc = false;
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            cLatlng = ll;
            handler.sendEmptyMessage(1);
            MapStatus mMapstatus = new MapStatus.Builder().target(ll).zoom(19f)
                    .build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(ll));
            mBaiduMap.animateMapStatus(u);
            mCurrentMode = LocationMode.NORMAL;
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    mCurrentMode, true, bdMyself, accuracyCircleFillColor, accuracyCircleStrokeColor));
            Util.pLog("NativeFrag LocateLisen:" + bdLocation.getLatitude() + "," + bdLocation.getLongitude() + " adr:" + bdLocation.getAddrStr() + " isFirstLoc:" + isFirstLoc);
        }
    }

    /**
     * 找到导游后将页面图重置
     */
    private void setPageLayout() {
        nativeInfoPoll.getMyGuideInfo(nativeOrderNum);//请求该土著的个人信息
        guide_tOrder_layout.setVisibility(View.VISIBLE);
        guide_fuc_layout.setVisibility(View.VISIBLE);
        onMap_layout.setVisibility(View.GONE);
        if (loadDialog.isShowing())
            loadDialog.dismiss();
        if (mBaiduMap != null)
            mBaiduMap.clear();
        Intent intent = new Intent();
        intent.putExtra("theOrderNum", nativeOrderNum);
        intent.putExtra("cancelType", 2);
        intent.setAction(Constant.ACTION_GCANCELORD);
        lBManager.sendBroadcast(intent);
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult rGeoCodeResult) {
        if (rGeoCodeResult == null)
            return;
        if (rGeoCodeResult.getPoiList() == null)
            return;
        if (rGeoCodeResult.getPoiList().size() == 0)
            return;
        cLatlng = rGeoCodeResult.getLocation();
        if (!isInOrder) {
            api.getNearGuide(cLatlng, nearGuideUrl, Constant.nativeType);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
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
        switch (v.getId()) {
            case R.id.guide_call_rl:
                api.getSetoffNum();
                break;
            case R.id.guide_call_layout:
                String setOff = main_outset_tv.getText().toString();
                String nativeType = Constant.nativeType;
                if (Util.isNull(setOff)) {
                    Util.showToast(getActivity(), getString(R.string.setoff_null));
                    return;
                }
                loadDialog.setMessage(getString(R.string.find_Native));
                api.hasGuide(uid, outsetJson, nativeType, userNums, "");
                break;
        }
    }

    private void setMyGuideInfo(){
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

    private void updateGuide(){
        if (mMarkerGuide != null)
            mMarkerGuide.setPosition(new LatLng(guideInfoBean.getGuideLat(), guideInfoBean.getGuideLon()));
        if (guideInfoBean.getStartTime() != 0 && isInOrder) {
            handler.sendEmptyMessage(99);
            uploadLag.removeUploadLatlng();
            nativeInfoPoll.removeGetInfo();
            mBaiduMap.clear();
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
                nativeInfoPoll.getMyGuideInfo(stateOderNum);
            }
        }
    }

    /**
     * 取消订单成功后的操作
     */
    private void cancelOrderSuc() {
        mBaiduMap.clear();
        handler.removeMessages(99);
        nativeInfoPoll.removeGetInfo();
        uploadLag.removeUploadLatlng();
        isInOrder = false;
        isFirstInOrder = true;
        countTraval = 0;
        guideInfoBean = null;
        nativeOrderNum = "";
        guide_tOrder_layout.setVisibility(View.GONE);
        onMap_layout.setVisibility(View.VISIBLE);
        guide_call_layout.setVisibility(View.GONE);
        LatLng latLng = SharePrefer.getLatlng(getActivity());
        if (latLng != null) {
            cLatlng = latLng;
            handler.sendEmptyMessage(1);
            MapStatus mMapstatus = new MapStatus.Builder().target(cLatlng).zoom(18f)
                    .build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
            mBaiduMap.setMapStatus(u);
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(cLatlng));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstLoc = true;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
//        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMapLoaded() {

    }
}

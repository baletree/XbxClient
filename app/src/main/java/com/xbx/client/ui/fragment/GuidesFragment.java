package com.xbx.client.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCoder;
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
import com.xbx.client.ui.activity.LoginActivity;
import com.xbx.client.ui.activity.ReservatGuideActivity;
import com.xbx.client.ui.activity.SearchAddressActivity;
import com.xbx.client.ui.activity.TogetherActivity;
import com.xbx.client.utils.Constant;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.StringUtil;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;
import com.xbx.client.view.LoadingDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class GuidesFragment extends BasedFragment implements BaiduMap.OnMarkerClickListener {
    private static GuidesFragment fragment = null;
    private View view = null;
    private TextureMapView mapView;
    private RelativeLayout guide_outset_rl;
    private TextView main_outset_tv; // 出发地
    private RelativeLayout onMap_layout; //出发地和大头针
    private ImageView user_marki_img;//大头针
    private TextView frag_reservat_tv;
    private TextView frag_immedia_tv;
    private RelativeLayout guide_fuc_rl;//预约和即时呼叫按钮布局
    private RelativeLayout guide_call_rl;

    private RelativeLayout guide_tOrder_layout;//呼叫导游成功后
    private RoundedImageView guide_head_img;
    private TextView guide_name_tv; //导游名字
    private TextView guide_code_tv; //导游证号码
    private TextView guide_star_tv;//星星评分
    private RatingBar guide_ratingbar;
    private TextView user_stroke_tv;//行程状态
    private RelativeLayout guide_phone_rl;
    private TextView price_show_tv;

    private TextView call_phone_tv;//用户手机号码

    private GeoCoder geoCoder = null;
    private LoadingDialog loadingDialog = null;
    private LocationMode mCurrentMode;
    private BaiduMap mBaiduMap;
    private LocalBroadcastManager lBManager = null;
    private LatLng currentLalng = null;
    private List<GuideBean> guideList = null;
    private Api api = null;
    private MyGuideInfoBean guideInfoBean = null;
    private View markerView;

    private PollUploadLag uploadLag = null; //上传经纬度
    private FindGuide findGuide = null;// 获取为我服务的导游
    private MyGuideInfo guideInfo = null;// 获取服务于我的导游信息
    private String[] peopleNum = null;
    private CancleOrderReceiver canOrderReciver = null;
    private IntentFilter intentFilter = null;
    private Marker mMarkerGuide = null;//服务于我的导游图标

    private long countTraval = 0;
    private String uid = "";
    private String nearGuideUrl = "";

    private boolean isVisibaleTouser = false;
    private boolean isFirstLoc = true; // 是否是第一次像用户定位
    private boolean isInOrder = false; // 是否处在订单中
    private boolean isFirstInOrder = true;
    private boolean isChangeTab = false;
    private String currentKey = "";
    private String outsetJson = ""; //当前地址的拼接
    private String userNums = ""; // 出行人数选择
    private String getOrderNum = "";
    private String togetherId = "";//随游的Id
    private int guideType = 1;
    private String guidePrice = "";
    private String nativePrice = "";
    private String togetherPrice = "";

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
                case 1://不同状态下获取导游土著的数据
                    if (isVisibaleTouser && !isInOrder && !Util.isNull(main_outset_tv.getText().toString())) {
                        if (currentLalng == null)
                            return;
                        if (guideType == Constant.togetherType)
                            api.getNearTogether(currentLalng.latitude + "," + currentLalng.longitude);
                        else
                            api.getNearGuide(currentLalng, nearGuideUrl, guideType + "");
                    }
                    break;
                case 30://没有找到导游界面重置
                    if (guideType == Constant.guideType)
                        Util.showToast(getActivity(), getString(R.string.no_guide));
                    else if (guideType == Constant.nativeType)
                        Util.showToast(getActivity(), getString(R.string.no_native));
                    else
                        Util.showToast(getActivity(), getString(R.string.no_together));
                    if (loadingDialog != null && loadingDialog.isShowing())
                        loadingDialog.dismiss();
                    findGuide.removeFindGuide();
                    break;
                case 99://服务的人开始服务后开始计时
                    user_stroke_tv.setText(getString(R.string.stork_time) + StringUtil.getCountTravel((guideInfoBean.getCurrentTime() * 1000 + countTraval * 1000) - guideInfoBean.getStartTime() * 1000));
                    handler.sendEmptyMessageDelayed(99, 1000);
                    countTraval = countTraval + 1;
                    break;
                case TaskFlag.REQUESTSUCCESS://解析导游土著和随游的数据列表
                    String guideData = (String) msg.obj;
                    if (Util.isNull(guideData))
                        return;
                    guideList = GuideParse.getGuideList(guideData);
                    Util.pLog("导游土著随游返回的数据：" + guideData + " guideList == null ? " + (guideList == null));
                    if (guideList == null)
                        return;
                    addOverlyGuide();
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
                case TaskFlag.PAGEREQUESTHREE://开始呼叫导游,获取周边是否有导游
                    getOrderNum = GuideParse.getImmdiaOrder((String) msg.obj);
                    Util.pLog("oderNum:" + getOrderNum);
                    if (Util.isNull(getOrderNum))
                        return;
                    loadingDialog.setCount(handler);
                    findGuide.toFindGuide(uid, getOrderNum);
                    break;
                case TaskFlag.PAGEREQUESFOUR://系统匹配到了导游
                    Util.showToast(getActivity(), getString(R.string.success_find_guide));
                    findGuide.removeFindGuide();
                    isInOrder = true;
                    setPageLayout();
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
                case TaskFlag.RMOVEHANDLERONE:
                    handler.removeMessages(1);
                    break;
                case TaskFlag.HTTPERROR:
                case TaskFlag.CODEZERO:
                    if (loadingDialog != null && loadingDialog.isShowing())
                        loadingDialog.dismiss();
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
        api = new Api(getActivity(), handler);
        uploadLag = new PollUploadLag(getActivity());
        findGuide = new FindGuide(getActivity(), handler);
        guideInfo = new MyGuideInfo(getActivity(), handler);
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
        geoCoder = GeoCoder.newInstance();
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.setMessage(getString(R.string.call_sure_tips));
        lBManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_GUIDEOVERSERVER);
        canOrderReciver = new CancleOrderReceiver();
        lBManager.registerReceiver(canOrderReciver, intentFilter);
        nearGuideUrl = getString(R.string.url_conIp).concat(getString(R.string.url_nearGuide));
        initViews();
    }

    protected void initViews() {
        mapView = (TextureMapView) view.findViewById(R.id.guide_map);
        guide_outset_rl = (RelativeLayout) view.findViewById(R.id.guide_outset_rl);
        main_outset_tv = (TextView) view.findViewById(R.id.main_outset_tv);
        view.findViewById(R.id.guide_locatemy_img).setOnClickListener(this);
        user_marki_img = (ImageView) view.findViewById(R.id.user_marki_img);

        guide_tOrder_layout = (RelativeLayout) view.findViewById(R.id.guide_tOrder_layout);
        frag_reservat_tv = (TextView) view.findViewById(R.id.frag_reservat_tv);
        frag_immedia_tv = (TextView) view.findViewById(R.id.frag_immedia_tv);
        guide_fuc_rl = (RelativeLayout) view.findViewById(R.id.guide_fuc_rl);
        guide_call_rl = (RelativeLayout) view.findViewById(R.id.guide_call_rl);
        price_show_tv = (TextView) view.findViewById(R.id.price_show_tv);

        guide_head_img = (RoundedImageView) view.findViewById(R.id.guide_head_img);
        guide_name_tv = (TextView) view.findViewById(R.id.guide_name_tv);
        guide_code_tv = (TextView) view.findViewById(R.id.guide_code_tv);
        guide_star_tv = (TextView) view.findViewById(R.id.guide_star_tv);
        user_stroke_tv = (TextView) view.findViewById(R.id.user_stroke_tv);
        guide_ratingbar = (RatingBar) view.findViewById(R.id.guide_ratingbar);
        guide_phone_rl = (RelativeLayout) view.findViewById(R.id.guide_phone_rl);
        onMap_layout = (RelativeLayout) view.findViewById(R.id.onMap_layout);
        guide_call_rl.setVisibility(View.GONE);
        call_phone_tv = (TextView) view.findViewById(R.id.call_phone_tv);

        mBaiduMap = mapView.getMap();
        mapView.showZoomControls(false); //隐藏缩放控件
        LocationBean locationBean = SharePrefer.getLocate(getActivity());
        if (locationBean != null && !Util.isNull(locationBean.getLat())) {
            currentLalng = new LatLng(Double.parseDouble(locationBean.getLat()), Double.parseDouble(locationBean.getLon()));
            MapStatus mMapstatus = new MapStatus.Builder().target(currentLalng).zoom(17f)
                    .build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
            mBaiduMap.setMapStatus(u);
        }
        initLisener();
    }

    protected void initLisener() {
        guide_outset_rl.setOnClickListener(this);
        frag_immedia_tv.setOnClickListener(this);
        frag_reservat_tv.setOnClickListener(this);
        guide_head_img.setOnClickListener(this);
        guide_phone_rl.setOnClickListener(this);
        view.findViewById(R.id.confirm_call_btn).setOnClickListener(this);

        mLocClient.registerLocationListener(this);
        guide_tOrder_layout.setVisibility(View.GONE);
        mapView.getMap().setOnMapLoadedCallback(this);
        mapView.getMap().setOnMapStatusChangeListener(this);
        geoCoder.setOnGetGeoCodeResultListener(this);
        mBaiduMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case outsetReques: // 出发地
                PoiInfo poiInfo = data.getParcelableExtra("outsetResult");
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
            case callGuideReques://人数选择
                userNums = data.getStringExtra("userNumType");
                if (Util.isNull(userNums))
                    return;
                /*data.setAction(Constant.ACTION_CALLGUIDEBTN);//告知主页面返回按钮出现
                lBManager.sendBroadcast(data);
                guide_fuc_rl.setVisibility(View.GONE);
                guide_call_rl.setVisibility(View.VISIBLE);*/
                break;
            case choiceTogether://随游选择某个随游后选择人数
                togetherId = data.getStringExtra("TogetherId");
                togetherPrice = data.getStringExtra("TogetherPrice");
                price_show_tv.setText(togetherPrice);
                if (Util.isNull(togetherId))
                    return;
                lBManager.sendBroadcast(new Intent(Constant.ACTION_CALLGUIDEBTN));
                guide_fuc_rl.setVisibility(View.GONE);
                guide_call_rl.setVisibility(View.VISIBLE);
//                api.getSetoffNum();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        uid = SharePrefer.getUserInfo(getActivity()).getUid();
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.guide_locatemy_img:
                LatLng latLng = SharePrefer.getLatlng(getActivity());
                if (latLng != null) {
                    MapStatus mMapstatus = new MapStatus.Builder().target(latLng).zoom(18f)
                            .build();
                    MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
                    mBaiduMap.animateMapStatus(u, 1000);
                    geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                            .location(latLng));
                }
                break;
            case R.id.guide_outset_rl://出发地
                intent.setClass(getActivity(), SearchAddressActivity.class);
                intent.putExtra("guide_code", Constant.outsetFlag);
                startActivityForResult(intent, outsetReques);
                break;
            case R.id.frag_reservat_tv://预约导游、随游
                if (Util.isNull(uid)) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                intent.setClass(getActivity(), ReservatGuideActivity.class);
                intent.putExtra("reservatGuideType", guideType + "");
                startActivity(intent);
                break;
            case R.id.frag_immedia_tv:
                if (Util.isNull(uid)) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                call_phone_tv.setText("+86 " + SharePrefer.getUserInfo(getActivity()).getUserPhone());
                if (guideType == Constant.togetherType) {
                    if (guideList != null && guideList.size() > 0) {
                        intent.setClass(getActivity(), TogetherActivity.class);
                        intent.putExtra("TogetherList", (Serializable) guideList);
                        startActivityForResult(intent, choiceTogether);
                    } else
                        Util.showToast(getActivity(), getString(R.string.no_hasTogether));
                } else {
                    lBManager.sendBroadcast(new Intent(Constant.ACTION_CALLGUIDEBTN));
                    guide_fuc_rl.setVisibility(View.GONE);
                    guide_call_rl.setVisibility(View.VISIBLE);
                }
//                    api.getSetoffNum();
                break;
            case R.id.confirm_call_btn://确认呼叫导游
                if (Util.isNull(uid)) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                String setOff = main_outset_tv.getText().toString();
                if (Util.isNull(setOff)) {
                    Util.showToast(getActivity(), getString(R.string.setoff_null));
                    return;
                }
                loadingDialog.show();
                if (guideType == Constant.togetherType)
                    api.hasGuide(uid, outsetJson, guideType + "", userNums, togetherId);
                else
                    api.hasGuide(uid, outsetJson, guideType + "", userNums, "");
                break;
            case R.id.guide_head_img://头像

                break;
            case R.id.guide_phone_rl://电话
                String guidePhone = guideInfoBean.getGuidePhone();
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + guidePhone);
                intentCall.setData(data);
                startActivity(intentCall);
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
        if (!isInOrder) {//没有处在订单当中并且
            handler.sendEmptyMessage(1);
        }
        currentLalng = rGeoCodeResult.getLocation();
        currentKey = rGeoCodeResult.getPoiList().get(0).name;
        main_outset_tv.setText(currentKey);
        outsetJson = rGeoCodeResult.getLocation().latitude + "," + rGeoCodeResult.getLocation().longitude + "," + currentKey;
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
        if (isChangeTab)
            return;
        main_outset_tv.setText("");
        main_outset_tv.setHint(getString(R.string.loading_locate));
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        if (isChangeTab)
            return;
        Util.pLog("onMapStatusChangeFinish...");
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(mapStatus.target));
    }

    /**
     * 将附近的导游添加到地图上
     */
    private void addOverlyGuide() {
        mBaiduMap.clear();
        if (isChangeTab) {
            MapStatus mMapstatus = new MapStatus.Builder().target(currentLalng).zoom(18f)
                    .build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
            mBaiduMap.animateMapStatus(u, 1000);
            main_outset_tv.setText(currentKey);
            isChangeTab = false;
        }
        for (int i = 0; i < guideList.size(); i++) {
            final GuideBean guideBean = guideList.get(i);
            markerView = LayoutInflater.from(getActivity()).inflate(R.layout.pin_inmap, null);
            final RoundedImageView markerImg = (RoundedImageView) markerView.findViewById(R.id.pinHeaded_img);
            final ImageView markerBg = (ImageView) markerView.findViewById(R.id.pinBg_img);
            markerBg.setImageResource(R.mipmap.pin_together);
            final int finalI = i;
            imageLoader.displayImage(guideBean.getGuideHead(), markerImg, configFactory.getHeadImg(), new AnimateFirstDisplayListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    LatLng latLng = new LatLng(guideBean.getLatitude(), guideBean.getLongitude());
                    if (guideType == Constant.togetherType) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("showCurrentIndex", finalI);
                        markerBg.setImageResource(R.mipmap.pin_together);
                        guideBdp = BitmapDescriptorFactory.fromBitmap(Util.getBitmapFromView(markerView));
                        MarkerOptions mos = new MarkerOptions().position(latLng).icon(guideBdp)
                                .zIndex(9).draggable(true).extraInfo(bundle);
                        mBaiduMap.addOverlay(mos);
                    } else {
                        if (guideType == Constant.guideType)
                            markerBg.setImageResource(R.mipmap.pin_guide);
                        else
                            markerBg.setImageResource(R.mipmap.pin_native);
                        guideBdp = BitmapDescriptorFactory.fromBitmap(Util.getBitmapFromView(markerView));
                        MarkerOptions mos = new MarkerOptions().position(latLng).icon(guideBdp)
                                .zIndex(9).draggable(true);
                        mBaiduMap.addOverlay(mos);
                    }
                    mapView.invalidate();
                }
            });
        }
    }

    /**
     * 设置为我服务的导游信息
     */
    private void setMyGuideInfo() {
        isFirstInOrder = false;
        Intent intent = new Intent();
        if (guideInfoBean.getStartTime() == 0) {
            uploadLag.uploadLatlng();
            intent.putExtra("theOrderNum", getOrderNum);
            intent.putExtra("cancelType", 1);
            intent.setAction(Constant.ACTION_GCANCELORD);
        } else {
            intent.setAction(Constant.ACTION_USERINORDER);
        }
        lBManager.sendBroadcast(intent);
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
            handler.sendEmptyMessage(99);
            uploadLag.removeUploadLatlng();
            guideInfo.removeGetInfo();
            mBaiduMap.clear();
            Intent intent = new Intent();
            intent.setAction(Constant.ACTION_USERINORDER);
            lBManager.sendBroadcast(intent);
            notifyUserStartStroke();
        }
    }

    /**
     * 找到导游后将页面图重置
     */
    private void setPageLayout() {
        guideInfo.getMyGuideInfo(getOrderNum);
        guide_tOrder_layout.setVisibility(View.VISIBLE);
        guide_fuc_rl.setVisibility(View.GONE);
        onMap_layout.setVisibility(View.GONE);
        guide_call_rl.setVisibility(View.GONE);
        lBManager.sendBroadcast(new Intent(Constant.ACTION_DISSMISSBACK));
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
        if (mBaiduMap != null)
            mBaiduMap.clear();
    }

    /**
     * 取消订单成功后的操作
     */
    public void cancelOrderSuc(boolean isRestmap) {
        resetParams();
        handler.removeMessages(99);
        guideInfo.removeGetInfo();
        uploadLag.removeUploadLatlng();
        if (!isRestmap)
            return;
        mBaiduMap.clear();
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
     * 取消订单后数据参数和界面重置
     */
    private void resetParams() {
        countTraval = 0;
        isInOrder = false;
        isFirstInOrder = true;
        getOrderNum = "";
        togetherId = "";
        userNums = "";
        guideInfoBean = null;
        guideInfoBean = null;
        guide_tOrder_layout.setVisibility(View.GONE);
        guide_fuc_rl.setVisibility(View.VISIBLE);
        onMap_layout.setVisibility(View.VISIBLE);
        guide_call_rl.setVisibility(View.GONE);
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
            MapStatus mMapstatus = new MapStatus.Builder().target(ll).zoom(19f)
                    .build();
            MapStatusUpdate mu = MapStatusUpdateFactory.newMapStatus(mMapstatus);
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(ll));
            mCurrentMode = LocationMode.NORMAL;
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    mCurrentMode, true, bdMyself, accuracyCircleFillColor, accuracyCircleStrokeColor));
            mBaiduMap.animateMapStatus(mu, 1000);
            mBaiduMap.setMyLocationEnabled(true);//开启定位图层
            if (currentLalng == null) {
                currentLalng = ll;
                handler.sendEmptyMessage(1);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        uid = SharePrefer.getUserInfo(getActivity()).getUid();
        if (Util.isNull(uid)) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return false;
        }
        if (guideType == Constant.togetherType) {
            Intent intent = new Intent(getActivity(), TogetherActivity.class);
            intent.putExtra("TogetherList", (Serializable) guideList);
            intent.putExtra("TegetherSelect", marker.getExtraInfo().getInt("showCurrentIndex", 0));
            startActivity(intent);
        }
        return true;
    }

    class CancleOrderReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.ACTION_GUIDEOVERSERVER.equals(action)) {//结束行程
                cancelOrderSuc(true);
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
        mapView.onPause();
    }

    /**
     * 在MainActivity中切换导航栏的状态显示
     *
     * @param tabPosition
     */
    public void setPageChange(int tabPosition) {
        isChangeTab = true;
        if (!Util.isNull(userNums)) {
            guide_fuc_rl.setVisibility(View.GONE);
            guide_call_rl.setVisibility(View.VISIBLE);
        }
        switch (tabPosition) {
            case 0:
                guideType = Constant.guideType;
                frag_reservat_tv.setVisibility(View.VISIBLE);
                frag_immedia_tv.setText(getString(R.string.main_call));
                frag_reservat_tv.setText(getString(R.string.main_reserve));
                loadingDialog = new LoadingDialog(getActivity());
                loadingDialog.setMessage(getString(R.string.call_sure_tips));
                break;
            case 1:
                guideType = Constant.nativeType;
                frag_reservat_tv.setVisibility(View.GONE);
                frag_immedia_tv.setText(getString(R.string.immedia_native));
                loadingDialog = new LoadingDialog(getActivity());
                loadingDialog.setMessage(getString(R.string.find_Native));
                break;
            case 2:
                guideType = Constant.togetherType;
                frag_reservat_tv.setVisibility(View.VISIBLE);
                frag_immedia_tv.setText(getString(R.string.immedia_together));
                frag_reservat_tv.setText(getString(R.string.reservat_together));
                loadingDialog = new LoadingDialog(getActivity());
                loadingDialog.setMessage(getString(R.string.find_Togeher));
                if (Util.isNull(togetherId)) {
                    guide_fuc_rl.setVisibility(View.VISIBLE);
                    guide_call_rl.setVisibility(View.GONE);
                }
                break;
        }
        resetMapview();
    }

    /**
     * 切换导导游、土著和随游的缩放
     */
    private void resetMapview() {
        guideList = new ArrayList<>();
        mBaiduMap.clear();
        if (currentLalng != null) {
            MapStatus mMapstatus = new MapStatus.Builder().target(currentLalng).zoom(17f)
                    .build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
            mBaiduMap.animateMapStatus(u, 1000);
            Util.pLog("currentLalng:" + currentLalng.latitude + "," + currentLalng.longitude);
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(currentLalng));
        }
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.pin_anim);
        user_marki_img.startAnimation(animation);
    }

    /**
     * MainActivity中判断有订单则进入订单
     */
    public void setPageInOrder(String tranOrderNum) {
        isInOrder = true;
        guide_tOrder_layout.setVisibility(View.VISIBLE);
        guide_call_rl.setVisibility(View.GONE);
        guide_fuc_rl.setVisibility(View.GONE);
        onMap_layout.setVisibility(View.GONE);
        getOrderNum = tranOrderNum;
        guideInfo.getMyGuideInfo(getOrderNum);
    }
}

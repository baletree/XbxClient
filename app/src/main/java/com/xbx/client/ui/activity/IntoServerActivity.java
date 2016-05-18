package com.xbx.client.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.makeramen.roundedimageview.RoundedImageView;
import com.xbx.client.R;
import com.xbx.client.beans.CancelInfoBean;
import com.xbx.client.beans.MyGuideInfoBean;
import com.xbx.client.beans.UserInfo;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.GuideParse;
import com.xbx.client.jsonparse.OrderParse;
import com.xbx.client.jsonparse.UtilParse;
import com.xbx.client.linsener.AnimateFirstDisplayListener;
import com.xbx.client.polling.MyGuideInfo;
import com.xbx.client.polling.PollUploadLag;
import com.xbx.client.utils.Constant;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;
import com.xbx.client.view.TipsDialog;

/**
 * Created by EricYuan on 2016/5/12.
 * 正在服务的页面
 */
public class IntoServerActivity extends BaseAppCompatActivity implements BDLocationListener, TipsDialog.DialogClickListener {
    private ImageView title_left_img;
    private TextView rightTxt;
    private DrawerLayout drawerLayout;
    private RoundedImageView guide_head_img;
    private TextView guide_name_tv; //导游名字
    private TextView guide_code_tv; //导游证号码
    private TextView guide_star_tv;//星星评分
    private RatingBar guide_ratingbar;
    private TextView user_stroke_tv;//行程状态
    private TextureMapView mapView;

    private RoundedImageView menu_head_img;
    private TextView menu_name_tv;
    private TextView menu_phone_tv;

    private UserInfo userInfo = null;
    private Api api = null;
    private MyGuideInfoBean guideInfoBean = null;
    public BitmapDescriptor guideDes = null;
    public BitmapDescriptor bdMyself = null;
    public LocationClient mLocClient;
    public LocationClientOption option = null;
    private BaiduMap mBaiduMap;
    private Marker mMarkerGuide = null;//服务于我的导游图标
    private PollUploadLag uploadLag = null; //上传经纬度
    private MyGuideInfo guideInfo = null;// 获取服务于我的导游信息
    private TipsDialog tipsDialog = null;
    private LocalBroadcastManager lBManager = null;
    private OrderReceiver canOrderReciver = null;
    private IntentFilter intentFilter = null;

    private boolean isFirstInOrder = true;
    private boolean isFirstLoc = true;
    private String orderNums = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TaskFlag.PAGEREQUESTWO://服务我的导游信息
                    String myGuideData = (String) msg.obj;
                    guideInfoBean = GuideParse.parseMyGuide(myGuideData);
                    if (guideInfoBean != null) {
                        setGuideInfo();
                    }
                    break;
                case TaskFlag.PAGEREQUESFIVE://订单取消成功
                    String allData = (String) msg.obj;
                    if (Util.isNull(allData)) {
                        return;
                    }
                    int codes = UtilParse.getRequestCode(allData);
                    Intent intent = new Intent();
                    switch (codes) {
                        case 1:
                            intent.setClass(IntoServerActivity.this, MainActivity.class);
                            startActivity(intent);
                            break;
                        case 2:
                            CancelInfoBean cancelInfoBean = OrderParse.getCancelInfo(UtilParse.getRequestData(allData));
                            if (cancelInfoBean == null)
                                return;
                            if (Util.isNull(cancelInfoBean.getCancelPay()))
                                return;
                            intent.putExtra("cancelSucInfo", cancelInfoBean);
                            intent.setClass(IntoServerActivity.this, CancelOrderSucActivity.class);
                            startActivity(intent);
                            break;
                        case 3:
                            break;
                    }
                    Util.showToast(IntoServerActivity.this, UtilParse.getRequestMsg(allData));
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_into_server);
        initDatas();
    }

    private void initDatas() {
        api = new Api(this, handler);
        orderNums = getIntent().getStringExtra("IntoServerOrderNum");
//        api.getMyGuideInfo(orderNums);
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_GUIDEOVERSERVER);
        lBManager = LocalBroadcastManager.getInstance(this);
        canOrderReciver = new OrderReceiver();
        lBManager.registerReceiver(canOrderReciver, intentFilter);
        guideInfo = new MyGuideInfo(IntoServerActivity.this, handler);
        guideInfo.getMyGuideInfo(orderNums);
        initBDinfo();
        initViews();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.title_txt_tv)).setText(getString(R.string.main_title));
        title_left_img = (ImageView) findViewById(R.id.title_left_img);
        title_left_img.setImageResource(R.mipmap.main_menu);
        rightTxt = (TextView) findViewById(R.id.title_rtxt_tv);
        rightTxt.setText(getString(R.string.cancel_order));
        drawerLayout = (DrawerLayout) findViewById(R.id.server_drawerLayout);
        drawerLayout.setScrimColor(0xbe000000);// 设置半透明度

        mapView = (TextureMapView) findViewById(R.id.server_map);
        guide_head_img = (RoundedImageView) findViewById(R.id.guide_head_img);
        guide_name_tv = (TextView) findViewById(R.id.guide_name_tv);
        guide_code_tv = (TextView) findViewById(R.id.guide_code_tv);
        guide_star_tv = (TextView) findViewById(R.id.guide_star_tv);
        user_stroke_tv = (TextView) findViewById(R.id.user_stroke_tv);
        user_stroke_tv.setVisibility(View.GONE);
        guide_ratingbar = (RatingBar) findViewById(R.id.guide_ratingbar);
        menu_head_img = (RoundedImageView) findViewById(R.id.menu_head_img);
        menu_name_tv = (TextView) findViewById(R.id.menu_name_tv);
        menu_phone_tv = (TextView) findViewById(R.id.menu_phone_tv);
        title_left_img.setOnClickListener(this);
        rightTxt.setOnClickListener(this);
        findViewById(R.id.menu_userInfo_layout).setOnClickListener(this);
        findViewById(R.id.menu_order_layout).setOnClickListener(this);
        findViewById(R.id.guide_phone_rl).setOnClickListener(this);
        findViewById(R.id.menu_setting_layout).setOnClickListener(this);
        findViewById(R.id.menu_recruit_layout).setOnClickListener(this);
        findViewById(R.id.menu_msg_layout).setOnClickListener(this);
        mBaiduMap = mapView.getMap();
        mapView.showZoomControls(false); //隐藏缩放控件
        setUserInfo();
    }

    private void initBDinfo() {
        mLocClient = new LocationClient(this);
        option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(2000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        bdMyself = BitmapDescriptorFactory
                .fromResource(R.mipmap.myself_locate);
        guideDes = BitmapDescriptorFactory
                .fromResource(R.mipmap.guide_locate);
        mLocClient.registerLocationListener(this);
    }

    private void setUserInfo() {
        userInfo = SharePrefer.getUserInfo(this);
        if (userInfo == null)
            return;
        imageLoader.displayImage(userInfo.getUserHead(), menu_head_img, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        if (!Util.isNull(userInfo.getNickName()))
            menu_name_tv.setText(userInfo.getNickName());
        if (!Util.isNull(userInfo.getUserPhone())) {
            String num = userInfo.getUserPhone();
            String str = num.substring(3, 6);
            menu_phone_tv.setText(num.replaceAll(str, "****"));
        }
        LatLng latLng = SharePrefer.getLatlng(this);
        if (latLng != null) {
            MapStatus mMapstatus = new MapStatus.Builder().target(latLng).zoom(17f)
                    .build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
            mBaiduMap.setMapStatus(u);
        }
    }

    private void setGuideInfo() {
        Util.pLog("getStartTime:" + guideInfoBean.getStartTime());
        LatLng guideLl = new LatLng(guideInfoBean.getGuideLat(), guideInfoBean.getGuideLon());
        if (isFirstInOrder) {
            imageLoader.displayImage(guideInfoBean.getGuideHeadImg(), guide_head_img, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
            guide_name_tv.setText(guideInfoBean.getGuideName());
            guide_code_tv.setText(guideInfoBean.getGuideNum());
            guide_star_tv.setText(guideInfoBean.getGuideStarts() + getString(R.string.scole));
            if (!Util.isNull(guideInfoBean.getGuideStarts()))
                guide_ratingbar.setRating(Float.valueOf(guideInfoBean.getGuideStarts()));
            if (guideInfoBean.getStartTime() != 0) {
                rightTxt.setVisibility(View.GONE);
                guideInfo.removeGetInfo();
                mBaiduMap.clear();
            } else {
                rightTxt.setVisibility(View.VISIBLE);
                MarkerOptions ooA = new MarkerOptions().position(guideLl).icon(guideDes)
                        .zIndex(9).draggable(true);
                mMarkerGuide = (Marker) mBaiduMap.addOverlay(ooA);
            }
            isFirstInOrder = false;
        } else {
            if (mMarkerGuide != null) {
                mMarkerGuide.setPosition(guideLl);
            }
        }
        if (guideInfoBean.getStartTime() != 0) {
            rightTxt.setVisibility(View.GONE);
            guideInfo.removeGetInfo();
            mBaiduMap.clear();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }
        super.onBackPressed();
    }

    /**
     * 改变左侧边栏打开状态
     */
    public void toggleLeftLayout() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.guide_phone_rl:
                String guidePhone = guideInfoBean.getGuidePhone();
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + guidePhone);
                intentCall.setData(data);
                startActivity(intentCall);
                return;
            case R.id.title_rtxt_tv:
                initCancelDialog();
                return;
        }
        toggleLeftLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null) {
            return;
        }
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
        SharePrefer.saveLatlng(this, bdLocation.getLongitude() + "", bdLocation.getLatitude() + "");
        if (isFirstLoc && !Util.isNull(bdLocation.getAddrStr())) {
            isFirstLoc = false;
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatus mMapstatus = new MapStatus.Builder().target(ll).zoom(18f)
                    .build();
            MapStatusUpdate mu = MapStatusUpdateFactory.newMapStatus(mMapstatus);
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, bdMyself, 0x00000000, 0x00000000));
            mBaiduMap.animateMapStatus(mu, 1000);
            mBaiduMap.setMyLocationEnabled(true);//开启定位图层
        }
    }

    private void initCancelDialog() {
        tipsDialog = new TipsDialog(this);
        tipsDialog.setBtnTxt(getString(R.string.cancel_stroke), getString(R.string.yes_stroke));
        tipsDialog.setClickListener(this);
        tipsDialog.show();
    }

    class OrderReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.ACTION_GUIDEOVERSERVER.equals(action)) {//结束行程
                finish();
            }
        }
    }

    @Override
    public void cancelDialog() {
        api.cancelOrder(orderNums);
        tipsDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (canOrderReciver != null)
            lBManager.unregisterReceiver(canOrderReciver);
        mapView.onDestroy();
    }

    @Override
    public void confirmDialog() {
        tipsDialog.dismiss();
    }
}

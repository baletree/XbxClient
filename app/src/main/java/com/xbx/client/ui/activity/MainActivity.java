package com.xbx.client.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx.client.R;
import com.xbx.client.adapter.MyViewPagerAdapter;
import com.xbx.client.beans.CancelInfoBean;
import com.xbx.client.beans.TogetherBean;
import com.xbx.client.beans.UserInfo;
import com.xbx.client.beans.UserStateBean;
import com.xbx.client.http.Api;
import com.xbx.client.http.IRequest;
import com.xbx.client.http.RequestParams;
import com.xbx.client.jsonparse.MainStateParse;
import com.xbx.client.jsonparse.OrderParse;
import com.xbx.client.jsonparse.UserInfoParse;
import com.xbx.client.jsonparse.UtilParse;
import com.xbx.client.linsener.AnimateFirstDisplayListener;
import com.xbx.client.linsener.ImageLoaderConfigFactory;
import com.xbx.client.ui.fragment.BowenFragment;
import com.xbx.client.ui.fragment.GuidesFragment;
import com.xbx.client.ui.fragment.NativesFragment;
import com.xbx.client.ui.fragment.TogetherFragment;
import com.xbx.client.ui.fragment.WithtourFragment;
import com.xbx.client.utils.Constant;
import com.xbx.client.linsener.RequestBackLisener;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;
import com.xbx.client.view.BanSlideViewpager;
import com.xbx.client.view.TipsDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.SocketHandler;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 控件名称定义
     */
    private DrawerLayout drawerLayout;
    private ImageView test_toggle;
    private Toolbar mToolbar;
    private TabLayout tabLayout;
    private BanSlideViewpager viewpager;
    private ImageView main_menu_img;
    private TextView cancel_order_tv;
    private LinearLayout menu_order_layout; //我的订单
    private RelativeLayout menu_userInfo_layout;// 个人中心
    private ImageView main_back_img;
    private RoundedImageView menu_head_img;
    private TextView menu_name_tv;
    private TextView menu_phone_tv;

    private MyViewPagerAdapter viewPagerAdapter = null;

    private GuidesFragment guidesFragment = null;
    private NativesFragment nativesFragment = null;
    private WithtourFragment withtourFragment = null;
    private TogetherFragment togetherFragment = null;
    private BowenFragment bowenFragment = null;

    private LocalReceiver localReceiver = null;
    private LocalBroadcastManager lBManager = null;
    private IntentFilter intentFilter = null;
    private Api api = null;
    private UserInfo userInfo = null;
    private ImageLoader imageLoader;
    private ImageLoaderConfigFactory configFactory;

    private TipsDialog tipsDialog = null;
    private boolean isFromLogin = false;
    private int cancelType = 0; //1-导游 2-土著  3-随游
    private String orderNum = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            switch (msg.what) {
                case TaskFlag.PAGEREQUESFIVE: //取消订单成功
                    String allData = (String) msg.obj;
                    if (Util.isNull(allData)) {
                        return;
                    }
                    cancelType = 0;
                    orderNum = "";
                    cancel_order_tv.setVisibility(View.GONE);
                    int codes = UtilParse.getRequestCode(allData);
                    switch (codes) {
                        case 1:
                            guidesFragment.cancelOrderSuc(true);
                            break;
                        case 2:
                            guidesFragment.cancelOrderSuc(true);
                            CancelInfoBean cancelInfoBean = OrderParse.getCancelInfo(UtilParse.getRequestData(allData));
                            if (cancelInfoBean == null)
                                return;
                            if (Util.isNull(cancelInfoBean.getCancelPay()))
                                return;
                            intent.putExtra("cancelSucInfo", cancelInfoBean);
                            intent.setClass(MainActivity.this, CancelOrderSucActivity.class);
                            startActivity(intent);
                            break;
                        case 3:
                            break;
                    }
                    Util.showToast(MainActivity.this, UtilParse.getRequestMsg(allData));
                    break;
                case TaskFlag.REQUESTSUCCESS://验证登录的时候如果有状态就显示状态
                    String checkData = (String) msg.obj;
                    if (UtilParse.getRequestCode(checkData) == 0) {
                        Util.showToast(MainActivity.this, getString(R.string.login_fail));
                        SharePrefer.saveUserInfo(MainActivity.this, new UserInfo());
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    } else if (UtilParse.getRequestCode(checkData) == 1) {
                        UserInfo userInfo = SharePrefer.getUserInfo(MainActivity.this);
                        MainStateParse.resetToken(MainActivity.this, userInfo, UtilParse.getRequestData(checkData));
                        String dataType = MainStateParse.checkDataType(UtilParse.getRequestData(checkData));
                        if (Util.isNull(dataType))
                            return;
                        UserStateBean stateBean = MainStateParse.checkUserState(UtilParse.getRequestData(checkData), dataType);
                        if (stateBean == null)
                            return;
                        if (Util.isNull(stateBean.getOrderNum()))
                            return;
                        dealUserState(intent, stateBean, dataType);
                    }
                    break;
            }
        }
    };

    private void dealUserState(Intent intent, UserStateBean stateBean, String stateKey) {
        intent.putExtra("stateOrderNumber", stateBean.getOrderNum());
        orderNum = stateBean.getOrderNum();
        if (stateKey.equals("unpay")) {
            intent.setClass(MainActivity.this, OrderDetailActivity.class);
            startActivity(intent);
        } else if (stateKey.equals("uncomment")) {
            intent.putExtra("GuideOrderNum", stateBean.getOrderNum());
            intent.setClass(MainActivity.this, SubCommentActivity.class);
            startActivity(intent);
        } else if (stateKey.equals("going")) {
            guidesFragment.setPageInOrder(orderNum);
            cancel_order_tv.setVisibility(View.VISIBLE);
            switch (stateBean.getGuideType()) {
                case 1:
                    tabLayout.getTabAt(0).select();
                    viewpager.setCurrentItem(0);
                    break;
                case 2://随游
                    tabLayout.getTabAt(2).select();
                    viewpager.setCurrentItem(0);
                    break;
                case 3://土著
                    tabLayout.getTabAt(1).select();
                    viewpager.setCurrentItem(0);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        initDatas();
    }

    protected void initDatas() {
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
        userInfo = SharePrefer.getUserInfo(MainActivity.this);
        api = new Api(MainActivity.this, handler);
        if (userInfo != null && !Util.isNull(userInfo.getUid()))
            api.checkLoginState();
        lBManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        String registerId = JPushInterface.getRegistrationID(this);
        Set<String> tagSet = new LinkedHashSet<>();
        tagSet.add(registerId);
        JPushInterface.setTags(this, tagSet, null);
        initViews();
    }

    protected void initViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawerLayout);
        drawerLayout.setScrimColor(0xbe000000);// 设置半透明度
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewpager = (BanSlideViewpager) findViewById(R.id.viewpager);
        main_menu_img = (ImageView) findViewById(R.id.main_menu_img);
        cancel_order_tv = (TextView) findViewById(R.id.cancel_order_tv);
        menu_order_layout = (LinearLayout) findViewById(R.id.menu_order_layout);
        menu_userInfo_layout = (RelativeLayout) findViewById(R.id.menu_userInfo_layout);
        main_back_img = (ImageView) findViewById(R.id.main_back_img);
        menu_head_img = (RoundedImageView) findViewById(R.id.menu_head_img);
        menu_name_tv = (TextView) findViewById(R.id.menu_name_tv);
        menu_phone_tv = (TextView) findViewById(R.id.menu_phone_tv);
        viewpager.setScrollble(false);
        viewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), this);
        findViewById(R.id.menu_setting_layout).setOnClickListener(this);
        findViewById(R.id.menu_recruit_layout).setOnClickListener(this);
        findViewById(R.id.menu_msg_layout).setOnClickListener(this);
        main_menu_img.setOnClickListener(this);
        main_back_img.setOnClickListener(this);
        cancel_order_tv.setOnClickListener(this);
        menu_order_layout.setOnClickListener(this);
        menu_userInfo_layout.setOnClickListener(this);
        setUserInfo();
        initControls();
        initBroadcast();
    }

    private void setUserInfo() {
        if (userInfo == null)
            return;
        imageLoader.displayImage(userInfo.getUserHead(), menu_head_img, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        if (!Util.isNull(userInfo.getNickName()))
            menu_name_tv.setText(userInfo.getNickName());
        if (!Util.isNull(userInfo.getUserPhone()))
            menu_phone_tv.setText(userInfo.getUserPhone());
    }

    private void initBroadcast() {
        intentFilter.addAction(Constant.ACTION_GCANCELORD);
        intentFilter.addAction(Constant.ACTION_GUIDEOVERSERVER);
        intentFilter.addAction(Constant.ACTION_CALLGUIDEBTN);
        intentFilter.addAction(Constant.ACTION_USERINORDER);
        intentFilter.addAction(Constant.ACTION_DISSMISSBACK);
        localReceiver = new LocalReceiver();
        lBManager.registerReceiver(localReceiver, intentFilter);
    }

    private void initControls() {
        guidesFragment = GuidesFragment.newInstance();
        nativesFragment = NativesFragment.newInstance();
        withtourFragment = WithtourFragment.newInstance();
        togetherFragment = TogetherFragment.newInstance();
        bowenFragment = BowenFragment.newInstance();
        viewPagerAdapter.addFragment(guidesFragment, getString(R.string.main_guide));
        viewPagerAdapter.addFragment(nativesFragment, getString(R.string.main_native));
        viewPagerAdapter.addFragment(togetherFragment, getString(R.string.main_withTour));
        viewPagerAdapter.addFragment(withtourFragment, getString(R.string.main_together));
        viewPagerAdapter.addFragment(bowenFragment, getString(R.string.main_bowen));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewpager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(0);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        guidesFragment.setPageChange(tab.getPosition());
                        viewpager.setCurrentItem(0);
                        break;
                    case 1:
                        guidesFragment.setPageChange(tab.getPosition());
                        viewpager.setCurrentItem(0);
                        tab.select();
                        break;
                    case 2:
                        guidesFragment.setPageChange(tab.getPosition());
                        viewpager.setCurrentItem(0);
                        tab.select();
                        break;
                    case 3:
                        viewpager.setCurrentItem(3);
                        break;
                    case 4:
                        viewpager.setCurrentItem(4);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK)
            return;
        if(data == null)
            return;
        if(requestCode == 1000)
            api.checkLoginState();
    }

    @Override
    public void onClick(View v) {
        userInfo = SharePrefer.getUserInfo(MainActivity.this);
        String uid = userInfo.getUid();
        switch (v.getId()) {
            case R.id.main_menu_img:
                if (Util.isNull(uid)) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("NoLoginAction",true);
                    startActivityForResult(intent,1000);
                    return;
                }
                toggleLeftLayout();
                break;
            case R.id.main_back_img:
                guidesFragment.cancelOrderSuc(false);
                main_menu_img.setVisibility(View.VISIBLE);
                main_back_img.setVisibility(View.GONE);
                break;
            case R.id.cancel_order_tv://取消订单
                initDialog();
                break;
            case R.id.menu_order_layout://我的订单
                startActivity(new Intent(MainActivity.this, MyOrderActivity.class));
                toggleLeftLayout();
                break;
            case R.id.menu_userInfo_layout:
                startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                toggleLeftLayout();
                break;
            case R.id.menu_setting_layout: //设置
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                toggleLeftLayout();
                break;
            case R.id.menu_msg_layout://消息中心
                startActivity(new Intent(MainActivity.this, TourDetailActivity.class));
                toggleLeftLayout();
                break;
            case R.id.menu_recruit_layout: //导游招募
                toggleLeftLayout();
                break;
        }
    }

    private void initDialog() {
        tipsDialog = new TipsDialog(MainActivity.this);
        tipsDialog.setClickListener(new TipsDialog.DialogClickListener() {
            @Override
            public void cancelDialog() {
                tipsDialog.dismiss();
                api.cancelOrder(orderNum);
            }

            @Override
            public void confirmDialog() {
                tipsDialog.dismiss();
            }
        });
        tipsDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (localReceiver != null)
            lBManager.unregisterReceiver(localReceiver);
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.ACTION_GCANCELORD.equals(action)) {
                cancel_order_tv.setVisibility(View.VISIBLE);
                cancelType = intent.getIntExtra("cancelType", 0);
                orderNum = intent.getStringExtra("theOrderNum");
            } else if (Constant.ACTION_GUIDEOVERSERVER.equals(action)) {
                cancel_order_tv.setVisibility(View.GONE);
                cancelType = 0;
                orderNum = "";
            } else if (Constant.ACTION_CALLGUIDEBTN.equals(action)) {
                main_back_img.setVisibility(View.VISIBLE);
                main_menu_img.setVisibility(View.GONE);
            } else if (Constant.ACTION_USERINORDER.equals(action)) {//用户处在订单当中不能取消订单
                orderNum = "";
                cancel_order_tv.setVisibility(View.GONE);
            } else if (Constant.ACTION_DISSMISSBACK.equals(action)) {
                main_menu_img.setVisibility(View.VISIBLE);
                main_back_img.setVisibility(View.GONE);
            }
        }
    }
}

package com.xbx.client.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.adapter.MyViewPagerAdapter;
import com.xbx.client.beans.UserInfo;
import com.xbx.client.http.IRequest;
import com.xbx.client.http.RequestParams;
import com.xbx.client.jsonparse.CommonParse;
import com.xbx.client.jsonparse.UserInfoParse;
import com.xbx.client.ui.fragment.BowenFragment;
import com.xbx.client.ui.fragment.GuidesFragment;
import com.xbx.client.ui.fragment.NativesFragment;
import com.xbx.client.ui.fragment.TogetherFragment;
import com.xbx.client.ui.fragment.WithtourFragment;
import com.xbx.client.utils.Constant;
import com.xbx.client.utils.RequestBackLisener;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.SpHelper;
import com.xbx.client.utils.Util;
import com.xbx.client.view.BanSlideViewpager;
import com.xbx.client.view.TipsDialog;

public class MainActivity extends BaseActivity {
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

    private MyViewPagerAdapter viewPagerAdapter = null;

    private GuidesFragment guidesFragment = null;
    private NativesFragment nativesFragment = null;
    private WithtourFragment withtourFragment = null;
    private TogetherFragment togetherFragment = null;
    private BowenFragment bowenFragment = null;

    private LocalReceiver localReceiver = null;
    private LocalBroadcastManager lBManager = null;
    private IntentFilter intentFilter = null;

    private TipsDialog tipsDialog = null;
    private boolean isFromLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        isFromLogin = getIntent().getBooleanExtra("isFromLoin",false);
        if(!isFromLogin)
            checkLoginState();
    }

    @Override
    protected void initViews() {
        super.initViews();
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawerLayout);
        drawerLayout.setScrimColor(0x32000000);// 设置半透明度
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewpager = (BanSlideViewpager) findViewById(R.id.viewpager);
        main_menu_img = (ImageView) findViewById(R.id.main_menu_img);
        cancel_order_tv = (TextView) findViewById(R.id.cancel_order_tv);
        menu_order_layout = (LinearLayout) findViewById(R.id.menu_order_layout);
        viewpager.setScrollble(false);
        viewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), this);
        main_menu_img.setOnClickListener(this);
        cancel_order_tv.setOnClickListener(this);
        menu_order_layout.setOnClickListener(this);
        initControls();
        initBroadcast();
    }

    private void initBroadcast() {
        lBManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_GCANCELORD);
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
        viewPagerAdapter.addFragment(withtourFragment, getString(R.string.main_withTour));
        viewPagerAdapter.addFragment(togetherFragment, getString(R.string.main_together));
        viewPagerAdapter.addFragment(bowenFragment, getString(R.string.main_bowen));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

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

    private void checkLoginState(){
        String postUrl = getString(R.string.url_conIp).concat(getString(R.string.url_Login));
        RequestParams params = new RequestParams();
        UserInfo userInfo = SharePrefer.getUserInfo(MainActivity.this);
        params.put("mobile", userInfo.getUserPhone());
        params.put("password", userInfo.getLoginToken());
        params.put("user_type", "0");//代表用户端
//        Util.pLog("Login phone=" + userInfo.getUserPhone()+" token:"+userInfo.getLoginToken());
        IRequest.post(this, postUrl, params , new RequestBackLisener(MainActivity.this) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("Login check Result=" + json);
                if(CommonParse.getRequest(json) == 0){
                    Util.showToast(MainActivity.this, getString(R.string.login_fail));
                    SharePrefer.saveUserInfo(MainActivity.this,new UserInfo());
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }else if(CommonParse.getRequest(json) == 1){
                    UserInfo userInfo = UserInfoParse.getUserInfo(CommonParse.getDataResult(json));
                    if(userInfo != null){
                        SharePrefer.saveUserInfo(MainActivity.this, userInfo);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.main_menu_img:
                toggleLeftLayout();
                break;
            case R.id.cancel_order_tv://取消订单
                initDialog();
                break;
            case R.id.menu_order_layout:
                startActivity(new Intent(MainActivity.this,MyOrderActivity.class));
                toggleLeftLayout();
                break;
        }
    }

    private void initDialog(){
        tipsDialog = new TipsDialog(MainActivity.this);
        tipsDialog.setClickListener(new TipsDialog.DialogClickListener() {
            @Override
            public void cancelDialog() {
                tipsDialog.dismiss();
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
            }
        }
    }
}

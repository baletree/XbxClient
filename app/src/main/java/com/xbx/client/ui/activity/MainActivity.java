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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.xbx.client.beans.UserInfo;
import com.xbx.client.beans.UserStateBean;
import com.xbx.client.beans.Version;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.MainStateParse;
import com.xbx.client.jsonparse.OrderParse;
import com.xbx.client.jsonparse.UtilParse;
import com.xbx.client.linsener.AnimateFirstDisplayListener;
import com.xbx.client.linsener.ImageLoaderConfigFactory;
import com.xbx.client.ui.fragment.BowenFragment;
import com.xbx.client.ui.fragment.GuidesFragment;
import com.xbx.client.ui.fragment.NativesFragment;
import com.xbx.client.ui.fragment.TogetherFragment;
import com.xbx.client.ui.fragment.WithtourFragment;
import com.xbx.client.utils.Constant;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;
import com.xbx.client.utils.updateversion.UpdateUtil;
import com.xbx.client.view.BanSlideViewpager;
import com.xbx.client.view.TipsDialog;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseAppCompatActivity implements TipsDialog.DialogClickListener {
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

    private TipsDialog tipsDialog = null;
    private String orderNum = "";
    private static boolean isExit = false;
    private int dialogType = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            switch (msg.what) {
                case 0:
                    isExit = false;
                    break;
                case TaskFlag.PAGEREQUESFIVE: //取消订单成功

                    break;
                case TaskFlag.REQUESTSUCCESS://验证登录的时候如果有状态就显示状态
                    String checkData = (String) msg.obj;
                    if (UtilParse.getRequestCode(checkData) == 0) {
                        Util.showToast(MainActivity.this, getString(R.string.login_fail));
                        SharePrefer.saveUserInfo(MainActivity.this, new UserInfo());
                        startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 1000);
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
                        dealUserState(stateBean, dataType);
                    }
                    break;
                case TaskFlag.PAGEREQUESFOUR:
                    Version version = MainStateParse.getVersion((String) msg.obj);
                    if (version == null)
                        return;
                    if (Util.isNull(version.getVersionCode()))
                        return;
                    checkUpdate(version);
                    break;
            }
        }
    };

    private void dealUserState(UserStateBean stateBean, String stateKey) {
        orderNum = stateBean.getOrderNum();
        if (stateKey.equals("unpay")) {
            dialogType = 1;
        } else if (stateKey.equals("uncomment")) {
            dialogType = 2;
        } else if (stateKey.equals("going")) {
            dialogType = 3;
        }
        initDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        initDatas();
    }

    protected void initDatas() {
        userInfo = SharePrefer.getUserInfo(MainActivity.this);
        api = new Api(MainActivity.this, handler);
        api.checkUpdate();
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
        userInfo = SharePrefer.getUserInfo(MainActivity.this);
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
    }

    private void initBroadcast() {
        intentFilter.addAction(Constant.ACTION_LOGINSUC);
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
        /*viewPagerAdapter.addFragment(togetherFragment, getString(R.string.main_withTour));
        viewPagerAdapter.addFragment(withtourFragment, getString(R.string.main_together));
        viewPagerAdapter.addFragment(bowenFragment, getString(R.string.main_bowen));*/
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
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        userInfo = SharePrefer.getUserInfo(MainActivity.this);
        String uid = userInfo.getUid();
        switch (v.getId()) {
            case R.id.main_menu_img:
                if (Util.isNull(uid)) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                break;
        }
        toggleLeftLayout();
    }

    private void initDialog() {
        tipsDialog = new TipsDialog(MainActivity.this);
        switch (dialogType) {
            case 1:
                tipsDialog.setBtnTxt("下次吧", "去支付");
                tipsDialog.setInfo("提醒", "您有一个未支付的订单，是否去支付该订单？");
                break;
            case 2:
                tipsDialog.setBtnTxt("下次吧", "去评论");
                tipsDialog.setInfo("提醒", "您有一个未评论的订单，是否去支付该订单？");
                break;
            case 3:
                tipsDialog.setBtnTxt("不进入", "进入");
                tipsDialog.setInfo("提醒", "您有一个进行中的订单，是否进入该行程？");
                break;
        }
        tipsDialog.setClickListener(this);
        tipsDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (localReceiver != null)
            lBManager.unregisterReceiver(localReceiver);
    }

    @Override
    public void cancelDialog() {
        tipsDialog.dismiss();
    }

    @Override
    public void confirmDialog() {
        Intent intent = new Intent();
        switch (dialogType) {
            case 1:
                intent.putExtra("stateOrderNumber", orderNum);
                intent.setClass(MainActivity.this, OrderDetailActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent.putExtra("GuideOrderNum", orderNum);
                intent.setClass(MainActivity.this, SubCommentActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent.putExtra("theOrderNums", orderNum);
                intent.setClass(MainActivity.this, IntoServerActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
                break;
        }
        tipsDialog.dismiss();
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.ACTION_LOGINSUC.equals(action)) {
                setUserInfo();
                api.checkLoginState();
            }
        }
    }

    private void checkUpdate(Version version) {
        new UpdateUtil(MainActivity.this, version);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Util.showToast(MainActivity.this, getString(R.string.exit_app));
            // 利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            this.finish();
        }
    }
}

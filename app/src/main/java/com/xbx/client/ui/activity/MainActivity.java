package com.xbx.client.ui.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.xbx.client.R;
import com.xbx.client.adapter.MyViewPagerAdapter;
import com.xbx.client.ui.fragment.BowenFragment;
import com.xbx.client.ui.fragment.GuidesFragment;
import com.xbx.client.ui.fragment.NativesFragment;
import com.xbx.client.ui.fragment.TogetherFragment;
import com.xbx.client.ui.fragment.WithtourFragment;
import com.xbx.client.utils.Util;
import com.xbx.client.view.BanSlideViewpager;

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

    private MyViewPagerAdapter viewPagerAdapter = null;

    private GuidesFragment guidesFragment = null;
    private NativesFragment nativesFragment = null;
    private WithtourFragment withtourFragment = null;
    private TogetherFragment togetherFragment = null;
    private BowenFragment bowenFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
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
        viewpager.setScrollble(false);
        viewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), this);
        main_menu_img.setOnClickListener(this);
        initControls();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.main_menu_img:
                toggleLeftLayout();
                break;
        }
    }
}

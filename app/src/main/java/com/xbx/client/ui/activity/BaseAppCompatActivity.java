package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx.client.R;
import com.xbx.client.linsener.ImageLoaderConfigFactory;

/**
 * Created by EricYuan on 2016/5/12.
 */
public class BaseAppCompatActivity extends AppCompatActivity implements View.OnClickListener {
    public ImageLoader imageLoader;
    public ImageLoaderConfigFactory configFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_order_layout://我的订单
                startActivity(new Intent(BaseAppCompatActivity.this, MyOrderActivity.class));
                break;
            case R.id.menu_userInfo_layout:
                startActivityForResult(new Intent(BaseAppCompatActivity.this, UserInfoActivity.class), 1050);
                break;
            case R.id.menu_setting_layout: //设置
                startActivity(new Intent(BaseAppCompatActivity.this, SettingActivity.class));
                break;
            case R.id.menu_msg_layout://消息中心
                startActivity(new Intent(BaseAppCompatActivity.this, TourDetailActivity.class));
                break;
            case R.id.menu_recruit_layout: //导游招募
                break;
        }
    }
}

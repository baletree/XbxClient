package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.xbx.client.R;
import com.xbx.client.beans.UserInfo;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.Util;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by EricYuan on 2016/4/6.
 */
public class LoadingActivity extends BaseActivity {

    private UserInfo userInfo = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                    /*if (userInfo != null && !Util.isNull(userInfo.getUid()))
                        startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                    else
                        startActivity(new Intent(LoadingActivity.this, LoginActivity.class));*/
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = this.getWindow();
        window.setFlags(flag, flag);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        userInfo = SharePrefer.getUserInfo(LoadingActivity.this);
        handler.sendEmptyMessageDelayed(1, 2500);
    }
}

package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.utils.Constant;

/**
 * Created by EricYuan on 2016/4/17.
 */
public class SettingActivity extends BaseActivity {

    private TextView title_txt_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initViews() {
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);

        title_txt_tv.setText(getString(R.string.setting_title));
        findViewById(R.id.title_left_img).setOnClickListener(this);
        findViewById(R.id.setting_user_guide_layout).setOnClickListener(this);
        findViewById(R.id.setting_consumer_hotline_layout).setOnClickListener(this);
        findViewById(R.id.setting_legal_layout).setOnClickListener(this);
        findViewById(R.id.setting_about_layout).setOnClickListener(this);
        findViewById(R.id.setting_feedback_layout).setOnClickListener(this);
        findViewById(R.id.setting_sign_out).setOnClickListener(this);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.title_left_img :
                finish();
                break;
            case R.id.setting_user_guide_layout :
                intent.setClass(this, UserGuideActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_consumer_hotline_layout :
                break;
            case R.id.setting_legal_layout :
                break;
            case R.id.setting_about_layout :
                intent.setClass(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_feedback_layout :
                break;
            case R.id.setting_sign_out :
                break;
        }
    }

}

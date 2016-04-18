package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xbx.client.R;

/**
 * Created by EricYuan on 2016/4/17.
 */
public class AboutActivity extends BaseActivity {

    private TextView title_txt_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void initViews() {
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);

        title_txt_tv.setText(getString(R.string.about_title));
        findViewById(R.id.title_left_img).setOnClickListener(this);
        findViewById(R.id.about_basic_intro_layout).setOnClickListener(this);
        findViewById(R.id.about_current_version_layout).setOnClickListener(this);
        findViewById(R.id.about_contact_us_layout).setOnClickListener(this);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_img :
                finish();
                break;
            case R.id.about_basic_intro_layout :
                break;
            case R.id.about_current_version_layout :
                break;
            case R.id.about_contact_us_layout :
                break;
        }
    }

}

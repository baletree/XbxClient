package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xbx.client.R;

/**
 * Created by EricYuan on 2016/4/17.
 */
public class UserGuideActivity extends BaseActivity {

    private TextView title_txt_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);
    }

    @Override
    protected void initViews() {
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);

        title_txt_tv.setText(getString(R.string.user_guide_title));
        findViewById(R.id.title_left_img).setOnClickListener(this);
        findViewById(R.id.user_guide_about_tour_layout).setOnClickListener(this);
        findViewById(R.id.user_guide_about_darky_layout).setOnClickListener(this);
        findViewById(R.id.user_guide_about_follow_layout).setOnClickListener(this);
        findViewById(R.id.user_guide_book_layout).setOnClickListener(this);
        findViewById(R.id.user_guide_cancel_order_layout).setOnClickListener(this);
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
            case R.id.user_guide_about_tour_layout :
                break;
            case R.id.user_guide_about_darky_layout :
                break;
            case R.id.user_guide_about_follow_layout :
                break;
            case R.id.user_guide_book_layout :
                break;
            case R.id.user_guide_cancel_order_layout :
                break;
        }
    }

}

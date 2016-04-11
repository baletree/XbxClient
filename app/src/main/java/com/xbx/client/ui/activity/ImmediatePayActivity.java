package com.xbx.client.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbx.client.R;

/**
 * Created by EricYuan on 2016/4/11.
 */
public class ImmediatePayActivity extends BaseActivity {
    private ImageView title_left_img;
    private TextView title_txt_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immedia_pay);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
    }

    @Override
    protected void initViews() {
        super.initViews();
        title_left_img = (ImageView) findViewById(R.id.title_left_img);
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);
        title_txt_tv.setText(getString(R.string.order_immedia_pay));
        title_left_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_left_img:
                finish();
                break;
        }
    }
}

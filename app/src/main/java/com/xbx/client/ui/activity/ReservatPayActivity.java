package com.xbx.client.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xbx.client.R;

/**
 * Created by EricYuan on 2016/4/22.
 * 预约服务的支付界面
 */
public class ReservatPayActivity extends BaseActivity {
    private TextView user_stroke_tv;//目的地
    private RelativeLayout payWay_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservat_pay);
    }

    @Override
    protected void initViews() {
        super.initViews();
        user_stroke_tv = (TextView) findViewById(R.id.user_stroke_tv);
        user_stroke_tv.setText("青城山");
        payWay_layout = (RelativeLayout) findViewById(R.id.payWay_layout);
        payWay_layout.setVisibility(View.GONE);
    }
}

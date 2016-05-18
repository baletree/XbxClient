package com.xbx.client.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.beans.OrderDetailBean;

/**
 * Created by EricYuan on 2016/5/14.
 * 交易明细
 */
public class PayDetailActivity extends BaseActivity {
    private OrderDetailBean detailBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_detail);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        detailBean = (OrderDetailBean) getIntent().getSerializableExtra("PayDetailBean");
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView)findViewById(R.id.title_txt_tv)).setText(getString(R.string.pay_detail));
        findViewById(R.id.title_left_img).setOnClickListener(this);
        if(detailBean != null){
            ((TextView)findViewById(R.id.payD_aMony_tv)).setText("￥"+detailBean.getOrderPay());
            ((TextView)findViewById(R.id.payD_Sever_tv)).setText("￥"+detailBean.getOrderOrignalPay());
            ((TextView)findViewById(R.id.payD_Reward_tv)).setText("￥"+detailBean.getRewardMoney());
            ((TextView)findViewById(R.id.payD_Coupon_tv)).setText("￥"+detailBean.getRebateMoney());
        }
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

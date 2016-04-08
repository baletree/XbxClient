package com.xbx.client.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/4/6.
 * 取消订单成功需要支付界面
 */
public class CancelOrderSucActivity extends BaseActivity {
    private TextView title_txt_tv;
    private ImageView title_left_img;
    private RadioButton wchat_pay_rb;
    private RadioButton alipay_pay_rb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_ordsuc);
    }

    @Override
    protected void initViews() {
        super.initViews();
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);
        title_txt_tv.setText(getString(R.string.cancel_order));
        title_left_img = (ImageView) findViewById(R.id.title_left_img);
        title_left_img.setOnClickListener(this);
        wchat_pay_rb = (RadioButton) findViewById(R.id.wchat_pay_rb);
        alipay_pay_rb = (RadioButton) findViewById(R.id.alipay_pay_rb);
        wchat_pay_rb.setOnClickListener(this);
        alipay_pay_rb.setOnClickListener(this);
        wchat_pay_rb.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_left_img:
                finish();
                break;
            case R.id.wchat_pay_rb:
                Util.pLog("微信"+wchat_pay_rb.isChecked());
                if(wchat_pay_rb.isChecked()){
                    wchat_pay_rb.setChecked(true);
                    alipay_pay_rb.setChecked(false);
                }
                break;
            case R.id.alipay_pay_rb:
                Util.pLog("支付宝"+alipay_pay_rb.isChecked());
                if(alipay_pay_rb.isChecked()){
                    alipay_pay_rb.setChecked(true);
                    wchat_pay_rb.setChecked(false);
                }
                break;
        }
    }
}

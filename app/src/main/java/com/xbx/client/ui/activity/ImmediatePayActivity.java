package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.alipay.Alipay;
import com.xbx.client.beans.PayInfoBean;

/**
 * Created by EricYuan on 2016/4/11.
 */
public class ImmediatePayActivity extends FragmentActivity implements View.OnClickListener{
    private ImageView title_left_img;
    private TextView title_txt_tv;
    private ImageView reward_img;
    private Button immedia_pay_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immedia_pay);
        initViews();
    }

    protected void initViews() {
        title_left_img = (ImageView) findViewById(R.id.title_left_img);
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);
        title_txt_tv.setText(getString(R.string.order_immedia_pay));
        title_left_img.setOnClickListener(this);
        reward_img = (ImageView) findViewById(R.id.reward_img);
        immedia_pay_btn = (Button) findViewById(R.id.immedia_pay_btn);
        reward_img.setOnClickListener(this);
        immedia_pay_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left_img:
                finish();
                break;
            case R.id.reward_img:
                Intent intent = new Intent(ImmediatePayActivity.this,SubCommentActivity.class);
                startActivity(intent);
                break;
            case R.id.immedia_pay_btn:
                PayInfoBean payBean = new PayInfoBean();
                payBean.setPayName("途途导由测试");
                payBean.setPayIntro("北京到成都春熙路旅游费用");
                payBean.setPayOutTradeNum("TTDY20160412101000231");
                payBean.setPayPrice("0.02");
                String notifyurl = "http://notify.msp.hk/notify.htm";
                Alipay alipay = new Alipay(ImmediatePayActivity.this,notifyurl);
                alipay.pay(payBean);
                break;
        }
    }
}

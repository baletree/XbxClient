package com.xbx.client.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx.client.R;
import com.xbx.client.alipay.Alipay;
import com.xbx.client.beans.OrderDetailBean;
import com.xbx.client.beans.PayInfoBean;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.OrderParse;
import com.xbx.client.linsener.AnimateFirstDisplayListener;
import com.xbx.client.linsener.ImageLoaderConfigFactory;
import com.xbx.client.utils.StringUtil;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/4/22.
 * 预约服务的支付界面
 */
public class ReservatPayActivity extends BaseActivity {
    private RelativeLayout payWay_layout;
    private RelativeLayout reservat_pay_rl;
    //导游信息
    private ImageView headImage;
    private TextView guide_typed_tv;
    private TextView guide_name_tv;
    private TextView guide_code_tv;
    private RatingBar guide_ratingbar;
    private TextView user_stroke_tv;//目的地
    //订单明细
    private TextView payExtrueMon_tv;
    private RelativeLayout rewardMon_rl;//小费
    private TextView rewardMon_tv;
    private RelativeLayout couponMon_rl;//优惠
    private TextView couponMon_tv;
    private RelativeLayout totalMon_rl;//总计
    private TextView totalMon_tv;

    private OrderDetailBean detailBean = null;
    private Api api = null;
    private ImageLoader imageLoader;
    private ImageLoaderConfigFactory configFactory;

    private boolean isFromOrder = false;
    private String orderNum = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TaskFlag.REQUESTSUCCESS:
                    String dataDetail = (String) msg.obj;
                    detailBean = OrderParse.getDetailOrder(dataDetail);
                    if (detailBean == null)
                        return;
                    setPayInfo();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservat_pay);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(ReservatPayActivity.this, handler);
        orderNum = getIntent().getStringExtra("GuideOrderNum");
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
        if (isFromOrder) {
            detailBean = (OrderDetailBean) getIntent().getSerializableExtra("orderDetailBean");
            if (detailBean == null)
                return;
            setPayInfo();
        } else
            api.getOrderDetail(orderNum);
    }

    @Override
    protected void initViews() {
        super.initViews();
        reservat_pay_rl = (RelativeLayout) findViewById(R.id.reservat_pay_rl);
        payWay_layout = (RelativeLayout) findViewById(R.id.payWay_layout);
        payWay_layout.setVisibility(View.GONE);
        reservat_pay_rl.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.title_txt_tv)).setText(getString(R.string.order_immedia_pay));
        findViewById(R.id.title_left_img).setOnClickListener(this);
        findViewById(R.id.reservat_pay_btn).setOnClickListener(this);
        findViewById(R.id.guides_call_img).setOnClickListener(this);
        headImage = (ImageView) findViewById(R.id.guide_head_img);
        guide_typed_tv = (TextView) findViewById(R.id.guide_typed_tv);
        guide_name_tv = (TextView) findViewById(R.id.guide_name_tv);
        guide_code_tv = (TextView) findViewById(R.id.guide_code_tv);
        guide_ratingbar = (RatingBar) findViewById(R.id.guide_ratingbar);
        user_stroke_tv = (TextView) findViewById(R.id.user_stroke_tv);
        rewardMon_rl = (RelativeLayout) findViewById(R.id.rewardMon_rl);
        couponMon_rl = (RelativeLayout) findViewById(R.id.couponMon_rl);
        totalMon_rl = (RelativeLayout) findViewById(R.id.totalMon_rl);
        payExtrueMon_tv = (TextView) findViewById(R.id.payExtrueMon_tv);
        rewardMon_tv = (TextView) findViewById(R.id.rewardMon_tv);
        couponMon_tv = (TextView) findViewById(R.id.couponMon_tv);
        totalMon_tv = (TextView) findViewById(R.id.totalMon_tv);
    }

    private void setPayInfo() {
        reservat_pay_rl.setVisibility(View.VISIBLE);
        imageLoader.displayImage(detailBean.getHeadImg(), headImage, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        guide_name_tv.setText(detailBean.getGuideName());
        guide_typed_tv.setText(StringUtil.getGuideType(this, detailBean.getGuideType()));
        guide_code_tv.setText(detailBean.getGuideNumber());
        user_stroke_tv.setText(detailBean.getUserAddress());
        payExtrueMon_tv.setText("￥" + detailBean.getOrderOrignalPay());
        totalMon_tv.setText("￥" + detailBean.getOrderPay());
        rewardMon_tv.setText("￥" + detailBean.getRewardMoney());
        couponMon_tv.setText("￥" + detailBean.getRebateMoney());
        if (!Util.isNull(detailBean.getGuideStart()))
            guide_ratingbar.setRating(Float.valueOf(detailBean.getGuideStart()));
        if (detailBean.getRewardMoney() == 0.0)
            rewardMon_rl.setVisibility(View.GONE);
        if (detailBean.getRebateMoney() == 0.0)
            couponMon_rl.setVisibility(View.GONE);
        if (detailBean.getOrderOrignalPay() == detailBean.getOrderPay())
            totalMon_rl.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_img:
                finish();
                break;
            case R.id.reservat_pay_btn:
                api.moniPayOrder(detailBean.getOrderNum());
                Util.showToast(ReservatPayActivity.this,"模拟支付："+detailBean.getOrderNum());
                PayInfoBean payBean = new PayInfoBean();
                payBean.setPayName("途途导由测试");
                payBean.setPayIntro("北京到成都春熙路旅游费用");
                payBean.setPayOutTradeNum("TTDY20160412101000231");
                payBean.setPayPrice("0.02");
                String notifyurl = "http://notify.msp.hk/notify.htm";
                Alipay alipay = new Alipay(ReservatPayActivity.this, notifyurl);
                alipay.pay(payBean);
                break;
            case R.id.guides_call_img:
                if (detailBean != null && !Util.isNull(detailBean.getGuidePhone())) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + detailBean.getGuidePhone()));
                    startActivity(intent);
                }
                break;
        }
    }
}

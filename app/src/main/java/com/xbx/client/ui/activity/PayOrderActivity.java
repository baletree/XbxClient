package com.xbx.client.ui.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
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
import com.xbx.client.view.TipsDialog;

import java.util.ArrayList;

/**
 * Created by EricYuan on 2016/4/11.
 * 支付订单界面
 * 1.推送行程结束进入
 * 2.订单详情进入
 * 3.首页检测进入
 */
public class PayOrderActivity extends FragmentActivity implements View.OnClickListener {
    private RelativeLayout immedia_pay_rl;
    private ImageView reward_img;//打赏按钮
    //导游信息
    private ImageView headImage;
    private TextView guide_typed_tv;
    private TextView guide_name_tv;
    private TextView guide_code_tv;
    private RatingBar guide_ratingbar;
    private TextView user_stroke_tv;//目的地
    //订单明细
    private TextView pay_orderState_tv;
    private RelativeLayout pay_serveTime_rl;
    private TextView serveTime_tv;
    //交易明细
    private TextView payExtrueMon_tv;
    private RelativeLayout rewardMon_rl;//小费
    private TextView rewardMon_tv;
    private RelativeLayout couponMon_rl;//优惠
    private TextView couponMon_tv;
    private RelativeLayout totalMon_rl;//总计
    private TextView totalMon_tv;
    private RelativeLayout payWay_layout;

    private OrderDetailBean detailBean = null;
    private Api api = null;
    private ImageLoader imageLoader;
    private ImageLoaderConfigFactory configFactory;
    private NotificationManager notiManaer = null;

    private boolean isFromOrder = false;
    private String orderNum = "";
    private int serverType = 0;
    private int notificationId = 0;

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
                case TaskFlag.PAGEREQUESFIVE:
                    Util.showToast(PayOrderActivity.this, "支付成功!");
                    if (!isFromOrder && notificationId != 0) {
                        notiManaer.cancel(notificationId);
                    }
                    setResult(RESULT_OK, new Intent());
                    finish();
                    break;
                case TaskFlag.PAGEREQUESFOUR:
                    String datas = (String) msg.obj;
                    if (Util.isNull(datas))
                        return;
                    Util.pLog("sign:"+datas);
                    String payInfo = OrderParse.getPayInfo(datas);
                    Alipay alipay = new Alipay(PayOrderActivity.this, payInfo);
                    alipay.pay();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immedia_pay);
        initDatas();
    }

    private void initDatas() {
        notiManaer = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        api = new Api(PayOrderActivity.this, handler);
        orderNum = getIntent().getStringExtra("GuideOrderNum");
        notificationId = getIntent().getIntExtra("JPushNotificationId", 0);
        isFromOrder = getIntent().getBooleanExtra("isFromOrderDetail", false);
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
        initViews();
        if (isFromOrder) {
            detailBean = (OrderDetailBean) getIntent().getSerializableExtra("orderDetailBean");
            if (detailBean == null)
                return;
            setPayInfo();
        } else
            api.getOrderDetail(orderNum);
    }

    protected void initViews() {
        immedia_pay_rl = (RelativeLayout) findViewById(R.id.immedia_pay_rl);
        immedia_pay_rl.setVisibility(View.GONE);
        reward_img = (ImageView) findViewById(R.id.reward_img);
        reward_img.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.title_txt_tv)).setText(getString(R.string.order_immedia_pay));
        findViewById(R.id.title_left_img).setOnClickListener(this);
        findViewById(R.id.guides_call_img).setOnClickListener(this);
        findViewById(R.id.reward_img).setOnClickListener(this);
        findViewById(R.id.immedia_pay_btn).setOnClickListener(this);
        user_stroke_tv = (TextView) findViewById(R.id.user_stroke_tv);
        payWay_layout = (RelativeLayout) findViewById(R.id.payWay_layout);
        payWay_layout.setVisibility(View.GONE);
        headImage = (ImageView) findViewById(R.id.guide_head_img);
        guide_typed_tv = (TextView) findViewById(R.id.guide_typed_tv);
        guide_name_tv = (TextView) findViewById(R.id.guide_name_tv);
        guide_code_tv = (TextView) findViewById(R.id.guide_code_tv);
        guide_ratingbar = (RatingBar) findViewById(R.id.guide_ratingbar);
        rewardMon_rl = (RelativeLayout) findViewById(R.id.rewardMon_rl);
        couponMon_rl = (RelativeLayout) findViewById(R.id.couponMon_rl);
        totalMon_rl = (RelativeLayout) findViewById(R.id.totalMon_rl);
        payExtrueMon_tv = (TextView) findViewById(R.id.payExtrueMon_tv);
        rewardMon_tv = (TextView) findViewById(R.id.rewardMon_tv);
        couponMon_tv = (TextView) findViewById(R.id.couponMon_tv);
        totalMon_tv = (TextView) findViewById(R.id.totalMon_tv);
        pay_orderState_tv = (TextView) findViewById(R.id.pay_orderState_tv);
        pay_serveTime_rl = (RelativeLayout) findViewById(R.id.pay_serveTime_rl);
        serveTime_tv = (TextView) findViewById(R.id.serveTime_tv);
    }

    private void setPayInfo() {
        immedia_pay_rl.setVisibility(View.VISIBLE);
        if (detailBean.getServerType() == 0 && detailBean.getOrderState() == 3)
            reward_img.setVisibility(View.VISIBLE);//当此状态为即时服务并且服务状态时完成
        imageLoader.displayImage(detailBean.getHeadImg(), headImage, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        guide_name_tv.setText(detailBean.getGuideName());
        guide_typed_tv.setText(StringUtil.getGuideType(this, detailBean.getGuideType()));
        guide_code_tv.setText(detailBean.getGuideNumber());
        user_stroke_tv.setText(detailBean.getUserAddress());
        payExtrueMon_tv.setText("￥" + detailBean.getOrderOrignalPay());
        totalMon_tv.setText("￥" + detailBean.getOrderPay());
        rewardMon_tv.setText("￥" + detailBean.getRewardMoney());
        couponMon_tv.setText("￥" + detailBean.getRebateMoney());
        String orderState = StringUtil.getOrderState(this, detailBean.getServerType(), detailBean.getOrderState());
        if (!Util.isNull(orderState))
            pay_orderState_tv.setText(orderState);
        serveTime_tv.setText(detailBean.getServerDate());
        if (!Util.isNull(detailBean.getGuideStar()))
            guide_ratingbar.setRating(Float.valueOf(detailBean.getGuideStar()) / 2);
        if (detailBean.getRewardMoney() == 0.0)
            rewardMon_rl.setVisibility(View.GONE);
        if (detailBean.getRebateMoney() == 0.0)
            couponMon_rl.setVisibility(View.GONE);
        Util.pLog(detailBean.getOrderOrignalPay() + " / " + detailBean.getOrderPay());
        if (detailBean.getOrderOrignalPay() == detailBean.getOrderPay())
            totalMon_rl.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_img:
                finish();
                break;
            case R.id.reward_img:
                if(detailBean.getRewardList() == null)
                    return;
                Intent intent = new Intent(PayOrderActivity.this, RewardActivity.class);
                intent.putStringArrayListExtra("rewardMonList", (ArrayList<String>) detailBean.getRewardList());
                startActivity(intent);
                break;
            case R.id.immedia_pay_btn:
//                api.getPayInfo(detailBean.getOrderNum(), "alipay");
                api.moniPayOrder(detailBean.getOrderNum());
                break;
            case R.id.guides_call_img:
                if (detailBean != null && !Util.isNull(detailBean.getGuidePhone())) {
                    Intent intents = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + detailBean.getGuidePhone()));
                    startActivity(intents);
                }
                break;
        }
    }
}

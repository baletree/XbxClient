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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx.client.R;
import com.xbx.client.alipay.Alipay;
import com.xbx.client.beans.OrderDetailBean;
import com.xbx.client.beans.PayInfoBean;
import com.xbx.client.beans.UserInfo;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.OrderParse;
import com.xbx.client.linsener.AnimateFirstDisplayListener;
import com.xbx.client.linsener.ImageLoaderConfigFactory;
import com.xbx.client.utils.SharePrefer;
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
    private ScrollView pay_scl;
    //导游信息
    private ImageView headImage;
    private TextView guide_typed_tv;
    private TextView guide_name_tv;
    private TextView guide_code_tv;
    private TextView guide_star_tv;
    private RatingBar guide_ratingbar;

    private TextView pay_aMony_tv;
    private TextView pay_locate_tv;
    private TextView pay_orderNum_tv;
    private TextView pay_startTime_tv;
    private TextView pay_stopTime_tv;
    private TextView pay_serveTime_tv;

    private TextView pay_rewarMon_tv;
    private RelativeLayout pay_reward_rl;//小费
    private TextView pay_coupons_tv;
    private RelativeLayout pay_coupons_rl;//优惠

    private LinearLayout pCasualty_ll;
    private TextView pUser_phone_tv;
    private TextView call_insPrice_tv;
    private EditText call_realName_et;
    private EditText call_idCard_et;

    private OrderDetailBean detailBean = null;
    private Api api = null;
    private ImageLoader imageLoader;
    private ImageLoaderConfigFactory configFactory;
    private NotificationManager notiManaer = null;
    private UserInfo userInfo = null;

    private String orderNum = "";
    private int notificationId = 0;
    private int rewardMoney = 0;

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
                    if (notificationId != 0) {
                        notiManaer.cancel(notificationId);
                    }
                    if (detailBean.getServerType() == 1) {
                        finish();
                        return;
                    }
                    Intent intent = new Intent(PayOrderActivity.this, SubCommentActivity.class);
                    intent.putExtra("SubCommentOrderNum", orderNum);
                    startActivity(intent);
                    finish();
                    break;
                case TaskFlag.PAGEREQUESFOUR:
                    String datas = (String) msg.obj;
                    if (Util.isNull(datas))
                        return;
                    Util.pLog("sign:" + datas);
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
        setContentView(R.layout.activity_pay_order);
        initDatas();
    }

    private void initDatas() {
        userInfo = SharePrefer.getUserInfo(PayOrderActivity.this);
        notiManaer = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        api = new Api(PayOrderActivity.this, handler);
        orderNum = getIntent().getStringExtra("PayOrderNum");
        notificationId = getIntent().getIntExtra("JPushNotificationId", 0);
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
        initViews();
        api.getOrderDetail(orderNum);
    }

    protected void initViews() {
        pay_scl = (ScrollView) findViewById(R.id.pay_scl);
        pay_scl.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.title_txt_tv)).setText(getString(R.string.order_immedia_pay));
        findViewById(R.id.title_left_img).setOnClickListener(this);
        headImage = (ImageView) findViewById(R.id.guide_head_img);
        guide_typed_tv = (TextView) findViewById(R.id.guide_typed_tv);
        guide_name_tv = (TextView) findViewById(R.id.guide_name_tv);
        guide_code_tv = (TextView) findViewById(R.id.guide_code_tv);
        guide_star_tv = (TextView) findViewById(R.id.guide_star_tv);
        guide_ratingbar = (RatingBar) findViewById(R.id.guide_ratingbar);
        pay_aMony_tv = (TextView) findViewById(R.id.pay_aMony_tv);
        pay_locate_tv = (TextView) findViewById(R.id.pay_locate_tv);
        pay_orderNum_tv = (TextView) findViewById(R.id.pay_orderNum_tv);
        pay_startTime_tv = (TextView) findViewById(R.id.pay_startTime_tv);
        pay_stopTime_tv = (TextView) findViewById(R.id.pay_stopTime_tv);
        pay_serveTime_tv = (TextView) findViewById(R.id.pay_serveTime_tv);
        pay_rewarMon_tv = (TextView) findViewById(R.id.pay_rewarMon_tv);
        pay_reward_rl = (RelativeLayout) findViewById(R.id.pay_reward_rl);
        pay_coupons_tv = (TextView) findViewById(R.id.pay_rewarMon_tv);
        pay_coupons_rl = (RelativeLayout) findViewById(R.id.pay_coupons_rl);
        pCasualty_ll = (LinearLayout) findViewById(R.id.pCasualty_ll);
        pUser_phone_tv = (TextView) findViewById(R.id.pUser_phone_tv);
        call_insPrice_tv = (TextView) findViewById(R.id.call_insPrice_tv);
        call_realName_et = (EditText) findViewById(R.id.call_realName_et);
        call_idCard_et = (EditText) findViewById(R.id.call_idCard_et);
        pay_reward_rl.setOnClickListener(this);
        pay_coupons_rl.setOnClickListener(this);
        findViewById(R.id.pay_confirm_btn).setOnClickListener(this);
        findViewById(R.id.guides_call_img).setVisibility(View.GONE);
        findViewById(R.id.pay_detail_rl).setOnClickListener(this);
    }

    private void setPayInfo() {
        pay_scl.setVisibility(View.VISIBLE);
        pUser_phone_tv.setText(userInfo.getUserPhone());
        call_realName_et.setText(userInfo.getUserRealName());
        call_idCard_et.setText(userInfo.getUserIdCard());
        if (detailBean.getServerType() == 1)
            pCasualty_ll.setVisibility(View.VISIBLE);//当此状态为预约服务时显示意外险
        if (detailBean.getServerType() == 0 && detailBean.getOrderState() == 3)
            pay_reward_rl.setVisibility(View.VISIBLE);//当此状态为即时服务并且服务状态时完成
        imageLoader.displayImage(detailBean.getHeadImg(), headImage, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        guide_name_tv.setText(detailBean.getGuideName());
        guide_typed_tv.setText(StringUtil.getGuideType(this, detailBean.getGuideType()));
        guide_code_tv.setText(detailBean.getGuideNumber());
        guide_star_tv.setText(detailBean.getGuideStar() + getString(R.string.scole));
        if (!Util.isNull(detailBean.getGuideStar()))
            guide_ratingbar.setRating(Float.valueOf(detailBean.getGuideStar()) / 2);
        pay_aMony_tv.setText(detailBean.getOrderPay() + "");
        pay_orderNum_tv.setText(detailBean.getOrderNum());
        pay_startTime_tv.setText(detailBean.getOrderStartTime());
        pay_stopTime_tv.setText(detailBean.getOrderStartTime());
        pay_serveTime_tv.setText(detailBean.getServerDate());
        if (Util.isNull(detailBean.getUserAddress()))
            return;
        if (detailBean.getUserAddress().contains(getString(R.string.provice)))
            pay_locate_tv.setText(detailBean.getUserAddress().split(getString(R.string.provice))[1]);
        else
            pay_locate_tv.setText(detailBean.getUserAddress());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        if (resultCode == RewardActivity.rewardResult) {
            String rewarMon = data.getStringExtra("RewardMoney");
            if (!Util.isNull(rewarMon)) {
                rewardMoney = Integer.parseInt(rewarMon);
                pay_rewarMon_tv.setText(rewarMon + getString(R.string.yuan));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_img:
                finish();
                break;
            case R.id.pay_reward_rl:
                if (detailBean.getRewardList() == null)
                    return;
                Intent intent = new Intent(PayOrderActivity.this, RewardActivity.class);
                intent.putStringArrayListExtra("rewardMonList", (ArrayList<String>) detailBean.getRewardList());
                startActivityForResult(intent, RewardActivity.rewardResult);
                break;
            case R.id.pay_confirm_btn:
//                api.getPayInfo(detailBean.getOrderNum(), "alipay");
                api.moniPayOrder(detailBean.getOrderNum());
                break;
            case R.id.pay_detail_rl://订单明细

                break;
        }
    }
}

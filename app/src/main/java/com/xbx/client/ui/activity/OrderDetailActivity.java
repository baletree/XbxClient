package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx.client.R;
import com.xbx.client.beans.OrderDetailBean;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.OrderParse;
import com.xbx.client.jsonparse.UtilParse;
import com.xbx.client.linsener.AnimateFirstDisplayListener;
import com.xbx.client.linsener.ImageLoaderConfigFactory;
import com.xbx.client.utils.StringUtil;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;
import com.xbx.client.view.FlowLayout;
import com.xbx.client.view.TipsDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/8.
 */
public class OrderDetailActivity extends BaseActivity {
    private ScrollView orderDetail_sv;
    private TextView title_rtxt_tv;
    private ImageView headImage;
    private TextView guide_typed_tv;
    private TextView guide_name_tv;
    private TextView guide_code_tv;
    private RatingBar guide_ratingbar;
    private TextView user_stroke_tv;

    private Button oDeatil_state_btn;//最下面的状态按钮
    private Button oDeatil_cancel_btn;//最下面的状态按钮
    private TextView orderState_tv;//订单状态
    private RelativeLayout serveTime_rl;//服务时间
    private TextView serveTime_tv;
    private TextView statePay_tv;//消费金额
    private RelativeLayout payExtrueMon_rl;//支付金额
    private TextView payExtrueMon_tv;
    private RelativeLayout rewardMon_rl;//小费
    private TextView rewardMon_tv;
    private RelativeLayout couponMon_rl;//优惠
    private TextView couponMon_tv;
    private RelativeLayout totalMon_rl;//总计或者取消金额
    private TextView total_payTip_tv;
    private TextView totalMon_tv;
    private RelativeLayout payWay_layout;//支付方式
    private TextView payWay_tv;
    private LinearLayout oDeatil_state_ll;

    private LinearLayout oDeail_comment_ll;
    private TextView commentCotent_tv;
    private FlowLayout guide_tag_flayout;
    private RatingBar guideComment_rb;

    private List<String> tagList = null;
    private Api api = null;
    private OrderDetailBean detailBean = null;
    private ImageLoader imageLoader;
    private ImageLoaderConfigFactory configFactory;

    private String orderNuber = "";

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
                    setOderInfo();
                    break;
                case TaskFlag.PAGEREQUESFIVE:
                    String allData = (String) msg.obj;
                    if (Util.isNull(allData))
                        return;
                    int codes = UtilParse.getRequestCode(allData);
                    if (codes == 1)
                        api.getOrderDetail(orderNuber);
                    Util.showToast(OrderDetailActivity.this, UtilParse.getRequestMsg(allData));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        orderNuber = getIntent().getStringExtra("stateOrderNumber");
        api = new Api(OrderDetailActivity.this, handler);
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
        tagList = new ArrayList<>();
    }

    @Override
    protected void initViews() {
        super.initViews();
        orderDetail_sv = (ScrollView) findViewById(R.id.orderDetail_sv);
        orderDetail_sv.setVisibility(View.GONE);
        title_rtxt_tv = (TextView) findViewById(R.id.title_rtxt_tv);
        guide_tag_flayout = (FlowLayout) findViewById(R.id.guide_tag_flayout);
        oDeatil_state_btn = (Button) findViewById(R.id.oDeatil_state_btn);
        oDeatil_cancel_btn = (Button) findViewById(R.id.oDeatil_cancel_btn);
        headImage = (ImageView) findViewById(R.id.guide_head_img);
        guide_typed_tv = (TextView) findViewById(R.id.guide_typed_tv);
        guide_name_tv = (TextView) findViewById(R.id.guide_name_tv);
        guide_code_tv = (TextView) findViewById(R.id.guide_code_tv);
        guide_ratingbar = (RatingBar) findViewById(R.id.guide_ratingbar);
        user_stroke_tv = (TextView) findViewById(R.id.user_stroke_tv);
        orderState_tv = (TextView) findViewById(R.id.orderState_tv);
        statePay_tv = (TextView) findViewById(R.id.statePay_tv);
        serveTime_rl = (RelativeLayout) findViewById(R.id.serveTime_rl);
        payExtrueMon_rl = (RelativeLayout) findViewById(R.id.payExtrueMon_rl);
        rewardMon_rl = (RelativeLayout) findViewById(R.id.rewardMon_rl);
        couponMon_rl = (RelativeLayout) findViewById(R.id.couponMon_rl);
        totalMon_rl = (RelativeLayout) findViewById(R.id.totalMon_rl);
        payWay_layout = (RelativeLayout) findViewById(R.id.payWay_layout);
        serveTime_tv = (TextView) findViewById(R.id.serveTime_tv);
        payExtrueMon_tv = (TextView) findViewById(R.id.payExtrueMon_tv);
        rewardMon_tv = (TextView) findViewById(R.id.rewardMon_tv);
        couponMon_tv = (TextView) findViewById(R.id.couponMon_tv);
        total_payTip_tv = (TextView) findViewById(R.id.total_payTip_tv);
        totalMon_tv = (TextView) findViewById(R.id.totalMon_tv);
        payWay_tv = (TextView) findViewById(R.id.payWay_tv);
        oDeail_comment_ll = (LinearLayout) findViewById(R.id.oDeail_comment_ll);
        commentCotent_tv = (TextView) findViewById(R.id.commentCotent_tv);
        guideComment_rb = (RatingBar) findViewById(R.id.guideComment_rb);
        oDeatil_state_ll = (LinearLayout) findViewById(R.id.oDeatil_state_ll);

        findViewById(R.id.title_left_img).setOnClickListener(this);
        findViewById(R.id.guides_call_img).setOnClickListener(this);
        ((TextView) findViewById(R.id.title_txt_tv)).setText(getString(R.string.order_detail_title));
        title_rtxt_tv.setText(R.string.Complaints);
        title_rtxt_tv.setVisibility(View.VISIBLE);

        title_rtxt_tv.setOnClickListener(this);
        oDeatil_state_btn.setOnClickListener(this);
        oDeatil_cancel_btn.setOnClickListener(this);

        api.getOrderDetail(orderNuber);
    }

    private TextView addTextView(String txt) {
        LinearLayout.LayoutParams txtLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(this);
        textView.setText(txt);
        textView.setPadding(20, 8, 20, 8);
        txtLp.rightMargin = 10;
        txtLp.bottomMargin = 8;
        textView.setBackgroundResource(R.drawable.guide_tag_bg);
        textView.setLayoutParams(txtLp);
        return textView;
    }

    private void setOderInfo() {
        orderDetail_sv.setVisibility(View.VISIBLE);
        imageLoader.displayImage(detailBean.getHeadImg(), headImage, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        guide_name_tv.setText(detailBean.getGuideName());
        guide_typed_tv.setText(StringUtil.getGuideType(this, detailBean.getGuideType()));
        guide_code_tv.setText(detailBean.getGuideNumber());
        Util.pLog("detailBean.getGuideStar():" + detailBean.getGuideStar());
        if (!Util.isNull(detailBean.getGuideStar()))
            guide_ratingbar.setRating(Float.valueOf(detailBean.getGuideStar()) / 2);
        user_stroke_tv.setText(detailBean.getUserAddress());
        setStateInfo();
    }

    private void setStateInfo() {
        orderState_tv.setText(StringUtil.getOrderState(this, detailBean.getServerType(), detailBean.getOrderState()));
        serveTime_tv.setText(detailBean.getServerDate());
        payExtrueMon_tv.setText("￥" + detailBean.getOrderOrignalPay());
        totalMon_tv.setText("￥" + detailBean.getOrderPay());
        rewardMon_tv.setText("￥" + detailBean.getRewardMoney());
        couponMon_tv.setText("￥" + detailBean.getRebateMoney());
        commentCotent_tv.setText(detailBean.getGuideCotent());
        if (!Util.isNull(detailBean.getCommentStar())) {
            guideComment_rb.setVisibility(View.VISIBLE);
            guideComment_rb.setRating(Float.valueOf(detailBean.getCommentStar())/2);
        }
        if (detailBean.getRewardMoney() == 0.0)
            rewardMon_rl.setVisibility(View.GONE);
        if (detailBean.getRebateMoney() == 0.0)
            couponMon_rl.setVisibility(View.GONE);
        if (Util.isNull(detailBean.getGuideCotent()))
            commentCotent_tv.setVisibility(View.GONE);
        Util.pLog(detailBean.getOrderOrignalPay() + " / " + detailBean.getOrderPay());
        if (detailBean.getOrderOrignalPay() == detailBean.getOrderPay())
            totalMon_rl.setVisibility(View.GONE);
        if (detailBean.getGuideTagList() != null && detailBean.getGuideTagList().size() > 0) {
            guide_tag_flayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < detailBean.getGuideTagList().size(); i++) {
                guide_tag_flayout.addView(addTextView(detailBean.getGuideTagList().get(i)));
            }
        }
        switch (detailBean.getServerType()) {
            case 0://即时服务
                oDeatil_cancel_btn.setVisibility(View.GONE);//取消按钮隐藏
                getImmediaState();
                break;
            case 1://预约服务
                getReservatState();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        if (requestCode == 1050) {
            api.getOrderDetail(orderNuber);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_img:
                finish();
                break;
            case R.id.guides_call_img://导游电话

                break;
            case R.id.title_rtxt_tv://投诉

                break;
            case R.id.oDeatil_state_btn:
                jumpActivity();
                break;
            case R.id.oDeatil_cancel_btn://取消订单
                api.cancelOrder(detailBean.getOrderNum());
                break;
        }
    }

    private void jumpActivity() {
        Intent intent = new Intent();
        intent.putExtra("isFromOrderDetail", true);
        intent.putExtra("orderDetailBean", detailBean);
        switch (detailBean.getServerType()) {
            case 0://即时服务
                getImediaJump(intent);
                break;
            case 1://预约服务
                getReservatJump(intent);
                break;
        }
    }

    private void getImediaJump(Intent intent) {
        switch (detailBean.getOrderState()) {
            case 3:
                intent.setClass(OrderDetailActivity.this, PayOrderActivity.class);
                startActivityForResult(intent, 1050);
                break;
            case 4:
                intent.setClass(OrderDetailActivity.this, SubCommentActivity.class);
                startActivityForResult(intent, 1050);
                break;
            case 6:
                intent.setClass(OrderDetailActivity.this, CancelOrderSucActivity.class);
                intent.putExtra("isFromOrderDetail", true);
                startActivityForResult(intent, 1050);
                break;
        }
    }

    private void getReservatJump(Intent intent) {
        switch (detailBean.getOrderState()) {
            case 0:
                intent.setClass(OrderDetailActivity.this, PayOrderActivity.class);
                startActivityForResult(intent, 1050);
                break;
            case 4:
                intent.setClass(OrderDetailActivity.this, SubCommentActivity.class);
                startActivityForResult(intent, 1050);
                break;
        }
    }

    private void getImmediaState() {
        switch (detailBean.getOrderState()) {
            case 0:
            case 1:
            case 2:
                orderState_tv.setText("进行中");
                oDeatil_state_ll.setVisibility(View.GONE);//底部布局按钮
                payWay_layout.setVisibility(View.GONE);//支付方式
                break;
            case 3://服务结束，待支付
                orderState_tv.setText("待支付");
                oDeatil_state_btn.setText("支付");
                oDeatil_state_ll.setVisibility(View.VISIBLE);
                payWay_layout.setVisibility(View.GONE);//支付方式
                break;
            case 4:
                orderState_tv.setText("待评价");
                oDeatil_state_btn.setText("去评价");
                oDeatil_state_ll.setVisibility(View.VISIBLE);
                break;
            case 5:
                orderState_tv.setText("已完成");
                oDeatil_state_ll.setVisibility(View.GONE);
                oDeail_comment_ll.setVisibility(View.VISIBLE);
                break;
            case 6:
                orderState_tv.setText("取消待支付");
                oDeatil_state_btn.setText("支付");
                total_payTip_tv.setText("取消费用");
                oDeatil_state_ll.setVisibility(View.VISIBLE);
                serveTime_rl.setVisibility(View.GONE);  //服务时间
                payWay_layout.setVisibility(View.GONE); //支付方式
                payExtrueMon_rl.setVisibility(View.GONE);//未除掉优惠券和小费的实际价格
                break;
            case 7://取消订单的已关闭，分为支付和违约超时
                orderState_tv.setText("已关闭");
                total_payTip_tv.setText("取消费用");
                if (detailBean.getOrderOrignalPay() == 0.0)
                    totalMon_tv.setText("免费");
                else {
                    payWay_layout.setVisibility(View.GONE);
                    totalMon_tv.setText("￥" + detailBean.getOrderOrignalPay());
                }
                payExtrueMon_rl.setVisibility(View.GONE);
                serveTime_rl.setVisibility(View.GONE);
                oDeail_comment_ll.setVisibility(View.GONE);
                break;
        }
    }

    private void getReservatState() {
        switch (detailBean.getOrderState()) {
            case 0://同时有取消订单
                orderState_tv.setText("待支付");
                oDeatil_state_btn.setText("支付");
                oDeatil_cancel_btn.setVisibility(View.VISIBLE);
                findViewById(R.id.od_splite_view).setVisibility(View.VISIBLE);
                oDeatil_state_ll.setVisibility(View.VISIBLE);//底部按钮布局
                payWay_layout.setVisibility(View.GONE);//支付方式
                break;
            case 1:
                orderState_tv.setText("待确认");
                oDeatil_cancel_btn.setVisibility(View.VISIBLE);
                oDeatil_state_btn.setVisibility(View.GONE);
                oDeatil_state_ll.setVisibility(View.VISIBLE);
                break;
            case 2:
                orderState_tv.setText("已预约");
                oDeatil_cancel_btn.setVisibility(View.VISIBLE);//取消按钮
                oDeatil_state_btn.setVisibility(View.GONE);//支付按钮
                oDeatil_state_ll.setVisibility(View.VISIBLE);
                break;
            case 3:
                orderState_tv.setText("进行中");
                oDeatil_state_ll.setVisibility(View.GONE);//底部操作按钮布局
                break;
            case 4:
                orderState_tv.setText("待评论");
                oDeatil_state_btn.setText("去评价");
                oDeatil_state_ll.setVisibility(View.VISIBLE);
                break;
            case 5:
                orderState_tv.setText("已完成");
                oDeail_comment_ll.setVisibility(View.VISIBLE);//评论布局
                oDeatil_state_ll.setVisibility(View.GONE);
                break;
            case 6:
                orderState_tv.setText("退款中");
                oDeatil_state_ll.setVisibility(View.GONE);
                break;
            case 7:
                orderState_tv.setText("已关闭");
                oDeatil_state_ll.setVisibility(View.GONE);
                break;
            case 8:
                orderState_tv.setText("拒单");
                oDeatil_state_ll.setVisibility(View.GONE);
                break;
        }
    }
}

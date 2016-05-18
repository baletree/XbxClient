package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx.client.R;
import com.xbx.client.beans.OrderDetailBean;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.GuideParse;
import com.xbx.client.jsonparse.OrderParse;
import com.xbx.client.jsonparse.UtilParse;
import com.xbx.client.linsener.AnimateFirstDisplayListener;
import com.xbx.client.linsener.ImageLoaderConfigFactory;
import com.xbx.client.utils.SharePrefer;
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
public class OrderDetailActivity extends BaseActivity implements RatingBar.OnRatingBarChangeListener, TipsDialog.DialogClickListener {
    private ScrollView orderDetail_sv;
    private TextView title_rtxt_tv;
    private ImageView headImage;
    private TextView guide_typed_tv;
    private TextView guide_name_tv;
    private TextView guide_code_tv;
    private RatingBar guide_ratingbar;

    private TextView pay_aMony_tv;
    private TextView pay_locate_tv;
    private TextView pay_orderNum_tv;
    private TextView pay_startTime_tv;
    private TextView pay_stopTime_tv;
    private TextView pay_serveTime_tv;

    private Button order_Confirm_btn;//提交按钮

    private LinearLayout order_Comment_ll;//评论总布局
    private RatingBar order_comment_rb;
    private FlowLayout order_Tagfl;
    private EditText order_CommentMsg_et;

    private LinearLayout order_CommentShow_ll;//显示评论总布局
    private RatingBar order_showComment_rb;
    private FlowLayout order_showTag_fl;
    private TextView order_showMsg_tv;

    private Api api = null;
    private List<String> tagList = null;
    private List<String> tagShowList = null;
    private OrderDetailBean detailBean = null;
    private ImageLoader imageLoader;
    private ImageLoaderConfigFactory configFactory;
    private boolean[] tagState = null;
    private List<Integer> indexList = null;//记录评论标签的下标
    private TipsDialog tipsDialog = null;

    private String orderNuber = "";
    private String uid = "";

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
                    if (codes == 1) {
                        title_rtxt_tv.setOnClickListener(null);
                        Util.showToast(OrderDetailActivity.this, UtilParse.getRequestMsg(allData));
                    }
                    break;
                case TaskFlag.PAGEREQUESTWO:
                    String tagData = (String) msg.obj;
                    tagList = new ArrayList<>();
                    tagList = GuideParse.getGuideTags(tagData);
                    if (tagList == null)
                        return;
                    if (tagList.size() == 0)
                        return;
                    setTagLayout();
                    break;
                case TaskFlag.PAGEREQUESTHREE:
                    order_Comment_ll.setVisibility(View.GONE);
                    order_Confirm_btn.setVisibility(View.GONE);
                    order_CommentShow_ll.setVisibility(View.VISIBLE);
                    tagShowList = new ArrayList<>();
                    for (int i = 0; i < tagList.size(); i++) {
                        if (tagState[i]) {
                            tagShowList.add(tagList.get(i));
                        }
                    }
                    detailBean.setGuideTagList(tagShowList);
                    detailBean.setGuideCotent(order_CommentMsg_et.getText().toString());
                    detailBean.setCommentStar(order_comment_rb.getRating() + "");
                    setCommentInfo();
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
        uid = SharePrefer.getUserInfo(this).getUid();
        orderNuber = getIntent().getStringExtra("DetailOrderNumber");
        api = new Api(OrderDetailActivity.this, handler);
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
        tagList = new ArrayList<>();
        indexList = new ArrayList<>();
    }

    @Override
    protected void initViews() {
        super.initViews();
        orderDetail_sv = (ScrollView) findViewById(R.id.orderDetail_sv);
        orderDetail_sv.setVisibility(View.GONE);
        title_rtxt_tv = (TextView) findViewById(R.id.title_rtxt_tv);
        findViewById(R.id.guides_call_img).setVisibility(View.GONE);
        headImage = (ImageView) findViewById(R.id.guide_head_img);
        guide_typed_tv = (TextView) findViewById(R.id.guide_typed_tv);
        guide_name_tv = (TextView) findViewById(R.id.guide_name_tv);
        guide_code_tv = (TextView) findViewById(R.id.guide_code_tv);
        guide_ratingbar = (RatingBar) findViewById(R.id.guide_ratingbar);

        pay_aMony_tv = (TextView) findViewById(R.id.pay_aMony_tv);
        pay_locate_tv = (TextView) findViewById(R.id.pay_locate_tv);
        pay_orderNum_tv = (TextView) findViewById(R.id.pay_orderNum_tv);
        pay_startTime_tv = (TextView) findViewById(R.id.pay_startTime_tv);
        pay_stopTime_tv = (TextView) findViewById(R.id.pay_stopTime_tv);
        pay_serveTime_tv = (TextView) findViewById(R.id.pay_serveTime_tv);

        order_Comment_ll = (LinearLayout) findViewById(R.id.order_Comment_ll);
        order_comment_rb = (RatingBar) findViewById(R.id.order_comment_rb);
        order_comment_rb.setOnRatingBarChangeListener(this);
        order_Tagfl = (FlowLayout) findViewById(R.id.order_Tagfl);
        order_CommentMsg_et = (EditText) findViewById(R.id.order_CommentMsg_et);

        order_CommentShow_ll = (LinearLayout) findViewById(R.id.order_CommentShow_ll);
        order_showComment_rb = (RatingBar) findViewById(R.id.order_showComment_rb);
        order_showTag_fl = (FlowLayout) findViewById(R.id.order_showTag_fl);
        order_showMsg_tv = (TextView) findViewById(R.id.order_showMsg_tv);

        order_Confirm_btn = (Button) findViewById(R.id.order_Confirm_btn);
        findViewById(R.id.title_left_img).setOnClickListener(this);
        findViewById(R.id.guides_call_img).setOnClickListener(this);
        findViewById(R.id.pay_detail_rl).setOnClickListener(this);
        ((TextView) findViewById(R.id.title_txt_tv)).setText(getString(R.string.order_detail_title));
        title_rtxt_tv.setText(R.string.Complaints);
        title_rtxt_tv.setVisibility(View.VISIBLE);

        title_rtxt_tv.setOnClickListener(this);
        order_Confirm_btn.setOnClickListener(this);
        api.getOrderDetail(orderNuber);
    }

    private void setOderInfo() {
        orderDetail_sv.setVisibility(View.VISIBLE);
        imageLoader.displayImage(detailBean.getHeadImg(), headImage, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        guide_name_tv.setText(detailBean.getGuideName());
        guide_typed_tv.setText(StringUtil.getGuideType(this, detailBean.getGuideType()));
        guide_code_tv.setText(detailBean.getGuideNumber());

        ((TextView) findViewById(R.id.pay_MonTip_tv)).setText(getString(R.string.pay_yes));
        pay_aMony_tv.setText(detailBean.getOrderPay() + "");
        pay_orderNum_tv.setText(detailBean.getOrderNum());
        pay_startTime_tv.setText(detailBean.getOrderStartTime());
        pay_stopTime_tv.setText(detailBean.getOrderStartTime());
        pay_serveTime_tv.setText(detailBean.getServerDate());
        setStateInfo();
        if (!Util.isNull(detailBean.getGuideStar())) {
            guide_ratingbar.setRating(Float.valueOf(detailBean.getGuideStar()) / 2);
            ((TextView) findViewById(R.id.guide_star_tv)).setText(detailBean.getGuideStar() + getString(R.string.scole));
        }
        if (Util.isNull(detailBean.getUserAddress()))
            return;
        if (detailBean.getUserAddress().contains(getString(R.string.provice)))
            pay_locate_tv.setText(detailBean.getUserAddress().split(getString(R.string.provice))[1]);
        else
            pay_locate_tv.setText(detailBean.getUserAddress());
    }

    private void setStateInfo() {
        switch (detailBean.getServerType()) {
            case 0://即时服务
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
                if (detailBean.getServerType() == 1 && (detailBean.getOrderState() == 1 || detailBean.getOrderState() == 2)) {//预约取消订单
                    switch (detailBean.getOrderState()) {
                        case 1://待确认
                        case 2://已预约
                            tipsDialog = new TipsDialog(this);
                            tipsDialog.setBtnTxt(getString(R.string.cancel_stroke), getString(R.string.yes_stroke));
                            tipsDialog.setInfo(getString(R.string.cancel_order), "确定取消该预约服务的订单?");
                            tipsDialog.setClickListener(this);
                            tipsDialog.show();
                            break;
                    }
                    return;
                }
                break;
            case R.id.order_Confirm_btn:
                if (order_comment_rb.getRating() == 0) {
                    Util.showToast(OrderDetailActivity.this, getString(R.string.input_star));
                    return;
                }
                String tagStr = "";
                for (int i = 0; i < tagState.length; i++) {
                    if (tagState[i])
                        indexList.add(i);
                }
                if (indexList != null && indexList.size() > 0) {
                    for (int k = 0; k < indexList.size(); k++) {
                        tagStr = tagStr + indexList.get(k) + ",";
                    }
                }
                if (!Util.isNull(tagStr)) {
                    tagStr = tagStr.substring(0, tagStr.length() - 1);
                }
                api.submitComment(uid, detailBean.getOrderNum(), order_CommentMsg_et.getText().toString(), String.valueOf(order_comment_rb.getRating() * 2), tagStr);
                break;
            case R.id.pay_detail_rl:
                Intent intent = new Intent(this, PayDetailActivity.class);
                intent.putExtra("PayDetailBean", detailBean);
                startActivity(intent);
                break;
        }
    }

    private void getImmediaState() {
        switch (detailBean.getOrderState()) {
            case 0:
            case 1:
            case 2://进行中
                break;
            case 3://服务结束，待支付
                break;
            case 6://取消待支付
                break;
            case 4://待评价
                order_Comment_ll.setVisibility(View.VISIBLE);
                break;
            case 5://已完成
                order_Comment_ll.setVisibility(View.GONE);
                order_CommentShow_ll.setVisibility(View.VISIBLE);
                setCommentInfo();
                break;
            case 7://取消订单的已关闭，分为支付和违约超时
                if (detailBean.getOrderOrignalPay() == 0.0) {
                    findViewById(R.id.order_CancelInfo_ll).setVisibility(View.VISIBLE);
                    findViewById(R.id.order_SeverInfo_ll).setVisibility(View.GONE);
                }
                break;
        }
    }

    private void getReservatState() {
        switch (detailBean.getOrderState()) {
            case 0://待支付
                break;
            case 1://待确认
            case 2://已预约
                title_rtxt_tv.setText(getString(R.string.cancel_order));
                break;
            case 3://进行中
                break;
            case 4://待评论
                order_Comment_ll.setVisibility(View.VISIBLE);
                break;
            case 5://已完成
                order_Comment_ll.setVisibility(View.GONE);
                order_CommentShow_ll.setVisibility(View.VISIBLE);
                setCommentInfo();
                break;
            case 6://退款中

                break;
            case 7://已关闭

                break;
            case 8://拒单

                break;
        }
    }

    private void setCommentInfo() {
        if (!Util.isNull(detailBean.getCommentStar()))
            order_showComment_rb.setRating(Float.valueOf(detailBean.getCommentStar()) / 2);
        if (detailBean.getGuideTagList() != null) {
            order_showTag_fl.setVisibility(View.VISIBLE);
            for (int i = 0; i < detailBean.getGuideTagList().size(); i++) {
                order_showTag_fl.addView(addTextView(detailBean.getGuideTagList().get(i)));
            }
        }
        if (!Util.isNull(detailBean.getGuideCotent())) {
            order_showMsg_tv.setVisibility(View.VISIBLE);
            order_showMsg_tv.setText(detailBean.getGuideCotent());
        }
    }

    private void setTagLayout() {
        order_Tagfl.setVisibility(View.VISIBLE);
        order_CommentMsg_et.setVisibility(View.VISIBLE);
        tagState = new boolean[tagList.size()];
        for (int i = 0; i < tagList.size(); i++) {
            final TextView txtView = addCommentTextView(tagList.get(i));
            order_Tagfl.addView(txtView);
            final int finalI = i;
            txtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tagState[finalI]) {
                        txtView.setBackgroundResource(R.drawable.guide_tag_nocheckbg);
                        tagState[finalI] = false;
                    } else {
                        txtView.setBackgroundResource(R.drawable.guide_tag_checkbg);
                        tagState[finalI] = true;
                    }
                }
            });
        }
    }

    private TextView addTextView(String txt) {
        LinearLayout.LayoutParams txtLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(this);
        textView.setText(txt);
        textView.setPadding(20, 8, 20, 8);
        txtLp.rightMargin = 10;
        txtLp.bottomMargin = 8;
        textView.setBackgroundResource(R.drawable.guide_tag_checkbg);
        textView.setLayoutParams(txtLp);
        return textView;
    }

    private TextView addCommentTextView(String txt) {
        LinearLayout.LayoutParams txtLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(this);
        textView.setText(txt);
        textView.setPadding(20, 8, 20, 8);
        txtLp.rightMargin = 10;
        txtLp.bottomMargin = 8;
        textView.setBackgroundResource(R.drawable.guide_tag_nocheckbg);
        textView.setLayoutParams(txtLp);
        return textView;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if (fromUser) {
            if (tagList.size() == 0)
                api.getGuideTag();
            order_Confirm_btn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void cancelDialog() {
        tipsDialog.dismiss();
        api.cancelOrder(detailBean.getOrderNum());
    }

    @Override
    public void confirmDialog() {
        tipsDialog.dismiss();
    }
}

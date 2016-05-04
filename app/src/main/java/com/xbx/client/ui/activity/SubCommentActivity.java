package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx.client.R;
import com.xbx.client.beans.OrderDetailBean;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.GuideParse;
import com.xbx.client.jsonparse.OrderParse;
import com.xbx.client.linsener.AnimateFirstDisplayListener;
import com.xbx.client.linsener.ImageLoaderConfigFactory;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.StringUtil;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;
import com.xbx.client.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/13.
 * 提交评价
 */
public class SubCommentActivity extends BaseActivity {
    private FlowLayout comment_tag_fl;
    private TextView titleTxt;
    private ImageView backImg;
    private RatingBar conmment_rtb;
    //导游信息
    private ImageView headImage;
    private TextView guide_typed_tv;
    private TextView guide_name_tv;
    private TextView guide_code_tv;
    private RatingBar guide_ratingbar;
    private TextView user_stroke_tv;//目的地
    private TextView comment_pay_tv;//支付金额
    private EditText comment_msg_tv;

    private List<String> tagList = null;
    private boolean[] tagState = null;
    private Api api = null;
    private List<Integer> indexList = null;
    private ImageLoader imageLoader;
    private ImageLoaderConfigFactory configFactory;

    private String uid = "";
    private String gorderNum;
    private boolean isFromOrder = false;
    private OrderDetailBean detailBean = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TaskFlag.PAGEREQUESTWO:
                    String tagData = (String) msg.obj;
                    tagList = GuideParse.getGuideTags(tagData);
                    if (tagList == null)
                        return;
                    if (tagList.size() == 0)
                        return;
                    setTagLayout();
                    break;
                case TaskFlag.REQUESTSUCCESS:
                    String dataDetail = (String) msg.obj;
                    detailBean = OrderParse.getDetailOrder(dataDetail);
                    if (detailBean == null)
                        return;
                    setCommentInfo();
                    break;
                case TaskFlag.PAGEREQUESTHREE:
                    Intent intent = new Intent();
                    if (detailBean.getServerType() == 1 && conmment_rtb.getRating() > 4.0)
                        intent.putExtra("reservatComment", true);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcoment);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        uid = SharePrefer.getUserInfo(SubCommentActivity.this).getUid();
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
        isFromOrder = getIntent().getBooleanExtra("isFromOrderDetail", false);
        gorderNum = getIntent().getStringExtra("GuideOrderNum");
        api = new Api(SubCommentActivity.this, handler);
        api.getGuideTag();
        indexList = new ArrayList<>();
    }

    @Override
    protected void initViews() {
        super.initViews();
        comment_tag_fl = (FlowLayout) findViewById(R.id.comment_tag_fl);
        titleTxt = (TextView) findViewById(R.id.title_txt_tv);
        titleTxt.setText(getString(R.string.submit_comment));
        backImg = (ImageView) findViewById(R.id.title_left_img);
        conmment_rtb = (RatingBar) findViewById(R.id.conmment_rtb);
        headImage = (ImageView) findViewById(R.id.guide_head_img);
        guide_typed_tv = (TextView) findViewById(R.id.guide_typed_tv);
        guide_name_tv = (TextView) findViewById(R.id.guide_name_tv);
        guide_code_tv = (TextView) findViewById(R.id.guide_code_tv);
        guide_ratingbar = (RatingBar) findViewById(R.id.guide_ratingbar);
        user_stroke_tv = (TextView) findViewById(R.id.user_stroke_tv);
        comment_pay_tv = (TextView) findViewById(R.id.comment_pay_tv);
        comment_msg_tv = (EditText) findViewById(R.id.comment_msg_tv);
        backImg.setOnClickListener(this);
        findViewById(R.id.submit_comm_btn).setOnClickListener(this);
        if (isFromOrder) {
            detailBean = (OrderDetailBean) getIntent().getSerializableExtra("orderDetailBean");
            if (detailBean == null)
                return;
            setCommentInfo();
        } else
            api.getOrderDetail(gorderNum);
    }

    private void setTagLayout() {
        comment_tag_fl.setVisibility(View.VISIBLE);
        tagState = new boolean[tagList.size()];
        for (int i = 0; i < tagList.size(); i++) {
            final TextView txtView = addTextView(tagList.get(i));
            comment_tag_fl.addView(txtView);
            final int finalI = i;
            txtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tagState[finalI]) {
                        txtView.setBackgroundResource(R.color.colorBackground);
                        tagState[finalI] = false;
                    } else {
                        txtView.setBackgroundResource(R.drawable.guide_tag_bg);
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
        textView.setLayoutParams(txtLp);
        return textView;
    }

    private void setCommentInfo() {
        imageLoader.displayImage(detailBean.getHeadImg(), headImage, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        guide_name_tv.setText(detailBean.getGuideName());
        guide_typed_tv.setText(StringUtil.getGuideType(this, detailBean.getGuideType()));
        guide_code_tv.setText(detailBean.getGuideNumber());
        user_stroke_tv.setText(detailBean.getUserAddress());
        comment_pay_tv.setText(detailBean.getOrderPay() + "");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_img:
                finish();
                break;
            case R.id.submit_comm_btn:
                if (conmment_rtb.getRating() == 0) {
                    Util.showToast(SubCommentActivity.this, getString(R.string.input_star));
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
                if (detailBean == null)
                    return;
                api.submitComment(uid, detailBean.getOrderNum(), comment_msg_tv.getText().toString(), String.valueOf(conmment_rtb.getRating() * 2), tagStr);
                break;
        }
    }
}

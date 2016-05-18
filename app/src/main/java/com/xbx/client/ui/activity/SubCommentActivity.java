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
import com.xbx.client.view.TipsDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/13.
 * 提交评价
 */
public class SubCommentActivity extends BaseActivity implements TipsDialog.DialogClickListener,RatingBar.OnRatingBarChangeListener {
    private TextView titleTxt;
    //导游信息
    private ImageView headImage;
    private TextView guide_typed_tv;
    private TextView guide_name_tv;
    private TextView guide_code_tv;
    private RatingBar guide_ratingbar;

    private RatingBar order_comment_rb;
    private FlowLayout order_Tagfl;
    private EditText order_CommentMsg_et;
    private Button sub_Comment_btn;

    private List<String> tagList = null;
    private boolean[] tagState = null;
    private Api api = null;
    private List<Integer> indexList = null;
    private ImageLoader imageLoader;
    private ImageLoaderConfigFactory configFactory;
    private TipsDialog tipsDialog = null;

    private String uid = "";
    private String gorderNum;
    private OrderDetailBean detailBean = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TaskFlag.PAGEREQUESTWO:
                    String tagData = (String) msg.obj;
                    tagList = GuideParse.getGuideTags(tagData);
                    break;
                case TaskFlag.REQUESTSUCCESS:
                    String dataDetail = (String) msg.obj;
                    detailBean = OrderParse.getDetailOrder(dataDetail);
                    if (detailBean == null)
                        return;
                    setCommentInfo();
                    break;
                case TaskFlag.PAGEREQUESTHREE:
                    if (detailBean.getServerType() == 1 && order_comment_rb.getRating() > 4.0){
                        tipsDialog = new TipsDialog(SubCommentActivity.this);
                        tipsDialog.setClickListener(SubCommentActivity.this);
                        tipsDialog.isSHowRewardImg(true);
                        tipsDialog.setInfo(getString(R.string.to_reward), getString(R.string.to_reward_tips));
                        tipsDialog.setBtnTxt(getString(R.string.to_reward_cancel), getString(R.string.to_reward_sure));
                        tipsDialog.show();
                        return;
                    }
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
        gorderNum = getIntent().getStringExtra("SubCommentOrderNum");
        api = new Api(SubCommentActivity.this, handler);
        api.getGuideTag();
        api.getOrderDetail(gorderNum);
        indexList = new ArrayList<>();
    }

    @Override
    protected void initViews() {
        super.initViews();
        titleTxt = (TextView) findViewById(R.id.title_txt_tv);
        titleTxt.setText(getString(R.string.submit_comment));
        headImage = (ImageView) findViewById(R.id.guide_head_img);
        guide_typed_tv = (TextView) findViewById(R.id.guide_typed_tv);
        guide_name_tv = (TextView) findViewById(R.id.guide_name_tv);
        guide_code_tv = (TextView) findViewById(R.id.guide_code_tv);
        guide_ratingbar = (RatingBar) findViewById(R.id.guide_ratingbar);

        order_comment_rb = (RatingBar) findViewById(R.id.order_comment_rb);
        order_comment_rb.setOnRatingBarChangeListener(this);
        order_Tagfl = (FlowLayout) findViewById(R.id.order_Tagfl);
        order_CommentMsg_et = (EditText) findViewById(R.id.order_CommentMsg_et);
        sub_Comment_btn = (Button) findViewById(R.id.sub_Comment_btn);

        findViewById(R.id.user_stroke_tv).setVisibility(View.GONE);
        findViewById(R.id.sub_Comment_ll).setVisibility(View.VISIBLE);
        findViewById(R.id.title_left_img).setOnClickListener(this);
        sub_Comment_btn.setOnClickListener(this);
    }

    private void setTagLayout() {
        order_Tagfl.setVisibility(View.VISIBLE);
        sub_Comment_btn.setVisibility(View.VISIBLE);
        tagState = new boolean[tagList.size()];
        for (int i = 0; i < tagList.size(); i++) {
            final TextView txtView = addTextView(tagList.get(i));
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
        textView.setLayoutParams(txtLp);
        textView.setBackgroundResource(R.drawable.guide_tag_nocheckbg);
        return textView;
    }

    private void setCommentInfo() {
        imageLoader.displayImage(detailBean.getHeadImg(), headImage, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        guide_name_tv.setText(detailBean.getGuideName());
        guide_typed_tv.setText(StringUtil.getGuideType(this, detailBean.getGuideType()));
        guide_code_tv.setText(detailBean.getGuideNumber());
        if (!Util.isNull(detailBean.getGuideStar())) {
            guide_ratingbar.setRating(Float.valueOf(detailBean.getGuideStar()) / 2);
            ((TextView) findViewById(R.id.guide_star_tv)).setText(detailBean.getGuideStar() + getString(R.string.scole));
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_img:
                finish();
                break;
            case R.id.sub_Comment_btn:
                if (order_comment_rb.getRating() == 0) {
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
                api.submitComment(uid, detailBean.getOrderNum(), order_CommentMsg_et.getText().toString(), String.valueOf(order_comment_rb.getRating() * 2), tagStr);
                break;
        }
    }

    @Override
    public void cancelDialog() {
        setResult(RESULT_OK, new Intent());
        tipsDialog.dismiss();
        finish();
    }

    @Override
    public void confirmDialog() {
        setResult(RESULT_OK, new Intent());
        tipsDialog.dismiss();
        finish();
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if(fromUser){
            order_CommentMsg_et.setVisibility(View.VISIBLE);
            if (tagList == null)
                return;
            if (tagList.size() == 0)
                return;
            setTagLayout();
        }
    }
}

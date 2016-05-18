package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx.client.R;
import com.xbx.client.beans.GuideDetailBean;
import com.xbx.client.beans.ReservatInfoBean;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.GuideParse;
import com.xbx.client.jsonparse.UtilParse;
import com.xbx.client.linsener.AnimateFirstDisplayListener;
import com.xbx.client.linsener.ImageLoaderConfigFactory;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;
import com.xbx.client.view.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by EricYuan on 2016/4/20.
 */
public class TourDetailActivity extends BaseActivity {
    private RoundedImageView personal_head_img;
    private TextView person_name_tv;
    private TextView person_code_tv;
    private RatingBar person_ratingbar;
    private TextView person_star_tv;

    private TextView personal_price_tv;
    private TextView personal_count_tv;
    private TextView personal_intro_tv;
    private TextView personal_stantat_tv;
    private FlowLayout personal_showTag_fl;

    private List<String> tagList = null;
    private Api api = null;
    private ReservatInfoBean reservatInfo = null;
    private GuideDetailBean guideDetail = null;
    private ImageLoader imageLoader;
    private ImageLoaderConfigFactory configFactory;

    private String guideId = "";
    private String uid = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TaskFlag.REQUESTSUCCESS://导游详情
                    String guideData = (String) msg.obj;
                    guideDetail = GuideParse.getGuideDetail(guideData);
                    if (guideDetail == null)
                        return;
                    setGuideInfo();
                    break;
                case TaskFlag.PAGEREQUESTHREE://下单成功
                    String guideDatas = (String) msg.obj;
                    String orderNum = getOrderNum(guideDatas);
                    Intent intent = new Intent(TourDetailActivity.this, PayOrderActivity.class);
                    intent.putExtra("PayOrderNum", orderNum);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
    }

    @Override
    protected void initViews() {
        super.initViews();
        personal_head_img = (RoundedImageView) findViewById(R.id.personal_head_img);
        person_name_tv = (TextView) findViewById(R.id.person_name_tv);
        person_code_tv = (TextView) findViewById(R.id.person_code_tv);
        person_ratingbar = (RatingBar) findViewById(R.id.person_ratingbar);
        person_star_tv = (TextView) findViewById(R.id.person_star_tv);

        personal_price_tv = (TextView) findViewById(R.id.personal_price_tv);
        personal_count_tv = (TextView) findViewById(R.id.personal_count_tv);

        personal_intro_tv = (TextView) findViewById(R.id.personal_intro_tv);
        personal_stantat_tv = (TextView) findViewById(R.id.personal_stantat_tv);
        personal_showTag_fl = (FlowLayout) findViewById(R.id.personal_showTag_fl);

        api.getGuideDetail(guideId);
        findViewById(R.id.title_left_img).setOnClickListener(this);
        findViewById(R.id.personal_Reservat_btn).setOnClickListener(this);
    }

    @Override
    protected void initDatas() {
        uid = SharePrefer.getUserInfo(TourDetailActivity.this).getUid();
        guideId = getIntent().getStringExtra("guideId");
        reservatInfo = (ReservatInfoBean) getIntent().getSerializableExtra("reservatInfo");
        api = new Api(TourDetailActivity.this, handler);
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
    }

    private void setGuideInfo() {
        imageLoader.displayImage(guideDetail.getGuideHead(), personal_head_img, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        person_name_tv.setText(guideDetail.getGuideName());
        person_code_tv.setText(guideDetail.getGuideNumber());
        person_star_tv.setText(guideDetail.getGuideStar() + getString(R.string.scole));
        personal_price_tv.setText(guideDetail.getGuideReservatPrice());
        personal_count_tv.setText(guideDetail.getGuideTimes());

        if(!Util.isNull(guideDetail.getGuideIntroduce())){
            personal_intro_tv.setText(guideDetail.getGuideIntroduce());
            findViewById(R.id.personal_intro_ll).setVisibility(View.VISIBLE);
        }
        if(!Util.isNull(guideDetail.getGuideStandard())){
            personal_stantat_tv.setText(guideDetail.getGuideStandard());
            findViewById(R.id.personal_stantat_ll).setVisibility(View.VISIBLE);
        }
        if (!Util.isNull(guideDetail.getGuideStar()))
            person_ratingbar.setRating(Float.valueOf(guideDetail.getGuideStar()) / 2);

        tagList = guideDetail.getTagList();
        if (tagList != null && tagList.size() > 0) {
            findViewById(R.id.personal_showTag_ll).setVisibility(View.VISIBLE);
            for (int i = 0; i < tagList.size(); i++) {
                personal_showTag_fl.addView(addTextView(tagList.get(i)));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_img:
                finish();
                break;
            case R.id.personal_Reservat_btn:
                if (reservatInfo == null)
                    return;
                Util.pLog("详情中下单导游类型：" + reservatInfo.getGuideType());
                api.reservatGuide(uid, guideId, reservatInfo.getCityId(), reservatInfo.getAddress(), reservatInfo.getStartTime(), reservatInfo.getEndTime(), reservatInfo.getGuideType());
                break;
        }
    }

    private TextView addTextView(String txt) {
        LinearLayout.LayoutParams txtLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(this);
        textView.setText(txt);
        textView.setPadding(20, 8, 20, 8);
        txtLp.rightMargin = 8;
        txtLp.bottomMargin = 12;
        textView.setBackgroundResource(R.drawable.guide_tag_nocheckbg);
        textView.setLayoutParams(txtLp);
        return textView;
    }

    private String getOrderNum(String json) {
        String orderNum = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (UtilParse.checkTag(jsonObject, "order_number"))
                orderNum = jsonObject.getString("order_number");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderNum;
    }
}

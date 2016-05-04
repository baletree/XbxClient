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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/20.
 */
public class TourDetailActivity extends BaseActivity {
    private RoundedImageView tour_detail_head_img;
    private TextView tour_detail_name;
    private TextView tour_detail_number;
    private RatingBar tour_detail_ratingbar;
    private TextView tour_detail_score;
    private TextView tour_detail_price_byhour;
    private TextView tour_detail_price_byday;
    private TextView tour_detail_num;
    private FlowLayout tour_detail_fl;
    private TextView tour_detail_intro;
    private TextView tour_detail_standard;

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
                    intent.putExtra("GuideOrderNum", orderNum);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = this.getWindow();
        window.setFlags(flag, flag);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
    }

    @Override
    protected void initViews() {
        super.initViews();
        tour_detail_head_img = (RoundedImageView) findViewById(R.id.tour_detail_head_img);
        tour_detail_name = (TextView) findViewById(R.id.tour_detail_name);
        tour_detail_number = (TextView) findViewById(R.id.tour_detail_number);
        tour_detail_ratingbar = (RatingBar) findViewById(R.id.tour_detail_ratingbar);
        tour_detail_score = (TextView) findViewById(R.id.tour_detail_score);
        tour_detail_price_byhour = (TextView) findViewById(R.id.tour_detail_price_byhour);
        tour_detail_price_byday = (TextView) findViewById(R.id.tour_detail_price_byday);
        tour_detail_num = (TextView) findViewById(R.id.tour_detail_num);
        tour_detail_fl = (FlowLayout) findViewById(R.id.tour_detail_fl);
        tour_detail_intro = (TextView) findViewById(R.id.tour_detail_intro);
        tour_detail_standard = (TextView) findViewById(R.id.tour_detail_standard);
        api.getGuideDetail(guideId);
        findViewById(R.id.tour_detail_back_img).setOnClickListener(this);
        findViewById(R.id.tour_detail_order).setOnClickListener(this);
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
        imageLoader.displayImage(guideDetail.getGuideHead(), tour_detail_head_img, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        tour_detail_name.setText(guideDetail.getGuideName());
        tour_detail_number.setText(guideDetail.getGuideNumber());
        tour_detail_score.setText(guideDetail.getGuideStar() + getString(R.string.scole));
        tour_detail_price_byhour.setText(guideDetail.getGuideImmediaPrice() + getString(R.string.server_day));
        tour_detail_price_byday.setText(guideDetail.getGuideReservatPrice() + getString(R.string.server_hour));
        tour_detail_num.setText(guideDetail.getGuideTimes() + getString(R.string.server_times));
        if(!Util.isNull(guideDetail.getGuideIntroduce())){
            tour_detail_intro.setText(guideDetail.getGuideIntroduce());
            tour_detail_intro.setVisibility(View.VISIBLE);
        }
        if(!Util.isNull(guideDetail.getGuideStandard())){
            tour_detail_standard.setText(guideDetail.getGuideStandard());
            tour_detail_standard.setVisibility(View.VISIBLE);
        }
        if (!Util.isNull(guideDetail.getGuideStar()))
            tour_detail_ratingbar.setRating(Float.valueOf(guideDetail.getGuideStar()) / 2);
        tagList = guideDetail.getTagList();
        if (tagList != null && tagList.size() > 0) {
            for (int i = 0; i < tagList.size(); i++) {
                tour_detail_fl.addView(addTextView(tagList.get(i)));
            }
        } else
            tour_detail_fl.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tour_detail_back_img:
                finish();
                break;
            case R.id.tour_detail_order:
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
        txtLp.rightMargin = 10;
        txtLp.bottomMargin = 8;
        textView.setBackgroundResource(R.drawable.guide_tag_bg);
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

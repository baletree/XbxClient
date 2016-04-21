package com.xbx.client.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.xbx.client.R;
import com.xbx.client.view.FlowLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        findViewById(R.id.tour_detail_back_img).setOnClickListener(this);
        findViewById(R.id.tour_detail_head_img).setOnClickListener(this);

        for (int i = 0; i < tagList.size(); i++) {
            tour_detail_fl.addView(addTextView(tagList.get(i)));
        }
    }

    @Override
    protected void initDatas() {
        tagList = new ArrayList<>();
        tagList.add("大胆");
        tagList.add("脾气大");
        tagList.add("路线熟悉活地图");
        tagList.add("管吃管住管穿");
        tagList.add("管吃管住");
        tagList.add("路线熟悉活地图的能忍");
        tagList.add("管吃管住");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tour_detail_back_img :
                finish();
                break;
            case R.id.tour_detail_head_img :
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
}

package com.xbx.client.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xbx.client.R;
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

    private List<String> tagList = null;
    private boolean[] tagState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcoment);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        tagList = new ArrayList<>();
        tagList.add("大胆");
        tagList.add("脾气大");
        tagList.add("路线熟悉活地图");
        tagList.add("管吃管住管穿");
        tagList.add("管吃管住");
        tagList.add("路线熟悉活地图的能忍");
        tagList.add("管吃管住");
        tagState = new boolean[tagList.size()];
    }

    @Override
    protected void initViews() {
        super.initViews();
        comment_tag_fl = (FlowLayout) findViewById(R.id.comment_tag_fl);
        titleTxt = (TextView) findViewById(R.id.title_txt_tv);
        titleTxt.setText(getString(R.string.submit_comment));
        backImg = (ImageView) findViewById(R.id.title_left_img);
        backImg.setOnClickListener(this);
        for (int i = 0; i < tagList.size(); i++) {
            final TextView txtView = addTextView(tagList.get(i));
            comment_tag_fl.addView(txtView);
            final int finalI = i;
            txtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tagState[finalI]){
                        txtView.setBackgroundResource(R.color.colorBackground);
                        tagState[finalI] = false;
                    }else{
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_left_img:
                finish();
                break;
        }
    }
}

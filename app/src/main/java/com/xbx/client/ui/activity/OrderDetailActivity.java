package com.xbx.client.ui.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/8.
 */
public class OrderDetailActivity extends BaseActivity {
    private FlowLayout guide_tag_flayout;
    private TextView title_txt;

    private List<String> tagList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
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
    }

    @Override
    protected void initViews() {
        super.initViews();
        title_txt = (TextView) findViewById(R.id.title_txt_tv);
        title_txt.setText(getString(R.string.order_detail_title));
        guide_tag_flayout = (FlowLayout) findViewById(R.id.guide_tag_flayout);
        for (int i = 0; i < tagList.size(); i++) {
            guide_tag_flayout.addView(addTextView(tagList.get(i)));
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

package com.xbx.client.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.adapter.GuideTagAdapter;
import com.xbx.client.utils.Util;
import com.xbx.client.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/8.
 */
public class OrderDetailActivity extends BaseActivity {
    private RecyclerView guide_tag_rv;
    private List<String> tagList = null;
    private GuideTagAdapter tagAdapter = null;
    private TextView title_txt_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        tagList = new ArrayList<>();
        tagList.add("大胆心细");
        tagList.add("脾气大");
        tagList.add("路线熟悉");
        tagList.add("管吃管住管穿");
    }

    @Override
    protected void initViews() {
        super.initViews();
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);
        title_txt_tv.setText(R.string.order_detail_title);
        guide_tag_rv = (RecyclerView) findViewById(R.id.guide_tag_rv);
        guide_tag_rv.setItemAnimator(new DefaultItemAnimator());
        guide_tag_rv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        guide_tag_rv.addItemDecoration(new RecycleViewDivider(this, R.color.colorBackground));
        tagAdapter = new GuideTagAdapter(tagList);
        guide_tag_rv.setAdapter(tagAdapter);
    }

}

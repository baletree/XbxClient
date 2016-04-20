package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.adapter.ChoiceGuideAdapter;
import com.xbx.client.beans.ReservatInfoBean;
import com.xbx.client.http.Api;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;
import com.xbx.client.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricPeng on 2016/4/11.
 * 预约导游选择导游
 */
public class ChoiceGuideActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, ChoiceGuideAdapter.ItemLisener {
    private TextView title_txt_tv;
    private ImageView title_left_img;
    private RecyclerView reserve_guide_rv;
    private SwipeRefreshLayout reserve_guide_swipe;

    private LinearLayoutManager layoutManager = null;
    private ChoiceGuideAdapter choiceAdapter = null;
    private List<String> infoList = null;
    private ReservatInfoBean reservatBean = null;
    private Api api = null;

    private boolean isLoading = false;
    private int maxCount = 6;
    private int pageIndex = 0;
    private int pageNum = 10;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TaskFlag.REQUESTSUCCESS:

                    break;
                case TaskFlag.REQUESTERROR:

                    break;
            }
            reserve_guide_swipe.setRefreshing(false);
            if (isLoading) {
                choiceAdapter.notifyItemRemoved(choiceAdapter.getItemCount());
                isLoading = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_guide);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        layoutManager = new LinearLayoutManager(this);
        infoList = new ArrayList<>();
        reservatBean = (ReservatInfoBean) getIntent().getSerializableExtra("ReservatInfo");
        api = new Api(ChoiceGuideActivity.this, handler);
        api.getReserveGuideList(reservatBean, pageIndex + "", pageNum + "");

    }

    @Override
    protected void initViews() {
        super.initViews();
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);
        title_txt_tv.setText(getString(R.string.choice_guide));
        title_left_img = (ImageView) findViewById(R.id.title_left_img);
        title_left_img.setOnClickListener(this);
        reserve_guide_rv = (RecyclerView) findViewById(R.id.reserve_guide_rv);
        reserve_guide_swipe = (SwipeRefreshLayout) findViewById(R.id.reserve_guide_swipe);
        reserve_guide_swipe.setColorSchemeColors(R.color.colorTheme);
        reserve_guide_swipe.setOnRefreshListener(this);
        reserve_guide_rv.setLayoutManager(layoutManager);
        reserve_guide_rv.addItemDecoration(new RecycleViewDivider(this, R.drawable.spitline_bg));
        reserve_guide_rv.setItemAnimator(new DefaultItemAnimator());
        reserve_guide_rv.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == choiceAdapter.getItemCount()) {
                    boolean isRefreshing = reserve_guide_swipe.isRefreshing();
                    if (isRefreshing) {
                        isLoading = false;
                        choiceAdapter.notifyItemRemoved(choiceAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        pageIndex++;
                        api.getReserveGuideList(reservatBean, pageIndex + "", pageNum + "");
                    }
                }
            }
        });
        choiceAdapter = new ChoiceGuideAdapter(ChoiceGuideActivity.this, infoList);
        reserve_guide_rv.setAdapter(choiceAdapter);
        choiceAdapter.setOnItemLisener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_img:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        api.getReserveGuideList(reservatBean, pageIndex + "", pageNum + "");
    }

    @Override
    public void clickDetail() {
        startActivity(new Intent(ChoiceGuideActivity.this, GuideDetailActivity.class));
    }

    @Override
    public void clickGiveOrder() {

    }
}

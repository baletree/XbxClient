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
import com.xbx.client.adapter.ChoicedGuideAdapter;
import com.xbx.client.beans.ReservatInfoBean;
import com.xbx.client.beans.ServerListBean;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.ServerParse;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;
import com.xbx.client.view.RecycleViewDivider;
import com.xbx.client.view.pulllistview.PullToRefreshLayout;
import com.xbx.client.view.pulllistview.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricPeng on 2016/4/11.
 * 预约导游选择导游
 */
public class ChoiceGuideActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private TextView title_txt_tv;
    private ImageView title_left_img;
    private PullToRefreshLayout choice_refresh_layout;
    private PullableListView choice_refresh_plv;

    private ChoicedGuideAdapter choiceAdapter = null;
    private ReservatInfoBean reservatBean = null;
    private Api api = null;
    private List<ServerListBean> sList = null;

    private int pageIndex = 1;
    private int pageNum = 10;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TaskFlag.REQUESTSUCCESS:
                    String sListData = (String) msg.obj;
                    sList = ServerParse.getServerList(sListData);
                    if (sList == null)
                        return;
                    choiceAdapter = new ChoicedGuideAdapter(ChoiceGuideActivity.this,sList);
                    choice_refresh_plv.setAdapter(choiceAdapter);
                    break;
                case TaskFlag.REQUESTERROR:

                    break;
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
        reservatBean = (ReservatInfoBean) getIntent().getSerializableExtra("ReservatInfo");
        api = new Api(ChoiceGuideActivity.this, handler);
//        api.getReserveGuideList(reservatBean, pageIndex + "", pageNum + "");
    }

    @Override
    protected void initViews() {
        super.initViews();
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);
        title_txt_tv.setText(getString(R.string.choice_guide));
        title_left_img = (ImageView) findViewById(R.id.title_left_img);
        title_left_img.setOnClickListener(this);
        choice_refresh_layout = (PullToRefreshLayout) findViewById(R.id.choice_refresh_layout);
        choice_refresh_plv = (PullableListView) findViewById(R.id.choice_refresh_plv);
        choice_refresh_layout.setOnRefreshListener(this);
        sList = new ArrayList<>();
        sList.add(new ServerListBean());
        sList.add(new ServerListBean());
        sList.add(new ServerListBean());
        sList.add(new ServerListBean());
        sList.add(new ServerListBean());
        sList.add(new ServerListBean());
        sList.add(new ServerListBean());
        sList.add(new ServerListBean());
        sList.add(new ServerListBean());
        sList.add(new ServerListBean());
        sList.add(new ServerListBean());
        choiceAdapter = new ChoicedGuideAdapter(ChoiceGuideActivity.this,sList);
        choice_refresh_plv.setAdapter(choiceAdapter);
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
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}

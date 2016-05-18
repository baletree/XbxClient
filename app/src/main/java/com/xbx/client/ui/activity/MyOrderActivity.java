package com.xbx.client.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.xbx.client.R;
import com.xbx.client.adapter.MyOrderAdapter;
import com.xbx.client.beans.OrderBean;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.OrderParse;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;
import com.xbx.client.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/6.
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity implements MyOrderAdapter.OnRecyItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ImageView title_left_img;
    private TextView title_txt_tv;
    private RecyclerView order_rv;
    private SwipeRefreshLayout od_refresh_lay;

    private LinearLayoutManager layoutManager = null;
    private MyOrderAdapter myOrderAdapter = null;
    private Api api = null;
    private List<OrderBean> orderList = null;

    private String uid = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TaskFlag.REQUESTSUCCESS:
                    String data = (String) msg.obj;
                    orderList = OrderParse.orderListParse(data);
                    myOrderAdapter = new MyOrderAdapter(MyOrderActivity.this, orderList);
                    order_rv.setAdapter(myOrderAdapter);
                    myOrderAdapter.setOnItemClickListener(MyOrderActivity.this);
                    od_refresh_lay.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        uid = SharePrefer.getUserInfo(this).getUid();
        layoutManager = new LinearLayoutManager(this);
        orderList = new ArrayList<>();
        api = new Api(MyOrderActivity.this, handler);
    }

    @Override
    protected void initViews() {
        super.initViews();
        title_left_img = (ImageView) findViewById(R.id.title_left_img);
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);
        od_refresh_lay = (SwipeRefreshLayout) findViewById(R.id.od_refresh_lay);
        order_rv = (RecyclerView) findViewById(R.id.order_rv);
        od_refresh_lay.setOnRefreshListener(this);
        title_txt_tv.setText(getString(R.string.myorder_title));
        title_left_img.setOnClickListener(this);
        order_rv.setLayoutManager(layoutManager);
        order_rv.addItemDecoration(new RecycleViewDivider(this, R.drawable.spitline_bg));
        order_rv.setItemAnimator(new DefaultItemAnimator());
        api.getMyOrderList(uid, true);
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
    public void onItemClick(View v, int position) {
        intoStateAct(position);
    }

    private void intoStateAct(int position) {
        Intent intent = new Intent();
        switch (orderList.get(position).getOrderType()) {
            case 0://即时服务
                switch (orderList.get(position).getOrderState()) {
                    case 3://PayOrderActivity
                        intent.setClass(MyOrderActivity.this, PayOrderActivity.class);
                        intent.putExtra("PayOrderNum", orderList.get(position).getOrderNum());
                        break;
                    case 6://CancelOrderSucActivity
                        intent.putExtra("CancelOrderNum", orderList.get(position).getOrderNum());
                        intent.putExtra("isFromOrderList", true);
                        intent.setClass(MyOrderActivity.this, CancelOrderSucActivity.class);
                        break;
                    default:
                        intent.putExtra("DetailOrderNumber", orderList.get(position).getOrderNum());
                        intent.setClass(MyOrderActivity.this, OrderDetailActivity.class);
                        break;
                }
                break;
            case 1://预约服务
                switch (orderList.get(position).getOrderState()) {
                    case 0://PayOrderActivity
                        intent.setClass(MyOrderActivity.this, PayOrderActivity.class);
                        intent.putExtra("PayOrderNum", orderList.get(position).getOrderNum());
                        break;
                    default:
                        intent.putExtra("DetailOrderNumber", orderList.get(position).getOrderNum());
                        intent.setClass(MyOrderActivity.this, OrderDetailActivity.class);
                        break;
                }
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        api.getMyOrderList(uid, false);
    }
}

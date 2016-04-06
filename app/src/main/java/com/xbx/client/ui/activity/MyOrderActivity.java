package com.xbx.client.ui.activity;

import android.os.Bundle;
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
import com.xbx.client.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/6.
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity {
    private ImageView title_left_img;
    private TextView title_txt_tv;
    private RecyclerView order_rv;

    private LinearLayoutManager layoutManager = null;
    private MyOrderAdapter myOrderAdapter = null;
    private List<OrderBean> orderList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        layoutManager = new LinearLayoutManager(this);
        orderList = new ArrayList<>();
        OrderBean orderBean1 = new OrderBean();
        orderBean1.setOrderTime("4月6日 10：35");
        orderBean1.setGuideType("随游");
        orderBean1.setOrderState("进行中");
        orderBean1.setOrderAddress("都江堰青城山");
        OrderBean orderBean2 = new OrderBean();
        orderBean2.setOrderTime("10月26日 10：35");
        orderBean2.setGuideType("土著");
        orderBean2.setOrderState("未开始");
        orderBean2.setOrderAddress("峨眉山");
        orderList.add(orderBean1);
        orderList.add(orderBean2);
    }

    @Override
    protected void initViews() {
        super.initViews();
        title_left_img = (ImageView) findViewById(R.id.title_left_img);
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);
        order_rv = (RecyclerView) findViewById(R.id.order_rv);
        title_txt_tv.setText(getString(R.string.myorder_title));
        title_left_img.setOnClickListener(this);
        order_rv.setLayoutManager(layoutManager);
        order_rv.addItemDecoration(new RecycleViewDivider(this, R.drawable.spitline_bg));
        RecyclerViewHeader recyclerHeader = (RecyclerViewHeader) findViewById(R.id.order_head_layout);
        recyclerHeader.attachTo(order_rv, true);
        order_rv.setItemAnimator(new DefaultItemAnimator());
        myOrderAdapter = new MyOrderAdapter(orderList);
        order_rv.setAdapter(myOrderAdapter);
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

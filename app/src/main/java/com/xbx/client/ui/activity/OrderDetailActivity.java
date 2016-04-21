package com.xbx.client.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.beans.OrderDetailBean;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.OrderParse;
import com.xbx.client.utils.TaskFlag;
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
    private Api api = null;
    private OrderDetailBean detailBean = null;

    private String orderNuber = "";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TaskFlag.REQUESTSUCCESS:
                    String dataDetail = (String) msg.obj;
                    detailBean = OrderParse.getDetailOrder(dataDetail);
                    if(detailBean == null)
                        return;
                    setOderInfo();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        orderNuber = getIntent().getStringExtra("orderNumber");
        api = new Api(OrderDetailActivity.this,handler);
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
        findViewById(R.id.title_left_img).setOnClickListener(this);
        guide_tag_flayout = (FlowLayout) findViewById(R.id.guide_tag_flayout);
        for (int i = 0; i < tagList.size(); i++) {
            guide_tag_flayout.addView(addTextView(tagList.get(i)));
        }
        api.getOrderDetail(orderNuber);
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

    private void setOderInfo(){

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

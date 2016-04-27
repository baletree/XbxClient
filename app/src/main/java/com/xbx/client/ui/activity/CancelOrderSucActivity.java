package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.beans.CancelInfoBean;
import com.xbx.client.beans.OrderDetailBean;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.OrderParse;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/4/6.
 * 取消订单成功需要支付界面
 */
public class CancelOrderSucActivity extends BaseActivity {
    private RadioButton wchat_pay_rb;
    private RadioButton alipay_pay_rb;
    private TextView canOrderTime_tv;
    private TextView canOrderPay_tv;

    private OrderDetailBean detailBean = null;
    private Api api = null;
    private CancelInfoBean canInfo = null;

    private boolean isFromOrder = false;
    private String orderNum = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TaskFlag.PAGEREQUESFIVE:
                    Util.showToast(CancelOrderSucActivity.this, "取消金支付成功!");
                    setResult(RESULT_OK, new Intent());
                    finish();
                    break;
                /*case TaskFlag.REQUESTSUCCESS:
                    String dataDetail = (String) msg.obj;
                    detailBean = OrderParse.getDetailOrder(dataDetail);
                    if (detailBean == null)
                        return;
                    setCancelInfo();
                    break;*/
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_ordsuc);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        orderNum = getIntent().getStringExtra("GuideOrderNum");
        isFromOrder = getIntent().getBooleanExtra("isFromOrderDetail", false);
        api = new Api(CancelOrderSucActivity.this, handler);
        if (isFromOrder)
            detailBean = (OrderDetailBean) getIntent().getSerializableExtra("orderDetailBean");
        else {
            canInfo = (CancelInfoBean) getIntent().getSerializableExtra("cancelSucInfo");
            detailBean = new OrderDetailBean();
            detailBean.setOrderNum(canInfo.getOrderNum());
            detailBean.setOrderPay(Double.parseDouble(canInfo.getCancelPay()));
            detailBean.setOrderCancelTime(canInfo.getCancelTime());
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) findViewById(R.id.title_txt_tv)).setText(getString(R.string.cancel_order));
        findViewById(R.id.title_left_img).setOnClickListener(this);
        findViewById(R.id.submit_pay_btn).setOnClickListener(this);
        wchat_pay_rb = (RadioButton) findViewById(R.id.wchat_pay_rb);
        alipay_pay_rb = (RadioButton) findViewById(R.id.alipay_pay_rb);
        wchat_pay_rb.setOnClickListener(this);
        alipay_pay_rb.setOnClickListener(this);
        wchat_pay_rb.setChecked(true);
        canOrderTime_tv = (TextView) findViewById(R.id.canOrderTime_tv);
        canOrderPay_tv = (TextView) findViewById(R.id.canOrderPay_tv);
        if (detailBean == null)
            return;
        canOrderTime_tv.setText(detailBean.getOrderCancelTime());
        canOrderPay_tv.setText("￥" + detailBean.getOrderPay());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_img:
                finish();
                break;
            case R.id.wchat_pay_rb:
                Util.pLog("微信" + wchat_pay_rb.isChecked());
                if (wchat_pay_rb.isChecked()) {
                    wchat_pay_rb.setChecked(true);
                    alipay_pay_rb.setChecked(false);
                }
                break;
            case R.id.alipay_pay_rb:
                Util.pLog("支付宝" + alipay_pay_rb.isChecked());
                if (alipay_pay_rb.isChecked()) {
                    alipay_pay_rb.setChecked(true);
                    wchat_pay_rb.setChecked(false);
                }
                break;
            case R.id.submit_pay_btn:
                api.cancelOrder(detailBean.getOrderNum());
                break;
        }
    }
}

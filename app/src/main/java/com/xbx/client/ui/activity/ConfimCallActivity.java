package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.beans.UserInfo;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.GuideParse;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/5/10.
 * 确认呼叫界面
 */
public class ConfimCallActivity extends BaseActivity {
    private EditText call_realName_et;
    private EditText call_idCard_et;

    private Api api = null;
    private UserInfo userInfo = null;
    private String getOrderNum = "";
    private int guideType = 0;
    private String outsetJson = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TaskFlag.PAGEREQUESTHREE://开始呼叫导游,获取周边是否有导游
                    getOrderNum = GuideParse.getImmdiaOrder((String) msg.obj);
                    Util.pLog("oderNum:" + getOrderNum);
                    userInfo.setUserRealName(call_realName_et.getText().toString());
                    userInfo.setUserIdCard(call_idCard_et.getText().toString());
                    SharePrefer.saveUserInfo(ConfimCallActivity.this, userInfo);
                    Intent intent = new Intent();
                    intent.putExtra("callConfimOrderNum", getOrderNum);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_call_guide);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(ConfimCallActivity.this, handler);
        userInfo = SharePrefer.getUserInfo(ConfimCallActivity.this);
        guideType = getIntent().getIntExtra("guideTypes", 0);
        outsetJson = getIntent().getStringExtra("setOffAddress");
    }

    @Override
    protected void initViews() {
        super.initViews();
        findViewById(R.id.title_left_img).setOnClickListener(this);
        findViewById(R.id.confirm_call_btn).setOnClickListener(this);
        ((TextView) findViewById(R.id.title_txt_tv)).setText(getString(R.string.main_title));
        ((TextView) findViewById(R.id.call_phone_tv)).setText("+86 " + userInfo.getUserPhone());
        call_realName_et = (EditText) findViewById(R.id.call_realName_et);
        call_idCard_et = (EditText) findViewById(R.id.call_idCard_et);
        if (!Util.isNull(userInfo.getUserRealName())){
            call_realName_et.setText(userInfo.getUserRealName());
            call_realName_et.setSelection(userInfo.getUserRealName().length());
        }
        if (!Util.isNull(userInfo.getUserIdCard()))
            call_idCard_et.setText(userInfo.getUserIdCard());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_img:
                finish();
                break;
            case R.id.confirm_call_btn:
                String realName = call_realName_et.getText().toString();
                String idCard = call_idCard_et.getText().toString();
                if (Util.isNull(realName)) {
                    Util.showToast(ConfimCallActivity.this, getString(R.string.call_input_name));
                    return;
                }
                if (Util.isNull(idCard)) {
                    Util.showToast(ConfimCallActivity.this, getString(R.string.call_input_iD));
                    return;
                }
                if (!Util.isCardID(idCard)) {
                    Util.showToast(ConfimCallActivity.this, getString(R.string.call_input_rightID));
                    return;
                }
                api.hasGuide(userInfo.getUid(), outsetJson, guideType + "", realName, idCard, "");
                break;
        }
    }
}

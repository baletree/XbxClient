package com.xbx.client.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.http.Api;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/4/24.
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity {
    private EditText feedback_et;

    private Api api = null;
    private String uid = "";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TaskFlag.REQUESTSUCCESS:
                    Util.showToast(FeedBackActivity.this,getString(R.string.thank_feedback));
                    feedback_et.setText("");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(this,handler);
        uid = SharePrefer.getUserInfo(this).getUid();
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView)findViewById(R.id.title_txt_tv)).setText(getString(R.string.setting_feedback));
        findViewById(R.id.title_left_img).setOnClickListener(this);
        findViewById(R.id.submit_feedback_btn).setOnClickListener(this);
        feedback_et = (EditText) findViewById(R.id.feedback_et);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_left_img:
                finish();
                break;
            case R.id.submit_feedback_btn:
                String content = feedback_et.getText().toString();
                api.toFeedback(uid,content);
                break;
        }
    }
}

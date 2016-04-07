package com.xbx.client.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.beans.UserInfo;
import com.xbx.client.http.IRequest;
import com.xbx.client.http.RequestParams;
import com.xbx.client.jsonparse.UtilParse;
import com.xbx.client.jsonparse.UserInfoParse;
import com.xbx.client.utils.RequestBackLisener;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class LoginActivity extends BaseActivity {
    private TextView title_txt_tv;
    private TextView login_agreepact_tv;
    private EditText login_phone_et;
    private EditText login_code_et;
    private Button login_btn;
    private Button login_code_btn;

    private int countDown = 60;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if(countDown > 0){
                        countDown--;
                        login_code_btn.setText(countDown + getString(R.string.login_code_minute));
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }else {
                        login_code_btn.setText(getString(R.string.login_code_get));
                        login_code_btn.setClickable(true);
                        login_code_btn.setBackgroundResource(R.drawable.button_bg);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initViews() {
        super.initViews();
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);
        login_agreepact_tv = (TextView) findViewById(R.id.login_agreepact_tv);
        login_btn = (Button) findViewById(R.id.login_btn);
        login_code_btn = (Button) findViewById(R.id.login_code_btn);
        login_phone_et = (EditText) findViewById(R.id.login_phone_et);
        login_code_et = (EditText) findViewById(R.id.login_code_et);
        title_txt_tv.setText(R.string.login_title);
        login_agreepact_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        login_btn.setOnClickListener(this);
        login_code_btn.setOnClickListener(this);
        String userPhone = SharePrefer.getUserPhone(LoginActivity.this);
        if(!Util.isNull(userPhone)){
            login_phone_et.setText(userPhone);
            login_phone_et.setSelection(userPhone.length());
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        String phone = login_phone_et.getText().toString();
        String code = login_code_et.getText().toString();
        switch (v.getId()) {
            case R.id.login_btn:
                if (Util.isNull(phone)) {
                    Util.showToast(LoginActivity.this, getString(R.string.phone_tips));
                    return;
                }
                if (Util.isNull(code)) {
                    Util.showToast(LoginActivity.this, getString(R.string.code_tips));
                    return;
                }
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                toLogin(phone, code);
                break;
            case R.id.login_code_btn:
                if (Util.isNull(phone)) {
                    Util.showToast(LoginActivity.this, getString(R.string.phone_tips));
                    return;
                }
                if(!Util.checkTel(phone)){
                    Util.showToast(LoginActivity.this, getString(R.string.phone_check));
                    return;
                }
                getCode(phone);
                break;
        }
    }

    private void getCode(final String phone) {
        String postUrl = getString(R.string.url_conIp).concat(getString(R.string.url_getCode));
        RequestParams params = new RequestParams();
        params.put("mobile", phone);
        SharePrefer.savePhone(LoginActivity.this, phone);
        IRequest.post(this, postUrl, params, "", new RequestBackLisener(LoginActivity.this){
            @Override
            public void requestSuccess(String json) {
                Util.pLog("getCode Result=" + json);
                if(UtilParse.getRequest(json) == 1){
                    countDown = 60;
                    login_code_btn.setBackgroundResource(R.drawable.button_code_bg);
                    login_code_btn.setClickable(false);
                    handler.sendEmptyMessage(1);
                }
                Util.showToast(LoginActivity.this, UtilParse.getRequestMsg(json));
            }
        });
    }

    private void toLogin(String phone,String code){
        String postUrl = getString(R.string.url_conIp).concat(getString(R.string.url_Login));
        RequestParams params = new RequestParams();
        params.put("mobile", phone);
        params.put("password", code);
        params.put("user_type", "0");//代表用户端
        IRequest.post(this, postUrl, params, "", new RequestBackLisener(LoginActivity.this){
            @Override
            public void requestSuccess(String json) {
                Util.pLog("Login Result=" + json);
                if(UtilParse.getRequest(json) == 1){
                    UserInfo userInfo = UserInfoParse.getUserInfo(UtilParse.getDataResult(json));
                    if(userInfo != null){
                        SharePrefer.saveUserInfo(LoginActivity.this, userInfo);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("isFromLoin",true);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Util.showToast(LoginActivity.this, UtilParse.getRequestMsg(json));
                }
            }
        });
    }
}

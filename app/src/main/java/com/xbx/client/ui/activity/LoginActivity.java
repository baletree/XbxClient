package com.xbx.client.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xbx.client.R;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class LoginActivity extends BaseActivity {
    private TextView title_txt_tv;
    private TextView login_agreepact_tv;
    private Button login_btn;

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
        title_txt_tv.setText(R.string.login_title);
        login_agreepact_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.login_btn:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
        }
    }
}

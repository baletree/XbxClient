package com.xbx.client.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.xbx.client.R;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class LoginActivity extends BaseActivity {
    private TextView title_txt_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    @Override
    protected void initViews() {
        super.initViews();
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);
        title_txt_tv.setText(R.string.login_title);
    }
}

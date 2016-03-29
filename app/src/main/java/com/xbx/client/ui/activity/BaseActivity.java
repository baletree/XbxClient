package com.xbx.client.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initDatas();
        initViews();
    }

    protected void initViews(){

    }

    protected void initDatas(){

    }

    @Override
    public void onClick(View v) {

    }
}

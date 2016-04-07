package com.xbx.client.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/3/30.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View baseView = getContentView(inflater, container);
        onCreateView(baseView);
        init();
        return baseView;
    }
    /** 初始化UI */
    protected abstract void onCreateView(View contentView);
    /**
     * 在子类中的onCreateView方法中必须最先调用该方法
     */
    protected abstract int getViewLayoutId();

    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(getViewLayoutId(), container, false);
    }

    private void init(){
        initViews();
        initDatas();
        initLisener();
    }

    protected void initDatas(){
    }

    protected void initViews(){
    }

    protected void initLisener(){
    }

    @Override
    public void onClick(View v) {

    }
}

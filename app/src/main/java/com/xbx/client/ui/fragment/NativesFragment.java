package com.xbx.client.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class NativesFragment extends BaseFragment {
    private static NativesFragment fragment = null;
    private View view = null;
    private EditText login_phone_et;

    public NativesFragment() {
    }

    public static NativesFragment newInstance() {
        if (fragment == null) {
            fragment = new NativesFragment();
        }
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    protected void onCreateView(View contentView) {
        this.view = contentView;
        Util.pLog("NativesFragment onCreateView()");
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        super.initViews();
        login_phone_et = (EditText) view.findViewById(R.id.login_phone_et);
        login_phone_et.setText("18602854129");
    }
}

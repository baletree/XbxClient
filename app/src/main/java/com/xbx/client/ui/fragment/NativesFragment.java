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
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class NativesFragment extends Fragment {
    private static NativesFragment fragment = null;
    private View view = null;

    public NativesFragment() {
    }

    public static NativesFragment newInstance() {
        if (fragment == null) {
            fragment = new NativesFragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_guide, container, false);

        return view;
    }
}

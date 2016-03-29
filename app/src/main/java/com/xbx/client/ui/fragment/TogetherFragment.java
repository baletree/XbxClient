package com.xbx.client.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class TogetherFragment extends Fragment {
    private static TogetherFragment fragment = null;

    public TogetherFragment() {
    }

    public static TogetherFragment newInstance() {
        if (fragment == null) {
            fragment = new TogetherFragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

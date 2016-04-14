package com.xbx.client.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx.client.utils.ImageLoaderConfigFactory;
import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/4/13.
 */
public class BasedFragment extends Fragment implements View.OnClickListener{
    public ImageLoader imageLoader;
    public ImageLoaderConfigFactory configFactory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Util.pLog("Base onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }
}

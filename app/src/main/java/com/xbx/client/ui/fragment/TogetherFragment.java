package com.xbx.client.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.makeramen.roundedimageview.RoundedImageView;
import com.xbx.client.R;
import com.xbx.client.beans.MyGuideInfoBean;
import com.xbx.client.beans.TogetherBean;
import com.xbx.client.http.Api;
import com.xbx.client.polling.FindGuide;
import com.xbx.client.polling.MyGuideInfo;
import com.xbx.client.polling.PollUploadLag;
import com.xbx.client.ui.activity.TogetherActivity;
import com.xbx.client.utils.Constant;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.Util;
import com.xbx.client.view.LoadingDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/3/29.
 * 随游
 */
public class TogetherFragment extends BasedFragment {
    private static TogetherFragment fragment = null;
    public TogetherFragment() {
    }

    public static TogetherFragment newInstance() {
        if (fragment == null) {
            fragment = new TogetherFragment();
        }
        return fragment;
    }
}

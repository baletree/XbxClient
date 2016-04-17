package com.xbx.client.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.xbx.client.R;
import com.xbx.client.beans.LocationBean;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.Util;

/**
 * Created by EricPeng on 2016/4/11.
 */
public class ChoiceGuideActivity extends BaseActivity {
    /*private TextView title_txt_tv;
    private ImageView title_left_img;*/
    private MapView test_map;
    private BaiduMap mBaiduMap;
    private LocationBean lBean = null;
    private LatLng currentLalng = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_guide);
    }
    @Override
    protected void initDatas() {
        super.initDatas();
    }

    @Override
    protected void initViews() {
        super.initViews();
        /*title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);
        title_left_img = (ImageView) findViewById(R.id.title_left_img);
        title_txt_tv.setText(getString(R.string.choice_guide));
        title_left_img.setOnClickListener(this);*/
        test_map = (MapView) findViewById(R.id.guide_map);
        mBaiduMap = test_map.getMap();
        lBean = SharePrefer.getLocate(this);
        if (lBean != null) {
            if (!Util.isNull(lBean.getLat()) && !Util.isNull(lBean.getLon())) {
                currentLalng = new LatLng(Double.parseDouble(lBean.getLat()),
                        Double.parseDouble(lBean.getLon()));
                LatLng ll = new LatLng(Double.parseDouble(lBean.getLat()),
                        Double.parseDouble(lBean.getLon()));
                MapStatus mMapstatus = new MapStatus.Builder().target(ll).zoom(20f)
                        .build();
                MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
                mBaiduMap.setMapStatus(u);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}

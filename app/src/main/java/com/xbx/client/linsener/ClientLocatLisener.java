package com.xbx.client.linsener;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.MyLocationData;
import com.xbx.client.utils.SharePrefer;

/**
 * Created by EricYuan on 2016/4/24.
 */
public class ClientLocatLisener implements BDLocationListener {
    private Context context;

    public ClientLocatLisener(Context context) {
        this.context = context;
    }



    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

    }
}

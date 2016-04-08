package com.xbx.client.app;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.xbx.client.beans.LocationBean;
import com.xbx.client.utils.MapLocate;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.Util;

public class App extends Application {
	private static Context mContext;
	private MapLocate mapLocate = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String phoneId = tm.getDeviceId();
		SharePrefer.savePhoneId(mContext,phoneId);
		SDKInitializer.initialize(this);
		toLocate();
	}

	private void toLocate(){
		mapLocate = new MapLocate(mContext);
		mapLocate.startLocate();
	}

	public static Context getContext() {
		return mContext;
	}
}

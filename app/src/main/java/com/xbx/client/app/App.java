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
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xbx.client.beans.LocationBean;
import com.xbx.client.utils.Constant;
import com.xbx.client.utils.MapLocate;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.Util;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

public class App extends Application {
	private static Context mContext;
	private MapLocate mapLocate = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		SDKInitializer.initialize(mContext);
		initImageLoader();
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String phoneId = tm.getDeviceId();
		SharePrefer.savePhoneId(mContext,phoneId);
		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(mContext);     		// 初始化 JPush
		toLocate();
	}

	private void initImageLoader(){
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.threadPoolSize(4)
				.diskCache(new UnlimitedDiscCache(new File(Constant.PICTURE_ALBUM_PATH)))
				.build();
		ImageLoader.getInstance().init(config);
	}

	private void toLocate(){
		mapLocate = new MapLocate(mContext);
		mapLocate.startLocate();
	}

	public static Context getContext() {
		return mContext;
	}
}

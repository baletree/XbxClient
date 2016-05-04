package com.xbx.client.app;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
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
        getPhoneId();
        initImageLoader();
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(mContext);            // 初始化 JPush
        SDKInitializer.initialize(mContext);
        toLocate();
    }

    private void getPhoneId() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        SharePrefer.savePhoneId(mContext, tm.getDeviceId());
    }

    private void initImageLoader() {
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

    private void toLocate() {
        mapLocate = new MapLocate(mContext);
        mapLocate.startLocate();
    }

    public static Context getContext() {
        return mContext;
    }
}

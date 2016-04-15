package com.xbx.client.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.xbx.client.Manifest;
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
        getPhoneId();
        SDKInitializer.initialize(mContext);
        initImageLoader();
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(mContext);            // 初始化 JPush
        toLocate();
    }

    private void getPhoneId() {
        if (Build.VERSION.SDK_INT >= 23) {
            String permission = "android.permission.READ_PHONE_STATE";
            int deviceIdPermission = ContextCompat.checkSelfPermission(mContext,permission);
            if (deviceIdPermission != PackageManager.PERMISSION_GRANTED) {
                //弹出对话框接收权限
//                ActivityCompat.requestPermissions(mContext, new String[]{permission}, id);
                return;
            }
        } else {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String phoneId = tm.getDeviceId();
            SharePrefer.savePhoneId(mContext, phoneId);
        }
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

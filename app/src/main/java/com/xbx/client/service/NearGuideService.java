package com.xbx.client.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.xbx.client.utils.Util;

import org.apache.http.conn.scheme.HostNameResolver;

/**
 * Created by EricYuan on 2016/4/14.
 */
public class NearGuideService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new NearGuideBinder();
    }

    private class NearGuideBinder extends Binder {
        public void nearGuide() {
            // 内部类调用外部类方法
            getNearGuide();
        }
    }

    private void getNearGuide(){
        Util.pLog("Test Service!");
    }
}

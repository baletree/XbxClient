package com.xbx.client.polling;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.xbx.client.http.Api;
import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/4/19.
 * 获取为我服务的导游的信息
 */
public class MyGuideInfo {
    private Context context;
    private Handler pHandler;

    private Api api = null;
    private String findGuideorderNum;

    public MyGuideInfo(Context context,Handler pHandler){
        this.context = context;
        this.pHandler = pHandler;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    api.getMyGuideInfo(findGuideorderNum);
                    handler.sendEmptyMessageDelayed(1, 3000);
                    break;
            }
        }
    };

    public void getMyGuideInfo(String findGuideorderNum){
        this.findGuideorderNum = findGuideorderNum;
        api = new Api(context,pHandler);
        handler.sendEmptyMessage(1);
    }

    public void removeGetInfo(){
        handler.removeMessages(1);
    }
}

package com.xbx.client.polling;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.xbx.client.http.Api;

/**
 * Created by EricYuan on 2016/4/19.
 * 是否找到导游
 */
public class FindGuide {
    private FragmentActivity context;
    private Api api = null;
    private Handler pHandler;

    private String uid = "";
    private String findGuideorderNum = "";
    private int count = 0;

    public FindGuide(FragmentActivity context,Handler pHandler){
        this.context = context;
        this.pHandler = pHandler;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    api.isFindGuide(uid, findGuideorderNum);
                    handler.sendEmptyMessageDelayed(1, 5000);
                    break;
                case 2:
                    handler.sendEmptyMessageDelayed(2,1000);
                    if(count < 51)
                        count ++;
                    else {
                        count = 0;
                        removeFindGuide();
                        pHandler.sendEmptyMessage(30);
                    }
                    break;
            }
        }
    };

    public void toFindGuide(String uid,String findGuideorderNum){
        api = new Api(context,pHandler);
        this.uid = uid;
        this.findGuideorderNum = findGuideorderNum;
        handler.sendEmptyMessage(1);
        handler.sendEmptyMessage(2);
    }

    public void removeFindGuide(){
        handler.removeMessages(1);
        handler.removeMessages(2);
    }
}

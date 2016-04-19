package com.xbx.client.polling;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.baidu.mapapi.model.LatLng;
import com.xbx.client.R;
import com.xbx.client.beans.UserInfo;
import com.xbx.client.http.Api;
import com.xbx.client.utils.SharePrefer;

/**
 * Created by EricYuan on 2016/4/19.
 * 上传经纬度
 */
public class PollUploadLag {
    private Context context;

    private Api api = null;
    private LatLng latLng = null;
    private UserInfo uInfo = null;
    private String url = "";

    public PollUploadLag(Context context){
        this.context = context;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    api.uploadLatlng(url, uInfo.getUid(), latLng.longitude + "", latLng.latitude + "");
                    handler.sendEmptyMessageDelayed(1, 5000);
                    break;
            }
        }
    };

    public void uploadLatlng(){
        api = new Api(context, handler);
        latLng = SharePrefer.getLatlng(context);
        uInfo = SharePrefer.getUserInfo(context);
        url = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_uploadLatlng));
        handler.sendEmptyMessage(1);
    }

    public void unUploadLatlng(){
        handler.removeMessages(1);
    }
}

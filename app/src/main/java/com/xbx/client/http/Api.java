package com.xbx.client.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.xbx.client.R;
import com.xbx.client.jsonparse.UtilParse;
import com.xbx.client.utils.RequestBackLisener;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/4/5.
 */
public class Api {
    private Handler mHandler;
    private Context context;

    boolean isFirst = true;

    public Api(Context context, Handler mHandler) {
        this.context = context;
        this.mHandler = mHandler;
    }
    /**
     * 获取附近的导游
     */
    public void getNearGuide(LatLng currentLalng, String nearGuideUrl,String uid) {
        if (currentLalng == null)
            return;
        if(context == null)
            return;

        RequestParams params = new RequestParams();
        params.put("lon", currentLalng.longitude + "");
        params.put("lat", currentLalng.latitude + "");
        params.put("uid", uid);
//        Util.pLog("lon:"+currentLalng.longitude+" lat:"+currentLalng.latitude);
        IRequest.post(context, nearGuideUrl, params, new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                super.requestSuccess(json);
                if(isFirst){
                    Util.pLog("MainguideList:" + json);
                    isFirst = false;
                }
                sendMsg(TaskFlag.REQUESTSUCCESS, json);
            }
            @Override
            public void requestError(VolleyError e) {
                //该接口不用回调错误信息
            }
        });
    }
    /**地图上显示为我服务的导游信息*/
    public void getMyGuideInfo(String uid){
        if(context == null)
            return;
        String guideInfoUrl = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_guideInfo));
        IRequest.get(context, guideInfoUrl,new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                super.requestSuccess(json);
                sendShowMsg(TaskFlag.PAGEREQUESTWO,json);
            }
        });
    }

    /**
     * 无判断code=0的情况
     */
    private void sendMsg(int flag, String json) {
        Message msg = mHandler.obtainMessage();
        if (UtilParse.getRequestCode(json) == 1) {
            msg.obj = UtilParse.getRequestData(json);
            msg.what = flag;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 有判断code=0的情况
     */
    private void sendShowMsg(int flag, String json) {
        Message msg = mHandler.obtainMessage();
        if (UtilParse.getRequestCode(json) == 1) {
            msg.obj = UtilParse.getRequestData(json);
            msg.what = flag;
            mHandler.sendMessage(msg);
        } else {
            String showMsg = UtilParse.getRequestMsg(json);
            if (!Util.isNull(showMsg))
                Util.showToast(context, showMsg);
        }
    }
}

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
    public void getNearGuide(LatLng currentLalng, String nearGuideUrl, String uid) {
        if (currentLalng == null)
            return;
        if (context == null)
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
                if (isFirst) {
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

    /**
     * 地图上显示为我服务的导游信息
     */
    public void getMyGuideInfo(String orderNum) {
        if (context == null)
            return;
        String guideInfoUrl = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_getMyGuideInfo)).concat("?order_number="+orderNum);
        IRequest.get(context, guideInfoUrl, new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                super.requestSuccess(json);
                sendShowMsg(TaskFlag.PAGEREQUESTWO, json);
            }
        });
    }

    /**
     * 出行人数选择
     */
    public void getSetoffNum() {
        if (context == null)
            return;
        String setoffNumUrl = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_setoffNum));
        IRequest.get(context, setoffNumUrl,new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                super.requestSuccess(json);
                Util.pLog("出行人数选择:" + json);
                sendShowMsg(TaskFlag.REQUESTSUCCESS, json);
            }
        });
    }

    /**
     *
     * @param uid
     * @param startInfo 用户开始地址Json
     * @param startInfo
     * @param startTime
     * @param endTime
     * @param serverTye
     * @param guideType
     * @param userNum
     */
    public void findGuide(String uid, String startInfo, String endInfo, String startTime, String endTime, String serverTye,
                          String guideType, String userNum) {
        if (context == null)
            return;
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("start_addr_info", startInfo);
        params.put("end_addr_info", endInfo);
        params.put("server_start_time", startTime);
        params.put("server_end_time", endTime);
        params.put("server_type", serverTye);
        params.put("service", guideType);
        params.put("number", userNum);
        Util.pLog("uid:"+uid+" startInfo:"+startInfo+" endInfo:"+endInfo+" startTime:"+startTime+" endTime:"+endTime+" serverType:"+serverTye+" guideType:"+guideType+" userNum:"+userNum);
        String findUrl = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_findGuide));
        IRequest.post(context, findUrl, params,"", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("呼叫导游:" + json);
                sendShowMsg(TaskFlag.PAGEREQUESTHREE,json);
            }
        });
    }

    public void isFindGuide(String uid,String orderNum){
        if (context == null)
            return;
        String isFindUrl = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_isFindGuide)).concat("?uid="+uid).concat("&order_number="+orderNum);
//        Util.pLog("isFindUrl:"+isFindUrl);
        IRequest.get(context, isFindUrl, new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("是否找到导游:" + json);
                sendMsg(TaskFlag.PAGEREQUESFOUR,json);
            }

            @Override
            public void requestError(VolleyError e) {
            }
        });
    }

    public void cancelFindGuide(String orderNum){
        if (context == null)
            return;
        String isFindUrl = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_cancelFindGuide)).concat("?order_number="+orderNum);
        IRequest.get(context, isFindUrl,context.getString(R.string.stop_find), new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("取消寻找导游:" + json);
                sendShowMsg(TaskFlag.PAGEREQUESFIVE,json);
            }
        });
    }

    public void cancelOrder(String orderNum){
        if (context == null)
            return;
        String isFindUrl = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_cancelImmeOrder)).concat("?order_number="+orderNum);
        IRequest.get(context, isFindUrl,context.getString(R.string.stop_order), new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("取消订单:" + json);
                sendMsg(TaskFlag.PAGEREQUESFIVE,json);
            }
        });
    }

    public void getMyOrderList(String uid){
        if (context == null)
            return;
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        String orderListUrl = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_orderList));
        IRequest.post(context, orderListUrl, params, new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                super.requestSuccess(json);
                Util.pLog("订单列表:" + json);
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

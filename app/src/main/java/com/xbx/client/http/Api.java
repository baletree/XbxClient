package com.xbx.client.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.xbx.client.R;
import com.xbx.client.beans.ReservatInfoBean;
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
        String guideInfoUrl = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_getMyGuideInfo)).concat("?order_number=" + orderNum);
        IRequest.get(context, guideInfoUrl, new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                super.requestSuccess(json);
                Util.pLog("服务我的导游:" + json);
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
        IRequest.get(context, setoffNumUrl, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                super.requestSuccess(json);
                Util.pLog("出行人数选择:" + json);
                sendShowMsg(TaskFlag.PAGEREQUESFIVE, json);
            }
        });
    }

    /**
     * @param uid
     * @param startInfo 用户开始地址Json
     * @param startInfo
     * @param startTime
     * @param endTime
     * @param serverTye 0即时或者1预约
     * @param guideType 导游或者伴游和土著
     * @param userNum
     * @param cityId
     * @param guideId 导游Id
     */
    public void findGuide(String uid, String startInfo, String startTime, String endTime, String serverTye,
                          String guideType, String userNum, String cityId,String guideId) {
        if (context == null)
            return;
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("server_addr_lnglat", startInfo);
        params.put("server_start_time", startTime);
        params.put("server_end_time", endTime);
        params.put("server_city_id", cityId);
        params.put("server_type", serverTye);
        params.put("guide_type", guideType);
        params.put("number", userNum);
        params.put("guid", guideId);
        Util.pLog("用户:" + uid + " 用户预约目的:" + startInfo + " 开始时间:" + startTime + " 结束时间:" + endTime + " 服务类型:" + serverTye + " 导游类型:" + guideType + " 人数:" + userNum + " 城市ID：" + cityId+" 导游id:"+guideId);
        String findUrl = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_findGuide));
        IRequest.post(context, findUrl, params, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("是否有导游:" + json);
                sendShowMsg(TaskFlag.PAGEREQUESTHREE, json);
            }
        });
    }

    public void isFindGuide(String uid, String orderNum) {
        if (context == null)
            return;
        String isFindUrl = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_isFindGuide)).concat("?uid=" + uid).concat("&order_number=" + orderNum);
        IRequest.get(context, isFindUrl, new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("是否匹配到了导游:" + json);
                sendMsg(TaskFlag.PAGEREQUESFOUR, json);
            }

            @Override
            public void requestError(VolleyError e) {
            }
        });
    }

    /**
     * 取消订单
     *
     * @param orderNum
     */
    public void cancelOrder(String orderNum) {
        if (context == null)
            return;
        String isFindUrl = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_cancelOrder)).concat("?order_number=" + orderNum);
        IRequest.get(context, isFindUrl, context.getString(R.string.stop_order), new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("取消订单:" + json);
                sendAllDatas(TaskFlag.PAGEREQUESFIVE, json);
            }
        });
    }

    /**
     * 用户订单列表
     *
     * @param uid
     */
    public void getMyOrderList(String uid) {
        if (context == null)
            return;
        String orderListUrl = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_orderList)).concat("?uid=" + uid);
        IRequest.get(context, orderListUrl, new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                super.requestSuccess(json);
                Util.pLog("订单列表:" + json);
                sendShowMsg(TaskFlag.REQUESTSUCCESS, json);
            }
        });
    }

    public void getOrderDetail(String orderNum) {
        if (context == null)
            return;
        String orderDetailUrl = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_orderDetail)).concat("?order_number=" + orderNum);
        Util.pLog("orderDetailUrl:"+orderDetailUrl);
        IRequest.get(context, orderDetailUrl,"",new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                super.requestSuccess(json);
                Util.pLog("订单详情:" + json);
                sendShowMsg(TaskFlag.REQUESTSUCCESS, json);
            }
        });
    }

    public void toUploadLatlng(String url, String uid, String lon, String lat) {
        if (context == null)
            return;
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("lon", lon);
        params.put("lat", lat);
        IRequest.post(context, url, params, new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
//                Util.pLog("上传经纬度:" + json);
            }

            @Override
            public void requestError(VolleyError e) {
            }
        });
    }

    /**
     * 获取预约导游列表
     *
     * @param reservatBean
     * @param pageIndex
     * @param pageNum
     */
    public void getReserveGuideList(ReservatInfoBean reservatBean, String pageIndex, String pageNum, final int requestFlag) {
        if (context == null)
            return;
        String paramsData = "?server_city_id=" + reservatBean.getCityId() + "&server_addr_lnglat=" + reservatBean.getAddress() + "&sex=" + reservatBean.getSexType()
                + "&lang_type=" + reservatBean.getLanguageType() + "&server_start_time=" + reservatBean.getStartTime() + "&server_end_time=" + reservatBean.getEndTime()
                + "&now_page=" + pageIndex + "&page_number=" + pageNum;
        String url = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_guideList)).concat(paramsData);
        Util.pLog(paramsData);
        IRequest.get(context, url, "", new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("预约导游列表:" + json);
                sendShowMsg(requestFlag,json);
            }

            @Override
            public void requestError(VolleyError e) {
                super.requestError(e);
                mHandler.sendEmptyMessage(TaskFlag.REQUESTERROR);
            }
        });
    }

    /**
     *
     * @param uid
     * @param realname
     * @param sex
     * @param idcard
     * @param nickname
     * @param birthday
     */
    public void modifyInfo(String uid,String realname,String sex,String idcard,String nickname,String birthday){
        if (context == null)
            return;
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("realname", realname);
        params.put("sex", sex);
        params.put("idcard", idcard);
        params.put("nickname", nickname);
        params.put("birthday", birthday);
        String url = context.getString(R.string.url_conIp).concat(context.getString(R.string.url_modifyUserinfo));
        IRequest.post(context, url, params, new RequestBackLisener(context) {
            @Override
            public void requestSuccess(String json) {
                Util.pLog("修改个人信息:" + json);
                sendShowMsg(TaskFlag.REQUESTSUCCESS,json);
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
            mHandler.sendEmptyMessage(TaskFlag.REQUESTERROR);
            String showMsg = UtilParse.getRequestMsg(json);
            if (!Util.isNull(showMsg))
                Util.showToast(context, showMsg);
        }
    }

    private void sendAllDatas(int flag, String json){
        Message msg = mHandler.obtainMessage();
        msg.obj = json;
        msg.what = flag;
        mHandler.sendMessage(msg);
    }
}

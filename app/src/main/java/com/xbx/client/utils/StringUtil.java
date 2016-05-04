package com.xbx.client.utils;

import android.content.Context;

import com.xbx.client.R;

/**
 * Created by EricYuan on 2016/4/22.
 */
public class StringUtil {
    public static String getGuideType(Context context, int guideType) {
        String guideTypeStr = "";
        switch (guideType) {
            case 1:
                guideTypeStr = context.getString(R.string.main_guide);
                break;
            case 2:
                guideTypeStr = context.getString(R.string.main_native);
                break;
            default:
                guideTypeStr = context.getString(R.string.main_withTour);
                break;
        }
        return guideTypeStr;
    }

    public static String severType(Context context, int guideType){
        String serverTypeStr = "";
        switch (guideType) {
            case 0:
                serverTypeStr = context.getString(R.string.immediaServer);
                break;
            case 1:
                serverTypeStr = context.getString(R.string.reserveServer);
                break;
        }
        return serverTypeStr;
    }

    public static String getCountTravel(long diff) {
        long dayUnit = 24 * 60 * 60 * 1000;
        long hourUnit = 60 * 60 * 1000;
        long minUnit = 60 * 1000;
        long secondUnit = 1000;

        long day = diff / dayUnit;
        long hour = diff % dayUnit / hourUnit;
        long min = diff % dayUnit % hourUnit / minUnit;
        long second = diff % dayUnit % hourUnit % minUnit / secondUnit;
        if (day == 0)
            return hour + "小时" + min + "分" + second + "秒";
        else
            return day + "天" + hour + "小时" + min + "分" + second + "秒";
    }



    public static String getOrderState(Context context, int serverType, int orderState) {
        String orderStateStr = "";
        switch (serverType) {
            case 0://即时服务
                orderStateStr = getImmediaStates(context,orderState);
                break;
            case 1://预约服务
                orderStateStr = getReservatStates(context,orderState);
                break;
        }
        return orderStateStr;
    }

    private static String getImmediaStates(Context context, int orderState) {
        String stateStr = "";
        switch (orderState){
            case 0:
            case 1:
            case 2:
                stateStr = "进行中";
                break;
            case 3:
                stateStr = "待支付";
                break;
            case 4:
                stateStr = "待评论";
                break;
            case 5:
                stateStr = "已完成";
                break;
            case 6:
                stateStr = "违约待支付";
                break;
            case 7:
                stateStr = "已关闭";
                break;
        }
        return stateStr;
    }
    private static String getReservatStates(Context context, int orderState) {
        String stateStr = "";
        switch (orderState){
            case 0:
                stateStr = "待支付";
                break;
            case 1:
                stateStr = "待确认";
                break;
            case 2:
                stateStr = "已预约";
                break;
            case 3:
                stateStr = "进行中";
                break;
            case 4:
                stateStr = "待评论";
                break;
            case 5:
                stateStr = "已完成";
                break;
            case 6:
                stateStr = "退款中";
                break;
            case 7:
                stateStr = "已关闭";
                break;
            case 8:
                stateStr = "拒单";
                break;
        }
        return stateStr;
    }
}

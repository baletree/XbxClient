package com.xbx.client.utils;

import android.content.Context;

import com.xbx.client.R;

/**
 * Created by EricYuan on 2016/4/22.
 */
public class StringUtil {
    public static String getGuideType(Context context, int guideType) {
        switch (guideType) {
            case 1:
                return context.getString(R.string.main_guide);
            case 2:
                return context.getString(R.string.main_native);
            default:
                return context.getString(R.string.main_withTour);
        }
    }

    public static long[] getCountTravel(long diff) {
        long[] timeRecode = new long[4];
        String dayStr = "";
        long dayUnit = 24 * 60 * 60 * 1000;
        long hourUnit = 60 * 60 * 1000;
        long minUnit = 60 * 1000;
        long secondUnit = 1000;

        long day = diff / dayUnit;
        long hour = diff % dayUnit / hourUnit;
        long min = diff % dayUnit % hourUnit / minUnit;
        long second = diff % dayUnit % hourUnit % minUnit / secondUnit;

        timeRecode[0] = day;
        timeRecode[1] = hour;
        timeRecode[2] = min;
        timeRecode[3] = second;
//        dayStr = day + "天" + hour + "小时" + min + "分" + second + "秒";
        return timeRecode;
    }

    /*public static String getCountStrTravel(long timeArra[], long countTime) {
        long dayUnit = 24 * 60 * 6;
        long hourUnit = 60 * 60;
        long minUnit = 60;
        long secondUnit = 1;
        if (countTime % dayUnit > 0) {
            timeArra[0] = countTime % dayUnit + timeArra[0];
        }else if(countTime%hourUnit > 0){
            timeArra[1] = countTime % hourUnit + timeArra[0];
        }else if(countTime%minUnit > 0){
            timeArra[2] = countTime % hourUnit + timeArra[0];
        }else if(countTime%minUnit > 0){

        }

    }*/
}

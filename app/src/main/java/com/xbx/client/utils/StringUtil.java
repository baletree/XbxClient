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
}

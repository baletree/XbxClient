package com.xbx.client.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by louise on 2015/12/16.
 */
public class Util {

    public static boolean checkTel(String phone) {
//        Pattern p = Pattern.compile("^[0][9]\\d{8}$");
        Pattern p = Pattern.compile("^[1][34578]\\d{9}$");
        if (p != null) {
            Matcher matcher = p.matcher(phone);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static void showToast(Context context, String message) {
        if (!Util.isNull(message))
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void pLog(String message) {
        Log.i("Tag", message);
    }

    /**
     * 重新估算ListView的高度
     *
     * @param listview
     */
    public static void setListViewHeight(ListView listview) {
        int totalHeight = 0;
        ListAdapter adapter = listview.getAdapter();
        if (null != adapter) {
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, listview);
                if (null != listItem) {
                    listItem.measure(0, 0);//  注意listview子项必须为LinearLayout才能调用该方法
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
            ViewGroup.LayoutParams params = listview.getLayoutParams();
            params.height = totalHeight
                    + (listview.getDividerHeight() * (listview.getCount() - 1));
            listview.setLayoutParams(params);
        }
    }

    /**
     * 判断是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        if ("null".equals(str)) {
            return true;
        }
        if (str == null) {
            return true;
        }
        return false;
    }

    public static void checkNetError(Context context, VolleyError e) {
        Util.pLog("VolleyError:" + e.getMessage());
        if (e instanceof NetworkError) {
            showToast(context, "请检查网络");
        } else if (e instanceof TimeoutError) {
            showToast(context, "连接超时");
        } else if (e instanceof ServerError) {
            showToast(context, "服务器异常");
        } else {
            showToast(context, "未知异常");
        }
    }
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}

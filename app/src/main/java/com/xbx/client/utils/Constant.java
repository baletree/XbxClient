package com.xbx.client.utils;

import android.content.Intent;
import android.os.Environment;

import com.xbx.client.beans.TogetherBean;
import com.xbx.client.ui.activity.TogetherActivity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/3/30.
 * 常量定义
 */
public class Constant {
    public static final String SPLOC_NAME = "location_spname";
    public static final String SPUSER_PHONE = "user_phone";
    public static final String SPUSER_INFO = "user_info";
    public static final String SPPHONE_ID = "phone_id";
    public static final String SPUSER_LATLNG = "user_latlng";
    public static final int outsetFlag = 100;
    public static final int destFlag = 101;
    public static final String ACTION_GCANCELORD = "com.xbx.client.guide.cancel.order";
    public static final String ACTION_GCANCELUIDEORDSUC = "com.xbx.client.guide.cancel.order.success";
    public static final String ACTION_GUIDEOVERSERVER = "com.xbx.client.guide.over.order";//结束行程
    public static final String ACTION_USERINORDER = "com.xbx.client.user.into.order";//用户进入行程取消订单功能不存在
    public static final String ACTION_CALLGUIDEBTN = "com.xbx.client.call.guide";//用户进入开始点击呼叫按钮之前
    public static final String ACTION_DISSMISSBACK = "com.xbx.client.dissmiss.back";//用户进入开始点击呼叫按钮之前
    public static final String ACTION_GUIDEINORDER = "com.xbx.client.guide.into.order";
    public static final String ACTION_NATIVEINORDER = "com.xbx.client.native.into.order";
    public static final String ACTION_TOGETHERINORDER = "com.xbx.client.together.into.order";
    public static final int guideType = 1;
    public static final int nativeType = 3;
    public static final int togetherType = 2;
    /**
     * 存储根目录
     */
    public static final String APP_ROOT_PATH = Environment.getExternalStorageDirectory().toString();
    /**
     * 图片缓存路径
     */
    public static final String PICTURE_ALBUM_PATH = APP_ROOT_PATH + "/XbxTravel/";
    public static final String PATH_PIC = PICTURE_ALBUM_PATH + File.separator + "Photo";
    /**
     * 系统图片存储路径
     */
    public static final String PHOTO_SYS_PATH = APP_ROOT_PATH + "/DCIM/Camera/";
    /**
     * 从Intent获取图片路径的KEY
     */
    public static final String KEY_PHOTO_PATH = "com.xbb.la.client.photo_path";

    public static String ROOT_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    /**
     * 更新apk的存放位置
     */
    public static String APK_PATH = ROOT_PATH + "/tutu/apk/xbx.apk";
}

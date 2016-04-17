package com.xbx.client.utils;

import android.os.Environment;

import java.io.File;

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

}

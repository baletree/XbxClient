package com.xbx.client.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

/**
 * Created by EricYuan on 2016/4/19.
 */
public class PhotoUtil {
    private final static String TAG = "PhotoUtil";

    /**
     * 拍照
     */
    public static final int TAKEPHOTO = 11121;
    /**
     * 相册选择
     */
    public static final int PICKPHOTO = 11122;
    /**
     * 裁剪图片
     */
    public static final int CROPPHOTO = 11123;

    public static final String PATH_PHOTOTEMP = Constant.PATH_PIC + File.separator + "tempPhoto.jpg";


    /**
     * 拍照获取图片
     */
    public static void takePhoto(Activity activity, Uri saveUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
        Log.i("Tag", "take=>" + saveUri);
        activity.startActivityForResult(intent, TAKEPHOTO);
    }

    /**
     * 请求Gallery相册程序
     *
     * @param activity
     */
    public static void pickPhoto(Activity activity, Uri saveUri) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
            activity.startActivityForResult(intent, PICKPHOTO);
        } catch (Exception e) {
            Toast.makeText(activity, "未获取到图片", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * 所有图片裁剪回调 如不需要裁剪及不用调用该方法
     *
     * @param context
     * @param uri     图片资源地址
     * @param saveUri 剪切图片保存地址
     */
    public static void cropPhoto(Activity context, Uri uri, Uri saveUri) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", true);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("return-data", false);
            Log.i("Tag", "saveUri=>" + (saveUri == null));
            Log.i("Tag", "cropPhoto=>" + saveUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
            context.startActivityForResult(intent, CROPPHOTO);
        } catch (Exception e) {
            Util.pLog(e.getMessage());
        }
    }

    /**
     * 获取不裁剪时图片返回的路径
     *
     * @param activity
     * @param data
     * @return path
     */
    public static String getPickPhotoPath(Activity activity, Intent data) {
        if (data == null)
            return null;
        String path = null;
        Uri imageuri = data.getData();
        if (imageuri != null) {
            path = FileUtil.getFilePathByUri(activity, imageuri);
        }
        return path;
    }
}

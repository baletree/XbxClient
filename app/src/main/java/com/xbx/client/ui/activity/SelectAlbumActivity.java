package com.xbx.client.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.xbx.client.R;
import com.xbx.client.utils.Constant;
import com.xbx.client.utils.FileUtil;
import com.xbx.client.utils.PhotoUtil;
import com.xbx.client.utils.Util;

import java.io.File;
import java.util.Date;

/**
 * Created by 鹏 on 2016/4/17.
 */
public class SelectAlbumActivity extends Activity implements View.OnClickListener {

    /***
     * 使用照相机拍照获取图片
     */
    private static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /***
     * 使用相册中的图片
     */
    private static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    /***
     * 裁剪图片
     */
    private static final int CROP_PHOTO = 3;

    /**
     * 获取到的图片路径
     */
    private String picPath;

    private Intent lastIntent;

    private Uri photoUri;

    private int type;

    private Bitmap bitmap;
    private String filePath;

    public static final String PATH_PHOTOTEMP = Constant.PATH_PIC + File.separator + "tempPhoto.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_album);
        initData();
        initViews();
    }

    public void initData() {
        lastIntent = new Intent();
        type = getIntent().getIntExtra("type", 2);
    }

    private void initViews() {
        findViewById(R.id.select_album_take_photo).setOnClickListener(this);
        findViewById(R.id.select_album_photo_gallery).setOnClickListener(this);
        findViewById(R.id.select_album_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_album_take_photo:
                PhotoUtil.takePhoto(SelectAlbumActivity.this, Uri.fromFile(
                        FileUtil.getFile(PhotoUtil.PATH_PHOTOTEMP)));
                break;
            case R.id.select_album_photo_gallery:
                PhotoUtil.pickPhoto(SelectAlbumActivity.this, Uri.fromFile(
                        FileUtil.getFile(PhotoUtil.PATH_PHOTOTEMP)));
                break;
            case R.id.select_album_cancel:
                finish();
                break;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        finish();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Util.pLog("data == null ? " + (data == null));
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case PhotoUtil.TAKEPHOTO:
                Uri uri = Uri.fromFile(new File(PhotoUtil.PATH_PHOTOTEMP));
                Util.pLog("data == null ? " + (data == null));
                PhotoUtil.cropPhoto(SelectAlbumActivity.this, uri, uri);
                break;
            case PhotoUtil.PICKPHOTO:
                Uri picUri = Uri.fromFile(new File(PhotoUtil.getPickPhotoPath(this, data)));
                Uri picUri2 = Uri.fromFile(FileUtil.getFile(PhotoUtil.PATH_PHOTOTEMP));
                PhotoUtil.cropPhoto(SelectAlbumActivity.this, picUri, picUri2);
                break;
            case PhotoUtil.CROPPHOTO:
                data.putExtra(Constant.KEY_PHOTO_PATH, PhotoUtil.PATH_PHOTOTEMP);
                setResult(RESULT_OK, data);
                finish();
                break;
        }
    }

}

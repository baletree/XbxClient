package com.xbx.client.linsener;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.xbx.client.utils.Util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by EricYuan
 * CreatedDate 2015/8/24 23:08
 * 类描述：解决图片被压缩
 */
public class AnimateFirstDisplayListener extends
        SimpleImageLoadingListener {
    final List<String> displayedImages = Collections
            .synchronizedList(new LinkedList<String>());

    @Override
    public void onLoadingComplete(String imageUri, View view,
                                  Bitmap loadedImage) {
        if (loadedImage != null) {
            ImageView imageView = (ImageView) view;
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(loadedImage);
            // 是否第一次显示
            boolean firstDisplay = !displayedImages.contains(imageUri);
            if (firstDisplay) {
                // 图片淡入效果
                FadeInBitmapDisplayer.animate(imageView, 500);
                displayedImages.add(imageUri);
            }
            Util.pLog("onLoadingCompleteId:"+view.getId());
        }
    }
}

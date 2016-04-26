package com.xbx.client.linsener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.xbx.client.R;

/**
 * Created by EricYuan on 2016/4/14.
 */
public class ImageLoaderConfigFactory {
    private DisplayImageOptions options;
    private static ImageLoaderConfigFactory factory;

    private ImageLoaderConfigFactory(){

    }

    public static ImageLoaderConfigFactory getInstance() {
        if(factory == null) {
            synchronized (ImageLoaderConfigFactory.class) {
                if(factory == null) {
                    factory = new ImageLoaderConfigFactory();
                }
            }
        }
        return factory;
    }

    public DisplayImageOptions getHeadImg() {
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .showImageOnLoading(R.mipmap.user_head)
                .showImageForEmptyUri(R.mipmap.user_head)
                .showImageOnFail(R.mipmap.user_head)
                .cacheInMemory(true)
                .build();
        return options;
    }
}

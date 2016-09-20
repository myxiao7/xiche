package com.zh.xiche.utils;

import android.widget.ImageView;


import com.zh.xiche.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * 图片加载
 * Created by zhanghao on 2016/9/18.
 */
public class ImageLoaderHelper {
    private static final ImageLoaderHelper imageLoaderHelper = new ImageLoaderHelper();
    private static ImageOptions options;
    private ImageLoaderHelper(){
        options = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                //设置加载过程中的图片
                .setLoadingDrawableId(R.mipmap.icon_user_default)
                //设置加载失败后的图片
                .setFailureDrawableId(R.mipmap.load_fail)
                .build();
    }

    public static ImageLoaderHelper getInstance(){
        return imageLoaderHelper;
    }

    /**
     * 加载网络图片
     * @param img
     * @param url
     */
    public void loadPic(ImageView img, String url){
        x.image().bind(img, url.trim());
    }
}

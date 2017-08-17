package com.truyayong.richtext.richview.ig;

/**
 * Created by alley_qiu on 2017/3/24.
 */

import android.graphics.Bitmap;

/**
 * Created by zhou on 2016/12/9.
 * 图片加载器
 */
interface ImageLoader {

    /**
     * 加载中，设置placeHolder图片
     */
    void onLoading();

    /**
     * 图片大小已获取到，进行图片缩放处理
     *
     * @param width  图片真实宽度
     * @param height 图片真实高度
     * @return 缩放倍数
     */
    int onSizeReady(int width, int height);

    /**
     * 图片加载失败，设errorImage
     *
     * @param e exception
     */
    void onFailure(Exception e);

    /**
     * 图片加载完成
     *
     * @param bitmap Bitmap
     */
    void onResourceReady(Bitmap bitmap);

}


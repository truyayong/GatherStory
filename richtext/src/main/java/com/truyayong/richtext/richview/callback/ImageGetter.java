package com.truyayong.richtext.richview.callback;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.truyayong.richtext.richview.ImageHolder;
import com.truyayong.richtext.richview.RichViewConfig;
import com.truyayong.richtext.richview.parser.ImageLoadNotify;

/**
 * Created by alley_qiu on 2017/3/24.
 * 图片加载器
 */

public interface ImageGetter extends Recyclable {

    /**
     * 获取图片
     *
     * @param holder   ImageHolder
     * @param config   RichTextConfig
     * @param textView TextView
     * @return Drawable
     * @see ImageHolder
     * @see RichViewConfig
     */
    Drawable getDrawable(ImageHolder holder, RichViewConfig config, TextView textView);

    void registerImageLoadNotify(ImageLoadNotify imageLoadNotify);
}

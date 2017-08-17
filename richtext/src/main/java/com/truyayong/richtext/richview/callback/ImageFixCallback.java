package com.truyayong.richtext.richview.callback;

import com.truyayong.richtext.richview.ImageHolder;

/**
 * Created by alley_qiu on 2017/3/24.
 */

public interface ImageFixCallback {
    /**
     * 修复图片尺寸的方法
     *
     * @param holder ImageHolder对象
     */
    void onFix(ImageHolder holder);
}

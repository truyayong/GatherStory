package com.truyayong.richtext.richview.callback;

import java.util.List;

/**
 * Created by alley_qiu on 2017/3/24.
 */

public interface OnImageClickListener {
    /**
     * 图片被点击后的回调方法
     *
     * @param imageUrls 本篇富文本内容里的全部图片
     * @param position  点击处图片在imageUrls中的位置
     */
    void imageClicked(List<String> imageUrls, int position);
}

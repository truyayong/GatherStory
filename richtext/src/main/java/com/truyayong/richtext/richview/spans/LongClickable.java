package com.truyayong.richtext.richview.spans;

import android.view.View;

/**
 * Created by alley_qiu on 2017/3/24.
 */

public interface LongClickable {
    /**
     * 长按点击时间
     *
     * @param widget 　view
     * @return true:已处理，false:交由onClick处理
     */
    boolean onLongClick(View widget);
}

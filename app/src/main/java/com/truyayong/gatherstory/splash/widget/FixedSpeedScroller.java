package com.truyayong.gatherstory.splash.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by alley_qiu on 2017/3/19.
 */

public class FixedSpeedScroller extends Scroller {

    private int mDuration = 800; //默认为800ms

    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    /**
     * 设置滑动时间
     * @param mDuration
     */
    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    /**
     * 获取滑动时间
     * @return
     */
    public int getmDuration() {
        return mDuration;
    }
}

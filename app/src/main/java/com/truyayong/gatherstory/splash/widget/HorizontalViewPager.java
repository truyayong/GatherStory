package com.truyayong.gatherstory.splash.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by alley_qiu on 2017/3/19.
 */

public class HorizontalViewPager extends ViewPager {

    private Bitmap bgBitmap;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public HorizontalViewPager(Context context) {
        super(context);
    }

    public HorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBackGround(Bitmap bitmap) {
        this.bgBitmap = bitmap;
        this.paint.setFilterBitmap(true);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (this.bgBitmap != null && getAdapter().getCount() > 1) {
            int width = this.bgBitmap.getWidth();
            int height =  this.bgBitmap.getHeight();
            int count = getAdapter().getCount();
            int x = getScrollX();
            //屏幕宽度对应图像的宽度
            int bgScreenWidth = height * getWidth() / getHeight();
            //每个屏幕对应图像的宽度
            int perBgScreenWidth = (width - bgScreenWidth) / (count - 1);
            //背景图片移动的宽度，屏幕移动宽度为x
            int moveWidth = x * ((width - perBgScreenWidth) / (count - 1)) / getWidth();

            canvas.drawBitmap(this.bgBitmap, new Rect(moveWidth, 0, moveWidth + perBgScreenWidth, height),
                    new Rect(x, 0, x + getWidth(), getHeight()), this.paint);

        }
        super.dispatchDraw(canvas);
    }
}

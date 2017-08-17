package com.truyayong.gatherstory.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by alley_qiu on 2017/3/4.
 */

public class DensityUtil {

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static  int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        int height = 1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }

    public static int getViewWidth(View view) {
        return view.getWidth();
    }

    public static int getViewHeight(View view) {
        return view.getHeight();
    }

    public static int caculateInSampleSize(BitmapFactory.Options options, int requestWidth, int requestHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (requestHeight == 0 || requestWidth == 0) return 1;
        if (height > requestHeight || width > requestWidth) {
            int heightRatio = Math.round((float)height / (float)requestHeight);
            int widthRatio = Math.round((float)width / (float)requestWidth);
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}

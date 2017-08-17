package com.truyayong.gatherstory.daemon;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * Created by jianghurenchenglidazui on 2017/8/15.
 * Email : truyayong@gmail.com
 * 异步任务管理类
 */

public class Daemon {

    private static HandlerThread networkLoadDeamon;

    public static CustomHandler networkHandler = null;

    public synchronized static CustomHandler getNetworkHandler() {
        if (networkLoadDeamon == null) {
            networkLoadDeamon = new HandlerThread("network_deamon");
            networkLoadDeamon.start();
        }
        if (networkHandler == null) {
            networkHandler = new CustomHandler(networkLoadDeamon.getLooper());
        }
        return networkHandler;
    }

    //自定义Handler类
    public static final class CustomHandler extends Handler {

        public CustomHandler(Looper looper) {
            super(looper);
        }
        //确保Runnable被删除
        public void safeRemoveCallback(final Runnable runnable) {
            if (Looper.myLooper() == getLooper()) {
                removeCallbacks(runnable);
            } else {
                post(new Runnable() {
                    @Override
                    public void run() {
                        safeRemoveCallback(runnable);
                    }
                });
            }
        }
    }
}

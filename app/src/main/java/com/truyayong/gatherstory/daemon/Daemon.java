package com.truyayong.gatherstory.daemon;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by jianghurenchenglidazui on 2017/8/15.
 * Email : truyayong@gmail.com
 */

public class Daemon {

    private static HandlerThread networkLoadDeamon;

    public static Handler networkHandler = null;


    public synchronized static Handler getNetworkHandler() {
        if (networkLoadDeamon == null) {
            networkLoadDeamon = new HandlerThread("network_deamon");
            networkLoadDeamon.start();
        }
        if (networkHandler == null) {
            networkHandler = new Handler(networkLoadDeamon.getLooper());
        }
        return networkHandler;
    }
}

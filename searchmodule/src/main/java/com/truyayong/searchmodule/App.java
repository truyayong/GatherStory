package com.truyayong.searchmodule;

import android.app.Application;

import com.truyayong.searchmodule.utils.BmobUtil;

/**
 * Created by alley_qiu on 2017/3/21.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BmobUtil.initBmob(getApplicationContext());
    }
}

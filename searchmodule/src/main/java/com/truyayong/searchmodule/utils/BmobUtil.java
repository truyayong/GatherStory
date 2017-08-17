package com.truyayong.searchmodule.utils;

import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * Created by alley_qiu on 2017/3/21.
 */

public class BmobUtil {

    public static final String BMOB_APP_KEY = "93d45d205c00931ecb893d0a5b311eca";

    public static void initBmob(Context context) {
        Bmob.initialize(context, BMOB_APP_KEY);
    }
}
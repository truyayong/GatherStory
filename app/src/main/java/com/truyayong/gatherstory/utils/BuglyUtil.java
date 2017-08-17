package com.truyayong.gatherstory.utils;

import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by alley_qiu on 2017/3/22.
 */

public class BuglyUtil {

    public static final String APP_ID = "928ea74d26";

    public static void initBugly(Context context, String processName) {
// 获取当前包名
        String packageName = context.getPackageName();
// 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
// 初始化Bugly
        CrashReport.initCrashReport(context, APP_ID, true, strategy);
    }
}

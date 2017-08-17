package com.truyayong.gatherstory;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;
import com.truyayong.gatherstory.Bus.events.FgUpdateSentenceEvent;
import com.truyayong.gatherstory.Bus.events.UpdateSentenceEvent;
import com.truyayong.gatherstory.daemon.Daemon;
import com.truyayong.gatherstory.im.Handlers.IMMessageHandler;
import com.truyayong.gatherstory.im.messages.UpdateSentenceMessage;
import com.truyayong.gatherstory.utils.BmobUtil;
import com.truyayong.gatherstory.utils.BuglyUtil;
import com.truyayong.gatherstory.utils.ProcessUtil;
import com.truyayong.searchmodule.SearchFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.event.MessageEvent;

/**
 * Created by alley_qiu on 2017/3/4.
 */

public class App extends Application {

    private RefWatcher refWatcher;

    public static SearchFragment mSearchFragment = SearchFragment.newInstance();
    public static String mProcessName = ProcessUtil.getMyProcessName();
    public static List<MessageEvent> chatMessageEvents = new ArrayList<>();
    public static List<FgUpdateSentenceEvent> updateSentenceEvents = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        BuglyUtil.initBugly(getApplicationContext(), mProcessName);
        BmobUtil.initBmob(getApplicationContext());
        //Bmob IM初始化
        if (getApplicationInfo().packageName.equals(mProcessName)) {
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new IMMessageHandler());
        }
        //初始化LeakCanary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        refWatcher = LeakCanary.install(this);

        //初始化StrictMode
        if (BuildConfig.DEBUG) {
            // 针对线程的相关策略
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//            .detectDiskReads()
//            .detectDiskWrites()
            .detectNetwork()
            .penaltyLog()
            .build());
            // 针对VM的相关策略
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }

        Fresco.initialize(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        App app = (App) context.getApplicationContext();
        return app.refWatcher;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        BmobIM.getInstance().disConnect();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}

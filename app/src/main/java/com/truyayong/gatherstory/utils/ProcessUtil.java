package com.truyayong.gatherstory.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by alley_qiu on 2017/3/10.
 */

public class ProcessUtil {

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

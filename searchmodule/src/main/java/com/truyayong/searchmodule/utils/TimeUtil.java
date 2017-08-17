package com.truyayong.searchmodule.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by alley_qiu on 2017/3/21.
 */

public class TimeUtil {

    public static BmobDate string2BmobDate(String strTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(strTime);
        BmobDate bmobDate = new BmobDate(date);
        return bmobDate;
    }
}

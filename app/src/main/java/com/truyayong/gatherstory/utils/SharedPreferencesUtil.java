package com.truyayong.gatherstory.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.truyayong.gatherstory.content.bean.RecentArticle;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alley_qiu on 2017/3/18.
 */

public class SharedPreferencesUtil {

    public static <T> List<T> getShares(Context context, String shareId, String contentId, Class<T> clz) {
        SharedPreferences share = context.getSharedPreferences(shareId, Context.MODE_PRIVATE);
        String text = share.getString(contentId,"");
        List<T> shares = JSON.parseArray(text, clz);
        return shares;
    }

    public static <T> void saveShares(Context context, String shareId, String contentId, T node, List<T> shares) {

        if (shares== null) {
            shares = new LinkedList<>();
        }
        if (shares.size() > 50) {
            shares.remove(shares.size() - 1);
        }
        if (node != null) {
            shares.add(0, node);
        }
        String text = JSON.toJSONString(shares.toArray());
        SharedPreferences.Editor editor = context.getSharedPreferences(shareId, Context.MODE_PRIVATE).edit();
        editor.putString(contentId, text);
        editor.commit();
    }
}

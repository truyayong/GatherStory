package com.truyayong.gatherstory.user.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.content.bean.RecentArticle;
import com.truyayong.gatherstory.user.adapter.RecentReadRecyclerViewAdapter;
import com.truyayong.gatherstory.user.view.IRecentReadView;
import com.truyayong.gatherstory.utils.SharedPreferencesUtil;

import java.util.List;

/**
 * Created by alley_qiu on 2017/3/17.
 */

public class RecentReadPresenter extends BasePresenter<IRecentReadView> {

    private Context mContext;

    public RecentReadPresenter(Context mContext) {
        this.mContext = mContext;
    }

    public void setupView() {
//        SharedPreferences share = mContext.getSharedPreferences("recent_share", Context.MODE_PRIVATE);
//        String recentText = share.getString("recent_text","");
//        List<RecentArticle> recents = JSON.parseArray(recentText, RecentArticle.class);
        List<RecentArticle> recents = SharedPreferencesUtil.getShares(mContext, "recent_share", "recent_text", RecentArticle.class);
        RecentReadRecyclerViewAdapter adapter = new RecentReadRecyclerViewAdapter(recents, mContext);
        getView().getRVRecentRead().setAdapter(adapter);
        getView().getRVRecentRead().setLayoutManager(new LinearLayoutManager(mContext));
    }
}

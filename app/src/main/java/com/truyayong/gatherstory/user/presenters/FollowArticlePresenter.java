package com.truyayong.gatherstory.user.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.content.bean.Article;
import com.truyayong.gatherstory.user.activities.ListFollowArticleActivity;
import com.truyayong.gatherstory.user.adapter.FollowArticleRecyclerViewAdapter;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.gatherstory.user.view.IFollowArticleView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by alley_qiu on 2017/3/15.
 */

public class FollowArticlePresenter extends BasePresenter<IFollowArticleView> {

    private Context mContext;

    public FollowArticlePresenter(Context mContext) {
        this.mContext = mContext;
    }

    public void setupView() {
        User user = getUser();
        BmobQuery<Article> query = new BmobQuery<>();
        query.addWhereRelatedTo("followArticle", new BmobPointer(user));
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                getView().getRVArticles().setLayoutManager(new LinearLayoutManager(mContext));
                FollowArticleRecyclerViewAdapter adapter = new FollowArticleRecyclerViewAdapter(list, mContext);
                getView().getRVArticles().setAdapter(adapter);
            }
        });
    }

    private User getUser() {
        Intent intent = ((Activity)mContext).getIntent();
        Bundle bundle = intent.getExtras();
        return (User) bundle.get("com.truyayong.oldhouse.user.bean.user");
    }
}

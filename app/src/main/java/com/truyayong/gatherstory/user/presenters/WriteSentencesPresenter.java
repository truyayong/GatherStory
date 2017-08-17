package com.truyayong.gatherstory.user.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.content.bean.Article;
import com.truyayong.gatherstory.content.bean.Sentence;
import com.truyayong.gatherstory.user.adapter.FollowArticleRecyclerViewAdapter;
import com.truyayong.gatherstory.user.adapter.WriteSentencesRecyclerViewAdapter;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.gatherstory.user.view.IWriteSentencesView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by alley_qiu on 2017/3/16.
 */

public class WriteSentencesPresenter extends BasePresenter<IWriteSentencesView> {

    private Context mContext;

    public WriteSentencesPresenter(Context mContext) {
        this.mContext = mContext;
    }

    public void setupView() {
        User user = getUser();
        BmobQuery<Sentence> query = new BmobQuery<>();
        query.addWhereEqualTo("author", user);
        query.findObjects(new FindListener<Sentence>() {
            @Override
            public void done(List<Sentence> list, BmobException e) {
                WriteSentencesRecyclerViewAdapter adapter = new WriteSentencesRecyclerViewAdapter(mContext, list);
                getView().getRVArticles().setAdapter(adapter);
                getView().getRVArticles().setLayoutManager(new LinearLayoutManager(mContext));
            }
        });
    }

    private User getUser() {
        Intent intent = ((Activity)mContext).getIntent();
        Bundle bundle = intent.getExtras();
        return (User) bundle.get("com.truyayong.oldhouse.user.bean.user");
    }
}

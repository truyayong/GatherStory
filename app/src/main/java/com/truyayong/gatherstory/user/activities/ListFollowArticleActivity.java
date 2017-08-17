package com.truyayong.gatherstory.user.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseAppCompatActivity;
import com.truyayong.gatherstory.content.bean.Article;
import com.truyayong.gatherstory.user.adapter.FollowArticleRecyclerViewAdapter;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.gatherstory.user.presenters.FollowArticlePresenter;
import com.truyayong.gatherstory.user.view.IFollowArticleView;

import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ListFollowArticleActivity extends MVPBaseAppCompatActivity<IFollowArticleView, FollowArticlePresenter> implements IFollowArticleView {

    @Bind(R.id.rv_list_follow_article)
    RecyclerView rv_list_follow_article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.setupView();
    }

    @Override
    protected FollowArticlePresenter createPresenter() {
        return new FollowArticlePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_list_follow_article;
    }

    @Override
    public RecyclerView getRVArticles() {
        return rv_list_follow_article;
    }
}

package com.truyayong.gatherstory.user.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseAppCompatActivity;
import com.truyayong.gatherstory.user.presenters.WriteArticlesPresenter;
import com.truyayong.gatherstory.user.view.IWriteArticlesView;

import butterknife.Bind;

public class UserWriteArticlesActivity extends MVPBaseAppCompatActivity<IWriteArticlesView, WriteArticlesPresenter> implements IWriteArticlesView {

    @Bind(R.id.rv_list_write_articles)
    RecyclerView rv_list_write_articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.setupView();
    }

    @Override
    protected WriteArticlesPresenter createPresenter() {
        return new WriteArticlesPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_user_write_articles;
    }

    @Override
    public RecyclerView getRVArticles() {
        return rv_list_write_articles;
    }
}

package com.truyayong.gatherstory.user.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseAppCompatActivity;
import com.truyayong.gatherstory.user.presenters.RecentReadPresenter;
import com.truyayong.gatherstory.user.view.IRecentReadView;

import butterknife.Bind;

public class RecentReadActivity extends MVPBaseAppCompatActivity<IRecentReadView, RecentReadPresenter> implements IRecentReadView {

    @Bind(R.id.rv_list_recent_read)
    RecyclerView rv_list_recent_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.setupView();
    }

    @Override
    protected RecentReadPresenter createPresenter() {
        return new RecentReadPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_recent_read;
    }

    @Override
    public RecyclerView getRVRecentRead() {
        return rv_list_recent_read;
    }
}

package com.truyayong.gatherstory.user.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseAppCompatActivity;
import com.truyayong.gatherstory.user.presenters.FollowMePresenter;
import com.truyayong.gatherstory.user.view.IFollowMeView;

import butterknife.Bind;

public class FollowMeActivity extends MVPBaseAppCompatActivity<IFollowMeView, FollowMePresenter> implements IFollowMeView {

    @Bind(R.id.rv_list_follow_me)
    RecyclerView rv_list_follow_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.setupView();
    }

    @Override
    protected FollowMePresenter createPresenter() {
        return new FollowMePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_follow_me;
    }

    @Override
    public RecyclerView getRVUsers() {
        return rv_list_follow_me;
    }
}

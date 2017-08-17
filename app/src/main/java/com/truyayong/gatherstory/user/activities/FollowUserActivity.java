package com.truyayong.gatherstory.user.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseAppCompatActivity;
import com.truyayong.gatherstory.user.presenters.FollowUserPresenter;
import com.truyayong.gatherstory.user.view.IFollowUserView;

import butterknife.Bind;

public class FollowUserActivity extends MVPBaseAppCompatActivity<IFollowUserView, FollowUserPresenter> implements IFollowUserView {

    @Bind(R.id.rv_list_follow_user)
    RecyclerView rv_list_follow_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.setupView();
    }

    @Override
    protected FollowUserPresenter createPresenter() {
        return new FollowUserPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_follow_user;
    }

    @Override
    public RecyclerView getRVUsers() {
        return rv_list_follow_user;
    }
}

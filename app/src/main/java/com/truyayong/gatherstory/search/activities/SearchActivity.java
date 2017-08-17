package com.truyayong.gatherstory.search.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseAppCompatActivity;
import com.truyayong.gatherstory.base.MVPBaseFragmentActivity;
import com.truyayong.gatherstory.search.presenters.SearchPresenter;
import com.truyayong.gatherstory.search.services.SearchService;
import com.truyayong.gatherstory.search.view.ISearchView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;

public class SearchActivity extends MVPBaseFragmentActivity<ISearchView, SearchPresenter> implements ISearchView {

    @Bind(R.id.fl_content_search_content)
    FrameLayout fl_content_search_content;
    @Bind(R.id.rl_search_content_search)
    RelativeLayout rl_search_content_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.displayView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_search;
    }


    @Override
    public FrameLayout getContent() {
        return fl_content_search_content;
    }

    @Override
    public RelativeLayout getRLSearch() {
        return rl_search_content_search;
    }
}

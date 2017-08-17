package com.truyayong.gatherstory.content.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.leakcanary.RefWatcher;
import com.truyayong.gatherstory.App;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseFragment;
import com.truyayong.gatherstory.content.presenters.HomeFgPresenter;
import com.truyayong.gatherstory.content.view.IHomeFgView;

import butterknife.Bind;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public class HomeFragment extends MVPBaseFragment<IHomeFgView, HomeFgPresenter> implements IHomeFgView {


    @Bind(R.id.swipe_refresh_index_home)
    SwipeRefreshLayout swipe_refresh_index_home;
    @Bind(R.id.recyclerview_index_home)
    RecyclerView recyclerview_index_home;
    @Bind(R.id.pb_process_index_home)
    ProgressBar pb_process_index_home;
    @Bind(R.id.fam_menu_index_home)
    FloatingActionMenu fam_menu_index_home;
    @Bind(R.id.fab_write_article_index_home)
    FloatingActionButton fab_write_article_index_home;
    @Bind(R.id.fab_write_sentence_index_home)
    FloatingActionButton fab_write_sentence_index_home;


    @Override
    protected HomeFgPresenter createPresenter() {
        return new HomeFgPresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerview_index_home;
    }


    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipe_refresh_index_home;
    }

    @Override
    public ProgressBar getPBProcess() {
        return pb_process_index_home;
    }

    @Override
    public FloatingActionMenu getFAM() {
        return fam_menu_index_home;
    }

    @Override
    public FloatingActionButton getFABWriteArticle() {
        return fab_write_article_index_home;
    }

    @Override
    public FloatingActionButton getFABWriteSentence() {
        return fab_write_sentence_index_home;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {
        mPresenter.displayView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}

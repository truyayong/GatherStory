package com.truyayong.gatherstory.content.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public interface IHomeFgView {

    RecyclerView getRecyclerView();
    SwipeRefreshLayout getSwipeRefreshLayout();
    ProgressBar getPBProcess();
    FloatingActionMenu getFAM();
    FloatingActionButton getFABWriteArticle();
    FloatingActionButton getFABWriteSentence();
}

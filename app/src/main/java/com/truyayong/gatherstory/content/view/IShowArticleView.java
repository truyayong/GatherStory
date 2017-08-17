package com.truyayong.gatherstory.content.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public interface IShowArticleView {

    ProgressBar getPBProcess();
    SwipeRefreshLayout getSwipeRefreshLayout();
    RecyclerView getRecyclerView();
    ImageButton getIBAddSentence();
    ImageButton getIBFollowArticle();
    ImageButton getIBFavoriteArticle();

}

package com.truyayong.gatherstory.search.view;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by alley_qiu on 2017/3/20.
 */

public interface ISearchView {

    FrameLayout getContent();
    RelativeLayout getRLSearch();

}

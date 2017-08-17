package com.truyayong.gatherstory.content.view;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public interface INotificationFgView {

    TextView getTVEmpty();
    RecyclerView getRVMessage();
}

package com.truyayong.gatherstory.search.view;

import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by alley_qiu on 2017/3/20.
 */

public interface ISearchContentFgView {

    TextView getTVEmpty();
    RecyclerView getRVSentences();
    ProgressBar getProcess();
    FrameLayout getForm();

}

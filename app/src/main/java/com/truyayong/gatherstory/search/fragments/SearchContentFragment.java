package com.truyayong.gatherstory.search.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseFragment;
import com.truyayong.gatherstory.search.presenters.SearchContentFgPresenter;
import com.truyayong.gatherstory.search.presenters.SearchPresenter;
import com.truyayong.gatherstory.search.view.ISearchContentFgView;

import butterknife.Bind;

/**
 * Created by alley_qiu on 2017/3/20.
 */

public class SearchContentFragment extends MVPBaseFragment<ISearchContentFgView, SearchContentFgPresenter> implements ISearchContentFgView {

    @Bind(R.id.pb_process_search_content)
    ProgressBar pb_process_search_content;
    @Bind(R.id.tv_empty_search_content)
    TextView tv_empty_search_content;
    @Bind(R.id.rv_message_search_content)
    RecyclerView rv_message_search_content;
    @Bind(R.id.fl_form_search_content)
    FrameLayout fl_form_search_content;

    @Override
    protected SearchContentFgPresenter createPresenter() {
        return new SearchContentFgPresenter(getActivity());
    }


    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mPresenter.setupView();
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_search_content;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public TextView getTVEmpty() {
        return tv_empty_search_content;
    }

    @Override
    public RecyclerView getRVSentences() {
        return rv_message_search_content;
    }

    @Override
    public ProgressBar getProcess() {
        return pb_process_search_content;
    }

    @Override
    public FrameLayout getForm() {
        return fl_form_search_content;
    }
}

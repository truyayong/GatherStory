package com.truyayong.gatherstory.content.fragments;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.squareup.leakcanary.RefWatcher;
import com.truyayong.gatherstory.App;
import com.truyayong.gatherstory.Bus.events.FgUserMessageEvent;
import com.truyayong.gatherstory.Bus.events.IndexUserMessageEvent;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseFragment;
import com.truyayong.gatherstory.content.presenters.MessageFgPresenter;
import com.truyayong.gatherstory.content.view.IMessageFgView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

/**
 * Created by alley_qiu on 2017/3/18.
 */

public class MessageFragment extends MVPBaseFragment<IMessageFgView, MessageFgPresenter> implements IMessageFgView {

    @Bind(R.id.tv_empty_index_message)
    TextView tv_empty_index_message;
    @Bind(R.id.rv_message_index_message)
    RecyclerView rv_message_index_message;


    public MessageFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mPresenter.displayView();
    }

    @Override
    protected MessageFgPresenter createPresenter() {
        return new MessageFgPresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public TextView getTVEmpty() {
        return tv_empty_index_message;
    }

    @Override
    public RecyclerView getRVMessage() {
        return rv_message_index_message;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}

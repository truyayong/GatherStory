package com.truyayong.gatherstory.content.fragments;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.leakcanary.RefWatcher;
import com.truyayong.gatherstory.App;
import com.truyayong.gatherstory.Bus.events.FgUpdateSentenceEvent;
import com.truyayong.gatherstory.Bus.events.UpdateSentenceEvent;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseFragment;
import com.truyayong.gatherstory.content.presenters.NotificationFgPresenter;
import com.truyayong.gatherstory.content.view.INotificationFgView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public class NotificationFragment extends MVPBaseFragment<INotificationFgView, NotificationFgPresenter> implements INotificationFgView {

    @Bind(R.id.tv_empty_index_notification)
    TextView tv_empty_index_notification;
    @Bind(R.id.rv_message_index_notification)
    RecyclerView rv_message_index_notification;

    private UpdateSentenceEvent sentenceEvent;

    public NotificationFragment() {
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateSentenceEvent(UpdateSentenceEvent event) {
        sentenceEvent = event;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (sentenceEvent != null) {
            FgUpdateSentenceEvent event = new FgUpdateSentenceEvent();
            event.setTitle(sentenceEvent.getTitle());
            event.setContent(sentenceEvent.getContent());
            EventBus.getDefault().post(event);
        }
    }

    @Override
    protected NotificationFgPresenter createPresenter() {
        return new NotificationFgPresenter(getContext());
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mPresenter.displayView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        EventBus.getDefault().unregister(this);
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_notification;
    }

    @Override
    public TextView getTVEmpty() {
        return tv_empty_index_notification;
    }

    @Override
    public RecyclerView getRVMessage() {
        return rv_message_index_notification;
    }
}

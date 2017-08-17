package com.truyayong.gatherstory.content.activities;

import android.support.design.widget.AppBarLayout;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.truyayong.gatherstory.App;
import com.truyayong.gatherstory.Bus.events.UpdateSentenceEvent;
import com.truyayong.gatherstory.Bus.events.IndexUserMessageEvent;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseFragmentActivity;
import com.truyayong.gatherstory.content.presenters.IndexPresenter;
import com.truyayong.gatherstory.content.view.IIndexView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

public class IndexActivity extends MVPBaseFragmentActivity<IIndexView, IndexPresenter> implements IIndexView {

    @Bind(R.id.app_bar_content_index)
    AppBarLayout app_bar_content_index;
    @Bind(R.id.rl_search_content_index)
    RelativeLayout rl_search_content_index;
    @Bind(R.id.fl_content_content_index)
    FrameLayout fl_content_content_index;
    @Bind(R.id.ll_bottom_content_index)
    LinearLayout ll_bottom_content_index;
    @Bind(R.id.rg_tabs_content_index)
    RadioGroup rg_tabs_content_index;
    @Bind(R.id.rb_home_tab_content_index)
    RadioButton rb_home_tab_content_index;
    @Bind(R.id.rb_notify_tab_content_index)
    RadioButton rb_notify_tab_content_index;
    @Bind(R.id.rb_user_tab_content_index)
    RadioButton rb_user_tab_content_index;
    @Bind(R.id.rb_message_tab_content_index)
    RadioButton rb_message_tab_content_index;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mPresenter.displayView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!App.chatMessageEvents.isEmpty()) {
            getRBTabMessage().setActivated(true);
        }
        if (!App.updateSentenceEvents.isEmpty()) {
            getRBTabNotify().setActivated(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateSentenceEvent(UpdateSentenceEvent event) {
        getRBTabNotify().setActivated(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserMessageEvent(IndexUserMessageEvent event) {
        Log.e("truyayong", "index activity onUserMessageEvent");
        getRBTabMessage().setActivated(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected IndexPresenter createPresenter() {
        return new IndexPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_index;
    }

    @Override
    public AppBarLayout getAppBar() {
        return app_bar_content_index;
    }

    @Override
    public RelativeLayout getRLSearch() {
        return rl_search_content_index;
    }

    @Override
    public FrameLayout getFLContent() {
        return fl_content_content_index;
    }

    @Override
    public LinearLayout getLLBottom() {
        return ll_bottom_content_index;
    }

    @Override
    public RadioGroup getRGTabsContent() {
        return rg_tabs_content_index;
    }

    @Override
    public RadioButton getRBTabHome() {
        return rb_home_tab_content_index;
    }

    @Override
    public RadioButton getRBTabNotify() {
        return rb_notify_tab_content_index;
    }

    @Override
    public RadioButton getRBTabMessage() {
        return rb_message_tab_content_index;
    }

    @Override
    public RadioButton getRBTabUser() {
        return rb_user_tab_content_index;
    }
}

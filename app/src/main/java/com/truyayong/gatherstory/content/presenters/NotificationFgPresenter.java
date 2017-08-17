package com.truyayong.gatherstory.content.presenters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.truyayong.gatherstory.App;
import com.truyayong.gatherstory.Bus.RxBus;
import com.truyayong.gatherstory.Bus.events.FgUpdateSentenceEvent;
import com.truyayong.gatherstory.Bus.events.UpdateSentenceEvent;
import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.content.adapter.NotificationRecyclerViewAdapter;
import com.truyayong.gatherstory.content.bean.Article;
import com.truyayong.gatherstory.content.view.INotificationFgView;
import com.truyayong.gatherstory.im.activities.ChatActivity;
import com.truyayong.gatherstory.im.messages.UpdateSentenceMessage;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.gatherstory.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public class NotificationFgPresenter extends BasePresenter<INotificationFgView> {

    private Context mContext;
    private List<FgUpdateSentenceEvent> events = new ArrayList<>();
    public NotificationRecyclerViewAdapter adapter;
    private static final String NOTIFICATION_SHARE_ID = "notification_share";
    private static final String NOTIFICATION_CONTENT_ID = "notification_text";

    public NotificationFgPresenter(Context context) {
        this.mContext = context;
        List<FgUpdateSentenceEvent> datas = SharedPreferencesUtil.getShares(mContext,
                NOTIFICATION_SHARE_ID, NOTIFICATION_CONTENT_ID, FgUpdateSentenceEvent.class);
        if (datas == null) {
            datas = new ArrayList<>();
            events = datas;
        } else {
            events = datas;
        }
        if (!App.updateSentenceEvents.isEmpty()) {
            for (FgUpdateSentenceEvent event : App.updateSentenceEvents) {
                Iterator<FgUpdateSentenceEvent> iterator = datas.iterator();
                while (iterator.hasNext()) {
                    FgUpdateSentenceEvent data = iterator.next();
                    if (data.getTitle().equals(event.getTitle())) {
                        iterator.remove();
                        break;
                    }
                }
                datas.add(0, event);
            }
        }
        SharedPreferencesUtil.saveShares(mContext, NOTIFICATION_SHARE_ID, NOTIFICATION_CONTENT_ID, null, datas);
        EventBus.getDefault().register(this);
        adapter = new NotificationRecyclerViewAdapter(mContext, events);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFgUpdateSentenceEvent(FgUpdateSentenceEvent event) {
        List<FgUpdateSentenceEvent> datas = SharedPreferencesUtil.getShares(mContext,
                NOTIFICATION_SHARE_ID, NOTIFICATION_CONTENT_ID, FgUpdateSentenceEvent.class);
        if (datas == null) {
            datas = new ArrayList<>();
        }
        Iterator<FgUpdateSentenceEvent> iterator = datas.iterator();
        while (iterator.hasNext()) {
            FgUpdateSentenceEvent data = iterator.next();
            if (data.getTitle().equals(event.getTitle())) {
                iterator.remove();
                break;
            }
        }
        datas.add(0, event);
        SharedPreferencesUtil.saveShares(mContext, NOTIFICATION_SHARE_ID, NOTIFICATION_CONTENT_ID, null, datas);
        adapter.changeEvents(datas);
        getView().getTVEmpty().setVisibility(View.GONE);
        getView().getRVMessage().setVisibility(View.VISIBLE);
    }
    public void displayView() {
        getView().getRVMessage().setAdapter(adapter);
        getView().getRVMessage().setLayoutManager(new LinearLayoutManager(mContext));
        if (events.isEmpty()) {
            getView().getTVEmpty().setVisibility(View.VISIBLE);
            getView().getRVMessage().setVisibility(View.GONE);
        } else {
            getView().getTVEmpty().setVisibility(View.GONE);
            getView().getRVMessage().setVisibility(View.VISIBLE);
        }

    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }
}

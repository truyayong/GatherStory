package com.truyayong.gatherstory.content.presenters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.truyayong.gatherstory.App;
import com.truyayong.gatherstory.Bus.events.FgUserMessageEvent;
import com.truyayong.gatherstory.Bus.events.IndexUserMessageEvent;
import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.content.adapter.MessageRecyclerViewAdapter;
import com.truyayong.gatherstory.content.view.IMessageFgView;
import com.truyayong.gatherstory.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;

/**
 * Created by alley_qiu on 2017/3/18.
 */

public class MessageFgPresenter extends BasePresenter<IMessageFgView> {

    private Context mContext;
    private List<FgUserMessageEvent> events = new ArrayList<>();
    public MessageRecyclerViewAdapter adapter;
    private static final String MESSAGE_SHARE_ID = "message_share";
    private static final String MESSAGE_CONTENT_ID = "message_text";

    public MessageFgPresenter(Context context) {
        this.mContext = context;
        List<FgUserMessageEvent> datas = SharedPreferencesUtil.getShares(mContext,
                MESSAGE_SHARE_ID, MESSAGE_CONTENT_ID, FgUserMessageEvent.class);
        if (datas == null) {
            datas = new ArrayList<>();
            events = datas;
        } else {
            events = datas;
        }
        if (!App.chatMessageEvents.isEmpty()) {
            for (MessageEvent event : App.chatMessageEvents) {
                BmobIMMessage message = event.getMessage();
                if (message != null) {
                    BmobIMUserInfo userInfo = event.getFromUserInfo();
                    Iterator<FgUserMessageEvent> iterator = datas.iterator();
                    while (iterator.hasNext()) {
                        FgUserMessageEvent messageEvent = iterator.next();
                        if (userInfo.getUserId().equals(messageEvent.getUserId())) {
                            iterator.remove();
                            break;
                        }
                    }
                    FgUserMessageEvent userEvent = new FgUserMessageEvent();
                    userEvent.setUserId(userInfo.getUserId());
                    userEvent.setUserName(userInfo.getName());
                    userEvent.setUserHead(userInfo.getAvatar());
                    userEvent.setMessage(event.getMessage().getContent());
                    datas.add(0,userEvent);
                    SharedPreferencesUtil.saveShares(mContext, MESSAGE_SHARE_ID, MESSAGE_CONTENT_ID, null, datas);
                }
            }
            App.chatMessageEvents.clear();
        }

        EventBus.getDefault().register(this);
        adapter = new MessageRecyclerViewAdapter(mContext, events);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserMessageEventFg(FgUserMessageEvent event) {
        List<FgUserMessageEvent> datas = SharedPreferencesUtil.getShares(mContext,
                MESSAGE_SHARE_ID, MESSAGE_CONTENT_ID, FgUserMessageEvent.class);
        if (datas == null) {
            datas = new ArrayList<>();
        }
        Iterator<FgUserMessageEvent> iterator = datas.iterator();
        while (iterator.hasNext()) {
            FgUserMessageEvent messageEvent = iterator.next();
            if (messageEvent.getUserId().equals(event.getUserId())) {
                iterator.remove();
                break;
            }
        }
        datas.add(0, event);
        SharedPreferencesUtil.saveShares(mContext, MESSAGE_SHARE_ID, MESSAGE_CONTENT_ID, null, datas);
        adapter.changeEvents(datas);
        //adapter.addEvent(event);
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

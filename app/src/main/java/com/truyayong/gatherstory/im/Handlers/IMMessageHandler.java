package com.truyayong.gatherstory.im.Handlers;

import android.os.Environment;
import android.util.Log;

import com.truyayong.gatherstory.App;
import com.truyayong.gatherstory.Bus.events.FgUpdateSentenceEvent;
import com.truyayong.gatherstory.Bus.events.UpdateSentenceEvent;
import com.truyayong.gatherstory.Bus.events.IndexUserMessageEvent;
import com.truyayong.gatherstory.im.messages.UpdateSentenceMessage;
import com.truyayong.gatherstory.user.bean.User;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.v3.BmobUser;

/**
 * Created by alley_qiu on 2017/3/10.
 */

public class IMMessageHandler extends BmobIMMessageHandler{
    @Override
    public void onMessageReceive(MessageEvent messageEvent) {
        //当接收到服务器发来的消息时，此方法被调用
        //文章更新消息
        handleMessageEvent(messageEvent);
        super.onMessageReceive(messageEvent);
    }

    @Override
    public void onOfflineReceive(OfflineMessageEvent offlineMessageEvent) {
        //每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
        super.onOfflineReceive(offlineMessageEvent);
        Map<String, List<MessageEvent>> map = offlineMessageEvent.getEventMap();
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> events = entry.getValue();
            for (MessageEvent event : events) {
                if (UpdateSentenceMessage.TYPE_UPDATE_SENTENCE_MSG.equals(event.getMessage().getMsgType())) {
                    FgUpdateSentenceEvent sentenceEvent = new FgUpdateSentenceEvent();
                    BmobIMMessage message = event.getMessage();
                    UpdateSentenceMessage msg = UpdateSentenceMessage.convert(message);
                    sentenceEvent.setTitle(msg.getTitle());
                    sentenceEvent.setContent(msg.getContent());
                    App.updateSentenceEvents.add(sentenceEvent);
                } else {
                    App.chatMessageEvents.add(event);
                }
            }
        }
    }

    public void handleMessageEvent(MessageEvent messageEvent) {
        if (UpdateSentenceMessage.TYPE_UPDATE_SENTENCE_MSG.equals(messageEvent.getMessage().getMsgType())) {
            UpdateSentenceEvent event = new UpdateSentenceEvent();
            BmobIMMessage message = messageEvent.getMessage();
            UpdateSentenceMessage msg = UpdateSentenceMessage.convert(message);
            event.setTitle(msg.getTitle());
            event.setContent(msg.getContent());
            EventBus.getDefault().post(event);
        } else {
            BmobIMMessage message = messageEvent.getMessage();
            if (message != null) {
                BmobIMUserInfo userInfo = message.getBmobIMUserInfo();
                IndexUserMessageEvent event = new IndexUserMessageEvent();
                event.setUserId(userInfo.getUserId());
                event.setUserName(userInfo.getName());
                event.setUserHead(userInfo.getAvatar());
                event.setMessage(messageEvent.getMessage().getContent());
                Log.e("truyayong", "IMMessageHandler handleMessageEvent name : " + event.getUserName() +
                        " head : " + event.getUserHead() + " msg : " + event.getMessage());
                EventBus.getDefault().post(event);

            }

        }
    }
}
